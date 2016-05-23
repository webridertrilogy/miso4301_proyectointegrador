/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, Carlos Morales, Javier Bautista,
 *          Alejandro Osorio, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.comun.constantes;

public class Notificaciones {

    public final static String CUENTA_PREGRADO_ISIS = "sisinfo";
    public final static String DOMINIO_CUENTA_UNIANDES = "@uniandes.edu.co";
    public final static String CORREO_INICIO_CONVOCATORIA = "CORREO_INICIO_CONVOCATORIA";
    public final static String CORREO_ESTUDIANTES_PREG_SISTEMAS = "estudiantespregradoingsistemas";
    public final static String CORREO_PROFESORES_SISTEMAS = "profsistemas";
    public final static String CORREO_PROFESORES_CATEDRA_SISTEMA = "catprisis";
    public final static String CORREO_ESTUDIANTES_MAESTRIA_SISTEMAS = "estudiantesmaestriaingsistemas";
    public final static String ASUNTO_PROBLEMA_APERTURA_CONVOCATORIA = "[Sisinfo] Problema Apertura Convocatoria";
    public final static String MENSAJE_NO_HAY_PERIODO_ACTUAL = "Estimado(a) asistente, <br /><br /> Hubo un problema al intentar abrir la convocatoria. Por favor verifique que el periodo anterior fue cerrado correctamente y que la cartelera para este semestre ya ha sido cargada y vuelva a configurar la fecha de inicio del plazo de monitorias.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String MENSAJE_YA_EXISTE_CONVOCATORIA = "Estimado(a) asistente, <br /><br /> Ya existe una convocatoria para el periodo actual. No es posible abrir de nuevo la convocatoria. Por favor vuelva a configurar la fecha de inicio del plazo A.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String MENSAJE_NO_EXISTEN_CURSOS = "Estimado(a) asistente, <br /><br /> No existen cursos registrados en el sistema. Por favor cargue el archivo de la cartelera para el periodo actual y vuelva a configurar la fecha de inicio del plazo A.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String MENSAJE_PLAZOS_NO_CONFIGURADOS = "Estimado(a) asistente, <br /><br /> Los plazos del proceso de monitores no han sido establecidos. Por favor configure los plazos en Sisinfo y vuelva a configurar la fecha de inicio del plazo A.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String MENSAJE_ALERTAS_NO_CREADAS = "Estimado(a) asistente, <br /><br /> Las alertas del proceso de monitores no han sido creadas en su totalidad. Por favor verifique las alertas creadas y vuelva a configurar la fecha de inicio del plazo A.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_EXITO_APERTURA_CONVOCATORIA = "[Sisinfo] Exito Apertura Convocatoria";
    public final static String MENSAJE_EXITO_APERTURA_CONVOCATORIA = "Estimado(a) asistente, <br /><br /> La convocatoria fue abierta exitosamente<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_APERTURA_CONVOCATORIA = "[Sisinfo] Apertura convocatoria";
    public final static String MENSAJE_APERTURA_CONVOCATORIA_PROFESORES = "Estimados profesores,<br /><br />"
            + "La convocatoria para monitorías del Departamento de Ingeniería Sistemas y Computación para el periodo <b>%</b> ha sido abierta. En <a href=\"http://sisinfo.uniandes.edu.co\">Sisinfo</a> podrán preseleccionar sus respectivos monitores, una vez haya "
            + "aspirantes para las secciones de los cursos que ofrecen. Ustedes podrán hacer preselecciones y des-preselecciones desde hoy hasta el <b>%</b>. El sistema solicitará la confirmación de los estudiantes preseleccionados por correo electrónico. "
            + "Una vez acepten la monitoría, la solicitud será verificada por la Coordinación del Departamento. Si se cumple con los requisitos "
            + "se procederá a radicar el convenio. Usted podrá ponerse en contacto con el monitor una vez su solicitud haya sido verificada por la Coordinación. En caso que el estudiante no acepte la monitoría, se le informará para que realice una nueva preselección.<br /><br />"
            + "Cordialmente.<br />- Sisinfo";
    public final static String MENSAJE_APERTURA_CONVOCATORIA_ESTUDIANTES = "Estimados estudiantes,<br /><br />"
            + "La convocatoria para monitorías del Departamento de Ingeniería de Sistemas y Computación para el periodo <b>%</b> ha sido abierta. Invitamos a todos los estudiantes interesados a "
            + "ingresar sus solicitudes de monitorías hasta el día <b>%</b> en <a href=\"http://sisinfo.uniandes.edu.co\">Sisinfo</a>. Si usted "
            + "es seleccionado como monitor, recibirá un correo solicitándole confirmar su disponibilidad como monitor en Sisinfo. Si todos sus datos son válidos, se le pedirá "
            + "traer algunos documentos por correo electrónico. Luego usted deberá ponerse en contacto con el profesor del curso.<br /><br />"
            + "Cordialmente.<br />- Sisinfo";
    public final static String ASUNTO_RESOLUCION_CURSO = "[Sisinfo] Monitorías ofrecidas para el curso ";
    public final static String MENSAJE_RESOLUCION_CURSO = "Estimados estudiantes:<br /> El curso <b>%</b> necesita "
            + "monitores para las secciones <b>% </b>. Si está interesado favor aplicar en la página de Sistemas de"
            + "Información del Departamento de Sistemas. <br /> Cordial Saludo. <br /> -Sisinfo";
    public final static String ASUNTO_INFORMAR_PRESELECCION = "[Sisinfo] Selección monitoría %1$s";
    public final static String MENSAJE_INFORMAR_PRESELECCION = "Estimado(a) estudiante,<br /><br/ > Usted ha sido seleccionado para ser monitor de %1$s <br /><br />Por favor confirme ingresando a la página de Sisinfo accesible desde la página del Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a>. <br /> "
            + "Recuerde que tiene 3 días para confirmar, de lo contrario la selección será revertida.<br /><br /> Cordial Saludo. <br /> - Sisinfo";
    public final static String ASUNTO_CONFIRMACION = "[Sisinfo] Confirmar solicitud monitoría";
//    public final static String MENSAJE_CONFIRMACION_PROFESOR = "Estimado(a) profesor(a),<br /><br /> El monitor <b>%</b>"
//            + " ha aceptado ser monitor de <b>%</b>. Por favor confirme si acepta o no al monitor en la página de "
//            + "Sistemas de Información del Departamento de Sistemas. <br /><br /> Cordial Saludo, <br /> -Sisinfo";
    public final static String HEADER_CONFIRMACION_ASPIRANTE = "Estimado(a) estudiante,<br /><br /> Le recordamos que debe confirmar la solicitud de monitoría para:";
    public final static String FOOTER_CONFIRMACION_ASPIRANTE = "Por favor confirme ingresando a la página de Sisinfo accesible desde la página del Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a>. <br /> "
            + "Recuerde que tiene 3 días para confirmar, de lo contrario la selección será revertida.<br /><br /> Cordial Saludo. <br /> - Sisinfo";
    public final static String MENSAJE_CONFIRMACION_ASPIRANTE = "%1$s";
    public final static String ASUNTO_REVERSION_SOLICITUD = "[Sisinfo] Reversión Solicitud %";
    public final static String MENSAJE_REVERSION_POR_CAMBIO_PRESENCIALIDAD_CURSO_PROFESOR = "Estimado(a) %, <br /><br /> La preselección realizada"
            + " para el curso <b>%</b> del(de la) estudiante <b>%</b> ha sido revertida, debido a un cambio en los parámetros del curso. Por favor realice una nueva preselección"
            + " ingresando a la página de Sisinfo accesible desde la página del Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a>. <br /><br /> Cordialmente, <br /> - Sisinfo";
    public final static String MENSAJE_REVERSION_POR_CAMBIO_PRESENCIALIDAD_CURSO_ESTUDIANTE = "Estimado(a) %, <br /><br /> La solicitud de monitoría para el curso "
            + "<b>%</b> ha sido revertida debido a un cambio en los parámetros del curso. Su horario en el sistema entra en conflicto con la sección y su solicitud ha regresado al estado \"Aspirante\".<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String MENSAJE_REVERSION_SOLICITUD_PROFESOR_GENERICA = "Estimado(a) profesor(a),<br /><br /> La solicitud de monitoría"
            + " para <b>%</b> del(de la) estudiante <b>%</b> ha sido rechazada por <b>%</b> %. Por favor realice una nueva preselección ingresando a la página de"
            + " Sisinfo accesible desde la página del Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a>. <br /><br /> Cordial Saludo. <br /> - Sisinfo";
    public final static String MENSAJE_REVERSION_SOLICITUD_MOTIVO_CAMBIO_HORARIO = "debido a un cambio en el horario de la seccion";
    public final static String MENSAJE_REVERSION_SOLICITUD_MOTIVO_SECCION_ELIMINADA = "debido a que la seccion fue eliminada";
    public final static String MENSAJE_REVERSION_SOLICITUD_MOTIVO_AUTOMATICA = "debido a que el estudiante no confirmo dentro del plazo autorizado";
    public final static String MENSAJE_REVERSION_SOLICITUD_MOTIVO_VERIFICACION = "debido a que la verificación de datos no fue correcta";
    public final static String MENSAJE_REVERSION_SOLICITUD_ASPIRANTE = "Estimado(a) estudiante,<br /><br /> La solicitud de monitoría"
            + " para <b>%</b> ha sido rechazada. Su solicitud ha vuelto a quedar en estado \"Aspirante\". <br />Cordial Saludo. <br /> - Sisinfo";
    public final static String MENSAJE_REVERSION_SOLICITUD_CUPI2 = "Estimado(a) administrador de Cupi2, <br /><br /> La solicitud del estudiante <b>%</b> para % de <b>%</b> ha sido rechazada por <b>%</b> %. Por favor "
            + "entrar a Sisinfo y volver a preseleccionar a otro estudiante. <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String MENSAJE_REVERSION_SOLICITUD_CUPI2_UNA_SOLICITUD = "Estimado(a) administrador de Cupi2, <br /><br /> Hubo una reversión en la solicitud del estudiante <b>%</b> para las seccion <b>%</b> de <b>%</b>. Por favor "
            + "entrar a Sisinfo y volver a preseleccionar a otro estudiante. <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_RECHAZO_SOLICITUD_VERIFICACION_DATOS = "[Sisinfo] Rechazo de Solicitud";
    public final static String MENSAJE_RECHAZO_SOLICITUD_VERIFICACION_DATOS = "Estimado(a) %1,<br /> La solicitud de monitoría"
            + " para <b>%2</b> ha sido rechazada. <br /> Razones del rechazo:<br /> %3 <br />Cordial Saludo. <br /> - Sisinfo";
    public final static String MENSAJE_RECHAZO_Y_ELIMINAR_SOLICITUD_VERIFICACION_DATOS = "Estimado(a) %1,<br /> La solicitud de monitoría"
            + " para <b>%2</b> ha sido rechazada y eliminada. <br /> <br />Cordial Saludo. <br /> - Sisinfo";
    public final static String ASUNTO_CONTACTO = "[Sisinfo] Contacto";
    public final static String MENSAJE_CONTACTO = "Estimados profesor y monitor,<br /><br /> Este correo confirma la monitoría %1$s de <b>%2$s</b> en la sección <b>%3$s</b> del curso <b>%4$s</b> con el profesor <b>%5$s</b>.<br />"
            + "Por favor comuníquese con el profesor de inmediato si no lo ha hecho aún.<br /><br />"
            + "Cordial Saludo.<br /> - Sisinfo";
    public final static String ASUNTO_FIRMA = "[Sisinfo] Firma Convenio";
    public final static String MENSAJE_FIRMA = "Estimado(a) estudiante,<br /><br /> Por favor acercarse a la secretaría del Departamento de Sistemas, a la mayor brevedad, para firmar el convenio académico que lo acredita como monitor.<br /><br />Cordialmente<br />- Sisinfo";
    public final static String ASUNTO_ACTUALIZACION_DATOS = "[Sisinfo] Actualización de datos para verificación";
    public final static String HEADER_ACTUALIZACION_DATOS = "Buen día,<br /><br />Le informamos que debe verificar la información las siguientes solicitudes:";
    public final static String MENSAJE_ACTUALIZACION_DATOS = "Solicitud %1$d de %2$s, para %3$s";
    public final static String FOOTER_ACTUALIZACION_DATOS = "Por favor verifique estas solicitudes ingresando a la página de Sisinfo accesible desde la página del Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a>. <br /> "
            + "<br /><br /> Cordial Saludo. <br /> - Sisinfo";
    public final static String ASUNTO_PAPELES_CONVENIO = "[Sisinfo] Documentos convenio monitoría  ";
    public final static String MENSAJE_PAPELES = "Estimado(a) %1$s,<br /><br />Le informamos que le fue aprobada su monitoria para el curso:<br /><b>%2$s</b><br />"
            + "Tenga en cuenta que debe contactar al profesor de la materia para comenzar de una vez las actividades relacionadas con la monitoria.<br /><br />"
            + "Para la firma del contrato <b>TODOS</b> los monitores, antiguos y nuevos, deben acercarse a la secretaría del Departamento de Ingeniería de Sistemas y Computación para entregar los siguientes documentos:<br />"
            + "<ol>"
            + "<li>Fotocopia de la cédula de ciudadanía ó tarjeta de identidad (ampliada a 150 %% )</li>"
            + "<li>Certificado de existencia bancaria. Para este certificado debe tener en cuenta que:"
            + "<ul>"
            + "<li>El estudiante debe ser el único titular de la cuenta (no se aceptan cuentas compartidas).</li>"
            + "<li>El <b>número de identificación <u>actual</u></b> del(de la) estudiante debe coincidir con el que tiene registrado en la entidad bancaria.</li>"
            + "<li>La cuenta debe encontrarse en estado activo.</li>"
            + "<li>En el certificado debe estar el tipo y número de cuenta.</li>"
            + "</ul>"
            + "</li>"
            + "</ol>"
            + "Una vez presentados los documentos, debe presentarse tres días hábiles después en la Secretaría del Departamento de Sistemas y Computación para firmar su contrato.<br /><br />"
            + "Cordialmente,<br />"
            + "- Sisinfo";
    public final static String MENSAJE_TAREA_PAPELES = "Buen día,<br /><br />"
            + "Le recordamos que debe acercarse a la secretaría del Departamento de Ingeniería de Sistemas y Computación con la mayor brevedad para hacer entrega de los siguientes documentos:<br /><br />"
            + "<ol>"
            + "<li>Fotocopia de la cédula de ciudadanía ó tarjeta de identidad (ampliada a 150 %% )</li>"
            + "<li>Certificado de existencia bancaria. Para este certificado debe tener en cuenta que:"
            + "<ul>"
            + "<li>El estudiante debe ser el único titular de la cuenta (no se aceptan cuentas compartidas).</li>"
            + "<li>El <b>número de identificación <u>actual</u></b> del(de la) estudiante debe coincidir con el que tiene registrado en la entidad bancaria.</li>"
            + "<li>La cuenta debe encontrarse en estado activo.</li>"
            + "<li>En el certificado debe estar el tipo y número de cuenta.</li>"
            + "</ul>"
            + "</li>"
            + "</ol>"
            + "Si usted ya realizó la entrega de los documentos, por favor recuerde presentarse 3 días hábiles después para la firma del contrato.<br /><br />"
            + "Cordialmente,<br />"
            + "- Sisinfo";;
    public final static String ASUNTO_VOTACION_CREADA = "[Sisinfo] Nueva votación";
    public final static String MENSAJE_VOTACION_CREADA = "Estimado(a) %,<br />"
            + "Se ha abierto una nueva votación en la cual usted puede participar: <br />"
            + "<b>%</b> <br />"
            + "<b>%</b> <br />"
            + "La votación se cerrará el <b>%</b>. <br />"
            + "Por favor ingrese a la página de Sisinfo accesible desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para votar por algún candidato.<br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_VOTAR = "[Sisinfo] Votaciones";
    public final static String HEADER_VOTAR = "Estimado(a) %1$s,<br><br> Le recordamos que aun tiene las siguientes votaciones pendientes:<br>";
    public final static String MENSAJE_VOTAR = "%1s - Disponible hasta %2$s";
    public final static String FOOTER_VOTAR = "Para poder elegir a un candidato, por favor acceda desde la sección de Tareas o la lista de votaciones en la pagina de Sisinfo.<br><br>Cordialmente Sisinfo";
    //public final static String MENSAJE_VOTAR = "Estimado(a) %,\n\n Le recordamos que tiene una votación pendiente (%),"
    //        + "la cual estará disponible hasta el %. Para votar por algún candidato, por favor ir a la sección de Tareas"
    //        + "de la página de Sisinfo.\n\nCordialmente,\n- Sisinfo";
    public final static String ASUNTO_NUEVO_INSCRITO = "[Sisinfo] Inscripciones";
    public final static String MENSAJE_NUEVO_INSCRITO = "Buen día,<br /> Le informamos que <b>% %</b> con correo: <b>%</b> "
            + ", ha confirmado su inscripción a <b>%</b>"
            + ".<br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_INSCRIBIRSE = "[Sisinfo] Inscripciones";
    public final static String FOOTER_INSCRIBIRSE = "Si esta interesado, puede confirmar su asistencia a través de este correo o ingresando a Sisinfo desde  <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para confirmar su asistencia.<br />Cordialmente,<br />- Sisinfo";
    public final static String HEADER_INSCRIBIRSE = "Estimado(a) %1$s,<br /> "
            + "Le recordamos que aun no ha confirmado su asistencia a las siguientes inscripciones:";
    public final static String MENSAJE_COMPLETO_INSCRIBIRSE = "Buen día,<br /> Le recordamos que tiene una inscripción pendiente: <br />"
            + "Nombre: %1$s <br />"
            + "Descripción: %2$s <br />"
            + "<br />La Inscripción se cerrará el %3$s. <br />"
            + "Por favor ingrese a la página de Sisinfo accesible desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para confirmar su participación.<br /><br />"
            + "<b>O confirme su asistencia a través de este correo</b><br />"
            + "<p style=\"border-style:solid;border-width:1px;border-color:gray;padding:10px 0px 10px 10px;width:300px;\"><b>¿Asistirá a este evento?</b><br />"
            + "&nbsp;&nbsp;&nbsp;<a href=\"%4$s\" target=\"_blank\">Sí</a>&nbsp;&nbsp;&nbsp;&nbsp;"
            + "<a href=\"%5$s\" target=\"_blank\">No</a></p><br />"
            + "<br />Cordialmente,<br />- Sisinfo";
    public final static String MENSAJE_INSCRIBIRSE_CORREO = "<b>%1$s</b> : Confirmar Asistencia: <a href=\"%2$s\" target=\"_blank\">Sí</a>&nbsp;&nbsp;/&nbsp;&nbsp;"
            + "<a href=\"%3$s\" target=\"_blank\">No</a> <br />Esta inscripción caduca en: %4$s";
    public final static String MENSAJE_INSCRIBIRSE = "<b>%1$s</b><br />Esta inscripción caduca en: %2$s";
    public final static String ASUNTO_INSCRIPCION_CREADA = "[Sisinfo] Nueva Inscripción";
    public final static String MENSAJE_INSCRIPCION_CREADA = "<style>p.estiloCajaConfirm{border-style:solid;border-width:1px;border-color:gray;padding:10px 0px 10px 10px;width:300px;}</style>"
            + "Estimado(a) %1$s,<br /><br />"
            + "Se ha abierto una nueva inscripción en la cual usted puede participar: <br />"
            + "<b>%2$s</b> <br />"
            + "Descripción: <i>%3$s</i> <br />"
            + "<br />La Inscripción se cerrará el %4$s. <br />"
            + "Por favor ingrese a la página de Sisinfo accesible desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para confirmar su participación.<br /><br />"
            + "<b>O confirme su asistencia a través de este correo</b><br />"
            + "<p style=\"border-style:solid;border-width:1px;border-color:gray;padding:10px 0px 10px 10px;width:300px;\"><b>¿Asistirá a este evento?</b><br />"
            + "&nbsp;&nbsp;&nbsp;<a href=\"%5$s\" target=\"_blank\">Sí</a>&nbsp;&nbsp;&nbsp;&nbsp;"
            + "<a href=\"%6$s\" target=\"_blank\">No</a></p><br />"
            + "<br />Cordialmente,<br />- Sisinfo";

