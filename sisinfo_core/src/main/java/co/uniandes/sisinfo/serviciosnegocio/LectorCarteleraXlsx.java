package co.uniandes.sisinfo.serviciosnegocio;

import java.io.FileInputStream;
import java.util.Iterator;

import javax.ejb.EJB;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.entities.datosmaestros.Programa;
import co.uniandes.sisinfo.serviciosfuncionales.CorreoLocal;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.ProgramaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.seguridad.AccesoLDAP;



/**
 * Servicio de lectura de cartelera
 * @author Carlos Morales
 */
public class LectorCarteleraXlsx {

    private final static int FILAS_HEADER = 3;

    //----------------------------------------------
    // ATRIBUTOS
    //----------------------------------------------
    @EJB
    private ProgramaFacadeLocal programaFacade;
    @EJB
    private CorreoLocal correoBean;
    @EJB
    private ConstanteLocal constanteBean;
    private ConflictoHorariosBeanHelper conversor;

    //----------------------------------------------
    // CONSTRUCTOR
    //----------------------------------------------
    public LectorCarteleraXlsx(ConstanteLocal constanteBean, CorreoLocal correoBean, ProgramaFacadeLocal programaFacade) {
        this.constanteBean = constanteBean;
        this.correoBean = correoBean;
        this.programaFacade = programaFacade;
        conversor = new ConflictoHorariosBeanHelper(getConstanteBean(), getCorreoBean());
    }

    //----------------------------------------------
    // MÉTODOS DE CARGA Y VALIDACION DE CARTELERA
    //----------------------------------------------
    public void cargarCartelera(FileInputStream file) throws Exception {

        Workbook workbook = WorkbookFactory.create(file);

        Sheet sheet = workbook.getSheetAt(0);

        //Valida que el archivo ingresado corresponda al formato esperado
        validarCartelera(sheet);

        //Inicia el proceso de lectura y extracción de información
        Iterator<Row> rows = sheet.rowIterator();
        final int skip = 4;

        for (int i = 0; i < skip - 1; i++) {
            rows.next();
        }

        while (rows.hasNext()) {
            Row row = (Row) rows.next();
            if (getString(row, 0).equals("**** Fin datos ****") || row.getCell(0).getCellType() != Cell.CELL_TYPE_NUMERIC) {
                break;
            }

            int ind = 0;
            row.getCell(ind++).getNumericCellValue();

            //Extrae la información de curso y sección
            int crnSeccion = (int) row.getCell(ind++).getNumericCellValue(); //CRN
            System.out.println("PRIMER CURSO: " + crnSeccion);
            String codigoCurso = getString(row, ind++); //CURSO
            int numeroSeccion = (int) row.getCell(ind++).getNumericCellValue(); //SECCION
            Cell celdaCreditos = row.getCell(ind++);
            double creditos = (celdaCreditos == null) ? 0
                    : (celdaCreditos.getCellType() == Cell.CELL_TYPE_NUMERIC) ? celdaCreditos.getNumericCellValue() : Double.parseDouble(celdaCreditos.getRichStringCellValue().getString()); //CREDITOS
            String nombreCurso = getString(row, ind++); //NOMBRE_CURSO
            Cell celdaMonitores = row.getCell(ind++);
            double monitores = (celdaMonitores == null) ? 0
                    : (celdaMonitores.getCellType() == Cell.CELL_TYPE_NUMERIC) ? celdaMonitores.getNumericCellValue() : Double.parseDouble(celdaMonitores.getRichStringCellValue().getString()); //MONITORES
            String esPresencial = getString(row, ind++); //ES_PRESENCIAL
            String esObligatoria = getString(row, ind++); //ES_OBLIGATORIA
            String nivel = getString(row, ind++);//NIVEL

            //Extrae la información de los horarios
            String horaInicio = getString(row, ind++); //HINI
            String horaFin = getString(row, ind++); //HFIN
            String salon = getString(row, ind++); //SALON
            String dias = "";
            for (int i = 1; i <= 6; i++) {
                dias += getString(row, ind++) + " ";
            } //L, M, I, J, V
            String horaInicio2 = getString(row, ind++); //HINI2
            String horaFin2 = getString(row, ind++); //HFIN2
            String salon2 = getString(row, ind++); //SALON2
            String dias2 = "";
            for (int i = 1; i <= 6; i++) {
                dias2 += getString(row, ind++) + " ";
            } //L, M, I, J, V
            String horaInicio3 = getString(row, ind++); //HINI3
            String horaFin3 = getString(row, ind++); //HFIN3
            String salon3 = getString(row, ind++); //SALON3
            String dias3 = "";
            for (int i = 1; i <= 6; i++) {
                dias3 += getString(row, ind++) + " ";
            }

            //Extrae la información de los profesores
            String profesor1 = getString(row, ind++); //PROFESOR1
            String profesor2 = getString(row, ind++); //PROFESOR2
            String profesor3 = getString(row, ind++); //PROFESOR3

            //Extrae la información de los cursos relacionados
            String relacionados = getString(row, ind++); //CURSOS_RELACIONADOS
            if (relacionados.trim().equals("")) {
                relacionados = "N/A";
            }

            //Llama al conversor para crear la sección de acuerdo a la información extraída
            getConversor().crearSeccion(crnSeccion, codigoCurso, numeroSeccion, creditos, nombreCurso, monitores, esPresencial, esObligatoria,
                    nivel, horaInicio, horaFin, salon, dias, horaInicio2, horaFin2, salon2, dias2, horaInicio3, horaFin3, salon3, dias3,
                    profesor1, profesor2, profesor3,
                    relacionados);
        }
    }


