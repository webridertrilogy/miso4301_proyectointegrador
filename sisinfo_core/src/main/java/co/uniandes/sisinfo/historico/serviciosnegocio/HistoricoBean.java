package co.uniandes.sisinfo.historico.serviciosnegocio;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.entities.Archivo;
import co.uniandes.sisinfo.entities.Aspirante;
import co.uniandes.sisinfo.entities.MonitoriaAceptada;
import co.uniandes.sisinfo.entities.Periodo;
import co.uniandes.sisinfo.entities.Solicitud;
import co.uniandes.sisinfo.entities.TipoArchivo;
import co.uniandes.sisinfo.entities.datosmaestros.Curso;
import co.uniandes.sisinfo.entities.datosmaestros.InformacionAcademica;
import co.uniandes.sisinfo.entities.datosmaestros.Profesor;
import co.uniandes.sisinfo.entities.datosmaestros.Seccion;
import co.uniandes.sisinfo.entities.datosmaestros.Sesion;
import co.uniandes.sisinfo.historico.entities.h_archivo;
import co.uniandes.sisinfo.historico.entities.h_curso;
import co.uniandes.sisinfo.historico.entities.h_informacionAcademica;
import co.uniandes.sisinfo.historico.entities.h_monitor;
import co.uniandes.sisinfo.historico.entities.h_monitoria;
import co.uniandes.sisinfo.historico.entities.h_profesor;
import co.uniandes.sisinfo.historico.entities.h_seccion;
import co.uniandes.sisinfo.historico.serviciosfuncionales.h_archivoFacadeLocal;
import co.uniandes.sisinfo.historico.serviciosfuncionales.h_cursoFacadeLocal;
import co.uniandes.sisinfo.historico.serviciosfuncionales.h_informacionAcademicaFacadeLocal;
import co.uniandes.sisinfo.historico.serviciosfuncionales.h_monitorFacadeLocal;
import co.uniandes.sisinfo.historico.serviciosfuncionales.h_monitoriaFacadeLocal;
import co.uniandes.sisinfo.historico.serviciosfuncionales.h_profesorFacadeLocal;
import co.uniandes.sisinfo.historico.serviciosfuncionales.h_seccionFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ArchivoFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.AspiranteFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.MonitoriaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.PeriodoFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.SolicitudFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.TipoArchivoFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.CursoFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.GrupoInvestigacionFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.ProfesorFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.SeccionFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import co.uniandes.sisinfo.serviciosnegocio.ConstanteRemote;
import co.uniandes.sisinfo.serviciosnegocio.HistoricoRemote;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

/**
 *
 * @author david
 */
@Stateless
public class HistoricoBean implements HistoricoRemote, HistoricoLocal {

    private ParserT parser;
    @EJB
    private h_monitorFacadeLocal h_monitorFacade;
    @EJB
    private h_cursoFacadeLocal h_cursoFacade;
    @EJB
    private h_seccionFacadeLocal h_seccionFacade;
    @EJB
    private h_archivoFacadeLocal h_archivoFacade;
    @EJB
    private ConstanteRemote constanteBean;
    private ServiceLocator serviceLocator;
    @EJB
    private PeriodoFacadeRemote periodoFacade;
    private String[] tipos;
    /**
     * Curso Facade
     */
    private CursoFacadeRemote cursoFacade;
    /**
     * Seccion Facade
     */
    private SeccionFacadeRemote seccionFacade;
    /**
     * Profesor Facade
     */
    private ProfesorFacadeRemote profesorFacade;
    /**
     * GrupoInvestigacion Facade
     */
    private GrupoInvestigacionFacadeRemote grupoInvestigacionFacade;
    /**
     * h_profesor Facade
     */
    @EJB
    private h_profesorFacadeLocal h_profesorFacade;
    /**
     * Solicitud Facade
     */
    private SolicitudFacadeRemote solicitudFacade;
    /**
     * Aspirante Facade
     */
    private AspiranteFacadeRemote aspiranteFacade;
    /**
     * h_monitoria Facade
     */
    @EJB
    private h_monitoriaFacadeLocal h_monitoriaFacade;
    /**
     * h_informacionAcademica Facade
     */
    @EJB
    private h_informacionAcademicaFacadeLocal h_informacionAcademicaFacade;
    /**
     * Monitoria Facade
     */
    private MonitoriaFacadeRemote monitoriaFacade;
    /**
     * Secciones.
     */
    private Collection<Seccion> secciones;
    /**
     * Secciones  historico.
     */
    private Collection<h_seccion> secciones_h;
    /**
     * TipoArchivo Facade
     */
    private TipoArchivoFacadeRemote tipoArchivoFacade;
    /**
     * Archivo Facade
     */
    private ArchivoFacadeRemote archivoFacade;