    /**
     * MaterialBibliografico
     */
    public final static String ASUNTO_AUTORIZACION_COMPRA_MAT_BIB = "[Sisinfo] Autorización compra material bibliográfico";
    public final static String HEADER_AUTORIZACION_COMPRA_MAT_BIB = "Estimado(a) %1$s,<br /> Usted debe aprobar o rechazar las siguientes adquisiciones de material bibliografico <br />";
    public final static String FOOTER_AUTORIZACION_COMPRA_MAT_BIB = "<br />Por favor ingrese a la sección de tareas de su cuenta para ver en detalle esta(s) solicitudes."
            + "<br /><br />Cordialmente,<br />-Sisinfo";
    public final static String MENSAJE_COMPLETO_AUTORIZACION_COMPRA_MAT_BIB = "Estimado(a) %1$s ,<br />"
            + "El(La) profesor(a) %2$s ha solicitado la adquisición de material bibliográfico. <br />"
            + "<b>Título:</b> %3$s <br />"
            + "<b>Autor:</b> %4$s <br />"
            + "Por favor ingrese a la sección de tareas de su cuenta para ver en detalle esta solicitud."
            + "<br />Cordialmente,<br />- Sisinfo";
    public final static String MENSAJE_AUTORIZACION_COMPRA_MAT_BIB = "%1$s - %2$s solicitado por %3$s";
    public final static String ASUNTO_CONFIRMAR_COMPRA_MAT_BIB = "[Sisinfo] Compra material bibliográfico";
    public final static String HEADER_CONFIRMAR_COMPRA_MAT_BIB = "Estimado(a) %1$s,<br /> Usted debe confirmar la compra del siguiente material bibliografico";
    public final static String FOOTER_CONFIRMAR_COMPRA_MAT_BIB = "Por favor ingrese a la sección de tareas de su cuenta para ver en detalle esta(s) solicitudes."
            + "<br /><br />Cordialmente,<br />-Sisinfo";
    public final static String MENSAJE_CONFIRMAR_COMPRA_MAT_BIB = "%1$s - %2$s solicitado por %3$s";
    public final static String MENSAJE_COMPLETO_CONFIRMAR_COMPRA_MAT_BIB = "Estimado(a) Auxiliar Financiero,<br />"
            + "El(La) profesor(a) <b>%1$s</b> ha solicitado la adquisición de material bibliográfico. <br />"
            + "Título: %2$s <br />"
            + "Autor: %3$s <br />"
            + "La solicitud ya ha sido aprobada por el Director del Departamento. <br />"
            + "Por favor ingrese a la sección de tareas de su cuenta para ver en detalle esta solicitud y solicitar la compra del material."
            + "<br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_CONFIRMAR_LLEGO_BIBLIOTECA_MAT_BIB = "[Sisinfo] Adquisición material bibliográfico";
    public final static String HEADER_CONFIRMAR_LLEGO_BIBLIOTECA_MAT_BIB = "Estimado(a) %1$s,<br /> Usted debe confirmar que las siguientes solicitudes de material bibliografico llegaron a biblioteca:";
    public final static String FOOTER_CONFIRMAR_LLEGO_BIBLIOTECA_MAT_BIB = "Por favor ingrese a la sección de tareas de su cuenta para ver en detalle esta(s) solicitudes."
            + "<br /><br />Cordialmente,<br />-Sisinfo";
    public final static String MENSAJE_CONFIRMAR_LLEGO_BIBLIOTECA_MAT_BIB = "%1$s - %2$s solicitado por %3$s";
    public final static String ASUNTO_ULTIMOS_LIBROS_SOLICITADOS_BIBLIOTECA = "[Sisinfo] Últimas adquisiciones de Material Bibliográfico";
    public final static String MENSAJE_ULTIMOS_LIBROS_SOLICITADOS_BIBLIOTECA = "Estimados profesores,<br />"
            + "Los siguientes son los nuevos títulos que han sido solicitados a biblioteca:<br />"
            + "%<br />"
            + "Cordialmente, <br />-Sisinfo";
    public final static String ASUNTO_COMPRA_MAT_BIB = "[Sisinfo] Compra material bibliográfico";
    public final static String MENSAJE_COMPRA_MAT_BIB = "Estimado(a) Auxiliar Financiero,<br />"
            + "El(La) profesor(a) <b>%</b> ha solicitado la adquisición de material bibliográfico. <br />"
            + "Título: % <br />"
            + "Autor: % <br />"
            + "La solicitud ya ha sido aprobada por el Director del Departamento. <br />"
            + "Por favor ingrese a la sección de tareas de su cuenta para ver en detalle esta solicitud y solicitar la compra del material."
            + "<br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_RECHAZO_SOLICITUD_MAT_BIB = "[Sisinfo] Rechazo solicitud material bibliográfico";
    public final static String MENSAJE_RECHAZO_SOLICITUD_MAT_BIB = "Estimado(a) %,<br />"
            + "Este es un mensaje de notificación informando que su solicitud de material bibliográfico fue rechazada.<br />"
            + "<b>Título:</b> % <br />"
            + "<b>Autor:</b> % <br />"
            + "<b>Editorial:</b> % <br />"
            + "<b>Número de ejemplares:</b> % <br />"
            + "Cordialmente, <br />-Sisinfo";
    public final static String ASUNTO_APROBACION_SOLICITUD_MAT_BIB = "[Sisinfo] Aprobación solicitud material bibliográfico";
    public final static String MENSAJE_APROBACION_SOLICITUD_MAT_BIB = "Estimado(a) %,<br />"
            + "Este es un mensaje de notificación informando que su solicitud de material bibliográfico fue aceptada y solicitada a la biblioteca.<br />"
            + "<b>Título:</b> % <br />"
            + "<b>Autor:</b> % <br />"
            + "<b>Editorial:</b> % <br />"
            + "<b>Número de ejemplares:</b> % <br />"
            + "Cordialmente, <br />-Sisinfo";
    public final static String ASUNTO_LLEGO_A_BIBLIOTECA_MAT_BIB = "[Sisinfo] Llegada de Material bibliográfico solicitado a biblioteca";
    public final static String MENSAJE_LLEGO_A_BIBLIOTECA_MAT_BIB = "Estimado(a) %,<br/><br/>"
            + "Este es un mensaje de notificación informando que el material bibliográfico solicitado ha llegado a la biblioteca.<br />"
            + "<b>Título:</b> % <br/>"
            + "<b>Autor:</b> % <br/>"
            + "<b>Editorial:</b> % <br/>"
            + "<b>Número de ejemplares:</b> % <br/><br/>"
            + "Cordialmente, <br/>-Sisinfo";
    /**
     * Publicaciones
     */
    public final static String ASUNTO_ACTUALIZACION_DATOS_PUBLICACION = "[Sisinfo] Actualización de datos de publicaciones";
    public final static String MENSAJE_ACTUALIZACION_DATOS_PUBLICACION = "Estimado(a) %,<br /><br />"
            + "En Sisinfo se ha recuperado información de su publicación <b>%</b> en Academia. Le solicitamos actualizar la información faltante (palabras clave, abstract, notas adicionales y una imagen opcional del evento donde fue publicado.).<br />"
            + "Por favor ingrese a la página de Sisinfo accesible desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para llenar el formato correspondiente .<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String MENSAJE_ACTUALIZACION_DATOS_PUBLICACION_DEFECTO = "Estimado(a) profesor(a),<br /><br />"
            + "En Sisinfo se ha recuperado información de sus publicaciones en Academia. Le solicitamos actualizar la información faltante (palabras clave, abstract y notas adicionales.).<br />"
            + "Por favor ingrese a la página de Sisinfo accesible desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para llenar el formato correspondiente .<br /><br />Cordialmente,<br />- Sisinfo";
    /**
     * Carga y compromisos
     */
    public final static String ASUNTO_LLENAR_CARGA = "[Sisinfo] Carga y Compromisos Profesores";
    public final static String MENSAJE_LLENAR_CARGA = "Buen día,<br /><br /> Le recordamos que tiene que completar el proceso de Carga y Compromisos "
            + "Por favor ingrese a la página de Sisinfo accesible desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para llenar el formato correspondiente .<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_PROCESO_CARGA_INCIADO = "[Sisinfo] Inicio Proceso Carga Y Compromisos Profesores";
    public final static String MENSAJE_PROCESO_CARGA_INCIADO = "Estimado(a) %1,<br /><br />"
            + "Se ha iniciado el proceso de carga y compromisos en el cual usted debe participar. <br />"
            + "El proceso se cerrará el %2. <br />"
            + "Por favor ingrese a la página de Sisinfo accesible desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a>.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_FINAL_PROCESO_CARGA_CERCA = "[Sisinfo] Final Proceso Carga Y Compromisos Profesores";
    public final static String MENSAJE_FINAL_PROCESO_CARGA_CERCA = "Estimado(a) %,<br /><br />"
            + "Le recordamos que el Proceso de Carga y Compromisos del periodo <b>%</b>"
            + " se cerrará el <b>%</b>. <br />"
            + "<br /><br />Cordialmente,<br />- Sisinfo";
    /*
     * TESIS
     */
    public final static String ASUNTO_ULTIMA_FECHA_INSCRIPCION_SUBAREA_ESTUDIANTE = "[Sisinfo] Ultima fecha para inscripción del plan de estudios";
    public final static String MENSAJE_ULTIMA_FECHA_INSCRIPCION_SUBAREA_ESTUDIANTE = "Estimado(a) %,<br /><br />"
            + "Le recordamos que las inscripciones del plan de estudios para empezar Tesis 1 en el <b>periodo % se cerrarán el día %</b>. <br />"
            + "<br />"
            + "Para inscribirse puede ingresar a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a>.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_RECHAZO_INSCRIPCION_SUBAREA_INVESTIGACION = "[Sisinfo] Rechazo Inscripción plan de estudios";
    public final static String MENSAJE_NOTIFICAR_RECHAZO_INSCRIPCION_SUBAREA_INVESTIGACION = "Estimado(a) %,<br /><br />"
            + "Su solicitud de registro de plan de estudios adscrito al perfil de % ha sido rechazada por %.<br />"
            + "<br /><br /> Cordialmente,<br />- Sisinfo";
    public final static String MENSAJE_NOTIFICAR_RECHAZO_AUTOMATICO_INSCRIPCION_SUBAREA_INVESTIGACION = "Estimado(a) %,<br /><br />"
            + "Su solicitud de registro de plan de estudios adscrito al perfil % ha sido rechazada automáticamente debido a que se venció el plazo máximo de aprobación de esta.<br />"
            + "<br /><br /> Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_APROBAR_INSCRIPCION_SUBAREA_ASESOR = "[Sisinfo] Registro de plan de estudios";
    public final static String MENSAJE_HEADER_APROBAR_INSCRIPCION_SUBAREA_ASESOR = "Buen día %1$s,<br /><br /> Le informamos que tiene las siguientes inscripciones pendientes por aprobar en el proceso de registro de plan de estudios:<br />";
    public final static String MENSAJE_FOOTER_APROBAR_INSCRIPCION_SUBAREA_ASESOR = "<br/> Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para aprobar las inscripciónes correspondientes.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String MENSAJE_APROBAR_INSCRIPCION_SUBAREA_MODIFICADA_ASESOR = "Buen día, %,<br /><br /> Le informamos que la solicitud de inscripción a Subárea de Investigación de % ha sido modificada y es necesario que la vuelva a aprobar.<br />"
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para aprobar la inscripción correspondiente.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_APROBACION_INSCRIPCION_SUBAREA = "[Sisinfo] Registró de plan de estudios";
    public final static String MENSAJE_APROBACION_INSCRIPCION_SUBAREA = "Estimado(a) %, <br /><br />"
            + "Le informamos que su registro de plan de estudios adscrito al perfil de % ha sido aprobado."
            + "<br /><br /> Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_APROBACION_INSCRIPCION_TESIS_1 = "[Sisinfo] Inscripción Tesis 1";
    public final static String MENSAJE_APROBACION_INSCRIPCION_TESIS_1 = "Estimado(a) %, <br /><br />"
            + "Su solicitud de Tesis 1 ha sido aprobada por el asesor.<br />"
            + "En los próximos días coordinación inscribirá la materia en Banner y con esto finalizara el proceso de inscripción a Tesis 1.<br />"
            + "<br /><br /> Cordialmente,<br />- Sisinfo";
    public final static String MENSAJE_APROBACION_INSCRIPCION_TESIS_1_EXTEMPORAL = "Estimado(a) %0,<br /><br />"
            + "Se ha realizado la inscripción de su Tesis 1 de forma extemporánea, para el semestre %1. <br />"
            + "<br /> A partir de este momento su tesis se encuentra en curso."
            + "<br /><br /> Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_RECHAZO_INSCRIPCION_TESIS_1 = "[Sisinfo] Inscripción Tesis 1";
    public final static String MENSAJE_NOTIFICAR_RECHAZO_INSCRIPCION_TESIS_1 = "Estimado(a) %,<br /><br />"
            + "Su solicitud de Tesis 1 ha sido rechazada por %.<br />"
            + "<br /><br /> Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_RECHAZO_AUTOMATICO_INSCRIPCION_TESIS_1 = "[Sisinfo] Inscripción Tesis 1";
    public final static String MENSAJE_RECHAZO_AUTOMATICO_INSCRIPCION_TESIS_1 = "Estimado(a) %,<br /><br />"
            + "Su solicitud de Tesis 1 ha sido rechazada automáticamente debido a que se venció el plazo máximo de aprobación de esta.<br />"
            + "<br /><br /> Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_RECHAZO_AUTOMATICO_INSCRIPCION_TESIS_2 = "[Sisinfo] Inscripción Tesis 2";
    public final static String MENSAJE_RECHAZO_AUTOMATICO_INSCRIPCION_TESIS_2 = "Estimado(a) %,<br /><br />"
            + "Su solicitud de Tesis 2 ha sido <b>rechazada</b> automáticamente debido a que se venció el plazo máximo de aprobación de esta.<br />"
            + "<br /><br /> Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_RECHAZO_INSCRIPCION_TESIS_2 = "[Sisinfo] Inscripción Tesis 2";
    public final static String MENSAJE_NOTIFICAR_RECHAZO_INSCRIPCION_TESIS_2 = "Estimado(a) %,<br /><br />"
            + "Su solicitud de Tesis 2 ha sido <b>rechazada</b> por  %.<br />"
            + "<br /><br /> Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_APROBACION_INSCRIPCION_TESIS_2 = "[Sisinfo] Inscripción Tesis 2";
    public final static String MENSAJE_APROBACION_INSCRIPCION_TESIS_2 = "Estimado(a) %,<br /><br />"
            + "Su solicitud de Tesis 2 ha sido <b>aprobada</b> por %.<br /><br />"
            + "En los próximos días coordinación inscribirá la materia en Banner y con esto finalizara el proceso de inscripción a Tesis 2.<br />"
            + "<br /><br /> Cordialmente,<br />- Sisinfo";
    public final static String MENSAJE_APROBACION_INSCRIPCION_TESIS_2_EXTEMPORANEO = "Estimado(a) %0,<br /><br />"
            + "Se ha realizado la inscripción de su Tesis 2 de forma extemporánea, para el semestre %1 . <br />"
            + "<br /> A partir de este momento su tesis se encuentra en curso."
            + "<br /><br /> Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_APROBACION_INSCRIPCION_TESIS_2_COORDINACION = "[Sisinfo] Inscripción Tesis 2 - %1";
    public final static String MENSAJE_APROBACION_INSCRIPCION_TESIS_2_COORDINACION = "Buen día,<br /><br />"
            + "Le informamos que el asesor %2 ha <b>aprobado</b> la solicitud de inscripción de Tesis 2 del estudiante <b>%1</b>.<br /><br />"
            + "Recuerde realizar la inscripción del estudiante en Banner para finalizar el proceso.<br />"
            + "<br /><br /> Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_CALIFICACION_TESIS = "[Sisinfo] Calificación Tesis 1 ";
    public final static String MENSAJE_CALIFICACION_TESIS = "Estimado(a) %1,<br /><br /> Le informamos que su asesor %2 ha colocado la nota correspondiente a Tesis 1: <b> %3 </b>.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_CALIFICACION_TESIS_AUTOMATICA = "[Sisinfo]  Calificación Automática Tesis 1  ";
    public final static String MENSAJE_CALIFICACION_TESIS_AUTOMATICA = "Buen día, %1,<br /><br /> Le informamos que su asesor %2  no ha colocado la nota correspondiente a Tesis 1: <br /> La fecha máxima para dicha acción ya paso razón por la cual el sistema ha colocado la nota de forma automática.<br /> "
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para conocerla.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_SOLICITUD_PENDIENTE_TESIS1_ESTUDIANTE = "[Sisinfo] Solicitud Pendiente Tesis 1 ";
    public final static String MENSAJE_SOLICITUD_PENDIENTE_TESIS1_ESTUDIANTE = "Buen día, %1, <br /><br /> Le informamos que su asesor(a) %2 ha solicitado el pendiente para Tesis 1. "
            + "Se le informará por este medio una vez que se haya aprobado ó rechazado la solicitud.<br /> "
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para conocerla.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_SOLICITUD_PENDIENTE_TESIS1_ASESOR = "[Sisinfo] Solicitud Pendiente Tesis 1 - %";
    public final static String MENSAJE_SOLICITUD_PENDIENTE_TESIS1_ASESOR = "Buen día,<br /><br /> Le informamos que la solicitud de pendiente de Tesis 1 del estudiante %1, se ha enviado a la Coordinación de Maestría. "
            + "Se le informará por este medio una vez que se haya aprobado ó rechazado la solicitud.<br /> "
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para conocerla.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_SOLICITUD_PENDIENTE_TESIS1_COORDINACION = "[Sisinfo] Solicitud Pendiente Tesis 1";
    public final static String MENSAJE_HEADER_SOLICITUD_PENDIENTE_TESIS1_COORDINACION = "Buen día, <br/><br/>Le recordamos que faltan por aprobar las siguientes solicitudes de pendiente de Tesis 1:";
    public final static String MENSAJE_BULLET_SOLICITUD_PENDIENTE_TESIS1_COORDINACION = "Estudiante: %1$s  (%2$s) \n Asesor: %3$s (%4$s)";
    public final static String MENSAJE_FOOTER_SOLICITUD_PENDIENTE_TESIS1_COORDINACION = "Por favor ingrese a Sisinfo desde la página del" + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para aprobar o rechazarlas <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_APROBACION_PENDIENTE_TESIS1_ESTUDIANTE = "[Sisinfo] Resultado Solicitud Pendiente Tesis 1 ";
    public final static String MENSAJE_APROBACION_PENDIENTE_TESIS1_ESTUDIANTE = "Buen día, %1,<br /><br /> Le informamos que se ha aprobado su solicitud de pendiente para Tesis 1. <br/>"
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para conocerla.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_APROBACION_PENDIENTE_TESIS1_ASESOR = "[Sisinfo] Resultado solicitud Pendiente Tesis 1 - %";
    public final static String MENSAJE_APROBACION_PENDIENTE_TESIS1_ASESOR = "Buen día,<br /><br /> Le informamos que la solicitud de pendiente de Tesis 1 del estudiante %1 se ha aprobado. "
            + "Recuerde que la fecha máxima para levantar este pendiente y subir la nota junto con el artículo es %2.<br/>"
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para conocerla.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_APROBACION_PENDIENTE_TESIS1_COORDINACION = "[Sisinfo] Resultado solicitud Pendiente Tesis 1 - % ";
    public final static String MENSAJE_APROBACION_PENDIENTE_TESIS1_COORDINACION = "Buen día,<br /><br /> Le confirmamos la aprobación de la solicitud de pendiente de Tesis 1, del estudiante %2 asesorado(a) por %1. <br /> "
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para conocerla.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_RECHAZO_PENDIENTE_TESIS1_ESTUDIANTE = "[Sisinfo] Resultado Solicitud Pendiente Tesis 1 ";
    public final static String MENSAJE_RECHAZO_PENDIENTE_TESIS1_ESTUDIANTE = "Buen día, %1,<br /><br /> Le informamos que se ha rechazado su solicitud de pendiente para Tesis 1. "
            + "Por favor comuníquese con la coordinación para más información.<br/>"
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para conocerla.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_RECHAZO_PENDIENTE_TESIS1_ASESOR = "[Sisinfo] Resultado solicitud Pendiente Tesis 1 - %";
    public final static String MENSAJE_RECHAZO_PENDIENTE_TESIS1_ASESOR = "Buen día,<br /><br /> Le informamos que la solicitud de pendiente de Tesis 1 del estudiante %1 se ha rechazado. "
            + "Por favor comuníquese con la coordinación para más información.<br/>"
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para conocerla.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_RECHAZO_PENDIENTE_TESIS1_COORDINACION = "[Sisinfo] Resultado solicitud Pendiente Tesis 1 - % ";
    public final static String MENSAJE_RECHAZO_PENDIENTE_TESIS1_COORDINACION = "Buen día,<br /><br /> Le confirmamos el rechazo de la solicitud de pendiente de Tesis 1, del estudiante %2 asesorado(a) por %1. <br /> "
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para conocerla.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_RETIRO_TESIS1_ESTUDIANTE = "[Sisinfo] Fecha máxima para retirar Tesis 1";
    public final static String MENSAJE_RETIRO_TESIS1_ESTUDIANTE = "Buen día, %1,<br /><br /> Le informamos que el retiro de Tesis 1 se ha registrado en Sisinfo. "
            + "Recuerde que debe hacer el retiro en Registro, de lo contrario Tesis 1 seguirá en curso.<br /> "
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para conocerla.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_RETIRO_TESIS1_ASESOR = "[Sisinfo] Retiro Tesis 1 - %";
    public final static String MENSAJE_RETIRO_TESIS1_ASESOR = "Buen día,<br /><br /> Le informamos que el estudiante %1 ha registrado el retiro de Tesis 1 en Sisinfo. <br/>"
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para conocerla.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_RETIRO_TESIS1_COORDINACION = "[Sisinfo] Retiro Tesis 1 - % ";
    public final static String MENSAJE_RETIRO_TESIS1_COORDINACION = "Buen día,<br /><br /> Le informamos que el estudiante %2, asesorado(a) por %1, ha registrado el retiro de Tesis 1 en Sisinfo. <br /> "
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para conocerla.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_RETIRO_TESIS2_ESTUDIANTE = "[Sisinfo] Retiro Tesis 2 ";
    public final static String MENSAJE_RETIRO_TESIS2_ESTUDIANTE = "Buen día, %1,<br /><br /> Le informamos que el retiro de Tesis 2 se ha registrado en Sisinfo. "
            + "Recuerde que debe hacer el retiro en Registro, de lo contrario Tesis 2 seguirá en curso.<br /> "
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para conocerla.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_RETIRO_TESIS2_ASESOR = "[Sisinfo] Retiro Tesis 2 - %";
    public final static String MENSAJE_RETIRO_TESIS2_ASESOR = "Buen día,<br /><br /> Le informamos que el estudiante %1 ha registrado el retiro de Tesis 2 en Sisinfo. <br/>"
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para conocerla.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_RETIRO_TESIS2_COORDINACION = "[Sisinfo] Retiro Tesis 2 - % ";
    public final static String MENSAJE_RETIRO_TESIS2_COORDINACION = "Buen día,<br /><br /> Le informamos que el estudiante %2, asesorado(a) por %1, ha registrado el retiro de Tesis 2 en Sisinfo. <br /> "
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para conocerla.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_CALIFICACION_TESIS1_COORDINACION = "[Sisinfo] Calificación Tesis 1 - %";
    public final static String MENSAJE_CALIFICACION_TESIS1_COORDINACION = "Buen día,<br /><br /> Le informamos que el asesor %1 ha colocado la nota: <strong>%2</strong> correspondiente a <strong>Tesis 1</strong>, del estudiante <strong>%3</strong>. <br /><br /> "
            + "Para más información ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a>.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_CALIFICACION_TESIS_2 = "[Sisinfo] Calificación Tesis 2 ";
    public final static String MENSAJE_CALIFICACION_TESIS_2 = "Estimado(a) %1,<br /><br /> Le informamos que Coordinación ha subido la nota correspondiente a Tesis 2: %2 <br /><br /> "
            + "Para más información ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para conocerla.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_CALIFICACION_TESIS_2_ASESOR = "[Sisinfo] Calificación Tesis 2 - %";
    public final static String MENSAJE_CALIFICACION_TESIS_2_ASESOR = "Buen día,<br /><br /> Le informamos que Coordinación ha subido la nota correspondiente a Tesis 2 del estudiante %1 : %2. <br /><br /> "
            + "Para más información ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a>.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_REPROBAR_TESIS_2 = "[Sisinfo] Calificación Tesis 2";
    public final static String MENSAJE_REPROBAR_TESIS_2 = "Buen día, %s,<br /><br /> Le informamos que su proyecto de Tesis 2 ha sido reprobado por el asesor con una calificación de %s <br /> "
            + "Para más información ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a>.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_REPROBAR_TESIS_2_ASESOR = "[Sisinfo] Calificación Tesis 2 - %s";
    public final static String MENSAJE_REPROBAR_TESIS_2_ASESOR = "Buen día,<br /><br /> Le confirmamos que el proyecto de Tesis 2 del estudiante %s ha sido reprobado con una calificación de %s. <br /> "
            + "Para más información ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a><br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_ERROR_REPROBAR_TESIS_2 = "[Sisinfo] Error al reprobar Tesis 2 de otros periodos automaticamente";
    public final static String MENSAJE_ERROR_REPROBAR_TESIS_2 = "Buen día,<br /><br /> Ocurrio un error al intentar reprobar tesis 2 con estado \"En Curso\" de semestres anteriores al actual<br /><br /> "
            + "Para más información ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a><br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_REPROBAR_TESIS_2_POR_SISINFO = "[Sisinfo] Calificación automatica Tesis 2 - %s";
    public final static String MENSAJE_REPROBAR_TESIS_2_POR_SISINFO = "Buen día,<br /><br /> Le informamos que el proyecto de Tesis 2 del estudiante %s del periodo %s ha sido reprobado automáticamente por SisInfo con una calificación de %s "
            + "debido a que no pertenece al periodo actual. <br /><br /> "
            + "Para más información ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a><br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_APROBAR_INSCRIPCION_TESIS2_ASESOR = "[Sisinfo] Inscripciones Tesis 2";
    public final static String MENSAJE_HEADER_APROBAR_INSCRIPCION_TESIS2_ASESOR = "Buen día %1$s,<br /><br />Le recordamos que tiene las siguientes solicitudes pendientes por aprobar en el proceso de Inscripción a Tesis 2: <br />";
    public final static String MENSAJE_FOOTER_APROBAR_INSCRIPCION_TESIS2_ASESOR = "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para aprobar las inscripciónes correspondientes.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String MENSAJE_APROBAR_INSCRIPCION_TESIS2_ASESOR_CON_ADVERTENCIA = "Buen día, %,<br /><br /> Le recordamos que tiene una solicitud pendiente por aprobar en el proceso de Inscripción a Tesis 2 del estudiante %. <br />"
            + "Tenga en cuenta a la hora de evaluar la solicitud que el estudiante no vio Tesis 1 en el periodo anterior."
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para aprobar la inscripción correspondiente.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_APROBAR_INSCRIPCION_TESIS2_CORREO = "[Sisinfo] Inscripción Tesis 2 %s";
    public final static String MENSAJE_APROBAR_INSCRIPCION_TESIS2_CORREO = "Estimado(a) %1$s,<br /><br />Le informamos que el estudiante<b> %2$s </b>ha inscrito <b>Tesis 2</b> y lo ha seleccionado como su asesor."
            + "<br><br>Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para aprobar la inscripción correspondiente.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_APROBAR_INSCRIPCION_TESIS1_ASESOR = "[Sisinfo] Inscripción Tesis 1";
    public final static String MENSAJE_HEADER_APROBAR_INSCRIPCION_TESIS1_ASESOR = "Buen día %1$s,<br /><br /> "
            + "Le recordamos que tiene las siguientes solicitudes pendientes por aprobar en el proceso de Inscripción a Tesis 1: <br />";
    public final static String MENSAJE_FOOTER_APROBAR_INSCRIPCION_TESIS1_ASESOR = "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para aprobar las inscripciones correspondientes.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_APROBAR_INSCRIPCION_TESIS1_COORDINACION = "[Sisinfo] Inscripción Tesis 1 - Coordinación ";
    /*public final static String MENSAJE_APROBAR_INSCRIPCION_TESIS1_COORDINACION = "Buen día, %,<br /><br /> Le recordamos que tiene una solicitud pendiente por aprobar en el proceso de Inscripción a Tesis 1."
    + "<br /> Por favor ingrese a Sisinfo desde la página del"
    + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para aprobar la inscripción correspondiente.<br /><br />Cordialmente,<br />- Sisinfo";
     */
    public final static String MENSAJE_HEADER_APROBAR_INSCRIPCION_TESIS1_COORDINACION = "Buen día,<br /><br /> Le recordamos que tiene las siguientes solicitudes pendientes por aprobar en el proceso de Inscripción a Tesis 1: <br />";
    public final static String MENSAJE_FOOTER_APROBAR_INSCRIPCION_TESIS1_COORDINACION = "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para aprobar las inscripciones correspondientes.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_ULTIMA_FECHA_INSCRIPCION_TESIS_1_ESTUDIANTE = "[Sisinfo] Inscripción Tesis 1";
    public final static String MENSAJE_ULTIMA_FECHA_INSCRIPCION_TESIS_1_ESTUDIANTE = "Estimado(a) %,<br /><br />"
            + "Le recordamos que las inscripciones de Tesis 1 para el <b>periodo % se cerrarán el día % </b>. <br />"
            + " <br />"
            + " Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para inscribirse<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_ULTIMA_FECHA_INSCRIPCION_TESIS_1_ASESOR = "[Sisinfo] Ultima fecha para aceptar Inscripciones Tesis 1 ";
    public final static String MENSAJE_ULTIMA_FECHA_INSCRIPCION_TESIS_1_ASESOR = "Estimado(a) %,<br /><br />"
            + "Le recordamos que las inscripciones de Tesis 1 para el <b>periodo % se cerrarán el día % </b>. <br />"
            + " <br />"
            + "Actualmente, usted tiene solicitudes de asesoría pendientes, <br /> Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para confirmar las asesorías.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_ULTIMA_FECHA_SUBIR_NOTAS_TESIS_1_ASESOR = "[Sisinfo] Máxima Fecha para subir Notas Tesis 1";
    public final static String MENSAJE_HEADER_ULTIMA_FECHA_SUBIR_NOTAS_TESIS_1_ASESOR = "Estimado(a) %,<br /><br />"
            + "Le recordamos que la fecha máxima para subir las notas de Tesis 1 del periodo <b> % es el día %, actualmente falta calificar las tesis de:  </b>. <br />"
            + " <br />";
    public final static String MENSAJE_ULTIMA_FECHA_SUBIR_NOTAS_TESIS_1 = "Estimado(a) %,<br /><br />"
            + "Le recordamos que la fecha máxima para subir las notas de Tesis 1 del periodo <b> % es el día %.  </b>. <br />"
            + " <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String MENSAJE_FOOTER_ULTIMA_FECHA_SUBIR_NOTAS_TESIS_1_ASESOR = "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para subir las notas.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_ULTIMA_FECHA_SOLICITAR_PENDIENTE_TESIS_1 = "[Sisinfo] Fecha máxima solicitar pendiente Tesis 1";
    public final static String MENSAJE_ULTIMA_FECHA_SOLICITAR_PENDIENTE_TESIS_1_ASESOR = "Estimado(a) %,<br /><br />"
            + "Le recordamos que la fecha máxima para solicitar Pendiente en Tesis 1 para el <b>periodo % es el día % </b> . <br />"
            + " <br />"
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para solicitar pendiente<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String MENSAJE_ULTIMA_FECHA_SOLICITAR_PENDIENTE_TESIS_1_ESTUDIANTE = "Estimado(a) %,<br /><br />"
            + "Le recordamos que la fecha máxima para solicitar Pendiente en Tesis 1 para el <b>periodo % es el día % </b> . <br />"
            + " <br />"
            + "Para poder solicitar el pendiente, su asesor debe ingresar a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a><br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_ULTIMA_FECHA_LEVANTAR_PENDIENTE_TESIS_1_ASESOR = "[Sisinfo] Máximo plazo levantar Pendiente Tesis 1";
    public final static String MENSAJE_ULTIMA_FECHA_LEVANTAR_PENDIENTE_TESIS_1_ASESOR = "Estimado(a) %,<br /><br />"
            + "Le recordamos que la fecha máxima para levantar el Pendiente de Tesis 1 del <b>periodo % es el día %  </b>. <br />"
            + " <br />"
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para subir las notas.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_ULTIMA_FECHA_LEVANTAR_PENDIENTE_TESIS_1_ESTUDIANTE = "[Sisinfo] Máximo plazo levantar Pendiente Tesis 1";
    public final static String MENSAJE_ULTIMA_FECHA_LEVANTAR_PENDIENTE_TESIS_1_ESTUDIANTE = "Estimado(a) %,<br /><br />"
            + "Le recordamos que la fecha máxima para levantar el Pendiente de Tesis 1 del <b>periodo % es el día %  </b>. <br />"
            + " <br />"
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para conocer el estado de su tesis..<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_APROBAR_INSCRIPCION_SUBAREA_DIRECTOR_SUBAREA = "[Sisinfo] Registro de plan de estudios.";
    public final static String MENSAJE_HEADER_APROBAR_INSCRIPCION_SUBAREA_DIRECTOR_SUBAREA = "Estimado(a) %1$s,<br /><br /> Le informamos que como director de la subárea de investigación tiene pendiente la aprobación  de las inscripciónes de los siguientes estudiantes:";
    public final static String MENSAJE_FOOTER_APROBAR_INSCRIPCION_SUBAREA_DIRECTOR_SUBAREA = "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para aprobar"
            + " las inscripciónes correspondientes.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String FORMATOE_BULLET_NOMBRE_ESTUDIANTE_CORREO = "%1$s (%2$s)";
    public final static String FORMATOE_BULLET_NOMBRE_ESTUDIANTE_CORREO_ADVERTENCIA = "%1$s (%2$s)-[%3$s]";
    public final static String MENSAJE_BULLET_MODIFICADO_APROBAR_INSCRIPCION_SUBAREA_DIRECTOR_SUBAREA = "%1$s (%2$s) - Inscripción Modificada";
    public final static String ASUNTO_SUSTENTACION_TESIS_2 = "[Sisinfo] Sustentacion Tesis 2";
    public final static String MENSAJE_SUSTENTACION_TESIS_2 = "Buen día, %,<br /><br /> Le informamos que el estudiante % hará la sustentación de Tesis 2 en la fecha % ."
            + "<br /><br />A continuación se anexan las plantillas de calificación para los jurados."
            + " <br />"
            + " <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String MENSAJE_BULLET_INSCRIPCION_BANNER_TESIS1_COORDINACION = "Estudiante: %1$s  (%2$s) \n Asesor: %3$s (%4$s)";
    public final static String MENSAJE_HEADER_INSCRIPCION_BANNER_TESIS1_COORDINACION = "Buen día,<br /><br /> Le recordamos que tiene las siguientes solicitudes de Tesis 1 pendientes por inscribir en Banner: <br />";
    public final static String MENSAJE_FOOTER_INSCRIPCION_BANNER_TESIS1_COORDINACION = "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para aprobar las inscripciones correspondientes.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_APROBAR_INSCRIPCION_BANNER_TESIS1_COORDINACION = "[Sisinfo] Inscripción Banner Tesis 1 - Coordinación ";
    public final static String MENSAJE_BULLET_INSCRIPCION_BANNER_TESIS2_COORDINACION = "Estudiante: %1$s  (%2$s) \n Asesor: %3$s (%4$s)";
    public final static String MENSAJE_HEADER_INSCRIPCION_BANNER_TESIS2_COORDINACION = "Buen día,<br /><br /> Le recordamos que tiene las siguientes solicitudes de Tesis 2 pendientes por inscribir en Banner: <br />";
    public final static String MENSAJE_FOOTER_INSCRIPCION_BANNER_TESIS2_COORDINACION = "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para aprobar las inscripciones correspondientes.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_APROBAR_INSCRIPCION_BANNER_TESIS2_COORDINACION = "[Sisinfo] Inscripción Banner Tesis 1 - Coordinación ";
    public final static String ASUNTO_CONFIRMACION_RESERVA_CITA = "[Sisinfo] Reserva de Cita con Coordinación de Sistemas";
    public final static String MENSAJE_CONFIRMACION_RESERVA_CITA = "Buen día, %1$s,<br /><br /> Le informamos que ha reservado de manera exitosa una cita con la coordinación del departamento de Sistemas. "
            + "Confirmamos su reserva con los siguientes datos:<br /><br />"
            + "<b>Fecha:</b> %2$s<br />"
            + "<b>Hora Inicio:</b> %3$s<br />"
            + "<b>Hora Fin:</b> %4$s<br />"
            + "<b>Motivo:</b> %5$s<br />"
            + "<b>Programa:</b> %6$s<br />"
            + "<br /><br />Recuerde que es muy importante llegar a tiempo para su cita."
            + "<br /><br />En caso de cualquier inquietud comuníquese con el departamento."
            + "<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_CONFIRMACION_CONFLICTO_HORARIO = "[Sisinfo] Petición de conflicto de horario";
    public final static String MENSAJE_CONFIRMACION_CONFLICTO_HORARIO = "Buen día %, <br /><br />"
            + "Le informamos que ha hecho una petición de conflicto de horario con los siguientes datos: <br />"
            + "%"
            + "<br />La coordinación de Sistemas procederá a evaluar su solicitud."
            + "<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_RESOLUCION_CONFLICTO_HORARIO = "[Sisinfo] Petición de conflicto de horario";
    public final static String MENSAJE_RESOLUCION_CONFLICTO_HORARIO = "Buen día, %, <br /><br />"
            + "Le informamos que se ha resuelto una petición de conflicto de horario con los siguientes datos: <br />"
            + "%"
            + "<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_CANCELACION_CONFLICTO_HORARIO = "[Sisinfo] Cancelación de conflicto de horario";
    public final static String MENSAJE_CANCELACION_CONFLICTO_HORARIO = "Buen día %, <br /><br />"
            + "Le informamos que ha cancelado una petición de conflicto de horario con los siguientes datos: <br />"
            + "%"
            + "<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_CANCELACION_RESERVA = "[Sisinfo] Cancelación de una reserva de cita con Coordinación de Sistemas";
    public final static String MENSAJE_CANCELACION_RESERVA = "Buen día, %1$s,<br /><br /> Le informamos que el estudiante %2$s ha cancelado una reserva "
            + "que tenía los siguientes datos:<br /><br />"
            + "<b>Fecha:</b> %3$s<br />"
            + "<b>Hora Inicio:</b> %4$s<br />"
            + "<b>Hora Fin:</b> %5$s<br />"
            + "<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_INSCRIPCION_EVENTO_EXITOSA = "[Sisinfo] Inscripción a evento realizada";
    public final static String MENSAJE_INSCRIPCION_EVENTO_EXITOSA = "Buen día, %1$s,<br /><br /> La inscripción al evento %2$s ha sido realizada satisfactoriamente. "
            + "Tenga en cuenta la siguiente información:<br /><br />"
            + "<b>Descripción:</b> %3$s<br />"
            + "<b>Fecha de inicio:</b> %4$s<br />"
            + "<b>Hora de inicio:</b> %5$s<br />"
            + "%6$s"
            + "<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_APROBACION_REVISTA_PARADIGMA_TESIS2 = "[Sisinfo] Artículo de Tesis 2 aprobado para Revista Paradigma";
    public final static String ASUNTO_APROBACION_REVISTA_PARADIGMA_TESIS1 = "[Sisinfo] Artículo de Tesis 1 aprobado para Revista Paradigma";
    public final static String MENSAJE_APROBACION_REVISTA_PARADIGMA_TESIS2 = "Buen día,<br /><br /> El(La) profesor(a) %1$s ha autorizado la publicación de un artículo en la Revista Paradiga: <br /><br />"
            + "<b>Nombre del estudiante: </b> %2$s<br />"
            + "<b>Tema de tesis:</b> %3$s<br />"
            + "<b>Grupo de Investigación:</b> %4$s<br />"
            + "<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String MENSAJE_APROBACION_REVISTA_PARADIGMA_TESIS1 = "Buen día,<br /><br /> El(La) profesor(a) %1$s ha autorizado la publicación de un artículo en la Revista Paradiga: <br /><br />"
            + "<b>Nombre del estudiante: </b> %2$s<br />"
            + "<b>Tema de tesis:</b> %3$s<br />"
            + "<b>Grupo de Investigación:</b> %4$s<br />"
            + "<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_CANCELACION_RESERVA_GRUPO = "[Sisinfo] Cancelación de una reserva de cita con Coordinación de Sistemas";
    public final static String MENSAJE_CANCELACION_RESERVA_GRUPO = "Buen día, %1$s,<br /><br /> Le informamos que la reserva con coordianción que tenía el: <b>%2$s</b><br /> "
            + "<b>Hora Inicio:</b> %3$s<br />"
            + "<b>Hora Fin:</b> %4$s<br /><br />"
            + "Ha sido <b>Cancelada</b> por el siguiente motivo:<br/><br/>"
            + "\"%5$s\"<br /><br />"
            + "<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String MENSAJE_NOTIFICAR_RECHAZO_INSCRIPCION_TESIS_1_COORDINACION = "Estimado(a) %,<br /><br />"
            + "Su solicitud de Tesis 1 ha sido rechazada por la Coordinación de Ingeniería de Sistemas.<br /><br />"
            + "Razón de rechazo: <br /> %"
            + "<br /><br /> Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_INFORME_30PORCIENTO_TESIS_1 = "[Sisinfo] Informe 30% Tesis 1";
    public final static String MENSAJE_INFORME_30PORCIENTO_TESIS_1 = "Estimado(a) %1, <br /><br />"
            + "Su asesor ha enviado el informe del 30% correspondiente a <strong>Tesis 1</strong>: "
            + "<br/>%2 %3<br/><br/> "
            + "Para más información por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a>.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_INFORME_30PORCIENTO_TESIS_1_COORD = "[Sisinfo] Informe 30% Tesis 1 - %1";
    public final static String MENSAJE_INFORME_30PORCIENTO_TESIS_1_COORD = "Buen día, <br /><br />"
            + "El asesor %1 ha enviado el informe del 30% correspondiente a <strong>Tesis 1</strong>, del estudiante <strong>%2</strong>:<br /> "
            + "<br/> %3 %4 <br/><br/>"
            + "Para más información por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a>. <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_INFORME_30PORCIENTO_TESIS_2 = "[Sisinfo] Informe 30% Tesis 2";
    public final static String MENSAJE_INFORME_30PORCIENTO_TESIS_2 = "Estimado(a) %1, <br /><br />"
            + "Su asesor ha enviado el informe del 30% correspondiente a <strong>Tesis 2</strong>: "
            + "<br/>%2 %3<br/><br/> "
            + "Para más información por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a>. <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_INFORME_30PORCIENTO_TESIS_2_COORD = "[Sisinfo] Informe 30% Tesis 2 - %1";
    public final static String MENSAJE_INFORME_30PORCIENTO_TESIS_2_COORD = "Buen día, <br /><br />"
            + "El asesor %1 ha enviado el informe del 30% correspondiente a <strong>Tesis 2</strong>, del estudiante <strong>%2</strong>:<br /> "
            + "<br/> %3 %4 <br/><br/>"
            + "Para más información por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a>. <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_ULTIMA_FECHA_INSCRIPCION_TESIS_2_ASESOR = "[Sisinfo] Ultima fecha para aceptar Inscripciones Tesis 2 ";
    public final static String MENSAJE_ULTIMA_FECHA_INSCRIPCION_TESIS_2_ASESOR = "Estimado(a) %,<br /><br />"
            + "Le recordamos que las inscripciones de Tesis 2 para el <b>periodo % se cerrarán el día % </b>. <br />"
            + " <br />"
            + "Actualmente, usted tiene solicitudes de asesoría pendiente, <br /> Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para confirmar las asesorías.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_ULTIMA_FECHA_INSCRIPCION_TESIS_2_ESTUDIANTE = "[Sisinfo] Ultima fecha para Inscripciones Tesis 2 ";
    public final static String MENSAJE_ULTIMA_FECHA_INSCRIPCION_TESIS_2_ESTUDIANTE = "Estimado(a) %,<br /><br />"
            + "Le recordamos que las inscripciones de Tesis 2 para el <b>periodo % se cerrarán el día % </b>. <br />"
            + " <br />"
            + "Actualmente, usted no ha inscrito tesis 2 <br /> Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para realizar la inscripción  <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_ULTIMA_FECHA_REPROBAR_ESTUDIANTE_SIN_SUSTENTACION = "[Sisinfo] Ultima fecha para reprobar Tesis 2 sin sustentación";
    public final static String MENSAJE_ULTIMA_FECHA_REPROBAR_ESTUDIANTE_SIN_SUSTENTACION = "Estimado(a) %,<br /><br />"
            + "Le recordamos que la fecha máxima  para reprobar las tesis del <b>periodo % es el día %</b>. <br />"
            + " <br />"
            + " <br /> Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para asignar la nota <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_ULTIMA_FECHA_PARA_PEDIR_PENDIENE_ESPECIAL_TESIS_2 = "[Sisinfo] Fecha máxima solicitar pendiente Tesis 2";
    public final static String MENSAJE_ULTIMA_FECHA_PARA_PEDIR_PENDIENE_ESPECIAL_TESIS_2 = "Estimado(a) %,<br /><br />"
            + "Le recordamos que la fecha máxima para solicitar Pendiente Especial en Tesis 2 para el <b>periodo % es el día % </b> . <br />"
            + " <br />"
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para solicitar pendiente<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_ULTIMA_FECHA_PARA_SUSTENTAR_TESIS_2 = "[Sisinfo] Fecha máxima sustentaciones Tesis 2";
    public final static String MENSAJE_ULTIMA_FECHA_PARA_SUSTENTAR_TESIS_2 = "Estimado(a) %,<br /><br />"
            + "Le recordamos que la fecha máxima de sustentación de Tesis 2 en el periodo <b> % es el día % </b>. <br />"
            + "Recuerde que los estudiantes deben seleccionar el horario con 3 días de anticipación a la fecha de la sustentación y usted debe aprobar ese horario.<br /><br />"
            + "Para más información por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para seleccionar su horario de sustentación<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_ULTIMA_FECHA_PARA_SUSTENTAR_TESIS_2_PE = "[Sisinfo] Fecha máxima sustentar Tesis 2 Pendiente Especial";
    public final static String MENSAJE_ULTIMA_FECHA_PARA_SUSTENTAR_TESIS_2_PE = "Estimado(a) %,<br /><br />"
            + "Le recordamos que la fecha máxima para sustentar Tesis 2  Pendiente Especial en el periodo <b> % es el día % </b>. <br />"
            + "Adicionalmente, el horario se debe registrar con 3 días de anticipación a la fecha de la sustentación<br /><br />"
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para seleccionar su horario de sustentación<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_CAMBIO_JURADOS_SUSTENTACION = "[Sisinfo] Jurados Tesis 2";
    public final static String MENSAJE_CAMBIO_JURADOS_SUSTENTACION = "Estimado(a)  %, <br /><br />"
            + "Se han modificado los jurados de su Tesis de Maestría. <br /><br />"
            + "Le recordamos que la fecha máxima para sustentar Tesis 2 en el periodo <b> % es el día %</b>. <br /><br />"
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para conocer los cambios. <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_RESERVAR_SALON_PARA_SUSTENTACION_TESIS_2 = "[Sisinfo] Horarios sustentación  Tesis 2";
    public final static String MENSAJE_HEADER_RESERVAR_SALON_PARA_SUSTENTACION_TESIS_2 = "Estimado(a)  Secretario de Coordinación, <br /><br />"
            + "Se debe asignar salón de sustentación de tesis de maestría para los siguientes estudiantes: <br /> ";
    public final static String MENSAJE_FOOTER_RESERVAR_SALON_PARA_SUSTENTACION_TESIS_2 = "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para asignar los salones correspondientes. <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String MENSAJE_BULLET_SRESERVAR_SALON_PARA_SUSTENTACION_TESIS_2 = "Estudiante: %1$s  (%2$s) Fecha: %3$s ";
    //  public final static String ASUNTO_CAMBIO_FECHA_SUSTENTACION_TESIS_2 = "[Sisinfo] Horario sustentación Tesis 2 - %1";
    public final static String MENSAJE_CAMBIO_FECHA_SUSTENTACION_TESIS_2 = "Estimado(a)  %1, <br /><br />"
            + "Se ha modificado el horario de sustentación de Tesis 2 Maestría, del(de la) estudiante: %2 <br /> "
            + "a la siguiente fecha: %3 <br />"
            + "<strong>Está pendiente la asignación de salón por parte del secretario de coordinación</strong>. Se le informará una vez que se haya hecho tal asignación. <br/>"
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para asignar el salón. <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_SALON_SUSTENTACION_TESIS = "[Sisinfo] Sustentación Tesis 2";
    public final static String MENSAJE_SALON_SUSTENTACION_TESIS = "Estimado(a)  %1, <br /><br />"
            + "El salón asignado para su sustentación de tesis es el: %2 el día: %3 <br />"
            + " <br />"
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para conocer los demás detalles. <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_FINAL_TESIS_2 = "[Sisinfo] Articulo Final Tesis 2";
    public final static String MENSAJE_HEADER_ASUNTO_FINAL_TESIS_2 = "Estimado(a)  %1$s, <br /><br />"
            + "Le recordamos que aún debe entregar el <b>articulo de finalizacion de Tesis 2</b> del(de la) estudiante: <br /><br />";
    public final static String MENSAJE_FOOTER_FINAL_TESIS_2 = " <br /><br />"
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para subir el archivo. <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_SUBIR_PROPUESTAS_TESIS = "[Sisinfo] Subir Propuestas Tesis ";
    public final static String MENSAJE_SUBIR_PROPUESTAS_TESIS = "Estimado(a) %,<br /><br />"
            + "Le recordamos que la fecha máxima para ingresar propuestas de tesis del <b> periodo % es el día % </b>. <br />"
            + " <br />"
            + " <br /> Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para registrar nuevas propuestas <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_COMENTARIOS_TESIS1 = "[Sisinfo] Informe 30% Tesis";
    public final static String ASUNTO_COMENTARIOS_TESIS2 = "[Sisinfo] Informe 30% Tesis II";
    public final static String MENSAJE_HEADER_COMENTARIOS_TESIS = "Estimado(a) %1$s,<br /><br />"
            + "Le recordamos que como parte del proceso de tesis, usted debe entregar una apreciación cualitativa del desempeño y resultados alcanzados por los estudiantes:<br />";
    public final static String MENSAJE_FOOTER_COMENTARIOS_TESIS = " <br />"
            + " <br /> Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para ingresar sus comentarios <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_APROBAR_INSCRIPCION_PROYECTO_DE_GRADO = "[Sisinfo] Aprobación Inscripción Proyecto de Grado";
    public final static String HEADER_APROBAR_INSCRIPCION_PROYECTO_DE_GRADO = "Buen día %1$s,<br /><br />"
            + "Le informamos que los siguientes estudiantes han solicitado inscribir Proyecto de Grado con usted:";
    public final static String FOOTER_APROBAR_INSCRIPCION_PROYECTO_DE_GRADO = "<br />Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para aprobar o rechazar"
            + " las inscripciones correspondientes.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String MENSAJE_APROBAR_INSCRIPCION_PROYECTO_DE_GRADO = "<b>%1$s</b>";
    public final static String MENSAJE_COMPLETO_APROBAR_INSCRIPCION_PROYECTO_DE_GRADO = "Buen día, %1$s,<br /><br />"
            + "Le informamos que el estudiante <b>%2$s</b> ha solicitado inscribir Proyecto de Grado con usted."
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para aprobar o rechazar"
            + " la inscripción correspondiente.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_APROBAR_INSCRIPCION_PROYECTO_DE_GRADO_COORDINACION = "[Sisinfo] Aprobación Inscripción Proyecto de Grado";
    public final static String MENSAJE_APROBAR_INSCRIPCION_PROYECTO_DE_GRADO_COORDINACION = "<b>%1$s</b>";
    public final static String MENSAJE_COMPLETO_APROBAR_INSCRIPCION_PROYECTO_DE_GRADO_COORDINACION = "Buen día,<br /><br />"
            + "Le informamos que el estudiante <b>%1$s</b> ha solicitado inscribir Proyecto de Grado. "
            + "<br>Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para aprobar o rechazar"
            + " la solicitud correspondiente.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String HEADER_APROBAR_INSCRIPCION_PROYECTO_DE_GRADO_COORDINACION = "Buen día,<br /><br />"
            + "Le informamos que los siguientes estudiantes han solicitado inscribir Proyecto de Grado:";
    public final static String FOOTER_APROBAR_INSCRIPCION_PROYECTO_DE_GRADO_COORDINACION = "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para aprobar o rechazar"
            + " las solicitudes correspondientes.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_RECHAZO_INSCRIPCION_PROYECTO_DE_GRADO_COORDINACION = "[Sisinfo] Rechazo Inscripción Proyecto de Grado";
    public final static String MENSAJE_RECHAZO_INSCRIPCION_PROYECTO_DE_GRADO_COORDINACION = "Estimado(a) %1$s<br /><br />"
            + "Le informamos que su solicitud de proyecto de grado \"%2$s\" para el periodo %3$s ha sido rechazada por coordinación.%4$s<br>"
            + "<br />Cordialmente,<br />- Sisinfo";
    //Mensaje de Aprobación de Afiche
    public final static String ASUNTO_AFICHE_PROYECTO_DE_GRADO_APROBADO = "[Sisinfo] Aprobación Afiche Proyecto de Grado";
    public final static String MENSAJE_AFICHE_PROYECTO_DE_GRADO_APROBADO = "Buen día, %,<br /><br />"
            + "Le informamos que su asesor (%) ha aprobado su propuesta de afiche para proyecto de grado."
            + "<br />Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para consultar"
            + " el estado más reciente de su tesis.<br /><br />Cordialmente,<br />- Sisinfo";
    //Mensaje rechazo afiche
    public final static String ASUNTO_AFICHE_PROYECTO_DE_GRADO_RECHAZADO = "[Sisinfo] Informe Estado de Afiche Proyecto de Grado";
    public final static String MENSAJE_AFICHE_PROYECTO_DE_GRADO_RECHAZADO = "Buen día, %,<br /><br />"
            + "Le informamos que su asesor (%) ha rechazado su propuesta de afiche para proyecto de grado. Recuerde ingresar a Sisinfo y realizar una nueva carga de afiche para que su asesor la apruebe."
            + "<br />Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para realizar una nueva carga de Afiche."
            + "<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_APROBAR_AFICHE_PROYECTO_DE_GRADO = "[Sisinfo] Aprobar/Rechazar Afiche Proyecto de Grado - %1$s";
    public final static String HEADER_APROBAR_AFICHE_PROYECTO_DE_GRADO = "Buen día, %1$s,<br /><br /> Le informamos que los siguientes estudiantes han subido el Afiche de Proyecto de Grado para su Aprobación/Rechazo: ";
    public final static String MENSAJE_APROBAR_AFICHE_PROYECTO_DE_GRADO = "<b>%1$s</b>";
    public final static String FOOTER_APROBAR_AFICHE_PROYECTO_DE_GRADO = "<br />Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para aprobar/rechazar"
            + " los afiches correspondientes.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String MENSAJE_COMPLETO_APROBAR_AFICHE_PROYECTO_DE_GRADO = "Buen día, %1$s,<br /><br /> Le informamos que <b>%2$s</b> ha subido el Afiche de Proyecto de Grado para su Aprobación/Rechazo. "
            + "<br />Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para aprobar/rechazar"
            + " el afiche correspondiente.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_RECHAZO_INSCRIPCION_PROYECTO_DE_GRADO = "[Sisinfo] Inscripción Proyecto de Grado";
    public final static String MENSAJE_RECHAZO_INSCRIPCION_PROYECTO_DE_GRADO = "Estimado(a) %,<br /><br />"
            + "Su solicitud de Proyecto de Grado ha sido rechazada por % <br />"
            + "<br /><br /> Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_PROYECTO_DE_GRADO_ACEPTADO = "[Sisinfo] Inscripción Proyecto de Grado";
    public final static String ASUNTO_PROYECTO_DE_GRADO_TAREA = "[Sisinfo] Tarea Proyecto de Grado";
    public final static String HEADER_PROYECTO_DE_GRADO_ACEPTADO = "Estimado(a) %1$s,<br /><br />"
            + "Su solicitud de Proyecto de Grado ha sido aceptada por %2$s. "
            + "Recuerde que tiene hasta el %3$s para enviar su documento de propuesta de proyecto de grado.";
    public final static String FOOTER_PROYECTO_DE_GRADO_ACEPTADO = "<br />Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para enviar "
            + "la propuesta correspondiente.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String MENSAJE_COMPLETO_PROYECTO_DE_GRADO_ACEPTADO = "Estimado(a) %1$s,<br /><br />"
            + "Su solicitud de Proyecto de Grado ha sido aceptada por %2$s. "
            + "Recuerde que tiene hasta el %3$s para enviar su documento de propuesta de proyecto de grado."
            + "<br />Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para enviar "
            + "la propuesta correspondiente.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_PROYECTO_DE_GRADO_CREADO_COORDINACION = "[Sisinfo] Coordinacion: Inscripción Proyecto de Grado";
    public final static String MENSAJE_PROYECTO_DE_GRADO_CREADO_COORDINACION = "Estimado(a) %,<br /><br />"
            + "Se ha creado el proyecto de Grado para el estudiante % <br />" + "<br /> En breve recibira el documento de propuesta."
            + "<br /><br /> Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_SUBIR_PROPUESTAS_TESIS_PREGRADO = "[Sisinfo] Subir Propuestas Proyecto de Grado";
    public final static String MENSAJE_SUBIR_PROPUESTAS_TESIS_PEGRADO = "Estimado(a) %,<br /><br />"
            + "Le recordamos que la fecha máxima para ingresar propuestas de Proyecto de Grado <b> para el periodo % es el día % </b>. <br />"
            + " <br />"
            + " <br /> Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para registrar nuevas propuestas <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_RESERVA_ELEMENTO_LABORATORIO_ELIMINADA = "[Sisinfo] Reserva Elemento Inventario Eliminada";
    public final static String MENSAJE_RESERVA_ELEMENTO_LABORATORIO_ELIMINADA = "Estimado(a) %,<br /><br />"
            + "Le informamos que la reserva del elemento %, fue eliminada, debido a que el administrador del laboratorio eliminó el elemento."
            + " <br />"
            + " <br /> Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para registrar crear otra reserva <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_ELEMENTO_LABORATORIO_NO_ENTREGADO = "[Sisinfo] Elemento Inventario No Entregado";
    public final static String MENSAJE_ELEMENTO_LABORATORIO_NO_ENTREGADO = "Estimado(a) %,<br /><br />"
            + "Le informamos que el usuario % no ha entregado el elemento % con serial % antes de la hora máxima del día."
            + " <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_VENCIMIENTO_USUARIO_LABORATORIO = "[Sisinfo] Vencimiento Usuario Laboratorio";
    public final static String MENSAJE_VENCIMIENTO_USUARIO_LABORATORIO = "Estimado(a) %,<br /><br />"
            + "Le informamos que los permisos del usuario %, con correo % , en el laboratorio %, se encuentran próximos a vencerse"
            + " <br />"
            + " <br /> Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para modificar los permisos del usuario <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_VENCIMIENTO_PRESTAMO_LABORATORIO = "[Sisinfo] Préstamo Elemento Laboratorio";
    public final static String MENSAJE_VENCIMIENTO_PRESTAMO_LABORATORIO = "Estimado(a) %,<br /><br />"
            + "Le recordamos que la hora de entrega del elemento % con serial % vencía a las: %, favor pasar por el laboratorio a entregarlo."
            + "  <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_APROBAR_PROPUESTA_PROYECTO_DE_GRADO = "[Sisinfo] Entrega Propuesta Proyecto de Grado";
    public final static String HEADER_APROBAR_PROPUESTA_PROYECTO_DE_GRADO = "Buen día %1$s,<br /><br /> "
            + "Le informamos que los siguientes estudiantes han subido su propuesta de Proyecto de Grado:";
    public final static String FOOTER_APROBAR_PROPUESTA_PROYECTO_DE_GRADO = "<br />Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para aprobar o rechazar"
            + " las propuestas correspondientes.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String MENSAJE_APROBAR_PROPUESTA_PROYECTO_DE_GRADO = "<b>%1$s</b>";
    public final static String MENSAJE_COMPLETO_APROBAR_PROPUESTA_PROYECTO_DE_GRADO = "Buen día %1$s,<br /><br /> "
            + "Le informamos que el estudiante <b>%2$s</b> ha subido su propuesta para Proyecto de Grado."
            + "<br />Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para aprobar o rechazar"
            + " la propuesta correspondiente.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_PROPUESTA_PROYECTO_DE_GRADO_APROBADA = "[Sisinfo] Propuesta Proyecto de Grado Aprobada";
    public final static String MENSAJE_PROPUESTA_PROYECTO_DE_GRADO_APROBADA = "Estimado(a) %,<br /><br />"
            + "Le informamos que su asesor de proyecto de grado ha aprobado su propuesta de tesis."
            + " <br />"
            + " Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para continuar con el proceso.  <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_PROPUESTA_PROYECTO_DE_GRADO_RECHAZADA = "[Sisinfo] Propuesta Proyecto de Grado Rechazada";
    public final static String MENSAJE_PROPUESTA_PROYECTO_DE_GRADO_RECHAZADA = "Estimado(a) %,<br /><br />"
            + "Le informamos que su asesor de proyecto de grado ha rechazado su propuesta de tesis."
            + " <br />"
            + " Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para continuar con el proceso.  <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_ENVIAR_PROPUESTA_PROYECTO_DE_GRADO = "[Sisinfo] Enviar Propuesta Proyecto de Grado";
    public final static String HEADER_ENVIAR_PROPUESTA_PROYECTO_DE_GRADO = "Estimado(a) %1$s,<br /><br />"
            + "Le recordamos que como parte del proceso de proyecto de grado, usted debe enviar la propuesta del proyecto de grado. <br />";
    public final static String MENSAJE_ENVIAR_PROPUESTA_PROYECTO_DE_GRADO = "";
    public final static String FOOTER_ENVIAR_PROPUESTA_PROYECTO_DE_GRADO = " <br /> Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para enviar la misma. <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String MENSAJE_COMPLETO_ENVIAR_PROPUESTA_PROYECTO_DE_GRADO = "Estimado(a) %1$s,<br /><br />"
            + "Le recordamos que como parte del proceso de proyecto de grado, usted debe enviar la propuesta del proyecto de grado. <br />"
            + " <br />"
            + " <br /> Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para enviar la misma. <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_GRUPOS_PERSONA_ELIMINADOS_POR_FECHA_VENCIMIENTO = "[Sisinfo] Grupos de Personas Eliminados";
    public final static String MENSAJE_GRUPOS_PERSONA_ELIMINADOS_POR_FECHA_VENCIMIENTO = "Estimado(a) Administrador,<br/><br/>"
            + "Los grupos de personas del semestre se han borrado, según  la fecha configurada<br />"
            + "<br /><br /> Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_SOLUCIONAR_ERROR_SISINFO = "[Sisinfo] Error en módulo";
    public final static String MENSAJE_SOLUCIONAR_ERROR_SISINFO = "Estimado(a) Administrador,<br/><br/>"
            + "Se ha producido un error en el módulo: <b> %1 </b>, método <b> %2 </b>.<br /> A continuación el XML con traza del error: <br /><br /> %3 <br />"
            + "<br /><br /> Cordialmente,<br/>- Sisinfo";
    public final static String ASUNTO_SOLUCIONAR_INCIDENTE_SISINFO = "[Sisinfo] Incidente";
    public final static String MENSAJE_SOLUCIONAR_INCIDENTE_SISINFO = "Estimado(a) Administrador,<br/><br/>"
            + "el usuario <b>%1</b> ha registrado un incidente, a continuación la descripción: <br /><br /> %2"
            + "<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_RESOLUCION_INCIDENTE = "[Sisinfo] Actualización de Estado de Requerimiento";
    public final static String MENSAJE_RESOLUCION_INCIDENTE = "Estimado(a) %1,<br/><br/>"
            + "El estado de su solicitud ha pasado a <b>%2</b> con los siguientes comentarios de parte del administrador:<br />"
            + "<b>%3</b><br /><br />"
            + "Seguiremos atentos de sus dudas y/o comentarios."
            + "<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_FECHA_RETIRO_TESIS = "[Sisinfo] Ultima Fecha Retiro Tesis";
    public final static String MENSAJE_FECHA_RETIRO_TESIS = "Estimado(a) %1,<br /><br />"
            + "Le recordamos que la fecha limite para retirar Tesis es el día <b>%2</b>, en caso de que usted vaya a retirar recuerde que debe hacerlo por también por <b>Banner en registro</b>."
            + "<br /> "
            + "En caso de que retire la tesis, por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para informar al departamento.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_APROBAR_HORARIO_SUSTENTACION_TESIS2_ASESOR = "[Sisinfo] Horario sustentación Tesis 2";
    public final static String MENSAJE_HEADER_APROBAR_HORARIO_SUSTENTACION_TESIS2_ASESOR = "Estimado(a)  %1$s, <br /><br />"
            + "Se ha modificado el horario de sustentación de Tesis 2 Maestría, de los siguientes estudiantes:<br /> ";
    public final static String MENSAJE_FOOTER_APROBAR_HORARIO_SUSTENTACION_TESIS2_ASESOR = "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para aprobar o rechazar los horarios correspondientes. <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_SELECCIONAR_HORARIO_SUSTENTACION_TESIS2_ESTUDIANTE = "[Sisinfo] Horario sustentación Tesis 2 ";
    public final static String MENSAJE_SELECCIONAR_HORARIO_SUSTENTACION_TESIS2_ESTUDIANTE = "Estimado(a) %1,<br /><br />"
            + "Le recordamos que la fecha máxima para sustentar Tesis 2 en el periodo <b> %2 es el día %3 </b> . <br />"
            + " adicionalmente, el horario se debe registrar con 3 días de anticipación a la fecha de la sustentación<br />"
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para seleccionar su horario de sustentación<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_SELECCIONAR_JURADOS_SUSTENTACION_TESIS2_ESTUDIANTE = "[Sisinfo] Jurados sustentación Tesis 2 ";
    public final static String MENSAJE_SELECCIONAR_JURADOS_SUSTENTACION_TESIS2_ESTUDIANTE = "Estimado(a) %1,<br /><br />"
            + "Le recordamos que la fecha máxima para seleccionar sus jurados de sustentación de Tesis 2 en el periodo <b> %2 es el día %3 </b> . <br />"
            + " adicionalmente, los jurados deben ser seleccionados con 1 semana de anticipación a la fecha de la sustentación<br />"
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para seleccionar su horario de sustentación<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_INFORME_30PORCIENTO_PROYECTO_GRADO = "[Sisinfo] Informe 30% Proyecto de Grado";
    public final static String MENSAJE_INFORME_30PORCIENTO_PROYECTO_GRADO = "Estimado(a) %1, <br /><br />"
            + "Su asesor ha enviado el informe del 30% correspondiente al <strong>Proyecto de Grado</strong>: "
            + "<br/>%2 %3<br/><br/> "
            + "Para más información por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a>. <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_INFORME_30PORCIENTO_PROYECTO_GRADO_COORD = "[Sisinfo] Informe 30% Proyecto de Grado - %1";
    public final static String MENSAJE_INFORME_30PORCIENTO_PROYECTO_GRADO_COORD = "Buen día, <br /><br />"
            + "El asesor %1 ha enviado el informe del 30% correspondiente al <strong>Proyecto de Grado</strong>, del estudiante <strong>%2</strong>:<br /> "
            + "<br/> %3 %4 <br/><br/>"
            + "Para más información por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a>. <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_SUBIR_AFICHE_ESTUDIANTE = "[Sisinfo] Recordatorio de subida de afiche - Proyecto de Grado";
    public final static String HEADER_SUBIR_AFICHE_ESTUDIANTE = "Buen día, %1$s,<br /><br />"
            + "Como parte del proceso de seguimiento de <strong>Proyecto de Grado</strong>, recuerde que debe subir el afiche de su propuesta de Proyecto de grado y recibir aprobación de su asesor antes de <strong>%2$s</strong>:<br /> ";
    public final static String MENSAJE_SUBIR_AFICHE_ESTUDIANTE = "Recuerde que tiene hasta el %1$s para enviar su afiche de proyecto de grado.";
    public final static String FOOTER_SUBIR_AFICHE_ESTUDIANTE = "Para más información por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a>. <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String MENSAJE_COMPLETO_SUBIR_AFICHE_ESTUDIANTE = "Buen día, %1$s,<br /><br />"
            + "Como parte del proceso de seguimiento de <strong>Proyecto de Grado</strong>, recuerde que debe subir el afiche de su propuesta de Proyecto de grado y recibir aprobación de su asesor antes de <strong>%2$s</strong>:<br /> "
            + "Para más información por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a>. <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_INFORME_ABET_COORD = "[Sisinfo] Informe ABET Proyecto de Grado - %1";
    public final static String MENSAJE_INFORME_ABET_COORD = "Buen día, <br /><br />"
            + "El asesor %1 ha enviado el documento ABET correspondiente al <strong>Proyecto de Grado</strong>, del estudiante <strong>%2</strong>:<br /> "
            + "Para más información por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a>. <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_CALIFICACION_PROYECTO_GRADO = "[Sisinfo] Calificación Proyecto de Grado ";
    public final static String MENSAJE_CALIFICACION_PROYECTO_GRADO = "Buen día, %1,<br /><br /> Le informamos que su asesor %2 ha colocado la nota correspondiente al Proyecto de Grado: %3. <br /> "
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para conocerla.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_CALIFICACION_PROYECTO_GRADO_COORDINACION = "[Sisinfo] Calificación Proyecto de Grado - %";
    public final static String MENSAJE_CALIFICACION_PROYECTO_GRADO_COORDINACION = "Buen día,<br /><br /> Le informamos que el asesor %1 ha colocado la nota: <strong>%2</strong> correspondiente al <strong>Proyecto de Grado</strong>, del estudiante <strong>%3</strong>. <br /> "
            + "Para más información ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para conocerla.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_SUBIR_TREINTA_POR_CIENTO = "[Sisinfo] Subir 30% Proyecto de Grado";
    public final static String MENSAJE_SUBIR_TREINTA_POR_CIENTO = "<b>%1$s</b>";
    public final static String HEADER_SUBIR_TREINTA_POR_CIENTO = "Estimado(a) %1,<br /><br />"
            + "Le recordamos que como parte del proceso de proyecto de grado, usted debe subir el 30% de los siguientes estudiantes: <br />";
    public final static String FOOTER_SUBIR_TREINTA_POR_CIENTO = " <br /> Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para subirlo. <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String MENSAJE_COMPLETO_SUBIR_TREINTA_POR_CIENTO = "Estimado(a) %1,<br /><br />"
            + "Le recordamos que como parte del proceso de proyecto de grado, usted debe subir el 30% del estudiante %2.<br />"
            + " <br />"
            + " <br /> Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para subirlo. <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_SUBIR_NOTA_PROYECTO_GRADO = "[Sisinfo] Subir Nota Proyecto de Grado";
    public final static String HEADER_SUBIR_NOTA_PROYECTO_GRADO = "Estimado(a) %1$s,<br /><br />"
            + "Le recordamos que como parte del proceso de proyecto de grado, usted debe subir la nota del proyecto de grado. <br />";
    public final static String FOOTER_SUBIR_NOTA_PROYECTO_GRADO = " <br /> Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para subirla. <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String MENSAJE_SUBIR_NOTA_PROYECTO_GRADO = "<b>%1$s</b>";
    public final static String MENSAJE_COMPLETO_SUBIR_NOTA_PROYECTO_GRADO = "Estimado(a) %1$s,<br /><br />"
            + "Le recordamos que como parte del proceso de proyecto de grado, usted debe subir la nota del proyecto de grado del estudiante %2$s ( %3$s ). <br />"
            + " <br />"
            + " <br /> Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para subirla. <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_SUBIR_ARCHIVO_ABET = "[Sisinfo] Subir Documento ABET";
    public final static String HEADER_SUBIR_ARCHIVO_ABET = "Estimado(a) %1$s,<br /><br />"
            + "Le recordamos que como parte del proceso de proyecto de grado, usted debe subir el documento para certificación ABET del proyecto de grado del estudiante %2$s ( %3$s ) . <br />";
    public final static String FOOTER_SUBIR_ARCHIVO_ABET = " <br /> Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para subirlo. <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String MENSAJE_SUBIR_ARCHIVO_ABET = "<b>%1$s</b>";
    public final static String MENSAJE_COMPLETO_SUBIR_ARCHIVO_ABET = "Estimado(a) %1$s,<br /><br />"
            + "Le recordamos que como parte del proceso de proyecto de grado, usted debe subir el documento para certificación ABET del proyecto de grado  del estudiante %2$s ( %3$s ). <br />"
            + " <br />"
            + " <br /> Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para subirlo. <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_INFORME_RETIRO_PROFESOR = "[Sisinfo] Retiro Proyecto de Grado ";
    public final static String MENSAJE_INFORME_RETIRO_PROFESOR = "Buen día, %1,<br /><br /> Le informamos que su estudiante %2 ha retirado Proyecto de Grado. <br /> "
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para conocer el informe de retiro.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_INFORME_RETIRO_ESTUDIANTE = "[Sisinfo] Retiro Proyecto de Grado - BANNER";
    public final static String MENSAJE_INFORME_RETIRO_ESTUDIANTE = "Buen día, %1,<br /><br /> Le informamos que aunque usted haya notificado el retiro de Proyecto de Grado por Sisinfo, "
            + " debe retirar directamente en el sistema BANNER para que sea válido. Recuerde que la notificación por Sisinfo es de carácter informativo únicamente, no le garantiza el retiro de Proyecto de Grado."
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para conocer el informe de retiro.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_INFORME_RETIRO_COORD = "[Sisinfo] Retiro Proyecto de Grado ";
    public final static String MENSAJE_INFORME_RETIRO_COORD = "Buen día,<br /><br /> Le informamos que el estudiante %1 ha retirado Proyecto de Grado. Su asesor era: %2 <br /> "
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para conocer el informe de retiro.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_ENVIAR_INFORME_RETIRO_ESTUDIANTE = "[Sisinfo] Ultima Fecha Retiro Proyecto de Grado ";
    public final static String HEADER_ENVIAR_INFORME_RETIRO_ESTUDIANTE = "Estimado(a) %1$s,<br /><br />"
            + "Le recordamos que como parte del proceso de proyecto de grado, usted debe enviar el informe de retiro de proyecto de grado. <br />";
    public final static String FOOTER_ENVIAR_INFORME_RETIRO_ESTUDIANTE = " <br /> Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para enviarlo. <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String MENSAJE_ENVIAR_INFORME_RETIRO_ESTUDIANTE = "";
    public final static String MENSAJE_COMPLETO_ENVIAR_INFORME_RETIRO_ESTUDIANTE = "Estimado(a) %1$s,<br /><br />"
            + "Le recordamos que como parte del proceso de proyecto de grado, usted debe enviar el informe de retiro de proyecto de grado. <br />"
            + " <br />"
            + " <br /> Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para enviarlo. <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_ENVIAR_INFORME_PENDIENTE_PROFESOR = "[Sisinfo] Informe Pendiente Proyecto de Grado ";
    public final static String HEADER_ENVIAR_INFORME_PENDIENTE_PROFESOR = "Estimado(a) %1$s,<br /><br />"
            + "Le recordamos que como parte del proceso de proyecto de grado, usted debe enviar el informe de pendiente de proyecto de grado. <br />";
    public final static String MENSAJE_ENVIAR_INFORME_PENDIENTE_PROFESOR = "";
    public final static String FOOTER_ENVIAR_INFORME_PENDIENTE_PROFESOR = " <br /> Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para enviarlo. <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String MENSAJE_COMPLETO_ENVIAR_INFORME_PENDIENTE_PROFESOR = "Estimado(a) %1$s,<br /><br />"
            + "Le recordamos que como parte del proceso de proyecto de grado, usted debe enviar el informe de pendiente de proyecto de grado. <br />"
            + " <br />"
            + " <br /> Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para enviarlo. <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_ENVIAR_INFORME_PENDIENTE_COORD = "[Sisinfo] Aprobación Pendiente Proyecto de Grado ";
    public final static String HEADER_ENVIAR_INFORME_PENDIENTE_COORD = "Estimado(a),<br /><br />"
            + "Le recordamos que como parte del proceso de proyecto de grado, usted debe aprobar las solicitudes de pendiente de proyecto de grado. <br />";
    public final static String FOOTER_ENVIAR_INFORME_PENDIENTE_COORD = " <br /> Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para enviarlo. <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String MENSAJE_ENVIAR_INFORME_PENDIENTE_COORD = "<b>%1$s</b>";
    public final static String MENSAJE_COMPLETO_ENVIAR_INFORME_PENDIENTE_COORD = "Estimado(a),<br /><br />"
            + "Le recordamos que como parte del proceso de proyecto de grado, usted debe aprobar la solicitud de pendiente del proyecto de grado del estudiante <b>%1$s</b>. <br />"
            + " <br />"
            + " <br /> Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para enviarlo. <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_ENVIAR_INFORME_PENDIENTE_ESPECIAL_COORD = "[Sisinfo] Aprobación Pendiente Especial Proyecto de Grado ";
    public final static String MENSAJE_COMPLETO_ENVIAR_INFORME_PENDIENTE_ESPECIAL_COORD = "Estimado(a),<br /><br />"
            + "Le recordamos que como parte del proceso de proyecto de grado, usted debe aprobar la solicitud de pendiente especial del proyecto de grado del estudiante <b>%1$s</b>. <br />"
            + " <br />"
            + " <br /> Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para enviarlo. <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String HEADER_ENVIAR_INFORME_PENDIENTE_ESPECIAL_COORD = "Estimado(a),<br /><br />"
            + "Le recordamos que como parte del proceso de proyecto de grado, usted debe aprobar las solicitudes de pendiente especial de proyecto de grado. <br />";
    public final static String FOOTER_ENVIAR_INFORME_PENDIENTE_ESPECIAL_COORD = " <br /> Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para enviarlo. <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String MENSAJE_ENVIAR_INFORME_PENDIENTE_ESPECIAL_COORD = "<b>%1$s</b>";
    public final static String ASUNTO_SOLICITUD_PENDIENTE_PROFESOR = "[Sisinfo] Pendiente Proyecto de Grado - Por Aprobar";
    public final static String MENSAJE_SOLICITUD_PENDIENTE_PROFESOR = "Buen día %1,<br /><br /> Le informamos que su solicitud de pendiente de proyecto de grado para el estudiante %2 "
            + "se encuentra en estado por aprobación de coordinación. Una vez sea aprobada su solicitud se le informará por correo."
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para conocer el estado de su solicitud.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_SOLICITUD_PENDIENTE_ESPECIAL_PROFESOR = "[Sisinfo] Pendiente Especial Proyecto de Grado - Por Aprobar";
    public final static String MENSAJE_SOLICITUD_PENDIENTE_ESPECIAL_PROFESOR = "Buen día %1,<br /><br /> Le informamos que su solicitud de pendiente especial de proyecto de grado para el estudiante %2 "
            + "se encuentra en estado por aprobación de coordinación. Una vez sea aprobada su solicitud se le informará por correo."
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para conocer el estado de su solicitud.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_SOLICITUD_PENDIENTE_COORD = "[Sisinfo] Solicitud Pendiente Proyecto de Grado";
    public final static String MENSAJE_SOLICITUD_PENDIENTE_COORD = "Buen día,<br /><br /> Le informamos que el profesor %1 ha solicitado pendiente de proyecto de grado para el estudiante %2."
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para consultarla y evaluar su aprobación.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_SOLICITUD_PENDIENTE_ESPECIAL_COORD = "[Sisinfo] Solicitud Pendiente Especial Proyecto de Grado";
    public final static String MENSAJE_SOLICITUD_PENDIENTE_ESPECIAL_COORD = "Buen día,<br /><br /> Le informamos que el profesor %1 ha solicitado pendiente especial de proyecto de grado para el estudiante %2."
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para consultarla y evaluar su aprobación.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_ENVIAR_APROBACION_PENDIENTE_PROFESOR = "[Sisinfo] Aprobación Pendiente Proyecto de Grado ";
    public final static String MENSAJE_ENVIAR_APROBACION_PENDIENTE_PROFESOR = "Buen día,<br /><br />"
            + "Le recordamos que como parte del proceso de proyecto de grado, usted debe enviar la aprobación o desaprobación de pendiente de proyecto de grado. <br />"
            + " <br />"
            + " <br /> Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para enviarlo. <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_APROBACION_PENDIENTE_COORD = "[Sisinfo] Estado Aprobación Pendiente Proyecto de Grado";
    public final static String MENSAJE_APROBACION_PENDIENTE_COORD = "Estimado(a) %1,<br /><br /> Le informamos que su solicitud de pendiente de proyecto de grado para el estudiante %2 %3 ha sido aprobada."
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para consultarla.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_APROBACION_HORARIO_SUSTENTACION_TESIS2_ESTUDIANTE = "[Sisinfo] Aprobado el horario de sustentación Tesis 2 ";
    public final static String MENSAJE_APROBACION_HORARIO_SUSTENTACION_TESIS2_ESTUDIANTE = "Estimado(a) %1,<br /><br />"
            + "Le informamos que su asesor ha aprobado su horario de el día <b>%2 </b>. <br /><br />"
            + "Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_FECHA_30_PORCIENTO_PERSONAS_PENDIENTES_POR_NOTA = "[Sisinfo] Reporte Tesis sin nota 30%";
    public final static String MENSAJE_FECHA_30_PORCIENTO_PERSONAS_PENDIENTES_POR_NOTA = "Estimado(a) %1,<br /><br />"
            + "A continuación presentamos un informe de las tesis a las cuales no les dieron nota de 30% en el periodo %3 : \n </b> . <br />"
            + "%2"
            + "<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_PERSONAS_PENDIENTES_POR_APROBAR_INSCRIPCION_TESIS_1_POR_COORDINACION = "[Sisinfo] Inscripción Tesis 1";
    public final static String MENSAJE_ASUNTO_PERSONAS_PENDIENTES_POR_APROBAR_INSCRIPCION_TESIS_1_POR_COORDINACION = "Estimado(a) %1,<br /><br />"
            + "Le recordamos que como parte del proceso de Tesis 1, usted debe aprobar la inscripción  de los candidatos.<br />"
            + " a la fecha hacen falta por aprobar las siguientes personas:<br /> %2"
            + " <br /> Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para aprobarlos. <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_PERSONAS_PENDIENTES_POR_APROBAR_INSCRIPCION_TESIS_1_POR_ASESOR_A_ASESOR = "[Sisinfo] Inscripción Tesis 1";
    public final static String MENSAJE_ASUNTO_PERSONAS_PENDIENTES_POR_APROBAR_INSCRIPCION_TESIS_1_POR_ASESOR_A_ASESOR = "Estimado(a) %1,<br /><br />"
            + "Le recordamos que como parte del proceso de Tesis 1, usted debe aprobar o rechazar la inscripción  de los estudiantes.<<br />"
            + "le informamos que la fecha máxima ya paso y quedaron pendiente por aceptar los siguientes estudiantes:  <br /> %2"
            + "<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_PERSONAS_PENDIENTES_POR_APROBAR_INSCRIPCION_TESIS_1_POR_ASESOR_A_COORDINACION = "[Sisinfo] Inscripción Tesis 1";
    public final static String MENSAJE_ASUNTO_PERSONAS_PENDIENTES_POR_APROBAR_INSCRIPCION_TESIS_1_POR_ASESOR_A_COORDINACION = "Estimado(a) %1,<br /><br />"
            + "Le recordamos que el plazo para que los asesores aprueben la inscripción a tesis 1 a finalizado. <br />"
            + " A la fecha los siguientes asesores tienen inscripciones pendientes por aprobar/rechazar:<br /> %2"
            + " <br /> Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a>"
            + " si desea modificar las fechas del proceso. <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_PERSONAS_PENDIENTES_POR_APROBAR_INSCRIPCION_SUBAREA_INVESTIGACION_POR_COORDINADOR_SUBAREA = "[Sisinfo] Registro de perfil";
    public final static String MENSAJE_PERSONAS_PENDIENTES_POR_APROBAR_INSCRIPCION_SUBAREA_INVESTIGACION_POR_COORDINADOR_SUBAREA = "Estimado(a) %1,<br /><br />"
            + "Le recordamos que como parte del proceso de tesis, usted debe aprobar o rechazar el plan de estudios de los estudiantes.<<br />"
            + "le informamos que la fecha máxima ya paso y quedaron pendiente por aceptar los siguientes estudiantes:  <br /> %2"
            + "<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_PERSONAS_PENDIENTES_POR_APROBAR_INSCRIPCION_SUBAREA_INVESTIGACION_POR_ASESOR = "[Sisinfo] Registro plan de estudios";
    public final static String MENSAJE_PERSONAS_PENDIENTES_POR_APROBAR_INSCRIPCION_SUBAREA_INVESTIGACION_POR_ASESOR = "Estimado(a) %1,<br /><br />"
            + "Le recordamos que como parte del proceso de tesis, usted debe aprobar o rechazar el plan de estudios de los estudiantes.<<br />"
            + "le informamos que la fecha máxima ya paso y quedaron pendiente por aceptar los siguientes estudiantes:  <br /> %2"
            + "<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_PERSONAS_PENDIENTES_POR_APROBAR_INSCRIPCION_TESIS_2_POR_ASESOR = "[Sisinfo] Inscripciones Tesis 2 Pendientes";
    public final static String MENSAJE_PERSONAS_PENDIENTES_POR_APROBAR_INSCRIPCION_TESIS_2_POR_ASESOR = "Estimado(a) %1,<br /><br />"
            + "Le recordamos que como parte del proceso de inscripción a Tesis 2, usted debe aprobar o rechazar la inscripción  de los estudiantes.<<br />"
            + "le informamos que la fecha máxima ya paso y quedaron pendiente por aceptar los siguientes estudiantes:  <br /> %2"
            + "<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_RECHAZO_HORARIO_SUSTENTACION_TESIS2_ESTUDIANTE = "[Sisinfo] Rechazado el horario de sustentación Tesis 2 ";
    public final static String MENSAJE_RECHAZO_HORARIO_SUSTENTACION_TESIS2_ESTUDIANTE = "Estimado(a) %1,<br /><br />"
            + "Le informamos que su asesor ha rechazado su horario de el día %2 </b> . <br />"
            + "<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_CALIFICAR_SUSTENTACION_TESIS2_ESTUDIANTE = "[Sisinfo] Calificar sustentación Tesis 2 ";
    public final static String MENSAJE_FOOTER_CALIFICAR_HORARIO_SUSTENTACION_TESIS2_ESTUDIANTE = " <br />"
            + " <br /> Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para enviar la calificacion. <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String MENSAJE_HEADER_CALIFICAR_HORARIO_SUSTENTACION_TESIS2_ESTUDIANTE = "Estimado(a)  %1$s,<br /><br />"
            + "Le recordamos que Ud. ha sido seleccionado <b>jurado de sustentación</b> de la tesis del estudiante ";
    public final static String MENSAJE_BULLET_CALIFICAR_HORARIO_SUSTENTACION_TESIS2_ESTUDIANTE = " %1$s  la cual tiene fecha de sustentación <b>%2$s en el salón:  %3$s</b>. <br />";
    public final static String ASUNTO_CALIFICAR_SUSTENTACION_TESIS2_ESTUDIANTE_JURADO_EXTERNO = "[Sisinfo] Calificar sustentación Tesis 2 ";
    public final static String MENSAJE_CALIFICAR_HORARIO_SUSTENTACION_TESIS2_JURADO_EXTERNOE = "<style>p.estiloCajaConfirm{border-style:solid;border-width:1px;border-color:gray;padding:10px 0px 10px 10px;width:300px;}</style>"
            + "Estimado(a) %1,<br /><br />"
            + "Para calificar la sustentación de Tesis del estudiante %2.<br />"
            + "La cual se llevara a cabo %3 en el salón %4  <br />"
            + "<b>Por favor ingrese a la plantilla de sustentación a través del link:     </b><br />"
            + "<p style=\"border-style:solid;border-width:1px;border-color:gray;padding:10px 0px 10px 10px;width:300px;\"><b>Plantilla de Calificación:</b><br />"
            + "&nbsp;&nbsp;&nbsp;<a href=%5 target=\"_blank\">Ir Plantilla de Calificaci&oacute;n</a>&nbsp;&nbsp;&nbsp;&nbsp;"
            + "</p><br />"
            + "<br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_CALIFICACION_TESIS_2_SIN_SUSTENTACION = "[Sisinfo]  Calificación Automática Tesis 2  ";
    public final static String MENSAJE_CALIFICACION_TESIS_2_SIN_SUSTENTACION = "Buen día, %1,<br /><br /> Le informamos que la fecha maxima para sustentaciones ya paso.<br /> Razón por la cual el sistema ha colocado la nota de forma automática.<br /> "
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para conocerla.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_SOLICITUD_PENDIENTE_ESPECIAL_TESIS2_COORDINACION = "[Sisinfo] Solicitud Pendiente Especial Tesis 2";
    public final static String MENSAJE_HEADER_SOLICITUD_PENDIENTE_ESPECIAL_TESIS2_COORDINACION = "Buen día, <br /><br /> Le informamos que se han solicitado los siguientes pendientes especiales dentro del proceso de Tesis 2 :<br />";
    public final static String MENSAJE_FOOTER_PENDIENTE_ESPECIAL_TESIS2_COORDINACION = "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" "
            + "target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para dar respuesta a las solitudes. <br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_APROBACION_PENDIENTE_ESPECIAL_TESIS2_ESTUDIANTE = "[Sisinfo] Resultado Solicitud Pendiente Especial Tesis 2 ";
    public final static String MENSAJE_APROBACION_PENDIENTE_ESPECIAL_TESIS2_ESTUDIANTE = "Buen día, %1,<br /><br /> Le informamos que se ha aprobado su solicitud de pendiente especial para Tesis 2. <br/>"
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para mas información.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_APROBACION_PENDIENTE_ESPECIAL_TESIS2_ASESOR = "[Sisinfo] Resultado solicitud Pendiente Especial Tesis 2 - %";
    public final static String MENSAJE_APROBACION_PENDIENTE_ESPECIAL_TESIS2_ASESOR = "Buen día,<br /><br /> Le informamos que la solicitud de pendiente especial de Tesis 2 del estudiante %1 se ha aprobado. "
            + "<br/>"
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para mas información..<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_APROBACION_PENDIENTE_ESPECIAL_TESIS2_COORDINACION = "[Sisinfo] Resultado solicitud Pendiente Especial Tesis 2 - % ";
    public final static String MENSAJE_APROBACION_PENDIENTE_ESPECIAL_TESIS2_COORDINACION = "Buen día,<br /><br /> Le confirmamos la aprobación de la solicitud de pendiente especial de Tesis 2, del estudiante %2 asesorado(a) por %1. <br /> "
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para mas información.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_RECHAZO_PENDIENTE_ESPECIAL_TESIS2_ESTUDIANTE = "[Sisinfo] Resultado Solicitud Pendiente Especial Tesis 2 ";
    public final static String MENSAJE_RECHAZO_PENDIENTE_ESPECIAL_TESIS2_ESTUDIANTE = "Buen día, %1,<br /><br /> Le informamos que se ha rechazado su solicitud de pendiente especial para Tesis 2. "
            + "Por favor comuníquese con la coordinación para más información.<br/>"
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para mas información.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_RECHAZO_PENDIENTE_ESPECIAL_TESIS2_ASESOR = "[Sisinfo] Resultado Solicitud Pendiente Especial Tesis 2  - %";
    public final static String MENSAJE_RECHAZO_PENDIENTE_ESPECIAL_TESIS2_ASESOR = "Buen día,<br /><br /> Le informamos que la solicitud de pendiente especial  de Tesis 2 del estudiante %1 se ha rechazado. "
            + "Por favor comuníquese con la coordinación para más información.<br/>"
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para mas información.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_RECHAZO_PENDIENTE_ESPECIAL_TESIS2_COORDINACION = "[Sisinfo] Resultado SolicitudPendiente Especial Tesis 2  - % ";
    public final static String MENSAJE_RECHAZO_PENDIENTE_ESPECIAL_TESIS2_COORDINACION = "Buen día,<br /><br /> Le confirmamos el rechazo de la solicitud de pendiente espécial  de Tesis 2, del estudiante %2 asesorado(a) por %1. <br /> "
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para mas información.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_MODIFICACION_NOTA_SUSTENTACION_TESIS2_JURADO = "[Sisinfo] Modificación Calificación  Jurado Sustentación Tesis 2  1% ";
    public final static String MENSAJE_MODIFICACION_NOTA_SUSTENTACION_TESIS2_JURADO = "Buen Día  %1, <br /><br />"
            + "Le informamos que el jurado:  %2 con correo  %3 ha modificado la calificación de sustentación del estudiante  %4.<br/><br/> "
            + "Cordial Saludo,<br/> -sisinfo";
    public final static String ASUNTO_ASIGNAR_INCIDENTE = "[Sisinfo] Se ha asignado un incidente";
    public final static String MENSAJE_ASIGNAR_INCIDENTE = "Estimado(a) %1,<br/><br/>"
            + "Se le ha asignado la solicitud <b>%2</b> para su resolucion.<br />"
            + "Seguiremos atentos de sus dudas y/o comentarios."
            + "<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_SOLICITUD_PENDIENTE_RECHAZADA_PROFESOR = "[Sisinfo] Pendiente Proyecto de Grado - Rechazada";
    public final static String MENSAJE_SOLICITUD_PENDIENTE_RECHAZADA_PROFESOR = "Buen día %1,<br /><br /> Le informamos que su solicitud de pendiente de proyecto de grado para el estudiante %2 "
            + "ha sido rechazada."
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para conocer el estado de su solicitud.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_SOLICITUD_PENDIENTE_APROBADA_PROFESOR = "[Sisinfo] Pendiente Proyecto de Grado - Aprobada";
    public final static String MENSAJE_SOLICITUD_PENDIENTE_APROBADA_PROFESOR = "Buen día %1,<br /><br /> Le informamos que su solicitud de pendiente de proyecto de grado para el estudiante %2 "
            + "ha sido aprobada."
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para conocer el estado de su solicitud.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_SOLICITUD_PENDIENTE_ESPECIAL_RECHAZADA_PROFESOR = "[Sisinfo] Pendiente Especial Proyecto de Grado - Rechazada";
    public final static String MENSAJE_SOLICITUD_PENDIENTE_ESPECIAL_RECHAZADA_PROFESOR = "Buen día %1,<br /><br /> Le informamos que su solicitud de pendiente especial de proyecto de grado para el estudiante %2 "
            + "ha sido rechazada."
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para conocer el estado de su solicitud.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_SOLICITUD_PENDIENTE_ESPECIAL_APROBADA_PROFESOR = "[Sisinfo] Pendiente Especial Proyecto de Grado - Aprobada";
    public final static String MENSAJE_SOLICITUD_PENDIENTE_ESPECIAL_APROBADA_PROFESOR = "Buen día %1,<br /><br /> Le informamos que su solicitud de pendiente especial de proyecto de grado para el estudiante %2 "
            + "ha sido aprobada."
            + "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para conocer el estado de su solicitud.<br /><br />Cordialmente,<br />- Sisinfo";

