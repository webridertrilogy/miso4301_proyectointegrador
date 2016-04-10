/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosnegocio;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.comun.constantes.Notificaciones;
import co.uniandes.sisinfo.entities.CalificacionCriterio;
import co.uniandes.sisinfo.entities.CalificacionJurado;
import co.uniandes.sisinfo.entities.CategoriaCriterioJurado;
import co.uniandes.sisinfo.entities.Coasesor;
import co.uniandes.sisinfo.entities.JuradoExternoUniversidad;
import co.uniandes.sisinfo.entities.TareaSencilla;
import co.uniandes.sisinfo.entities.Tesis2;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import co.uniandes.sisinfo.serviciosfuncionales.CalificacionCriterioFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.CalificacionJuradoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.CategoriaCriterioJuradoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.CoasesorFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.CorreoRemote;
import co.uniandes.sisinfo.serviciosfuncionales.JuradoExternoUniversidadFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.TareaSencillaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.Tesis2FacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.PersonaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

/**
 *
 * @author Ivan Mauricio Melo Suarez
 */
@Stateless
public class JuradosTesisBean implements JuradosTesisBeanRemote, JuradosTesisBeanLocal {

    @EJB
    private JuradoExternoUniversidadFacadeLocal juradoEFacade;
    @EJB
    private CoasesorFacadeLocal coasesorEFacade;
    @EJB
    private Tesis2FacadeLocal tesis2Facade;
    @EJB
    private ConstanteRemote constanteBean;
    @EJB
    private CalificacionJuradoFacadeLocal calJuradoFacade;
    @EJB
    private CalificacionCriterioFacadeLocal calificacionCriterioFacade;
    @EJB
    private CategoriaCriterioJuradoFacadeLocal categoriafriterioJurado;
    @EJB
    private PersonaFacadeRemote personaFacade;
    @EJB
    private CorreoRemote correoBean;
    private ParserT parser;
    private ServiceLocator serviceLocator;
    private ConversorTesisMaestria conversor;
    @EJB
    private TareaSencillaRemote tareaSencillaBean;
    @EJB
    private TareaMultipleRemote tareaBean;
    @EJB
    private TareaSencillaFacadeRemote tareaSencillaFacade;

