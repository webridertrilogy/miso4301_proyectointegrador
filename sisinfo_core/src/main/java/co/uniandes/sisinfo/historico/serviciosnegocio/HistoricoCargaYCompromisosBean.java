/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.historico.serviciosnegocio;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.historico.entities.cyc.h_cargaProfesor_cyc;
import co.uniandes.sisinfo.historico.entities.cyc.h_curso_planeado;
import co.uniandes.sisinfo.historico.entities.cyc.h_direccion_tesis;
import co.uniandes.sisinfo.historico.entities.cyc.h_evento;
import co.uniandes.sisinfo.historico.entities.cyc.h_intencion_publicacion;
import co.uniandes.sisinfo.historico.entities.cyc.h_otras_actividades;
import co.uniandes.sisinfo.historico.entities.cyc.h_periodo_planeacion;
import co.uniandes.sisinfo.historico.entities.cyc.h_proyecto_financiado;
import co.uniandes.sisinfo.historico.serviciosfuncionales.cyc.h_cargaProfesor_cycFacadeLocal;
import co.uniandes.sisinfo.historico.serviciosfuncionales.cyc.h_curso_planeadoFacadeLocal;
import co.uniandes.sisinfo.historico.serviciosfuncionales.cyc.h_direccion_tesisFacadeLocal;
import co.uniandes.sisinfo.historico.serviciosfuncionales.cyc.h_eventoFacadeLocal;
import co.uniandes.sisinfo.historico.serviciosfuncionales.cyc.h_intencion_publicacionFacadeLocal;
import co.uniandes.sisinfo.historico.serviciosfuncionales.cyc.h_otras_actividadesFacadeLocal;
import co.uniandes.sisinfo.historico.serviciosfuncionales.cyc.h_periodo_planeacionFacadeLocal;
import co.uniandes.sisinfo.historico.serviciosfuncionales.cyc.h_proyecto_financiadoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import co.uniandes.sisinfo.serviciosnegocio.ConstanteRemote;
import co.uniandes.sisinfo.serviciosnegocio.HistoricoCargaYCompromisosBeanRemote;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

/**
 *
 * @author Asistente, Marcela Morales
 */
@Stateless
public class HistoricoCargaYCompromisosBean implements HistoricoCargaYCompromisosBeanRemote, HistoricoCargaYCompromisosBeanLocal {

    @EJB
    private ConstanteRemote constanteBean;
    @EJB
    private h_cargaProfesor_cycFacadeLocal cargaProfesorFacade;
    @EJB
    private h_periodo_planeacionFacadeLocal periodoPlaneacionFacade;
    @EJB
    private h_curso_planeadoFacadeLocal cursoFacade;
    @EJB
    private h_intencion_publicacionFacadeLocal publicacionFacade;
    @EJB
    private h_direccion_tesisFacadeLocal direccionTesisFacade;
    @EJB
    private h_proyecto_financiadoFacadeLocal proyectoFinanciadoFacade;
    @EJB
    private h_otras_actividadesFacadeLocal otrasActividadesFacade;
    @EJB
    private h_eventoFacadeLocal eventoFacade;
    private ParserT parser;
    private ServiceLocator serviceLocator;