    /* Notificacones crear alertas. */
    /*TODO: mejorar*/
    public final static String ASUNTO_ALERTA_PRESELECCION_MONITOR = "[Sisinfo] Preseleccion monitor";
    public final static String MENSAJE_ALERTA_PRESELECCION_MONITOR = "Se abre la preselccion de monitores";
    public final static String ASUNTO_ALERTA_CONFIRMACION_ESTUDIANTE = "[Sisinfo] Confirmacion estudiante";
    public final static String MENSAJE_ALERTA_CONFIRMACION_ESTUDIANTE = "Se abre la confirmacon de estudiantes";
    public final static String ASUNTO_ALERTA_ACTUALIZIACION_ESTUDIANTE = "[Sisinfo] Actualizacion informacion estudiante";
    public final static String MENSAJE_ALERTA_ACTUALIZIACION_ESTUDIANTE = "Es tiempo de actualizar informacion de estudiantes";
    public final static String ASUNTO_ALERTA_CONVENIO_ESTUDIANTE = "[Sisinfo] Convenios estudiantes";
    public final static String MENSAJE_ALERTA_CONVENIO_ESTUDIANTE = "Es tiempo de formalizar convenios de estudiantes";
    public final static String ASUNTO_ALERTA_CONVENIO_ESTUDIANTE_FIRMA = "[Sisinfo] Firmas estudiantes";
    public final static String MENSAJE_ALERTA_CONVENIO_ESTUDIANTE_FIRMA = "Es tiempo que firmen los estudiantes";
    public final static String ASUNTO_PRESELECCION_MONITOR_SECCION = "[Sisinfo] Selección Monitores";
    public final static String HEADER_PRESELECCION_MONITOR_SECCION = "Estimado(a) profesor(a),<br /><br /> Le recordamos que Ud. aún tiene monitores pendientes por seleccionar en los siguientes cursos:";
    public final static String MENSAJE_PRESELECCION_MONITOR_SECCION = "%1$s monitor(es) para el curso: <b>%2$s</b> sección: <b>%3$d</b> ";
    public final static String FOOTER_PRESELECCION_MONITOR_SECCION = "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para seleccionar a su(s) candidato(s).<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String MENSAJE_REGLAS_MONITORIAS = "Le recordamos que las reglas para aplicar a una monitoria son las siguientes:"
            + "<ul>"
            + "<li style=\"text-align:left\"> El promedio acumulado debe ser mayor o igual a %1$s </li>"
            + "<li style=\"text-align:left\"> El estudiante no puede tener mas de %2$s monitorias con el departamento</li>"
            + "<li style=\"text-align:left\"> Si el promedio del estudiante es inferior a %3$s, debe tener %4$s creditos como máximo</li>"
            + "<li style=\"text-align:left\"> Si el promedio del estudiante es superior a %3$s, debe tener %5$s creditos como máximo</li>"
            + "</ul>"
            + "Tome en cuenta que si su solicitud no cumple estas reglas, podría ser rechazada por coordinación.<br />";
    public final static String ASUNTO_CAMBIO_RANGO_FECHAS = "[Sisinfo] Cambio rango fechas";
    public final static String MENSAJE_CAMBIO_RANGO_FECHAS = "Estimado Sisinfo <br /> <br /> El rango de fechas para el periodo actual ha sido modificado. Por favor verifique el estado de la convocatoria.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_SUBIR_NOTAS_MONITOR = "Subir notas monitores";
    public final static String HEADER_SUBIR_NOTAS_MONITOR = "Estimado profesor <br> Le recordamos hace falta subir las notas de los siguientes monitores:<br>";
    public final static String FOOTER_SUBIR_NOTAS_MONITOR = " Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para ingresar las notas faltantes.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String MENSAJE_SUBIR_NOTAS_MONITOR = "%1$s del curso: %2$s";
    //public final static String MENSAJE_SUBIR_NOTAS_MONITOR = "Se debe subir la nota para el monitor %s " +
    //                            "con e-mail %s del curso: %s-%s";
    public final static String ASUNTO_PROYECTO_GRADO_REPROBADO_ESTUDIANTE = "[Sisinfo]Proyecto de grado reprobado";
    public final static String MENSAJE_PROYECTO_GRADO_REPROBADO_ESTUDIANTE = "Buen día, %s,<br /><br />"
            + "Le informamos que su proyecto de grado realizado con el profesor %s fue reprobado con una nota final de %s."
            + "<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_PROYECTO_GRADO_REPROBADO_COORDINACION = "[Sisinfo]Proyecto de grado de %s reprobado";
    public final static String MENSAJE_PROYECTO_GRADO_REPROBADO_COORDINACION = "Buen día,<br /><br />"
            + "Le informamos que el proyecto de grado del estudiante %s fue reprobado por el profesor con una nota final de %s."
            + "<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_PROYECTO_GRADO_REPROBADO_PROFESOR = "[Sisinfo]Proyecto de grado de %s reprobado";
    public final static String MENSAJE_PROYECTO_GRADO_REPROBADO_PROFESOR = "Buen día,<br /><br />"
            + "Le confirmamos que el proyecto de grado del estudiante %s fue reprobado con una nota final de %s."
            + "<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_PETICION_ELIMINADA_POR_SECCION_ELIMINADA = "[Sisinfo]Petición conflicto horario cancelada por sección cancelada";
    public final static String MENSAJE_PETICION_ELIMINADA_POR_SECCION_ELIMINADA = "Estimado(a)  %1, <br /><br />"
            + "Su petición de %2 la sección con CRN %3 del curso %4 ha sido cancelada debido a que esta la sección fue eliminada"
            + " <br />"
            + "<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_HORARIO_SUSTENTACION_TESIS2_ASIGNADO_SIN_JURADOS = "[Sisinfo]Asignación salon sustentación tesis 2 sin jurados asignados";
    public final static String MENSAJE_HORARIO_SUSTENTACION_TESIS2_ASIGNADO_SIN_JURADOS = "Buen día, <br /><br />"
            + "Le informamos que ha sido asignado salón para la sustentación de tesis de maestría del estudiante %1, sin embargo aún no se ha elegido %2"
            + " <br />"
            + "<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_MIGRACION_TESIS_2_TERMINADAS_A_HISTORICOS_SIN_TESISES = "[Sisinfo]Migración tesis 2 terminadas a historicos %1";
    public final static String MENSAJE_MIGRACION_TESIS_2_TERMINADAS_A_HISTORICOS_SIN_TESISES = "Buen día, <br /><br />"
            + "Le informamos que la migración de las tesis 2 terminadas a históricos fue ejecutada, sin embargo %1, "
            + "por tal razon el timmer ha sido creado nuevamente."
            + " <br />"
            + "<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_MIGRACION_TESIS_A_HISTORICOS_PROBLEMA = "[Sisinfo]Migración tesis %1 a historicos %2";
    public final static String MENSAJE_MIGRACION_TESIS_A_HISTORICOS_PROBLEMA = "Buen día, <br /><br />"
            + "Le informamos que la migración de la tesis %1 a histórico del estudiante %2 fue ejecutada, sin embargo %3, ."
            + " <br />"
            + "<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_MIGRACION_TESIS_2_TERMINADAS_A_HISTORICOS_INFORMACION_GENERAL = "[Sisinfo]Migración tesis 2 terminadas a historicos %1";
    public final static String MENSAJE_MIGRACION_TESIS_2_TERMINADAS_A_HISTORICOS_INFORMACION_GENERAL = "Buen día, <br /><br />"
            + "Le informamos que la migración de las tesis 2 terminadas a históricos para el periodo %1 fue ejecutada, %2."
            + " <br />"
            + "<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String DESCRIPCION_TAREA_APROBAR_INSCRIPCION_PG_COORDINACION = "El coordinador debe verificar si las inscripciones a proyecto de grado de los estudiantes <br>%1$s<br>son validas y debe aceptarlas o rechazarlas";

