/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.serviciosnegocio;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.entities.Aspirante;
import co.uniandes.sisinfo.entities.MonitoriaAceptada;
import co.uniandes.sisinfo.entities.MonitoriaRealizada;
import co.uniandes.sisinfo.entities.Solicitud;
import co.uniandes.sisinfo.entities.datosmaestros.Curso;
import co.uniandes.sisinfo.entities.datosmaestros.Parametro;
import co.uniandes.sisinfo.entities.datosmaestros.Seccion;
import co.uniandes.sisinfo.serviciosfuncionales.MonitoriaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.MonitoriaRealizadaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.SolicitudFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.CursoFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.SeccionFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Atributo;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

/**
 * Servicio de negocio: Administración de monitorías
 */
@Stateless
@EJB(name = "MonitoriaBean", beanInterface = co.uniandes.sisinfo.serviciosnegocio.MonitoriaLocal.class)
public class MonitoriaBean implements MonitoriaRemote, MonitoriaLocal {

    //---------------------------------------
    // Atributos
    //---------------------------------------
    /**
     * Parser
     */
    private ParserT parser;
    /**
     * MonitoriaFacade
     */
    @EJB
    private MonitoriaFacadeLocal monitoriaFacade;
    /**
     * CursoFacade
     */
    @EJB
    private CursoFacadeRemote cursoFacade;
    /**
     *  ConstanteBean
     */
    @EJB
    private ConstanteRemote constanteBean;
    

    @EJB
    private ListaNegraLocal listaNegraBean;
    @EJB
    private SolicitudFacadeLocal solicitudFacade;

    @EJB
    private RangoFechasBeanLocal rangoFechasBean;

    @EJB
    private AccionVencidaBeanRemote accionVencidaBean;
    
    private ServiceLocator serviceLocator;

