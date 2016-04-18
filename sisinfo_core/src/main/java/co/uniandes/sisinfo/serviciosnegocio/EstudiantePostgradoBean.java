package co.uniandes.sisinfo.serviciosnegocio;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.html.simpleparser.StyleSheet;
import com.lowagie.text.pdf.PdfWriter;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.entities.EstudiantePosgrado;
import co.uniandes.sisinfo.entities.HojaVida;
import co.uniandes.sisinfo.entities.datosmaestros.Estudiante;
import co.uniandes.sisinfo.entities.datosmaestros.InformacionAcademica;
import co.uniandes.sisinfo.entities.datosmaestros.NivelFormacion;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import co.uniandes.sisinfo.entities.datosmaestros.soporte.Pais;
import co.uniandes.sisinfo.entities.datosmaestros.soporte.TipoDocumento;
import co.uniandes.sisinfo.serviciosfuncionales.EstudiantePosgradoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.EstudiantePosgradoFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.HojaVidaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.HojaVidaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.EstudianteFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.InformacionAcademicaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.NivelFormacionFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.PersonaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import co.uniandes.sisinfo.serviciosfuncionales.soporte.PaisFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.soporte.TipoDocumentoFacadeRemote;

/**
 * Servicios de administración de estudiantes de postgrado
 * @author David Naranjo, Camilo Cortés, Marcela Morales
 */
@Stateless
@EJB(name = "EstudiantePostgradoBean", beanInterface = co.uniandes.sisinfo.serviciosnegocio.EstudiantePostgradoLocal.class)
public class EstudiantePostgradoBean implements EstudiantePostgradoRemote, EstudiantePostgradoLocal {

    //---------------------------------------
    // Atributos
    //---------------------------------------
    //Remotos
    @EJB
    private InformacionAcademicaFacadeRemote informacionAcademicaFacade;
    @EJB
    private PaisFacadeRemote paisFacade;
    @EJB
    private PersonaFacadeRemote personaFacade;
    @EJB
    private EstudianteFacadeRemote estudianteFacade;
    @EJB
    private TipoDocumentoFacadeRemote tipoDocumentofacade;
    @EJB
    private NivelFormacionFacadeRemote nivelFormacionFacade;
    //Locales
    @EJB
    private HojaVidaFacadeLocal hojaVidaFacade;
    @EJB
    private EstudiantePosgradoFacadeLocal estudiantePosgradoFacade;
    @EJB
    private HojaVidaFacadeRemote hojaVidaFacadeRemote;
    @EJB
    private EstudiantePosgradoFacadeRemote estudiantePosgradoFacadeRemote;
    //Útiles
    private ParserT parser;
    @EJB
    private ConstanteRemote constanteBean;
    private ServiceLocator serviceLocator;
    private ConversorBolsaEmpleo conversor;
    private ConversorEstudiante conversorEstudiante;