    public void validarCartelera(){

    }

    public void validarCartelera(Sheet sheet) throws Exception {
        Iterator<Row> rows = sheet.rowIterator();
        for (int i = 0; i < FILAS_HEADER; i++) {
            rows.next();
        }

        //Empieza la validación en la 4 fila. Validación de las columnas.
        String nombres[] = {"N", "CRN", "CURSO", "SECCION", "CREDITOS", "NOMBRE_CURSO", "MONITORES", "ES_PRESENCIAL", "ES_OBLIGATORIA", "NIVEL", "HINI", "HFIN", "SALON", "L", "M", "I", "J", "V", "S", "HINI2", "HFIN2", "SALON2", "L", "M", "I", "J", "V", "S", "HINI3", "HFIN3", "SALON3", "L", "M", "I", "J", "V", "S", "PROFESOR1", "PROFESOR2", "PROFESOR3", "CURSOS_RELACIONADOS"};
        Row row = rows.next();
        for (int i = 0; i < nombres.length; i++) {
            String nombreColumna = nombres[i];
            String nombreCol2 = getString(row, i);
            if (!nombreColumna.equalsIgnoreCase(nombreCol2)) {
                System.out.println("Comparo:"+nombreColumna+" y "+nombreCol2);
                String mensaje = "La columna " + nombreColumna + " no se encuentra especificada en el archivo. (" + nombreCol2 + ")";
                if (i == 0) {
                    mensaje += "Recuerde que esta columna debe estar antes de " + nombres[i + 1];
                } else if (i == nombres.length - 1) {
                    mensaje += "Recuerde que esta columna debe estar luego de " + nombres[nombres.length - 2];
                } else {
                    mensaje += "Recuerde que esta columna debe estar antes de " + nombres[i + 1] + " y luego de " + nombres[i - 1];
                }
                throw new Exception(mensaje);
            }
        }

        //Inicio de la validación del contenido
        while (rows.hasNext()) {
            row = rows.next();
            Iterator<Cell> cells = row.cellIterator();

            //La primera columna no es importante. Columna N
            Cell cell = cells.next();
            if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                if (cell.getRichStringCellValue().getString().equals(getConstanteBean().getConstante(Constantes.TAG_FIN_DE_DATOS))) {
                    break;
                }
            }

            //El crn debe ser númerico
            cell = cells.next();
            int tipo = cell.getCellType();
            if (cell.toString().equals("null") || cell.toString().equals("") || tipo != Cell.CELL_TYPE_NUMERIC) {
                throw new Exception("Todos los CRNs deben ser númericos. Por favor revise el archivo.");
            }

            //El curso debe ser una cadena de caracteres y el programa debe estar cargado
            cell = cells.next();
            tipo = cell.getCellType();
            if (cell.toString().equals("null") || cell.toString().equals("") || tipo != Cell.CELL_TYPE_STRING) {
                throw new Exception("Todos los CURSOs deben ser cadenas de caracteres. Por favor revise el archivo.");
            }
            String codCurso = cell.getRichStringCellValue().getString();
            System.out.println("codigo:"+codCurso);
            String programa = codCurso.split("-")[0];
            Programa p = getProgramaFacade().findByCodigo(programa);
            if (p == null) {
                //Enviar correo a Sisinfo
                getCorreoBean().enviarMail(getConstanteBean().getConstante(Constantes.CONFIG_MAIL_USER) + getConstanteBean().getConstante(Constantes.TAG_PARAM_SUFIJO_CORREO), "Programa Conflicto Faltante", null, null, null, "Falta un programa en el archivo de programas de conflicto de horario: " + programa);
                throw new Exception("El programa \"" + programa + "\" no se encuentra cargado en el sistema. Se le ha enviado una notificación al administrador informando del problema.");
            }

            //SECCION debe ser numérico
            cell = cells.next();
            tipo = cell.getCellType();
            if (cell.toString().equals("null") || cell.toString().equals("") || tipo != Cell.CELL_TYPE_NUMERIC) {
                throw new Exception("Todos los SECCs deben ser númericos. Por favor revise el archivo.");
            }

            //CREDITOS debe ser numérico
            cell = cells.next();
            tipo = cell.getCellType();
            if (cell.toString().equals("null") || cell.toString().equals("") || tipo != Cell.CELL_TYPE_NUMERIC) {
                throw new Exception("Todos los CREDITOSs deben ser númericos. Por favor revise el archivo.");
            }

            //NOMBRE_CURSO debe ser una cadena de caracteres
            cell = cells.next();
            tipo = cell.getCellType();
            if (cell.toString().equals("null") || cell.toString().equals("") || tipo != Cell.CELL_TYPE_STRING) {
                throw new Exception("Todas los NOMBRE_CURSO deben ser cadenas de caracteres. Por favor revise el archivo.");
            }

            //MONITORES debe ser numérico
            cell = cells.next();
            tipo = cell.getCellType();
            if (cell.toString().equals("null") || cell.toString().equals("") || tipo != Cell.CELL_TYPE_NUMERIC) {
                throw new Exception("Todos los MONITORESs deben ser númericos. Por favor revise el archivo.");
            }

            //ES_PRESENCIAL debe ser cadena de caracteres
            cell = cells.next();
            tipo = cell.getCellType();
            if (cell.toString().equals("null") || cell.toString().equals("") || tipo != Cell.CELL_TYPE_STRING) {
                throw new Exception("Todos los ES_PRESENCIALs deben ser cadenas de caracteres. Por favor revise el archivo.");
            }

            //ES_OBLIGATORIA debe ser cadena de  caracteres
            cell = cells.next();
            tipo = cell.getCellType();
            if (cell.toString().equals("null") || cell.toString().equals("") || tipo != Cell.CELL_TYPE_STRING) {
                throw new Exception("Todos los ES_OBLIGATORIAs deben ser cadenas de caracteres. Por favor revise el archivo.");
            }

            //NIVEL debe ser cadena de  caracteres
            cell = cells.next();
            tipo = cell.getCellType();
            if (cell.toString().equals("null") || cell.toString().equals("") || tipo != Cell.CELL_TYPE_STRING) {
                throw new Exception("Todos los NIVELs deben ser cadenas de caracteres. Por favor revise el archivo.");
            }

            cell = cells.next();
            boolean continuar = true;
            for (int i = 0; i < 3 && continuar; i++) {
                tipo = cell.getCellType();
                if (tipo == Cell.CELL_TYPE_NUMERIC) {
                    //HINI debe ser numérico
                    if (cell.toString().equals("null") || cell.toString().equals("") || tipo != Cell.CELL_TYPE_NUMERIC) {
                        throw new Exception("Todos los HINIs deben ser númericos. Por favor revise el archivo.");
                    }
                    //HFIN debe ser numérico
                    cell = cells.next();
                    tipo = cell.getCellType();
                    if (cell.toString().equals("null") || cell.toString().equals("") || tipo != Cell.CELL_TYPE_NUMERIC) {
                        throw new Exception("Todos los HFINs deben ser númericos. Por favor revise el archivo.");
                    }
                    //SALON debe ser una cadena de caracteres
                    cell = cells.next();
                    tipo = cell.getCellType();
                    if (cell.toString().equals("null") || cell.toString().equals("") || tipo != Cell.CELL_TYPE_STRING) {
                        throw new Exception("Todos los SALONes deben ser cadenas de caracteres. Por favor revise el archivo.");
                    }
                    //Horario
                    int cantDias = 0;
                    boolean parar = false;
                    for (int j = 0; j < 6 && !parar; j++) {
                        cell = cells.next();
                        String valorS = cell.toString().trim();
                        if (cell.getCellType() == Cell.CELL_TYPE_STRING && (valorS.equals("L") || valorS.equals("M") || valorS.equals("I") || valorS.equals("J") || valorS.equals("V") || valorS.equals("S"))) {
                            cantDias++;
                        } else {
                            parar = true;
                        }
                    }
                    if (cantDias == 6) {
                        cell = cells.next();
                    }
                } //Esta porción de código se encarga de manejar el caso cuando no hay un SEGUNDO HORARIO
                else {
                    continuar = false;
                    cell = cells.next();
                    //Esta porción de código se encarga de manejar el caso cuando no hay un TERCER HORARIO
                    if (i == 1) {
                        cell = cells.next();
                    }
                }
            }


            //PROFESOR1, PROFESOR2, PROFESOR3 Y CURSOS_RELACIONADOS deben ser una cadenas de caracteres
            tipo = cell.getCellType();

            //comprueba si el profesor existe en el LDAP

            if (cell.toString().equals("null") || cell.toString().equals("") || tipo != Cell.CELL_TYPE_STRING) {
                throw new Exception("Todos los PROFESOR1 deben ser cadenas de caracteres. Por favor revise el archivo.");
            }

            //comprueba si el profesor existe en el LDAP
            if(!cell.toString().equals("sisinfo")){
                try {
                
                    AccesoLDAP.obtenerNombres(cell.toString());
                } catch (Exception e) {
                    throw new Exception("El correo del profesor " + cell.toString() + " no es valido.");
                }
            }

            if (cells.hasNext()) {
                cell = cells.next();
                tipo = cell.getCellType();
                if (cell.toString().equals("null") || cell.toString().equals("") || tipo != Cell.CELL_TYPE_STRING) {
                    throw new Exception("Todos los PROFESOR2, PROFESOR3 Y Relacionado deben ser cadenas de caracteres. Por favor revise el archivo.");
                }
            }




        }
    }

    //----------------------------------------------
    // MÉTODOS AUXILIARES
    //----------------------------------------------
    private String getString(Row row, int col) {
        Cell celda = row.getCell(col);
        if (celda == null) {
            return "";
        }
        if (celda.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            return (int) celda.getNumericCellValue() + "";
        }
        if (celda.getCellType() == Cell.CELL_TYPE_STRING) {
            return celda.getRichStringCellValue().getString().trim();
        }
        return "";
    }

    //----------------------------------------------
    // MÉTODOS PRIVADOS
    //----------------------------------------------
    private ConstanteLocal getConstanteBean() {
        return constanteBean;
    }

    private CorreoLocal getCorreoBean() {
        return correoBean;
    }

    private ProgramaFacadeLocal getProgramaFacade() {
        return programaFacade;
    }

    public ConflictoHorariosBeanHelper getConversor() {
        return conversor;
    }
}
