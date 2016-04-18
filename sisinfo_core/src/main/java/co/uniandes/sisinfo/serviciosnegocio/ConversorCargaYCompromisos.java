package co.uniandes.sisinfo.serviciosnegocio;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.entities.CargaProfesor;
import co.uniandes.sisinfo.entities.CursoPlaneado;
import co.uniandes.sisinfo.entities.DescargaProfesor;
import co.uniandes.sisinfo.entities.DireccionTesis;
import co.uniandes.sisinfo.entities.Evento;
import co.uniandes.sisinfo.entities.IntencionPublicacion;
import co.uniandes.sisinfo.entities.NivelTesis;
import co.uniandes.sisinfo.entities.OtrasActividades;
import co.uniandes.sisinfo.entities.PeriodoPlaneacion;
import co.uniandes.sisinfo.entities.PeriodoTesis;
import co.uniandes.sisinfo.entities.PeriodoTesisPregrado;
import co.uniandes.sisinfo.entities.ProyectoDeGrado;
import co.uniandes.sisinfo.entities.ProyectoFinanciado;
import co.uniandes.sisinfo.entities.Tesis1;
import co.uniandes.sisinfo.entities.Tesis2;
import co.uniandes.sisinfo.entities.TipoPublicacion;
import co.uniandes.sisinfo.entities.datosmaestros.Curso;
import co.uniandes.sisinfo.entities.datosmaestros.Estudiante;
import co.uniandes.sisinfo.entities.datosmaestros.NivelFormacion;
import co.uniandes.sisinfo.entities.datosmaestros.Seccion;
import co.uniandes.sisinfo.serviciosfuncionales.CargaProfesorFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.NivelTesisFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ProyectoDeGradoFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.Tesis1FacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.Tesis2FacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.TipoPublicacionFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.CursoFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.EstudianteFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.NivelFormacionFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.SeccionFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;

public class ConversorCargaYCompromisos {

    private SeccionFacadeRemote seccionFacadeRemote;
    private CursoFacadeRemote cursoFacadeRemote;
    private Tesis1FacadeRemote tesis1Facade;
    private Tesis2FacadeRemote tesis2Facade;
    private ProyectoDeGradoFacadeRemote proyectoDeGradoFacade;
    private NivelTesisFacadeLocal nivelTesisFacadeRemote;
    private TipoPublicacionFacadeRemote tipoPublicacionFacade;
    private NivelFormacionFacadeRemote nivelFormacionRemote;
    private EstudianteFacadeRemote estudianteFacadeRemote;
    private CargaProfesorFacadeLocal cargaProfesorFacade;
    private ConstanteRemote constanteBean;

    public ConversorCargaYCompromisos(SeccionFacadeRemote seccionFacadeRemote, CursoFacadeRemote cursoFacadeRemote, Tesis1FacadeRemote tesis1Facade,
            Tesis2FacadeRemote tesis2Facade, ProyectoDeGradoFacadeRemote proyectoDeGradoFacade, NivelTesisFacadeLocal nivelTesisFacadeRemote,
            TipoPublicacionFacadeRemote tipoPublicacionFacade, NivelFormacionFacadeRemote nivelFormacionRemote, EstudianteFacadeRemote estudianteFacadeRemote,
            CargaProfesorFacadeLocal cargaProfesorFacade, ConstanteRemote constanteBean) {

        this.seccionFacadeRemote = seccionFacadeRemote;
        this.cursoFacadeRemote = cursoFacadeRemote;
        this.tesis1Facade = tesis1Facade;
        this.tesis2Facade = tesis2Facade;
        this.proyectoDeGradoFacade = proyectoDeGradoFacade;
        this.nivelTesisFacadeRemote = nivelTesisFacadeRemote;
        this.tipoPublicacionFacade = tipoPublicacionFacade;
        this.nivelFormacionRemote = nivelFormacionRemote;
        this.estudianteFacadeRemote = estudianteFacadeRemote;
        this.cargaProfesorFacade = cargaProfesorFacade;
        this.constanteBean = constanteBean;
    }