    ;
    public final static String ASUNTO_MIGRACION_TESIS_2_RETIRADAS_A_HISTORICOS_SIN_TESISES = "[Sisinfo]Migración tesis 2 retiradas a historicos %1";
    public final static String MENSAJE_MIGRACION_TESIS_2_RETIRADAS_A_HISTORICOS_SIN_TESISES = "Buen día, <br /><br />"
            + "Le informamos que la migración de las tesis 2 retiradas a históricos fue ejecutada, sin embargo %1, "
            + "por tal razon el timmer ha sido creado nuevamente."
            + " <br />"
            + "<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_CREACION_AUTOMATICA_ALERTA = "[Sisinfo]Creación automática de alerta";
    public final static String MENSAJE_CREACION_AUTOMATICA_ALERTA = "Estimado(a) Administrador,<br><br>"
            + "Se ha creado una alerta de tipo <b>%1$s</b> de manera automática. Por favor ingrese a sisinfo para activar y completar la información de la alerta."
            + "<br><br>Cordialmente,<br>- Sisinfo";
    public final static String FOOTER_GENERAL = "<br /><br />Cordialmente,<br />-Sisinfo";
    public final static String ASUNTO_ERROR_EJECUCION_ALERTA = "[Sisinfo]Error en la ejecucion de la alerta";
    public final static String MENSAJE_ERROR_EJECUCION_ALERTA = "Estimado(a) Administrador,<br><br>"
            + "Ha ocurrido un error con la ejecución de la alerta de tipo %s. La causa del error es la siguiente:<br><br>%s<br>"
            + "Por favor tome la acción adecuada para solucionar este inconveniente."
            + "<br><br>Cordialmente,<br>- Sisinfo";
    /**
     * Bolsa de Empleo
     * */
    public final static String ASUNTO_CALIFICAR_ASISTENCIA_GRADUADA_PROFESOR = "[Sisinfo] Calificación asistencias graduadas";
    public final static String HEADER_CALIFICAR_ASISTENCIA_GRADUADA_PROFESOR = "Estimado(a) %1$s,<br /> Recuerde que como parte del proceso de seguimiento de las <strong>Asistencias Graduadas</strong> del Departamento,"
            + " usted debe calificar a los siguientes asistentes graduados: <br />";
    public final static String FOOTER_CALIFICAR_ASISTENCIA_GRADUADA_PROFESOR = "<br />Por favor ingrese a la sección de tareas de su cuenta para calificar a sus asistentes graduados."
            + "<br /><br />Cordialmente,<br />-Sisinfo";
    public final static String MENSAJE_COMPLETO_CALIFICAR_ASISTENCIA_GRADUADA_PROFESOR = "Estimado(a) %1$s,<br />"
            + "Recuerde que como parte del proceso de seguimiento de las <strong>Asistencias Graduadas<strong> del Departamento,"
            + " usted debe calificar al asistente graduado </b> %2$s <br />"
            + "Por favor ingrese a la sección de tareas de su cuenta para calificar a su asistente graduado."
            + "<br />Cordialmente,<br />- Sisinfo";
    public final static String MENSAJE_CALIFICAR_ASISTENCIA_GRADUADA_PROFESOR = "%1$s";
    public final static String ASUNTO_CREACION_NUEVA_OFERTA = "[Sisinfo] Nueva oferta creada";
    public final static String MENSAJE_CREACION_NUEVA_OFERTA = "Estimados estudiantes, <br /><br />"
            + "Se ha creado una nueva oferta para %1$s en el proyecto %2$s. Para ver más información ingrese a la página de Sisinfo accesible desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> <br /> <br />"
            + "Si está interesado, usted debe primero actualizar su hoja de vida en el sistema y aplicar a través de Sisinfo. Para mayor información sobre la oferta escriba al correo %3$s.<br /><br />"
            + "Cordialmente, <br /><br />-Sisinfo";
    public final static String ASUNTO_NOTIFICAR_VENCIMIENTO_OFERTA = "[Sisinfo] Oferta próxima a vencer";
    public final static String MENSAJE_NOTIFICAR_VENCIMIENTO_OFERTA = "Estimado(a) %1$s,<br /><br />"
            + "La oferta detallada a continuación vence mañana y será eliminada del sistema:<br />"
            + "<b>Nombre:</b> %2$s <br />"
            + "<b>Titulo:</b> %3$s <br />"
            + "<b>Descripción:</b> %4$s <br /><br />"
            + "Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_NOTIFICAR_VENCIMIENTO_OFERTA_SISINFO = "[Sisinfo] Notificar profesor vencimiento oferta (FAIL)";
    public final static String MENSAJE_NOTIFICAR_VENCIMIENTO_OFERTA_SISINFO = "OfertaBean / notificarProfesorVencimientoOferta"
            + " falló con idOferta: %1$s <br /><br />"
            + "Excepción: %2$s";
    public final static String ASUNTO_ELIMINAR_OFERTA_POR_VENCIMIENTO_SISINFO = "[Sisinfo] Eliminar oferta por vencimiento (FAIL)";
    public final static String MENSAJE_ELIMINAR_OFERTA_POR_VENCIMIENTO_SISINFO = "OfertaBean / eliminarOfertaPorVencimiento"
            + " falló con idOferta: %1$s <br /><br />"
            + "Excepción: %2$s";
    public final static String ASUNTO_ASPIRANTE_APLICA_A_OFERTA = "[Sisinfo]  Nuevo Aspirante a Oferta";
    public final static String MENSAJE_ASPIRANTE_APLICA_A_OFERTA = "Estimado(a) %1$s,<br /><br />"
            + "En el archivo adjunto se encuentra la hoja de vida del aspirante interesado en la oferta: <b>%2$s</b>, con periodo de vinculación: %3$s.<br />"
            + "<br /><br /> Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_NOTIFICAR_ESTUDIANTE_CITA_COORDINACION = "[Sisinfo] Recordatorio - Cita con La Coordinación";
    public final static String MENSAJE_NOTIFICAR_ESTUDIANTE_CITA_COORDINACION = "Estimado(a) %1$s,<br /><br />"
            + "Le recordamos que usted reservó una cita con La Coordinación con la siguiente información:<br />"
            + "<b>Fecha:</b> %2$s <br />"
            + "<b>Motivo:</b> %3$s <br />"
            + "Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_NOTIFICAR_ESTUDIANTE_CITA_COORDINACION_SISINFO = "[Sisinfo] Recordatorio - Cita con La Coordinación (FAIL)";
    public final static String MENSAJE_NOTIFICAR_ESTUDIANTE_CITA_COORDINACION_SISINFO = "ReservasBean / notificarEstudianteReserva"
            + " falló con idCita: %1$s <br /><br />"
            + "Excepción: %2$s";
    public final static String ASUNTO_MIGRACION_TESIS_2_PERDIDAS_A_HISTORICOS_SIN_TESISES = "[Sisinfo]Migración tesis 2 perdidas a historicos %1";
    public final static String ASUNTO_MIGRACION_TESIS_1_RETIRADAS_A_HISTORICOS_SIN_TESISES = "[Sisinfo]Migración tesis 1 retiradas a historicos %1";
    public final static String MENSAJE_MIGRACION_TESIS_1_RETIRADAS_A_HISTORICOS_SIN_TESISES = "Buen día, <br /><br />"
            + "Le informamos que la migración de las tesis 1 retiradas a históricos fue ejecutada, sin embargo %1, "
            + "por tal razon el timmer ha sido creado nuevamente."
            + " <br />"
            + "<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_MIGRACION_TESIS_1_PERDIDAS_A_HISTORICOS_SIN_TESISES = "[Sisinfo]Migración tesis 1 perdidas a historicos %1";
    public final static String MENSAJE_MIGRACION_TESIS_1_PERDIDAS_A_HISTORICOS_SIN_TESISES = "Buen día, <br /><br />"
            + "Le informamos que la migración de las tesis 1 perdidas a históricos fue ejecutada, sin embargo %1, "
            + "por tal razon el timmer ha sido creado nuevamente."
            + " <br />"
            + "<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String MENSAJE_MIGRACION_TESIS_2_PERDIDAS_A_HISTORICOS_SIN_TESISES = "Buen día, <br /><br />"
            + "Le informamos que la migración de las tesis 2 perdidas a históricos fue ejecutada, %1, "
            + "por tal razon el timmer ha sido creado nuevamente."
            + " <br />"
            + "<br /><br />Cordialmente,<br />- Sisinfo";
    /**
     * Mensajes de Planeacion Academica
     */
    public final static String MENSAJE_HEADER_SUBIR_PROGRAMAS_CURSOS = "Estimado(a) %1$s,<br /><br />  Le recordamos que debe subir a Sisinfo el programa del(los) siguiente(s) curso(s): <br /><br />  ";
    public final static String MENSAJE_FOOTER_SUBIR_ARCHIVOS_CURSOS = "Por favor ingrese a Sisinfo antes del %1$s para subir los archivos respectivos.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String FORMATOE_BULLET_NOMBRE_CURSO_SECCION = "Curso: %1$s (Sección: %2$s)";
    public final static String ASUNTO_SUBIR_PROGRAMAS_CURSOS = "[Sisinfo] Subir Programas Cursos";
    public final static String MENSAJE_HEADER_SUBIR_TREINTA_POR_CIENTO = "Estimado(a) %1$s,<br /><br />  Le recordamos que "
            + "debe subir a Sisinfo las notas del treinta por ciento del(os) siguiente(s) curso(s): <br /><br />  ";
    public final static String ASUNTO_SUBIR_TREINTA_POR_CIENTO_CURSOS = "[Sisinfo] Cursos Notas 30%";
    public final static String MENSAJE_HEADER_SUBIR_CIERRE_CURSOS = "Estimado(a) %1$s,<br /><br />  Le recordamos que como parte del proceso de cierre de cursos "
            + "debe subir a Sisinfo el archivo correspondiente del(os) siguiente(s) curso(s): <br /><br />  ";
    public final static String ASUNTO_SUBIR_CIERRE_CURSOS = "[Sisinfo] Cursos Cierre";
    public final static String ASUNTO_DOCUMENTOS_FALTANTES_PLANEACION_ACADEMICA = "[Sisinfo] Documentos faltantes planeación academica";
    public final static String MENSAJE_DOCUMENTOS_FALTANTES_PLANEACION_ACADEMICA = "Buen día,<br /><br /> Le informamos que las siguientes secciones se encuentran pendientes de subir los Archivos de planeación academica:  <br><br>"
            + "<table border=\"1\" style = \"float:center;text-align:center\">"
            + "<tr>"
            + "	<th>Nombre curso</th>"
            + "	<th>Numero sección</th>"
            + "	<th>Profesor</th>"
            + "	<th>Archivo</th>"
            + "</tr>  %1$s</table> "
            + "<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String CONTENIDO_MENSAJE_DOCUMENTOS_FALTANTES_PLANEACION_ACADEMICA = "	<tr>"
            + "<td>%1$s</td>"
            + "<td>%2$s</td>"
            + "<td>%3$s</td>"
            + "<td>%4$s</td>"
            + "</tr>";
    public final static String ASUNTO_MIGRACION_TESIS_PREGRADO_RETIRADAS_A_HISTORICOS_SIN_TESISES = "[Sisinfo]Migración tesis pregrado retiradas a historicos %1";
    public final static String MENSAJE_MIGRACION_TESIS_PREGRADO_RETIRADAS_A_HISTORICOS_SIN_TESISES = "Buen día, <br /><br />"
            + "Le informamos que la migración de las tesis pregrado retiradas a históricos fue ejecutada, sin embargo %1, "
            + "por tal razon el timmer ha sido creado nuevamente."
            + " <br />"
            + "<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_MIGRACION_TESIS_PREGRADO_TERMINADAS_A_HISTORICOS_SIN_TESISES = "[Sisinfo]Migración tesis pregrado terminadas a historicos %1";
    public final static String MENSAJE_MIGRACION_TESIS_PREGRADO_TERMINADAS_A_HISTORICOS_SIN_TESISES = "Buen día, <br /><br />"
            + "Le informamos que la migración de las tesis pregrado terminadas a históricos fue ejecutada, sin embargo %1, "
            + "por tal razon el timmer ha sido creado nuevamente."
            + " <br />"
            + "<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_MIGRACION_TESIS_PREGRADO_PERDIDAS_A_HISTORICOS_SIN_TESISES = "[Sisinfo]Migración tesis pregrado perdidas a historicos %1";
    public final static String MENSAJE_MIGRACION_TESIS_PREGRADO_PERDIDAS_A_HISTORICOS_SIN_TESISES = "Buen día, <br /><br />"
            + "Le informamos que la migración de las tesis pregrado perdidas a históricos fue ejecutada, sin embargo %1, "
            + "por tal razon el timmer ha sido creado nuevamente."
            + " <br />"
            + "<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_MIGRACION_TESIS_PREGRADO_A_HISTORICOS = "[Sisinfo]Migración tesis pregrado %1 a historicos";
    public final static String MENSAJE_MIGRACION_TESIS_PREGRADO_A_HISTORICOS = "Buen día, <br /><br />"
            + "Le informamos que la migración de los proyectos de pregrado %1 a históricos fue ejecutada."
            + " <br />"
            + "<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_MIGRACION_TESIS_PREGRADO_A_HISTORICOS_PROBLEMA = "[Sisinfo]Migración tesis pregrado %1 a historicos %2";
    public final static String MENSAJE_MIGRACION_TESIS_PREGRADO_A_HISTORICOS_PROBLEMA = "Buen día, <br /><br />"
            + "Le informamos que la migración de la tesis de pregrado %1 a histórico del estudiante %2 fue ejecutada, %3."
            + " <br />"
            + "<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String MENSAJE_ERROR_INICIANDO_TAREAS_FECHAS_POR_RANGO_INVALIDO = "Estimado Sisinfo, <br><br>  se disparo el timer de crear tarea %1 pero no se puedo iniciar el proceso por que:"
            + "<br><br> - Rango fechas no se encontro el rango de fechas.  <br> <br> Cordial Saludo, <br> Sisinfo.";
    public final static String MENSAJE_ERROR_INICIANDO_TAREAS_FECHAS_POR_FECHA_FIN_INVALIDA = "Estimado Sisinfo <br><br> , se disparo el timer de crear tarea %1 pero no se puedo iniciar el proceso por que:"
            + "<br><br> - La fecha de fin del rango ya paso. <br> <br> Cordial Saludo, <br> Sisinfo.";
    public final static String ASUNTO_ERROR_CREANDO_TAREAS = "Error al crear tareas %1s";
    public final static String RAZON_INGRESO_AUTOMATICO_LISTA_NEGRA_RESERVA_CITAS = "El estudiante ingreso de manera automática a la lista negra debido a que no asistio a una cita programada para el %s";
    public final static String ASUNTO_SUBIR_NOTA_TESIS_2 = "[Sisinfo] Subir nota final tesis 2";
    public final static String HEADER_SUBIR_NOTA_TESIS_2 = "Buen día,<br /><br /> Le recordamos que debe subir la nota definitiva de Tesis 2 de los siguientes estudiantes:<br /> ";
    public final static String FOOTER_SUBIR_NOTA_TESIS_2 = "Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para poder realizar esa acción.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String MENSAJE_SUBIR_NOTA_TESIS_2 = "<b>%s</b>";
    public final static String ASUNTO_VENCIMIENTO_PLAZO_SOLICITUD_HORARIO_SUSTENTACION = "[Sisinfo] Vencimiento de plazo para seleccionar horario de sustentación para Tesis 2";
    public final static String MENSAJE_VENCIMIENTO_PLAZO_SOLICITUD_HORARIO_SUSTENTACION = "Estimado(a) %1,<br /><br />"
            + "Le recordamos que debe seleccionar jurados de calificación y horario de sustentación <b>lo antes posible</b> para poder finalizar su proceso de Tesis de Maestría."
            + "Tambien le recordamos que la fecha limite de sustentación es el <b>%2</b> . <br />"
            + "Por favor comuniquese con su Asesor lo más pronto posible para definir estas fechas</b> . <br />"
            + "<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_VENCIMIENTO_PLAZO_SOLICITUD_HORARIO_SUSTENTACION_COORDINACION = "[Sisinfo] Estudiantes Tesis 2 sin horario de sustentación";
    public final static String MENSAJE_VENCIMIENTO_PLAZO_SOLICITUD_HORARIO_SUSTENTACION_COORDINACION = "Buen día, <br /><br />"
            + "Le informamos que los siguientes estudiantes aun se encuentran pendientes de seleccionar horario de sustentación para Tesis 2:<br /><br /><ul>%1$s</ul>"
            + "<br /> A estos estudiantes ya se les ha enviado una notificación solicitandoles que seleccionen horario de sustentación.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_RECORDATORIO_RESERVA_INVENTARIO = "[Sisinfo] Recordatorio reserva sala %s";
    public final static String MENSAJE_RECORDATORIO_RESERVA_INVENTARIO = "Estimado(a) %1$s,<br /><br />"
            + "Le recordamos que usted tiene reservada la sala <b>%2$s</b> para el %3$s desde las %4$s hasta las %5$s."
            + "<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String MENSAJE_RESERVAS_VIGENTES_SALA = "<b>Laboratorio: %1$s</b><br><ul>%2$s</ul>";
    public final static String MENSAJE_RESERVAS_VIGENTES_RESERVA = "<li><b>Responsable:</b> %1$s<br><b>Hora reserva:</b>%2$s - %3$s<br><b>Elementos solicitados:</b> %4$s </li>";
    public final static String ASUNTO_RESERVAS_DEL_DIA = "[Sisinfo] Reservas de salas";
    public final static String MENSAJE_RESERVAS_DEL_DIA = "Estimado encargado <br /><br/>"
            + "Le queremos informar que para el dia de hoy se encuentran registradas las siguientes reservas:<br/> %1$s"
            + "<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String NOMBRE_ACCION_CREAR_LABORATORIO = "Crear laboratorio";

