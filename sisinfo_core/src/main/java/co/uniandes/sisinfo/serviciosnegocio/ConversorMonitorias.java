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
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.naming.NamingException;

import co.uniandes.sisinfo.bo.excepciones.InformacionAcademicaException;
import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.entities.Aspirante;
import co.uniandes.sisinfo.entities.Horario_Disponible;
import co.uniandes.sisinfo.entities.MonitoriaAceptada;
import co.uniandes.sisinfo.entities.MonitoriaOtroDepartamento;
import co.uniandes.sisinfo.entities.MonitoriaRealizada;
import co.uniandes.sisinfo.entities.Monitoria_Solicitada;
import co.uniandes.sisinfo.entities.Solicitud;
import co.uniandes.sisinfo.entities.datosmaestros.Curso;
import co.uniandes.sisinfo.entities.datosmaestros.Estudiante;
import co.uniandes.sisinfo.entities.datosmaestros.InformacionAcademica;
import co.uniandes.sisinfo.entities.datosmaestros.NivelFormacion;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import co.uniandes.sisinfo.entities.datosmaestros.soporte.Pais;
import co.uniandes.sisinfo.entities.datosmaestros.soporte.TipoCuenta;
import co.uniandes.sisinfo.entities.datosmaestros.soporte.TipoDocumento;
import co.uniandes.sisinfo.serviciosfuncionales.AspiranteFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.Horario_DisponibleFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.MonitoriaOtroDepartamentoFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.CursoFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.EstudianteFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.InformacionAcademicaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.NivelFormacionFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.PersonaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.ProgramaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Atributo;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import co.uniandes.sisinfo.serviciosfuncionales.seguridad.AccesoLDAP;
import co.uniandes.sisinfo.serviciosfuncionales.soporte.PaisFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.soporte.TipoCuentaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.soporte.TipoDocumentoFacadeRemote;

/**
 *
 * @author Juan Manuel Moreno B.
 */
public class ConversorMonitorias {

    /**
     * Parser
     */
    private ParserT parser;

    /**
     *  ConstanteBean
     */
    @EJB
    private ConstanteRemote constanteBean;

    /**
     * AspiranteFacade
     */
    @EJB
    private AspiranteFacadeRemote aspiranteFacade;
    
    /**
     * TipoCuentaFacade
     */
    @EJB
    private TipoCuentaFacadeRemote tipoCuentaFacade;

    /**
     * PersonaFacade
     */
    @EJB
    private PersonaFacadeRemote personaFacade;

    /**
     * EstudianteFacade
     */
    @EJB
    private EstudianteFacadeRemote estudianteFacade;

    /**
     * TipoDocumentoFacade
     */
    @EJB
    private TipoDocumentoFacadeRemote tipoDocumentoFacade;

    /**
     * PaisFacade
     */
    @EJB
    private PaisFacadeRemote paisFacade;

    /**
     * ProgramaFacade
     */
    @EJB
    private ProgramaFacadeRemote programaFacade;

    /**
     * CursoFacade
     */
    @EJB
    private CursoFacadeRemote cursoFacade;

    /**
     * MonitoriaOtroDepartamentoFacade
     */
    @EJB
    private MonitoriaOtroDepartamentoFacadeRemote monitoriaOtrosDepartamentosFacade;

    /**
     * Informacion_AcademicaFacade
     */
    @EJB
    private InformacionAcademicaFacadeRemote informacionAcademicaFacade;

    /**
     * Horario_DisponibleFacade
     */
    @EJB
    private Horario_DisponibleFacadeRemote horario_DisponibleFacade;

    /**
     * NivelFormacionFacade
     */
    @EJB
    private NivelFormacionFacadeRemote nivelFormacionFacade;

    /**
     * Retorna Parser
     * @return parser Parser
     */
    private ParserT getParser() {
        if (parser == null) {
            parser = new ParserT();
        }
        return parser;
    }

    /**
     * Retorna ConstanteBean
     * @return constanteBean ConstanteBean
     */
    private ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    private TipoCuentaFacadeRemote getTipoCuentaFacade() {

        return tipoCuentaFacade;
    }

    private PersonaFacadeRemote getPersonaFacade() {

        return personaFacade;
    }

    private EstudianteFacadeRemote getEstudianteFacade() {
        
        return estudianteFacade;
    }

