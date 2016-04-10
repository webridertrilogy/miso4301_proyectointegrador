/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, Carlos Morales, Javier Bautista,
 *          Alejandro Osorio, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.serviciosnegocio;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
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
import co.uniandes.sisinfo.entities.Periodo;
import co.uniandes.sisinfo.entities.datosmaestros.Curso;
import co.uniandes.sisinfo.entities.datosmaestros.Profesor;
import co.uniandes.sisinfo.entities.datosmaestros.Seccion;
import co.uniandes.sisinfo.entities.datosmaestros.Sesion;
import co.uniandes.sisinfo.serviciosfuncionales.PeriodoFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.CursoFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.ProfesorFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.SeccionFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;

/**
 * Servicio de negocio: Administración de carga
 */
@Stateless
@EJB(name = "CarteleraBean", beanInterface = co.uniandes.sisinfo.serviciosnegocio.CarteleraLocal.class)
public class CarteleraBean implements CarteleraRemote, CarteleraLocal {

    //---------------------------------------
    // Atributos
    //---------------------------------------
    /**
     * Parser
     */
    private ParserT parser;
    /**
     * CursoFacade
     */
    @EJB
    private CursoFacadeRemote cursoFacade;
    /**
     * SeccionFacade
     */
    @EJB
    private SeccionFacadeRemote seccionFacade;
    /**
     * ProfesorFacade
     */
    @EJB
    private ProfesorFacadeRemote profesorFacade;
  
    /**
     * ListaNegraBean
     */
    @EJB
    private ListaNegraLocal listaNegraBean;
    
    /**
     * PeriodoFacade
     */
    @EJB
    private PeriodoFacadeRemote periodoFacade;
    /**
     *  ConstanteBean
     */
    @EJB
    private ConstanteRemote constanteBean;

    private ServiceLocator serviceLocator;


    //---------------------------------------
    // Constructor
    //---------------------------------------
    /**
     * Constructor de CarteleraBean
     */
    public CarteleraBean() {
        try {
            serviceLocator = new ServiceLocator();            
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);            
            profesorFacade = (ProfesorFacadeRemote) serviceLocator.getRemoteEJB(ProfesorFacadeRemote.class);
            seccionFacade = (SeccionFacadeRemote) serviceLocator.getRemoteEJB(SeccionFacadeRemote.class);
            cursoFacade = (CursoFacadeRemote) serviceLocator.getRemoteEJB(CursoFacadeRemote.class);
            periodoFacade = (PeriodoFacadeRemote) serviceLocator.getRemoteEJB(PeriodoFacadeRemote.class);
        } catch (NamingException ex) {
            Logger.getLogger(CarteleraBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //---------------------------------------
    // Métodos
    //---------------------------------------
    @Override
    public String cargarCartelera(String xml) {
        try {
            Periodo periodoActual = periodoFacade.findActual();
            if(periodoActual != null)
            {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0020, new LinkedList<Secuencia>());
            }

            getListaNegraBean().eliminarTemporalesDeListaNegra();
            getParser().leerXML(xml);

            String periodoS = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO)).getValor();
            Periodo p = periodoFacade.findByPeriodo(periodoS);
            //Si ya existe un periodo en la bd, no se puede volver a cargar la cartelera
            if(p != null)
            {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0012, new LinkedList<Secuencia>());
            }