    //Acciones dinámicas MISIS
    public final static String NOMBRE_ACCION_ENVIAR_SOLICITUD_INSCRIPCION_TESIS1 = "Inscribir Tesis 1";
    public final static String NOMBRE_ACCION_VER_ESTADO_SOLICITUD_TESIS1 = "Ver Tesis 1";
    public final static String NOMBRE_ACCION_ENVIAR_SOLICITUD_INSCRIPCION_TESIS2 = "Inscribir Tesis 2";
    public final static String NOMBRE_ACCION_VER_ESTADO_SOLICITUD_TESIS2 = "Ver Tesis 2";
    public final static String NOMBRE_ACCION_CONSULTAR_TEMAS_TESIS = "Consultar temas de tesis disponibles";
    public final static String NOMBRE_ACCION_CONSULTAR_FECHAS_DE_TESIS = "Consultar fechas de tesis";
    public final static String NOMBRE_ACCION_ENVIAR_SOLICITUD_INSCRIPCION_SUBAREA = "Registrar plan de estudios";
    public final static String NOMBRE_ACCION_VER_ESTADO_DE_SOLICITUD_INSCRIPCION_SUBAREA = "Ver plan de estudios";
    public final static String NOMBRE_ACCION_VER_HORARIOS_DE_SUSTENTACION_TESIS2 = "Ver Horarios de Sustentaciones Tesis 2";
    
