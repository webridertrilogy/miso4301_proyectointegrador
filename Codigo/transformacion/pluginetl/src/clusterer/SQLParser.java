package clusterer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.replace.Replace;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.TablesNamesFinder;
import visualizacionMetricas3.VisualizacionMetricas3Factory;
import visualizacionMetricas3.impl.VisualizacionMetricas3FactoryImpl;

/**
 * 
 * Representa la transformación texto a modelo de los archivos de logs hacia
 * el modelo de representacion.  
 * 
 * 1. Lee el archivo de logs SQL que se generó con la libreria log4jdbc.
 * Extrae las lineas con el siguiente patrón:
 * 
 * \d\. .*\((.*)\) returned net.sf.log4jdbc.ResultSetSpy@\w+  .*\((\w+)\.java:\d+\)
 *   La cual hace match con expresiones Statement.executeQuery y Connection.prepareStatement. 
 * 
 * Donde el grupo 1 corresponde a la sentencia sql nativa.
 * Donde el grupo 2 corresponde a la clase de negocio que generó o ejecutó la sentencia (llamado a jdbc o algun ORM).
 * 
 * 2. Al string del grupo 1 se le aplica un parser sql (libreria jsqlparser) para obtener las tablas contenidas en la sentencia.
 * 
 * 3. El resultado es que cada clase contendrá las tablas a las cuales está relacionada.
 * 
 * 4. Se grega dcha información al modelo de representacion. 
 *  
 *  
 */
public class SQLParser {
	// Fabrica de elementos del modelo de visualizacion.
	private VisualizacionMetricas3Factory factory;
	// Patron de las lineas a extraer del log, la cual contiene una sentencia sql y la clase que la manda a ejecutar 
	private String LINE_PATTERN1 = "(prepareStatement|executeQuery)\\((.*)\\) clase   \\((\\w+)\\.java\\)";
	
	
	private Pattern p1 = Pattern.compile(LINE_PATTERN1);
	
	
	public SQLParser() {
		//factory = VisualizacionMetricas3Factory.eINSTANCE;
		 factory = new VisualizacionMetricas3FactoryImpl();
	}

	public Map<String, Set<RelacionTabla>> analyzeSQL(String rutaLog) {	
		
		// por cada clase se guardan las tablas a las que está relacionada y el tipo de operacion.
		Map<String, Set<RelacionTabla>> claseTablas = new HashMap<String, Set<RelacionTabla>>();

		System.out.println("****************** Iniciando tranformación T2M del archivo de SQL log *****************************");
		if (rutaLog != null && !rutaLog.equals("")) {
			try {
				File file = new File(rutaLog);
				if (file.exists()) {
					LineIterator it = FileUtils.lineIterator(file, "UTF-8");
					
					while (it.hasNext()) {
						String line = it.nextLine();
						Matcher m1 = p1.matcher(line);
						
												
						if (m1.find()) {							
							String sql = m1.group(2);
							String clase = m1.group(3);							
							System.out.println("Clase " + clase + ", parseando SQL: " + sql);
		
							try {
								Statement statement = CCJSqlParserUtil.parse(sql);
								TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
								List<String> tableList = new ArrayList<String>();
								String tipo = "";
								if (statement instanceof Select) {
									tableList = tablesNamesFinder.getTableList((Select) statement);
									tipo = "select";
								} else if (statement instanceof CreateTable) {
									tableList = tablesNamesFinder.getTableList((CreateTable) statement);
									tipo = "create";
								} else if (statement instanceof Expression) {
									tableList = tablesNamesFinder.getTableList((Expression) statement);
									tipo = "expression";
								} else if (statement instanceof Replace) {
									tableList = tablesNamesFinder.getTableList((Replace) statement);
									tipo = "replace";
								} else if (statement instanceof Insert) {
										tableList = tablesNamesFinder.getTableList((Insert) statement);
										tipo = "insert";									
								} else if (statement instanceof Update) {
									tableList = tablesNamesFinder.getTableList((Update) statement);
									tipo = "update";
								}
								else if (statement instanceof Delete) {
									tableList = tablesNamesFinder.getTableList((Delete) statement);
									tipo = "delete";
								}
								System.out.println("->Tablas : " + tableList);
								
								if (!claseTablas.containsKey(clase)) {
									claseTablas.put(clase, new HashSet<RelacionTabla>());
								}
								for (String tabla : tableList) {
									RelacionTabla r = new RelacionTabla(tabla, tipo);
									claseTablas.get(clase).add(r);
								}
									
							    
								//TODO AGREGAR RELACIONES AL MODELO Ó RETORNAR DATOS AL ETL
								
							} catch (Exception e) {
								System.out.println("Error al parsear sql: ");
								e.printStackTrace();
							}
						}
					}
					it.close();
					
					System.out.println("----------------Resultado analisis sentencias  SQL----------");
					for (String clase : claseTablas.keySet()) {
						System.out.println("Clase " + clase +  " Tablas: " + claseTablas.get(clase));
					}
				}
				else {
					System.out.println("No se ejecuta parsing SQL debido a que el archivo no existe en la ruta especificada: " + rutaLog);
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("->Error parsing SQL: " + e.getMessage());
			}
			
		}
		else {
			System.out.println("No se ejecuta parsing SQL debido a que no se especificó archivo de log.");
		}
		
		return claseTablas;

	}
	
	
	public static void main(String...strings){
		SQLParser p = new SQLParser();
		p.analyzeSQL("C:\\Users\\caespinosam\\Documents\\uniandes\\PIN\\VersionDiego\\GITHUB\\miso4301_proyectointegrador\\miso4301_201520\\models\\votaciones-log-sql.log");
		//p.analyzeSQL("C:\\Andes\\ProyectoIntegraodrCodigoVF\\miso4301_201520\\models\\votaciones-log-sql.log");
				
	}
}
