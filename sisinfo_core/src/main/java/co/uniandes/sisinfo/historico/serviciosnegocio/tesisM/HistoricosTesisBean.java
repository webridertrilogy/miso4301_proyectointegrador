/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.historico.serviciosnegocio.tesisM;

import co.uniandes.sisinfo.serviciosnegocio.HistoricosTesisBeanRemote;
import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.historico.entities.tesisM.h_curso_tomado;
import co.uniandes.sisinfo.historico.entities.tesisM.h_estudiante_maestria;
import co.uniandes.sisinfo.historico.entities.tesisM.h_inscripcion_subarea;
import co.uniandes.sisinfo.historico.serviciosfuncionales.tesisM.h_EstudianteMaestria_FacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import co.uniandes.sisinfo.serviciosnegocio.ConstanteRemote;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;


/**
 *
 * @author Asistente
 */
@Stateless
public class HistoricosTesisBean implements HistoricosTesisBeanRemote, HistoricosTesisBeanLocal {

    //EJB
    @EJB
    private ConstanteRemote constanteBean;

    //---OTROS--------------------
    private ParserT parser;
    private ServiceLocator serviceLocator;
    private ConversorTesisMaestriaH conversor;


    @EJB
    private h_EstudianteMaestria_FacadeLocal h_EstudianteFacade;


    public HistoricosTesisBean() {
        try {
            parser = new ParserT();
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB( ConstanteRemote.class);
            conversor = new ConversorTesisMaestriaH();
        } catch (NamingException ex) {
            Logger.getLogger(HistoricosTesisBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    


    public String darHistoricoEstudiantesTesis(String xml) {
        try {
            parser.leerXML(xml);
            Collection<h_estudiante_maestria> estudiantes = h_EstudianteFacade.findAll();

            Secuencia secSoli = conversor.pasarEstudiantesHistoricoASecuenciaLigera(estudiantes);

            System.out.println(secSoli.toString());
            
            ArrayList<Secuencia> param = new ArrayList<Secuencia>();
            param.add(secSoli);
            return getParser().generarRespuesta(param, getConstanteBean().getConstante(Constantes.CMD_DAR_HISTORICO_ESTUDIANTES_TESIS_MAESTRIA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());


        } catch (Exception ex) {
            try {
                Logger.getLogger(HistoricosTesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_HISTORICO_ESTUDIANTES_TESIS_MAESTRIA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), null, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(HistoricosTesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }


    public String darHistoricoEstudianteTesis(String xml) {

        try {
            parser.leerXML(xml);
            Secuencia secId = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Long id = Long.parseLong(secId.getValor().trim());
            h_estudiante_maestria estudiante = h_EstudianteFacade.find(id);
            Secuencia secSoli = conversor.pasarEstudianteHistoricoASecuenciaCompleta(estudiante);
            ArrayList<Secuencia> param = new ArrayList<Secuencia>();
            param.add(secSoli);
            return getParser().generarRespuesta(param, getConstanteBean().getConstante(Constantes.CMD_DAR_HISTORICO_ESTUDIANTE_TESIS_MAESTRIA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {
            try {
                Logger.getLogger(HistoricosTesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_HISTORICO_ESTUDIANTE_TESIS_MAESTRIA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), null, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(HistoricosTesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String darHistoricosEstudiantesTesisMaestriaProfesor(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secProfesor = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
            Collection<h_estudiante_maestria> estudiantes = h_EstudianteFacade.findByProfesor(secProfesor.getValor());            
            Secuencia secSoli = conversor.pasarEstudiantesHistoricoASecuenciaCompleta(estudiantes);
            ArrayList<Secuencia> param = new ArrayList<Secuencia>();
            param.add(secSoli);
            return getParser().generarRespuesta(param, getConstanteBean().getConstante(Constantes.CMD_DAR_HISTORICO_ESTUDIANTES_TESIS_MAESTRIA_PROFESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {
            try {
                Logger.getLogger(HistoricosTesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_HISTORICO_ESTUDIANTES_TESIS_MAESTRIA_PROFESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), null, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(HistoricosTesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    private ParserT getParser() {
        return (parser == null) ? new ParserT() : parser;
    }
}
