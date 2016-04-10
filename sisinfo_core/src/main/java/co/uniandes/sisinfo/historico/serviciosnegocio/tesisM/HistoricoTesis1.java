package co.uniandes.sisinfo.historico.serviciosnegocio.tesisM;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.comun.constantes.Notificaciones;
import co.uniandes.sisinfo.historico.entities.tesisM.h_estudiante_maestria;
import co.uniandes.sisinfo.historico.entities.tesisM.h_tesis_1;
import co.uniandes.sisinfo.historico.serviciosfuncionales.tesisM.h_EstudianteMaestria_FacadeLocal;
import co.uniandes.sisinfo.historico.serviciosfuncionales.tesisM.h_tesis_1FacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.CorreoBean;
import co.uniandes.sisinfo.serviciosfuncionales.CorreoRemote;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import co.uniandes.sisinfo.serviciosnegocio.ConstanteRemote;

import co.uniandes.sisinfo.serviciosnegocio.HistoricoTesis1Remote;
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
 * Servicios de negocio para Histórico de Tesis 1
 * @author Marcela Morales, Paola Gomez
 */
@Stateless
public class HistoricoTesis1 implements HistoricoTesis1Remote, HistoricoTesis1Local {

    @EJB
    private h_tesis_1FacadeLocal tesis1Facade;
    @EJB
    private h_EstudianteMaestria_FacadeLocal estudianteMaestriaFacade;
    @EJB
    private ConstanteRemote constanteBean;
    private ServiceLocator serviceLocator;
    private ConversorTesisMaestriaH conversor;
    private ParserT parser;
    @EJB
    private CorreoRemote correoBean;

