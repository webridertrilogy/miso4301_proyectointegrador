package co.uniandes.sisinfo.historico.serviciosnegocio.tesisP;

import co.uniandes.sisinfo.historico.serviciosnegocio.tesisM.HistoricosTesisBean;
import co.uniandes.sisinfo.serviciosnegocio.HistoricoTesisPregradoRemote;
import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.historico.entities.tesispregrado.h_estudiante_pregrado;
import co.uniandes.sisinfo.historico.entities.tesispregrado.h_tesisPregrado;
import co.uniandes.sisinfo.historico.serviciosfuncionales.tesispregrado.h_EstudiantePregrado_FacadeLocal;
import co.uniandes.sisinfo.historico.serviciosfuncionales.tesispregrado.h_tesisPregradoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import co.uniandes.sisinfo.serviciosnegocio.ConstanteRemote;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

/**
 * Servicios de negocio para Histórico de Proyectos de Grado
 * @author Ivan Mauricio Melo S, Marcela Morales
 */
@Stateless
public class HistoricoTesisPregrado implements HistoricoTesisPregradoRemote, HistoricoTesisPregradoLocal {

    @EJB
    private h_tesisPregradoFacadeLocal tesisPregradoFacade;
    @EJB
    private h_EstudiantePregrado_FacadeLocal estudiantePregradoFacade;
    @EJB
    private ConstanteRemote constanteBean;
    private ServiceLocator serviceLocator;
    private ConversorTesisPregradoH conversor;
    private ParserT parser;

