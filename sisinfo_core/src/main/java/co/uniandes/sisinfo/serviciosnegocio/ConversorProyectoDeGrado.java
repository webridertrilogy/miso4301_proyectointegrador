package co.uniandes.sisinfo.serviciosnegocio;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.entities.CategoriaProyectoDeGrado;
import co.uniandes.sisinfo.entities.ComentarioTesisPregrado;
import co.uniandes.sisinfo.entities.PeriodoTesisPregrado;
import co.uniandes.sisinfo.entities.ProyectoDeGrado;
import co.uniandes.sisinfo.entities.TemaTesisPregrado;
import co.uniandes.sisinfo.entities.datosmaestros.Estudiante;
import co.uniandes.sisinfo.entities.datosmaestros.Profesor;
import co.uniandes.sisinfo.serviciosfuncionales.CategoriaProyectoDeGradoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.PeriodoTesisPregradoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.EstudianteFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.ProfesorFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;

/**
 * Conversor del módulo Proyecto de Grado
 * @author Marcela Morales
 */
public class ConversorProyectoDeGrado {

    private ConstanteRemote constanteBean;
    private ProfesorFacadeRemote profesorFacade;
    private CategoriaProyectoDeGradoFacadeLocal categoriaTesisFacade;
    private PeriodoTesisPregradoFacadeLocal periodoFacadelocal;
    private EstudianteFacadeRemote estudianteFacadeRemote;

    public ConversorProyectoDeGrado(ConstanteRemote constanteBean, ProfesorFacadeRemote profesorFacade, CategoriaProyectoDeGradoFacadeLocal categoriaTesisFacade,
            PeriodoTesisPregradoFacadeLocal periodoFacadelocal, EstudianteFacadeRemote estudianteFacadeRemote) {
        this.constanteBean = constanteBean;
        this.profesorFacade = profesorFacade;
        this.categoriaTesisFacade = categoriaTesisFacade;
        this.periodoFacadelocal = periodoFacadelocal;
        this.estudianteFacadeRemote = estudianteFacadeRemote;
    }

