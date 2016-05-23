/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.comun.constantes;

public class Mensajes {

    //******************************************************
    //Comienzo
    //Comun
    //*****************************************************
    //.................................MENSAJES.................................
    //Éxito en operación
    public final static String COM_MSJ_0001 = "COM_MSJ_0001";
    //..........................Errores........................................
    //Error en el formato de los campos. Verifique los datos e intente de nuevo
    public final static String COM_ERR_0001 = "COM_ERR_0001";
    //Error al cargar los datos. Por favor contacte al administrador
    public final static String COM_ERR_0002 = "COM_ERR_0002";
    //Error en la construcción del comando. Por favor contacte al administrador
    public final static String COM_ERR_0003 = "COM_ERR_0003";
    //******************************************************
    //Fin
    //Comun
    //*****************************************************
    //******************************************************
    //Comienzo
    //Administracion Sisinfo
    //*****************************************************
    //Error al recargar los timers
    public final static String ADM_ERR_0001 = "ADM_ERR_0001";
    //Error al consultar los timers
    public final static String ADM_ERR_0002 = "ADM_ERR_0002";
    //El timer no fue ejecutado con éxito, no se encontró el timer con el id dado.
    public final static String ADM_ERR_0003 = "ADM_ERR_0003";
    //El timer no fue eliminado con éxito, no se encontró el timer con el id dado.
    public final static String ADM_ERR_0004 = "ADM_ERR_0004";
    //El timer no fue detenido con éxito, no se encontró el timer con el id dado.
    public final static String ADM_ERR_0005 = "ADM_ERR_0005";
    //El timer no fue editado con éxito, no se encontró el timer en memoria con el id dado.
    public final static String ADM_ERR_0006 = "ADM_ERR_0006";
    //El timer no fue editado con éxito, no se encontró el timer en base de datos con el id dado.
    public final static String ADM_ERR_0007 = "ADM_ERR_0007";
    //La consulta del timer no se realizó con éxito. No existe un timer asociado al id dado.
    public final static String ADM_ERR_0008 = "ADM_ERR_0008";
    //******************************************************
    //Fin
    //Administracion Sisinfo
    //*****************************************************
    //******************************************************
    //Comienzo
    //Monitorias
    //*****************************************************
    //..................................ERRORES.................................
    //Para configurar las alertas asociadas al proceso de monitorías usted debe configurar los plazos primero. Vaya por favor a la acción "Configurar Plazos".
    public final static String MON_ERR_0001 = "MON_ERR_0001";
    //Para configurar los plazos usted debe cargar primero una cartelera y un periodo. Vaya por favor a la acción "Subir Archivo Cartelera".
    public final static String MON_ERR_0002 = "MON_ERR_0002";
    //Errores de subir cartelera. Errores asociados al servlet
    //El formato del archivo de cartelera es erróneo. Todos los campos de la columna CRN deben ser numéricos. Por favor revise el archivo e inténtelo de nuevo.
    public final static String MON_ERR_0003 = "El formato del archivo de cartelera es erróneo. Todos los campos de la columna CRN deben ser numéricos. Por favor revise el archivo e inténtelo de nuevo.";
    //El formato del archivo de cartelera es erróneo. Todos los campos de la columna SECC deben ser numéricos. Por favor revise el archivo e inténtelo de nuevo.
    public final static String MON_ERR_0004 = "El formato del archivo de cartelera es erróneo. Todos los campos de la columna SECC deben ser numéricos. Por favor revise el archivo e inténtelo de nuevo.";
    //El formato del archivo de cartelera es erróneo. Todos los campos de la columna CRED deben ser numéricos. Por favor revise el archivo e inténtelo de nuevo.
    public final static String MON_ERR_0005 = "El formato del archivo de cartelera es erróneo. Todos los campos de la columna CRED deben ser numéricos. Por favor revise el archivo e inténtelo de nuevo.";
    //El formato del archivo de cartelera es erróneo. Todos los campos de la columna NOMBRE_CURSO deben ser texto. Por favor revise el archivo e inténtelo de nuevo.
    public final static String MON_ERR_0006 = "El formato del archivo de cartelera es erróneo. Todos los campos de la columna NOMBRE_CURSO deben ser texto. Por favor revise el archivo e inténtelo de nuevo.";
    //El formato del archivo de cartelera es erróneo. Todos los campos de la columna PROFESOR1 deben ser texto. Por favor revise el archivo e inténtelo de nuevo.
    public final static String MON_ERR_0007 = "El formato del archivo de cartelera es erróneo. Todos los campos de la columna PROFESOR1 deben ser texto. Por favor revise el archivo e inténtelo de nuevo.";
    //El formato del archivo de cartelera es erróneo. Todos los campos de la columna PROFESOR1 deben corresponder con el login de un profesor del Departamento. Por favor revise el archivo e inténtelo de nuevo.
    public final static String MON_ERR_0008 = "El formato del archivo de cartelera es erróneo. Todos los campos de la columna PROFESOR1 deben corresponder con el login de un profesor del Departamento. Por favor revise el archivo e inténtelo de nuevo.";
    //El formato del archivo de cartelera es erróneo. Todos los campos de las columnas HIHI y HFINI deben ser numéricos. Por favor revise el archivo e inténtelo de nuevo.
    public final static String MON_ERR_0009 = "El formato del archivo de cartelera es erróneo. Todos los campos de las columnas HIHI y HFINI deben ser numéricos. Por favor revise el archivo e inténtelo de nuevo.";
    //El formato del archivo de cartelera es erróneo. Todos los campos de la columna MATERIA deben ser texto. Por favor revise el archivo e inténtelo de nuevo.
    public final static String MON_ERR_0010 = "El formato del archivo de cartelera es erróneo. Todos los campos de la columna MATERIA deben ser texto. Por favor revise el archivo e inténtelo de nuevo.";
    //El formato del archivo de cartelera es erróneo. Existe una sección que no se está dictando en ningún día de la semana
    public final static String MON_ERR_0011 = "El formato del archivo de cartelera es erróneo. Existe una sección que no se está dictando en ningún día de la semana";
    //Ya se cargo una cartelera para el periodo seleccionado. No es posible volver a cargar una cartelera para este periodo. Por favor seleccione otro periodo
    public final static String MON_ERR_0012 = "Ya se cargo una cartelera para el periodo seleccionado. No es posible volver a cargar una cartelera para este periodo. Por favor seleccione otro periodo";
    //Ya existe un periodo actual en el sistema. No es posible cargar un nuevo archivo de configuración. Usted debe primero cerrar el periodo actual para poder cargar una nueva cartelera.
    public final static String MON_ERR_0020 = "Ya existe un periodo actual en el sistema. No es posible cargar un nuevo archivo de configuración. Usted debe primero cerrar el periodo actual para poder cargar una nueva cartelera.";
    //El formato del archivo de cartelera es incorrecto. El archivo debe estar guardado como un Excel 97-2003
    public final static String MON_ERR_0022 = "El formato del archivo de cartelera es incorrecto. El archivo debe estar guardado como un Excel 97-2003 (xls)";
    //El formato del archivo de cartelera es erróneo. Todos los campos de la columna CANT_MONITORES deben ser númericos. Por favor revise el archivo e inténtelo de nuevo.
    public final static String MON_ERR_0023 = "El formato del archivo de cartelera es erróneo. Todos los campos de las columna CANT_MONITORES deben ser númericos. Por favor revise el archivo e inténtelo de nuevo.";
    //El formato del archivo de cartelera es erróneo. Todos los campos de la columna ES_PRESENCIAL deben ser texto. Por favor revise el archivo e inténtelo de nuevo.
    public final static String MON_ERR_0024 = "El formato del archivo de cartelera es erróneo. Todos los campos de la columna ES_PRESENCIAL deben ser igual a \"true\" o \"false\". Por favor revise el archivo e inténtelo de nuevo.";
    //El formato del archivo de cartelera es incorrecto. El archivo debe estar guardado como un Excel 97-2003
    //El formato del archivo de cartelera es erróneo. Todos los campos de la columna ES_OBLIGATORIO deben ser texto. Por favor revise el archivo e inténtelo de nuevo.
    public final static String MON_ERR_0025 = "El formato del archivo de cartelera es erróneo. Todos los campos de la columna ES_OBLIGATORIO deben ser igual a \"true\" o \"false\". Por favor revise el archivo e inténtelo de nuevo.";
    //El formato del archivo de cartelera es erróneo. El orden de las columnas debe de ser: N - CRN - MATERIA - SECC - CRED - NOMBRE_CURSO - CANT_MONITORES - ES_PRESENCIAL - ES_OBLIGATORIA - PROFESOR1 - (HINIX - HFINX - SALONX - L - M - I - J - V - S)+
    public final static String MON_ERR_0027 = "El formato del archivo de cartelera es erróneo. El orden de las columnas debe de ser: N - CRN - MATERIA - SECC - CRED - NOMBRE_CURSO - CANT_MONITORES - ES_PRESENCIAL - ES_OBLIGATORIA - PROFESOR1 - (HINIX - HFINX - SALONX - L - M - I - J - V - S)+";
    //FIN Errores de subir cartelera. Errores asociados al servlet
    //No se puede asignar un número de sección que ya esté siendo utilizado.
    public final static String MON_ERR_0013 = "MON_ERR_0013";
    //No se puede modificar la cantidad de monitores, ya que el profesor de la sección ya realizó preselecciones
    public final static String MON_ERR_0014 = "MON_ERR_0014";
    //Para modificar los datos de un curso, usted debe primer configurar los plazos de monitorías. Vaya por favor a la acción "Configurar Plazos".
    public final static String MON_ERR_0015 = "MON_ERR_0015";
    //Para modificar los datos de una sección, usted debe primer configurar los plazos de monitorías. Vaya por favor a la acción "Configurar Plazos".
    public final static String MON_ERR_0016 = "MON_ERR_0016";
    //Hubo un error al pasar los datos del módulo de monitorías al histórico. Por favor contacte al administrador.
    public final static String MON_ERR_0017 = "MON_ERR_0017";
    //Hubo un error al pasar los datos del módulo de planeación al histórico. Por favor contacte al administrador.
    public final static String MON_ERR_0018 = "MON_ERR_0018";
    //Hubo un error al eliminar los datos viejos. Por favor contacte al administrador.
    public final static String MON_ERR_0019 = "MON_ERR_0019";
    //No hay vacantes para las secciones escogidas. Por favor contacte al administrador.
    public final static String MON_ERR_0021 = "MON_ERR_0021";
    //El nombre de usuario del profesor que intenta agregar ya se encuentra registrado. Por favor verifique los datos.
    public final static String MON_ERR_0026 = "MON_ERR_0026";
    //La convocatoria ya fue abierta, y no es posible modificar la cantidad de monitores del curso.
    public final static String MON_ERR_0028 = "MON_ERR_0028";
    //Usted se encuentra en la lista negra de monitorías y no puede ingresar una solicitud. Para mayor información por favor contactar con la Coordinación del Departamento
    public final static String MON_ERR_0029 = "MON_ERR_0029";
    // La solicitud del estudiante no pudo ser seleccionada por que ya tiene una monitoria asignada. Por favor preseleccione a otro estudiante.
    public final static String MON_ERR_0030 = "MON_ERR_0030";
    // La seccion especificada no tiene vacantes para monitores
    public final static String MON_ERR_0031 = "MON_ERR_0031";
    // La solicitud seleccionada no fue encontrada en el sistema
    public final static String MON_ERR_0032 = "MON_ERR_0032";
    // La seccion especificada no fue encontrada
    public final static String MON_ERR_0033 = "MON_ERR_0033";
    // La solicitud seleccionada ya ha sido verificada
    public final static String MON_ERR_0034 = "MON_ERR_0034";
    // La seccion especificada fue revertida
    public final static String MON_ERR_0035 = "MON_ERR_0035";
    //ERR-0001 = "Error interno del sistema. Por favor intente de nuevo"
    public final static String ERR_0001 = "ERR_0001";
    //ERR-0003 = "La cartelera no se ha cargado con exito. Intente nuevamente."
    public final static String ERR_0003 = "ERR_0003";
    //ERR-0004 = "El listado de profesores para el periodo $PERIODO$ no se ha cargado con exito. Intente nuevamente."
    public final static String ERR_0004 = "ERR_0004";
    //ERR-0006 = "La actualizacion de la solicitud con id $ID_SOLICITUD$ no se ha realizado exitosamente. Intente nuevamente."
    public final static String ERR_0006 = "ERR_0006";
    //ERR-0007 = "La confirmacion del estudiante no fue realizada con exito (no fue posible cancelar la preseleccion para la solicitud con id $ID_SOLICITUD$), intente nuevamente"
    public final static String ERR_0007 = "ERR_0007";
    //ERR-0008 = "La confirmacion del estudiante no fue realizada con exito, intente nuevamente"
    public final static String ERR_0008 = "ERR_0008";
    //ERR-0009 = "La actualizacion de la solicitud con id $ID_SOLICITUD$ y la verificacion de los datos no se realizo exitosamente. Intente de nuevo."
    public final static String ERR_0009 = "ERR_0009";
    //ERR-0010 = "La confirmación de la sección no fué realizada con éxito (no fué posible cancelar la preselección), intente nuevamente"
    public final static String ERR_0010 = "ERR_0010";
    //ERR-0012 = "El registro de firma del convenio no se realizó con éxito (no se encontró la solicitud con id $ID_SOLICITUD$), intente nuevamente"
    public final static String ERR_0012 = "ERR_0012";
    //ERR-0013 = "El registro del convenio para la solicitud con id $ID_SOLICITUD$ no se realizó con éxito, intente nuevamente"
    public final static String ERR_0013 = "ERR_0013";
    //ERR-0016 = "No se pudo realizar la apertura de convocatoria. Ya existe una convocatoria cerrada para este periodo."
    public final static String ERR_0016 = "ERR_0016";
    //ERR-0018 = "No se pudo realizar la apertura de convocatoria. Ya existe una convocatoria abierta para este periodo."
    public final static String ERR_0018 = "ERR_0018";
    //ERR-0020 = "Los datos del curso con codigo $CODIGO_CURSO$ no fueron modificados con éxito. Intente nuevamente."
    public final static String ERR_0020 = "ERR_0020";
    //ERR-0021 = "El periodo $PERIODO$ no fue iniciado exitosamente. Intente de nuevo."
    public final static String ERR_0021 = "ERR_0021";
    //ERR-0024 = "La consulta de cursos con vacantes no se realizo con exito. Intente nuevamente."
    public final static String ERR_0024 = "ERR_0024";
    //ERR-0026 = "La consulta de facultades no se realizo con exito. Intente nuevamente."
    public final static String ERR_0026 = "ERR_0026";
    //ERR-0029 = "La consulta de programas en la facultad de $NOMBRE_FACULTAD% no se realizo con exito. Intente nuevamente."
    public final static String ERR_0029 = "ERR_0029";
    //ERR-0073 = "El horario no se modificó dado a que los plazos de modificación de información no están vigentes"
    public final static String ERR_0073 = "ERR_0073";
    //ERR-0033 = "La consulta de secciones con vacantes no se realizo con exito, intente nuevamente"
    public final static String ERR_0033 = "ERR_0033";
    //ERR-0036 = "La consulta de secciones con vacantes para el profesor con login $LOGIN_PROFESOR$ no se realizo con exito. Intente nuevamente."
    public final static String ERR_0036 = "ERR_0036";
    //ERR-0039 = "La consulta a la lista negra no se realizo con exito. Intente nuevamente."
    public final static String ERR_0039 = "ERR_0039";
    //ERR-0040 = "El estudiante con login $LOGIN_ESTUDIANTE$ no se pudo agregar a la lista negra. Intente de nuevo."
    public final static String ERR_0040 = "ERR_0040";
    //ERR-0041 = "El estudiante con login $LOGIN_ESTUDIANTE$ no se pudo eliminar de la lista negra. Intente de nuevo."
    public final static String ERR_0041 = "ERR_0041";
    //ERR-0043 = "La consulta de programas no se realizó con éxito, intente nuevamente"
    public final static String ERR_0043 = "ERR_0043";
    //ERR-0047	La solicitud del estudiante $NOMBRES_ESTUDIANTE$ $APELLIDOS_ESTUDIANTE$ para el curso $NOMBRE_CURSO$ no se ha realizado. Intente de nuevo.
    public final static String ERR_0047 = "ERR_0047";
    //ERR-0049	La solicitud con id $ID_SOLICITUD$ no se ha eliminado del sistema. Intente de nuevo.
    public final static String ERR_0049 = "ERR_0049";
    //ERR-0053  La confirmacion de la sección no fue realizada con exito, intente nuevamente
    public final static String ERR_0053 = "ERR_0053";
    //ERR-0056  La consulta de solicitudes por estado no se realizó con éxito, intente nuevamente
    public final static String ERR_0056 = "ERR_0056";
    //ERR-0058  La actualización de los datos académicos no se realizó con éxito
    public final static String ERR_0058 = "ERR_0058";
    //ERR-0059  La consulta de la informacion de emergencia para el estudiante con login $LOGIN_ESTUDIANTE$ no se realizo con exito. Intente nuevamente.
    public final static String ERR_0059 = "ERR_0059";
    //ERR-0060  La consulta de criterios de busqueda no se pudo realizar correctamente. Intente nuevamente
    public final static String ERR_0060 = "ERR_0060";
    //ERR-0061  La actualización de los datos personales y de emergencia no se realizó con éxito, intente nuevamente
    public final static String ERR_0061 = "ERR_0061";
    //ERR-0061  La consulta de las secciones sin preseleccion para el profesor con login $LOGIN_PROFESOR$ no se realizo con exito. Intente nuevamente.
    public final static String ERR_0062 = "ERR_0062";
    //ERR-0063	La consulta de preselecciones realizadas por coordinacion para el profesor con login $LOGIN_PROFESOR$ no se ha realizado exitosamente. Intente de nuevo.
    public final static String ERR_0063 = "ERR_0063";
    //ERR-0065  El histórico no fué cargado con éxito, intente nuevamente
    public final static String ERR_0065 = "ERR_0065";
    //ERR-0066	La consulta de preselecciones realizadas para la seccion con crn $CRN_SECCION$ no se ha realizado exitosamente. Intente de nuevo.
    public final static String ERR_0066 = "ERR_0066";
    //ERR-0068  La consulta de solicitudes en convenio no se realizó con éxito, intente nuevamente
    public final static String ERR_0068 = "ERR_0068";
    //ERR-0069	La consulta de aspiraciones para la seccion con crn $CRN_SECCION$ no se ha realizado exitosamente. Intente de nuevo.
    public final static String ERR_0069 = "ERR_0069";
    //ERR-0070	La solicitud con id $ID_SOLICITUD$ no se ha cancelado por carga. Intente de nuevo.
    public final static String ERR_0070 = "ERR_0070";
    //ERR-0071  La consulta se secciones por tipo y estado no se realizó con éxito, intente nuevamente
    public final static String ERR_0071 = "ERR_0071";
    //ERR-0072 La consulta de tareas por correo y estado no fué realizada exitosamente, intente nuevamente
    public final static String ERR_0072 = "ERR_0072";
    //ERR-0074	La consulta de solicitudes no se realizo exitosamente. Intente de nuevo.
    public final static String ERR_0074 = "ERR_0074";
    //ERR-0075	La consulta de cursos no se realizo exitosamente. Intente de nuevo.
    public final static String ERR_0075 = "ERR_0075";
    //ERR-0076	La consulta de alertas por estado de tarea no fué realizada con éxito, intente nuevamente
    public final static String ERR_0076 = "ERR_0076";
    //ERR-0077	La consulta de la información de los profesores no se realizó con éxito, intente nuevamente
    public final static String ERR_0077 = "ERR_0077";
    //ERR-0078	La parametrización de fechas no fué realizada con éxito, intente nuevamente
    public final static String ERR_0078 = "ERR_0078";
    //ERR-0079	La actualización de los datos del profesor no fué realizada con éxito, intente nuevamente
    public final static String ERR_0079 = "ERR_0079";
    //ERR-0080	La consulta de tarea por id no se realizó con éxito, intente nuevamente
    public final static String ERR_0080 = "ERR_0080";
    //ERR-0081	No se encontró la tarea buscada
    public final static String ERR_0081 = "ERR_0081";
    //ERR-0082	La actualizacion del estado de la tarea no fué realizada con éxito, intente nuevamente
    public final static String ERR_0082 = "ERR_0082";
    //ERR-0083	La adición del profesor no fué realizada con éxito, intente nuevamente
    public final static String ERR_0083 = "ERR_0083";
    //ERR-0084	La consulta de alertas no fué realizada con éxito, intente nuevamente
    public final static String ERR_0084 = "ERR_0084";
    //ERR-0085	La carga de facultades no fué realizada con éxito, intente nuevamente
    public final static String ERR_0085 = "ERR_0085";
    //ERR-0086	La consulta de monitorias para la seccion $NUMERO_SECCION$ del curso $NOMBRE_CURSO$ no se realizo con exito.
    public final static String ERR_0086 = "ERR_0086";
    //ERR-0087	La tarea con id $ID_TAREA$ no fué encontrada
    public final static String ERR_0087 = "ERR_0087";
    //ERR-0088	La confirmación de subida de archivo no fué realizada con éxito, intente nuevamente
    public final static String ERR_0088 = "ERR_0088";
    //ERR-0089	No se encontraron parámetros de la tarea: crn de la sección
    public final static String ERR_0089 = "ERR_0089";
    //ERR-0090	La actualización de notas de los monitores no fué realizada con éxito, intente nuevamente
    public final static String ERR_0090 = "ERR_0090";
    //ERR-0091	La consulta de solicitudes después de estado no fué realizada con éxito, intente nuevamente
    public final static String ERR_0091 = "ERR_0091";
    //ERR-0092 No se pudo enviar la solicitud debido a que existe un conflicto de horario entre la sección y el horario del estudiante.
    public final static String ERR_0092 = "ERR_0092";
    //ERR-0093 No se pudo enviar la solicitud debido a que existe un conflicto de horario entre todas las secciones del curso y el horario del estudiante.
    public final static String ERR_0093 = "ERR_0093";
    //ERR-0094	Su rol dentro de la organización no está soportado por el sistema, por favor comuníqeuse con el departamento si cree que hay un error
    public final static String ERR_0094 = "ERR_0094";
    //ERR-0095	Por favor ingrese un nombre de usuario válido
    public final static String ERR_0095 = "ERR_0095";
    //ERR-0096	La verificación de datos saltando reglas no fué realizada con éxito, intente nuevamente
    public final static String ERR_0096 = "ERR_0096";
    //ERR-0097	La carga de información de monitores no fué realizada con éxito, intente nuevamente
    public final static String ERR_0097 = "ERR_0097";
    //ERR-0098 No se encontro la monitoria de ese estudiante para la seccion en el periodo especificado
    public final static String ERR_0098 = "ERR_0098";
    //ERR-0099 No existe una convocatoria abierta para este semestre.
    public final static String ERR_0099 = "ERR_0099";
    //ERR-0121 Las alertas no se han creado exitosamente.
    public final static String ERR_0121 = "ERR_0121";
    //ERR-0133 No se ha actualizado la información académica debido a que no cumplen con los parámetros de solicitud de monitoría.
    public final static String ERR_0133 = "ERR_0133";
    //ERR-134 La solicitud no fue actualizada con éxito. Revise la información ingresada e intente nuevamente.
    public final static String ERR_0134 = "ERR_0134";
    //ERR-0123 La consulta de la información del profesor no se realizó con éxito, intente nuevamente
    public final static String ERR_0123 = "ERR_0123";
    //No se encontró la sección
    public final static String ERR_0129 = "ERR_0129";
    // En el momento no hay cursos con vacantes. Sin embargo, la convocatoria sigue abierta, así que
    // esté pendiente en estos días, en caso de que alguien cancele o se abran nuevos cupos.
    public final static String ERR_0130 = "ERR_0130";
    //ERR_0131 La consulta de los datos de la monitoria no se realizó con éxito
    public final static String ERR_0131 = "ERR_0131";
    //ERR_0132 No se pudo determinar correctamente si el estudiante tiene o no solicitudes
    public final static String ERR_0132 = "ERR_0132";
    //ERR_0140 No se pueden agregar más solicitudes a este curso. Se ha alcanzado el número máximo de solicitudes que puede enviar
    public final static String ERR_0140 = "ERR_0140";
    //ERR-0141 = "No se encontró la solicitud. Por favor intente de nuevo"
    public final static String ERR_0141 = "ERR_0141";
    //Errores que faltan:
    //28,30,31,32,34,35,37,38,42,44,45,46,48,50,52,54,55,57,64,67,73
    //ERR-0126 El estudiante no cumple con las reglas para solicitar monitoría. La solicitud no ha sido enviada.
    public final static String ERR_0126 = "ERR_0126";
    //ERR_0127 El tipo de tarea no existe.
    public final static String ERR_0127 = "ERR_0127";
    //ERR_0128 La tarea del tipo y los parámetros dados no existe
    public final static String ERR_0128 = "ERR_0128";
    //ERR_0011 El profesor no fué eliminado exitosamente, intente nuevamente
    public final static String ERR_0011 = "ERR_0011";
    //ERR_0014 Los profesores no fueron agregados con éxito, intente nuevamente
    public final static String ERR_0014 = "ERR_0014";
    //ERR_0015 El grupo de investigación no fué agregado con éxito, intente nuevamente
    public final static String ERR_0015 = "ERR_0015";
    //ERR_0017 Los grupos de investigación no fueron agregados con éxito, intente nuevamente
    public final static String ERR_0017 = "ERR_0017";
    //ERR_0019 El grupo de investigación no fué eliminado con éxito, intente nuevamente
    public final static String ERR_0019 = "ERR_0019";
    //ERR_0022 El grupo de investigación no fué modificado con éxito, intente nuevamente
    public final static String ERR_0022 = "ERR_0022";
    //ERR_0025 La consulta de pertenencia a grupo de investigación no fué realizada con éxito, intente nuevamente
    public final static String ERR_0025 = "ERR_0025";
    //ERR_0027 La consulta de profesores de grupo de investigación no fué realizada con éxito, intente nuevamente
    public final static String ERR_0027 = "ERR_0027";
    //ERR_0028 La consulta de profesores por tipo no fué realizada con éxito, intente nuevamente
    public final static String ERR_0028 = "EeRR_0028";
    //ERR_0029 La consulta de grupos de investigación no fué realizada con éxito, intente nuevamente
    public final static String ERR_0030 = "ERR_0030";
    //ERR_0048 Ya venció el plazo para crear Solicitudes de Monitorías. La Solicitud no ha sido enviada.
    public final static String ERR_0142 = "ERR_0142";
    //ERR_0143 El estudiante no cumple con las reglas para solicitar monitoría. La solicitud no ha sido enviada. El promedio acumulado debe ser superior o igual a 3.5.
    public final static String ERR_0143 = "ERR_0143";
    //ERR_0144 El estudiante no cumple con las reglas para solicitar monitoría. La solicitud no ha sido enviada. La cantidad de créditos sin contar monitorías debe ser inferior o igual a 18.5 si su promedio es inferior a 4.0 o bien inferior o igual a 23 si su promedio es superior o igual a 4.0.
    public final static String ERR_0144 = "ERR_0144";
    //ERR_0145 El estudiante no cumple con las reglas para solicitar monitoría. La solicitud no ha sido enviada. El nivel de la materia no corresponde con el nivel del estudiante
    public final static String ERR_0145 = "ERR_0145";
    //ERR_0146 El estudiante no cumple con las reglas para solicitar monitoría. La solicitud no ha sido enviada. El estudiante ya tiene demasiadas monitorias con el departamento
    public final static String ERR_0146 = "ERR_0146";
    //ERR_0147 El estudiante no cumple con las reglas para solicitar monitoría. La solicitud no ha sido enviada. La nota del estudiante en la materia no es suficiente para aplicar a una monitoria
    public final static String ERR_0147 = "ERR_0147";
    //ERR_0148=El estudiante con correo {0} no se encuentra en el sistema.err
    public final static String ERR_0148 = "ERR_0148";
    //El evento al que intenta inscribirse está cerrado
    public final static String ERR_0149 = "ERR_0149";
    //El evento al que intenta inscribirse no tiene cupo disponible
    public final static String ERR_0159 = "ERR_0159";
    //La inscripcion no pudo ser realizada.Por favor contacte al administrador.
    public final static String ERR_0160 = "ERR_0160";
    //No se puede eliminar la categoría ya que hay eventos creados que la utilizan.
    public final static String ERR_0161 = "ERR_0161";
    //No se puede eliminar el campo ya que hay eventos creados que lo utilizan.
    public final static String ERR_0162 = "ERR_0162";
    //Ya existe un evento con el mismo nombre.
    public final static String ERR_0163 = "ERR_0163";
    //.................................MENSAJES.................................
    //MSJ-0001 = "El sistema inicio con exito"
    public final static String MSJ_0001 = "MSJ_0001";
    //MSJ-0002 = "La consulta de la informacion academica del estudiante con codigo $CODIGO_ESTUDIANTE$ se ha realizado con exito"
    public final static String MSJ_0002 = "MSJ_0002";
    //MSJ-0003 = "La consulta de la informacion personal del estudiante con codigo $CODIGO_ESTUDIANTE$ se ha realizado con exito"
    public final static String MSJ_0003 = "MSJ_0003";
    //MSJ-0004 = "La cartelera para el periodo $PERIODO$ se ha cargado con exito."
    public final static String MSJ_0004 = "MSJ_0004";
    //MSJ-0005 = "El listado de profesores para el periodo $PERIODO$ se ha cargado con exito."
    public final static String MSJ_0005 = "MSJ_0005";
    //MSJ-0006 = "La actualizacion de la solicitud con id $ID_SOLICITUD$ se ha realizado con exito."
    public final static String MSJ_0006 = "MSJ_0006";
    //MSJ-0007 = "La confirmacion del estudiante se ha realizado con exito"
    public final static String MSJ_0007 = "MSJ_0007";
    //MSJ-0008 = "La actualizacion de la solicitud con id $ID_SOLICITUD$y la verificacion de los datos se realizo exitosamente."
    public final static String MSJ_0008 = "MSJ_0008";
    //MSJ-0009 = "La confirmación de la sección $NUMERO_SECCION$ se ha realizado con éxito"
    public final static String MSJ_0009 = "MSJ_0009";
    //MSJ-0010 = "La consulta del horario del estudiante con codigo $CODIGO_ESTUDIANTE$ se ha realizado con exito"
    public final static String MSJ_0010 = "MSJ_0010";
    //MSJ-0011 = "La consulta del horario del estudiante con login $LOGIN_ESTUDIANTE$ se ha realizado con exito"
    public final static String MSJ_0011 = "MSJ_0011";
    //MSJ-0012 = "El registro del convenio para la solicitud con id $ID_SOLICITUD$ se ha realizado con éxito"
    public final static String MSJ_0012 = "MSJ_0012";
    //MSJ-0013 = "La convocatoria para el periodo $PERIODO$ fué abierta con éxito"
    public final static String MSJ_0013 = "MSJ_0013";
    //MSJ-0014 = "La convocatoria para el periodo $PERIODO$ fué cerrada con éxito"
    public final static String MSJ_0014 = "MSJ_0014";
    //MSJ-0015 = "Los datos del curso con codigo $CODIGO_CURSO$ fueron modificados con éxito"
    public final static String MSJ_0015 = "MSJ_0015";
    //MSJ-0016 = "El periodo $PERIODO$ fue iniciado exitosamente."
    public final static String MSJ_0016 = "MSJ_0016";
    //MSJ-0017 = "La consulta de cursos con vacantes se ha realizado con exito. Intente nuevamente."
    public final static String MSJ_0017 = "MSJ_0017";
    //MSJ-0018 = "La consulta de facultades se ha realizado con exito."
    public final static String MSJ_0018 = "MSJ_0018";
    //MSJ-0019 = "La consulta de programas en la facultad de $NOMBRE_FACULTAD% se realizo con exito."
    public final static String MSJ_0019 = "MSJ_0019";
    //MSJ-0020 = "La consulta de secciones con vacantes se realizo con exito"
    public final static String MSJ_0020 = "MSJ_0020";
    //MSJ-0021 = "La consulta de secciones con vacantes para el profesor con login $LOGIN_PROFESOR$ se realizo con exito."
    public final static String MSJ_0021 = "MSJ_0021";
    //MSJ-0022 = "La consulta a la lista negra se realizo con exito. El estudiante con codigo $CODIGO_ESTUDIANTE$ esta en lista negra"
    public final static String MSJ_0022 = "MSJ_0022";
    //MSJ-0023 = "La consulta a la lista negra se realizo con exito. El estudiante con login $LOGIN_ESTUDIANTE$ esta en lista negra"
    public final static String MSJ_0023 = "MSJ_0023";
    //MSJ-0024 = "El estudiante con login $LOGIN_ESTUDIANTE$ se agrego a la lista negra exitosamente."
    public final static String MSJ_0024 = "MSJ_0024";
    //MSJ-0025 = "El estudiante con login $LOGIN_ESTUDIANTE$ se elimino de la lista negra exitosamente."
    public final static String MSJ_0025 = "MSJ_0025";
    //MSJ-0026	La consulta de programas se realizo con exito.
    public final static String MSJ_0026 = "MSJ_0026";
    //MSJ-0027	El estudiante $NOMBRES_ESTUDIANTE$ $APELLIDOS_ESTUDIANTE$ ha sido preseleccionado exitosamente para la seccion numero $NUMERO_SECCION$ del profesor $NOMBRE_COMPLETO_PROFESOR$
    public final static String MSJ_0027 = "MSJ_0027";
    //MSJ-0028    La solicitud del estudiante $NOMBRES_ESTUDIANTE$ $APELLIDOS_ESTUDIANTE$ para el curso $NOMBRE_CURSO$ se ha realizado exitosamente.
    public final static String MSJ_0028 = "MSJ_0028";
    //MSJ-0029	La consulta de solicitudes para el estudiante  $NOMBRES_ESTUDIANTE$ $APELLIDOS_ESTUDIANTE$ se realizo exitosamente.
    public final static String MSJ_0029 = "MSJ_0029";
    //MSJ-0030	La consulta de la solicitud con id $ID_SOLICITUD$ se realizo exitosamente.
    public final static String MSJ_0030 = "MSJ_0030";
    //MSJ-0031	La solicitud con id $ID_SOLICITUD$ se ha eliminado exitosamente del sistema
    public final static String MSJ_0031 = "MSJ_0031";
    //MSJ-0032	La consulta de datos basicos de sesion y la autorizacion para $LOGIN_USUARIO$ se realizaron exitosamente.
    public final static String MSJ_0032 = "MSJ_0032";
    //MSJ-0033 La consulta a la lista negra se realizo con exito. El estudiante con codigo $CODIGO_ESTUDIANTE$ no esta en lista negra
    public final static String MSJ_0033 = "MSJ_0033";
    //MSJ-0034 La consulta a la lista negra se realizo con exito. El estudiante con login $LOGIN_ESTUDIANTE$ no esta en lista negra
    public final static String MSJ_0034 = "MSJ_0034";
    //MSJ-0035 La consulta de las secciones se realizo con exito.
    public final static String MSJ_0035 = "MSJ_0035";
    //MSJ-0036  La consulta de solicitudes por estado se ha efectuado con éxito
    public final static String MSJ_0036 = "MSJ_0036";
    //MSJ-0037 La consulta del historico se realizo con exito
    public final static String MSJ_0037 = "MSJ_0037";
    //MSJ-0038  La actualización de los datos académicos se realizó con éxito
    public final static String MSJ_0038 = "MSJ_0038";
    //MSJ-0039  La consulta de la informacion de emergencia para el estudiante con login $LOGIN_ESTUDIANTE$ se realizo con exito.
    public final static String MSJ_0039 = "MSJ_0039";
    //MSJ-0040  La consulta de criterios para la busqueda se realizo con exito
    public final static String MSJ_0040 = "MSJ_0040";
    //MSJ-0041  La actualización de los datos personales y de emergencia se realizó con éxito
    public final static String MSJ_0041 = "MSJ_0041";
    //MSJ-0042 La consulta de las seciones sin preseleccion para el profesor con login $LOGIN_PROFESOR$ se realizo con exito.
    public final static String MSJ_0042 = "MSJ_0042";
    //MSJ-0043	La consulta se ha realizado con exito. El profesor con login $LOGIN_PROFESOR$ no tiene preselecciones realizadas por coordinacion.
    public final static String MSJ_0043 = "MSJ_0043";
    //MSJ-0044	La consulta se ha realizado con exito. El profesor con login $LOGIN_PROFESOR$ tiene preselecciones realizadas por coordinacion.
    public final static String MSJ_0044 = "MSJ_0044";
    //MSJ-0045	La consulta se ha realizado con exito. La seccion con crn $CRN$ no tiene solicitudes preseleccionadas
    public final static String MSJ_0045 = "MSJ_0045";
    //MSJ-0046	La consulta se ha realizado con exito. La seccion con crn $CRN$ tiene solicitudes preseleccionadas
    public final static String MSJ_0046 = "MSJ_0046";
    //MSJ-0047	El histórico fue cargado con éxito
    public final static String MSJ_0047 = "MSJ_0047";
    //MSJ-0048 Lista negra cargada
    public final static String MSJ_0048 = "MSJ_0048";
    //MSJ-0049 Intercambio realizado
    public final static String MSJ_0049 = "MSJ_0049";
    //MSJ-0050 La consulta de solicitudes en convenio se realizó con éxito
    public final static String MSJ_0050 = "MSJ_0050";
    //MSJ-0051	La consulta de paises se realizo con exito. No se encontraron paises en el sistema
    public final static String MSJ_0051 = "MSJ_0051";
    //MSJ-0052	La consulta de paises se realizo con exito. Se encontraron paises en el sistema
    public final static String MSJ_0052 = "MSJ_0052";
    //MSJ-0053	La consulta se ha realizado con exito. La seccion con crn $CRN$ no tiene solicitudes en aspiracion
    public final static String MSJ_0053 = "MSJ_0053";
    //MSJ-0054	La consulta se ha realizado con exito. La seccion con crn $CRN$ tiene solicitudes en aspiracion
    public final static String MSJ_0054 = "MSJ_0054";
    //MSJ-0055	La solicitud con id $ID_SOLICITUD$ se ha cancelado por carga exitosamente
    public final static String MSJ_0055 = "MSJ_0055";
    //MSJ-0056	La consulta de las solicitudes se ha eralizado con exito.
    public final static String MSJ_0056 = "MSJ_0056";
    //MSJ-0057	La consulta de las solicitudes se ha eralizado con exito. NO hay solicitudes en el sisetma.
    public final static String MSJ_0057 = "MSJ_0057";
    //MSJ-0058  La consulta se secciones por tipo y estado se realizó con éxito
    public final static String MSJ_0058 = "MSJ_0058";
    //MSJ-0059  La consulta de tareas por correo y estado fué realizada exitosamente
    public final static String MSJ_0059 = "MSJ_0059";
    //MSJ-0059  La consulta cursos fué realizada exitosamente
    public final static String MSJ_0060 = "MSJ_0060";
    //MSJ-0061	La consulta de cursos fué realizada exitosamente. No se encontraron cursos en el sistema.
    public final static String MSJ_0061 = "MSJ_0061";
    //MSJ-0062  Se ha realizado la resolución del curso $CODIGO_CURSO$. 
    public final static String MSJ_0062 = "MSJ_0062";
    //MSJ-0063	Se ha realizado la resolución del curso $CODIGO_CURSO$. No se encontraron aspirantes suficientes para las secciones $CRN_SECCIONES$, así que se ha enviado un correo a los estudiantes para que se presenten.
    public final static String MSJ_0063 = "MSJ_0063";
    //MSJ-0064	La consulta de alertas por estado de tarea fué realizada con éxito
    public final static String MSJ_0064 = "MSJ_0064";
    //MSJ-0065	La parametrización de fechas fué realizada con éxito
    public final static String MSJ_0065 = "MSJ_0065";
    //MSJ-0066	La consulta de tarea por id se ha realizado con éxito
    public final static String MSJ_0066 = "MSJ_0066";
    //MSJ-0067	La actualización del estado de la tarea se realizó con éxito
    public final static String MSJ_0067 = "MSJ_0067";
    //MSJ-0068	La consulta de alertas se realizó con éxito
    public final static String MSJ_0068 = "MSJ_0068";
    //MSJ-0069	La consulta de monitorias para la seccion $NUMERO_SECCION$ del curso $NOMBRE_CURSO$ se realizo con exito.
    public final static String MSJ_0069 = "MSJ_0069";
    //MSJ-0070	La consulta de monitorias para la seccion $NUMERO_SECCION$ del curso $NOMBRE_CURSO$ se realizo con exito. La seccion no tiene monitorias.
    public final static String MSJ_0070 = "MSJ_0070";
    //ERR-0002 = "El estudiante con login $LOGIN_ESTUDIANTE$  no existe en el sistema"
    public final static String MSJ_0071 = "MSJ_0071";
    //ERR-0005 = "La solicitud con id $ID_SOLICITUD$  no existe en el sistema"
    public final static String MSJ_0072 = "MSJ_0072";
    //ERR-0011 = "El estudiante con login $LOGIN_ESTUDIANTE$  no existe en el sistema"
    public final static String MSJ_0073 = "MSJ_0073";
    //ERR-0014 = "La convocatoria no fué abierta con éxito (no se encontró el periodo $PERIODO$). Intente nuevamente"
    public final static String MSJ_0074 = "MSJ_0074";
    //ERR-0015 = "La convocatoria no fué abierta con éxito (no hay cursos en el periodo $PERIODO$). Intente nuevamente"
    public final static String MSJ_0075 = "MSJ_0075";
    //ERR-0017 = "La convocatoria no fué cerrada con éxito (no se encontró el periodo $PERIODO$). Intente nuevamente"
    public final static String MSJ_0076 = "MSJ_0076";
    //ERR-0019 = "Los datos del curso no fueron modificados con éxito (no se encontró el curso con codigo $CODIGO_CURSO$), intente nuevamente"
    public final static String MSJ_0077 = "MSJ_0077";
    //ERR-0022 = "No se encontraron cursos registrados en el sistema"
    public final static String MSJ_0078 = "MSJ_0078";
    //ERR-0023 = "No se encontraron cursos con vacantes para monitores"
    public final static String MSJ_0079 = "MSJ_0079";
    //ERR-0025 = "No se encontraron facultades registradas en el sistema"
    public final static String MSJ_0080 = "MSJ_0080";
    //ERR-0027 = "No se encontro la facultad buscada ($NOMBRE_FACULTAD$)."
    public final static String MSJ_0081 = "MSJ_0081";
    //ERR-0028 = "No se encontraron programas en la facultad de $NOMBRE_FACULTAD$"
    public final static String MSJ_0082 = "MSJ_0082";
    //ERR-0030 = "El curso con codigo $CODIGO_CURSO$ no fue encontrado en el sistema"
    public final static String MSJ_0083 = "MSJ_0083";
    //ERR-0031 = "El curso con codigo $CODIGO_CURSO$ no tiene secciones registradas"
    public final static String MSJ_0084 = "MSJ_0084";
    //ERR-0032 = "El curso con codigo $CODIGO_CURSO$ no tiene secciones con vacantes para monitores"
    public final static String MSJ_0085 = "MSJ_0085";
    //ERR-0034 = "El profesor con login $LOGIN_PROFESOR$ no se encuentra registrado en el sistema."
    public final static String MSJ_0086 = "MSJ_0086";
    //ERR-0035 = "No se encontraron secciones para el profesor con login $LOGIN_PROFESOR$"
    public final static String MSJ_0087 = "MSJ_0087";
    //ERR-0037 = "El estudiante con codigo $CODIGO_ESTUDIANTE$ no esta en lista negra."
    public final static String MSJ_0088 = "MSJ_0088";
    //ERR-0038 = "El estudiante con login $LOGIN_ESTUDIANTE$ no esta en lista negra."
    public final static String MSJ_0089 = "MSJ_0089";
    //ERR-0042 = "No se encontraron programas"
    public final static String MSJ_0090 = "MSJ_0090";
    //ERR-0044	La preseleccion no se pudio realizar. La seccion numero $NUMERO_SECCION$ del profesor $NOMBRE_COMPLETO_PROFESOR$ ya cuenta con los monitores preseleccionados.
    public final static String MSJ_0091 = "MSJ_0091";
    //ERR-0045	El estudiante $NOMBRES_ESTUDIANTE$ $APELLIDOS_ESTUDIANTE$ excede el limite de creditos.
    public final static String MSJ_0092 = "MSJ_0092";
    //ERR-0046	El estudiante $NOMBRES_ESTUDIANTE$ $APELLIDOS_ESTUDIANTE$ presenta conflictos de horario
    public final static String MSJ_0093 = "MSJ_0093";
    //ERR-0048	El estudiante con login $NOMBRES_ESTUDIANTE$ $APELLIDOS_ESTUDIANTE$ no tiene solicitudes en el sistema.
    public final static String MSJ_0094 = "MSJ_0094";
    //ERR-0050	El usuario con login $LOGIN_USUARIO$ no existe en el sistema.
    public final static String MSJ_0095 = "MSJ_0095";
    //MSJ-0096	La solicitud ha sido modificada satisfactoriamente.
    public final static String MSJ_0096 = "MSJ_0096";
    //ERR-0051	El usuario o contraseña ingresados no son correctos. Verifique los datos e intente de nuevo.
    public final static String ERR_0051 = "ERR_0051";
    //ERR-0052	El estudiante $NOMBRES_ESTUDIANTE$ $APELLIDOS_ESTUDIANTE$ no tiene solicitudes para la seccion $NUMERO_SECCION$
    public final static String MSJ_0097 = "MSJ_0097";
    //ERR-0054  La actualizacion de la solicitud con id $ID_SOLICITUD$ se realizó exitosamente, sin embargo, la validación de los datos informa error
    public final static String MSJ_0098 = "MSJ_0098";
    //ERR-0055  No se encontraron solicitudes con estado $ESTADO$
    public final static String MSJ_0099 = "MSJ_0099";
    //ERR-0057 La consulta de historico no se pudo realizar correctamente, los periodos especificados no son validos
    public final static String MSJ_0100 = "MSJ_0100";
    //ERR-0064	La seccion con crn $CRN$ no se encuentra en el sistema
    public final static String MSJ_0101 = "MSJ_0101";
    //ERR-0067 El estudiante con codigo $CODIGO_ESTUDIANTE$ no tiene monitorías dentro la sección con CRN $CRN_SECCION$
    public final static String MSJ_0102 = "MSJ_0102";
    //ERR-0073 No se encontraron tareas para el estado dado
    public final static String MSJ_0103 = "MSJ_0103";
    //ERR-0079	El tipo de fecha a actualizar no fué encontrado
    public final static String MSJ_0104 = "MSJ_0104";
    //ERR-0077	No se encontraron alertas para el tipo de tarea dado
    public final static String MSJ_0105 = "MSJ_0105";
    //ERR-0083	No se encontró la tarea a actualizar
    public final static String MSJ_0106 = "MSJ_0106";
    //ERR-0085	No se encontraron alertas
    public final static String MSJ_0107 = "MSJ_0107";
    //MSJ-0108	La confirmación de subida de archivo fué realizada con éxito
    public final static String MSJ_0108 = "MSJ_0108";
    //MSJ-0109	La actualización de notas de los monitores fué realizada con éxito
    public final static String MSJ_0109 = "MSJ_0109";
    //MSJ-0110	El estudiante ya tiene una solicitud para la seccion con crn $CRN_SECCION$
    public final static String MSJ_0110 = "MSJ_0110";
    //MSJ-0111	El estudiante ya tiene una solicitud para el curso sin preferencia de seccion
    public final static String MSJ_0111 = "MSJ_0111";
    //MSJ-0112	La consulta de tipos de documento se realizo con exito. No se encontraron paises en el sistema
    public final static String MSJ_0112 = "MSJ_0112";
    //MSJ-0113	La consulta de tipos de documento se realizo con exito. Se encontraron paises en el sistema
    public final static String MSJ_0113 = "MSJ_0113";
    //MSJ-0114 Se ha realizado el cambio de horario.
    public final static String MSJ_0114 = "MSJ_0114";
    //MSJ-0115 Datos del curso obtenidos.
    public final static String MSJ_0115 = "MSJ_0115";
    //MSJ-116	La consulta de solicitudes después de estado fué realizada con éxito, no hay solicitudes
    public final static String MSJ_0116 = "MSJ_0116";
    //MSJ-117	La consulta de solicitudes después de estado fué realizada con éxito
    public final static String MSJ_0117 = "MSJ_0117";
    //MSJ-118 Datos de la sección obtenidos.
    public final static String MSJ_0118 = "MSJ_0118";
    //MSJ-119 Estadísticas obtenidas
    public final static String MSJ_0119 = "MSJ_0119";
    //MSJ-120 Existen conflictos con el estudiante y la sección.
    public final static String MSJ_0120 = "MSJ_0120";
    //MSJ-120 No existen conflictos con el estudiante y la sección.
    public final static String MSJ_0121 = "MSJ_0121";
    //MSJ-122	La validación de los datos saltando reglas se efectuó con éxito, validación correcta
    public final static String MSJ_0122 = "MSJ_0122";
    //MSJ-123	La validación de los datos saltando reglas se efectuó con éxito, validación incorrecta
    public final static String MSJ_0123 = "MSJ_0123";
    //MSJ-124	La carga de información de monitores se efectuó con éxito
    public final static String MSJ_0124 = "MSJ_0124";
    //MSJ-125   La actualizacion de la nota en el historico se realizo con exito
    public final static String MSJ_0125 = "MSJ_0125";
    //MSJ-126	La carga de facultades se ha realizado con éxito
    public final static String MSJ_0126 = "MSJ_0126";
    //MSJ-127	La adición del profesor se realizó con éxito
    public final static String MSJ_0127 = "MSJ_0127";
    //MSJ-128	La actualización de los datos del profesor se realizó con éxito
    public final static String MSJ_0128 = "MSJ_0128";
    //MSJ-129	La consulta de la información de los profesores se realizó con éxito
    public final static String MSJ_0129 = "MSJ_0129";
    //MSJ-147 Las alertas fueron creadas correctamente.
    public final static String MSJ_0147 = "MSJ_0147";
    //MSJ-151 La consulta ubicacion hora fecha de tesis 2 no se pudo realizar. Intente nuevamente
    public final static String MSJ_0151 = "MSJ_0151";
    //MSJ_0159 La consulta de los parámetros por tipo de tarea se realizó con éxito
    public final static String MSJ_0159 = "MSJ_0159";
    //MSJ_0160 La consulta del id de una tarea dado su tipo y parámetros se realizó con éxito
    public final static String MSJ_0160 = "MSJ_0160";
    //MSJ_0161 No se encontró el profesor a eliminar
    public final static String MSJ_0161 = "MSJ_0161";
    //MSJ_0162 El profesor fué eliminado con éxito
    public final static String MSJ_0162 = "MSJ_0162";
    //MSJ_0163 Los profesores fueron agregados con éxito
    public final static String MSJ_0163 = "MSJ_0163";
    //MSJ_0164 El grupo de investigación fue agregado con éxito
    public final static String MSJ_0164 = "MSJ_0164";
    //MSJ_0165 El grupo de investigación a agregar ya existe
    public final static String MSJ_0165 = "MSJ_0165";
    //MSJ_0166 Se agregaron con éxito los grupos de investigación
    public final static String MSJ_0166 = "MSJ_0166";
    //MSJ_0167 No se encontró el grupo de investigación a eliminar
    public final static String MSJ_0167 = "MSJ_0167";
    //MSJ_0168 El grupo de investigación fué eliminado con éxito
    public final static String MSJ_0168 = "MSJ_0168";
    //MSJ_0169 No se encontró el grupo de investigación a modificar
    public final static String MSJ_0169 = "MSJ_0169";
    //MSJ_0170 El grupo de investigación fué modificado con éxito
    public final static String MSJ_0170 = "MSJ_0170";
    //MSJ_0171 No se encontró el profesor a consultar
    public final static String MSJ_0171 = "MSJ_0171";
    //MSJ_0172 La consulta de pertenencia a grupo de investigación se realizó con éxito
    public final static String MSJ_0172 = "MSJ_0172";
    //MSJ_0173 No se encontró el grupo de investigación a consultar
    public final static String MSJ_0173 = "MSJ_0173";
    //MSJ_0174 La consulta de profesores de un grupo de investigación se realizó con éxito
    public final static String MSJ_0174 = "MSJ_0174";
    //MSJ_0175 El profesor a agregar ya existe
    public final static String MSJ_0175 = "MSJ_0175";
    //MSJ_0176 Tareas generadas exitosamente.
    public final static String MSJ_0176 = "MSJ_0176";
    //MSJ_0177 La consulta de profesores por tipo se efectuó con éxito
    public final static String MSJ_0177 = "MSJ_0177";
    //MSJ_0178 La consulta de grupso de investigación se efectuó con éxito
    public final static String MSJ_0178 = "MSJ_0178";
    //*****************************************************
    //NUEVOS MENSAJES Y ERRORES PARA BOLSA DE EMPLEO
    //*****************************************************
    //BOL_ERR_0001 El formato del archivo de estudiantes externos es inválido. Recuerde que todos los campos son obligatorios.
    public final static String BOL_ERR_0001 = "BOL_ERR_0001";
    //ERR_0100 No se encontró el estudiante
    public final static String ERR_0100 = "ERR_0100";
    //ERR_0101 La actualización de la hoja de vida no se realizó con éxito, intente nuevamente
    public final static String ERR_0101 = "ERR_0101";
    //ERR_0102 La actualización de la información académica no se realizó con éxito, intente nuevamente
    public final static String ERR_0102 = "ERR_0102";
    //ERR_0103 La actualización de la información personal no se realizó con éxito, intente nuevamente
    public final static String ERR_0103 = "ERR_0103";
    //ERR_0104 La consulta del estudiante no se realizó con éxito, intente nuevamente
    public final static String ERR_0104 = "ERR_0104";
    //ERR_0105 La consulta de la hoja de vida no se realizó con éxito, intente nuevamente
    public final static String ERR_0105 = "ERR_0105";
    //ERR_0106 La consulta de la información académica no se realizó con éxito, intente nuevamente
    public final static String ERR_0106 = "ERR_0106";
    //ERR_0107 La consulta de la información personal no se realizó con éxito, intente nuevamente
    public final static String ERR_0107 = "ERR_0107";
    //ERR_0108 La creación del estudiante no se realizó con éxito, intente nuevamente
    public final static String ERR_0108 = "ERR_0108";
    //ERR_0109 La eliminación del estudiante no se realizó con éxito, intente nuevamente
    public final static String ERR_0109 = "ERR_0109";
    //ERR_0110 No se encontró el proponente.
    public final static String ERR_0110 = "ERR_0110";
    //ERR_0111 Parámetro inválido
    public final static String ERR_0111 = "ERR_0111";
    //ERR_0112 No se encontró la oferta
    public final static String ERR_0112 = "ERR_0112";
    //ERR_0113 La actualización de la oferta no se realizó con éxito, intente nuevamente
    public final static String ERR_0113 = "ERR_0113";
    //ERR_0114 La consulta de estudiantes no se realizó con éxito, intente nuevamente
    public final static String ERR_0114 = "ERR_0114";
    //ERR_0115 La consulta de oferta no se realizó con éxito, intente nuevamente
    public final static String ERR_0115 = "ERR_0115";
    //ERR_0116 La consulta de ofertas no se realizó con éxito, intente nuevamente
    public final static String ERR_0116 = "ERR_0116";
    //ERR_0117 La consulta de ofertas por proponente no se realizó con éxito, intente nuevamente
    public final static String ERR_0117 = "ERR_0117";
    //ERR_0118 No se encontró el proponente
    public final static String ERR_0118 = "ERR_0118";
    //ERR_0119 La creación de oferta no se realizó con éxito, intente nuevamente
    public final static String ERR_0119 = "ERR_0119";
    //ERR_0120 La eliminación de oferta no se realizó con éxito, intente nuevamente
    public final static String ERR_0120 = "ERR_0120";
    //ERR_0120 Error al revertir la preselección
    public final static String ERR_0122 = "ERR_0122";
    //MSJ_0130 La actualización de la hoja de vida se realizó con éxito
    public final static String MSJ_0130 = "MSJ_0130";
    //MSJ_0131 La actualización de la información académica se realizó con éxito
    public final static String MSJ_0131 = "MSJ_0131";
    //MSJ_0132 La actualización de la información académica se realizó con éxito
    public final static String MSJ_0132 = "MSJ_0132";
    //MSJ_0133 La consulta del estudiante se realizó con éxito
    public final static String MSJ_0133 = "MSJ_0133";
    //MSJ_0134 La consulta de la hoja de vida se realizó con éxito
    public final static String MSJ_0134 = "MSJ_0134";
    //MSJ_0135 La consulta de la información académica se realizó con éxito
    public final static String MSJ_0135 = "MSJ_0135";
    //MSJ_0136 La consulta de la información personal se realizó con éxito
    public final static String MSJ_0136 = "MSJ_0136";
    //MSJ_0137 La creación del estudiante se realizó con éxito
    public final static String MSJ_0137 = "MSJ_0137";
    //MSJ_0138 La eliminación del estudiante se realizó con éxito
    public final static String MSJ_0138 = "MSJ_0138";
    //MSJ_0139 Consulta OK
    public final static String MSJ_0139 = "MSJ_0139";
    //MSJ_0140 La actualización de la oferta se realizó con éxito
    public final static String MSJ_0140 = "MSJ_0140";
    //MSJ_0141 La consulta de estudiantes se realizó con éxito
    public final static String MSJ_0141 = "MSJ_0141";
    //MSJ_0142 La consulta de oferta se realizó con éxito
    public final static String MSJ_0142 = "MSJ_0142";
    //MSJ_0143 La consulta de ofertas se realizó con éxito
    public final static String MSJ_0143 = "MSJ_0143";
    //MSJ_0144 La consulta de ofertas por proponente se realizó con éxito
    public final static String MSJ_0144 = "MSJ_0144";
    //MSJ_0145 La creación de oferta se realizó con éxito
    public final static String MSJ_0145 = "MSJ_0145";
    //MSJ_0146 La eliminación de oferta se realizó con éxito
    public final static String MSJ_0146 = "MSJ_0146";
    //MSJ_0148  Se ha revertido correctamente la preselección del estudiante.
    public final static String MSJ_0148 = "MSJ_0148";
    //MSJ_0149 OK Alerta borrada
    public final static String MSJ_0149 = "MSJ_0149";
    //MSJ_0150 OK Alerta borrada
    public final static String MSJ_0150 = "MSJ_0150";
    //BOLSA_EMPLEO_MSJ_0001 La aplicacion fué enviada exitósamente.
    public final static String BOLSA_EMPLEO_MSJ_0001 = "BOLSA_EMPLEO_MSJ_0001";
    //BOLSA_EMPLEO_MSJ_0002 La aplicación no fué enviada con éxito, actualice la hoja de vida primero.
    public final static String BOLSA_EMPLEO_MSJ_0002 = "BOLSA_EMPLEO_MSJ_0002";
    //BOLSA_EMPLEO_MSJ_0003 La aplicación no fué enviada con éxito, intente nuevamente.
    public final static String BOLSA_EMPLEO_MSJ_0003 = "BOLSA_EMPLEO_MSJ_0003";
    //*****************************************************
    //NUEVOS MENSAJES Y ERRORES PARA DOCUMENTOS PRIVADOS
    //*****************************************************
    //ERR_0124 = "La subida de los metadatos del documento privado no se pudo realizar. Verifique los datos e intente nuevamente."
    public final static String ERR_0124 = "ERR_0124";
    //ERR_0125 = "El documento con el identificador proporcionado no existe. Verifique los datos e intente de nuevo."
    public final static String ERR_0125 = "ERR_0125";
    //ERR_0150 = "El nodo con el identificador proporcionado no existe. Verifique los datos e intente de nuevo."
    public final static String ERR_0150 = "ERR_0150";
    //ERR_0151 = "El nodo con el identificador proporcionado no existe. Verifique los datos e intente de nuevo."
    public final static String ERR_0151 = "ERR_0151";
    //MSJ_0152 = "La subida de los metadatos del documento privado se realizó con éxito."
    public final static String MSJ_0152 = "MSJ_0152";
    //MSJ_0153 = "La actualización de los metadatos del documento privado se realizó con éxito."
    public final static String MSJ_0153 = "MSJ_0153";
    //MSJ_0154 = "La confirmación de la subida del documento privado se realizó con éxito."
    public final static String MSJ_0154 = "MSJ_0154";
    //MSJ_0155 = "La consulta de los datos de un documento privado se realizó con éxito."
    public final static String MSJ_0155 = "MSJ_0155";
    //MSJ_0156 = "La consulta de información de descarga de un documento privado se realizó con éxito."
    public final static String MSJ_0156 = "MSJ_0156";
    //MSJ_0157 = "La eliminación del documento privado se realizó con éxito."
    public final static String MSJ_0157 = "MSJ_0157";
    //MSJ_0158 = "La consulta de todos los documentos privados se realizó con éxito."
    public final static String MSJ_0158 = "MSJ_0158";
    //*****************************************************
    //MENSAJES Y ERRORES PARA MATERIAL BIBLIOGRAFICO
    //*****************************************************
    //MSJ_0179 = "La creación de la solicitud no se efectuó con éxito, el correo del profesor es inválido."
    public final static String MSJ_0179 = "MSJ_0179";
    //MSJ_0179 = "La creación de la solicitud no se efectuó con éxito, el profesor no es de planta."
    public final static String MSJ_0180 = "MSJ_0180";
    //MSJ_0179 = "La creación de la solicitud se efectuó con éxito."
    public final static String MSJ_0181 = "MSJ_0181";
    //MSJ_0182 "La autorización de la solicitud no fué efectuada con éxito, no se encontró la tarea asociada."
    public final static String MSJ_0182 = "MSJ_0182";
    //MSJ_0183 "La autorización de la solicitud fué efectuada con éxito."
    public final static String MSJ_0183 = "MSJ_0183";
    //MSJ_0184 "La confirmación de la compra no fué efectuada con éxito, no se encontró la tarea asociada."
    public final static String MSJ_0184 = "MSJ_0184";
    //MSJ_0185 "La conformación de la compra fué efectuada con éxito."
    public final static String MSJ_0185 = "MSJ_0185";
    //MSJ_0186 "La consulta de solicitudes por rango de precios fué efectuada con éxito."
    public final static String MSJ_0186 = "MSJ_0186";
    //MSJ_0187 "La consulta de solicitudes por proveedor fué efectuada con éxito."
    public final static String MSJ_0187 = "MSJ_0187";
    //MSJ_0187 "La consulta de solicitudes por año de publicación fué efectuada con éxito."
    public final static String MSJ_0188 = "MSJ_0188";
    //MSJ_0189 "La consulta de solicitudes por rango de fechas fué efectuada con éxito."
    public final static String MSJ_0189 = "MSJ_0189";
    //MSJ_0190 "La consulta de solicitudes por estado fué efectuada con éxito."
    public final static String MSJ_0190 = "MSJ_0190";
    //MSJ_0191 "La consulta de costo total de solicitudes por rango de fecha fué efectuada con éxito."
    public final static String MSJ_0191 = "MSJ_0191";
    //MSJ_0192 "La consulta de costo promedio de solicitudes por rango de fecha fué efectuada con éxito."
    public final static String MSJ_0192 = "MSJ_0192";
    //MSJ_0193 "La consulta de la monitoria dada una solicitud fue efectuada con éxito."
    public final static String MSJ_0193 = "MSJ_0193";
    //MSJ_0194 "Se determinó correctamente si el estudiante tiene solicitudes."
    //MSJ_195 La consulta de grupso de investigación se efectuó con éxito
    public final static String MSJ_0195 = "MSJ_0195";
    //ERR_0031 = "La creación de la solicitud no se efectuó con éxito, intente nuevamente."
    public final static String ERR_0031 = "ERR_0031";
    //ERR_0032 "La autorización de la solicitud no fué efectuada con éxito, intente nuevamente."
    public final static String ERR_0032 = "ERR_0032";
    //ERR_0034 "La confirmación de la compra no fué efectuada con éxito, intente nuevamente."
    public final static String ERR_0034 = "ERR_0034";
    //ERR_0035 "La consulta de solicitudes por rango de precios no fué efectuada con éxito."
    public final static String ERR_0035 = "ERR_0035";
    //ERR_0037 "La consulta de solicitudes por proveedor no fué efectuada con éxito."
    public final static String ERR_0037 = "ERR_0037";
    //ERR_0038 "La consulta de solicitudes por año de publicación no fué efectuada con éxito."
    public final static String ERR_0038 = "ERR_0038";
    //ERR_PUB_0001 "La consulta de Publicaciones por correo no fue efectuada con éxito."
    public final static String PUB_ERR_0001 = "PUB_ERR_0001";
    //ERR_PUB_0002 "La consulta de la publicación por el Id externo de Academia no fue efectuada con éxito."
    public final static String PUB_ERR_0002 = "PUB_ERR_0002";
    //ERR_PUB_0003 "La consulta de la publicación por año y correo no se realizó con éxito."
    public final static String PUB_ERR_0003 = "PUB_ERR_0003";
    //ERR_PUB_0004 "La publicación no fue modificada exitosamente."
    public final static String PUB_ERR_0004 = "PUB_ERR_0004";
    //ERR_0042 "La consulta de solicitudes por rango de fechas no fué efectuada con éxito."
    public final static String ERR_0042 = "ERR_0042";
    //ERR_0044 "La consulta de solicitudes por estado no fué efectuada con éxito."
    public final static String ERR_0044 = "ERR_0044";
    //ERR_0045 "La consulta de costo total de solicitudes por rango de fecha no fué efectuada con éxito."
    public final static String ERR_0045 = "ERR_0045";
    //ERR_0046 "La consulta de costo promedio de solicitudes por rango de fecha no fué efectuada con éxito."
    public final static String ERR_0046 = "ERR_0046";
    //El material bibliográfico  no ha sido pedido a biblioteca
    public final static String ERR_MB_0040 = "ERR_MB_0040";
    //El estado no se pudo actualizar por favor intente de nuevo
    public final static String ERR_MB_0039 = "ERR_MB_0039";
    //El número de ejemplares solicitado es mayor al número maximo de ejemplares permitidos por biblioteca
    public final static String ERR_MB_0041 = "ERR_MB_0041";
    //Errores que faltan:
    //37,38,42,44,45,46,48,50,51,52,54,55,57,64,67,73
    //******************************************************
    //Comienzo
    //Votaciones - VotacionBean
    //*****************************************************
    //..........................MENSAJES.............................//
    public final static String VOT_MSJ_0001 = "VOT_MSJ_0001";
    public final static String VOT_MSJ_0002 = "VOT_MSJ_0002";
    public final static String VOT_MSJ_0003 = "VOT_MSJ_0003";
    public final static String VOT_MSJ_0004 = "VOT_MSJ_0004";
    public final static String VOT_MSJ_0005 = "VOT_MSJ_0005";
    public final static String VOT_MSJ_0006 = "VOT_MSJ_0006";
    public final static String VOT_MSJ_0007 = "VOT_MSJ_0007";
    public final static String VOT_MSJ_0008 = "VOT_MSJ_0008";
    public final static String VOT_MSJ_0009 = "VOT_MSJ_0009";
    public final static String VOT_MSJ_0010 = "VOT_MSJ_0010";
    public final static String VOT_MSJ_0011 = "VOT_MSJ_0011";
    public final static String VOT_MSJ_0012 = "VOT_MSJ_0012";
    //..........................ERRORES.............................//
    public final static String VOT_ERR_0001 = "VOT_ERR_0001";
    public final static String VOT_ERR_0002 = "VOT_ERR_0002";
    public final static String VOT_ERR_0003 = "VOT_ERR_0003";
    public final static String VOT_ERR_0004 = "VOT_ERR_0004";
    public final static String VOT_ERR_0005 = "VOT_ERR_0005";
    public final static String VOT_ERR_0006 = "VOT_ERR_0006";
    public final static String VOT_ERR_0007 = "VOT_ERR_0007";
    public final static String VOT_ERR_0008 = "VOT_ERR_0008";
    public final static String VOT_ERR_0009 = "VOT_ERR_0009";
    public final static String VOT_ERR_0010 = "VOT_ERR_0010";
    //La fecha maxima para votar ya paso.
    public final static String VOT_ERR_0011 = "VOT_ERR_0011";
    //******************************************************
    //Fin
    //Votaciones - VotacionBean
    //*****************************************************
    //******************************************************
    //Inscripciones genericas
    //*****************************************************
    //ERR_INS0048 "La inscripción no fue editada con éxito. La inscripción ya se encuentra cerrada."
    public final static String ERR_INS0048 = "ERR_INS0048";
    //ERR_INS0049 "El enlace de confirmación de la inscripción no es válido. La fecha limite para realizar la inscripcion fue el {0}"
    public final static String ERR_INS0049 = "ERR_INS0049";
    //ERR_INS0046 "La inscripción no fue eliminada con éxito. La inscripción debe cerrarse antes de ser eliminada."
    public final static String ERR_INS00123 = "ERR_INS00123";
    //ERR_INS00124 "La inscripción no fue editada con éxito. La inscripción no fue encontrada."
    public final static String ERR_INS00124 = "ERR_INS00124";
    //ERR_INS00125 "La inscripción no fue cerrada con éxito. La inscripción no fue encontrada."
    public final static String ERR_INS00125 = "ERR_INS00125";
    //ERR_INS00126 "La inscripción no fue eliminada con éxito. La inscripción no fue encontrada."
    public final static String ERR_INS00126 = "ERR_INS00126";
    //ERR_INS00127 "La inscripción no fue encontrada."
    public final static String ERR_INS00127 = "ERR_INS00127";
    //******************************************************
    //Fin
    //Inscripciones genericas
    //*****************************************************
    //******************************************************
    //Contactos CRM
    //*****************************************************
    public final static String CRM_ERR_0001 = "CRM_ERR_0001";
    //ya existe otro contacto con el mismo correo
    public final static String CRM_ERR_0002 = "CRM_ERR_0002";
    //El usuario no ha sido activado aun. Por favor revise su correo para obtener el link de activación. Usted puede solicitar que se envie nuevamente el link de activación seleccionando la opción Olvido Contraeña
    public final static String CRM_ERR_0003 = "CRM_ERR_0003";
    //El usuario ya fue activado o la direccion es incorrecta
    public final static String CRM_ERR_0004 = "CRM_ERR_0004";
    //El usuario ya se encuentra registrado o  fue habilitado previamente.
    public final static String CRM_ERR_0005 = "CRM_ERR_0005";
    //El usuario ya se encuentra inscrito en el evento.
    public final static String CRM_ERR_0006 = "CRM_ERR_0006";
    //El evento especificado no existe
    public final static String CRM_ERR_0007 = "CRM_ERR_0007";
    //Error realizando la inscripción. El evento seleccionado no tiene cupos disponibles.
    public final static String CRM_ERR_0008 = "CRM_ERR_0008";
    //Error realizando la inscripción. Las inscripciones para el evento seleccionado ya se encuentran cerradas.
    public final static String CRM_ERR_0009 = "CRM_ERR_0009";
    //El contacto con correo {0} ya se encuentra inscrito para el evento seleccionado.
    public final static String CRM_ERR_0010 = "CRM_ERR_0010";
    //El enlace está incorrecto o no corresponde a un usuario válido
    public final static String CRM_ERR_0011 = "CRM_ERR_0011";
    //El contacto especificado no se encuentra inscrito al evento.
    public final static String CRM_ERR_0012 = "CRM_ERR_0012";
    //******************************************************
    //Fin
    //Contactos Crm
    //*****************************************************
    //******************************************************
    //CArga y Compromisos
    //TODO:por ahora no estan mapeados a ningun lado...
    //*****************************************************
    //se inicio el proceso correctamente
    public final static String CYC_MSJ_0001 = "CYC_MSJ_0001";
    //resp ok;
    public final static String CYC_MSJ_0000 = "CYC_MSJ_0000";
    //El periodo no existe
    public final static String CYC_ERR_0001 = "CYC_ERR_0001";
    //El profesor no existe
    public final static String CYC_ERR_0002 = "CYC_ERR_0002";
    //******************************************************
    //Fin
    //CArga y Compromisos
    //*****************************************************
    //******************************************************
    //Parametros mensajes
    //*****************************************************
    //..........................VALORES ATRIBUTOS.............................//
    public final static String VAL_ATR_PARAMETRO_CODIGO_ESTUDIANTE = "$CODIGO_ESTUDIANTE$";
    public final static String VAL_ATR_PARAMETRO_ID_SOLICITUD = "$ID_SOLICITUD$";
    public final static String VAL_ATR_PARAMETRO_PERIODO = "$PERIODO$";
    public final static String VAL_ATR_PARAMETRO_LOGIN_ESTUDIANTE = "$LOGIN_ESTUDIANTE$";
    public final static String VAL_ATR_PARAMETRO_CODIGO_CURSO = "$CODIGO_CURSO$";
    public final static String VAL_ATR_PARAMETRO_NOMBRE_FACULTAD = "$NOMBRE_FACULTAD$";
    public final static String VAL_ATR_PARAMETRO_LOGIN_PROFESOR = "$LOGIN_PROFESOR$";
    public final static String VAL_ATR_PARAMETRO_NUMERO_SECCION = "$NUMERO_SECCION$";
    public final static String VAL_ATR_PARAMETRO_NOMBRE_COMPLETO_PROFESOR = "$NOMBRE_COMPLETO_PROFESOR$";
    public final static String VAL_ATR_PARAMETRO_NOMBRES_ESTUDIANTE = "$NOMBRES_ESTUDIANTE$";
    public final static String VAL_ATR_PARAMETRO_APELLIDOS_ESTUDIANTE = "$APELLIDOS_ESTUDIANTE$";
    public final static String VAL_ATR_PARAMETRO_NOMBRE_CURSO = "$NOMBRE_CURSO$";
    public final static String VAL_ATR_PARAMETRO_LOGIN_USUARIO = "$LOGIN_USUARIO$";
    public final static String VAL_ATR_PARAMETRO_CRN_SECCION = "$CRN_SECCION$";
    public final static String VAL_ATR_PARAMETRO_ESTADO_SOLICITUD = "$ESTADO_SOLICITUD$";
    //ATTRIBUTOS TESIS
    public final static String VAL_ATR_PARAMETRO_MAX_FECHA = "$MAX_FECHA$";
    //El numero de Cursos de la Solicitud no es el esperado
    public final static String TESIS_ERR_0002 = "TESIS_ERR_0002";
    //El Profesor no existe
    public final static String TESIS_ERR_0003 = "TESIS_ERR_0003";
    //el Estudiante no a sido aprobado en ninguna subarea de investigacion
    public final static String TESIS_ERR_0004 = "TESIS_ERR_0004";
    //El grupo de investigacion no coincide con el de la subarea de investigacion
    public final static String TESIS_ERR_0005 = "TESIS_ERR_0005";
    //el asesor de tesis no coincide con el de la inscripcion a subarea de investigacion
    public final static String TESIS_ERR_0006 = "TESIS_ERR_0006";
    //la tiene permisos para aprobar la tesis
    public final static String TESIS_ERR_0007 = "TESIS_ERR_0007";
    //no se encuentra la tesis con id indicado
    public final static String TESIS_ERR_0008 = "TESIS_ERR_0008";
    //no se puede subir la nota pues no se encuentra el archivo requerido(sin archivo no hay nota)
    public final static String TESIS_ERR_0009 = "TESIS_ERR_0009";
    //el asesor inscrito en tesis 1 no es el mismo de tesis 2
    public final static String TESIS_ERR_00010 = "TESIS_ERR_00010";
    //no se puede inscribir tesis 2 debido a que el estudiante no ha visto tesis 1 o no la vio en el periodo anterior
    public final static String TESIS_ERR_00011 = "TESIS_ERR_00011";
    //no se puede agregar jurado por que la tesis no se encuentra en curso
    public final static String TESIS_ERR_00012 = "TESIS_ERR_00012";
    //El profesor seleccionado no puede ser asesor de tesis en maestria
    public final static String TESIS_ERR_00013 = "TESIS_ERR_00013";
    //la fecha de inicio ya paso
    public final static String TESIS_ERR_00014 = "TESIS_ERR_00014";
    //Todavia no se puede inscribir tesis para el periodo seleccionado
    public final static String TESIS_ERR_00015 = "TESIS_ERR_00015";
    //Ya existe otra tesis en proceso para el mismo periodo
    public final static String TESIS_ERR_00016 = "TESIS_ERR_00016";
    //La Fecha maxima para dicha accion ya paso
    public final static String TESIS_ERR_00017 = "TESIS_ERR_00017";
    //Nota no valida
    public final static String TESIS_ERR_00018 = "TESIS_ERR_00018";
    //la fecha de terminacion de tesis 1 no esta permitida (no esta dentro de rango maximo)
    public final static String TESIS_ERR_00020 = "TESIS_ERR_00020";
    //no se encuentra el archivo de inicio de tesis 2
    public final static String TESIS_ERR_00021 = "TESIS_ERR_00021";
    //no se puede subir la nota de tesis 2 hasta que el asesor no entregue el articulo final de tesis 2 del estudiante
    public final static String TESIS_ERR_00022 = "TESIS_ERR_00022";
    //no se encuentra la tesis 2 buscada
    public final static String TESIS_ERR_00023 = "TESIS_ERR_00023";
    //no se han configurado las fechas para Tesis 1, en el período seleccionado
    public final static String TESIS_ERR_00024 = "TESIS_ERR_00024";
    //no se han configurado las fechas para Tesis 2, en el período seleccionado
    public final static String TESIS_ERR_00025 = "TESIS_ERR_00025";
    //no se puede eliminar el tema de tesis, debido a que ya existen tesis con este tema
    public final static String TESIS_ERR_00026 = "TESIS_ERR_00026";
    //la fecha de terminacion de tesis 2 no esta permitida (no esta dentro de rango mínimo[ultimo dia solicitar tesis 2] y máximo[último día sustentar tesis 2])
    public final static String TESIS_ERR_00027 = "TESIS_ERR_00027";
    //Usted ya no puede registrar una solicitud de inscripción a subarea porque el plazo máximo se venció. (Fecha límite: {0})
    public final static String TESIS_ERR_00028 = "TESIS_ERR_00028";
    //La fecha de sustentación supera la fecha máxima para sustentar
    public final static String TESIS_ERR_00029 = "TESIS_ERR_00029";
    //Tesis 2 debe cursarse un semestre posterior a Tesis 1
    public final static String TESIS_ERR_00030 = "TESIS_ERR_00030";
    //no se puede solicitar el pendiente para tesis 1 pues no se encuentra el archivo requerido
    public final static String TESIS_ERR_00031 = "TESIS_ERR_00031";
    //No se puede cambiar el estado de la tesis a pendiente, porque no se ha hecho la solicitud
    public final static String TESIS_ERR_00032 = "TESIS_ERR_00032";
    //Sólo el estudiante que cursa la tesis, la puede retirar
    public final static String TESIS_ERR_00033 = "TESIS_ERR_00033";
    //En el estado actual de la tesis no se puede realizar la accion solicitada
    public final static String TESIS_ERR_00034 = "TESIS_ERR_00034";
    //El Archivo de Afiche no se cargó correctamente
    public final static String TESIS_ERR_00035 = "TESIS_ERR_00035";
    //La fecha de solicitud de pendiente especial para tesis 2 ya pasó
    public final static String TESIS_ERR_00038 = "TESIS_ERR_00038";
    //El archivo con la documentación de pendiente especial para tesis 2 no fue encontrado
    public final static String TESIS_ERR_00039 = "TESIS_ERR_00039";
    //SE EXEDIO EL MAXIMO DE COASESORES
    public final static String TESIS_ERR_00037 = "TESIS_ERR_00037";
    //se debe adjuntar un archivo con la carta y pruebas del por que se necesita el pendiente especial
    public final static String TESIS_ERR_00036 = "TESIS_ERR_00036";
    //ya existen calificaciones para esta tesis, por lo que no se pueden eliminar
    public final static String TESIS_ERR_00040 = "TESIS_ERR_00040";
    //No se tienen aun jurados ni horarios asignados, no se lanzo el comportamiento de emergencia
    public final static String TESIS_ERR_00042 = "TESIS_ERR_00042";
    //No se puede reprobar una tesis que ya finalizo
    public final static String TESIS_ERR_00041 = "TESIS_ERR_00041";
    public final static String TESIS_ERR_00043 = "TESIS_ERR_00043";
    //Tesis 1 debe ser cursada en el periodo estrictamente anterior a Tesis 2
    public final static String TESIS_ERR_00044 = "TESIS_ERR_00044";
    //Tesis 1 debe ser cursada en el periodo estrictamente anterior a Tesis 2
    public final static String TESIS_ERR_00045 = "TESIS_ERR_00045";
    //La tesis no tiene un artículo asociado que pueda ser enviado a la Revista Paradigma.
    public final static String TESIS_ERR_00046 = "TESIS_ERR_00046";
    //Seccion de Errores para la reserva de citas con la coordinacion de sistemas
    //La reserva no se efectuó con éxito, la duración de la cita debe ser de máximo 20 minutos
    public final static String RESERVA_ERR_001 = "RESERVA_ERR_001";
    //La reserva no se efectuó con éxito, La Coordinación no se encuentra disponible para la fecha dada
    public final static String RESERVA_ERR_002 = "RESERVA_ERR_002";
    //La reserva no se efectuó con éxito, ya existe una reserva para la fecha dada
    public final static String RESERVA_ERR_003 = "RESERVA_ERR_003";
    //La reserva no se efectuó con éxito, la fecha de reservación ya pasó
    public final static String RESERVA_ERR_004 = "RESERVA_ERR_004";
    //La reserva no se efectuó con éxito, no se puede reservar para el mismo día. Mínimo 1 día antes de anticipación, antes del medio día.
    public final static String RESERVA_ERR_005 = "RESERVA_ERR_005";
    //La reserva no se efectuó con éxito, se deben realizar las reservas con mínimo 1 día antes de anticipación, antes del medio día
    public final static String RESERVA_ERR_006 = "RESERVA_ERR_006";
    //La operación no se pudo efectuar con éxito. La reserva no fue encontrada.
    public final static String RESERVA_ERR_007 = "RESERVA_ERR_007";
    //La reserva no se realizó con éxito. Ya existe una reserva activa para este semestre.
    public final static String RESERVA_ERR_008 = "RESERVA_ERR_008";
    // No se pudo cancelar la reserva. La cancelación debe realizarse almenos un día antes de la fecha de reserva. Comuníquese directamente con La Coordinación para cancelar.
    public final static String RESERVA_ERR_009 = "RESERVA_ERR_009";
    //  El horario escogido no es valido, la fecha de vencimiento debe ser posterior a la actual
    public final static String RESERVA_ERR_010 = "RESERVA_ERR_010";
    // El estudiante especificado no fue encontrado en lista negra
    public final static String RESERVA_ERR_011 = "RESERVA_ERR_011";
    // La reserva no fue realizada debido a que el estudiante se encuentra en lista negra
    public final static String RESERVA_ERR_012 = "RESERVA_ERR_012";
    /**
     * Conflicto de horarios
     */
    //La petición fue creada exitósamente.
    public final static String CONFLICTOS_MSJ_001 = "CONFLICTOS_MSJ_001";
    //La consulta de materias fue realizada exitósamente.
    public final static String CONFLICTOS_MSJ_002 = "CONFLICTOS_MSJ_002";
    //La consulta de secciones fue realizada exitósamente.
    public final static String CONFLICTOS_MSJ_003 = "CONFLICTOS_MSJ_003";
    //La consulta de programas fue realizada exitósamente.
    public final static String CONFLICTOS_MSJ_004 = "CONFLICTOS_MSJ_004";
    //La consulta de peticiones por estudiante fue realizada exitósamente.
    public final static String CONFLICTOS_MSJ_005 = "CONFLICTOS_MSJ_005";
    //La petición fue cancelada exitósamente.
    public final static String CONFLICTOS_MSJ_006 = "CONFLICTOS_MSJ_006";
    //La consulta de peticiones fue realizada exitósamente.
    public final static String CONFLICTOS_MSJ_007 = "CONFLICTOS_MSJ_007";
    //La consulta de peticiones por materia fue realizada exitósamente.
    public final static String CONFLICTOS_MSJ_008 = "CONFLICTOS_MSJ_008";
    //La actualización de peticiones fue realizada exitósamente.
    public final static String CONFLICTOS_MSJ_009 = "CONFLICTOS_MSJ_009";
    //La sección fue creada exitósamente.
    public final static String CONFLICTOS_MSJ_010 = "CONFLICTOS_MSJ_010";
    //La sección fue actualizada exitósamente.
    public final static String CONFLICTOS_MSJ_011 = "CONFLICTOS_MSJ_011";
    //La consulta de fechas fue realizada exitósamente.
    public final static String CONFLICTOS_MSJ_012 = "CONFLICTOS_MSJ_012";
    //Las fechas se han configurado exitósamente.
    public final static String CONFLICTOS_MSJ_013 = "CONFLICTOS_MSJ_013";
    //La cartelera de conflicto de horario se ha cargado exitósamente.
    public final static String CONFLICTOS_MSJ_014 = "CONFLICTOS_MSJ_014";
    //La peticion no fue creada. La sección solicitada no se encuentra activa.
    public final static String CONFLICTOS_ERR_001 = "CONFLICTOS_ERR_001";
    //La peticion no fue creada. El estudiante ya tiene una petición para la materia solicitada.
    public final static String CONFLICTOS_ERR_002 = "CONFLICTOS_ERR_002";
    //La sección no fue creada con éxito. La sección ya existe.
    public final static String CONFLICTOS_ERR_003 = "CONFLICTOS_ERR_003";
    //La peticion no fue creada. La petición se encuentra fuera de las fechas aptas para petición de conflicto.
    public final static String CONFLICTOS_ERR_004 = "CONFLICTOS_ERR_004";
    //La cartelera de conflicto de horario no se cargo exitósamente. Revise el formato del archivo e intente nuevamente.
    public final static String CONFLICTOS_ERR_005 = "CONFLICTOS_ERR_005";
    //La cancelación de la petición no fue realizada con éxito. La petición ya fue resuelta.
    public final static String CONFLICTOS_ERR_006 = "CONFLICTOS_ERR_006";
    //No se ha cargado ninguna cartelera de cursos en el sistema. Por favor cargue primero un archivo de cartelera y luego proceda a configurar las fechas.
    public final static String CONFLICTOS_ERR_007 = "CONFLICTOS_ERR_007";
    //La consulta de secciones no se realizó con éxito. No existe curso para el código dado.
    public final static String CONFLICTOS_ERR_008 = "CONFLICTOS_ERR_008";
    //La petición no fue creada con éxito. Intente nuevamente.
    public final static String CONFLICTOS_ERR_009 = "CONFLICTOS_ERR_009";
    //La consulta de materias no fue realizada exitósamente. Intente nuevamente.
    public final static String CONFLICTOS_ERR_010 = "CONFLICTOS_ERR_010";
    //La consulta de secciones no se realizo con éxito. Intente nuevamente.
    public final static String CONFLICTOS_ERR_011 = "CONFLICTOS_ERR_011";
    //La consulta de programas no se realizo con éxito. Intente nuevamente.
    public final static String CONFLICTOS_ERR_012 = "CONFLICTOS_ERR_012";
    //La consulta de peticiones por estudiante no se realizo con éxito. Intente nuevamente.
    public final static String CONFLICTOS_ERR_013 = "CONFLICTOS_ERR_013";
    //La cancelación de la petición no se realizo con éxito. Intente nuevamente.
    public final static String CONFLICTOS_ERR_014 = "CONFLICTOS_ERR_014";
    //La consulta de peticiones no se realizo con éxito. Intente nuevamente.
    public final static String CONFLICTOS_ERR_015 = "CONFLICTOS_ERR_015";
    //La consulta de peticiones por materia no se realizo con éxito. Intente nuevamente.
    public final static String CONFLICTOS_ERR_016 = "CONFLICTOS_ERR_016";
    //La actualización del estado de las peticiones no fue realizada con éxito. Alguna(s) de las peticiones no fueron encontradas.
    public final static String CONFLICTOS_ERR_017 = "CONFLICTOS_ERR_017";
    //La actualización del estado de las peticiones no fue realizada con éxito. Intente nuevamente.
    public final static String CONFLICTOS_ERR_018 = "CONFLICTOS_ERR_018";
    //La sección no fue creada con éxito. Intente nuevamente.
    public final static String CONFLICTOS_ERR_019 = "CONFLICTOS_ERR_019";
    //La actualización de la seccion no fue realizada con éxito. La sección no fue encontrada.
    public final static String CONFLICTOS_ERR_020 = "CONFLICTOS_ERR_020";
    //La actualización de la seccion no fue realizada con éxito. Intente nuevamente.
    public final static String CONFLICTOS_ERR_021 = "CONFLICTOS_ERR_021";
    //La consulta de fechas no fue realizada exitósamente. Intente nuevamente.
    public final static String CONFLICTOS_ERR_022 = "CONFLICTOS_ERR_022";
    //Las fechas no se han configurado exitósamente. Intente nuevamente.
    public final static String CONFLICTOS_ERR_023 = "CONFLICTOS_ERR_023";
    //La petición no fue creada con éxito. Debe ingresar el curso y sección de destino.
    public final static String CONFLICTOS_ERR_024 = "CONFLICTOS_ERR_024";
    //La petición no fue creada con éxito. Debe ingresar el curso y sección de origen.
    public final static String CONFLICTOS_ERR_025 = "CONFLICTOS_ERR_025";
    //La sección no fue creada con éxito. El curso no existe.
    public final static String CONFLICTOS_ERR_026 = "CONFLICTOS_ERR_026";
    //La sección no fue actualizada con éxito. La sección no existe.
    public final static String CONFLICTOS_ERR_027 = "CONFLICTOS_ERR_027";
    //No se pudo realizar la operación con éxito. Las fechas de conflicto no han sido configuradas.
    public final static String CONFLICTOS_ERR_028 = "CONFLICTOS_ERR_028";
    //El tipo de solicitud es inválido. Verifique la información suministrada
    public final static String CONFLICTOS_ERR_029 = "CONFLICTOS_ERR_029";
    //La cartelera de conflicto de horario no se cargo exitósamente. La ruta es inválida.
    public final static String CONFLICTOS_ERR_030 = "CONFLICTOS_ERR_030";
    //No se encontró la sección asociada el número dado. Revise el número de la sección.
    public final static String CONFLICTOS_ERR_031 = "CONFLICTOS_ERR_031";
    //Ya existe una convotatoria abierta
    public final static String CONFLICTOS_ERR_032 = "CONFLICTOS_ERR_032";
    // La modificación no puede llevarse acabo por que el numero de monitores nuevo es inferior al actual
    public final static String CONFLICTOS_ERR_033 = "CONFLICTOS_ERR_033";
    //No fue posible crear el curso. Ya existe un curso con el código ingresado
    public final static String CONFLICTOS_ERR_034 = "CONFLICTOS_ERR_034";
    //El curso no fue creado con éxito. Intente nuevamente
    public final static String CONFLICTOS_ERR_035 = "CONFLICTOS_ERR_035";
    //
    /**
     * mensajes de proyecto de Grado
     */
    //Lo sentiomos la fecha para esta accion ya paso
    public final static String TESIS_PREGRADO_ERR_0001 = "TESIS_PREGRADO_ERR_0001";
    //el tema seleccionado no existe
    public final static String TESIS_PREGRADO_ERR_0002 = "TESIS_PREGRADO_ERR_0002";
    //el estudiante ya tiene otra tesis para ese periodo
    public final static String TESIS_PREGRADO_ERR_0003 = "TESIS_PREGRADO_ERR_0003";
    //no hay acciones disponibles para el periodo seleccionado
    public final static String TESIS_PREGRADO_ERR_0004 = "TESIS_PREGRADO_ERR_0004";
    //accion no disponible para estudiatnes de posgrado
    public final static String TESIS_PREGRADO_ERR_0005 = "TESIS_PREGRADO_ERR_0005";
    //accion no disponible
    public final static String TESIS_PREGRADO_ERR_0006 = "TESIS_PREGRADO_ERR_0006";
    //
    public final static String TESIS_PREGRADO_ERR_0007 = "TESIS_PREGRADO_ERR_0007";
    //No se encuentra el proyecto de grado asociado al id dado
    public final static String TESIS_PREGRADO_ERR_0008 = "TESIS_PREGRADO_ERR_0008";
    //La acción no pudo ser realizada. Revise las fechas y el estado de su proyecto de grado para continuar.
    public final static String TESIS_PREGRADO_ERR_0009 = "TESIS_PREGRADO_ERR_0009";
    //Estado no válido de tesis de pregrado para subir afiche
    public final static String TESIS_PREGRADO_ERR_0010 = "TESIS_PREGRADO_ERR_0010";
    //La fecha máxima para aprobar o rechazar afiches ya pasó, comuníquese con la coordinación del departamento para tratar este caso.
    public final static String TESIS_PREGRADO_ERR_0011 = "TESIS_PREGRADO_ERR_0011";
    // No se puede reprobar un proyecto de grado que ya finalizo.
    public final static String TESIS_PREGRADO_ERR_0012 = "TESIS_PREGRADO_ERR_0012";
    // No se encuentra el estudiante con el login dado
    public final static String TESIS_PREGRADO_ERR_0013 = "TESIS_PREGRADO_ERR_0013";
    // La acción no pudo ser realizada. El archivo no fue almacenado con éxito.
    public final static String TESIS_PREGRADO_ERR_0014 = "TESIS_PREGRADO_ERR_0014";
    // No se pudo agregar el tema de proyecto de grado, debido a que las fechas para el periodo aun no han sido configuradas
    public final static String TESIS_PREGRADO_ERR_0015 = "TESIS_PREGRADO_ERR_0015";
    /*
     * Reservas e inventario
     */
    //su permiso se ha vencido
    public final static String RESERVAS_E_INVENTARIO_ERR_0001 = "RESERVAS_E_INVENTARIO_ERR_0001";
    //ud no tiene autorizado reservar equipos en este laboratorio
    public final static String RESERVAS_E_INVENTARIO_ERR_0000 = "RESERVAS_E_INVENTARIO_ERR_0000";
//lo sentimos ya hay otra reserva en esas horas
    public final static String RESERVAS_E_INVENTARIO_ERR_0002 = "RESERVAS_E_INVENTARIO_ERR_0002";
    // lo sentimos el elementoque ud desea reservar se encuentra fuera de circulacion
    public final static String RESERVAS_E_INVENTARIO_ERR_0003 = "RESERVAS_E_INVENTARIO_ERR_0003";
    //las resercas no pueden ser de mas de un dia
    public final static String RESERVAS_E_INVENTARIO_ERR_0004 = "RESERVAS_E_INVENTARIO_ERR_0004";
    //la hora de la reserva no es valida
    public final static String RESERVAS_E_INVENTARIO_ERR_0005 = "RESERVAS_E_INVENTARIO_ERR_0005";
    //la reserva no se puede eliminar por que esta en curso (ya fue usada)
    public final static String RESERVAS_E_INVENTARIO_ERR_0006 = "RESERVAS_E_INVENTARIO_ERR_0006";
    //la hora de la resreva ya paso
    public final static String RESERVAS_E_INVENTARIO_ERR_0007 = "RESERVAS_E_INVENTARIO_ERR_0007";
    //el equipo aun no ha sido devuelto, se encuentra prestado a otra persona
    public final static String RESERVAS_E_INVENTARIO_ERR_0008 = "RESERVAS_E_INVENTARIO_ERR_0008";
    //la reserva no existe
    public final static String RESERVAS_E_INVENTARIO_ERR_0009 = "RESERVAS_E_INVENTARIO_ERR_0009";
    //ya esiste el usuario en el laboratorio
    public final static String RESERVAS_E_INVENTARIO_ERR_0010 = "RESERVAS_E_INVENTARIO_ERR_0010";
    //el grupo seleccionado no existe
    public final static String RESERVAS_E_INVENTARIO_ERR_0011 = "RESERVAS_E_INVENTARIO_ERR_0011";
    //la hora de inicio del prestamo es erronea
    public final static String RESERVAS_E_INVENTARIO_ERR_00051 = "RESERVAS_E_INVENTARIO_ERR_00051";
//la hora de fin del prestamo es erronea
    public final static String RESERVAS_E_INVENTARIO_ERR_00052 = "RESERVAS_E_INVENTARIO_ERR_00052";
    public final static String RESERVAS_E_INVENTARIO_ERR_00012 = "RESERVAS_E_INVENTARIO_ERR_00012";
    /*
     * Persona
     */
    //No se encontró ninguna persona registrada con el correo
    public final static String PERSONA_ERR_0001 = "PERSONA_ERR_0001";
    ////////////////////////////////////////////////////////////////////////////
    ///////// ADMINISTRACION SISINFO
    ////////////////////////////////////////////////////////////////////////////

