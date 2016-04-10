package co.uniandes.sisinfo.historico.serviciosnegocio.tesisM;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.comun.constantes.Notificaciones;
import co.uniandes.sisinfo.entities.Constante;
import co.uniandes.sisinfo.historico.entities.tesisM.h_estudiante_maestria;
import co.uniandes.sisinfo.historico.entities.tesisM.h_tesis_2;
import co.uniandes.sisinfo.historico.serviciosfuncionales.tesisM.h_EstudianteMaestria_Facade;
import co.uniandes.sisinfo.historico.serviciosfuncionales.tesisM.h_EstudianteMaestria_FacadeLocal;
import co.uniandes.sisinfo.historico.serviciosfuncionales.tesisM.h_tesis_2FacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import co.uniandes.sisinfo.serviciosnegocio.ConstanteRemote;
import co.uniandes.sisinfo.serviciosnegocio.HistoricoTesis2Remote;
import co.uniandes.sisinfo.serviciosfuncionales.CorreoRemote;
import co.uniandes.sisinfo.serviciosnegocio.Tesis2BeanRemote;
import java.io.File;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

/**
 * Servicios de negocio para Histórico de Tesis 2
 * @author Marcela Morales, Paola Gomez
 */
@Stateless
public class HistoricoTesis2 implements HistoricoTesis2Remote, HistoricoTesis2Local {

    @EJB
    private h_EstudianteMaestria_FacadeLocal estudianteMaestriaFacade;
    @EJB
    private h_tesis_2FacadeLocal hTesis2Facade;
    @EJB
    private ConstanteRemote constanteBean;
    private ServiceLocator serviceLocator;
    private ConversorTesisMaestriaH conversor;
    private ParserT parser;
    @EJB
    private CorreoRemote correoBean;