    public HistoricoTesisPregrado() {
        try {
            parser = new ParserT();
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            conversor = new ConversorTesisPregradoH();
        } catch (NamingException ex) {
            Logger.getLogger(HistoricosTesisBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String pasarTesisPregradoRechazadaAHistorico(String xml) {
        try {
            getParser().leerXML(xml);
            Secuencia secTesis = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROYECTO_DE_GRADO));
            h_tesisPregrado tesis = conversor.pasarSecuenciaATesisPregradoHistorica(secTesis);

            String correo = conversor.pasarSecuenciaAEstudianteCorreo(secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE)));
            h_estudiante_pregrado estudianteP = estudiantePregradoFacade.findByCorreoEstudiante(correo);

            if(estudianteP == null){
                Secuencia secEstudiante = secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE));
                h_estudiante_pregrado estudiantePregrado = conversor.pasarSecuenciaAEstudianteSimple(secEstudiante);
                estudiantePregrado.setId(null);
                tesis.setId(null);
                Collection <h_tesisPregrado> tesises = new ArrayList();
                tesises.add(tesis);
                estudiantePregrado.setTesisPregrado(tesises);
                estudiantePregrado.setInfoRechazada(true);
                getEstudiantePregradoFacade().create(estudiantePregrado);
            }else{
                Collection <h_tesisPregrado> tesises = estudianteP.getTesisPregrado();

                boolean estaMigrada = false;

                if(tesises == null){
                    estaMigrada = false;
                }else{
                    if(tesises.isEmpty()){
                        estaMigrada = false;
                    }else{
                        for(h_tesisPregrado tesisPre : tesises){
                            if(tesisPre.getFechaCreacion().equals(tesis.getFechaCreacion())&&
                                    tesisPre.getEstado().equals(tesis.getEstado())){
                                estaMigrada = true;
                                break;
                            }
                        }
                    }
                }

                if(estaMigrada == false){
                    tesis.setId(null);
                    if(tesises == null){
                        tesises = new ArrayList();
                        tesises.add(tesis);
                    }else{
                        tesises.add(tesis);
                    }
                    estudianteP.setTesisPregrado(tesises);
                    estudianteP.setInfoRechazada(true);
                    getEstudiantePregradoFacade().edit(estudianteP);
                }
            }

            ArrayList<Secuencia> secs = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secs, getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS_PREGRADO_RECHAZADAS_A_HISTORICOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

        } catch (Exception ex) {
            Logger.getLogger(HistoricoTesisPregrado.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String pasarTesisPregradoRetiradaAHistorico(String xml) {
        try {
            System.out.println("\n\nLllego a pasarTesisPregradoRetiradaAHistorico con: " + xml + "\n\n");
            getParser().leerXML(xml);
            Secuencia secTesisesP = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROYECTOS_DE_GRADO));
            ArrayList<Secuencia> secTesises = secTesisesP.getSecuencias();
            for (Iterator<Secuencia> it = secTesises.iterator(); it.hasNext();) {
                Secuencia secT = it.next();
                Secuencia secTesis = secT.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROYECTO_DE_GRADO));
                h_tesisPregrado tesis = conversor.pasarSecuenciaATesisPregradoHistorica(secTesis);

                String correo = conversor.pasarSecuenciaAEstudianteCorreo(secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE)));
                h_estudiante_pregrado estudianteP = estudiantePregradoFacade.findByCorreoEstudiante(correo);

                if(estudianteP == null){
                    Secuencia secEstudiante = secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE));
                    h_estudiante_pregrado estudiantePregrado = conversor.pasarSecuenciaAEstudianteSimple(secEstudiante);
                    estudiantePregrado.setId(null);
                    tesis.setId(null);
                    Collection <h_tesisPregrado> tesises = new ArrayList();
                    tesises.add(tesis);
                    estudiantePregrado.setTesisPregrado(tesises);
                    estudiantePregrado.setInfoRetirada(true);
                    getEstudiantePregradoFacade().create(estudiantePregrado);
                }else{
                    Collection <h_tesisPregrado> tesises = estudianteP.getTesisPregrado();

                    boolean estaMigrada = false;

                    if(tesises == null){
                        estaMigrada = false;
                    }else{
                        if(tesises.isEmpty()){
                            estaMigrada = false;
                        }else{
                            for(h_tesisPregrado tesisPre : tesises){
                                if(tesisPre.getFechaCreacion().equals(tesis.getFechaCreacion())&&
                                        tesisPre.getEstado().equals(tesis.getEstado())){
                                    estaMigrada = true;
                                    break;
                                }
                            }
                        }
                    }

                    if(estaMigrada == false){
                        tesis.setId(null);
                        if(tesises == null){
                            tesises = new ArrayList();
                            tesises.add(tesis);
                        }else{
                            tesises.add(tesis);
                        }
                        estudianteP.setTesisPregrado(tesises);
                        estudianteP.setInfoRetirada(true);
                        getEstudiantePregradoFacade().edit(estudianteP);
                    }
                }

            }
            ArrayList<Secuencia> secs = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secs, getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS_PREGRADO_RETIRADAS_A_HISTORICOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(HistoricoTesisPregrado.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String pasarTesisPregradoPerdidaAHistorico(String xml) {
        try {
            System.out.println("Lo que tiene xml en pasarTesisPregradoPerdidaAHistorico: \n" + xml + "\n\n");
            getParser().leerXML(xml);
            Secuencia secTesisesP = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROYECTOS_DE_GRADO));
            ArrayList<Secuencia> secTesises = secTesisesP.getSecuencias();
            for (Iterator<Secuencia> it = secTesises.iterator(); it.hasNext();) {
                Secuencia secT = it.next();
                Secuencia secTesis = secT.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROYECTO_DE_GRADO));
                h_tesisPregrado tesis = conversor.pasarSecuenciaATesisPregradoHistorica(secTesis);

                String correo = conversor.pasarSecuenciaAEstudianteCorreo(secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE)));
                h_estudiante_pregrado estudianteP = estudiantePregradoFacade.findByCorreoEstudiante(correo);

                if(estudianteP == null){
                    Secuencia secEstudiante = secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE));
                    h_estudiante_pregrado estudiantePregrado = conversor.pasarSecuenciaAEstudianteSimple(secEstudiante);
                    estudiantePregrado.setId(null);
                    tesis.setId(null);
                    tesis.setEstado(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_PERDIDA));
                    Collection <h_tesisPregrado> tesises = new ArrayList();
                    tesises.add(tesis);
                    estudiantePregrado.setTesisPregrado(tesises);
                    estudiantePregrado.setInfoPerdida(true);
                    getEstudiantePregradoFacade().create(estudiantePregrado);
                }else{
                    Collection <h_tesisPregrado> tesises = estudianteP.getTesisPregrado();

                    boolean estaMigrada = false;

                    if(tesises == null){
                        estaMigrada = false;
                    }else{
                        if(tesises.isEmpty()){
                            estaMigrada = false;
                        }else{
                            for(h_tesisPregrado tesisPre : tesises){
                                if(tesisPre.getFechaCreacion().equals(tesis.getFechaCreacion())&&
                                        tesisPre.getEstado().equals(tesis.getEstado())){
                                    estaMigrada = true;
                                    break;
                                }
                            }
                        }
                    }

                    if(estaMigrada == false){
                        tesis.setId(null);
                        tesis.setEstado(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_PERDIDA));
                        if(tesises == null){
                            tesises = new ArrayList();
                            tesises.add(tesis);
                        }else{
                            tesises.add(tesis);
                        }
                        estudianteP.setTesisPregrado(tesises);
                        estudianteP.setInfoPerdida(true);
                        getEstudiantePregradoFacade().edit(estudianteP);
                    }
                }

            }
            ArrayList<Secuencia> secs = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secs, getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS_PREGRADO_PERDIDAS_A_HISTORICOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(HistoricoTesisPregrado.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String pasarTesisPregradoTerminadaAHistorico(String xml) {
        try {
            System.out.println("Lo que va a migrar pregrado antes del parser:" + xml);
            getParser().leerXML(xml);
            System.out.println("Lo que va a migrar pregrado:" + xml);
            Secuencia secTesisesP = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROYECTOS_DE_GRADO));
            ArrayList<Secuencia> secTesises = secTesisesP.getSecuencias();
            for (Iterator<Secuencia> it = secTesises.iterator(); it.hasNext();) {
                Secuencia secT = it.next();
                Secuencia secTesis = secT.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROYECTO_DE_GRADO));
                h_tesisPregrado tesis = conversor.pasarSecuenciaATesisPregradoHistorica(secTesis);

                String correo = conversor.pasarSecuenciaAEstudianteCorreo(secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE)));
                h_estudiante_pregrado estudianteP = estudiantePregradoFacade.findByCorreoEstudiante(correo);


                if(estudianteP == null){
                    Secuencia secEstudiante = secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE));
                    h_estudiante_pregrado estudiantePregrado = conversor.pasarSecuenciaAEstudianteSimple(secEstudiante);
                    estudiantePregrado.setId(null);
                    tesis.setId(null);
                    Collection <h_tesisPregrado> tesises = new ArrayList();
                    tesises.add(tesis);
                    estudiantePregrado.setTesisPregrado(tesises);
                    estudiantePregrado.setInfoTerminada(true);
                    asignarAsesorAEstudiante(estudiantePregrado,tesis);
                    getEstudiantePregradoFacade().create(estudiantePregrado);
                }else{
                    Collection <h_tesisPregrado> tesises = estudianteP.getTesisPregrado();

                    boolean estaMigrada = false;

                    if(tesises == null){
                        estaMigrada = false;
                    }else{
                        if(tesises.isEmpty()){
                            estaMigrada = false;
                        }else{
                            for(h_tesisPregrado tesisPre : tesises){
                                if(tesisPre.getCorreoAsesor().equals(tesis.getCorreoAsesor()) &&
                                        tesisPre.getSemestre().equals(tesis.getSemestre()) &&
                                        tesisPre.getEstado().equals(tesis.getEstado())){
                                    estaMigrada = true;
                                    break;
                                }
                            }
                        }
                    }

                    if(estaMigrada == false){
                        tesis.setId(null);
                        if(tesises == null){
                            tesises = new ArrayList();
                            tesises.add(tesis);
                        }else{
                            tesises.add(tesis);
                        }

                        estudianteP.setTesisPregrado(tesises);
                        estudianteP.setInfoTerminada(true);
                        asignarAsesorAEstudiante(estudianteP,tesis);
                        getEstudiantePregradoFacade().edit(estudianteP);
                    }
                }
            }
            ArrayList<Secuencia> secs = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secs, getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS_PREGRADO_TERMINADAS_A_HISTORICOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(HistoricoTesisPregrado.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String darHistoricoEstudiantesTesisPregrado(String xml) {
        try {
            parser.leerXML(xml);
            Collection<h_estudiante_pregrado> estudiantes = estudiantePregradoFacade.findAll();

            Secuencia secSoli = conversor.pasarEstudiantesPregradoHistoricoASecuenciaLigera(estudiantes);

            ArrayList<Secuencia> param = new ArrayList<Secuencia>();
            param.add(secSoli);
            return getParser().generarRespuesta(param, getConstanteBean().getConstante(Constantes.CMD_DAR_HISTORICO_ESTUDIANTES_TESIS_DE_PREGRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());


        } catch (Exception ex) {
            try {
                Logger.getLogger(HistoricoTesisPregrado.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_HISTORICO_ESTUDIANTES_TESIS_DE_PREGRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), null, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(HistoricoTesisPregrado.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String darHistoricoEstudiantesTesisPregradoProfesor(String xml) {
        try {
            parser.leerXML(xml);

            String correo = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            Collection<h_estudiante_pregrado> estudiantes = estudiantePregradoFacade.findByCorreoProfesor(correo);

            Secuencia secSoli = conversor.pasarEstudiantesPregradoHistoricoASecuenciaLigera(estudiantes);

            ArrayList<Secuencia> param = new ArrayList<Secuencia>();
            param.add(secSoli);
            return getParser().generarRespuesta(param, getConstanteBean().getConstante(Constantes.CMD_DAR_HISTORICO_ESTUDIANTES_TESIS_DE_PREGRADO_POR_PROFESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());


        } catch (Exception ex) {
            try {
                Logger.getLogger(HistoricoTesisPregrado.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_HISTORICO_ESTUDIANTES_TESIS_DE_PREGRADO_POR_PROFESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), null, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(HistoricoTesisPregrado.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String darHistoricoEstudianteTesisPregrado(String xml) {

        try {
            parser.leerXML(xml);

            Secuencia secId = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Long id = Long.parseLong(secId.getValor().trim());

            h_estudiante_pregrado estudiante = estudiantePregradoFacade.find(id);

            Secuencia secSoli = conversor.pasarEstudianteHistoricoASecuenciaCompleta(estudiante);


            ArrayList<Secuencia> param = new ArrayList<Secuencia>();
            param.add(secSoli);
            return getParser().generarRespuesta(param, getConstanteBean().getConstante(Constantes.CMD_DAR_HISTORICO_ESTUDIANTE_TESIS_DE_PREGRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());


        } catch (Exception ex) {
            try {
                Logger.getLogger(HistoricoTesisPregrado.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_HISTORICO_ESTUDIANTE_TESIS_DE_PREGRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), null, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(HistoricoTesisPregrado.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }






    // *****  METODOS QUE YA ESTAN EN EL CONVERSOR (BORRARLOS DE ESTE BEAN ... TO DO)
/*
    private h_tesisPregrado pasarSecuenciaATesisHistorica(Secuencia sec){
        h_tesisPregrado tesis = new h_tesisPregrado();
        Secuencia secId = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
        if (secId != null) {
            Long id = Long.parseLong(secId.getValor().trim());
            tesis.setId(id);
        }
        Secuencia secFechaCreacion = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_CREACION));
        if (secFechaCreacion != null) {
            String fechaCreacion = secFechaCreacion.getValor();
            try {
                SimpleDateFormat formatoDeFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date fecha = formatoDeFecha.parse(fechaCreacion);
                tesis.setFechaCreacion(new Timestamp(fecha.getTime()));
            } catch (ParseException ex) {
                Logger.getLogger(HistoricoTesisPregrado.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        tesis.setFechaFin(new Timestamp(new Date().getTime()));
        Secuencia secTemaTesis = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TEMA_TESIS));
        if (secTemaTesis != null) {
            tesis.setTemaTesis(secTemaTesis.getValor());
        }
        Secuencia secRutaPoster = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA_POSTER));
        if (secRutaPoster != null) {
            tesis.setRutaPosterTesis(secRutaPoster.getValor());
        }
        Secuencia secNota = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA));
        if (secNota != null) {
            Double nota = Double.parseDouble(secNota.getValor());
            tesis.setCalificacionTesis(nota);
        }
        Secuencia secRutaABET = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA_ARCHIVO_ABET));
        if (secRutaABET != null) {
            tesis.setRutaABET(secRutaABET.getValor());
        }
        Secuencia secEstudiante = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE));
        if (secEstudiante != null) {
            pasarSecuenciaAEstudiante(secEstudiante, tesis);
        }
        Secuencia secAsesor = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASESOR_TESIS));
        if (secAsesor != null) {
            pasarSecuenciaAProfesor(secAsesor, tesis);
        }
        Secuencia secSemestre = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE));
        if (secSemestre != null) {
            String sem = pasarSecuenciaAPeriodo(secSemestre);
            tesis.setSemestre(sem);
        }
        Secuencia secEstadoTesis = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO_TESIS));
        if (secEstadoTesis != null) {
            tesis.setEstado(secEstadoTesis.getValor());
        }
        return tesis;
    }

    private void pasarSecuenciaAEstudiante(Secuencia secEstudiante, h_tesisPregrado tesis) {
        Secuencia secNombre = secEstudiante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES));
        if (secNombre != null) {
            tesis.setNombresEstudiante(secNombre.getValor());
        }
        Secuencia secApp = secEstudiante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS));
        if (secApp != null) {
            tesis.setApellidosEstudiante(secApp.getValor());
        }
        Secuencia secCorreo = secEstudiante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
        if (secCorreo != null) {
            tesis.setCorreoEstudiante(secCorreo.getValor());
        }
    }

    private void pasarSecuenciaAProfesor(Secuencia secuencia, h_tesisPregrado p) {
        Secuencia secProfesor = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR));
        Secuencia secNombre = secProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES));
        if (secNombre != null) {
            p.setNombresAsesor(secNombre.getValor());
        }
        Secuencia secApp = secProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS));
        if (secApp != null) {
            p.setApellidosAsesor(secApp.getValor());
        }
        Secuencia secCorreo = secProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
        if (secCorreo != null) {
            p.setCorreoAsesor(secCorreo.getValor());
        }
    }

    private String pasarSecuenciaAPeriodo(Secuencia secSemestre) {
        Secuencia secPeriodo = secSemestre.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE));
        if (secPeriodo != null) {
            return secPeriodo.getValor();
        }
        return null;
    }

    private Secuencia pasarHistoricoProyectosDeGradoASecuencia(List<h_tesisPregrado> proyectos) {
        Secuencia secProyectos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROYECTOS_DE_GRADO), "");
        for (h_tesisPregrado proyectoDeGrado : proyectos) {
            Secuencia secProyecto = pasarHistoricoProyectoDeGradoASecuencia(proyectoDeGrado);
            secProyectos.agregarSecuencia(secProyecto);
        }
        return secProyectos;
    }

    private Secuencia pasarHistoricoProyectoDeGradoASecuencia(h_tesisPregrado proyectoDeGrado) {
        SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Secuencia secPrincipal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROYECTO_DE_GRADO), "");
        if (proyectoDeGrado.getId() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), proyectoDeGrado.getId().toString());
            secPrincipal.agregarSecuencia(secId);
        }
        if (proyectoDeGrado.getFechaCreacion() != null) {
            Secuencia secFechaCreacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_CREACION), sdfHMS.format(new Date(proyectoDeGrado.getFechaCreacion().getTime())));
            secPrincipal.agregarSecuencia(secFechaCreacion);
        }
        if (proyectoDeGrado.getFechaFin() != null) {
            Secuencia secFechaFin = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN), sdfHMS.format(new Date(proyectoDeGrado.getFechaFin().getTime())));
            secPrincipal.agregarSecuencia(secFechaFin);
        }
        if (proyectoDeGrado.getTemaTesis() != null) {
            Secuencia secTemaTesis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TEMA_TESIS), proyectoDeGrado.getTemaTesis());
            secPrincipal.agregarSecuencia(secTemaTesis);
        }
        if (proyectoDeGrado.getRutaPosterTesis() != null) {
            Secuencia secRutaPoster = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_ARCHIVO_POSTER), proyectoDeGrado.getRutaPosterTesis());
            secPrincipal.agregarSecuencia(secRutaPoster);
        }
        if (proyectoDeGrado.getCalificacionTesis() != null) {
            Secuencia secNota = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA), proyectoDeGrado.getCalificacionTesis().toString());
            secPrincipal.agregarSecuencia(secNota);
        }
        if (proyectoDeGrado.getRutaABET() != null) {
            Secuencia secRutaABET = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA_ARCHIVO_ABET), proyectoDeGrado.getRutaABET());
            secPrincipal.agregarSecuencia(secRutaABET);
        }
        Secuencia secEstudiante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE), "");
        if (proyectoDeGrado.getNombresEstudiante() != null) {
            Secuencia secNombresEstudiante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), proyectoDeGrado.getNombresEstudiante());
            secEstudiante.agregarSecuencia(secNombresEstudiante);
        }
        if (proyectoDeGrado.getApellidosEstudiante() != null) {
            Secuencia secApellidosEstudiante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), proyectoDeGrado.getApellidosEstudiante());
            secEstudiante.agregarSecuencia(secApellidosEstudiante);
        }
        if (proyectoDeGrado.getCorreoEstudiante() != null) {
            Secuencia secCorreoEstudiante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), proyectoDeGrado.getCorreoEstudiante());
            secEstudiante.agregarSecuencia(secCorreoEstudiante);
        }
        secPrincipal.agregarSecuencia(secEstudiante);
        Secuencia secAsesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASESOR_TESIS), "");
        Secuencia secProfesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR), "");
        if (proyectoDeGrado.getNombresEstudiante() != null) {
            Secuencia secNombresProfesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), proyectoDeGrado.getNombresAsesor());
            secProfesor.agregarSecuencia(secNombresProfesor);
        }
        if (proyectoDeGrado.getApellidosEstudiante() != null) {
            Secuencia secApellidosProfesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), proyectoDeGrado.getApellidosAsesor());
            secProfesor.agregarSecuencia(secApellidosProfesor);
        }
        if (proyectoDeGrado.getCorreoEstudiante() != null) {
            Secuencia secCorreoProfesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), proyectoDeGrado.getCorreoAsesor());
            secProfesor.agregarSecuencia(secCorreoProfesor);
        }
        secAsesor.agregarSecuencia(secProfesor);
        secPrincipal.agregarSecuencia(secAsesor);
        if (proyectoDeGrado.getSemestre() != null) {
            Secuencia secSemestre = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE), "");
            Secuencia secNombreSemestre = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), proyectoDeGrado.getSemestre());
            secSemestre.agregarSecuencia(secNombreSemestre);
            secPrincipal.agregarSecuencia(secSemestre);
        }
        if (proyectoDeGrado.getEstado() != null) {
            Secuencia secEstado = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO_TESIS), proyectoDeGrado.getEstado());
            secPrincipal.agregarSecuencia(secEstado);
        }
        return secPrincipal;
    }

*/

    
    //********** METODOS AUXILIARES *************

    private void asignarAsesorAEstudiante(h_estudiante_pregrado estudianteMaestria, h_tesisPregrado tesis) {

        if(tesis.getNombresAsesor() != null){
            estudianteMaestria.setNombresAsesor(tesis.getNombresAsesor());
        }

        if(tesis.getApellidosAsesor() != null){
            estudianteMaestria.setApellidosAsesor(tesis.getApellidosAsesor());
        }

        if(tesis.getCorreoAsesor() != null){
            estudianteMaestria.setCorreoAsesor(tesis.getCorreoAsesor());
        }
    }


    //********** METODOS AUXILIARES DE RETORNO DE ATRIBUTOS *************
    
    private ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    private ParserT getParser() {
        return (parser == null) ? new ParserT() : parser;
    }

    private h_tesisPregradoFacadeLocal getTesisPregradoFacade() {
        return tesisPregradoFacade;
    }

    private h_EstudiantePregrado_FacadeLocal getEstudiantePregradoFacade() {
        return estudiantePregradoFacade;
    }
}