    public TemaTesisPregrado pasarSecuenciaATemaTesisPregrado(Secuencia secTemaTesis) {
        TemaTesisPregrado tema = new TemaTesisPregrado();
        if (secTemaTesis.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)) != null) {
            tema.setId(Long.parseLong(secTemaTesis.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)).getValor().trim()));
        }
        if (secTemaTesis.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE)) != null) {
            tema.setNombreTema(secTemaTesis.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE)).getValor());
        }
        if (secTemaTesis.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION)) != null) {
            tema.setDescripcion(secTemaTesis.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION)).getValor());
        }
        if (secTemaTesis.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_AREA)) != null) {
            tema.setAreasInteres(secTemaTesis.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_AREA)).getValor());
        }
        if (secTemaTesis.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_CANDIDATOS)) != null) {
            tema.setNumeroEstudiantes(Integer.parseInt(secTemaTesis.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_CANDIDATOS)).getValor()));
        }
        if (secTemaTesis.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE)) != null) {
            PeriodoTesisPregrado periodo = pasarSecuenciaAPeriodo(secTemaTesis.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE)));
            tema.setPeriodo(periodo);
        }
        if (secTemaTesis.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASESOR_TESIS)) != null) {
            Profesor periodo = pasarSecuenciaAProfesor(secTemaTesis.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASESOR_TESIS)));
            tema.setAsesor(periodo);
        }
        if (secTemaTesis.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_CATEGORIA_PROYECTO_DE_GRADO)) != null) {
            CategoriaProyectoDeGrado periodo = pasarSecuenciaACategoriaProyectoDeGrado(secTemaTesis.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_CATEGORIA_PROYECTO_DE_GRADO)));
            tema.setCategoria(periodo);
        }
        return tema;
    }

    public Secuencia pasarTemasTesisPregradoASecuencia(Collection<TemaTesisPregrado> lista) {
        Secuencia secTemasTesis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TEMAS_TESIS), null);
        for (TemaTesisPregrado temaTesisPregrado : lista) {
            Secuencia secTemaTesis = pasarTemaTesisPregradoASecuencia(temaTesisPregrado);
            secTemasTesis.agregarSecuencia(secTemaTesis);
        }
        return secTemasTesis;
    }

    public Secuencia pasarTemaTesisPregradoASecuencia(TemaTesisPregrado tema) {
        Secuencia secTemaTesis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TEMA_TESIS), null);
        if (tema.getId() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), tema.getId().toString());
            secTemaTesis.agregarSecuencia(secId);
        }
        if (tema.getNombreTema() != null) {
            secTemaTesis.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), tema.getNombreTema()));
        }
        if (tema.getDescripcion() != null) {
            secTemaTesis.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION), tema.getDescripcion()));
        }
        if (tema.getAreasInteres() != null) {
            secTemaTesis.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_AREA), tema.getAreasInteres()));
        }
        if (tema.getNumeroEstudiantes() != null) {
            secTemaTesis.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_CANDIDATOS), tema.getNumeroEstudiantes().toString()));
        }
        if (tema.getPeriodo() != null) {
            Secuencia secPeriodo = pasarPeriodoPregradoASecuencia(tema.getPeriodo());
            secTemaTesis.agregarSecuencia(secPeriodo);
        }
        if (tema.getAsesor() != null) {
            Secuencia secAsesor = pasarProfesorASecuencia(tema.getAsesor());
            secTemaTesis.agregarSecuencia(secAsesor);
        }
        if (tema.getCategoria() != null) {
            Secuencia secCategoria = pasarCategoriaProyectoDeGradoASecuencia(tema.getCategoria());
            secTemaTesis.agregarSecuencia(secCategoria);
        }
        return secTemaTesis;
    }

    public Profesor pasarSecuenciaAProfesor(Secuencia secuencia) {
        Long id = secuencia.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)) != null
                ? Long.parseLong(secuencia.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)).getValor().trim())
                : null;
        if (id != null) {
            Profesor profesor = profesorFacade.find(id);
            return profesor;
        } else {
            String correo = secuencia.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)) != null
                    ? secuencia.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor().trim()
                    : null;
            if (correo != null) {
                Profesor profesor = profesorFacade.findByCorreo(correo);
                return profesor;
            }
            return null;
        }
    }

    public CategoriaProyectoDeGrado pasarSecuenciaACategoriaProyectoDeGrado(Secuencia sec) {
        Secuencia secId = sec.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
        if (secId != null) {
            return categoriaTesisFacade.find(Long.parseLong(secId.getValor().trim()));
        }
        return null;
    }

    public Secuencia pasarProfesorASecuencia(Profesor profesor) {
        Secuencia secEstudiante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR), "");
        
        secEstudiante.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), (profesor.getId() != null) ? profesor.getId().toString() : ""));
        secEstudiante.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), (profesor.getPersona() != null && profesor.getPersona().getNombres() != null) ? profesor.getPersona().getNombres() : ""));
        secEstudiante.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), (profesor.getPersona() != null && profesor.getPersona().getApellidos() != null) ? profesor.getPersona().getApellidos() : ""));
        secEstudiante.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), (profesor.getPersona() != null && profesor.getPersona().getCorreo() != null) ? profesor.getPersona().getCorreo() : ""));

        return secEstudiante;
    }

    public Secuencia pasarCategoriaProyectoDeGradoASecuencia(CategoriaProyectoDeGrado tema) {
        Secuencia secCategoria = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CATEGORIA_PROYECTO_DE_GRADO), null);
        if (tema.getId() != null) {
            secCategoria.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), tema.getId().toString()));
        }
        if (tema.getNombre() != null) {
            secCategoria.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), tema.getNombre()));
        }
        if (tema.getDescripcion() != null) {
            secCategoria.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION), tema.getDescripcion()));
        }
        return secCategoria;
    }

    public Secuencia pasarProyectosDeGradoASecuencia(Collection<ProyectoDeGrado> proyectos) {
        Secuencia secProyectos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROYECTOS_DE_GRADO), "");
        for (ProyectoDeGrado proyectoDeGrado : proyectos) {
            Secuencia secProyecto = pasarProyectoDeGradoASecuencia(proyectoDeGrado);
            secProyectos.agregarSecuencia(secProyecto);
        }
        return secProyectos;
    }

    public PeriodoTesisPregrado pasarSecuenciaAPeriodo(Secuencia secPeriodo) {
        Secuencia secid = secPeriodo.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
        if (secid != null) {
            PeriodoTesisPregrado periodo = periodoFacadelocal.find(Long.parseLong(secid.getValor().trim()));
            return periodo;
        } else {
            Secuencia secNombre = secPeriodo.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE));
            if (secNombre != null) {
                PeriodoTesisPregrado periodo = periodoFacadelocal.findByPeriodo(secNombre.getValor());
                return periodo;
            } else {
                return null;
            }
        }
    }

    public Secuencia pasarPeriodoPregradoASecuencia(PeriodoTesisPregrado periodo) {
        Secuencia secPeriodo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE), null);
        if (periodo.getId() != null) {
            secPeriodo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), periodo.getId().toString()));
        }
        if (periodo.getNombre() != null) {
            secPeriodo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), periodo.getNombre()));
        }
        return secPeriodo;
    }

    public Secuencia pasarProyectoDeGradoASecuencia(ProyectoDeGrado tesis) {
        SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Secuencia secPrincipal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROYECTO_DE_GRADO), "");
        if (tesis.getId() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), tesis.getId().toString());
            secPrincipal.agregarSecuencia(secId);
        }
        if (tesis.getTemaTesis() != null) {
            Secuencia secTemaTesis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TEMA_TESIS), tesis.getTemaTesis());
            secPrincipal.agregarSecuencia(secTemaTesis);
        }
        if (tesis.getFechaCreacion() != null) {
            Secuencia secFechaFin = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_CREACION), sdfHMS.format(new Date(tesis.getFechaCreacion().getTime())));
            secPrincipal.agregarSecuencia(secFechaFin);
        }
        if (tesis.getEstudiante() != null) {
            Secuencia SecEstudiante = pasarEstudianteASecuencia(tesis.getEstudiante());
            secPrincipal.agregarSecuencia(SecEstudiante);
        }
        if (tesis.getAsesor() != null) {
            Secuencia secAsesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASESOR_TESIS), "");
            secAsesor.agregarSecuencia(pasarProfesorASecuencia(tesis.getAsesor()));
            secPrincipal.agregarSecuencia(secAsesor);
        }
        if (tesis.getSemestreIniciacion() != null) {
            Secuencia secSemestre = pasarPeriodoPregradoASecuencia(tesis.getSemestreIniciacion());
            secPrincipal.agregarSecuencia(secSemestre);
        }
        if (tesis.isAprobadoAsesor() != null) {
            Secuencia secAprobadoasesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APROBADO_ASESOR), tesis.isAprobadoAsesor().toString().toUpperCase());
            secPrincipal.agregarSecuencia(secAprobadoasesor);
        }
        if (tesis.getCalificacionTesis() != null) {
            Secuencia secNotaTesis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA), tesis.getCalificacionTesis().toString());
            secPrincipal.agregarSecuencia(secNotaTesis);
        }
        if (tesis.getRutaArchivoPropuesta() != null) {
            String nombreArchivo = tesis.getRutaArchivoPropuesta().replace(getConstanteBean().getConstante(Constantes.RUTA_ARCHIVOS_TESIS_PREGRADO), "");
            Secuencia secRutaPropuesta = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_ARCHIVO_PROPUESTA_TESIS_PREGRADO), nombreArchivo);
            secPrincipal.agregarSecuencia(secRutaPropuesta);
        }
        if (tesis.getRutaPosterTesis() != null) {
            String rutaCompleta = tesis.getRutaPosterTesis();
            String nombreArc = rutaCompleta.substring(rutaCompleta.lastIndexOf("/") + 1);
            Secuencia secNombreAfiche = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_ARCHIVO_POSTER), nombreArc);
            secPrincipal.agregarSecuencia(secNombreAfiche);
            Secuencia secRutaAfiche = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA_ARCHIVO_POSTER), tesis.getRutaPosterTesis());
            secPrincipal.agregarSecuencia(secRutaAfiche);
        }
        if (tesis.getRutaArchivoPendienteEspecial() != null) {
            String rutaCompleta = tesis.getRutaArchivoPendienteEspecial();
            String nombreArc = rutaCompleta.substring(rutaCompleta.lastIndexOf("/") + 1);
            Secuencia secRutaPendiente = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_ARCHIVO_PENDIENTE_ESPECIAL), nombreArc);
            secPrincipal.agregarSecuencia(secRutaPendiente);
        }
        if (tesis.getRutaABET() != null) {
            String nombreArchivo = tesis.getRutaABET().replace(getConstanteBean().getConstante(Constantes.RUTA_ARCHIVO_ABET), "");
            Secuencia secRutaArticulo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA_ARCHIVO_ABET), nombreArchivo);
            secPrincipal.agregarSecuencia(secRutaArticulo);
        }
        if (tesis.getEstadoTesis() != null) {
            Secuencia secEstadoTesis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO_TESIS), tesis.getEstadoTesis());
            secPrincipal.agregarSecuencia(secEstadoTesis);
        }
        if (tesis.getComentariosAsesor() != null) {
            Secuencia secEstadoTesis = pasarComentariosTesisPregradoASecuencia(tesis.getComentariosAsesor());
            secPrincipal.agregarSecuencia(secEstadoTesis);
        }
        return secPrincipal;
    }

    public ProyectoDeGrado pasarSecuenciaAProyectoDeGrado(Secuencia sec) {
        ProyectoDeGrado tesis = new ProyectoDeGrado();
        Secuencia secId = sec.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
        if (secId != null) {
            Long id = Long.parseLong(secId.getValor().trim());
            tesis.setId(id);
        }
        Secuencia secTemaTesis = sec.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_TEMA_TESIS));
        if (secTemaTesis != null) {
            tesis.setTemaTesis(secTemaTesis.getValor());
        }
        tesis.setFechaCreacion(new Timestamp(new Date().getTime()));
        Secuencia SecEstudiante = sec.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE));
        if (SecEstudiante != null) {
            Estudiante e = pasarSecuenciaAEstudiante(SecEstudiante);
            tesis.setEstudiante(e);
        }
        Secuencia secAsesor = sec.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASESOR_TESIS));
        if (secAsesor != null) {
            Secuencia secProfesor = secAsesor.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR));
            if (secProfesor != null) {
                tesis.setAsesor(pasarSecuenciaAProfesor(secProfesor));
            }
        }
        Secuencia secSemestre = sec.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE));
        if (secSemestre != null) {
            PeriodoTesisPregrado sem = periodoFacadelocal.findByPeriodo(secSemestre.getValor());
            tesis.setSemestreIniciacion(sem);
        }
        Secuencia secAprobadoasesor = sec.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_APROBADO_ASESOR));
        if (secAprobadoasesor != null) {
            tesis.setAprobadoAsesor(secAprobadoasesor.getValor().trim());
        }
        Secuencia secNotaTesis = sec.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA));
        if (secNotaTesis != null) {
            tesis.setCalificacionTesis(Double.parseDouble(secNotaTesis.getValor()));
        }
        Secuencia secRutaArticulo = sec.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA));
        if (secRutaArticulo != null) {
            tesis.setRutaArticuloTesis1(secRutaArticulo.getValor());
        }
        Secuencia secEstadoTesis = sec.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO_TESIS));
        if (secEstadoTesis != null) {
            tesis.setEstadoTesis(secEstadoTesis.getValor());
        }
        Secuencia secRutaPoster = sec.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA_POSTER));
        if (secRutaPoster != null) {
            tesis.setRutaPosterTesis(secRutaPoster.getValor());
        }
        Secuencia secRutaABET = sec.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA_ABET));
        if (secRutaABET != null) {
            tesis.setRutaABET(secRutaABET.getValor());
        }
        return tesis;
    }

    public Estudiante pasarSecuenciaAEstudiante(Secuencia secEstudiante) {
        Long id = secEstudiante.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)) != null
                ? Long.parseLong(secEstudiante.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)).getValor().trim())
                : null;
        if (id != null) {
            Estudiante estudiante = estudianteFacadeRemote.find(id);
            return estudiante;
        } else {
            String correo = secEstudiante.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)) != null
                    ? secEstudiante.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor().trim()
                    : null;
            if (correo != null) {
                Estudiante estudiante = estudianteFacadeRemote.findByCorreo(correo);
                return estudiante;
            }
            return null;
        }
    }

    public Secuencia pasarEstudianteASecuencia(Estudiante estudiante) {
        Secuencia secEstudiante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE), "");

        secEstudiante.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), (estudiante.getId() != null) ? estudiante.getId().toString() : ""));
        secEstudiante.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), (estudiante.getPersona() != null && estudiante.getPersona().getNombres() != null) ? estudiante.getPersona().getNombres() : ""));
        secEstudiante.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), (estudiante.getPersona() != null && estudiante.getPersona().getApellidos() != null) ? estudiante.getPersona().getApellidos() : ""));
        secEstudiante.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), (estudiante.getPersona() != null && estudiante.getPersona().getCorreo() != null) ? estudiante.getPersona().getCorreo() : ""));

        return secEstudiante;
    }

    public Secuencia pasarComentariosTesisPregradoASecuencia(Collection<ComentarioTesisPregrado> coments) {
        Secuencia secComentarios = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COMENTARIOS_TESIS), null);
        for (ComentarioTesisPregrado comentarioTesis : coments) {
            Secuencia secComent = pasarComentarioTesisASecuencia(comentarioTesis);
            secComentarios.agregarSecuencia(secComent);
        }
        return secComentarios;
    }

    public Secuencia pasarComentarioTesisASecuencia(ComentarioTesisPregrado coments) {
        Secuencia secComentario = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COMENTARIO_TESIS), null);
        if (coments.getId() != null) {
            secComentario.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), coments.getId().toString()));
        }
        if (coments.getComentario() != null) {
            secComentario.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COMENTARIO), coments.getComentario()));
        }
        if (coments.getDebeRetirar() != null) {
            secComentario.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DEBE_RETIRAR), coments.getDebeRetirar().toString().toUpperCase()));
        }
        if (coments.getFecha() != null) {
            secComentario.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_CREACION), coments.getFecha().toString()));
        }
        return secComentario;
    }

    public PeriodoTesisPregrado pasarSecuenciaAPeriodoConfiguracion(Secuencia secSemestre) {
        try {
            PeriodoTesisPregrado periodo = new PeriodoTesisPregrado();
            SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Secuencia secId = secSemestre.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            if (secId != null) {
                periodo.setId(Long.parseLong(secId.getValor().trim()));
            }
            Secuencia secNombre = secSemestre.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE));
            if (secNombre != null) {
                periodo.setNombre(secNombre.getValor());
            }
            Secuencia secPublicacion = secSemestre.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_MAXIMA_SUBIR_TEMAS_TESIS));
            if (secPublicacion != null) {
                Date fechaInicioDateTemp = sdfHMS.parse(secPublicacion.getValor());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fechaInicioDateTemp);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                Date fechaInicioDate = calendar.getTime();
                periodo.setPublicacionTemasTesis(new Timestamp(fechaInicioDate.getTime()));

            }
            secPublicacion = secSemestre.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_INSCRIPCION_PROYECTO_DE_GRADO));
            if (secPublicacion != null) {
                Date fechaInicioDateTemp = sdfHMS.parse(secPublicacion.getValor());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fechaInicioDateTemp);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                Date fechaInicioDate = calendar.getTime();
                periodo.setInscripcionTesisEstudiante(new Timestamp(fechaInicioDate.getTime()));

            }
            secPublicacion = secSemestre.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ASESOR_ACEPTAR_TESIS_PREGRADO));
            if (secPublicacion != null) {
                Date fechaInicioDateTemp = sdfHMS.parse(secPublicacion.getValor());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fechaInicioDateTemp);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                Date fechaInicioDate = calendar.getTime();
                periodo.setAcesorAceptePy(new Timestamp(fechaInicioDate.getTime()));

            }
            secPublicacion = secSemestre.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ESTUDIANTE_ENVIAR_PROPUESTA_TESIS_PREGRADO));
            if (secPublicacion != null) {
                Date fechaInicioDateTemp = sdfHMS.parse(secPublicacion.getValor());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fechaInicioDateTemp);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                Date fechaInicioDate = calendar.getTime();
                periodo.setEnvioPropuestaPyEstud(new Timestamp(fechaInicioDate.getTime()));

            }
            secPublicacion = secSemestre.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ASESOR_VALIDACION_PROPUESTA_PROYECTO));
            if (secPublicacion != null) {
                Date fechaInicioDateTemp = sdfHMS.parse(secPublicacion.getValor());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fechaInicioDateTemp);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                Date fechaInicioDate = calendar.getTime();
                periodo.setValidacionAsesorPropuestaDeProyecto(new Timestamp(fechaInicioDate.getTime()));

            }
            secPublicacion = secSemestre.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ASESOR_APRECIACION_CUALITATIVA_PROYECTO));
            if (secPublicacion != null) {
                Date fechaInicioDateTemp = sdfHMS.parse(secPublicacion.getValor());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fechaInicioDateTemp);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                Date fechaInicioDate = calendar.getTime();
                periodo.setApreciacionCualitativa(new Timestamp(fechaInicioDate.getTime()));
            }
            secPublicacion = secSemestre.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ESTUDIANTE_ENTREGA_POSTER));
            if (secPublicacion != null) {
                Date fechaInicioDateTemp = sdfHMS.parse(secPublicacion.getValor());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fechaInicioDateTemp);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                Date fechaInicioDate = calendar.getTime();
                periodo.setEntregaPoster(new Timestamp(fechaInicioDate.getTime()));

            }
            secPublicacion = secSemestre.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ASESOR_ACEPTAR_POSTER));
            if (secPublicacion != null) {
                Date fechaInicioDateTemp = sdfHMS.parse(secPublicacion.getValor());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fechaInicioDateTemp);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                Date fechaInicioDate = calendar.getTime();
                periodo.setDarVistoBuenoPoster(new Timestamp(fechaInicioDate.getTime()));

            }
            secPublicacion = secSemestre.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ASESOR_LLENAR_ABET));
            if (secPublicacion != null) {
                Date fechaInicioDateTemp = sdfHMS.parse(secPublicacion.getValor());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fechaInicioDateTemp);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                Date fechaInicioDate = calendar.getTime();
                periodo.setRubricaABET(new Timestamp(fechaInicioDate.getTime()));

            }
            secPublicacion = secSemestre.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ASESOR_ENVIO_NOTAS));
            if (secPublicacion != null) {
                Date fechaInicioDateTemp = sdfHMS.parse(secPublicacion.getValor());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fechaInicioDateTemp);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                Date fechaInicioDate = calendar.getTime();
                periodo.setReporteNotas(new Timestamp(fechaInicioDate.getTime()));

            }
            secPublicacion = secSemestre.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ESTUDIANTE_NOTIFICA_RETIRO));
            if (secPublicacion != null) {
                Date fechaInicioDateTemp = sdfHMS.parse(secPublicacion.getValor());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fechaInicioDateTemp);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                Date fechaInicioDate = calendar.getTime();
                periodo.setInformeRetiro(new Timestamp(fechaInicioDate.getTime()));

            }
            secPublicacion = secSemestre.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ASESOR_PEDIR_PENDIENTE));
            if (secPublicacion != null) {
                Date fechaInicioDateTemp = sdfHMS.parse(secPublicacion.getValor());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fechaInicioDateTemp);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                Date fechaInicioDate = calendar.getTime();
                periodo.setPedirPendiente(new Timestamp(fechaInicioDate.getTime()));

            }
            secPublicacion = secSemestre.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ASESOR_LEVANTAR_PENDIENTE));
            if (secPublicacion != null) {
                Date fechaInicioDateTemp = sdfHMS.parse(secPublicacion.getValor());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fechaInicioDateTemp);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                Date fechaInicioDate = calendar.getTime();
                periodo.setLevantarPendiente(new Timestamp(fechaInicioDate.getTime()));

            }
            secPublicacion = secSemestre.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ESTUDIANTE_ENTREGA_POSTER_PENDIENTE));
            if (secPublicacion != null) {
                Date fechaInicioDateTemp = sdfHMS.parse(secPublicacion.getValor());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fechaInicioDateTemp);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                Date fechaInicioDate = calendar.getTime();
                periodo.setEntregaPosterPendiente(new Timestamp(fechaInicioDate.getTime()));

            }
            secPublicacion = secSemestre.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ASESOR_ACEPTAR_POSTER_PENDIENTE));
            if (secPublicacion != null) {
                Date fechaInicioDateTemp = sdfHMS.parse(secPublicacion.getValor());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fechaInicioDateTemp);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                Date fechaInicioDate = calendar.getTime();
                periodo.setDarVistoBuenoPosterPendiente(new Timestamp(fechaInicioDate.getTime()));
            }
            secPublicacion = secSemestre.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ASESOR_PEDIR_PENDIENTE_ESPECIAL));
            if (secPublicacion != null) {
                Date fechaInicioDateTemp = sdfHMS.parse(secPublicacion.getValor());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fechaInicioDateTemp);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                Date fechaInicioDate = calendar.getTime();
                periodo.setPedirPendienteEspecial(new Timestamp(fechaInicioDate.getTime()));

            }
            secPublicacion = secSemestre.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ASESOR_LEVANTAR_PEN_ESPECIAL_PREGRADO));
            if (secPublicacion != null) {
                Date fechaInicioDateTemp = sdfHMS.parse(secPublicacion.getValor());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fechaInicioDateTemp);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                Date fechaInicioDate = calendar.getTime();
                periodo.setLevantarPendienteEspecial(new Timestamp(fechaInicioDate.getTime()));

            }
            secPublicacion = secSemestre.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ESTUDIANTE_ENTREGA_POSTER_PENDIENTE_ESPECIAL));
            if (secPublicacion != null) {
                Date fechaInicioDateTemp = sdfHMS.parse(secPublicacion.getValor());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fechaInicioDateTemp);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                Date fechaInicioDate = calendar.getTime();
                periodo.setEntregaPosterPendienteEspecial(new Timestamp(fechaInicioDate.getTime()));

            }
            secPublicacion = secSemestre.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ASESOR_ACEPTAR_POSTER_PENDIENTE_ESPECIAL));
            if (secPublicacion != null) {
                Date fechaInicioDateTemp = sdfHMS.parse(secPublicacion.getValor());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fechaInicioDateTemp);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                Date fechaInicioDate = calendar.getTime();
                periodo.setDarVistoBuenoPosterPendienteEspecial(new Timestamp(fechaInicioDate.getTime()));

            }
            return periodo;
        } catch (Exception e) {
            return null;
        }
    }

    public Secuencia pasarPeriodoPregradoConfiguracionASecuencia(PeriodoTesisPregrado periodo) {
        SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Secuencia secP = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE), null);
        if (periodo.getId() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), periodo.getId().toString());
            secP.agregarSecuencia(secId);
        }
        if (periodo.getNombre() != null) {
            Secuencia secNombre = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), periodo.getNombre());
            secP.agregarSecuencia(secNombre);
        }
        if (periodo.getPublicacionTemasTesis() != null) {
            Secuencia secPublicacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_MAXIMA_SUBIR_TEMAS_TESIS), sdfHMS.format(periodo.getPublicacionTemasTesis()));
            secP.agregarSecuencia(secPublicacion);
        }
        if (periodo.getInscripcionTesisEstudiante() != null) {
            Secuencia secPublicacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_INSCRIPCION_PROYECTO_DE_GRADO), sdfHMS.format(periodo.getInscripcionTesisEstudiante()));
            secP.agregarSecuencia(secPublicacion);
        }
        if (periodo.getAcesorAceptePy() != null) {
            Secuencia secPublicacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ASESOR_ACEPTAR_TESIS_PREGRADO), sdfHMS.format(periodo.getAcesorAceptePy()));
            secP.agregarSecuencia(secPublicacion);
        }
        if (periodo.getEnvioPropuestaPyEstud() != null) {
            Secuencia secPublicacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ESTUDIANTE_ENVIAR_PROPUESTA_TESIS_PREGRADO), sdfHMS.format(periodo.getEnvioPropuestaPyEstud()));
            secP.agregarSecuencia(secPublicacion);
        }
        if (periodo.getValidacionAsesorPropuestaDeProyecto() != null) {
            Secuencia secPublicacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ASESOR_VALIDACION_PROPUESTA_PROYECTO), sdfHMS.format(periodo.getValidacionAsesorPropuestaDeProyecto()));
            secP.agregarSecuencia(secPublicacion);
        }
        if (periodo.getApreciacionCualitativa() != null) {
            Secuencia secPublicacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ASESOR_APRECIACION_CUALITATIVA_PROYECTO), sdfHMS.format(periodo.getApreciacionCualitativa()));
            secP.agregarSecuencia(secPublicacion);
        }
        if (periodo.getEntregaPoster() != null) {
            Secuencia secPublicacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ESTUDIANTE_ENTREGA_POSTER), sdfHMS.format(periodo.getEntregaPoster()));
            secP.agregarSecuencia(secPublicacion);
        }
        if (periodo.getDarVistoBuenoPoster() != null) {
            Secuencia secPublicacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ASESOR_ACEPTAR_POSTER), sdfHMS.format(periodo.getDarVistoBuenoPoster()));
            secP.agregarSecuencia(secPublicacion);
        }
        if (periodo.getRubricaABET() != null) {
            Secuencia secPublicacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ASESOR_LLENAR_ABET), sdfHMS.format(periodo.getRubricaABET()));
            secP.agregarSecuencia(secPublicacion);
        }
        if (periodo.getReporteNotas() != null) {
            Secuencia secPublicacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ASESOR_ENVIO_NOTAS), sdfHMS.format(periodo.getReporteNotas()));
            secP.agregarSecuencia(secPublicacion);
        }
        if (periodo.getInformeRetiro() != null) {
            Secuencia secPublicacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ESTUDIANTE_NOTIFICA_RETIRO), sdfHMS.format(periodo.getInformeRetiro()));
            secP.agregarSecuencia(secPublicacion);
        }
        if (periodo.getPedirPendiente() != null) {
            Secuencia secPublicacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ASESOR_PEDIR_PENDIENTE), sdfHMS.format(periodo.getPedirPendiente()));
            secP.agregarSecuencia(secPublicacion);
        }
        if (periodo.getLevantarPendiente() != null) {
            Secuencia secPublicacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ASESOR_LEVANTAR_PENDIENTE), sdfHMS.format(periodo.getLevantarPendiente()));
            secP.agregarSecuencia(secPublicacion);
        }
        if (periodo.getEntregaPosterPendiente() != null) {
            Secuencia secPublicacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ESTUDIANTE_ENTREGA_POSTER_PENDIENTE), sdfHMS.format(periodo.getEntregaPosterPendiente()));
            secP.agregarSecuencia(secPublicacion);
        }
        if (periodo.getDarVistoBuenoPosterPendiente() != null) {
            Secuencia secPublicacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ASESOR_ACEPTAR_POSTER_PENDIENTE), sdfHMS.format(periodo.getDarVistoBuenoPosterPendiente()));
            secP.agregarSecuencia(secPublicacion);
        }
        if (periodo.getPedirPendienteEspecial() != null) {
            Secuencia secPublicacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ASESOR_PEDIR_PENDIENTE_ESPECIAL), sdfHMS.format(periodo.getPedirPendienteEspecial()));
            secP.agregarSecuencia(secPublicacion);
        }
        if (periodo.getLevantarPendienteEspecial() != null) {
            Secuencia secPublicacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ASESOR_LEVANTAR_PEN_ESPECIAL_PREGRADO), sdfHMS.format(periodo.getLevantarPendienteEspecial()));
            secP.agregarSecuencia(secPublicacion);
        }
        if (periodo.getEntregaPosterPendienteEspecial() != null) {
            Secuencia secPublicacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ESTUDIANTE_ENTREGA_POSTER_PENDIENTE_ESPECIAL), sdfHMS.format(periodo.getEntregaPosterPendienteEspecial()));
            secP.agregarSecuencia(secPublicacion);
        }
        if (periodo.getDarVistoBuenoPosterPendienteEspecial() != null) {
            Secuencia secPublicacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ASESOR_ACEPTAR_POSTER_PENDIENTE_ESPECIAL), sdfHMS.format(periodo.getDarVistoBuenoPosterPendienteEspecial()));
            secP.agregarSecuencia(secPublicacion);
        }
        return secP;
    }

    public Secuencia pasarPeriodosPregradoASecuencia(Collection<PeriodoTesisPregrado> periodos) {
        Secuencia secPeriodos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRES), null);
        for (PeriodoTesisPregrado periodoTesisPregrado : periodos) {
            Secuencia secP = pasarPeriodoPregradoASecuencia(periodoTesisPregrado);
            secPeriodos.agregarSecuencia(secP);
        }
        return secPeriodos;
    }

    public Secuencia pasarCategoriasProyectoDeGradoASecuencias(Collection<CategoriaProyectoDeGrado> categorias) {
        Secuencia secCategorias = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CATEGORIAS_PROYECTO_DE_GRADO), null);
        for (CategoriaProyectoDeGrado categoriaProyectoDeGrado : categorias) {
            Secuencia secCategoria = pasarCategoriaProyectoDeGradoASecuencia(categoriaProyectoDeGrado);
            secCategorias.agregarSecuencia(secCategoria);
        }
        return secCategorias;
    }

    public ComentarioTesisPregrado pasarSecuenciaAComentarioProyectoGrado(Secuencia secComentarioPG) {
        Secuencia secId = secComentarioPG.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
        ComentarioTesisPregrado coment = new ComentarioTesisPregrado();
        if (secId != null) {
            coment.setId(Long.parseLong(secId.getValor().trim()));
        }
        Secuencia secTxt = secComentarioPG.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COMENTARIO));
        if (secTxt != null) {
            coment.setComentario(secTxt.getValor());
        }
        Secuencia secDebeRetirar = secComentarioPG.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DEBE_RETIRAR));
        if (secDebeRetirar != null) {
            Boolean resp = Boolean.parseBoolean(secDebeRetirar.getValor());
            coment.setDebeRetirar(resp);
        }
        return coment;
    }
    
    public Secuencia pasarProyectosDeGradoASecuenciaLight(Collection<ProyectoDeGrado> proyectos) {
        Secuencia secProyectos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROYECTOS_DE_GRADO), "");
        for (ProyectoDeGrado proyectoDeGrado : proyectos) {
            Secuencia secProyecto = pasarProyectoDeGradoASecuenciaLight(proyectoDeGrado);
            secProyectos.agregarSecuencia(secProyecto);
        }
        return secProyectos;
    }

    /**
     * Contiene la siguiente información de proyecto de grado:
     * Id, tema, fecha creación, estudiante (id, nombres, apellidos, correo), asesor (id, nombres, apellidos, correo), semestre iniciación
     */
    public Secuencia pasarProyectoDeGradoASecuenciaLight(ProyectoDeGrado tesis) {
        Secuencia secPrincipal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROYECTO_DE_GRADO), "");
        if (tesis.getId() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), tesis.getId().toString());
            secPrincipal.agregarSecuencia(secId);
        }
        if (tesis.getTemaTesis() != null) {
            Secuencia secTemaTesis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TEMA_TESIS), tesis.getTemaTesis());
            secPrincipal.agregarSecuencia(secTemaTesis);
        }
        if (tesis.getFechaCreacion() != null) {
            SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Secuencia secFechaFin = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_CREACION), sdfHMS.format(new Date(tesis.getFechaCreacion().getTime())));
            secPrincipal.agregarSecuencia(secFechaFin);
        }
        if (tesis.getEstudiante() != null) {
            Secuencia SecEstudiante = pasarEstudianteASecuencia(tesis.getEstudiante());
            secPrincipal.agregarSecuencia(SecEstudiante);
        }
        if (tesis.getAsesor() != null) {
            Secuencia secAsesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASESOR_TESIS), "");
            secAsesor.agregarSecuencia(pasarProfesorASecuencia(tesis.getAsesor()));
            secPrincipal.agregarSecuencia(secAsesor);
        }
        if (tesis.getSemestreIniciacion() != null) {
            Secuencia secSemestre = pasarPeriodoPregradoASecuencia(tesis.getSemestreIniciacion());
            secPrincipal.agregarSecuencia(secSemestre);
        }
        return secPrincipal;
    }

    public boolean existeArchivo(String valor) {
        File fArchivo = new File(valor);
        return fArchivo.exists();
    }

    private ConstanteRemote getConstanteBean() {
        return constanteBean;
    }
}
