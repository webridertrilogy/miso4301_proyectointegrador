package co.uniandes.sisinfo.comun.constantes;

public class Constantes {

    //--------------------------------------------------------------------------
    // Comun
    //--------------------------------------------------------------------------
    public final static String NULL = "NULL";
    public final static String TRUE = "TRUE";
    public final static String FALSE = "FALSE";
    public final static String NO_APLICA = "NO_APLICA";
    public final static String VAL_CODIGO_APOI = "VAL_CODIGO_APOI";
    public final static String VAL_CODIGO_LAB_APOI = "VAL_CODIGO_LAB_APOI";
    public final static String VAL_CODIGO_APOII = "VAL_CODIGO_APOII";
    public final static String VAL_CODIGO_LAB_APOII = "VAL_CODIGO_LAB_APOII";
    public final static String VAL_CODIGO_ESTRUCTURAS = "VAL_CODIGO_ESTRUCTURAS";
    public final static String VAL_CODIGO_LAB_ESTRUCTURAS = "VAL_CODIGO_LAB_ESTRUCTURAS";
    public final static String VAL_TIPO_PROFESOR_CATEDRA = "VAL_TIPO_PROFESOR_CATEDRA";
    public final static String VAL_TIPO_PROFESOR_PLANTA = "VAL_TIPO_PROFESOR_PLANTA";
    public final static String VAL_NOMBRE_AUXILIAR_MB = "VAL_NOMBRE_AUXILIAR_MB";
    public final static String VAL_CORREO_AUXILIAR_MB = "VAL_CORREO_AUXILIAR_MB";
    public final static String VAL_NOMBRE_DIRECTOR_MB = "VAL_NOMBRE_DIRECTOR_MB";
    public final static String VAL_CORREO_DIRECTOR_MB = "VAL_CORREO_DIRECTOR_MB";
    public final static String VAL_CODIGO_APO_CON_HONORES = "VAL_CODIGO_APO_CON_HONORES";
    public final static String VAL_CODIGO_LAB_APO_CON_HONORES = "VAL_CODIGO_LAB_APO_CON_HONORES";


    //--------------------------------------------------------------------------
    // Roles
    //--------------------------------------------------------------------------
    public final static String ROL_ADMINISTRADOR_SISINFO = "ROL_ADMINISTRADOR_SISINFO";
    public final static String ROL_SECRETARIO_COORDINACION = "ROL_SECRETARIO_COORDINACION";
    public final static String ROL_ESTUDIANTE = "ROL_ESTUDIANTE";
    public final static String ROL_COORDINACION = "ROL_COORDINACION";
    public final static String ROL_PROFESOR = "ROL_PROFESOR";
    public final static String ROL_PROFESOR_CATEDRA = "ROL_PROFESOR_CATEDRA";
    public final static String ROL_PROFESOR_PLANTA = "ROL_PROFESOR_PLANTA";
    public final static String ROL_SECRETARIA = "ROL_SECRETARIA";
    public final static String ROL_DIRECCION_DEPARTAMENTO = "ROL_DIRECCION_DEPARTAMENTO";
    public final static String ROL_AUXILIAR_FINACIERO = "ROL_AUXILIAR_FINACIERO";
    public final static String ROL_INDETERMINADO = "ROL_INDETERMINADO";
    public final static String ROL_ASISTENTE = "ROL_ASISTENTE";
    public final static String ROL_COORDINADOR_SUBAREA_INVESTIGACION = "ROL_COORDINADOR_SUBAREA_INVESTIGACION";
    public final static String ROL_CUPI2 = "ROL_CUPI2";
    public final static String ROL_ADMINISTRADOR_BOLSAEMPLEO = "ROL_ADMINISTRADOR_BOLSAEMPLEO";
    public final static String ROL_ADMINISTRADOR_VOTACIONES = "ROL_ADMINISTRADOR_VOTACIONES";
    public final static String ROL_ADMINISTRADOR_INSCRIPCIONES = "ROL_ADMINISTRADOR_INSCRIPCIONES";
    public final static String ROL_ADMINISTRADOR_GRUPOS_Y_PERSONAS = "ROL_ADMINISTRADOR_GRUPOS_Y_PERSONAS";
    public final static String ROL_EXTERNO_BOLSAEMPLEO = "ROL_EXTERNO_BOLSAEMPLEO";
    public final static String ROL_ASISTENTE_DIRECCION = "ROL_ASISTENTE_DIRECCION";
    public final static String ROL_ADMINISTRADOR_RESERVA_INVENTARIO = "ROL_ADMINISTRADOR_RESERVA_INVENTARIO";
    public final static String ROL_ENCARGADO_LABORATORIO = "ROL_ENCARGADO_LABORATORIO";
    public final static String ROL_TODOS = "ROL_TODOS";
    //public final static String ROL_SOPORTE_SISINFO="ROL_SOPORTE_SISINFO";
    public final static String VAG_TAG_ROL_COORDINACION = "VAG_TAG_ROL_COORDINACION";
    public final static String VAG_TAG_ROL_SECRETARIA = "VAG_TAG_ROL_SECRETARIA";
    public final static String ROL_ADMINISTRADOR_LABORATORIO = "ROL_ADMINISTRADOR_LABORATORIO";
    public final static String ROL_ADMINISTRADOR_INVENTARIO_LABORATORIO = "ROL_ADMINISTRADOR_INVENTARIO_LABORATORIO";
    public final static String CORREO_ASISTENTE_DIRECCION = "CORREO_ASISTENTE_DIRECCION";
    public final static String ROL_ESTUDIANTE_EXTERNO = "ROL_ESTUDIANTE_EXTERNO";
    //--------------------------------------------------------------------------
    // Estados
    //--------------------------------------------------------------------------
    //..................................TAGS..................................//
    public final static String TAG_PARAM_ESTADO = "TAG_PARAM_ESTADO";
    //........................VALORES-TAG-SOLICITUD...........................//
    public final static String ESTADO_ASPIRANTE = "ESTADO_ASPIRANTE";
    public final static String ESTADO_PENDIENTE_CONFIRMACION_PROFESOR = "ESTADO_PENDIENTE_CONFIRMACION_PROFESOR";
    public final static String ESTADO_PENDIENTE_CONFIRMACION_ESTUDIANTE = "ESTADO_PENDIENTE_CONFIRMACION_ESTUDIANTE";
    public final static String ESTADO_PENDIENTE_VERIFICACION_DATOS = "ESTADO_PENDIENTE_VERIFICACION_DATOS";
    public final static String ESTADO_PENDIENTE_REGISTRO_CONVENIO = "ESTADO_PENDIENTE_REGISTRO_CONVENIO"; //Convenio que no se han creado
    public final static String ESTADO_PENDIENTE_FIRMA_CONVENIO_ESTUDIANTE = "ESTADO_PENDIENTE_FIRMA_CONVENIO_ESTUDIANTE";
    public final static String ESTADO_PENDIENTE_FIRMA_CONVENIO_DEPARTAMENTO = "ESTADO_PENDIENTE_FIRMA_CONVENIO_DEPARTAMENTO";
    public final static String ESTADO_PENDIENTE_RADICACION = "ESTADO_PENDIENTE_RADICACION";
    public final static String ESTADO_ASIGNADO = "ESTADO_ASIGNADO";
    public final static String ESTADO_INVALIDA = "ESTADO_INVALIDA";
    public final static String ESTADO_DESPUES_DE = "ESTADO_DESPUES_DE";
    //FIXME: Revisar
    public final static String[] JERARQUIA_ESTADOS = {ESTADO_ASPIRANTE,
        ESTADO_PENDIENTE_CONFIRMACION_PROFESOR,
        ESTADO_PENDIENTE_CONFIRMACION_ESTUDIANTE,
        ESTADO_PENDIENTE_VERIFICACION_DATOS,
        ESTADO_PENDIENTE_REGISTRO_CONVENIO,
        ESTADO_PENDIENTE_FIRMA_CONVENIO_ESTUDIANTE,
        ESTADO_PENDIENTE_FIRMA_CONVENIO_DEPARTAMENTO,
        ESTADO_PENDIENTE_RADICACION, ESTADO_ASIGNADO};
    //...........................VALORES-TAG-TAREA............................//
    public final static String ESTADO_TAREA_VENCIDA = "ESTADO_TAREA_VENCIDA";
    public final static String ESTADO_TAREA_PENDIENTE = "ESTADO_TAREA_PENDIENTE";
    public final static String ESTADO_TAREA_TERMINADA_MODIFICABLE = "ESTADO_TAREA_TERMINADA_MODIFICABLE";
    public final static String ESTADO_TAREA_TERMINADA = "ESTADO_TAREA_TERMINADA";
    public final static String ESTADO_TAREA_PENDIENTE_VENCIDA = "ESTADO_TAREA_PENDIENTE_VENCIDA";
    public final static String VAL_NUMERO_EJECUCIONES_PENDIENTES = "VAL_NUMERO_EJECUCIONES_PENDIENTES";
    public final static String VAL_PERIODICIDAD_ENVIO_CORREO_TAREAS_VENCIDAS = "VAL_PERIODICIDAD_ENVIO_CORREO_TAREAS_VENCIDAS";
    //.......................VALORES-TAG-CONVOCATORIA.........................//
    public final static String ESTADO_ABIERTA = "ESTADO_ABIERTA";
    public final static String ESTADO_CERRADA = "ESTADO_CERRADA";
    //--------------------------------------------------------------------------
    // Resultado
    //--------------------------------------------------------------------------
    //..................................TAGS..................................//
    public final static String TAG_RESULTADO = "TAG_RESULTADO";
    public final static String TAG_RESPUESTA = "TAG_RESPUESTA";
    public final static String TAG_RESPUESTA_PROCESO = "TAG_RESPUESTA_PROCESO";
    public final static String TAG_DESCRIPCION = "TAG_DESCRIPCION";
    //...............................ATRIBUTOS................................//
    public final static String ATR_EDITABLE = "ATR_EDITABLE";
    //--------------------------------------------------------------------------
    // Comando
    //--------------------------------------------------------------------------
    //.............................VALORES TAGS...............................//
    public final static String VAL_TAG_TIPO_CMD_CONSULTA = "VAL_TAG_TIPO_CMD_CONSULTA";
    public final static String VAL_TAG_TIPO_CMD_PROCESO = "VAL_TAG_TIPO_CMD_PROCESO";
    //..................................TAGS..................................//
    public final static String TAG_COMANDO = "TAG_COMANDO";
    public final static String TAG_PARAMETRO = "TAG_PARAMETRO";
    public final static String TAG_PARAMETROS = "TAG_PARAMETROS";
    public final static String TAG_ROL = "TAG_ROL";
    public final static String TAG_ROLES = "TAG_ROLES";
    public final static String TAG_TIPO_COMANDO = "TAG_TIPO_COMANDO";
    public final static String TAG_NOMBRE_COMANDO = "TAG_NOMBRE_COMANDO";
    public final static String TAG_USUARIO = "TAG_USUARIO";
    //--------------------------------------------------------------------------
    // Respuesta
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    // Servicios Nucleo
    //--------------------------------------------------------------------------
    //..........................................................................
    // Servicios NivelFormacion y CYC
    //..........................................................................
    public final static String CMD_CONSULTAR_NIVELES_FORMACION = "CMD_CONSULTAR_NIVELES_FORMACION";
    public final static String TAG_PARAM_NIVEL_MAESTRIA = "TAG_PARAM_NIVEL_MAESTRIA";
    public final static String TAG_PARAM_NIVEL_PREGRADO = "TAG_PARAM_NIVEL_PREGRADO";
    public final static String TAG_PARAM_NIVEL_ESPECIALIZACION = "TAG_PARAM_NIVEL_ESPECIALIZACION";
    public final static String TAG_PARAM_NIVEL_DOCTORADO = "TAG_PARAM_NIVEL_DOCTORADO";
    public final static String TAG_PARAM_NIVELES_FORMACION = "TAG_PARAM_NIVELES_FORMACION";
    public final static String TAG_PARAM_NIVEL_FORMACION = "TAG_PARAM_NIVEL_FORMACION";
    public final static String[] NIVELES_FORMACION = {TAG_PARAM_NIVEL_MAESTRIA, TAG_PARAM_NIVEL_PREGRADO, TAG_PARAM_NIVEL_ESPECIALIZACION, TAG_PARAM_NIVEL_DOCTORADO};
    //..........................................................................
    // Servicios NivelPlanta
    //..........................................................................
    public final static String CMD_CONSULTAR_NIVELES_PLANTA = "CMD_CONSULTAR_NIVELES_PLANTA";
    public final static String TAG_PARAM_NIVEL_INSTRUCTOR = "TAG_PARAM_NIVEL_INSTRUCTOR";
    public final static String TAG_PARAM_NIVEL_ASISTENTE = "TAG_PARAM_NIVEL_ASISTENTE";
    public final static String TAG_PARAM_NIVEL_ASOCIADO = "TAG_PARAM_NIVEL_ASOCIADO";
    public final static String TAG_PARAM_NIVEL_TITULAR = "TAG_PARAM_NIVEL_TITULAR";
    public final static String TAG_PARAM_NIVELES_PLANTA = "TAG_PARAM_NIVELES_PLANTA";
    public final static String TAG_PARAM_NIVEL_PLANTA = "TAG_PARAM_NIVEL_PLANTA";
    public final static String[] NIVELES_PLANTA = {TAG_PARAM_NIVEL_INSTRUCTOR, TAG_PARAM_NIVEL_ASISTENTE, TAG_PARAM_NIVEL_ASOCIADO, TAG_PARAM_NIVEL_TITULAR};
    //..........................................................................
    // Servicios Grupo
    //..........................................................................
    public final static String CMD_CONSULTAR_GRUPO_POR_NOMBRE = "CMD_CONSULTAR_GRUPO_POR_NOMBRE";
    public final static String CMD_VACIAR_GRUPO = "CMD_VACIAR_GRUPO";
    public final static String CMD_ELIMINAR_GRUPO_PERSONAS = "CMD_ELIMINAR_GRUPO_PERSONAS";
    public final static String VAL_TAG_NOMBRE_GRUPO_PROFESORES_PLANTA = "VAL_TAG_NOMBRE_GRUPO_PROFESORES_PLANTA";
    //servicios persona
    public final static String CMD_DAR_PERSONA_POR_CORREO = "CMD_DAR_PERSONA_POR_CORREO";
    public final static String CMD_ESTA_REGISTRADO_LDAP = "CMD_ESTA_REGISTRADO_LDAP";
    public final static String TAG_PARAM_REGISTRADO = "TAG_PARAM_REGISTRADO";
    public final static String CMD_EDITAR_PERSONA = "CMD_EDITAR_PERSONA";
    //--------------------------------------------------------------------------
    // Fin Servicios Nucleo
    //--------------------------------------------------------------------------
    //..................................TAGS..................................//
    public final static String TAG_MENSAJE = "TAG_MENSAJE";
    public final static String TAG_TIPO_MENSAJE = "TAG_TIPO_MENSAJE";
    public final static String TAG_ID_MENSAJE = "TAG_ID_MENSAJE";
    //.............................VALORES TAGS...............................//
    public final static String VAL_TAG_TIPO_MENSAJE_ERROR = "VAL_TAG_TIPO_MENSAJE_ERROR";
    public final static String VAL_TAG_TIPO_MENSAJE_MENSAJE = "VAL_TAG_TIPO_MENSAJE_MENSAJE";
    //...............................ATRIBUTOS................................//
    public final static String ATR_ID_PARAMETRO_MENSAJE = "ATR_ID_PARAMETRO_MENSAJE";
    //--------------------------------------------------------------------------
    //Servicios de RangoFechas
    //--------------------------------------------------------------------------
    public final static String CMD_RANGO_INICIADO = "CMD_RANGO_INICIADO";
    public final static String CMD_CREAR_RANGOS_FECHAS = "CMD_CREAR_RANGOS_FECHAS";
    public final static String CMD_EDITAR_RANGOS_FECHAS = "CMD_EDITAR_RANGOS_FECHAS";
    public final static String CMD_CONSULTAR_RANGOS_FECHAS = "CMD_CONSULTAR_RANGOS_FECHAS";
    public final static String VAL_RANGO_FECHAS_GENERAL = "VAL_RANGO_FECHAS_GENERAL";
    public final static String VAL_RANGO_FECHAS_NOTAS_MONITORES = "VAL_RANGO_FECHAS_NOTAS_MONITORES";
    public final static String TAG_PARAM_RANGOS = "TAG_PARAM_RANGOS";
    public final static String TAG_PARAM_RANGO = "TAG_PARAM_RANGO";
    //--------------------------------------------------------------------------
    // Servicios de Sesion
    //--------------------------------------------------------------------------
    //................................COMANDOS................................//
    public final static String CMD_DAR_DATOS_BASICOS_SESION = "CMD_DAR_DATOS_BASICOS_SESION";
    public final static String CMD_CREAR_CREDENCIAL = "CMD_CREAR_CREDENCIAL";
    //--------------------------------------------------------------------------
    // Servicios Estudiante
    //--------------------------------------------------------------------------
    //................................COMANDOS................................//
    public final static String CMD_ESTA_EN_LISTA_NEGRA_POR_LOGIN = "CMD_ESTA_EN_LISTA_NEGRA_POR_LOGIN";
    public final static String CMD_ESTA_EN_LISTA_NEGRA_POR_CODIGO = "CMD_ESTA_EN_LISTA_NEGRA_POR_CODIGO";
    public final static String CMD_DAR_DATOS_PERSONALES = "CMD_DAR_DATOS_PERSONALES";
    public final static String CMD_DAR_DATOS_ACADEMICOS = "CMD_DAR_DATOS_ACADEMICOS";
    public final static String CMD_DAR_DATOS_EMERGENCIA = "CMD_DAR_DATOS_EMERGENCIA";
    public final static String CMD_DAR_MONITORIAS_OTROS_DEPTOS = "CMD_DAR_MONITORIAS_OTROS_DEPTOS";
    public final static String CMD_ACTUALIZAR_DATOS_ACADEMICOS = "CMD_ACTUALIZAR_DATOS_ACADEMICOS";
    public final static String CMD_ACTUALIZAR_DATOS_PERSONALES_Y_EMERGENCIA = "CMD_ACTUALIZAR_DATOS_PERSONALES_Y_EMERGENCIA";
    public final static String CMD_DAR_SOLICITUDES_ESTUDIANTE_POR_CODIGO = "CMD_DAR_SOLICITUDES_ESTUDIANTE_POR_CODIGO";
    public final static String CMD_DAR_SOLICITUDES_ESTUDIANTE_POR_LOGIN = "CMD_DAR_SOLICITUDES_ESTUDIANTE_POR_LOGIN";
    /*Comandos Cupi2*/
    public final static String CMD_DAR_MONITORIAS_HISTORICO_POR_CORREO = "CMD_DAR_MONITORIAS_HISTORICO_POR_CORREO";
    public final static String CMD_CONSULTAR_MONITORIAS_HISTORICAS_NUEVAS = "CMD_CONSULTAR_MONITORIAS_HISTORICAS_NUEVAS";
    public final static String CMD_DAR_HORARIO_ESTUDIANTE_POR_LOGIN = "CMD_DAR_HORARIO_ESTUDIANTE_POR_LOGIN";
    public final static String CMD_DAR_HORARIO_ESTUDIANTE_POR_CODIGO = "CMD_DAR_HORARIO_ESTUDIANTE_POR_CODIGO";
    public final static String CMD_CONFIRMAR_SECCION = "CMD_CONFIRMAR_SECCION";
    public final static String CMD_CANCELAR_PRESELECCION = "CMD_CANCELAR_PRESELECCION";
    public final static String CMD_DAR_CURSOS = "CMD_DAR_CURSOS";
    public final static String CMD_MODIFICAR_HORARIO = "CMD_MODIFICAR_HORARIO";
    public final static String CMD_ACTUALIZAR_SOLICITUD = "CMD_ACTUALIZAR_SOLICITUD";
    public final static String CMD_DAR_CURSO = "CMD_DAR_CURSO";
    public final static String CMD_DAR_SECCION = "CMD_DAR_SECCION";
    public final static String CMD_TIENE_SOLICITUDES_POR_LOGIN = "CMD_TIENE_SOLICITUDES_POR_LOGIN";
    public final static String CMD_ES_RANGO_VALIDO = "CMD_ES_RANGO_VALIDO";
    public final static String CMD_CONSULTAR_ES_ESTUDIANTE_POR_CORREO = "CMD_CONSULTAR_ES_ESTUDIANTE_POR_CORREO";
    //.................................TAGS................................//
    public final static String TAG_PARAM_TIENE_SOLICITUDES = "TAG_PARAM_TIENE_SOLICITUDES";
    //--------------------------------------------------------------------------
    // Servicios Lista Negra
    //--------------------------------------------------------------------------
    //................................COMANDOS................................//
    public final static String CMD_AGREGAR_A_LISTA_NEGRA = "CMD_AGREGAR_A_LISTA_NEGRA";
    public final static String CMD_ELIMINAR_DE_LISTA_NEGRA = "CMD_ELIMINAR_DE_LISTA_NEGRA";
    public final static String TAG_PARAM_MOTIVO = "TAG_PARAM_MOTIVO";
    public final static String TAG_PARAM_LOGIN = "TAG_PARAM_LOGIN";
    public final static String CMD_DAR_LISTA_NEGRA = "CMD_DAR_LISTA_NEGRA";
    //..................................TAGS..................................//
    public final static String TAG_PARAM_LISTA_NEGRA = "TAG_PARAM_LISTA_NEGRA";
    public final static String TAG_PARAM_FECHA_EN_LISTA_NEGRA = "TAG_PARAM_FECHA_EN_LISTA_NEGRA";
    public final static String TAG_PARAM_ESTUDIANTES = "TAG_PARAM_ESTUDIANTES";
    public final static String TAG_PARAM_ESTUDIANTE = "TAG_PARAM_ESTUDIANTE";
    public final static String TAG_PARAM_TEMPORAL = "TAG_PARAM_TEMPORAL";
    public final static String VAL_NUMERO_MESES_EN_LISTA_NEGRA = "VAL_NUMERO_MESES_EN_LISTA_NEGRA";
    //--------------------------------------------------------------------------
    // Servicios Solicitud
    //--------------------------------------------------------------------------
    //................................COMANDOS................................//
    public final static String CMD_DAR_SOLICITUDES_POR_ESTADO = "CMD_DAR_SOLICITUDES_POR_ESTADO";
    public final static String CMD_DAR_SOLICITUDES_POR_ESTADO_SECRETARIA = "CMD_DAR_SOLICITUDES_POR_ESTADO_SECRETARIA";
    public final static String CMD_DAR_SOLICITUDES_RESUELTAS_SECRETARIA = "CMD_DAR_SOLICITUDES_RESUELTAS_SECRETARIA";
    public final static String CMD_DAR_SOLICITUD_SECRETARIA = "CMD_DAR_SOLICITUD_SECRETARIA";
    public final static String CMD_DAR_SOLICITUDES_DESPUES_DE_ESTADO = "CMD_DAR_SOLICITUDES_DESPUES_DE_ESTADO";
    public final static String CMD_DAR_SOLICITUDES_PRESELECCIONADAS_POR_CORDINACION_A_PROFESOR = "CMD_DAR_SOLICITUDES_PRESELECCIONADAS_POR_CORDINACION_A_PROFESOR";
    public final static String CMD_DAR_SOLICITUD_POR_ID = "CMD_DAR_SOLICITUD_POR_ID";
    public final static String CMD_DAR_CURSOS_CON_VACANTES = "CMD_DAR_CURSOS_CON_VACANTES";
    public final static String CMD_DAR_SECCIONES_CON_VACANTES_POR_CURSO = "CMD_DAR_SECCIONES_CON_VACANTES_POR_CURSO";
    public final static String CMD_DAR_PROGRAMAS_ACADEMICOS = "CMD_DAR_PROGRAMAS_ACADEMICOS";
    public final static String CMD_DAR_NACIONALIDADES = "CMD_DAR_NACIONALIDADES";
    public final static String CMD_DAR_TIPOS_DOCUMENTO = "CMD_DAR_TIPOS_DOCUMENTO";
    public final static String CMD_ENVIAR_SOLICITUD = "CMD_ENVIAR_SOLICITUD";
    public final static String CMD_ELIMINAR_SOLICITUD = "CMD_ELIMINAR_SOLICITUD";
    public final static String CMD_DAR_FACULTADES = "CMD_DAR_FACULTADES";
    public final static String CMD_DAR_PROGRAMAS_FACULTAD = "CMD_DAR_PROGRAMAS_FACULTAD";
    public final static String CMD_DAR_FACULTADES_CON_PROGRAMAS = "CMD_DAR_FACULTADES_CON_PROGRAMAS";
    public final static String CMD_DAR_MONITORIAS_POR_SECCION = "CMD_DAR_MONITORIAS_POR_SECCION";
    public final static String CMD_DAR_SOLICITUDES_SIN_MONITORIA = "CMD_DAR_SOLICITUDES_SIN_MONITORIA";
    public final static String CMD_DAR_SECCIONES_APOI_POR_CORREO = "CMD_DAR_SECCIONES_APOI_POR_CORREO";
    public final static String CMD_DAR_DATOS_MONITORIA_POR_ID_SOLICITUD = "CMD_DAR_DATOS_MONITORIA_POR_ID_SOLICITUD";
    public final static String CMD_TIMER_CONFIRMACION_MONITOR_3_DIAS = "CMD_TIMER_CONFIRMACION_MONITOR_3_DIAS";
    public final static String CMD_DAR_SOLICITUDES_MONITORIAS = "CMD_DAR_SOLICITUDES_MONITORIAS";
    public final static String CMD_REGISTRAR_FIRMAS_ESTUDIANTES = "CMD_REGISTRAR_FIRMAS_ESTUDIANTES";
    public final static String CMD_REGISTRAR_FIRMAS_DEPARTAMENTO = "CMD_REGISTRAR_FIRMAS_DEPARTAMENTO";
    //..................................TAGS..................................//
    public final static String TAG_ID_SOLICITUD = "TAG_ID_SOLICITUD";
    public final static String TAG_FECHA_VENCIMIENTO_APROBACION_PROFESOR = "TAG_FECHA_VENCIMIENTO_APROBACION_PROFESOR";
    public final static String TAG_FECHA_VENCIMIENTO_APROBACION_ESTUDIANTE = "TAG_FECHA_VENCIMIENTO_APROBACION_ESTUDIANTE";
    public final static String TAG_CARGA_SECCION = "TAG_CARGA_SECCION";
    public final static String TAG_CARGA_MONITORIA = "TAG_CARGA_MONITORIA";
    public final static String TAG_PARAM_MONITORIA = "TAG_PARAM_MONITORIA";
    public final static String TAG_PARAM_MONITORIAS = "TAG_PARAM_MONITORIAS";
    public final static String TAG_PARAM_MONITORIAS_HISTORICAS_NUEVAS = "TAG_PARAM_MONITORIAS_HISTORICAS_NUEVAS";
    public final static String TAG_PARAM_MONITORIA_HISTORICA_NUEVA = "TAG_PARAM_MONITORIA_HISTORICA_NUEVA";
    //--------------------------------------------------------------------------
    // Servicios Profesor
    //--------------------------------------------------------------------------
    //................................COMANDOS................................//
    public final static String CMD_DAR_SOLICITUDES_PRESELECCIONADAS_POR_SECCION = "CMD_DAR_SOLICITUDES_PRESELECCIONADAS_POR_SECCION";
    public final static String CMD_DAR_SOLICITUDES_PRESELECCIONADAS_POR_CORDINACION = "CMD_DAR_SOLICITUDES_PRESELECCIONADAS_POR_CORDINACION";
    public final static String CMD_DAR_SOLICITUDES_EN_ASPIRACION_POR_SECCION = "CMD_DAR_SOLICITUDES_EN_ASPIRACION_POR_SECCION";
    public final static String CMD_DAR_SECCIONES_POR_LOGIN_PROFESOR = "CMD_DAR_SECCIONES_POR_LOGIN_PROFESOR";
    public final static String CMD_DAR_SECCIONES_POR_LOGIN_PROFESOR_BASICO = "CMD_DAR_SECCIONES_POR_LOGIN_PROFESOR_BASICO";
    public final static String CMD_DAR_SECCIONES_PRESELECCIONADAS_POR_SECCION = "CMD_DAR_SECCIONES_PRESELECCIONADAS_POR_SECCION";
    public final static String CMD_DAR_DETALLE_SOLICITUD = "CMD_DAR_DETALLE_SOLICITUD";
    public final static String CMD_ENVIAR_PRESELECCION = "CMD_ENVIAR_PRESELECCION";
    public final static String CMD_CONFIRMAR_ESTUDIANTE = "CMD_CONFIRMAR_ESTUDIANTE";
    public final static String CMD_DAR_SECCIONES_SIN_PRESELECCION = "CMD_DAR_SECCIONES_SIN_PRESELECCION";
    public final static String CMD_CONSULTAR_SECCIONES_POR_TIPO_ARCHIVO = "CMD_CONSULTAR_SECCIONES_POR_TIPO_ARCHIVO";
    public final static String CMD_ENVIAR_NOTA_MONITOR = "CMD_ENVIAR_NOTA_MONITOR";
    public final static String VAL_NUMERO_UNIVERSIDAD = "VAL_NUMERO_UNIVERSIDAD";
    public final static String TIMER_CREAR_RANGOFECHAS = "TIMER_CREAR_RANGOFECHAS";
    public final static String TIMER_CERRAR_CONVOCATORIA = "TIMER_CERRAR_CONVOCATORIA";
    public final static String TIMER_SUBIR_NOTAS_MONITORES = "TIMER_SUBIR_NOTAS_MONITORES";
    //--------------------------------------------------------------------------
    // Servicios Cupi2
    //--------------------------------------------------------------------------
    //................................COMANDOS................................//
    public final static String CMD_DAR_CURSOS_CUPI2 = "CMD_DAR_CURSOS_CUPI2";
    //-------------------------------------------------------------------------
    // Servicios Coordinación
    //-------------------------------------------------------------------------
    //................................COMANDOS................................//
    public final static String CMD_HACER_EXCEPCION_REGLA = "CMD_HACER_EXCEPCION_REGLA";
    public final static String CMD_ACTUALIZAR_SOLICITUD_VERIFICACION = "CMD_ACTUALIZAR_SOLICITUD_VERIFICACION";
    public final static String CMD_DETERMINAR_RESULTADO_VERIFICACION = "CMD_DETERMINAR_RESULTADO_VERIFICACION";
    public final static String CMD_HACER_EXCEPCION_REGLACMD_DAR_CONFLICTO_SECCION = "CMD_HACER_EXCEPCION_REGLACMD_DAR_CONFLICTO_SECCION";
    public final static String CMD_DAR_RESULTADOS_RESOLUCION = "CMD_DAR_RESULTADOS_RESOLUCION";
    public final static String CMD_ENVIAR_ESTUDIANTES_PRESELECCION = "CMD_ENVIAR_ESTUDIANTES_PRESELECCION";
    public final static String CMD_DAR_SOLICITUDES_POR_CRITERIO = "CMD_DAR_SOLICITUDES_POR_CRITERIO";
    public final static String CMD_HACER_INTERCAMBIO_MONITORES = "CMD_HACER_INTERCAMBIO_MONITORES";
    public final static String CMD_DAR_SOLICITUDES = "CMD_DAR_SOLICITUDES";
    public final static String CMD_DAR_CURSOS_PENDIENTES_POR_MONITORES = "CMD_DAR_CURSOS_PENDIENTES_POR_MONITORES";
    public final static String CMD_DAR_CURSOS_SEMESTRE_ACTUAL = "CMD_DAR_CURSOS_SEMESTRE_ACTUAL";
    public final static String CMD_DAR_PENSUM = "CMD_DAR_PENSUM";
    public final static String CMD_DAR_CONFLICTO_SECCION = "CMD_DAR_CONFLICTO_SECCION";
    public final static String CMD_ACTUALIZAR_DATOS_PROFESOR = "CMD_ACTUALIZAR_DATOS_PROFESOR";
    public final static String CMD_AGREGAR_PROFESOR = "CMD_AGREGAR_PROFESOR";
    public final static String CMD_CONSULTAR_DATOS_PROFESORES = "CMD_CONSULTAR_DATOS_PROFESORES";
    public final static String CMD_CONSULTAR_DATOS_PROFESOR = "CMD_CONSULTAR_DATOS_PROFESOR";
    public final static String CMD_INICIAR_PERIODO = "CMD_INICIAR_PERIODO";
    public final static String CMD_ABRIR_CONVOCATORIA = "CMD_ABRIR_CONVOCATORIA";
    public final static String CMD_CERRAR_CONVOCATORIA = "CMD_CERRAR_CONVOCATORIA";
    public final static String CMD_MODIFICAR_DATOS_CURSO = "CMD_MODIFICAR_DATOS_CURSO";
    public final static String CMD_ASIGNAR_PARAMS_CONVOCATORIA_POR_SECCIONES = "CMD_ASIGNAR_PARAMS_CONVOCATORIA_POR_SECCIONES";
    public final static String CMD_GENERAR_REPORTES = "CMD_GENERAR_REPORTES";
    public final static String CMD_DETERMINAR_SI_HAY_CONVOCATORIA_ABIERTA = "CMD_DETERMINAR_SI_HAY_CONVOCATORIA_ABIERTA";
    //.................................TAGS................................//
    public final static String TAG_PARAM_ABIERTA = "TAG_PARAM_ABIERTA";
    public final static String TAG_PARAM_TIPO_REPORTE = "TAG_PARAM_TIPO_REPORTE";
    public final static String TAG_PARAM_CONFLICTO = "TAG_PARAM_CONFLICTO";
    public final static String TAG_PARAM_FORZADA = "TAG_PARAM_FORZADA";
    public final static String TAG_PARAM_ID_PENSUM = "TAG_PARAM_ID_PENSUM";
    public final static String TAG_PARAM_SEMESTRES = "TAG_PARAM_SEMESTRES";
    public final static String TAG_PARAM_SEMESTRE = "TAG_PARAM_SEMESTRE";
    public final static String TAG_PARAM_NUMERO_SEMESTRE = "TAG_PARAM_NUMERO_SEMESTRE";
    public final static String TAG_PARAM_MATERIAS = "TAG_PARAM_MATERIAS";
    public final static String TAG_PARAM_MATERIA = "TAG_PARAM_MATERIA";
    public final static String TAG_PARAM_NOMBRE_MATERIA = "TAG_PARAM_NOMBRE_MATERIA";
    public final static String TAG_PARAM_CODIGO_MATERIA = "TAG_PARAM_CODIGO_MATERIA";
    public final static String TAG_PARAM_NUMERO_CREDITOS = "TAG_PARAM_NUMERO_CREDITOS";
    public final static String TAG_PARAM_PENSUM_ACTUAL = "TAG_PARAM_PENSUM_ACTUAL";
    public final static String TAG_PARAM_NOMBRE_AREA = "TAG_PARAM_NOMBRE_AREA";
    public final static String TAG_PARAM_PRERREQUISITOS = "TAG_PARAM_PRERREQUISITOS";
    public final static String TAG_PARAM_GRUPO_PRERREQUISITOS = "TAG_PARAM_GRUPO_PRERREQUISITOS";
    public final static String TAG_PARAM_ID_GRUPO_PRERREQUISITO = "TAG_PARAM_ID_GRUPO_PRERREQUISITO";
    public final static String TAG_PARAM_OPERADOR_ENTRE_MATERIAS = "TAG_PARAM_OPERADOR_ENTRE_MATERIAS";
    public final static String TAG_PARAM_CORREQUISITOS = "TAG_PARAM_CORREQUISITOS";
    public final static String TAG_PARAM_AREAS = "TAG_PARAM_AREAS";
    public final static String TAG_PARAM_AREA = "TAG_PARAM_AREA";
    public final static String TAG_PARAM_ITEMS_AREA = "TAG_PARAM_ITEMS_AREA";
    public final static String TAG_PARAM_ITEM_AREA = "TAG_PARAM_ITEM_AREA";
    public final static String TAG_PARAM_TOTAL_CREDITOS_OBLIGATORIOS = "TAG_PARAM_TOTAL_CREDITOS_OBLIGATORIOS";
    public final static String TAG_PARAM_TOTAL_CREDITOS_OPCIONALES = "TAG_PARAM_TOTAL_CREDITOS_OPCIONALES";
    public final static String TAG_PARAM_VERIFICACIONES_REGLAS = "TAG_PARAM_VERIFICACIONES_REGLAS";
    public final static String TAG_PARAM_VERIFICACION_REGLA = "TAG_PARAM_VERIFICACION_REGLA";
    public final static String TAG_PARAM_RESULTADO_REGLA = "TAG_PARAM_RESULTADO_REGLA";
    public final static String TAG_PARAM_FECHA = "TAG_PARAM_FECHA";
    public final static String TAG_PARAM_RUTA = "TAG_PARAM_RUTA";
    public final static String TAG_PARAM_PERIODO = "TAG_PARAM_PERIODO";
    public final static String TAG_PARAM_PERIODOS = "TAG_PARAM_PERIODOS";
    public final static String TAG_FIN_DE_DATOS = "TAG_FIN_DE_DATOS";
    public final static String MAX_NUMERO_SOLICITUDES_POR_RESOLUCION = "MAX_NUMERO_SOLICITUDES_POR_RESOLUCION";
    public final static String RELACION_PRERREQUISITO = "RELACION_PRERREQUISITO";
    public final static String TAG_RAYITA = "TAG_RAYITA";
    public final static String TAG_PARAM_REPORTE_SIMPLE = "TAG_PARAM_REPORTE_SIMPLE";
    //--------------------------------------------------------------------------
    // Servicios Secretaria
    //--------------------------------------------------------------------------
    //................................COMANDOS................................//
    public final static String CMD_DAR_CONVENIOS = "CMD_DAR_CONVENIOS";
    public final static String CMD_CANCELAR_SOLICITUD_POR_CARGA = "CMD_CANCELAR_SOLICITUD_POR_CARGA";
    public final static String CMD_REGISTRAR_CONVENIO_PENDIENTE_ESTUDIANTE = "CMD_REGISTRAR_CONVENIO_PENDIENTE_ESTUDIANTE";
    public final static String CMD_REGISTRAR_CONVENIO_PENDIENTE_DEPARTAMENTO = "CMD_REGISTRAR_CONVENIO_PENDIENTE_DEPARTAMENTO";
    public final static String CMD_REGISTRAR_CONVENIO_PENDIENTE_RADICACION = "CMD_REGISTRAR_CONVENIO_PENDIENTE_RADICACION";
    public final static String CMD_RADICAR_CONVENIOS = "CMD_RADICAR_CONVENIOS";
    //..................................TAGS..................................//
    public final static String TAG_PARAM_FECHA_RADICACION = "TAG_PARAM_FECHA_RADICACION";
    public final static String TAG_PARAM_NUMERO_RADICACION = "TAG_PARAM_NUMERO_RADICACION";
    //--------------------------------------------------------------------------
    // Servicios Timer
    //--------------------------------------------------------------------------
    //FIXME: Valor en produccion 864000000 (10 dias), valor en pruebas 10000 (10 secs)
    public final static String TIMER_TIMEOUT_CONFIRMACION_PROFESOR = "TIMER_TIMEOUT_CONFIRMACION_PROFESOR";
    public final static String TIMER_TIMEOUT_CONFIRMACION_ESTUDIANTE = "TIMER_TIMEOUT_CONFIRMACION_ESTUDIANTE";
    //--------------------------------------------------------------------------
    // Servicios Correo
    //--------------------------------------------------------------------------
    public final static String CONFIG_MAIL_HOST = "CONFIG_MAIL_HOST";
    public final static String CONFIG_MAIL_HOST_PORT = "CONFIG_MAIL_HOST_PORT";
    public final static String CONFIG_MAIL_USER = "CONFIG_MAIL_USER";
    public final static String CONFIG_MAIL_PASS = "CONFIG_MAIL_PASS";
    public final static String CONFIG_MAIL_MAILER = "CONFIG_MAIL_MAILER";
    public final static String TAG_PARAM_SUFIJO_CORREO = "TAG_PARAM_SUFIJO_CORREO";
    //--------------------------------------------------------------------------
    // Servicios Reglas
    //--------------------------------------------------------------------------
    //................................COMANDOS................................//
    public final static String CMD_CREAR_REGLA = "CMD_CREAR_REGLA";
    public final static String CMD_ELIMINAR_REGLA = "CMD_ELIMINAR_REGLA";
    public final static String CMD_DAR_REGLAS = "CMD_DAR_REGLAS";
    public final static String CMD_VALIDAR_REGLAS = "CMD_VALIDAR_REGLAS";
    //...................................TAGS.................................//
    public final static String TAG_REGLAS = "TAG_REGLAS";
    public final static String TAG_REGLA = "TAG_REGLA";
    public final static String TAG_PARAM_TIPO = "TAG_PARAM_TIPO";
    public final static String TAG_PARAM_SUBTIPO = "TAG_PARAM_SUBTIPO";
    public final static String TAG_PARAM_SUBTIPOS = "TAG_PARAM_SUBTIPOS";
    public final static String TAG_PARAM_VALOR = "TAG_PARAM_VALOR";
    public final static String TAG_PARAM_NOMBRE = "TAG_PARAM_NOMBRE";
    public final static String TAG_PARAM_ID_REGLA = "TAG_PARAM_ID_REGLA";
    //................................Operadores..............................//
    public final static String OP_NUMERICO = "OP_NUMERICO";
    public final static String OP_MENOR_QUE = "OP_MENOR_QUE";
    public final static String OP_MAYOR_QUE = "OP_MAYOR_QUE";
    public final static String OP_MENOR_O_IGUAL = "OP_MENOR_O_IGUAL";
    public final static String OP_DIFERENTE = "OP_DIFERENTE";
    public final static String OP_IGUAL = "OP_IGUAL";
    public final static String NO = "NO";
    public final static String OP_MAYOR_O_IGUAL = "OP_MAYOR_O_IGUAL";
    public final static String TAG_PARAM_OPERADOR = "TAG_PARAM_OPERADOR";
    public final static String TIPO_CADENA_CARACETERES = "TIPO_CADENA_CARACETERES";
    //..................................Valores...............................//
    public final static String VAL_CREDITOS_MONITORIA_ADMINISTRATIVA = "VAL_CREDITOS_MONITORIA_ADMINISTRATIVA";
    public final static String VAL_CREDITOS_MONITORIA_T1T = "VAL_CREDITOS_MONITORIA_T1T";
    public final static String VAL_CREDITOS_MONITORIA_T2 = "VAL_CREDITOS_MONITORIA_T2";
    public final static String VAL_TIPO_MONITORIA_T1 = "VAL_TIPO_MONITORIA_T1";
    public final static String VAL_TIPO_MONITORIA_T2 = "VAL_TIPO_MONITORIA_T2";
    public final static String VAL_CARGA_MONITORIA_MEDIA = "VAL_CARGA_MONITORIA_MEDIA";
    public final static String VAL_CARGA_MONITORIA_COMPLETA = "VAL_CARGA_MONITORIA_COMPLETA";
    public final static String VAL_TIPO_MONITORIA_ADMINISTRATIVA = "VAL_TIPO_MONITORIA_ADMINISTRATIVA";
    public final static String VAL_CREDITOS_MAXIMOS = "VAL_CREDITOS_MAXIMOS";
    public final static String VAL_CREDITOS_MAXIMOS_EXTRACREDITOS = "VAL_CREDITOS_MAXIMOS_EXTRACREDITOS";
    public final static String VAL_CARGA_MONITORIA_T1 = "VAL_CARGA_MONITORIA_T1";
    public final static String VAL_CARGA_MONITORIA_T2 = "VAL_CARGA_MONITORIA_T2";
    //..................................Reglas................................//
    public final static String REGLA_PROMEDIO_TOTAL = "REGLA_PROMEDIO_TOTAL";
    public final static String REGLA_PROMEDIO_TOTAL_DESCRIPCION = "REGLA_PROMEDIO_TOTAL_DESCRIPCION";
    public final static String REGLA_SEMESTRE_ACTUAL = "REGLA_SEMESTRE_ACTUAL";
    public final static String REGLA_SEMESTRE_ACTUAL_DESCRIPCION = "REGLA_SEMESTRE_ACTUAL_DESCRIPCION";
    public final static String REGLA_NOTA_MATERIA = "REGLA_NOTA_MATERIA";
    public final static String REGLA_NOTA_MATERIA_DESCRIPCION = "REGLA_NOTA_MATERIA_DESCRIPCION";
    public final static String REGLA_CREDITOS_SEMESTRE_ACTUAL = "REGLA_CREDITOS_SEMESTRE_ACTUAL";
    public final static String REGLA_CREDITOS_SEMESTRE_ACTUAL_PROMEDIO_INFERIOR_CUATRO = "REGLA_CREDITOS_SEMESTRE_ACTUAL_PROMEDIO_INFERIOR_CUATRO";
    public final static String REGLA_CREDITOS_SEMESTRE_ACTUAL_DESRIPCION = "REGLA_CREDITOS_SEMESTRE_ACTUAL_DESRIPCION";
    public final static String REGLA_BANCO_NO_VACIO_NULO = "REGLA_BANCO_NO_VACIO_NULO";
    public final static String REGLA_BANCO_NO_VACIO_NULO_DESCRIPCION = "REGLA_BANCO_NO_VACIO_NULO_DESCRIPCION";
    public final static String REGLA_CUENTA_NO_VACIA_NULA = "REGLA_CUENTA_NO_VACIA_NULA";
    public final static String REGLA_CUENTA_NO_VACIA_NULA_DESCRIPCION = "REGLA_CUENTA_NO_VACIA_NULA_DESCRIPCION";
    public final static String REGLA_TIPO_CUENTA_NO_VACIO_NULO = "REGLA_TIPO_CUENTA_NO_VACIO_NULO";
    public final static String REGLA_TIPO_CUENTA_NO_VACIO_NULO_DESCRIPCION = "REGLA_TIPO_CUENTA_NO_VACIO_NULO_DESCRIPCION";
    public final static String PROMEDIO_MINIMO_EXTRACREDITOS = "PROMEDIO_MINIMO_EXTRACREDITOS";
    public final static String REGLA_NUMERO_MAXIMO_MONITORIAS = "REGLA_NUMERO_MAXIMO_MONITORIAS";
    public final static String REGLA_NUMERO_MAXIMO_MONITORIAS_DESCRIPCION = "REGLA_NUMERO_MAXIMO_MONITORIAS_DESCRIPCION";
    public final static String REGLA_PUEDE_EXTRACREDITARSE = "REGLA_PUEDE_EXTRACREDITARSE";
    public final static String REGLA_PUEDE_EXTRACREDITARSE_DESCRIPCION = "REGLA_PUEDE_EXTRACREDITARSE_DESCRIPCION";
    //--------------------------------------------------------------------------
    // Servicios Cargar
    //--------------------------------------------------------------------------
    //................................COMANDOS................................//
    public final static String CMD_CARGAR_CARTELERA = "CMD_CARGAR_CARTELERA";
    public final static String CMD_CARGAR_INFORMACION_MONITORES = "CMD_CARGAR_INFORMACION_MONITORES";
    public final static String CMD_CARGAR_FACULTADES = "CMD_CARGAR_FACULTADES";
    public final static String CMD_GENERAR_TAREAS = "CMD_GENERAR_TAREAS";
    public final static String CMD_RETORNAR_PERIODOS_ACADEMICOS = "CMD_RETORNAR_PERIODOS_ACADEMICOS";
    //retorna un rango de periodo con periodos posteriores, anteriores, y opcionalmente con intersemestrales
    public final static String CMD_RETORNAR_PERIODOS_RANGO = "CMD_RETORNAR_PERIODOS_RANGO";
    public final static String TAG_PARAM_PERIODOS_ANTERIORES = "TAG_PARAM_PERIODOS_ANTERIORES";
    public final static String TAG_PARAM_PERIODOS_POSTERIORES = "TAG_PARAM_PERIODOS_POSTERIORES";
    public final static String TAG_PARAM_INCLUYE_INTERSEMESTRALES = "TAG_PARAM_INCLUYE_INTERSEMESTRALES";
    //--------------------------------------------------------------------------
    // Servicios Historico
    //--------------------------------------------------------------------------
    //................................COMANDOS................................//
    public final static String CMD_DAR_CRITERIOS_BUSQUEDA_HISTORICO = "CMD_DAR_CRITERIOS_BUSQUEDA_HISTORICO";
    public final static String CMD_CONSULTAR_HISTORICO = "CMD_CONSULTAR_HISTORICO";
    public final static String CMD_ACTUALIZAR_NOTA_HISTORICO = "CMD_ACTUALIZAR_NOTA_HISTORICO";
    public final static String CMD_PASAR_MONITORIAS_A_HISTORICOS = "CMD_PASAR_MONITORIAS_A_HISTORICOS";
    //...............................TAGS.....................................//
    public final static String TAG_CATEGORIAS = "TAG_CATEGORIAS";
    public final static String TAG_CATEGORIA = "TAG_CATEGORIA";
    public final static String TAG_CRITERIOS = "TAG_CRITERIOS";
    public final static String TAG_CRITERIO = "TAG_CRITERIO";
    public final static String TAG_TIPO = "TAG_TIPO";
    public final static String TAG_OPERADORES = "TAG_OPERADORES";
    public final static String TAG_OPERADOR = "TAG_OPERADOR";
    public final static String TAG_NOMBRE = "TAG_NOMBRE";
    public final static String TAG_VALOR = "TAG_VALOR";
    public final static String TAG_PERIODO_INICIAL = "TAG_PERIODO_INICIAL";
    public final static String TAG_PERIODO_FINAL = "TAG_PERIODO_FINAL";
    public final static String TAG_RESULTADOS_CONSULTA = "TAG_RESULTADOS_CONSULTA";
    public final static String TAG_RESULTADO_CONSULTA = "TAG_RESULTADO_CONSULTA";
    public final static String TAG_RESP_NOTA_MONITORIA = "TAG_RESP_NOTA_MONITORIA";
    public final static String TAG_RESP_TIPO_MONITORIA = "TAG_RESP_TIPO_MONITORIA";
    public final static String TAG_RESP_PERIODO_ACADEMICO = "TAG_RESP_PERIODO_ACADEMICO";
    public final static String TAG_POSICION = "TAG_POSICION";
    public final static String TAG_OPERADOR_BINARIO = "TAG_OPERADOR_BINARIO";
    public final static String TAG_OPERADOR_UNITARIO = "TAG_OPERADOR_UNITARIO";
    public final static String TAG_OPERADORES_BINARIOS = "TAG_OPERADORES_BINARIOS";
    public final static String TAG_OPERADORES_UNITARIOS = "TAG_OPERADORES_UNITARIOS";
    public final static String TAG_VALORES_POSIBLES = "TAG_VALORES_POSIBLES";
    public final static String TAG_VALOR_POSIBLE = "TAG_VALOR_POSIBLE";
    //................................ATRIBUTOS..............................//
    public final static String ATR_CATEGORIA = "ATR_CATEGORIA";
    //................................CATEGORIAS..............................//
    public final static String CATEGORIA_ESTUDIANTE = "CATEGORIA_ESTUDIANTE";
    public final static String CATEGORIA_PROFESOR = "CATEGORIA_PROFESOR";
    public final static String CATEGORIA_CURSO = "CATEGORIA_CURSO";
    public final static String CATEGORIA_PERIODO = "CATEGORIA_PERIODO";
    //................................CRITERIOS...............................//
    public final static String CRITERIO_NOMBRES = "CRITERIO_NOMBRES";
    public final static String CRITERIO_NOMBRE = "CRITERIO_NOMBRE";
    public final static String CRITERIO_CODIGO = "CRITERIO_CODIGO";
    public final static String CRITERIO_CORREO = "CRITERIO_CORREO";
    public final static String CRITERIO_APELLIDOS = "CRITERIO_APELLIDOS";
    public final static String CRITERIO_NOTA = "CRITERIO_NOTA";
    public final static String CRITERIO_PERIODO = "CRITERIO_PERIODO";
    //FIXME: Revisar
    public final static String[] CRITERIOS_ESTUDIANTE = {CRITERIO_NOMBRES, CRITERIO_CODIGO, CRITERIO_CORREO, CRITERIO_APELLIDOS, CRITERIO_NOTA};
    //FIXME: Revisar
    public final static String[] CRITERIOS_PROFESOR = {CRITERIO_NOMBRES, CRITERIO_APELLIDOS, CRITERIO_CORREO};
    //FIXME: Revisar
    public final static String[] CRITERIOS_CURSO = {CRITERIO_NOMBRE, CRITERIO_CODIGO};
    //FIXME: Revisar
    public final static String[] CRITERIOS_PERIODO = {CRITERIO_PERIODO};
    //................................OPERADORES..............................//
    public final static String OP_CONTIENE = "OP_CONTIENE";
    public final static String OP_AND = "OP_AND";
    public final static String OP_OR = "OP_OR";
    public final static String OP_NOT = "OP_NOT";
    public final static String OP_VACIO = "OP_VACIO";
    //................................TIPOS...................................//
    public final static String TIPO_NUMERICO = "TIPO_NUMERICO";
    public final static String TIPO_STRING = "TIPO_STRING";
    public final static String TIPO_PERIODO = "TIPO_PERIODO";
    //FIXME: Revisar
    public final static String[] CRITERIOS_CON_TIPO_STRING = {CRITERIO_NOMBRE, CRITERIO_NOMBRES, CRITERIO_CODIGO, CRITERIO_CORREO, CRITERIO_APELLIDOS};
    //FIXME: Revisar
    public final static String[] CRITERIOS_CON_TIPO_NUMERICO = {CRITERIO_NOTA};
    //FIXME: Revisar
    public final static String[] CRITERIOS_CON_TIPO_PERIODO = {CRITERIO_PERIODO};
    //FIXME: Revisar
    public final static String[] OPERADORES_NUMERICO = {OP_MENOR_QUE, OP_MAYOR_QUE, OP_MENOR_O_IGUAL, OP_MAYOR_O_IGUAL, OP_DIFERENTE, OP_IGUAL};
    //FIXME: Revisar
    public final static String[] OPERADORES_STRING = {OP_CONTIENE};
    //FIXME: Revisar
    public final static String[] OPERADORES_PERIODO = {OP_MENOR_QUE, OP_MAYOR_QUE, OP_MENOR_O_IGUAL, OP_MAYOR_O_IGUAL, OP_DIFERENTE, OP_IGUAL, OP_CONTIENE};
    //--------------------------------------------------------------------------
    // Servicios Credencial
    //--------------------------------------------------------------------------
    public final static String KEY_GENERATOR_DES = "KEY_GENERATOR_DES";
    public final static String CIPHER_PKCS5PADDING = "CIPHER_PKCS5PADDING";
    //--------------------------------------------------------------------------
    // Servicios Tareas/Alertas/Noticias
    //--------------------------------------------------------------------------
    public final static String CMD_REGENERAR_ALERTA = "CMD_REGENERAR_ALERTA";
    public final static String CMD_CONSULTAR_SECCIONES_TIPO_ESTADO = "CMD_CONSULTAR_SECCIONES_TIPO_ESTADO";
    public final static String CMD_CONSULTAR_TAREAS_CORREO_ESTADO = "CMD_CONSULTAR_TAREAS_CORREO_ESTADO";
    public final static String CMD_CONSULTAR_TAREAS_CORREO_ESTADO_TIPO = "CMD_CONSULTAR_TAREAS_CORREO_ESTADO_TIPO";
    public final static String CMD_CONSULTAR_TAREAS_CORREO_ESTADO_SIN_CADUCAR = "CMD_CONSULTAR_TAREAS_CORREO_ESTADO_SIN_CADUCAR";
    public final static String CMD_PARAMETRIZAR_FECHAS = "CMD_PARAMETRIZAR_FECHAS";
    public final static String CMD_CONSULTAR_ALERTAS_TIPO_TAREA = "CMD_CONSULTAR_ALERTAS_TIPO_TAREA";
    public final static String CMD_CONSULTAR_TAREA_ID = "CMD_CONSULTAR_TAREA_ID";
    public final static String CMD_ACTUALIZAR_ESTADO_TAREA = "CMD_ACTUALIZAR_ESTADO_TAREA";
    public final static String CMD_CONSULTAR_ALERTAS = "CMD_CONSULTAR_ALERTAS";
    public final static String CMD_CONSULTAR_ALERTAS_SIN_TAREAS = "CMD_CONSULTAR_ALERTAS_SIN_TAREAS";
    public final static String CMD_CONSULTAR_ALERTAS_SIN_TAREAS_PARA_COORDINACION = "CMD_CONSULTAR_ALERTAS_SIN_TAREAS_PARA_COORDINACION";
    public final static String CMD_CONFIRMAR_SUBIDA_ARCHIVO = "CMD_CONFIRMAR_SUBIDA_ARCHIVO";
    public final static String CMD_ACTUALIZAR_NOTA_MONITOR = "CMD_ACTUALIZAR_NOTA_MONITOR";
    public final static String CMD_DAR_PARAMETROS_TIPO_TAREA = "CMD_DAR_PARAMETROS_TIPO_TAREA";
    public final static String CMD_CONSULTAR_ID_TAREA_PARAMETROS_TIPO = "CMD_CONSULTAR_ID_TAREA_PARAMETROS_TIPO";
    public final static String TAG_PARAM_TIPO_TAREA = "TAG_PARAM_TIPO_TAREA";
    public final static String CMD_EDITAR_ALERTA_GENERICA = "CMD_EDITAR_ALERTA_GENERICA";
    public final static String TAG_PARAM_FECHA_INGRESO = "TAG_PARAM_FECHA_INGRESO";
    public final static String TAG_PARAM_FECHA_CADUCACION = "TAG_PARAM_FECHA_CADUCACION";
    public final static String TAG_PARAM_COMANDO = "TAG_PARAM_COMANDO";
    public final static String TAG_PARAM_NUEVA = "TAG_PARAM_NUEVA";
    public final static String TAG_PARAM_DURACION_ALERTA = "TAG_PARAM_DURACION_ALERTA";
    public final static String TAG_PARAM_HEADER = "TAG_PARAM_HEADER";
    public final static String TAG_PARAM_FOOTER = "TAG_PARAM_FOOTER";
    public final static String TAG_PARAM_PERSONAL = "TAG_PARAM_PERSONAL";
    public final static String TAG_PARAM_AGRUPABLE = "TAG_PARAM_AGRUPABLE";
    public final static String TAG_PARAM_TAREAS_SENCILLAS = "TAG_PARAM_TAREAS_SENCILLAS";
    public final static String TAG_PARAM_ENVIAR_CORREO_ALERTA = "TAG_PARAM_ENVIAR_CORREO_ALERTA";
    public final static String TAG_PARAM_TAREAS_PENDIENTES = "TAG_PARAM_TAREAS_PENDIENTES";
    public final static String TAG_PARAM_TAREA_PENDIENTE = "TAG_PARAM_TAREA_PENDIENTE";
    public final static String CMD_DAR_TAREAS_PENDIENTES_VENCIDAS_USUARIO = "CMD_DAR_TAREAS_PENDIENTES_VENCIDAS_USUARIO";
    public final static String VAL_PERIODICIDAD_TAREAS_VENCIDAS = "VAL_PERIODICIDAD_TAREAS_VENCIDAS";
    //--------------------------------------------------------------------------
    // Estadisticas
    //--------------------------------------------------------------------------
    ///..............................Estadisticas..............................//
    public final static String ESTADISTICA_SOLICITUDES_RECHAZADAS = "ESTADISTICA_SOLICITUDES_RECHAZADAS";
    public final static String ESTADISTICA_SOLICITUDES_RECIBIDAS = "ESTADISTICA_SOLICITUDES_RECIBIDAS";
    public final static String ESTADISTICA_CURSOS_CON_MONITORES_COMPLETOS = "ESTADISTICA_CURSOS_CON_MONITORES_COMPLETOS";
    public final static String ESTADISTICA_CURSOS_SIN_MONITORES = "ESTADISTICA_CURSOS_SIN_MONITORES";
    public final static String ESTADISTICA_CURSOS_CON_MONITORES_INCOMPLETOS = "ESTADISTICA_CURSOS_CON_MONITORES_INCOMPLETOS";
    public final static String ESTADISTICA_SECCIONES_CON_MONITORES_COMPLETOS = "ESTADISTICA_SECCIONES_CON_MONITORES_COMPLETOS";
    public final static String ESTADISTICA_SECCIONES_SIN_MONITORES = "ESTADISTICA_SECCIONES_SIN_MONITORES";
    public final static String ESTADISTICA_SECCIONES_CON_MONITORES_INCOMPLETOS = "ESTADISTICA_SECCIONES_CON_MONITORES_INCOMPLETOS";
    public final static String ESTADISTICA_MONITORIAS_POSIBLES = "ESTADISTICA_MONITORIAS_POSIBLES";
    public final static String ESTADISTICA_NUMERO_MONITORIAS_ACEPTADAS = "ESTADISTICA_NUMERO_MONITORIAS_ACEPTADAS";
    public final static String ESTADISTICA_PORCENTAJE_MONITORIAS_ACEPTADAS = "ESTADISTICA_PORCENTAJE_MONITORIAS_ACEPTADAS";
    public final static String ESTADISTICA_NUMERO_SOLICITUDES_ASPIRANTE = "ESTADISTICA_NUMERO_SOLICITUDES_ASPIRANTE";
    public final static String ESTADISTICA_NUMERO_SOLICITUDES_PENDIENTE_CONFIRMACION_PROFESOR = "ESTADISTICA_NUMERO_SOLICITUDES_PENDIENTE_CONFIRMACION_PROFESOR";
    public final static String ESTADISTICA_NUMERO_SOLICITUDES_PENDIENTE_CONFIRMACION_ESTUDIANTE = "ESTADISTICA_NUMERO_SOLICITUDES_PENDIENTE_CONFIRMACION_ESTUDIANTE";
    public final static String ESTADISTICA_NUMERO_SOLICITUDES_PENDIENTE_VERIFICACION_DATOS = "ESTADISTICA_NUMERO_SOLICITUDES_PENDIENTE_VERIFICACION_DATOS";
    public final static String ESTADISTICA_NUMERO_SOLICITUDES_PENDIENTE_REGISTRO_CONVENIO = "ESTADISTICA_NUMERO_SOLICITUDES_PENDIENTE_REGISTRO_CONVENIO";
    public final static String ESTADISTICA_NUMERO_SOLICITUDES_PENDIENTE_FIRMA_CONVENIO_ESTUDIANTE = "ESTADISTICA_NUMERO_SOLICITUDES_PENDIENTE_FIRMA_CONVENIO_ESTUDIANTE";
    public final static String ESTADISTICA_NUMERO_SOLICITUDES_PENDIENTE_FIRMA_CONVENIO_DEPARTAMENTO = "ESTADISTICA_NUMERO_SOLICITUDES_PENDIENTE_FIRMA_CONVENIO_DEPARTAMENTO";
    public final static String ESTADISTICA_NUMERO_SOLICITUDES_PENDIENTE_RADICACION = "ESTADISTICA_NUMERO_SOLICITUDES_PENDIENTE_RADICACION";
    public final static String ESTADISTICA_NUMERO_SOLICITUDES_ASIGNADAS = "ESTADISTICA_NUMERO_SOLICITUDES_ASIGNADAS";
    public final static String ESTADISTICA_NUMERO_TOTAL_SOLICITUDES = "ESTADISTICA_NUMERO_TOTAL_SOLICITUDES";
    public final static String ESTADISTICA_PORCENTAJE_MONITORIAS_ACEPTADAS_MONITORIAS_EN_PROCESO = "ESTADISTICA_PORCENTAJE_MONITORIAS_ACEPTADAS_MONITORIAS_EN_PROCESO";
    public final static String ESTADISTICA_SOLICITUDES_ELIMINADAS = "ESTADISTICA_SOLICITUDES_ELIMINADAS";
    //FIXME: CONSTANTES
    public final static String[] ESTADISTICAS_TIPO_STRING = {};
    //FIXME: CONSTANTES
    public final static String[] ESTADISTICAS_TIPO_PORCENTAJE = {ESTADISTICA_PORCENTAJE_MONITORIAS_ACEPTADAS, ESTADISTICA_PORCENTAJE_MONITORIAS_ACEPTADAS_MONITORIAS_EN_PROCESO};
    public final static String TIPO_PORCENTAJE = "TIPO_PORCENTAJE";
    //FIXME: CONSTANTES
    public final static String[] ESTADISTICAS_FIJAS_COORDINACION = {ESTADISTICA_SOLICITUDES_RECHAZADAS, ESTADISTICA_SOLICITUDES_RECIBIDAS, ESTADISTICA_CURSOS_CON_MONITORES_COMPLETOS, ESTADISTICA_CURSOS_SIN_MONITORES, ESTADISTICA_SECCIONES_CON_MONITORES_INCOMPLETOS, ESTADISTICA_MONITORIAS_POSIBLES, ESTADISTICA_NUMERO_MONITORIAS_ACEPTADAS, ESTADISTICA_NUMERO_SOLICITUDES_ASPIRANTE, ESTADISTICA_NUMERO_SOLICITUDES_PENDIENTE_CONFIRMACION_PROFESOR, ESTADISTICA_NUMERO_SOLICITUDES_PENDIENTE_CONFIRMACION_ESTUDIANTE, ESTADISTICA_NUMERO_SOLICITUDES_PENDIENTE_VERIFICACION_DATOS, ESTADISTICA_NUMERO_SOLICITUDES_PENDIENTE_REGISTRO_CONVENIO, ESTADISTICA_NUMERO_SOLICITUDES_PENDIENTE_FIRMA_CONVENIO_ESTUDIANTE, ESTADISTICA_NUMERO_SOLICITUDES_PENDIENTE_FIRMA_CONVENIO_DEPARTAMENTO, ESTADISTICA_NUMERO_SOLICITUDES_PENDIENTE_RADICACION, ESTADISTICA_NUMERO_SOLICITUDES_ASIGNADAS, ESTADISTICA_NUMERO_TOTAL_SOLICITUDES, ESTADISTICA_PORCENTAJE_MONITORIAS_ACEPTADAS_MONITORIAS_EN_PROCESO};
    //............................Comandos....................................//
    public final static String CMD_DAR_ESTADISTICAS = "CMD_DAR_ESTADISTICAS";
    //.............................TAGS.......................................//
    public final static String TAG_PARAM_ESTADISTICA = "TAG_PARAM_ESTADISTICA";
    public final static String TAG_PARAM_ESTADISTICAS = "TAG_PARAM_ESTADISTICAS";
    public final static String TAG_PARAM_ESTADISTICAS_ARREGLO = "TAG_PARAM_ESTADISTICAS_ARREGLO";
    //--------------------------------------------------------------------------
    // Tareas
    //--------------------------------------------------------------------------
    //.............................TAGS.......................................//
    public final static String TAG_PARAM_TAREAS = "TAG_PARAM_TAREAS";
    public final static String TAG_PARAM_TAREA = "TAG_PARAM_TAREA";
    public final static String TAG_PARAM_ID_TAREA = "TAG_PARAM_ID_TAREA";
    public final static String TAG_PARAM_FECHA_INICIO = "TAG_PARAM_FECHA_INICIO";
    public final static String TAG_PARAM_FECHA_FIN = "TAG_PARAM_FECHA_FIN";
    public final static String TAG_PARAM_CAMPO = "TAG_PARAM_CAMPO";
    public final static String TAG_PARAM_PARAMETROS_TAREA = "TAG_PARAM_PARAMETROS_TAREA";
    public final static String TAG_PARAM_PARAMETRO_TAREA = "TAG_PARAM_PARAMETRO_TAREA";
    //.............................Tipos......................................//
    public final static String TAG_PARAM_TIPO_TREINTA_POR_CIENTO = "TAG_PARAM_TIPO_TREINTA_POR_CIENTO";
    public final static String TAG_PARAM_TIPO_CIERRE = "TAG_PARAM_TIPO_CIERRE";
    public final static String TAG_PARAM_TIPO_PROGRAMA = "TAG_PARAM_TIPO_PROGRAMA";
    public final static String TAG_PARAM_TIPO_NOTAS_MONITORES = "TAG_PARAM_TIPO_NOTAS_MONITORES";
    public final static String TAG_PARAM_TIPO_PRESELECCION_MONITOR = "TAG_PARAM_TIPO_PRESELECCION_MONITOR";
    public final static String TAG_PARAM_TIPO_CONFIRMACION_ESTUDIANTE = "TAG_PARAM_TIPO_CONFIRMACION_ESTUDIANTE";
    public final static String TAG_PARAM_TIPO_ACTUALIZAR_DATOS_VERIFICACION = "TAG_PARAM_TIPO_ACTUALIZAR_DATOS_VERIFICACION";
    public final static String TAG_PARAM_TIPO_REGISTRAR_CONVENIO_ESTUDIANTE = "TAG_PARAM_TIPO_REGISTRAR_CONVENIO_ESTUDIANTE";
    public final static String TAG_PARAM_TIPO_REGISTRAR_FIRMA_CONVENIO_ESTUDIANTE = "TAG_PARAM_TIPO_REGISTRAR_FIRMA_CONVENIO_ESTUDIANTE";
    public final static String TAG_PARAM_TIPO_REGISTRAR_CONVENIO_FIRMA_DEPARTAMENTO = "TAG_PARAM_TIPO_REGISTRAR_CONVENIO_FIRMA_DEPARTAMENTO";
    public final static String TAG_PARAM_TIPO_RADICAR_CONVENIO = "TAG_PARAM_TIPO_RADICAR_CONVENIO";
    public final static String TAG_PARAM_TIPO_FIRMAR_CONVENIO = "TAG_PARAM_TIPO_FIRMAR_CONVENIO";
    public final static String TAG_PARAM_TIPO_CALIFICAR_ASISTENCIA_GRADUADA = "TAG_PARAM_TIPO_CALIFICAR_ASISTENCIA_GRADUADA";
    //.....Configuración cursos SIN programa/cierre/treintaPorCiento.....//
    public final static String CONFIGURACION_PARAM_CURSOS_SIN_PROGRAMA = "CONFIGURACION_PARAM_CURSOS_SIN_PROGRAMA";
    public final static String CONFIGURACION_PARAM_CURSOS_SIN_CIERRE = "CONFIGURACION_PARAM_CURSOS_SIN_CIERRE";
    public final static String CONFIGURACION_PARAM_CURSOS_SIN_TREINTA_POR_CIENTO = "CONFIGURACION_PARAM_CURSOS_SIN_TREINTA_POR_CIENTO";
    public final static String VAL_NOTIFICAR_DOCUMENTOS_PENDIENTES = "VAL_NOTIFICAR_DOCUMENTOS_PENDIENTES";
    public final static String VAL_PERIODICIDAD_NOTIFICACION_DOCUMENTOS_PENDIENTES = "VAL_PERIODICIDAD_NOTIFICACION_DOCUMENTOS_PENDIENTES";
    //--------------------------------------------------------------------------
    // Días de la semana
    //--------------------------------------------------------------------------
    //...........................VALORES-ATRIBUTOS............................//
    public final static String VAL_ATR_DIA_LUNES = "VAL_ATR_DIA_LUNES";
    public final static String VAL_ATR_DIA_MARTES = "VAL_ATR_DIA_MARTES";
    public final static String VAL_ATR_DIA_MIERCOLES = "VAL_ATR_DIA_MIERCOLES";
    public final static String VAL_ATR_DIA_JUEVES = "VAL_ATR_DIA_JUEVES";
    public final static String VAL_ATR_DIA_VIERNES = "VAL_ATR_DIA_VIERNES";
    public final static String VAL_ATR_DIA_SABADO = "VAL_ATR_DIA_SABADO";
    public final static String VAL_ATR_DIA_DOMINGO = "VAL_ATR_DIA_DOMINGO";
    //--------------------------------------------------------------------------
    // FECHAS INICIO-FIN PERIODOS
    //--------------------------------------------------------------------------
    //...........................VALORES-ATRIBUTOS............................//
    public final static String VAL_MES_INICIO_PRIMER_SEMESTRE = "VAL_MES_INICIO_PRIMER_SEMESTRE";
    public final static String VAL_MES_FIN_PRIMER_SEMESTRE = "VAL_MES_FIN_PRIMER_SEMESTRE";
    public final static String VAL_MES_INICIO_SEMESTRE_VACACIONES = "VAL_MES_INICIO_SEMESTRE_VACACIONES";
    public final static String VAL_MES_FIN_SEMESTRE_VACACIONES = "VAL_MES_FIN_SEMESTRE_VACACIONES";
    public final static String VAL_MES_INICIO_SEGUNDO_SEMESTRE = "VAL_MES_INICIO_SEGUNDO_SEMESTRE";
    public final static String VAL_MES_FIN_SEGUNDO_SEMESTRE = "VAL_MES_FIN_SEGUNDO_SEMESTRE";
    //--------------------------------------------------------------------------
    // Parametros
    //--------------------------------------------------------------------------
    public final static String TAG_PARAM_PAISES = "TAG_PARAM_PAISES";
    public final static String TAG_PARAM_PAIS = "TAG_PARAM_PAIS";
    public final static String TAG_PARAM_TIPOS_DOCUMENTO = "TAG_PARAM_TIPOS_DOCUMENTO";
    public final static String TAG_PARAM_TIPO_DOCUMENTO = "TAG_PARAM_TIPO_DOCUMENTO";
    public final static String TAG_PARAM_DESC_TIPO_DOCUMENTO = "TAG_PARAM_DESC_TIPO_DOCUMENTO";
    public final static String TAG_PARAM_LUGAR_NACIMIENTO = "TAG_PARAM_LUGAR_NACIMIENTO";
    public final static String TAG_PARAM_FACULTADES = "TAG_PARAM_FACULTADES";
    public final static String TAG_PARAM_FACULTAD = "TAG_PARAM_FACULTAD";
    public final static String TAG_PARAM_PROGRAMAS = "TAG_PARAM_PROGRAMAS";
    public final static String TAG_PARAM_PROGRAMA = "TAG_PARAM_PROGRAMA";
    ///.........................Solicitudes...................................//
    public final static String TAG_PARAM_SOLICITUDES = "TAG_PARAM_SOLICITUDES";
    public final static String TAG_PARAM_SOLICITUD = "TAG_PARAM_SOLICITUD";
    public final static String TAG_PARAM_CONFIRMACION = "TAG_PARAM_CONFIRMACION";
    ///.........................Informacion Personal..........................//
    public final static String TAG_PARAM_INFORMACION_PERSONAL = "TAG_PARAM_INFORMACION_PERSONAL";
    public final static String TAG_PARAM_CORREO = "TAG_PARAM_CORREO";
    public final static String TAG_PARAM_CORREOS = "TAG_PARAM_CORREOS";
    public final static String TAG_PARAM_CONTRASENHA = "TAG_PARAM_CONTRASENHA";
    public final static String TAG_PARAM_NOMBRES = "TAG_PARAM_NOMBRES";
    public final static String TAG_PARAM_APELLIDOS = "TAG_PARAM_APELLIDOS";
    public final static String TAG_PARAM_CODIGO_ESTUDIANTE = "TAG_PARAM_CODIGO_ESTUDIANTE";
    public final static String TAG_PARAM_PROGRAMA_ACADEMICO = "TAG_PARAM_PROGRAMA_ACADEMICO";
    public final static String TAG_PARAM_DOBLE_PROGRAMA = "TAG_PARAM_DOBLE_PROGRAMA";
    public final static String TAG_PARAM_DOCUMENTOS = "TAG_PARAM_DOCUMENTOS";
    public final static String TAG_PARAM_DOCUMENTO = "TAG_PARAM_DOCUMENTO";
    public final static String TAG_PARAM_FECHA_NACIMIENTO = "TAG_PARAM_FECHA_NACIMIENTO";
    public final static String TAG_PARAM_DIRECCION_RESIDENCIA = "TAG_PARAM_DIRECCION_RESIDENCIA";
    public final static String TAG_PARAM_TELEFONO_RESIDENCIA = "TAG_PARAM_TELEFONO_RESIDENCIA";
    public final static String TAG_PARAM_EXTENSION = "TAG_PARAM_EXTENSION";
    public final static String TAG_PARAM_OFICINA = "TAG_PARAM_OFICINA";
    public final static String TAG_PARAM_CELULAR = "TAG_PARAM_CELULAR";
    public final static String TAG_PARAM_BANCO = "TAG_PARAM_BANCO";
    public final static String TAG_PARAM_TIPO_CUENTA = "TAG_PARAM_TIPO_CUENTA";
    public final static String TAG_PARAM_CUENTA = "TAG_PARAM_CUENTA";
    ////..................................Valores.............................//   
    public final static String VAL_TAG_TIPO_CUENTA_CORRIENTE = "VAL_TAG_TIPO_CUENTA_CORRIENTE";
    public final static String VAL_TAG_TIPO_CUENTA_AHORROS = "VAL_TAG_TIPO_CUENTA_AHORROS";
    public final static String VAL_TAG_SITUACION_ACADEMICA_NORMAL = "VAL_TAG_SITUACION_ACADEMICA_NORMAL";
    public final static String VAL_TAG_SITUACION_ACADEMICA_REINTEGRO = "VAL_TAG_SITUACION_ACADEMICA_REINTEGRO";
    ///.........................Informacion Emergencia........................//
    public final static String TAG_PARAM_INFORMACION_EMERGENCIA = "TAG_PARAM_INFORMACION_EMERGENCIA";
    public final static String TAG_PARAM_TELEFONO_EMERGENCIA = "TAG_PARAM_TELEFONO_EMERGENCIA";
    ///........................Informacion Academica..........................//
    public final static String TAG_PARAM_INFORMACION_ACADEMICA = "TAG_PARAM_INFORMACION_ACADEMICA";
    public final static String TAG_PARAM_SEMESTRE_SEGUN_CREDITOS = "TAG_PARAM_SEMESTRE_SEGUN_CREDITOS";
    public final static String TAG_PARAM_PROMEDIO_TOTAL = "TAG_PARAM_PROMEDIO_TOTAL";
    public final static String TAG_PARAM_CREDITOS_SEMESTRE_ACTUAL = "TAG_PARAM_CREDITOS_SEMESTRE_ACTUAL";
    public final static String TAG_PARAM_CREDITOS_MONITORIAS_ISIS = "TAG_PARAM_CREDITOS_MONITORIAS_ISIS";
    public final static String TAG_PARAM_CREDITOS_APROBADOS = "TAG_PARAM_CREDITOS_APROBADOS";
    public final static String TAG_PARAM_CREDITOS_CURSADOS = "TAG_PARAM_CREDITOS_CURSADOS";
    public final static String TAG_PARAM_PROMEDIO_ULTIMO = "TAG_PARAM_PROMEDIO_ULTIMO";
    public final static String TAG_PARAM_PROMEDIO_HACE_DOS_SEMESTRES = "TAG_PARAM_PROMEDIO_HACE_DOS_SEMESTRES";
    public final static String TAG_PARAM_PROMEDIO_HACE_TRES_SEMESTRES = "TAG_PARAM_PROMEDIO_HACE_TRES_SEMESTRES";
    ///........................Informacion Monitoria..........................//
    public final static String TAG_PARAM_MONITORIA_SOLICITADA = "TAG_PARAM_MONITORIA_SOLICITADA";
    public final static String TAG_PARAM_TIPO_MONITORIA = "TAG_PARAM_TIPO_MONITORIA";
    public final static String TAG_PARAM_CARGA_MONITORIA = "TAG_PARAM_CARGA_MONITORIA";
    public final static String TAG_PARAM_NOTA = "TAG_PARAM_NOTA";
    public final static String TAG_PARAM_MONITORIAS_TIENE_PERMISOS_ESPECIALES = "TAG_PARAM_MONITORIAS_TIENE_PERMISOS_ESPECIALES";
    ////........................Informacion Cursos............................//
    public final static String TAG_PARAM_CURSOS = "TAG_PARAM_CURSOS";
    /////........................Informacion Curso............................//
    public final static String TAG_PARAM_CURSO = "TAG_PARAM_CURSO";
    public final static String TAG_PARAM_CODIGO_CURSO = "TAG_PARAM_CODIGO_CURSO";
    public final static String TAG_PARAM_NOMBRE_CURSO = "TAG_PARAM_NOMBRE_CURSO";
    public final static String TAG_PARAM_PRESENCIAL = "TAG_PARAM_PRESENCIAL";
    public final static String TAG_PARAM_CURSO_VISTO_REQUERIDO = "TAG_PARAM_CURSO_VISTO_REQUERIDO";
    ///...............Informacion Monitorias Otros Departamentos...............//
    public final static String TAG_PARAM_MONITORIAS_OTRO_DEPTOS = "TAG_PARAM_MONITORIAS_OTRO_DEPTOS";
    public final static String TAG_PARAM_MONITORIA_OTRO_DEPTO = "TAG_PARAM_MONITORIA_OTRO_DEPTO";
    //////.....................Informacion Secciones..........................//
    public final static String TAG_PARAM_SECCIONES = "TAG_PARAM_SECCIONES";
    ///////.......................Informacion Seccion.........................//
    public final static String TAG_PARAM_SECCION = "TAG_PARAM_SECCION";
    public final static String TAG_PARAM_CRN_SECCION = "TAG_PARAM_CRN_SECCION";
    public final static String TAG_PARAM_NUMERO_SECCION = "TAG_PARAM_NUMERO_SECCION";
    public final static String TAG_PARAM_MAX_CANTIDAD_MONITORES = "TAG_PARAM_MAX_CANTIDAD_MONITORES";
    public final static String CMD_CAMBIAR_CANTIDAD_MONITORES_SECCION = "CMD_CAMBIAR_CANTIDAD_MONITORES_SECCION";
    ////////.....................Informacion Profesor.........................//
    public final static String TAG_PARAM_PROFESOR = "TAG_PARAM_PROFESOR";
    public final static String TAG_PARAM_PROFESOR_CATEDRA = "TAG_PARAM_PROFESOR_CATEDRA";
    public final static String TAG_PARAM_PROFESORES = "TAG_PARAM_PROFESORES";
    public final static String TAG_PARAM_NOMBRE_COMPLETO = "TAG_PARAM_NOMBRE_COMPLETO";
    public final static String TAG_PARAM_CORREO_NUEVO = "TAG_PARAM_CORREO_NUEVO";
    public final static String TAG_PARAM_CORREO_NUEVO_PROFESOR = "TAG_PARAM_CORREO_NUEVO_PROFESOR";
    ////......................Informacion Curso Visto.........................//
    public final static String TAG_PARAM_CURSO_VISTO = "TAG_PARAM_CURSO_VISTO";
    public final static String TAG_PARAM_NOTA_MATERIA = "TAG_PARAM_NOTA_MATERIA";
    public final static String TAG_PARAM_PERIODO_ACADEMICO = "TAG_PARAM_PERIODO_ACADEMICO";
    public final static String TAG_PARAM_PROFESOR_CON_QUIEN_LA_VIO = "TAG_PARAM_PROFESOR_CON_QUIEN_LA_VIO";
    ///...............................Horario.................................//
    public final static String TAG_PARAM_HORARIO = "TAG_PARAM_HORARIO";
    public final static String TAG_PARAM_HAY_CONFLICTO = "TAG_PARAM_HAY_CONFLICTO";
    public final static String TAG_PARAM_HORARIOS_MONITORIAS = "TAG_PARAM_HORARIOS_MONITORIAS";
    public final static String TAG_PARAM_FRANJAS_OCUPADAS = "TAG_PARAM_FRANJAS_OCUPADAS";
    public final static String TAG_PARAM_FRANJA_OCUPADA = "TAG_PARAM_FRANJA_OCUPADA";
    //..............................Atributos.................................//
    ///..........................Horario......................................//
    public final static String ATR_DIA = "ATR_DIA";
    public final static String ATR_HORA_INICIO = "ATR_HORA_INICIO";
    public final static String ATR_MINUTO_INICIO = "ATR_MINUTO_INICIO";
    public final static String ATR_HORA_FIN = "ATR_HORA_FIN";
    public final static String ATR_MINUTO_FIN = "ATR_MINUTO_FIN";
    ////...........................VALORES RANGO HORARIO......................//
    public final static String VAL_ATR_HORA_INICIO_ESTUDIANTE = "VAL_ATR_HORA_INICIO_ESTUDIANTE";
    public final static String VAL_ATR_MINUTO_INICIO_ESTUDIANTE = "VAL_ATR_MINUTO_INICIO_ESTUDIANTE";
    public final static String VAL_ATR_HORA_FIN_ESTUDIANTE = "VAL_ATR_HORA_FIN_ESTUDIANTE";
    public final static String VAL_ATR_MINUTO_FIN_ESTUDIANTE = "VAL_ATR_MINUTO_FIN_ESTUDIANTE";
    ///...............................Facultad................................//
    public final static String ATR_NOMBRE_FACULTAD = "ATR_NOMBRE_FACULTAD";
    ///...............................Alertas.................................//
    public final static String TAG_PARAM_PARAMETROS_ALERTA = "TAG_PARAM_PARAMETROS_ALERTA";
    public final static String TAG_PARAM_PARAMETRO_ALERTA = "TAG_PARAM_PARAMETRO_ALERTA";
    public final static String TAG_PARAM_ALERTAS = "TAG_PARAM_ALERTAS";
    public final static String TAG_PARAM_ALERTA = "TAG_PARAM_ALERTA";
    public final static String TAG_PARAM_ID_ALERTA = "TAG_PARAM_ID_ALERTA";
    public final static String TAG_PARAM_MENSAJE = "TAG_PARAM_MENSAJE";
    public final static String TAG_PARAM_TIPO_ALERTA = "TAG_PARAM_TIPO_ALERTA";
    public final static String TAG_PARAM_TIPO_INTERVALO = "TAG_PARAM_TIPO_INTERVALO";
    public final static String TAG_PARAM_DURACION_TAREA = "TAG_PARAM_DURACION_TAREA";
    public final static String TAG_PARAM_ASUNTO = "TAG_PARAM_ASUNTO";
    public final static String TAG_PARAM_DESTINATARIOS = "TAG_PARAM_DESTINATARIOS";
    public final static String TAG_PARAM_DESTINATARIOSCC = "TAG_PARAM_DESTINATARIOSCC";
    public final static String TAG_PARAM_DESTINATARIOSCCO = "TAG_PARAM_DESTINATARIOSCCO";
    public final static String TAG_PARAM_PERIODICIDADES = "TAG_PARAM_PERIODICIDADES";
    public final static String TAG_PARAM_PERIODICIDAD = "TAG_PARAM_PERIODICIDAD";
    public final static String TAG_PARAM_ACTIVA = "TAG_PARAM_ACTIVA";
    public final static String TAG_PARAM_ENVIA_CORREO = "TAG_PARAM_ENVIA_CORREO";
    public final static String TAG_PARAM_PERMITE_PENDIENTE = "TAG_PARAM_PERMITE_PENDIENTE";
    public final static String VAL_TIPO_INTERVALO_RANGO = "VAL_TIPO_INTERVALO_RANGO";
    public final static String VAL_TIPO_INTERVALO_INTERVALO = "VAL_TIPO_INTERVALO_INTERVALO";
    public final static String CMD_CREAR_ALERTAS = "CMD_CREAR_ALERTAS";
    public final static String CMD_EDITAR_ALERTA = "CMD_EDITAR_ALERTA";
    public final static String TAG_PARAM_CREADA = "TAG_PARAM_CREADA";
    public final static String CMD_DAR_TIPOS_ALERTAS = "CMD_DAR_TIPOS_ALERTAS";
    public final static String CMD_CONSULTAR_PARAMETROS_ALERTA = "CMD_CONSULTAR_PARAMETROS_ALERTA";
    public final static String CMD_CONSULTAR_PARAMETROS_ALERTAS = "CMD_CONSULTAR_PARAMETROS_ALERTAS";
    public final static String CMD_ELIMINAR_ALERTA = "CMD_ELIMINAR_ALERTA";
    public final static String CMD_DAR_PERIODICIDADES = "CMD_DAR_PERIODICIDADES";
    public final static String CMD_PAUSAR_ALERTA = "CMD_PAUSAR_ALERTA";
    public final static String CMD_REANUDAR_ALERTA = "CMD_REANUDAR_ALERTA";
    ///...............................Votaciones.................................//
    public final static String TAG_PARAM_CANDIDATOS = "TAG_PARAM_CANDIDATOS";
    public final static String TAG_PARAM_CANDIDATO = "TAG_PARAM_CANDIDATO";
    public final static String TAG_PARAM_VOTANTES = "TAG_PARAM_VOTANTES";
    public final static String TAG_PARAM_VOTANTE = "TAG_PARAM_VOTANTE";
    public final static String TAG_PARAM_VOTACIONES = "TAG_PARAM_VOTACIONES";
    public final static String TAG_PARAM_VOTACION = "TAG_PARAM_VOTACION";
    public final static String TAG_PARAM_ID_VOTACION = "TAG_PARAM_ID_VOTACION";
    public final static String TAG_PARAM_DESCRIPCION = "TAG_PARAM_DESCRIPCION";
    public final static String VAL_TIPO_ESTADO_ABIERTA = "VAL_TIPO_ESTADO_ABIERTA";
    public final static String VAL_TIPO_ESTADO_CERRADA = "VAL_TIPO_ESTADO_CERRADA";
    public final static String TAG_PARAM_ID_CANDIDATO = "TAG_PARAM_ID_CANDIDATO";
    public final static String TAG_PARAM_NUMERO_VOTOS = "TAG_PARAM_NUMERO_VOTOS";
    public final static String TAG_PARAM_ID_VOTANTE = "TAG_PARAM_ID_VOTANTE";
    public final static String TAG_PARAM_YA_VOTO = "TAG_PARAM_YA_VOTO";
    public final static String CMD_CREAR_VOTACION = "CMD_CREAR_VOTACION";
    public final static String CMD_DAR_PERSONAS_SIN_VOTACION = "CMD_DAR_PERSONAS_SIN_VOTACION";
    public final static String CMD_DAR_VOTACIONES_POR_CORREO = "CMD_DAR_VOTACIONES_POR_CORREO";
    public final static String CMD_DAR_ESTADO_VOTANTES_POR_ID_VOTACION = "CMD_DAR_ESTADO_VOTANTES_POR_ID_VOTACION";
    public final static String TAG_PARAM_NUMERO_CANDIDATOS = "TAG_PARAM_NUMERO_CANDIDATOS";
    public final static String TAG_PARAM_NUMERO_VOTANTES = "TAG_PARAM_NUMERO_VOTANTES";
    public final static String TAG_PARAM_VOTOS_REALIZADOS = "TAG_PARAM_VOTOS_REALIZADOS";
    public final static String VAL_PARAMETRO_CRN_SECCION = "VAL_PARAMETRO_CRN_SECCION";
    public final static String VAL_PARAMETRO_NUMERO_SECCION = "VAL_PARAMETRO_NUMERO_SECCION";
    public final static String VAL_PARAMETRO_NOMBRE_CURSO = "VAL_PARAMETRO_NOMBRE_CURSO";
    public final static String VAL_PARAMETRO_CODIGO_CURSO = "VAL_PARAMETRO_CODIGO_CURSO";
    public final static String VAL_PARAMETRO_NOMBRE_PROFESOR = "VAL_PARAMETRO_NOMBRE_PROFESOR";
    public final static String VAL_PARAMETRO_CRN_SECCION2 = "VAL_PARAMETRO_CRN_SECCION2";
    public final static String VAL_PARAMETRO_NUMERO_SECCION2 = "VAL_PARAMETRO_NUMERO_SECCION2";
    public final static String VAL_PARAMETRO_NOMBRE_CURSO2 = "VAL_PARAMETRO_NOMBRE_CURSO2";
    public final static String VAL_PARAMETRO_CODIGO_CURSO2 = "VAL_PARAMETRO_CODIGO_CURSO2";
    public final static String VAL_PARAMETRO_NOMBRE_PROFESOR2 = "VAL_PARAMETRO_NOMBRE_PROFESOR2";
    public final static String VAL_PARAMETRO_CANTIDAD_MONITORES = "VAL_PARAMETRO_CANTIDAD_MONITORES";
    public final static String VAL_PARAMETRO_NOMBRE_MONITOR = "VAL_PARAMETRO_NOMBRE_MONITOR";
    public final static String VAL_PARAMETRO_CODIGO_MONITOR = "VAL_PARAMETRO_CODIGO_MONITOR";
    public final static String VAL_PARAMETRO_CORREO_MONITOR = "VAL_PARAMETRO_CORREO_MONITOR";
    public final static String VAL_PARAMETRO_ID_ASISTENCIA_GRADUADA = "VAL_PARAMETRO_ID_ASISTENCIA_GRADUADA";
    ///...............................Archivos................................//
    public final static String TAG_PARAM_CAMPO_RUTA_DIRECTORIO = "TAG_PARAM_CAMPO_RUTA_DIRECTORIO";
    public final static String TAG_PARAM_MIME = "TAG_PARAM_MIME";
    ///.............................Monitores.................................//
    public final static String TAG_PARAM_MONITORES = "TAG_PARAM_MONITORES";
    public final static String TAG_PARAM_MONITOR = "TAG_PARAM_MONITOR";
    //--------------------------------------------------------------------------
    //FIXME Clasificarlos donde tienen que ir y ver si no hay repetidos
    public final static String MAX_HORARIO_DIA = "MAX_HORARIO_DIA";
    //Nucleo
    public final static String MSJ_TIPO_COMANDO_INVALIDO = "MSJ_TIPO_COMANDO_INVALIDO";
    public final static String MSJ_CONSULTA_INVALIDA = "MSJ_CONSULTA_INVALIDA";
    public final static String MSJ_PROCESO_INVALIDO = "MSJ_PROCESO_INVALIDO";
    public final static String TAG_PARAM_CANTIDAD_ACTUAL_MONITORES = "TAG_PARAM_CANTIDAD_ACTUAL_MONITORES";
    public final static String TAG_PARAMETROS_MENSAJE = "TAG_PARAMETROS_MENSAJE";
    public final static String TAG_PARAMETRO_MENSAJE = "TAG_PARAMETRO_MENSAJE";
    //NUEVAS CONSTANTES PARA BOLSA DE EMPLEO
    //EstudianteBean
    public final static String TAG_PARAM_ID_ESTUDIANTE = "TAG_PARAM_ID_ESTUDIANTE";
    public final static String TAG_PARAM_CONTENIDO = "TAG_PARAM_CONTENIDO";
    public final static String TAG_PARAM_HOJA_VIDA = "TAG_PARAM_HOJA_VIDA";
    public final static String TAG_PARAM_TITULO = "TAG_PARAM_TITULO";
    public final static String TAG_PARAM_NOMBRE_UNIVERSIDAD = "TAG_PARAM_NOMBRE_UNIVERSIDAD";
    public final static String TAG_PARAM_FECHA_GRADUACION = "TAG_PARAM_FECHA_GRADUACION";
    public final static String TAG_PARAM_CIUDAD = "TAG_PARAM_CIUDAD";
    public final static String TAG_PARAM_NACIONALIDAD = "TAG_PARAM_NACIONALIDAD";
    public final static String TAG_PARAM_CORREO_ALTERNO = "TAG_PARAM_CORREO_ALTERNO";
    public final static String TAG_PARAM_ES_EXTERNO = "TAG_PARAM_ES_EXTERNO";
    public final static String TAG_PARAM_FECHA_CREACION = "TAG_PARAM_FECHA_CREACION";
    public final static String TAG_PARAM_FECHA_ACTUALIZACION = "TAG_PARAM_FECHA_ACTUALIZACION";
    public final static String TAG_PARAM_ES_PRIMER_SEMESTRE_MAESTRIA = "TAG_PARAM_ES_PRIMER_SEMESTRE_MAESTRIA";
    public final static String CMD_ACTUALIZAR_HOJA_VIDA = "CMD_ACTUALIZAR_HOJA_VIDA";
    public final static String CMD_ACTUALIZAR_INFORMACION_ACADEMICA = "CMD_ACTUALIZAR_INFORMACION_ACADEMICA";
    public final static String CMD_ACTUALIZAR_INFORMACION_PERSONAL = "CMD_ACTUALIZAR_INFORMACION_PERSONAL";
    public final static String CMD_CONSULTAR_ESTUDIANTE = "CMD_CONSULTAR_ESTUDIANTE";
    public final static String CMD_CONSULTAR_ESTUDIANTE_POR_ID = "CMD_CONSULTAR_ESTUDIANTE_POR_ID";
    public final static String CMD_CONSULTAR_HOJA_VIDA = "CMD_CONSULTAR_HOJA_VIDA";
    public final static String CMD_CONSULTAR_ESTUDIANTES = "CMD_CONSULTAR_ESTUDIANTES";
    public final static String CMD_CONSULTAR_INFORMACION_ACADEMICA = "CMD_CONSULTAR_INFORMACION_ACADEMICA";
    public final static String CMD_CONSULTAR_INFORMACION_PERSONAL = "CMD_CONSULTAR_INFORMACION_PERSONAL";
    public final static String CMD_CREAR_ESTUDIANTE = "CMD_CREAR_ESTUDIANTE";
    public final static String CMD_ELIMINAR_ESTUDIANTE = "CMD_ELIMINAR_ESTUDIANTE";
    public final static String CMD_CARGAR_ESTUDIANTES_EXTERNOS_POR_ARCHIVO = "CMD_CARGAR_ESTUDIANTES_EXTERNOS_POR_ARCHIVO";
    //Administración de Estudiantes (EstudianteBean)
    public final static String CMD_CREAR_ESTUDIANTE_PERSONA = "CMD_CREAR_ESTUDIANTE_PERSONA";
    public final static String CMD_CONSULTAR_ESTUDIANTES_PERSONAS = "CMD_CONSULTAR_ESTUDIANTES_PERSONAS";
    public final static String CMD_CONSULTAR_ESTUDIANTE_PERSONA = "CMD_CONSULTAR_ESTUDIANTE_PERSONA";
    public final static String CMD_ELIMINAR_ESTUDIANTE_PERSONA = "CMD_ELIMINAR_ESTUDIANTE_PERSONA";
    public final static String CMD_ACTUALIZAR_ESTADO_ACTIVO_ESTUDIANTE = "CMD_ACTUALIZAR_ESTADO_ACTIVO_ESTUDIANTE";
    //Administración de Profesores (ProfesorBean)
    public final static String CMD_CREAR_PROFESOR_PERSONA = "CMD_CREAR_PROFESOR_PERSONA";
    public final static String CMD_CONSULTAR_PROFESORES_PERSONAS = "CMD_CONSULTAR_PROFESORES_PERSONAS";
    public final static String CMD_CONSULTAR_PROFESOR_PERSONA = "CMD_CONSULTAR_PROFESOR_PERSONA";
    public final static String CMD_ELIMINAR_PROFESOR_PERSONA = "CMD_ELIMINAR_PROFESOR_PERSONA";
    // public final static String CMD_CONSULTAR_PERSONA = "CMD_CONSULTAR_PERSONA";
    public final static String CMD_ACTUALIZAR_ESTADO_ACTIVO_PROFESOR = "CMD_ACTUALIZAR_ESTADO_ACTIVO_PROFESOR";
    //ProponenteBean
    public final static String TAG_PARAM_ID_PROPONENTE = "TAG_PARAM_ID_PROPONENTE";
    public final static String TAG_PARAM_PROPONENTE = "TAG_PARAM_PROPONENTE";
    public final static String TAG_PARAM_CARGO = "TAG_PARAM_CARGO";
    public final static String TAG_PARAM_TELEFONO = "TAG_PARAM_TELEFONO";
    public final static String TAG_PARAM_ES_EMPRESA = "TAG_PARAM_ES_EMPRESA";
    public final static String TAG_PARAM_EMPRESA = "TAG_PARAM_EMPRESA";
    public final static String CMD_CONSULTAR_PROPONENTE = "CMD_CONSULTAR_PROPONENTE";
    public final static String CMD_CONSULTAR_PROPONENTES = "CMD_CONSULTAR_PROPONENTES";
    public final static String TAG_PARAM_NOMBRE_EMPRESA = "TAG_PARAM_NOMBRE_EMPRESA";
    public final static String TAG_PARAM_TELEFONO_FIJO = "TAG_PARAM_TELEFONO_FIJO";
    public final static String TAG_PARAM_ROL = "TAG_PARAM_ROL";
    public final static String CMD_ELIMINAR_PROPONENTE = "CMD_ELIMINAR_PROPONENTE";
    public final static String CMD_CREAR_PROPONENTE = "CMD_CREAR_PROPONENTE";
    public final static String VAL_TAG_TODOS = "VAL_TAG_TODOS";
    public final static String VAL_TAG_EMPRESA = "VAL_TAG_EMPRESA";
    public final static String TAG_PARAM_PROPONENTES = "TAG_PARAM_PROPONENTES";
    public final static String CMD_ACTUALIZAR_PROPONENTE = "CMD_ACTUALIZAR_PROPONENTE";
    public final static String CMD_CONSULTAR_PROPONENTE_POR_LOGIN = "CMD_CONSULTAR_PROPONENTE_POR_LOGIN";
    //OfertaBean
    public final static String CMD_ACTUALIZAR_OFERTA = "CMD_ACTUALIZAR_OFERTA";
    public final static String TAG_PARAM_ID_OFERTA = "TAG_PARAM_ID_OFERTA";
    public final static String TAG_PARAM_DIRECCION_WEB = "TAG_PARAM_DIRECCION_WEB";
    public final static String TAG_PARAM_FECHA_INICIO_VINCULACION = "TAG_PARAM_FECHA_INICIO_VINCULACION";
    public final static String TAG_PARAM_PERIODO_VINCULACION = "TAG_PARAM_PERIODO_VINCULACION";
    public final static String TAG_PARAM_REQUISITOS = "TAG_PARAM_REQUISITOS";
    public final static String CMD_CONSULTAR_OFERTA = "CMD_CONSULTAR_OFERTA";
    public final static String TAG_PARAM_OFERTA = "TAG_PARAM_OFERTA";
    public final static String TAG_PARAM_OFERTAS = "TAG_PARAM_OFERTAS";
    public final static String CMD_CONSULTAR_OFERTAS = "CMD_CONSULTAR_OFERTAS";
    public final static String CMD_CONSULTAR_OFERTAS_PROPONENTE = "CMD_CONSULTAR_OFERTAS_PROPONENTE";
    public final static String CMD_CREAR_OFERTA = "CMD_CREAR_OFERTA";
    public final static String CMD_ELIMINAR_OFERTA = "CMD_ELIMINAR_OFERTA";
    public final static String CMD_APLICAR_OFERTA = "CMD_APLICAR_OFERTA";
    public final static String TAG_PARAM_ESTADO_OFERTA = "TAG_PARAM_ESTADO_OFERTA";
    public final static String CTE_OFERTA_VIGENTE = "CTE_OFERTA_VIGENTE";
    public final static String CTE_OFERTA_VENCIDA = "CTE_OFERTA_VENCIDA";
    public final static String CTE_OFERTA_RETIRADA = "CTE_OFERTA_RETIRADA";
    public final static String VAL_CORREOS_NOTIFICAR_OFERTA = "VAL_CORREOS_NOTIFICAR_OFERTA";
    //ArchivosBean
    public final static String CMD_DAR_ARCHIVOS_PROFESOR = "CMD_DAR_ARCHIVOS_PROFESOR";
    public final static String TAG_PARAM_ARCHIVOS = "TAG_PARAM_ARCHIVOS";
    public final static String TAG_PARAM_ARCHIVO = "TAG_PARAM_ARCHIVO";
    public final static String TAG_PARAM_SUBIDO = "TAG_PARAM_SUBIDO";
    public final static String TAG_PARAM_ID_ARCHIVO = "TAG_PARAM_ID_ARCHIVO";
    public final static String TAG_PARAM_NOMBRE_ARCHIVO = "TAG_PARAM_NOMBRE_ARCHIVO";
    public final static String TAG_PARAM_TIPO_ARCHIVO = "TAG_PARAM_TIPO_ARCHIVO";
    public final static String CMD_DAR_INFO_ARCHIVO = "CMD_DAR_INFO_ARCHIVO";
    public final static String CMD_OBTENER_RUTA_DIRECTORIO_ARCHIVO = "CMD_OBTENER_RUTA_DIRECTORIO_ARCHIVO";
    public final static String CMD_CONFIRMAR_REEMPLAZO_ARCHIVO = "CMD_CONFIRMAR_REEMPLAZO_ARCHIVO";
    public final static String TAG_PARAM_RUTA_DIRECTORIO = "TAG_PARAM_RUTA_DIRECTORIO";
    //Periodicidades - AlertaBean
    public final static String VAL_PERIODICIDAD_HORA = "VAL_PERIODICIDAD_HORA";
    public final static String VAL_PERIODICIDAD_DOS_HORAS = "VAL_PERIODICIDAD_DOS_HORAS";
    public final static String VAL_PERIODICIDAD_DOCE_HORAS = "VAL_PERIODICIDAD_DOCE_HORAS";
    public final static String VAL_PERIODICIDAD_DIARIO = "VAL_PERIODICIDAD_DIARIO";
    public final static String VAL_PERIODICIDAD_DOS_DIAS = "VAL_PERIODICIDAD_DOS_DIAS";
    public final static String VAL_PERIODICIDAD_TRES_DIAS = "VAL_PERIODICIDAD_TRES_DIAS";
    public final static String VAL_PERIODICIDAD_SEMANAL = "VAL_PERIODICIDAD_SEMANAL";
    public final static String VAL_PERIODICIDAD_MENSUAL = "VAL_PERIODICIDAD_MENSUAL";
    //NUEVAS CONSTANTES PARA DOCUMENTOS PRIVADOS
    public final static String VAL_RUTABASE_DIRECTORIO_DOCPRIVADOS = "VAL_RUTABASE_DIRECTORIO_DOCPRIVADOS";
    public final static String VAL_RUTABASE_DIRECTORIO_ARCHIVOS = "VAL_RUTABASE_DIRECTORIO_ARCHIVOS";
    public final static String TAG_PARAM_IDDOCUMENTO = "TAG_PARAM_IDDOCUMENTO";
    public final static String TAG_PARAM_PALABRAS_CLAVE = "TAG_PARAM_PALABRAS_CLAVE";
    public final static String TAG_PARAM_SORTBY = "TAG_PARAM_SORTBY";
    public final static String TAG_PARAM_ORDEN = "TAG_PARAM_ORDEN";
    public final static String TAG_PARAM_INDICE = "TAG_PARAM_INDICE";
    public final static String TAG_PARAM_NUMERO_RESULTADOS = "TAG_PARAM_NUMERO_RESULTADOS";
    public final static String TAG_PARAM_FECHA_PUBLICACION = "TAG_PARAM_FECHA_PUBLICACION";
    public final static String TAG_PARAM_TIPO_ACTUALIZAR_DATOS_PUBLICACION = "TAG_PARAM_TIPO_ACTUALIZAR_DATOS_PUBLICACION";
    public final static String CMD_ACTUALIZAR_METADATOS_DOCPRIVADO = "CMD_ACTUALIZAR_METADATOS_DOCPRIVADO";
    public final static String CMD_CONFIRMAR_SUBIDA_DOCPRIVADO = "CMD_CONFIRMAR_SUBIDA_DOCPRIVADO";
    public final static String CMD_CONSULTAR_DATOS_DOCPRIVADO = "CMD_CONSULTAR_DATOS_DOCPRIVADO";
    public final static String CMD_CONSULTAR_DOCUMENTOS_PRIVADOS = "CMD_CONSULTAR_DOCUMENTOS_PRIVADOS";
    public final static String CMD_DAR_INFO_DESCARGA_DOCPRIVADO = "CMD_DAR_INFO_DESCARGA_DOCPRIVADO";
    public final static String CMD_ELIMINAR_DOCPRIVADO = "CMD_ELIMINAR_DOCPRIVADO";
    public final static String CMD_SUBIR_METADATOS_DOCPRIVADO = "CMD_SUBIR_METADATOS_DOCPRIVADO";
    //Publicaciones Bean
    public final static String TAG_PARAM_GRUPO_INVESTIGACION = "TAG_PARAM_GRUPO_INVESTIGACION";
    public final static String TAG_PARAM_GRUPOS_INVESTIGACION = "TAG_PARAM_GRUPOS_INVESTIGACION";
    public final static String TAG_PARAM_DATOS_COMPLETOS_PROFESOR = "TAG_PARAM_DATOS_COMPLETOS_PROFESOR";
    public final static String CMD_ELIMINAR_PROFESOR = "CMD_ELIMINAR_PROFESOR";
    public final static String TAG_PARAM_PAGINA = "TAG_PARAM_PAGINA";
    public final static String CMD_AGREGAR_PROFESORES = "CMD_AGREGAR_PROFESORES";
    public final static String CMD_AGREGAR_GRUPO_INVESTIGACION = "CMD_AGREGAR_GRUPO_INVESTIGACION";
    public final static String CMD_AGREGAR_GRUPOS_INVESTIGACION = "CMD_AGREGAR_GRUPOS_INVESTIGACION";
    public final static String CMD_ELIMINAR_GRUPO_INVESTIGACION = "CMD_ELIMINAR_GRUPO_INVESTIGACION";
    public final static String CMD_MODIFICAR_GRUPO_INVESTIGACION = "CMD_MODIFICAR_GRUPO_INVESTIGACION";
    public final static String TAG_PARAM_PERTENECE = "TAG_PARAM_PERTENECE";
    public final static String CMD_CONSULTAR_PROFESOR_PERTENECE_A_GRUPO_INVESTIGACION = "CMD_CONSULTAR_PROFESOR_PERTENECE_A_GRUPO_INVESTIGACION";
    public final static String CMD_CONSULTAR_PROFESORES_GRUPO_INVESTIGACION = "CMD_CONSULTAR_PROFESORES_GRUPO_INVESTIGACION";
    public final static String CMD_CONSULTAR_PROFESORES_TIPO = "CMD_CONSULTAR_PROFESORES_TIPO";
    public final static String CMD_CONSULTAR_GRUPOS_INVESTIGACION = "CMD_CONSULTAR_GRUPOS_INVESTIGACION";
    //--------------------------------------------------------------------------
    // Material Bibliográfico
    //--------------------------------------------------------------------------
    public final static String CMD_CREAR_SOLICITUD_COMPRA_MATERIAL_BIBLIOGRAFICO = "CMD_CREAR_SOLICITUD_COMPRA_MATERIAL_BIBLIOGRAFICO";
    public final static String CMD_EDITAR_SOLICITUD_COMPRA_MATERIAL_BIBLIOGRAFICO = "CMD_EDITAR_SOLICITUD_COMPRA_MATERIAL_BIBLIOGRAFICO";
    public final static String CMD_CONSULTAR_SOLICITUD_COMPRA_MATERIAL_BIBLIOGRAFICO_POR_ID_SOLICITUD = "CMD_CONSULTAR_SOLICITUD_COMPRA_MATERIAL_BIBLIOGRAFICO_POR_ID_SOLICITUD";
    public final static String CMD_ELIMINAR_SOLICITUD_MATERIAL_BIBLIOGRAFICO = "CMD_ELIMINAR_SOLICITUD_MATERIAL_BIBLIOGRAFICO";
    public final static String CMD_ENVIAR_CONFIRMACION_COMPRA_MATERIAL_BIBLIOGRAFICO_BIBLIOTECA = "CMD_ENVIAR_CONFIRMACION_COMPRA_MATERIAL_BIBLIOGRAFICO_BIBLIOTECA";
    public final static String CMD_ENVIAR_AUTORIZACION_COMPRA_MATERIAL_BIBLIOGRAFICO = "CMD_ENVIAR_AUTORIZACION_COMPRA_MATERIAL_BIBLIOGRAFICO";
    public final static String CMD_CONSULTAR_COSTO_PROMEDIO_POR_RANGO_DE_FECHA = "CMD_CONSULTAR_COSTO_PROMEDIO_POR_RANGO_DE_FECHA";
    public final static String CMD_CONSULTAR_COSTO_TOTAL_POR_RANGO_DE_FECHA = "CMD_CONSULTAR_COSTO_TOTAL_POR_RANGO_DE_FECHA";
    public final static String CMD_CONSULTAR_COSTO_TOTAL_POR_SOLICITANTE = "CMD_CONSULTAR_COSTO_TOTAL_POR_SOLICITANTE";
    public final static String CMD_CONSULTAR_COSTO_PROMEDIO_POR_SOLICITANTE = "CMD_CONSULTAR_COSTO_PROMEDIO_POR_SOLICITANTE";
    public final static String CMD_CONSULTAR_SOLICITUDES_DEPARTAMENTO_POR_ESTADO = "CMD_CONSULTAR_SOLICITUDES_DEPARTAMENTO_POR_ESTADO";
    public final static String CMD_CONSULTAR_SOLICITUDES_DEPARTAMENTO_POR_PERIODO = "CMD_CONSULTAR_SOLICITUDES_DEPARTAMENTO_POR_PERIODO";
    public final static String CMD_CONSULTAR_SOLICITUDES_DEPARTAMENTO_RANGO_FECHA = "CMD_CONSULTAR_SOLICITUDES_DEPARTAMENTO_RANGO_FECHA";
    public final static String CMD_CONSULTAR_SOLICITUDES_POR_ANIO_PUBLICACION = "CMD_CONSULTAR_SOLICITUDES_POR_ANIO_PUBLICACION";
    public final static String CMD_ACTUALIZAR_DATOS_PUBLICACION = "CMD_ACTUALIZAR_DATOS_PUBLICACION";
    public final static String CMD_CONSULTAR_SOLICITUDES_POR_PERIODO_ACADEMICO = "CMD_CONSULTAR_SOLICITUDES_POR_PERIODO_ACADEMICO";
    public final static String CMD_CONSULTAR_SOLICITUDES_POR_PROVEEDOR = "CMD_CONSULTAR_SOLICITUDES_POR_PROVEEDOR";
    public final static String CMD_CONSULTAR_SOLICITUDES_MATERIAL_BIBLIOGRAFICO_RANGO_PRECIOS = "CMD_CONSULTAR_SOLICITUDES_MATERIAL_BIBLIOGRAFICO_RANGO_PRECIOS";
    public final static String CMD_CONSULTAR_SOLICITUDES_SOLICITANTE = "CMD_CONSULTAR_SOLICITUDES_SOLICITANTE";
    public final static String CMD_ENVIAR_CONFIRMACION_LLEGO_A_BIBLIOTECA_MATERIAL_BIBLIOGRAFICO = "CMD_ENVIAR_CONFIRMACION_LLEGO_A_BIBLIOTECA_MATERIAL_BIBLIOGRAFICO";
    ///...............................Parametros Material Bibliográfico................................//
    public final static String TAG_PARAM_SOLICITUD_MATERIAL_BIBLIOGRAFICO = "TAG_PARAM_SOLICITUD_MATERIAL_BIBLIOGRAFICO";
    public final static String TAG_PARAM_SOLICITUDES_MATERIAL_BIBLIOGRAFICO = "TAG_PARAM_SOLICITUDES_MATERIAL_BIBLIOGRAFICO";
    public final static String TAG_PARAM_DATOS_MATERIAL_BIBLIOGRAFICO = "TAG_PARAM_DATOS_MATERIAL_BIBLIOGRAFICO";
    public final static String TAG_PARAM_FECHA_SOLICITUD_PROFESOR = "TAG_PARAM_FECHA_SOLICITUD_PROFESOR";
    public final static String TAG_PARAM_FECHA_AUTORIZACION_DIRECCION = "TAG_PARAM_FECHA_AUTORIZACION_DIRECCION";
    public final static String TAG_PARAM_FECHA_ENVIO_A_BIBLIOTECA = "TAG_PARAM_FECHA_ENVIO_A_BIBLIOTECA";
    public final static String TAG_PARAM_AUTOR = "TAG_PARAM_AUTOR";
    public final static String TAG_PARAM_ISBN_O_ISSN = "TAG_PARAM_ISBN_O_ISSN";
    public final static String TAG_PARAM_PROVEEDOR = "TAG_PARAM_PROVEEDOR";
    public final static String TAG_PARAM_OBSERVACIONES = "TAG_PARAM_OBSERVACIONES";
    public final static String TAG_PARAM_PRECIO = "TAG_PARAM_PRECIO";
    public final static String TAG_PARAM_TEMATICA = "TAG_PARAM_TEMATICA";
    public final static String TAG_PARAM_EDITORIAL = "TAG_PARAM_EDITORIAL";
    public final static String TAG_PARAM_ANIOS = "TAG_PARAM_ANIOS";
    public final static String TAG_PARAM_ANIO = "TAG_PARAM_ANIO";
    public final static String TAG_PARAM_VOLUMENES = "TAG_PARAM_VOLUMENES";
    public final static String TAG_PARAM_VOLUMEN = "TAG_PARAM_VOLUMEN";
    public final static String TAG_PARAM_NUMERO = "TAG_PARAM_NUMERO";
    public final static String TAG_PARAM_NUMERO_EJEMPLARES = "TAG_PARAM_NUMERO_EJEMPLARES";
    public final static String TAG_PARAM_NUMERO_VOLUMEN = "TAG_PARAM_NUMERO_VOLUMEN";
    public final static String TAG_PARAM_INFO_VOLUMEN = "TAG_PARAM_INFO_VOLUMEN";
    public final static String TAG_PARM_PRECIO_DESDE = "TAG_PARM_PRECIO_DESDE";
    public final static String TAG_PARM_PRECIO_HASTA = "TAG_PARM_PRECIO_HASTA";
    public final static String TAG_PARAM_RESULTADO = "TAG_PARAM_RESULTADO";
    public final static String TAG_PARAM_PRECIO_DESDE = "TAG_PARAM_PRECIO_DESDE";
    public final static String TAG_PARAM_PRECIO_HASTA = "TAG_PARAM_PRECIO_HASTA";
    public final static String TAG_PARAM_FECHA_DESDE = "TAG_PARAM_FECHA_DESDE";
    public final static String TAG_PARAM_FECHA_HASTA = "TAG_PARAM_FECHA_HASTA";
    public final static String TAG_PARAM_CANTIDAD_TOTAL = "TAG_PARAM_CANTIDAD_TOTAL";
    public final static String TAG_PARAM_TEXTO_GUIA = "TAG_PARAM_TEXTO_GUIA";
    public final static String TAG_PARAM_CURSO_TEXTO_GUIA = "TAG_PARAM_CURSO_TEXTO_GUIA";
    public final static String VAL_NUMERO_MAX_EJEMPLARES_NO_TEXTO_GUIA = "VAL_NUMERO_MAX_EJEMPLARES_NO_TEXTO_GUIA";
    ///...............................Niveles de Formacion................................//
    /*public final static String TAG_PARAM_NIVEL_MAESTRIA = "TAG_PARAM_NIVEL_MAESTRIA";
    public final static String TAG_PARAM_NIVEL_PREGRADO = "TAG_PARAM_NIVEL_PREGRADO";
    public final static String TAG_PARAM_NIVEL_ESPECIALIZACION = "TAG_PARAM_NIVEL_ESPECIALIZACION";*/
    public final static String TAG_PARAM_NIVEL_INVESTIGACION = "TAG_PARAM_NIVEL_INVESTIGACION";
    //public final static String[] NIVELES_FORMACION = {TAG_PARAM_NIVEL_MAESTRIA, TAG_PARAM_NIVEL_PREGRADO, TAG_PARAM_NIVEL_ESPECIALIZACION, TAG_PARAM_NIVEL_INVESTIGACION};
    ///...............................Estados solicitud de material bibliográfico................................//
    public final static String TAG_PARAM_DIRECTOR_POR_AUTORIZAR = "TAG_PARAM_DIRECTOR_POR_AUTORIZAR";
    public final static String TAG_PARAM_AUXILIAR_POR_SOLICITAR_BIBLIOTECA = "TAG_PARAM_AUXILIAR_POR_SOLICITAR_BIBLIOTECA";
    public final static String TAG_PARAM_RECHAZADA_POR_DIRECTOR = "TAG_PARAM_RECHAZADA_POR_DIRECTOR";
    public final static String TAG_PARAM_SOLICITADO_A_BIBLIOTECA = "TAG_PARAM_SOLICITADO_A_BIBLIOTECA";
    public final static String TAG_PARAM_LLEGO_A_BIBLIOTECA = "TAG_PARAM_LLEGO_A_BIBLIOTECA";
    public final static String[] ESTADOS_SOLICITUD_MATERIAL_BIBLIOGRAFICO = {TAG_PARAM_DIRECTOR_POR_AUTORIZAR, TAG_PARAM_AUXILIAR_POR_SOLICITAR_BIBLIOTECA, TAG_PARAM_RECHAZADA_POR_DIRECTOR, TAG_PARAM_SOLICITADO_A_BIBLIOTECA, TAG_PARAM_LLEGO_A_BIBLIOTECA};
    ///...............................Tareas solicitud de material bibliográfico................................//
    public final static String TAG_PARAM_ID_SOLICITUD_MATERIAL_BIBLIOGRAFICO = "TAG_PARAM_ID_SOLICITUD_MATERIAL_BIBLIOGRAFICO";
    public final static String TAG_PARAM_TIPO_AUTORIZAR_SOLICITUD = "TAG_PARAM_TIPO_AUTORIZAR_SOLICITUD";
    public final static String TAG_PARAM_TIPO_CONFIRMAR_COMPRA = "TAG_PARAM_TIPO_CONFIRMAR_COMPRA";
    public final static String TAG_PARAM_TIPO_REPORTAR_ADQUISICION = "TAG_PARAM_TIPO_REPORTAR_ADQUISICION";
    public final static String VAL_PROFESOR_INDETERMINADO = "VAL_PROFESOR_INDETERMINADO";
    /**
     * Grupos
     */
    public final static String TAG_PARAM_GRUPOS = "TAG_PARAM_GRUPOS";
    public final static String TAG_PARAM_GRUPO = "TAG_PARAM_GRUPO";
    public final static String TAG_PARAM_PERSONAS = "TAG_PARAM_PERSONAS";
    public final static String TAG_PARAM_PERSONA = "TAG_PARAM_PERSONA";
    public final static String CMD_DAR_GRUPOS = "CMD_DAR_GRUPOS";
    public final static String TAG_PARAM_ID_GRUPO = "TAG_PARAM_ID_GRUPO";
    public final static String CMD_DAR_VOTACION_CON_CANDIDATOS = "CMD_DAR_VOTACION_CON_CANDIDATOS";
    public final static String CMD_VOTAR = "CMD_VOTAR";
    public final static String TAG_PARAM_CORREO_VOTANTE = "TAG_PARAM_CORREO_VOTANTE";
    public final static String TAG_PARAM_CORREO_CANDIDATO = "TAG_PARAM_CORREO_CANDIDATO";
    public final static String TAG_PARAM_TIPO_VOTAR = "TAG_PARAM_TIPO_VOTAR";
    public final static String CMD_DAR_RESULTADOS_VOTACION = "CMD_DAR_RESULTADOS_VOTACION";
    public final static String CMD_DAR_VOTACIONES = "CMD_DAR_VOTACIONES";
    public final static String CMD_TIMER_ABRIR_VOTACION = "CMD_TIMER_ABRIR_VOTACION";
    /***
     * tags de Inscripciones genericas...
     */
    public final static String TAG_PARAM_HASH_INSCRIPCION = "TAG_PARAM_HASH_INSCRIPCION";
    public final static String TAG_PARAM_TIPO_INSCRIBIRSE = "TAG_PARAM_TIPO_INSCRIBIRSE";
    public final static String TAG_PARAM_INFORMACION_INSCRIPCION = "TAG_PARAM_INFORMACION_INSCRIPCION";
    public final static String TAG_PARAM_INFORMACION_INSCRIPCIONES = "TAG_PARAM_INFORMACION_INSCRIPCIONES";
    public final static String TAG_PARAM_INVITADOS = "TAG_PARAM_INVITADOS";
    public final static String TAG_PARAM_INVITADO = "TAG_PARAM_INVITADO";
    public final static String TAG_PARAM_CORREO_CREADOR = "TAG_PARAM_CORREO_CREADOR";
    public final static String TAG_PARAM_CORREO_NOTIFICACION = "TAG_PARAM_CORREO_NOTIFICACION";
    public final static String TAG_PARAM_ID_INSCRIPCION = "TAG_PARAM_ID_INSCRIPCION";
    public final static String TAG_PARAM_ID_INVITADO = "TAG_PARAM_ID_INVITADO";
    public final static String TAG_PARAM_OTROS = "TAG_PARAM_OTROS";
    public final static String TAG_PARAM_LUGAR_EVENTO = "TAG_PARAM_LUGAR_EVENTO";
    public final static String TAG_PARAM_FECHA_EVENTO = "TAG_PARAM_FECHA_EVENTO";
    public final static String TAG_PARAM_NUM_INVITADOS = "TAG_PARAM_NUM_INVITADOS";
    public final static String TAG_PARAM_NUM_CONFIRMADOS = "TAG_PARAM_NUM_CONFIRMADOS";
    public final static String TAG_PARAM_INSCRITO = "TAG_PARAM_INSCRITO";
    public final static String CTE_INSCRIPCION_PENDIENTE = "CTE_INSCRIPCION_PENDIENTE";
    public final static String CTE_INSCRIPCION_CONFIRMADO = "CTE_INSCRIPCION_CONFIRMADO";
    public final static String CTE_INSCRIPCION_NO_ASISTE = "CTE_INSCRIPCION_NO_ASISTE";
    public final static String CMD_CONFIRMAR_INSCRIPCION_CORREO = "CMD_CONFIRMAR_INSCRIPCION_CORREO";
    public final static String CMD_CREAR_INSCRIPCION = "CMD_CREAR_INSCRIPCION";
    public final static String CMD_CERRAR_INSCRIPCION = "CMD_CERRAR_INSCRIPCION";
    public final static String CMD_EDITAR_INSCRIPCION = "CMD_EDITAR_INSCRIPCION";
    public final static String CMD_EDITAR_INSCRIPCION_USUARIO = "CMD_EDITAR_INSCRIPCION_USUARIO";
    public final static String CMD_ELIMINAR_INSCRIPCION = "CMD_ELIMINAR_INSCRIPCION";
    public final static String CMD_DAR_INSCRIPCION_ADMON = "CMD_DAR_INSCRIPCION_ADMON";
    public final static String CMD_DAR_DETALLES_INSCRIPCION_ADMON_POR_ID = "CMD_DAR_DETALLES_INSCRIPCION_ADMON_POR_ID";
    public final static String CMD_DAR_INSCRIPCIONES_USUARIO = "CMD_DAR_INSCRIPCIONES_USUARIO";
    public final static String CMD_DAR_DETALLES_INSCRIPCION_USUARIO_POR_ID = "CMD_DAR_DETALLES_INSCRIPCION_USUARIO_POR_ID";
    public final static String CMD_DAR_INSCRIPCIONES = "CMD_DAR_INSCRIPCIONES";
    public final static String CTE_URL_INSCRIPCIONES = "CTE_URL_INSCRIPCIONES";
    /**
     * Documentos
     */
    public final static String TAG_PARAM_NODO = "TAG_PARAM_NODO";
    public final static String TAG_PARAM_NODOS = "TAG_PARAM_NODOS";
    public final static String TAG_PARAM_ID_NODO = "TAG_PARAM_ID_NODO";
    public final static String TAG_PARAM_ID_NODO_PADRE = "TAG_PARAM_ID_NODO_PADRE";
    public final static String TAG_PARAM_NOMBRE_NODO = "TAG_PARAM_NOMBRE_NODO";
    public final static String TAG_PARAM_DESCRIPCION_NODO = "TAG_PARAM_DESCRIPCION_NODO";
    public final static String TAG_PARAM_ES_DOCUMENTO = "TAG_PARAM_ES_DOCUMENTO";
    public final static String TAG_PARAM_ID_RAIZ_ARBOL = "TAG_PARAM_ID_RAIZ_ARBOL";
    public final static String CMD_DAR_ARBOL_DOCUMENTOS = "CMD_DAR_ARBOL_DOCUMENTOS";
    public final static String CMD_AGREGAR_NODO_ARBOL_DOCUMENTOS = "CMD_AGREGAR_NODO_ARBOL_DOCUMENTOS";
    public final static String CMD_ELIMINAR_NODO_ARBOL_DOCUMENTOS = "CMD_ELIMINAR_NODO_ARBOL_DOCUMENTOS";
    public final static String CMD_EDITAR_NODO_ARBOL_DOCUMENTOS = "CMD_EDITAR_NODO_ARBOL_DOCUMENTOS";
    public final static String CMD_ELIMINAR_ARBOL_DOCUMENTOS = "CMD_ELIMINAR_ARBOL_DOCUMENTOS";
    public final static String ROL_ADMINISTRADOR_DOCUMENTOS = "ROL_ADMINISTRADOR_DOCUMENTOS";
    /**
     * GRUPOS Y PERSONAS
     */
    public final static String TAG_PARAM_ID_PAIS = "TAG_PARAM_ID_PAIS";
    public final static String TAG_PARAM_ID_PERSONA = "TAG_PARAM_ID_PERSONA";
    /**
     * Constantes relacionadas con las publicaciones privadas
     */
    //tags
    public static final String TAG_PARAM_PUBLICACIONES = "TAG_PARAM_PUBLICACIONES";
    public static final String TAG_PARAM_PUBLICACION = "TAG_PARAM_PUBLICACION";
    public static final String TAG_PARAM_ID_PUBLICACION = "TAG_PARAM_ID_PUBLICACION";
    public static final String TAG_PARAM_BIBTEXKEY = "TAG_PARAM_BIBTEXKEY";
    public static final String TAG_PARAM_TIPO_PUBLICACION = "TAG_PARAM_TIPO_PUBLICACION";
    public static final String TAG_PARAM_AUTORES_EXTERNOS = "TAG_PARAM_AUTORES_EXTERNOS";
    public static final String TAG_PARAM_AUTORES_PUBLICACION = "TAG_PARAM_AUTORES_PUBLICACION";
    public static final String TAG_PARAM_AUTOR_PUBLICACION = "TAG_PARAM_AUTOR_PUBLICACION";
    public static final String TAG_PARAM_ID_GRUPO_INVESTIGACION = "TAG_PARAM_ID_GRUPO_INVESTIGACION";
    public static final String TAG_PARAM_PAGINAS = "TAG_PARAM_PAGINAS";
    public static final String TAG_PARAM_MES = "TAG_PARAM_MES";
    public static final String TAG_PARAM_RESUMEN = "TAG_PARAM_RESUMEN";
    public static final String TAG_PARAM_NOTAS = "TAG_PARAM_NOTAS";
    public static final String TAG_PARAM_DOI = "TAG_PARAM_DOI";
    public static final String TAG_PARAM_URL = "TAG_PARAM_URL";
    public static final String TAG_PARAM_CAMPOS_PUBLICACION = "TAG_PARAM_CAMPOS_PUBLICACION";
    public static final String TAG_PARAM_CAMPO_PUBLICACION = "TAG_PARAM_CAMPO_PUBLICACION";
    public static final String TAG_PARAM_ID_CAMPO_PUBLICACION = "TAG_PARAM_ID_CAMPO_PUBLICACION";
    public static final String CMD_CREAR_PUBLICACION = "CMD_CREAR_PUBLICACION";
    public static final String CMD_MODIFICAR_PUBLICACION = "CMD_MODIFICAR_PUBLICACION";
    public static final String CMD_DAR_PUBLICACION = "CMD_DAR_PUBLICACION";
    public static final String CMD_DAR_PUBLICACIONES_POR_CORREO = "CMD_DAR_PUBLICACIONES_POR_CORREO";
    public static final String CMD_DAR_PUBLICACIONES_POR_CORREO_ANIO = "CMD_DAR_PUBLICACIONES_POR_CORREO_ANIO";
    public static final String CMD_DAR_ANIOS_PUBLICACIONES = "CMD_DAR_ANIOS_PUBLICACIONES";
    public static final String CMD_DAR_PUBLICADORES_PUBLICACIONES = "CMD_DAR_PUBLICADORES_PUBLICACIONES";
    public static final String CMD_ACTUALIZAR_PUBLICACIONES_ACADEMIA_POR_CORREO = "CMD_ACTUALIZAR_PUBLICACIONES_ACADEMIA_POR_CORREO";
    public static final String CMD_ACTUALIZAR_PUBLICACIONES_ACADEMIA = "CMD_ACTUALIZAR_PUBLICACIONES_ACADEMIA";
    //directorio de persistencia publicaciones
    public static final String RUTA_PUBLICACION = "RUTA_PUBLICACION";
    //ubicacion de las imagenes almacenadas en servidor version grande
    public static final String RUTA_IMG_MAX = "RUTA_IMG_MAX";
    /**
     * CRM
     */
    public final static String CORREO_REPLY_TO_CRM = "CORREO_REPLY_TO_CRM";
    public final static String CMD_CONSULTAR_CONTACTOS = "CMD_CONSULTAR_CONTACTOS";
    public final static String CMD_CONSULTAR_CONTACTO = "CMD_CONSULTAR_CONTACTO";
    public final static String CMD_ELIMINAR_CONTACTO = "CMD_ELIMINAR_CONTACTO";
    public final static String CMD_ELIMINAR_EVENTO_EXTERNO = "CMD_ELIMINAR_EVENTO_EXTERNO";
    public final static String CMD_EDITAR_CONTACTO = "CMD_EDITAR_CONTACTO";
    public final static String CMD_AGREGAR_CONTACTO = "CMD_AGREGAR_CONTACTO";
    public final static String CMD_ACTUALIZAR_CATEGORIAS = "CMD_ACTUALIZAR_CATEGORIAS";
    public final static String CMD_ENVIAR_CORREO_CONTACTOS = "CMD_ENVIAR_CORREO_CONTACTOS";
    public final static String CMD_ELIMINAR_ARCHIVO_ADJUNTO = "CMD_ELIMINAR_ARCHIVO_ADJUNTO";
    public final static String CMD_DAR_CARGOS = "CMD_DAR_CARGOS";
    public final static String TAG_PARAM_INFORMACION_CARGO = "TAG_PARAM_INFORMACION_CARGO";
    public final static String TAG_PARAM_INFORMACION_CARGOS = "TAG_PARAM_INFORMACION_CARGOS";
    public final static String CMD_GUARDAR_INSCRIPCION = "CMD_GUARDAR_INSCRIPCION";
    public final static String TAG_PARAM_DEPARTAMENTO = "TAG_PARAM_DEPARTAMENTO";
    public final static String TAG_PARAM_DEPARTAMENTOS = "TAG_PARAM_DEPARTAMENTOS";
    public final static String CMD_REGISTRAR_USUARIO_PUBLICO = "CMD_REGISTRAR_USUARIO_PUBLICO";
    public final static String CMD_ACTIVAR_USUARIO_PUBLICO = "CMD_ACTIVAR_USUARIO_PUBLICO";
    public final static String TAG_PARAM_HASH_ACTIVAR_USUARIO = "TAG_PARAM_HASH_ACTIVAR_USUARIO";
    public final static String TAG_PARAM_ID_CARGO = "TAG_PARAM_ID_CARGO";
    public final static String CMD_CONSULTAR_USUARIO_HASH_PUBLICO = "CMD_CONSULTAR_USUARIO_HASH_PUBLICO";
    public final static String CMD_DAR_INFO_ARCHIVO_TERMINOS_Y_CONDICIONES = "CMD_DAR_INFO_ARCHIVO_TERMINOS_Y_CONDICIONES";
    public final static String CMD_ELIMINAR_INSCRIPCION_EVENTO_EXTERNO = "CMD_ELIMINAR_INSCRIPCION_EVENTO_EXTERNO";
    public final static String CMD_CONSULTAR_INSCRITOS_EVENTO_EXTERNO = "CMD_CONSULTAR_INSCRITOS_EVENTO_EXTERNO";
    public final static String TAG_PARAM_CONTACTO_IDEVENTO = "TAG_PARAM_CONTACTO_IDEVENTO";

    //CONTACTOS
    public final static String TAG_PARAM_INFORMACION_CONTACTOS = "TAG_PARAM_INFORMACION_CONTACTOS";
    public final static String TAG_PARAM_INFORMACION_CONTACTO = "TAG_PARAM_INFORMACION_CONTACTO";
    public final static String TAG_PARAM_DIRECCION = "TAG_PARAM_DIRECCION";
    public final static String TAG_PARAM_INDICATIVO = "TAG_PARAM_INDICATIVO";
    public final static String TAG_PARAM_FAX = "TAG_PARAM_FAX";
    public final static String TAG_PARAM_SECTOR_CORPORATIVO = "TAG_PARAM_SECTOR_CORPORATIVO";
    public final static String TAG_PARAM_ID_CONTACTO = "TAG_PARAM_ID_CONTACTO";
    public final static String CMD_LOGIN_PUBLICO = "CMD_LOGIN_PUBLICO";
    public final static String CMD_OLVIDO_CONTRASENA_PUBLICO = "CMD_OLVIDO_CONTRASENA_PUBLICO";
    public final static String CMD_CAMBIAR_CONTRASENA = "CMD_CAMBIAR_CONTRASENA";
    public final static String CMD_CONSULTAR_CONTACTOS_CON_PARAMETROS = "CMD_CONSULTAR_CONTACTOS_CON_PARAMETROS";
    public final static String TAG_PARAM_ = "TAG_PARAM_";
    public final static String TAG_PARAM_CONTACTO_NOMBRE = "TAG_PARAM_CONTACTO_NOMBRE";
    public final static String TAG_PARAM_CONTACTO_APELLIDOS = "TAG_PARAM_CONTACTO_APELLIDOS";
    public final static String TAG_PARAM_CONTACTO_ID = "TAG_PARAM_CONTACTO_ID";
    public final static String TAG_PARAM_CONTACTO_CARGO = "TAG_PARAM_CONTACTO_CARGO";
    public final static String TAG_PARAM_CONTACTO_EMPRESA = "TAG_PARAM_CONTACTO_EMPRESA";
    public final static String TAG_PARAM_CONTACTO_CIUDAD = "TAG_PARAM_CONTACTO_CIUDAD";
    public final static String TAG_PARAM_CONTACTO_CELULAR = "TAG_PARAM_CONTACTO_CELULAR";
    public final static String TAG_PARAM_CONTACTO_CORREO = "TAG_PARAM_CONTACTO_CORREO";
    public final static String TAG_PARAM_CONTACTO_SECTOR = "TAG_PARAM_CONTACTO_SECTOR";
    public final static String TAG_PARAM_CONTACTO_FECHA = "TAG_PARAM_CONTACTO_FECHA";


    //ENVIO MAIL CONTACTOS
    public final static String TAG_PARAM_INFORMACION_CORREO = "TAG_PARAM_INFORMACION_CORREO";
    public final static String TAG_PARAM_DIRECCIONES_CORREO = "TAG_PARAM_DIRECCIONES_CORREO";
    public final static String TAG_PARAM_SECTORES_CORPORATIVOS = "TAG_PARAM_SECTORES_CORPORATIVOS";
    public final static String CMD_CONSULTAR_SECTORES_CORPORATIVOS = "CMD_CONSULTAR_SECTORES_CORPORATIVOS";
    //ROLES:
    public final static String ROL_ADMINISTRADOR_CRM = "ROL_ADMINISTRADOR_CRM";
    public final static String ROL_USUARIO_CRM = "ROL_USUARIO_CRM";
    //RUTAS
    public final static String RUTA_TEMPORAL_ADJUNTOS = "RUTA_TEMPORAL_ADJUNTOS";
    //RUTA directorio de persistencia Reportes JASPER
    public static final String RUTA_REPORTES_JASPER = "RUTA_REPORTES_JASPER";
    //ARREGLOS BOLSA
    public static final String TAG_PARAM_CORREO_CONTACTO = "TAG_PARAM_CORREO_CONTACTO";



    /**
     * -------------------------------------------------------------------------
     * cARGA Y cOMPROMISOS
     * -------------------------------------------------------------------------
     */
    public final static String CMD_TERMINAR_TAREA_CYC = "CMD_TERMINAR_TAREA_CYC";
    public final static String TAG_PARAM_TIPO_TERMINAR_CARGA_Y_COMPROMISOS = "TAG_PARAM_TIPO_TERMINAR_CARGA_Y_COMPROMISOS";
    public static final String TAG_PARAM_INFORMACION_PROCESO = "TAG_PARAM_INFORMACION_PROCESO";
    public final static String CMD_CONSULTAR_CARGAYCOMPROMISOS_PROFESOR = "CMD_CONSULTAR_CARGAYCOMPROMISOS_PROFESOR";
    public final static String CMD_CONSULTAR_CARGAYCOMPROMISOS_PROFESOR_POR_CORREO_PERIODO = "CMD_CONSULTAR_CARGAYCOMPROMISOS_PROFESOR_POR_CORREO_PERIODO";
    public final static String CMD_CONSULTAR_CARGAYCOMPROMISOS_PROFESOR_ULTIMO_PERIODO = "CMD_CONSULTAR_CARGAYCOMPROMISOS_PROFESOR_ULTIMO_PERIODO";
    public final static String TAG_PARAM_ID_CARGA = "TAG_PARAM_ID_CARGA";
    public final static String CMD_ENVIAR_CORREO_ADMON_CARGRA_Y_C = "CMD_ENVIAR_CORREO_ADMON_CARGRA_Y_C";
    public final static String CMD_CERRAR_PROCESO_CARGRA_Y_C = "CMD_CERRAR_PROCESO_CARGRA_Y_C";
    public final static String CMD_INICIAR_PROCESO_CARGA_COMPROMISOS = "CMD_INICIAR_PROCESO_CARGA_COMPROMISOS";
    public final static String CMD_CREAR_CARGA_VACIA_A_PROFESOR = "CMD_CREAR_CARGA_VACIA_A_PROFESOR";
    public final static String TAG_PARAM_CARGA_PROFESOR = "TAG_PARAM_CARGA_PROFESOR";
    public final static String TAG_PARAM_PERIODO_PLANEACION = "TAG_PARAM_PERIODO_PLANEACION";
    public final static String TAG_PARAM_ID_GENERAL = "TAG_PARAM_ID_GENERAL";
    public final static String TAG_PARAM_INFORMACION_BASICA = "TAG_PARAM_INFORMACION_BASICA";
    public final static String TAG_PARAM_NUMERO_PUBLICACIONES_PLANEADAS = "TAG_PARAM_NUMERO_PUBLICACIONES_PLANEADAS";
    public final static String TAG_PARAM_NUMERO_ESTUDIANTES_EN_TESIS = "TAG_PARAM_NUMERO_ESTUDIANTES_EN_TESIS";
    public final static String TAG_PARAM_CARGA_EFECTIVA = "TAG_PARAM_CARGA_EFECTIVA";
    public final static String TAG_PARAM_INFORMACION_CURSOS = "TAG_PARAM_INFORMACION_CURSOS";
    public final static String TAG_PARAM_INFORMACION_CURSO = "TAG_PARAM_INFORMACION_CURSO";
    public final static String TAG_PARAM_INFORMACION_PUBLICACIONES = "TAG_PARAM_INFORMACION_PUBLICACIONES";
    public final static String TAG_PARAM_INFORMACION_PUBLICACION = "TAG_PARAM_INFORMACION_PUBLICACION";
    public final static String TAG_PARAM_COAUTORES = "TAG_PARAM_COAUTORES";
    public final static String TAG_PARAM_COAUTOR = "TAG_PARAM_COAUTOR";
    public final static String TAG_PARAM_INFORMACION_TESISES = "TAG_PARAM_INFORMACION_TESISES";
    public final static String TAG_PARAM_ID_TESIS = "TAG_PARAM_ID_TESIS";
    public final static String TAG_PARAM_INFORMACION_TESIS = "TAG_PARAM_INFORMACION_TESIS";
    public final static String TAG_PARAM_NIVEL_ESTADO_TESIS = "TAG_PARAM_NIVEL_ESTADO_TESIS";
    public final static String TAG_PARAM_NIVEL_FORMACION_TESIS = "TAG_PARAM_NIVEL_FORMACION_TESIS";
    public final static String TAG_PARAM_INFORMACION_PROYECTOS_FINANCIADOS = "TAG_PARAM_INFORMACION_PROYECTOS_FINANCIADOS";
    public final static String TAG_PARAM_INFORMACION_PROYECTO_FINANCIADO = "TAG_PARAM_INFORMACION_PROYECTO_FINANCIADO";
    public final static String TAG_PARAM_ENTIDAD_FINICIADORA = "TAG_PARAM_ENTIDAD_FINICIADORA";
    public final static String TAG_PARAM_COLABORADORES = "TAG_PARAM_COLABORADORES";
    public final static String TAG_PARAM_INFORMACION_OTROS = "TAG_PARAM_INFORMACION_OTROS";
    public final static String TAG_PARAM_INFORMACION_OTRO = "TAG_PARAM_INFORMACION_OTRO";
    public final static String TAG_PARAM_TIPO_DESCARGA_PROFESOR = "TAG_PARAM_TIPO_DESCARGA_PROFESOR";
    public final static String TAG_PARAM_CARGA_CURSO = "TAG_PARAM_CARGA_CURSO";
    public final static String CMD_AGREGAR_CURSO_CYC = "CMD_AGREGAR_CURSO_CYC";
    public final static String CMD_AGREGAR_INTENCION_PUBLICACION_CYC = "CMD_AGREGAR_INTENCION_PUBLICACION_CYC";
    public final static String CMD_AGREGAR_ASESORIA_TESIS_CYC = "CMD_AGREGAR_ASESORIA_TESIS_CYC";
    public final static String CMD_AGREGAR_PROYECTO_FINANCIADO_CYC = "CMD_AGREGAR_PROYECTO_FINANCIADO_CYC";
    public final static String CMD_AGREGAR_OTRA_ACTIVIDAD_CYC = "CMD_AGREGAR_OTRA_ACTIVIDAD_CYC";
    public final static String CMD_AGREGAR_DESCARGA_PROFESOR_CYC = "CMD_AGREGAR_DESCARGA_PROFESOR_CYC";
    public final static String CMD_ELIMINAR_CURSO_CYC = "CMD_ELIMINAR_CURSO_CYC";
    public final static String CMD_ELIMINAR_INTENCION_PUBLICACION_CYC = "CMD_ELIMINAR_INTENCION_PUBLICACION_CYC";
    public final static String CMD_ELIMINAR_ASESORIA_TESIS_CYC = "CMD_ELIMINAR_ASESORIA_TESIS_CYC";
    public final static String CMD_ELIMINAR_PROYECTO_FINANCIADO_CYC = "CMD_ELIMINAR_PROYECTO_FINANCIADO_CYC";
    public final static String CMD_ELIMINAR_OTRA_ACTIVIDAD_CYC = "CMD_ELIMINAR_OTRA_ACTIVIDAD_CYC";
    public final static String CMD_EDITAR_CURSO_CYC = "CMD_EDITAR_CURSO_CYC";
    public final static String CMD_EDITAR_INTENCION_PUBLICACION_CYC = "CMD_EDITAR_INTENCION_PUBLICACION_CYC";
    public final static String CMD_EDITAR_ASESORIA_TESIS_CYC = "CMD_EDITAR_ASESORIA_TESIS_CYC";
    public final static String CMD_EDITAR_PROYECTO_FINANCIADO_CYC = "CMD_EDITAR_PROYECTO_FINANCIADO_CYC";
    public final static String CMD_EDITAR_OTRA_ACTIVIDAD_CYC = "CMD_EDITAR_OTRA_ACTIVIDAD_CYC";
    public final static String CMD_EDITAR_DESCARGA_PROFESOR_CYC = "CMD_EDITAR_DESCARGA_PROFESOR_CYC";
    public final static String HOJA_DE_VIDA_VACIA_BOLSA_EMPLEO = "HOJA_DE_VIDA_VACIA_BOLSA_EMPLEO";
    public final static String TAG_PARAM_TIPOS_PUBLICACION = "TAG_PARAM_TIPOS_PUBLICACION";
    public final static String CMD_DAR_TIPOS_PUBLICACION = "CMD_DAR_TIPOS_PUBLICACION";
    public final static String TAG_PARAM_NIVELES_ESTADO_TESIS = "TAG_PARAM_NIVELES_ESTADO_TESIS";
    public final static String CMD_DAR_NIVELES_ESTADO_TESIS = "CMD_DAR_NIVELES_ESTADO_TESIS";
    public final static String CMD_DAR_DESCARGAS_PROFESOR_CYC = "CMD_DAR_DESCARGAS_PROFESOR_CYC";
    public final static String TAG_PARAM_MAXIMO_NIVEL_TESIS = "TAG_PARAM_MAXIMO_NIVEL_TESIS";
    public final static String TAG_PARAM_HORAS_DEDICACION_SEMANAL = "TAG_PARAM_HORAS_DEDICACION_SEMANAL";
    public final static String TAG_PARAM_COLABORADOR = "TAG_PARAM_COLABORADOR";
    public final static String CMD_CONSULTAR_PROYECTOS_FINANCIADOS_ULTIMO_PERIODO = "CMD_CONSULTAR_PROYECTOS_FINANCIADOS_ULTIMO_PERIODO";
    public final static String CMD_VINCULAR_CARGAYCOMPROMISOS_PROFESOR_PROYECTO_FINANCIADO = "CMD_VINCULAR_CARGAYCOMPROMISOS_PROFESOR_PROYECTO_FINANCIADO";
    public final static String TAG_PARAM_NUMERO_CAPITULOS_LIBRO = "TAG_PARAM_NUMERO_CAPITULOS_LIBRO";
    public final static String TAG_PARAM_NUMERO_CONGRESOS_INTERNACIONALES = "TAG_PARAM_NUMERO_CONGRESOS_INTERNACIONALES";
    public final static String TAG_PARAM_NUMERO_CONGRESOS_NACIONALES = "TAG_PARAM_NUMERO_CONGRESOS_NACIONALES";
    public final static String TAG_PARAM_NUMERO_CURSOS = "TAG_PARAM_NUMERO_CURSOS";
    public final static String TAG_PARAM_NUMERO_REVISTAS = "TAG_PARAM_NUMERO_REVISTAS";
    public final static String TAG_PARAM_NUMERO_ESTUDIANTES_DOCTORADO = "TAG_PARAM_NUMERO_ESTUDIANTES_DOCTORADO";
    public final static String TAG_PARAM_NUMERO_ESTUDIANTES_PREGRADO = "TAG_PARAM_NUMERO_ESTUDIANTES_PREGRADO";
    public final static String TAG_PARAM_NUMERO_ESTUDIANTES_TESIS1 = "TAG_PARAM_NUMERO_ESTUDIANTES_TESIS1";
    public final static String TAG_PARAM_NUMERO_ESTUDIANTES_TESIS2 = "TAG_PARAM_NUMERO_ESTUDIANTES_TESIS2";
    public final static String TAG_PARAM_NUMERO_ESTUDIANTES_TESIS2_PENDIENTE = "TAG_PARAM_NUMERO_ESTUDIANTES_TESIS2_PENDIENTE";
    public final static String TAG_PARAM_NUMERO_PROYECTOS_FINANCIADOS = "TAG_PARAM_NUMERO_PROYECTOS_FINANCIADOS";
    public final static String TAG_PARAM_EVENTOS = "TAG_PARAM_EVENTOS";
    public final static String TAG_PARAM_EVENTO = "TAG_PARAM_EVENTO";
    //TIPOS de Publicacion, deben qudar con el mismo nombre en la tabla de tipo_publicacion
    public final static String TAG_PARAM_TIPO_PUBLICACION_REVISTA = "TAG_PARAM_TIPO_PUBLICACION_REVISTA";
    public final static String TAG_PARAM_TIPO_PUBLICACION_CONGRESO_INTERNACIONAL = "TAG_PARAM_TIPO_PUBLICACION_CONGRESO_INTERNACIONAL";
    public final static String TAG_PARAM_TIPO_PUBLICACION_CONGRESO_NACIONAL = "TAG_PARAM_TIPO_PUBLICACION_CONGRESO_NACIONAL";
    public final static String TAG_PARAM_TIPO_PUBLICACION_CAPITULO_LIBRO = "TAG_PARAM_TIPO_PUBLICACION_CAPITULO_LIBRO";
    public final static String TAG_PARAM_NIVEL_TESIS_1 = "TAG_PARAM_NIVEL_TESIS_1";
    public final static String TAG_PARAM_NIVEL_TESIS_2 = "TAG_PARAM_NIVEL_TESIS_2";
    public final static String TAG_PARAM_NIVEL_TESIS_2_PENDIENTE = "TAG_PARAM_NIVEL_TESIS_2_PENDIENTE";
    public final static String TAG_PARAM_NIVEL_TESIS_PROYECTO_DE_GRADO = "TAG_PARAM_NIVEL_TESIS_PROYECTO_DE_GRADO";
    public final static String TAG_PARAM_NIVEL_TESIS_DOCTORADO = "TAG_PARAM_NIVEL_TESIS_DOCTORADO";
    public final static String TAG_PARAM_SIN_DESCARGA = "TAG_PARAM_SIN_DESCARGA";
    public final static String CMD_DAR_PERIODOS_PLANEACION = "CMD_DAR_PERIODOS_PLANEACION";
    public final static String RUTA_REPORTES_HISTORICOS_CARGA_Y_COMPROMISOS = "RUTA_REPORTES_HISTORICOS_CARGA_Y_COMPROMISOS";
    public final static String CMD_MIGRAR_CYC = "CMD_MIGRAR_CYC";
    //Historicos
    public final static String CMD_MIGRAR_CARGAS_PROFESORES_A_HISTORICOS = "CMD_MIGRAR_CARGAS_PROFESORES_A_HISTORICOS";
    /**
     * -------------------------------------------------------------------------
     * DESPRESELECCION
     * -------------------------------------------------------------------------
     */
    //public final static String TAG_PARAM_="TAG_PARAM_HORAS_DEDICACION_SEMANAL";
    public final static String CMD_ESTA_EN_PERIODO_DE_DESPRESELECCION = "CMD_ESTA_EN_PERIODO_DE_DESPRESELECCION";
    public final static String TAG_PARAM_TIPO_DESCARGAS_PROFESOR = "TAG_PARAM_TIPO_DESCARGAS_PROFESOR";
    /**
     * --------------------------------------------------------------------------


    /**
     * -------------------------------------------------------------------------
     * CUPI2
     * -------------------------------------------------------------------------
     */
    public final static String CMD_CONSULTAR_SECCIONES_SIN_MONITOR_T2 = "CMD_CONSULTAR_SECCIONES_SIN_MONITOR_T2";
    public final static String CMD_CONSULTAR_MONITORES_T2 = "CMD_CONSULTAR_MONITORES_T2";
    public final static String CMD_PRESELECCIONAR_MONITORES_T2 = "CMD_PRESELECCIONAR_MONITORES_T2";
    public final static String CMD_CONSULTAR_MONITORES_CANDIDATOS_T2 = "CMD_CONSULTAR_MONITORES_CANDIDATOS_T2";

    /*
     * HISTÓRICO DE ARCHIVOS
     * --------------------------------------------------------------------------
     */
    public final static String CMD_DAR_ARCHIVOS_PROFESOR_POR_PERIODO = "CMD_DAR_ARCHIVOS_PROFESOR_POR_PERIODO";
    /*
     * TESIS MAESTRIA
     */
    public final static String CMD_COLOCAR_NOTA_TESIS_1_ASESOR = "CMD_COLOCAR_NOTA_TESIS_1_ASESOR";
    public final static String CMD_DAR_SOLICITUD_INGRESOSUBAREA_POR_ID = "CMD_DAR_SOLICITUD_INGRESOSUBAREA_POR_ID";
    public final static String CMD_CREAR_SOLICITUD_INGRESO_SUBAREA_ESTUDIANTE = "CMD_CREAR_SOLICITUD_INGRESO_SUBAREA_ESTUDIANTE";
    public final static String CMD_CREAR_SOLICITUD_INGRESO_SUBAREA_ESTUDIANTE_POR_COORDINACION = "CMD_CREAR_SOLICITUD_INGRESO_SUBAREA_ESTUDIANTE_POR_COORDINACION";
    public final static String CMD_DAR_SOLICITUDES_INGRESO_SUBAREA_ASESOR = "CMD_DAR_SOLICITUDES_INGRESO_SUBAREA_ASESOR";
    public final static String CMD_DAR_SOLICITUDES_INGRESO_SUBAREA_COORDINADOR = "CMD_DAR_SOLICITUDES_INGRESO_SUBAREA_COORDINADOR";
    public final static String CMD_DAR_SOLICITUDES_INGRESO_SUBAREA = "CMD_DAR_SOLICITUDES_INGRESO_SUBAREA";
    public final static String CMD_APROBAR_INSCRIPCION_A_SUBAREA_ASESOR = "CMD_APROBAR_INSCRIPCION_A_SUBAREA_ASESOR";
    public final static String CMD_APROBAR_INSCRIPCION_A_SUBAREA_DIRECTOR = "CMD_APROBAR_INSCRIPCION_A_SUBAREA_DIRECTOR";
    public final static String CMD_DAR_SOLICITUDES_APROBADAS_INGRESO_COORDINACION = "CMD_DAR_SOLICITUDES_APROBADAS_INGRESO_COORDINACION";
    public final static String CMD_AGREGAR_SOLICITUD_TESIS1 = "CMD_AGREGAR_SOLICITUD_TESIS1";
    public final static String CMD_DAR_CURSOS_MAESTRIA_TESIS = "CMD_DAR_CURSOS_MAESTRIA_TESIS";
    public final static String CMD_DAR_UBICACION_HORA_TESIS2 = "CMD_DAR_UBICACION_HORA_TESIS2";
    public final static String CMD_DAR_DETALLES_UBICACION_HORA_TESIS2 = "CMD_DAR_DETALLES_UBICACION_HORA_TESIS2";
    public final static String CMD_AGREGAR_CURSOS_MAESTRIA_TESIS = "CMD_AGREGAR_CURSOS_MAESTRIA_TESIS";
    public final static String CMD_DAR_CURSO_MAESTRIA_POR_ID = "CMD_DAR_CURSO_MAESTRIA_POR_ID";
    public final static String CMD_SUBIR_NOTA_TESIS_1 = "CMD_SUBIR_NOTA_TESIS_1";
    public final static String CMD_DAR_SOLICITUDES_TESIS_1 = "CMD_DAR_SOLICITUDES_TESIS_1";
    public final static String CMD_DAR_SOLICITUD_TESIS1_ESTUDIANTE = "CMD_DAR_SOLICITUD_TESIS1_ESTUDIANTE";
    public final static String CMD_DAR_SOLICITUDES_TESIS1_PROFESOR = "CMD_DAR_SOLICITUDES_TESIS1_PROFESOR";
    public final static String CMD_DAR_SOLICITUD_TESIS1 = "CMD_DAR_SOLICITUD_TESIS1";
    public final static String CMD_APROBAR_SOLICITUD_TESIS1 = "CMD_APROBAR_SOLICITUD_TESIS1";
    public final static String CMD_CREAR_SOLICITUD_TESIS_2_ESTUDIANTE = "CMD_CREAR_SOLICITUD_TESIS_2_ESTUDIANTE";
    public final static String CMD_CREAR_SOLICITUD_TESIS_2_COORDINADOR = "CMD_CREAR_SOLICITUD_TESIS_2_COORDINADOR";
    public final static String CMD_DAR_TESIS2_POR_ID = "CMD_DAR_TESIS2_POR_ID";
    public final static String CMD_APROBAR_SOLICITUD_TESIS2 = "CMD_APROBAR_SOLICITUD_TESIS2";
    public final static String CMD_SUBIR_NOTA_TESIS_2 = "CMD_SUBIR_NOTA_TESIS_2";
    public final static String CMD_MODIFICAR_JURADO_TESIS_2 = "CMD_MODIFICAR_JURADO_TESIS_2";
    public final static String CMD_CAMBIAR_TESIS2_PENDIENTE = "CMD_CAMBIAR_TESIS2_PENDIENTE";
    public final static String CMD_CONSULTAR_SUBAREAS_INVESTIGACION = "CMD_CONSULTAR_SUBAREAS_INVESTIGACION";
    public final static String CMD_DAR_CURSOS_MAESTRIA_TESIS_POR_CLASIFICACION = "CMD_DAR_CURSOS_MAESTRIA_TESIS_POR_CLASIFICACION";
    public final static String CMD_DAR_SOLICITUDES_TESIS1_PARA_COORDINACION = "CMD_DAR_SOLICITUDES_TESIS1_PARA_COORDINACION";
    public final static String CMD_DAR_SOLICITUDES_TESIS2_PARA_COORDINACION = "CMD_DAR_SOLICITUDES_TESIS2_PARA_COORDINACION";
    public final static String CMD_DAR_PERIDOS_TESIS = "CMD_DAR_PERIDOS_TESIS";
    public final static String CMD_MIGRAR_INSCRIPCIONES_SUBAREA_RECHAZADAS_A_HISTORICOS = "CMD_MIGRAR_INSCRIPCIONES_SUBAREA_RECHAZADAS_A_HISTORICOS";
    public final static String CMD_SOLICITUD_ESTUDIANTE_SUBAREA_POR_CORREO = "CMD_SOLICITUD_ESTUDIANTE_SUBAREA_POR_CORREO";
    public final static String CMD_MIGRAR_TESIS_1_RECHAZADAS_A_HISTORICOS = "CMD_MIGRAR_TESIS_1_RECHAZADAS_A_HISTORICOS";
    public final static String CMD_MIGRAR_TESIS_2_RECHAZADAS_A_HISTORICOS = "CMD_MIGRAR_TESIS_2_RECHAZADAS_A_HISTORICOS";
    public final static String CMD_PASAR_DATOS_TESIS_A_HISTORICOS = "CMD_PASAR_DATOS_TESIS_A_HISTORICOS";
    public final static String CMD_MIGRAR_TESIS_CUMPLIDA_A_HISTORICOS = "CMD_MIGRAR_TESIS_CUMPLIDA_A_HISTORICOS";
    public final static String CMD_MIGRAR_TESIS_1_PERDIDAS_A_HISTORICOS = "CMD_MIGRAR_TESIS_1_PERDIDAS_A_HISTORICOS";
    public final static String CMD_MIGRAR_TESIS_2_PERDIDAS_A_HISTORICOS = "CMD_MIGRAR_TESIS_2_PERDIDAS_A_HISTORICOS";
    public final static String CMD_EDITAR_FECHAS_PERIODO_TESIS = "CMD_EDITAR_FECHAS_PERIODO_TESIS";
    public final static String CMD_ENVIAR_CORREO_ULTIMO_DIA_INSCRIPCION_SUBAREA = "CMD_ENVIAR_CORREO_ULTIMO_DIA_INSCRIPCION_SUBAREA";
    public final static String CMD_ENVIAR_CORREO_ULTIMO_DIA_INSCRIPCION_TESIS_1 = "CMD_ENVIAR_CORREO_ULTIMO_DIA_INSCRIPCION_TESIS_1";
    public final static String CMD_ENVIAR_CORREO_ULTIMO_DIA_INSCRIPCION_TESIS_1_OPCIONAL = "CMD_ENVIAR_CORREO_ULTIMO_DIA_INSCRIPCION_TESIS_1_OPCIONAL";
    public final static String CMD_ENVIAR_CORREO_ULTIMO_DIA_SUBIR_NOTAS_TESIS_1 = "CMD_ENVIAR_CORREO_ULTIMO_DIA_SUBIR_NOTAS_TESIS_1";
    public final static String CMD_ENVIAR_CORREO_ULTIMO_DIA_COLOCAR_PENDIENTE_NOTAS_TESIS_1 = "CMD_ENVIAR_CORREO_ULTIMO_DIA_COLOCAR_PENDIENTE_NOTAS_TESIS_1";
    public final static String CMD_ENVIAR_CORREO_ULTIMO_DIA_QUITAR_PENDIENTE_NOTAS_TESIS_1 = "CMD_ENVIAR_CORREO_ULTIMO_DIA_QUITAR_PENDIENTE_NOTAS_TESIS_1";
    public final static String CMD_APROBAR_SOLICITUD_TESIS1_COORDINADOR = "CMD_APROBAR_SOLICITUD_TESIS1_COORDINADOR";
    public final static String CMD_DAR_CONFIGURACION_PERIODO_TESIS_POR_NOMBRE = "CMD_DAR_CONFIGURACION_PERIODO_TESIS_POR_NOMBRE";
    public final static String CMD_COLOCAR_PENDIENTE_TESIS_1 = "CMD_COLOCAR_PENDIENTE_TESIS_1";
    public final static String CMD_RETIRAR_TESIS1 = "CMD_RETIRAR_TESIS1";
    public final static String CMD_RETIRAR_TESIS2 = "CMD_RETIRAR_TESIS2";
    public final static String CMD_DAR_SOLICITUDES_APROBADAS_TESIS_1 = "CMD_DAR_SOLICITUDES_APROBADAS_TESIS_1";
    public final static String CMD_ENVIAR_CORREO_ARCHIVOS_JURADOS_SUSTENTACION_TESIS_2 = "CMD_ENVIAR_CORREO_ARCHIVOS_JURADOS_SUSTENTACION_TESIS_2";
    public final static String CMD_EDITAR_FECHA_SUSTENTACION_TESIS_2 = "CMD_EDITAR_FECHA_SUSTENTACION_TESIS_2";
    public final static String CMD_DAR_SOLICITUDES_TESIS_2_ASESOR = "CMD_DAR_SOLICITUDES_TESIS_2_ASESOR";
    public final static String CMD_DAR_TODAS_LAS_TESIS_2 = "CMD_DAR_TODAS_LAS_TESIS_2";
    public final static String CMD_DAR_TODAS_LAS_TESIS_2_A_MIGRAR = "CMD_DAR_TODAS_LAS_TESIS_2_A_MIGRAR";
    public final static String CMD_DAR_TODAS_LAS_TESIS_1_A_MIGRAR = "CMD_DAR_TODAS_LAS_TESIS_1_A_MIGRAR";
    public final static String CMD_APROBAR_PENDIENTE_TESIS1 = "CMD_APROBAR_PENDIENTE_TESIS1";
    public final static String CMD_REPROBAR_TESIS_2 = "CMD_REPROBAR_TESIS_2";
    public final static String CMD_ACTUALIZAR_TESIS_2 = "CMD_ACTUALIZAR_TESIS_2";
    public final static String TAG_PARAM_TEMA_TESIS = "TAG_PARAM_TEMA_TESIS";
    public final static String TAG_PARAM_APROBADO_SUBAREA = "TAG_PARAM_APROBADO_SUBAREA";
    public final static String TAG_PARAM_NOMBRE_SUBAREA_INVESTIGACION = "TAG_PARAM_NOMBRE_SUBAREA_INVESTIGACION";
    public final static String TAG_PARAM_APROBADO_ASESOR = "TAG_PARAM_APROBADO_ASESOR";
    public final static String TAG_PARAM_ESTADO_SOLICITUD = "TAG_PARAM_ESTADO_SOLICITUD";
    public final static String TAG_PARAM_ASESOR_TESIS = "TAG_PARAM_ASESOR_TESIS";
    public final static String TAG_PARAM_COORDINADOR_SUBAREA = "TAG_PARAM_COORDINADOR_SUBAREA";
    public final static String TAG_PARAM_CURSOS_OBLIGATORIOS = "TAG_PARAM_CURSOS_OBLIGATORIOS";
    public final static String TAG_PARAM_CURSOS_SUBAREAS = "TAG_PARAM_CURSOS_SUBAREAS";
    public final static String TAG_PARAM_CURSOS_OTRA_SUBAREA = "TAG_PARAM_CURSOS_OTRA_SUBAREA";
    public final static String TAG_PARAM_CURSOS_OTRA_MAESTRIA = "TAG_PARAM_CURSOS_OTRA_MAESTRIA";
    public final static String TAG_PARAM_CURSOS_NIVELATORIOS = "TAG_PARAM_CURSOS_NIVELATORIOS";
    public final static String TAG_PARAM_VISTO = "TAG_PARAM_VISTO";
    public final static String TAG_PARAM_TIPO_TERMINAR_INSCRIPCION_SUBAREA_INVESTIGACION_COORDINADOR = "TAG_PARAM_TIPO_TERMINAR_INSCRIPCION_SUBAREA_INVESTIGACION_COORDINADOR";
    public final static String TAG_PARAM_TIPO_TERMINAR_INSCRIPCION_SUBAREA_INVESTIGACION_ASESOR = "TAG_PARAM_TIPO_TERMINAR_INSCRIPCION_SUBAREA_INVESTIGACION_ASESOR";
    public final static String TAG_PARAM_ESTADO_TESIS = "TAG_PARAM_ESTADO_TESIS";
    public final static String TAG_PARAM_SUBAREA_INVESTIGACION = "TAG_PARAM_SUBAREA_INVESTIGACION";
    public final static String TAG_PARAM_TESIS1 = "TAG_PARAM_TESIS1";
    public final static String TAG_PARAM_TESIS2 = "TAG_PARAM_TESIS2";
    public final static String TAG_PARAM_TESISES1 = "TAG_PARAM_TESISES1";
    public final static String TAG_PARAM_TESISES2 = "TAG_PARAM_TESISES2";
    public final static String TAG_PARAM_TIPO_APROBAR_HORARIO_SUSTENTACION_TESIS2_ASESOR = "TAG_PARAM_TIPO_APROBAR_HORARIO_SUSTENTACION_TESIS2_ASESOR";
    public final static String TAG_PARAM_TIPO_APROBAR_INSCRIPCION_TESIS2_ASESOR = "TAG_PARAM_TIPO_APROBAR_INSCRIPCION_TESIS2_ASESOR";
    public final static String TAG_PARAM_TIPO_APROBAR_INSCRIPCION_TESIS1_ASESOR = "TAG_PARAM_TIPO_APROBAR_INSCRIPCION_TESIS1_ASESOR";
    public final static String TAG_PARAM_TIPO_APROBAR_PENDIENTE_TESIS1_COORDINACION = "TAG_PARAM_TIPO_APROBAR_PENDIENTE_TESIS1_COORDINACION";
    public final static String TAG_PARAM_TIPO_APROBAR_PENDIENTE_ESPECIAL_TESIS2_COORDINACION = "TAG_PARAM_TIPO_APROBAR_PENDIENTE_ESPECIAL_TESIS2_COORDINACION";
    public final static String TAG_PARAM_CLASIFICACION = "TAG_PARAM_CLASIFICACION";
    public final static String TAG_PARAM_TIPO_APROBAR_INSCRIPCION_TESIS1_COORDINADOR_MAESTRIA = "TAG_PARAM_TIPO_APROBAR_INSCRIPCION_TESIS1_COORDINADOR_MAESTRIA";
    public final static String TAG_PARAM_APROBADO_COORDINADOR_MAESTRIA = "TAG_PARAM_APROBADO_COORDINADOR_MAESTRIA";
    public final static String TAG_PARAM_RUTA_ARTICULO_FIN_TESIS_2 = "TAG_PARAM_RUTA_ARTICULO_FIN_TESIS_2";
    public final static String TAG_PARAM_RUTA_ARTICULO = "TAG_PARAM_RUTA_ARTICULO";
    public final static String TAG_PARAM_JURADOS_TESIS = "TAG_PARAM_JURADOS_TESIS";
    public final static String TAG_PARAM_JURADO_TESIS = "TAG_PARAM_JURADO_TESIS";
    public final static String TAG_PARAM_HORARIO_SUSTENTACION = "TAG_PARAM_HORARIO_SUSTENTACION";
    public final static String TAG_PARAM_JURADOS_TESIS_INTERNOS = "TAG_PARAM_JURADOS_TESIS_INTERNOS";
    public final static String TAG_PARAM_JURADOS_TESIS_EXTERNOS = "TAG_PARAM_JURADOS_TESIS_EXTERNOS";
    public final static String TAG_PARAM_VIDEO_CONFERENCIA = "TAG_PARAM_VIDEO_CONFERENCIA";
    public final static String TAG_PARAM_SKYPE = "TAG_PARAM_SKYPE";
    public final static String TAG_PARAM_SALON_SUSTENTACION = "TAG_PARAM_SALON_SUSTENTACION";
    public final static String TAG_PARAM_FECHA_SUSTENTACION = "TAG_PARAM_FECHA_SUSTENTACION";
    public final static String TAG_PARAM_JURADO_TESIS_EXTERNO = "TAG_PARAM_JURADO_TESIS_EXTERNO";
    public final static String TAG_PARAM_FECHA_INSCRIPCION_SUBAREA = "TAG_PARAM_FECHA_INSCRIPCION_SUBAREA";
    public final static String TAG_PARAM_FECHA_APROBAR_INSCRIPCION_SUBAREA = "TAG_PARAM_FECHA_APROBAR_INSCRIPCION_SUBAREA";
    public final static String TAG_PARAM_FECHA_INSCRIPCIONT1 = "TAG_PARAM_FECHA_INSCRIPCIONT1";
    public final static String TAG_PARAM_FECHA_APROBAR_T1 = "TAG_PARAM_FECHA_APROBAR_T1";
    public final static String TAG_PARAM_FECHA_NOTA_T1 = "TAG_PARAM_FECHA_NOTA_T1";
    public final static String TAG_PARAM_FECHA_PENDIENTE_T1 = "TAG_PARAM_FECHA_PENDIENTE_T1";
    public final static String TAG_PARAM_FECHA_LEVANTAR_PENDIENTE_T1 = "TAG_PARAM_FECHA_LEVANTAR_PENDIENTE_T1";
    public final static String TAG_PARAM_FECHA_FIN_INSCRIPCION_TESIS_2 = "TAG_PARAM_FECHA_FIN_INSCRIPCION_TESIS_2";
    public final static String TAG_PARAM_FECHA_APROBAR_T2 = "TAG_PARAM_FECHA_APROBAR_T2";
    public final static String TAG_PARAM_FECHA_ULTIMA_PENDIENTE_ESPECIAL_TESIS_2 = "TAG_PARAM_FECHA_ULTIMA_PENDIENTE_ESPECIAL_TESIS_2";
    public final static String TAG_PARAM_FECHA_ULTIMA_REPORTAR_REPROBADA_SS_TESIS_2 = "TAG_PARAM_FECHA_ULTIMA_REPORTAR_REPROBADA_SS_TESIS_2";
    public final static String TAG_PARAM_FECHA_ULTIMA_SUSTENTACION_TESIS_2 = "TAG_PARAM_FECHA_ULTIMA_SUSTENTACION_TESIS_2";
    public final static String TAG_PARAM_SEMESTRE_INICIO_TESIS1 = "TAG_PARAM_SEMESTRE_INICIO_TESIS1";
    public final static String TAG_PARAM_SEMESTRE_INICIO_TESIS2 = "TAG_PARAM_SEMESTRE_INICIO_TESIS2";
    public final static String TAG_PARAM_FECHA_APROBAR_INSCRIPCION_SUBAREA_COORDINACION = "TAG_PARAM_FECHA_APROBAR_INSCRIPCION_SUBAREA_COORDINACION";
    public final static String CMD_APROBAR_HORARIO_SUSTENTACION_TESIS2_POR_ASESOR = "CMD_APROBAR_HORARIO_SUSTENTACION_TESIS2_POR_ASESOR";
    public final static String TAG_PARAM_TIPO_SELECCIONAR_HORARIO_SUSTENTACION_TESIS2 = "TAG_PARAM_TIPO_SELECCIONAR_HORARIO_SUSTENTACION_TESIS2";
    public final static String TAG_PARAM_TIPO_SELECCIONAR_JURADOS_SUSTENTACION_TESIS2 = "TAG_PARAM_TIPO_SELECCIONAR_JURADOS_SUSTENTACION_TESIS2";
    public final static String TAREA_TESIS_2_ESTUDAINTE_MODIFICAR_HORARIO = "TAREA_TESIS_2_ESTUDAINTE_MODIFICAR_HORARIO";
    public final static String TAREA_TESIS_2_ESTUDIANTE_SELECCIONAR_JURADOS = "TAREA_TESIS_2_ESTUDIANTE_SELECCIONAR_JURADOS";
    public final static String TAREA_TESIS_PROFESOR_APROBAR_RECHAZAR_HORARIO_SUSTENTACION_TESIS2 = "TAREA_TESIS_PROFESOR_APROBAR_RECHAZAR_HORARIO_SUSTENTACION_TESIS2";
    public final static String CMD_ENVIAR_CORREO_ULTIMO_DIA_SELECCIONAR_HORARIO = "CMD_ENVIAR_CORREO_ULTIMO_DIA_SELECCIONAR_HORARIO";
    public final static String CMD_ULTIMO_DIA_CALIFICAR_TESIS_2 = "CMD_ULTIMO_DIA_CALIFICAR_TESIS_2";
    public final static String TAG_PARAM_APROBACION_ARTICULO_PARADIGMA = "TAG_PARAM_APROBACION_ARTICULO_PARADIGMA";
    //ruta para subir archivo de propuesta
    public final static String RUTA_ARCHIVO_TEXTO_PROPUESTA_TESIS2 = "RUTA_ARCHIVO_TEXTO_PROPUESTA_TESIS2";
    //ruta para subir artículo de tesis 1
    public final static String RUTA_ARCHIVO_ARTICULO_TESIS1 = "RUTA_ARCHIVO_ARTICULO_TESIS1";
    //ruta para subir artículo de tesis 1
    public final static String RUTA_ARCHIVO_CARTA_PENDIENTE_TESIS1 = "RUTA_ARCHIVO_CARTA_PENDIENTE_TESIS1";
    //ruta para subir documentación de solicitud de pendiente especial de tesis 2
    public final static String RUTA_ARCHIVO_DOCUMENTACION_PENDIENTE_ESPECIAL_T2 = "RUTA_ARCHIVO_DOCUMENTACION_PENDIENTE_ESPECIAL_T2";
    //estados inscripción subárea de investigación
    public final static String CTE_INSCRIPCION_APROBADA = "CTE_INSCRIPCION_APROBADA";
    public final static String CTE_INSCRIPCION_POR_APROBAR_COORDINADOR_SUB_AREA = "CTE_INSCRIPCION_POR_APROBAR_COORDINADOR_SUB_AREA";
    public final static String CTE_INSCRIPCION_RECHAZADA_POR_ASESOR = "CTE_INSCRIPCION_RECHAZADA_POR_ASESOR";
    public final static String CTE_INSCRIPCION_RECHAZADA_COORDINADOR_SUBAREA = "CTE_INSCRIPCION_RECHAZADA_COORDINADOR_SUBAREA";
    public final static String CTE_INSCRIPCION_POR_APROBAR_ASESOR = "CTE_INSCRIPCION_POR_APROBAR_ASESOR";
    public final static String CTE_INSCRIPCION_RECHAZADA_POR_TIMEOUT_DE_APROBACION = "CTE_INSCRIPCION_RECHAZADA_POR_TIMEOUT_DE_APROBACION";
    //estado inscripción tesis 1-2
    public final static String CTE_TESIS_EN_CURSO = "CTE_TESIS_EN_CURSO";
    public final static String CTE_TESIS1_TERMINADA = "CTE_TESIS1_TERMINADA";
    public final static String CTE_TESIS2_TERMINADA = "CTE_TESIS2_TERMINADA";
    public final static String CTE_TESIS_APROBADA = "CTE_TESIS_APROBADA";
    public final static String CTE_TESIS_RECHAZADA = "CTE_TESIS_RECHAZADA";
    public final static String CTE_TESIS_PENDIENTE_ESPECIAL = "CTE_TESIS_PENDIENTE_ESPECIAL";
    public final static String CTE_INSCRIPCION_RECHAZADA_POR_COORDINACION_MAESTRIA = "CTE_INSCRIPCION_RECHAZADA_POR_COORDINACION_MAESTRIA";
    public final static String CTE_TESIS_POR_APROBAR_COORDINACION_MAESTRIA = "CTE_TESIS_POR_APROBAR_COORDINACION_MAESTRIA";
    public final static String CTE_INSCRIPCION_POR_REAPROBAR_APROBAR_ASESOR = "CTE_INSCRIPCION_POR_REAPROBAR_APROBAR_ASESOR";
    public final static String CTE_TESIS_PERDIDA = "CTE_TESIS_PERDIDA";
    public final static String TAG_PARAM_TESIS_1_PERDIDA = "TAG_PARAM_TESIS_1_PERDIDA";
    public final static String TAG_PARAM_TESIS_2_PERDIDA = "TAG_PARAM_TESIS_2_PERDIDA";
    public final static String TAG_PARAM_TESIS_1_RECHAZADA = "TAG_PARAM_TESIS_1_RECHAZADA";
    public final static String TAG_PARAM_TESIS_2_RECHAZADA = "TAG_PARAM_TESIS_2_RECHAZADA";
    public final static String TAG_PARAM_TESIS_1_RETIRADA = "TAG_PARAM_TESIS_1_RETIRADA";
    public final static String TAG_PARAM_TESIS_2_RETIRADA = "TAG_PARAM_TESIS_2_RETIRADA";
    public final static String TAG_PARAM_TESIS_1_TERMINADA = "TAG_PARAM_TESIS_1_TERMINADA";
    public final static String TAG_PARAM_TESIS_2_TERMINADA = "TAG_PARAM_TESIS_2_TERMINADA";
    public final static String CTE_TESIS_ESPERANDO_APROBACION_PENDIENTE = "CTE_TESIS_ESPERANDO_APROBACION_PENDIENTE";
    public final static String CTE_TESIS_1_RETIRADA = "CTE_TESIS_1_RETIRADA";
    public final static String CTE_TESIS_2_RETIRADA = "CTE_TESIS_2_RETIRADA";
    public final static String CTE_TESIS_EN_PENDIENTE = "CTE_TESIS_EN_PENDIENTE";
    public final static String CTE_NOTA_APROBADA = "CTE_NOTA_APROBADA";
    public final static String CTE_NOTA_REPROBADA = "CTE_NOTA_REPROBADA";
    public final static String CTE_NUCLEO_BASICO = "CTE_NUCLEO_BASICO";
    public final static String CTE_CURSO_SUBAREA = "CTE_CURSO_SUBAREA";
    public final static String CTE_CURSO_NIVELATORIO = "CTE_CURSO_NIVELATORIO";
    public final static String CTE_CORREO_COORDINADOR_MAESTRTIA = "CTE_CORREO_COORDINADOR_MAESTRTIA";
    public final static String CTE_TESIS_ESPERANDO_APROBACION_PENDIENTE_ESPECIAL = "CTE_TESIS_ESPERANDO_APROBACION_PENDIENTE_ESPECIAL";
    public final static String CMD_ENVIAR_CORREO_ULTIMO_DIA_INSCRIPCION_TESIS_2 = "CMD_ENVIAR_CORREO_ULTIMO_DIA_INSCRIPCION_TESIS_2";
    public final static String CMD_ENVIAR_CORREO_ULTIMO_DIA_REPROBAR_TESIS_2_SIN_SUSTENTACION = "CMD_ENVIAR_CORREO_ULTIMO_DIA_REPROBAR_TESIS_2_SIN_SUSTENTACION";
    public final static String CMD_ENVIAR_CORREO_ULTIMO_DIA_PEDIR_PENDIENTE_ESPECIAL_TESIS_2 = "CMD_ENVIAR_CORREO_ULTIMO_DIA_PEDIR_PENDIENTE_ESPECIAL_TESIS_2";
    public final static String CMD_ENVIAR_CORREO_ULTIMO_DIA_SUSTENTACION_TESIS_2 = "CMD_ENVIAR_CORREO_ULTIMO_DIA_SUSTENTACION_TESIS_2";
    public final static String CMD_FIN_INSCRIPCION_SUBAREA = "CMD_FIN_INSCRIPCION_SUBAREA";
    public final static String CMD_FIN_INSCRIPCION_TESIS_1 = "CMD_FIN_INSCRIPCION_TESIS_1";
    public final static String CMD_FIN_DIA_SUBIR_NOTAS_TESIS_1 = "CMD_FIN_DIA_SUBIR_NOTAS_TESIS_1";
    public final static String CMD_FIN_DIA_QUITAR_PENDIENTE_NOTAS_TESIS_1 = "CMD_FIN_DIA_QUITAR_PENDIENTE_NOTAS_TESIS_1";
    public final static String CMD_FIN_DIA_INSCRIPCION_TESIS_2 = "CMD_FIN_DIA_INSCRIPCION_TESIS_2";
    public final static String CMD_FIN_DIA_SUSTENTACION_TESIS_2 = "CMD_FIN_DIA_SUSTENTACION_TESIS_2";
    public final static String TAG_PARAM_TIPO_RESERVAR_SALON_PARA_SUSTENTACION_TESIS_2 = "TAG_PARAM_TIPO_RESERVAR_SALON_PARA_SUSTENTACION_TESIS_2";
    public final static String CTE_CORREO_SECRETARIO_COORDINADOR_MAESTRTIA = "CTE_CORREO_SECRETARIO_COORDINADOR_MAESTRTIA";
    public final static String CMD_CAMBIAR_ESTADO_SUSTENTACION_TESIS_2_A_ESPERAR_ARTICULO = "CMD_CAMBIAR_ESTADO_SUSTENTACION_TESIS_2_A_ESPERAR_ARTICULO";
    public final static String CTE_ESPERANDO_ARTICULO_FINAL_TESIS_2 = "CTE_ESPERANDO_ARTICULO_FINAL_TESIS_2";
    public final static String TAG_PARAM_TIPO_SUBIR_ARTICULO_TESIS_2 = "TAG_PARAM_TIPO_SUBIR_ARTICULO_TESIS_2";
    public final static String CMD_SUBIR_ARTICULO_TESIS_2_FINAL = "CMD_SUBIR_ARTICULO_TESIS_2_FINAL";
    public final static String RUTA_ARCHIVO_ARTICULO_TESIS2 = "RUTA_ARCHIVO_ARTICULO_TESIS2";
    public final static String CTE_ESPERANDO_NOTA_FINAL_TESIS_2 = "CTE_ESPERANDO_NOTA_FINAL_TESIS_2";
    public final static String CMD_BUSCAR__TESIS_2_POR_HORARIO = "CMD_BUSCAR__TESIS_2_POR_HORARIO";
    public final static String CMD_AGREGAR_MODIFICAR_TEMA_TESIS_1 = "CMD_AGREGAR_MODIFICAR_TEMA_TESIS_1";
    public final static String CMD_DAR_TEMAS_TESIS_1 = "CMD_DAR_TEMAS_TESIS_1";
    public final static String CMD_DAR_TEMAS_TESIS_1_POR_ASESOR = "CMD_DAR_TEMAS_TESIS_1_POR_ASESOR";
    public final static String CMD_ELIMINAR_TEMA_TESIS = "CMD_ELIMINAR_TEMA_TESIS";
    public final static String TAG_PARAM_TEMAS_TESIS = "TAG_PARAM_TEMAS_TESIS";
    public final static String TAG_PARAM_FECHA_MAXIMA_SUBIR_TEMAS_TESIS = "TAG_PARAM_FECHA_MAXIMA_SUBIR_TEMAS_TESIS";
    public final static String TAG_PARAM_COMENTARIO_TESIS = "TAG_PARAM_COMENTARIO_TESIS";
    public final static String CMD_AGREGAR_COMENTARIO_TESIS_1 = "CMD_AGREGAR_COMENTARIO_TESIS_1";
    public final static String CMD_AGREGAR_COMENTARIO_TESIS_2 = "CMD_AGREGAR_COMENTARIO_TESIS_2";
    public final static String CMD_DAR_COMENTARIO_TESIS_2 = "CMD_DAR_COMENTARIO_TESIS_2";
    public final static String CMD_DAR_COMENTARIO_TESIS_1 = "CMD_DAR_COMENTARIO_TESIS_1";
    public final static String TAG_PARAM_COMENTARIO = "TAG_PARAM_COMENTARIO";
    public final static String TAG_PARAM_DEBE_RETIRAR = "TAG_PARAM_DEBE_RETIRAR";
    public final static String TAG_PARAM_COMENTARIOS_TESIS = "TAG_PARAM_COMENTARIOS_TESIS";
    public final static String TAG_PARAM_FECHA_30_PORCIENTO = "TAG_PARAM_FECHA_30_PORCIENTO";
    public final static String CMD_ENVIAR_CORREO_ULTIMA_FECHA_PROPUESTAS_TESIS = "CMD_ENVIAR_CORREO_ULTIMA_FECHA_PROPUESTAS_TESIS";
    public final static String CMD_COMPORTAMIENTO_COMENTARIOS_TESIS_30_PORCIENTO = "CMD_COMPORTAMIENTO_COMENTARIOS_TESIS_30_PORCIENTO";
    public final static String TAG_PARAM_FECHA_APROBAR_T1_COODINACION = "TAG_PARAM_FECHA_APROBAR_T1_COODINACION";
    public final static String TAG_PARAM_FECHA_RETIRAR_TESIS = "TAG_PARAM_FECHA_RETIRAR_TESIS";
    public final static String CMD_COMPORTAMIENTO_FECHA_RETIROS_TESIS = "CMD_COMPORTAMIENTO_FECHA_RETIROS_TESIS";
    public final static String TAREA_SUBIR_ARTICULO_TESIS2 = "TAREA_SUBIR_ARTICULO_TESIS2";
    public final static String CMD_FIN_INSCRIPCION_SUBAREA_COORDINADOR_SUBAREA = "CMD_FIN_INSCRIPCION_SUBAREA_COORDINADOR_SUBAREA";
    public final static String CMD_FIN_INSCRIPCION_TESIS_1_COORDINACION = "CMD_FIN_INSCRIPCION_TESIS_1_COORDINACION";
    public final static String TAG_PARAM_ESTADO_HORARIO_SUSTENTACION_TESIS2 = "TAG_PARAM_ESTADO_HORARIO_SUSTENTACION_TESIS2";
    public final static String CTE_HORARIO_PENDIENTE = "CTE_HORARIO_PENDIENTE";
    public final static String CTE_HORARIO_SELECCIONADO = "CTE_HORARIO_SELECCIONADO";
    public final static String CTE_HORARIO_APROBADO = "CTE_HORARIO_APROBADO";
    public final static String CMD_CREAR_TAREAS_JURADOD_TESIS = "CMD_CREAR_TAREAS_JURADOD_TESIS";
    public final static String CMD_CONSULTAR_JURADO_TESIS_2 = "CMD_CONSULTAR_JURADO_TESIS_2";
    public final static String CMD_EDITAR_JURADO_TESIS_2_EXTERNO = "CMD_EDITAR_JURADO_TESIS_2_EXTERNO";
    public final static String TAG_PARAM_TIPO_CALIFICAR_SUSTENTACION_TESIS2 = "TAG_PARAM_TIPO_CALIFICAR_SUSTENTACION_TESIS2";
    public final static String TAG_PARAM_HASH_CALIFICACION = "TAG_PARAM_HASH_CALIFICACION";
    public final static String TAREA_TESIS_2_JURADO_INTERNO_CALIFICAR_SUSTENTACION = "TAREA_TESIS_2_JURADO_INTERNO_CALIFICAR_SUSTENTACION";
    public final static String CTE_JURADO_TESIS_INTERNO = "CTE_JURADO_TESIS_INTERNO";
    public final static String CTE_ASESOR_TESIS = "CTE_ASESOR_TESIS";
    public final static String CTE_COASESOR_TESIS = "CTE_COASESOR_TESIS";
    public final static String CTE_JURADO_EXTERNO = "CTE_JURADO_EXTERNO";
    public final static String CTE_URL_CALIFICACION_JURADOS_EXTERNOS = "CTE_URL_CALIFICACION_JURADOS_EXTERNOS";
    public final static String CMD_EDITAR_SALON_SUSTENTACION_TESIS_2 = "CMD_EDITAR_SALON_SUSTENTACION_TESIS_2";
    public final static String CTE_TESIS_MAXIMO_NUM_COASESORES = "CTE_TESIS_MAXIMO_NUM_COASESORES";
    public final static String TAG_PARAM_PORCENTAJE_CATEGORIA = "TAG_PARAM_PORCENTAJE_CATEGORIA";
    public final static String TAG_PARAM_CATEGORIAS_CRITERIOS_TESIS_2 = "TAG_PARAM_CATEGORIAS_CRITERIOS_TESIS_2";
    public final static String TAG_PARAM_CATEGORIA_CRITERIO_TESIS_2 = "TAG_PARAM_CATEGORIA_CRITERIO_TESIS_2";
    public final static String TAG_PARAM_RUTA_ARCHIVO_SOLICITUDUD_PENDIENTE_ESPECIAL = "TAG_PARAM_RUTA_ARCHIVO_SOLICITUDUD_PENDIENTE_ESPECIAL";
    public final static String CTE_TESIS2_RUTA_PENDIENTES_ESPECIALES = "CTE_TESIS2_RUTA_PENDIENTES_ESPECIALES";
    public final static String CMD_PEDIR_PENDIENTE_ESPECIAL_TESIS2_ASESOR = "CMD_PEDIR_PENDIENTE_ESPECIAL_TESIS2_ASESOR";
    public final static String CMD_MODIFICAR_COASESORES_TESIS_2 = "CMD_MODIFICAR_COASESORES_TESIS_2";
    public final static String TAG_PARAM_COASESORES_TESIS_2 = "TAG_PARAM_COASESORES_TESIS_2";
    public final static String TAG_PARAM_COASESOR = "TAG_PARAM_COASESOR";
    public final static String CTE_TESIS2_COASESOR = "CTE_TESIS2_COASESOR";
    public final static String TAG_PARAM_SI_ESTA_EN_PENDIENTE = "TAG_PARAM_SI_ESTA_EN_PENDIENTE";
    public final static String CMD_ENVIAR_CORREOS_SUSTENTACIONES_TESIS_2_PENDIENTE_ESPECIAL = "CMD_ENVIAR_CORREOS_SUSTENTACIONES_TESIS_2_PENDIENTE_ESPECIAL";
    public final static String CMD_DAR_SOLICITUDES_TESIS_2_ASESOR_CONSULTA_EXTERNOS = "CMD_DAR_SOLICITUDES_TESIS_2_ASESOR_CONSULTA_EXTERNOS";
    public final static String VAL_PROCESO_TESIS = "VAL_PROCESO_TESIS";
    public final static String VAL_MODULO_TESIS = "VAL_MODULO_TESIS";
    public final static String VAL_ACCION_AGREGAR_TEMA_TESIS = "VAL_ACCION_AGREGAR_TEMA_TESIS";
    public final static String VAL_ACCION_APROBAR_SOLICITUD_TESIS_2_ASESOR = "VAL_ACCION_APROBAR_SOLICITUD_TESIS_2_ASESOR";
    public final static String VAL_ACCION_APROBAR_INSCRIPCION_SUBAREA_INVESTIGACION_ASESOR = "VAL_ACCION_APROBAR_INSCRIPCION_SUBAREA_INVESTIGACION_ASESOR";
    public final static String VAL_ACCION_APROBAR_INSCRIPCION_SUBAREA_INVESTIGACION_COORDINACION = "VAL_ACCION_APROBAR_INSCRIPCION_SUBAREA_INVESTIGACION_COORDINACION";
    public final static String VAL_ACCION_REALIZAR_INFORME_30 = "VAL_ACCION_REALIZAR_INFORME_30";
    public final static String VAL_ACCION_APROBAR_INSCRIPCION_TESIS1_COORDINACION = "VAL_ACCION_APROBAR_INSCRIPCION_TESIS1_COORDINACION";
    public final static String VAL_ACCION_APROBAR_INSCRIPCION_TESIS1_ASESOR = "VAL_ACCION_APROBAR_INSCRIPCION_TESIS1_ASESOR";
    public final static String VAL_ACCION_REPORTAR_NOTA = "VAL_ACCION_REPORTAR_NOTA";
    public final static String VAL_ACCION_SOLICITAR_PENDIENTE = "VAL_ACCION_SOLICITAR_PENDIENTE";
    public final static String VAL_ACCION_LEVANTAR_PENDIENTE = "VAL_ACCION_LEVANTAR_PENDIENTE";
    public final static String VAL_ACCION_REPORTAR_NOTA_REPROBADA = "VAL_ACCION_REPORTAR_NOTA_REPROBADA";
    public final static String VAL_ACCION_SOLICITAR_PENDIENTE_ESPECIAL = "VAL_ACCION_SOLICITAR_PENDIENTE_ESPECIAL";
    public final static String TAG_PARAM_COMENTARIOS_EXTEMPORAL = "TAG_PARAM_COMENTARIOS_EXTEMPORAL";
    public final static String TAG_PARAM_RUTA_ARCHIVO_EXTEMPORAL = "TAG_PARAM_RUTA_ARCHIVO_EXTEMPORAL";
    public final static String RUTA_ARCHIVO_EXTEMPORANEO_TESIS2 = "RUTA_ARCHIVO_EXTEMPORANEO_TESIS2";
    public final static String CMD_AGREGAR_SOLICITUD_TESIS1_EXTEMPORANEA = "CMD_AGREGAR_SOLICITUD_TESIS1_EXTEMPORANEA";
    public final static String TAG_PARAM_FECHA_MAXIMA_SUSTENTACION_PENDIENTE_ESPECIAL_TESIS_2 = "TAG_PARAM_FECHA_MAXIMA_SUSTENTACION_PENDIENTE_ESPECIAL_TESIS_2";
    public final static String CMD_LANZAR_COMPORTAMIENTO_EMERGENCIA_JURADOS_TESIS_2 = "CMD_LANZAR_COMPORTAMIENTO_EMERGENCIA_JURADOS_TESIS_2";
    public final static String CMD_LANZAR_COMPORTAMIENTO_EMERGENCIA_MIGRAR_TESIS_2 = "CMD_LANZAR_COMPORTAMIENTO_EMERGENCIA_MIGRAR_TESIS_2";
    public final static String CMD_LANZAR_COMPORTAMIENTO_EMERGENCIA_MIGRAR_TESIS_1 = "CMD_LANZAR_COMPORTAMIENTO_EMERGENCIA_MIGRAR_TESIS_1";
    public final static String CMD_LANZAR_COMPORTAMIENTO_EMERGENCIA_MIGRAR_PROYECTO_DE_GRADO = "CMD_LANZAR_COMPORTAMIENTO_EMERGENCIA_MIGRAR_PROYECTO_DE_GRADO";
    public final static String TAG_PARAM_CANCELADA = "TAG_PARAM_CANCELADA";
    public final static String TAG_PARAM_TIPO_SUBIR_NOTA_TESIS_2_COORDINACION = "TAG_PARAM_TIPO_SUBIR_NOTA_TESIS_2_COORDINACION";
    public final static String TAG_PARAM_FECHA_MAXIMA_SOLICITAR_HORARIO_SUSTENTACION = "TAG_PARAM_FECHA_MAXIMA_SOLICITAR_HORARIO_SUSTENTACION";
    public final static String TAG_PARAM_FECHA_MAXIMA_CALIFICAR_TESIS_2 = "TAG_PARAM_FECHA_MAXIMA_CALIFICAR_TESIS_2";
    public final static String CTE_CODIGOS_MAESTRIAS_CON_TESIS = "CTE_CODIGOS_MAESTRIAS_CON_TESIS";
    public final static String TAREA_INSCRIBIR_TESIS1_BANNER_POR_COODINACION = "TAREA_INSCRIBIR_TESIS1_BANNER_POR_COODINACION";
    public final static String TAREA_INSCRIBIR_TESIS2_BANNER_POR_COODINACION = "TAREA_INSCRIBIR_TESIS2_BANNER_POR_COODINACION";
    public final static String CMD_DAR_SOLICITUDES_TESIS1_BANNER_PARA_COORDINACION = "CMD_DAR_SOLICITUDES_TESIS1_BANNER_PARA_COORDINACION";
    public final static String CMD_ESTABLECER_APROBACION_REVISTA_PARADIGMA_TESIS2 = "CMD_ESTABLECER_APROBACION_REVISTA_PARADIGMA_TESIS2";
    public final static String CMD_ESTABLECER_APROBACION_REVISTA_PARADIGMA_HISTORICOS_TESIS_2 = "CMD_ESTABLECER_APROBACION_REVISTA_PARADIGMA_HISTORICOS_TESIS_2";
    public final static String CMD_ESTABLECER_APROBACION_REVISTA_PARADIGMA_TESIS1 = "CMD_ESTABLECER_APROBACION_REVISTA_PARADIGMA_TESIS1";
    public final static String CMD_ESTABLECER_APROBACION_REVISTA_PARADIGMA_HISTORICOS = "CMD_ESTABLECER_APROBACION_REVISTA_PARADIGMA_HISTORICOS";
    public final static String CMD_ESTABLECER_APROBACION_REVISTA_PARADIGMA_HISTORICOS_TESIS_1 = "CMD_ESTABLECER_APROBACION_REVISTA_PARADIGMA_HISTORICOS_TESIS_1";
    // public final static String TAG_PARAM_HORARIO_APROBADO_ASESOR="TAG_PARAM_HORARIO_APROBADO_ASESOR";
    /*CMD_FIN_INSCRIPCION_TESIS_1_COORDINACION
     *
     * Administracion Sisinfo
     */
    public final static String CMD_RECARGAR_TIMERS = "CMD_RECARGAR_TIMERS";
    public final static String CMD_CONSULTAR_TIMERS = "CMD_CONSULTAR_TIMERS";
    public final static String CMD_ARREGLAR_TIMERS = "CMD_ARREGLAR_TIMERS";
    public final static String CMD_EJECUTAR_TIMER_EN_MEMORIA = "CMD_EJECUTAR_TIMER_EN_MEMORIA";
    public final static String CMD_ELIMINAR_TIMER = "CMD_ELIMINAR_TIMER";
    public final static String CMD_DETENER_TIMER = "CMD_DETENER_TIMER";
    public final static String CMD_EDITAR_TIMER = "CMD_EDITAR_TIMER";
    public final static String CMD_CONSULTAR_TIMER = "CMD_CONSULTAR_TIMER";
    public final static String TAG_TIMERS = "TAG_TIMERS";
    public final static String TAG_TIMERS_MEMORIA = "TAG_TIMERS_MEMORIA";
    public final static String TAG_TIMERS_BD = "TAG_TIMERS_BD";
    public final static String TAG_TIMER = "TAG_TIMER";
    public final static String TAG_CLASE_TIMER = "TAG_CLASE_TIMER";
    public final static String TAG_METODO_TIMER = "TAG_METODO_TIMER";
    public final static String TAG_FECHA_TIMER = "TAG_FECHA_TIMER";
    public final static String TAG_PARAMETRO_TIMER = "TAG_PARAMETRO_TIMER";
    public final static String TAG_PARAM_EXISTE_EN_BD = "TAG_PARAM_EXISTE_EN_BD";
    public final static String TAG_PARAM_EXISTE_EN_MEMORIA = "TAG_PARAM_EXISTE_EN_MEMORIA";
    public final static String CMD_ADMINISTRADOR_SISINFO = "CMD_ADMINISTRADOR_SISINFO";

    /*
     * REserva de citas con coordinacion
     */
    public final static String CMD_GUARDAR_ESTADO_RESERVAS = "CMD_GUARDAR_ESTADO_RESERVAS";
    public final static String VAL_RESERVA_CUMPLIDA = "VAL_RESERVA_CUMPLIDA";
    public final static String VAL_RESERVA_NO_CUMPLIDA = "VAL_RESERVA_NO_CUMPLIDA";
    public final static String VAL_RESERVA_PENDIENTE = "VAL_RESERVA_PENDIENTE";
    public final static String TAG_PARAM_ESTADO_CITA = "TAG_PARAM_ESTADO_CITA";
    public final static String CMD_CONSULTAR_DATOS_CONTACTO = "CMD_CONSULTAR_DATOS_CONTACTO";
    public final static String CMD_MODIFICAR_RESERVA = "CMD_MODIFICAR_RESERVA";
    public final static String CMD_CANCELAR_RESERVA = "CMD_CANCELAR_RESERVA";
    public final static String CMD_CONSULTAR_RESERVAS_ACTUALES = "CMD_CONSULTAR_RESERVAS_ACTUALES";
    public final static String CMD_CONSULTAR_HORARIO_COORDINACION = "CMD_CONSULTAR_HORARIO_COORDINACION";
    public final static String CMD_CONSULTAR_RESERVAS_LOGIN = "CMD_CONSULTAR_RESERVAS_LOGIN";
    public final static String CMD_RESERVAR_CITA = "CMD_RESERVAR_CITA";
    public final static String CMD_EDITAR_HORARIO_COORDINACION = "CMD_EDITAR_HORARIO_COORDINACION";
    public final static String CMD_CONSULTAR_RESERVAS = "CMD_CONSULTAR_RESERVAS";
    public final static String CMD_AGREGAR_LISTA_NEGRA_RESERVA_CITAS = "CMD_AGREGAR_LISTA_NEGRA_RESERVA_CITAS";
    public final static String CMD_EDITAR_LISTA_NEGRA_RESERVA_CITAS = "CMD_EDITAR_LISTA_NEGRA_RESERVA_CITAS";
    public final static String CMD_ELIMINAR_LISTA_NEGRA_RESERVA_CITAS = "CMD_ELIMINAR_LISTA_NEGRA_RESERVA_CITAS";
    public final static String CMD_CONSULTAR_LISTA_NEGRA_RESERVA_CITAS = "CMD_CONSULTAR_LISTA_NEGRA_RESERVA_CITAS";
    public final static String TAG_PARAM_LISTA_NEGRA_RESERVA_CITAS = "TAG_PARAM_LISTA_NEGRA_RESERVA_CITAS";
    public final static String TAG_PARAM_LISTAS_NEGRAS_RESERVA_CITAS = "TAG_PARAM_LISTAS_NEGRAS_RESERVA_CITAS";
    public final static String TAG_PARAM_FECHA_VENCIMIENTO = "TAG_PARAM_FECHA_VENCIMIENTO";
    public final static String TAG_RESERVAS = "TAG_RESERVAS";
    public final static String TAG_RESERVA = "TAG_RESERVA";
    public final static String TAG_ID_RESERVA = "TAG_ID_RESERVA";
    public final static String TAG_PARAM_RESERVA = "TAG_PARAM_RESERVA";
    public final static String TAG_PARAM_FECHA_CITA = "TAG_PARAM_FECHA_CITA";
    public final static String TAG_PARAM_ANO_CITA = "TAG_PARAM_AÑO_CITA";
    public final static String TAG_PARAM_MES_CITA = "TAG_PARAM_MES_CITA";
    public final static String TAG_PARAM_DIA_CITA = "TAG_PARAM_DIA_CITA";
    public final static String TAG_PARAM_HORARIO_CITAS = "TAG_PARAM_HORARIO_CITAS";
    public final static String TAG_PARAM_NUMERO_DIA_CITA = "TAG_PARAM_NUMERO_DIA_CITA";
    public final static String TAG_PARAM_HORA_CITA = "TAG_PARAM_HORA_CITA";
    public final static String TAG_PARAM_MINUTO_CITA = "TAG_PARAM_MINUTO_CITA";
    public final static String TAG_PARAM_INICIO_CITA = "TAG_PARAM_INICIO_CITA";
    public final static String TAG_PARAM_FIN_CITA = "TAG_PARAM_FIN_CITA";
    public final static String TAG_PARAM_MOTIVO_CITA = "TAG_PARAM_MOTIVO_CITA";
    public final static String TAG_PARAM_PROGRAMA_CITA = "TAG_PARAM_PROGRAMA_CITA";
    public final static String TAG_PARAM_COMENTARIOS_CITA = "TAG_PARAM_COMENTARIOS_CITA";
    public final static String TAG_PARAM_NOMBRE_CONTACTO = "TAG_PARAM_NOMBRE_CONTACTO";
    public final static String TAG_PARAM_CELULAR_CONTACTO = "TAG_PARAM_CELULAR_CONTACTO";
    public final static String TAG_PARAM_USUARIO = "TAG_PARAM_USUARIO";
    public final static String VAL_CORREO_SECRETARIO_COORDINACION_RESERVAS = "VAL_CORREO_SECRETARIO_COORDINACION_RESERVAS";
    public final static String CMD_CONSULTAR_RESERVAS_DIA = "CMD_CONSULTAR_RESERVAS_DIA";
    public final static String TAG_PARAM_TIPO_INGRESAR_COMENTARIO_TESIS = "TAG_PARAM_TIPO_INGRESAR_COMENTARIO_TESIS";
    public final static String CMD_DAR_TEMA_TESIS_POR_ID = "CMD_DAR_TEMA_TESIS_POR_ID";
    public final static String TAG_PARAM_TIPO_INGRESAR_COMENTARIO_TESIS_1 = "TAG_PARAM_TIPO_INGRESAR_COMENTARIO_TESIS_1";
    public final static String TAG_PARAM_TIPO_INGRESAR_COMENTARIO_TESIS_2 = "TAG_PARAM_TIPO_INGRESAR_COMENTARIO_TESIS_2";
    public final static String CMD_DAR_SOLICITUD_TESIS1_PARA_COMENTARIO = "CMD_DAR_SOLICITUD_TESIS1_PARA_COMENTARIO";
    public final static String CMD_DAR_TESIS2_POR_ID_PARA_30_PORCIENTO = "CMD_DAR_TESIS2_POR_ID_PARA_30_PORCIENTO";
    public final static String VAL_RESERVA_CITAS_NUMERO_DIAS_NOTIFICACION = "VAL_RESERVA_CITAS_NUMERO_DIAS_NOTIFICACION";
    public final static String TAG_PARAM_MOTIVO_CANCELACION = "TAG_PARAM_MOTIVO_CANCELACION";
    public final static String CMD_CANCELAR_GRUPO_RESERVAS = "CMD_CANCELAR_GRUPO_RESERVAS";
    public final static String CMD_CONSULTAR_RANGO_RESERVAS = "CMD_CONSULTAR_RANGO_RESERVAS";
    public final static String TAG_PARAM_ESTADO_CANCELADO_COORDINACION = "TAG_PARAM_ESTADO_CANCELADO_COORDINACION";
    public final static String VAL_RESERVA_CITAS_NUMERO_DIAS_EN_LISTA_NEGRA = "VAL_RESERVA_CITAS_NUMERO_DIAS_EN_LISTA_NEGRA";

    /*
     * Sistema de inventarios y reservas
     */
    //comandos
    public final static String TAG_PARAM_NOMBRE_MODULO = "TAG_PARAM_NOMBRE_MODULO";
    public final static String CTE_NOMBRE_MODULO_RESERVAS_E_INVENTARIO = "CTE_NOMBRE_MODULO_RESERVAS_E_INVENTARIO";
    public final static String CMD_DAR_LISTA_EQUIPOS_LABORATORIO_PARA_ADMINISTRADOR = "CMD_DAR_LISTA_EQUIPOS_LABORATORIO_PARA_ADMINISTRADOR";
    public final static String CMD_AGREGAR_EQUIPO_LABORATORIO = "CMD_AGREGAR_EQUIPO_LABORATORIO";
    public final static String CMD_ELIMINAR_EQUIPO_LABORATORIO = "CMD_ELIMINAR_EQUIPO_LABORATORIO";
    public final static String CMD_PONER_EN_CIRCULACION_ELEMENTO_LABORATORIO = "CMD_PONER_EN_CIRCULACION_ELEMENTO_LABORATORIO";
    public final static String CMD_CAMBIAR_ESTADO_CIRCULACION_EQUIPO_LABORATORIO = "CMD_CAMBIAR_ESTADO_CIRCULACION_EQUIPO_LABORATORIO";
    public final static String CMD_AGREGAR_USUARIO_LABORATORIO = "CMD_AGREGAR_USUARIO_LABORATORIO";
    public final static String CMD_ELIMINAR_USUARIO_LABORATORIO = "CMD_ELIMINAR_USUARIO_LABORATORIO";
    public final static String CMD_DAR_LABORATORIOS_INVENTARIO = "CMD_DAR_LABORATORIOS_INVENTARIO";
    public final static String CMD_DAR_USUARIOS_LABORATORIO = "CMD_DAR_USUARIOS_LABORATORIO";
    public final static String CMD_CREAR_RESERVA_INVENTARIO = "CMD_CREAR_RESERVA_INVENTARIO";
    public final static String CMD_DAR_EQUIPOS_LABORATORIOS_INVENTARIO = "CMD_DAR_EQUIPOS_LABORATORIOS_INVENTARIO";
    public final static String CMD_DAR_RESERVAS_EQUIPOS_LABORATORIOS_INVENTARIO = "CMD_DAR_RESERVAS_EQUIPOS_LABORATORIOS_INVENTARIO";
    public final static String CMD_ELIMINAR_RESERVA_EQUIPO_LABORATORIO_INVENTARIO = "CMD_ELIMINAR_RESERVA_EQUIPO_LABORATORIO_INVENTARIO";
    public final static String CMD_PRESTAR_EQUIPO_LABORATORIO = "CMD_PRESTAR_EQUIPO_LABORATORIO";
    public final static String CMD_DAR_PRESTAMOS_LABORATORIO_EN_CURSO = "CMD_DAR_PRESTAMOS_LABORATORIO_EN_CURSO";
    public final static String CMD_DAR_DETALLES_PRESTAMO_LABORATORIO = "CMD_DAR_DETALLES_PRESTAMO_LABORATORIO";
    public final static String CMD_DAR_DETALLES_USUARIO_LABORATORIO = "CMD_DAR_DETALLES_USUARIO_LABORATORIO";
    public final static String CMD_TERMINAR_PRESTAMO_LABORATORIO = "CMD_TERMINAR_PRESTAMO_LABORATORIO";
    public final static String CMD_DAR_PRESTAMOS_LABORATORIO_POR_FECHAS_Y_ELEMENTO = "CMD_DAR_PRESTAMOS_LABORATORIO_POR_FECHAS_Y_ELEMENTO";
    public final static String CMD_CREAR_GRUPO_LABORATORIO = "CMD_CREAR_GRUPO_LABORATORIO";
    public final static String CMD_EDITAR_GRUPO_LABORATORIO = "CMD_EDITAR_GRUPO_LABORATORIO";
    public final static String CMD_BORRAR_GRUPO_LABORATORIO = "CMD_BORRAR_GRUPO_LABORATORIO";
    public final static String CMD_DAR_PERMISOS_GRUPO_LABORATORIO = "CMD_DAR_PERMISOS_GRUPO_LABORATORIO";
    public final static String CMD_CONSULTAR_RESERVA_ISMULTIPLE = "CMD_CONSULTAR_RESERVA_ISMULTIPLE";
    public final static String CMD_CANCELAR_RESERVAMULTIPLE= "CMD_CANCELAR_RESERVAMULTIPLE";

    //comandos de timmers:
    public final static String CMD_REVISAR_ENTREGA_FIN_DIA_ELEMENTO = "CMD_REVISAR_ENTREGA_FIN_DIA_ELEMENTO";
    public final static String CMD_ENVIAR_CORREO_VENCIMIENTO_USUARIO = "CMD_ENVIAR_CORREO_VENCIMIENTO_USUARIO";
    public final static String CMD_VENCIMIENTO_USUARIO = "CMD_VENCIMIENTO_USUARIO";
    public final static String CMD_COMPROBAR_FIN_PRESTAMO = "CMD_COMPROBAR_FIN_PRESTAMO";
    //constantes
    public final static String CTE_ELEMENTO_LABORATORIO_DISPONIBLE = "CTE_ELEMENTO_LABORATORIO_DISPONIBLE";
    public final static String CTE_ELEMENTO_LABORATORIO_FUERA_CIRCULACION = "CTE_ELEMENTO_LABORATORIO_FUERA_CIRCULACION";
    //para reservas
    public final static String CTE_RESERVA_CREADA = "CTE_RESERVA_CREADA";
    public final static String CTE_PRESTAMO_EN_CURSO_REI = "CTE_PRESTAMO_EN_CURSO_REI";
    public final static String CTE_PRESTAMO_TERMINADO_OK = "CTE_PRESTAMO_TERMINADO_OK";
    public final static String CTE_PRESTAMO_TERMINADO_OK_TARDE = "CTE_PRESTAMO_TERMINADO_OK_TARDE";
    //tagas
    public final static String TAG_PARAM_ITEM_LABORATORIO = "TAG_PARAM_ITEM_LABORATORIO";
    public final static String TAG_PARAM_LABORATORIOS = "TAG_PARAM_LABORATORIOS";
    public final static String TAG_PARAM_LABORATORIO = "TAG_PARAM_LABORATORIO";
    public final static String TAG_PARAM_ELEMENTOS_LABORATORIO = "TAG_PARAM_ELEMENTOS_LABORATORIO";
    public final static String TAG_PARAM_DESCRIPCION_ELEMENTO = "TAG_PARAM_DESCRIPCION_ELEMENTO";
    public final static String TAG_PARAM_SERIAL = "TAG_PARAM_SERIAL";
    public final static String TAG_PARAM_ES_CAJA = "TAG_PARAM_ES_CAJA";
    public final static String TAG_PARAM_ESTA_PRESTADO = "TAG_PARAM_ESTA_PRESTADO";
    public final static String TAG_PARAM_HORA_INICIO = "TAG_PARAM_HORA_INICIO";
    public final static String TAG_PARAM_HORA = "TAG_PARAM_HORA";
    public final static String TAG_PARAM_MINUTO = "TAG_PARAM_MINUTO";
    public final static String TAG_PARAM_MINUTO_INICIO = "TAG_PARAM_MINUTO_INICIO";
    public final static String TAG_PARAM_HORA_FIN = "TAG_PARAM_HORA_FIN";
    public final static String TAG_PARAM_MINUTO_FIN = "TAG_PARAM_MINUTO_FIN";
    public final static String TAG_PARAM_ELEMENTOS_CAJA = "TAG_PARAM_ELEMENTOS_CAJA";
    public final static String TAG_PARAM_ADMINISTRADOR_LABORATORIO = "TAG_PARAM_ADMINISTRADOR_LABORATORIO";
    public final static String TAG_PARAM_ADMINISTRADOR_INVENTARIO = "TAG_PARAM_ADMINISTRADOR_INVENTARIO";
    public final static String TAG_PARAM_USUARIOS_LABORATORIO = "TAG_PARAM_USUARIOS_LABORATORIO";
    public final static String CMD_EDITAR_EQUIPO_LABORATORIO = "CMD_EDITAR_EQUIPO_LABORATORIO";
    public final static String TAG_PARAM_PRESTAMO = "TAG_PARAM_PRESTAMO";
    public final static String TAG_PARAM_PRESTAMOS = "TAG_PARAM_PRESTAMOS";
    public final static String TAG_PARAM_PERMISOS_ESPECIALES = "TAG_PARAM_PERMISOS_ESPECIALES";
    public final static String VAL_URL_SERVICIO_ADMONSIS = "VAL_URL_SERVICIO_ADMONSIS";
    public final static String VAL_METODO_HTTP_SERVICIO_ADMONSIS = "VAL_METODO_HTTP_SERVICIO_ADMONSIS";
    public final static String VAL_METODO_HTTP_GET = "VAL_METODO_HTTP_GET";
    public final static String VAL_METODO_HTTP_POST = "VAL_METODO_HTTP_POST";
    public final static String TAG_PARAM_ACTIVO = "TAG_PARAM_ACTIVO";
    public final static String CMD_ACTIVAR_LABORATORIO = "CMD_ACTIVAR_LABORATORIO";
    public final static String CMD_CONSULTAR_LABORATORIOS_ADMINISTRADOR = "CMD_CONSULTAR_LABORATORIOS_ADMINISTRADOR";
    public final static String VAL_RESERVA_MULTIPLE_PERIODICIDAD_DIARIA_L_V = "VAL_RESERVA_MULTIPLE_PERIODICIDAD_DIARIA_L_V";
    public final static String VAL_RESERVA_MULTIPLE_PERIODICIDAD_DIARIA_L_S = "VAL_RESERVA_MULTIPLE_PERIODICIDAD_DIARIA_L_S";
    public final static String VAL_RESERVA_MULTIPLE_PERIODICIDAD_SEMANAL = "VAL_RESERVA_MULTIPLE_PERIODICIDAD_SEMANAL";
    public final static String VAL_RESERVA_MULTIPLE_PERIODICIDAD_MENSUAL = "VAL_RESERVA_MULTIPLE_PERIODICIDAD_MENSUAL";
    public final static String TAG_PARAM_RESERVAMULTIPLE_ISMULTIPLE = "TAG_PARAM_RESERVAMULTIPLE_ISMULTIPLE";
    public final static String TAG_PARAM_RESERVAMULTIPLE_ID = "TAG_PARAM_RESERVAMULTIPLE_ID";
    public final static String TAG_PARAM_RESERVAMULTIPLE_PERIODICIDAD = "TAG_PARAM_RESERVAMULTIPLE_PERIODICIDAD";
    public final static String TAG_PARAM_RESERVAMULTIPLE_INICIORESERVA = "TAG_PARAM_RESERVAMULTIPLE_INICIORESERVA";
    public final static String TAG_PARAM_RESERVAMULTIPLE_FINALRESERVA = "TAG_PARAM_RESERVAMULTIPLE_FINALRESERVA";
    public final static String TAG_PARAM_RESERVAMULTIPLE = "TAG_PARAM_RESERVAMULTIPLE";
    /*
     * Conflicto de horarios
     */
    public final static String RUTA_ARCHIVO_CARTELERA_CONFLICTO_HORARIOS = "RUTA_ARCHIVO_CARTELERA_CONFLICTO_HORARIOS";
    public final static String RUTA_ARCHIVO_PROGRAMAS_CARTELERA_CONFLICTO_HORARIOS = "RUTA_ARCHIVO_PROGRAMAS_CARTELERA_CONFLICTO_HORARIOS";
    public final static String CMD_CARGAR_CARTELERA_CONFLICTO_HORARIOS = "CMD_CARGAR_CARTELERA_CONFLICTO_HORARIOS";
    public final static String CMD_CONSULTAR_FECHAS_CONFLICTO_HORARIO = "CMD_CONSULTAR_FECHAS_CONFLICTO_HORARIO";
    public final static String CMD_GUARDAR_FECHAS_CONFLICTO_HORARIO = "CMD_GUARDAR_FECHAS_CONFLICTO_HORARIO";
    public final static String CMD_CAMBIAR_ESTADO_SECCION_CONFLICTO_HORARIO = "CMD_CAMBIAR_ESTADO_SECCION_CONFLICTO_HORARIO";
    public final static String CMD_CREAR_CURSO_CONFLICTO_HORARIO = "CMD_CREAR_CURSO_CONFLICTO_HORARIO";
    public final static String CMD_CREAR_SECCION_CONFLICTO_HORARIO = "CMD_CREAR_SECCION_CONFLICTO_HORARIO";
    public final static String CMD_GUARDAR_ESTADO_PETICIONES_CONFLICTO_HORARIO = "CMD_GUARDAR_ESTADO_PETICIONES_CONFLICTO_HORARIO";
    public final static String CMD_CANCELAR_PETICION_CONFLICTO_HORARIO_ESTUDIANTE = "CMD_CANCELAR_PETICION_CONFLICTO_HORARIO_ESTUDIANTE";
    public final static String CMD_CONSULTAR_PETICIONES_CONFLICTO_HORARIO_ESTUDIANTE = "CMD_CONSULTAR_PETICIONES_CONFLICTO_HORARIO_ESTUDIANTE";
    public final static String CMD_CONSULTAR_PROGRAMAS_CONFLICTO_HORARIO = "CMD_CONSULTAR_PROGRAMAS_CONFLICTO_HORARIO";
    public final static String CMD_CREAR_PETICION_CONFLICTO_HORARIO = "CMD_CREAR_PETICION_CONFLICTO_HORARIO";
    public final static String CMD_CONSULTAR_MATERIAS_PROGRAMA = "CMD_CONSULTAR_MATERIAS_PROGRAMA";
    public final static String CMD_CONSULTAR_SECCIONES_MATERIA = "CMD_CONSULTAR_SECCIONES_MATERIA";
    public final static String CMD_CONSULTAR_SECCION = "CMD_CONSULTAR_SECCION";
    public final static String TAG_FECHA_CREACION_CONFLICTO_HORARIO = "TAG_FECHA_CREACION_CONFLICTO_HORARIO";
    public final static String TAG_FECHA_RESOLUCION_CONFLICTO_HORARIO = "TAG_FECHA_RESOLUCION_CONFLICTO_HORARIO";
    public final static String TAG_PETICIONES_CONFLICTO_HORARIO = "TAG_PETICIONES_CONFLICTO_HORARIO";
    public final static String TAG_PROGRAMAS_CONFLICTO_HORARIO = "TAG_PROGRAMAS_CONFLICTO_HORARIO";
    public final static String TAG_CODIGO_PROGRAMA_CONFLICTO_HORARIO = "TAG_CODIGO_PROGRAMA_CONFLICTO_HORARIO";
    public final static String TAG_PROFESORES_CONFLICTO_HORARIO = "TAG_PROFESORES_CONFLICTO_HORARIO";
    public final static String TAG_INICIO_FECHAS_CONFLICTO_HORARIO = "TAG_INICIO_FECHAS_CONFLICTO_HORARIO";
    public final static String TAG_FIN_FECHAS_CONFLICTO_HORARIO = "TAG_FIN_FECHAS_CONFLICTO_HORARIO";
    public final static String TAG_PROFESOR_CONFLICTO_HORARIO = "TAG_PROFESOR_CONFLICTO_HORARIO";
    public final static String TAG_SECCIONES_CONFLICTO_HORARIO = "TAG_SECCIONES_CONFLICTO_HORARIO";
    public final static String TAG_HORARIO_SECCION_CONFLICTO_HORARIO = "TAG_HORARIO_SECCION_CONFLICTO_HORARIO";
    public final static String TAG_HORARIOS_SECCION_CONFLICTO_HORARIO = "TAG_HORARIOS_SECCION_CONFLICTO_HORARIO";
    public final static String TAG_DIAS_SECCION_CONFLICTO_HORARIO = "TAG_DIAS_SECCION_CONFLICTO_HORARIO";
    public final static String TAG_INICIO_SECCION_CONFLICTO_HORARIO = "TAG_INICIO_SECCION_CONFLICTO_HORARIO";
    public final static String TAG_FIN_SECCION_CONFLICTO_HORARIO = "TAG_FIN_SECCION_CONFLICTO_HORARIO";
    public final static String TAG_SALON_SECCION_CONFLICTO_HORARIO = "TAG_SALON_SECCION_CONFLICTO_HORARIO";
    public final static String TAG_RELACIONADOS_SECCION_CONFLICTO_HORARIO = "TAG_RELACIONADOS_SECCION_CONFLICTO_HORARIO";
    public final static String TAG_PETICION_CONFLICTO_HORARIO = "TAG_PETICION_CONFLICTO_HORARIO";
    public final static String TAG_TIPO_PETICION_CONFLICTO_HORARIO = "TAG_TIPO_PETICION_CONFLICTO_HORARIO";
    public final static String TAG_MATERIAS_CONFLICTO_HORARIO = "TAG_MATERIAS_CONFLICTO_HORARIO";
    public final static String TAG_MATERIA_CONFLICTO_HORARIO = "TAG_MATERIA_CONFLICTO_HORARIO";
    public final static String TAG_CREDITOS_MATERIA_CONFLICTO_HORARIO = "TAG_CREDITOS_MATERIA_CONFLICTO_HORARIO";
    public final static String TAG_NOMBRE_MATERIA_CONFLICTO_HORARIO = "TAG_NOMBRE_MATERIA_CONFLICTO_HORARIO";
    public final static String TAG_CODIGO_MATERIA_CONFLICTO_HORARIO = "TAG_CODIGO_MATERIA_CONFLICTO_HORARIO";
    public final static String TAG_MATERIA_ORIGEN_CONFLICTO_HORARIO = "TAG_MATERIA_ORIGEN_CONFLICTO_HORARIO";
    public final static String TAG_MATERIA_DESTINO_CONFLICTO_HORARIO = "TAG_MATERIA_DESTINO_CONFLICTO_HORARIO";
    public final static String TAG_CURSO_ORIGEN_CONFLICTO_HORARIO = "TAG_CURSO_ORIGEN_CONFLICTO_HORARIO";
    public final static String TAG_CURSO_DESTINO_CONFLICTO_HORARIO = "TAG_CURSO_DESTINO_CONFLICTO_HORARIO";
    public final static String TAG_SECCION_ORIGEN_CONFLICTO_HORARIO = "TAG_SECCION_ORIGEN_CONFLICTO_HORARIO";
    public final static String TAG_SECCION_DESTINO_CONFLICTO_HORARIO = "TAG_SECCION_DESTINO_CONFLICTO_HORARIO";
    public final static String TAG_PROGRAMA_CONFLICTO_HORARIO = "TAG_PROGRAMA_CONFLICTO_HORARIO";
    public final static String TAG_PROGRAMA_ESTUDIANTE_CONFLICTO_HORARIO = "TAG_PROGRAMA_ESTUDIANTE_CONFLICTO_HORARIO";
    public final static String TAG_NOMBRE_PROGRAMA_CONFLICTO_HORARIO = "TAG_NOMBRE_PROGRAMA_CONFLICTO_HORARIO";
    public final static String TAG_COMENTARIOS_CONFLICTO_HORARIO = "TAG_COMENTARIOS_CONFLICTO_HORARIO";
    public final static String TAG_COMENTARIOS_RESOLUCION_CONFLICTO_HORARIO = "TAG_COMENTARIOS_RESOLUCION_CONFLICTO_HORARIO";
    public final static String TAG_ID_CONFLICTO_HORARIO = "TAG_ID_CONFLICTO_HORARIO";
    public final static String TAG_SECCION_CONFLICTO_HORARIO = "TAG_SECCION_CONFLICTO_HORARIO";
    public final static String TAG_ESTADO_SECCION_CONFLICTO_HORARIO = "TAG_ESTADO_SECCION_CONFLICTO_HORARIO";
    public final static String TAG_ESTADO_ACTIVO_SECCION_CONFLICTO_HORARIO = "TAG_ESTADO_ACTIVO_SECCION_CONFLICTO_HORARIO";
    public final static String TAG_CRN_SECCION_CONFLICTO_HORARIO = "TAG_CRN_SECCION_CONFLICTO_HORARIO";
    public final static String TAG_NUMERO_SECCION_CONFLICTO_HORARIO = "TAG_NUMERO_SECCION_CONFLICTO_HORARIO";
    public final static String CMD_CONSULTAR_PETICIONES_MATERIAS_CONFLICTO_HORARIO = "CMD_CONSULTAR_PETICIONES_MATERIAS_CONFLICTO_HORARIO";
    public final static String CMD_EDITAR_DATOS_SECCION_CONFLICTO_HORARIO = "CMD_EDITAR_DATOS_SECCION_CONFLICTO_HORARIO";
    public final static String CMD_ELIMINAR_SECCION_CONFLICTO_HORARIO = "CMD_ELIMINAR_SECCION_CONFLICTO_HORARIO";
    public final static String CMD_CONSULTAR_PETICIONES_POR_MATERIA_CONFLICTO_HORARIO = "CMD_CONSULTAR_PETICIONES_POR_MATERIA_CONFLICTO_HORARIO";
    public final static String TAG_ADICION_CONFLICTO_HORARIO = "TAG_ADICION_CONFLICTO_HORARIO";
    public final static String TAG_RETIRO_CONFLICTO_HORARIO = "TAG_RETIRO_CONFLICTO_HORARIO";
    public final static String TAG_CAMBIO_CONFLICTO_HORARIO = "TAG_CAMBIO_CONFLICTO_HORARIO";
    public final static String VAL_ADICION_CONFLICTO_HORARIO = "VAL_ADICION_CONFLICTO_HORARIO";
    public final static String VAL_RETIRO_CONFLICTO_HORARIO = "VAL_RETIRO_CONFLICTO_HORARIO";
    public final static String VAL_CAMBIO_CONFLICTO_HORARIO = "VAL_CAMBIO_CONFLICTO_HORARIO";
    public final static String VAL_CONFLICTO_HORARIO_ESTADO_PENDIENTE = "VAL_CONFLICTO_HORARIO_ESTADO_PENDIENTE";
    public final static String VAL_CONFLICTO_HORARIO_ESTADO_CANCELADO = "VAL_CONFLICTO_HORARIO_ESTADO_CANCELADO";
    public final static String VAL_CONFLICTO_HORARIO_ESTADO_EXITOSO = "VAL_CONFLICTO_HORARIO_ESTADO_EXITOSO";
    public final static String VAL_CONFLICTO_HORARIO_ESTADO_NO_EXITOSO_PRERREQUISITO = "VAL_CONFLICTO_HORARIO_ESTADO_NO_EXITOSO_PRERREQUISITO";
    public final static String VAL_CONFLICTO_HORARIO_ESTADO_NO_EXITOSO_DIRIJIRSE_DEPT = "VAL_CONFLICTO_HORARIO_ESTADO_NO_EXITOSO_DIRIJIRSE_DEPT";
    public final static String VAL_CONFLICTO_HORARIO_ESTADO_NO_EXITOSO_SECCION_LLENA = "VAL_CONFLICTO_HORARIO_ESTADO_NO_EXITOSO_SECCION_LLENA";
    public final static String VAL_CONFLICTO_HORARIO_ESTADO_NO_EXITOSO_INSCRITA = "VAL_CONFLICTO_HORARIO_ESTADO_NO_EXITOSO_INSCRITA";
    public final static String VAL_CONFLICTO_HORARIO_ESTADO_NO_EXITOSO_ERROR_FORMATO = "VAL_CONFLICTO_HORARIO_ESTADO_NO_EXITOSO_ERROR_FORMATO";
    public final static String VAL_CONFLICTO_HORARIO_ESTADO_NO_EXITOSO_CONFLICTO_HORARIO = "VAL_CONFLICTO_HORARIO_ESTADO_NO_EXITOSO_CONFLICTO_HORARIO";
    public final static String VAL_CONFLICTO_HORARIO_ESTADO_EXITOSO_OTRA_SECCION = "VAL_CONFLICTO_HORARIO_ESTADO_EXITOSO_OTRA_SECCION";
    public final static String VAL_CONFLICTO_HORARIO_ESTADO_NO_EXITOSO_PROMEDIO_EXTRACREDITO = "VAL_CONFLICTO_HORARIO_ESTADO_NO_EXITOSO_PROMEDIO_EXTRACREDITO";
    public final static String VAL_CONFLICTO_HORARIO_ESTADO_NO_EXITOSO_DIRIJIRSE_COORDINACION = "VAL_CONFLICTO_HORARIO_ESTADO_NO_EXITOSO_DIRIJIRSE_COORDINACION";
    public final static String VAL_CONFLICTO_HORARIO_ESTADO_NO_EXITOSO_CORREQUISITO = "VAL_CONFLICTO_HORARIO_ESTADO_NO_EXITOSO_CORREQUISITO";
    public final static String VAL_CODIGO_SISTEMAS = "VAL_CODIGO_SISTEMAS";
    public final static String TAG_INICIO = "TAG_INICIO";
    public final static String TAG_CANTIDAD = "TAG_CANTIDAD";
    public final static String TAG_PARAM_CURSO_CREDITOS = "TAG_PARAM_CURSO_CREDITOS";
    public final static String TAG_PARAM_NIVEL_PROGRAMA = "TAG_PARAM_NIVEL_PROGRAMA";
    public final static String TAG_PARAM_MAXIMO_MONITORES = "TAG_PARAM_MAXIMO_MONITORES";
    public final static String TAG_PARAM_PETICIONES = "TAG_PARAM_PETICIONES";
    public final static String TAG_PARAM_PETICION_COMENTARIOS = "TAG_PARAM_PETICION_COMENTARIOS";
    public final static String TAG_PARAM_PETICION_COMENTARIOS_RESOLUCION = "TAG_PARAM_PETICION_COMENTARIOS_RESOLUCION";
    public final static String TAG_PARAM_PETICION_TIPO = "TAG_PARAM_PETICION_TIPO";
    public final static String TAG_PARAM_FECHA_RESOLUCION = "TAG_PARAM_FECHA_RESOLUCION";
    public final static String TAG_PARAM_PETICION_CURSO_ORIGEN = "TAG_PARAM_PETICION_CURSO_ORIGEN";
    public final static String TAG_PARAM_PETICION_CURSO_DESTINO = "TAG_PARAM_PETICION_CURSO_DESTINO";
    public final static String TAG_PARAM_PETICION_SECCION_ORIGEN = "TAG_PARAM_PETICION_SECCION_ORIGEN";
    public final static String TAG_PARAM_PETICION_SECCION_DESTINO = "TAG_PARAM_PETICION_SECCION_DESTINO";
    public final static String TAG_PARAM_CURSO_TOTAL_PETICIONES = "TAG_PARAM_CURSO_TOTAL_PETICIONES";
    public final static String TAG_PARAM_CURSO_TOTAL_ADICIONES = "TAG_PARAM_CURSO_TOTAL_ADICIONES";
    public final static String TAG_PARAM_CURSO_TOTAL_RETIRO = "TAG_PARAM_CURSO_TOTAL_RETIRO";
    public final static String TAG_PARAM_CURSO_TOTAL_CAMBIO = "TAG_PARAM_CURSO_TOTAL_CAMBIO";
    public final static String TAG_PARAM_CURSO_PRESENCIAL = "TAG_PARAM_CURSO_PRESENCIAL";
    public final static String TAG_PARAM_CURSOS_RELACIONADOS = "TAG_PARAM_CURSOS_RELACIONADOS";
    public final static String TAG_PARAM_SECCIONES_RELACIONADAS = "TAG_PARAM_SECCIONES_RELACIONADAS";
    public final static String TAG_PARAM_PROFESOR_PRINCIPAL = "TAG_PARAM_PROFESOR_PRINCIPAL";
    public final static String TAG_PARAM_SESIONES = "TAG_PARAM_SESIONES";
    public final static String TAG_PARAM_SESION = "TAG_PARAM_SESION";
    public final static String TAG_PARAM_SALON = "TAG_PARAM_SALON";
    public final static String TAG_PARAM_DIA_LUNES = "TAG_PARAM_DIA_LUNES";
    public final static String TAG_PARAM_DIA_MARTES = "TAG_PARAM_DIA_MARTES";
    public final static String TAG_PARAM_DIA_MIERCOLES = "TAG_PARAM_DIA_MIERCOLES";
    public final static String TAG_PARAM_DIA_JUEVES = "TAG_PARAM_DIA_JUEVES";
    public final static String TAG_PARAM_DIA_VIERNES = "TAG_PARAM_DIA_VIERNES";
    public final static String CMD_CONSULTAR_PETICIONES_RESUELTAS_CONFLICTO_HORARIO = "CMD_CONSULTAR_PETICIONES_RESUELTAS_CONFLICTO_HORARIO";
    public final static String TAG_PARAM_CREDITOS_CURSO = "TAG_PARAM_CREDITOS_CURSO";
    public final static String TAG_PARAM_NIVEL_CURSO = "TAG_PARAM_NIVEL_CURSO";
    public final static String TAG_PARAM_CURSO_REQUERIDA = "TAG_PARAM_CURSO_REQUERIDA";
    /**
     * constantes Proyecto de Grado
     */
    public final static String CMD_REPROBAR_PROYECTO_GRADO = "CMD_REPROBAR_PROYECTO_GRADO";
    public final static String CMD_AGREGAR_SOLICITUD_PROYECTO_DE_GRADO = "CMD_AGREGAR_SOLICITUD_PROYECTO_DE_GRADO";
    public final static String CMD_AGREGAR_SOLICITUD_PROYECTO_DE_GRADO_COORDINACION = "CMD_AGREGAR_SOLICITUD_PROYECTO_DE_GRADO_COORDINACION";
    public final static String CMD_APROBAR_SOLICITUD_TESIS_PROYECTO_DE_GRADO = "CMD_APROBAR_SOLICITUD_TESIS_PROYECTO_DE_GRADO";
    public final static String CMD_AGREGAR_MODIFICAR_TEMA_TESIS_PREGRADO = "CMD_AGREGAR_MODIFICAR_TEMA_TESIS_PREGRADO";
    public final static String CMD_ELIMINAR_TEMA_TESIS_PREGRADO = "CMD_ELIMINAR_TEMA_TESIS_PREGRADO";
    public final static String CMD_DAR_TEMAS_TESIS_PREGRADO_POR_ASESOR = "CMD_DAR_TEMAS_TESIS_PREGRADO_POR_ASESOR";
    public final static String CMD_DAR_TEMA_TESIS_PREGRADO_POR_ID = "CMD_DAR_TEMA_TESIS_PREGRADO_POR_ID";
    public final static String CMD_PROYCTOS_DE_GRADO_POR_ASESOR = "CMD_PROYCTOS_DE_GRADO_POR_ASESOR";
    public final static String CMD_PROYECTOS_DE_GRADO_POR_COORDINADOR = "CMD_PROYECTOS_DE_GRADO_POR_COORDINADOR";
    public final static String CMD_DAR_TODOS_LOS_PROYECTOS_PREGRADO_A_MIGRAR = "CMD_DAR_TODOS_LOS_PROYECTOS_PREGRADO_A_MIGRAR";
    public final static String CMD_PROYCTOS_DE_GRADO_POR_ESTUDIANTE = "CMD_PROYCTOS_DE_GRADO_POR_ESTUDIANTE";
    public final static String TAREA_INSCRIBIR_PROYECTO_BANNER_POR_COODINACION = "TAREA_INSCRIBIR_PROYECTO_BANNER_POR_COODINACION";
    public final static String TAG_PARAM_TIPO_APROBAR_INSCRIPCION_TESIS_PROYECTO_DE_GRADO_ASESOR = "TAG_PARAM_TIPO_APROBAR_INSCRIPCION_TESIS_PROYECTO_DE_GRADO_ASESOR";
    public final static String TAG_PARAM_TIPO_APROBAR_INSCRIPCION_TESIS_PROYECTO_DE_GRADO_COORDINACION = "TAG_PARAM_TIPO_APROBAR_INSCRIPCION_TESIS_PROYECTO_DE_GRADO_COORDINACION";
    public final static String TAG_PARAM_TIPO_SUBIR_AFICHE = "TAG_PARAM_TIPO_SUBIR_AFICHE";
    public final static String TAG_PARAM_TIPO_SUBIR_AFICHE_PENDIENTE = "TAG_PARAM_TIPO_SUBIR_AFICHE_PENDIENTE";
    public final static String TAG_PARAM_TIPO_SUBIR_AFICHE_PENDIENTE_ESPECIAL = "TAG_PARAM_TIPO_SUBIR_AFICHE_PENDIENTE_ESPECIAL";
    public final static String TAG_PARAM_TIPO_APROBAR_AFICHE_PROYECTO_DE_GRADO_ASESOR = "TAG_PARAM_TIPO_APROBAR_AFICHE_PROYECTO_DE_GRADO_ASESOR";
    public final static String TAG_PARAM_TIPO_APROBAR_AFICHE_PROYECTO_DE_GRADO_ASESOR_PENDIENTE = "TAG_PARAM_TIPO_APROBAR_AFICHE_PROYECTO_DE_GRADO_ASESOR_PENDIENTE";
    public final static String TAG_PARAM_TIPO_APROBAR_AFICHE_PROYECTO_DE_GRADO_ASESOR_PENDIENTE_ESPECIAL = "TAG_PARAM_TIPO_APROBAR_AFICHE_PROYECTO_DE_GRADO_ASESOR_PENDIENTE_ESPECIAL";
    public final static String CMD_APROBAR_RECHAZAR_AFICHE_ESTUDIANTE = "CMD_APROBAR_RECHAZAR_AFICHE_ESTUDIANTE";
    public final static String CMD_APROBAR_RECHAZAR_AFICHE_ESTUDIANTE_PENDIENTE = "CMD_APROBAR_RECHAZAR_AFICHE_ESTUDIANTE_PENDIENTE";
    public final static String CMD_APROBAR_RECHAZAR_AFICHE_ESTUDIANTE_PENDIENTE_ESPECIAL = "CMD_APROBAR_RECHAZAR_AFICHE_ESTUDIANTE_PENDIENTE_ESPECIAL";
    public final static String CMD_PROYCTOS_DE_GRADO_POR_ESTADO = "CMD_PROYCTOS_DE_GRADO_POR_ESTADO";
    public final static String TAG_PARAM_CATEGORIA_PROYECTO_DE_GRADO = "TAG_PARAM_CATEGORIA_PROYECTO_DE_GRADO";
    public final static String TAG_PARAM_PROYECTOS_DE_GRADO = "TAG_PARAM_PROYECTOS_DE_GRADO";
    public final static String TAG_PARAM_PROYECTO_DE_GRADO = "TAG_PARAM_PROYECTO_DE_GRADO";
    public final static String TAG_PARAM_RUTA_POSTER = "TAG_PARAM_RUTA_POSTER";
    public final static String TAG_PARAM_NOMBRE_ARCHIVO_POSTER = "TAG_PARAM_NOMBRE_ARCHIVO_POSTER";
    public final static String TAG_PARAM_NOMBRE_ARCHIVO_PENDIENTE_ESPECIAL = "TAG_PARAM_NOMBRE_ARCHIVO_PENDIENTE_ESPECIAL";
    public final static String TAG_PARAM_RUTA_ABET = "TAG_PARAM_RUTA_ABET";
    public final static String CMD_DAR_CONFIGURACION_PERIODO_TESIS_PREGRADO_POR_NOMBRE = "CMD_DAR_CONFIGURACION_PERIODO_TESIS_PREGRADO_POR_NOMBRE";
    public final static String TAG_PARAM_FECHA_INSCRIPCION_PROYECTO_DE_GRADO = "TAG_PARAM_FECHA_INSCRIPCION_PROYECTO_DE_GRADO";
    public final static String TAG_PARAM_FECHA_ASESOR_ACEPTAR_TESIS_PREGRADO = "TAG_PARAM_FECHA_ASESOR_ACEPTAR_TESIS_PREGRADO";
    public final static String TAG_PARAM_FECHA_ESTUDIANTE_ENVIAR_PROPUESTA_TESIS_PREGRADO = "TAG_PARAM_FECHA_ESTUDIANTE_ENVIAR_PROPUESTA_TESIS_PREGRADO";
    public final static String TAG_PARAM_FECHA_ASESOR_VALIDACION_PROPUESTA_PROYECTO = "TAG_PARAM_FECHA_ASESOR_VALIDACION_PROPUESTA_PROYECTO";
    public final static String TAG_PARAM_FECHA_ASESOR_APRECIACION_CUALITATIVA_PROYECTO = "TAG_PARAM_FECHA_ASESOR_APRECIACION_CUALITATIVA_PROYECTO";
    public final static String TAG_PARAM_FECHA_ESTUDIANTE_ENTREGA_POSTER = "TAG_PARAM_FECHA_ESTUDIANTE_ENTREGA_POSTER";
    public final static String TAG_PARAM_FECHA_ASESOR_ACEPTAR_POSTER = "TAG_PARAM_FECHA_ASESOR_ACEPTAR_POSTER";
    public final static String TAG_PARAM_FECHA_ESTUDIANTE_ENTREGA_POSTER_PENDIENTE_ESPECIAL = "TAG_PARAM_FECHA_ESTUDIANTE_ENTREGA_POSTER_PENDIENTE_ESPECIAL";
    public final static String TAG_PARAM_FECHA_ASESOR_ACEPTAR_POSTER_PENDIENTE_ESPECIAL = "TAG_PARAM_FECHA_ASESOR_ACEPTAR_POSTER_PENDIENTE_ESPECIAL";
    public final static String TAG_PARAM_FECHA_ESTUDIANTE_ENTREGA_POSTER_PENDIENTE = "TAG_PARAM_FECHA_ESTUDIANTE_ENTREGA_POSTER_PENDIENTE";
    public final static String TAG_PARAM_FECHA_ASESOR_ACEPTAR_POSTER_PENDIENTE = "TAG_PARAM_FECHA_ASESOR_ACEPTAR_POSTER_PENDIENTE";
    public final static String TAG_PARAM_FECHA_ASESOR_LLENAR_ABET = "TAG_PARAM_FECHA_ASESOR_LLENAR_ABET";
    public final static String TAG_PARAM_FECHA_ASESOR_ENVIO_NOTAS = "TAG_PARAM_FECHA_ASESOR_ENVIO_NOTAS";
    public final static String TAG_PARAM_FECHA_ASESOR_PEDIR_PENDIENTE_ESPECIAL = "TAG_PARAM_FECHA_ASESOR_PEDIR_PENDIENTE_ESPECIAL";
    public final static String TAG_PARAM_FECHA_ASESOR_LEVANTAR_PEN_ESPECIAL_PREGRADO = "TAG_PARAM_FECHA_ASESOR_LEVANTAR_PEN_ESPECIAL_PREGRADO";
    public final static String TAG_PARAM_FECHA_ESTUDIANTE_NOTIFICA_RETIRO = "TAG_PARAM_FECHA_ESTUDIANTE_NOTIFICA_RETIRO";
    public final static String TAG_PARAM_FECHA_ASESOR_PEDIR_PENDIENTE = "TAG_PARAM_FECHA_ASESOR_PEDIR_PENDIENTE";
    public final static String TAG_PARAM_FECHA_ASESOR_LEVANTAR_PENDIENTE = "TAG_PARAM_FECHA_ASESOR_LEVANTAR_PENDIENTE";
    public final static String TAG_PARAM_TESIS_PREGRADO_PERDIDA = "TAG_PARAM_TESIS_PREGRADO_PERDIDA";
    public final static String TAG_PARAM_TESIS_PREGRADO_RECHAZADA = "TAG_PARAM_TESIS_PREGRADO_RECHAZADA";
    public final static String TAG_PARAM_TESIS_PREGRADO_RETIRADA = "TAG_PARAM_TESIS_PREGRADO_RETIRADA";
    public final static String TAG_PARAM_TESIS_PREGRADO_TERMINADA = "TAG_PARAM_TESIS_PREGRADO_TERMINADA";
    public final static String CMD_ENVIAR_CORREO_ULTIMO_DIA_SUBIR_TEMAS_TESIS_PREGRADO = "CMD_ENVIAR_CORREO_ULTIMO_DIA_SUBIR_TEMAS_TESIS_PREGRADO";
    public final static String CMD_MIGRAR_TESIS_PREGRADO_RECHAZADAS_A_HISTORICOS = "CMD_MIGRAR_TESIS_PREGRADO_RECHAZADAS_A_HISTORICOS";
    public final static String CMD_MIGRAR_TESIS_PREGRADO_RETIRADAS_A_HISTORICOS = "CMD_MIGRAR_TESIS_PREGRADO_RETIRADAS_A_HISTORICOS";
    public final static String CMD_MIGRAR_TESIS_PREGRADO_PERDIDAS_A_HISTORICOS = "CMD_MIGRAR_TESIS_PREGRADO_PERDIDAS_A_HISTORICOS";
    public final static String CMD_MIGRAR_TESIS_PREGRADO_TERMINADAS_A_HISTORICOS = "CMD_MIGRAR_TESIS_PREGRADO_TERMINADAS_A_HISTORICOS";
    public final static String CMD_MIGRAR_TESIS1_RECHAZADAS_A_HISTORICOS = "CMD_MIGRAR_TESIS1_RECHAZADAS_A_HISTORICOS";
    public final static String CMD_MIGRAR_TESIS1_RETIRADAS_A_HISTORICOS = "CMD_MIGRAR_TESIS1_RETIRADAS_A_HISTORICOS";
    public final static String CMD_MIGRAR_TESIS1_PERDIDAS_A_HISTORICOS = "CMD_MIGRAR_TESIS1_PERDIDAS_A_HISTORICOS";
    public final static String CMD_MIGRAR_TESIS1_TERMINADAS_A_HISTORICOS = "CMD_MIGRAR_TESIS1_TERMINADAS_A_HISTORICOS";
    public final static String CMD_MIGRAR_TESIS2_RECHAZADAS_A_HISTORICOS = "CMD_MIGRAR_TESIS2_RECHAZADAS_A_HISTORICOS";
    public final static String CMD_MIGRAR_TESIS2_RETIRADAS_A_HISTORICOS = "CMD_MIGRAR_TESIS2_RETIRADAS_A_HISTORICOS";
    public final static String CMD_MIGRAR_TESIS2_PERDIDAS_A_HISTORICOS = "CMD_MIGRAR_TESIS2_PERDIDAS_A_HISTORICOS";
    public final static String CMD_MIGRAR_TESIS2_TERMINADAS_A_HISTORICOS = "CMD_MIGRAR_TESIS2_TERMINADAS_A_HISTORICOS";
    public final static String CMD_MIGRAR_INSCRIPCION_SUBAREA_A_HISTORICOS = "CMD_MIGRAR_INSCRIPCION_SUBAREA_A_HISTORICOS";
    public final static String VAL_DIAS_REPROBAR_TESIS_2_AUTOMATICAMENTE = "VAL_DIAS_REPROBAR_TESIS_2_AUTOMATICAMENTE";
    public final static String CMD_DAR_PERIODOS_CONFIGURADOS_TESIS_PREGRADO = "CMD_DAR_PERIODOS_CONFIGURADOS_TESIS_PREGRADO";
    public final static String CMD_DAR_CATEGORIAS_TESIS_PREGRADO = "CMD_DAR_CATEGORIAS_TESIS_PREGRADO";
    public final static String TAG_PARAM_CATEGORIAS_PROYECTO_DE_GRADO = "TAG_PARAM_CATEGORIAS_PROYECTO_DE_GRADO";
    public final static String CMD_DAR_TEMAS_TESIS_PREGRADO_TODOS = "CMD_DAR_TEMAS_TESIS_PREGRADO_TODOS";
    public final static String CMD_EDITAR_FECHAS_PERIODO_PREGRADO = "CMD_EDITAR_FECHAS_PERIODO_PREGRADO";
    public final static String CMD_RECHAZAR_TESIS_PENDIENTES_PERIODO = "CMD_RECHAZAR_TESIS_PENDIENTES_PERIODO";
    public final static String TAG_PARAM_TIPO_APROBAR_PROPUESTA_TESIS_PROYECTO_DE_GRADO_ASESOR = "TAG_PARAM_TIPO_APROBAR_PROPUESTA_TESIS_PROYECTO_DE_GRADO_ASESOR";
    public final static String CMD_AGREGAR_PROPUESTA_PROYECTO_DE_GRADO = "CMD_AGREGAR_PROPUESTA_PROYECTO_DE_GRADO";
    public final static String RUTA_ARCHIVOS_TESIS_PREGRADO = "RUTA_ARCHIVOS_TESIS_PREGRADO";
    public final static String RUTA_AFICHES_TESIS_PREGRADO = "RUTA_AFICHES_TESIS_PREGRADO";
    public final static String RUTA_PENDIENTES_ESPECIALES_TESIS_PREGRADO = "RUTA_PENDIENTES_ESPECIALES_TESIS_PREGRADO";
    public final static String TAG_PARAM_APROBADA_PROPUESTA_ASESOR = "TAG_PARAM_APROBADA_PROPUESTA_ASESOR";
    public final static String TAG_PARAM_TIPO_ENVIAR_PROPUESTA_TESIS_PROYECTO_DE_GRADO_ASESOR = "TAG_PARAM_TIPO_ENVIAR_PROPUESTA_TESIS_PROYECTO_DE_GRADO_ASESOR";
    public final static String TAG_PARAM_TIPO_INSCRIBIR_BANNER = "TAG_PARAM_TIPO_INSCRIBIR_BANNER";
    public final static String CMD_ACEPTAR_PROPUESTA_PROYECTO_DE_GRADO = "CMD_ACEPTAR_PROPUESTA_PROYECTO_DE_GRADO";
    public final static String CMD_DAR_PROYECTO_DE_GRADO_POR_ID = "CMD_DAR_PROYECTO_DE_GRADO_POR_ID";
    public final static String TAG_PARAM_NOMBRE_ARCHIVO_PROPUESTA_TESIS_PREGRADO = "TAG_PARAM_NOMBRE_ARCHIVO_PROPUESTA_TESIS_PREGRADO";
    public final static String TAG_PARAM_NOMBRE_ARCHIVO_CARTA_PENDIENTE_TESIS1 = "TAG_PARAM_NOMBRE_ARCHIVO_CARTA_PENDIENTE_TESIS1";
    public final static String CMD_AGREGAR_COMENTARIO_PROYECTO_GRADO = "CMD_AGREGAR_COMENTARIO_PROYECTO_GRADO";
    public final static String TAG_PARAM_COMENTARIO_PROYECTO_GRADO = "TAG_PARAM_COMENTARIO_PROYECTO_GRADO";
    public final static String CMD_DAR_COMENTARIO_PROYECTO_GRADO = "CMD_DAR_COMENTARIO_PROYECTO_GRADO";
    public final static String TAG_PARAM_COMENTARIOS_PROYECTO_GRADO = "TAG_PARAM_COMENTARIOS_PROYECTO_GRADO";
    public final static String RUTA_ARCHIVO_ABET = "RUTA_ARCHIVO_ABET";
    public final static String CMD_SUBIR_ARCHIVO_ABET = "CMD_SUBIR_ARCHIVO_ABET";
    public final static String CMD_SUBIR_ARCHIVO_ABET_PENDIENTE_ESPECIAL = "CMD_SUBIR_ARCHIVO_ABET_PENDIENTE_ESPECIAL";
    public final static String CMD_SUBIR_ARCHIVO_ABET_PENDIENTE = "CMD_SUBIR_ARCHIVO_ABET_PENDIENTE";
    public final static String TAG_PARAM_RUTA_ARCHIVO_POSTER = "TAG_PARAM_RUTA_ARCHIVO_POSTER";
    public final static String TAG_PARAM_RUTA_ARCHIVO_ABET = "TAG_PARAM_RUTA_ARCHIVO_ABET";
    public final static String TAG_PARAM_ARCHIVO_ABET = "TAG_PARAM_ARCHIVO_ABET";
    public final static String CMD_SUBIR_NOTA_PROYECTO_GRADO = "CMD_SUBIR_NOTA_PROYECTO_GRADO";
    public final static String CMD_SUBIR_NOTA_PROYECTO_GRADO_PENDIENTE = "CMD_SUBIR_NOTA_PROYECTO_GRADO_PENDIENTE";
    public final static String CMD_SUBIR_NOTA_PROYECTO_GRADO_PENDIENTE_ESPECIAL = "CMD_SUBIR_NOTA_PROYECTO_GRADO_PENDIENTE_ESPECIAL";
    public final static String CMD_SUBIR_ARCHIVO_AFICHE_PROYECTO_GRADO = "CMD_SUBIR_ARCHIVO_AFICHE_PROYECTO_GRADO";
    public final static String CMD_SUBIR_ARCHIVO_AFICHE_PROYECTO_GRADO_PENDIENTE = "CMD_SUBIR_ARCHIVO_AFICHE_PROYECTO_GRADO_PENDIENTE";
    public final static String CMD_SUBIR_ARCHIVO_AFICHE_PROYECTO_GRADO_PENDIENTE_ESPECIAL = "CMD_SUBIR_ARCHIVO_AFICHE_PROYECTO_GRADO_PENDIENTE_ESPECIAL";
    public final static String TAG_PARAM_TIPO_SUBIR_ARCHIVO_ABET = "TAG_PARAM_TIPO_SUBIR_ARCHIVO_ABET";
    public final static String TAG_PARAM_TIPO_SUBIR_NOTA_PROYECTO_GRADO = "TAG_PARAM_TIPO_SUBIR_NOTA_PROYECTO_GRADO";
    public final static String TAG_PARAM_TIPO_SUBIR_ARCHIVO_ABET_PENDIENTE = "TAG_PARAM_TIPO_SUBIR_ARCHIVO_ABET_PENDIENTE";
    public final static String TAG_PARAM_TIPO_SUBIR_ARCHIVO_ABET_PENDIENTE_ESPECIAL = "TAG_PARAM_TIPO_SUBIR_ARCHIVO_ABET_PENDIENTE_ESPECIAL";
    public final static String TAG_PARAM_TIPO_SUBIR_NOTA_PROYECTO_GRADO_PENDIENTE = "TAG_PARAM_TIPO_SUBIR_NOTA_PROYECTO_GRADO_PENDIENTE";
    public final static String TAG_PARAM_TIPO_SUBIR_NOTA_PROYECTO_GRADO_PENDIENTE_ESPECIAL = "TAG_PARAM_TIPO_SUBIR_NOTA_PROYECTO_GRADO_PENDIENTE_ESPECIAL";
    public final static String CMD_ENVIAR_INFORME_RETIRO_PROYECTO_GRADO = "CMD_ENVIAR_INFORME_RETIRO_PROYECTO_GRADO";
    public final static String TAG_PARAM_RETIRO = "TAG_PARAM_RETIRO";
    public final static String TAG_PARAM_TIPO_ENVIAR_INFORME_RETIRO = "TAG_PARAM_TIPO_ENVIAR_INFORME_RETIRO";
    public final static String CMD_ENVIAR_INFORME_PENDIENTE_PROYECTO_GRADO = "CMD_ENVIAR_INFORME_PENDIENTE_PROYECTO_GRADO";
    public final static String CMD_ENVIAR_INFORME_PENDIENTE_ESPECIAL_PROYECTO_GRADO = "CMD_ENVIAR_INFORME_PENDIENTE_ESPECIAL_PROYECTO_GRADO";
    public final static String TAG_PARAM_TIPO_ENVIAR_INFORME_PENDIENTE = "TAG_PARAM_TIPO_ENVIAR_INFORME_PENDIENTE";
    public final static String CMD_ENVIAR_APROBACION_PENDIENTE_PROYECTO_GRADO = "CMD_ENVIAR_APROBACION_PENDIENTE_PROYECTO_GRADO";
    public final static String CMD_ENVIAR_APROBACION_PENDIENTE_ESPECIAL_PROYECTO_GRADO = "CMD_ENVIAR_APROBACION_PENDIENTE_ESPECIAL_PROYECTO_GRADO";
    public final static String TAG_PARAM_TIPO_ENVIAR_APROBACION_PENDIENTE = "TAG_PARAM_TIPO_ENVIAR_APROBACION_PENDIENTE";
    public final static String TAG_PARAM_TIPO_ENVIAR_APROBACION_PENDIENTE_ESPECIAL = "TAG_PARAM_TIPO_ENVIAR_APROBACION_PENDIENTE_ESPECIAL";
    public final static String TAG_PARAM_CALIFICACIONES_JURADOS = "TAG_PARAM_CALIFICACIONES_JURADOS";
    public final static String TAG_PARAM_CALIFICACION_JURADO = "TAG_PARAM_CALIFICACION_JURADO";
    public final static String TAG_PARAM_JURADO_TESIS_INTERNO = "TAG_PARAM_JURADO_TESIS_INTERNO";
    public final static String TAG_PARAM_TERINADO = "TAG_PARAM_TERINADO";
    public final static String TAG_PARAM_CALIFICACION_CRITERIO = "TAG_PARAM_CALIFICACION_CRITERIO";
    public final static String TAG_PARAM_CALIFICACION_CRITERIOS = "TAG_PARAM_CALIFICACION_CRITERIOS";
    public final static String CMD_DAR_EVALUACION_JURADO_POR_HASH = "CMD_DAR_EVALUACION_JURADO_POR_HASH";
    public final static String CMD_GUARDAR_INFORMACION_JURADO_EXTERNO = "CMD_GUARDAR_INFORMACION_JURADO_EXTERNO";
    public final static String TAG_PARAM_PESO = "TAG_PARAM_PESO";
    public final static String TAG_PARAM_NOMBRE_CATEGORIA = "TAG_PARAM_NOMBRE_CATEGORIA";
    public final static String TAG_PARAM_ORDEN_CATEGORIA = "TAG_PARAM_ORDEN_CATEGORIA";
    public final static String TAG_PARAM_ORDEN_CRITERIO = "TAG_PARAM_ORDEN_CRITERIO";
    public final static String CMD_GUARDAR_EVALUACION_POR_HASH = "CMD_GUARDAR_EVALUACION_POR_HASH";
    public final static String CTE_TESIS_CALIFICACION_JURADOS = "CTE_TESIS_CALIFICACION_JURADOS";
    public final static String CTE_TESIS_CALIFICACION_ASESOR = "CTE_TESIS_CALIFICACION_ASESOR";
    public final static String TAG_PARAM_NOTA_SUSTENTACION_TESIS2 = "TAG_PARAM_NOTA_SUSTENTACION_TESIS2";
    public final static String CMD_FIN_INSCRIPCION_SUBAREA_REGANHO_CORREO = "CMD_FIN_INSCRIPCION_SUBAREA_REGANHO_CORREO";
    public final static String CMD_FIN_INSCRIPCION_SUBAREA_COORDINADOR_SUBAREA_CORREO_REGANHO = "CMD_FIN_INSCRIPCION_SUBAREA_COORDINADOR_SUBAREA_CORREO_REGANHO";
    public final static String CMD_FIN_INSCRIPCION_TESIS_1_REGANHO_ASESOR = "CMD_FIN_INSCRIPCION_TESIS_1_REGANHO_ASESOR";
    public final static String CMD_FIN_INSCRIPCION_TESIS_1_COORDINACION_CORREO_REGANHO = "CMD_FIN_INSCRIPCION_TESIS_1_COORDINACION_CORREO_REGANHO";
    public final static String CMD_FIN_DIA_INSCRIPCION_TESIS_2_REGANHO_ASESOR = "CMD_FIN_DIA_INSCRIPCION_TESIS_2_REGANHO_ASESOR";
    public final static String CMD_DAR_HISTORICO_TESIS_PREGRADO_POR_ESTADO_SEMESTRE_Y_CORREO_ASESOR = "CMD_DAR_HISTORICO_TESIS_PREGRADO_POR_ESTADO_SEMESTRE_Y_CORREO_ASESOR";
    public final static String CMD_DAR_DETALLE_HISTORICO_PROYECTO_GRADO = "CMD_DAR_DETALLE_HISTORICO_PROYECTO_GRADO";
    public final static String CMD_ACTUALIZAR_PROYECTO_GRADO = "CMD_ACTUALIZAR_PROYECTO_GRADO";
    public final static String CMD_ACTUALIZAR_PROYECTO_GRADO_ESTADO = "CMD_ACTUALIZAR_PROYECTO_GRADO_ESTADO";
    public final static String CMD_DAR_HISTORICO_TESIS1_POR_ESTADO_SEMESTRE_Y_CORREO_ASESOR = "CMD_DAR_HISTORICO_TESIS1_POR_ESTADO_SEMESTRE_Y_CORREO_ASESOR";
    public final static String CMD_DAR_DETALLE_HISTORICO_TESIS1 = "CMD_DAR_DETALLE_HISTORICO_TESIS1";
    public final static String CMD_DAR_HISTORICO_TESIS2_POR_ESTADO_SEMESTRE_Y_CORREO_ASESOR = "CMD_DAR_HISTORICO_TESIS2_POR_ESTADO_SEMESTRE_Y_CORREO_ASESOR";
    public final static String CMD_DAR_DETALLE_HISTORICO_TESIS2 = "CMD_DAR_DETALLE_HISTORICO_TESIS2";
    public final static String CMD_DAR_HISTORICO_ESTUDIANTES_TESIS_MAESTRIA_PROFESOR = "CMD_DAR_HISTORICO_ESTUDIANTES_TESIS_MAESTRIA_PROFESOR";
    public final static String CMD_DAR_HISTORICO_ESTUDIANTES_TESIS_MAESTRIA = "CMD_DAR_HISTORICO_ESTUDIANTES_TESIS_MAESTRIA";
    public final static String CMD_DAR_HISTORICO_ESTUDIANTE_TESIS_MAESTRIA = "CMD_DAR_HISTORICO_ESTUDIANTE_TESIS_MAESTRIA";
    public final static String CMD_DAR_HISTORICO_ESTUDIANTES_TESIS_DE_PREGRADO = "CMD_DAR_HISTORICO_ESTUDIANTES_TESIS_DE_PREGRADO";
    public final static String CMD_DAR_HISTORICO_ESTUDIANTES_TESIS_DE_PREGRADO_POR_PROFESOR = "CMD_DAR_HISTORICO_ESTUDIANTES_TESIS_DE_PREGRADO_POR_PROFESOR";
    public final static String CMD_DAR_HISTORICO_ESTUDIANTE_TESIS_DE_PREGRADO = "CMD_DAR_HISTORICO_ESTUDIANTE_TESIS_DE_PREGRADO";
    public final static String CMD_DAR_DETALLE_HISTORICO_ESTUDIANTE_TESIS_MAESTRIA = "CMD_DAR_DETALLE_HISTORICO_ESTUDIANTE_TESIS_MAESTRIA";
    public final static String CMD_CONSULTAR_PROYECTOS_DE_GRADO_PARA_EXTERNOS = "CMD_CONSULTAR_PROYECTOS_DE_GRADO_PARA_EXTERNOS";
    public final static String CMD_DAR_SOLICITUDES_TESIS1_EXTERNOS = "CMD_DAR_SOLICITUDES_TESIS1_EXTERNOS";
    public final static String CMD_APROBAR_PROYECTO_GRADO_COORDINACION = "CMD_APROBAR_PROYECTO_GRADO_COORDINACION";
    public final static String TAG_PARAM_RAZON = "TAG_PARAM_RAZON";
    public final static String CMD_FECHA_PROYECTO_GRADO_POR_PERIODO = "CMD_FECHA_PROYECTO_GRADO_POR_PERIODO";
    
    // Acciones Proyecto grado
    public final static String VAL_ACCION_ACEPTAR_SOLICITUD_PROYECTO_GRADO_ASESOR = "VAL_ACCION_ACEPTAR_SOLICITUD_PROYECTO_GRADO_ASESOR";
    public final static String VAL_ACCION_ENVIAR_PROPUESTA_PROYECTO_GRADO = "VAL_ACCION_ENVIAR_PROPUESTA_PROYECTO_GRADO";
    public final static String VAL_ACCION_ACEPTAR_PROPUESTA_TESIS_POR_ASESOR = "VAL_ACCION_ACEPTAR_PROPUESTA_TESIS_POR_ASESOR";
    public final static String VAL_ACCION_AGREGAR_COMENTARIO_PROYECTO_GRADO = "VAL_ACCION_AGREGAR_COMENTARIO_PROYECTO_GRADO";
    public final static String VAL_ACCION_ENVIAR_AFICHE_PROYECTO_GRADO = "VAL_ACCION_ENVIAR_AFICHE_PROYECTO_GRADO";
    public final static String VAL_ACCION_ENVIAR_APROBACION_AFICHE_PROYECTO_GRADO = "VAL_ACCION_ENVIAR_APROBACION_AFICHE_PROYECTO_GRADO";
    public final static String VAL_ACCION_INFORME_RETIRO_PROYECTO_GRADO = "VAL_ACCION_INFORME_RETIRO_PROYECTO_GRADO";
    public final static String VAL_ACCION_SUBIR_NOTA_PROYECTO_GRADO = "VAL_ACCION_SUBIR_NOTA_PROYECTO_GRADO";
    public final static String VAL_ACCION_SUBIR_DOCUMENTO_ABET_PROYECTO_GRADO = "VAL_ACCION_SUBIR_DOCUMENTO_ABET_PROYECTO_GRADO";
    public final static String VAL_ACCION_ENVIAR_INFORME_PENDIENTE_PROYECTO_GRADO = "VAL_ACCION_ENVIAR_INFORME_PENDIENTE_PROYECTO_GRADO";
    public final static String VAL_ACCION_ENVIAR_INFORME_PENDIENTE_ESPECIAL_PROYECTO_GRADO = "VAL_ACCION_ENVIAR_INFORME_PENDIENTE_ESPECIAL_PROYECTO_GRADO";
    public final static String VAL_MODULO_PROYECTO_GRADO = "VAL_MODULO_PROYECTO_GRADO";
    public final static String VAL_PROCESO_PROYECTO_GRADO = "VAL_PROCESO_PROYECTO_GRADO";
    //ESTADOS PROYECTO DE GRADO
    //estados
    public final static String CTE_TESIS_ACEPTADA_ASESOR_ESPERANDO_DOCUMENTO_PROPUESTA = "CTE_TESIS_ACEPTADA_ASESOR_ESPERANDO_DOCUMENTO_PROPUESTA";
    public final static String CTE_TESIS_PY_PROPUESTA_ENVIADA_ESPERANDO_APROBACION_ASESOR = "CTE_TESIS_PY_PROPUESTA_ENVIADA_ESPERANDO_APROBACION_ASESOR";
    public final static String CTE_TESIS_PREGRADO_RETIRADA = "CTE_TESIS_PREGRADO_RETIRADA";
    public final static String CTE_TESIS_PY_ESPERANDO_REPORTE_TREINTA_POR_CIENTO = "CTE_TESIS_PY_ESPERANDO_REPORTE_TREINTA_POR_CIENTO";
    public final static String CTE_TESIS_PY_ESPERANDO_INFORME_RETIRO = "CTE_TESIS_PY_ESPERANDO_INFORME_RETIRO";
    public final static String CTE_TESIS_PY_ESPERANDO_POSTER = "CTE_TESIS_PY_ESPERANDO_POSTER";
    public final static String CTE_TESIS_PY_ESPERANDO_POSTER_PENDIENTE = "CTE_TESIS_PY_ESPERANDO_POSTER_PENDIENTE";
    public final static String CTE_TESIS_PY_ESPERANDO_POSTER_PENDIENTE_ESPECIAL = "CTE_TESIS_PY_ESPERANDO_POSTER_PENDIENTE_ESPECIAL";
    public final static String CTE_TESIS_PY_POSTER_ENVIADO_ESPERANDO_APROBACION_ASESOR = "CTE_TESIS_PY_POSTER_ENVIADO_ESPERANDO_APROBACION_ASESOR";
    public final static String CTE_TESIS_PY_POSTER_ENVIADO_ESPERANDO_APROBACION_ASESOR_PENDIENTE = "CTE_TESIS_PY_POSTER_ENVIADO_ESPERANDO_APROBACION_ASESOR_PENDIENTE";
    public final static String CTE_TESIS_PY_POSTER_ENVIADO_ESPERANDO_APROBACION_ASESOR_PENDIENTE_ESPECIAL = "CTE_TESIS_PY_POSTER_ENVIADO_ESPERANDO_APROBACION_ASESOR_PENDIENTE_ESPECIAL";
    public final static String CTE_TESIS_PY_ESPERANDO_INFORME_PENDIENTE = "CTE_TESIS_PY_ESPERANDO_INFORME_PENDIENTE";
    public final static String CTE_TESIS_PY_ESPERANDO_NOTA = "CTE_TESIS_PY_ESPERANDO_NOTA";
    public final static String CTE_TESIS_PY_ESPERANDO_ABET = "CTE_TESIS_PY_ESPERANDO_ABET";
    public final static String CTE_TESIS_PY_ESPERANDO_NOTA_PENDIENTE = "CTE_TESIS_PY_ESPERANDO_NOTA_PENDIENTE";
    public final static String CTE_TESIS_PY_ESPERANDO_NOTA_PENDIENTE_ESPECIAL = "CTE_TESIS_PY_ESPERANDO_NOTA_PENDIENTE_ESPECIAL";
    public final static String CTE_TESIS_PY_ESPERANDO_ABET_PENDIENTE = "CTE_TESIS_PY_ESPERANDO_ABET_PENDIENTE";
    public final static String CTE_TESIS_PY_ESPERANDO_ABET_PENDIENTE_ESPECIAL = "CTE_TESIS_PY_ESPERANDO_ABET_PENDIENTE_ESPECIAL";
    public final static String CTE_TESIS_PY_TERMINADA = "CTE_TESIS_PY_TERMINADA";
    public final static String CTE_TESIS_PY_PERDIDA = "CTE_TESIS_PY_PERDIDA";
    public final static String CTE_TESIS_PY_ESPERANDO_APROBACION_COORDINACION = "CTE_TESIS_PY_ESPERANDO_APROBACION_COORDINACION";
    public final static String CTE_TESIS_PY_RECHAZADA_POR_COORDINACION = "CTE_TESIS_PY_RECHAZADA_POR_COORDINACION";
    public final static String CTE_TESIS_PY_ESPERANDO_APROBACION_PENDIENTE_ESPECIAL = "CTE_TESIS_PY_ESPERANDO_APROBACION_PENDIENTE_ESPECIAL";
    public final static String CTE_RETIRO_SI = "CTE_RETIRO_SI";
    public final static String CTE_RETIRO_NO = "CTE_RETIRO_NO";
    public final static String TAG_PARAM_PENDIENTE = "TAG_PARAM_PENDIENTE";
    public final static String CTE_PENDIENTE_SI = "CTE_PENDIENTE_SI";
    public final static String CTE_PENDIENTE_NO = "CTE_PENDIENTE_NO";
    public final static String[] ESTADOS = {CTE_INSCRIPCION_POR_APROBAR_ASESOR, CTE_TESIS_PY_ESPERANDO_APROBACION_COORDINACION, CTE_TESIS_PY_RECHAZADA_POR_COORDINACION, CTE_TESIS_ACEPTADA_ASESOR_ESPERANDO_DOCUMENTO_PROPUESTA, CTE_TESIS_PY_PROPUESTA_ENVIADA_ESPERANDO_APROBACION_ASESOR,
        CTE_TESIS_PY_ESPERANDO_REPORTE_TREINTA_POR_CIENTO, CTE_TESIS_PY_ESPERANDO_POSTER, CTE_TESIS_PY_POSTER_ENVIADO_ESPERANDO_APROBACION_ASESOR,
        CTE_TESIS_PY_ESPERANDO_INFORME_PENDIENTE, CTE_TESIS_PY_ESPERANDO_NOTA, CTE_TESIS_PY_ESPERANDO_ABET, CTE_TESIS_PY_TERMINADA, CTE_TESIS_PREGRADO_RETIRADA};
    public final static String CTE_TESIS_ACEPTADA_ASESOR_ESPERANDO_INSCRIPCION_BANNER = "CTE_TESIS_ACEPTADA_ASESOR_ESPERANDO_INSCRIPCION_BANNER";
    public final static String CMD_CONFIRMAR_INSCRIPCION_BANNER_PROYECTO_GRADO = "CMD_CONFIRMAR_INSCRIPCION_BANNER_PROYECTO_GRADO";
    public final static String CMD_CONFIRMAR_INSCRIPCION_BANNER_TESIS1 = "CMD_CONFIRMAR_INSCRIPCION_BANNER_TESIS1";
    public final static String CMD_CONFIRMAR_INSCRIPCION_BANNER_TESIS2 = "CMD_CONFIRMAR_INSCRIPCION_BANNER_TESIS2";
    /**
     * CONSTANTES ADMINISTRADOR SISINFO
     */
    //Carga Grupo
    public final static String RUTA_ARCHIVO_CARGA_GRUPO = "RUTA_ARCHIVO_CARGA_GRUPO";
    public final static String TAG_PARAM_TIPO_CARGA = "TAG_PARAM_TIPO_CARGA";
    public final static String TAG_PARAM_CARGA_GRUPO = "TAG_PARAM_CARGA_GRUPO";
    public final static String TAG_PARAM_RUTA_CARGA_ARCHIVO = "TAG_PARAM_RUTA_CARGA_ARCHIVO";
    public final static String CMD_SUBIR_ARCHIVO_CARGA_GRUPO = "CMD_SUBIR_ARCHIVO_CARGA_GRUPO";
    public final static String TIPO_CARGA_GRUPO_ESTUDIANTES_MATRICULADOS = "TIPO_CARGA_GRUPO_ESTUDIANTES_MATRICULADOS";
    public final static String TIPO_CARGA_GRUPO_ESTUDIANTES = "TIPO_CARGA_GRUPO_ESTUDIANTES";
    public final static String TIPO_CARGA_GRUPO_PROFESORES = "TIPO_CARGA_GRUPO_PROFESORES";
    //Consulta y edición de roles
    public final static String CMD_GUARDAR_CAMBIOS_USUARIO = "CMD_GUARDAR_CAMBIOS_USUARIO";
    public final static String CMD_CONSULTAR_ROLES = "CMD_CONSULTAR_ROLES";
    public final static String CMD_CONSULTAR_USUARIOS = "CMD_CONSULTAR_USUARIOS";
    public final static String CMD_CONSULTAR_USUARIO = "CMD_CONSULTAR_USUARIO";
    public final static String CMD_CONSULTAR_CONSTANTES_ROLES = "CMD_CONSULTAR_CONSTANTES_ROLES";
    public final static String TAG_PARAM_ROLES = "TAG_PARAM_ROLES";
    public final static String TAG_PARAM_USUARIOS = "TAG_PARAM_USUARIOS";
    //Creación de usuarios
    public final static String CMD_CREAR_USUARIO = "CMD_CREAR_USUARIO";
    public final static String TAG_PARAM_ES_PERSONA = "TAG_PARAM_ES_PERSONA";
    public final static String CMD_CONSULTAR_PAISES = "CMD_CONSULTAR_PAISES";
    public final static String CMD_CONSULTAR_TIPOS_DOCUMENTO = "CMD_CONSULTAR_TIPOS_DOCUMENTO";
    //creacion de Grupos
    public final static String CMD_CREAR_GRUPO_PERSONAS = "CMD_CREAR_GRUPO_PERSONAS";
    public final static String CMD_CONSULTAR_GRUPO_POR_ID = "CMD_CONSULTAR_GRUPO_POR_ID";
    public final static String CMD_EDITAR_GRUPO_PERSONAS = "CMD_EDITAR_GRUPO_PERSONAS";
    public final static String CMD_ELIMINAR_GRUPOS_PERSONA_POR_FINAL_PERIODO = "CMD_ELIMINAR_GRUPOS_PERSONA_POR_FINAL_PERIODO";
    public final static String TAG_PARAM_DUEHNO_GRUPO = "TAG_PARAM_DUEHNO_GRUPO";
    public final static String CMD_CONFIGURAR_TIMMER_ELIMINAR_GRUPOS = "CMD_CONFIGURAR_TIMMER_ELIMINAR_GRUPOS";
    //Correos de auditoría
    public final static String CMD_CONSULTAR_CORREOS_AUDITORIA = "CMD_CONSULTAR_CORREOS_AUDITORIA";
    public final static String CMD_CONSULTAR_CORREO_AUDITORIA = "CMD_CONSULTAR_CORREO_AUDITORIA";
    public final static String CMD_CONSULTAR_CORREO_AUDITORIA_POR_DESTINATARIOS_FECHA_Y_ASUNTO = "CMD_CONSULTAR_CORREO_AUDITORIA_POR_DESTINATARIOS_FECHA_Y_ASUNTO";
    public final static String CMD_ELIMINAR_CORREO_AUDITORIA_POR_DESTINATARIOS_FECHA_Y_ASUNTO = "CMD_ELIMINAR_CORREO_AUDITORIA_POR_DESTINATARIOS_FECHA_Y_ASUNTO";
    public final static String CMD_ELIMINAR_CORREO_AUDITORIA = "CMD_ELIMINAR_CORREO_AUDITORIA";
    //Auditoria usuario
    public final static String CMD_CONSULTAR_AUDITORIA_USUARIO_POR_USUARIO_FECHA_ROL_Y_COMANDO = "CMD_CONSULTAR_AUDITORIA_USUARIO_POR_USUARIO_FECHA_ROL_Y_COMANDO";
    // Filtros de correos
    public final static String OP_EMPIEZA_POR = "OP_EMPIEZA_POR";
    public final static String OP_TERMINA_EN = "OP_TERMINA_EN";
    public final static String VAL_FILTRO_DESTINATARIO = "VAL_FILTRO_DESTINATARIO";
    public final static String VAL_FILTRO_ASUNTO = "VAL_FILTRO_ASUNTO";
    public final static String VAL_FILTRO_MENSAJE = "VAL_FILTRO_MENSAJE";
    public final static String TAG_PARAM_FILTROS_CORREO = "TAG_PARAM_FILTROS_CORREO";
    public final static String TAG_PARAM_FILTRO_CORREO = "TAG_PARAM_FILTRO_CORREO";
    public final static String TAG_PARAM_REDIRECCION = "TAG_PARAM_REDIRECCION";
    public final static String TAG_PARAM_ID_FILTRO = "TAG_PARAM_ID_FILTRO";
    public final static String TAG_PARAM_ID_CORREO = "TAG_PARAM_ID_CORREO";
    public final static String TAG_PARAM_CORREO_PARA = "TAG_PARAM_CORREO_PARA";
    public final static String TAG_PARAM_CORREO_ASUNTO = "TAG_PARAM_CORREO_ASUNTO";
    public final static String TAG_PARAM_CORREO_CC = "TAG_PARAM_CORREO_CC";
    public final static String TAG_PARAM_CORREO_CCO = "TAG_PARAM_CORREO_CCO";
    public final static String TAG_PARAM_CORREO_ARCHIVO = "TAG_PARAM_CORREO_ARCHIVO";
    public final static String TAG_PARAM_CORREO_MENSAJE = "TAG_PARAM_CORREO_MENSAJE";
    public final static String TAG_PARAM_CORREOS_SIN_ENVIAR = "TAG_PARAM_CORREOS_SIN_ENVIAR";
    public final static String TAG_PARAM_CORREO_SIN_ENVIAR = "TAG_PARAM_CORREO_SIN_ENVIAR";
    public final static String TAG_PARAM_TIPOS_FILTRO_CORREO = "TAG_PARAM_TIPOS_FILTRO_CORREO";
    public final static String TAG_PARAM_OPERACIONES_FILTRO_CORREO = "TAG_PARAM_OPERACIONES_FILTRO_CORREO";
    public final static String TAG_PARAM_OPERACION_FILTRO_CORREO = "TAG_PARAM_OPERACION_FILTRO_CORREO";
    // Comandos filtros de correo
    public final static String CMD_DAR_CORREOS_SIN_ENVIAR = "CMD_DAR_CORREOS_SIN_ENVIAR";
    public final static String CMD_DAR_CORREO_SIN_ENVIAR = "CMD_DAR_CORREO_SIN_ENVIAR";
    public final static String CMD_ELIMINAR_CORREOS_SIN_ENVIAR = "CMD_ELIMINAR_CORREOS_SIN_ENVIAR";
    public final static String CMD_ENVIAR_CORREOS_SIN_ENVIAR = "CMD_ENVIAR_CORREOS_SIN_ENVIAR";
    public final static String CMD_DAR_FILTROS_CORREO = "CMD_DAR_FILTROS_CORREO";
    public final static String CMD_DAR_FILTRO_CORREO = "CMD_DAR_FILTRO_CORREO";
    public final static String CMD_AGREGAR_FILTRO_CORREO = "CMD_AGREGAR_FILTRO_CORREO";
    public final static String CMD_ELIMINAR_FILTRO_CORREO = "CMD_ELIMINAR_FILTRO_CORREO";
    public final static String CMD_EDITAR_FILTRO_CORRREO = "CMD_EDITAR_FILTRO_CORRREO";
    public final static String CMD_DAR_TIPOS_FILTRO_CORREO = "CMD_DAR_TIPOS_FILTRO_CORREO";
    public final static String CMD_DAR_OPERACIONES_FILTRO_CORREO = "CMD_DAR_OPERACIONES_FILTRO_CORREO";
    public final static String CMD_EDITAR_CORREO_SIN_ENVIAR = "CMD_EDITAR_CORREO_SIN_ENVIAR";
    //periodicidad correo diagnostico
    public final static String VAL_PERIODICIDAD_ENVIO_CORREO_DIAGNOSTICO = "VAL_PERIODICIDAD_ENVIO_CORREO_DIAGNOSTICO";
    //MasterPassword
    public final static String VAL_LDAP_MASTER_PASSWORD = "VAL_LDAP_MASTER_PASSWORD";
    
    /*
     *
     * Configuracion de pruebas
     */
    public static String CONFIGURACION_PARAM_PRUEBA = "CONFIGURACION_PARAM_PRUEBA";
    public static String CONFIGURACION_PARAM_CONNECTSTRING = "CONFIGURACION_PARAM_CONNECTSTRING";
    public static String CONFIGURACION_PARAM_USER = "CONFIGURACION_PARAM_USER";
    public static String CONFIGURACION_PARAM_PASSWORD = "CONFIGURACION_PARAM_PASSWORD";
    public static String CONFIGURACION_PARAM_CORREO_PRUEBA = "CONFIGURACION_PARAM_CORREO_PRUEBA";
    public static String CONFIGURACION_PARAM_CONNECTSTRING_HISTORICO = "CONFIGURACION_PARAM_CONNECTSTRING_HISTORICO";
    public static String CONFIGURACION_PARAM_USER_HISTORICO = "CONFIGURACION_PARAM_USER_HISTORICO";
    public static String CONFIGURACION_PARAM_PASSWORD_HISTORICO = "CONFIGURACION_PARAM_PASSWORD_HISTORICO";

    /*
     * Modulo soporte sisinfo
     */
    public final static String TAG_PARAM_TIPO_SOLUCIONAR_EXCEPCION = "TAG_PARAM_TIPO_SOLUCIONAR_EXCEPCION";
    public final static String VAL_CORREO_SOPORTE_SISINFO = "VAL_CORREO_SOPORTE_SISINFO";
    public final static String CMD_CONSULTAR_EXCEPCION_POR_ID = "CMD_CONSULTAR_EXCEPCION_POR_ID";
    public final static String CMD_CONSULTAR_EXCEPCIONES_POR_ESTADO = "CMD_CONSULTAR_EXCEPCIONES_POR_ESTADO";
    public final static String CMD_SOLUCIONAR_EXCEPCION = "CMD_SOLUCIONAR_EXCEPCION";
    public final static String TAG_PARAM_EXCEPCION = "TAG_PARAM_EXCEPCION";
    public final static String TAG_PARAM_EXCEPCIONES = "TAG_PARAM_EXCEPCIONES";
    public final static String TAG_PARAM_SOLUCIONADO = "TAG_PARAM_SOLUCIONADO";
    public final static String TAG_PARAM_COMANDO_SISINFO = "TAG_PARAM_COMANDO_SISINFO";
    public final static String TAG_PARAM_FECHA_ERROR = "TAG_PARAM_FECHA_ERROR";
    public final static String TAG_PARAM_FECHA_SOLUCION = "TAG_PARAM_FECHA_SOLUCION";
    public final static String TAG_PARAM_METODO_SISINFO = "TAG_PARAM_METODO_SISINFO";
    public final static String TAG_PARAM_MODULO_SISINFO = "TAG_PARAM_MODULO_SISINFO";
    public final static String TAG_PARAM_RESPUESTA = "TAG_PARAM_RESPUESTA";
    public final static String TAG_PARAM_REPORTEINCIDENTE = "TAG_PARAM_REPORTEINCIDENTE";
    public final static String CMD_CONSULTAR_INCIDENTE_POR_ID = "CMD_CONSULTAR_INCIDENTE_POR_ID";
    public final static String CMD_REPORTAR_INCIDENTE_SISINFO = "CMD_REPORTAR_INCIDENTE_SISINFO";
    public final static String CMD_CONSULTAR_INCIDENTE_SISINFO_POR_ID = "CMD_CONSULTAR_INCIDENTE_SISINFO_POR_ID";
    public final static String CMD_CONSULTAR_INCIDENTE_SISINFO_POR_CORREO_CREADOR = "CMD_CONSULTAR_INCIDENTE_SISINFO_POR_CORREO_CREADOR";
    public final static String CMD_CONSULTAR_INCIDENTE_SISINFO_POR_ESTADO = "CMD_CONSULTAR_INCIDENTE_SISINFO_POR_ESTADO";
    public final static String CMD_MODIFICAR_INCIDENTE_SISINFO = "CMD_MODIFICAR_INCIDENTE_SISINFO";
    public final static String TAG_PARAM_REPORTES_INCIDENTES = "TAG_PARAM_REPORTES_INCIDENTES";
    public final static String TAG_PARAM_DESCRIPCION_INCIDENTE = "TAG_PARAM_DESCRIPCION_INCIDENTE";
    public final static String TAG_PARAM_DESCRIPCION_SOLUCION = "TAG_PARAM_DESCRIPCION_SOLUCION";
    public final static String CMD_ELIMINAR_INCIDENTE_SISINFO_POR_ID = "CMD_ELIMINAR_INCIDENTE_SISINFO_POR_ID";
    public final static String CMD_ELIMINAR_EXCEPCION_POR_ID = "CMD_ELIMINAR_EXCEPCION_POR_ID";
    public final static String CMD_CONSULTAR_INCIDENTE_SISINFO_SOPORTE = "CMD_CONSULTAR_INCIDENTE_SISINFO_SOPORTE";
    public final static String CMD_CONSULTAR_EXCEPCIONES_SISINFO_SOPORTE = "CMD_CONSULTAR_EXCEPCIONES_SISINFO_SOPORTE";
    public final static String TAG_PARAM_MODULOS_SISINFO = "TAG_PARAM_MODULOS_SISINFO";
    public final static String CMD_CONSULTAR_MODULOS_SISINFO = "CMD_CONSULTAR_MODULOS_SISINFO";
    public final static String TAG_PARAM_XML_ENTRADA = "TAG_PARAM_XML_ENTRADA";
    public final static String CMD_DAR_LISTA_PERSONA_SOPORTE = "CMD_DAR_LISTA_PERSONA_SOPORTE";
    public final static String CMD_ASIGNAR_INCIDENTE = "CMD_ASIGNAR_INCIDENTE";
    public final static String CMD_DAR_LISTA_BLANCA = "CMD_DAR_LISTA_BLANCA";
    public final static String CMD_DAR_LISTA_COMANDO = "CMD_DAR_LISTA_COMANDO";
    public final static String TAG_PARAM_LISTA_PERSONA_SOPORTE = "TAG_PARAM_LISTA_PERSONA_SOPORTE";
    public final static String TAG_PARAM_LISTA_BLANCA = "TAG_PARAM_LISTA_BLANCA";
    public final static String TAG_PARAM_PERSONA_SOPORTE = "TAG_PARAM_PERSONA_SOPORTE";
    public final static String CMD_CONSULTAR_PERSONA_SOPORTE_POR_ID = "CMD_CONSULTAR_PERSONA_SOPORTE_POR_ID";
    public final static String CMD_CONSULTAR_INCIDENTES_PERSONA_SOPORTE = "CMD_CONSULTAR_INCIDENTES_PERSONA_SOPORTE";
    public final static String CMD_GUARDAR_COMANDO_XML = "CMD_GUARDAR_COMANDO_XML";
    public final static String CMD_CONSULTAR_LISTA_ACCION_VENCIDA = "CMD_CONSULTAR_LISTA_ACCION_VENCIDA";
    public final static String TAG_PARAM_ACCION_VENCIDA = "TAG_PARAM_ACCION_VENCIDA";
    public final static String TAG_PARAM_ACCION = "TAG_PARAM_ACCION";
    public final static String TAG_PARAM_PROCESO = "TAG_PARAM_PROCESO";
    public final static String TAG_PARAM_ERROR_LISTA_BLANCA = "TAG_PARAM_ERROR_LISTA_BLANCA";
    public final static String TAG_PARAM_ID_ERROR = "TAG_PARAM_ID_ERROR";
    public final static String TAG_PARAM_EXPLICACION = "TAG_PARAM_EXPLICACION";
    public final static String CMD_ELIMINAR_ERROR_LISTA_BLANCA = "CMD_ELIMINAR_ERROR_LISTA_BLANCA";
    public final static String TAG_PARAM_LISTA_COMANDO = "TAG_PARAM_LISTA_COMANDO";
    public final static String TAG_PARAM_COMANDO_LISTA_COMANDO = "TAG_PARAM_COMANDO_LISTA_COMANDO";
    public final static String TAG_PARAM_NOMBRE_COMANDO = "TAG_PARAM_NOMBRE_COMANDO";
    public final static String CMD_ELIMINAR_COMANDO_LISTA_COMANDO = "CMD_ELIMINAR_COMANDO_LISTA_COMANDO";
    public final static String CMD_CONSULTAR_TAREAS_POR_LOGIN = "CMD_CONSULTAR_TAREAS_POR_LOGIN";
    public final static String CMD_RECORDATORIO_A_DIRECTOR_NOTAS_FALTANTES = "CMD_RECORDATORIO_A_DIRECTOR_NOTAS_FALTANTES";
    /*Historicos -- Tesis MAestria*/
    public final static String CMD_CONSULTAR_TESIS1_RECHADAS_POR_PERIODO = "CMD_CONSULTAR_TESIS1_RECHADAS_POR_PERIODO";
    public final static String CMD_CONSULTAR_TESIS2_RECHADAS_POR_PERIODO = "CMD_CONSULTAR_TESIS2_RECHADAS_POR_PERIODO";
    public final static String TAG_PARAM_TIPO_CALIFICAR_TESIS1 = "TAG_PARAM_TIPO_CALIFICAR_TESIS1";
    public final static String TAG_PARAM_TIPO_CALIFICAR_TESIS1_PENDIENTE = "TAG_PARAM_TIPO_CALIFICAR_TESIS1_PENDIENTE";
    public final static String CMD_COMPORTAMIENTO_CREAR_TAREAS_TESIS1_CALIFICACION_PENDIENTE = "CMD_COMPORTAMIENTO_CREAR_TAREAS_TESIS1_CALIFICACION_PENDIENTE";
    public final static String CMD_COMPORTAMIENTO_CREAR_TAREAS_TESIS1_CALIFICACION = "CMD_COMPORTAMIENTO_CREAR_TAREAS_TESIS1_CALIFICACION";
    /*Directorio staff departamento*/
    public final static String TAG_PARAM_MIEMBROS_STAFF = "TAG_PARAM_MIEMBROS_STAFF";
    public final static String TAG_PARAM_MIEMBRO_STAFF = "TAG_PARAM_MIEMBRO_STAFF";
    public final static String TAG_PARAM_TIPO_STAFF = "TAG_PARAM_TIPO_STAFF";
    public final static String TAG_PARAM_INFORMACION_MIEMBROSSTAFF = "TAG_PARAM_INFORMACION_MIEMBROSSTAFF";
    public final static String TAG_PARAM_INFORMACION_TIPOS_STAFF = "TAG_PARAM_INFORMACION_TIPOS_STAFF";
    public final static String TAG_PARAM_TIPO_STAFF_NOMBRE = "TAG_PARAM_TIPO_STAFF_NOMBRE";
    public final static String TAG_PARAM_TIPO_STAFF_ID = "TAG_PARAM_TIPO_STAFF_ID";
    public final static String CMD_CONSULTAR_STAFF_POR_NOMBRE = "CMD_CONSULTAR_STAFF_POR_NOMBRE";
    public final static String CMD_CONSULTAR_STAFF_POR_APELLIDOS = "CMD_CONSULTAR_STAFF_POR_APELLIDOS";
    public final static String CMD_CONSULTAR_STAFF_POR_CORREO = "CMD_CONSULTAR_STAFF_POR_CORREO";
    public final static String CMD_CONSULTAR_STAFF_TODOS = "CMD_CONSULTAR_STAFF_TODOS";
    public final static String CMD_AGREGAR_MIEMBRO_STAFF_DEPARTAMENTO = "CMD_AGREGAR_MIEMBRO_STAFF_DEPARTAMENTO";
    public final static String CMD_EDITAR_MIEMBRO_STAFF_DEPARTAMENTO = "CMD_EDITAR_MIEMBRO_STAFF_DEPARTAMENTO";
    public final static String TAG_PARAM_TIPOS_STAFF = "TAG_PARAM_TIPOS_STAFF";
    public final static String CMD_CONSULTAR_TIPOS_STAFF = "CMD_CONSULTAR_TIPOS_STAFF";
    public final static String TAG_PARAM_NOMBRE_TIPO_STAFF = "TAG_PARAM_NOMBRE_TIPO_STAFF";
    public final static String CMD_CONSULTAR_TIPOS_STAFF_POR_NOMBRE = "CMD_CONSULTAR_TIPOS_STAFF_POR_NOMBRE";
    public final static String CMD_CONSULTAR_PROFESORES_ISIS = "CMD_CONSULTAR_PROFESORES_ISIS";
    public final static String CMD_REGENERAR_TAREAS_CAMBIO_VERSION = "CMD_REGENERAR_TAREAS_CAMBIO_VERSION";
    /*
     * Correcciones generales sisinfo
     */
    public final static String CMD_CREAR_ALERTA_POR_TIMER = "CMD_CREAR_ALERTA_POR_TIMER";
    /**
     * Monitorias version 2.0
     */
    public final static String CTE_MONITORIAS_CURSOS_MONITOR_A_CARGO_CUPI2 = "CTE_MONITORIAS_CURSOS_MONITOR_A_CARGO_CUPI2";
    public static String CMD_DAR_MENSAJE_REGLAS_MONITORIAS = "CMD_DAR_MENSAJE_REGLAS_MONITORIAS";
    public static String TAG_PARAM_MENSAJE_REGLAS = "TAG_PARAM_MENSAJE_REGLAS";
    public final static String VAL_ACCION_SUBIR_NOTA_MONITOR = "VAL_ACCION_SUBIR_NOTA_MONITOR";
    public final static String VAL_PROCESO_SUBIR_NOTA_MONITORIA = "VAL_PROCESO_SUBIR_NOTA_MONITORIA";
    public final static String VAL_MODULO_MONITORIAS = "VAL_MODULO_MONITORIAS";
    //----------------------------------------------
    // ASISTENCIAS GRADUADAS
    //----------------------------------------------
    //Comandos
    public final static String CMD_CREAR_ASISTENCIA_GRADUADA = "CMD_CREAR_ASISTENCIA_GRADUADA";
    public final static String CMD_CONSULTAR_ASISTENCIA_GRADUADA = "CMD_CONSULTAR_ASISTENCIA_GRADUADA";
    public final static String CMD_CONSULTAR_ASISTENCIAS_GRADUADAS = "CMD_CONSULTAR_ASISTENCIAS_GRADUADAS";
    public final static String CMD_CONSULTAR_ASISTENCIAS_GRADUADAS_POR_PROFESOR = "CMD_CONSULTAR_ASISTENCIAS_GRADUADAS_POR_PROFESOR";
    public final static String CMD_CONSULTAR_ASISTENCIAS_GRADUADAS_POR_ESTUDIANTE = "CMD_CONSULTAR_ASISTENCIAS_GRADUADAS_POR_ESTUDIANTE";
    public final static String CMD_CALIFICAR_ASISTENCIA_GRADUADA = "CMD_CALIFICAR_ASISTENCIA_GRADUADA";
    public final static String CMD_CONSULTAR_TIPOS_ASISTENCIAS_GRADUADAS = "CMD_CONSULTAR_TIPOS_ASISTENCIAS_GRADUADAS";
    //Tags
    public final static String TAG_PARAM_ASISTENCIA_GRADUADA = "TAG_PARAM_ASISTENCIA_GRADUADA";
    public final static String TAG_PARAM_ASISTENCIAS_GRADUADAS = "TAG_PARAM_ASISTENCIAS_GRADUADAS";
    public final static String TAG_PARAM_ENCARGADO = "TAG_PARAM_ENCARGADO";
    public final static String TAG_PARAM_TIPOS_ASISTENCIAS_GRADUADAS = "TAG_PARAM_TIPOS_ASISTENCIAS_GRADUADAS";
    public final static String TAG_PARAM_TIPO_ASISTENCIAS_GRADUADAS = "TAG_PARAM_TIPO_ASISTENCIAS_GRADUADAS";
    public final static String TAG_PARAM_INFORMACION_REQUERIDA = "TAG_PARAM_INFORMACION_REQUERIDA";
    //----------------------------------------------
    //TESIS MAESTRIA HISTORICOS
    //----------------------------------------------
    //Tags
    public final static String TAG_PARAM_INSCRIPCIONES_SUBAREA = "TAG_PARAM_INSCRIPCIONES_SUBAREA";
    public final static String TAG_PARAM_INSCRIPCION_SUBAREA = "TAG_PARAM_INSCRIPCION_SUBAREA";
    public final static String TAG_PARAM_CURSOS_SUBAREA = "TAG_PARAM_CURSOS_SUBAREA";
    public final static String TAG_PARAM_CURSO_SUBAREA = "TAG_PARAM_CURSO_SUBAREA";
    //----------------------------------------------
    //RANGO FECHAS GENERAL
    //----------------------------------------------
    //Constantes para rangos
    public final static String RANGO_FECHAS_GENERAL_PROGRAMA = "RANGO_FECHAS_GENERAL_PROGRAMA";
    public final static String RANGO_FECHAS_GENERAL_CIERRE = "RANGO_FECHAS_GENERAL_CIERRE";
    public final static String RANGO_FECHAS_GENERAL_TREINTA_POR_CIENTO = "RANGO_FECHAS_GENERAL_TREINTA_POR_CIENTO";
    public final static String RANGO_FECHAS_GENERAL_NOTAS_MONITORES = "RANGO_FECHAS_GENERAL_NOTAS_MONITORES";
    public final static String RANGO_FECHAS_GENERAL_CALIFICAR_ASISTENCIA_GRADUADA = "RANGO_FECHAS_GENERAL_CALIFICAR_ASISTENCIA_GRADUADA";
    //Tags
    public final static String TAG_PARAM_ID_TIMER_INICIO = "TAG_PARAM_ID_TIMER_INICIO";
    public final static String TAG_PARAM_ID_TIMER_FIN = "TAG_PARAM_ID_TIMER_FIN";
    public final static String TAG_PARAM_COMANDO_INICIO = "TAG_PARAM_COMANDO_INICIO";
    public final static String TAG_PARAM_COMANDO_FIN = "TAG_PARAM_COMANDO_FIN";
    //Comandos
    public final static String CMD_CREAR_RANGO_FECHAS_GENERALES = "CMD_CREAR_RANGO_FECHAS_GENERALES";
    public final static String CMD_EDITAR_RANGO_FECHAS_GENERALES = "CMD_EDITAR_RANGO_FECHAS_GENERALES";
    public final static String CMD_EDITAR_RANGOS_FECHAS_GENERALES = "CMD_EDITAR_RANGOS_FECHAS_GENERALES";
    public final static String CMD_CONSULTAR_RANGO_FECHAS_GENERALES_POR_NOMBRE = "CMD_CONSULTAR_RANGO_FECHAS_GENERALES_POR_NOMBRE";
    public final static String CMD_CONSULTAR_RANGO_FECHAS_GENERALES = "CMD_CONSULTAR_RANGO_FECHAS_GENERALES";
    public final static String CMD_CONSULTAR_RANGOS_FECHAS_GENERALES = "CMD_CONSULTAR_RANGOS_FECHAS_GENERALES";
    public final static String CMD_CONSULTAR_RANGOS_Y_COMANDOS = "CMD_CONSULTAR_RANGOS_Y_COMANDOS";
    public final static String CMD_ELIMINAR_RANGO_FECHAS_GENERALES = "CMD_ELIMINAR_RANGO_FECHAS_GENERALES";
    public final static String CMD_ELIMINAR_RANGOS_FECHAS_GENERALES = "CMD_ELIMINAR_RANGOS_FECHAS_GENERALES";
    //Comandos para rangos: Asistencias graduadas
    public final static String CMD_INICIALIZAR_RANGO_FECHAS_CALIFICAR_ASISTENCIAS_GRADUADAS = "CMD_INICIALIZAR_RANGO_FECHAS_CALIFICAR_ASISTENCIAS_GRADUADAS";
    public final static String CMD_FINALIZAR_RANGO_FECHAS_CALIFICAR_ASISTENCIAS_GRADUADAS = "CMD_FINALIZAR_RANGO_FECHAS_CALIFICAR_ASISTENCIAS_GRADUADAS";
    //Comandos par arangos: Planeación académica
    public final static String CMD_CREAR_TAREAS_SUBIR_PROGRAMA_PLANEACION_ACADEMICA = "CMD_CREAR_TAREAS_SUBIR_PROGRAMA_PLANEACION_ACADEMICA";
    public final static String CMD_CREAR_TAREAS_SUBIR_NOTAS_TREINTAP_PLANEACION_ACADEMICA = "CMD_CREAR_TAREAS_SUBIR_NOTAS_TREINTAP_PLANEACION_ACADEMICA";
    public final static String CMD_CREAR_TAREAS_SUBIR_ARCHIVO_CIERRE_PLANEACION_ACADEMICA = "CMD_CREAR_TAREAS_SUBIR_ARCHIVO_CIERRE_PLANEACION_ACADEMICA";
    public final static String CMD_COMPORTAMIENTO_FINRANGOFECHAS_SUBIR_PROGRAMA = "CMD_COMPORTAMIENTO_FINRANGOFECHAS_SUBIR_PROGRAMA";
    public final static String CMD_COMPORTAMIENTO_FIN_RANGOFECHAS_TAREAS_30PORCIENTO = "CMD_COMPORTAMIENTO_FIN_RANGOFECHAS_TAREAS_30PORCIENTO";
    public final static String CMD_COMPORTAMIENTO_FIN_RANGOFECHAS_TAREAS_CIERRE_CURSOS = "CMD_COMPORTAMIENTO_FIN_RANGOFECHAS_TAREAS_CIERRE_CURSOS";
    public final static String CMD_CONSULTAR_RUTA_ARCHIVO = "CMD_CONSULTAR_RUTA_ARCHIVO";
    public final static String VAL_RUTA_ARCHIVO_PLANEACION_ACADEMICA = "VAL_RUTA_ARCHIVO_PLANEACION_ACADEMICA";
    public final static String VAL_PLANEACION_ACADEMICA_TIPO_ARCHIVO_PROGRAMA = "VAL_PLANEACION_ACADEMICA_TIPO_ARCHIVO_PROGRAMA";
    public final static String VAL_PLANEACION_ACADEMICA_TIPO_ARCHIVO_TREINTA_PORCIENTO = "VAL_PLANEACION_ACADEMICA_TIPO_ARCHIVO_TREINTA_PORCIENTO";
    public final static String VAL_PLANEACION_ACADEMICA_TIPO_ARCHIVO_CIERRE = "VAL_PLANEACION_ACADEMICA_TIPO_ARCHIVO_CIERRE";
    public final static String VAL_PLANEACION_ACADEMICA_CARPETA_ARCHIVO_PROGRAMA = "VAL_PLANEACION_ACADEMICA_CARPETA_ARCHIVO_PROGRAMA";
    public final static String VAL_PLANEACION_ACADEMICA_CARPETA_ARCHIVO_TREINTA_PORCIENTO = "VAL_PLANEACION_ACADEMICA_CARPETA_ARCHIVO_TREINTA_PORCIENTO";
    public final static String VAL_PLANEACION_ACADEMICA_CARPETA_ARCHIVO_CIERRE = "VAL_PLANEACION_ACADEMICA_CARPETA_ARCHIVO_CIERRE";
    public final static String CMD_DAR_RUTA_ARCHIVO_POR_CRN_Y_TIPO = "CMD_DAR_RUTA_ARCHIVO_POR_CRN_Y_TIPO";
    public final static String VAL_PLANEACION_ACADEMICA_CARPETA_ARCHIVO_POR_DEFECTO = "VAL_PLANEACION_ACADEMICA_CARPETA_ARCHIVO_POR_DEFECTO";
    public final static String CMD_DAR_PERIODOS_PLANEACION_ACADEMICA = "CMD_DAR_PERIODOS_PLANEACION_ACADEMICA";
    // ReservaInventario
    public final static String TAG_PARAM_HORARIOS_LABORATORIO = "TAG_PARAM_HORARIOS_LABORATORIO";
    public final static String TAG_PARAM_RESERVABLE = "TAG_PARAM_RESERVABLE";
    public final static String TAG_PARAM_ELEMENTO = "TAG_PARAM_ELEMENTO";
    public final static String TAG_PARAM_ID_ELEMENTO = "TAG_PARAM_ID_ELEMENTO";
    public final static String TAG_PARAM_ELEMENTOS = "TAG_PARAM_ELEMENTOS";
    public final static String TAG_PARAM_NOMBRE_LABORATORIO = "TAG_PARAM_NOMBRE_LABORATORIO";
    public final static String TAG_PARAM_ID_LABORATORIO = "TAG_PARAM_ID_LABORATORIO";
    public final static String TAG_PARAM_DIA_RESERVA = "TAG_PARAM_DIA_RESERVA";
    public final static String TAG_PARAM_NUMERO_DIA_RESERVA = "TAG_PARAM_NUMERO_DIA_RESERVA";
    public final static String TAG_PARAM_NOMBRE_DIA_RESERVA = "TAG_PARAM_NOMBRE_DIA_RESERVA";
    public final static String TAG_PARAM_RESERVAS_INVENTARIO = "TAG_PARAM_RESERVAS_INVENTARIO";
    public final static String TAG_PARAM_RESERVA_INVENTARIO = "TAG_PARAM_RESERVA_INVENTARIO";
    public final static String TAG_PARAM_ESTADO_RESERVA = "TAG_PARAM_ESTADO_RESERVA";
    public final static String TAG_PARAM_RESPONSABLE_RESERVA = "TAG_PARAM_RESPONSABLE_RESERVA";
    public final static String TAG_PARAM_ID_RESERVA_INVENTARIO = "TAG_PARAM_ID_RESERVA_INVENTARIO";
    public final static String TAG_PARAM_AUTORIZADO = "TAG_PARAM_AUTORIZADO";
    public final static String TAG_PARAM_CREADOR = "TAG_PARAM_CREADOR";
    public final static String TAG_PARAM_PERSONA_RESERVA_INVENTARIO = "TAG_PARAM_PERSONA_RESERVA_INVENTARIO";
    public final static String TAG_PARAM_ENCARGADOS = "TAG_PARAM_ENCARGADOS";
    public final static String TAG_PARAM_ADMINISTRADORES = "TAG_PARAM_ADMINISTRADORES";
    public final static String TAG_PARAM_AUTORIZADOS = "TAG_PARAM_AUTORIZADOS";
    public final static String VAL_ESTADO_RESERVA_INVENTARIO_VIGENTE = "VAL_ESTADO_RESERVA_INVENTARIO_VIGENTE";
    public final static String VAL_ESTADO_RESERVA_INVENTARIO_CUMPLIDA = "VAL_ESTADO_RESERVA_INVENTARIO_CUMPLIDA";
    public final static String VAL_ESTADO_RESERVA_INVENTARIO_CANCELADA = "VAL_ESTADO_RESERVA_INVENTARIO_CANCELADA";
    public final static String CMD_CONSULTAR_RESERVAS_LABORATORIO = "CMD_CONSULTAR_RESERVAS_LABORATORIO";
    public final static String CMD_CONSULTAR_LABORATORIO = "CMD_CONSULTAR_LABORATORIO";
    public final static String CMD_CONSULTAR_LABORATORIOS_AUTORIZADOS = "CMD_CONSULTAR_LABORATORIOS_AUTORIZADOS";
    public final static String CMD_CONSULTAR_AUTORIZADO_LABORATORIO = "CMD_CONSULTAR_AUTORIZADO_LABORATORIO";
    public final static String CMD_CREAR_RESERVA_LABORATORIO = "CMD_CREAR_RESERVA_LABORATORIO";
    public final static String CMD_CONSULTAR_LABORATORIOS = "CMD_CONSULTAR_LABORATORIOS";
    public final static String TAG_PARAM_ACTIVIDADES_AUDITORIA = "TAG_PARAM_ACTIVIDADES_AUDITORIA";
    public final static String TAG_PARAM_ACTIVIDAD_AUDITORIA = "TAG_PARAM_ACTIVIDAD_AUDITORIA";
    public final static String TAG_PARAM_COMANDO_AUDITORIA = "TAG_PARAM_COMANDO_AUDITORIA";
    public final static String TAG_PARAM_ROL_AUDITORIA = "TAG_PARAM_ROL_AUDITORIA";
    public final static String TAG_PARAM_ACCION_EXITOSA_AUDITORIA = "TAG_PARAM_ACCION_EXITOSA_AUDITORIA";
    public final static String TAG_PARAM_PARAMETROS_AUDITORIA = "TAG_PARAM_PARAMETROS_AUDITORIA";
    public final static String CMD_CONSULTAR_AUDITORIA_USUARIO_ACTIVIDAD = "CMD_CONSULTAR_AUDITORIA_USUARIO_ACTIVIDAD";
    public final static String CMD_DAR_HORARIO_DISPONIBLE_LABORATORIO = "CMD_DAR_HORARIO_DISPONIBLE_LABORATORIO";
    public final static String TAG_PARAM_INTERVALO_DISPONIBLE = "TAG_PARAM_INTERVALO_DISPONIBLE";
    public final static String TAG_PARAM_INTERVALOS_DISPONIBLES = "TAG_PARAM_INTERVALOS_DISPONIBLES";
    public final static String TAG_PARAM_HORA_RESERVA = "TAG_PARAM_HORA_RESERVA";
    public final static String CMD_CONSULTAR_RESERVAS_PERSONA = "CMD_CONSULTAR_RESERVAS_PERSONA";
    public final static String CMD_CANCELAR_RESERVA_INVENTARIO = "CMD_CANCELAR_RESERVA_INVENTARIO";
    public final static String CMD_CONSULTAR_RESERVA_INVENTARIO = "CMD_CONSULTAR_RESERVA_INVENTARIO";
    public final static String TAG_PARAM_CONDICION_FILTRO_CORREO = "TAG_PARAM_CONDICION_FILTRO_CORREO";
    public final static String TAG_PARAM_CONDICIONES_FILTRO_CORREO = "TAG_PARAM_CONDICIONES_FILTRO_CORREO";
    public final static String CMD_EDITAR_LABORATORIO = "CMD_EDITAR_LABORATORIO";
    public final static String CMD_CONSULTAR_RANGO_RESERVAS_LABORATORIO = "CMD_CONSULTAR_RANGO_RESERVAS_LABORATORIO";
    public final static String CMD_CANCELAR_GRUPO_RESERVAS_LABORATORIO = "CMD_CANCELAR_GRUPO_RESERVAS_LABORATORIO";
    public final static String CMD_CONSULTAR_LABORATORIOS_ENCARGADO = "CMD_CONSULTAR_LABORATORIOS_ENCARGADO";
    public final static String CMD_DAR_HORARIO_OCUPADO_LABORATORIO = "CMD_DAR_HORARIO_OCUPADO_LABORATORIO";
    public final static String VAL_RESERVA_INVENTARIO_TIMER_CUENTAS_USUARIO = "VAL_RESERVA_INVENTARIO_TIMER_CUENTAS_USUARIO";
    public final static String VAL_RESERVA_INVENTARIO_TIMER_COMPLETAR_RESERVAS = "VAL_RESERVA_INVENTARIO_TIMER_COMPLETAR_RESERVAS";
    public final static String VAL_RESERVA_INVENTARIO_TIMER_RECORDAR_RESERVA = "VAL_RESERVA_INVENTARIO_TIMER_RECORDAR_RESERVA";
    public final static String VAL_RESERVA_INVENTARIO_TIMER_RESERVAS_DEL_DIA = "VAL_RESERVA_INVENTARIO_TIMER_RESERVAS_DEL_DIA";
    public final static String TAG_PARAM_ENVIAR_RECORDATORIO = "TAG_PARAM_ENVIAR_RECORDATORIO";
    public final static String CMD_MARCAR_GRUPO_RESERVAS_LABORATORIO = "CMD_MARCAR_GRUPO_RESERVAS_LABORATORIO";
    public final static String VAL_ESTADO_RESERVA_INVENTARIO_INCUMPLIDA = "VAL_ESTADO_RESERVA_INVENTARIO_INCUMPLIDA";
    public final static String TAG_PARAM_CUENTA_INVITADO = "TAG_PARAM_CUENTA_INVITADO";
    public final static String TAG_PARAM_NOMBRE_SALA_SERVICIO = "TAG_PARAM_NOMBRE_SALA_SERVICIO";
    public final static String CMD_ELIMINAR_LABORATORIO = "CMD_ELIMINAR_LABORATORIO";
    public final static String TAG_PARAM_RESERVA_TERCEROS = "TAG_PARAM_RESERVA_TERCEROS";
    public final static String CMD_DAR_ACCIONES = "CMD_DAR_ACCIONES";
    public final static String TAG_PARAM_NOMBRE_ACCION = "TAG_PARAM_NOMBRE_ACCION";
    public final static String TAG_PARAM_DESCRIPCION_ACCION = "TAG_PARAM_DESCRIPCION_ACCION";
    public final static String TAG_PARAM_COMANDO_ACCION = "TAG_PARAM_COMANDO_ACCION";
    public final static String TAG_PARAM_SECCION_ACCION = "TAG_PARAM_SECCION_ACCION";
    public final static String TAG_PARAM_ACCIONES = "TAG_PARAM_ACCIONES";
    public final static String VAL_NOMBRE_METODO_DAR_ACCIONES = "VAL_NOMBRE_METODO_DAR_ACCIONES";
    public final static String VAL_ACCION_COMANDO_CREAR_LABORATORIO = "VAL_ACCION_COMANDO_CREAR_LABORATORIO";
    public final static String VAL_ACCION_SECCION_RESERVA_INVENTARIO = "VAL_ACCION_SECCION_RESERVA_INVENTARIO";
    public final static String VAL_ACCION_SECCION_TESIS_MAESTRIA = "VAL_ACCION_SECCION_TESIS_MAESTRIA";
    public final static String VAL_ACCION_SECCION_TESIS_PREGRADO = "VAL_ACCION_SECCION_TESIS_PREGRADO";

    public final static String VAL_ACCION_ESTUDIANTE_ENVIAR_SOLICITUD_INSCRIPCION_TESIS1 = "VAL_ACCION_ESTUDIANTE_ENVIAR_SOLICITUD_INSCRIPCION_TESIS1";
    public final static String VAL_ACCION_ESTUDIANTE_VER_ESTADO_SOLICITUD_TESIS1 = "VAL_ACCION_ESTUDIANTE_VER_ESTADO_SOLICITUD_TESIS1";
    public final static String VAL_ACCION_ESTUDIANTE_ENVIAR_SOLICITUD_INSCRIPCION_TESIS2 = "VAL_ACCION_ESTUDIANTE_ENVIAR_SOLICITUD_INSCRIPCION_TESIS2";
    public final static String VAL_ACCION_ESTUDIANTE_VER_ESTADO_SOLICITUD_TESIS2 = "VAL_ACCION_ESTUDIANTE_VER_ESTADO_SOLICITUD_TESIS2";

    public final static String VAL_ACCION_ESTUDIANTE_CONSULTAR_TEMAS_TESIS = "VAL_ACCION_ESTUDIANTE_CONSULTAR_TEMAS_TESIS";
    public final static String VAL_ACCION_ESTUDIANTE_CONSULTAR_FECHAS_TESIS = "VAL_ACCION_ESTUDIANTE_CONSULTAR_FECHAS_DE_TESIS";
    public final static String VAL_ACCION_ESTUDIANTE_ENVIAR_SOLICITUD_INSCRIPCION_SUBAREA = "VAL_ACCION_ESTUDIANTE_ENVIAR_SOLICITUD_INSCRIPCION_SUBAREA";
    public final static String VAL_ACCION_ESTUDIANTE_VER_ESTADO_DE_SOLICITUD_INSCRIPCION_SUBAREA = "VAL_ACCION_ESTUDIANTE_VER_ESTADO_DE_SOLICITUD_INSCRIPCION_SUBAREA";
    public final static String VAL_ACCION_ESTUDIANTE_VER_HORARIOS_DE_SUSTENTACION_TESIS2 = "VAL_ACCION_ESTUDIANTE_VER_HORARIOS_DE_SUSTENTACION_TESIS2";

    public final static String VAL_ACCION_ESTUDIANTE_CONSULTAR_TEMAS_TESIS_PREGRADO = "VAL_ACCION_ESTUDIANTE_CONSULTAR_TEMAS_TESIS_PREGRADO";
    public final static String VAL_ACCION_ENVIAR_SOLICITUD_INSCRIPCION_TESIS_PREGRADO = "VAL_ACCION_ENVIAR_SOLICITUD_INSCRIPCION_TESIS_PREGRADO";
    public final static String VAL_ACCION_VER_ESTADO_SOLICITUD_INSCRIPCION_TESIS_PREGRADO = "VAL_ACCION_VER_ESTADO_SOLICITUD_INSCRIPCION_TESIS_PREGRADO";
    public final static String VAL_ACCION_CONSULTAR_FECHA_TESIS_PREGRADO = "VAL_ACCION_CONSULTAR_FECHA_TESIS_PREGRADO";
    
    public final static String VAL_ACCION_COMANDO_MARCAR_RESERVAS = "VAL_ACCION_COMANDO_MARCAR_RESERVAS";
    public final static String VAL_ACCION_COMANDO_CANCELAR_RESERVAS = "VAL_ACCION_COMANDO_CANCELAR_RESERVAS";
    public final static String VAL_ACCION_COMANDO_CONSULTAR_MIS_RESERVAS = "VAL_ACCION_COMANDO_CONSULTAR_MIS_RESERVAS";
    public final static String VAL_ACCION_COMANDO_CONSULTAR_HORARIO_RESERVA_INVENTARIO = "VAL_ACCION_COMANDO_CONSULTAR_HORARIO_RESERVA_INVENTARIO";
    public final static String VAL_ACCION_COMANDO_CONSULTAR_LABORATORIOS = "VAL_ACCION_COMANDO_CONSULTAR_LABORATORIOS";
    public final static String TAG_PARAM_FORMATO_REPORTE = "TAG_PARAM_FORMATO_REPORTE";
    public final static String VAL_RESERVA_INVENTARIO_TIMER_NOTIFICAR_ADMONSIS = "VAL_RESERVA_INVENTARIO_TIMER_NOTIFICAR_ADMONSIS";
    public final static String CMD_COORDINACION_CONSULTAR_SOLICITUDES_INSCRIPCION_TESIS1 = "CMD_COORDINACION_CONSULTAR_SOLICITUDES_INSCRIPCION_TESIS1";
    public final static String TAG_PARAM_EVENTO_EXTERNO = "TAG_PARAM_EVENTO_EXTERNO";
    public final static String TAG_PARAM_EVENTOS_EXTERNOS = "TAG_PARAM_EVENTOS_EXTERNOS";
    public final static String TAG_PARAM_ID_EVENTO_EXTERNO = "TAG_PARAM_ID_EVENTO_EXTERNO";
    public final static String TAG_PARAM_RUTA_IMAGEN = "TAG_PARAM_RUTA_IMAGEN";
    public final static String TAG_PARAM_CUPO = "TAG_PARAM_CUPO";
    public final static String TAG_PARAM_FECHA_HORA = "TAG_PARAM_FECHA_HORA";
    public final static String TAG_PARAM_PUBLICADO = "TAG_PARAM_PUBLICADO";
    public final static String TAG_PARAM_ABIERTO = "TAG_PARAM_ABIERTO";
    public final static String TAG_PARAM_PREGUNTAS = "TAG_PARAM_PREGUNTAS";
    public final static String TAG_PARAM_PREGUNTA = "TAG_PARAM_PREGUNTA";
    public final static String TAG_PARAM_CAMPOS_ADICIONALES = "TAG_PARAM_CAMPOS_ADICIONALES";
    public final static String TAG_PARAM_CAMPO_ADICIONAL = "TAG_PARAM_CAMPO_ADICIONAL";
    public final static String TAG_PARAM_ID_PREGUNTA = "TAG_PARAM_ID_PREGUNTA";
    public final static String TAG_PARAM_VALOR_PREGUNTA = "TAG_PARAM_VALOR_PREGUNTA";
    public final static String TAG_PARAM_ID_CAMPO_ADICIONAL = "TAG_PARAM_ID_CAMPO_ADICIONAL";
    public final static String TAG_PARAM_VALOR_CAMPO_ADICIONAL = "TAG_PARAM_VALOR_CAMPO_ADICIONAL";
    public final static String TAG_PARAM_TIPO_CAMPO = "TAG_PARAM_TIPO_CAMPO";
    public final static String TAG_PARAM_TIPOS_CAMPO = "TAG_PARAM_TIPOS_CAMPO";
    public final static String TAG_PARAM_NOMBRE_TIPO_CAMPO = "TAG_PARAM_NOMBRE_TIPO_CAMPO";
    public final static String TAG_PARAM_CAMPO_OBLIGATORIO = "TAG_PARAM_CAMPO_OBLIGATORIO";
    public final static String TAG_PARAM_ID_TIPO_CAMPO = "TAG_PARAM_ID_TIPO_CAMPO";
    public final static String TAG_PARAM_CATEGORIA_EVENTO_EXTERNO = "TAG_PARAM_CATEGORIA_EVENTO_EXTERNO";
    public final static String TAG_PARAM_CATEGORIAS_EVENTO_EXTERNO = "TAG_PARAM_CATEGORIAS_EVENTO_EXTERNO";
    public final static String TAG_PARAM_ID_CATEGORIA = "TAG_PARAM_ID_CATEGORIA";
    public final static String VAL_LINK_INSCRIPCION_EVENTO = "VAL_LINK_INSCRIPCION_EVENTO";
    public final static String VAL_LINK_REGISTRO_USUARIO = "VAL_LINK_REGISTRO_USUARIO";
    public final static String VAL_LINK_OLVIDO_CONTRASENIA = "VAL_LINK_OLVIDO_CONTRASENIA";
    public final static String TAG_PARAM_LINK_INSCRIPCION_EVENTO = "TAG_PARAM_LINK_INSCRIPCION_EVENTO";
    public final static String VAL_ESTADO_EVENTO_ABIERTO = "VAL_ESTADO_EVENTO_ABIERTO";
    public final static String VAL_ESTADO_EVENTO_CERRADO = "VAL_ESTADO_EVENTO_CERRADO";
    public final static String TAG_PARAM_NUMERO_INSCRITOS_EVENTO_EXTERNO = "TAG_PARAM_NUMERO_INSCRITOS_EVENTO_EXTERNO";
    public final static String TAG_PARAM_CONTACTOS = "TAG_PARAM_CONTACTOS";
    public final static String TAG_PARAM_FECHA_LIMITE_INSCRIPCIONES = "TAG_PARAM_FECHA_LIMITE_INSCRIPCIONES";
    public final static String TAG_PARAM_RESPUESTAS = "TAG_PARAM_RESPUESTAS";
    public final static String TAG_PARAM_ID_RESPUESTA = "TAG_PARAM_ID_RESPUESTA";
    public final static String TAG_PARAM_VALOR_RESPUESTA = "TAG_PARAM_VALOR_RESPUESTA";
    public final static String TAG_PARAM_EVENTO_INSCRIPCIONES = "TAG_PARAM_EVENTO_INSCRIPCIONES";
    public final static String TAG_PARAM_EVENTO_INSCRIPCION = "TAG_PARAM_EVENTO_INSCRIPCION";
    public final static String TAG_PARAM_EVENTO_INSCRIPCION_ID = "TAG_PARAM_EVENTO_INSCRIPCION_ID";

    //COMANDOS
    public final static String CMD_EDITAR_EVENTO_EXTERNO = "CMD_EDITAR_EVENTO_EXTERNO";
    public final static String CMD_CONSULTAR_EVENTO_EXTERNO = "CMD_CONSULTAR_EVENTO_EXTERNO";
    public final static String CMD_CONSULTAR_EVENTOS_EXTERNOS = "CMD_CONSULTAR_EVENTOS_EXTERNOS";
    public final static String CMD_CONSULTAR_CATEGORIAS_EVENTO_EXTERNO = "CMD_CONSULTAR_CATEGORIAS_EVENTO_EXTERNO";
    public final static String CMD_CONSULTAR_INSCRITOS = "CMD_CONSULTAR_INSCRITOS";
    public final static String CMD_CONSULTAR_CAMPOS_ADICIONALES_EVENTO_EXTERNO = "CMD_CONSULTAR_CAMPOS_ADICIONALES_EVENTO_EXTERNO";
    public final static String CMD_CONSULTAR_TIPOS_CAMPO_ADICIONAL_EVENTO_EXTERNO = "CMD_CONSULTAR_TIPOS_CAMPO_ADICIONAL_EVENTO_EXTERNO";
    public final static String CMD_GUARDAR_TIPOS_CAMPO_ADICIONAL_EVENTO_EXTERNO = "CMD_GUARDAR_TIPOS_CAMPO_ADICIONAL_EVENTO_EXTERNO";
    public final static String CMD_ELIMINAR_CATEGORIA_EVENTO_EXTERNO = "CMD_ELIMINAR_CATEGORIA_EVENTO_EXTERNO";
    public final static String CMD_ELIMINAR_TIPO_CAMPO_EVENTO_EXTERNO = "CMD_ELIMINAR_TIPO_CAMPO_EVENTO_EXTERNO";
    public final static String CMD_INSCRIBIR_USUARIOS_VIP = "CMD_INSCRIBIR_USUARIOS_VIP";
    public final static String CMD_CONSULTAR_DEPARTAMENTOS = "CMD_CONSULTAR_DEPARTAMENTOS";
    public final static String CMD_CAMBIAR_ESTADO_EVENTO_EXTERNO = "CMD_CAMBIAR_ESTADO_EVENTO_EXTERNO";
    public final static String CMD_CONSULTAR_CONTACTOS_LIGHT = "CMD_CONSULTAR_CONTACTOS_LIGHT";
    public final static String CMD_CONSULTAR_CIUDADES_POR_DEPARTAMENTO = "CMD_CONSULTAR_CIUDADES_POR_DEPARTAMENTO ";
    public final static String TAG_PARAM_CIUDADES = "TAG_PARAM_CIUDADES";
    public final static String RUTA_ARCHIVO_TERMINOS_Y_CONDICIONES = "RUTA_ARCHIVO_TERMINOS_Y_CONDICIONES";
    public final static String CMD_SUBIR_IMAGEN_EVENTO_EXTERNO = "CMD_SUBIR_IMAGEN_EVENTO_EXTERNO ";
    public final static String RUTA_IMAGEN_EVENTO_EXTERNO = "RUTA_IMAGEN_EVENTO_EXTERNO";
    public final static String CORREO_REVISTA_PARADIGMA = "CORREO_REVISTA_PARADIGMA";
    public final static String RUTA_IMAGEN_EVENTO_EXTERNO_TEMPORAL = "RUTA_IMAGEN_EVENTO_EXTERNO_TEMPORAL";
    public final static String VAL_AUTENTICAR_USUARIOS_EXTERNOS_CON_LDAP = "VAL_AUTENTICAR_USUARIOS_EXTERNOS_CON_LDAP";
    public static String CMD_CERRAR_EVENTOS_CON_FECHAS_ANTERIORES_A_HOY="CMD_CERRAR_EVENTOS_CON_FECHAS_ANTERIORES_A_HOY";
    
}
