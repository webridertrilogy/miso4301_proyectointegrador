/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosnegocio;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.comun.constantes.Notificaciones;
import co.uniandes.sisinfo.entities.MaterialBibliografico;
import co.uniandes.sisinfo.entities.SolicitudMaterialBibliografico;
import co.uniandes.sisinfo.entities.TareaSencilla;
import co.uniandes.sisinfo.entities.Volumen;
import co.uniandes.sisinfo.entities.datosmaestros.Parametro;
import co.uniandes.sisinfo.entities.datosmaestros.Profesor;
import co.uniandes.sisinfo.serviciosfuncionales.AlertaMultipleFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.CorreoLocal;
import co.uniandes.sisinfo.serviciosfuncionales.MaterialBibliograficoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.PeriodicidadFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.SolicitudMaterialBibliograficoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.TareaMultipleFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.TareaSencillaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.ProfesorFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;

/**
 *
 * @author Marcela
 */
@Stateless
public class MaterialBibliograficoBean implements MaterialBibliograficoLocal {

    private ParserT parser;
    @EJB
    private ConstanteLocal constanteBean;
    @EJB
    private SolicitudMaterialBibliograficoFacadeLocal solicitudFacade;
    @EJB
    private MaterialBibliograficoFacadeLocal materialBibliograficoFacadeLocal;
    private ServiceLocator serviceLocator;
    @EJB
    private ProfesorFacadeLocal profesorFacade;
    @EJB
    private TareaMultipleLocal tareaBean;
    @EJB
    private TareaSencillaLocal tareaSencillaBean;
    @EJB
    private TareaSencillaFacadeLocal tareaSencillaFacade;
    @EJB
    private AlertaMultipleFacadeLocal alertaFacade;
    @EJB
    private TareaMultipleFacadeLocal tareaFacade;
    @EJB
    private AlertaMultipleLocal alertaBean;
    @EJB
    private PeriodicidadFacadeLocal periodicidadFacade;
    @EJB
    private CorreoLocal correoBean;
    @EJB
    private TimerGenericoBeanLocal timerGenerico;

    /** Contenedor para renovar tareas. */
    private List<String> listSolicitud;