    public HistoricoTesis1() {
        try {
            parser = new ParserT();
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            correoBean = (CorreoRemote) serviceLocator.getRemoteEJB(CorreoRemote.class);
            conversor = new ConversorTesisMaestriaH();
        } catch (NamingException ex) {
            Logger.getLogger(HistoricoTesis1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String pasarTesis1RechazadaAHistorico(String xml) {
        try {
            getParser().leerXML(xml);
            Secuencia secTesis = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS1));
            h_tesis_1 tesis = conversor.pasarSecuenciaATesis1Historica(secTesis);

            String correo = conversor.pasarSecuenciaAEstudianteCorreo(secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE)));
            h_estudiante_maestria estudianteM = estudianteMaestriaFacade.findByCorreoEstudiante(correo);

            if (estudianteM == null) {
                Secuencia secEstudiante = secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE));
                h_estudiante_maestria estudianteMaestria = conversor.pasarSecuenciaAEstudianteSimple(secEstudiante);
                estudianteMaestria.setId(null);
                tesis.setId(null);
                Collection<h_tesis_1> tesises = new ArrayList();
                tesises.add(tesis);
                estudianteMaestria.setTesis1(tesises);
                estudianteMaestria.setInfoT1Rechazada(true);
                getEstudianteMaestriaFacade().create(estudianteMaestria);
            } else {
                Collection<h_tesis_1> tesises = estudianteM.getTesis1();

                boolean estaMigrada = false;

                if (tesises == null) {
                    estaMigrada = false;
                } else {
                    if (tesises.isEmpty()) {
                        estaMigrada = false;
                    } else {
                        for (h_tesis_1 tesis1 : tesises) {
                            if (tesis1.getFechaCreacion().equals(tesis.getFechaCreacion())
                                    && tesis1.getEstado().equals(tesis.getEstado())) {
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
                    estudianteM.setTesis1(tesises);
                    estudianteM.setInfoT1Rechazada(true);
                    getEstudianteMaestriaFacade().edit(estudianteM);
                }
            }

            ArrayList<Secuencia> secs = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secs, getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS1_RECHAZADAS_A_HISTORICOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

        } catch (Exception ex) {
            Logger.getLogger(HistoricoTesis1.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String pasarTesis1RetiradasAHistorico(String xml) {
        try {
            getParser().leerXML(xml);
            Secuencia secTesises1 = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_TESISES));
            ArrayList<Secuencia> secTesises = secTesises1.getSecuencias();
            for (Iterator<Secuencia> it = secTesises.iterator(); it.hasNext();) {
                Secuencia secTesis = it.next();
                h_tesis_1 tesis = conversor.pasarSecuenciaATesis1Historica(secTesis);

                String correo = conversor.pasarSecuenciaAEstudianteCorreo(secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE)));
                h_estudiante_maestria estudianteM = estudianteMaestriaFacade.findByCorreoEstudiante(correo);

                if (estudianteM == null) {
                    Secuencia secEstudiante = secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE));
                    h_estudiante_maestria estudianteMaestria = conversor.pasarSecuenciaAEstudianteSimple(secEstudiante);
                    estudianteMaestria.setId(null);
                    tesis.setId(null);
                    Collection<h_tesis_1> tesises = new ArrayList();
                    tesises.add(tesis);
                    estudianteMaestria.setTesis1(tesises);
                    estudianteMaestria.setInfoT1Retirada(true);
                    getEstudianteMaestriaFacade().create(estudianteMaestria);
                } else {
                    Collection<h_tesis_1> tesises = estudianteM.getTesis1();

                    boolean estaMigrada = false;

                    if (tesises == null) {
                        estaMigrada = false;
                    } else {
                        if (tesises.isEmpty()) {
                            estaMigrada = false;
                        } else {
                            for (h_tesis_1 tesis1 : tesises) {
                                if (tesis1.getFechaCreacion().equals(tesis.getFechaCreacion())
                                        && tesis1.getEstado().equals(tesis.getEstado())) {
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
                        estudianteM.setTesis1(tesises);
                        estudianteM.setInfoT1Retirada(true);
                        getEstudianteMaestriaFacade().edit(estudianteM);
                    }
                }

            }
            ArrayList<Secuencia> secs = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secs, getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS1_RETIRADAS_A_HISTORICOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(HistoricoTesis1.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String pasarTesis1PerdidasAHistorico(String xml) {
        try {
            getParser().leerXML(xml);
            Secuencia secTesises1 = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_TESISES));
            ArrayList<Secuencia> secTesises = secTesises1.getSecuencias();
            for (Iterator<Secuencia> it = secTesises.iterator(); it.hasNext();) {
                Secuencia secTesis = it.next();
                h_tesis_1 tesis = conversor.pasarSecuenciaATesis1Historica(secTesis);

                String correo = conversor.pasarSecuenciaAEstudianteCorreo(secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE)));
                h_estudiante_maestria estudianteM = estudianteMaestriaFacade.findByCorreoEstudiante(correo);

                if (estudianteM == null) {
                    Secuencia secEstudiante = secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE));
                    h_estudiante_maestria estudianteMaestria = conversor.pasarSecuenciaAEstudianteSimple(secEstudiante);

                    estudianteMaestria.setId(null);
                    tesis.setId(null);
                    Collection<h_tesis_1> tesises = new ArrayList();
                    tesises.add(tesis);
                    estudianteMaestria.setTesis1(tesises);
                    estudianteMaestria.setInfoT1Perdida(true);
                    getEstudianteMaestriaFacade().create(estudianteMaestria);
                } else {
                    Collection<h_tesis_1> tesises = estudianteM.getTesis1();

                    boolean estaMigrada = false;

                    if (tesises == null) {
                        estaMigrada = false;
                    } else {
                        if (tesises.isEmpty()) {
                            estaMigrada = false;
                        } else {
                            for (h_tesis_1 tesis1 : tesises) {
                                if (tesis1.getFechaCreacion().equals(tesis.getFechaCreacion())
                                        && tesis1.getEstado().equals(tesis.getEstado())) {
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
                        estudianteM.setTesis1(tesises);
                        estudianteM.setInfoT1Perdida(true);
                        getEstudianteMaestriaFacade().edit(estudianteM);
                    }
                }

            }
            ArrayList<Secuencia> secs = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secs, getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS1_PERDIDAS_A_HISTORICOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(HistoricoTesis1.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String pasarTesis1TerminadasAHistorico(String xml) {
        try {
            getParser().leerXML(xml);
            Secuencia secTesises1 = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_TESISES));
            ArrayList<Secuencia> secTesises = secTesises1.getSecuencias();
            for (Iterator<Secuencia> it = secTesises.iterator(); it.hasNext();) {
                Secuencia secTesis = it.next();
                h_tesis_1 tesis = conversor.pasarSecuenciaATesis1Historica(secTesis);

                String correo = conversor.pasarSecuenciaAEstudianteCorreo(secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE)));
                h_estudiante_maestria estudianteM = estudianteMaestriaFacade.findByCorreoEstudiante(correo);

                if (estudianteM == null) {
                    Secuencia secEstudiante = secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE));
                    h_estudiante_maestria estudianteMaestria = conversor.pasarSecuenciaAEstudianteSimple(secEstudiante);
                    estudianteMaestria.setId(null);
                    tesis.setId(null);
                    Collection<h_tesis_1> tesises = new ArrayList();
                    tesises.add(tesis);
                    estudianteMaestria.setTesis1(tesises);
                    estudianteMaestria.setInfoT1Terminada(true);
                    getEstudianteMaestriaFacade().create(estudianteMaestria);
                } else {
                    Collection<h_tesis_1> tesises = estudianteM.getTesis1();

                    boolean estaMigrada = false;

                    if (tesises == null) {
                        estaMigrada = false;
                    } else {
                        if (tesises.isEmpty()) {
                            estaMigrada = false;
                        } else {
                            for (h_tesis_1 tesis1 : tesises) {
                                if (tesis1.getCorreoAsesor().equals(tesis.getCorreoAsesor())
                                        && tesis1.getSemestre().equals(tesis.getSemestre())
                                        && tesis1.getEstado().equals(tesis.getEstado())) {
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
                        estudianteM.setTesis1(tesises);
                        estudianteM.setInfoT1Terminada(true);
                        getEstudianteMaestriaFacade().edit(estudianteM);
                    }
                }
            }

            ArrayList<Secuencia> secs = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secs, getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS1_TERMINADAS_A_HISTORICOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(HistoricoTesis1.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    //Métodos auxiliares de retorno de atributos
    private ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    private ParserT getParser() {
        return (parser == null) ? new ParserT() : parser;
    }

    private h_tesis_1FacadeLocal getTesis1Facade() {
        return tesis1Facade;
    }

    private h_EstudianteMaestria_FacadeLocal getEstudianteMaestriaFacade() {
        return estudianteMaestriaFacade;
    }

    public String establecerAprobacionParadigmaHistoricoTesis1(String comandoXML) {
        try {
            getParser().leerXML(comandoXML);

            //valor de la aprobación (Verdadero o falso)
            Secuencia secAprobacionParadigma = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APROBACION_ARTICULO_PARADIGMA));
            boolean aprobacionParadigma = secAprobacionParadigma.getValor().equals(getConstanteBean().getConstante(Constantes.TRUE)) ? true : false;

            //Estudiante
            Secuencia secHIdEstudiante = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Long hIdEstudiante = Long.parseLong(secHIdEstudiante.getValor());
            h_estudiante_maestria hEstudiante = estudianteMaestriaFacade.find(hIdEstudiante);

            //Tesis
            Secuencia secHIdTesis = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_TESIS));
            Long hIdTesis = Long.parseLong(secHIdTesis.getValor());
            h_tesis_1 hTesis1 = tesis1Facade.find(hIdTesis);

            //Envío de correo

            String rutaArticulo = hTesis1.getRutaArticulo();
            String rutaCompleta = "";
            String rutaCarpeta = getConstanteBean().getConstante(Constantes.RUTA_ARCHIVO_ARTICULO_TESIS2);

            if (rutaArticulo.startsWith("/") || rutaArticulo.startsWith("\\")) {
                rutaCompleta = hTesis1.getRutaArticulo();
            } else {
                rutaCompleta = rutaCarpeta + hTesis1.getRutaArticulo();
            }

            File archivoArticulo = new File(rutaCompleta);
            if (archivoArticulo.exists()) {
                String mensajeCorreo = String.format(Notificaciones.MENSAJE_APROBACION_REVISTA_PARADIGMA_TESIS1, hEstudiante.getNombresAsesor() + " " + hEstudiante.getApellidosAsesor(), hEstudiante.getNombres() + " " + hEstudiante.getApellidos(), hTesis1.getTema(), hTesis1.getSubareaInvestigacion());
                correoBean.enviarMail(getConstanteBean().getConstante(Constantes.CORREO_REVISTA_PARADIGMA), Notificaciones.ASUNTO_APROBACION_REVISTA_PARADIGMA_TESIS1, null, null, rutaCompleta, mensajeCorreo);
                hTesis1.setAprobadoParaParadigma(aprobacionParadigma);
                tesis1Facade.edit(hTesis1);
                return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ESTABLECER_APROBACION_REVISTA_PARADIGMA_HISTORICOS_TESIS_1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
            } else {
                return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ESTABLECER_APROBACION_REVISTA_PARADIGMA_HISTORICOS_TESIS_1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00046, new ArrayList());
            }
        } catch (Exception ex) {
            Logger.getLogger(HistoricoTesis1.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