    public HistoricoTesis2() {
        try {
            parser = new ParserT();
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            correoBean = (CorreoRemote) serviceLocator.getRemoteEJB(CorreoRemote.class);
            conversor = new ConversorTesisMaestriaH();
        } catch (NamingException ex) {
            Logger.getLogger(HistoricoTesis2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String pasarTesis2RechazadaAHistorico(String xml) {
        try {
            getParser().leerXML(xml);
            Secuencia secTesis = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS2));
            h_tesis_2 tesis = conversor.pasarSecuenciaATesis2Historica(secTesis);

            String correo = conversor.pasarSecuenciaAEstudianteCorreo(secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE)));
            h_estudiante_maestria estudianteM = estudianteMaestriaFacade.findByCorreoEstudiante(correo);

            if (estudianteM == null) {
                Secuencia secEstudiante = secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE));
                h_estudiante_maestria estudianteMaestria = conversor.pasarSecuenciaAEstudianteSimple(secEstudiante);
                estudianteMaestria.setId(null);
                tesis.setId(null);
                Collection<h_tesis_2> tesises = new ArrayList();
                tesises.add(tesis);
                estudianteMaestria.setTesis2(tesises);
                estudianteMaestria.setInfoT2Rechazada(true);
                getEstudianteMaestriaFacade().create(estudianteMaestria);
            } else {
                Collection<h_tesis_2> tesises = estudianteM.getTesis2();

                boolean estaMigrada = false;

                if (tesises == null) {
                    estaMigrada = false;
                } else {
                    if (tesises.isEmpty()) {
                        estaMigrada = false;
                    } else {
                        for (h_tesis_2 tesis2 : tesises) {
                            if (tesis2.getFechaCreacion().equals(tesis.getFechaCreacion())
                                    && tesis2.getEstado().equals(tesis.getEstado())) {
                                estaMigrada = true;
                                break;
                            }
                        }
                    }
                }

                if (estaMigrada == false) {
                    tesis.setId(null);
                    if (tesises == null) {
                        tesises = new ArrayList();
                        tesises.add(tesis);
                    } else {
                        tesises.add(tesis);
                    }
                    estudianteM.setTesis2(tesises);
                    estudianteM.setInfoT2Rechazada(true);
                    getEstudianteMaestriaFacade().edit(estudianteM);
                }
            }

            ArrayList<Secuencia> secs = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secs, getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS2_RECHAZADAS_A_HISTORICOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

        } catch (Exception ex) {
            Logger.getLogger(HistoricoTesis2.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String pasarTesis2RetiradasAHistorico(String xml) {
        try {
            getParser().leerXML(xml);
            Secuencia secTesises2 = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_TESISES));
            ArrayList<Secuencia> secTesises = secTesises2.getSecuencias();
            for (Iterator<Secuencia> it = secTesises.iterator(); it.hasNext();) {
                Secuencia secTesis = it.next();
                h_tesis_2 tesis = conversor.pasarSecuenciaATesis2Historica(secTesis);
                String correo = conversor.pasarSecuenciaAEstudianteCorreo(secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE)));
                h_estudiante_maestria estudianteM = estudianteMaestriaFacade.findByCorreoEstudiante(correo);
                if (estudianteM == null) {
                    Secuencia secEstudiante = secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE));
                    h_estudiante_maestria estudianteMaestria = conversor.pasarSecuenciaAEstudianteSimple(secEstudiante);
                    estudianteMaestria.setId(null);
                    tesis.setId(null);
                    Collection<h_tesis_2> tesises = new ArrayList();
                    tesises.add(tesis);
                    estudianteMaestria.setTesis2(tesises);
                    estudianteMaestria.setInfoT2Retirada(true);
                    getEstudianteMaestriaFacade().create(estudianteMaestria);
                } else {
                    Collection<h_tesis_2> tesises = estudianteM.getTesis2();
                    boolean estaMigrada = false;
                    if (tesises == null) {
                        estaMigrada = false;
                    } else {
                        if (tesises.isEmpty()) {
                            estaMigrada = false;
                        } else {
                            for (h_tesis_2 tesis2 : tesises) {
                                if (tesis2.getFechaCreacion().equals(tesis.getFechaCreacion())
                                        && tesis2.getEstado().equals(tesis.getEstado())) {
                                    estaMigrada = true;
                                    break;
                                }
                            }
                        }
                    }

                    if (estaMigrada == false) {
                        tesis.setId(null);
                        if (tesises == null) {
                            tesises = new ArrayList();
                            tesises.add(tesis);
                        } else {
                            tesises.add(tesis);
                        }
                        estudianteM.setTesis2(tesises);
                        estudianteM.setInfoT2Retirada(true);
                        getEstudianteMaestriaFacade().edit(estudianteM);
                    }
                }

            }
            ArrayList<Secuencia> secs = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secs, getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS2_RETIRADAS_A_HISTORICOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(HistoricoTesis2.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String pasarTesis2PerdidasAHistorico(String xml) {
        try {
            getParser().leerXML(xml);
            Secuencia secTesises2 = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_TESISES));
            ArrayList<Secuencia> secTesises = secTesises2.getSecuencias();
            for (Iterator<Secuencia> it = secTesises.iterator(); it.hasNext();) {
                Secuencia secTesis = it.next();
                h_tesis_2 tesis = conversor.pasarSecuenciaATesis2Historica(secTesis);

                String correo = conversor.pasarSecuenciaAEstudianteCorreo(secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE)));
                h_estudiante_maestria estudianteM = estudianteMaestriaFacade.findByCorreoEstudiante(correo);

                if (estudianteM == null) {
                    Secuencia secEstudiante = secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE));
                    h_estudiante_maestria estudianteMaestria = conversor.pasarSecuenciaAEstudianteSimple(secEstudiante);
                    estudianteMaestria.setId(null);
                    tesis.setId(null);
                    Collection<h_tesis_2> tesises = new ArrayList();
                    tesises.add(tesis);
                    estudianteMaestria.setTesis2(tesises);
                    estudianteMaestria.setInfoT2Perdida(true);
                    getEstudianteMaestriaFacade().create(estudianteMaestria);
                } else {
                    Collection<h_tesis_2> tesises = estudianteM.getTesis2();

                    boolean estaMigrada = false;

                    if (tesises == null) {
                        estaMigrada = false;
                    } else {
                        if (tesises.isEmpty()) {
                            estaMigrada = false;
                        } else {
                            for (h_tesis_2 tesis2 : tesises) {
                                if (tesis2.getFechaCreacion().equals(tesis.getFechaCreacion())
                                        && tesis2.getEstado().equals(tesis.getEstado())) {
                                    estaMigrada = true;
                                    break;
                                }
                            }
                        }
                    }

                    if (estaMigrada == false) {
                        tesis.setId(null);
                        if (tesises == null) {
                            tesises = new ArrayList();
                            tesises.add(tesis);
                        } else {
                            tesises.add(tesis);
                        }
                        estudianteM.setTesis2(tesises);
                        estudianteM.setInfoT2Perdida(true);
                        getEstudianteMaestriaFacade().edit(estudianteM);
                    }
                }

            }
            ArrayList<Secuencia> secs = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secs, getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS2_PERDIDAS_A_HISTORICOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(HistoricoTesis2.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String pasarTesis2TerminadasAHistorico(String xml) {
        try {
            getParser().leerXML(xml);
            Secuencia secTesises2 = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_TESISES));
            ArrayList<Secuencia> secTesises = secTesises2.getSecuencias();

            for (Iterator<Secuencia> it = secTesises.iterator(); it.hasNext();) {
                Secuencia secHTesis = it.next();
                h_tesis_2 hTesis2 = conversor.pasarSecuenciaATesis2Historica(secHTesis);
                String correo = conversor.pasarSecuenciaAEstudianteCorreo(secHTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE)));
                h_estudiante_maestria estudianteM = estudianteMaestriaFacade.findByCorreoEstudiante(correo);

                if (estudianteM == null) {
                    Secuencia secEstudiante = secHTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE));
                    h_estudiante_maestria estudianteMaestria = conversor.pasarSecuenciaAEstudianteSimple(secEstudiante);
                    estudianteMaestria.setId(null);
                    hTesis2.setId(null);
                    Collection<h_tesis_2> hTesises = new ArrayList();
                    hTesises.add(hTesis2);

                    estudianteMaestria.setTesis2(hTesises);
                    estudianteMaestria.setInfoT2Terminada(true);
                    asignarAsesorAEstudiante(estudianteMaestria, hTesis2);
                    getEstudianteMaestriaFacade().create(estudianteMaestria);

                } else {
                    Collection<h_tesis_2> hTesises = estudianteM.getTesis2();
                    boolean estaMigrada = false;
                    if (hTesises == null) {
                        estaMigrada = false;
                    } else {
                        if (hTesises.isEmpty()) {
                            estaMigrada = false;
                        } else {
                            for (h_tesis_2 hT2 : hTesises) {
                                if (hT2.getCorreoAsesor().equals(hT2.getCorreoAsesor())
                                        && hT2.getSemestre().equals(hT2.getSemestre())
                                        && hT2.getEstado().equals(hT2.getEstado())) {
                                    estaMigrada = true;
                                    break;
                                }
                            }
                        }
                    }

                    if (estaMigrada == false) {
                        hTesis2.setId(null);
                        if (hTesises == null) {
                            hTesises = new ArrayList();
                            hTesises.add(hTesis2);
                        } else {
                            hTesises.add(hTesis2);
                        }
                        estudianteM.setTesis2(hTesises);
                        estudianteM.setInfoT2Terminada(true);
                        asignarAsesorAEstudiante(estudianteM, hTesis2);
                        getEstudianteMaestriaFacade().edit(estudianteM);
                    }
                }
            }
            ArrayList<Secuencia> secs = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secs, getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS2_TERMINADAS_A_HISTORICOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(HistoricoTesis2.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String consultarTesis2PorEstadoYSemestreYCorreoAsesor(String xml) {
        try {
            getParser().leerXML(xml);
            String semestre = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE)).getValor();
            String estado = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO)).getValor();
            String correo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();

            List<h_tesis_2> tesises;
            //Si el semestre y el estado son TODOS, se consulta por el correo del asesor
            if (semestre.equals(getConstanteBean().getConstante(Constantes.VAL_TAG_TODOS)) && estado.equals(getConstanteBean().getConstante(Constantes.VAL_TAG_TODOS))) {
                if (correo.equals(getConstanteBean().getConstante(Constantes.VAL_TAG_TODOS))) {
                    tesises = hTesis2Facade.findAll();
                } else {
                    tesises = hTesis2Facade.findByCorreoAsesor(correo);
                }
            } //Si el semestre es TODOS, se consulta por estado y correo del asesor
            else if (semestre.equals(getConstanteBean().getConstante(Constantes.VAL_TAG_TODOS))) {
                if (correo.equals(getConstanteBean().getConstante(Constantes.VAL_TAG_TODOS))) {
                    tesises = hTesis2Facade.findByEstado(estado);
                } else {
                    tesises = hTesis2Facade.findByEstadoYCorreoAsesor(estado, correo);
                }
            } //Si el estado es TODOS, se consulta por semestre y correo del asesor
            else if (estado.equals(getConstanteBean().getConstante(Constantes.VAL_TAG_TODOS))) {
                if (correo.equals(getConstanteBean().getConstante(Constantes.VAL_TAG_TODOS))) {
                    tesises = hTesis2Facade.findBySemestre(semestre);
                } else {
                    tesises = hTesis2Facade.findBySemestreYCorreoAsesor(semestre, correo);
                }
            } //Si no hay TODOS, se busca por correo, estado y semestre
            else {
                if (correo.equals(getConstanteBean().getConstante(Constantes.VAL_TAG_TODOS))) {
                    tesises = hTesis2Facade.findByEstadoYSemestre(semestre, estado);
                } else {
                    tesises = hTesis2Facade.findByEstadoYSemestreYCorreoAsesor(semestre, estado, correo);
                }
            }

            Secuencia secTesises2 = conversor.pasarHistoricoTesises2ASecuencia(tesises);
            ArrayList<Secuencia> secs = new ArrayList<Secuencia>();
            secs.add(secTesises2);
            return getParser().generarRespuesta(secs, getConstanteBean().getConstante(Constantes.CMD_DAR_HISTORICO_TESIS2_POR_ESTADO_SEMESTRE_Y_CORREO_ASESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(HistoricoTesis2.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String consultarDetalleHistoricoTesis2(String xml) {
        try {
            getParser().leerXML(xml);
            String id = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)).getValor();
            if (id == null) {
                return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_DAR_DETALLE_HISTORICO_TESIS2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), null, new ArrayList());
            } else {
                Long idLong = Long.parseLong(id);
                h_tesis_2 tesis = hTesis2Facade.find(idLong);
                Secuencia secTesis = conversor.pasarHistoricoTesis2ASecuencia(tesis);
                ArrayList<Secuencia> secs = new ArrayList<Secuencia>();
                secs.add(secTesis);
                return getParser().generarRespuesta(secs, getConstanteBean().getConstante(Constantes.CMD_DAR_DETALLE_HISTORICO_TESIS2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
            }
        } catch (Exception ex) {
            Logger.getLogger(HistoricoTesis2.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    //********** METODOS AUXILIARES *************
    private void asignarAsesorAEstudiante(h_estudiante_maestria estudianteMaestria, h_tesis_2 tesis) {

        if (tesis.getNombresAsesor() != null) {
            estudianteMaestria.setNombresAsesor(tesis.getNombresAsesor());
        }

        if (tesis.getApellidosAsesor() != null) {
            estudianteMaestria.setApellidosAsesor(tesis.getApellidosAsesor());
        }

        if (tesis.getCorreoAsesor() != null) {
            estudianteMaestria.setCorreoAsesor(tesis.getCorreoAsesor());
        }
    }

    public String establecerAprobacionParadigmaHistoricoTesis2(String comandoXML) {
        try {
            getParser().leerXML(comandoXML);
            Secuencia secAprobacionParadigma = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APROBACION_ARTICULO_PARADIGMA));
            boolean aprobacionParadigma = secAprobacionParadigma.getValor().equals(getConstanteBean().getConstante(Constantes.TRUE)) ? true : false;

            Secuencia secHIdEstudiante = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Long hIdEstudiante = Long.parseLong(secHIdEstudiante.getValor());
            h_estudiante_maestria hEstudiante = estudianteMaestriaFacade.find(hIdEstudiante);

            //Tesis
            Secuencia secHIdTesis = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_TESIS));
            Long hIdTesis = Long.parseLong(secHIdTesis.getValor());
            h_tesis_2 hTesis2 = hTesis2Facade.find(hIdTesis);


            //Envío de correo
            String rutaArticulo = hTesis2.getRutaArticuloFin();
            String rutaCompleta = "";

            String rutaCarpeta = getConstanteBean().getConstante(Constantes.RUTA_ARCHIVO_ARTICULO_TESIS2);

            if (rutaArticulo.startsWith("/") || rutaArticulo.startsWith("\\")) {
                rutaCompleta = hTesis2.getRutaArticuloFin();
            } else {
                rutaCompleta = rutaCarpeta + hTesis2.getRutaArticuloFin();
            }

            //comprueba si el archivo se encuentra en la ruta especificada
            File archivoArticulo = new File(rutaCompleta);
            if (archivoArticulo.exists()) {
                String mensajeCorreo = String.format(Notificaciones.MENSAJE_APROBACION_REVISTA_PARADIGMA_TESIS2, hEstudiante.getNombresAsesor() + " " + hEstudiante.getApellidosAsesor(), hEstudiante.getNombres() + " " + hEstudiante.getApellidos(), hTesis2.getTemaTesis(), hTesis2.getSubareaInvestigacion());
                correoBean.enviarMail(getConstanteBean().getConstante(Constantes.CORREO_REVISTA_PARADIGMA), Notificaciones.ASUNTO_APROBACION_REVISTA_PARADIGMA_TESIS2, null, null, rutaCompleta, mensajeCorreo);
                hTesis2.setAprobadoParaParadigma(aprobacionParadigma);
                hTesis2Facade.edit(hTesis2);
                return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ESTABLECER_APROBACION_REVISTA_PARADIGMA_HISTORICOS_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
            } else {
                return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ESTABLECER_APROBACION_REVISTA_PARADIGMA_HISTORICOS_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00046, new ArrayList());
            }
        } catch (Exception ex) {
            Logger.getLogger(HistoricoTesis2.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    //********** METODOS AUXILIARES DE RETORNO DE ATRIBUTOS *************
    private ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    private ParserT getParser() {
        return (parser == null) ? new ParserT() : parser;
    }

    private h_EstudianteMaestria_FacadeLocal getEstudianteMaestriaFacade() {
        return estudianteMaestriaFacade;
    }

    private Object getTesis2Facade() {
        return hTesis2Facade;
    }
}
