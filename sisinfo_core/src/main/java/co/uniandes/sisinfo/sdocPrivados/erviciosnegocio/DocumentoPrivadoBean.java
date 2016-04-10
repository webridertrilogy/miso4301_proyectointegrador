package co.uniandes.sisinfo.sdocPrivados.erviciosnegocio;

import co.uniandes.sisinfo.serviciosnegocio.DocumentoPrivadoRemote;
import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.docPrivados.entities.DocumentoPrivado;
import co.uniandes.sisinfo.docPrivados.entities.Nodo;
import co.uniandes.sisinfo.docPrivados.entities.PalabraClave;
import co.uniandes.sisinfo.docPrivados.entities.Publicador;
import co.uniandes.sisinfo.docPrivados.serviciosfuncionales.DocumentoPrivadoFacadeLocal;
import co.uniandes.sisinfo.docPrivados.serviciosfuncionales.NodoFacadeLocal;
import co.uniandes.sisinfo.docPrivados.serviciosfuncionales.PublicadorFacadeLocal;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.PersonaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import co.uniandes.sisinfo.serviciosnegocio.ConstanteRemote;
import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

/**
 * Servicios de administración de documentos
 * @author David Naranjo, Camilo Cortés, Marcela Morales
 */
@Stateless
public class DocumentoPrivadoBean implements DocumentoPrivadoRemote, DocumentoPrivadoLocal {

    //---------------------------------------
    // Atributos
    //---------------------------------------
    //Locales
    @EJB
    private DocumentoPrivadoFacadeLocal documentoPrivadoFacade;
    @EJB
    private NodoFacadeLocal nodoFacade;
    @EJB
    private PublicadorFacadeLocal publicadorFacade;
    //Remotos
    @EJB
    private PersonaFacadeRemote personaFacade;
    //Útiles
    private ParserT parser;
    @EJB
    private ConstanteRemote constanteBean;
    private ServiceLocator serviceLocator;
    