    public Secuencia crearSecuenciaCargasProfesor(Collection<CargaProfesor> cargas) {
        Secuencia secCargas = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGA_GRUPO), "");
        for (CargaProfesor cargaProfesor : cargas) {
            Secuencia secCarga = crearSecuenciaCargaProfesor(cargaProfesor);
            secCargas.agregarSecuencia(secCarga);
        }
        return secCargas;
    }

    public Secuencia crearSecuenciaCargaProfesor(CargaProfesor carga) {
        Secuencia secPrincipalCarga = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGA_PROFESOR), "");
        Secuencia secid = carga.getId() != null
                ? new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), carga.getId().toString())
                : null;
        if (secid != null) {
            secPrincipalCarga.agregarSecuencia(secid);
        }

        Secuencia secMaximoNivelTesis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MAXIMO_NIVEL_TESIS), carga.getMaximoNivelTesis().getNombre());
        secPrincipalCarga.agregarSecuencia(secMaximoNivelTesis);
        Secuencia secPeriodo = getSecuenciaPeriodo(carga.getPeriodoPlaneacion());
        Secuencia secInfoBasica = getSecuenciaInformacionBasica(carga);
        Secuencia secInfoCursos = getSecuenciaInformacionCursos(carga);
        Secuencia secInfoPublicaciones = getSecuenciaInfoPublicaciones(carga.getIntencionPublicaciones());
        Secuencia secInfoTesiss = getSecuenciaInfoTesises(carga);
        Secuencia secProyectosFinanciados = getSecuenciaProyectosFinanciados(carga.getProyectosFinanciados());
        Secuencia secOtros = getSecuenciaOtroses(carga.getOtros());
        Secuencia secDescarga = getSecuenciaDescarga(carga.getDescarga());
        Secuencia secCarga = getSecuenciaCarga(carga);
        Secuencia secEventos = getSecuenciaEventos(carga.getEventos());

        secPrincipalCarga.agregarSecuencia(secPeriodo);
        secPrincipalCarga.agregarSecuencia(secInfoBasica);
        secPrincipalCarga.agregarSecuencia(secInfoCursos);
        secPrincipalCarga.agregarSecuencia(secInfoPublicaciones);
        secPrincipalCarga.agregarSecuencia(secInfoTesiss);
        secPrincipalCarga.agregarSecuencia(secProyectosFinanciados);
        secPrincipalCarga.agregarSecuencia(secOtros);
        secPrincipalCarga.agregarSecuencia(secDescarga);
        secPrincipalCarga.agregarSecuencia(secCarga);
        secPrincipalCarga.agregarSecuencia(secEventos);

        return secPrincipalCarga;
    }

    public Secuencia getSecuenciaCarga(CargaProfesor carga) {
        Secuencia secCarga = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGA_GRUPO), "");

        Secuencia secCargaEfectiva = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGA_EFECTIVA), carga.getCargaEfectiva().toString());
        Secuencia secCargaProfesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGA_PROFESOR), carga.getCargaProfesor().toString());
        Secuencia secNumCapitulosLibro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_CAPITULOS_LIBRO), carga.getNumCapitulosLibro().toString());
        Secuencia secNumCongresosInternacio = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_CONGRESOS_INTERNACIONALES), carga.getNumCongresosInternacio().toString());
        Secuencia secNumCongresosNacio = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_CONGRESOS_NACIONALES), carga.getNumCongresosNacionales().toString());
        Secuencia secCursosPreMas = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_CURSOS), carga.getNumCursosPregradoMaestria().toString());
        Secuencia secNumRevistas = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_REVISTAS), carga.getNumRevistas().toString());
        Secuencia secDoctorado = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_ESTUDIANTES_DOCTORADO), carga.getNumeroEstudiantesDoctorado().toString());
        Secuencia secPregrado = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_ESTUDIANTES_PREGRADO), carga.getNumeroEstudiantesPregrado().toString());
        Secuencia secTesis1 = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_ESTUDIANTES_TESIS1), carga.getNumeroEstudiantesTesis1().toString());
        Secuencia secTesis2 = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_ESTUDIANTES_TESIS2), carga.getNumeroEstudiantesTesis2().toString());
        Secuencia secTesis2Pendiente = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_ESTUDIANTES_TESIS2_PENDIENTE), carga.getNumeroEstudiantesTesis2Pendiente().toString());
        Secuencia secProyectos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_PROYECTOS_FINANCIADOS), carga.getNumeroProyectosFinanciados().toString());
        Secuencia secPublicaciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_PUBLICACIONES_PLANEADAS), carga.getNumeroPublicacionesPlaneadas().toString());

        secCarga.agregarSecuencia(secCargaEfectiva);
        secCarga.agregarSecuencia(secCargaProfesor);
        secCarga.agregarSecuencia(secNumCapitulosLibro);
        secCarga.agregarSecuencia(secNumCongresosInternacio);
        secCarga.agregarSecuencia(secNumCongresosNacio);
        secCarga.agregarSecuencia(secCursosPreMas);
        secCarga.agregarSecuencia(secNumRevistas);
        secCarga.agregarSecuencia(secDoctorado);
        secCarga.agregarSecuencia(secPregrado);
        secCarga.agregarSecuencia(secTesis1);
        secCarga.agregarSecuencia(secTesis2);
        secCarga.agregarSecuencia(secTesis2Pendiente);
        secCarga.agregarSecuencia(secProyectos);
        secCarga.agregarSecuencia(secPublicaciones);

        return secCarga;
    }

    public Secuencia getSecuenciaEventos(Collection<Evento> eventos) {
        Secuencia secEventos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_EVENTOS), "");
        for (Evento evento : eventos) {
            Secuencia secEvento = getSecuenciaEvento(evento);
            secEventos.agregarSecuencia(secEvento);
        }
        return secEventos;
    }

    public Secuencia getSecuenciaEvento(Evento evento) {
        Secuencia secEvento = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_EVENTO), "");

        Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), evento.getId().toString());
        Secuencia secNombre = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), evento.getNombre());
        Secuencia secObservaciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OBSERVACIONES), evento.getObservaciones());

        secEvento.agregarSecuencia(secId);
        secEvento.agregarSecuencia(secNombre);
        secEvento.agregarSecuencia(secObservaciones);

        return secEvento;
    }

    public Secuencia getSecuenciaPeriodo(PeriodoPlaneacion periodoPlaneacion) {
        Secuencia secPrincipalCarga = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO_PLANEACION), "");
        Secuencia secid = periodoPlaneacion.getId() != null
                ? new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), periodoPlaneacion.getId().toString())
                : null;
        String valorEstado = periodoPlaneacion.isActual() ? getConstanteBean().getConstante(Constantes.ESTADO_ABIERTA) : getConstanteBean().getConstante(Constantes.ESTADO_CERRADA);
        Secuencia secestado = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO), valorEstado);

        Secuencia secperiodo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO), periodoPlaneacion.getPeriodo());

        Secuencia secInicioFecha = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_INICIO), periodoPlaneacion.getFechaInicio().toString());
        Secuencia secFInFecha = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN), periodoPlaneacion.getFechaFin().toString());

        if (secid != null) {
            secPrincipalCarga.agregarSecuencia(secid);
        }
        secPrincipalCarga.agregarSecuencia(secestado);
        secPrincipalCarga.agregarSecuencia(secperiodo);
        secPrincipalCarga.agregarSecuencia(secInicioFecha);
        secPrincipalCarga.agregarSecuencia(secFInFecha);
        return secPrincipalCarga;
    }

    public Secuencia getSecuenciaInformacionBasica(CargaProfesor carga) {
        Secuencia secPrincipalCarga = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR), "");

        Secuencia secNombres = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), carga.getProfesor().getPersona().getNombres());
        Secuencia secApell = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), carga.getProfesor().getPersona().getApellidos());
        Secuencia secCorreo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), carga.getProfesor().getPersona().getCorreo());

        secPrincipalCarga.agregarSecuencia(secNombres);
        secPrincipalCarga.agregarSecuencia(secApell);
        secPrincipalCarga.agregarSecuencia(secCorreo);

        return secPrincipalCarga;
    }

    public Secuencia getSecuenciaInformacionCursos(CargaProfesor carga) {
        Collection<CursoPlaneado> cursos = carga.getCursos();
        Secuencia secPrincipalCarga = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_CURSOS), "");

        //Agregar cursos de monitorías de este profesor
        List<Seccion> listaSeccionesMonitorias = seccionFacadeRemote.findByCorreoProfesor(carga.getProfesor().getPersona().getCorreo());
        for (Seccion seccion : listaSeccionesMonitorias) {

            Curso c = cursoFacadeRemote.findByCRNSeccion(seccion.getCrn());
            Secuencia secCurso = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_CURSO), "");
            Secuencia secIdCurso = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), "-1");
            Secuencia secNombreCurso = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CURSO), c.getCodigo() + " - " + c.getNombre() + " Sección " + seccion.getNumeroSeccion());
            Secuencia secNivelFormacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_FORMACION), c.getNivelPrograma().getNombre());
            Secuencia secObservaciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OBSERVACIONES), "");
            Secuencia secCargaCurso = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGA_CURSO), "1");
            Secuencia secEditable = new Secuencia(getConstanteBean().getConstante(Constantes.ATR_EDITABLE), "" + false);

            secCurso.agregarSecuencia(secIdCurso);
            secCurso.agregarSecuencia(secNombreCurso);
            secCurso.agregarSecuencia(secNivelFormacion);
            secCurso.agregarSecuencia(secObservaciones);
            secCurso.agregarSecuencia(secCargaCurso);
            secCurso.agregarSecuencia(secEditable);
            secPrincipalCarga.agregarSecuencia(secCurso);
        }

        //Agregar cursos propios planeados
        for (CursoPlaneado cursoPlaneado : cursos) {
            Secuencia secCurso = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_CURSO), "");
            Secuencia secidCurso = cursoPlaneado.getId() != null
                    ? new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), cursoPlaneado.getId().toString())
                    : null;
            Secuencia secNombreCurso = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CURSO), cursoPlaneado.getNombreCurso());
            Secuencia secNivelFormacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_FORMACION), cursoPlaneado.getNivelDelCurso().getNombre());
            Secuencia secobservaciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OBSERVACIONES), cursoPlaneado.getObservaciones());
            Secuencia secCargaCurso = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGA_CURSO), cursoPlaneado.getCarga() + "");
            Secuencia secEditable = new Secuencia(getConstanteBean().getConstante(Constantes.ATR_EDITABLE), "" + true);

            if (secidCurso != null) {
                secCurso.agregarSecuencia(secidCurso);
            }
            secCurso.agregarSecuencia(secNombreCurso);
            secCurso.agregarSecuencia(secNivelFormacion);
            secCurso.agregarSecuencia(secobservaciones);
            secCurso.agregarSecuencia(secCargaCurso);
            secCurso.agregarSecuencia(secEditable);
            secPrincipalCarga.agregarSecuencia(secCurso);
        }
        return secPrincipalCarga;
    }

    public Secuencia getSecuenciaInfoTesises(CargaProfesor carga) {
        Collection<DireccionTesis> tesisAcargo = carga.getTesisAcargo();
        Secuencia secPrincipalCarga = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_TESISES), "");

        for (DireccionTesis direccionTesis : tesisAcargo) {
            Secuencia secinfoTesis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_TESIS), "");

            Secuencia secidTEsis = direccionTesis.getId() != null
                    ? new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), direccionTesis.getId().toString())
                    : null;
            Secuencia secNombreEstudiante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), direccionTesis.getAutorEstudiante().getPersona().getNombres());
            Secuencia secApellidosEstudiante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), direccionTesis.getAutorEstudiante().getPersona().getApellidos());
            Secuencia seccorreoEstudiante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), direccionTesis.getAutorEstudiante().getPersona().getCorreo());
            Secuencia secEstadoTEsis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_ESTADO_TESIS), direccionTesis.getEstadoTesis().getNivelTesis());
            Secuencia secNivelFormacionTEsis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_FORMACION_TESIS), direccionTesis.getNivelFormacionTesis().getNombre());
            Secuencia secObservaciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OBSERVACIONES), direccionTesis.getObservaciones());
            Secuencia secTitulo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TITULO), direccionTesis.getTitulo());
            Secuencia secEditable = new Secuencia(getConstanteBean().getConstante(Constantes.ATR_EDITABLE), "" + true);

            if (secidTEsis != null) {
                secinfoTesis.agregarSecuencia(secidTEsis);
            }
            secinfoTesis.agregarSecuencia(secNombreEstudiante);
            secinfoTesis.agregarSecuencia(secApellidosEstudiante);
            secinfoTesis.agregarSecuencia(seccorreoEstudiante);
            secinfoTesis.agregarSecuencia(secEstadoTEsis);
            secinfoTesis.agregarSecuencia(secNivelFormacionTEsis);
            secinfoTesis.agregarSecuencia(secObservaciones);
            secinfoTesis.agregarSecuencia(secTitulo);
            secinfoTesis.agregarSecuencia(secEditable);
            secPrincipalCarga.agregarSecuencia(secinfoTesis);
        }

        //Sacar tesis en módulo/Proceso de Tesis
        Collection<Tesis1> tesis1Profe = tesis1Facade.findByCorreoAsesor(carga.getProfesor().getPersona().getCorreo());
        for (Tesis1 tesis1 : tesis1Profe) {
            //solo agrega las tesis de este periodo
            PeriodoTesis pt = tesis1.getSemestreIniciacion();
            if (pt.getPeriodo().equals(carga.getPeriodoPlaneacion().getPeriodo())) {
                Secuencia secinfoTesis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_TESIS), "");

                Secuencia secidTEsis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), "");
                Secuencia secNombreEstudiante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), tesis1.getEstudiante().getPersona().getNombres());
                Secuencia secApellidosEstudiante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), tesis1.getEstudiante().getPersona().getApellidos());
                Secuencia seccorreoEstudiante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), tesis1.getEstudiante().getPersona().getCorreo());
                Secuencia secEstadoTEsis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_ESTADO_TESIS), "Tesis - 1");
                Secuencia secNivelFormacionTEsis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_FORMACION_TESIS), "Maestría");
                Secuencia secObservaciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OBSERVACIONES), "");
                Secuencia secTitulo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TITULO), tesis1.getTemaTesis());
                Secuencia secEditable = new Secuencia(getConstanteBean().getConstante(Constantes.ATR_EDITABLE), "" + false);

                if (secidTEsis != null) {
                    secinfoTesis.agregarSecuencia(secidTEsis);
                }
                secinfoTesis.agregarSecuencia(secNombreEstudiante);
                secinfoTesis.agregarSecuencia(secApellidosEstudiante);
                secinfoTesis.agregarSecuencia(seccorreoEstudiante);
                secinfoTesis.agregarSecuencia(secEstadoTEsis);
                secinfoTesis.agregarSecuencia(secNivelFormacionTEsis);
                secinfoTesis.agregarSecuencia(secObservaciones);
                secinfoTesis.agregarSecuencia(secTitulo);
                secinfoTesis.agregarSecuencia(secEditable);
                secPrincipalCarga.agregarSecuencia(secinfoTesis);
            }
        }

        //Tesis2
        Collection<Tesis2> tesis2Profe = tesis2Facade.findByCorreoAsesor(carga.getProfesor().getPersona().getCorreo());
        for (Tesis2 tesis2 : tesis2Profe) {

            PeriodoTesis pt = tesis2.getSemestreInicio();
            if (pt.getPeriodo().equals(carga.getPeriodoPlaneacion().getPeriodo())) {
                Secuencia secinfoTesis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_TESIS), "");

                Secuencia secidTEsis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), "");
                Secuencia secNombreEstudiante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), tesis2.getEstudiante().getPersona().getNombres());
                Secuencia secApellidosEstudiante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), tesis2.getEstudiante().getPersona().getApellidos());
                Secuencia seccorreoEstudiante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), tesis2.getEstudiante().getPersona().getCorreo());
                Secuencia secEstadoTEsis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_ESTADO_TESIS), "Tesis - 2");
                Secuencia secNivelFormacionTEsis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_FORMACION_TESIS), "Maestría");
                Secuencia secObservaciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OBSERVACIONES), "");
                Secuencia secTitulo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TITULO), tesis2.getTemaProyecto());
                Secuencia secEditable = new Secuencia(getConstanteBean().getConstante(Constantes.ATR_EDITABLE), "" + false);

                if (secidTEsis != null) {
                    secinfoTesis.agregarSecuencia(secidTEsis);
                }
                secinfoTesis.agregarSecuencia(secNombreEstudiante);
                secinfoTesis.agregarSecuencia(secApellidosEstudiante);
                secinfoTesis.agregarSecuencia(seccorreoEstudiante);
                secinfoTesis.agregarSecuencia(secEstadoTEsis);
                secinfoTesis.agregarSecuencia(secNivelFormacionTEsis);
                secinfoTesis.agregarSecuencia(secObservaciones);
                secinfoTesis.agregarSecuencia(secTitulo);
                secinfoTesis.agregarSecuencia(secEditable);

                secPrincipalCarga.agregarSecuencia(secinfoTesis);
            }
        }

        //Sacar tesis en módulo de/proceso de proyecto de grado pregrado
        Collection<ProyectoDeGrado> listaProyectos = proyectoDeGradoFacade.findByCorreoAsesor(carga.getProfesor().getPersona().getCorreo());
        for (ProyectoDeGrado proyectoPregrado : listaProyectos) {

            PeriodoTesisPregrado ptp = proyectoPregrado.getSemestreIniciacion();
            if (ptp.getNombre().equals(carga.getPeriodoPlaneacion().getPeriodo())) {
                Secuencia secinfoTesis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_TESIS), "");

                Secuencia secidTEsis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), "");
                Secuencia secNombreEstudiante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), proyectoPregrado.getEstudiante().getPersona().getNombres());
                Secuencia secApellidosEstudiante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), proyectoPregrado.getEstudiante().getPersona().getApellidos());
                Secuencia seccorreoEstudiante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), proyectoPregrado.getEstudiante().getPersona().getCorreo());
                Secuencia secEstadoTEsis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_ESTADO_TESIS), "Proyecto de Grado");
                Secuencia secNivelFormacionTEsis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_FORMACION_TESIS), "Pregrado");
                Secuencia secObservaciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OBSERVACIONES), "");
                Secuencia secTitulo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TITULO), proyectoPregrado.getTemaTesis());
                Secuencia secEditable = new Secuencia(getConstanteBean().getConstante(Constantes.ATR_EDITABLE), "" + false);
                if (secidTEsis != null) {
                    secinfoTesis.agregarSecuencia(secidTEsis);
                }
                secinfoTesis.agregarSecuencia(secNombreEstudiante);
                secinfoTesis.agregarSecuencia(secApellidosEstudiante);
                secinfoTesis.agregarSecuencia(seccorreoEstudiante);
                secinfoTesis.agregarSecuencia(secEstadoTEsis);
                secinfoTesis.agregarSecuencia(secNivelFormacionTEsis);
                secinfoTesis.agregarSecuencia(secObservaciones);
                secinfoTesis.agregarSecuencia(secTitulo);
                secinfoTesis.agregarSecuencia(secEditable);

                secPrincipalCarga.agregarSecuencia(secinfoTesis);
            }
        }
        return secPrincipalCarga;
    }

    public Secuencia getSecuenciaInfoPublicaciones(Collection<IntencionPublicacion> intencionPublicaciones) {

        Secuencia secPrincipalCarga = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_PUBLICACIONES), "");
        for (IntencionPublicacion cursoPlaneado : intencionPublicaciones) {
            Secuencia secInfoPublicacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_PUBLICACION), "");
            Secuencia ssecTituloPub = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TITULO), cursoPlaneado.getTituloPublicacion());
            Secuencia ssecobserva = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OBSERVACIONES), cursoPlaneado.getObservaciones());
            Secuencia secTipoPublicacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_PUBLICACION), cursoPlaneado.getTipoPublicacion().getTipoPublicacion());
            Secuencia secIdPublic = cursoPlaneado.getId() != null ? new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_PUBLICACION), cursoPlaneado.getId().toString()) : null;
            Collection<CargaProfesor> temp = cursoPlaneado.getCoAutores();
            Secuencia secCooAutroes = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COAUTORES), "");
            for (CargaProfesor carga : temp) {
                Secuencia secAutor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COAUTOR), "");
                Secuencia secNombres = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), carga.getProfesor().getPersona().getNombres());
                Secuencia secApell = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), carga.getProfesor().getPersona().getApellidos());
                Secuencia secCorreo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), carga.getProfesor().getPersona().getCorreo());
                secAutor.agregarSecuencia(secNombres);
                secAutor.agregarSecuencia(secApell);
                secAutor.agregarSecuencia(secCorreo);

                secCooAutroes.agregarSecuencia(secAutor);
            }

            secInfoPublicacion.agregarSecuencia(ssecTituloPub);
            secInfoPublicacion.agregarSecuencia(ssecobserva);
            secInfoPublicacion.agregarSecuencia(secTipoPublicacion);
            if (secIdPublic != null) {
                secInfoPublicacion.agregarSecuencia(secIdPublic);
            }
            secInfoPublicacion.agregarSecuencia(secCooAutroes);
            secPrincipalCarga.agregarSecuencia(secInfoPublicacion);
        }
        return secPrincipalCarga;
    }

    public Secuencia getSecuenciaProyectosFinanciados(Collection<ProyectoFinanciado> proyectosFinanciados) {

        Secuencia secPrincipalCarga = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_PROYECTOS_FINANCIADOS), "");
        for (ProyectoFinanciado proyectoFinanciado : proyectosFinanciados) {
            Secuencia secPoyecto = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_PROYECTO_FINANCIADO), "");
            Secuencia secidPoyecto = proyectoFinanciado == null ? null : new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), proyectoFinanciado.getId().toString());
            Secuencia secNombrePoyecto = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), proyectoFinanciado.getNombre());
            Secuencia secEntidadFinanciadora = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ENTIDAD_FINICIADORA), proyectoFinanciado.getEntidadFinanciadora());
            Secuencia secObservaciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION), proyectoFinanciado.getDescripcion());
            Secuencia secColaboradores = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COLABORADORES), "");
            Collection<CargaProfesor> colaboradores = proyectoFinanciado.getProfesores();
            for (CargaProfesor carga : colaboradores) {
                Secuencia secAutor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COLABORADOR), "");
                Secuencia secNombres = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), carga.getProfesor().getPersona().getNombres());
                Secuencia secApell = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), carga.getProfesor().getPersona().getApellidos());
                Secuencia secCorreo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), carga.getProfesor().getPersona().getCorreo());
                secAutor.agregarSecuencia(secNombres);
                secAutor.agregarSecuencia(secApell);
                secAutor.agregarSecuencia(secCorreo);

                secColaboradores.agregarSecuencia(secAutor);
            }
            if (secidPoyecto != null) {
                secPoyecto.agregarSecuencia(secidPoyecto);
            }
            secPoyecto.agregarSecuencia(secNombrePoyecto);
            secPoyecto.agregarSecuencia(secEntidadFinanciadora);
            secPoyecto.agregarSecuencia(secColaboradores);
            secPoyecto.agregarSecuencia(secObservaciones);
            secPrincipalCarga.agregarSecuencia(secPoyecto);
        }
        return secPrincipalCarga;

    }

    public Secuencia getSecuenciaOtroses(Collection<OtrasActividades> otros) {
        Secuencia secPrincipalCarga = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_OTROS), "");
        for (OtrasActividades otrasActividades : otros) {
            Secuencia secOtro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_OTRO), "");

            Secuencia secIdOtro = otrasActividades.getId() != null ? new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), otrasActividades.getId().toString()) : null;
            Secuencia secNombreOtro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), otrasActividades.getNombre());
            Secuencia secDescripOtro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION), otrasActividades.getDescripcion());

            Secuencia secDedicacionSemanal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HORAS_DEDICACION_SEMANAL), String.valueOf(otrasActividades.getDedicacionSemanal()));

            if (secIdOtro != null) {
                secOtro.agregarSecuencia(secIdOtro);
            }
            secOtro.agregarSecuencia(secNombreOtro);
            secOtro.agregarSecuencia(secDescripOtro);
            secOtro.agregarSecuencia(secDedicacionSemanal);

            secPrincipalCarga.agregarSecuencia(secOtro);
        }
        return secPrincipalCarga;

    }

    public Secuencia getSecuenciaDescarga(DescargaProfesor descarga) {
        Secuencia secPrincipalCarga = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_DESCARGA_PROFESOR), "");
        Secuencia secnombreCarga = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), descarga.getNombre());
        secPrincipalCarga.agregarSecuencia(secnombreCarga);

        return secPrincipalCarga;

    }

    public Collection<CursoPlaneado> obtenerCursos(Secuencia secCursos) {
        Collection<Secuencia> secCurso = secCursos.getSecuencias();
        Collection<CursoPlaneado> cursos = new ArrayList<CursoPlaneado>();
        for (Secuencia secuencia : secCurso) {
            CursoPlaneado cur = obtenerCursoDeSecuencia(secuencia);
            cursos.add(cur);
        }
        return cursos;
    }

    public CursoPlaneado obtenerCursoDeSecuencia(Secuencia secuencia) {
        Long id = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)) != null
                ? Long.parseLong(secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)).getValor().trim())
                : null;
        String nombreCurso = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CURSO)) != null
                ? secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CURSO)).getValor().trim()
                : null;
        String nombreNivelFormacion = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_FORMACION)) != null
                ? secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_FORMACION)).getValor().trim()
                : null;
        String observaciones = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OBSERVACIONES)) != null
                ? secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OBSERVACIONES)).getValor().trim()
                : null;
        Double cargaCurso = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGA_CURSO)) != null
                ? Double.parseDouble(secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGA_CURSO)).getValor().trim())
                : null;
        CursoPlaneado cursoP = new CursoPlaneado();
        if (id != null) {
            cursoP.setId(id);
        }
        cursoP.setNombreCurso(nombreCurso);

        NivelFormacion nivel = nivelFormacionRemote.findByName(nombreNivelFormacion);
        cursoP.setNivelDelCurso(nivel);
        if (observaciones != null) {
            cursoP.setObservaciones(observaciones);
        }
        if (cargaCurso != null) {
            cursoP.setCarga(cargaCurso);
        }

        return cursoP;

    }

    public Collection<IntencionPublicacion> obtenerPublicacionesDeSecuencia(Secuencia secPublicaciones, Long idPeriodo) {
        Collection<Secuencia> secCurso = secPublicaciones.getSecuencias();
        Collection<IntencionPublicacion> cursos = new ArrayList<IntencionPublicacion>();
        for (Secuencia secuencia : secCurso) {
            IntencionPublicacion cur = obtenerIntencionPublicacionDeSecuencia(secuencia, idPeriodo);
            cursos.add(cur);
        }
        return cursos;
    }

    public IntencionPublicacion obtenerIntencionPublicacionDeSecuencia(Secuencia secuencia, Long idPeriodo) {
        Long idP = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_PUBLICACION)) != null
                ? Long.parseLong(secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_PUBLICACION)).getValor().trim())
                : null;

        String titulo = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TITULO)) != null
                ? secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TITULO)).getValor().trim()
                : null;

        String observa = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OBSERVACIONES)) != null
                ? secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OBSERVACIONES)).getValor().trim()
                : null;

        String tipoPubli = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_PUBLICACION)) != null
                ? secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_PUBLICACION)).getValor().trim()
                : null;

        IntencionPublicacion intencionP = new IntencionPublicacion();
        intencionP.setId(idP);
        intencionP.setTituloPublicacion(titulo);
        intencionP.setObservaciones(observa);
        if (tipoPubli != null) {
            TipoPublicacion tp = tipoPublicacionFacade.findByTipoPublicacion(tipoPubli);
            intencionP.setTipoPublicacion(tp);
        }

        Secuencia secCoautores = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COAUTORES));//TAG_PARAM_COAUTORES
        if (secCoautores != null) {
            Collection<Secuencia> secAutores = secCoautores.getSecuencias();
            Collection<CargaProfesor> coautoresCarga = new ArrayList<CargaProfesor>();
            for (Secuencia secuencia1 : secAutores) {
                Secuencia secCOrreo = secuencia1.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
                Collection<CargaProfesor> cargasProfesor = cargaProfesorFacade.findByCorreo(secCOrreo.getValor());
                for (CargaProfesor cargaProfesor : cargasProfesor) {
                    PeriodoPlaneacion plan = cargaProfesor.getPeriodoPlaneacion();
                    if (plan.getId().equals(idPeriodo)) {
                        coautoresCarga.add(cargaProfesor);
                        break;
                    }
                }
            }
            intencionP.setCoAutores(coautoresCarga);
        } else {
            Collection<CargaProfesor> coautoresCarga = new ArrayList<CargaProfesor>();
            intencionP.setCoAutores(coautoresCarga);
        }
        return intencionP;
    }

    public Collection<DireccionTesis> obtenerListaAsesoriaTesisDeSecuencia(Secuencia secInfoTesises) {
        Collection<Secuencia> secCurso = secInfoTesises.getSecuencias();
        Collection<DireccionTesis> cursos = new ArrayList<DireccionTesis>();
        for (Secuencia secuencia : secCurso) {
            DireccionTesis cur = obtenerDireccionTesisDeSecuencia(secuencia);
            cursos.add(cur);
        }
        return cursos;
    }

    public Collection<ProyectoFinanciado> obtenerListaProyectosFinanciadosDeSecuencia(Secuencia secInfoTesises, Long idPeriodo) {
        Collection<Secuencia> secCurso = secInfoTesises.getSecuencias();
        Collection<ProyectoFinanciado> cursos = new ArrayList<ProyectoFinanciado>();
        for (Secuencia secuencia : secCurso) {
            ProyectoFinanciado cur = obtenerProyectoFinanciadoDeSecuencia(secuencia, idPeriodo);
            cursos.add(cur);
        }
        return cursos;
    }

    public Collection<OtrasActividades> obtenerOtrasActividadesDeSecuencia(Secuencia secCursos) {
        Collection<Secuencia> secCurso = secCursos.getSecuencias();
        Collection<OtrasActividades> cursos = new ArrayList<OtrasActividades>();
        for (Secuencia secuencia : secCurso) {
            OtrasActividades cur = obtenerOtraActividadDeSecuencia(secuencia);
            cursos.add(cur);
        }
        return cursos;
    }

    public DireccionTesis obtenerDireccionTesisDeSecuencia(Secuencia secuencia) {
        Long idP = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)) != null
                ? Long.parseLong(secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)).getValor().trim())
                : null;

        String correoEstudiante = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)) != null
                ? secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor().trim()
                : null;

        String strNivelEstadoTesis = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_ESTADO_TESIS)) != null
                ? secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_ESTADO_TESIS)).getValor().trim()
                : null;

        String strNivelFormacion = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_FORMACION_TESIS)) != null
                ? secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_FORMACION_TESIS)).getValor().trim()
                : null;

        String observaciones = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OBSERVACIONES)) != null
                ? secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OBSERVACIONES)).getValor().trim()
                : null;
        String titulo = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TITULO)) != null
                ? secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TITULO)).getValor().trim()
                : null;

        DireccionTesis nuevaTesis = new DireccionTesis();
        nuevaTesis.setId(idP);
        nuevaTesis.setObservaciones(observaciones);
        nuevaTesis.setTitulo(titulo);

        Estudiante estud = estudianteFacadeRemote.findByCorreo(correoEstudiante);
        NivelFormacion nivelFormacion = nivelFormacionRemote.findByName(strNivelFormacion);

        nuevaTesis.setAutorEstudiante(estud);
        nuevaTesis.setNivelFormacionTesis(nivelFormacion);

        if (strNivelEstadoTesis != null) {
            Collection<NivelTesis> niveles = nivelTesisFacadeRemote.findAll();
            for (NivelTesis nivelTesis : niveles) {
                if (nivelTesis.getNivelTesis().equals(strNivelEstadoTesis)) {
                    nuevaTesis.setEstadoTesis(nivelTesis);
                    break;
                }
            }
        }
        return nuevaTesis;
    }

    public ProyectoFinanciado obtenerProyectoFinanciadoDeSecuencia(Secuencia secuencia, Long idPeriodo) {
        ProyectoFinanciado proyectoF = new ProyectoFinanciado();

        Long idP = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)) != null
                ? Long.parseLong(secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)).getValor().trim())
                : null;
        proyectoF.setId(idP);
        String nombreProyecto = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE)) != null
                ? secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE)).getValor().trim()
                : null;
        proyectoF.setNombre(nombreProyecto);
        String entidadFinanciadora = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ENTIDAD_FINICIADORA)) != null
                ? secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ENTIDAD_FINICIADORA)).getValor().trim()
                : null;
        proyectoF.setEntidadFinanciadora(entidadFinanciadora);
        String descripcion = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION)) != null
                ? secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION)).getValor().trim()
                : null;
        proyectoF.setDescripcion(descripcion);
        Secuencia secColaboradores = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COLABORADORES));
        Collection<CargaProfesor> colaboradorsCarga = new ArrayList<CargaProfesor>();
        if (secColaboradores != null) {
            Collection<Secuencia> secColab = secColaboradores.getSecuencias();
            for (Secuencia secuencia1 : secColab) {
                String correoCol = secuencia1.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)) != null
                        ? secuencia1.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor() : null;

                if (correoCol != null) {
                    Collection<CargaProfesor> cargasProf = cargaProfesorFacade.findByCorreo(correoCol);
                    for (CargaProfesor cargaProfesor : cargasProf) {
                        if (cargaProfesor.getPeriodoPlaneacion().getId().equals(idPeriodo)) {
                            colaboradorsCarga.add(cargaProfesor);
                        }
                    }
                }
            }
        }
        proyectoF.setProfesores(colaboradorsCarga);
        return proyectoF;
    }

    public OtrasActividades obtenerOtraActividadDeSecuencia(Secuencia secuencia) {
        OtrasActividades otraAct = new OtrasActividades();
        Long idP = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)) != null
                ? Long.parseLong(secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)).getValor().trim())
                : null;
        otraAct.setId(idP);
        String nombreProyecto = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE)) != null
                ? secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE)).getValor().trim()
                : null;
        otraAct.setNombre(nombreProyecto);
        String descripcion = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION)) != null
                ? secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION)).getValor().trim()
                : null;

        String dedicacion = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HORAS_DEDICACION_SEMANAL)) != null
                ? secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HORAS_DEDICACION_SEMANAL)).getValor().trim()
                : null;
        if (dedicacion != null) {
            otraAct.setDedicacionSemanal(Double.parseDouble(dedicacion));
        }
        otraAct.setDescripcion(descripcion);
        return otraAct;
    }

    private ConstanteRemote getConstanteBean() {
        return constanteBean;
    }
}
