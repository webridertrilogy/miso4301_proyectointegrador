/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosnegocio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.entities.CategoriaEventoExterno;
import co.uniandes.sisinfo.entities.EventoExterno;
import co.uniandes.sisinfo.entities.TipoCampo;
import co.uniandes.sisinfo.serviciosfuncionales.CategoriaEventoExternoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ContactoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.EventoExternoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.TipoCampoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;

/**
 *
 * @author Administrador
 */
@Stateless
public class EventoExternoBean implements  EventoExternoBeanLocal {

    private final static String RUTA_INTERFAZ_REMOTA = "co.uniandes.sisinfo.serviciosnegocio.EventoExternoBeanLocal";
    private final static String NOMBRE_METODO_TIMER = "manejoTimmersEventoExterno";
    private ServiceLocator serviceLocator;
    @EJB
    private ConstanteLocal constanteBean;
    @EJB
    private EventoExternoFacadeLocal eventoExternoFacade;
    private ParserT parser;
    private ConversorEventoExterno conversor;
    @EJB
    private CategoriaEventoExternoFacadeLocal categoriaFacade;
    @EJB
    private ContactoFacadeLocal contactoFacade;
    @EJB
    private TipoCampoFacadeLocal tipoCampoFacade;
    @EJB
    private TimerGenericoBeanLocal timerGenerico;

    public EventoExternoBean() {
        parser = new ParserT();
//        try {
//            serviceLocator = new ServiceLocator();
//            constanteBean = (ConstanteLocal) serviceLocator.getLocalEJB(ConstanteLocal.class);
//            timerGenerico = (TimerGenericoBeanLocal) serviceLocator.getLocalEJB(TimerGenericoBeanLocal.class);
//
//        } catch (NamingException ex) {
//            Logger.getLogger(EventoExternoBean.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public ConstanteLocal getConstanteBean() {
        return constanteBean;
    }

    public String crearEditarEventoExterno(String xml) {
        String respuesta = null;
        try {
            getParser().leerXML(xml);

            Secuencia secuenciaEvento = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_EVENTO_EXTERNO));
            EventoExterno eventoexterno = getConversor().consultarEventoExterno(secuenciaEvento);

            String estadoAnterior = getConstanteBean().getConstante(Constantes.VAL_ESTADO_EVENTO_ABIERTO);

            if (eventoexterno.getId() == null) {
                eventoexterno.setEstado(getConstanteBean().getConstante(Constantes.VAL_ESTADO_EVENTO_ABIERTO));
                EventoExterno temp = eventoExternoFacade.findEventosByTitulo(eventoexterno.getTitulo());
                //si no existe nigun otro evento con el mismo nombre, creelo
                if (temp == null) {
                    eventoExternoFacade.create(eventoexterno);
                } else {
                    //error que indica que hay otro enveto con el mismo nombre
                    return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_CATEGORIA_EVENTO_EXTERNO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0163, new ArrayList());
                }
            } else {
                EventoExterno eventoAnterior = eventoExternoFacade.find(eventoexterno.getId());
                estadoAnterior = eventoAnterior.getEstado();
                eventoexterno.setInscripciones(eventoAnterior.getInscripciones());
            }
            String ruta = eventoexterno.getRutaImagen();
            if (ruta != null && !ruta.trim().isEmpty()) {
                String extension = ruta.substring(ruta.lastIndexOf("."));
                ruta = ruta.replace("\\", "/");
                if (ruta.contains(getConstanteBean().getConstante(Constantes.RUTA_IMAGEN_EVENTO_EXTERNO_TEMPORAL))) {
                    String nuevaRuta = getConstanteBean().getConstante(Constantes.RUTA_IMAGEN_EVENTO_EXTERNO) + "ImgEvento_" + eventoexterno.getTitulo() + extension;
                    FileChannel source = new FileInputStream(new File(ruta)).getChannel();
                    FileChannel destination = new FileOutputStream(new File(nuevaRuta)).getChannel();
                    destination.transferFrom(source, 0, source.size());
                    eventoexterno.setRutaImagen(nuevaRuta);
                }
            }

            eventoexterno.setLinkInscripcion(String.format(getConstanteBean().getConstante(Constantes.VAL_LINK_INSCRIPCION_EVENTO), "" + eventoexterno.getId()));
            eventoexterno.setEstado(estadoAnterior);
            eventoExternoFacade.edit(eventoexterno);

            //Si no existe, se crea un timer que revise todos los dias los eventos que ya pasaron y les cambie el estado a "Cerrado"
