package co.uniandes.sisinfo.serviciosnegocio;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.entities.Inscripcion;
import co.uniandes.sisinfo.entities.InscripcionAsistente;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import co.uniandes.sisinfo.serviciosfuncionales.InscripcionAsistenteFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.InscripcionFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.PersonaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;

/**
 * Converosr del módulo de Inscripciones
 * @author Marcela Morales
 */
public class ConversorInscripciones {

    //----------------------------------------------
    // ATRIBUTOS
    //----------------------------------------------
    //Útil
    private ConstanteLocal constanteBean;
    //Servicios
    private InscripcionFacadeLocal inscripcionFacade;
    private InscripcionAsistenteFacadeLocal inscripcionAsistenteFacade;
    private PersonaFacadeLocal personaFacade;

    //----------------------------------------------
    // CONSTRUCTOR
    //----------------------------------------------
    /**
     * Conversor del módulo de inscripciones
     * @param constanteBean Referencia a los servicios de las constantes
     */
    public ConversorInscripciones(ConstanteLocal constanteBean, InscripcionFacadeLocal inscripcionFacade, InscripcionAsistenteFacadeLocal inscripcionAsistenteFacade,
            PersonaFacadeLocal personaFacade) {
            this.constanteBean = constanteBean;
            this.inscripcionAsistenteFacade = inscripcionAsistenteFacade;
            this.inscripcionFacade = inscripcionFacade;
            this.personaFacade = personaFacade;
    }