    public final static String DESCRIPCION_ACCION_ENVIAR_SOLICITUD_INSCRIPCION_TESIS1 = "Enviar Solicitud de Inscripción de Tesis 1.";
    public final static String DESCRIPCION_ACCION_VER_ESTADO_SOLICITUD_TESIS1 = "Ver Estado de Inscripción de Tesis 1.";
    public final static String DESCRIPCION_ACCION_ENVIAR_SOLICITUD_INSCRIPCION_TESIS2 = "Enviar Solicitud de Inscripción de Tesis 2.";
    public final static String DESCRIPCION_ACCION_VER_ESTADO_SOLICITUD_TESIS2 = "Ver Estado de Inscripción de Tesis 2.";
    public final static String DESCRIPCION_ACCION_CONSULTAR_TEMAS_TESIS = "Permite consultar los temas de tesis que ofrece cada profesor.";
    public final static String DESCRIPCION_ACCION_CONSULTAR_FECHAS_DE_TESIS = "Permite consultar las fechas de tesis, que ha establecido coordinación para un período.";
    public final static String DESCRIPCION_ACCION_ENVIAR_SOLICITUD_INSCRIPCION_SUBAREA = "Permite registrar el plan de estudios, requisito necesario para inscribir Tesis 1.";
    public final static String DESCRIPCION_ACCION_VER_ESTADO_DE_SOLICITUD_INSCRIPCION_SUBAREA = "Permite ver el estado del plan de estudios, el cual depende de la aprobación del asesor y del coordinador de la subárea.";
    public final static String DESCRIPCION_ACCION_UBICACION_HORA_TESIS2 = "Permite consultar los horarios de sustentación por periodo.";