    //---------------------------------------
    // Constructor
    //---------------------------------------
    /**
     * Constructor de EstudiantePostgradoBean
     */
    public EstudiantePostgradoBean() {
        try {
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            paisFacade = (PaisFacadeRemote) serviceLocator.getRemoteEJB(PaisFacadeRemote.class);
            tipoDocumentofacade = (TipoDocumentoFacadeRemote) serviceLocator.getRemoteEJB(TipoDocumentoFacadeRemote.class);
            personaFacade = (PersonaFacadeRemote) serviceLocator.getRemoteEJB(PersonaFacadeRemote.class);
            estudianteFacade = (EstudianteFacadeRemote) serviceLocator.getRemoteEJB(EstudianteFacadeRemote.class);
            informacionAcademicaFacade = (InformacionAcademicaFacadeRemote) serviceLocator.getRemoteEJB(InformacionAcademicaFacadeRemote.class);
            nivelFormacionFacade = (NivelFormacionFacadeRemote) serviceLocator.getRemoteEJB(NivelFormacionFacadeRemote.class);
            hojaVidaFacadeRemote = (HojaVidaFacadeRemote) serviceLocator.getRemoteEJB(HojaVidaFacadeRemote.class);
            estudiantePosgradoFacadeRemote = (EstudiantePosgradoFacadeRemote) serviceLocator.getRemoteEJB(EstudiantePosgradoFacadeRemote.class);
            conversorEstudiante = new ConversorEstudiante(
                    getConstanteBean(), estudianteFacade, personaFacade, paisFacade,
                    tipoDocumentofacade, informacionAcademicaFacade, hojaVidaFacadeRemote, estudiantePosgradoFacadeRemote);
        } catch (NamingException ex) {
            Logger.getLogger(EstudiantePostgradoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //---------------------------------------
    // Métodos
    //---------------------------------------
    @Override
    public String actualizarHojaVida(String comando) {
        try {
            getParser().leerXML(comando);
            //Extrae los parámetros
            String correo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            EstudiantePosgrado estudiante = getEstudiantePosgradoFacade().findByCorreo(correo);
            //Valida que el estudiante exista
            if (estudiante == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_HOJA_VIDA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0100, new LinkedList<Secuencia>());
            }
            //Actualiza la hoja de vida. En caso de que ésta no exista, la crea.
            Secuencia secuenciaHojaVida = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HOJA_VIDA));
            Secuencia secContenido = secuenciaHojaVida.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CONTENIDO));
            HojaVida hojaVida = estudiante.getHojaVida();
            if (hojaVida == null) {
                hojaVida = new HojaVida();
                hojaVida.setFechaCreacion(new Date(System.currentTimeMillis()));
                getHojaVidaFacade().create(hojaVida);
            }
            if (secContenido != null) {
                hojaVida.setContenido(secContenido.getValor());
                hojaVida.setFechaActualizacion(new Date(System.currentTimeMillis()));
            }
            getHojaVidaFacade().edit(hojaVida);
            estudiante.setHojaVida(hojaVida);
            getEstudiantePosgradoFacade().edit(estudiante);
            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_HOJA_VIDA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0130, new LinkedList<Secuencia>());

        } catch (Exception e) {
            try {
                Logger.getLogger(EstudiantePostgradoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_HOJA_VIDA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0101, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(EstudiantePostgradoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public String actualizarInformacionAcademica(String comando) {
        try {
            getParser().leerXML(comando);
            //Extrae los parámetros
            String correo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            EstudiantePosgrado estudiante = getEstudiantePosgradoFacade().findByCorreo(correo);
            //Valida que el estudiante exista
            if (estudiante == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_INFORMACION_ACADEMICA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0100, new LinkedList<Secuencia>());
            }
            //Actualiza la información académica
            Secuencia secuenciaInformacionAcademica = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_ACADEMICA));
            Secuencia secTitulo = secuenciaInformacionAcademica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TITULO));
            if (secTitulo != null) {
                estudiante.setTitulo(secTitulo.getValor());
            }
            Secuencia secNombreUniversidad = secuenciaInformacionAcademica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_UNIVERSIDAD));
            if (secNombreUniversidad != null) {
                estudiante.setUniversidadPregrado(secNombreUniversidad.getValor());
            }
            Secuencia secFechaGraduacion = secuenciaInformacionAcademica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_GRADUACION));
            if (secFechaGraduacion != null) {
                SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                estudiante.setFechaGraduacion(sdfHMS.parse(secFechaGraduacion.getValor()));
            }
            Secuencia secCiudad = secuenciaInformacionAcademica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CIUDAD));
            if (secCiudad != null) {
                estudiante.setCiudadUniversidadPregrado(secCiudad.getValor());
            }
            Secuencia secPais = secuenciaInformacionAcademica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PAIS));
            if (secPais != null) {
                Pais paisEntitY = getPaisFacade().findByNombre(secPais.getValor().trim());
                estudiante.setPaisUniversidadPregrado(paisEntitY);
            }
            Secuencia secEsPrimerSemestreMaestria = secuenciaInformacionAcademica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ES_PRIMER_SEMESTRE_MAESTRIA));
            if (secEsPrimerSemestreMaestria != null) {
                if (secEsPrimerSemestreMaestria.getValor().equalsIgnoreCase(Boolean.TRUE.toString())) {
                    estudiante.setEsPrimerSemestreMaestria(Boolean.TRUE);
                } else {
                    estudiante.setEsPrimerSemestreMaestria(Boolean.FALSE);
                }
            }
            Secuencia secPromedio = secuenciaInformacionAcademica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_TOTAL));
            if (secPromedio != null) {
                Estudiante est = estudiante.getEstudiante();
                InformacionAcademica infoAcademica = estudiante.getEstudiante().getInformacion_Academica();
                if (infoAcademica == null) {
                    infoAcademica = new InformacionAcademica();
                    getInformacionAcademicaFacade().create(infoAcademica);
                }
                infoAcademica.setPromedioTotal(secPromedio.getValorDouble());
                getInformacionAcademicaFacade().edit(infoAcademica);
                est.setInformacion_Academica(infoAcademica);
            }
            getEstudiantePosgradoFacade().edit(estudiante);
            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_INFORMACION_ACADEMICA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0131, new LinkedList<Secuencia>());

        } catch (Exception e) {
            try {
                Logger.getLogger(EstudiantePostgradoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_INFORMACION_ACADEMICA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0102, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(EstudiantePostgradoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public String actualizarInformacionPersonal(String comando) {
        try {
            getParser().leerXML(comando);
            Secuencia secCorreo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
            EstudiantePosgrado estudiantePosgrado = getEstudiantePosgradoFacade().findByCorreo(secCorreo.getValor());
            //Valida si el estudiante de posgrado existe
            if (estudiantePosgrado == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_INFORMACION_PERSONAL), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0100, new LinkedList<Secuencia>());
            }
            //Una vez el estudiante existe, se asigna la información personal
            Estudiante estudiante = estudiantePosgrado.getEstudiante();
            Persona persona = estudiante.getPersona();
            Secuencia secuenciaInformacionPersonal = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_PERSONAL));
            Secuencia secNombres = secuenciaInformacionPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES));
            if (secNombres != null) {
                persona.setNombres(secNombres.getValor());
            }
            Secuencia secApellidos = secuenciaInformacionPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS));
            if (secApellidos != null) {
                persona.setApellidos(secApellidos.getValor());
            }
            Secuencia secNacionalidad = secuenciaInformacionPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NACIONALIDAD));
            if (secNacionalidad != null) {
                Pais paisEntitY = getPaisFacade().findByNombre(secNacionalidad.getValor().trim());
                persona.setPais(paisEntitY);
            }
            Secuencia secTelefonoFijo = secuenciaInformacionPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TELEFONO_FIJO));
            if (secTelefonoFijo != null) {
                persona.setTelefono(secTelefonoFijo.getValor());
            }
            Secuencia secTelefonoCelular = secuenciaInformacionPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CELULAR));
            if (secTelefonoCelular != null) {
                persona.setCelular(secTelefonoCelular.getValor());
            }
            Secuencia secTipoDocumento = secuenciaInformacionPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_DOCUMENTO));
            if (secTipoDocumento != null) {
                TipoDocumento td = getTipoDocumentofacade().findByDescripcion(secTipoDocumento.getValor());
                persona.setTipoDocumento(td);
            }
            Secuencia secNumeroDocumento = secuenciaInformacionPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DOCUMENTO));
            if (secNumeroDocumento != null) {
                persona.setNumDocumentoIdentidad(secNumeroDocumento.getValor());
            }
            Secuencia secCorreoAlterno = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO_ALTERNO));
            if (secCorreoAlterno != null) {
                persona.setCorreoAlterno(secCorreoAlterno.getValor());
            }
            Secuencia secEsExterno = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ES_EXTERNO));
            if (secEsExterno != null) {
                estudiantePosgrado.setEsExterno(Boolean.parseBoolean(secEsExterno.getValor()));
            }
            getPersonaFacade().edit(persona);
            getEstudiantePosgradoFacade().edit(estudiantePosgrado);
            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_INFORMACION_PERSONAL), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0132, new LinkedList<Secuencia>());
        } catch (Exception e) {
            try {
                Logger.getLogger(EstudiantePostgradoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_INFORMACION_PERSONAL), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0103, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(EstudiantePostgradoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public String consultarEstudiante(String comando) {
        try {
            getParser().leerXML(comando);
            Secuencia secCorreo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
            EstudiantePosgrado estudiantePosgrado = getEstudiantePosgradoFacade().findByCorreo(secCorreo.getValor());
            //Valida si el estudiante de posgrado existe
            if (estudiantePosgrado == null) {
                Estudiante estudiante = estudianteFacade.findByCorreo(secCorreo.getValor());
                if (estudiante == null) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0100, new LinkedList<Secuencia>());
                }
                estudiantePosgrado = new EstudiantePosgrado();
                estudiantePosgrado.setEstudiante(estudiante);
                estudiantePosgradoFacade.create(estudiantePosgrado);
            }
            estudiantePosgrado = getEstudiantePosgradoFacade().findByCorreo(secCorreo.getValor());
            //Retorna al estudiante encontrado
            Secuencia secuenciaEstudiante = getConversor().crearSecuenciaEstudiante(estudiantePosgrado);
            Collection<Secuencia> secuencias = new LinkedList<Secuencia>();
            secuencias.add(secuenciaEstudiante);
            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0133, secuencias);

        } catch (Exception e) {
            try {
                Logger.getLogger(EstudiantePostgradoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0104, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(EstudiantePostgradoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public String consultarEstudiantePorId(String comando) {
        try {
            getParser().leerXML(comando);
            Secuencia secIdEstudiante = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_ESTUDIANTE));
            EstudiantePosgrado estudiantePosgrado = getEstudiantePosgradoFacade().find(secIdEstudiante.getValorLong());
            //Valida si el estudiante de posgrado existe
            if (estudiantePosgrado == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ESTUDIANTE_POR_ID), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0100, new LinkedList<Secuencia>());
            }
            //Retorna al estudiante encontrado
            Secuencia secuenciaEstudiante = getConversor().crearSecuenciaEstudiante(estudiantePosgrado);
            Collection<Secuencia> secuencias = new LinkedList<Secuencia>();
            secuencias.add(secuenciaEstudiante);
            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ESTUDIANTE_POR_ID), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0133, secuencias);

        } catch (Exception e) {
            try {
                Logger.getLogger(EstudiantePostgradoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ESTUDIANTE_POR_ID), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0104, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(EstudiantePostgradoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public String consultarEstudiantes(String comando) {
        try {
            getParser().leerXML(comando);
            Collection<EstudiantePosgrado> estudiantes = getEstudiantePosgradoFacade().findAll();
            Secuencia secuenciaEstudiantes = getConversor().crearSecuenciaEstudiantesPostgrado(estudiantes);
            Collection<Secuencia> secuencias = new LinkedList<Secuencia>();
            secuencias.add(secuenciaEstudiantes);
            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ESTUDIANTES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0141, secuencias);

        } catch (Exception e) {
            try {
                Logger.getLogger(EstudiantePostgradoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ESTUDIANTES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0114, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(EstudiantePostgradoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public String consultarHojaVida(String comando) {
        try {
            getParser().leerXML(comando);
            String correo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            EstudiantePosgrado estudiante = getEstudiantePosgradoFacade().findByCorreo(correo);
            if (estudiante == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_HOJA_VIDA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0100, new LinkedList<Secuencia>());
            }
            Secuencia secuenciaHojaVida = getConversor().crearSecuenciaHojaVida(estudiante.getHojaVida());
            Collection<Secuencia> secuencias = new LinkedList<Secuencia>();
            secuencias.add(secuenciaHojaVida);
            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_HOJA_VIDA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0134, secuencias);

        } catch (Exception e) {
            try {
                Logger.getLogger(EstudiantePostgradoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_HOJA_VIDA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0105, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(EstudiantePostgradoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public String consultarInformacionAcademica(String comando) {
        try {
            getParser().leerXML(comando);
            String correo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            EstudiantePosgrado estudiante = getEstudiantePosgradoFacade().findByCorreo(correo);
            if (estudiante == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_INFORMACION_ACADEMICA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0100, new LinkedList<Secuencia>());
            }
            Secuencia secuenciaInformacionAcademica = getConversor().crearSecuenciaInformacionAcademica(estudiante);
            Collection<Secuencia> secuencias = new LinkedList<Secuencia>();
            secuencias.add(secuenciaInformacionAcademica);
            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_INFORMACION_ACADEMICA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0135, secuencias);

        } catch (Exception e) {
            try {
                Logger.getLogger(EstudiantePostgradoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_INFORMACION_ACADEMICA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0106, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(EstudiantePostgradoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public String consultarInformacionPersonal(String comando) {
        try {
            getParser().leerXML(comando);
            String correo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            EstudiantePosgrado estudiante = getEstudiantePosgradoFacade().findByCorreo(correo);
            if (estudiante == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_INFORMACION_PERSONAL), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0100, new LinkedList<Secuencia>());
            }
            Secuencia secuenciaInformacionPersonal = getConversor().crearSecuenciaInformacionPersonal(estudiante);
            Collection<Secuencia> secuencias = new LinkedList<Secuencia>();
            secuencias.add(secuenciaInformacionPersonal);
            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_INFORMACION_PERSONAL), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0136, secuencias);

        } catch (Exception e) {
            try {
                Logger.getLogger(EstudiantePostgradoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_INFORMACION_PERSONAL), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0107, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(EstudiantePostgradoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public String crearEstudiante(String comando) {
        try {
            getParser().leerXML(comando);
            Secuencia secEstudiante = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE));
            getConversor().crearEstudiantePostgradoDesdeSecuencia(secEstudiante);
            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CREAR_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0137, new LinkedList<Secuencia>());

        } catch (Exception e) {
            try {
                Logger.getLogger(EstudiantePostgradoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CREAR_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0108, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(EstudiantePostgradoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public String eliminarEstudiante(String comando) {
        try {
            getParser().leerXML(comando);
            String correo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            EstudiantePosgrado estudiantePostgrado = getEstudiantePosgradoFacade().findByCorreo(correo);
            if (estudiantePostgrado == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0100, new LinkedList<Secuencia>());
            }
            getEstudiantePosgradoFacade().remove(estudiantePostgrado);
            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0137, new LinkedList<Secuencia>());

        } catch (Exception e) {
            try {
                Logger.getLogger(EstudiantePostgradoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0109, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(EstudiantePostgradoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String hacerReporteTodasHojasDeVida(String xml) {
        int BUFFER = 2048;
        FileOutputStream dest = null;
        File archivoDestino = new File(getConstanteBean().getConstante(Constantes.RUTA_TEMPORAL_ADJUNTOS) + "reporteHVs.zip");
        try {
            Collection<EstudiantePosgrado> list = getEstudiantePosgradoFacade().findAll();
            Collection<File> archivosAEnviar = new ArrayList<File>();
            //Hacer un Zip con las hojas de vida
            for (EstudiantePosgrado estudiantePosgrado : list) {
                //Hacer el PDF con la hoja de vida
                HojaVida hv = estudiantePosgrado.getHojaVida();
                if (hv != null && hv.getContenido() != null) {
                    //System.out.println("Lee Hoja de vida"+hv.getContenido());
                    Reader htmlreader = null;
                    try {
                        Document pdfDocument = new Document();
                        String encabezadoHTML = new String();
                        encabezadoHTML += "<h2>Hoja de Vida</h2><h4>" + estudiantePosgrado.getEstudiante().getPersona().getNombres() + " " + estudiantePosgrado.getEstudiante().getPersona().getApellidos() + "</h4><br />";
                        htmlreader = new BufferedReader(new StringReader(encabezadoHTML + hv.getContenido()));
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        PdfWriter.getInstance(pdfDocument, baos);
                        pdfDocument.open();
                        StyleSheet styles = new StyleSheet();
                        //styles.loadTagStyle("body", "font", "Bitstream Vera Sans");
                        ArrayList arrayElementList = HTMLWorker.parseToList(htmlreader, styles);
                        for (int i = 0; i < arrayElementList.size(); ++i) {
                            Element e = (Element) arrayElementList.get(i);
                            pdfDocument.add(e);
                            pdfDocument.add(Chunk.NEWLINE);
                        }
                        pdfDocument.close();
                        byte[] bs = baos.toByteArray();
                        File pdfFile = new File(getConstanteBean().getConstante(Constantes.RUTA_TEMPORAL_ADJUNTOS) + "/hojaVida" + estudiantePosgrado.getEstudiante().getPersona().getCorreo().split("@")[0] + ".pdf");
                        FileOutputStream out = new FileOutputStream(pdfFile);
                        out.write(bs);
                        out.close();
                        System.out.println("PDF creado:" + pdfFile.getAbsolutePath());
                        htmlreader.close();
                        archivosAEnviar.add(pdfFile);
                    } catch (IOException ex) {
                        Logger.getLogger(EstudiantePostgradoBean.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (DocumentException ex) {
                        Logger.getLogger(EstudiantePostgradoBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            dest = new FileOutputStream(archivoDestino);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            byte data[] = new byte[BUFFER];
            for (File file : archivosAEnviar) {
                BufferedInputStream origin;
                FileInputStream fi = new FileInputStream(file);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(file.getName());
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(EstudiantePostgradoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return archivoDestino.getAbsolutePath();
    }

    //---------------------------------------------
    // MÉTODOS PRIVADOS
    //---------------------------------------------
    private ParserT getParser() {
        if (parser == null) {
            parser = new ParserT();
        }
        return parser;
    }

    private ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    private HojaVidaFacadeLocal getHojaVidaFacade() {
        return hojaVidaFacade;
    }

    private InformacionAcademicaFacadeRemote getInformacionAcademicaFacade() {
        return informacionAcademicaFacade;
    }

    private EstudiantePosgradoFacadeLocal getEstudiantePosgradoFacade() {
        return estudiantePosgradoFacade;
    }

    private PaisFacadeRemote getPaisFacade() {
        return paisFacade;
    }

    private TipoDocumentoFacadeRemote getTipoDocumentofacade() {
        return tipoDocumentofacade;
    }

    private EstudianteFacadeRemote getEstudianteFacade() {
        return estudianteFacade;
    }

    private PersonaFacadeRemote getPersonaFacade() {
        return personaFacade;
    }

    private ConversorEstudiante getConversor() {
        return conversorEstudiante;
    }

    private NivelFormacionFacadeRemote getNivelFormacionFacade() {
        return nivelFormacionFacade;
    }

    //---------------------------------------------
    // MÉTODOS DEPRECADOS
    //---------------------------------------------
    @Deprecated
    /**
     * Este método se utilizaba para cargar estudiantes externos por archivo
     * Actualmente no se utiliza. En caso de utilizarse, revisar la funcionalidad.
     */
    public String cargarEstudiantesExternosPorArchivo(String xml) {
        try {
            String respuesta = null;
            int cantDatos = 16;
            getParser().leerXML(xml);
            String root = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA)).getValor();
            FileInputStream file = new FileInputStream(root);
            POIFSFileSystem fileSystem = new POIFSFileSystem(file);
            HSSFWorkbook workbook = new HSSFWorkbook(fileSystem);
            HSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.rowIterator();
            if (rows.hasNext()) {
                rows.next();
            }
            //Revisa que no hayan datos faltantes
            boolean faltantes = false;
            boolean finArchivo = false;
            while (rows.hasNext() && !faltantes && !finArchivo) {
                Row row = rows.next();
                Iterator<Cell> cells = row.cellIterator();
                int tam = 0;
                while (cells.hasNext() && !finArchivo && !faltantes) {
                    tam++;
                    Cell cell = cells.next();
                    if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                        if (cell.getRichStringCellValue().getString().equals(getConstanteBean().getConstante(Constantes.TAG_FIN_DE_DATOS))) {
                            finArchivo = true;
                            break;
                        }
                    }
                    if (cell.toString().equals("") || cell.toString().equals("null")) {
                        faltantes = true;
                    }
                }
                if (!finArchivo && tam != cantDatos) {
                    faltantes = true;
                }
            }
            //Se cargan los estudiantes en el sistema si no hay errores en el archivo
            if (!faltantes) {
                rows = sheet.rowIterator();
                if (rows.hasNext()) {
                    rows.next();
                }
                while (rows.hasNext()) {
                    Row row = rows.next();
                    Iterator<Cell> cells = row.cellIterator();
                    Cell cell = cells.next();
                    if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                        if (cell.getRichStringCellValue().getString().equals(getConstanteBean().getConstante(Constantes.TAG_FIN_DE_DATOS))) {
                            break;
                        }
                    }
                    //Extraemos los nombres
                    String nombres = cell.getRichStringCellValue().getString().trim();
                    //Extraemos los apellidos
                    cell = cells.next();
                    String apellidos = cell.getRichStringCellValue().getString().trim();
                    //Extraemos el correo
                    cell = cells.next();
                    String correo = cell.getRichStringCellValue().getString().trim();
                    //Extraemos el país
                    cell = cells.next();
                    String pais = cell.getRichStringCellValue().getString().trim();
                    //Extraemos la ciudad de nacimiento
                    cell = cells.next();
                    String ciudadNac = cell.getRichStringCellValue().getString().trim();
                    //Extraemos el teléfono fijo
                    cell = cells.next();
                    String telFijo = null;
                    if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                        telFijo = cell.getRichStringCellValue().getString().trim();
                    } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        telFijo = ((int) cell.getNumericCellValue()) + "";
                    }
                    //Extraemos el celular
                    cell = cells.next();
                    String celular = null;
                    if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                        celular = cell.getRichStringCellValue().getString().trim();
                    } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        celular = ((int) cell.getNumericCellValue()) + "";
                    }
                    //Extraemos el tipo de documento
                    cell = cells.next();
                    String tipoDoc = cell.getRichStringCellValue().getString().trim();
                    //Extraemos el número del documento
                    cell = cells.next();
                    String numeroDoc = null;
                    if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                        numeroDoc = cell.getRichStringCellValue().getString().trim();
                    } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        numeroDoc = ((int) cell.getNumericCellValue()) + "";
                    }
                    boolean existePersona = true;
                    Persona persona = getPersonaFacade().findByCorreo(correo);
                    if (persona == null) {
                        existePersona = false;
                        //Se crea a la nueva persona
                        persona = new Persona();
                    }
                    persona.setNombres(nombres);
                    persona.setApellidos(apellidos);
                    persona.setCorreo(correo);
                    Pais elPais = getPaisFacade().findByNombre(pais);
                    persona.setPais(elPais);
                    persona.setCiudadNacimiento(ciudadNac);
                    persona.setTelefono(telFijo);
                    persona.setCelular(celular);
                    TipoDocumento tipoDocumento = getTipoDocumentofacade().findByDescripcion(tipoDoc);
                    persona.setTipoDocumento(tipoDocumento);
                    persona.setNumDocumentoIdentidad(numeroDoc);
                    if (!existePersona) {
                        getPersonaFacade().create(persona);
                    } else {
                        getPersonaFacade().edit(persona);
                    }
                    persona = getPersonaFacade().findByCorreo(correo);
                    //Extraemos el promedio de pregrado
                    cell = cells.next();
                    Double promedio = Double.valueOf(cell.getNumericCellValue());
                    Estudiante estudiante = null;
                    estudiante = getEstudianteFacade().findByCorreo(correo);
                    if (estudiante == null) {
                        //Se crea un estudiante
                        estudiante = new Estudiante();
                        InformacionAcademica infoAcad = new InformacionAcademica();
                        infoAcad.setPromedioTotal(promedio);
                        estudiante.setInformacion_Academica(infoAcad);
                        NivelFormacion nivel = getNivelFormacionFacade().findByName(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_MAESTRIA));
                        estudiante.setTipoEstudiante(nivel);
                        estudiante.setPersona(persona);
                        getEstudianteFacade().create(estudiante);
                    } else {
                        InformacionAcademica infoAcad = estudiante.getInformacion_Academica();
                        infoAcad.setPromedioTotal(promedio);
                        getInformacionAcademicaFacade().edit(infoAcad);
                        NivelFormacion nivel = estudiante.getTipoEstudiante();
                        if (!nivel.getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_MAESTRIA))) {
                            NivelFormacion nivelMaestria = getNivelFormacionFacade().findByName(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_MAESTRIA));
                            estudiante.setTipoEstudiante(nivelMaestria);
                            estudiante.setPersona(persona);
                            getEstudianteFacade().edit(estudiante);
                        }
                    }
                    //Extraemos el país de la universidad
                    cell = cells.next();
                    String paisUni = cell.getRichStringCellValue().getString().trim();
                    //Extraemos la ciudad de la universidad
                    cell = cells.next();
                    String ciuUni = cell.getRichStringCellValue().getString().trim();
                    //Extraemos la fecha de graduación. Formato dd/MM/aaaa
                    cell = cells.next();
                    Date fechaGrad = cell.getDateCellValue();
                    //Extraemos la universidad en la que cursó el pregrado
                    cell = cells.next();
                    String uniPregrado = cell.getRichStringCellValue().getString().trim();
                    //Extraemos el titulo obtenido
                    cell = cells.next();
                    String titulo = cell.getRichStringCellValue().getString().trim();
                    //Extraemos la hoja de vida
                    cell = cells.next();
                    String contenidoHojaVida = cell.getRichStringCellValue().getString().trim();
                    boolean existePosgrado = true;
                    EstudiantePosgrado posgrado = estudiantePosgradoFacade.findByCorreo(correo);
                    if (posgrado == null) {
                        existePosgrado = false;
                        //Se crea al estudiante de posgrado
                        posgrado = new EstudiantePosgrado();
                        HojaVida hojaVida = new HojaVida();
                        hojaVida.setContenido(contenidoHojaVida);
                        hojaVida.setFechaCreacion(new Date(System.currentTimeMillis()));
                        hojaVida.setFechaActualizacion(new Date(System.currentTimeMillis()));
                        posgrado.setHojaVida(hojaVida);
                    } else {
                        HojaVida hojaVida = posgrado.getHojaVida();
                        hojaVida.setContenido(contenidoHojaVida);
                        hojaVida.setFechaCreacion(new Date(System.currentTimeMillis()));
                        hojaVida.setFechaActualizacion(new Date(System.currentTimeMillis()));
                        posgrado.setHojaVida(hojaVida);
                    }
                    posgrado.setCiudadUniversidadPregrado(ciuUni);
                    posgrado.setEsExterno(Boolean.TRUE);
                    posgrado.setEsPrimerSemestreMaestria(Boolean.TRUE);
                    posgrado.setFechaGraduacion(fechaGrad);
                    Pais paisUniversidad = getPaisFacade().findByNombre(paisUni);
                    posgrado.setPaisUniversidadPregrado(paisUniversidad);
                    posgrado.setTitulo(titulo);
                    posgrado.setUniversidadPregrado(uniPregrado);
                    estudiante = getEstudianteFacade().findByCorreo(correo);
                    posgrado.setEstudiante(estudiante);
                    if (existePosgrado) {
                        getEstudiantePosgradoFacade().edit(posgrado);
                    } else {
                        getEstudiantePosgradoFacade().create(posgrado);
                    }
                }
                respuesta = getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_ESTUDIANTES_EXTERNOS_POR_ARCHIVO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new LinkedList<Secuencia>());
            } //Hubo errores en la lectura del archivo
            else {
                respuesta = getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_ESTUDIANTES_EXTERNOS_POR_ARCHIVO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.BOL_ERR_0001, new LinkedList<Secuencia>());
            }
            return respuesta;
        } catch (Exception e) {
            try {
                Logger.getLogger(EstudiantePostgradoBean.class.getName()).log(Level.SEVERE, null, e);
                String respuesta = getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_ESTUDIANTES_EXTERNOS_POR_ARCHIVO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0002, new LinkedList<Secuencia>());
                return respuesta;
            } catch (Exception ex) {
                Logger.getLogger(EstudiantePostgradoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }
}