    //---------------------------------------
    // Constructor
    //---------------------------------------
    /**
     * Constructor de DocumentoPrivadoBean
     */
    public DocumentoPrivadoBean() {
        try {
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            personaFacade = (PersonaFacadeRemote) serviceLocator.getRemoteEJB(PersonaFacadeRemote.class);
        } catch (NamingException ex) {
            Logger.getLogger(DocumentoPrivadoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //---------------------------------------
    // Métodos
    //---------------------------------------
    @Override
    public String subirMetadatosDocumentoPrivado(String xmlComando) {
        try {
            //Se extraen los parámetros del comando
            getParser().leerXML(xmlComando);
            String correo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            String titulo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TITULO)).getValor();
            Long idPadre = Long.parseLong(getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_NODO_PADRE)).getValor());
            Publicador publicador = null;
            publicador = getPublicadorFacade().findByCorreo(correo);
            if (publicador == null) {
                publicador = new Publicador();
                Persona persona = getPersonaFacade().findByCorreo(correo);
                publicador.setPersona(persona);
            }
            getPublicadorFacade().create(publicador);
            //Desactiva todos los documentos anteriores
            desactivarDocumentos();
            //Se crea un nuevo documento con los metadatos suministrados
            DocumentoPrivado documento = new DocumentoPrivado();
            documento.setPublicador(publicador);
            documento.setTitulo(titulo);
            //documento.setPalabrasClave(lista);
            documento.setIdPadre(idPadre);
            documento.setActivo(true);
            //Se persiste el documento privado
            getDocumentoPrivadoFacade().create(documento);
            Nodo nodo = getNodoFacade().find(idPadre);
            nodo.agregarDoc(documento);
            //Se genera una respuesta
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            Secuencia secDocumento = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DOCUMENTO), "");
            String idDocumento = "" + documento.getId();
            Secuencia secIdDocumento = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_IDDOCUMENTO), "" + idDocumento);
            secDocumento.agregarSecuencia(secIdDocumento);
            secuencias.add(secDocumento);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_SUBIR_METADATOS_DOCPRIVADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0152, new LinkedList<Secuencia>());

        } catch (Exception ex) {
            try {
                Logger.getLogger(DocumentoPrivadoBean.class.getName()).log(Level.SEVERE, null, ex);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_METADATOS_DOCPRIVADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0124, new LinkedList<Secuencia>());
            } catch (Exception ex1) {
                Logger.getLogger(DocumentoPrivadoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    @Override
    public String actualizarMetadatosDocumentoPrivado(String xmlComando) {
        try {
            //Se extraen los parámetros del comando
            getParser().leerXML(xmlComando);
            Secuencia secDocumento = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DOCUMENTO));
            String idDocumento = secDocumento.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_IDDOCUMENTO)).getValor();
            String titulo = secDocumento.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TITULO)).getValor();
            //Buscamos el documento con el identificador dado
            Long id = Long.valueOf(idDocumento).longValue();
            DocumentoPrivado documento = getDocumentoPrivadoFacade().find(id);
            //Si el documento no existe retorna un mensaje de error
            if (documento == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_METADATOS_DOCPRIVADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0125, new LinkedList<Secuencia>());
            }
            //El documento si existe y actualizamos los datos del documento con los parámetros suministrados
            //Desactiva todos los documentos anteriores
            desactivarDocumentos();
            //Modifica el documento
            documento.setTitulo(titulo);
            documento.setActivo(true);
            Collection<PalabraClave> lista = documento.getPalabrasClave();
            lista.clear();
            getDocumentoPrivadoFacade().edit(documento);
            documento.setPalabrasClave(new ArrayList<PalabraClave>());
            Timestamp fecha = new Timestamp(System.currentTimeMillis());
            documento.setFecha(fecha);
            //Se persisten los cambios al documento
            getDocumentoPrivadoFacade().edit(documento);
            //Se genera una respuesta
            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_METADATOS_DOCPRIVADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0153, new LinkedList<Secuencia>());
            
        } catch (Exception ex) {
            try {
                Logger.getLogger(DocumentoPrivadoBean.class.getName()).log(Level.SEVERE, null, ex);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_METADATOS_DOCPRIVADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0124, new LinkedList<Secuencia>());
            } catch (Exception ex1) {
                Logger.getLogger(DocumentoPrivadoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    @Override
    public String confirmarSubidaDocumentoPrivado(String xmlComando) {
        try {
            //Se extraen los parámetros del comando
            getParser().leerXML(xmlComando);
            String idDocumento = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_IDDOCUMENTO)).getValor();
            String rutaDocumento = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA_DIRECTORIO)).getValor();
            String mime = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MIME)).getValor();
            //Buscamos el documento con el identificador dado
            Long id = Long.valueOf(idDocumento).longValue();
            DocumentoPrivado documento = getDocumentoPrivadoFacade().find(id);
            //Si el documento no existe retorna un mensaje de error
            if (documento == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONFIRMAR_SUBIDA_DOCPRIVADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0125, new LinkedList<Secuencia>());
            }
            //El documento si existe y procedemos a realizar las actualizaciones
            //Desactiva todos los documentos anteriores
            desactivarDocumentos();
            //Verificamos si el documento tiene una ruta de documento establecida
            String rutaAnterior = documento.getRuta();
            //Si hay una ruta de directorio se borra el documento físico actual
            if (rutaAnterior != null && !rutaAnterior.equals("") && !rutaAnterior.equals(rutaDocumento)) {
                eliminarArchivoFisico(rutaAnterior);
            }
            //Se actualizan los datos del documento
            documento.setRuta(rutaDocumento);
            documento.setTipoMime(mime);
            documento.setFecha(new Timestamp(System.currentTimeMillis()));
            documento.setActivo(true);
            //Se persisten los cambios al documento
            getDocumentoPrivadoFacade().edit(documento);
            //Se genera una respuesta
            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONFIRMAR_SUBIDA_DOCPRIVADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0154, new LinkedList<Secuencia>());
            
        } catch (Exception ex) {
            try {
                Logger.getLogger(DocumentoPrivadoBean.class.getName()).log(Level.SEVERE, null, ex);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONFIRMAR_SUBIDA_DOCPRIVADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0124, new LinkedList<Secuencia>());
            } catch (Exception ex1) {
                Logger.getLogger(DocumentoPrivadoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    @Override
    public String consultarDatosDocumentoPrivado(String xmlComando) {
        try {
            //Se extraen los parámetros del comando
            getParser().leerXML(xmlComando);
            String idDocumento = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_IDDOCUMENTO)).getValor();
            //Buscamos el documento con el identificador dado
            Long id = Long.valueOf(idDocumento).longValue();
            DocumentoPrivado documento = getDocumentoPrivadoFacade().find(id);
            //Si el documento no existe retorna un mensaje de error
            if (documento == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_DATOS_DOCPRIVADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0125, new LinkedList<Secuencia>());
            } 
            //Desactiva todos los documentos anteriores
            desactivarDocumentos();
            documento.setActivo(true);
            getDocumentoPrivadoFacade().edit(documento);
            //Se extraen los datos del publicador
            Publicador publicador = documento.getPublicador();
            String correo = publicador.getPersona().getCorreo();
            String nombres = publicador.getPersona().getNombres();
            String apellidos = publicador.getPersona().getApellidos();
            Boolean esActivo = documento.getActivo();
            //Se formatea la fecha del documento al formato: yyyy/mm/dd
            Timestamp tFecha = documento.getFecha();
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(tFecha.getTime());
            int anioI = cal.get(Calendar.YEAR);
            int mesI = cal.get(Calendar.MONTH) + 1;
            int diaI = cal.get(Calendar.DAY_OF_MONTH);
            String anio = "" + anioI;
            String mes = "";
            if (mesI < 10) {
                mes = "0" + mesI;
            } else {
                mes = "" + mesI;
            }
            String dia = "";
            if (diaI < 10) {
                dia = "0" + diaI;
            } else {
                dia = "" + diaI;
            }
            String fecha = anio + "/" + mes + "/" + dia;
            //Se genera una respuesta
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            Secuencia secDocumento = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DOCUMENTO), "");
            Secuencia secIdDocumento = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_IDDOCUMENTO), "" + idDocumento);
            Secuencia secCorreo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), correo);
            Secuencia secNombres = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), nombres + " " + apellidos);
            Secuencia secTitulo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TITULO), documento.getTitulo());
            Secuencia secFecha = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_PUBLICACION), fecha);
            Secuencia secEsActivo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ACTIVA), (esActivo == null) ? "false" : esActivo.toString());
            secDocumento.agregarSecuencia(secIdDocumento);
            secDocumento.agregarSecuencia(secCorreo);
            secDocumento.agregarSecuencia(secNombres);
            secDocumento.agregarSecuencia(secTitulo);
            secDocumento.agregarSecuencia(secFecha);
            secDocumento.agregarSecuencia(secEsActivo);
            secuencias.add(secDocumento);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_DATOS_DOCPRIVADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0155, new LinkedList<Secuencia>());
            
        } catch (Exception ex) {
            try {
                Logger.getLogger(DocumentoPrivadoBean.class.getName()).log(Level.SEVERE, null, ex);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_DATOS_DOCPRIVADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0124, new LinkedList<Secuencia>());
            } catch (Exception ex1) {
                Logger.getLogger(DocumentoPrivadoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    @Override
    public String consultarDocumentosPrivados(String xmlComando) {
        try {
            //Se genera una respuesta
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            Secuencia secDocumentos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DOCUMENTOS), "");
            Collection<DocumentoPrivado> listaDocumentos = getDocumentoPrivadoFacade().findAll();
            Iterator<DocumentoPrivado> iteradorDocs = listaDocumentos.iterator();
            while (iteradorDocs.hasNext()) {
                DocumentoPrivado documento = iteradorDocs.next();
                if (documento.getRuta() == null || documento.getRuta().equals("")) {
                    eliminarDocumentoPrivadoPorId(documento.getId());
                } else {
                    //Se extraen los datos del publicador
                    Publicador publicador = documento.getPublicador();
                    String correo = publicador.getPersona().getCorreo();
                    String nombres = publicador.getPersona().getNombres();
                    String apellidos = publicador.getPersona().getApellidos();
                    Boolean esActivo = documento.getActivo();
                    //Se formatea la fecha del documento al formato: yyyy/mm/dd
                    Timestamp tFecha = documento.getFecha();
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(tFecha.getTime());
                    int anioI = cal.get(Calendar.YEAR);
                    int mesI = cal.get(Calendar.MONTH) + 1;
                    int diaI = cal.get(Calendar.DAY_OF_MONTH);
                    String anio = "" + anioI;
                    String mes = "";
                    if (mesI < 10) {
                        mes = "0" + mesI;
                    } else {
                        mes = "" + mesI;
                    }
                    String dia = "";
                    if (diaI < 10) {
                        dia = "0" + diaI;
                    } else {
                        dia = "" + diaI;
                    }
                    String fecha = anio + "/" + mes + "/" + dia;
                    Secuencia secDocumento = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DOCUMENTO), "");
                    Secuencia secIdDocumento = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_IDDOCUMENTO), "" + documento.getId());
                    Secuencia secIdPadre = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_NODO_PADRE), "" + documento.getIdPadre());
                    Secuencia secCorreo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), correo);
                    Secuencia secNombres = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), nombres + " " + apellidos);
                    Secuencia secTitulo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TITULO), documento.getTitulo());
                    Secuencia secFecha = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_PUBLICACION), fecha);
                    Secuencia secEsActivo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ACTIVA), (esActivo == null) ? "false" : esActivo.toString());
                    secDocumento.agregarSecuencia(secIdDocumento);
                    secDocumento.agregarSecuencia(secCorreo);
                    secDocumento.agregarSecuencia(secNombres);
                    secDocumento.agregarSecuencia(secIdPadre);
                    secDocumento.agregarSecuencia(secTitulo);
                    secDocumento.agregarSecuencia(secFecha);
                    secDocumento.agregarSecuencia(secEsActivo);
                    secDocumentos.agregarSecuencia(secDocumento);
                }
            }
            secuencias.add(secDocumentos);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_DOCUMENTOS_PRIVADOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0158, new LinkedList<Secuencia>());

        } catch (Exception ex) {
            try {
                Logger.getLogger(DocumentoPrivadoBean.class.getName()).log(Level.SEVERE, null, ex);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_DOCUMENTOS_PRIVADOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0124, new LinkedList<Secuencia>());
            } catch (Exception ex1) {
                Logger.getLogger(DocumentoPrivadoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    @Override
    public String darInfoDescargaDocumentoPrivado(String xmlComando) {
        try {
            //Se extraen los parámetros del comando
            getParser().leerXML(xmlComando);
            String idDocumento = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_IDDOCUMENTO)).getValor();
            //Buscamos el documento con el identificador dado
            Long id = Long.valueOf(idDocumento).longValue();
            DocumentoPrivado documento = getDocumentoPrivadoFacade().find(id);
            //Si el documento no existe retorna un mensaje de error
            if (documento == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_INFO_DESCARGA_DOCPRIVADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0125, new LinkedList<Secuencia>());
            }
            //De lo contrario retornamos la información de descarga del documento
            //Desactiva todos los documentos anteriores
            desactivarDocumentos();
            documento.setActivo(true);
            getDocumentoPrivadoFacade().edit(documento);
            //Se extraen los datos del documento para la consulta
            String mime = documento.getTipoMime();
            String ruta = documento.getRuta();
            //Se genera una respuesta
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            Secuencia secMime = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MIME), mime);
            Secuencia secRuta = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA_DIRECTORIO), ruta);
            secuencias.add(secMime);
            secuencias.add(secRuta);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_INFO_DESCARGA_DOCPRIVADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0156, new LinkedList<Secuencia>());
            
        } catch (Exception ex) {
            try {
                Logger.getLogger(DocumentoPrivadoBean.class.getName()).log(Level.SEVERE, null, ex);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_INFO_DESCARGA_DOCPRIVADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0124, new LinkedList<Secuencia>());
            } catch (Exception ex1) {
                Logger.getLogger(DocumentoPrivadoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    @Override
    public String eliminarDocumentoPrivado(String xmlComando) {
        try {
            //Se extraen los parámetros del comando
            getParser().leerXML(xmlComando);
            String idDocumento = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_IDDOCUMENTO)).getValor();
            //Buscamos el documento con el identificador dado
            Long id = Long.valueOf(idDocumento).longValue();
            DocumentoPrivado documento = getDocumentoPrivadoFacade().find(id);
            //Se elimina el documento de la lista de docs del nodo padre
            Nodo nodoPadre = getNodoFacade().findByDocumentoId(id);
            Collection<DocumentoPrivado> documentos = nodoPadre.getDocs();
            boolean encontro = false;
            for (Iterator<DocumentoPrivado> it = documentos.iterator(); it.hasNext() && !encontro;) {
                DocumentoPrivado documentoPrivado = it.next();
                if (documentoPrivado.getId() == id) {
                    documentos.remove(documentoPrivado);
                    encontro = true;
                }
            }
            nodoPadre.setDocs(documentos);
            getNodoFacade().edit(nodoPadre);
            //Si el documento no existe retorna un mensaje de error
            if (documento == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_DOCPRIVADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0125, new LinkedList<Secuencia>());
            }
            //De lo contrario eliminamos el documento privado del sistema
            //Eliminamos el archivo físico
            String ruta = documento.getRuta();
            eliminarArchivoFisico(ruta);
            //Eliminamos el documento de la base de datos
            getDocumentoPrivadoFacade().remove(documento);
            //Se genera una respuesta
            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_DOCPRIVADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0157, new LinkedList<Secuencia>());
            
        } catch (Exception ex) {
            try {
                Logger.getLogger(DocumentoPrivadoBean.class.getName()).log(Level.SEVERE, null, ex);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_DOCPRIVADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0124, new LinkedList<Secuencia>());
            } catch (Exception ex1) {
                Logger.getLogger(DocumentoPrivadoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String darArbolDocumentos(String xmlComando) {
        try {
            //Se extraen los parámetros del comando
            getParser().leerXML(xmlComando);
            String idRoot = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_RAIZ_ARBOL)).getValor();
            //Buscamos el nodo con el identificador dado
            Long id = Long.valueOf(idRoot).longValue();
            getNodoFacade().findByParentId("" + id);
            Nodo nodo = getNodoFacade().find(id);
            ArrayList<Secuencia> listaSecuenciaNodos = new ArrayList<Secuencia>();
            leerNodo(nodo, listaSecuenciaNodos);
            Secuencia secuenciaNodos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NODOS), "");
            secuenciaNodos.setSecuencias(listaSecuenciaNodos);
            //Si el nodo no existe retorna un mensaje de error
            if (nodo == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_ARBOL_DOCUMENTOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0150, new LinkedList<Secuencia>());
            }
            //De lo contrario retornamos la información de descarga del documento
            //Se genera una respuesta
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secuenciaNodos);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_ARBOL_DOCUMENTOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0156, new LinkedList<Secuencia>());
            
        } catch (Exception ex) {
            try {
                Logger.getLogger(DocumentoPrivadoBean.class.getName()).log(Level.SEVERE, null, ex);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_ARBOL_DOCUMENTOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0124, new LinkedList<Secuencia>());
            } catch (Exception ex1) {
                Logger.getLogger(DocumentoPrivadoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String editarNodoArbolDocumentos(String xmlComando) {
        try {
            //Se extraen los parámetros del comando
            getParser().leerXML(xmlComando);
            String idNodo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_NODO)).getValor();
            String nombreNodo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_NODO)).getValor();
            //Buscamos el nodo con el identificador dado
            Long idNodoLong = Long.valueOf(idNodo).longValue();
            Nodo nodoAEditar = getNodoFacade().find(idNodoLong);
            if (nodoAEditar == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_NODO_ARBOL_DOCUMENTOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0150, new LinkedList<Secuencia>());
            }
            nodoAEditar.setNombre(nombreNodo);
            getNodoFacade().edit(nodoAEditar);
            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_NODO_ARBOL_DOCUMENTOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0156, new LinkedList<Secuencia>());

        } catch (Exception ex) {
            try {
                Logger.getLogger(DocumentoPrivadoBean.class.getName()).log(Level.SEVERE, null, ex);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_NODO_ARBOL_DOCUMENTOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0124, new LinkedList<Secuencia>());
            } catch (Exception ex1) {
                Logger.getLogger(DocumentoPrivadoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String agregarNodoArbolDocumentos(String xmlComando) {
        try {
            //Se extraen los parámetros del comando
            getParser().leerXML(xmlComando);
            String nombreNodo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_NODO)).getValor();
            String descripcionNodo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION_NODO)).getValor();
            String idNodoPadre = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_NODO_PADRE)).getValor();
            //Buscamos el nodo con el identificador dado
            Long idPadre = Long.valueOf(idNodoPadre).longValue();
            getNodoFacade().findByParentId("" + idPadre);
            Nodo nuevoHijo = new Nodo();
            nuevoHijo.setDescripcion(descripcionNodo);
            nuevoHijo.setDocs(new ArrayList<DocumentoPrivado>());
            nuevoHijo.setIdPadre(idPadre);
            nuevoHijo.setNombre(nombreNodo);
            nuevoHijo.setNodos(new ArrayList<Nodo>());
            getNodoFacade().create(nuevoHijo);
            Nodo nodo = getNodoFacade().find(idPadre);
            nodo.agregarNodo(nuevoHijo);
            getNodoFacade().edit(nodo);
            if (nodo == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_AGREGAR_NODO_ARBOL_DOCUMENTOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0125, new LinkedList<Secuencia>());
            }
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_NODO_ARBOL_DOCUMENTOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0156, new LinkedList<Secuencia>());
            
        } catch (Exception ex) {
            try {
                Logger.getLogger(DocumentoPrivadoBean.class.getName()).log(Level.SEVERE, null, ex);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_AGREGAR_NODO_ARBOL_DOCUMENTOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0124, new LinkedList<Secuencia>());
            } catch (Exception ex1) {
                Logger.getLogger(DocumentoPrivadoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String eliminarNodoArbolDocumentos(String xmlComando) {
        try {
            //Se extraen los parámetros del comando
            getParser().leerXML(xmlComando);
            String idNodo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_NODO)).getValor();
            //Buscamos el nodo con el identificador dado
            Long idNodoLong = Long.valueOf(idNodo).longValue();
            Nodo nodoAEliminar = getNodoFacade().find(idNodoLong);
            if (nodoAEliminar == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_NODO_ARBOL_DOCUMENTOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0125, new LinkedList<Secuencia>());
            }
            Nodo padre = getNodoFacade().find(nodoAEliminar.getIdPadre());
            padre.eliminarNodo(nodoAEliminar);
            getNodoFacade().edit(padre);
            getNodoFacade().remove(nodoAEliminar);
            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_NODO_ARBOL_DOCUMENTOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new LinkedList<Secuencia>());
            
        } catch (Exception ex) {
            try {
                Logger.getLogger(DocumentoPrivadoBean.class.getName()).log(Level.SEVERE, null, ex);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_NODO_ARBOL_DOCUMENTOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0124, new LinkedList<Secuencia>());
            } catch (Exception ex1) {
                Logger.getLogger(DocumentoPrivadoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String eliminarArbolDocumentos(String xmlComando) {
        try {
            Nodo nodoAEliminar = getNodoFacade().find(new Long(-1));
            Collection<Nodo> nodos = nodoAEliminar.getNodos();
            Collection<DocumentoPrivado> docs = nodoAEliminar.getDocs();
            for (Iterator<DocumentoPrivado> it = docs.iterator(); it.hasNext();) {
                DocumentoPrivado documentoPrivado = it.next();
                getDocumentoPrivadoFacade().remove(documentoPrivado);
            }
            for (Iterator<Nodo> it = nodos.iterator(); it.hasNext();) {
                Nodo nodo = it.next();
                getNodoFacade().remove(nodo);
            }
            Collection<Nodo> nodosNuevo = new ArrayList<Nodo>();
            Collection<DocumentoPrivado> documentosNuevo = new ArrayList<DocumentoPrivado>();
            nodoAEliminar.setDocs(documentosNuevo);
            nodoAEliminar.setNodos(nodosNuevo);
            getNodoFacade().edit(nodoAEliminar);
            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_ARBOL_DOCUMENTOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0125, new LinkedList<Secuencia>());

        } catch (Exception ex) {
            try {
                Logger.getLogger(DocumentoPrivadoBean.class.getName()).log(Level.SEVERE, null, ex);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_ARBOL_DOCUMENTOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0124, new LinkedList<Secuencia>());
            } catch (Exception ex1) {
                Logger.getLogger(DocumentoPrivadoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    //---------------------------------------
    // Métodos privados útiles
    //---------------------------------------
    private void desactivarDocumentos(){
        List<DocumentoPrivado> documentosActivos = getDocumentoPrivadoFacade().findActiveDocs();
        for (Iterator<DocumentoPrivado> it = documentosActivos.iterator(); it.hasNext();) {
            DocumentoPrivado documentoPrivado = it.next();
            documentoPrivado.setActivo(false);
            getDocumentoPrivadoFacade().edit(documentoPrivado);
        }
    }

    private void eliminarDocumentoPrivadoPorId(Long id) {
        DocumentoPrivado documento = getDocumentoPrivadoFacade().find(id);
        //Se elimina el documento de la lista de docs del nodo padre
        Nodo nodoPadre = getNodoFacade().findByDocumentoId(id);
        Collection<DocumentoPrivado> documentos = nodoPadre.getDocs();
        boolean encontro = false;
        for (Iterator<DocumentoPrivado> it = documentos.iterator(); it.hasNext() && !encontro;) {
            DocumentoPrivado documentoPrivado = it.next();
            if (documentoPrivado.getId() == id) {
                documentos.remove(documentoPrivado);
                encontro = true;
            }
        }
        nodoPadre.setDocs(documentos);
        getNodoFacade().edit(nodoPadre);
        //Eliminamos el archivo físico
        String ruta = documento.getRuta();
        eliminarArchivoFisico(ruta);
        //Eliminamos el documento de la base de datos
        getDocumentoPrivadoFacade().remove(documento);
    }

    /**
     * Elimina el archivo físico que se encuentra en la ruta suministrada
     * @param ruta La ruta donde se encuentra el archivo
     */
    private void eliminarArchivoFisico(String ruta) {
        if (ruta != null && !ruta.equals("")) {
            File archivo = new File(ruta);
            //Se verifica que en efecto la ruta corresponda a un archivo
            //De lo contrario no se hace nada
            if (archivo.exists() && archivo.isFile()) {
                archivo.delete();
            }
        }
    }

    private void leerNodo(Nodo nodo, ArrayList<Secuencia> listaSecuenciaNodos) {
        String descripcion = nodo.getDescripcion();
        String idNodo = nodo.getId() + "";
        String idPadre = nodo.getIdPadre() + "";
        String nombre = nodo.getNombre();
        Secuencia secuenciaNodo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NODO), "");
        ArrayList<Secuencia> listaSecuencia = new ArrayList<Secuencia>();
        listaSecuencia.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_NODO), nombre));
        listaSecuencia.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION_NODO), descripcion));
        listaSecuencia.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_NODO_PADRE), idPadre));
        listaSecuencia.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_NODO), idNodo));
        secuenciaNodo.setSecuencias(listaSecuencia);
        listaSecuenciaNodos.add(secuenciaNodo);
        Collection<Nodo> nodos = nodo.getNodos();
        for (Iterator<Nodo> it = nodos.iterator(); it.hasNext();) {
            Nodo nodo1 = it.next();
            leerNodo(nodo1, listaSecuenciaNodos);
        }
    }

    //---------------------------------------
    // Métodos privados
    //---------------------------------------
    private ParserT getParser() {
        if (parser == null) {
            parser = new ParserT();
        }
        return parser;
    }

    private ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    private DocumentoPrivadoFacadeLocal getDocumentoPrivadoFacade() {
        return documentoPrivadoFacade;
    }

    private NodoFacadeLocal getNodoFacade() {
        return nodoFacade;
    }

    private PersonaFacadeRemote getPersonaFacade() {
        return personaFacade;
    }

    private PublicadorFacadeLocal getPublicadorFacade() {
        return publicadorFacade;
    }
}