    //-CONSTRUCTOR
    public HistoricoCargaYCompromisosBean() {
        try {
            parser = new ParserT();
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
        } catch (NamingException ex) {
            Logger.getLogger(HistoricoCargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String consultarCargaPorCorreoPeriodo(String xml) {
        try {
            parser.leerXML(xml);

            Secuencia secCorreo = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
            Secuencia secPeriodo = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO));
            if (secCorreo != null && secPeriodo != null) {
                String correo = secCorreo.getValor();
                String periodo = secPeriodo.getValor();

                h_cargaProfesor_cyc hCargaProfesor = cargaProfesorFacade.findCargaByCorreoProfesorYNombrePeriodo(correo, periodo);
                if (hCargaProfesor != null) {
                    Secuencia secCargaProfesor = crearSecuenciaCargaProfesor(hCargaProfesor);
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    secuencias.add(secCargaProfesor);

                    return parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_CARGAYCOMPROMISOS_PROFESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CYC_MSJ_0000, new ArrayList());
                }

                Secuencia secCargaProfesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGA_PROFESOR), "");
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                secuencias.add(secCargaProfesor);

                return parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_CARGAYCOMPROMISOS_PROFESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CYC_MSJ_0000, new ArrayList());
            } else {
                throw new Exception("Debe incluir los parámetros correo y período de la consulta");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "EXCEPCIÓN: " + ex.getMessage();
        }
    }

    public String consultarPeriodos(String xml) {
        try {
            Secuencia secPeriodos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODOS), "");
            Collection<h_periodo_planeacion> perios = periodoPlaneacionFacade.findAll();
            if (perios != null) {
                for (h_periodo_planeacion periodoPlaneacion : perios) {
                    Secuencia secP = getSecuenciaPeriodo(periodoPlaneacion);
                    secPeriodos.agregarSecuencia(secP);
                }
            } else {
                // ////System.out.println("LA COLECCION DE PERIODOS LLEGO  EN NULL");
            }

            ArrayList<Secuencia> secs = new ArrayList<Secuencia>();
            secs.add(secPeriodos);
            return parser.generarRespuesta(secs, getConstanteBean().getConstante(Constantes.CMD_DAR_PERIODOS_PLANEACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CYC_MSJ_0000, new ArrayList());
        } catch (Exception ex) {
            /*  ////System.out.println("EXCEPTION EN METODO DAR PERIODOS HISTORICOS");
            ex.printStackTrace();*/
            return "EXCEPCIÓN: " + ex.getMessage();
        }
    }

    //MÉTODOS AUXILIARES PARA PROCESAMIENTO DE XML
    public Secuencia crearSecuenciaCargaProfesor(h_cargaProfesor_cyc carga) {
        Secuencia secPrincipalCarga = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGA_PROFESOR), "");
        Secuencia secid = carga.getId() != null
                ? new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), carga.getId().toString())
                : null;
        if (secid != null) {
            secPrincipalCarga.agregarSecuencia(secid);
        }
        Secuencia secMaximoNivelTesis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MAXIMO_NIVEL_TESIS), carga.getMaximoNivelTesis());
        secPrincipalCarga.agregarSecuencia(secMaximoNivelTesis);

        Secuencia secPeriodo = getSecuenciaPeriodo(carga.getPeriodoPlaneacion());//1
        Secuencia secInfoBasica = getSecuenciaInformacionBasica(carga);
        Secuencia secInfoCursos = getSecuenciaInformacionCursos(carga.getCursos());//3
        Secuencia secInfoPublicaciones = getSecuenciaInfoPublicaciones(carga.getIntencionPublicaciones());
        Secuencia secInfoTesiss = getSecuenciaInfoTesises(carga.getTesisAcargo());//5
        Secuencia secProyectosFinanciados = getSecuenciaProyectosFinanciados(carga.getProyectosFinanciados());
        Secuencia secOtros = getSecuenciaOtroses(carga.getOtros());//7
        Secuencia secDescarga = getSecuenciaDescarga(carga.getDescarga());

        secPrincipalCarga.agregarSecuencia(secPeriodo);
        secPrincipalCarga.agregarSecuencia(secInfoBasica);
        secPrincipalCarga.agregarSecuencia(secInfoCursos);
        secPrincipalCarga.agregarSecuencia(secInfoPublicaciones);
        secPrincipalCarga.agregarSecuencia(secInfoTesiss);
        secPrincipalCarga.agregarSecuencia(secProyectosFinanciados);
        secPrincipalCarga.agregarSecuencia(secOtros);
        secPrincipalCarga.agregarSecuencia(secDescarga);

        return secPrincipalCarga;
    }

    private Secuencia getSecuenciaPeriodo(h_periodo_planeacion periodoPlaneacion) {
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
            ////System.out.println(secid.toString());
        }
        secPrincipalCarga.agregarSecuencia(secestado);
        secPrincipalCarga.agregarSecuencia(secperiodo);
        secPrincipalCarga.agregarSecuencia(secInicioFecha);
        secPrincipalCarga.agregarSecuencia(secFInFecha);

        return secPrincipalCarga;
    }

    private Secuencia getSecuenciaInformacionBasica(h_cargaProfesor_cyc carga) {

        Secuencia secPrincipalCarga = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR), "");

        Secuencia secNombres = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), carga.getNombres());
        Secuencia secApell = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), carga.getApellidos());
        Secuencia secCorreo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), carga.getCorreo());

        secPrincipalCarga.agregarSecuencia(secNombres);
        secPrincipalCarga.agregarSecuencia(secApell);
        secPrincipalCarga.agregarSecuencia(secCorreo);

        return secPrincipalCarga;
    }

    private Secuencia getSecuenciaInformacionCursos(Collection<h_curso_planeado> cursos) {

        Secuencia secPrincipalCarga = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_CURSOS), "");
        for (h_curso_planeado cursoPlaneado : cursos) {
            Secuencia secCurso = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_CURSO), "");
            Secuencia secidCurso = cursoPlaneado.getId() != null
                    ? new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), cursoPlaneado.getId().toString())
                    : null;
            Secuencia secNombreCurso = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CURSO), cursoPlaneado.getNombreCurso());
            Secuencia secNivelFormacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_FORMACION), cursoPlaneado.getNivelDelCurso());
            Secuencia secobservaciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OBSERVACIONES), cursoPlaneado.getObservaciones());
            Secuencia secCargaCurso = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGA_CURSO), cursoPlaneado.getCarga() + "");

            if (secidCurso != null) {
                secCurso.agregarSecuencia(secidCurso);
                ////System.out.println(secidCurso.toString());
            }
            secCurso.agregarSecuencia(secNombreCurso);
            secCurso.agregarSecuencia(secNivelFormacion);
            secCurso.agregarSecuencia(secobservaciones);
            secCurso.agregarSecuencia(secCargaCurso);
            secPrincipalCarga.agregarSecuencia(secCurso);
        }
        return secPrincipalCarga;
    }

    private Secuencia getSecuenciaInfoTesises(Collection<h_direccion_tesis> tesisAcargo) {

        Secuencia secPrincipalCarga = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_TESISES), "");
        for (h_direccion_tesis direccionTesis : tesisAcargo) {
            Secuencia secinfoTesis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_TESIS), "");

            Secuencia secidTEsis = direccionTesis.getId() != null
                    ? new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), direccionTesis.getId().toString())
                    : null;
            Secuencia secNombreEstudiante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), direccionTesis.getNombreEstudiante());
            Secuencia secApellidosEstudiante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), direccionTesis.getApellidoEstudiante());
            Secuencia seccorreoEstudiante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), direccionTesis.getCorreoEstudiante());
            Secuencia secEstadoTEsis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_ESTADO_TESIS), direccionTesis.getNivelEstadoTesis());
            Secuencia secNivelFormacionTEsis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_FORMACION_TESIS), direccionTesis.getNivelFormacionTesis());
            Secuencia secObservaciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OBSERVACIONES), direccionTesis.getObservaciones());
            Secuencia secTitulo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TITULO), direccionTesis.getTitulo());

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

            secPrincipalCarga.agregarSecuencia(secinfoTesis);
        }

        return secPrincipalCarga;

    }

    private Secuencia getSecuenciaInfoPublicaciones(Collection<h_intencion_publicacion> intencionPublicaciones) {

        Secuencia secPrincipalCarga = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_PUBLICACIONES), "");
        for (h_intencion_publicacion cursoPlaneado : intencionPublicaciones) {
            Secuencia secInfoPublicacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_PUBLICACION), "");
            Secuencia ssecTituloPub = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TITULO), cursoPlaneado.getTituloPublicacion());
            Secuencia ssecobserva = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OBSERVACIONES), cursoPlaneado.getObservaciones());
            Secuencia secTipoPublicacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_PUBLICACION), cursoPlaneado.getTipoPublicacion());
            Secuencia secIdPublic = cursoPlaneado.getId() != null ? new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_PUBLICACION), cursoPlaneado.getId().toString()) : null;
            Collection<h_cargaProfesor_cyc> temp = cursoPlaneado.getCoAutores();
            Secuencia secCooAutroes = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COAUTORES), "");
            for (h_cargaProfesor_cyc carga : temp) {
                Secuencia secAutor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COAUTOR), "");
                Secuencia secNombres = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), carga.getNombres());
                Secuencia secApell = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), carga.getApellidos());
                Secuencia secCorreo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), carga.getCorreo());
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

    private Secuencia getSecuenciaProyectosFinanciados(Collection<h_proyecto_financiado> proyectosFinanciados) {

        Secuencia secPrincipalCarga = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_PROYECTOS_FINANCIADOS), "");
        for (h_proyecto_financiado proyectoFinanciado : proyectosFinanciados) {
            Secuencia secPoyecto = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_PROYECTO_FINANCIADO), "");
            Secuencia secidPoyecto = proyectoFinanciado == null ? null : new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), proyectoFinanciado.getId().toString());
            Secuencia secNombrePoyecto = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), proyectoFinanciado.getNombre());
            Secuencia secEntidadFinanciadora = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ENTIDAD_FINICIADORA), proyectoFinanciado.getEntidadFinanciadora());
            Secuencia secObservaciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION), proyectoFinanciado.getDescripcion());
            Secuencia secColaboradores = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COLABORADORES), "");
            Collection<h_cargaProfesor_cyc> colaboradores = proyectoFinanciado.getProfesores();
            for (h_cargaProfesor_cyc carga : colaboradores) {
                Secuencia secAutor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COLABORADOR), "");
                Secuencia secNombres = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), carga.getNombres());
                Secuencia secApell = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), carga.getApellidos());
                Secuencia secCorreo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), carga.getCorreo());
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

    private Secuencia getSecuenciaOtroses(Collection<h_otras_actividades> otros) {
        Secuencia secPrincipalCarga = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_OTROS), "");
        for (h_otras_actividades otrasActividades : otros) {
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

    private Secuencia getSecuenciaDescarga(String nombreDescarga) {
        Secuencia secPrincipalCarga = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_DESCARGA_PROFESOR), "");
        Secuencia secnombreCarga = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), nombreDescarga);
        secPrincipalCarga.agregarSecuencia(secnombreCarga);

        return secPrincipalCarga;

    }

    //MÉTODOS AUXILIARES PARA PROCESAMIENTO DE XML
    public ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    @Deprecated //como las migraciones ya no se hacen por ETL, sino por codigo, esto ya no se usa.
    public String pasarPeriodoPlaneacionAHistoricos(String xml) {

        return null;
    }

    public String pasarCargasProfesoresAHistoricos(String xml) {
        try {
            getParser().leerXML(xml);
            ////System.out.println("ESTE EL EL XML QUE LLEGA A HISTORICOS: \n" + xml);
            Secuencia secCargasProfesores = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGA_GRUPO));
            ArrayList<Secuencia> secCargas = secCargasProfesores.getSecuencias();
            HashMap<String, h_cargaProfesor_cyc> cargas = new HashMap<String, h_cargaProfesor_cyc>();
            for (Iterator<Secuencia> it = secCargas.iterator(); it.hasNext();) {
                Secuencia secCarga = it.next();
                h_cargaProfesor_cyc carga = crearHistoricoCargaBasica(secCarga);
                if(carga != null)
                    cargas.put(carga.getCorreo(), carga);
            }
            for (Secuencia secuencia : secCargas) {
                String correo = null;
                Secuencia secInformacionBasica = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR));
                if (secInformacionBasica != null) {
                    Secuencia secCorreo = secInformacionBasica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
                    if (secCorreo != null) {
                        correo = secCorreo.getValor();
                    }
                }
                if (correo != null) {
                    h_cargaProfesor_cyc carga = cargas.get(correo);
                    if(carga != null){
                        carga = cargaProfesorFacade.findCargaByCorreoProfesorYNombrePeriodo(correo, carga.getPeriodoPlaneacion().getPeriodo());
                        pasarElRestoDeInformacionDeCargaAHistoricos(secuencia, carga);
                    }
                }
            }
            ArrayList<Secuencia> secs = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secs, getConstanteBean().getConstante(Constantes.CMD_MIGRAR_CARGAS_PROFESORES_A_HISTORICOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(HistoricoCargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private ParserT getParser() {
        return (parser == null) ? new ParserT() : parser;
    }

    private h_cargaProfesor_cyc crearHistoricoCargaBasica(Secuencia sec) {
        h_cargaProfesor_cyc carga = new h_cargaProfesor_cyc();

        // Crea una carga con la información básica
        String periodo = null;
        Secuencia secPeriodoPlaneacion = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO_PLANEACION));
        if (secPeriodoPlaneacion != null) {
            periodo = pasarInformacionPeriodoPlaneacion(secPeriodoPlaneacion);
        }
        Secuencia secInformacionBasica = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR));
        if (secInformacionBasica != null) {
            pasarInformacionBasicaAHistorica(secInformacionBasica, carga);
        }
        Secuencia secDescarga = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_DESCARGA_PROFESOR));
        if (secDescarga != null) {
            Secuencia secNombreDescarga = secDescarga.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE));
            if (secNombreDescarga != null) {
                carga.setDescarga(secNombreDescarga.getValor());
            }
        }
        Secuencia secMaximoNivelTesis = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MAXIMO_NIVEL_TESIS));
        if (secMaximoNivelTesis != null) {
            carga.setMaximoNivelTesis(secMaximoNivelTesis.getValor());
        }
        Secuencia secInfoCarga = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGA_GRUPO));
        if (secInfoCarga != null) {
            pasarInformacionCargaAHistorica(secInfoCarga, carga);
        }
        carga.setCursos(new ArrayList<h_curso_planeado>());
        carga.setIntencionPublicaciones(new ArrayList<h_intencion_publicacion>());
        carga.setTesisAcargo(new ArrayList<h_direccion_tesis>());
        carga.setProyectosFinanciados(new ArrayList<h_proyecto_financiado>());
        carga.setOtros(new ArrayList<h_otras_actividades>());
        carga.setEventos(new ArrayList<h_evento>());
        carga.setId(null);

        //Valida si la carga ya existe
        h_cargaProfesor_cyc cargaExiste = cargaProfesorFacade.findCargaByCorreoProfesorYNombrePeriodo(carga.getCorreo(), periodo);
        if(cargaExiste != null)
            return null;

        //Relaciona carga a periodo
        h_periodo_planeacion periodoBD = periodoPlaneacionFacade.findByNombre(periodo);
        carga.setPeriodoPlaneacion(periodoBD);
        cargaProfesorFacade.create(carga);

        //Asociar carga a periodo y al reves
        carga = cargaProfesorFacade.findCargaByCorreoProfesorYNombrePeriodo(carga.getCorreo(), periodo);
        Collection<h_cargaProfesor_cyc> listaCargas = periodoBD.getCargaProfesores();
        if (listaCargas == null) {
            listaCargas = new ArrayList<h_cargaProfesor_cyc>();
        }
        listaCargas.add(carga);
        periodoBD.setCargaProfesores(listaCargas);
        periodoPlaneacionFacade.edit(periodoBD);
        
        return carga;
    }

    private void pasarInformacionBasicaAHistorica(Secuencia secInformacionBasica, h_cargaProfesor_cyc carga) {
        Secuencia secNombres = secInformacionBasica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES));
        if (secNombres != null) {
            carga.setNombres(secNombres.getValor());
        }
        Secuencia secApellidos = secInformacionBasica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS));
        if (secApellidos != null) {
            carga.setApellidos(secApellidos.getValor());
        }
        Secuencia secCorreo = secInformacionBasica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
        if (secCorreo != null) {
            carga.setCorreo(secCorreo.getValor());
        }
    }

    private void pasarInformacionCursosAHistorica(Secuencia secInformacionCursos, h_cargaProfesor_cyc carga) {
        for (Secuencia secCurso : secInformacionCursos.getSecuencias()) {
            pasarInformacionCursoAHistorica(secCurso, carga);
        }
    }

    private void pasarInformacionCursoAHistorica(Secuencia secCurso, h_cargaProfesor_cyc carga) {
        h_curso_planeado curso = new h_curso_planeado();
        Secuencia secIdCurso = secCurso.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
        if (secIdCurso != null) {
            Long id = Long.parseLong(secIdCurso.getValor());
            curso.setId(id);
        }
        Secuencia secNombreCurso = secCurso.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CURSO));
        if (secNombreCurso != null) {
            curso.setNombreCurso(secNombreCurso.getValor());
        }
        Secuencia secNivelCurso = secCurso.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_FORMACION));
        if (secNivelCurso != null) {
            curso.setNivelDelCurso(secNivelCurso.getValor());
        }
        Secuencia secObservacionesCurso = secCurso.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OBSERVACIONES));
        if (secObservacionesCurso != null) {
            curso.setObservaciones(secObservacionesCurso.getValor());
        }
        Secuencia secCargaCurso = secCurso.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGA_CURSO));
        if (secCargaCurso != null) {
            Double cargaCurso = Double.parseDouble(secCargaCurso.getValor());
            curso.setCarga(cargaCurso);
        }
        curso.setProfesor(carga);
        curso.setId(null);
        cursoFacade.create(curso);

        Collection<h_curso_planeado> cursos = carga.getCursos();
        cursos.add(curso);
        carga.setCursos(cursos);
        cargaProfesorFacade.edit(carga);
    }

    private void pasarInformacionPublicacionesAHistorica(Secuencia secInformacionPublicaciones, h_cargaProfesor_cyc carga) {
        for (Secuencia secPublicacion : secInformacionPublicaciones.getSecuencias()) {
            pasarInformacionPublicacionAHistorica(secPublicacion, carga);
        }
    }

    private void pasarInformacionPublicacionAHistorica(Secuencia secPublicacion, h_cargaProfesor_cyc carga) {
        h_intencion_publicacion publicacion = new h_intencion_publicacion();
        Secuencia secTitulo = secPublicacion.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TITULO));
        if (secTitulo != null) {
            publicacion.setTituloPublicacion(secTitulo.getValor());
        }
        Secuencia secObservaciones = secPublicacion.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OBSERVACIONES));
        if (secObservaciones != null) {
            publicacion.setObservaciones(secObservaciones.getValor());
        }
        Secuencia secTipo = secPublicacion.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_PUBLICACION));
        if (secTipo != null) {
            publicacion.setTipoPublicacion(secTipo.getValor());
        }
        publicacion.setCoAutores(new ArrayList<h_cargaProfesor_cyc>());
        publicacion.setId(null);

        h_intencion_publicacion publicacionBD = publicacionFacade.findByTituloObservacionesTipo(publicacion.getTituloPublicacion(), publicacion.getObservaciones(), publicacion.getTipoPublicacion());

        if (publicacionBD == null) {
            publicacionFacade.create(publicacion);
        } else {
            publicacion = publicacionBD;
        }
        /*
         * 2 opciones la publicacion ya existe o no existe
         * toca bucarla y si existe asociar la carga a la publicacion
         * si no; crearla y asociar
         */
        Secuencia secAutores = secPublicacion.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COAUTORES));
        for (Secuencia secAutor : secAutores.getSecuencias()) {

            h_cargaProfesor_cyc coautor = null;
            Secuencia secCorreo = secAutor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
            if (secCorreo != null) {
                h_cargaProfesor_cyc lista = cargaProfesorFacade.findCargaByCorreoProfesorYNombrePeriodo(secCorreo.getValor(), carga.getPeriodoPlaneacion().getPeriodo());
                coautor = lista;
            }
            //Si el coautor no existía, asocia las propiedades y lo crea
            if (coautor == null) {
                coautor = new h_cargaProfesor_cyc();
                Secuencia secNombres = secAutor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES));
                if (secNombres != null) {
                    coautor.setNombres(secNombres.getValor());
                }
                Secuencia secApellidos = secAutor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS));
                if (secApellidos != null) {
                    coautor.setApellidos(secApellidos.getValor());
                }
                Secuencia secEmail = secAutor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
                if (secEmail != null) {
                    coautor.setCorreo(secEmail.getValor());
                }
                coautor.setId(null);
                cargaProfesorFacade.create(coautor);
            }

            Collection<h_cargaProfesor_cyc> coautores = publicacion.getCoAutores();
            coautores.add(coautor);
            publicacion.setCoAutores(coautores);
            publicacionFacade.edit(publicacion);
        }

        Collection<h_intencion_publicacion> publicaciones = carga.getIntencionPublicaciones();
        publicaciones.add(publicacion);
        carga.setIntencionPublicaciones(publicaciones);
        cargaProfesorFacade.edit(carga);
    }

    private void pasarInformacionTesisesAHistorica(Secuencia secInformacionTesises, h_cargaProfesor_cyc carga) {
        for (Secuencia secTesis : secInformacionTesises.getSecuencias()) {
            pasarInformacionTesisAHistorica(secTesis, carga);
        }
    }

    private void pasarInformacionTesisAHistorica(Secuencia secTesis, h_cargaProfesor_cyc carga) {
        h_direccion_tesis tesis = new h_direccion_tesis();

        Secuencia secNombreEstudiante = secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES));
        if (secNombreEstudiante != null) {
            tesis.setNombreEstudiante(secNombreEstudiante.getValor());
        }
        Secuencia secApellidos = secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS));
        if (secApellidos != null) {
            tesis.setApellidoEstudiante(secApellidos.getValor());
        }
        Secuencia secCorreo = secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
        if (secCorreo != null) {
            tesis.setCorreoEstudiante(secCorreo.getValor());
        }
        Secuencia secEstado = secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_ESTADO_TESIS));
        if (secEstado != null) {
            tesis.setNivelEstadoTesis(secEstado.getValor());
        }
        Secuencia secNivel = secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_FORMACION_TESIS));
        if (secNivel != null) {
            tesis.setNivelFormacionTesis(secNivel.getValor());
        }
        Secuencia secObservaciones = secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OBSERVACIONES));
        if (secObservaciones != null) {
            tesis.setObservaciones(secObservaciones.getValor());
        }
        Secuencia secTitulo = secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TITULO));
        if (secTitulo != null) {
            tesis.setTitulo(secTitulo.getValor());
        }
        tesis.setDirectorTesis(carga);
        tesis.setId(null);
        direccionTesisFacade.create(tesis);

        Collection<h_direccion_tesis> tesises = carga.getTesisAcargo();
        tesises.add(tesis);
        carga.setTesisAcargo(tesises);
        cargaProfesorFacade.edit(carga);
    }

    private void pasarInformacionProyectosFinanciadosAHistorica(Secuencia secInformacionProyectosFinanciados, h_cargaProfesor_cyc carga) {
        for (Secuencia secProyecto : secInformacionProyectosFinanciados.getSecuencias()) {
            pasarInformacionProyectoFinanciadoAHistorica(secProyecto, carga);
        }
    }

    private void pasarInformacionProyectoFinanciadoAHistorica(Secuencia secProyecto, h_cargaProfesor_cyc carga) {
        h_proyecto_financiado proyecto = new h_proyecto_financiado();
        Secuencia secNombres = secProyecto.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE));
        if (secNombres != null) {
            proyecto.setNombre(secNombres.getValor());
        }
        Secuencia secEntidadFinanciadora = secProyecto.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ENTIDAD_FINICIADORA));
        if (secEntidadFinanciadora != null) {
            proyecto.setEntidadFinanciadora(secEntidadFinanciadora.getValor());
        }
        Secuencia secDescripcion = secProyecto.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION));
        if (secDescripcion != null) {
            proyecto.setDescripcion(secDescripcion.getValor());
        }
        proyecto.setPeriodo(carga.getPeriodoPlaneacion().getPeriodo());
        proyecto.setProfesores(new ArrayList<h_cargaProfesor_cyc>());
        proyecto.setId(null);

        h_proyecto_financiado proyectoBD = proyectoFinanciadoFacade.findByNombreEntidadDescripcionYPeriodo(proyecto.getNombre(), proyecto.getEntidadFinanciadora(), proyecto.getDescripcion(), carga.getPeriodoPlaneacion().getPeriodo());
        if (proyectoBD == null) {
            proyectoFinanciadoFacade.create(proyecto);
        } else {
            proyecto = proyectoBD;
        }

        Secuencia secColaboradores = secProyecto.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COLABORADORES));
        for (Secuencia secColaborador : secColaboradores.getSecuencias()) {

            h_cargaProfesor_cyc colaborador = null;
            Secuencia secCorreo = secColaborador.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
            if (secCorreo != null) {
                h_cargaProfesor_cyc lista = cargaProfesorFacade.findCargaByCorreoProfesorYNombrePeriodo(secCorreo.getValor(), carga.getPeriodoPlaneacion().getPeriodo());
                colaborador = lista;
            }
            //Si el colaborador no existía, asocia las propiedades y lo crea
            if (colaborador == null) {
                colaborador = new h_cargaProfesor_cyc();
                Secuencia secNombresColaborador = secColaborador.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES));
                if (secNombresColaborador != null) {
                    colaborador.setNombres(secNombresColaborador.getValor());
                }
                Secuencia secApellidos = secColaborador.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS));
                if (secApellidos != null) {
                    colaborador.setApellidos(secApellidos.getValor());
                }
                Secuencia secEmail = secColaborador.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
                if (secEmail != null) {
                    colaborador.setCorreo(secEmail.getValor());
                }
                colaborador.setId(null);
                cargaProfesorFacade.create(colaborador);
            }

            Collection<h_cargaProfesor_cyc> colaboradores = proyecto.getProfesores();
            colaboradores.add(colaborador);
            proyecto.setProfesores(colaboradores);
            proyectoFinanciadoFacade.edit(proyecto);
        }

        Collection<h_proyecto_financiado> proyectosFinanciados = carga.getProyectosFinanciados();
        proyectosFinanciados.add(proyecto);
        carga.setProyectosFinanciados(proyectosFinanciados);
        cargaProfesorFacade.edit(carga);
    }

    private void pasarInformacionOtrosAHistorica(Secuencia secOtros, h_cargaProfesor_cyc carga) {
        for (Secuencia secOtro : secOtros.getSecuencias()) {
            pasarInformacionOtroAHistorica(secOtro, carga);
        }
    }

    private void pasarInformacionOtroAHistorica(Secuencia secOtro, h_cargaProfesor_cyc carga) {
        h_otras_actividades otro = new h_otras_actividades();
        Secuencia secId = secOtro.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
        if (secId != null) {
            Long id = Long.parseLong(secId.getValor());
            otro.setId(id);
        }
        Secuencia secNombre = secOtro.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE));
        if (secNombre != null) {
            otro.setNombre(secNombre.getValor());
        }
        Secuencia secDescripcion = secOtro.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION));
        if (secDescripcion != null) {
            otro.setDescripcion(secDescripcion.getValor());
        }
        Secuencia secDedicacionSemanal = secOtro.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HORAS_DEDICACION_SEMANAL));
        if (secDedicacionSemanal != null) {
            Double dedicacionSemanal = Double.parseDouble(secDedicacionSemanal.getValor());
            otro.setDedicacionSemanal(dedicacionSemanal);
        }
        otro.setId(null);
        otrasActividadesFacade.create(otro);

        Collection<h_otras_actividades> otrasActividades = carga.getOtros();
        otrasActividades.add(otro);
        carga.setOtros(otrasActividades);
        cargaProfesorFacade.edit(carga);
    }

    private void pasarInformacionCargaAHistorica(Secuencia secInfoCarga, h_cargaProfesor_cyc carga) {
        Secuencia secCargaEfectiva = secInfoCarga.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGA_EFECTIVA));
        if (secCargaEfectiva != null) {
            carga.setCargaEfectiva(secCargaEfectiva.getValorDouble());
        }
        Secuencia secCargaProfesor = secInfoCarga.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGA_PROFESOR));
        if (secCargaProfesor != null) {
            carga.setCargaProfesor(secCargaProfesor.getValorDouble());
        }
        Secuencia secNumCapitulosLibro = secInfoCarga.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_CAPITULOS_LIBRO));
        if (secNumCapitulosLibro != null) {
            carga.setNumCapitulosLibro(secNumCapitulosLibro.getValorInt());
        }
        Secuencia secNumCongresosInternacio = secInfoCarga.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_CONGRESOS_INTERNACIONALES));
        if (secNumCongresosInternacio != null) {
            carga.setNumCongresosInternacio(secNumCongresosInternacio.getValorInt());
        }
        Secuencia secNumCongresosNacio = secInfoCarga.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_CONGRESOS_NACIONALES));
        if (secNumCongresosNacio != null) {
            carga.setNumCongresosNacionales(secNumCongresosNacio.getValorInt());
        }
        Secuencia secCursosPreMas = secInfoCarga.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_CURSOS));
        if (secCursosPreMas != null) {
            carga.setNumCursosPregradoMaestria(secCursosPreMas.getValorInt());
        }
        Secuencia secNumRevistas = secInfoCarga.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_REVISTAS));
        if (secNumRevistas != null) {
            carga.setNumRevistas(secNumRevistas.getValorInt());
        }
        Secuencia secDoctorado = secInfoCarga.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_ESTUDIANTES_DOCTORADO));
        if (secDoctorado != null) {
            carga.setNumeroEstudiantesDoctorado(secDoctorado.getValorInt());
        }
        Secuencia secPregrado = secInfoCarga.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_ESTUDIANTES_PREGRADO));
        if (secPregrado != null) {
            carga.setNumeroEstudiantesPregrado(secPregrado.getValorInt());
        }
        Secuencia secTesis1 = secInfoCarga.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_ESTUDIANTES_TESIS1));
        if (secTesis1 != null) {
            carga.setNumeroEstudiantesTesis1(secTesis1.getValorInt());
        }
        Secuencia secTesis2 = secInfoCarga.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_ESTUDIANTES_TESIS2));
        if (secTesis2 != null) {
            carga.setNumeroEstudiantesTesis2(secTesis2.getValorInt());
        }
        Secuencia secTesis2Pendiente = secInfoCarga.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_ESTUDIANTES_TESIS2_PENDIENTE));
        if (secTesis2Pendiente != null) {
            carga.setNumeroEstudiantesTesis2Pendiente(secTesis2Pendiente.getValorInt());
        }
        Secuencia secProyectos = secInfoCarga.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_PROYECTOS_FINANCIADOS));
        if (secProyectos != null) {
            carga.setNumeroProyectosFinanciados(secProyectos.getValorInt());
        }
        Secuencia secPublicaciones = secInfoCarga.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_PUBLICACIONES_PLANEADAS));
        if (secPublicaciones != null) {
            carga.setNumeroPublicacionesPlaneadas(secPublicaciones.getValorInt());
        }
    }

    private String pasarInformacionPeriodoPlaneacion(Secuencia secPeriodoPlaneacion) {

        h_periodo_planeacion periodo = new h_periodo_planeacion();
        Secuencia secPeriodo = secPeriodoPlaneacion.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO));
        Boolean yaExisteElPeriodo = false;
        if (secPeriodo != null) {
            ////System.out.println("El periodo que esta buscando=" + secPeriodo.getNombre() + "-" + secPeriodo.getValor());
            h_periodo_planeacion existeEnBD = periodoPlaneacionFacade.findByNombre(secPeriodo.getValor());
            if (existeEnBD != null) {
                yaExisteElPeriodo = true;
//                Collection<h_cargaProfesor_cyc> profesores = existeEnBD.getCargaProfesores();
//                profesores.add(carga);
//                periodo.setCargaProfesores(profesores);
//                periodoPlaneacionFacade.edit(periodo);
//                carga.setPeriodoPlaneacion(periodo);
//                cargaProfesorFacade.edit(carga);
            }
            periodo.setPeriodo(secPeriodo.getValor());
        }
        if (!yaExisteElPeriodo) {
            Secuencia secFechaInicio = secPeriodoPlaneacion.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_INICIO));
            if (secFechaInicio != null) {
                String fechaInicio = secFechaInicio.getValor();
                try {
                    SimpleDateFormat formatoDeFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date fecha = formatoDeFecha.parse(fechaInicio);
                    periodo.setFechaInicio(new Timestamp(fecha.getTime()));
                } catch (ParseException ex) {
                    Logger.getLogger(HistoricoCargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            Secuencia secFechaFin = secPeriodoPlaneacion.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN));
            if (secFechaFin != null) {
                String fechaFin = secFechaFin.getValor();
                try {
                    SimpleDateFormat formatoDeFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date fecha = formatoDeFecha.parse(fechaFin);
                    periodo.setFechaFin(new Timestamp(fecha.getTime()));
                } catch (ParseException ex) {
                    Logger.getLogger(HistoricoCargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            Secuencia secEstado = secPeriodoPlaneacion.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO));
            if (secEstado != null) {
                boolean esActual = secEstado.getValor().equals(getConstanteBean().getConstante(Constantes.ESTADO_ABIERTA));
                periodo.setActual(esActual);
            }
//            Collection<h_cargaProfesor_cyc> profesores = new ArrayList<h_cargaProfesor_cyc>();
//            profesores.add(carga);
//            periodo.setCargaProfesores(profesores);   TODO: borrar anteriores lineas
            periodo.setId(null);
            periodoPlaneacionFacade.create(periodo);

//            carga.setPeriodoPlaneacion(periodo);
//            cargaProfesorFacade.edit(carga);
        }

        return periodo.getPeriodo();
    }

    private void pasarInformacionEventosAHistorica(Secuencia secEventos, h_cargaProfesor_cyc carga) {
        for (Secuencia secEvento : secEventos.getSecuencias()) {
            pasarInformacionEventoAHistorica(secEvento, carga);
        }
    }

    private void pasarInformacionEventoAHistorica(Secuencia secEvento, h_cargaProfesor_cyc carga) {
        h_evento evento = new h_evento();
        Secuencia secId = secEvento.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
        if (secId != null) {
            evento.setId(secId.getValorLong());
        }
        Secuencia secNombre = secEvento.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE));
        if (secNombre != null) {
            evento.setNombre(secNombre.getValor());
        }
        Secuencia secObservaciones = secEvento.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OBSERVACIONES));
        if (secObservaciones != null) {
            evento.setObservaciones(secObservaciones.getValor());
        }
        evento.setProfesor(carga);
        evento.setId(null);
        eventoFacade.create(evento);

        Collection<h_evento> eventos = carga.getEventos();
        eventos.add(evento);
        carga.setEventos(eventos);
        cargaProfesorFacade.edit(carga);
    }

    public void pasarElRestoDeInformacionDeCargaAHistoricos(Secuencia sec, h_cargaProfesor_cyc carga) {

        Secuencia secInformacionCursos = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_CURSOS));
        if (secInformacionCursos != null) {
            pasarInformacionCursosAHistorica(secInformacionCursos, carga);
        }
        Secuencia secInformacionPublicaciones = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_PUBLICACIONES));
        if (secInformacionPublicaciones != null) {
            pasarInformacionPublicacionesAHistorica(secInformacionPublicaciones, carga);
        }
        Secuencia secInformacionTesises = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_TESISES));
        if (secInformacionTesises != null) {
            pasarInformacionTesisesAHistorica(secInformacionTesises, carga);
        }
        Secuencia secProyectosFinanciados = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_PROYECTOS_FINANCIADOS));
        if (secProyectosFinanciados != null) {
            pasarInformacionProyectosFinanciadosAHistorica(secProyectosFinanciados, carga);
        }
        Secuencia secOtros = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_OTROS));
        if (secOtros != null) {
            pasarInformacionOtrosAHistorica(secOtros, carga);
        }
        Secuencia secEventos = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_EVENTOS));
        if (secEventos != null) {
            pasarInformacionEventosAHistorica(secEventos, carga);
        }
    }
}
