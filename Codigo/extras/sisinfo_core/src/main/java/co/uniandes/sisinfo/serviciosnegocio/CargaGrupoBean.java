package co.uniandes.sisinfo.serviciosnegocio;

import java.io.FileInputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.entities.EstudianteMatriculado;
import co.uniandes.sisinfo.entities.datosmaestros.Estudiante;
import co.uniandes.sisinfo.entities.datosmaestros.InformacionAcademica;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import co.uniandes.sisinfo.entities.datosmaestros.Profesor;
import co.uniandes.sisinfo.entities.datosmaestros.Rol;
import co.uniandes.sisinfo.entities.datosmaestros.Usuario;
import co.uniandes.sisinfo.serviciosfuncionales.EstudianteMatriculadoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.EstudianteFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.GrupoInvestigacionFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.InformacionAcademicaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.NivelFormacionFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.NivelPlantaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.PersonaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.ProfesorFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.ProgramaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import co.uniandes.sisinfo.serviciosfuncionales.seguridad.RolFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.seguridad.UsuarioFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.soporte.PaisFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.soporte.TipoCuentaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.soporte.TipoDocumentoFacadeLocal;

/**
 * Servicios de carga de grupo
 * @author Marcela Morales
 */
@Stateless
public class CargaGrupoBean implements  CargaGrupoBeanLocal {

    //----------------------------------------------
    // ATRIBUTOS
    //----------------------------------------------
    @EJB
    private ConstanteLocal constanteBean;
    @EJB
    private EstudianteMatriculadoFacadeLocal estudianteMatriculadoFacade;
    @EJB
    private PaisFacadeLocal paisFacade;
    @EJB
    private TipoDocumentoFacadeLocal tipoDocumentoFacadeLocal;
    @EJB
    private PersonaFacadeLocal personaFacade;
    @EJB
    private EstudianteFacadeLocal estudianteFacade;
    @EJB
    private ProgramaFacadeLocal programaFacade;
    @EJB
    private TipoCuentaFacadeLocal tipoCuentaFacade;
    @EJB
    private NivelFormacionFacadeLocal nivelFormacionFacade;
    @EJB
    private InformacionAcademicaFacadeLocal infoAcademicaFacade;
    @EJB
    private ProfesorFacadeLocal profesorFacade;
    @EJB
    private GrupoInvestigacionFacadeLocal grupoInvestigacionFacade;
    @EJB
    private NivelPlantaFacadeLocal nivelPlantaFacade;
    @EJB
    private UsuarioFacadeLocal usuarioFacade;
    @EJB
    private RolFacadeLocal rolFacade;
    private ParserT parser;
    private ServiceLocator serviceLocator;
    
