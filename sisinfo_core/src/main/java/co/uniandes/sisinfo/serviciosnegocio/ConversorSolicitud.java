/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.naming.NamingException;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.entities.MonitoriaAceptada;
import co.uniandes.sisinfo.entities.MonitoriaRealizada;
import co.uniandes.sisinfo.entities.Monitoria_Solicitada;
import co.uniandes.sisinfo.entities.Solicitud;
import co.uniandes.sisinfo.entities.datosmaestros.Curso;
import co.uniandes.sisinfo.entities.datosmaestros.Estudiante;
import co.uniandes.sisinfo.entities.datosmaestros.InformacionAcademica;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import co.uniandes.sisinfo.entities.datosmaestros.Seccion;
import co.uniandes.sisinfo.entities.datosmaestros.Sesion;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.SeccionFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Atributo;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;

/**
 *
 * @author Juan Manuel Moreno B.
 */
public class ConversorSolicitud {

        /**
     * Parser
     */
    private ParserT parser;

    /**
     *  ConstanteBean
     */
    @EJB
    private ConstanteLocal constanteBean;

    /**
     *  ServiceLocator
     */
    private ServiceLocator serviceLocator;

    /**
     * SeccionFacade
     */
    @EJB
    private SeccionFacadeLocal seccionFacade;