//            boolean existe = timerGenerico.existeTimerCompletamenteIgual(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, getConstanteBean().getConstante(Constantes.CMD_CERRAR_EVENTOS_CON_FECHAS_ANTERIORES_A_HOY));
//            if (!existe) {
//                long day = 1000 * 60 * 60 * 24;
//                long time = diaDeHoySinHora().getTime() + day;
//                timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(time), getConstanteBean().getConstante(Constantes.CMD_CERRAR_EVENTOS_CON_FECHAS_ANTERIORES_A_HOY), "EventoExternoBean", this.getClass().getName(), "cerrarEventosConFechasAnterioresAHoy", "este timer cierra todos los eventos que ya pasaron");
//            }


            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            respuesta = parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_EDITAR_EVENTO_EXTERNO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());
            return respuesta;

        } catch (Exception ex) {
            Logger.getLogger(EventoExternoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return respuesta;
    }

    public String consultarEventoExterno(String xml) {
        try {
            parser.leerXML(xml);
            //------ obtener datos del xml...
            Secuencia sec = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_EVENTO_EXTERNO));
            Long idEventoExterno = Long.parseLong(sec.getValor().trim());
            EventoExterno evento = eventoExternoFacade.find(idEventoExterno);
            Secuencia secInformacionEventoExterno = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_CONTACTOS), "");

            Secuencia secEvento = getConversor().consultarEventoExterno(evento);

            secInformacionEventoExterno.agregarSecuencia(secEvento);
            ArrayList<Secuencia> secs = new ArrayList<Secuencia>();
            secs.add(secInformacionEventoExterno);
            return getParser().generarRespuesta(secs, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_EVENTO_EXTERNO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.ERR_0001, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(ContactoBean.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    @Override
    public String consultarEventosExternos(String xml) {
        String respuesta = null;
        try {
            getParser().leerXML(xml);
            Collection<EventoExterno> eventosExternos = eventoExternoFacade.findAll();
            Secuencia secEventos = getConversor().consultarEventosExternos(eventosExternos);
            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secEventos);
            respuesta = parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_EVENTOS_EXTERNOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());
            return respuesta;
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(EventoExterno.class.getName()).log(Level.SEVERE, null, ex);
            return respuesta;
        }
    }

    public String eliminarCategoria(String xml) {
        try {
            parser.leerXML(xml);
            //------ obtener datos del xml...
            Secuencia sec = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_CATEGORIA));
            Long idCategoria = Long.parseLong(sec.getValor().trim());
            CategoriaEventoExterno categoria = categoriaFacade.find(idCategoria);

            //se obtienen los eventos que tengan relacion con la categoria
            Collection<EventoExterno> eventosRelacionados = eventoExternoFacade.findEventosByIdCategoria(idCategoria);

            //si no hay enventos relacionados con sicha categoria, se elimina la categoria
            if (eventosRelacionados.isEmpty()) {
                categoriaFacade.remove(categoria);
            } else {
                return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_CATEGORIA_EVENTO_EXTERNO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0161, new ArrayList());
            }
            return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_CATEGORIA_EVENTO_EXTERNO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(ContactoBean.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    public String cambiarEstadoEvento(String xml) {
        try {
            parser.leerXML(xml);
            //------ obtener datos del xml...
            Secuencia secIdEvento = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Secuencia secEstado = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO));

            Long idEvento = Long.parseLong(secIdEvento.getValor().trim());
            EventoExterno evento = eventoExternoFacade.find(idEvento);
            evento.setEstado(secEstado.getValor());

            return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_CAMBIAR_ESTADO_EVENTO_EXTERNO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(ContactoBean.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    public String eliminarTipoCampo(String xml) {
        try {
            parser.leerXML(xml);
            //------ obtener datos del xml...
            Secuencia sec = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_TIPO_CAMPO));
            Long idTipoCampo = Long.parseLong(sec.getValor().trim());
            TipoCampo tipoCampo = tipoCampoFacade.find(idTipoCampo);

            if (tipoCampo != null) {
                tipoCampoFacade.remove(tipoCampo);
            }
            return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_CATEGORIA_EVENTO_EXTERNO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(ContactoBean.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    public String editarCategorias(String xml) {

        try {
            String respuesta = null;
            parser.leerXML(xml);
            Secuencia secCategorias = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CATEGORIAS_EVENTO_EXTERNO));
            Collection<CategoriaEventoExterno> categoriasNuevas = getConversor().consultarCategoriasEventoExterno(secCategorias);

            Collection<CategoriaEventoExterno> categoriasExistentes = categoriaFacade.findAll();


            //se crean las categorias nuevas
            if (categoriasNuevas != null) {
                for (CategoriaEventoExterno categoriaNueva : categoriasNuevas) {
                    if (categoriaNueva.getId() == null) {
                        categoriaFacade.create(categoriaNueva);
                    }
                }
            }


            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            respuesta = parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_CATEGORIAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());
            return respuesta;
        } catch (Exception ex) {
            Logger.getLogger(ContactoBean.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    public String editarTiposCampo(String xml) {

        try {
            String respuesta = null;
            parser.leerXML(xml);
            Secuencia secTiposCampo = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPOS_CAMPO));
            Collection<TipoCampo> tiposCampoNuevos = getConversor().consultarTiposCampo(secTiposCampo);

            Collection<TipoCampo> tiposCampoExistentes = tipoCampoFacade.findAll();



            //se crean los tiposCampo nuevos
            if (tiposCampoNuevos != null) {
                for (TipoCampo tipoCampoNuevo : tiposCampoNuevos) {
                    if (tipoCampoNuevo.getId() == null) {
                        //tipoCampo nuevo
                        tipoCampoFacade.create(tipoCampoNuevo);
                    }
                }
            }

            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            respuesta = parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_GUARDAR_TIPOS_CAMPO_ADICIONAL_EVENTO_EXTERNO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());
            return respuesta;
        } catch (Exception ex) {
            Logger.getLogger(ContactoBean.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    public String consultarCategorias(String xml) {
        String respuesta = null;
        try {
            getParser().leerXML(xml);
            Collection<CategoriaEventoExterno> categoriasEventoExterno = categoriaFacade.findAll();
            Secuencia secCategoriasEventoExterno = getConversor().consultarCategorias(categoriasEventoExterno);
            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secCategoriasEventoExterno);
            respuesta = parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_CATEGORIAS_EVENTO_EXTERNO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());
            return respuesta;
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(EventoExterno.class.getName()).log(Level.SEVERE, null, ex);
            return respuesta;
        }
    }

    public String consultarTiposCampoAdicional(String xml) {
        String respuesta = null;
        try {
            getParser().leerXML(xml);

            Collection<TipoCampo> tipoCampoEventoExterno = tipoCampoFacade.findAll();

            Secuencia secTipoCampoEventoExterno = getConversor().consultarTiposCampo(tipoCampoEventoExterno);

            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secTipoCampoEventoExterno);

            respuesta = parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_TIPOS_CAMPO_ADICIONAL_EVENTO_EXTERNO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());
            return respuesta;
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(EventoExterno.class.getName()).log(Level.SEVERE, null, ex);
            return respuesta;
        }
    }

    private ParserT getParser() {
        if (parser == null) {
            parser = new ParserT();
        }
        return parser;
    }

    public ConversorEventoExterno getConversor() {
        if (conversor == null) {
            conversor = new ConversorEventoExterno(constanteBean, eventoExternoFacade, categoriaFacade);
        }
        return conversor;
    }

    public String eliminarEventoExterno(String xml) {
        try {
            parser.leerXML(xml);
            //------ obtener datos del xml...
            Secuencia sec = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_EVENTO_EXTERNO));
            Long idEventoExterno = Long.parseLong(sec.getValor().trim());

            EventoExterno evento = eventoExternoFacade.find(idEventoExterno);
            if (evento != null) {
                eventoExternoFacade.remove(evento);
            }
            return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_EVENTO_EXTERNO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(ContactoBean.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    public String subirImagenEventoExterno(String comandoXML) {
        try {
            parser.leerXML(comandoXML);
            Secuencia secTituloEvento = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TITULO));
            Secuencia secRutaImagen = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA_IMAGEN));
            String tituloEvento = secTituloEvento.getValor();
            EventoExterno eventoExterno = eventoExternoFacade.findEventosByTitulo(tituloEvento);
            if (secRutaImagen != null) {
                String ruta = getConstanteBean().getConstante(Constantes.RUTA_IMAGEN_EVENTO_EXTERNO) + secRutaImagen.getValor();
                eventoExterno.setRutaImagen(ruta);
                eventoExternoFacade.edit(eventoExterno);
            }
            return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_IMAGEN_EVENTO_EXTERNO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(EventoExternoBean.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    public void manejoTimmersEventoExterno(String comando) {

        if (comando.equals(getConstanteBean().getConstante(Constantes.CMD_CERRAR_EVENTOS_CON_FECHAS_ANTERIORES_A_HOY))) {
            cerrarEventrosConFechasAnterioresAHoy();
        }


    }

    /**
     * Cambia a estado "Cerrado" todos los eventos que esten en estado "Abierto" y su fecha de inicio sea menor a hoy.
     */
    private void cerrarEventrosConFechasAnterioresAHoy() {
        Collection<EventoExterno> eventos = eventoExternoFacade.findEventosByEstado(getConstanteBean().getConstante(Constantes.VAL_ESTADO_EVENTO_ABIERTO));
        //revisa si existe algun evento que ya paso. Si es asi, lo cambia a estado "Cerrado"
        for (EventoExterno evento : eventos) {
            if (evento.getFechaHoraInicio().before(new Date()) ) {
                evento.setEstado(getConstanteBean().getConstante(Constantes.VAL_ESTADO_EVENTO_CERRADO));
            }
            eventoExternoFacade.edit(evento);
        }

        //renueva el timer
        long day = 1000 * 60 * 60 * 24;
        long time = diaDeHoySinHora().getTime() + day;
        timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(time), getConstanteBean().getConstante(Constantes.CMD_CERRAR_EVENTOS_CON_FECHAS_ANTERIORES_A_HOY), "EventoExternoBean", this.getClass().getName(), "cerrarEventosConFechasAnterioresAHoy", "este timer cierra todos los eventos que ya pasaron");

    }

    /**
     * Retorna un objeto de tipo date con la fecha de hoy a la hora 00:00
     * @return
     */
    private Date diaDeHoySinHora() {

        Calendar today = Calendar.getInstance();
        today.setTime(new Date());
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        return today.getTime();
    }
}