    //----------------------------------------------
    // CONSTRUCTOR
    //----------------------------------------------
    public CargaGrupoBean(){
//        try {
//            parser = new ParserT();
//            serviceLocator = new ServiceLocator();
//            estudianteMatriculadoFacade = (EstudianteMatriculadoFacadeLocal) serviceLocator.getLocalEJB(EstudianteMatriculadoFacadeLocal.class);
//            constanteBean = (ConstanteLocal) serviceLocator.getLocalEJB(ConstanteLocal.class);
//            paisFacade = (PaisFacadeLocal) serviceLocator.getLocalEJB(PaisFacadeLocal.class);
//            tipoDocumentoFacadeLocal = (TipoDocumentoFacadeLocal) serviceLocator.getLocalEJB(TipoDocumentoFacadeLocal.class);
//            personaFacade = (PersonaFacadeLocal) serviceLocator.getLocalEJB(PersonaFacadeLocal.class);
//            estudianteFacade = (EstudianteFacadeLocal) serviceLocator.getLocalEJB(EstudianteFacadeLocal.class);
//            programaFacade = (ProgramaFacadeLocal) serviceLocator.getLocalEJB(ProgramaFacadeLocal.class);
//            tipoCuentaFacade = (TipoCuentaFacadeLocal) serviceLocator.getLocalEJB(TipoCuentaFacadeLocal.class);
//            nivelFormacionFacade = (NivelFormacionFacadeLocal) serviceLocator.getLocalEJB(NivelFormacionFacadeLocal.class);
//            infoAcademicaFacade = (InformacionAcademicaFacadeLocal) serviceLocator.getLocalEJB(InformacionAcademicaFacadeLocal.class);
//            profesorFacade = (ProfesorFacadeLocal) serviceLocator.getLocalEJB(ProfesorFacadeLocal.class);
//            grupoInvestigacionFacade = (GrupoInvestigacionFacadeLocal) serviceLocator.getLocalEJB(GrupoInvestigacionFacadeLocal.class);
//            nivelPlantaFacade = (NivelPlantaFacadeLocal) serviceLocator.getLocalEJB(NivelPlantaFacadeLocal.class);
//        } catch (NamingException ex) {
//            Logger.getLogger(CargaGrupoBean.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    //----------------------------------------------
    // MÉTODOS
    //----------------------------------------------
    public String cargarGrupo(String xml){
        try {
            getParser().leerXML(xml);
            //Consulta el tipo de carga de grupo
            Secuencia secCargaGrupo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGA_GRUPO));
            String tipo = secCargaGrupo.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CARGA)).getValor();
            //De acuerdo al tipo de carga, redirecciona al método respectivo
            if(tipo.equals(getConstanteBean().getConstante(Constantes.TIPO_CARGA_GRUPO_ESTUDIANTES_MATRICULADOS))){
                return cargarEstudiantesMatriculados(xml);
            }else if(tipo.equals(getConstanteBean().getConstante(Constantes.TIPO_CARGA_GRUPO_ESTUDIANTES))){
                return cargarEstudiantes(xml);
            }else if(tipo.equals(getConstanteBean().getConstante(Constantes.TIPO_CARGA_GRUPO_PROFESORES))){
                return cargarProfesores(xml);
            }else{
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004, new LinkedList<Secuencia>());
            }
        }catch (Exception e) {
             try {
                Logger.getLogger(CargaGrupoBean.class.getName()).log(Level.SEVERE, null, e);
                String respuesta = getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0003, new LinkedList<Secuencia>());
                return respuesta;
            } catch (Exception ex) {
                Logger.getLogger(CargaGrupoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String cargarEstudiantesMatriculados(String xml) {
        try {
            getParser().leerXML(xml);
            Secuencia secCargaGrupo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGA_GRUPO));
            String nombreArchivo = secCargaGrupo.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA_CARGA_ARCHIVO)).getValor();
            String rutaCarpeta = getConstanteBean().getConstante(Constantes.RUTA_ARCHIVO_CARGA_GRUPO);
            //Verificación del formato del archivo
            String root = rutaCarpeta + nombreArchivo;
            System.out.println("RUTA ARCHIVO"+root);
            if(!root.endsWith("xls")){
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0001, new LinkedList<Secuencia>());
            }
            //Lectura del archivo
            FileInputStream file = new FileInputStream(root);
            POIFSFileSystem fileSystem = new POIFSFileSystem(file);
            HSSFWorkbook workbook = new HSSFWorkbook(fileSystem);
            HSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.rowIterator();
            //Verifica el encabezado del archivo
            if (rows.hasNext()) {
                String nombre = "";
                Row row = rows.next();
                row = rows.next();
                Iterator<Cell> cells = row.cellIterator();
                Cell cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("SEMESTRE")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }                
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("CARNET")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }                
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("APELLIDOS")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }                
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("NOMBRES")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }                
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("PROGRAMA")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }                
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("DOBLE PROG")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }                
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("DOC IDENT")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }                
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("FECHA NAC")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }                
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("SEXO")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }                
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("DIRECCION ESTUD")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }                
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("TELEFONO")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }                
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("SIT ACAD")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }                
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("CRED TOMADOS")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }                
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("CRED APROB")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }                
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("CRED PGA")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }                
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("PROM ACUM")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }                
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("CRED TRANSF")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("ULT SEM CURS")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("CRED SEM TOMADOS")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("CRED SEM APROB")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("CRED SEM PGA")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("PROM SEM")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("SSC")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("EMAIL")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("CRED SEM ACTUAL")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
            }
            //Revisa que no hayan datos faltantes (revisa que el formato de cada celda sea el esperado)
            while (rows.hasNext()) {
                Row row = rows.next();
                Iterator<Cell> cells = row.cellIterator();

                    Cell cell = cells.next();
                    if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                        if (cell.getRichStringCellValue().getString().equals(getConstanteBean().getConstante(Constantes.TAG_FIN_DE_DATOS))) {
                            break;
                        }
                    }
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004, new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004, new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004, new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004, new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004, new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004, new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004, new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004, new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004, new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004, new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004, new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004, new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004, new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004, new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004, new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004, new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004, new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                    }
            }
            //Carga la información del archivo
             rows = sheet.rowIterator();
            if (rows.hasNext()) {
                rows.next();
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
                //SEMESTRE
                String semestre = Integer.toString((int)cell.getNumericCellValue());
                //CARNET
                cell = cells.next();
                String carnet = Integer.toString((int)cell.getNumericCellValue());
                //APELLIDOS
                cell = cells.next();
                String apellidos = cell.getRichStringCellValue().getString().trim();
                //NOMBRES
                cell = cells.next();
                String nombres = cell.getRichStringCellValue().getString().trim();
                //PROGRAMA
                cell = cells.next();
                String programa = cell.getRichStringCellValue().getString().trim();
                //DOBLE PROGRAMA
                cell = cells.next();
                String doblePrograma = cell.getRichStringCellValue().getString().trim();
                 //DOCUMENTO DE IDENTIDAD
                cell = cells.next();
                String documentoDeIdentidad = Integer.toString((int)cell.getNumericCellValue());
                //FECHA DE NACIMIENTO
                cell = cells.next();
                Date fechaNacimiento = cell.getDateCellValue();
                //SEXO
                cell = cells.next();
                String sexo = cell.getRichStringCellValue().getString().trim();
                //DIRECCION
                cell = cells.next();
                String direccion = cell.getRichStringCellValue().getString().trim();
                //TELEFONO
                cell = cells.next();
                String telefono = Integer.toString((int)cell.getNumericCellValue());
                //SITUACION ACADEMICA
                cell = cells.next();
                String situacionAcademica = cell.getRichStringCellValue().getString().trim();
                //CREDITOS TOMADOS
                cell = cells.next();
                double creditosTomados = cell.getNumericCellValue();
                //CREDITOS APROBADOS
                cell = cells.next();
                double creditosAprobados = cell.getNumericCellValue();
                //CREDITOS PGA
                cell = cells.next();
                double creditosPGA = cell.getNumericCellValue();
                //PROMEDIO ACUMULADO
                cell = cells.next();
                double promedioAcumulado = cell.getNumericCellValue();
                //CREDITOS TRANSFERENCIA
                cell = cells.next();
                double creditosTransferencia = cell.getNumericCellValue();
                //ULTIMO SEMESTRE CURSADO
                cell = cells.next();
                String ultimoSemestreCursado = Integer.toString((int)cell.getNumericCellValue());
                //CREDITOS SEMESTRE TOMADOS
                cell = cells.next();
                double creditosSemestreTomados = cell.getNumericCellValue();
                //CREDITOS SEMESTRE APROBADOS
                cell = cells.next();
                double creditosSemestreAprobados = cell.getNumericCellValue();
                //CREDITOS SEMESTRE PGA
                cell = cells.next();
                double creditosSemestrePGA = cell.getNumericCellValue();
                //PROMEDIO SEMESTRE
                cell = cells.next();
                double promedioSemestre = cell.getNumericCellValue();
                //SSC
                cell = cells.next();
                double SSC = cell.getNumericCellValue();
                //EMAIL
                cell = cells.next();
                String email = cell.getRichStringCellValue().getString().trim();
                //CREDITOS SEMESTRE ACTUAL
                cell = cells.next();
                double creditosSemestreActual = cell.getNumericCellValue();
                //Crea y persiste al estudiante matriculado
                EstudianteMatriculado estudianteMatriculado = new EstudianteMatriculado();
                estudianteMatriculado.setSemestre(semestre);
                estudianteMatriculado.setCarnet(carnet);
                estudianteMatriculado.setApellidos(apellidos);
                estudianteMatriculado.setNombres(nombres);
                estudianteMatriculado.setPrograma(programa);
                estudianteMatriculado.setDoblePrograma(doblePrograma);
                estudianteMatriculado.setDocumentoDeIdentidad(documentoDeIdentidad);
                estudianteMatriculado.setFechaNacimiento(new Timestamp(fechaNacimiento.getTime()));
                estudianteMatriculado.setSexo(sexo);
                estudianteMatriculado.setDireccion(direccion);
                estudianteMatriculado.setTelefono(telefono);
                estudianteMatriculado.setSituacionAcademica(situacionAcademica);
                estudianteMatriculado.setCreditosTomados(creditosTomados);
                estudianteMatriculado.setCreditosAprobados(creditosAprobados);
                estudianteMatriculado.setCreditosPGA(creditosPGA);
                estudianteMatriculado.setPromedioAcumulado(promedioAcumulado);
                estudianteMatriculado.setCreditosTransferencia(creditosTransferencia);
                estudianteMatriculado.setUltimoSemestreCursado(ultimoSemestreCursado);
                estudianteMatriculado.setCreditosSemestreTomados(creditosSemestreTomados);
                estudianteMatriculado.setCreditosSemestreAprobados(creditosSemestreAprobados);
                estudianteMatriculado.setCreditosSemestrePGA(creditosSemestrePGA);
                estudianteMatriculado.setPromedioSemestre(promedioSemestre);
                estudianteMatriculado.setSSC(SSC);
                estudianteMatriculado.setEmail(email);
                estudianteMatriculado.setCreditosSemestreActual(creditosSemestreActual);
                //Si no existe un estudiante con el carnet dado, lo persiste
                EstudianteMatriculado estudianteExiste = getEstudianteMatriculadoFacade().findByCarnet(carnet);
                if(estudianteExiste == null)
                    getEstudianteMatriculadoFacade().create(estudianteMatriculado);
            }
            //Genera y retorna la respuesta de éxito
            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CARGA_MSJ_0001, new LinkedList<Secuencia>());
        } catch (Exception e) {
             try {
                Logger.getLogger(CargaGrupoBean.class.getName()).log(Level.SEVERE, null, e);
                String respuesta = getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0003, new LinkedList<Secuencia>());
                return respuesta;
            } catch (Exception ex) {
                Logger.getLogger(CargaGrupoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String cargarEstudiantes(String xml) {
        try {
            getParser().leerXML(xml);
            Secuencia secCargaGrupo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGA_GRUPO));
            String nombreArchivo = secCargaGrupo.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA_CARGA_ARCHIVO)).getValor();
            String rutaCarpeta = getConstanteBean().getConstante(Constantes.RUTA_ARCHIVO_CARGA_GRUPO);
            //Verificación del formato del archivo
            String root = rutaCarpeta + nombreArchivo;
            System.out.println("RUTA ARCHIVO:"+root);
            if(!root.endsWith("xls")){
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0001, new LinkedList<Secuencia>());
            }
            //Lectura del archivo
            FileInputStream file = new FileInputStream(root);
            POIFSFileSystem fileSystem = new POIFSFileSystem(file);
            HSSFWorkbook workbook = new HSSFWorkbook(fileSystem);
            HSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.rowIterator();
            //Verifica el encabezado del archivo
            if (rows.hasNext()) {
                String nombre = "";
                Row row = rows.next();
                row = rows.next();
                Iterator<Cell> cells = row.cellIterator();
                Cell cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("SEMESTRE")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("CODIGO")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("APELLIDOS")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("NOMBRES")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("CORREO")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("CORREO_ALTERNO")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("TIPO_DOCUMENTO")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("NUM_DOCUMENTO")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("FECHA_NACIMIENTO")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("CIUDAD_NACIMIENTO")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("PAIS_NACIMIENTO")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("SEXO")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("DIRECCION")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("TELEFONO")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("CELULAR")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("EXTENSION")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("PROGRAMA")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("DOBLE_PROGRAMA")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("NIVEL_PROGRAMA")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("AVISAR_EMERGENCIA_APELLIDOS")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("AVISAR_EMERGENCIA_NOMBRES")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("AVISAR_EMERGENCIA_TELEFONO")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("BANCO")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("TIPO_CUENTA")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("CUENTA_BANCARIA")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("SITUACION_ACADEMICA")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("CREDITOS_APROBADOS")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("CREDITOS_CURSADOS")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("CREDITOS_SEMESTRE_ACTUAL")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("PROMEDIO_ANTEPENULTIMO")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("PROMEDIO_PENULTIMO")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("PROMEDIO_ULTIMO")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("PROMEDIO_TOTAL")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("SEMESTRE_SEGUN_CREDITOS")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
            }
            //Revisa que no hayan datos faltantes (revisa que el formato de cada celda sea el esperado)
            while (rows.hasNext()) {
                Row row = rows.next();
                Iterator<Cell> cells = row.cellIterator();

                    Cell cell = cells.next();
                    if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                        if (cell.getRichStringCellValue().getString().equals(getConstanteBean().getConstante(Constantes.TAG_FIN_DE_DATOS))) {
                            break;
                        }
                    }
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004, new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004, new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004, new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004, new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004, new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004, new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004, new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC){
                        if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                            if(!cell.getRichStringCellValue().toString().equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                               return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"2", new LinkedList<Secuencia>());
                        }else{
                            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"2", new LinkedList<Secuencia>());
                        }
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC){
                        if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                            if(!cell.getRichStringCellValue().toString().equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                               return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"2", new LinkedList<Secuencia>());
                        }else{
                            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"2", new LinkedList<Secuencia>());
                        }
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004, new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004, new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004, new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004, new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC){
                        if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                            if(!cell.getRichStringCellValue().toString().equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                               return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"2", new LinkedList<Secuencia>());
                        }else{
                            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"2", new LinkedList<Secuencia>());
                        }
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC){
                        if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                            if(!cell.getRichStringCellValue().toString().equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                               return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"2", new LinkedList<Secuencia>());
                        }else{
                            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"2", new LinkedList<Secuencia>());
                        }
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC){
                        if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                            if(!cell.getRichStringCellValue().toString().equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                               return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"2", new LinkedList<Secuencia>());
                        }else{
                            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"2", new LinkedList<Secuencia>());
                        }
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004, new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002+"1", new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002+"2", new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002+"3", new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002+"4", new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC){
                        if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                            if(!cell.getRichStringCellValue().toString().equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                               return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"2", new LinkedList<Secuencia>());
                        }else{
                            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"2", new LinkedList<Secuencia>());
                        }
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002+"6", new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002+"7", new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC){
                        if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                            if(!cell.getRichStringCellValue().toString().equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                               return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"2", new LinkedList<Secuencia>());
                        }else{
                            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"2", new LinkedList<Secuencia>());
                        }
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002+"9", new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC){
                        if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                            if(!cell.getRichStringCellValue().toString().equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                               return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"2", new LinkedList<Secuencia>());
                        }else{
                            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"2", new LinkedList<Secuencia>());
                        }
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC){
                        if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                            if(!cell.getRichStringCellValue().toString().equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                               return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"2", new LinkedList<Secuencia>());
                        }else{
                            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"2", new LinkedList<Secuencia>());
                        }
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC){
                        if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                            if(!cell.getRichStringCellValue().toString().equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                               return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"2", new LinkedList<Secuencia>());
                        }else{
                            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"2", new LinkedList<Secuencia>());
                        }
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC){
                        if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                            if(!cell.getRichStringCellValue().toString().equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                               return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"2", new LinkedList<Secuencia>());
                        }else{
                            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"2", new LinkedList<Secuencia>());
                        }
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC){
                        if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                            if(!cell.getRichStringCellValue().toString().equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                               return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"2", new LinkedList<Secuencia>());
                        }else{
                            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"2", new LinkedList<Secuencia>());
                        }
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC){
                        if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                            if(!cell.getRichStringCellValue().toString().equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                               return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"2", new LinkedList<Secuencia>());
                        }else{
                            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"2", new LinkedList<Secuencia>());
                        }
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC){
                        if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                            if(!cell.getRichStringCellValue().toString().equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                               return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"2", new LinkedList<Secuencia>());
                        }else{
                            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"2", new LinkedList<Secuencia>());
                        }
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC){
                        if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                            if(!cell.getRichStringCellValue().toString().equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                               return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"2", new LinkedList<Secuencia>());
                        }else{
                            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"2", new LinkedList<Secuencia>());
                        }
                    }
            }
            //Carga la información del archivo
            rows = sheet.rowIterator();
            if (rows.hasNext()) {
                rows.next();
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
                //SEMESTRE (OBLIGATORIO)
                String semestre = Integer.toString((int)cell.getNumericCellValue());
                //CODIGO (OBLIGATORIO)
                cell = cells.next();
                String codigo = Integer.toString((int)cell.getNumericCellValue());
                //APELLIDOS (OBLIGATORIO)
                cell = cells.next();
                String apellidos = cell.getRichStringCellValue().getString().trim();
                //NOMBRES (OBLIGATORIO)
                cell = cells.next();
                String nombres = cell.getRichStringCellValue().getString().trim();
                //CORREO (OBLIGATORIO)
                cell = cells.next();
                String correo = cell.getRichStringCellValue().getString().trim();
                //CORREO_ALTERNO
                cell = cells.next();
                String correoAlterno = cell.getRichStringCellValue().getString().trim();
                if(correoAlterno.equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                    correoAlterno = null;
                //TIPO_DOCUMENTO
                cell = cells.next();
                String tipoDocumento = cell.getRichStringCellValue().getString().trim();
                if(tipoDocumento.equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                    tipoDocumento = null;
                //NUM_DOCUMENTO
                cell = cells.next();
                String documentoDeIdentidad = null;
                try{
                    documentoDeIdentidad = Integer.toString((int)cell.getNumericCellValue());
                } catch(IllegalStateException e){
                    documentoDeIdentidad = cell.getRichStringCellValue().getString().trim();
                    if(documentoDeIdentidad.equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                        documentoDeIdentidad = null;
                }
                //FECHA_NACIMIENTO
                cell = cells.next();
                Date fechaNacimiento = null;
                try{
                    fechaNacimiento = cell.getDateCellValue();
                } catch(IllegalStateException e){
                    String fecha = cell.getRichStringCellValue().getString().trim();
                    if(fecha.equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                        fechaNacimiento = null;
                }
                //CIUDAD_NACIMIENTO
                cell = cells.next();
                String ciudadNacimiento = cell.getRichStringCellValue().getString().trim();
                if(ciudadNacimiento.equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                    ciudadNacimiento = null;
                //PAIS_NACIMIENTO
                cell = cells.next();
                String paisNacimiento = cell.getRichStringCellValue().getString().trim();
                if(paisNacimiento.equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                    paisNacimiento = null;
                //SEXO
                cell = cells.next();
                String sexo = cell.getRichStringCellValue().getString().trim();
                if(sexo.equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                    sexo = null;
                //DIRECCION
                cell = cells.next();
                String direccion = cell.getRichStringCellValue().getString().trim();
                if(direccion.equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                    direccion = null;
                //TELEFONO
                cell = cells.next();
                String telefono = null;
                try{
                    telefono = Integer.toString((int)cell.getNumericCellValue());
                } catch(IllegalStateException e){
                    telefono = cell.getRichStringCellValue().getString().trim();
                    if(telefono.equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                        telefono = null;
                }
                //CELULAR
                cell = cells.next();
                String celular = null;
                try{
                    celular = Integer.toString((int)cell.getNumericCellValue());
                } catch(IllegalStateException e){
                    celular = cell.getRichStringCellValue().getString().trim();
                    if(celular.equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                        celular = null;
                }
                //EXTENSION
                cell = cells.next();
                String extension = null;
                try{
                    extension = Integer.toString((int)cell.getNumericCellValue());
                } catch(IllegalStateException e){
                    extension = cell.getRichStringCellValue().getString().trim();
                    if(extension.equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                        extension = null;
                }
                //PROGRAMA (OBLIGATORIO)
                cell = cells.next();
                String programa = cell.getRichStringCellValue().getString().trim();
                //DOBLE_PROGRAMA
                cell = cells.next();
                String doblePrograma = cell.getRichStringCellValue().getString().trim();
                if(doblePrograma.equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                    doblePrograma = null;
                //NIVEL_PROGRAMA (OBLIGATORIO)
                cell = cells.next();
                String nivelPrograma = cell.getRichStringCellValue().getString().trim();
                //AVISAR_EMERGENCIA_APELLIDOS
                cell = cells.next();
                String emergenciaApellidos = cell.getRichStringCellValue().getString().trim();
                if(emergenciaApellidos.equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                    emergenciaApellidos = null;
                //AVISAR_EMERGENCIA_NOMBRES
                cell = cells.next();
                String emergenciaNombres = cell.getRichStringCellValue().getString().trim();
                if(emergenciaNombres.equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                    emergenciaNombres = null;
                //AVISAR_EMERGENCIA_TELEFONO
                cell = cells.next();
                String emergenciaTelefono = null;
                try{
                    emergenciaTelefono = Integer.toString((int)cell.getNumericCellValue());
                } catch(IllegalStateException e){
                    emergenciaTelefono = cell.getRichStringCellValue().getString().trim();
                    if(emergenciaTelefono.equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                        emergenciaTelefono = null;
                }
                //BANCO
                cell = cells.next();
                String banco = cell.getRichStringCellValue().getString().trim();
                if(banco.equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                    banco = null;
                //TIPO_CUENTA
                cell = cells.next();
                String tipoCuenta = cell.getRichStringCellValue().getString().trim();
                if(tipoCuenta.equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                    tipoCuenta = null;
                //CUENTA_BANCARIA
                cell = cells.next();
                String cuentaBancaria = null;
                try{
                    cuentaBancaria = Integer.toString((int)cell.getNumericCellValue());
                } catch(IllegalStateException e){
                    cuentaBancaria = cell.getRichStringCellValue().getString().trim();
                    if(cuentaBancaria.equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                        cuentaBancaria = null;
                }
                //SITUACION_ACADEMICA
                cell = cells.next();
                String situacionAcademica = cell.getRichStringCellValue().getString().trim();
                if(situacionAcademica.equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                    situacionAcademica = null;
                //CREDITOS_APROBADOS
                cell = cells.next();
                double creditosAprobados = 0.0;
                try{
                    creditosAprobados = cell.getNumericCellValue();
                } catch(IllegalStateException e){
                    String creditos = cell.getRichStringCellValue().getString().trim();
                    if(creditos.equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                        creditosAprobados = 0.0;
                }
                //CREDITOS_CURSADOS
                cell = cells.next();
                double creditosCursados = 0.0;
                try{
                    creditosCursados = cell.getNumericCellValue();
                } catch(IllegalStateException e){
                    String creditos = cell.getRichStringCellValue().getString().trim();
                    if(creditos.equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                        creditosCursados = 0.0;
                }
                //CREDITOS_SEMESTRE_ACTUAL
                cell = cells.next();
                double creditosSemestreActual = 0.0;
                try{
                    creditosSemestreActual = cell.getNumericCellValue();
                } catch(IllegalStateException e){
                    String creditos = cell.getRichStringCellValue().getString().trim();
                    if(creditos.equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                        creditosSemestreActual = 0.0;
                }
                //PROMEDIO_ANTEPENULTIMO
                cell = cells.next();
                double promedioAntepenultimo = 0.0;
                try{
                    promedioAntepenultimo = cell.getNumericCellValue();
                } catch(IllegalStateException e){
                    String promedio = cell.getRichStringCellValue().getString().trim();
                    if(promedio.equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                        promedioAntepenultimo = 0.0;
                }
                //PROMEDIO_PENULTIMO
                cell = cells.next();
                double promedioPenultimo = 0.0;
                try{
                    promedioPenultimo = cell.getNumericCellValue();
                } catch(IllegalStateException e){
                    String promedio = cell.getRichStringCellValue().getString().trim();
                    if(promedio.equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                        promedioPenultimo = 0.0;
                }
                //PROMEDIO_ULTIMO
                cell = cells.next();
                double promedioUltimo = 0.0;
                try{
                    promedioUltimo = cell.getNumericCellValue();
                } catch(IllegalStateException e){
                    String promedio = cell.getRichStringCellValue().getString().trim();
                    if(promedio.equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                        promedioUltimo = 0.0;
                }
                //PROMEDIO_TOTAL
                cell = cells.next();
                double promedioTotal = 0.0;
                try{
                    promedioTotal = cell.getNumericCellValue();
                } catch(IllegalStateException e){
                    String promedio = cell.getRichStringCellValue().getString().trim();
                    if(promedio.equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                        promedioTotal = 0.0;
                }
                //SEMESTRE_SEGUN_CREDITOS
                cell = cells.next();
                String semestreSegunCreditos = null;
                try{
                    semestreSegunCreditos = Integer.toString((int)cell.getNumericCellValue());
                } catch(IllegalStateException e){
                    semestreSegunCreditos = cell.getRichStringCellValue().getString().trim();
                    if(semestreSegunCreditos.equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                        semestreSegunCreditos = null;
                }

                //Busca la persona.
                //1. Si la persona no existe: crea la persona
                //2. Si la persona existe: actualiza la información de la persona
                Persona persona = getPersonaFacade().findByCorreo(correo);
                if(persona == null){
                    persona = new Persona();
                    persona.setActivo(true);
                    persona.setApellidos(apellidos);
                    persona.setCelular(celular);
                    persona.setCiudadNacimiento(ciudadNacimiento);
                    persona.setCodigo(codigo);
                    persona.setCorreo(correo);
                    persona.setCorreoAlterno(correoAlterno);
                    persona.setDireccionResidencia(direccion);
                    persona.setExtension(extension);
                    if(fechaNacimiento != null) persona.setFechaNacimiento(new Timestamp(fechaNacimiento.getTime()));
                    else persona.setFechaNacimiento(null);
                    persona.setNombres(nombres);
                    persona.setNumDocumentoIdentidad(documentoDeIdentidad);
                    if(paisNacimiento != null) persona.setPais(getPaisFacade().findByNombre(paisNacimiento));
                    else persona.setPais(null);
                    persona.setTelefono(telefono);
                    if(tipoDocumento != null) persona.setTipoDocumento(getTipoDocumentoFacadeLocal().findByTipo(tipoDocumento));
                    else persona.setTipoDocumento(null);
                    getPersonaFacade().create(persona);
                }else{
                    persona.setActivo(true);
                    persona.setApellidos(apellidos);
                    persona.setCelular(celular);
                    persona.setCiudadNacimiento(ciudadNacimiento);
                    persona.setCodigo(codigo);
                    persona.setCorreo(correo);
                    persona.setCorreoAlterno(correoAlterno);
                    persona.setDireccionResidencia(direccion);
                    persona.setExtension(extension);
                    if(fechaNacimiento != null) persona.setFechaNacimiento(new Timestamp(fechaNacimiento.getTime()));
                    else persona.setFechaNacimiento(null);
                    persona.setNombres(nombres);
                    persona.setNumDocumentoIdentidad(documentoDeIdentidad);
                    if(paisNacimiento != null) persona.setPais(getPaisFacade().findByNombre(paisNacimiento));
                    else persona.setPais(null);
                    persona.setTelefono(telefono);
                    if(tipoDocumento != null) persona.setTipoDocumento(getTipoDocumentoFacadeLocal().findByTipo(tipoDocumento));
                    else persona.setTipoDocumento(null);
                    getPersonaFacade().edit(persona);
                }

                //Busca el usuario
                //1. Si el usuario no existe: crea el usuario con el rol estudiante y lo asocia a la persona creada anteriormente
                //2. Si el usuario existe: se agrega el rol estudiante al usuario existente
                Usuario usuario = getUsuarioFacade().findByLogin(correo);
                if (usuario == null) {
                    usuario = new Usuario();
                    usuario.setEsPersona(true);
                    usuario.setPersona(getPersonaFacade().findByCorreo(correo));
                    Collection<Rol> roles = new ArrayList<Rol>();
                    Rol rol = getRolFacade().findByRol(getConstanteBean().getConstante(Constantes.ROL_ESTUDIANTE));
                    roles.add(rol);
                    usuario.setRoles(roles);
                    getUsuarioFacade().create(usuario);
                } else {
                    Collection<Rol> roles = usuario.getRoles();
                    Rol rol = getRolFacade().findByRol(getConstanteBean().getConstante(Constantes.ROL_ESTUDIANTE));
                    roles.add(rol);
                    usuario.setRoles(roles);
                    getUsuarioFacade().edit(usuario);
                }

                //Busca el estudiante
                //1. Si el estudiante no existe: crea el estudiante
                //2. Si el estudiante ya existe: actualiza la información del estudiante
                Estudiante estudiante = getEstudianteFacade().findByCorreo(correo);
                if(estudiante == null){
                    estudiante = new Estudiante();
                    //Información personal
                    estudiante.setPersona(getPersonaFacade().findByCorreo(correo));
                    //Información estudiante
                    estudiante.setPrograma(getProgramaFacade().findByNombre(programa));
                    if(doblePrograma != null) estudiante.setDoblePrograma(getProgramaFacade().findByNombre(doblePrograma));
                    else estudiante.setDoblePrograma(null);
                    estudiante.setTipoEstudiante(getNivelFormacionFacade().findByName(nivelPrograma));
                    //Información bancaria
                    estudiante.setBanco(banco);
                    estudiante.setCuentaBancaria(cuentaBancaria);
                    if(tipoCuenta != null) estudiante.setTipoCuenta(getTipoCuentaFacade().findByName(tipoCuenta).get(0));
                    else estudiante.setTipoCuenta(null);
                    //Información de emergencia
                    estudiante.setAvisarEmergenciaApellidos(emergenciaApellidos);
                    estudiante.setAvisarEmergenciaNombres(emergenciaNombres);
                    estudiante.setTelefonoEmergencia(emergenciaTelefono);
                    //Información académica
                    InformacionAcademica informacionAcademica = new InformacionAcademica();
                    informacionAcademica.setCreditosAprobados(creditosAprobados);
                    informacionAcademica.setCreditosCursados(creditosCursados);
                    informacionAcademica.setCreditosMonitoriasISISEsteSemestre(0.0);
                    informacionAcademica.setCreditosSemestreActual(creditosSemestreActual);
                    informacionAcademica.setPromedioAntepenultipo(promedioAntepenultimo);
                    informacionAcademica.setPromedioPenultimo(promedioPenultimo);
                    informacionAcademica.setPromedioTotal(promedioTotal);
                    informacionAcademica.setPromedioUltimo(promedioUltimo);
                    informacionAcademica.setSemestreSegunCreditos(semestreSegunCreditos);
                    getInfoAcademicaFacade().create(informacionAcademica);
                   // estudiante.setInformacion_Academica(getInfoAcademicaFacade().findByCodigoEstudiante(codigo));

                    getEstudianteFacade().create(estudiante);
                }else{
                    //Información estudiante
                    estudiante.setPrograma(getProgramaFacade().findByNombre(programa));
                    if(doblePrograma != null) estudiante.setDoblePrograma(getProgramaFacade().findByNombre(doblePrograma));
                    else estudiante.setDoblePrograma(null);
                    estudiante.setTipoEstudiante(getNivelFormacionFacade().findByName(nivelPrograma));
                    //Información bancaria
                    estudiante.setBanco(banco);
                    estudiante.setCuentaBancaria(cuentaBancaria);
                    if(tipoCuenta != null) estudiante.setTipoCuenta(getTipoCuentaFacade().findByName(tipoCuenta).get(0));
                    else estudiante.setTipoCuenta(null);
                    //Información de emergencia
                    estudiante.setAvisarEmergenciaApellidos(emergenciaApellidos);
                    estudiante.setAvisarEmergenciaNombres(emergenciaNombres);
                    estudiante.setTelefonoEmergencia(emergenciaTelefono);
                    //Información académica
                    InformacionAcademica informacionAcademica = null;//getInfoAcademicaFacade().findByCodigoEstudiante(codigo);
                    if(informacionAcademica == null){
                        informacionAcademica = new InformacionAcademica();
                        informacionAcademica.setCreditosAprobados(creditosAprobados);
                        informacionAcademica.setCreditosCursados(creditosCursados);
                        informacionAcademica.setCreditosMonitoriasISISEsteSemestre(0.0);
                        informacionAcademica.setCreditosSemestreActual(creditosSemestreActual);
                        informacionAcademica.setPromedioAntepenultipo(promedioAntepenultimo);
                        informacionAcademica.setPromedioPenultimo(promedioPenultimo);
                        informacionAcademica.setPromedioTotal(promedioTotal);
                        informacionAcademica.setPromedioUltimo(promedioUltimo);
                        informacionAcademica.setSemestreSegunCreditos(semestreSegunCreditos);
                        getInfoAcademicaFacade().create(informacionAcademica);
                    }else{
                        informacionAcademica.setCreditosAprobados(creditosAprobados);
                        informacionAcademica.setCreditosCursados(creditosCursados);
                        informacionAcademica.setCreditosMonitoriasISISEsteSemestre(0.0);
                        informacionAcademica.setCreditosSemestreActual(creditosSemestreActual);
                        informacionAcademica.setPromedioAntepenultipo(promedioAntepenultimo);
                        informacionAcademica.setPromedioPenultimo(promedioPenultimo);
                        informacionAcademica.setPromedioTotal(promedioTotal);
                        informacionAcademica.setPromedioUltimo(promedioUltimo);
                        informacionAcademica.setSemestreSegunCreditos(semestreSegunCreditos);
                        getInfoAcademicaFacade().edit(informacionAcademica);
                    }
                    //estudiante.setInformacion_Academica(getInfoAcademicaFacade().findByCodigoEstudiante(codigo));
                    
                    getEstudianteFacade().edit(estudiante);
                }
            }

            //Genera y retorna la respuesta de éxito
            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CARGA_MSJ_0001, new LinkedList<Secuencia>());
        } catch (Exception e) {
           e.printStackTrace();
             try {
                Logger.getLogger(CargaGrupoBean.class.getName()).log(Level.SEVERE, null, e);
                String respuesta = getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0003, new LinkedList<Secuencia>());
                return respuesta;
            } catch (Exception ex) {
                Logger.getLogger(CargaGrupoBean.class.getName()).log(Level.SEVERE, null, ex);
                 return null;
            }
        }
    }

    public String cargarProfesores(String xml) {
        try {
            getParser().leerXML(xml);
            Secuencia secCargaGrupo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGA_GRUPO));
            String nombreArchivo = secCargaGrupo.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA_CARGA_ARCHIVO)).getValor();
            String rutaCarpeta = getConstanteBean().getConstante(Constantes.RUTA_ARCHIVO_CARGA_GRUPO);
            //Verificación del formato del archivo
            String root = rutaCarpeta + nombreArchivo;
            System.out.println("RUTA ARCHIVO"+root);
            if(!root.endsWith("xls")){
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0001, new LinkedList<Secuencia>());
            }
            //Lectura del archivo
            FileInputStream file = new FileInputStream(root);
            POIFSFileSystem fileSystem = new POIFSFileSystem(file);
            HSSFWorkbook workbook = new HSSFWorkbook(fileSystem);
            HSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.rowIterator();
            //Verifica el encabezado del archivo
            if (rows.hasNext()) {
                String nombre = "";
                Row row = rows.next();
                row = rows.next();
                Iterator<Cell> cells = row.cellIterator();
                Cell cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("SEMESTRE")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("CODIGO")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("APELLIDOS")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("NOMBRES")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("CORREO")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("CORREO_ALTERNO")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("TIPO_DOCUMENTO")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("NUM_DOCUMENTO")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("FECHA_NACIMIENTO")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("CIUDAD_NACIMIENTO")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("PAIS_NACIMIENTO")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("SEXO")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("DIRECCION")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("TELEFONO")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("CELULAR")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("EXTENSION")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("OFICINA")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("TIPO_PROFESOR")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("NIVEL_FORMACION")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("NIVEL_PLANTA")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("GRUPO_INVESTIGACION")) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                }
            }
            //Revisa que no hayan datos faltantes (revisa que el formato de cada celda sea el esperado)
            while (rows.hasNext()) {
                Row row = rows.next();
                Iterator<Cell> cells = row.cellIterator();

                    Cell cell = cells.next();
                    if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                        if (cell.getRichStringCellValue().getString().equals(getConstanteBean().getConstante(Constantes.TAG_FIN_DE_DATOS))) {
                            break;
                        }
                    }
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"1", new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC){
                        if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                            if(!cell.getRichStringCellValue().toString().equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                               return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"2", new LinkedList<Secuencia>());
                        }else{
                            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"2", new LinkedList<Secuencia>());
                        }
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"3", new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"4", new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"5", new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"6", new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"7", new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC){
                        if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                            if(!cell.getRichStringCellValue().toString().equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                               return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"2", new LinkedList<Secuencia>());
                        }else{
                            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"2", new LinkedList<Secuencia>());
                        }
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC){
                        if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                            if(!cell.getRichStringCellValue().toString().equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                               return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"2", new LinkedList<Secuencia>());
                        }else{
                            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"2", new LinkedList<Secuencia>());
                        }
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"10", new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"11", new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"12", new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"13", new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC){
                        if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                            if(!cell.getRichStringCellValue().toString().equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                               return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"2", new LinkedList<Secuencia>());
                        }else{
                            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"2", new LinkedList<Secuencia>());
                        }
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC){
                        if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                            if(!cell.getRichStringCellValue().toString().equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                               return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"2", new LinkedList<Secuencia>());
                        }else{
                            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"2", new LinkedList<Secuencia>());
                        }
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC){
                        if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                            if(!cell.getRichStringCellValue().toString().equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                               return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"2", new LinkedList<Secuencia>());
                        }else{
                            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"2", new LinkedList<Secuencia>());
                        }
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0004+"17", new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                    }
                    cell = cells.next();
                    if(cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0002, new LinkedList<Secuencia>());
                    }
            }
            //Carga la información del archivo
             rows = sheet.rowIterator();
            if (rows.hasNext()) {
                rows.next();
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
                //SEMESTRE (OBLIGATORIO)
                String semestre = Integer.toString((int)cell.getNumericCellValue());
                //CODIGO
                cell = cells.next();
                String codigo = null;
                try{
                    codigo = Integer.toString((int)cell.getNumericCellValue());
                } catch(IllegalStateException e){
                    codigo = cell.getRichStringCellValue().getString().trim();
                    if(codigo.equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                        codigo = null;
                }
                //APELLIDOS (OBLIGATORIO)
                cell = cells.next();
                String apellidos = cell.getRichStringCellValue().getString().trim();
                //NOMBRES (OBLIGATORIO)
                cell = cells.next();
                String nombres = cell.getRichStringCellValue().getString().trim();
                //CORREO (OBLIGATORIO)
                cell = cells.next();
                String correo = cell.getRichStringCellValue().getString().trim();
                //CORREO_ALTERNO
                cell = cells.next();
                String correoAlterno = cell.getRichStringCellValue().getString().trim();
                if(correoAlterno.equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                    correoAlterno = null;
                //TIPO_DOCUMENTO
                cell = cells.next();
                String tipoDocumento = cell.getRichStringCellValue().getString().trim();
                if(tipoDocumento.equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                    tipoDocumento = null;
                //NUM_DOCUMENTO
                cell = cells.next();
                String documentoDeIdentidad = null;
                try{
                    documentoDeIdentidad = Integer.toString((int)cell.getNumericCellValue());
                } catch(IllegalStateException e){
                    documentoDeIdentidad = cell.getRichStringCellValue().getString().trim();
                    if(documentoDeIdentidad.equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                        documentoDeIdentidad = null;
                }
                //FECHA_NACIMIENTO
                cell = cells.next();
                Date fechaNacimiento = null;
                try{
                    fechaNacimiento = cell.getDateCellValue();
                } catch(IllegalStateException e){
                    String fecha = cell.getRichStringCellValue().getString().trim();
                    if(fecha.equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                        fechaNacimiento = null;
                }
                //CIUDAD_NACIMIENTO
                cell = cells.next();
                String ciudadNacimiento = cell.getRichStringCellValue().getString().trim();
                if(ciudadNacimiento.equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                    ciudadNacimiento = null;
                //PAIS_NACIMIENTO
                cell = cells.next();
                String paisNacimiento = cell.getRichStringCellValue().getString().trim();
                if(paisNacimiento.equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                    paisNacimiento = null;
                //SEXO
                cell = cells.next();
                String sexo = cell.getRichStringCellValue().getString().trim();
                if(sexo.equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                    sexo = null;
                //DIRECCION
                cell = cells.next();
                String direccion = cell.getRichStringCellValue().getString().trim();
                if(direccion.equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                    direccion = null;
                //TELEFONO
                cell = cells.next();
                String telefono = null;
                try{
                    telefono = Integer.toString((int)cell.getNumericCellValue());
                } catch(IllegalStateException e){
                    telefono = cell.getRichStringCellValue().getString().trim();
                    if(telefono.equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                        telefono = null;
                }
                //CELULAR
                cell = cells.next();
                String celular = null;
                try{
                    celular = Integer.toString((int)cell.getNumericCellValue());
                } catch(IllegalStateException e){
                    celular = cell.getRichStringCellValue().getString().trim();
                    if(celular.equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                        celular = null;
                }
                //EXTENSION
                cell = cells.next();
                String extension = null;
                try{
                    extension = Integer.toString((int)cell.getNumericCellValue());
                } catch(IllegalStateException e){
                    extension = cell.getRichStringCellValue().getString().trim();
                    if(extension.equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                        extension = null;
                }
                //OFICINA
                cell = cells.next();
                String oficina = cell.getRichStringCellValue().getString().trim();
                if(oficina.equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                    oficina = null;
                //TIPO_PROFESOR (OBLIGATORIO)
                cell = cells.next();
                String tipoProfesor = cell.getRichStringCellValue().getString().trim();
                if(tipoProfesor.equals("Planta")) tipoProfesor = getConstanteBean().getConstante(Constantes.VAL_TIPO_PROFESOR_CATEDRA);
                else if(tipoProfesor.equals("Cátedra")) tipoProfesor = getConstanteBean().getConstante(Constantes.VAL_TIPO_PROFESOR_PLANTA);
                //NIVEL_FORMACION
                cell = cells.next();
                String nivelFormacion = cell.getRichStringCellValue().getString().trim();
                if(nivelFormacion.equalsIgnoreCase(getConstanteBean().getConstante(Constantes.TAG_RAYITA)))
                    nivelFormacion = null;
                //NIVEL_PLANTA (OBLIGATORIO)
                cell = cells.next();
                String nivelPlanta = cell.getRichStringCellValue().getString().trim();
                //GRUPO_INVESTIGACION (OBLIGATORIO)
                cell = cells.next();
                String grupoInvestigacion = cell.getRichStringCellValue().getString().trim();
                
                //Busca la persona.
                //1. Si la persona no existe: crea la persona
                //2. Si la persona existe: actualiza la información de la persona
                Persona persona = getPersonaFacade().findByCorreo(correo);
                if(persona == null){
                    persona = new Persona();
                    persona.setActivo(true);
                    persona.setApellidos(apellidos);
                    persona.setCelular(celular);
                    persona.setCiudadNacimiento(ciudadNacimiento);
                    persona.setCodigo(codigo);
                    persona.setCorreo(correo);
                    persona.setCorreoAlterno(correoAlterno);
                    persona.setDireccionResidencia(direccion);
                    persona.setExtension(extension);
                    if(fechaNacimiento != null) persona.setFechaNacimiento(new Timestamp(fechaNacimiento.getTime()));
                    else persona.setFechaNacimiento(null);
                    persona.setNombres(nombres);
                    persona.setNumDocumentoIdentidad(documentoDeIdentidad);
                    if(paisNacimiento != null) persona.setPais(getPaisFacade().findByNombre(paisNacimiento));
                    else persona.setPais(null);
                    persona.setTelefono(telefono);
                    if(tipoDocumento != null) persona.setTipoDocumento(getTipoDocumentoFacadeLocal().findByTipo(tipoDocumento));
                    else persona.setTipoDocumento(null);
                    getPersonaFacade().create(persona);
                }else{
                    persona.setActivo(true);
                    persona.setApellidos(apellidos);
                    persona.setCelular(celular);
                    persona.setCiudadNacimiento(ciudadNacimiento);
                    persona.setCodigo(codigo);
                    persona.setCorreo(correo);
                    persona.setCorreoAlterno(correoAlterno);
                    persona.setDireccionResidencia(direccion);
                    persona.setExtension(extension);
                    if(fechaNacimiento != null) persona.setFechaNacimiento(new Timestamp(fechaNacimiento.getTime()));
                    else persona.setFechaNacimiento(null);
                    persona.setNombres(nombres);
                    persona.setNumDocumentoIdentidad(documentoDeIdentidad);
                    if(paisNacimiento != null) persona.setPais(getPaisFacade().findByNombre(paisNacimiento));
                    else persona.setPais(null);
                    persona.setTelefono(telefono);
                    if(tipoDocumento != null) persona.setTipoDocumento(getTipoDocumentoFacadeLocal().findByTipo(tipoDocumento));
                    else persona.setTipoDocumento(null);
                    getPersonaFacade().edit(persona);
                }

                //Busca el usuario
                //1. Si el usuario no existe: crea el usuario con el rol profesor y lo asocia a la persona creada anteriormente
                //2. Si el usuario existe: se agrega el rol profesor al usuario existente
                Usuario usuario = getUsuarioFacade().findByLogin(correo);
                if (usuario == null) {
                    usuario = new Usuario();
                    usuario.setEsPersona(true);
                    usuario.setPersona(getPersonaFacade().findByCorreo(correo));
                    Collection<Rol> roles = new ArrayList<Rol>();
                    if(tipoProfesor.equalsIgnoreCase(getConstanteBean().getConstante(Constantes.VAL_TIPO_PROFESOR_CATEDRA))){
                        Rol rol = getRolFacade().findByRol(getConstanteBean().getConstante(Constantes.ROL_PROFESOR_CATEDRA));
                        roles.add(rol);
                    } else if(tipoProfesor.equalsIgnoreCase(getConstanteBean().getConstante(Constantes.VAL_TIPO_PROFESOR_PLANTA))){
                        Rol rol = getRolFacade().findByRol(getConstanteBean().getConstante(Constantes.ROL_PROFESOR_PLANTA));
                        roles.add(rol);
                    }
                    usuario.setRoles(roles);
                    getUsuarioFacade().create(usuario);
                } else {
                    Collection<Rol> roles = usuario.getRoles();
                    if(tipoProfesor.equalsIgnoreCase(getConstanteBean().getConstante(Constantes.VAL_TIPO_PROFESOR_CATEDRA))){
                        Rol rol = getRolFacade().findByRol(getConstanteBean().getConstante(Constantes.ROL_PROFESOR_CATEDRA));
                        roles.add(rol);
                    } else if(tipoProfesor.equalsIgnoreCase(getConstanteBean().getConstante(Constantes.VAL_TIPO_PROFESOR_PLANTA))){
                        Rol rol = getRolFacade().findByRol(getConstanteBean().getConstante(Constantes.ROL_PROFESOR_PLANTA));
                        roles.add(rol);
                    }
                    usuario.setRoles(roles);
                    getUsuarioFacade().edit(usuario);
                }

                //Busca el profesor
                //1. Si el profesor no existe: crea el profesor
                //2. Si el profesor ya existe: actualiza la información del profesor
                Profesor profesor = getProfesorFacade().findByCorreo(correo);
                if(profesor == null){
                    profesor = new Profesor();
                    profesor.setGrupoInvestigacion(getGrupoInvestigacionFacade().findByNombre(grupoInvestigacion));
                    if(nivelFormacion != null) profesor.setNivelFormacion(getNivelFormacionFacade().findByName(nivelFormacion));
                    else profesor.setNivelFormacion(null);
                    profesor.setNivelPlanta(getNivelPlantaFacade().findByNombre(nivelPlanta));
                    profesor.setOficina(oficina);
                    profesor.setPersona(getPersonaFacade().findByCorreo(correo));
                    profesor.setTipo(tipoProfesor);
                    getProfesorFacade().create(profesor);
                }else{
                    profesor.setGrupoInvestigacion(getGrupoInvestigacionFacade().findByNombre(grupoInvestigacion));
                    if(nivelFormacion != null) profesor.setNivelFormacion(getNivelFormacionFacade().findByName(nivelFormacion));
                    else profesor.setNivelFormacion(null);
                    profesor.setNivelPlanta(getNivelPlantaFacade().findByNombre(nivelPlanta));
                    profesor.setOficina(oficina);
                    profesor.setPersona(getPersonaFacade().findByCorreo(correo));
                    profesor.setTipo(tipoProfesor);
                    getProfesorFacade().edit(profesor);
                }
            }
             
            //Genera y retorna la respuesta de éxito
            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CARGA_MSJ_0001, new LinkedList<Secuencia>());
        } catch (Exception e) {
           e.printStackTrace();
             try {
                Logger.getLogger(CargaGrupoBean.class.getName()).log(Level.SEVERE, null, e);
                String respuesta = getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CARGA_ERR_0003, new LinkedList<Secuencia>());
                return respuesta;
            } catch (Exception ex) {
                Logger.getLogger(CargaGrupoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    //----------------------------------------------
    // MÉTODOS PRIVADOS
    //----------------------------------------------
    private ConstanteLocal getConstanteBean() {
        return constanteBean;
    }

    private ParserT getParser() {
        if(parser == null)
            parser = new ParserT();
        return parser;
    }

    private EstudianteMatriculadoFacadeLocal getEstudianteMatriculadoFacade() {
        return estudianteMatriculadoFacade;
    }

    private PaisFacadeLocal getPaisFacade() {
        return paisFacade;
    }

    private TipoDocumentoFacadeLocal getTipoDocumentoFacadeLocal() {
        return tipoDocumentoFacadeLocal;
    }

    private PersonaFacadeLocal getPersonaFacade() {
        return personaFacade;
    }

    private EstudianteFacadeLocal getEstudianteFacade() {
        return estudianteFacade;
    }

    private ProgramaFacadeLocal getProgramaFacade() {
        return programaFacade;
    }

    private TipoCuentaFacadeLocal getTipoCuentaFacade() {
        return tipoCuentaFacade;
    }

    private NivelFormacionFacadeLocal getNivelFormacionFacade() {
        return nivelFormacionFacade;
    }

    private InformacionAcademicaFacadeLocal getInfoAcademicaFacade() {
        return infoAcademicaFacade;
    }

    private ProfesorFacadeLocal getProfesorFacade() {
        return profesorFacade;
    }

    private GrupoInvestigacionFacadeLocal getGrupoInvestigacionFacade() {
        return grupoInvestigacionFacade;
    }

    private NivelPlantaFacadeLocal getNivelPlantaFacade() {
        return nivelPlantaFacade;
    }

    private RolFacadeLocal getRolFacade() {
        return rolFacade;
    }

    private UsuarioFacadeLocal getUsuarioFacade() {
        return usuarioFacade;
    }
}