    /**
     * Constructor de ConversorSolicitud
     */
    public ConversorSolicitud() throws Exception {

        try {

            initConversor();
        } catch (NamingException ex) {

            Logger.getLogger(PreseleccionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Inicializa las caracteristicas
     */
    public void initConversor() throws NamingException {

//        serviceLocator = new ServiceLocator();
//        constanteBean = (ConstanteLocal) serviceLocator.getLocalEJB(ConstanteLocal.class);
//        seccionFacade = (SeccionFacadeLocal) serviceLocator.getLocalEJB(SeccionFacadeLocal.class);
    }
    
    /**
     * Da una secuencia de solicitud simple.
     */
    //TODO: revisar corregido de afan
    public Secuencia darSecuenciaSolicitudSimple(Solicitud solicitud) {
        
        Secuencia seqSolicitud =
        new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUD), getConstanteBean().getConstante(Constantes.NULL));
        seqSolicitud.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_ID_SOLICITUD), Long.toString(solicitud.getId())));
        Secuencia seqEstado = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO), solicitud.getEstadoSolicitud());
        seqSolicitud.agregarSecuencia(seqEstado);
        Secuencia secuenciaMonitoria = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MONITORIA_SOLICITADA), getConstanteBean().getConstante(Constantes.NULL));
        Curso curso = solicitud.getMonitoria_solicitada().getCurso();

        /* Si la solicitud está en estado "Aspirante"
         * se manda en el tag de secciones, todas las secciones a las cuales aplicó el estudiante.
         * De lo contrario, se manda las secciones a las que está escogido
         */
        Collection<Seccion> secciones = new ArrayList<Seccion>();
        if(solicitud.getEstadoSolicitud().equals(getConstanteBean().getConstante(Constantes.ESTADO_ASPIRANTE))) {

            //secciones.addAll(solicitud.getMonitoria_solicitada().getCurso().getSecciones());
        }
        else {
            
            Collection<MonitoriaAceptada> listaMonitorias = solicitud.getMonitorias();
            for (Iterator<MonitoriaAceptada> it = listaMonitorias.iterator(); it.hasNext();) {
                
                MonitoriaAceptada monitoria = it.next();
                secciones.add(monitoria.getSeccion());
            }
        }
        Secuencia secuenciaCurso =
                new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO), getConstanteBean().getConstante(Constantes.NULL));
        secuenciaCurso.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO), curso.getCodigo()));
        secuenciaCurso.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CURSO), curso.getNombre()));
        if (!secciones.isEmpty()) {
            
            Iterator<Seccion> iterator = secciones.iterator();
            Secuencia secuenciaSecciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCIONES), "");
            while (iterator.hasNext()) {
                
                Seccion seccion = iterator.next();
                Secuencia secuenciaSeccion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCION), getConstanteBean().getConstante(Constantes.NULL));
                Secuencia secuenciaProfesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR), getConstanteBean().getConstante(Constantes.NULL));
                secuenciaProfesor.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_COMPLETO), seccion.getProfesorPrincipal().getPersona().getNombres() + " " + seccion.getProfesorPrincipal().getPersona().getApellidos()));
                secuenciaSeccion.agregarSecuencia(secuenciaProfesor);
                secuenciaSeccion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_SECCION), Integer.toString(seccion.getNumeroSeccion())));
                secuenciaSeccion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION), seccion.getCrn()));
                secuenciaSecciones.agregarSecuencia(secuenciaSeccion);
            }
            secuenciaCurso.agregarSecuencia(secuenciaSecciones);
        }
        secuenciaMonitoria.agregarSecuencia(secuenciaCurso);
        seqSolicitud.agregarSecuencia(secuenciaMonitoria);
        Monitoria_Solicitada monSol = solicitud.getMonitoria_solicitada();
        Secuencia secCursoVisto = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO_VISTO), "");
        Secuencia secNotaMateria = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA_MATERIA), "" + monSol.getNota_materia());
        Secuencia secPeriodoMateria = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO), monSol.getPeriodoAcademicoEnQueSeVio());
        Secuencia secProfesorConQuienVioMateria = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR_CON_QUIEN_LA_VIO), monSol.getProfesorConQuienLaVio());
        secCursoVisto.agregarSecuencia(secPeriodoMateria);
        secCursoVisto.agregarSecuencia(secProfesorConQuienVioMateria);
        secCursoVisto.agregarSecuencia(secNotaMateria);
        seqSolicitud.agregarSecuencia(secCursoVisto);
        
        return seqSolicitud;
    }

    /**
     * Da secuencia estudiante T2
     * @param estudiante
     * @return
     */
    public Secuencia darSecuenciaEstudianteT2(Estudiante estudiante) {
        
        Secuencia secEstudiante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE), getConstanteBean().getConstante(Constantes.NULL));
        Secuencia secInfoPersonal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_PERSONAL), getConstanteBean().getConstante(Constantes.NULL));
        secInfoPersonal.agregarAtributo(new Atributo(getConstanteBean().getConstante(Constantes.ATR_EDITABLE), "" + true));

        Persona persona = estudiante.getPersona();
        Secuencia secNombres = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), persona.getNombres());
        Secuencia secApellidos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), persona.getApellidos());
        Secuencia secCodigoEstudiante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_ESTUDIANTE), persona.getCodigo());
        Secuencia secCorreo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), persona.getCorreo());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Secuencia secFechaNac = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_NACIMIENTO), sdf.format(new Date(persona.getFechaNacimiento().getTime())));
        Secuencia secLugarNac = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_LUGAR_NACIMIENTO), persona.getCiudadNacimiento());
        Secuencia secDocumento = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DOCUMENTO), persona.getNumDocumentoIdentidad());
        Secuencia secTipoDoc = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_DOCUMENTO), persona.getTipoDocumento().getTipo());
        Secuencia secDirResid = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DIRECCION_RESIDENCIA), persona.getDireccionResidencia());
        Secuencia secPais = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PAIS), persona.getPais().getNombre());
        Secuencia secTelResid = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TELEFONO_RESIDENCIA), persona.getTelefono());
        Secuencia secCelular = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CELULAR), persona.getCelular());
        Secuencia secBanco = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_BANCO), estudiante.getBanco());
        Secuencia secCuenta = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CUENTA), estudiante.getCuentaBancaria());
        Secuencia secTipoCuenta = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CUENTA), (estudiante.getTipoCuenta() != null)?estudiante.getTipoCuenta().getNombre():"");
        Secuencia secFacultad = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FACULTAD), getConstanteBean().getConstante(Constantes.NULL));
        secFacultad.agregarAtributo(new Atributo(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), estudiante.getPrograma().getFacultad().getNombre()));
        Secuencia secPrograma = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROGRAMA_ACADEMICO), estudiante.getPrograma().getNombre());
        secFacultad.agregarSecuencia(secPrograma);
        
        secInfoPersonal.agregarSecuencia(secNombres);
        secInfoPersonal.agregarSecuencia(secApellidos);
        secInfoPersonal.agregarSecuencia(secCodigoEstudiante);
        secInfoPersonal.agregarSecuencia(secCorreo);
        secInfoPersonal.agregarSecuencia(secFechaNac);
        secInfoPersonal.agregarSecuencia(secLugarNac);
        secInfoPersonal.agregarSecuencia(secDocumento);
        secInfoPersonal.agregarSecuencia(secTipoDoc);
        secInfoPersonal.agregarSecuencia(secDirResid);
        secInfoPersonal.agregarSecuencia(secPais);
        secInfoPersonal.agregarSecuencia(secTelResid);
        secInfoPersonal.agregarSecuencia(secCelular);
        secInfoPersonal.agregarSecuencia(secBanco);
        secInfoPersonal.agregarSecuencia(secBanco);
        secInfoPersonal.agregarSecuencia(secCuenta);
        secInfoPersonal.agregarSecuencia(secTipoCuenta);
        secInfoPersonal.agregarSecuencia(secFacultad);

        Secuencia secInfoAcademica = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_ACADEMICA), getConstanteBean().getConstante(Constantes.NULL));
        secInfoAcademica.agregarAtributo(new Atributo(getConstanteBean().getConstante(Constantes.ATR_EDITABLE), "" + true));

        InformacionAcademica infoAcad = estudiante.getInformacion_Academica();
        Secuencia secSemestreSegunCreditos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE_SEGUN_CREDITOS), infoAcad.getSemestreSegunCreditos());
        Secuencia secPromedioTotal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_TOTAL), infoAcad.getPromedioTotal().toString());
        Secuencia secCreditosSemestreActual = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CREDITOS_SEMESTRE_ACTUAL), infoAcad.getCreditosSemestreActual().toString());
        Secuencia secCreditosAprobados = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CREDITOS_APROBADOS), infoAcad.getCreditosAprobados().toString());
        Secuencia secCreditosCursados = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CREDITOS_CURSADOS), infoAcad.getCreditosCursados().toString());
        Secuencia secPromedioUltimo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_ULTIMO), infoAcad.getPromedioUltimo().toString());

        secInfoAcademica.agregarSecuencia(secSemestreSegunCreditos);
        secInfoAcademica.agregarSecuencia(secPromedioTotal);
        secInfoAcademica.agregarSecuencia(secCreditosSemestreActual);
        secInfoAcademica.agregarSecuencia(secCreditosAprobados);
        secInfoAcademica.agregarSecuencia(secCreditosCursados);
        secInfoAcademica.agregarSecuencia(secPromedioUltimo);

        if(infoAcad.getPromedioPenultimo() != null) {
            Secuencia secPromedioPenultimo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_HACE_DOS_SEMESTRES), infoAcad.getPromedioPenultimo().toString());
            secInfoAcademica.agregarSecuencia(secPromedioPenultimo);
        }
        if(infoAcad.getPromedioAntepenultipo() != null) {
            Secuencia secPromedioAntepenultimo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_HACE_TRES_SEMESTRES), infoAcad.getPromedioAntepenultipo().toString());
            secInfoAcademica.agregarSecuencia(secPromedioAntepenultimo);
        }

        secEstudiante.agregarSecuencia(secInfoPersonal);
        secEstudiante.agregarSecuencia(secInfoAcademica);

        return secEstudiante;
    }

    /**
     * Da secuencia solicitud T2.
     * @param solicitud
     * @param sec1
     * @param sec2
     * @return
     */
    public Secuencia darSecuenciaSolicitudMonitorT2(Solicitud solicitud, Seccion sec1, Seccion sec2) {

        Secuencia secSolicitud = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUD), getConstanteBean().getConstante(Constantes.NULL));
        secSolicitud.agregarAtributo(new Atributo(getConstanteBean().getConstante(Constantes.ATR_EDITABLE), "" + true));

        Secuencia secSecciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCIONES), getConstanteBean().getConstante(Constantes.NULL));
        Secuencia secSeccion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCION), getConstanteBean().getConstante(Constantes.NULL));
        Secuencia secCrn = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION), sec1.getCrn());
        Secuencia secNumeroSeccion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_SECCION), "" + sec1.getNumeroSeccion());
        secSeccion.agregarSecuencia(secCrn);
        secSeccion.agregarSecuencia(secNumeroSeccion);
        secSecciones.agregarSecuencia(secSeccion);
        
        if(sec2 != null) {
            
            secSeccion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCION), getConstanteBean().getConstante(Constantes.NULL));
            secCrn = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION), sec2.getCrn());
            secNumeroSeccion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_SECCION), "" + sec2.getNumeroSeccion());
            secSeccion.agregarSecuencia(secCrn);
            secSeccion.agregarSecuencia(secNumeroSeccion);
            secSecciones.agregarSecuencia(secSeccion);
        }

        Secuencia secIdSolicitud = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_ID_SOLICITUD), "" + solicitud.getId());
        Secuencia secEstado = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO), solicitud.getEstadoSolicitud());
        Secuencia secEstudiante = darSecuenciaEstudianteT2(solicitud.getEstudiante().getEstudiante());

        secSolicitud.agregarSecuencia(secSecciones);
        secSolicitud.agregarSecuencia(secIdSolicitud);
        secSolicitud.agregarSecuencia(secEstado);
        secSolicitud.agregarSecuencia(secEstudiante);

        return secSolicitud;
    }

    /**
     * Método que retorna la secuencia completa de una solicitud dada
     * @param solicitud Solicitud
     * @return Secuencia de solicitud completa
     */
    public Secuencia darSecuenciaSolicitudCompleta(Solicitud solicitud) {
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
        Secuencia secuenciaInfoEmergencia = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_EMERGENCIA), getConstanteBean().getConstante(Constantes.NULL));
        secuenciaInfoEmergencia.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), solicitud.getEstudiante().getEstudiante().getAvisarEmergenciaNombres()));
        secuenciaInfoEmergencia.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), solicitud.getEstudiante().getEstudiante().getAvisarEmergenciaApellidos()));
        secuenciaInfoEmergencia.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TELEFONO_EMERGENCIA), solicitud.getEstudiante().getEstudiante().getTelefonoEmergencia()));
        secuenciaEstudiante.agregarSecuencia(secuenciaInfoEmergencia);
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
        Secuencia secuenciaMonitoria = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MONITORIA_SOLICITADA), getConstanteBean().getConstante(Constantes.NULL));
        Curso curso = solicitud.getMonitoria_solicitada().getCurso();
        Collection<Seccion> secciones;
        HashMap<String, Double> notaSeccion= new HashMap<String,Double>();
        if (!solicitud.getEstadoSolicitud().equals(getConstanteBean().getConstante(Constantes.ESTADO_ASPIRANTE))/*solicitud.getEstadoSolicitud().equals(getConstanteBean().getConstante(Constantes.ESTADO_PENDIENTE_REGISTRO_CONVENIO))
                || solicitud.getEstadoSolicitud().equals(getConstanteBean().getConstante(Constantes.ESTADO_PENDIENTE_VERIFICACION_DATOS))
                || solicitud.getEstadoSolicitud().equals(getConstanteBean().getConstante(Constantes.ESTADO_PENDIENTE_FIRMA_CONVENIO_ESTUDIANTE))
                || solicitud.getEstadoSolicitud().equals(getConstanteBean().getConstante(Constantes.ESTADO_PENDIENTE_FIRMA_CONVENIO_DEPARTAMENTO))
                || solicitud.getEstadoSolicitud().equals(getConstanteBean().getConstante(Constantes.ESTADO_PENDIENTE_RADICACION))
                || solicitud.getEstadoSolicitud().equals(getConstanteBean().getConstante(Constantes.ESTADO_ASIGNADO))*/
                ) {
            Iterator<MonitoriaAceptada> monitorias = solicitud.getMonitorias().iterator();
            secciones = new Vector<Seccion>();
            while (monitorias.hasNext()) {
                Seccion s = monitorias.next().getSeccion();
                secciones.add(s);
            }

            Secuencia secTipo = null;
            Secuencia secCarga = null;
            Collection<MonitoriaAceptada> listaMonitorias = solicitud.getMonitorias();
            if(listaMonitorias.size() == 1)
            {
                MonitoriaAceptada mon = listaMonitorias.iterator().next();
                
                
                if(mon.getCarga() == 2)
                {
                    secTipo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_MONITORIA), getConstanteBean().getConstante(Constantes.VAL_TIPO_MONITORIA_T1));
                    secCarga = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGA_MONITORIA), getConstanteBean().getConstante(Constantes.VAL_CARGA_MONITORIA_COMPLETA));

                }
                else
                {
                    secTipo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_MONITORIA), getConstanteBean().getConstante(Constantes.VAL_TIPO_MONITORIA_T2));
                    secCarga = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGA_MONITORIA), getConstanteBean().getConstante(Constantes.VAL_CARGA_MONITORIA_MEDIA));

                }
            }
            else if(listaMonitorias.size() == 2)
            {
                secTipo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_MONITORIA), getConstanteBean().getConstante(Constantes.VAL_TIPO_MONITORIA_T2));
                secCarga = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGA_MONITORIA), getConstanteBean().getConstante(Constantes.VAL_CARGA_MONITORIA_COMPLETA));
            }
            secuenciaSolicitud.agregarSecuencia(secTipo);
            secuenciaMonitoria.agregarSecuencia(secCarga);
            for (MonitoriaAceptada monitoriaAceptada : listaMonitorias) {
                notaSeccion.put(monitoriaAceptada.getSeccion().getCrn(), monitoriaAceptada.getNota());
            }
        } else {

            secciones = new ArrayList();
        }

        Secuencia secuenciaCurso = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO), getConstanteBean().getConstante(Constantes.NULL));
        secuenciaCurso.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO), curso.getCodigo()));
        secuenciaCurso.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CURSO), curso.getNombre()));
        if (!secciones.isEmpty()) {
            Iterator<Seccion> iterator = secciones.iterator();
            Secuencia secuenciaSecciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCIONES), getConstanteBean().getConstante(Constantes.NULL));
            while (iterator.hasNext()) {
                Seccion seccion = iterator.next();
                Secuencia secuenciaSeccion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCION), getConstanteBean().getConstante(Constantes.NULL));
                Secuencia secuenciaProfesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR), getConstanteBean().getConstante(Constantes.NULL));
                secuenciaProfesor.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_COMPLETO), seccion.getProfesorPrincipal().getPersona().getNombres() + " " + seccion.getProfesorPrincipal().getPersona().getApellidos()));
                secuenciaSeccion.agregarSecuencia(secuenciaProfesor);
                secuenciaSeccion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_SECCION), Integer.toString(seccion.getNumeroSeccion())));
                secuenciaSeccion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION), seccion.getCrn()));
                String strNota;
                Double nota = notaSeccion.get(seccion.getCrn());
                if(nota==null)
                    strNota = "0";
                else
                    strNota=nota.toString();
                secuenciaSeccion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA), strNota));
                Secuencia secuenciaHorario = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HORARIO), getConstanteBean().getConstante(Constantes.NULL));
                Collection<Sesion> sesiones = seccion.getHorarios();
                secuenciaHorario.agregarSecuencia(
                        new ConversorServiciosSoporteProcesos().pasarSesionesASecuencia(sesiones));
                secuenciaSeccion.agregarSecuencia(secuenciaHorario);
                secuenciaSecciones.agregarSecuencia(secuenciaSeccion);
                secuenciaCurso.agregarSecuencia(secuenciaSecciones);
            }
        }
        double notaD = solicitud.getMonitoria_solicitada().getNota_materia();
        String nota = "";
        if (notaD == -1) {
            nota = getConstanteBean().getConstante(Constantes.NO_APLICA);
        } else {
            nota = Double.toString(notaD);
        }
        Secuencia secuenciaNota = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA_MATERIA), nota);
        Secuencia secuenciaPeriodo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO_ACADEMICO), solicitud.getMonitoria_solicitada().getPeriodoAcademicoEnQueSeVio());
        Secuencia secuenciaProfesorConQuienLaVio = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR_CON_QUIEN_LA_VIO), solicitud.getMonitoria_solicitada().getProfesorConQuienLaVio());
        secuenciaMonitoria.agregarSecuencia(secuenciaCurso);
        secuenciaMonitoria.agregarSecuencia(secuenciaNota);
        secuenciaMonitoria.agregarSecuencia(secuenciaPeriodo);
        secuenciaMonitoria.agregarSecuencia(secuenciaProfesorConQuienLaVio);
        secuenciaSolicitud.agregarSecuencia(secuenciaMonitoria);

        // Monitorias historicas nuevas
        Secuencia secMonitoriasHistoricasNuevas=new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MONITORIAS_HISTORICAS_NUEVAS), getConstanteBean().getConstante(Constantes.NULL));

        Collection<MonitoriaRealizada> listaMonRealizadas = solicitud.getEstudiante().getMonitoriasRealizadas();
        for (Iterator<MonitoriaRealizada> it = listaMonRealizadas.iterator(); it.hasNext();) {
            MonitoriaRealizada monitoriaRealizada = it.next();

            String codigoCurso = monitoriaRealizada.getCodigoCurso();
            String nombreCurso = monitoriaRealizada.getNombreCurso();
            String nombreProfesor = monitoriaRealizada.getNombreProfesor();
            String notaObtenida = monitoriaRealizada.getNota();
            String periodo = monitoriaRealizada.getPeriodo();
            String tipoMonitoria = monitoriaRealizada.getTipoMonitoria();


            Secuencia secMonRealizada = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MONITORIA_HISTORICA_NUEVA), getConstanteBean().getConstante(Constantes.NULL));
            Secuencia secCurso = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO), getConstanteBean().getConstante(Constantes.NULL));
            Secuencia secCodigoCurso = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO), codigoCurso);
            Secuencia secNombreCurso = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CURSO), nombreCurso);
            Secuencia secNombreProfesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), nombreProfesor);

            secCurso.agregarSecuencia(secCodigoCurso);
            secCurso.agregarSecuencia(secNombreCurso);
            secCurso.agregarSecuencia(secNombreProfesor);

            Secuencia secNotaObtenida = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA), notaObtenida);
            Secuencia secPeriodo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO), periodo);
            Secuencia secTipoMonitoria = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_MONITORIA), tipoMonitoria);

            secMonRealizada.agregarSecuencia(secCurso);
            secMonRealizada.agregarSecuencia(secNotaObtenida);
            secMonRealizada.agregarSecuencia(secPeriodo);
            secMonRealizada.agregarSecuencia(secTipoMonitoria);

            secMonitoriasHistoricasNuevas.agregarSecuencia(secMonRealizada);
        }
        secuenciaSolicitud.agregarSecuencia(secMonitoriasHistoricasNuevas);
        return secuenciaSolicitud;
    }

    /**
     * Da secuencia de solicitud para Cupi2
     * @param solicitud
     * @return
     */
    public Secuencia darSecuenciaSolicitudCupi2(Solicitud solicitud) {
        
        Secuencia secuenciaSolicitud = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUD), getConstanteBean().getConstante(Constantes.NULL));
        secuenciaSolicitud.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_ID_SOLICITUD), Long.toString(solicitud.getId())));
        Secuencia secEstado = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO), solicitud.getEstadoSolicitud());
        secuenciaSolicitud.agregarSecuencia(secEstado);

        //Incluir información académica y personal
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

        Secuencia secuenciaInfoAcademica = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_ACADEMICA), getConstanteBean().getConstante(Constantes.NULL));
        secuenciaInfoAcademica.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE_SEGUN_CREDITOS), solicitud.getEstudiante().getEstudiante().getInformacion_Academica().getSemestreSegunCreditos()));
        secuenciaInfoAcademica.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_TOTAL), Double.toString(solicitud.getEstudiante().getEstudiante().getInformacion_Academica().getPromedioTotal())));
        secuenciaInfoAcademica.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CREDITOS_SEMESTRE_ACTUAL), Double.toString(solicitud.getEstudiante().getEstudiante().getInformacion_Academica().getCreditosSemestreActual())));
        secuenciaInfoAcademica.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CREDITOS_APROBADOS), Double.toString(solicitud.getEstudiante().getEstudiante().getInformacion_Academica().getCreditosAprobados())));
        secuenciaInfoAcademica.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CREDITOS_CURSADOS), Double.toString(solicitud.getEstudiante().getEstudiante().getInformacion_Academica().getCreditosCursados())));
        secuenciaInfoAcademica.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_ULTIMO), Double.toString(solicitud.getEstudiante().getEstudiante().getInformacion_Academica().getPromedioUltimo())));
        secuenciaInfoAcademica.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_HACE_DOS_SEMESTRES), Double.toString(solicitud.getEstudiante().getEstudiante().getInformacion_Academica().getPromedioPenultimo())));
        secuenciaInfoAcademica.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_HACE_TRES_SEMESTRES), Double.toString(solicitud.getEstudiante().getEstudiante().getInformacion_Academica().getPromedioAntepenultipo())));
        secuenciaInfoAcademica.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CREDITOS_MONITORIAS_ISIS), Double.toString(solicitud.getEstudiante().getEstudiante().getInformacion_Academica().getCreditosMonitoriasISISEsteSemestre())));

        Monitoria_Solicitada monSol=solicitud.getMonitoria_solicitada();
        Secuencia secuenciaMonSol=new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MONITORIA_SOLICITADA), "");
        Secuencia secNotaMateria = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA_MATERIA), "" + monSol.getNota_materia());
        Secuencia secPeriodoMateria = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO), monSol.getPeriodoAcademicoEnQueSeVio());
        Secuencia secProfesorConQuienVioMateria = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR_CON_QUIEN_LA_VIO), monSol.getProfesorConQuienLaVio());
        secuenciaMonSol.agregarSecuencia(secPeriodoMateria);
        secuenciaMonSol.agregarSecuencia(secProfesorConQuienVioMateria);
        secuenciaMonSol.agregarSecuencia(secNotaMateria);
        Curso curso=solicitud.getMonitoria_solicitada().getCurso();
        Secuencia secuenciaCurso = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO), getConstanteBean().getConstante(Constantes.NULL));
        secuenciaCurso.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO), curso.getCodigo()));
        secuenciaCurso.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CURSO), curso.getNombre()));
        secuenciaMonSol.agregarSecuencia(secuenciaCurso);

        secuenciaSolicitud.agregarSecuencia(secuenciaInfoPersonal);
        secuenciaSolicitud.agregarSecuencia(secuenciaInfoAcademica);
        secuenciaSolicitud.agregarSecuencia(secuenciaMonSol);
        return secuenciaSolicitud;
    }

    /**
     * Da secuencia de solicitudes sin horario.
     * @param solicitud
     * @return
     */
    public Secuencia darSecuenciaSolicitudSinHorario(Solicitud solicitud) {

        Secuencia secuenciaSolicitud = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUD), getConstanteBean().getConstante(Constantes.NULL));
        secuenciaSolicitud.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_ID_SOLICITUD), Long.toString(solicitud.getId())));
        Secuencia secEstado = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO), solicitud.getEstadoSolicitud());
        secuenciaSolicitud.agregarSecuencia(secEstado);
        Secuencia secuenciaMonitoria = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MONITORIA_SOLICITADA), getConstanteBean().getConstante(Constantes.NULL));
        Curso curso = solicitud.getMonitoria_solicitada().getCurso();
        Collection<Seccion> secciones = seccionFacade.findAll();
        Secuencia secuenciaCurso = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO), getConstanteBean().getConstante(Constantes.NULL));
        secuenciaCurso.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO), curso.getCodigo()));
        secuenciaCurso.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CURSO), curso.getNombre()));
        if (secciones.size() != 0) {
            Iterator<Seccion> iterator = secciones.iterator();
            Secuencia secuenciaSecciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCIONES), "");
            while (iterator.hasNext()) {
                Seccion seccion = iterator.next();
                Secuencia secuenciaSeccion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCION), getConstanteBean().getConstante(Constantes.NULL));
                Secuencia secuenciaProfesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR), getConstanteBean().getConstante(Constantes.NULL));
                secuenciaProfesor.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_COMPLETO), seccion.getProfesorPrincipal().getPersona().getNombres()));
                secuenciaSeccion.agregarSecuencia(secuenciaProfesor);
                secuenciaSeccion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_SECCION), Integer.toString(seccion.getNumeroSeccion())));
                secuenciaSeccion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION), seccion.getCrn()));

                secuenciaSecciones.agregarSecuencia(secuenciaSeccion);
            }
            secuenciaCurso.agregarSecuencia(secuenciaSecciones);
        }
        secuenciaMonitoria.agregarSecuencia(secuenciaCurso);
        secuenciaSolicitud.agregarSecuencia(secuenciaMonitoria);
        return secuenciaSolicitud;
    }
    
    /**
     * Retorna ConstanteBean
     * @return constanteBean ConstanteBean
     */
    private ConstanteLocal getConstanteBean() {
        return constanteBean;
    }

    Secuencia darSecuenciaSolicitudes(Collection<Solicitud> listaSolicitudes) {
        Secuencia secSolicitudes = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUDES),"");
        for(Solicitud solicitud : listaSolicitudes){
            Secuencia secSolicitud = darSecuenciaSolicitud(solicitud);
            secSolicitudes.agregarSecuencia(secSolicitud);
        }
        return secSolicitudes;
    }

    private Secuencia darSecuenciaSolicitud(Solicitud solicitud) {
        Secuencia secSolicitud = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUD), "");
        if(solicitud.getId() != null){
            secSolicitud.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), solicitud.getId().toString()));
        }
        if(solicitud.getFechaCreacion() != null){
            secSolicitud.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_CREACION), solicitud.getFechaCreacion().toString()));
        }
        if(solicitud.getEstadoSolicitud() != null){
            secSolicitud.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO), solicitud.getEstadoSolicitud()));
        }
        if(solicitud.getMonitoria_solicitada() != null){
            if(solicitud.getMonitoria_solicitada().getCurso() != null){
                Secuencia secCurso = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO), "");
                if(solicitud.getMonitoria_solicitada().getCurso().getCodigo() != null){
                    secCurso.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO), solicitud.getMonitoria_solicitada().getCurso().getCodigo()));
                }
                if(solicitud.getMonitoria_solicitada().getCurso().getNombre() != null){
                    secCurso.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CURSO), solicitud.getMonitoria_solicitada().getCurso().getNombre()));
                }
                secSolicitud.agregarSecuencia(secCurso);
            }
        }
        if(solicitud.getEstudiante() != null){
            if(solicitud.getEstudiante().getPersona() != null){
                Secuencia secEstudiante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE), "");
                if(solicitud.getEstudiante().getPersona().getNombres() != null){
                    secEstudiante.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), solicitud.getEstudiante().getPersona().getNombres()));
                }
                if(solicitud.getEstudiante().getPersona().getApellidos() != null){
                    secEstudiante.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), solicitud.getEstudiante().getPersona().getApellidos()));
                }
                if(solicitud.getEstudiante().getPersona().getCorreo() != null){
                    secEstudiante.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), solicitud.getEstudiante().getPersona().getCorreo()));
                }
                if(solicitud.getEstudiante().getPersona().getCodigo() != null){
                    secEstudiante.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_ESTUDIANTE), solicitud.getEstudiante().getPersona().getCodigo()));
                }
                secSolicitud.agregarSecuencia(secEstudiante);
            }
        }
        return secSolicitud;
    }

    public Secuencia crearSecuenciasSolicitudesResueltas(Collection<Solicitud> solicitudes) {
        Secuencia secSolicitudes = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUDES), "");

        for (Solicitud solicitud : solicitudes) {
            Secuencia secSolicitud = crearSecuenciaSolicitudResuelta(solicitud);
            secSolicitudes.agregarSecuencia(secSolicitud);
        }

        return secSolicitudes;
    }

    public Secuencia crearSecuenciaSolicitudResuelta(Solicitud solicitud) {

        Secuencia secSolicitud = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUD), "");

        if (solicitud == null) {
            return secSolicitud;
        }

        if (solicitud.getId() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), solicitud.getId().toString());
            secSolicitud.agregarSecuencia(secId);
        }

        if(solicitud.getNumeroRadicacion() != null){
            secSolicitud.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_RADICACION), solicitud.getNumeroRadicacion()));
        }

        Timestamp fechaRadicacion = solicitud.getFechaRadicacion();
        if(fechaRadicacion != null){
            secSolicitud.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_RADICACION), fechaRadicacion.toString() ));
        }

        if(solicitud.getEstudiante() != null){
            if(solicitud.getEstudiante().getPersona() != null){
                Secuencia secEstudiante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE), "");
                if(solicitud.getEstudiante().getPersona().getNombres() != null){
                    secEstudiante.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), solicitud.getEstudiante().getPersona().getNombres()));
                }
                if(solicitud.getEstudiante().getPersona().getApellidos() != null){
                    secEstudiante.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), solicitud.getEstudiante().getPersona().getApellidos()));
                }
                if(solicitud.getEstudiante().getPersona().getCorreo() != null){
                    secEstudiante.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), solicitud.getEstudiante().getPersona().getCorreo()));
                }
                if(solicitud.getEstudiante().getPersona().getCodigo() != null){
                    secEstudiante.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_ESTUDIANTE), solicitud.getEstudiante().getPersona().getCodigo()));
                }
                secSolicitud.agregarSecuencia(secEstudiante);
            }
        }

        if(solicitud.getMonitoria_solicitada() != null){
            if(solicitud.getMonitoria_solicitada().getCurso() != null){
                Secuencia secCurso = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO), "");
                if(solicitud.getMonitoria_solicitada().getCurso().getCodigo() != null){
                    secCurso.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO), solicitud.getMonitoria_solicitada().getCurso().getCodigo()));
                }
                if(solicitud.getMonitoria_solicitada().getCurso().getNombre() != null){
                    secCurso.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CURSO), solicitud.getMonitoria_solicitada().getCurso().getNombre()));
                }
                secSolicitud.agregarSecuencia(secCurso);
            }
        }

        if(solicitud.getMonitorias() != null){
            Secuencia secSecciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCIONES), "");

            for(MonitoriaAceptada m : solicitud.getMonitorias()){
                Secuencia secSeccion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCION), "");

                if(m.getSeccion().getCrn() != null){
                    secSeccion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION), m.getSeccion().getCrn()));
                }

                secSecciones.agregarSecuencia(secSeccion);
            }

            secSolicitud.agregarSecuencia(secSecciones);
        }

        return secSolicitud;
    }



}