    /**
     * Retorna TipoDocumentoFacade
     * @return tipoDocumentoFacade TipoDocumentoFacade
     */
    private TipoDocumentoFacadeRemote getTipoDocumentoFacade() {
        
        return tipoDocumentoFacade;
    }

    /**
     * Retorna PaisFacade
     * @return paisFacade PaisFacade
     */
    private PaisFacadeRemote getPaisFacade() {
        
        return paisFacade;
    }

    /**
     * Retorna ProgramaFacade
     * @return programaFacade ProgramaFacade
     */
    private ProgramaFacadeRemote getProgramaFacade() {
        
        return programaFacade;
    }

    /**
     * Retorna CursoFacade
     * @return cursoFacade CursoFacade
     */
    private CursoFacadeRemote getCursoFacade() {
        
        return cursoFacade;
    }

    /**
     * Retorna MonitoriaOtroDepartamentoFacade
     * @return monitoriaOtrosDepartamentosFacade MonitoriaOtroDepartamentoFacade
     */
    private MonitoriaOtroDepartamentoFacadeRemote getMonitoriaOtrosDepartamentosFacade() {
        
        return monitoriaOtrosDepartamentosFacade;
    }

    /**
     * Retorna Informacion_AcademicaFacade
     * @return informacion_AcademicaFacade Informacion_AcademicaFacade
     */
    private InformacionAcademicaFacadeRemote getInformacionAcademicaFacade() {

        return informacionAcademicaFacade;
    }

    /**
     * Retorna Horario_DisponibleFacade
     * @return horario_DisponibleFacade Horario_DisponibleFacade
     */
    private Horario_DisponibleFacadeRemote getHorario_DisponibleFacade() {
        
        return horario_DisponibleFacade;
    }
    
    /**
     * Dado un String, si no es nulo, retorna vacio, si no, retorna contenido.
     */
    public String returnNoNull(String contents) {

        return contents == null ? "" : contents;
    }