    //Acciones dinámicas pregrado
    public final static String NOMBRE_ACCION_CONSULTAR_TEMAS_TESIS_PREGRADO = "Consultar temas de tesis disponibles";
    public final static String NOMBRE_ACCION_ENVIAR_SOLICITUD_INSCRIPCION_TESIS_PREGRADO = "Inscribir Proyecto de Grado";
    public final static String NOMBRE_ACCION_VER_ESTADO_SOLICITUD_INSCRIPCION_TESIS_PREGRADO = "Ver Proyecto de Pregrado";
    public final static String NOMBRE_ACCION_CONSULTAR_FECHA_TESIS_PREGRADO = "Consultar fechas de Proyecto de Grado";

    public final static String DESCRIPCION_ACCION_CONSULTAR_TEMAS_TESIS_PREGRADO = "Permite consultar los temas de proyecto de grado que ofrece cada profesor.";
    public final static String DESCRIPCION_ENVIAR_SOLICITUD_INSCRIPCION_TESIS_PREGRADO = "Permite crear una inscripción a proyecto de grado.";
    public final static String DESCRIPCION_VER_ESTADO_SOLICITUD_INSCRIPCION_TESIS_PREGRADO = "Permite ver el estado de una inscripción a proyecto de grado.";
    public final static String DESCRIPCION_ACCION_CONSULTAR_FECHA_TESIS_PREGRADO = "Permite ver las fechas a tener en cuenta para Proyecto de Grado.";

