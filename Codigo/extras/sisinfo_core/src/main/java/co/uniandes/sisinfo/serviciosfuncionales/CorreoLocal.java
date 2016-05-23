/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, Carlos Morales, Javier Bautista,
 *          Alejandro Osorio, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.serviciosfuncionales;

import java.util.Collection;
import javax.ejb.Local;

/**
 * Interface Local
 * Servicios Correo
 */
@Local
public interface CorreoLocal {

    /**
     * Envía un correo de acuerdo a la información suministrada (asunto, receptor, transmisor, cc, cco, archivo, mensaje)
     * @param para Receptor del correo
     * @param de Transmisor del correo
     * @param asunto Asunto del correo
     * @param cc Receptor con copia
     * @param cco Receptor con copia oculta
     * @param archivo Archivo adjunto del correo
     * @param mensaje Mensaje del correo
     */
    public void enviarMail(String para, String asunto, String cc, String cco, String archivo, String mensaje);
     /**
     * envia un correo a listas de correo
     * @param para: Collection<String> con las direcciones a las que se va a enviar
     * @param asunto: asunto del correo
     * @param cc: Collection<String> con las direcciones a las que se va a enviar copia
     * @param cco: Collection<String> con las direcciones a las que se va a enviar copia oculta
     * @param nombresArchivos: nombre de los archivos que se van a adjuntar(con su ruta)
     * @param mensaje: mensaje a enviar
     * @param correoReplyTO: String direccion de reply to, si no se desea colocar entonces null
     */public void enviarMailLista(Collection<String> para, String asunto, Collection<String> cc, Collection<String> cco, Collection<String> nombresArchivos, String mensaje, String correoReplyTO) ;
}