    /*
     * Subir Archivos de Carga de grupo
     */
    //El formato del archivo de carga es incorrecto. El archivo debe estar guardado como un Excel 97-2003
    public final static String CARGA_ERR_0001 = "CARGA_ERR_0001";
    //El formato del archivo de carga es incorrecto. El orden de las columnas es inválido.
    public final static String CARGA_ERR_0002 = "CARGA_ERR_0002";
    //El archivo no fue cargado con éxito. Intente nuevamente.
    public final static String CARGA_ERR_0003 = "CARGA_ERR_0003";
    //El formato del archivo de carga es incorrecto. El formato de las columnas es inválido.
    public final static String CARGA_ERR_0004 = "CARGA_ERR_0004";
    //El tipo de carga de grupo es inválido.
    public final static String CARGA_ERR_0005 = "CARGA_ERR_0005";
    //El archivo fue cargado exitósamente.
    public final static String CARGA_MSJ_0001 = "CARGA_MSJ_0001";

    /*
     * Consulta y edición de roles
     */
    //La consulta de roles se realizó con éxito.
    public final static String ROL_ERR_001 = "ROL_ERR_001";
    //La consulta de roles no fue realizada con éxito. Intente nuevamente.
    public final static String ROL_MSJ_001 = "ROL_MSJ_001";
    //La consulta de usuarios se realizó con éxito.
    public final static String USER_ERR_001 = "USER_ERR_001";
    //La consulta de usuarios no fue realizada con éxito. Intente nuevamente.
    public final static String USER_MSJ_001 = "USER_MSJ_001";
    //La actualización de usuarios no se realizó con éxito.
    public final static String USER_ERR_002 = "USER_ERR_002";
    //La actualización de usuarios no fue realizada con éxito. Intente nuevamente.
    public final static String USER_MSJ_002 = "USER_MSJ_002";
    /*
     * Creación de usuarios
     */
    //El usuario fue creado exitósamente.
    public final static String USER_ERR_003 = "USER_ERR_003";
    //El usuario no fue creado exitósamente. Intente nuevamente.
    public final static String USER_MSJ_003 = "USER_MSJ_003";
    //Los países se cargaron exitósamente.
    public final static String USER_ERR_004 = "USER_ERR_004";
    //Los países no fueron cargados exitósamente. Intente nuevamente.
    public final static String USER_MSJ_004 = "USER_MSJ_004";
    //Los tipos de documento se cargaron exitósamente.
    public final static String USER_ERR_005 = "USER_ERR_005";
    //Los tipos de documento no fueron cargados exitósamente. Intente nuevamente.
    public final static String USER_MSJ_005 = "USER_MSJ_005";
    //ud no tiene permisos para borrar el grupo, solo la persona que lo creo puede realizar esta accion
    public final static String GRUPOS_PERSONAS_0001 = "GRUPOS_PERSONAS_0001";
    //ud no tiene permisos para editar el grupo, solo la persona que lo creo puede realizar esta accion
    public final static String GRUPOS_PERSONAS_0002 = "GRUPOS_PERSONAS_0002";
    //falta mapear: la fecha de configuracion ya paso.
    public final static String GRUPOS_PERSONAS_0003 = "GRUPOS_PERSONAS_0003";
    //calificacion jurados: HASH NO VALIDO: TESIS NO ENCONTRADA
    public final static String CALIFICACION_JURADOS_0001 = "CALIFICACION_JURADOS_0001";
    // El correo especificado no corresponde a una persona dentro del sistema
    public final static String CONFIGURAR_ALERTAS_GENERICAS_ERR_0001 = "CONFIGURAR_ALERTAS_GENERICAS_ERR_0001";
    // El rol especificado no corresponde a un rol dentro del sistema
    public final static String CONFIGURAR_ALERTAS_GENERICAS_ERR_0002 = "CONFIGURAR_ALERTAS_GENERICAS_ERR_0002";
    // La periodicidad especificada no corresponde a una periodicidad dentro del sistema
    public final static String CONFIGURAR_ALERTAS_GENERICAS_ERR_0003 = "CONFIGURAR_ALERTAS_GENERICAS_ERR_0003";
    //----------------------------------------------
    // ASISTENCIAS GRADUADAS
    //----------------------------------------------
    //La asistencia graduada no fue creada con éxito. Ya existe una asistencia para el estudiante en el periodo dado.
    public final static String ASISTENCIA_ERR_0002 = "ASISTENCIA_ERR_0002";
    //La consulta de asistencias no fue realizada con éxito. No existe ninguna persona asociada al correo dado.
    public final static String ASISTENCIA_ERR_0003 = "ASISTENCIA_ERR_0003";
    //La consulta de asistencias no fue realizada con éxito. No existe ningún estudiante asociado al correo dado.
    public final static String ASISTENCIA_ERR_0001 = "ASISTENCIA_ERR_0001";
    //La calificación de la asistencia no fue realizada con éxito. La asistencia no fue encontrada.
    public final static String ASISTENCIA_ERR_0004 = "ASISTENCIA_ERR_0004";
    //La consulta de asistencia no fue realizada con éxito. La asistencia no fue encontrada.
    public final static String ASISTENCIA_ERR_0005 = "ASISTENCIA_ERR_0005";
    //La consulta del estudiante no fue realizada con éxito. No se encontró a ningún estudiante con el correo dado.
    public final static String ASISTENCIA_ERR_0006 = "ASISTENCIA_ERR_0006";
    //-----------------------------------------------
    // RANGO FECHAS GENERALES
    //-----------------------------------------------
    //El rango no se creó con éxito. Ya existe un rango asociado al nombre dado.
    public final static String RANGO_ERR_0001 = "RANGO_ERR_0001";
    //La acción no se realizó con éxito. No se encontró el rango asociado al id dado.
    public final static String RANGO_ERR_0002 = "RANGO_ERR_0002";
    //La acción no se realizó con éxito. No se encontró el rango asociado al nombre dado.
    public final static String RANGO_ERR_0003 = "RANGO_ERR_0003";
    //-----------------------------------------------
    // RESERVA INVENTARIO
    //-----------------------------------------------
    //El laboratorio de nombre {0} no se encuentra registrado en el sistema
    public final static String RESERVA_INVENTARIO_ERR_0001 = "RESERVA_INVENTARIO_ERR_0001";
    //La reserva no se pudo realizar debido a que entra en conflicto con otra reserva existente
    public final static String RESERVA_INVENTARIO_ERR_0002 = "RESERVA_INVENTARIO_ERR_0002";
    //La reserva no se pudo realizar debido a que el horario seleccionado no se encuentra dentro de los horarios disponibles del laboratorio
    public final static String RESERVA_INVENTARIO_ERR_0003 = "RESERVA_INVENTARIO_ERR_0003";
    //La reserva no se pudo realizar debido a que el horario seleccionado debe ser posterior a la fecha actual
    public final static String RESERVA_INVENTARIO_ERR_0004 = "RESERVA_INVENTARIO_ERR_0004";
    //La reserva no se pudo realizar debido a que el laboratorio seleccionado no es reservable
    public final static String RESERVA_INVENTARIO_ERR_0005 = "RESERVA_INVENTARIO_ERR_0005";
    //Usted no se encuentra autorizado para realizar reservas sobre esta sala. Si desea realizar una reserva sobre esta sala contacte directamente al administrador de laboratorios
    public final static String RESERVA_INVENTARIO_ERR_0006 = "RESERVA_INVENTARIO_ERR_0006";
    //El laboratorio no se pudo crear debido a que ya existe un laboratorio con el nombre especificado.
    public final static String RESERVA_INVENTARIO_ERR_0007 = "RESERVA_INVENTARIO_ERR_0007";
    //La fecha de inicio de la reserva debe ser anterior a la fecha de finalización de la reserva multiple
    public final static String RESERVA_INVENTARIO_ERR_0008 = "RESERVA_INVENTARIO_ERR_0008";
    //El servicio de autenticación de la universidad no se encuentra disponible en este momento. Por favor intente despues
    public final static String SESION_ERR_001 = "SESION_ERR_001";
}