    /**
     * Constructor.
     */
    public ConversorMonitorias() {
        try {

            ServiceLocator serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            tipoDocumentoFacade = (TipoDocumentoFacadeRemote) serviceLocator.getRemoteEJB(TipoDocumentoFacadeRemote.class);
            personaFacade = (PersonaFacadeRemote) serviceLocator.getRemoteEJB(PersonaFacadeRemote.class);
            estudianteFacade = (EstudianteFacadeRemote) serviceLocator.getRemoteEJB(EstudianteFacadeRemote.class);
            tipoCuentaFacade = (TipoCuentaFacadeRemote) serviceLocator.getRemoteEJB(TipoCuentaFacadeRemote.class);
            paisFacade = (PaisFacadeRemote) serviceLocator.getRemoteEJB(PaisFacadeRemote.class);
            programaFacade = (ProgramaFacadeRemote) serviceLocator.getRemoteEJB(ProgramaFacadeRemote.class);
            informacionAcademicaFacade = (InformacionAcademicaFacadeRemote) serviceLocator.getRemoteEJB(InformacionAcademicaFacadeRemote.class);
            cursoFacade = (CursoFacadeRemote) serviceLocator.getRemoteEJB(CursoFacadeRemote.class);
            nivelFormacionFacade = (NivelFormacionFacadeRemote) serviceLocator.getRemoteEJB(NivelFormacionFacadeRemote.class);
            aspiranteFacade = (AspiranteFacadeRemote) serviceLocator.getRemoteEJB(AspiranteFacadeRemote.class);
            monitoriaOtrosDepartamentosFacade = (MonitoriaOtroDepartamentoFacadeRemote) serviceLocator.getRemoteEJB(MonitoriaOtroDepartamentoFacadeRemote.class);
            horario_DisponibleFacade = (Horario_DisponibleFacadeRemote) serviceLocator.getRemoteEJB(Horario_DisponibleFacadeRemote.class);
        } catch (NamingException ex) {
            
            ex.printStackTrace();
            Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Pasa un comando a aspirante de solicitud.
     * @return
     */
    public Aspirante pasarComandoAAspirante(String xml) throws Exception {

        getParser().leerXML(xml);
        Secuencia seqPersonal =
                getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_PERSONAL));
        String correo =
                seqPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
        Aspirante aspirante = aspiranteFacade.findByCorreo(correo);
        Secuencia seqNivelFormacion =
                getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_FORMACION));
        Persona persona = null; // Persona.
        String codigo = AccesoLDAP.obtenerCodigo(correo.split("@")[0]);
        Estudiante estudiante = null; // Estudiante.
        if (aspirante != null) { // El aspirante existe.

            estudiante = aspirante.getEstudiante();
        } else {

            estudiante = getEstudianteFacade().findByCorreo(correo);
            aspirante = new Aspirante();
            aspirante.setEstudiante(estudiante);
        }
        persona = estudiante.getPersona();
        NivelFormacion nivel = nivelFormacionFacade.findByName(seqNivelFormacion.getValor());
        estudiante.setTipoEstudiante(nivel);

        // Actualiza campos de persona y estudiante.
        Collection<Secuencia> collTagPersonal = seqPersonal.getSecuencias();
        for (Secuencia tag : collTagPersonal) {

            if (tag.getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES))) {

                persona.setNombres(returnNoNull(tag.getValor()));
            } else if (tag.getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS))) {

                persona.setApellidos(returnNoNull(tag.getValor()));
            } else if (tag.getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO))) {

                persona.setCorreo(returnNoNull(tag.getValor()));
            } else if (tag.getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_NACIMIENTO))) {

                SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date d = sdfHMS.parse(tag.getValor());
                Timestamp time = new Timestamp(d.getTime());
                persona.setFechaNacimiento(time);

            } else if (tag.getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_DOCUMENTO))) {

                persona.setNumDocumentoIdentidad(returnNoNull(tag.getValor()));
            } else if (tag.getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_DOCUMENTO))) {

                TipoDocumento tipoDocumento = getTipoDocumentoFacade().findByDescripcion(tag.getValor());
                persona.setTipoDocumento(tipoDocumento);
            } else if (tag.getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_DIRECCION_RESIDENCIA))) {

                persona.setDireccionResidencia(returnNoNull(tag.getValor()));
            } else if (tag.getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_PAIS))) {

                Pais pais = getPaisFacade().findByNombre(tag.getValor());
                persona.setPais(pais);
            } else if (tag.getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_LUGAR_NACIMIENTO))) {

                persona.setCiudadNacimiento(returnNoNull(tag.getValor()));
            } else if (tag.getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_TELEFONO_RESIDENCIA))) {

                persona.setTelefono(returnNoNull(tag.getValor()));
            } else if (tag.getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_CELULAR))) {

                persona.setCelular(returnNoNull(tag.getValor()));
            } else if (tag.getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_BANCO))) {

                estudiante.setBanco(returnNoNull(tag.getValor()));
            } else if (tag.getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_CUENTA))) {

                estudiante.setCuentaBancaria(returnNoNull(tag.getValor()));
            } else if (tag.getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CUENTA))) {

                List<TipoCuenta> tp = getTipoCuentaFacade().findByName(tag.getValor());
                if (!tp.isEmpty())
                    estudiante.setTipoCuenta(tp.get(0));
            }
        }

        // Facultad.
        Secuencia secFacultad =
                getParser().obtenerSecuencia(
                    getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_PERSONAL)).obtenerPrimeraSecuencia(
                        getConstanteBean().getConstante(Constantes.TAG_PARAM_FACULTAD));
        Secuencia secPrograma = secFacultad.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROGRAMA_ACADEMICO));
        estudiante.setPrograma(getProgramaFacade().findByNombre(secPrograma.getValor()));

        // Informacion emergencia.
        Secuencia secInformacionEmergencia = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_EMERGENCIA));
        String nombresEmergencia = secInformacionEmergencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES)).getValor();
        estudiante.setAvisarEmergenciaNombres(nombresEmergencia);
        String apellidosEmergencia = secInformacionEmergencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS)).getValor();
        estudiante.setAvisarEmergenciaApellidos(apellidosEmergencia);
        String telefonoEmergencia = secInformacionEmergencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TELEFONO_EMERGENCIA)).getValor();
        estudiante.setTelefonoEmergencia(telefonoEmergencia);

        // Monitorias otros departamentos.
        aspirante.setMonitoriasOtrosDepartamentos(crearMonitoriasOtrosDepartamentos(codigo));
        estudiante.setInformacion_Academica(crearInformacionAcademica(codigo));
        //estudiante.setTipoEstudiante(nivelFormacionFacade.findByName(Constantes.TAG_PARAM_NIVEL_PREGRADO));

        // Horario.
        aspirante.setHorario_disponible(crearHorarioDisponible(codigo));

        // Monitorias otras.
        Secuencia secMonitoriasNuevas =
                getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MONITORIAS_HISTORICAS_NUEVAS));
        List<MonitoriaRealizada> listRealizadas = new ArrayList<MonitoriaRealizada>();
        for (Secuencia secTmp : secMonitoriasNuevas.getSecuencias()) {

            // Pasamos curso a monitoria realizada.
            MonitoriaRealizada realizada = new MonitoriaRealizada();
            Secuencia secCodigoCurso = secTmp.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO));
            if(secCodigoCurso != null) {

                realizada.setCodigoCurso(secCodigoCurso.getValor());
            }

            Secuencia secNombreCurso = secTmp.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CURSO));
            if(secNombreCurso != null) {

                realizada.setNombreCurso(secNombreCurso.getValor());
            }

            Secuencia secNombreProfe = secTmp.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES));
            if(secNombreProfe != null) {

                realizada.setNombreProfesor(secNombreProfe.getValor());
            }

            Secuencia secPeriodo = secTmp.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO));
            if(secPeriodo != null) {

                realizada.setPeriodo(secPeriodo.getValor());
            }

            Secuencia secNota = secTmp.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA));
            if(secNota != null) {

                realizada.setNota(secNota.getValor());
            }

            Secuencia secTipoMonitoria = secTmp.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_MONITORIA));
            if(secTipoMonitoria != null) {

                realizada.setTipoMonitoria(secTipoMonitoria.getValor());
            }
            listRealizadas.add(realizada);
        }
        aspirante.setMonitoriasRealizadas(listRealizadas);
        return aspirante;
    }

    /**
     * Pasa comando a monitoria solicitada.
     * @return
     */
    public Monitoria_Solicitada pasarComandoAMonitoriaSolicitada(String xml) throws Exception {
        Monitoria_Solicitada monitoriaSolicitada = new Monitoria_Solicitada();

        getParser().leerXML(xml);
        Secuencia secMonitoriasolicitada = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MONITORIA_SOLICITADA));
        Secuencia secuenciaCurso = secMonitoriasolicitada.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO));

        // Curso.
        Curso curso = getCursoFacade().findByCodigo(secuenciaCurso.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO)).getValor());
        monitoriaSolicitada.setCurso(curso);

        // Curso visto.
        Secuencia secuenciaCursoVisto = secMonitoriasolicitada.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO_VISTO));
        Secuencia secuenciaNota = secuenciaCursoVisto.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA_MATERIA));
        try{
            monitoriaSolicitada.setNota_materia(Double.parseDouble(secuenciaNota.getValor()));
        }catch(NumberFormatException e){
            monitoriaSolicitada.setNota_materia(-1);
        }

        // Otros.
        Secuencia secuenciaPeriodo = secuenciaCursoVisto.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO_ACADEMICO));
        monitoriaSolicitada.setPeriodoAcademicoEnQueSeVio(secuenciaPeriodo.getValor());
        Secuencia profesor = secMonitoriasolicitada.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR)).obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_COMPLETO));
        String nombreProfesor = profesor.getValor();
        monitoriaSolicitada.setProfesorConQuienLaVio(nombreProfesor);

        return monitoriaSolicitada;
    }

    /**
     * Paasa comando a entity de Solicitud.
     * @return
     */
    public Solicitud pasarComandoASolicitud(String xml) throws Exception {
        Solicitud solicitud = new Solicitud();

        solicitud.setEstadoSolicitud(getConstanteBean().getConstante(Constantes.ESTADO_ASPIRANTE));
        Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
        solicitud.setFechaCreacion(fechaActual);
        solicitud.setMonitorias(new LinkedList<MonitoriaAceptada>());
        getParser().leerXML(xml); // Lee para comenzar a extraer secuencias.
        
        return solicitud;
    }

    /**
     * Metodo que instancia/crea monitorias en otros departamentos para un estudiante determinado
     * @param codigo - Codigo del estudiante
     * @return MonitoriaOtroDepartamento: Monitoria en otro departamento
     * TODO: Logica de negocio esta entre comentarios. Ver que hacer.
     */
    public Collection<MonitoriaOtroDepartamento> crearMonitoriasOtrosDepartamentos(String codigo) {
        Collection<MonitoriaOtroDepartamento> collMonitoriasOtros = new ArrayList<MonitoriaOtroDepartamento>();
        
        Secuencia seqMonitoriasOtros =
                getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MONITORIAS_OTRO_DEPTOS));
        Collection<Secuencia> seqsMonitoriasOtrosDeptos = seqMonitoriasOtros.getSecuencias();
        for (Secuencia secuencia : seqsMonitoriasOtrosDeptos) {

            if (secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO)).getValor().trim().equals("")) 
                continue;

            /*MonitoriaOtroDepartamento monitoriaOtro =
                getMonitoriaOtrosDepartamentosFacade().findByCodigoEstudianteAndCodigoCurso(codigo, secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO)).getValor());
            if (monitoriaOtro == null) {

                monitoriaOtro = new MonitoriaOtroDepartamento();
            }*/
            MonitoriaOtroDepartamento monitoriaOtro = new MonitoriaOtroDepartamento();
            monitoriaOtro.setCodigoCurso(
                    secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO)).getValor());
            monitoriaOtro.setNombreCurso(
                    secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CURSO)).getValor());
            monitoriaOtro.setNombreProfesor(
                    secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR)).obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_COMPLETO)).getValor());
            monitoriaOtro.setPeriodo(
                    secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO_ACADEMICO)).getValor());
            //getMonitoriaOtrosDepartamentosFacade().edit(monitoriaOtro);
            collMonitoriasOtros.add(monitoriaOtro);
        }
        return collMonitoriasOtros;
    }

    /**
     * Metodo que instancia un horario disponible para un aspirante dado su codigo.
     * Si el horario no existe crea uno nuevo
     * @param codigo - codigo del aspirante al cual se le pedira su horario
     * @return Horario_Disponible: horario del aspirante
     * TODO: Logica de negocio esta entre comentarios. Ver que hacer.
     */
     public Horario_Disponible crearHorarioDisponible(String codigo) {
        //Horario_Disponible horarioDisponible = getHorario_DisponibleFacade().findByCodigoEstudiante(codigo);
        //if (horarioDisponible == null) {

        Horario_Disponible horarioDisponible = new Horario_Disponible();
        ConversorServiciosSoporteProcesos conversorServiciosSoporte = new ConversorServiciosSoporteProcesos();
        horarioDisponible.setDias_disponibles(
                conversorServiciosSoporte.pasarSecuenciaADiasCompletos(
                    getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HORARIO))));
        //getHorario_DisponibleFacade().edit(horarioDisponible);
        return horarioDisponible;
    }

    /**
     * Metodo que instancia la informacion academica de un aspirante dado su codigo.
     * Si esta infomracion no existe la a partir del archivo del comando de cracion de solicitud
     * @param codigo - codigo del aspirante al cual se le pedira su informacion academica
     * @return Informacion_Academica: informacion academica del aspirante
     * TODO: Logica de negocio esta entre comentarios. Ver que hacer.
     */
    public InformacionAcademica crearInformacionAcademica(String codigo) throws InformacionAcademicaException {
        
        InformacionAcademica informacionAcademica = new InformacionAcademica();
        Secuencia seqInformacionAcademica =
                getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_ACADEMICA));
        for (Secuencia secuencia : seqInformacionAcademica.getSecuencias()) {

            if (secuencia.getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE_SEGUN_CREDITOS))) {

                informacionAcademica.setSemestreSegunCreditos(secuencia.getValor());
            } else if (secuencia.getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_TOTAL))) {

                informacionAcademica.setPromedioTotal(Double.parseDouble(secuencia.getValor()));
            } else if (secuencia.getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_CREDITOS_SEMESTRE_ACTUAL))) {

                informacionAcademica.setCreditosSemestreActual(Double.parseDouble(secuencia.getValor()));
            } else if (secuencia.getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_CREDITOS_APROBADOS))) {

                informacionAcademica.setCreditosAprobados(Double.parseDouble(secuencia.getValor()));
            } else if (secuencia.getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_CREDITOS_CURSADOS))) {

                informacionAcademica.setCreditosCursados(Double.parseDouble(secuencia.getValor()));
            } else if (secuencia.getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_ULTIMO))) {

                informacionAcademica.setPromedioUltimo(Double.parseDouble(secuencia.getValor()));
            } else if (secuencia.getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_HACE_DOS_SEMESTRES))) {

                informacionAcademica.setPromedioPenultimo(Double.parseDouble(secuencia.getValor()));
            } else if (secuencia.getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_HACE_TRES_SEMESTRES))) {
                
                informacionAcademica.setPromedioAntepenultipo(Double.parseDouble(secuencia.getValor()));
            } else if (secuencia.getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_FORMACION))) {

                informacionAcademica.setNivelEstudios(secuencia.getValor());
            }
        }
        
        if(informacionAcademica.getCreditosMonitoriasISISEsteSemestre() == null) // Verifica que el campo de creditosMonitoriasISISEsteSemestre
            informacionAcademica.setCreditosMonitoriasISISEsteSemestre(new Double(0));
        //getInformacion_AcademicaFacade().edit(informacionAcademica);

        return informacionAcademica;
    }

    /**
     * Crea un contenedor de secuencia de parametros.
     * @return
     */
    public Collection<Secuencia> crearParametrosMensaje(String nombres, String apellidos, String monitoria) throws Exception {
        Collection<Secuencia> parametros = new LinkedList<Secuencia>();

        Secuencia secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO), nombres);
        Atributo atrParametro = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_NOMBRES_ESTUDIANTE);
        secParametro.agregarAtributo(atrParametro);
        parametros.add(secParametro);
        secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO), apellidos);
        atrParametro = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_APELLIDOS_ESTUDIANTE);
        secParametro.agregarAtributo(atrParametro);
        parametros.add(secParametro);
        secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO), monitoria);
        atrParametro = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_NOMBRE_CURSO);
        secParametro.agregarAtributo(atrParametro);
        parametros.add(secParametro);
        return parametros;
    }

    /**
     * Informa si tiene permisos especiales dado el comando.
     */
    public boolean tienePermisosEspeciales(String xml) {
        boolean tiene = false;
        
        try {
            
            getParser().leerXML(xml);
            Secuencia seqTiene =
                getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MONITORIAS_TIENE_PERMISOS_ESPECIALES));
            if (seqTiene != null) {

                tiene = Boolean.parseBoolean(seqTiene.getValor());
            }
        } catch (Exception ex) {}
        return tiene;
    }

    /**
     * Pasa de aspirante a secuencia.
     */
    public Secuencia pasarAspiranteASecuencia(Aspirante aspirante) {

        Estudiante estudiante = aspirante.getEstudiante();
        Persona persona = estudiante.getPersona();

        // Principal.
        Secuencia seqAspirante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_PERSONAL), "");
        // Nombres.
        seqAspirante.agregarSecuencia(
                new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), persona.getNombres()));
        // Apellidos.
        seqAspirante.agregarSecuencia(
                new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), persona.getApellidos()));
        // Codigo.
        seqAspirante.agregarSecuencia(
                new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_ESTUDIANTE), persona.getCodigo()));
        // Correo.
        seqAspirante.agregarSecuencia(
                new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), persona.getCorreo()));
        
        // Facultad.
        Secuencia seqInfoPersonal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FACULTAD), null);
        seqInfoPersonal.agregarAtributo(
                new Atributo(getConstanteBean().getConstante(Constantes.ATR_NOMBRE_FACULTAD),
                    estudiante.getPrograma().getFacultad().getNombre()));
        seqInfoPersonal.agregarSecuencia(
                new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROGRAMA_ACADEMICO), estudiante.getPrograma().getNombre()));
        seqAspirante.agregarSecuencia(seqInfoPersonal);
        
        // Fecha nacimiento.
        seqAspirante.agregarSecuencia(
                new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_NACIMIENTO), persona.getFechaNacimiento().toString()));
        // Documento.
        seqAspirante.agregarSecuencia(
                new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DOCUMENTO), persona.getNumDocumentoIdentidad()));
        // Tipo documento.
        seqAspirante.agregarSecuencia(
                new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_DOCUMENTO), persona.getTipoDocumento().getDescripcion()));
        // Direccion residencia.
        seqAspirante.agregarSecuencia(
                new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DIRECCION_RESIDENCIA), persona.getDireccionResidencia()));
        // Pais.
        seqAspirante.agregarSecuencia(
                new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PAIS), persona.getPais().getNombre()));
        // Tel. residencia.
        seqAspirante.agregarSecuencia(
                    new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TELEFONO_RESIDENCIA), persona.getTelefono()));
        // Lugar nacimiento.
        seqAspirante.agregarSecuencia(
                    new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_LUGAR_NACIMIENTO), persona.getCiudadNacimiento()));
        // Celular.
        seqAspirante.agregarSecuencia(
                    new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CELULAR), persona.getCelular()));
        // Banco.
        seqAspirante.agregarSecuencia(
                    new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_BANCO), estudiante.getBanco()));
        // Cuenta.
        seqAspirante.agregarSecuencia(
                    new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CUENTA), estudiante.getCuentaBancaria()));
        // Tipo cuenta.
        seqAspirante.agregarSecuencia(
                    new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CUENTA), estudiante.getTipoCuenta().getNombre()));

        // Horario.
        seqAspirante.agregarSecuencia(
                new ConversorServiciosSoporteProcesos().pasarDiasCompletosASecuencia(aspirante.getHorario_disponible().getDias_disponibles()));
        return seqAspirante;
    }

    /**
     * Pasa de informacion emergencia a secuencia.
     */
    public Secuencia pasarInformacionEmergenciaASecuencia(Estudiante estudiante) {
        Secuencia seqInfoEmergencia = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_EMERGENCIA), null);
        
        // Avisar nombres.
        seqInfoEmergencia.agregarSecuencia(
                new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), estudiante.getAvisarEmergenciaNombres()));
        // Avisar apellidos.
        seqInfoEmergencia.agregarSecuencia(
                new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), estudiante.getAvisarEmergenciaApellidos()));
        // Telefono emergencia.
        seqInfoEmergencia.agregarSecuencia(
                new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TELEFONO_EMERGENCIA), estudiante.getTelefonoEmergencia()));
        return seqInfoEmergencia;
    }

    /**
     * Pasa de monitoria solicitada a secuencia
     */
    private Secuencia pasarMonitoriaSolicitadaASecuencia(Monitoria_Solicitada monitoriaSolicitada) {
        Secuencia seqMonitoriaSolicitada = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MONITORIA_SOLICITADA), "");

        // Curso.
        Secuencia seqInfoMonitoriaSolicitada = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO), null);
        Curso curso = monitoriaSolicitada.getCurso();
        // Codigo.
        seqInfoMonitoriaSolicitada.agregarSecuencia(
                    new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO), monitoriaSolicitada.getCurso().getCodigo()));
        // Nombre
        seqInfoMonitoriaSolicitada.agregarSecuencia(
                    new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CURSO), monitoriaSolicitada.getCurso().getNombre()));
        // Presencial.
        seqInfoMonitoriaSolicitada.agregarSecuencia(
                    new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PRESENCIAL), Boolean.toString(monitoriaSolicitada.getCurso().isPresencial())));
        seqMonitoriaSolicitada.agregarSecuencia(seqInfoMonitoriaSolicitada);
        // Curso visto.
        seqInfoMonitoriaSolicitada = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO_VISTO), null);
        // Periodo academico.
        seqInfoMonitoriaSolicitada.agregarSecuencia(
                    new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO_ACADEMICO), monitoriaSolicitada.getPeriodoAcademicoEnQueSeVio()));
        // Profesor.
        Secuencia seqProfesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR), null);
        seqProfesor.agregarSecuencia(
                    new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_COMPLETO), monitoriaSolicitada.getProfesorConQuienLaVio()));
        seqInfoMonitoriaSolicitada.agregarSecuencia(seqProfesor);
        seqMonitoriaSolicitada.agregarSecuencia(seqInfoMonitoriaSolicitada);
        
        return seqMonitoriaSolicitada;
    }

    /**
     * Pasa de otras monitorías a secuencia.
     */
    private Secuencia pasarMonitoriasOtrasASecuencia(Collection<MonitoriaRealizada> monitoriasRealizadas) {
        Secuencia seqMonitoriasOtras = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MONITORIAS_HISTORICAS_NUEVAS), "");

        Secuencia seqMonitoriaOtra = null;
        for (MonitoriaRealizada realizada : monitoriasRealizadas) {

            // Nueva secuencia monitoria.
            seqMonitoriaOtra = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MONITORIA_HISTORICA_NUEVA), "");
            // Codigo curso.
            seqMonitoriaOtra.agregarSecuencia(new Secuencia(
                    getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO), realizada.getCodigoCurso()));
            // Nombre curso.
            seqMonitoriaOtra.agregarSecuencia(new Secuencia(
                    getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CURSO), realizada.getNombreCurso()));
            // Nombre profesor.
            seqMonitoriaOtra.agregarSecuencia(new Secuencia(
                    getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), realizada.getNombreProfesor()));
            // Periodo.
            seqMonitoriaOtra.agregarSecuencia(new Secuencia(
                    getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO), realizada.getPeriodo()));
            // Nota.
            seqMonitoriaOtra.agregarSecuencia(new Secuencia(
                    getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA), realizada.getNota()));
            // Tipo monitoria.
            seqMonitoriaOtra.agregarSecuencia(new Secuencia(
                    getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_MONITORIA), realizada.getTipoMonitoria()));
            // Agregamos a secuencia de monitorias.
            seqMonitoriasOtras.agregarSecuencia(seqMonitoriaOtra);
        }
        return seqMonitoriasOtras;
    }

    /**
     * Pasa de informacion academica a secuencia
     */
    public Secuencia pasarInformacionAcademicaASecuencia(InformacionAcademica informacionAcademica) {
        Secuencia seqInfoAcademica = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_ACADEMICA), "");

        // Semestre segun creditos.
        seqInfoAcademica.agregarSecuencia(new Secuencia(
            getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE_SEGUN_CREDITOS), informacionAcademica.getSemestreSegunCreditos()));
        // Promedio total.
        seqInfoAcademica.agregarSecuencia(new Secuencia(
            getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_TOTAL), informacionAcademica.getPromedioTotal().toString()));
        // Creditos semestre actual.
        seqInfoAcademica.agregarSecuencia(new Secuencia(
            getConstanteBean().getConstante(Constantes.TAG_PARAM_CREDITOS_SEMESTRE_ACTUAL), informacionAcademica.getCreditosSemestreActual().toString()));
        // Creditos aprobados.
        seqInfoAcademica.agregarSecuencia(new Secuencia(
            getConstanteBean().getConstante(Constantes.TAG_PARAM_CREDITOS_APROBADOS), informacionAcademica.getCreditosAprobados().toString()));
        // Creditos cursados.
        seqInfoAcademica.agregarSecuencia(new Secuencia(
            getConstanteBean().getConstante(Constantes.TAG_PARAM_CREDITOS_CURSADOS), informacionAcademica.getCreditosCursados().toString()));
        // Promedio ultimo.
        seqInfoAcademica.agregarSecuencia(new Secuencia(
            getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_ULTIMO), informacionAcademica.getPromedioUltimo().toString()));
        // Promedio penultimo.
        seqInfoAcademica.agregarSecuencia(new Secuencia(
            getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_HACE_DOS_SEMESTRES), informacionAcademica.getPromedioPenultimo().toString()));
        // Promedio antepenultimo.
        seqInfoAcademica.agregarSecuencia(new Secuencia(
            getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_HACE_TRES_SEMESTRES), informacionAcademica.getPromedioAntepenultipo().toString()));
        // Nivel estudios.
        seqInfoAcademica.agregarSecuencia(new Secuencia(
            getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_FORMACION), informacionAcademica.getNivelEstudios()));
        return seqInfoAcademica;
    }

    /**
     * Pasa de monitorias otros departamentos a secuencia.
     */
    public Secuencia pasarMonitoriasOtrosDepartamentosASecuencia(Collection<MonitoriaOtroDepartamento> collMonitoriasOtros) {
        Secuencia seqMonitoriasOtrosDepartamentos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MONITORIAS_OTRO_DEPTOS), "");

        Secuencia seqMonitoriaOtro = null;
        for(MonitoriaOtroDepartamento monitoriaOtro : collMonitoriasOtros) {

             // Nueva secuencia monitoria.
            seqMonitoriaOtro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MONITORIA_OTRO_DEPTO), "");
            // Codigo curso.
            seqMonitoriaOtro.agregarSecuencia(new Secuencia(
                    getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO), monitoriaOtro.getCodigoCurso()));
            // Codigo curso.
            seqMonitoriaOtro.agregarSecuencia(new Secuencia(
                    getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CURSO), monitoriaOtro.getNombreCurso()));
            // Codigo curso.
            seqMonitoriaOtro.agregarSecuencia(new Secuencia(
                    getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR), monitoriaOtro.getNombreProfesor()));
            // Codigo curso.
            seqMonitoriaOtro.agregarSecuencia(new Secuencia(
                    getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO_ACADEMICO), monitoriaOtro.getPeriodo()));
            seqMonitoriasOtrosDepartamentos.agregarSecuencia(seqMonitoriaOtro);
        }
        return seqMonitoriasOtrosDepartamentos;
    }

    public  Collection<Secuencia> generarParametros(String... parametros){
        Collection<Secuencia> params = new ArrayList<Secuencia>();
        for (String parametro : parametros) {
            Secuencia secParam = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_MENSAJE), parametro);
            params.add(secParam);
        }

        return params;
    }
}