    public JuradosTesisBean() {
        try {
            parser = new ParserT();
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            conversor = new ConversorTesisMaestria();

            personaFacade = (PersonaFacadeRemote) serviceLocator.getRemoteEJB(PersonaFacadeRemote.class);
            correoBean = (CorreoRemote) serviceLocator.getRemoteEJB(CorreoRemote.class);

            tareaSencillaBean = (TareaSencillaRemote) serviceLocator.getRemoteEJB(TareaSencillaRemote.class);
            tareaSencillaFacade = (TareaSencillaFacadeRemote) serviceLocator.getRemoteEJB(TareaSencillaFacadeRemote.class);
            tareaBean = (TareaMultipleRemote) serviceLocator.getRemoteEJB(TareaMultipleRemote.class);


        } catch (NamingException ex) {
            Logger.getLogger(JuradosTesisBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String consultarJuradosExternosPorCorreo(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secCorreoTesis = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            String correo = secCorreoTesis.getValor();
            JuradoExternoUniversidad jurado = juradoEFacade.findByCorreo(correo);
            Secuencia secJurado = conversor.pasarJuradoExternoASecuencia(jurado);
            //enviar error tesis no existe
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secJurado);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_JURADO_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {
            try {
                Logger.getLogger(JuradosTesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_JURADO_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String actualizarJuradoExterno(String xml) {
        try {
            //TODO: aca cambiar tambien puede ser un coasesor en vez de un jurado externo
            parser.leerXML(xml);
            //jurado externo
            Secuencia secJurado = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_JURADO_TESIS_EXTERNO));
            if (secJurado != null) {
                JuradoExternoUniversidad e = conversor.pasarSecuenciaAJuradoExternoUniversidad(secJurado);

                JuradoExternoUniversidad jue = juradoEFacade.findByCorreo(e.getCorreo());
                jue.setOrganizacion(e.getOrganizacion());
                jue.setCargo(e.getCargo());
                jue.setDireccion(e.getDireccion());
                jue.setTelefono(e.getTelefono());

                juradoEFacade.edit(jue);
            }
            //coasesor externo
            Secuencia secCoAsesor = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COASESOR));
            if (secCoAsesor != null) {
                Coasesor c = conversor.pasarSecuenciaACoasesor(secCoAsesor);

                Coasesor coasesor = coasesorEFacade.findByCorreo(c.getCorreo());
                coasesor.setOrganizacion(c.getOrganizacion());
                coasesor.setCargo(c.getCargo());
                coasesor.setDireccion(c.getDireccion());
                coasesor.setTelefono(c.getTelefono());

                coasesorEFacade.edit(coasesor);
            }
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_EDITAR_JURADO_TESIS_2_EXTERNO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());


        } catch (Exception ex) {
            try {
                Logger.getLogger(JuradosTesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_EDITAR_JURADO_TESIS_2_EXTERNO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0002, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    public ParserT getParser() {
        return parser;
    }

    /**
     * metodo que dado un hash retorna una tesis2 con solo una calicificacion de jurado
     * @param xml
     * @return
     */
    public String darEvaluacionJuradoPorHash(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secHash = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HASH_CALIFICACION));
            String hash = secHash.getValor();
            CalificacionJurado calj = calJuradoFacade.findByHash(hash);
            if (calj == null) {
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_EVALUACION_JURADO_POR_HASH), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CALIFICACION_JURADOS_0001, new ArrayList());

            }
            Tesis2 tTemp = calj.getTesisCalificada();
            Tesis2 tesis2 = new Tesis2();
            tesis2.setId(tTemp.getId());

            tesis2.setEstudiante(tTemp.getEstudiante());
            tesis2.setAsesor(tTemp.getAsesor());
            tesis2.setSemestreInicio(tTemp.getSemestreInicio());
            tesis2.setHorarioSustentacion(tTemp.getHorarioSustentacion());
            tesis2.setTemaProyecto(tTemp.getTemaProyecto());
            Collection<CalificacionJurado> caljurados = new ArrayList<CalificacionJurado>();
            caljurados.add(calj);
            tesis2.setCalificacionesJurados(caljurados);
            Secuencia secTesis = conversor.pasarTesis2ASecuencia(tesis2);

            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secTesis);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_EVALUACION_JURADO_POR_HASH), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(JuradosTesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_EVALUACION_JURADO_POR_HASH), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /**
     * metodo que recibe una calificacion jurado con hash
     * @param xml
     * @return
     */
    public String guardarEvaluacionJuradoPorHash(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secNotaJurado = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CALIFICACION_JURADO));
            CalificacionJurado cWeb = conversor.pasarSecuenciaACalificacionJurado(secNotaJurado);
            CalificacionJurado calBD = calJuradoFacade.findByHash(cWeb.getHash());
            boolean envioCorreo = calBD.getTerminado();
            Collection<CategoriaCriterioJurado> catsJurado = cWeb.getCategoriaCriteriosJurado();
            Collection<CalificacionCriterio> criteriosWeb = new ArrayList<CalificacionCriterio>();
            //edita los comentarios de las categorias y saca los criterios
            for (CategoriaCriterioJurado categoriaCriterioJurado : catsJurado) {
                //criteriosWeb.addAll(categoriaCriterioJurado.getCalCriterios());
                Collection<CalificacionCriterio> criteriosTemp = categoriaCriterioJurado.getCalCriterios();
                for (CalificacionCriterio calificacionCriterio : criteriosTemp) {
                    criteriosWeb.add(calificacionCriterio);
                }
                CategoriaCriterioJurado catBD = categoriafriterioJurado.find(categoriaCriterioJurado.getId());
                catBD.setComentario(categoriaCriterioJurado.getComentario());
                categoriafriterioJurado.edit(catBD);
            }
            System.out.println("num categorias = " + catsJurado.size() + "-- num criterios=" + criteriosWeb.size());

            Long idTesis = calBD.getTesisCalificada().getId();
            Tesis2 tesis2 = tesis2Facade.find(idTesis);
            //si ha pasado un dia de la fecha maxima de calificacion o la tesis ya fue calificada por coordinacion
            if (new Timestamp(tesis2.getSemestreInicio().getFechaMaximaCalificarTesis2().getTime() + 1000L * 60 * 60 * 24).before(new Date())
                    || tesis2.getCalificacion() != null || tesis2.getSemestreInicio().getFechaMaximaCalificarTesis2().before(new Date())) {
                //retorna un error de que ya no se puede calificar por que la tesis no esta en pendeinte especial:
                if (!tesis2.getEstaEnPendienteEspecial()) {
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_GUARDAR_EVALUACION_POR_HASH), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00017, new ArrayList());
                }
                //en caso de que este en pendiente especial, si
               else if (new Timestamp(tesis2.getSemestreInicio().getFechaMaxSustentacionT2PendEspecial().getTime()+ 1000L*60*60*24).before(new Date())
                    || tesis2.getCalificacion() != null )
                {
                 Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_GUARDAR_EVALUACION_POR_HASH), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00017, new ArrayList());
                }
            }
            for (CalificacionCriterio calificacionCriterio : criteriosWeb) {
                CalificacionCriterio calCritBD = calificacionCriterioFacade.find(calificacionCriterio.getId());
                calCritBD.setComentario(calificacionCriterio.getComentario());
                calCritBD.setValor(calificacionCriterio.getValor());
                calificacionCriterioFacade.edit(calCritBD);
            }
            //  calBD.setCalCriterios(cBD);
            //editar las categorias:
            calBD = calJuradoFacade.findByHash(cWeb.getHash());
            calBD.setFechaCalificacion(new Timestamp(new Date().getTime()));
            calBD.setTerminado(true);
            calBD.setNotaJurado(calcularEvaluacion(calBD));

            calJuradoFacade.edit(calBD);

            if (envioCorreo) {
                enviarCorreoCambioNotaCoordinacion(calBD);
            }

            calBD = calJuradoFacade.find(calBD.getId());
            Tesis2 t = calBD.getTesisCalificada();
            if (tesisListaParaCalificacion(t)) {
                crearTareaCoordinacionSubirNotaTesis2(t);
                calcularNotaTesis2(t);
                tesis2Facade.edit(t);
            }
            // borrar tarea jurado
            // ver que ya no exista la tarea:
            if (calBD.getRolJurado().equals(getConstanteBean().getConstante(Constantes.CTE_JURADO_TESIS_INTERNO)) || (calBD.getRolJurado().equals(getConstanteBean().getConstante(Constantes.CTE_COASESOR_TESIS)) && calBD.getCoasesor().getCoasesor() != null)
                    || calBD.getRolJurado().equals(getConstanteBean().getConstante(Constantes.CTE_ASESOR_TESIS)) && calBD.getJuradoInterno() != null) {
                Properties propiedades = new Properties();
                propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_HASH_CALIFICACION), cWeb.getHash());
                String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CALIFICAR_SUSTENTACION_TESIS2);
                completarTareaSencilla(tipo, propiedades);
            }
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_GUARDAR_EVALUACION_POR_HASH), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {
            try {
                Logger.getLogger(JuradosTesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_GUARDAR_EVALUACION_POR_HASH), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }


    public void crearTareaCoordinacionSubirNotaTesis2(Tesis2 tesis){
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SUBIR_NOTA_TESIS_2_COORDINACION);
        Persona personaEstudiante = tesis.getEstudiante().getPersona();
        String mensaje = String.format(Notificaciones.MENSAJE_SUBIR_NOTA_TESIS_2, personaEstudiante.getNombres()+" "+personaEstudiante.getApellidos());
        String nombreRol = getConstanteBean().getConstante(Constantes.ROL_COORDINACION);
        String header = Notificaciones.HEADER_SUBIR_NOTA_TESIS_2;
        String footer = Notificaciones.FOOTER_SUBIR_NOTA_TESIS_2;
        String asunto = Notificaciones.ASUNTO_SUBIR_NOTA_TESIS_2;
        String cmd = getConstanteBean().getConstante(Constantes.CMD_SUBIR_NOTA_TESIS_2);
        HashMap<String,String> params = new HashMap<String,String>();
        params.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), tesis.getId().toString());
        Calendar calendar = Calendar.getInstance();
        Timestamp fechaInicio = new Timestamp(calendar.getTimeInMillis());
        calendar.add(Calendar.MONTH, 6);
        Timestamp fechaFin = new Timestamp(calendar.getTimeInMillis());
        tareaBean.crearTareaRol(mensaje, tipo, nombreRol, true, header, footer, fechaInicio, fechaFin, cmd, params, asunto);
    }

    private Double calcularEvaluacion(CalificacionJurado calBD) {
        Collection<CategoriaCriterioJurado> categorias = calBD.getCategoriaCriteriosJurado();
        Collection<CalificacionCriterio> cal = new ArrayList<CalificacionCriterio>();
        for (CategoriaCriterioJurado categoriaCriterioJurado : categorias) {
            cal.addAll(categoriaCriterioJurado.getCalCriterios());
        }

        Double nota = 0.0;
        Double peso = 0.0;
        for (CalificacionCriterio calificacionCriterio : cal) {
            nota = nota + (calificacionCriterio.getPeso() * calificacionCriterio.getValor());
            peso = peso + calificacionCriterio.getPeso();
        }
        if (peso > 0) {
            return nota / peso;
        } else {
            return 0.0;
        }
    }

    private Double calcularNotaTesis2(Tesis2 t) {
        Collection<CalificacionJurado> calificaciones = t.getCalificacionesJurados();

        int numJurados = 0;
        Double promJurados = 00.0;
        int asesores = 00;
        Double promedioA = 00.0;
        for (CalificacionJurado calificacionJurado : calificaciones) {

            if (calificacionJurado.getNotaJurado() != null) {
                if (calificacionJurado.getRolJurado().equals(getConstanteBean().getConstante(Constantes.CTE_ASESOR_TESIS))
                        || calificacionJurado.getRolJurado().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS2_COASESOR))) {
                    asesores++;
                    promedioA = promedioA + calificacionJurado.getNotaJurado();
                } else {
                    numJurados++;
                    promJurados = promJurados + calificacionJurado.getNotaJurado();
                }
            }
        }
        if (numJurados > 0) {
            promJurados = promJurados / numJurados;
        } else {
            promJurados = 0.0;
        }

        if (promedioA > 0) {
            promedioA = promedioA / asesores;
        } else {
            promedioA = 0.0;
        }

        Double porcentajeJurados = Double.parseDouble(getConstanteBean().getConstante(Constantes.CTE_TESIS_CALIFICACION_JURADOS));
        Double porcentajeAsesores = Double.parseDouble(getConstanteBean().getConstante(Constantes.CTE_TESIS_CALIFICACION_ASESOR));
        Double notaFinal = (promJurados * porcentajeJurados / 100) + (promedioA * porcentajeAsesores / 100);
        t.setNotaSustentacion(notaFinal);
        return notaFinal;
    }

    public void calcularNotaSustencion(Tesis2 t) {
        if (tesisListaParaCalificacion(t)) {
            calcularNotaTesis2(t);
            tesis2Facade.edit(t);
        } else {
            t.setNotaSustentacion(null);
            tesis2Facade.edit(t);
        }
    }

    private boolean tesisListaParaCalificacion(Tesis2 t) {
        Collection<CalificacionJurado> calificaiones = t.getCalificacionesJurados();
        for (CalificacionJurado calificacionJurado : calificaiones) {

            //si la calificacion no ha sido terminada y tampoco esta cancelada
            /*if (!calificacionJurado.getTerminado()&& calificacionJurado.getCancelada()==false  ) {
            return false;
            }*/

            if (calificacionJurado.getRolJurado().equals(Constantes.CTE_ASESOR_TESIS) && !calificacionJurado.getTerminado()) {
                return false;
            }

            if (!calificacionJurado.getRolJurado().equals(Constantes.CTE_ASESOR_TESIS) && !calificacionJurado.getTerminado() && calificacionJurado.getCancelada() == false) {
                return false;
            }

        }
        return true;
    }

    private void enviarCorreoCambioNotaCoordinacion(CalificacionJurado calBD) {
        //SimpleDateFormat sdfHMS = new SimpleDateFormat("EEEE dd 'de' MMMMM 'de' yyyy ' a las ' hh':'mm a ", new Locale("es"));

        String asunto = Notificaciones.ASUNTO_MODIFICACION_NOTA_SUSTENTACION_TESIS2_JURADO;
        asunto = asunto.replace("%1",
                calBD.getTesisCalificada().getEstudiante().getPersona().getNombres()
                + " " + calBD.getTesisCalificada().getEstudiante().getPersona().getApellidos());
        String mensaje = Notificaciones.MENSAJE_MODIFICACION_NOTA_SUSTENTACION_TESIS2_JURADO;

        String correo = getConstanteBean().getConstante(Constantes.CTE_CORREO_COORDINADOR_MAESTRTIA);
        Persona p = personaFacade.findByCorreo(correo);
        mensaje = mensaje.replace("%1", p.getNombres() + " " + p.getApellidos());
        if (calBD.getRolJurado().equals(getConstanteBean().getConstante(Constantes.CTE_ASESOR_TESIS))) {
            mensaje = mensaje.replace("%2", calBD.getJuradoInterno().getPersona().getNombres() + " " + calBD.getJuradoInterno().getPersona().getApellidos());
            mensaje = mensaje.replace("%3", calBD.getJuradoInterno().getPersona().getCorreo());
        } else if (calBD.getRolJurado().equals(getConstanteBean().getConstante(Constantes.CTE_JURADO_TESIS_INTERNO))) {
            mensaje = mensaje.replace("%2", calBD.getJuradoInterno().getPersona().getNombres() + " " + calBD.getJuradoInterno().getPersona().getApellidos());
            mensaje = mensaje.replace("%3", calBD.getJuradoInterno().getPersona().getCorreo());
        } else if (calBD.getRolJurado().equals(getConstanteBean().getConstante(Constantes.CTE_JURADO_EXTERNO))) {
            mensaje = mensaje.replace("%2", calBD.getJuradoExterno().getNombres() + " " + calBD.getJuradoExterno().getApellidos());
            mensaje = mensaje.replace("%3", calBD.getJuradoExterno().getCorreo());
        } else if (calBD.getRolJurado().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS2_COASESOR))) {
            if (calBD.getCoasesor().getCoasesor() != null) {
                mensaje = mensaje.replace("%2", calBD.getCoasesor().getCoasesor().getPersona().getNombres() + " " + calBD.getCoasesor().getCoasesor().getPersona().getApellidos());
                mensaje = mensaje.replace("%3", calBD.getCoasesor().getCoasesor().getPersona().getCorreo());
            } else {
                mensaje = mensaje.replace("%2", calBD.getCoasesor().getNombres() + " " + calBD.getCoasesor().getApellidos());
                mensaje = mensaje.replace("%3", calBD.getCoasesor().getCorreo());
            }
        }
        mensaje = mensaje.replace("%4", calBD.getTesisCalificada().getEstudiante().getPersona().getNombres() + " " + calBD.getTesisCalificada().getEstudiante().getPersona().getApellidos());
        correoBean.enviarMail(correo, asunto, null, null, null, mensaje);
    }

    /*
     * Metodos para las nuevas tareaas:...
     */
    private void completarTareaSencilla(String tipo, Properties propiedades) {
        TareaSencilla tarea = tareaSencillaFacade.findById(tareaBean.consultarIdTareaPorParametrosTipo(tipo, propiedades));
        if (tarea != null) {
            tareaSencillaBean.realizarTareaPorId(tarea.getId());
        }
    }
}