    public HistoricoBean() {

        try {
            parser = new ParserT();
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            periodoFacade = (PeriodoFacadeRemote) serviceLocator.getRemoteEJB(PeriodoFacadeRemote.class);
            tipos = new String[]{getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_PROGRAMA),
                        getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_TREINTA_POR_CIENTO),
                        getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CIERRE)};

            // Monitorias.
            cursoFacade = (CursoFacadeRemote) serviceLocator.getRemoteEJB(CursoFacadeRemote.class);
            seccionFacade = (SeccionFacadeRemote) serviceLocator.getRemoteEJB(SeccionFacadeRemote.class);
            profesorFacade = (ProfesorFacadeRemote) serviceLocator.getRemoteEJB(ProfesorFacadeRemote.class);
            grupoInvestigacionFacade = (GrupoInvestigacionFacadeRemote) serviceLocator.getRemoteEJB(GrupoInvestigacionFacadeRemote.class);
            solicitudFacade = (SolicitudFacadeRemote) serviceLocator.getRemoteEJB(SolicitudFacadeRemote.class);
            aspiranteFacade = (AspiranteFacadeRemote) serviceLocator.getRemoteEJB(AspiranteFacadeRemote.class);
            monitoriaFacade = (MonitoriaFacadeRemote) serviceLocator.getRemoteEJB(MonitoriaFacadeRemote.class);

            // Cartelera.
            tipoArchivoFacade = (TipoArchivoFacadeRemote) serviceLocator.getRemoteEJB(TipoArchivoFacadeRemote.class);
            archivoFacade = (ArchivoFacadeRemote) serviceLocator.getRemoteEJB(ArchivoFacadeRemote.class);

        } catch (NamingException ex) {
            Logger.getLogger(HistoricoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String consultarMonitoriasEnHistorico(String comando) {
        try {
            parser.leerXML(comando);
            String correo = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            h_monitor monitor = h_monitorFacade.findByCorreo(correo);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            Secuencia secMonitorias = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MONITORIAS), "");
            if (monitor != null) {
                Collection<h_monitoria> monitorias = monitor.getMonitorias();
                Collection<h_monitoria> monitoriasFinal = new ArrayList<h_monitoria>();
                for (h_monitoria h : monitorias) {
                    if (verificarValoresRepetidos(monitoriasFinal, h)) {
                        monitoriasFinal.add(h);
                        Secuencia secMonitoria = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MONITORIA), "");
                        secMonitoria.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CREDITOS_APROBADOS), Double.toString(h.getCreditos())));
                        secMonitoria.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_CREACION), (h.getFechaCreacion() != null) ? h.getFechaCreacion().toString() : ""));
                        secMonitoria.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_RADICACION), (h.getFechaRadicacion() != null) ? h.getFechaRadicacion().toString() : ""));
                        secMonitoria.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA), Double.toString(h.getNota())));
                        secMonitoria.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_RADICACION), h.getNumeroRadicacion()));
                        secMonitoria.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO), h.getPeriodo()));
                        secMonitoria.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_MONITORIA), h.getTipoMonitoria()));
                        secMonitoria.agregarSecuencia(darSecuenciaCursoHistorico(h));
                        secMonitorias.agregarSecuencia(secMonitoria);
                    }
                }
            }
            secuencias.add(secMonitorias);
            String respuesta = parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_MONITORIAS_HISTORICO_POR_CORREO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0139, new ArrayList());
            return respuesta;
        } catch (Exception ex) {
            Logger.getLogger(HistoricoBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private boolean verificarValoresRepetidos(Collection<h_monitoria> collection, h_monitoria monitoria) {
        boolean valida = true, validaFechas1 = true, validaFechas2 = true, validaNumeroRadicacion = true, validaPeriodo = true, validaSeccion = true, validaMonitoria = true;
        
        if (collection.isEmpty()) {
            valida = true;
        } else {
            for (h_monitoria h : collection) {

                if ((h.getFechaCreacion() == null && monitoria.getFechaCreacion() != null)|| (h.getFechaCreacion() != null && monitoria.getFechaCreacion() == null)) {
                    validaFechas1 = true;
                } else if ((h.getFechaCreacion() == null && monitoria.getFechaCreacion() == null) || h.getFechaCreacion().equals(monitoria.getFechaCreacion())) {
                    validaFechas1 = false;
                } else {
                    validaFechas1 = true;
                }

                if ((h.getFechaRadicacion() == null && monitoria.getFechaRadicacion() != null) || (h.getFechaRadicacion() != null && monitoria.getFechaRadicacion() == null)) {
                    validaFechas2 = true;
                } else if ((h.getFechaRadicacion() == null && monitoria.getFechaRadicacion() == null) || h.getFechaRadicacion().equals(monitoria.getFechaRadicacion())) {
                    validaFechas2 = false;
                } else {
                    validaFechas2 = true;
                }

                if ((h.getNumeroRadicacion() == null && monitoria.getNumeroRadicacion()!=null) || (h.getNumeroRadicacion()!= null && monitoria.getNumeroRadicacion() == null)) {
                    validaNumeroRadicacion = true;
                } else if ((h.getNumeroRadicacion() == null && monitoria.getNumeroRadicacion() == null) || h.getNumeroRadicacion().equals(monitoria.getNumeroRadicacion()) ) {
                    validaNumeroRadicacion = false;
                } else {
                    validaNumeroRadicacion = true;
                }

                if ((h.getPeriodo() == null && monitoria.getPeriodo() == null) || h.getPeriodo().equals(monitoria.getPeriodo())) {
                    validaPeriodo = false;
                }

                if ((h.getSeccion() == null && monitoria.getSeccion() == null) || (h.getSeccion().getId() == monitoria.getSeccion().getId())) {
                    validaSeccion = false;
                }

                if ((h.getTipoMonitoria() == null && monitoria.getTipoMonitoria() == null) || h.getTipoMonitoria().equals(monitoria.getTipoMonitoria())) {
                    validaMonitoria = false;
                }

                if (!validaFechas1 && !validaFechas2 && !validaNumeroRadicacion && !validaPeriodo && !validaSeccion && !validaMonitoria && h.getCreditos() == monitoria.getCreditos() && h.getNota() == monitoria.getNota()) {
                    valida = false;
                }
            }
        }
        return valida;
    }

    private Secuencia darSecuenciaCursoHistorico(h_monitoria h) {
        Secuencia secCurso = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO), getConstanteBean().getConstante(Constantes.NULL));
        h_seccion s = h.getSeccion();
        h_curso c = h_cursoFacade.findByCRNSeccion(s.getCrn());


        if (c != null) {
            Secuencia secCodigo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO), c.getCodigo());
            Secuencia secCreditos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CREDITOS_APROBADOS), "" + c.getCreditos());
            Secuencia secNombreCurso = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CURSO), c.getNombre());
            secCurso.agregarSecuencia(secCodigo);
            secCurso.agregarSecuencia(secCreditos);
            secCurso.agregarSecuencia(secNombreCurso);

            Secuencia secSecciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCIONES), getConstanteBean().getConstante(Constantes.NULL));
            Secuencia secSeccion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCION), getConstanteBean().getConstante(Constantes.NULL));
            Secuencia secCRN = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION), s.getCrn());
            Secuencia secHorario = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HORARIO), s.getHorario());
            Secuencia secNumeroSeccion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_SECCION), s.getNumeroSeccion());
            Secuencia secPeriodo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO), s.getPeriodo());

            Secuencia secProfesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR), getConstanteBean().getConstante(Constantes.NULL));
            h_profesor p = s.getProfesor();
            Secuencia secApellidos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), p.getApellidos());
            Secuencia secNombres = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), p.getNombres());
            Secuencia secCatedra = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR_CATEDRA), "" + p.isCatedra());
            Secuencia secCorreo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), p.getCorreo());
            Secuencia secGrupoInvestigacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_GRUPO_INVESTIGACION), p.getGrupoInvestigacion());

            secProfesor.agregarSecuencia(secApellidos);
            secProfesor.agregarSecuencia(secNombres);
            secProfesor.agregarSecuencia(secCatedra);
            secProfesor.agregarSecuencia(secCorreo);
            secProfesor.agregarSecuencia(secGrupoInvestigacion);

            secSeccion.agregarSecuencia(secCRN);
            secSeccion.agregarSecuencia(secHorario);
            secSeccion.agregarSecuencia(secNumeroSeccion);
            secSeccion.agregarSecuencia(secPeriodo);
            secSeccion.agregarSecuencia(secProfesor);

            secSecciones.agregarSecuencia(secSeccion);
            secCurso.agregarSecuencia(secSecciones);



        }
        return secCurso;


    }

    public String darArchivosProfesorPorPeriodoEnHistorico(String comando) {
        try {
            parser.leerXML(comando);

            String periodo = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO)).getValor();
            List<h_seccion> secciones = h_seccionFacade.findByPeriodo(periodo);

            Secuencia secCursos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSOS), "");
            Secuencia secCurso = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO), "");

            //cruza secciones vs cursos, para agregar la sección dentro de la secuencia del cursode cada curso de sección
            Secuencia secSecciones = null;


            for (h_seccion s : secciones) {
                h_curso c = h_cursoFacade.findBySeccionId(s.getId());


                boolean encontrado = false;


                for (Iterator<Secuencia> it = secCursos.getSecuencias().iterator(); it.hasNext();) {
                    Secuencia secTemp = it.next();



                    if (secTemp != null && secTemp.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CURSO)) != null
                            && secTemp.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CURSO)).getValor().equals(c.getNombre())) {
                        secSecciones = secTemp.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCIONES));
                        encontrado = true;


                    }
                }

                if (!encontrado) {
                    secCurso = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO), "");
                    secCurso.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO), c.getCodigo()));
                    secCurso.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CURSO), c.getNombre()));
                    secSecciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCIONES), "");
                    secCurso.agregarSecuencia(secSecciones);
                    secCursos.agregarSecuencia(secCurso);


                }
                Secuencia secSeccion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCION), "");
                secSeccion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION), s.getCrn()));
                secSeccion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_SECCION), "" + s.getNumeroSeccion()));
                Secuencia secArchivos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ARCHIVOS), "");
                Secuencia secArchivo;



                for (int i = 0; i
                        < tipos.length; i++) {
                    h_archivo arch = h_archivoFacade.findBySeccionAndTipo("" + s.getId(), tipos[i]);
                    secArchivo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ARCHIVO), "");


                    if (arch != null) {
                        secArchivo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SUBIDO), "true"));
                        secArchivo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_ARCHIVO), arch.getId().longValue() + ""));
                        secArchivo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_ARCHIVO), "Archivo Descarga - " + tipos[i]));
                        secArchivo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_ARCHIVO), arch.getTipoArchivo()));


                    } else {
                        secArchivo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_ARCHIVO), ""));
                        secArchivo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_ARCHIVO), "No disponible"));
                        secArchivo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SUBIDO), "false"));
                        secArchivo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_ARCHIVO), tipos[i]));



                    }
                    secArchivos.agregarSecuencia(secArchivo);


                }
                secSeccion.agregarSecuencia(secArchivos);

                //agrega profesor a la secuencia
                h_profesor profesorSeccion = s.getProfesor();


                if (profesorSeccion != null) {
                    Secuencia secProfesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR), "");
                    secProfesor.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), profesorSeccion.getNombres()));
                    secProfesor.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), profesorSeccion.getApellidos()));
                    secProfesor.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), profesorSeccion.getCorreo()));

                    secSeccion.agregarSecuencia(secProfesor);


                }

                secSecciones.agregarSecuencia(secSeccion);


            }

            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secCursos);



            return parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_ARCHIVOS_PROFESOR_POR_PERIODO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0139, new ArrayList());




        } catch (Exception ex) {
            Logger.getLogger(HistoricoBean.class.getName()).log(Level.SEVERE, null, ex);



            return null;
        }


    }

    private ConstanteRemote getConstanteBean() {
        return constanteBean;


    }

    public String pasarMonitoriasAHistoricos(String comando) {

        try {

            pasarProfesoresAHistoricos(); // Pasamos profesores.
            pasarCursosAHistoricos(); // Pasamos cursos.
            pasarMonitoresAHistoricos(); // Pasamos monitores con monitorias.
            pasarCartelera(); // Pasamos cartelera
            borrarMonitoriasSisinfo(); // Borramos en Sisinfo.

            Periodo actual = periodoFacade.findActual(); //Coloca el periodo actual como NO actual
            String[] args = new String[2];
            args[

0] = "--context=Default";


            if (actual != null) {
                args[1] = "--periodo=" + actual.getPeriodo();
                actual.setActual(false);
                periodoFacade.edit(actual);


            }
            return parser.generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_PASAR_MONITORIAS_A_HISTORICOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new ArrayList());




        } catch (Exception ex) {
            Logger.getLogger(HistoricoBean.class.getName()).log(Level.SEVERE, null, ex);



            return null;
        }


    }

    /**
     * Pasa cursos a historico
     * @param comando
     */
    public void pasarCursosAHistoricos() {

        List<Curso> listCurso = cursoFacade.findAll(); // Recorremos cursos.


        boolean nuevo = false;
        h_curso hist_curso;


        for (Curso curso : listCurso) {

            List<h_curso> listH_Curso = this.h_cursoFacade.findAll(); // Recorremos cursos historicos.
            nuevo = true; // Variables para manejar objeto historico curso.
            hist_curso = null;


            for (h_curso hist_cursoTmp : listH_Curso) {

                if ((hist_cursoTmp.getCodigo() != null && hist_cursoTmp.getCodigo().equals(curso.getCodigo()))
                        && (hist_cursoTmp.getNombre() != null && hist_cursoTmp.getNombre().equals(curso.getNombre()))) {
                    // Un curso historico ya existe si del curso a pasar ya tiene mismo codigo y mismo nombre.

                    hist_curso = hist_cursoTmp;
                    nuevo = false;


                    break;


                }
            }

            // Copia el curso.
            if (hist_curso == null) {

                hist_curso = new h_curso();


            }
            hist_curso = HistoricoTranslator.fromCurso2h_curso(curso, hist_curso);

            // Pasamos secciones entre cursos.


            if (curso.getSecciones() != null && !curso.getSecciones().isEmpty()) {

                HistoricoTranslator.pasarSecciones(curso, hist_curso, this.h_profesorFacade.findAll(), periodoFacade.findActual());


            } // Persisitmos secciones.
            if (hist_curso.getSecciones() != null && !hist_curso.getSecciones().isEmpty()) {

                pasarSeccionesAHistoricos(hist_curso);


            }

            if (nuevo) {

                this.h_cursoFacade.create(hist_curso); // Creamos.


            } else {

                this.h_cursoFacade.edit(hist_curso); // Actualizamos.


            }
        }
    }

    /**
     * Pasa secciones a historico
     * @param comando
     */
    public void pasarSeccionesAHistoricos(h_curso hist_curso) {
        for (h_seccion hist_seccion : hist_curso.getSecciones()) {
            this.h_seccionFacade.create(hist_seccion); // Creamos.


            if (this.secciones_h == null) {

                this.secciones_h = new ArrayList<h_seccion>();


            }
            this.secciones_h.add(hist_seccion);


        }
    }

    /**
     * Pasa profesores a historico
     * @param comando
     */
    public void pasarProfesoresAHistoricos() {

        List<Profesor> listProfesor = profesorFacade.findAll();


        boolean nuevo = false;
        h_profesor hist_profesor;


        for (Profesor profesor : listProfesor) {

            List<h_profesor> listH_profesor = this.h_profesorFacade.findAll();
            nuevo = true;
            hist_profesor = null;


            for (h_profesor hist_profesorTmp : listH_profesor) {

                if (hist_profesorTmp.getCorreo() != null && // El correo de historico profesor puede ser null.
                        hist_profesorTmp.getCorreo().equals(profesor.getPersona().getCorreo())) { // profesor existe.

                    hist_profesor = hist_profesorTmp;
                    nuevo = false;


                    break;


                }
            }

            // Copia el profesor.
            if (hist_profesor == null) {

                hist_profesor = new h_profesor();


            }
            hist_profesor = HistoricoTranslator.fromProfesor2h_profesor(profesor, hist_profesor);



            if (nuevo) {

                this.h_profesorFacade.create(hist_profesor); // Creamos.


            } else {

                this.h_profesorFacade.edit(hist_profesor); // Actualizamos.


            }
        }
    }

    /**
     * Pasa monitores a historico
     * @param comando
     */
    public void pasarMonitoresAHistoricos() {

        List<Solicitud> listSolicitud = solicitudFacade.findAll();
        h_monitor hist_monitor = null;


        boolean nuevo = false;


        for (Solicitud solicitud : listSolicitud) { // Hay solicitudes siempre.

            List<h_monitor> listH_monitor = this.h_monitorFacade.findAll();
            nuevo = true;
            hist_monitor = null;


            for (h_monitor hist_monitorTmp : listH_monitor) {

                if (hist_monitorTmp.getCorreo() != null
                        && hist_monitorTmp.getCorreo().equals(
                        solicitud.getEstudiante().getEstudiante().getPersona().getCorreo())) {
                    // El monitor ya existe y su correo no es null.

                    hist_monitor = hist_monitorTmp;
                    nuevo = false;


                    break;


                }
            }

            // Copia el monitor.
            if (hist_monitor == null) {

                hist_monitor = new h_monitor();


            }
            hist_monitor = HistoricoTranslator.fromAspirante2h_monitor(solicitud.getEstudiante(), hist_monitor, this.periodoFacade.findActual());


            if (nuevo) {

                this.h_monitorFacade.create(hist_monitor); // Creamos.


            } else {

                this.h_monitorFacade.edit(hist_monitor); // Actualizamos.


            } // Pasamos informacion academica
            if (solicitud.getEstudiante().getEstudiante().getInformacion_Academica() != null) {

                pasarInformacionAcademicaAHistoricos(
                        solicitud.getEstudiante().getEstudiante().getInformacion_Academica(), hist_monitor);


            } // Pasamos monitorias.
            pasarMonitoriasAHistoricos(solicitud, hist_monitor);


        }
    }

    /**
     * Pasa informacion academica a historico
     * @param comando
     */
    public void pasarInformacionAcademicaAHistoricos(InformacionAcademica informacionAcademica, h_monitor hist_monitor) {

        // Creamos nueva informacion academica.
        h_informacionAcademica hist_informacionAcademica =
                HistoricoTranslator.fromInformacionAcademica2h_informacionAcademica(informacionAcademica, periodoFacade.findActual());


        this.h_informacionAcademicaFacade.create(hist_informacionAcademica); // Creamos.

        // Asociamos a monitor.


        if (hist_monitor.getInformacionAcademica() == null) {

            hist_monitor.setInformacionAcademica(new ArrayList<h_informacionAcademica>());


        }
        hist_monitor.getInformacionAcademica().add(hist_informacionAcademica);


        this.h_monitorFacade.edit(hist_monitor); // Actualizamos.*/


    }

    /**
     * Pasa monitorias a historico desde solicitud.
     * @param comando
     */
    public void pasarMonitoriasAHistoricos(Solicitud solicitud, h_monitor hist_monitor) {

        h_monitoria hist_monitoria = null;
        h_seccion hist_seccion = null;
        Periodo periodo = periodoFacade.findActual();


        for (MonitoriaAceptada monitoriaAceptada : solicitud.getMonitorias()) { // Pasa solo monitorias aceptadas.

            // Recorremos secciones historico en contenedor.
            for (h_seccion hist_seccionTmp : this.secciones_h) {

                if (hist_seccionTmp.getPeriodo().equals(periodo.getPeriodo()) && hist_seccionTmp.getCrn().equals(monitoriaAceptada.getSeccion().getCrn())) {
                    // CRN no es null

                    hist_seccion = hist_seccionTmp;


                    break;


                }
            }
            hist_monitoria = HistoricoTranslator.fromMonitoria2h_monitoria(solicitud, monitoriaAceptada, periodo, hist_monitor, hist_seccion);


            this.h_monitoriaFacade.create(hist_monitoria); // Creamos.

            // Asociamos también al monitor su monitoría.


            if (hist_monitor.getMonitorias() == null || hist_monitor.getMonitorias().isEmpty()) {

                hist_monitor.setMonitorias(new ArrayList<h_monitoria>());


            }
            hist_monitor.getMonitorias().add(hist_monitoria);


            this.h_monitorFacade.edit(hist_monitor); // Actualizamos.


        }
    }

    /**
     * Borra la información de monitorias en Sisisnfo.
     */
    public void borrarMonitoriasSisinfo() {

        borrarSolicitudes();
        borrarCursos();
        borrarSecciones();
        borrarCartelera();


    }

    /**
     * Borra solicitudes.
     */
    public void borrarSolicitudes() {

        List<Solicitud> listSolicitud = solicitudFacade.findAll();


        if (listSolicitud != null && !listSolicitud.isEmpty()) {

            Collection<MonitoriaAceptada> aceptadas = null;
            Aspirante aspirante = null;


            for (Solicitud solicitud : listSolicitud) {

                aceptadas = solicitud.getMonitorias(); // Aislamos de aceptadas.
                solicitud.setMonitorias(null);

                solicitud.setEstudiante(null); // Aislamos de aspirante.
                solicitudFacade.edit(solicitud);



                if (aceptadas != null && !aceptadas.isEmpty()) {

                    borrarMonitorias(aceptadas); // Borra monitorias.


                }

                solicitudFacade.remove(solicitud); // Borramos solicitud.


            }
            borrarAspirantes(); // Borra aspirantes.


        }
    }

    /**
     * Borra aspirante.
     */
    public void borrarAspirantes() {

        Collection<Aspirante> aspirantes = aspiranteFacade.findAll();


        if (aspirantes != null && !aspirantes.isEmpty()) {

            for (Aspirante aspirante : aspirantes) {

                aspirante.setEstudiante(null);


                this.aspiranteFacade.edit(aspirante); // Borra aspirante.


                this.aspiranteFacade.remove(aspirante); // Borra aspirante.


            }
        }
    }

    /**
     * Borra monitorias.
     */
    public void borrarMonitorias(Collection<MonitoriaAceptada> aceptadas) {

        for (MonitoriaAceptada aceptada : aceptadas) {

            aceptada.setSolicitud(null);
            aceptada.setSecciones(null);
            monitoriaFacade.edit(aceptada);
            monitoriaFacade.remove(aceptada);


        }
    }

    /**
     * Borra cursos.
     */
    public void borrarCursos() {

        List<Curso> listCurso = cursoFacade.findAll();


        if (listCurso != null && !listCurso.isEmpty()) {

            Collection<Curso> relacionados = null;
            Collection<Seccion> seccionesCurso = null;


            for (Curso curso : listCurso) { // Cursos

                relacionados = curso.getCursosRelacionados(); // Aislamos de relacionados.
                curso.setCursosRelacionados(null);

                seccionesCurso = curso.getSecciones(); // Aislamos de secciones.
                curso.setSecciones(null);
                cursoFacade.edit(curso);

                // Agregamos secciones a otro contenedor.


                if (this.secciones == null) {

                    this.secciones = new ArrayList<Seccion>();


                }
                this.secciones.addAll(seccionesCurso);

                cursoFacade.remove(curso); // Borramos curso.


            }
        }
    }

    /**
     * Borra secciones.
     */
    public void borrarSecciones() {

        Collection<Sesion> horarios = null;


        if (this.secciones != null) {
            for (Seccion seccion : this.secciones) { // Secciones.

                horarios = seccion.getHorarios(); // Aislamos de horarios.
                seccion.setHorarios(null);

                seccion.setProfesores(null); // Aislamos de profesores.
                seccion.setProfesorPrincipal(null);
                seccionFacade.edit(seccion);
                seccionFacade.remove(seccion); // Borramos seccion.


            }
        }
    }

    /**
     * Pasa cartelera
     */
    public void pasarCartelera() {


        List<TipoArchivo> listTipoArchivo = this.tipoArchivoFacade.findAll(); // Consultamos tipos de archivo.
        List<h_seccion> listh_seccion = this.h_seccionFacade.findAll(); // Consultamos secciones de historicos.
        // Recorremos archivos.
        List<Archivo> listArchivo = this.archivoFacade.findAll();
        h_archivo hist_archivo = null;


        for (Archivo archivo : listArchivo) {

            hist_archivo = HistoricoTranslator.fromArchivo2h_archivo(archivo, listTipoArchivo, listh_seccion, periodoFacade.findActual());


            this.h_archivoFacade.create(hist_archivo);

            // Asociamos en historicos el archivo a lista de archivos de su objeto seccion correspondiente.


            for (h_seccion hist_seccion : listh_seccion) {

                if (hist_seccion.equals(hist_archivo.getSeccion())) {

                    if (hist_seccion.getArchivos() == null || hist_seccion.getArchivos().isEmpty()) {

                        hist_seccion.setArchivos(new ArrayList<h_archivo>());


                    }
                    hist_seccion.getArchivos().add(hist_archivo);


                }
                this.h_seccionFacade.edit(hist_seccion);


            }
        }
    }

    /**
     * Borra cartelera
     */
    public void borrarCartelera() {

        // Recorremos archivos.
        List<Archivo> listArchivo = this.archivoFacade.findAll();


        for (Archivo archivo : listArchivo) {

            this.archivoFacade.remove(archivo);


        }
    }

    /**
     * 
     * @param comando
     * @return
     */
    public String darInfoArchivoHistorico(String comando) {
        try {
            parser.leerXML(comando);
            String id = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_ARCHIVO)).getValor();
            Vector<Secuencia> secuencias = new Vector<Secuencia>();

            h_archivo a = h_archivoFacade.find(new Long(id));



            if (a != null) {
                Secuencia secArchivo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ARCHIVO), "");
                secArchivo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MIME), a.getTipoMIME()));
                secArchivo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA_DIRECTORIO), a.getRuta()));
                secuencias.add(secArchivo);


            }

            return parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_INFO_ARCHIVO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0139, new Vector());




        } catch (Exception ex) {
            Logger.getLogger(HistoricoBean.class.getName()).log(Level.SEVERE, null, ex);



            return null;
        }


    }

    public Collection<String> darPeriodosPlaneacionAcademicaHistoricos() {
        return h_archivoFacade.findPeriodos();

    }
}