    //Acciones dinámicas Reserva Inventario
    public final static String DESCRIPCION_ACCION_CREAR_LABORATORIO = "Crea un nuevo laboratorio y lo agrega al sistema";
    public final static String NOMBRE_ACCION_CONSULTAR_LABORATORIOS = "Consultar/Editar laboratorios";
    public final static String DESCRIPCION_ACCION_CONSULTAR_LABORATORIOS = "Permite consultar y editar los laboratorios existentes dentro del sistema";
    public final static String NOMBRE_ACCION_MARCAR_RESERVAS = "Marcar reservas";
    public final static String DESCRIPCION_ACCION_MARCAR_RESERVAS = "Permite reportar si una reserva se cumplio o no";
    public final static String NOMBRE_ACCION_CANCELAR_RESERVAS = "Cancelar reservas";
    public final static String DESCRIPCION_ACCION_CANCELAR_RESERVAS = "Permite cancelar reservas previamente agregadas para un laboratorio";
    public final static String NOMBRE_ACCION_CONSULTAR_MIS_RESERVAS = "Consultar las Reservas de Laboratorio Vigentes";
    public final static String DESCRIPCION_ACCION_CONSULTAR_MIS_RESERVAS = "Consulte sus reservas vigentes realizadas para todos los laboratorios";
    public final static String NOMBRE_ACCION_CONSULTAR_HORARIO_RESERVA_INVENTARIO = "Realizar reserva";
    public final static String DESCRIPCION_ACCION_CONSULTAR_HORARIO_RESERVA_INVENTARIO = "Realice una reserva para un cierto laboratorio en una fecha específica";
    public final static String ASUNTO_ERROR_OBTENIENDO_ACCIONES = "[Sisinfo] Error obteniendo acciones";
    public final static String MENSAJE_ERROR_OBTENIENDO_ACCIONES = "Estimado Administrador Sisinfo <br /><br/>"
            + "Ha ocurrido un error al tratar de obtener las acciones para uno de los roles. La causa del error es la siguiente:<br/>"
            + "%s<br /><br />La acción ha sido deshabilitada mientras toma la acción correspondiente para solucionar el error.<br/><br/>Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_INCRIPCION_BANNER_PROYECTO_DE_GRADO_TAREA = "[Sisinfo] Inscripción Proyecto de grado en Banner ";
    public final static String MENSAJE_INCRIPCION_BANNER_PROYECTO_DE_GRADO_TAREA = "<b>%s</b>";
    public final static String HEADER_INCRIPCION_BANNER_PROYECTO_DE_GRADO_ACEPTADO = "Buen día,<br /><br /> Le recordamos que debe inscrbir al sistema Banner los siguientes estudiantes:<br /> ";
    public final static String FOOTER_INCRIPCION_BANNER_PROYECTO_DE_GRADO_ACEPTADO = "<br />Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para enviar "
            + "la propuesta correspondiente.<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_REGISTRO_USUARIO_EVENTO_EXTERNO = "[Sisinfo] Registro eventos Departamento Ingenieria de Sistemas";
    public final static String MENSAJE_REGISTRO_USUARIO_EVENTO_EXTERNO = "Buen día,<br /><br /> Para confirmar la inscripción al sistema de eventos del departamento de Sistemas haga click en el siguiente vinculo. "
            + "<br /><br />"
            + "%1$s<br />"
            + "<br /><br />En caso de cualquier inquietud comuníquese con el departamento."
            + "<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_OLVIDO_CONTRASENA_USUARIO_EXTERNO = "[Sisinfo] Olvido Contraseña";
    public final static String MENSAJE_OLVIDO_CONTRASENA_USUARIO_EXTERNO = "Buen día,<br /><br /> Para reestablecer su contraseña haga click en el siguiente vinculo. "
            + "<br /><br />"
            + "%1$s<br />"
            + "<br /><br />En caso de cualquier inquietud comuníquese con el departamento."
            + "<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_RECORDATORIO_NOTAS_FALTANTES = "[Sisinfo] Notas Faltantes Tesis2";
    public final static String MENSAJE_RECORDATORIO_NOTAS_FALTANTES = "Buen día,<br /><br /> Le informamos que los siguientes jurados aún se encuentran pendientes "
            + "por asignar la calificación a la tesis %1 del estudiante %2."
            + "<br /><br />"
            + "%3"
            + "<br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_DIAGNOSTICO_SISINFO = "[Sisinfo] Diagnostico Sisinfo";
    public final static String MENSAJE_DIAGNOSTICO_SISINFO = "Buen día, Administrador de Sisinfo <br /><br />"
            + "El siguiente es el estado de Sisinfo<br />"
            + "<h4>Alertas</h4>"
            + "Alertas que no tienen un timer asociado en memoria o la base de datos <br /> <br /> "
            + "%1<br />"
            + "<h4>Timers</h4>"
            + "Timers que se encuentran en memoria pero no en la base de datos o viceversa<br /> <br />"
            + "%2 <br /><br />"
            + "Cordialmente,<br />- Sisinfo ";
    public final static String ASUNTO_TAREAS_VENCIDAS = "[Sisinfo] Tareas Vencidas";
    public final static String MENSAJE_TAREAS_VENCIDAS = "Buen día,<br /><br />"
            + "Queremos recordarle que usted aun tiene las siguientes tareas vencidas en Sisinfo:<br /><br />"
            + "%1$s"
            + "<br /><br />Aunque el plazo para terminar estas tareas ya paso, usted aun puede completarlas ingresando a la página principal de sisinfo en sisinfo.uniandes.edu.co<br /><br />"
            + "Cordialmente,<br />- Sisinfo ";
    public final static String ASUNTO_BIENVENIDA_PROFESOR_SISINFO= "[Sisinfo] Perfil de profesor agregado";
    public final static String MENSAJE_BIENVENIDA_PROFESOR_SISINFO= "Buen día, <br /><br /> El perfil de profesor ha sido agregado a su cuenta de Sisinfo"
            + " al cual puede acceder a través de la pagina <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> ingresando su correo y contraseña Uniandes. Allí encontrará las siguientes opciones:"
            + "<br /><br />"
            + "<ul>"
            + "<li> <i>Sección Monitorias:</i> Permite gestionar los monitores de sus cursos. "
            + "<li> <i>Sección Planeación Académica:</i> Permite cargar los archivos de programa, treinta por ciento y cierre de sus curso actuales."
            + "</ul>"
            + "<br /><br />"
            + "Para mas información, visite <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> o escribanos a <a href=\"mailto:sisinfo@uniandes.edu.co\">sisinfo@uniandes.edu.co</a> "
            + "<br /><br />Cordialmente,<br />- Sisinfo";
    public final static String ASUNTO_ULTIMA_FECHA_INSCRIPCION_TESIS_1_ESTUDIANTE_OPCIONAL = "[Sisinfo] Inscripción tesis de investigación ";
    public final static String MENSAJE_ULTIMA_FECHA_INSCRIPCION_TESIS_1_ESTUDIANTE_OPCIONAL = "Estimado(a) %,<br /><br />"
            + "Usted tiene la posibilidad de realizar tesis de investigación. "
            + "En este caso, le recordamos que las inscripciones de Tesis 1 para el <b>periodo % se cerrarán el día % </b>. <br />"
            + " <br />"
            + " Por favor ingrese a Sisinfo desde la página del"
            + " Departamento: <a href=\"http://sistemas.uniandes.edu.co\" target=\"_blank\">http://sistemas.uniandes.edu.co</a> o desde <a href=\"http://sisinfo.uniandes.edu.co\" target=\"_blank\">http://sisinfo.uniandes.edu.co</a> para inscribirse<br /><br />Cordialmente,<br />- Sisinfo";


}
