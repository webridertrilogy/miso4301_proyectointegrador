/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;

/**
 *
 * @author da-naran
 */
@Stateless
@EJB(name = "PensumBean", beanInterface = co.uniandes.sisinfo.serviciosnegocio.PensumLocal.class)
public class PensumBean implements  PensumLocal {
    
    private ParserT parser;


    @EJB
    private ConstanteLocal constanteBean;

    private ServiceLocator serviceLocator;

    public PensumBean(){
//        try {
//            serviceLocator = new ServiceLocator();            
//            constanteBean = (ConstanteLocal) serviceLocator.getLocalEJB(ConstanteLocal.class);
//        } catch (NamingException ex) {
//            Logger.getLogger(PensumBean.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public String darPensum(String comando) {
        /*try {
            getParser().leerXML(comando);
            //idPensum: numérico; idPensum=0 equivale al pensum actual.

            String idPensum=getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_PENSUM)).getValor();
            Pensum pensum;
            if(idPensum.equals("0")){
                pensum=pensumFacade.findPensumActual();
            }else{
                pensum=pensumFacade.find(new Long(idPensum));
            }
            if(pensum==null){
            }
            Collection<Secuencia> secuencias=new Vector<Secuencia>();
            Secuencia semestres=new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRES),"");
            int numSemestres=pensum.getNumeroSemestres();
            Collection<Semestre> sems=pensum.getSemestres();
            for(int i=1;i<numSemestres+1;i++){
                Secuencia semestre=new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE),"");
                semestre.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_SEMESTRE),Integer.toString(i)));
                Secuencia materias=new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MATERIAS),"");
                Semestre sem=null;
                for(Semestre s:sems){
                    if(s.getNumeroSemestre()==i){
                        sem=s;
                    }
                }
                Collection<Materia> materiasSemestre=sem.getMateriasSemestre();
                for(Materia m:materiasSemestre){
                    Secuencia materia=new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MATERIA),"");
                    materia.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_MATERIA),m.getNombre()));
                    materia.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_MATERIA),m.getIdMateria()));
                    materia.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_CREDITOS),Double.toString(m.getCreditosTotales())));
                    Area a=areaFacade.findByIDMateria(m.getId());
                    if(a!=null){
                        materia.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_AREA),a.getNombre()));
                    }else{
                        materia.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_AREA),""));
                    }
                    materia.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PENSUM_ACTUAL),Boolean.toString(pensum.isActual())));
                    Secuencia prerrequisitos=new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PRERREQUISITOS),"");

                    Secuencia correquisitos=new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREQUISITOS),"");

                    materia.agregarSecuencia(prerrequisitos);
                    materia.agregarSecuencia(correquisitos);
                    materias.agregarSecuencia(materia);
                }
                semestre.agregarSecuencia(materias);
                semestres.agregarSecuencia(semestre);
            }
            secuencias.add(semestres);

            Secuencia areas=new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_AREAS),"");

            secuencias.add(areas);
            String respuesta=getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_PENSUM), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE),"", new Vector());
            return respuesta;
        } catch (Exception ex) {
            Logger.getLogger(PensumBean.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        return null;
    }
    
    private ParserT getParser(){
        if(parser==null){
            parser=new ParserT();
        }
        return parser;    
    }

    public ConstanteLocal getConstanteBean() {
        return constanteBean;
    }


}