            String root = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA)).getValor();
            if(!root.endsWith("xls"))
            {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0022, new LinkedList<Secuencia>());
            }
            FileInputStream file = new FileInputStream(root);
            POIFSFileSystem fileSystem = new POIFSFileSystem(file);
            HSSFWorkbook workbook = new HSSFWorkbook(fileSystem);
            HSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.rowIterator();
            //Verifica el encabezado
            if (rows.hasNext()) {
                String nombre = "";
                Row row = rows.next();
                Iterator<Cell> cells = row.cellIterator();
                
                Cell cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("N"))
                {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0027, new LinkedList<Secuencia>());
                }
                
                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("CRN"))
                {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0027, new LinkedList<Secuencia>());
                }

                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("MATERIA"))
                {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0027, new LinkedList<Secuencia>());
                }

                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("SECC"))
                {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0027, new LinkedList<Secuencia>());
                }

                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("CRED"))
                {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0027, new LinkedList<Secuencia>());
                }

                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("NOMBRE_CURSO"))
                {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0027, new LinkedList<Secuencia>());
                }

                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("CANT_MONITORES"))
                {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0027, new LinkedList<Secuencia>());
                }

                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("ES_PRESENCIAL"))
                {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0027, new LinkedList<Secuencia>());
                }

                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("ES_OBLIGATORIA"))
                {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0027, new LinkedList<Secuencia>());
                }

                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("PROFESOR1"))
                {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0027, new LinkedList<Secuencia>());
                }

                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("HINI"))
                {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0027, new LinkedList<Secuencia>());
                }

                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("HFIN"))
                {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0027, new LinkedList<Secuencia>());
                }

                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("SALON"))
                {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0027, new LinkedList<Secuencia>());
                }

                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("L"))
                {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0027, new LinkedList<Secuencia>());
                }

                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("M"))
                {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0027, new LinkedList<Secuencia>());
                }

                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("I"))
                {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0027, new LinkedList<Secuencia>());
                }

                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("J"))
                {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0027, new LinkedList<Secuencia>());
                }

                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("V"))
                {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0027, new LinkedList<Secuencia>());
                }

                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("S"))
                {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0027, new LinkedList<Secuencia>());
                }

                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("HINI2"))
                {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0027, new LinkedList<Secuencia>());
                }

                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("HFIN2"))
                {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0027, new LinkedList<Secuencia>());
                }

                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("SALON2"))
                {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0027, new LinkedList<Secuencia>());
                }

                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("L"))
                {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0027, new LinkedList<Secuencia>());
                }

                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("M"))
                {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0027, new LinkedList<Secuencia>());
                }

                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("I"))
                {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0027, new LinkedList<Secuencia>());
                }

                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("J"))
                {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0027, new LinkedList<Secuencia>());
                }

                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("V"))
                {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0027, new LinkedList<Secuencia>());
                }

                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("S"))
                {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0027, new LinkedList<Secuencia>());
                }

                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("HINI3"))
                {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0027, new LinkedList<Secuencia>());
                }

                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("HFIN3"))
                {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0027, new LinkedList<Secuencia>());
                }

                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("SALON3"))
                {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0027, new LinkedList<Secuencia>());
                }

                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("L"))
                {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0027, new LinkedList<Secuencia>());
                }

                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("M"))
                {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0027, new LinkedList<Secuencia>());
                }

                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("I"))
                {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0027, new LinkedList<Secuencia>());
                }

                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("J"))
                {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0027, new LinkedList<Secuencia>());
                }

                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("V"))
                {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0027, new LinkedList<Secuencia>());
                }

                cell = cells.next();
                nombre = cell.getRichStringCellValue().getString();
                nombre = nombre.trim();
                if(!nombre.equals("S"))
                {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0027, new LinkedList<Secuencia>());
                }
            }
            //Revisa que no hayan datos faltantes
            while (rows.hasNext()) {
                Row row = rows.next();
                Iterator<Cell> cells = row.cellIterator();
                
                    //La primera columna no es importante. Columna N
                    Cell cell = cells.next();
                    if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                        if (cell.getRichStringCellValue().getString().equals(getConstanteBean().getConstante(Constantes.TAG_FIN_DE_DATOS))) {
                            break;
                        }
                    }

                    //La segunda columna corresponde al CRN debe ser un número
                    cell = cells.next();
                    int tipo = cell.getCellType();
                    if(cell.toString().equals("null") || cell.toString().equals("") || tipo != Cell.CELL_TYPE_NUMERIC) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0003, new LinkedList<Secuencia>());
                    }

                    //La tercera columna corresponde al código del curso
                    cell = cells.next();
                    tipo = cell.getCellType();
                    if(cell.toString().equals("null") || cell.toString().equals("") || tipo != Cell.CELL_TYPE_STRING) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0010, new LinkedList<Secuencia>());
                    }

                    //La cuarta columna corresponde al número de la sección
                    cell = cells.next();
                    tipo = cell.getCellType();
                    if(cell.toString().equals("null") || cell.toString().equals("") || tipo != Cell.CELL_TYPE_NUMERIC) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0004, new LinkedList<Secuencia>());
                    }

                    //La quinta columna corresponde a los créditos del curso
                    cell = cells.next();
                    tipo = cell.getCellType();
                    if(cell.toString().equals("null") || cell.toString().equals("") || tipo != Cell.CELL_TYPE_NUMERIC) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0005, new LinkedList<Secuencia>());
                    }

                    //La sexta columna corresponde al nombre del curso
                    cell = cells.next();
                    tipo = cell.getCellType();
                    if(cell.toString().equals("null") || cell.toString().equals("") || tipo != Cell.CELL_TYPE_STRING) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0006, new LinkedList<Secuencia>());
                    }

                    //La séptima columna corresponde a la cantidad de monitores de la sección
                    cell = cells.next();
                    tipo = cell.getCellType();
                    if(cell.toString().equals("null") || cell.toString().equals("") || tipo != Cell.CELL_TYPE_NUMERIC) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0023, new LinkedList<Secuencia>());
                    }

                    //La octaba columna determina si el curso es presencial. Todos las secciones de un mismo curso deben tener el mismo valor.
                    cell = cells.next();
                    tipo = cell.getCellType();
                    if(cell.toString().equals("null") || cell.toString().equals("") || tipo != Cell.CELL_TYPE_STRING || (!cell.getRichStringCellValue().toString().equalsIgnoreCase(Boolean.TRUE.toString()) && !cell.getRichStringCellValue().toString().equals(Boolean.FALSE.toString()) ) ) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0024, new LinkedList<Secuencia>());
                    }

                    //La novena columna determina si el curso debió de ser visto. Todos las secciones de un mismo curso deben tener el mismo valor.
                    cell = cells.next();
                    tipo = cell.getCellType();
                    if(cell.toString().equals("null") || cell.toString().equals("") || tipo != Cell.CELL_TYPE_STRING || (!cell.getRichStringCellValue().toString().equalsIgnoreCase(Boolean.TRUE.toString()) && !cell.getRichStringCellValue().toString().equals(Boolean.FALSE.toString()) ) ) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0025, new LinkedList<Secuencia>());
                    }
                 
                    //La décima columna corresponde al correo del profesor
                    cell = cells.next();
                    tipo = cell.getCellType();
                    if(cell.toString().equals("null") || cell.toString().equals("") || tipo != Cell.CELL_TYPE_STRING) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0007, new LinkedList<Secuencia>());
                    }
                    String correo = "";
                    String login = cell.getRichStringCellValue().toString().trim();
                    if(login.contains("@"))
                    {
                        if(!login.endsWith(getConstanteBean().getConstante(Constantes.TAG_PARAM_SUFIJO_CORREO)))
                        {
                            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0008 + " ("+ login +")", new LinkedList<Secuencia>());
                        }
                        else
                        {
                            correo = login;
                        }
                    }
                    else
                    {
                        correo = login + getConstanteBean().getConstante(Constantes.TAG_PARAM_SUFIJO_CORREO);
                    }
                    Profesor profesor = profesorFacade.findByCorreo(correo);
                    if(profesor == null)
                    {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0008 + " ("+ login +")", new LinkedList<Secuencia>());
                    }
                                                           

                    //Se verifican los horarios y sesiones
                    boolean continuar = true;
                    cell = cells.next();
                    for(int i = 0; i < 3 && continuar; i++) {

                        //La undécima columna corresponde a la hora inicio
                        tipo = cell.getCellType();
                        if(!cell.toString().equals("") && (cell.toString().equals("null") || tipo != Cell.CELL_TYPE_NUMERIC)) {
                            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0009, new LinkedList<Secuencia>());
                        }

                        //La dúodecima columna corresponde a la hora fin
                        cell = cells.next();
                        tipo = cell.getCellType();
                        if(!cell.toString().equals("") && (cell.toString().equals("null") || tipo != Cell.CELL_TYPE_NUMERIC)) {
                            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0009, new LinkedList<Secuencia>());
                        }

                        //Las demás columnas no tienen restricción de formato

                        //Columna SALON. No hay restricción
                        cell = cells.next();

                        //Primer horario
                        int cantDias = 0;
                        boolean parar = false;
                        for(int j = 0; j < 6 && !parar; j++)
                        {
                            cell = cells.next();
                            String valorS = cell.toString().trim();

                            if(cell.getCellType() == Cell.CELL_TYPE_STRING && (valorS.equals("L") || valorS.equals("M") || valorS.equals("I") || valorS.equals("J") || valorS.equals("V") || valorS.equals("S") ))
                            {
                                cantDias++;
                            }
                            else
                            {
                                if(cantDias == 0)
                                {
                                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0011, new LinkedList<Secuencia>());
                                }
                                else
                                {
                                    parar = true;
                                }
                            }
                        }

                        //Horarios posteriores
                        if(cell.getCellType() == Cell.CELL_TYPE_STRING)
                        {
                            continuar = false;
                        }
                    }
            }


            //Carga la cartelera
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
                Curso cursoTemporal = new Curso();
                Seccion seccionTemporal = new Seccion();
                //CRN
                cell = cells.next();
                int crn = (int) cell.getNumericCellValue();
                seccionTemporal.setCrn(Integer.toString(crn));
                //Código del curso
                cell = cells.next();
                String codigo = cell.getRichStringCellValue().getString().trim();
                cursoTemporal.setCodigo(codigo);
                //Nivel del curso
                int nivel = Integer.parseInt(codigo.charAt(5) + "");
                cursoTemporal.setNivel(nivel);
                //Número de la sección
                cell = cells.next();
                int numSeccion = (int) cell.getNumericCellValue();
                seccionTemporal.setNumeroSeccion(numSeccion);
                //Créditos del curso
                cell = cells.next();
                double creditos = cell.getNumericCellValue();
                cursoTemporal.setCreditos(creditos);
                //Nombre del curso
                cell = cells.next();
                String nombre = cell.getRichStringCellValue().getString().trim();
                cursoTemporal.setNombre(nombre);
                //Cantidad de monitores
                cell = cells.next();
                double cantMonitores = cell.getNumericCellValue();
                seccionTemporal.setMaximoMonitores(cantMonitores);
                //Es presencial
                cell = cells.next();
                boolean presencial = Boolean.parseBoolean(cell.getRichStringCellValue().getString().trim());
                cursoTemporal.setPresencial(presencial);
                //Es obligatoria
                cell = cells.next();
                boolean obligatoria = Boolean.parseBoolean(cell.getRichStringCellValue().getString().trim());
                cursoTemporal.setRequerido(obligatoria);
                //Crea el curso
                cursoTemporal.setSecciones(new LinkedList<Seccion>());
                Curso curso = getCursoFacade().findByCodigoSinProfesor(codigo);
                if (curso == null) {
                    getCursoFacade().create(cursoTemporal);
                }
                //Agrega la sección
                Seccion seccion = getSeccionFacade().findByCRN(Integer.toString(crn));
                if (seccion == null) {
                    seccionTemporal.setHorarios(new LinkedList<Sesion>());
                    seccionTemporal.setProfesores(new ArrayList<Profesor>());
                    getSeccionFacade().create(seccionTemporal);
                }
                //Agregar Sección a Curso
                curso = getCursoFacade().findByCodigoSinProfesor(codigo);
                Collection<Seccion> secciones = curso.getSecciones();
                Seccion seccionAgregar = getSeccionFacade().findByCRN(Integer.toString(crn));
                secciones.add(seccionAgregar);
                curso.setSecciones(secciones);
                getCursoFacade().edit(curso);
                extraerInformacionProfesor(cells, cells.next(), crn);
                extraerInformacionHorario(cells, cells.next(), crn);
            }            
            //crea el nuevo periodo
            
            Periodo periodo = new Periodo();
            periodo.setActual(true);
            periodo.setPeriodo(periodoS);
            periodoFacade.create(periodo);
            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0004, new LinkedList<Secuencia>());
        } catch (Exception e) {
            try {
                Logger.getLogger(CarteleraBean.class.getName()).log(Level.SEVERE, null, e);
                String respuesta = getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0003, new LinkedList<Secuencia>());
                return respuesta;
            } catch (Exception ex) {
                Logger.getLogger(CarteleraBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    /**
     * Carga la información de los profesores
     * @param cells Iterador de celdas
     * @param cell Celda actual
     * @param crn CRN de la sección
     */
    private void extraerInformacionProfesor(Iterator<Cell> cells, Cell cell,
            int crn) {
        String nombreProfesor = cell.getRichStringCellValue().getString().trim();
        nombreProfesor = nombreProfesor.trim();
        String correo = nombreProfesor.trim().toLowerCase().replaceAll(" ", "");
        if(!nombreProfesor.endsWith(getConstanteBean().getConstante(Constantes.TAG_PARAM_SUFIJO_CORREO)))
        {
            correo += getConstanteBean().getConstante(Constantes.TAG_PARAM_SUFIJO_CORREO);
        }
        //Agrega el profesor principal en caso de que no exista
        Profesor profesorPrincipal = getProfesorFacade().findByCorreo(correo);
        Seccion seccion = getSeccionFacade().findByCRN(Integer.toString(crn));
        if (seccion.getProfesorPrincipal() == null) {
            seccion.setProfesorPrincipal(profesorPrincipal);
        }
        //Agrega el profesor a la colección de profesores de la sección
        Collection<Profesor> profesoresSeccion = seccion.getProfesores();
        profesorPrincipal = getProfesorFacade().findByCorreo(correo);
        profesoresSeccion.add(profesorPrincipal);
        seccion.setProfesores(profesoresSeccion);
        getSeccionFacade().edit(seccion);
    }

    /**
     * Carga la información de los horarios
     * @param cells Iterador de celdas
     * @param cell Celda actual
     * @param seccion Sección
     */
    private void extraerInformacionHorario(Iterator<Cell> cells, Cell cell, int crn) {
        /*
        Seccion seccion = getSeccionFacade().findByCRN(Integer.toString(crn));
        Collection<Sesion> sesiones = seccion.getHorarios();
        
        Sesion s = new Sesion();
        Collection<DiaCompleto> dias = new LinkedList<DiaCompleto>();
        s.setDias(dias);
        Dia d = new Dia();
        Collection<Franja> franjas = new LinkedList<Franja>();
        Franja f = new Franja();
        //HINI
        int horaInicio = (int) cell.getNumericCellValue();
        int minutoInicio = horaInicio % 100;
        if (minutoInicio != 0) {
            horaInicio = horaInicio - minutoInicio;
        }
        f.setHora_inicio(horaInicio / 100);
        f.setMinuto_inicio(minutoInicio);
        //HFIN
        cell = cells.next();
        int horaFin = (int) cell.getNumericCellValue();
        int minutoFin = horaFin % 100;
        if (minutoFin != 0) {
            horaFin = horaFin - minutoFin;
        }
        f.setHora_fin(horaFin / 100);
        f.setMinuto_fin(minutoFin);

        franjas.add(f);

        //SALON
        cell = cells.next();
        String salon = cell.getRichStringCellValue().getString().trim();
        s.setSalon(salon);
        //DIAS
        cell = cells.next();
        String valorS = cell.getRichStringCellValue().getString().trim();
        while (valorS.equals("L") || valorS.equals("M") || valorS.equals("I") || valorS.equals("J") || valorS.equals("V")) {
            d = new Dia();
            d.setDia_semana(valorS);
            d.setFranjas_libre(franjas);
            dias.add(d);

            cell = cells.next();
            if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                valorS = cell.getRichStringCellValue().getString().trim();
            } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                break;

            }
        }
        s.setDias(dias);
        sesiones.add(s);

        //Sesion2
        if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
        {
            s = new Sesion();
            dias = new LinkedList<Dia>();
            s.setDias(dias);
            d = new Dia();
            franjas = new LinkedList<Franja>();
             f = new Franja();
            //HINI
            horaInicio = (int) cell.getNumericCellValue();
            minutoInicio = horaInicio % 100;
            if (minutoInicio != 0) {
                horaInicio = horaInicio - minutoInicio;
            }
            f.setHora_inicio(horaInicio / 100);
            f.setMinuto_inicio(minutoInicio);
            //HFIN
            cell = cells.next();
            horaFin = (int) cell.getNumericCellValue();
            minutoFin = horaFin % 100;
            if (minutoFin != 0) {
                horaFin = horaFin - minutoFin;
            }
            f.setHora_fin(horaFin / 100);
            f.setMinuto_fin(minutoFin);

            franjas.add(f);

            //SALON
            cell = cells.next();
            salon = cell.getRichStringCellValue().getString().trim();
            s.setSalon(salon);
            //DIAS
            cell = cells.next();
            valorS = cell.getRichStringCellValue().getString().trim();
            while (valorS.equals("L") || valorS.equals("M") || valorS.equals("I") || valorS.equals("J") || valorS.equals("V")) {
                d = new Dia();
                d.setDia_semana(valorS);
                d.setFranjas_libre(franjas);
                dias.add(d);

                cell = cells.next();
                if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    valorS = cell.getRichStringCellValue().getString().trim();
                } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    break;

                }
            }
            s.setDias(dias);
            sesiones.add(s);
        }

        //Sesion3
        if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
        {
            s = new Sesion();
            dias = new LinkedList<Dia>();
            s.setDias(dias);
            d = new Dia();
            franjas = new LinkedList<Franja>();
             f = new Franja();
            //HINI
            horaInicio = (int) cell.getNumericCellValue();
            minutoInicio = horaInicio % 100;
            if (minutoInicio != 0) {
                horaInicio = horaInicio - minutoInicio;
            }
            f.setHora_inicio(horaInicio / 100);
            f.setMinuto_inicio(minutoInicio);
            //HFIN
            cell = cells.next();
            horaFin = (int) cell.getNumericCellValue();
            minutoFin = horaFin % 100;
            if (minutoFin != 0) {
                horaFin = horaFin - minutoFin;
            }
            f.setHora_fin(horaFin / 100);
            f.setMinuto_fin(minutoFin);

            franjas.add(f);

            //SALON
            cell = cells.next();
            salon = cell.getRichStringCellValue().getString().trim();
            s.setSalon(salon);
            //DIAS
            cell = cells.next();
            valorS = cell.getRichStringCellValue().getString().trim();
            while (valorS.equals("L") || valorS.equals("M") || valorS.equals("I") || valorS.equals("J") || valorS.equals("V")) {
                d = new Dia();
                d.setDia_semana(valorS);
                d.setFranjas_libre(franjas);
                dias.add(d);

                cell = cells.next();
                if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    valorS = cell.getRichStringCellValue().getString().trim();
                } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    break;

                }
            }
            s.setDias(dias);
            sesiones.add(s);
            sesiones.add(s);
        }
        
        seccion.setHorarios(sesiones);
        getSeccionFacade().edit(seccion);
         *
         */
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
    private CursoFacadeRemote getCursoFacade() {
        return cursoFacade;
    }

    /**
     * Retorna ListaNegraBean
     * @return listaNegraBean ListaNegraBean
     */
    private ListaNegraLocal getListaNegraBean() {
        return listaNegraBean;
    }

    /**
     * Retorna ProfesorFacade
     * @return profesorFacade ProfesorFacade
     */
    private ProfesorFacadeRemote getProfesorFacade() {
        return profesorFacade;
    }

    /**
     * Retorna SeccionFacade
     * @return seccionFacade SeccionFacade
     */
    private SeccionFacadeRemote getSeccionFacade() {
        return seccionFacade;
    }

    /**
     * Retorna ConstanteBean
     * @return constanteBean ConstanteBean
     */
    private ConstanteRemote getConstanteBean() {
        return constanteBean;
    }



}