    //----------------------------------------------
    // MÉTODOS PARA CONVERSIÓN A ENTIDADES
    //----------------------------------------------
    /**
     * Crea una inscripción a partir de una secuencia
     * @param secInscripcion Secuencia
     * @return Inscripción construída a partir de la secuencia dada
     */
    public Inscripcion crearInscripcionDesdeSecuencia(Secuencia secInscripcion) throws ParseException, UnsupportedEncodingException, NoSuchAlgorithmException {
        Inscripcion inscripcion = new Inscripcion();
        Secuencia correoCreador = secInscripcion.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO_CREADOR));
        if (correoCreador != null) {
            inscripcion.setCorreoCreador(correoCreador.getValor());
        }
        Secuencia nombreInscripcion = secInscripcion.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE));
        if (nombreInscripcion != null) {
            inscripcion.setNombreInscripcion(nombreInscripcion.getValor());
        }
        Secuencia descripcionInscripcion = secInscripcion.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION));
        if (descripcionInscripcion != null) {
            inscripcion.setDetallesInscripcion(descripcionInscripcion.getValor());
        }
        SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Secuencia fechaInicio = secInscripcion.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_INICIO));
        if (fechaInicio != null) {
            Date fechaInicioDate = sdfHMS.parse(fechaInicio.getValor());
            Timestamp fechaI = new Timestamp(fechaInicioDate.getTime());
            inscripcion.setFechaInicio(fechaI);
            if(fechaI!=null && (fechaI.before(new Date()) || fechaI.equals(new Date())))
            {
                inscripcion.setAbierta(true);
            }
            else{
                inscripcion.setAbierta(false);
            }
        }
        Secuencia fechaFin = secInscripcion.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN));
        if (fechaFin != null) {
            Date fechaFinDate = sdfHMS.parse(fechaFin.getValor());
            Timestamp fechaF = new Timestamp(fechaFinDate.getTime());
            inscripcion.setFechaFin(fechaF);
        }
        Secuencia correoNotificacion = secInscripcion.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO_NOTIFICACION));
        if (correoNotificacion != null) {
            inscripcion.setCorreoNotificacion(correoNotificacion.getValor());
        } else {
            inscripcion.setCorreoNotificacion("sisinfo@uniandes.edu.co");
        }
        Secuencia lugarEvento = secInscripcion.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_LUGAR_EVENTO));
        if (lugarEvento != null) {
            inscripcion.setLugarEvento(lugarEvento.getValor());
        }
        Secuencia fechaEvento = secInscripcion.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_EVENTO));
        if (fechaEvento != null) {
            Timestamp fechaEventoDate = Timestamp.valueOf(fechaEvento.getValor());
            inscripcion.setFechaEvento(fechaEventoDate);
        }
        getInscripcionFacade().create(inscripcion);
        HashMap<String, Persona> invitados = new HashMap<String, Persona>();
        Secuencia secInvitados = secInscripcion.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INVITADOS));
        if (secInvitados != null) {
            for (Secuencia s : secInvitados.getSecuencias()) {
                Secuencia secMail = s.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
                String correo = secMail.getValor();
                Persona persona = getPersonaFacade().findByCorreo(correo);
                invitados.put(correo, persona);
            }
        }        

        //Crea la inscripción a los asistentes
        Collection<InscripcionAsistente> inscripcionesAsistenes = new ArrayList<InscripcionAsistente>();
        for (Persona persona : invitados.values()) {
            InscripcionAsistente inscAsistente = new InscripcionAsistente(persona, inscripcion);
            inscAsistente.setInscrito(Constantes.CTE_INSCRIPCION_PENDIENTE);
             //Generacion de Hash MD5
            String stringHash = persona.getCorreo().split("@")[0] + "__" + inscripcion.getId();
            byte[] bytesCadena = stringHash.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] thedigest = md.digest(bytesCadena);
            String cadenaHash = toHexadecimal(thedigest);
            inscAsistente.setHashInscrito(cadenaHash);
            getInscripcionAsistenteFacade().create(inscAsistente);
            /*if (inscAsistente.getId() == null) {
                Collection<InscripcionAsistente> todas = getInscripcionAsistenteFacade().findAll();
                for (InscripcionAsistente inscripcionAsistente : todas) {
                    if(inscAsistente.getHashInscrito().equals(inscAsistente.getHashInscrito()) &&
                            inscAsistente.getInscripcion().equals(inscAsistente.getInscripcion()) &&
                            inscAsistente.getPersona().equals(inscAsistente.getPersona())){
                        inscAsistente = inscripcionAsistente;
                    }
                }
            }*/

            inscripcionesAsistenes.add(inscAsistente);
        }
        inscripcion.setInvitados(inscripcionesAsistenes);
        getInscripcionFacade().edit(inscripcion);
        return inscripcion;
    }

    /***
     * Convierte un arreglo de bytes a String usando valores hexadecimales
     * @param digest arreglo de bytes a convertir
     * @return String creado a partir de <code>digest</code>
     */
    private String toHexadecimal(byte[] digest) {
        String hash = "";
        for (byte aux : digest) {
            int b = aux & 0xff;
            if (Integer.toHexString(b).length() == 1) {
                hash += "0";
            }
            hash += Integer.toHexString(b);
        }
        return hash;
    }

    //----------------------------------------------
    // MÉTODOS PARA CONVERSIÓN A SECUENCIAS
    //----------------------------------------------
    /**
     * Crea una secuencia dada una inscripción
     * @param inscripcion Inscripción
     * @return Secuencia construída a partir de la inscripción dada
     */
    public Secuencia crearSecuenciaAPartirDeInscripcion(Inscripcion inscripcion) {
        //Extrae la información básica
        Secuencia secInformacionInscripcion = crearSecuenciaInscripcionBasica(inscripcion);

        //Extrae la información de los invitados
        Secuencia secInfoCandidatos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INVITADOS), "");
        Collection<InscripcionAsistente> invitados = inscripcion.getInvitados();
        for (InscripcionAsistente invitado : invitados) {
            Secuencia secInfoInvitado = crearSecuenciaInscripcionInvitados(invitado);
            secInfoCandidatos.agregarSecuencia(secInfoInvitado);
        }
        secInformacionInscripcion.agregarSecuencia(secInfoCandidatos);
        return secInformacionInscripcion;
    }



    /**
     * Crea una secuencia con la información básica de una inscripción dada una inscripción
     * @param inscripcion Inscripción
     * @return Secuencia construída a partir de la inscripción dada
     */
    private Secuencia crearSecuenciaInscripcionBasica(Inscripcion inscripcion) {
        Secuencia secInformacionInscripcion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_INSCRIPCION), "");

        Secuencia secIdInscripcion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_INSCRIPCION), (inscripcion.getId() != null) ? "" + inscripcion.getId() : "");
        Secuencia secNombreInscripcion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), (inscripcion.getNombreInscripcion() != null) ? inscripcion.getNombreInscripcion() : "");
        Secuencia secDescripcionInscripcion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION), (inscripcion.getDetallesInscripcion() != null) ? inscripcion.getDetallesInscripcion() : "");
        Secuencia secCorreoNotificacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO_NOTIFICACION), (inscripcion.getCorreoNotificacion() != null) ? inscripcion.getCorreoNotificacion() : "");
        Secuencia secFechaInicioInscripcion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_INICIO), (inscripcion.getFechaInicio() != null) ? inscripcion.getFechaInicio().toString() : "");
        Secuencia secFechaFinInscripcion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN), (inscripcion.getFechaFin() != null) ? inscripcion.getFechaFin().toString() : "");
        String valorEstado = inscripcion.isAbierta() ? getConstanteBean().getConstante(Constantes.ESTADO_ABIERTA) : getConstanteBean().getConstante(Constantes.ESTADO_CERRADA);
        Secuencia secAbierta = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO), valorEstado);
        Secuencia secLugarEvento = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_LUGAR_EVENTO), (inscripcion.getLugarEvento() != null) ? inscripcion.getLugarEvento() : "");
        SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Secuencia secFechaEvento = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_EVENTO), (inscripcion.getFechaEvento() != null) ? sdfHMS.format(inscripcion.getFechaEvento()) : "");

        secInformacionInscripcion.agregarSecuencia(secLugarEvento);
        secInformacionInscripcion.agregarSecuencia(secFechaEvento);
        secInformacionInscripcion.agregarSecuencia(secIdInscripcion);
        secInformacionInscripcion.agregarSecuencia(secNombreInscripcion);
        secInformacionInscripcion.agregarSecuencia(secDescripcionInscripcion);
        secInformacionInscripcion.agregarSecuencia(secFechaInicioInscripcion);
        secInformacionInscripcion.agregarSecuencia(secCorreoNotificacion);
        secInformacionInscripcion.agregarSecuencia(secFechaFinInscripcion);
        secInformacionInscripcion.agregarSecuencia(secAbierta);

        return secInformacionInscripcion;
    }

    /**
     * Crea una secuencia con la información de los invitados de una inscripción dada una inscripción
     * @param invitado Inscripción
     * @return Secuencia construída a partir de la inscripción dada
     */
    private Secuencia crearSecuenciaInscripcionInvitados(InscripcionAsistente invitado) {
        Secuencia secInfoInvitado = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INVITADO), "");

        Secuencia secIdInvitado = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_INVITADO), (invitado.getId() != null) ? String.valueOf(invitado.getId()) : "");
        Secuencia secNombreInvitado = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), (invitado.getPersona() != null && invitado.getPersona().getNombres() != null) ? invitado.getPersona().getNombres() : "");
        Secuencia secloginInvitado = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), (invitado.getPersona() != null && invitado.getPersona().getCorreo() != null) ? invitado.getPersona().getCorreo() : "");
        Secuencia secApellidoInvitado = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), (invitado.getPersona() != null && invitado.getPersona().getApellidos() != null) ? invitado.getPersona().getApellidos() : "");
        Secuencia secAtiendeInvitado = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INSCRITO), (invitado.getInscrito() != null) ? invitado.getInscrito() : "");
        Secuencia secOtros = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OTROS), (invitado.getOtros() != null) ? invitado.getOtros() : "");

        secInfoInvitado.agregarSecuencia(secIdInvitado);
        secInfoInvitado.agregarSecuencia(secNombreInvitado);
        secInfoInvitado.agregarSecuencia(secApellidoInvitado);
        secInfoInvitado.agregarSecuencia(secloginInvitado);
        secInfoInvitado.agregarSecuencia(secAtiendeInvitado);
        secInfoInvitado.agregarSecuencia(secOtros);

        return secInfoInvitado;
    }

    /**
     * Crea una secuencia dada una colección de inscripciones
     * Para funciones administrativas adiciona información del total de invitados y confirmados
     * @param listaInscripciones Colección de inscripciones
     * @return Secuencia construída a partir de la colección de inscripciones dada
     */
    public Secuencia crearSecuenciaAPartirDeInscripcionesAdmin(Collection<Inscripcion> listaInscripciones) {
        Secuencia secInfoInscripciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_INSCRIPCIONES), "");
        for (Inscripcion inscripcion : listaInscripciones) {
            //Extrae la información básica
            Secuencia secInfoInscripcion = crearSecuenciaInscripcionBasica(inscripcion);

            //Extrae la información de los invitados
            Secuencia secInfoCandidatos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INVITADOS), "");
            Collection<InscripcionAsistente> invitados = inscripcion.getInvitados();
            for (InscripcionAsistente invitado : invitados) {
                Secuencia secInfoInvitado = crearSecuenciaInscripcionInvitados(invitado);
                secInfoCandidatos.agregarSecuencia(secInfoInvitado);
            }
            secInfoInscripcion.agregarSecuencia(secInfoCandidatos);

            //Agrega la información requerida por administración
            int numInvitados = invitados.size();
            int confirmados = 0;
            for (InscripcionAsistente inv : invitados) {
                if (inv.getInscrito().equals(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_CONFIRMADO))) {
                    confirmados++;
                }
            }
            Secuencia secNumInvitados = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUM_INVITADOS), String.valueOf(numInvitados));
            Secuencia secNumConfirmados = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUM_CONFIRMADOS), String.valueOf(confirmados));
            secInfoInscripcion.agregarSecuencia(secNumInvitados);
            secInfoInscripcion.agregarSecuencia(secNumConfirmados);

            secInfoInscripciones.agregarSecuencia(secInfoInscripcion);
        }
        return secInfoInscripciones;
    }

    /**
     * Crea una secuencia dada una colección de inscripciones
     * Para funciones administrativas adiciona información del total de invitados y confirmados
     * @param listaInscripciones Colección de inscripciones
     * @return Secuencia construída a partir de la colección de inscripciones dada
     */
    public Secuencia crearSecuenciaAPartirDeInscripcionesAdminLigera(Collection<Inscripcion> listaInscripciones) {
        Secuencia secInfoInscripciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_INSCRIPCIONES), "");
        for (Inscripcion inscripcion : listaInscripciones) {
            //Extrae la información básica
            Secuencia secInfoInscripcion = crearSecuenciaInscripcionBasica(inscripcion);

            //Extrae la información de los invitados
            Secuencia secInfoCandidatos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INVITADOS), "");
            Collection<InscripcionAsistente> invitados = inscripcion.getInvitados();
            
            secInfoInscripcion.agregarSecuencia(secInfoCandidatos);

            //Agrega la información requerida por administración
            int numInvitados = invitados.size();
            int confirmados = 0;
            for (InscripcionAsistente inv : invitados) {
                if (inv.getInscrito().equals(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_CONFIRMADO))) {
                    confirmados++;
                }
            }
            Secuencia secNumInvitados = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUM_INVITADOS), String.valueOf(numInvitados));
            Secuencia secNumConfirmados = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUM_CONFIRMADOS), String.valueOf(confirmados));
            secInfoInscripcion.agregarSecuencia(secNumInvitados);
            secInfoInscripcion.agregarSecuencia(secNumConfirmados);

            secInfoInscripciones.agregarSecuencia(secInfoInscripcion);
        }
        return secInfoInscripciones;
    }

    /**
     * Crea una secuencia dada una colección de inscripciones de asistentes
     * @param listaInscripciones Colección de inscripciones
     * @return Secuencia construída a partir de la colección de inscripciones dada
     */
    public Secuencia crearSecuenciaAPartirDeInscripcionesAsistente(Collection<InscripcionAsistente> inscripcionesAsistente) {
        Secuencia secInscripciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_INSCRIPCIONES), "");
        for (InscripcionAsistente asistente : inscripcionesAsistente) {
            Secuencia secInfoInscripcion = crearSecuenciaAPartirDeInscripcion(asistente.getInscripcion());
            secInscripciones.agregarSecuencia(secInfoInscripcion);
        }
        return secInscripciones;
    }

    /**
     * Crea una secuencia lugeradada una colección de inscripciones de asistentes
     * Una secuencia ligera no contiene información de invitados, unicamente del asistente
     * sobre el cual se realizo la busqueda
     * @param listaInscripciones Colección de inscripciones
     * @return Secuencia construída a partir de la colección de inscripciones dada
     */
    public Secuencia crearSecuenciaLigeraAPartirDeInscripcionesAsistente(Collection<InscripcionAsistente> inscripcionesAsistente) {
        Secuencia secInscripciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_INSCRIPCIONES), "");
        for (InscripcionAsistente asistente : inscripcionesAsistente) {
            Secuencia secInfoInscripcion = crearSecuenciaInscripcionBasica(asistente.getInscripcion());
            Secuencia secInfoCandidatos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INVITADOS), "");
            Secuencia secInfoInvitado = crearSecuenciaInscripcionInvitados(asistente);
            secInfoCandidatos.agregarSecuencia(secInfoInvitado);
            secInfoInscripcion.agregarSecuencia(secInfoCandidatos);
            secInscripciones.agregarSecuencia(secInfoInscripcion);
        }
        return secInscripciones;
    }
    /**
     * Crea una secuencia dada una colección de inscripciones de un usuario
     * @param listaInscripciones Colección de inscripciones
     * @return Secuencia construída a partir de la colección de inscripciones dada
     */
    public Secuencia consultarSecuenciaAPartirDeInscripcionUsuario(InscripcionAsistente asistente) {
        Secuencia secInscripciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_INSCRIPCIONES), "");

        //Extrae la información básica
        Secuencia secInfoInscripcion = crearSecuenciaInscripcionBasica(asistente.getInscripcion());

        //Extrae la información de los invitados
        Secuencia secInvitados = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INVITADOS), "");
        Secuencia secInfoInvitado = crearSecuenciaInscripcionInvitados(asistente);
        secInvitados.agregarSecuencia(secInfoInvitado);

        secInfoInscripcion.agregarSecuencia(secInvitados);
        secInscripciones.agregarSecuencia(secInfoInscripcion);
        
        return secInscripciones;
    }

    /**
     * Crea una secuencia dada una colección de inscripciones
     * @param listaInscripciones Colección de inscripciones
     * @return Secuencia construída a partir de la colección de inscripciones dada
     */
    public Secuencia crearSecuenciaAPartirDeInscripciones(Collection<Inscripcion> listaInscripciones) {
        Secuencia secInscripciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_INSCRIPCIONES), "");
        for (Inscripcion inscripcion : listaInscripciones) {
            Secuencia secInfoInscripcion = crearSecuenciaAPartirDeInscripcion(inscripcion);
            secInscripciones.agregarSecuencia(secInfoInscripcion);
        }
        return secInscripciones;
    }

    //----------------------------------------------
    // MÉTODOS PRIVADOS
    //----------------------------------------------
    private ConstanteLocal getConstanteBean() {
        return constanteBean;
    }

    private InscripcionFacadeLocal getInscripcionFacade() {
        return inscripcionFacade;
    }

    private InscripcionAsistenteFacadeLocal getInscripcionAsistenteFacade() {
        return inscripcionAsistenteFacade;
    }

    private PersonaFacadeLocal getPersonaFacade() {
        return personaFacade;
    }

    public void setConstanteBean(ConstanteLocal constanteBean) {
        this.constanteBean = constanteBean;
    }

    public void setInscripcionAsistenteFacade(InscripcionAsistenteFacadeLocal inscripcionAsistenteFacade) {
        this.inscripcionAsistenteFacade = inscripcionAsistenteFacade;
    }

    public void setInscripcionFacade(InscripcionFacadeLocal inscripcionFacade) {
        this.inscripcionFacade = inscripcionFacade;
    }

    public void setPersonaFacade(PersonaFacadeLocal personaFacade) {
        this.personaFacade = personaFacade;
    }

    
}