    public MaterialBibliograficoBean() {
//        try {
//            serviceLocator = new ServiceLocator();
//            tareaFacade = (TareaMultipleFacadeLocal) serviceLocator.getLocalEJB(TareaMultipleFacadeLocal.class);
//            tareaBean = (TareaMultipleLocal) serviceLocator.getLocalEJB(TareaMultipleLocal.class);
//            tareaSencillaBean = (TareaSencillaLocal) serviceLocator.getLocalEJB(TareaSencillaLocal.class);
//            tareaSencillaFacade = (TareaSencillaFacadeLocal) serviceLocator.getLocalEJB(TareaSencillaFacadeLocal.class);
//            constanteBean = (ConstanteLocal) serviceLocator.getLocalEJB(ConstanteLocal.class);
//            alertaBean = (AlertaMultipleLocal) serviceLocator.getLocalEJB(AlertaMultipleLocal.class);
//            alertaFacade = (AlertaMultipleFacadeLocal) serviceLocator.getLocalEJB(AlertaMultipleFacadeLocal.class);
//            profesorFacade = (ProfesorFacadeLocal) serviceLocator.getLocalEJB(ProfesorFacadeLocal.class);
//            correoBean = (CorreoLocal) serviceLocator.getLocalEJB(CorreoLocal.class);
//            periodicidadFacade = (PeriodicidadFacadeLocal) serviceLocator.getLocalEJB(PeriodicidadFacadeLocal.class);
//            timerGenerico = (TimerGenericoBeanLocal) serviceLocator.getLocalEJB(TimerGenericoBeanLocal.class);
//        } catch (NamingException ex) {
//            Logger.getLogger(MaterialBibliograficoBean.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    @Override
    public String crearSolicitudCompraMaterialBibliografico(String xml) {
        try {
            getParser().leerXML(xml);
            String correo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            Profesor profesor = getProfesorFacade().findByCorreo(correo);
            if (profesor == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CREAR_SOLICITUD_COMPRA_MATERIAL_BIBLIOGRAFICO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0179, new LinkedList<Secuencia>());
            } else if (profesor.getTipo().equals(getConstanteBean().getConstante(Constantes.ROL_PROFESOR_CATEDRA))) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CREAR_SOLICITUD_COMPRA_MATERIAL_BIBLIOGRAFICO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0180, new LinkedList<Secuencia>());
            } else {

                Secuencia secuenciaMaterial = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUD_MATERIAL_BIBLIOGRAFICO));
                String autor = null;
                String titulo = null;
                String ISBNoISSN = null;
                String editorial = null;
                String anio = null;
                String precio = null;
                String proveedor = null;
                String observaciones = null;
                String nivelFormacion = null;
                String tematica = null;
                boolean textoGuia = false;
                String cursoTextoGuia = null;

                Secuencia secAutor = secuenciaMaterial.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_AUTOR));
                if (secAutor != null) {
                    autor = secAutor.getValor();
                }

                Secuencia secTitulo = secuenciaMaterial.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TITULO));
                if (secTitulo != null) {
                    titulo = secTitulo.getValor();
                }

                Secuencia secISBN = secuenciaMaterial.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ISBN_O_ISSN));
                if (secISBN != null) {
                    ISBNoISSN = secISBN.getValor();
                }

                Secuencia secEditorial = secuenciaMaterial.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_EDITORIAL));
                if (secEditorial != null) {
                    editorial = secEditorial.getValor();
                }

                Secuencia secAnio = secuenciaMaterial.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ANIO));
                if (secAnio != null) {
                    anio = secAnio.getValor();
                }

                Secuencia secPrecio = secuenciaMaterial.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PRECIO));
                if (secPrecio != null) {
                    precio = secPrecio.getValor();
                }

                Secuencia secProveedor = secuenciaMaterial.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROVEEDOR));
                if (secProveedor != null) {
                    proveedor = secProveedor.getValor();
                }

                Secuencia secObservaciones = secuenciaMaterial.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OBSERVACIONES));
                if (secObservaciones != null) {
                    observaciones = secObservaciones.getValor();
                }

                Secuencia secFormacion = secuenciaMaterial.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_FORMACION));
                if (secFormacion != null) {
                    nivelFormacion = secFormacion.getValor();
                }

                Secuencia secTematica = secuenciaMaterial.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TEMATICA));
                if (secTematica != null) {
                    tematica = secTematica.getValor();
                }

                Secuencia secTextoGuia = secuenciaMaterial.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TEXTO_GUIA));
                if (secTextoGuia != null) {
                    textoGuia = Boolean.parseBoolean(secTextoGuia.getValor());
                }

                Secuencia secCursoTextoGuia = secuenciaMaterial.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO_TEXTO_GUIA));
                if (secCursoTextoGuia != null) {
                    cursoTextoGuia = secCursoTextoGuia.getValor();
                }

                //Creación de material bibliográfico
                MaterialBibliografico materialBibliografico = new MaterialBibliografico();
                if (anio != null) {
                    materialBibliografico.setAnio(Integer.parseInt(anio));
                } else {
                    materialBibliografico.setAnio(0);
                }

                materialBibliografico.setAutor(autor);
                materialBibliografico.setEditorial(editorial);
                materialBibliografico.setISBNoISSN(ISBNoISSN);
                materialBibliografico.setNivelFormacion(nivelFormacion);
                //Procesar los volúmenes de la solicitud
                Secuencia secVolumenes = secuenciaMaterial.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VOLUMENES));

                Collection<Volumen> volumenes = new ArrayList<Volumen>();
                int numeroEjem = 0;
                if (secVolumenes != null) {
                    Collection<Secuencia> secuenciasVolumen = secVolumenes.getSecuencias();

                    for (Secuencia secuenciaVolumen : secuenciasVolumen) {
                        if (secuenciaVolumen.getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_VOLUMEN))) {
                            String numeroVolumen = secuenciaVolumen.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_VOLUMEN)).getValor();
                            String numero = secuenciaVolumen.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFO_VOLUMEN)).getValor();
                            String numeroEjemplares = secuenciaVolumen.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_EJEMPLARES)).getValor();
                            Volumen v = new Volumen();
                            v.setNumeroVolumen(Double.parseDouble(numeroVolumen));
                            v.setNumeroEjemplares(Integer.parseInt(numeroEjemplares));
                            int n = Integer.parseInt(numero);
                            v.setVolumen(n);
                            volumenes.add(v);
                            numeroEjem += Integer.parseInt(numeroEjemplares);
                        }
                    }
                } else {
                    String cant = secuenciaMaterial.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_EJEMPLARES)).getValor();
                    numeroEjem = Integer.parseInt(cant);
                }
                materialBibliografico.setVolumenes(volumenes);
                materialBibliografico.setObservaciones(observaciones);
                if (precio != null) {
                    materialBibliografico.setPrecio(Double.parseDouble(precio));
                }

                if (!validarNumeroEjemplares(textoGuia, numeroEjem)){
                    int numMaxEjemplares = Integer.parseInt(getConstanteBean().getConstante(Constantes.VAL_NUMERO_MAX_EJEMPLARES_NO_TEXTO_GUIA));
                    Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                    Secuencia secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_MENSAJE), Integer.toString(numMaxEjemplares));
                    //agrega el parámetro a la lista
                    parametros.add(secParametro);
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CREAR_SOLICITUD_COMPRA_MATERIAL_BIBLIOGRAFICO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_MB_0041, parametros);
                }

                

                materialBibliografico.setProveedor(proveedor);
                materialBibliografico.setTematica(tematica);
                materialBibliografico.setTitulo(titulo);
                materialBibliografico.setCantidadTotal(numeroEjem);
                materialBibliografico.setTextoGuia(textoGuia);
                materialBibliografico.setCursoTextoGuia(cursoTextoGuia);
                getMaterialBibliograficoFacadeLocal().create(materialBibliografico);
                //Creación de solicitud
                SolicitudMaterialBibliografico solicitud = new SolicitudMaterialBibliografico();
                solicitud.setEstado(getConstanteBean().getConstante(Constantes.TAG_PARAM_DIRECTOR_POR_AUTORIZAR));
                solicitud.setFechaEnvioABiblioteca(null);
                solicitud.setFechaModificacionDirector(null);
                solicitud.setFechaSolicitudProfesor(new Date(System.currentTimeMillis()));
                solicitud.setMaterialBibliografico(materialBibliografico);
                solicitud.setProfesor(profesor);
                getSolicitudFacade().create(solicitud);
                crearTareaAutorizacionCompraMaterialBibliografico(Long.toString(solicitud.getId()), profesor, autor, titulo);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CREAR_SOLICITUD_COMPRA_MATERIAL_BIBLIOGRAFICO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0181, new LinkedList<Secuencia>());
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(MaterialBibliograficoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CREAR_SOLICITUD_COMPRA_MATERIAL_BIBLIOGRAFICO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0031, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(MaterialBibliograficoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    private boolean validarNumeroEjemplares(boolean textoGuia,int numeroEjem){
        // Se revisa que el número de solicitudes no exceda el maximo para ese tipo de material
        if (!textoGuia) {
            int numMaxEjemplares = Integer.parseInt(getConstanteBean().getConstante(Constantes.VAL_NUMERO_MAX_EJEMPLARES_NO_TEXTO_GUIA));
            return !(numeroEjem > numMaxEjemplares);
        }
        return true;
    }

    @Override
    public String eliminarSolicitudMaterialBibliografico(String xml) {
        try {
            getParser().leerXML(xml);
            String idSolicitud = getParser().obtenerSecuencia(getConstanteBean().getConstante((Constantes.TAG_PARAM_ID_SOLICITUD_MATERIAL_BIBLIOGRAFICO))).getValor();
            SolicitudMaterialBibliografico solicitud = getSolicitudFacade().findById(Long.valueOf(idSolicitud));

            Collection<Secuencia> secuencias = new LinkedList<Secuencia>();
            Properties properties = new Properties();
            properties.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_SOLICITUD_MATERIAL_BIBLIOGRAFICO), idSolicitud);
            TareaSencilla borrar = tareaSencillaFacade.findById(tareaBean.consultarIdTareaPorParametrosTipo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_AUTORIZAR_SOLICITUD)
                    , properties));
            //Se completa la tarea si se encontró
            if (borrar != null) {
                tareaSencillaBean.realizarTareaPorId(borrar.getId());
            }
            solicitudFacade.remove(solicitud);

            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_SOLICITUD_MATERIAL_BIBLIOGRAFICO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0190, new LinkedList<Secuencia>());
        } catch (Exception e) {
            try {
                Logger.getLogger(MaterialBibliograficoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_SOLICITUD_MATERIAL_BIBLIOGRAFICO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0044, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(MaterialBibliograficoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public String enviarAutorizacionCompraMaterialBibliografico(String xml) {
        try {
            getParser().leerXML(xml);
            String idSolicitud = getParser().obtenerSecuencia(getConstanteBean().getConstante((Constantes.TAG_PARAM_ID_SOLICITUD_MATERIAL_BIBLIOGRAFICO))).getValor();
            String resultado = getParser().obtenerSecuencia(getConstanteBean().getConstante((Constantes.TAG_PARAM_RESULTADO))).getValor();
            String tipo = getConstanteBean().getConstante((Constantes.TAG_PARAM_TIPO_AUTORIZAR_SOLICITUD));
            Properties params = new Properties();
            params.put(getConstanteBean().getConstante((Constantes.TAG_PARAM_ID_SOLICITUD_MATERIAL_BIBLIOGRAFICO)),idSolicitud);
            TareaSencilla tarea = tareaSencillaFacade.findById(tareaBean.consultarIdTareaPorParametrosTipo(tipo, params));
            if (tarea != null) {
                tareaSencillaBean.realizarTareaPorId(tarea.getId());
            }
            SolicitudMaterialBibliografico solicitud = getSolicitudFacade().findById(Long.parseLong(idSolicitud));
            boolean aprobacion = Boolean.parseBoolean(resultado);
            if (aprobacion) {
                solicitud.setEstado(getConstanteBean().getConstante(Constantes.TAG_PARAM_AUXILIAR_POR_SOLICITAR_BIBLIOTECA));
                crearTareaCompraMaterialBibliografico(idSolicitud);
            } else {
                solicitud.setEstado(getConstanteBean().getConstante(Constantes.TAG_PARAM_RECHAZADA_POR_DIRECTOR));

                //Mandar un correo al profesor indicandole que la solicitud fue rechazada
                Profesor p = solicitud.getProfesor();
                MaterialBibliografico mat = solicitud.getMaterialBibliografico();
                String asunto = Notificaciones.ASUNTO_RECHAZO_SOLICITUD_MAT_BIB;
                String mensaje = Notificaciones.MENSAJE_RECHAZO_SOLICITUD_MAT_BIB;
                mensaje = mensaje.replaceFirst("%", p.getPersona().getNombres() + " " + p.getPersona().getApellidos());
                mensaje = mensaje.replaceFirst("%", mat.getTitulo());
                mensaje = mensaje.replaceFirst("%", mat.getAutor());
                mensaje = mensaje.replaceFirst("%", mat.getEditorial());
                mensaje = mensaje.replaceFirst("%", "" + mat.getCantidadTotal());

                correoBean.enviarMail(p.getPersona().getCorreo(), asunto, null, null, null, mensaje);

                // Completa las tareas de compra de material bibliografico en caso de que haya sido aceptada y despues rechazada
                tipo = getConstanteBean().getConstante((Constantes.TAG_PARAM_TIPO_CONFIRMAR_COMPRA));
                params = new Properties();
                params.put(getConstanteBean().getConstante((Constantes.TAG_PARAM_ID_SOLICITUD_MATERIAL_BIBLIOGRAFICO)),idSolicitud);
                tarea = tareaSencillaFacade.findById(tareaBean.consultarIdTareaPorParametrosTipo(tipo, params));
                if (tarea != null) {
                    tareaSencillaBean.realizarTareaPorId(tarea.getId());
                }
            }
            solicitud.setFechaModificacionDirector(new Date(System.currentTimeMillis()));
            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ENVIAR_AUTORIZACION_COMPRA_MATERIAL_BIBLIOGRAFICO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0183, new LinkedList<Secuencia>());
        } catch (Exception e) {
            try {
                Logger.getLogger(MaterialBibliograficoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ENVIAR_AUTORIZACION_COMPRA_MATERIAL_BIBLIOGRAFICO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0032, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(MaterialBibliograficoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public String enviarConfirmacionCompraMaterialBibliograficoBiblioteca(String xml) {
        try {
            getParser().leerXML(xml);
            String idSol = getParser().obtenerSecuencia(getConstanteBean().getConstante((Constantes.TAG_PARAM_ID_SOLICITUD_MATERIAL_BIBLIOGRAFICO))).getValor();
            Properties params = new Properties();
            params.put(getConstanteBean().getConstante((Constantes.TAG_PARAM_ID_SOLICITUD_MATERIAL_BIBLIOGRAFICO)),idSol);
            TareaSencilla tarea = tareaSencillaFacade.findById(tareaBean.consultarIdTareaPorParametrosTipo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CONFIRMAR_COMPRA)
                    , params));
            if (tarea!=null) {
                tareaSencillaBean.realizarTareaPorId(tarea.getId());
            } 
            SolicitudMaterialBibliografico solicitud = getSolicitudFacade().findById(Long.parseLong(idSol));
            solicitud.setEstado(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITADO_A_BIBLIOTECA));
            solicitud.setFechaEnvioABiblioteca(new Date(System.currentTimeMillis()));


            //Mandar un correo al profesor indicandole que la solicitud fue aprobada
            Profesor p = solicitud.getProfesor();
            MaterialBibliografico mat = solicitud.getMaterialBibliografico();
            String asunto = Notificaciones.ASUNTO_APROBACION_SOLICITUD_MAT_BIB;
            String mensaje = Notificaciones.MENSAJE_APROBACION_SOLICITUD_MAT_BIB;
            mensaje = mensaje.replaceFirst("%", p.getPersona().getNombres() + " " + p.getPersona().getApellidos());
            mensaje = mensaje.replaceFirst("%", mat.getTitulo());
            mensaje = mensaje.replaceFirst("%", mat.getAutor());
            mensaje = mensaje.replaceFirst("%", mat.getEditorial());
            mensaje = mensaje.replaceFirst("%", "" + mat.getCantidadTotal());

            correoBean.enviarMail(p.getPersona().getCorreo(), asunto, null, null, null, mensaje);
            
            String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_REPORTAR_ADQUISICION);
            String nombreRol = getConstanteBean().getConstante(Constantes.ROL_AUXILIAR_FINACIERO);
            String header = String.format(Notificaciones.HEADER_CONFIRMAR_LLEGO_BIBLIOTECA_MAT_BIB,nombreRol);
            String footer = Notificaciones.FOOTER_CONFIRMAR_LLEGO_BIBLIOTECA_MAT_BIB;
            String mensajeT = String.format(Notificaciones.MENSAJE_CONFIRMAR_LLEGO_BIBLIOTECA_MAT_BIB,mat.getTitulo(),mat.getAutor(),p.getPersona().getNombres() + " " + p.getPersona().getApellidos());
            String comando = getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CONFIRMACION_LLEGO_A_BIBLIOTECA_MATERIAL_BIBLIOGRAFICO);
            String asuntoT = Notificaciones.ASUNTO_CONFIRMAR_LLEGO_BIBLIOTECA_MAT_BIB;
            HashMap<String,String> paramsT = new HashMap<String, String>();
            paramsT.put(getConstanteBean().getConstante((Constantes.TAG_PARAM_ID_SOLICITUD_MATERIAL_BIBLIOGRAFICO)),idSol);
            tareaBean.crearTareaRol(mensajeT,
                    tipo, nombreRol, true, header, footer, new Timestamp(System.currentTimeMillis()),new Timestamp(System.currentTimeMillis()+(1000L*60L*60L*24L*30L)) , comando, paramsT, asuntoT);

            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CONFIRMACION_COMPRA_MATERIAL_BIBLIOGRAFICO_BIBLIOTECA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0185, new LinkedList<Secuencia>());
            
        } catch (Exception e) {
            try {
                Logger.getLogger(MaterialBibliograficoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CONFIRMACION_COMPRA_MATERIAL_BIBLIOGRAFICO_BIBLIOTECA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0034, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(MaterialBibliograficoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public String consultarSolicitudesSolicitante(String xml) {
        try {
            getParser().leerXML(xml);
            String correoSolicitante = getParser().obtenerSecuencia(getConstanteBean().getConstante((Constantes.TAG_PARAM_CORREO))).getValor();
            List<SolicitudMaterialBibliografico> solicitudes = getSolicitudFacade().findyBySolicitante(correoSolicitante);
            Collection<Secuencia> secuencias = new LinkedList<Secuencia>();
            Secuencia secuenciaSolicitudes = getSecuenciaSolicitudes(solicitudes);
            secuencias.add(secuenciaSolicitudes);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_SOLICITUDES_SOLICITANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0190, new LinkedList<Secuencia>());
        } catch (Exception e) {
            try {
                Logger.getLogger(MaterialBibliograficoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_SOLICITUDES_SOLICITANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0044, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(MaterialBibliograficoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Deprecated
    public String consultarSolicitudesPorRangoDePrecio(String xml) {
        try {
            getParser().leerXML(xml);
            String precioInicio = getParser().obtenerPrimeraSecuencia(getConstanteBean().getConstante((Constantes.TAG_PARAM_PRECIO_DESDE))).getValor();
            String precioFin = getParser().obtenerPrimeraSecuencia(getConstanteBean().getConstante((Constantes.TAG_PARAM_PRECIO_HASTA))).getValor();
            List<SolicitudMaterialBibliografico> solicitudes = getSolicitudFacade().findByRangoPrecio(Double.parseDouble(precioInicio), Double.parseDouble(precioFin));
            Collection<Secuencia> secuencias = new LinkedList<Secuencia>();
            Secuencia secuenciaSolicitudes = getSecuenciaSolicitudes(solicitudes);
            secuencias.add(secuenciaSolicitudes);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_SOLICITUDES_MATERIAL_BIBLIOGRAFICO_RANGO_PRECIOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0186, new LinkedList<Secuencia>());
        } catch (Exception e) {
            try {
                Logger.getLogger(MaterialBibliograficoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_SOLICITUDES_MATERIAL_BIBLIOGRAFICO_RANGO_PRECIOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0035, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(MaterialBibliograficoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Deprecated
    public String consultarSolicitudesPorProveedor(String xml) {
        try {
            getParser().leerXML(xml);
            String proveedor = getParser().obtenerPrimeraSecuencia(getConstanteBean().getConstante((Constantes.TAG_PARAM_PROVEEDOR))).getValor();
            List<SolicitudMaterialBibliografico> solicitudes = getSolicitudFacade().findyByProveedor(proveedor);
            Collection<Secuencia> secuencias = new LinkedList<Secuencia>();
            Secuencia secuenciaSolicitudes = getSecuenciaSolicitudes(solicitudes);
            secuencias.add(secuenciaSolicitudes);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_SOLICITUDES_POR_PROVEEDOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0187, new LinkedList<Secuencia>());
        } catch (Exception e) {
            try {
                Logger.getLogger(MaterialBibliograficoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_SOLICITUDES_POR_PROVEEDOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0037, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(MaterialBibliograficoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Deprecated
    public String consultarSolicitudesPorPeriodoAcademico(String xml) {
        throw new UnsupportedOperationException("Not supported yet.");//FIXME: Implementar
    }

    @Deprecated
    public String consultarSolicitudesPorAnioPublicacion(String xml) {
        try {
            getParser().leerXML(xml);
            String anio = getParser().obtenerPrimeraSecuencia(getConstanteBean().getConstante((Constantes.TAG_PARAM_ANIO))).getValor();
            List<SolicitudMaterialBibliografico> solicitudes = getSolicitudFacade().findByAnioPublicacion(Integer.parseInt(anio));
            Collection<Secuencia> secuencias = new LinkedList<Secuencia>();
            Secuencia secuenciaSolicitudes = getSecuenciaSolicitudes(solicitudes);
            secuencias.add(secuenciaSolicitudes);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_SOLICITUDES_POR_ANIO_PUBLICACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0188, new LinkedList<Secuencia>());
        } catch (Exception e) {
            try {
                Logger.getLogger(MaterialBibliograficoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_SOLICITUDES_POR_ANIO_PUBLICACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0038, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(MaterialBibliograficoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Deprecated
    public String consultarSolicitudesDepartamentoPorRangoDeFecha(String xml) {
        try {
            getParser().leerXML(xml);
            String fechaInicio = getParser().obtenerPrimeraSecuencia(getConstanteBean().getConstante((Constantes.TAG_PARAM_FECHA_DESDE))).getValor();
            String fechaFin = getParser().obtenerPrimeraSecuencia(getConstanteBean().getConstante((Constantes.TAG_PARAM_FECHA_HASTA))).getValor();
            List<SolicitudMaterialBibliografico> solicitudes = getSolicitudFacade().findByRangoFecha(Date.valueOf(fechaInicio), Date.valueOf(fechaFin));
            Collection<Secuencia> secuencias = new LinkedList<Secuencia>();
            Secuencia secuenciaSolicitudes = getSecuenciaSolicitudes(solicitudes);
            secuencias.add(secuenciaSolicitudes);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_COSTO_PROMEDIO_POR_RANGO_DE_FECHA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0189, new LinkedList<Secuencia>());
        } catch (Exception e) {
            try {
                Logger.getLogger(MaterialBibliograficoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_COSTO_PROMEDIO_POR_RANGO_DE_FECHA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0042, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(MaterialBibliograficoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public String consultarSolicitudesDepartamentoPorEstado(String xml) {
        try {
            getParser().leerXML(xml);
            String estado = getParser().obtenerSecuencia(getConstanteBean().getConstante((Constantes.TAG_PARAM_ESTADO))).getValor();
            List<SolicitudMaterialBibliografico> solicitudes = getSolicitudFacade().findyByEstado(estado);
            Collection<Secuencia> secuencias = new LinkedList<Secuencia>();
            Secuencia secuenciaSolicitudes = getSecuenciaSolicitudes(solicitudes);
            secuencias.add(secuenciaSolicitudes);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_SOLICITUDES_DEPARTAMENTO_POR_ESTADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0190, new LinkedList<Secuencia>());
        } catch (Exception e) {
            try {
                Logger.getLogger(MaterialBibliograficoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_SOLICITUDES_DEPARTAMENTO_POR_ESTADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0044, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(MaterialBibliograficoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Deprecated
    public String consultarCostoTotalPorSolicitante(String xml) {
        throw new UnsupportedOperationException("Not supported yet.");//FIXME: Implementar
    }

    @Deprecated
    public String consultarCostoTotalPorRangoDeFecha(String xml) {
        try {
            getParser().leerXML(xml);
            String fechaInicio = getParser().obtenerPrimeraSecuencia(getConstanteBean().getConstante((Constantes.TAG_PARAM_FECHA_DESDE))).getValor();
            String fechaFin = getParser().obtenerPrimeraSecuencia(getConstanteBean().getConstante((Constantes.TAG_PARAM_FECHA_HASTA))).getValor();
            double precio = getSolicitudFacade().findPrecioTotalByRangoFecha(Date.valueOf(fechaInicio), Date.valueOf(fechaFin));
            Collection<Secuencia> secuencias = new LinkedList<Secuencia>();
            Secuencia secuenciaPrecio = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PRECIO), Double.toString(precio));
            secuencias.add(secuenciaPrecio);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_COSTO_PROMEDIO_POR_RANGO_DE_FECHA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0191, new LinkedList<Secuencia>());
        } catch (Exception e) {
            try {
                Logger.getLogger(MaterialBibliograficoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_COSTO_PROMEDIO_POR_RANGO_DE_FECHA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0045, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(MaterialBibliograficoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Deprecated
    public String consultarCostoPromedioPorSolicitante(String xml) {
        throw new UnsupportedOperationException("Not supported yet.");//FIXME: Implementar
    }

    @Deprecated
    public String consultarCostoPromedioPorRangoDeFecha(String xml) {
        try {
            getParser().leerXML(xml);
            String fechaInicio = getParser().obtenerPrimeraSecuencia(getConstanteBean().getConstante((Constantes.TAG_PARAM_FECHA_DESDE))).getValor();
            String fechaFin = getParser().obtenerPrimeraSecuencia(getConstanteBean().getConstante((Constantes.TAG_PARAM_FECHA_HASTA))).getValor();
            double precio = getSolicitudFacade().findPrecioPromedioByRangoFecha(Date.valueOf(fechaInicio), Date.valueOf(fechaFin));
            Collection<Secuencia> secuencias = new LinkedList<Secuencia>();
            Secuencia secuenciaPrecio = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PRECIO), Double.toString(precio));
            secuencias.add(secuenciaPrecio);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_COSTO_PROMEDIO_POR_RANGO_DE_FECHA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0192, new LinkedList<Secuencia>());
        } catch (Exception e) {
            try {
                Logger.getLogger(MaterialBibliograficoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_COSTO_PROMEDIO_POR_RANGO_DE_FECHA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0046, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(MaterialBibliograficoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    private Secuencia getSecuenciaSolicitudTabla(SolicitudMaterialBibliografico solicitud) {
        Secuencia secuenciaSolicitud = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUD_MATERIAL_BIBLIOGRAFICO), getConstanteBean().getConstante(Constantes.NULL));
        secuenciaSolicitud.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_SOLICITUD_MATERIAL_BIBLIOGRAFICO), Long.toString(solicitud.getId())));
        secuenciaSolicitud.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_SOLICITUD_PROFESOR), solicitud.getFechaSolicitudProfesor().toString()));
        secuenciaSolicitud.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO), solicitud.getEstado()));

        Secuencia secuenciaDatos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DATOS_MATERIAL_BIBLIOGRAFICO), getConstanteBean().getConstante(Constantes.NULL));
        secuenciaDatos.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_AUTOR), solicitud.getMaterialBibliografico().getAutor()));
        secuenciaDatos.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TITULO), solicitud.getMaterialBibliografico().getTitulo()));
        secuenciaDatos.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ISBN_O_ISSN), solicitud.getMaterialBibliografico().getISBNoISSN()));
        secuenciaDatos.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_EDITORIAL), solicitud.getMaterialBibliografico().getEditorial()));
        secuenciaDatos.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ANIO), ((solicitud.getMaterialBibliografico().getAnio() != null) ? Integer.toString(solicitud.getMaterialBibliografico().getAnio()) : getConstanteBean().getConstante(Constantes.NULL))));
        secuenciaDatos.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TEXTO_GUIA), ((solicitud.getMaterialBibliografico().isTextoGuia())==null)?Boolean.FALSE.toString():solicitud.getMaterialBibliografico().isTextoGuia().toString()));

        secuenciaSolicitud.agregarSecuencia(secuenciaDatos);
        return secuenciaSolicitud;
    }

    private Secuencia getSecuenciaSolicitud(SolicitudMaterialBibliografico solicitud) {

        Secuencia secuenciaSolicitud = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUD_MATERIAL_BIBLIOGRAFICO), getConstanteBean().getConstante(Constantes.NULL));
        secuenciaSolicitud.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_SOLICITUD_MATERIAL_BIBLIOGRAFICO), Long.toString(solicitud.getId())));

        secuenciaSolicitud.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), solicitud.getProfesor().getPersona().getNombres()));
        secuenciaSolicitud.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), solicitud.getProfesor().getPersona().getApellidos()));


        secuenciaSolicitud.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), solicitud.getProfesor().getPersona().getCorreo()));


        secuenciaSolicitud.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_SOLICITUD_PROFESOR), solicitud.getFechaSolicitudProfesor().toString()));
        secuenciaSolicitud.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_AUTORIZACION_DIRECCION), (solicitud.getFechaModificacionDirector() != null) ? solicitud.getFechaModificacionDirector().toString() : ""));
        secuenciaSolicitud.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ENVIO_A_BIBLIOTECA), (solicitud.getFechaEnvioABiblioteca() != null) ? solicitud.getFechaEnvioABiblioteca().toString() : ""));
        secuenciaSolicitud.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO), solicitud.getEstado()));

        Secuencia secuenciaDatos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DATOS_MATERIAL_BIBLIOGRAFICO), getConstanteBean().getConstante(Constantes.NULL));

        secuenciaDatos.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_AUTOR), solicitud.getMaterialBibliografico().getAutor()));
        secuenciaDatos.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TITULO), solicitud.getMaterialBibliografico().getTitulo()));
        secuenciaDatos.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ISBN_O_ISSN), solicitud.getMaterialBibliografico().getISBNoISSN()));
        secuenciaDatos.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_EDITORIAL), solicitud.getMaterialBibliografico().getEditorial()));
        secuenciaDatos.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ANIO), ((solicitud.getMaterialBibliografico().getAnio() != null) ? Integer.toString(solicitud.getMaterialBibliografico().getAnio()) : getConstanteBean().getConstante(Constantes.NULL))));

        secuenciaDatos.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_EJEMPLARES), ((solicitud.getMaterialBibliografico().getCantidadTotal() != null) ? Integer.toString(solicitud.getMaterialBibliografico().getCantidadTotal()) : getConstanteBean().getConstante(Constantes.NULL))));

        Secuencia secuenciaVolumenes = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VOLUMENES), getConstanteBean().getConstante(Constantes.NULL));
        Collection<Volumen> listaVolumenes = solicitud.getMaterialBibliografico().getVolumenes();
        for (Volumen volumen : listaVolumenes) {
            Secuencia secuenciaVolumen = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VOLUMEN), getConstanteBean().getConstante(Constantes.NULL));
            secuenciaVolumen.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_VOLUMEN), "" + volumen.getNumeroVolumen()));
            secuenciaVolumen.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFO_VOLUMEN), "" + volumen.getVolumen()));
            secuenciaVolumen.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_EJEMPLARES), "" + volumen.getNumeroEjemplares()));
            secuenciaVolumenes.agregarSecuencia(secuenciaVolumen);
        }

        secuenciaDatos.agregarSecuencia(secuenciaVolumenes);
        secuenciaDatos.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROVEEDOR), solicitud.getMaterialBibliografico().getProveedor()));
        secuenciaDatos.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OBSERVACIONES), solicitud.getMaterialBibliografico().getObservaciones()));
        secuenciaDatos.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_FORMACION), solicitud.getMaterialBibliografico().getNivelFormacion()));
        secuenciaDatos.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TEMATICA), solicitud.getMaterialBibliografico().getTematica()));
        secuenciaDatos.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PRECIO), ((solicitud.getMaterialBibliografico().getPrecio() != null) ? Double.toString(solicitud.getMaterialBibliografico().getPrecio()) : getConstanteBean().getConstante(Constantes.NULL))));
        secuenciaDatos.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TEXTO_GUIA),((solicitud.getMaterialBibliografico().isTextoGuia()==null)?Boolean.FALSE.toString():solicitud.getMaterialBibliografico().isTextoGuia().toString())));
        secuenciaDatos.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO_TEXTO_GUIA),((solicitud.getMaterialBibliografico().getCursoTextoGuia()==null)?"":solicitud.getMaterialBibliografico().getCursoTextoGuia())));

        secuenciaSolicitud.agregarSecuencia(secuenciaDatos);
        return secuenciaSolicitud;
    }

    private Secuencia getSecuenciaSolicitudes(Collection<SolicitudMaterialBibliografico> solicitudes) {
        Secuencia secuenciaSolicitudes = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUDES_MATERIAL_BIBLIOGRAFICO), getConstanteBean().getConstante(Constantes.NULL));
        Iterator<SolicitudMaterialBibliografico> iter = solicitudes.iterator();
        while (iter.hasNext()) {
            SolicitudMaterialBibliografico solicitudMaterialBibliografico = iter.next();
            Secuencia secuenciaSolicitud = getSecuenciaSolicitudTabla(solicitudMaterialBibliografico);
            secuenciaSolicitudes.agregarSecuencia(secuenciaSolicitud);
        }
        return secuenciaSolicitudes;
    }

    private ParserT getParser() {
        if (parser == null) {
            parser = new ParserT();
        }
        return parser;
    }

    private ConstanteLocal getConstanteBean() {
        return constanteBean;
    }

    private MaterialBibliograficoFacadeLocal getMaterialBibliograficoFacadeLocal() {
        return materialBibliograficoFacadeLocal;
    }

    private ServiceLocator getServiceLocator() {
        return serviceLocator;
    }

    private SolicitudMaterialBibliograficoFacadeLocal getSolicitudFacade() {
        return solicitudFacade;
    }

    private ProfesorFacadeLocal getProfesorFacade() {
        return profesorFacade;
    }

    private TareaMultipleLocal getTareaBean() {
        return tareaBean;
    }

    private TareaMultipleFacadeLocal getTareaFacade() {
        return tareaFacade;
    }

    private void crearTareaAutorizacionCompraMaterialBibliografico(String idSolicitud, Profesor p, String autor, String titulo) {

        Timestamp fI = new Timestamp(System.currentTimeMillis());
        Timestamp fF = new Timestamp(System.currentTimeMillis() + (3600000L * 24L * 30L /** 6L*/));
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_AUTORIZAR_SOLICITUD);

        String categoriaResponsable = getConstanteBean().getConstante(Constantes.ROL_DIRECCION_DEPARTAMENTO);
        HashMap<String, String> paramsNew = new HashMap<String, String>();
        paramsNew.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_SOLICITUD_MATERIAL_BIBLIOGRAFICO), idSolicitud);

        String comando = getConstanteBean().getConstante(Constantes.CMD_ENVIAR_AUTORIZACION_COMPRA_MATERIAL_BIBLIOGRAFICO);
        String asunto = Notificaciones.ASUNTO_AUTORIZACION_COMPRA_MAT_BIB;
        String header = String.format(Notificaciones.HEADER_AUTORIZACION_COMPRA_MAT_BIB,categoriaResponsable);
        String footer = Notificaciones.FOOTER_AUTORIZACION_COMPRA_MAT_BIB;
        String mensaje = String.format(Notificaciones.MENSAJE_AUTORIZACION_COMPRA_MAT_BIB,titulo,autor,p.getPersona().getNombres() + " " + p.getPersona().getApellidos());

        getTareaBean().crearTareaRol(mensaje,tipo,
                categoriaResponsable, true, header, footer, fI, fF, comando, paramsNew,asunto);

        String mensajeCompleto = String.format(Notificaciones.MENSAJE_COMPLETO_AUTORIZACION_COMPRA_MAT_BIB,categoriaResponsable,p.getPersona().getNombres() + " " + p.getPersona().getApellidos(),titulo,autor);
        //Se manda un correo al director informándole que hay una nueva solicitud de material bibliográfico por evaluar
        correoBean.enviarMail(getConstanteBean().getConstante(Constantes.VAL_CORREO_DIRECTOR_MB), asunto, null, null, null, mensajeCompleto);
    }

    private void crearTareaCompraMaterialBibliografico(String idSolicitud) {

        Timestamp fI = new Timestamp(System.currentTimeMillis());
        Timestamp fF = new Timestamp(System.currentTimeMillis() + (3600000L * 24L * 30L * 6L));
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CONFIRMAR_COMPRA);        

        SolicitudMaterialBibliografico solicitud = solicitudFacade.findById(Long.valueOf(idSolicitud));
        Profesor p = solicitud.getProfesor();
        MaterialBibliografico material = solicitud.getMaterialBibliografico();
        String autor = material.getAutor();
        String titulo = material.getTitulo();

        String categoriaResponsable = getConstanteBean().getConstante(Constantes.ROL_AUXILIAR_FINACIERO);

        HashMap<String, String> paramsNew = new HashMap<String, String>();
        paramsNew.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_SOLICITUD_MATERIAL_BIBLIOGRAFICO), idSolicitud);

        String comando = getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CONFIRMACION_COMPRA_MATERIAL_BIBLIOGRAFICO_BIBLIOTECA);
        String header = String.format(Notificaciones.HEADER_CONFIRMAR_COMPRA_MAT_BIB, categoriaResponsable);
        String footer = Notificaciones.FOOTER_CONFIRMAR_COMPRA_MAT_BIB;
        String asunto = Notificaciones.ASUNTO_CONFIRMAR_COMPRA_MAT_BIB;
        String mensaje = String.format(Notificaciones.MENSAJE_CONFIRMAR_COMPRA_MAT_BIB,titulo,autor,p.getPersona().getNombres() + " " + p.getPersona().getApellidos());

        getTareaBean().crearTareaRol(mensaje, tipo,
                categoriaResponsable, true, header, footer, fI, fF, comando, paramsNew,asunto);

        String mensajeCompleto = String.format(Notificaciones.MENSAJE_COMPLETO_CONFIRMAR_COMPRA_MAT_BIB,p.getPersona().getNombres() + " " + p.getPersona().getApellidos(),titulo,autor);

        //Se manda un correo al auxiliar informándole que hay una nueva solicitud de material bibliográfico para mandar comprar
        correoBean.enviarMail(getConstanteBean().getConstante(Constantes.VAL_CORREO_AUXILIAR_MB), asunto, null, null, null, mensajeCompleto);
    }

    private void crearTareaReportarAdquisicion(String idSolicitud) {

        Timestamp fI = new Timestamp(System.currentTimeMillis());
        Timestamp fF = new Timestamp(System.currentTimeMillis() + (3600000L * 24L * 30L * 6L));
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_REPORTAR_ADQUISICION);

        SolicitudMaterialBibliografico solicitud = solicitudFacade.findById(Long.valueOf(idSolicitud));
        Profesor p = solicitud.getProfesor();
        MaterialBibliografico material = solicitud.getMaterialBibliografico();
        String autor = material.getAutor();
        String titulo = material.getTitulo();

        String categoriaResponsable = getConstanteBean().getConstante(Constantes.ROL_AUXILIAR_FINACIERO);

        HashMap<String, String> paramsNew = new HashMap<String, String>();
        paramsNew.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_SOLICITUD_MATERIAL_BIBLIOGRAFICO), idSolicitud);

        String comando = getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CONFIRMACION_LLEGO_A_BIBLIOTECA_MATERIAL_BIBLIOGRAFICO);
        String header = String.format(Notificaciones.HEADER_CONFIRMAR_LLEGO_BIBLIOTECA_MAT_BIB, categoriaResponsable);
        String footer = Notificaciones.FOOTER_CONFIRMAR_LLEGO_BIBLIOTECA_MAT_BIB;
        String asunto = Notificaciones.ASUNTO_CONFIRMAR_LLEGO_BIBLIOTECA_MAT_BIB;
        String mensaje = String.format(Notificaciones.MENSAJE_CONFIRMAR_LLEGO_BIBLIOTECA_MAT_BIB,titulo,autor,p.getPersona().getNombres() + " " + p.getPersona().getApellidos());

        getTareaBean().crearTareaRol(mensaje, tipo,
                categoriaResponsable, true, header, footer, fI, fF, comando, paramsNew,asunto);
    }

    /**
     * Método que retorna el valor de un parámetro, el cuál se encuentra en la lista
     * de parámetros dada, cuyo nombre del campo es igual al que llega como parámetro
     * @param parametros Colección de parámetros
     * @param campo Nombre del campo del parámetro
     * @return Valor del parámetro consultado. Null en caso de que el parámetro no sea encontrado
     */
    private String consultarValorParametro(Collection<Parametro> parametros, String campo) {
        Iterator<Parametro> iterator = parametros.iterator();
        while (iterator.hasNext()) {
            Parametro parametro = iterator.next();
            if (parametro.getCampo().equals(campo)) {
                return parametro.getValor();
            }
        }
        return null;
    }

    
    public String consultarSolicitudPorIdSolicitud(String xml) {
        try {
            getParser().leerXML(xml);
            String idSolicitud = getParser().obtenerSecuencia(getConstanteBean().getConstante((Constantes.TAG_PARAM_ID_SOLICITUD_MATERIAL_BIBLIOGRAFICO))).getValor();
            SolicitudMaterialBibliografico solicitud = getSolicitudFacade().findById(Long.valueOf(idSolicitud));
            if (solicitud != null) {
                Collection<Secuencia> secuencias = new LinkedList<Secuencia>();
                Secuencia secuenciaSolicitudes = getSecuenciaSolicitud(solicitud);
                secuencias.add(secuenciaSolicitudes);
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_SOLICITUD_COMPRA_MATERIAL_BIBLIOGRAFICO_POR_ID_SOLICITUD), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0190, new LinkedList<Secuencia>());
            } else {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_SOLICITUD_COMPRA_MATERIAL_BIBLIOGRAFICO_POR_ID_SOLICITUD), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0044, new LinkedList<Secuencia>());
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(MaterialBibliograficoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_SOLICITUD_COMPRA_MATERIAL_BIBLIOGRAFICO_POR_ID_SOLICITUD), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0044, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(MaterialBibliograficoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public void enviarNotificacionAdquisicionesUltimoMes() {
        List<SolicitudMaterialBibliografico> listaSolicitadosABib = solicitudFacade.findyByEstado(getConstanteBean().getConstante(Constantes.ESTADOS_SOLICITUD_MATERIAL_BIBLIOGRAFICO[3]));

        Calendar calendar = Calendar.getInstance();
        int anioActual = calendar.get(Calendar.YEAR);
        int mesActual = calendar.get(Calendar.MONTH);
        ArrayList<SolicitudMaterialBibliografico> listaSolsUltimoMes = new ArrayList<SolicitudMaterialBibliografico>();
        for (SolicitudMaterialBibliografico solicitudMaterialBibliografico : listaSolicitadosABib) {
            Date fechaEnvioBiblioteca = solicitudMaterialBibliografico.getFechaEnvioABiblioteca();
            if (fechaEnvioBiblioteca != null) {
                Calendar calFechaEnvioBiblioteca = Calendar.getInstance();
                calFechaEnvioBiblioteca.setTime(fechaEnvioBiblioteca);
                int anioSolicitud = calFechaEnvioBiblioteca.get(Calendar.YEAR);
                int mesSolicitud = calFechaEnvioBiblioteca.get(Calendar.MONTH);
                if (anioSolicitud == anioActual && mesActual == mesSolicitud) {
                    listaSolsUltimoMes.add(solicitudMaterialBibliografico);
                }
            }
        }

        if (!listaSolsUltimoMes.isEmpty()) {
            String mensaje = Notificaciones.MENSAJE_ULTIMOS_LIBROS_SOLICITADOS_BIBLIOTECA;
            String listaLibros = "<br><ul>";
            for (SolicitudMaterialBibliografico solicitudMaterialBibliografico : listaSolsUltimoMes) {
                MaterialBibliografico mb = solicitudMaterialBibliografico.getMaterialBibliografico();
                listaLibros += "<li>" + "<b>" + mb.getTitulo() + "</b> - " + mb.getAutor() + ". " + mb.getEditorial() + ". " + mb.getAnio() + ". " + "<i>(Cantidad solicitada:" + mb.getCantidadTotal() + ")</i></li>";
            }
            listaLibros += "</ul>";
            mensaje = mensaje.replaceFirst("%", listaLibros);
            correoBean.enviarMail("profsistemas", Notificaciones.ASUNTO_ULTIMOS_LIBROS_SOLICITADOS_BIBLIOTECA, null, null, null, mensaje);
        }


        //Reprogramar Timer para la siguiente notificacion
        Calendar calSigMes = Calendar.getInstance();
        calSigMes.add(Calendar.MONTH, 1);
        int ultimoDiaMes = calSigMes.getActualMaximum(Calendar.DATE);
        calSigMes.set(Calendar.DATE, ultimoDiaMes);
        calSigMes.set(Calendar.HOUR_OF_DAY, 23);
        calSigMes.set(Calendar.MINUTE, 55);
        calSigMes.set(Calendar.SECOND, 0);
        calSigMes.set(Calendar.MILLISECOND, 0);


        Timestamp tsFechaTimer = new Timestamp(calSigMes.getTimeInMillis());

        if (!timerGenerico.existeTimerCompletamenteIgual("co.uniandes.sisinfo.serviciosnegocio.MaterialBibliograficoLocal", "enviarNotificacionAdquisicionesUltimoMes", tsFechaTimer, ""))
        {
            timerGenerico.crearTimer2("co.uniandes.sisinfo.serviciosnegocio.MaterialBibliograficoLocal", "enviarNotificacionAdquisicionesUltimoMes", tsFechaTimer, "",
                        "MaterialBibliografico", this.getClass().getName(), "enviarNotificacionAdquisicionesUltimoMes", "Este timer se crea cuando se envia notificación de adquisiciones del ultimo mes, una vez ejecutado se programa nuevamente para el proxima mes");
        }

    }

    @Override
    public String editarSolicitudCompraMaterialBibliografico(String xml) {
        try {
            getParser().leerXML(xml);
            Secuencia secSolMat = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUD_MATERIAL_BIBLIOGRAFICO));
            if (secSolMat == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_SOLICITUD_COMPRA_MATERIAL_BIBLIOGRAFICO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0179, new LinkedList<Secuencia>());
            }
            String idSolicitudMaterial = secSolMat.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_ID_SOLICITUD)).getValor();
            SolicitudMaterialBibliografico sol = getSolicitudFacade().findById(Long.parseLong(idSolicitudMaterial));

            if (sol == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_SOLICITUD_COMPRA_MATERIAL_BIBLIOGRAFICO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0179, new LinkedList<Secuencia>());
            } else {
                MaterialBibliografico materialBibliografico = sol.getMaterialBibliografico();
                Secuencia secuenciaMaterial = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUD_MATERIAL_BIBLIOGRAFICO));
                String autor = null;
                String titulo = null;
                String ISBNoISSN = null;
                String editorial = null;
                String anio = null;
                String precio = null;
                String proveedor = null;
                String observaciones = null;
                String nivelFormacion = null;
                String tematica = null;
                boolean textoGuia=false;
                String cursoTextoGuia=null;
                Secuencia secAutor = secuenciaMaterial.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_AUTOR));
                if (secAutor != null) {
                    autor = secAutor.getValor();
                }

                Secuencia secTitulo = secuenciaMaterial.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TITULO));
                if (secTitulo != null) {
                    titulo = secTitulo.getValor();
                }

                Secuencia secISBN = secuenciaMaterial.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ISBN_O_ISSN));
                if (secISBN != null) {
                    ISBNoISSN = secISBN.getValor();
                }

                Secuencia secEditorial = secuenciaMaterial.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_EDITORIAL));
                if (secEditorial != null) {
                    editorial = secEditorial.getValor();
                }

                Secuencia secAnio = secuenciaMaterial.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ANIO));
                if (secAnio != null) {
                    anio = secAnio.getValor();
                }

                Secuencia secPrecio = secuenciaMaterial.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PRECIO));
                if (secPrecio != null) {
                    precio = secPrecio.getValor();
                }

                Secuencia secProveedor = secuenciaMaterial.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROVEEDOR));
                if (secProveedor != null) {
                    proveedor = secProveedor.getValor();
                }

                Secuencia secObservaciones = secuenciaMaterial.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OBSERVACIONES));
                if (secObservaciones != null) {
                    observaciones = secObservaciones.getValor();
                }

                Secuencia secFormacion = secuenciaMaterial.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_FORMACION));
                if (secFormacion != null) {
                    nivelFormacion = secFormacion.getValor();
                }

                Secuencia secTematica = secuenciaMaterial.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TEMATICA));
                if (secTematica != null) {
                    tematica = secTematica.getValor();
                }

                Secuencia secTextoGuia = secuenciaMaterial.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TEXTO_GUIA));
                if (secTextoGuia != null) {
                    textoGuia = Boolean.parseBoolean(secTextoGuia.getValor());
                }

                Secuencia secCursoTextoGuia = secuenciaMaterial.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO_TEXTO_GUIA));
                if (secCursoTextoGuia != null) {
                    cursoTextoGuia = secCursoTextoGuia.getValor();
                }

                //Creación de material bibliográfico
                if (anio != null) {
                    materialBibliografico.setAnio(Integer.parseInt(anio));
                } else {
                    materialBibliografico.setAnio(0);
                }

                materialBibliografico.setAutor(autor);
                materialBibliografico.setEditorial(editorial);
                materialBibliografico.setISBNoISSN(ISBNoISSN);
                materialBibliografico.setNivelFormacion(nivelFormacion);
                materialBibliografico.setTextoGuia(textoGuia);
                materialBibliografico.setCursoTextoGuia(cursoTextoGuia);
                //Procesar los volúmenes de la solicitud
                Secuencia secVolumenes = secuenciaMaterial.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VOLUMENES));

                Collection<Volumen> volumenes = new ArrayList<Volumen>();
                int numeroEjem = 0;
                if (secVolumenes != null) {
                    Collection<Secuencia> secuenciasVolumen = secVolumenes.getSecuencias();

                    for (Secuencia secuenciaVolumen : secuenciasVolumen) {
                        if (secuenciaVolumen.getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_VOLUMEN))) {
                            String numeroVolumen = secuenciaVolumen.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_VOLUMEN)).getValor();
                            String numero = secuenciaVolumen.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFO_VOLUMEN)).getValor();
                            String numeroEjemplares = secuenciaVolumen.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_EJEMPLARES)).getValor();
                            Volumen v = new Volumen();
                            v.setNumeroVolumen(Double.parseDouble(numeroVolumen));
                            v.setNumeroEjemplares(Integer.parseInt(numeroEjemplares));
                            int n = Integer.parseInt(numero);
                            v.setVolumen(n);
                            volumenes.add(v);
                            numeroEjem += Integer.parseInt(numeroEjemplares);
                        }
                    }
                } else {
                    String cant = secuenciaMaterial.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_EJEMPLARES)).getValor();
                    numeroEjem = Integer.parseInt(cant);
                }

                if (!validarNumeroEjemplares(textoGuia, numeroEjem)){
                    int numMaxEjemplares = Integer.parseInt(getConstanteBean().getConstante(Constantes.VAL_NUMERO_MAX_EJEMPLARES_NO_TEXTO_GUIA));
                    Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                    Secuencia secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_MENSAJE), Integer.toString(numMaxEjemplares));
                    //agrega el parámetro a la lista
                    parametros.add(secParametro);
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CREAR_SOLICITUD_COMPRA_MATERIAL_BIBLIOGRAFICO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_MB_0041, parametros);
                }

                materialBibliografico.setVolumenes(volumenes);
                materialBibliografico.setObservaciones(observaciones);
                if (precio != null) {
                    materialBibliografico.setPrecio(Double.parseDouble(precio));
                }


                materialBibliografico.setProveedor(proveedor);
                materialBibliografico.setTematica(tematica);
                materialBibliografico.setTitulo(titulo);
                materialBibliografico.setCantidadTotal(numeroEjem);
                getMaterialBibliograficoFacadeLocal().edit(materialBibliografico);
                
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_SOLICITUD_COMPRA_MATERIAL_BIBLIOGRAFICO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0181, new LinkedList<Secuencia>());
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(MaterialBibliograficoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_SOLICITUD_COMPRA_MATERIAL_BIBLIOGRAFICO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0031, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(MaterialBibliograficoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }
    @Override
    public String enviarConfirmacionLLegoMaterialABiblioteca(String xml) {
        try {
            getParser().leerXML(xml);
            Secuencia secSolMat = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUD_MATERIAL_BIBLIOGRAFICO));
            if (secSolMat == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_SOLICITUD_COMPRA_MATERIAL_BIBLIOGRAFICO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0179, new LinkedList<Secuencia>());
            }
            String idSolicitudMaterial = secSolMat.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_ID_SOLICITUD)).getValor();
            SolicitudMaterialBibliografico sol = getSolicitudFacade().findById(Long.parseLong(idSolicitudMaterial));
            //mirar si el estado es esperando biblioteca,
            if (sol.getEstado() != null && sol.getEstado().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITADO_A_BIBLIOTECA))) {

                Properties params = new Properties();
                params.put(getConstanteBean().getConstante((Constantes.TAG_PARAM_ID_SOLICITUD_MATERIAL_BIBLIOGRAFICO)),idSolicitudMaterial);
                TareaSencilla tarea = tareaSencillaFacade.findById(tareaBean.consultarIdTareaPorParametrosTipo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_REPORTAR_ADQUISICION)
                    , params));
                if (tarea!=null) {
                    tareaSencillaBean.realizarTareaPorId(tarea.getId());
                }

                //enviar notificacion al profesor
                Profesor p = sol.getProfesor();
                MaterialBibliografico mat = sol.getMaterialBibliografico();
                String asunto = Notificaciones.ASUNTO_LLEGO_A_BIBLIOTECA_MAT_BIB;
                String mensaje = Notificaciones.MENSAJE_LLEGO_A_BIBLIOTECA_MAT_BIB;
                mensaje = mensaje.replaceFirst("%", p.getPersona().getNombres() + " " + p.getPersona().getApellidos());
                mensaje = mensaje.replaceFirst("%", mat.getTitulo());
                mensaje = mensaje.replaceFirst("%", mat.getAutor());
                mensaje = mensaje.replaceFirst("%", mat.getEditorial());
                mensaje = mensaje.replaceFirst("%", "" + mat.getCantidadTotal());

                correoBean.enviarMail(p.getPersona().getCorreo(), asunto, null, null, null, mensaje);
                //cambiar estado
                sol.setEstado(getConstanteBean().getConstante(Constantes.TAG_PARAM_LLEGO_A_BIBLIOTECA));
                //guardar
                getSolicitudFacade().edit(sol);
                //enviar msj ok

                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CONFIRMACION_LLEGO_A_BIBLIOTECA_MATERIAL_BIBLIOGRAFICO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
            } else {
                //lanzar error de que la solicitud no ha sido pedida a biblioteca
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CONFIRMACION_LLEGO_A_BIBLIOTECA_MATERIAL_BIBLIOGRAFICO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_MB_0040, new LinkedList<Secuencia>());
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(MaterialBibliograficoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CONFIRMACION_LLEGO_A_BIBLIOTECA_MATERIAL_BIBLIOGRAFICO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_MB_0039, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(MaterialBibliograficoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    /**
     * Renueva las tareas de solicitud de material bibliografico.
     */
    public void renovarTareasSolicitudMaterialBibliografico() {

        // Recorremos tarea por tipo. Agregamos desde el tipo más avanzado de tarea en un LinkedHashMap.
        Map<String, ISolicitudMaterialBibliograficoTarea> mapTareas = new LinkedHashMap<String, ISolicitudMaterialBibliograficoTarea>();
        mapTareas.put(
            getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_REPORTAR_ADQUISICION),
            new SolicitudMaterialBibliograficoTareaReportarAdquisicion());
        mapTareas.put(
            getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CONFIRMAR_COMPRA),
            new SolicitudMaterialBibliograficoTareaConfirmarCompra());
        mapTareas.put(
            getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_AUTORIZAR_SOLICITUD),
            new SolicitudMaterialBibliograficoTareaAutorizar());
        this.listSolicitud = new ArrayList<String>();
        for (String constante : mapTareas.keySet()) {

            Collection<TareaSencilla> listaTareas = tareaSencillaFacade.findByTipo(constante);
            for (TareaSencilla tarea : listaTareas) {

                tareaSencillaBean.realizarTareaPorId(tarea.getId()); // Realizamos tarea.
                mapTareas.get(constante).renovarTarea(tarea); // Renovamos.
            }
        }
    }

    /**
     * Interfaz de manejo de solicitudes con sus tareas.
     */
    public interface ISolicitudMaterialBibliograficoTarea {

        void renovarTarea(TareaSencilla tarea);
    }

    /**
     * Clase de manejo de solicitudes con sus tareas para autorizar compra de material.
     */
    public class SolicitudMaterialBibliograficoTareaAutorizar implements ISolicitudMaterialBibliograficoTarea {

        public void renovarTarea(TareaSencilla tarea) {

            // Creamos una tarea nueva.
            String constante = getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_SOLICITUD_MATERIAL_BIBLIOGRAFICO);
            SolicitudMaterialBibliografico solicitud = null;
            for(Parametro parametro : tarea.getParametros()) {

                if (parametro.getCampo().equals(constante)) {

                    if (!listSolicitud.contains(parametro.getValor())) {

                        solicitud =
                            getSolicitudFacade().findById(Long.valueOf(parametro.getValor()));
                        listSolicitud.add(parametro.getValor());
                    }
                    break;
                }
            }
            if (solicitud != null) {

                crearTareaAutorizacionCompraMaterialBibliografico(
                        Long.toString(solicitud.getId()),
                        solicitud.getProfesor(),
                        solicitud.getMaterialBibliografico().getAutor(),
                        solicitud.getMaterialBibliografico().getTitulo());
            }
        }
    }

    /**
     * Clase de manejo de solicitudes con sus tareas para confirmar compra de material.
     */
    public class SolicitudMaterialBibliograficoTareaConfirmarCompra implements ISolicitudMaterialBibliograficoTarea {

        public void renovarTarea(TareaSencilla tarea) {

            // Creamos una tarea nueva.
            String constante = getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_SOLICITUD_MATERIAL_BIBLIOGRAFICO);
            for(Parametro parametro : tarea.getParametros()) {

                if (parametro.getCampo().equals(constante)) {

                    if (!listSolicitud.contains(parametro.getValor())) {

                        crearTareaCompraMaterialBibliografico(parametro.getValor());
                        listSolicitud.add(parametro.getValor());
                    }
                    break;
                }
            }
        }
    }

    /**
     * Clase de manejo de solicitudes con sus tareas para reportar adquisición de material.
     */
    public class SolicitudMaterialBibliograficoTareaReportarAdquisicion implements ISolicitudMaterialBibliograficoTarea {

        public void renovarTarea(TareaSencilla tarea) {

            // Creamos una tarea nueva.
            String constante = getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_SOLICITUD_MATERIAL_BIBLIOGRAFICO);
            for(Parametro parametro : tarea.getParametros()) {

                if (parametro.getCampo().equals(constante)) {

                    if (!listSolicitud.contains(parametro.getValor())) {

                        crearTareaReportarAdquisicion(parametro.getValor());
                        listSolicitud.add(parametro.getValor());
                    }
                    break;
                }
            }
        }
    }
}