    public MonitoriaBean() {
        try {
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            cursoFacade = (CursoFacadeRemote) serviceLocator.getRemoteEJB(CursoFacadeRemote.class);
            accionVencidaBean = (AccionVencidaBeanRemote) serviceLocator.getRemoteEJB(AccionVencidaBeanRemote.class);
        } catch (NamingException ex) {
            Logger.getLogger(MonitoriaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //---------------------------------------
    // Atributos
    //---------------------------------------
    @Deprecated
    @Override    
    public String darMonitoriasPorSeccion(String comando) {
        
        String retorno = null;
/*
        Collection<Secuencia> secuencias = new LinkedList<Secuencia>();
        Collection<Secuencia> parametros = new LinkedList<Secuencia>();
        try {
            try {
                getParser().leerXML(comando);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_MONITORIAS_POR_SECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                    return retorno;
                } catch (Exception ex2) {
                    ex2.printStackTrace();
                    Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex2);
                }
                Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, e);
            }
            String crn = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCION)).obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION)).getValor();
            Secuencia secSeccion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCION), getConstanteBean().getConstante(Constantes.NULL));
            secSeccion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION), crn));
            Secuencia secMonitorias = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MONITORES), getConstanteBean().getConstante(Constantes.NULL));
            Collection<MonitoriaAceptada> monitorias = getMonitoria().findByCRNSeccion(crn);
            String nombreCurso = getCurso().findByCRNSeccion(crn).getNombre();
            String numSeccion = Integer.toString(getSeccion().findByCRN(crn).getNumeroSeccion());
            if (monitorias.size() == 0) {
                Secuencia secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO), numSeccion);
                Atributo atrParametro = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_NUMERO_SECCION);
                secParametro.agregarAtributo(atrParametro);
                parametros.add(secParametro);
                secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO), nombreCurso);
                atrParametro = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_NOMBRE_CURSO);
                secParametro.agregarAtributo(atrParametro);
                parametros.add(secParametro);
                secSeccion.agregarSecuencia(secMonitorias);
                secuencias.add(secSeccion);
                retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_MONITORIAS_POR_SECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0070, parametros);
                return retorno;
            }
            MonitoriaAceptada mon = null;
            Aspirante estudiante = null;
            for (Iterator<MonitoriaAceptada> it = monitorias.iterator(); it.hasNext();) {
                mon = it.next();
                estudiante = mon.getSolicitud().getEstudiante();
                Secuencia secMonitor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MONITOR), getConstanteBean().getConstante(Constantes.NULL));
                Secuencia secInformacionPersonal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_PERSONAL), getConstanteBean().getConstante(Constantes.NULL));
                secInformacionPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), estudiante.getPersona().getNombres()));
                secInformacionPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), estudiante.getPersona().getApellidos()));
                secInformacionPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), estudiante.getPersona().getCorreo()));
                secMonitor.agregarSecuencia(secInformacionPersonal);
                String tipo = null;
                if (mon.getCarga() < 1) {
                    tipo = getConstanteBean().getConstante(Constantes.VAL_TIPO_MONITORIA_T2);
                } else {
                    tipo = getConstanteBean().getConstante(Constantes.VAL_TIPO_MONITORIA_T1);
                }
                Secuencia secTipoMonitoria = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_MONITORIA), tipo);
                secMonitor.agregarSecuencia(secTipoMonitoria);
                secMonitorias.agregarSecuencia(secMonitor);
            }
            secSeccion.agregarSecuencia(secMonitorias);
            secSeccion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MAX_CANTIDAD_MONITORES), Double.toString(mon.getSeccion().getMaximoMonitores())));
            secuencias.add(secSeccion);
            parametros.add(new Secuencia(Mensajes.VAL_ATR_PARAMETRO_NUMERO_SECCION, numSeccion));
            parametros.add(new Secuencia(Mensajes.VAL_ATR_PARAMETRO_NOMBRE_CURSO, nombreCurso));
            retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_MONITORIAS_POR_SECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0069, parametros);
            return retorno;
        } catch (Exception e) {
            e.printStackTrace();
            Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, e);
            try {
                retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_MONITORIAS_POR_SECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0086, parametros);
                return retorno;
            } catch (Exception ex2) {
                ex2.printStackTrace();
                Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex2);
            }
        }*/
        return retorno;
    }

    @Override
    public String actualizarNotaMonitor(String root) {
        try {
            getParser().leerXML(root);
            String crnSeccion = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION)).getValor();
            String nota = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA)).getValor();
            String idSolicitud = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_ID_SOLICITUD)).getValor();
            Solicitud solicitud = solicitudFacade.findById(Long.parseLong(idSolicitud));
            MonitoriaAceptada monitoria = null;
            for (MonitoriaAceptada monitoriaAceptada : solicitud.getMonitorias()) {
                if(monitoriaAceptada.getSeccion().getCrn().equals(crnSeccion)){
                    monitoria = monitoriaAceptada;
                }
            }
            if (monitoria == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_NOTA_MONITOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0090, new LinkedList<Secuencia>());
            } else {
                Timestamp fechaFinal = rangoFechasBean.darFechaFinalRangoPorNombre(getConstanteBean().getConstante(Constantes.VAL_RANGO_FECHAS_NOTAS_MONITORES));
                if(fechaFinal.before(new Date())){
                     String comando = parser.obtenerValor(getConstanteBean().getConstante(Constantes.TAG_NOMBRE_COMANDO));
                     String login = monitoria.getSeccion().getProfesorPrincipal().getPersona().getCorreo();
                     String accion = getConstanteBean().getConstante(Constantes.VAL_ACCION_SUBIR_NOTA_MONITOR);
                     reportarEntregaTardia(fechaFinal,comando ,accion ,login );
                }
                if (Double.parseDouble(nota) <= 2.5) {
                    listaNegraBean.agregarAListaNegraPorNota(solicitud.getEstudiante().getEstudiante().getPersona().getCorreo());
                }
                monitoria.setNota(Double.parseDouble(nota));
                getMonitoriaFacade().edit(monitoria);
                String tipoTarea = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_NOTAS_MONITORES);
                rangoFechasBean.realizarTareaSeccionSolicitud(tipoTarea, crnSeccion,Long.parseLong(idSolicitud));
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_NOTA_MONITOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0109, new LinkedList<Secuencia>());
                
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(MonitoriaBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_NOTA_MONITOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0090, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(MonitoriaBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    private void reportarEntregaTardia(Date fechaLimite, String comando, String accion, String login){
        Timestamp actual = new Timestamp(System.currentTimeMillis());
        Timestamp acordada  = new Timestamp(fechaLimite.getTime());
        String proceso = getConstanteBean().getConstante(Constantes.VAL_PROCESO_SUBIR_NOTA_MONITORIA);
        String modulo = getConstanteBean().getConstante(Constantes.VAL_MODULO_MONITORIAS);
        accionVencidaBean.guardarAccionVencida(acordada, actual, accion, login, proceso, modulo, comando, "");
    }

    @Override
    public String darDatosMonitoriaPorIdSolicitud(String xmlComando) {
        try {
            getParser().leerXML(xmlComando);
            String idSolicitud = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_ID_SOLICITUD)).getValor();
            Solicitud solicitud = solicitudFacade.findById(Long.valueOf(idSolicitud));
            Iterator<MonitoriaAceptada> ite = solicitud.getMonitorias().iterator();
            MonitoriaAceptada monitoria = ite.next();
            Seccion seccion1 = monitoria.getSeccion();
            Seccion seccion2 = null;
            if(solicitud.getMonitorias().size() == 2)
            {
                MonitoriaAceptada temp = ite.next();
                seccion2 = temp.getSeccion();
            }
            Curso curso = getCurso().findByCRNSeccion(seccion1.getCrn());
            Aspirante aspirante = solicitud.getEstudiante();

            ArrayList<Secuencia> secuencias = new ArrayList();

            Secuencia secuenciaSolicitud = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUD), getConstanteBean().getConstante(Constantes.NULL));
            secuenciaSolicitud.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_ID_SOLICITUD), Long.toString(solicitud.getId())));
            secuenciaSolicitud.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO), solicitud.getEstadoSolicitud()));
            Secuencia secuenciaEstudiante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE), getConstanteBean().getConstante(Constantes.NULL));
            Secuencia secuenciaInfoPersonal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_PERSONAL), getConstanteBean().getConstante(Constantes.NULL));
            secuenciaInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), solicitud.getEstudiante().getPersona().getNombres()));
            secuenciaInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), solicitud.getEstudiante().getPersona().getApellidos()));
            secuenciaInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_ESTUDIANTE), solicitud.getEstudiante().getPersona().getCodigo()));
            secuenciaInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), solicitud.getEstudiante().getPersona().getCorreo()));
            secuenciaInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_NACIMIENTO), solicitud.getEstudiante().getPersona().getFechaNacimiento().toString()));
            secuenciaInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_LUGAR_NACIMIENTO), solicitud.getEstudiante().getPersona().getCiudadNacimiento()));
            secuenciaInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DOCUMENTO), solicitud.getEstudiante().getPersona().getNumDocumentoIdentidad()));
            secuenciaInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_DOCUMENTO), solicitud.getEstudiante().getPersona().getTipoDocumento().getTipo()));
            secuenciaInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DIRECCION_RESIDENCIA), solicitud.getEstudiante().getPersona().getDireccionResidencia()));
            secuenciaInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PAIS), solicitud.getEstudiante().getPersona().getPais().getNombre()));
            secuenciaInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TELEFONO_RESIDENCIA), solicitud.getEstudiante().getPersona().getTelefono()));
            secuenciaInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CELULAR), solicitud.getEstudiante().getPersona().getCelular()));
            secuenciaInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_BANCO), solicitud.getEstudiante().getEstudiante().getBanco()));
            secuenciaInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CUENTA), solicitud.getEstudiante().getEstudiante().getCuentaBancaria()));
            secuenciaInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CUENTA), (solicitud.getEstudiante().getEstudiante().getTipoCuenta() != null)?solicitud.getEstudiante().getEstudiante().getTipoCuenta().getNombre():""));
            Secuencia secuenciaFacultad = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FACULTAD), "");
            Atributo attr = new Atributo(getConstanteBean().getConstante(Constantes.ATR_NOMBRE_FACULTAD), solicitud.getEstudiante().getEstudiante().getPrograma().getFacultad().getNombre());
            secuenciaFacultad.agregarAtributo(attr);
            secuenciaFacultad.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROGRAMA_ACADEMICO), solicitud.getEstudiante().getEstudiante().getPrograma().getNombre()));
            secuenciaInfoPersonal.agregarSecuencia(secuenciaFacultad);
            secuenciaEstudiante.agregarSecuencia(secuenciaInfoPersonal);
            Secuencia secuenciaInfoAcademica = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_ACADEMICA), getConstanteBean().getConstante(Constantes.NULL));
            secuenciaInfoAcademica.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE_SEGUN_CREDITOS), solicitud.getEstudiante().getEstudiante().getInformacion_Academica().getSemestreSegunCreditos()));
            secuenciaInfoAcademica.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_TOTAL), Double.toString(solicitud.getEstudiante().getEstudiante().getInformacion_Academica().getPromedioTotal())));
            secuenciaInfoAcademica.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CREDITOS_SEMESTRE_ACTUAL), Double.toString(solicitud.getEstudiante().getEstudiante().getInformacion_Academica().getCreditosSemestreActual())));
            secuenciaInfoAcademica.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CREDITOS_APROBADOS), Double.toString(solicitud.getEstudiante().getEstudiante().getInformacion_Academica().getCreditosAprobados())));
            secuenciaInfoAcademica.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CREDITOS_CURSADOS), Double.toString(solicitud.getEstudiante().getEstudiante().getInformacion_Academica().getCreditosCursados())));
            secuenciaInfoAcademica.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_ULTIMO), Double.toString(solicitud.getEstudiante().getEstudiante().getInformacion_Academica().getPromedioUltimo())));
            secuenciaInfoAcademica.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_HACE_DOS_SEMESTRES), Double.toString(solicitud.getEstudiante().getEstudiante().getInformacion_Academica().getPromedioPenultimo())));
            secuenciaInfoAcademica.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_HACE_TRES_SEMESTRES), Double.toString(solicitud.getEstudiante().getEstudiante().getInformacion_Academica().getPromedioAntepenultipo())));
            secuenciaEstudiante.agregarSecuencia(secuenciaInfoAcademica);
            secuenciaSolicitud.agregarSecuencia(secuenciaEstudiante);

            Secuencia secuenciaMonitoria = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MONITORIA), getConstanteBean().getConstante(Constantes.NULL));
            Secuencia secuenciaCursos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSOS), getConstanteBean().getConstante(Constantes.NULL));
            Secuencia secuenciaCurso = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO), getConstanteBean().getConstante(Constantes.NULL));
            Secuencia secuenciaCodigoCurso = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO), curso.getCodigo());
            Secuencia secuenciaNombreCurso = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CURSO), curso.getNombre());
            Secuencia secuenciaSecciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCIONES), getConstanteBean().getConstante(Constantes.NULL));
            Secuencia secuenciaSeccion1 = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCION), getConstanteBean().getConstante(Constantes.NULL));
            Secuencia secuenciaProfesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR), getConstanteBean().getConstante(Constantes.NULL));
            Secuencia secuenciaNombreCompleto = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_COMPLETO), seccion1.getProfesorPrincipal().getPersona().getNombres() + " " + seccion1.getProfesorPrincipal().getPersona().getApellidos());
            Secuencia secuenciaNumeroSeccion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_SECCION), "" + seccion1.getNumeroSeccion());
            Secuencia secuenciaCrnSeccion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION), seccion1.getCrn());

            secuenciaProfesor.agregarSecuencia(secuenciaNombreCompleto);

            secuenciaSeccion1.agregarSecuencia(secuenciaProfesor);
            secuenciaSeccion1.agregarSecuencia(secuenciaNumeroSeccion);
            secuenciaSeccion1.agregarSecuencia(secuenciaCrnSeccion);

            secuenciaSecciones.agregarSecuencia(secuenciaSeccion1);

            if(seccion2 != null)
            {
                Secuencia secuenciaSeccion2 = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCION), getConstanteBean().getConstante(Constantes.NULL));
                Secuencia secuenciaProfesor2 = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR), getConstanteBean().getConstante(Constantes.NULL));
                Secuencia secuenciaNombreCompleto2 = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_COMPLETO), seccion2.getProfesorPrincipal().getPersona().getNombres() + " " + seccion2.getProfesorPrincipal().getPersona().getApellidos());
                Secuencia secuenciaNumeroSeccion2 = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_SECCION), "" + seccion2.getNumeroSeccion());
                Secuencia secuenciaCrnSeccion2 = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION), seccion2.getCrn());

                secuenciaProfesor2.agregarSecuencia(secuenciaNombreCompleto);

                secuenciaSeccion2.agregarSecuencia(secuenciaProfesor2);
                secuenciaSeccion2.agregarSecuencia(secuenciaNumeroSeccion2);
                secuenciaSeccion2.agregarSecuencia(secuenciaCrnSeccion2);

                secuenciaSecciones.agregarSecuencia(secuenciaSeccion2);
            }

            secuenciaCurso.agregarSecuencia(secuenciaCodigoCurso);
            secuenciaCurso.agregarSecuencia(secuenciaNombreCurso);
            secuenciaCurso.agregarSecuencia(secuenciaSecciones);

            secuenciaCursos.agregarSecuencia(secuenciaCurso);

            secuenciaMonitoria.agregarSecuencia(secuenciaCursos);

            secuencias.add(secuenciaSolicitud);
            secuencias.add(secuenciaMonitoria);

            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_DATOS_MONITORIA_POR_ID_SOLICITUD), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0193, new ArrayList<Secuencia>());
        } catch (Exception e) {
            try {
                Logger.getLogger(MonitoriaBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_DATOS_MONITORIA_POR_ID_SOLICITUD), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0131, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(MonitoriaBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    /**
     * Método que retorna el valor de un parámetro, el cuál se encuentra en la lista
     * de parámetros dada, cuyo nombre del campo es igual al que llega como parámetro
     * @param parametros Colección de parámetros
     * @param campo Nombre del campo del parámetro
     * @return Valor del parámetro consultado. Null en caso de que el parámetro no sea encontrado
     */
    private String consultarValorParametro(Collection<Parametro> parametros, String campo) {
        Iterator<Parametro> iterator = parametros.iterator();
        while (iterator.hasNext()) {
            Parametro parametro = iterator.next();
            if (parametro.getCampo().equals(campo)) {
                return parametro.getValor();
            }
        }
        return null;
    }

    /**
     * Retorna el Parser
     * @return parser Parser
     */
    private ParserT getParser() {
        if (parser == null) {
            parser = new ParserT();
        }
        return parser;
    }


    /**
     * Retorna CursoFacade
     * @return cursoFacade CursoFacade
     */
    private CursoFacadeRemote getCurso() {
        return cursoFacade;
    } 
    /**
     * Retorna ConstanteBean
     * @return constanteBean ConstanteBean
     */
    private ConstanteRemote getConstanteBean() {
        return constanteBean;
    }


    /**
     * Retorna MonitoriaFacade
     * @return monitoriaFacade MonitoriaFacade
     */
    private MonitoriaFacadeLocal getMonitoriaFacade() {
        return monitoriaFacade;
    }

    

    @Override
    public double darMonitoresAsignados(String crn) {
        return monitoriaFacade.findByCRNSeccion(crn).size();
    }

    @Override
    public double darCargaMonitoriasPorSeccion(String crn) {
        double cuenta = 0.0;/*
        List<MonitoriaAceptada> monitorias = monitoriaFacade.findByCRNSeccion(crn);
        for (MonitoriaAceptada m : monitorias) {
            cuenta += m.getCarga();
        }*/
        return cuenta;
    }

    @Override
    public boolean hayVacantes(String crn, double maximoMonitores) {
        
        List<MonitoriaAceptada> monitorias = getMonitoriaFacade().findByCRNSeccion(crn);
        double sumaCargas = 0;
        for (MonitoriaAceptada m : monitorias) {
            double carga = m.getCarga();
            double valCargaMonitoriaT1 = Double.parseDouble(getConstanteBean().getConstante(Constantes.VAL_CARGA_MONITORIA_T1));
            if (carga == valCargaMonitoriaT1) {
                sumaCargas += 1;
            } else {
                sumaCargas += 0.5;
            }
        }

        double vacantes = maximoMonitores - sumaCargas;
        if (vacantes > 0) {

            return true;
        } else {
            return false;
        }
    }

    @Override
    public String darMonitoriasRealizadasPorCorreo(String comando) {
        try {
            getParser().leerXML(comando);
            String correo=getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            
            Collection<MonitoriaRealizada> monitorias = new ArrayList<MonitoriaRealizada>();
            Collection<Solicitud> listaSolicitudes = solicitudFacade.findByLogin(correo);
            if(listaSolicitudes.size() != 0) {
                
                Solicitud solicitud = listaSolicitudes.iterator().next();
                Aspirante aspirante = solicitud.getEstudiante();
                if(aspirante != null && 
                        aspirante.getMonitoriasRealizadas() != null &&
                        !aspirante.getMonitoriasRealizadas().isEmpty())
                monitorias = aspirante.getMonitoriasRealizadas();
            }
            Collection<Secuencia> secuencias=new ArrayList<Secuencia>();
            Secuencia secMonitorias=new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MONITORIAS_HISTORICAS_NUEVAS),"");
            for(MonitoriaRealizada m:monitorias){
                Secuencia secMonitoria=new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MONITORIA_HISTORICA_NUEVA),"");
                secMonitoria.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO), m.getCodigoCurso()));
                secMonitoria.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CURSO), m.getNombreCurso()));
                secMonitoria.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), m.getNombreProfesor()));
                secMonitoria.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO), m.getPeriodo()));
                secMonitoria.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA), m.getNota()));
                secMonitoria.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_MONITORIA), m.getTipoMonitoria()));
                secMonitorias.agregarSecuencia(secMonitoria);
            }
            secuencias.add(secMonitorias);
            String respuesta=getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_MONITORIAS_HISTORICAS_NUEVAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0139, new ArrayList());
            return respuesta;
        } catch (Exception ex) {
            Logger.getLogger(MonitoriaBean.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return null;
        }
    }



}
