package co.uniandes.sisinfo.serviciosnegocio;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;

import javax.ejb.EJB;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.entities.datosmaestros.Programa;
import co.uniandes.sisinfo.serviciosfuncionales.CorreoRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.ProgramaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.seguridad.AccesoLDAP;


/**
 * Servicio de lectura de cartelera
 * @author German Florez, Marcela Morales
 */
public class LectorCartelera {

    //----------------------------------------------
    // ATRIBUTOS
    //----------------------------------------------
    @EJB
    private ProgramaFacadeRemote programaFacade;
    @EJB
    private CorreoRemote correoBean;
    @EJB
    private ConstanteRemote constanteBean;
    private ConflictoHorariosBeanHelper conversor;

    //----------------------------------------------
    // CONSTRUCTOR
    //----------------------------------------------
    public LectorCartelera(ConstanteRemote constanteBean, CorreoRemote correoBean, ProgramaFacadeRemote programaFacade) {
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
        Row row = rows.next();

        while(row!=null)
        {
            String str = getString(row, 0);
            if(str.equals("N")){
                break;
            }else{
                System.out.println("Row skipped");
                row = rows.next();
            }
        }

        while (rows.hasNext()) {
            row = (Row) rows.next();

            // Si la primera columna no tiene valores númericos entonces se llego al fin del archivo
            if (row==null || getString(row, 0).equals("**** Fin datos ****") || row.getCell(0)==null || row.getCell(0).getCellType() != Cell.CELL_TYPE_NUMERIC) {
                break;
            }

            int ind = 1;

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

    public void validarCartelera(Sheet sheet) throws Exception {
        String nombres[] = {"N", "CRN", "CURSO", "SECCION", "CREDITOS", "NOMBRE_CURSO", "MONITORES", "ES_PRESENCIAL", "ES_OBLIGATORIA", "NIVEL", "HINI", "HFIN", "SALON", "L", "M", "I", "J", "V", "S", "HINI2", "HFIN2", "SALON2", "L", "M", "I", "J", "V", "S", "HINI3", "HFIN3", "SALON3", "L", "M", "I", "J", "V", "S", "PROFESOR1", "PROFESOR2", "PROFESOR3", "CURSOS_RELACIONADOS"};

        Iterator<Row> rows = sheet.rowIterator();
        Row row = rows.next();
        int skippedRows = 0;

        while(row!=null)
        {
            String str = getString(row, 0);
            if(str.equals(nombres[0])){
                break;
            }else{
                skippedRows++;
                row = rows.next();
            }
        }
        if(row==null){
            throw new Exception ("La tabla de materias debe contener una fila con los encabezados de sus columnas. La primera columna debe tener el encabezado "+nombres[0]);
        }
        //Empieza la validación de los encabezados
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

        ArrayList<String> mensajesValidacion = new ArrayList();
        int numCurso = 0;
        //Inicio de la validación del contenido
        while (rows.hasNext()) {
            row = rows.next();
            numCurso++;

            int ind = 0;
            //La primera columna no es importante. Columna N
            Cell cell = row.getCell(ind++);
            if(cell == null)
                break;
            if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                if (cell.getRichStringCellValue().getString().equals(getConstanteBean().getConstante(Constantes.TAG_FIN_DE_DATOS))) {
                    break;
                }
            }

            //El crn debe ser númerico
            cell = row.getCell(ind++);
            
            if (cell == null || cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC) {
                mensajesValidacion.add("El CRN del curso en la fila " + (numCurso+skippedRows+2) +" no corresponde a un valor númerico.");
                continue;
            }
            //El curso debe ser una cadena de caracteres y el programa debe estar cargado
            cell = row.getCell(ind++);
            if (cell==null ||cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                mensajesValidacion.add("El codigo del curso en la fila " + (numCurso+skippedRows+2) +" no es valido.");
            }else{
                String codCurso = cell.getRichStringCellValue().getString();
                String programa = codCurso.split("-")[0];
                Programa p = getProgramaFacade().findByCodigo(programa);
                if (p == null) {
                    //Enviar correo a Sisinfo
                    getCorreoBean().enviarMail(getConstanteBean().getConstante(Constantes.CONFIG_MAIL_USER) + getConstanteBean().getConstante(Constantes.TAG_PARAM_SUFIJO_CORREO), "Programa Conflicto Faltante", null, null, null, "Falta un programa en el archivo de programas de conflicto de horario: " + programa);
                    throw new Exception("El programa \"" + programa + "\" no se encuentra cargado en el sistema. Se le ha enviado una notificación al administrador informando del problema.");
                }
            }

            //SECCION debe ser numérico
            cell = row.getCell(ind++);
            if (cell == null || cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC) {
                mensajesValidacion.add("El número de sección del curso en la fila "+(numCurso+skippedRows+2)+" es invalido (Debe ser númerico).");
            }

            //CREDITOS debe ser numérico
            cell = row.getCell(ind++);
            if (cell==null || cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC) {
                mensajesValidacion.add("El número de creditos del curso en la fila "+(numCurso+skippedRows+2)+" es invalido (Debe ser númerico).");
            }

            //NOMBRE_CURSO debe ser una cadena de caracteres
            cell = row.getCell(ind++);
            if (cell == null || cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                mensajesValidacion.add("El nombre del curso en la fila "+(numCurso+skippedRows+2)+" no es valido.");
            }

            //MONITORES debe ser numérico
            cell = row.getCell(ind++);
            if (cell==null || cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC) {
                mensajesValidacion.add("El número de monitores del curso en la fila "+(numCurso+skippedRows+2)+" es invalido (Debe ser númerico).");
            }

            //ES_PRESENCIAL debe ser cadena de caracteres
            cell = row.getCell(ind++);
            if (cell==null || cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                mensajesValidacion.add("La presencialidad del curso en la fila "+(numCurso+skippedRows+2)+" no es valido.");
            }
            else if(!cell.toString().toLowerCase().equals("true") && !cell.toString().equals("false"))
            {
                mensajesValidacion.add("El valor del campo ES_PRESENCIAL para el curso en la fila "+(numCurso+skippedRows+2)+" debe ser true o false.");
            }

            //ES_OBLIGATORIA debe ser cadena de  caracteres
            cell = row.getCell(ind++);
            if (cell==null || cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                mensajesValidacion.add("El campo ES_OBLIGATORIA del curso en la fila "+(numCurso+skippedRows+2)+" no es valido.");
            }
            else if(!cell.toString().toLowerCase().equals("true") && !cell.toString().equals("false"))
            {
                mensajesValidacion.add("El valor del campo ES_OBLIGATORIA para el curso en la fila "+(numCurso+skippedRows+2)+" debe ser true o false.");
            }

            //NIVEL debe ser cadena de  caracteres
            cell = row.getCell(ind++);
            if (cell == null || cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                mensajesValidacion.add("El valor del nivel del curso en la fila "+(numCurso+skippedRows+2)+" no es valido.");
            }
            else if(!cell.toString().equals("Pregrado") && !cell.toString().equals("Maestria")&& !cell.toString().equals("Doctorado")&& !cell.toString().equals("Especializacion"))
            {
                mensajesValidacion.add("El valor del nivel para el curso en la fila "+(numCurso+skippedRows+2)+" debe ser uno de los siguientes: Pregrado, Maestria, Especializacion o Doctorado");
            }
            for (int i = 0; i < 3; i++) {
                cell = row.getCell(ind++);
                
                if (cell!=null && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {

                    //HINI debe ser numérico
                    int hini=-1;
                    if (cell == null || cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC) {
                        mensajesValidacion.add("El horario de inicio del curso en la fila "+(numCurso+skippedRows+2)+" no es valido.");
                    }else{
                        hini = horarioToNumber(""+(int)cell.getNumericCellValue());
                    }

                    //HFIN debe ser numérico
                    cell = row.getCell(ind++);
                    int hfin = -1;
                    if (cell == null || cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_NUMERIC) {
                        mensajesValidacion.add("El horario de fin del curso en la fila "+(numCurso+skippedRows+2)+" no es valido.");
                    }else{
                        hfin = horarioToNumber(""+(int)cell.getNumericCellValue());
                    }

                    // Solo comparo las horas si ambas son validas
                    if(hini>=0 && hfin >= 0 && hini> hfin){
                        mensajesValidacion.add("La hora de inicio para el curso en la fila "+(numCurso+skippedRows+2)+" es posterior a su hora de fin.");
                    }

                    //SALON debe ser una cadena de caracteres
                    cell = row.getCell(ind++);
                    if (cell==null || cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                        mensajesValidacion.add("El salon del curso en la fila "+(numCurso+skippedRows+2)+" no es valido.");
                    }
                   
                    //Horario
                    int cantDias = 0;
                    for (int j = 0; j < 6; j++) {

                        cell = row.getCell(ind++);
                        if(cell==null){
                            continue;
                        }
                        String valorS;
                        if(cell!=null)
                            valorS = cell.toString().trim();
                        else
                            valorS = "";
                        
                        if (cell.getCellType() == Cell.CELL_TYPE_STRING && (valorS.equals("L") || valorS.equals("M") || valorS.equals("I") || valorS.equals("J") || valorS.equals("V") || valorS.equals("S"))) {
                            cantDias++;
                        }
                    }
                    if(cantDias==0){
                        mensajesValidacion.add("Uno de los horarios definidos para el curso en la fila "+(numCurso+skippedRows+2)+" no tiene ningún día especificado");
                    }
                } //Esta porción de código se encarga de manejar el caso cuando no hay un horario
                else {
                    ind+=8;
                }
            }


            //PROFESOR1, PROFESOR2, PROFESOR3 Y CURSOS_RELACIONADOS deben ser una cadenas de caracteres
            cell = row.getCell(ind++);
            //comprueba si el profesor existe en el LDAP
            if (cell == null || cell.toString().equals("null") || cell.toString().equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                mensajesValidacion.add("El profesor1 del curso en la fila "+(numCurso+skippedRows+2)+" no es valido.");
            }

            //comprueba si el profesor existe en el LDAP
            else if(!cell.toString().equals("sisinfo"))
            {
                try {                
                    AccesoLDAP.obtenerNombres(cell.toString());
                } catch (Exception e) {
                    mensajesValidacion.add("El correo del profesor <" + cell.toString() + "> del curso en la fila "+(numCurso+skippedRows+2)+" no es valido.");
                }
            }

            cell = row.getCell(ind++);
            String val;
            if(cell!=null){
                val = cell.toString().trim();
                //comprueba si el profesor existe en el LDAP
                if (!(val.equals("null") || val.equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING)){
                    //comprueba si el profesor existe en el LDAP
                    if(!val.equals("sisinfo")){
                        try {
                            AccesoLDAP.obtenerNombres(cell.toString());
                        } catch (Exception e) {
                            mensajesValidacion.add("El correo del profesor <" + cell.toString() + "> del curso en la fila "+(numCurso+skippedRows+2)+" no es valido.");
                        }
                    }
                }
            }

            cell = row.getCell(ind++);
            if(cell!=null){
                val = cell.toString().trim();
                //comprueba si el profesor existe en el LDAP
                if (!(val.equals("null") || val.equals("") || cell.getCellType() != Cell.CELL_TYPE_STRING)){
                    //comprueba si el profesor existe en el LDAP
                    if(!val.equals("sisinfo")){
                        try {
                            AccesoLDAP.obtenerNombres(cell.toString());
                        } catch (Exception e) {
                            mensajesValidacion.add("El correo del profesor <" + cell.toString() + "> del curso en la fila "+(numCurso+skippedRows+2)+" no es valido.");
                        }
                    }
                }
            }

        }
        if(mensajesValidacion.size()>0){
            String errorMsg="Se encontraron los siguientes problemas subiendo el archivo al sistema:<br><ul>";
            int numMaxMensajes = 5;
            for (String string : mensajesValidacion) {
                System.out.println(string);
                errorMsg+="<li>"+string+"<br>";
                numMaxMensajes--;
                if(numMaxMensajes==0)
                    break;
            }
            throw new Exception(errorMsg+"</ul>");
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
    private ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    private CorreoRemote getCorreoBean() {
        return correoBean;
    }

    private ProgramaFacadeRemote getProgramaFacade() {
        return programaFacade;
    }

    public ConflictoHorariosBeanHelper getConversor() {
        return conversor;
    }

    /**
     * Método que transforma un string con la información de un horario a su
     * representacion entera en minutos.
     * @param str El string con la informacion de horario. La lngitud del horario
     * debe ser 3 (para horas antes de las 10) y 4 (para horas posteriores a las 10)
     * y la hora se debe representar en formato 24h
     * @return Un entero con la representacion en minutos de la hora o -1 si el string
     * recibido no es valido.
     */
    private int horarioToNumber(String str){
        try{
            String h = ""+(int)(Double.parseDouble(str));
            if(h.length()<4)
                h = "0"+str;
            if(h.length()!=4)
                throw new Exception("Tamaño invalido");
            int horas = Integer.parseInt(h.substring(0,2));
            if(horas < 0 || horas > 24)
                throw new Exception("Número de horas invalido");
            int minutos = Integer.parseInt(h.substring(2));
            if(minutos < 0 || minutos > 60)
                throw new Exception("Número de minutos invalido");
            return (horas*60)+minutos;
        }catch(Exception e){
            return -1;
        }
    }
}
