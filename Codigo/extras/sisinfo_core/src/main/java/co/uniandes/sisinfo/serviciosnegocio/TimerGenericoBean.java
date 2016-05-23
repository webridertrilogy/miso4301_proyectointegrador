package co.uniandes.sisinfo.serviciosnegocio;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.entities.TimerAuditoria;
import co.uniandes.sisinfo.entities.TimerGenerico;
import co.uniandes.sisinfo.serviciosfuncionales.PeriodicidadFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.TimerAuditoriaFacade;
import co.uniandes.sisinfo.serviciosfuncionales.TimerAuditoriaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.TimerGenericoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;

/**
 * Servicios de negocio para administración de timers
 * @author im.melo33, Marcela Morales
 */
@Stateless
public class TimerGenericoBean implements TimerGenericoBeanLocal {

    // ----------------------------------------------------------------------
    // Atributos
    // ----------------------------------------------------------------------
	@PersistenceContext(unitName="SoporteSisinfoPU")
    private EntityManager em;
    private SimpleDateFormat sdf;
    private SimpleDateFormat sdfHMS;
    @Resource
    private SessionContext ctx;
    @EJB
    private ConstanteLocal constanteBean;
    @EJB
    private TimerAuditoriaFacadeLocal timerAuditoriaLocal;
    private ParserT parser;
    private PeriodicidadFacadeLocal periodicidadFacade;
    @EJB
    private TimerGenericoFacadeLocal timerGenericoFacade;

    // ----------------------------------------------------------------------
    // Constructor
    // ----------------------------------------------------------------------
    public TimerGenericoBean() {
//        try {
//            sdf = new SimpleDateFormat("yyyy-MM-dd");
//            sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            ServiceLocator serviceLocator = new ServiceLocator();
//            constanteBean = (ConstanteLocal) serviceLocator.getLocalEJB(ConstanteLocal.class);
//            timerAuditoriaLocal = (TimerAuditoriaFacadeLocal) serviceLocator.getLocalEJB(TimerAuditoriaFacadeLocal.class);
//            periodicidadFacade = (PeriodicidadFacadeLocal) serviceLocator.getLocalEJB(PeriodicidadFacadeLocal.class);
//        } catch (NamingException ex) {
//            Logger.getLogger(TimerGenericoBean.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    // ----------------------------------------------------------------------
    // Método
    // ----------------------------------------------------------------------
    /**
     * Método que crea un timer genérico, el cual al vencerse llamará al método de la interfaz (remota) que se especifique
     * el método debe recibir un sólo parámetro el cual es un string donde esta la informacion que necesite
     *
     * @param direccionInterfazRemotaBean: dirección de la interfaz remota EJ: co.edu.uniandes.sisinfo.serviciosFuncionales.XXXLocal
     * @param nombreMetodoALLamar: nombre del método que recibe como único parametro un string y sabe que hacer cuando se venza el timer
     * @param fechaFin: fecha en la cual se debe activar el timer
     * @param infoTimer: la información que se debe devolver del timer debería ser un string
     * @return id del timer creado
     *
     */
    public Long crearTimer(String direccionInterfazRemotaBean, String nombreMetodoALLamar, Timestamp fechaFin, String infoTimer) {

        if (infoTimer.contains("=")) {
            infoTimer.replace("=", "IIGGUUAALL");
        }

        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());

        TimerGenerico timerEntity = new TimerGenerico(direccionInterfazRemotaBean, nombreMetodoALLamar, fechaFin, infoTimer, "sinInfo", "sinInfo", "sinInfo", ts, "sinInfo");
        em.persist(timerEntity);

        TimerService timerService = ctx.getTimerService();
        String infoTimerGenericoBean = null;
        if (infoTimer != null && !infoTimer.isEmpty()) {
            infoTimerGenericoBean = timerEntity.getId() + "=" + direccionInterfazRemotaBean + "=" + nombreMetodoALLamar + "=" + infoTimer;
        } else {
            infoTimerGenericoBean = timerEntity.getId() + "=" + direccionInterfazRemotaBean + "=" + nombreMetodoALLamar;
        }
        timerService.createTimer(fechaFin, infoTimerGenericoBean);

        return timerEntity.getId();
    }

    public Long crearTimer2(String direccionInterfazRemotaBean, String nombreMetodoALLamar, Timestamp fechaFin, String infoTimer,
            String moduloQuienLoCrea, String beanQuienLoCrea, String metodoQuienLoCrea, String descripcionTimer) {

        if (infoTimer.contains("=")) {
            infoTimer.replace("=", "IIGGUUAALL");
        }

        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        TimerGenerico timerEntity = new TimerGenerico(direccionInterfazRemotaBean, nombreMetodoALLamar, fechaFin, infoTimer,
                moduloQuienLoCrea, beanQuienLoCrea, metodoQuienLoCrea, ts, descripcionTimer);
        em.persist(timerEntity);

        TimerService timerService = ctx.getTimerService();
        String infoTimerGenericoBean = null;
        if (infoTimer != null && !infoTimer.isEmpty()) {
            infoTimerGenericoBean = timerEntity.getId() + "=" + direccionInterfazRemotaBean + "=" + nombreMetodoALLamar + "=" + infoTimer;
        } else {
            infoTimerGenericoBean = timerEntity.getId() + "=" + direccionInterfazRemotaBean + "=" + nombreMetodoALLamar;
        }
        timerService.createTimer(fechaFin, infoTimerGenericoBean);

        return timerEntity.getId();
    }

    /**
     * Método que crea un timer en la base de datos a partir de un timer en memoria
     * @param t Timer en memoria
     */
    public void crearTimerEnBD(Timer t) {

        String inf = (String) t.getInfo();

        String[] partes = inf.split("=");
        String dirInterfaz = partes[1].trim();
        String nMetodo = partes[2].trim();
        String info = null;
        if (partes.length == 4) {
            info = partes[3];
            info.replace("IIGGUUAALL", "=");
        }

        Date date = t.getNextTimeout();
        Timestamp ts = new Timestamp(date.getTime());

        TimerGenerico timerEntity = new TimerGenerico(dirInterfaz, nMetodo, ts, info, "prueba", "prueba", "prueba", ts, "prueba");
        em.persist(timerEntity);
    }

    /**
     * Método que reinicia un timer sin agregarlo a la BD
     * Si el timer ya venció, lo reinicia para que se ejecute en pocos segundos
     * Si no ha vencido, lo reinicia para la fecha prevista
     */
    public String recrearTimer(Long id, String direccionInterfazRemotaBean, String nombreMetodoALLamar, Timestamp fechaFin, String infoTimer) {
        //Intenta borrar el timer de memoria
        Collection<Timer> timers = ctx.getTimerService().getTimers();
        if (timers != null) {
            for (Iterator iterator = timers.iterator(); iterator.hasNext();) {
                Timer t = (Timer) iterator.next();
                String Linea = (String) t.getInfo();
                Long idT = Long.parseLong(Linea.split("=")[0]);
                if (idT.equals(id)) {
                    t.cancel();
                }
            }
        }

        if (infoTimer != null && infoTimer.contains("=")) {
            infoTimer.replace("=", "IIGGUUAALL");
        }
        String infoTimerGenericoBean = null;
        if (infoTimer != null && !infoTimer.isEmpty()) {
            infoTimerGenericoBean = id + "=" + direccionInterfazRemotaBean + "=" + nombreMetodoALLamar + "=" + infoTimer;
        } else {
            infoTimerGenericoBean = id + "=" + direccionInterfazRemotaBean + "=" + nombreMetodoALLamar;
        }

        Date fechaInicioDate = new Date();
        Timestamp fechaActualEnSistema = new Timestamp(fechaInicioDate.getTime());

        //Si el timer ya venció durante la caída del sistema, lo crea para pocos segundos
        if (fechaFin.before(fechaActualEnSistema)) {
            Date d = new Date();
            long milisecs = d.getTime();

            milisecs = milisecs + 20000;

            Date date = new Date(milisecs);
            Timestamp timeStamp = new Timestamp(date.getTime());
            TimerService timerService = ctx.getTimerService();
            timerService.createTimer(timeStamp, infoTimerGenericoBean);

            borrarTimerDeBaseDeDatos(id);
        } else {
            //crea el timer para la fecha prevista
            TimerService timerService = ctx.getTimerService();
            timerService.createTimer(fechaFin, infoTimerGenericoBean);
        }
        return infoTimerGenericoBean;
    }

    /**
     * Método que se ejecuta cuando un timer se ha cumplido, se encarga de llamar a la clase y método correspondiente
     * y de eliminarlo de la base de datos, con el fin de que no vuelva a ser creado
     */
    @Timeout
    public void tiempoCumplido(Timer t) {

        String infoBean = (String) t.getInfo();
        String[] partes = infoBean.split("=");
        Long idTimer = Long.parseLong(partes[0].trim());
        String dirInterfaz = partes[1].trim();
        String nMetodo = partes[2].trim();

        //Auditoria de Timers
        TimerAuditoria tAudit = new TimerAuditoria();
        tAudit.setNombreMetodoALLamar(nMetodo);
        tAudit.setDireccionInterfaz(dirInterfaz);
        tAudit.setFechaEjecucionTimer(new Timestamp(System.currentTimeMillis()));
        tAudit.setInfoTimer(infoBean);
        try {
            timerAuditoriaLocal.create(tAudit);
        } catch (Exception e) {
            Logger.getLogger(TimerAuditoriaFacade.class.getName()).log(Level.SEVERE, null, e);
        }

        String info = null;
        if (partes.length == 4) {
            if (!partes[3].equals("")) {
                info = partes[3];
                info.replace("IIGGUUAALL", "=");
            }

        }

        Context ctx = null;
        Object objref = null;
        Method metodoALLmar = null;
        Class[] parameterTypes = new Class[1];
        parameterTypes[0] = String.class;

        try {
            ctx = new InitialContext();
            objref = ctx.lookup(dirInterfaz);
            Class c = objref.getClass();
            //System.out.println("el objeto"+objref + "\n"+ c.getCanonicalName());
            //1.llamar al metodo que pusieron como parametro

            if (info != null) {
                metodoALLmar = c.getMethod(nMetodo, parameterTypes);
                Object[] test = {(Object) info};
                metodoALLmar.invoke(objref, test);
            } else {
                metodoALLmar = c.getMethod(nMetodo);
                metodoALLmar.invoke(objref);
            }
            //borrar timer de la base de datos
            borrarTimerDeBaseDeDatos(idTimer);

        } catch (IllegalAccessException ex) {
            Logger.getLogger(TimerGenericoBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(TimerGenericoBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(TimerGenericoBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(TimerGenericoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método que borra todos los timers de memoria, mas no de la base de datos
     */
    public void borrarTodosLosTimers() {
        Collection<Timer> timers = ctx.getTimerService().getTimers();

        if (timers != null) {
            for (Iterator iterator = timers.iterator(); iterator.hasNext();) {
                Timer t = (Timer) iterator.next();
                t.cancel();
            }
        }
    }

    /**
     * Método que reinicia todos los timers de la BD
     */
    public void recargarTimersDeBD() {
        List<TimerGenerico> list = em.createQuery("select object(o) from TimerGenerico as o").getResultList();
        for (Iterator<TimerGenerico> it1 = list.iterator(); it1.hasNext();) {
            TimerGenerico timerGenerico = it1.next();
            recrearTimer(timerGenerico.getId(), timerGenerico.getDireccionInterfaz(), timerGenerico.getNombreMetodoALLamar(), timerGenerico.getFechaFin(), timerGenerico.getInfoTimer());
        }
    }

    /**
     * Método que elimina un timer de memoria y de base de datos dado el id
     * @param idTimer Id del timer
     */
    public void eliminarTimer(Long idTimer) {
        Collection<Timer> timers = ctx.getTimerService().getTimers();

        if (timers != null) {
            for (Iterator iterator = timers.iterator(); iterator.hasNext();) {
                Timer t = (Timer) iterator.next();
                String Linea = (String) t.getInfo();
                Long idT = Long.parseLong(Linea.split("=")[0]);
                if (idT.equals(idTimer)) {
                    t.cancel();
                }
            }
        }
        borrarTimerDeBaseDeDatos(idTimer);
    }

    /**
     * Método que elimina un timer de memoria y de base de datos dado un parámetro externo
     * @param parametro Parámetro externo
     */
    public void eliminarTimerPorParametroExterno(String parametro) {
        Collection<Timer> timers = ctx.getTimerService().getTimers();

        if (timers != null) {
            for (Iterator iterator = timers.iterator(); iterator.hasNext();) {
                Timer t = (Timer) iterator.next();
                String Linea = (String) t.getInfo();
                String partes[] = Linea.split("=");
                if (partes.length == 4) {
                    String parametroExterno = Linea.split("=")[3];
                    if (parametroExterno.equals(parametro)) {
                        t.cancel();
                    }
                }
            }
        }
        borrarTimersPorParametroExterno(parametro);
    }

    /**
     * Método que borra un timer de la base de datos dado el id
     * @param idTimer Id del timer
     */
    private void borrarTimerDeBaseDeDatos(Long idTimer) {
        try {
            TimerGenerico timer = (TimerGenerico) em.createNamedQuery("TimerGenerico.findById").setParameter("id", idTimer).getSingleResult();
            em.remove(em.merge(timer));
        } catch (NoResultException nre) {
        }
    }

    /**
     * Método que elimina un conjunto de timers de memoria y de base de datos dado un parámetro externo
     * @param parametro Parámetro externo
     */
    private void borrarTimersPorParametroExterno(String parametroExterno) {
        List lista = em.createNamedQuery("TimerGenerico.findByParametroExterno").setParameter("infoTimer", parametroExterno).getResultList();
        Iterator ite = lista.iterator();
        while (ite.hasNext()) {
            TimerGenerico timer = (TimerGenerico) ite.next();
            em.remove(timer);
        }
    }

    /**
     * Método que se encarga de recrear un conjunto de timers dado un método a llamar y una fecha de fin
     * Los timers corresponden a los que tienen como método a llamar el que viene como parámetro
     * Los timers se recrean según la fecha de fin
     */
    public void recargarTimerDeBDPorMetodoALlamar(String metodoALlamar, Timestamp fechaFin) throws Exception {
        TimerGenerico timerGenerico = null;

        List lista = em.createNamedQuery("TimerGenerico.findByNombreMetodoALlamar").setParameter("nombreMetodo", metodoALlamar).getResultList();
        if (lista.isEmpty()) {
            throw new Exception();
        } else {
            for (Iterator it = lista.iterator(); it.hasNext();) {
                timerGenerico = (TimerGenerico) it.next();
                recrearTimer(timerGenerico.getId(), timerGenerico.getDireccionInterfaz(), timerGenerico.getNombreMetodoALLamar(), fechaFin, timerGenerico.getInfoTimer());
                timerGenerico.setFechaFin(fechaFin);
                em.merge(timerGenerico);
            }
        }
    }

    /**
     * Pasa los timers en memoria no referenciados a la base de datos
     */
    public String arreglarTimers(String parameter) {
        try {
            //Timers en base de datos y en memoria
            List<TimerGenerico> timersBD = em.createQuery("select object(o) from TimerGenerico as o").getResultList();
            Collection<Timer> timersMemoria = ctx.getTimerService().getTimers();

            //TODO: timersBDErr NO SE USA
            ArrayList<TimerGenerico> timersBDErr = new ArrayList<TimerGenerico>();
            ArrayList<Timer> timersMemoriaErr = new ArrayList<Timer>();

            boolean timersBDOK[] = new boolean[timersBD.size()];
            boolean timersMemoriaOK[] = new boolean[timersMemoria.size()];

            //Compara los timers en memoria con los timers en base de datos
            //La matriz se llena de 'true' en caso de que el timer esté en ambos lugares
            int i = 0;
            for (Timer timerMemoria : timersMemoria) {
                for (int j = 0; j < timersBD.size(); j++) {
                    TimerGenerico timerBD = timersBD.get(j);
                    if (!timersBDOK[j] && !timersMemoriaOK[i]) {
                        if (igual(timerBD, timerMemoria)) {
                            timersBDOK[j] = true;
                            timersMemoriaOK[i] = true;
                        }
                    }
                }
                i++;
            }

            //Reconoce los timers que no están en memoria y están en la base de datos
            i = 0;
            for (Timer timerMemoria : timersMemoria) {
                if (!timersMemoriaOK[i]) {
                    timersMemoriaErr.add(timerMemoria);
                }
                i++;
            }
            //TODO: Reconoce los timers que no están en la base de datos y están en memoria (Esto no se usa)
            for (int j = 0; j < timersBD.size(); j++) {
                TimerGenerico timerBD = timersBD.get(j);
                if (!timersBDOK[j]) {
                    timersBDErr.add(timerBD);
                }
            }

            //Crea los timers que faltaban en la base de datos
            int cuenta = 0;
            for (Timer timerMemoria : timersMemoriaErr) {
                crearTimerEnBD(timerMemoria);
                cuenta++;
            }

            Collection<Secuencia> secuenciasRespuesta = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuenciasRespuesta, getConstanteBean().getConstante(Constantes.CMD_ARREGLAR_TIMERS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());

        } catch (Exception e) {
            try {
                Logger.getLogger(TimerGenericoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_RECARGAR_TIMERS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ADM_ERR_0002, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(TimerGenericoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    /**
     * Método que indica que un timer en base de datos y un timer en memoria son el mismo
     */
    private boolean igual(TimerGenerico t1, Timer t2) {
        String inf = t2.getInfo().toString();
        String[] partes = inf.split("=");
        String dirInterfaz = partes[1].trim();
        String nMetodo = partes[2].trim();
        String info = null;
        if (partes.length == 4) {
            if (!partes[3].equals("")) {
                info = partes[3];
                info.replace("IIGGUUAALL", "=");
            }
        }
        if (t1.getDireccionInterfaz().equals(dirInterfaz) && t1.getNombreMetodoALLamar().equals(nMetodo)) {
            String f1 = sdfHMS.format(t1.getFechaFin()), f2 = sdfHMS.format(t2.getNextTimeout());
            if (f1.equals(f2)) {
                if (t1.getInfoTimer() == null) {
                    return info == null;
                } else {
                    return t1.getInfoTimer().equals(info);
                }
            }
        }
        return false;
    }

    /**
     * Metodo que devuelve true si ya esiste un timer con los mismos parametros
     */
    public boolean existeTimerCompletamenteIgual(String dirInterfaz, String nombreMetdo, Timestamp fecha, String paramExterno) {
        List<TimerGenerico> lista = em.createNamedQuery("TimerGenerico.findByTodosLosParametros").setParameter("nombreMetodo", nombreMetdo).setParameter("direccionInterfaz", dirInterfaz).setParameter("infoTimer", paramExterno).setParameter("FechaFin", fecha).getResultList();
        return lista.isEmpty() ? false : true;
    }

    /**
     * Método que retorna el id del timer dado el nombre de la interfaz, método y parámetros
     */
    public Long darIdTimer(String direccionInterfaz, String nombreMetodo, String parametroExterno) {
        List<TimerGenerico> lista = em.createNamedQuery("TimerGenerico.findByDireccionNombreYParametro").setParameter("nombreMetodoALLamar", nombreMetodo).setParameter("direccionInterfaz", direccionInterfaz).setParameter("infoTimer", parametroExterno).getResultList();
        if (lista.size() > 0) {
            return lista.get(0).getId();
        } else {
            return null;
        }
    }

    // ----------------------------------------------------------------------
    // Comandos
    // ----------------------------------------------------------------------
    /**
     * 1. Elimina todos los timers en memoria
     * 2. Recarga en memoria todos los timers de la base de datos
     */
    public String recargarTimers(String comando) {
        try {
            borrarTodosLosTimers();
            crearTimerDiagnosticoSisinfo();
            recargarTimersDeBD();

            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_RECARGAR_TIMERS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new LinkedList<Secuencia>());

        } catch (Exception e) {
            try {
                Logger.getLogger(TimerGenericoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_RECARGAR_TIMERS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ADM_ERR_0001, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(TimerGenericoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    /**
     * Consulta todos los timers: en base de datos y en memoria     *
     */
    public String consultarTimers(String comando) {
        try {
            Secuencia secTimers = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_TIMERS), "");

            //Timers en memoria
            Collection<Timer> timersMemoria = ctx.getTimerService().getTimers();
            Secuencia secTimersMemoria = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_TIMERS_MEMORIA), "");

            for (Timer timerMemoria : timersMemoria) {

                String informacion = timerMemoria.getInfo().toString();
                String[] atributosInfo = informacion.split("=");

                Secuencia secTimer = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_TIMER), "");
                if (atributosInfo[0] != null) {
                    secTimer.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), atributosInfo[0]));
                }
                if (atributosInfo[1] != null) {
                    secTimer.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_CLASE_TIMER), atributosInfo[1]));
                }
                if (atributosInfo[2] != null) {
                    secTimer.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_METODO_TIMER), atributosInfo[2]));
                }
                if (timerMemoria.getNextTimeout() != null) {
                    secTimer.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_FECHA_TIMER), sdfHMS.format(timerMemoria.getNextTimeout())));
                }
                if (atributosInfo.length == 4) {
                    if (atributosInfo[3] != null) {
                        secTimer.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_TIMER), atributosInfo[3]));
                    }
                }
                secTimersMemoria.agregarSecuencia(secTimer);
            }
            secTimers.agregarSecuencia(secTimersMemoria);

            //Timers de la base de datos
            List<TimerGenerico> timersBD = em.createQuery("select object(o) from TimerGenerico as o").getResultList();
            Secuencia secTimersBD = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_TIMERS_BD), "");

            for (TimerGenerico timerBD : timersBD) {
                Secuencia secTimer = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_TIMER), "");
                if (timerBD.getId() != null) {
                    secTimer.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), timerBD.getId().toString()));
                }
                if (timerBD.getDireccionInterfaz() != null) {
                    secTimer.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_CLASE_TIMER), timerBD.getDireccionInterfaz()));
                }
                if (timerBD.getNombreMetodoALLamar() != null) {
                    secTimer.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_METODO_TIMER), timerBD.getNombreMetodoALLamar()));
                }
                if (timerBD.getFechaFin() != null) {
                    secTimer.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_FECHA_TIMER), sdfHMS.format(timerBD.getFechaFin())));
                }
                if (timerBD.getInfoTimer() != null) {
                    secTimer.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_TIMER), timerBD.getInfoTimer()));
                }
                secTimersBD.agregarSecuencia(secTimer);
            }
            secTimers.agregarSecuencia(secTimersBD);

            Collection<Secuencia> secuenciasRespuesta = new ArrayList<Secuencia>();
            secuenciasRespuesta.add(secTimers);
            return getParser().generarRespuesta(secuenciasRespuesta, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_TIMERS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());

        } catch (Exception e) {
            try {
                Logger.getLogger(TimerGenericoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_RECARGAR_TIMERS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ADM_ERR_0002, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(TimerGenericoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    /**
     * Método que se encarga de ejecutar un timer que se encuentra en memoria 
     */
    public String ejecutarTimerEnMemoria(String comando) {
        try {
            //Extrae los parámetros (id)
            getParser().leerXML(comando);
            Secuencia secId = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            //Valida los parámetros de entrada
            if (secId == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_EJECUTAR_TIMER_EN_MEMORIA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new LinkedList<Secuencia>());
            }

            //Busca el timer en memoria
            Timer timer = null;
            Collection<Timer> timersMemoria = ctx.getTimerService().getTimers();
            for (Iterator iterator = timersMemoria.iterator(); iterator.hasNext();) {
                Timer timerMemoria = (Timer) iterator.next();
                String infoTimer = (String) timerMemoria.getInfo();
                Long idT = Long.parseLong(infoTimer.split("=")[0]);
                if (idT.equals(secId.getValorLong())) {
                    timer = timerMemoria;
                }
            }

            //Valida que haya encontrado el timer
            if (timer == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_EJECUTAR_TIMER_EN_MEMORIA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ADM_ERR_0003, new LinkedList<Secuencia>());
            }
            //Ejecuta el timer
            tiempoCumplido(timer);
            Collection<Timer> timers = ctx.getTimerService().getTimers();

            if (timers != null) {
                for (Iterator iterator = timers.iterator(); iterator.hasNext();) {
                    Timer t = (Timer) iterator.next();
                    String Linea = (String) t.getInfo();
                    Long idT = Long.parseLong(Linea.split("=")[0]);
                    if (idT.equals(secId.getValorLong())) {
                        t.cancel();
                    }
                }
            }
            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_EJECUTAR_TIMER_EN_MEMORIA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new LinkedList<Secuencia>());

        } catch (Exception e) {
            try {
                Logger.getLogger(TimerGenericoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_EJECUTAR_TIMER_EN_MEMORIA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(TimerGenericoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    
    public boolean timerExisteEnMemoria(Long id) {
        Collection<Timer> timersMemoria = ctx.getTimerService().getTimers();
        for (Timer timerMemoria : timersMemoria) {

            String informacion = timerMemoria.getInfo().toString();
            String[] atributosInfo = informacion.split("=");

            if (atributosInfo[0] != null) {
                if (Long.parseLong(atributosInfo[0]) == id) {
                    return true;
                }
            }
        }
        return false;
    }

    
    public boolean timerExisteEnBD(Long id) {
        try {
            TimerGenerico timerBD = (TimerGenerico) em.createNamedQuery("TimerGenerico.findById").setParameter("id", id).getSingleResult();
            return timerBD != null;
        } catch (NoResultException nre) {
            return false;
        }

    }

    /**
     * Método que se encarga de eliminar un timer de memoria y de la base de datos
     */
    public String eliminarTimer(String comando) {
        try {
            //Extrae los parámetros (id)
            getParser().leerXML(comando);
            Secuencia secId = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            //Valida los parámetros de entrada
            if (secId == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_TIMER), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new LinkedList<Secuencia>());
            }

            //Busca el timer en memoria
            Timer timer = null;
            Collection<Timer> timersMemoria = ctx.getTimerService().getTimers();
            for (Iterator iterator = timersMemoria.iterator(); iterator.hasNext();) {
                Timer timerMemoria = (Timer) iterator.next();
                String infoTimer = (String) timerMemoria.getInfo();
                Long idT = Long.parseLong(infoTimer.split("=")[0]);
                if (idT.equals(secId.getValorLong())) {
                    timer = timerMemoria;
                }
            }

            //Valida que haya encontrado el timer
            if (timer == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_TIMER), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ADM_ERR_0004, new LinkedList<Secuencia>());
            }
            //Elimina el timer
            String infoTimer = (String) timer.getInfo();
            Long idT = Long.parseLong(infoTimer.split("=")[0]);
            eliminarTimer(idT);

            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_TIMER), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new LinkedList<Secuencia>());

        } catch (Exception e) {
            try {
                Logger.getLogger(TimerGenericoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_TIMER), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(TimerGenericoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    /**
     * Método que se encarga de detener un timer de memoria
     */
    public String detenerTimerEnMemoria(String comando) {
        try {
            //Extrae los parámetros (id)
            getParser().leerXML(comando);
            Secuencia secId = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            //Valida los parámetros de entrada
            if (secId == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DETENER_TIMER), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new LinkedList<Secuencia>());
            }

            //Busca el timer en memoria
            Timer timer = null;
            Collection<Timer> timersMemoria = ctx.getTimerService().getTimers();
            for (Iterator iterator = timersMemoria.iterator(); iterator.hasNext();) {
                Timer timerMemoria = (Timer) iterator.next();
                String infoTimer = (String) timerMemoria.getInfo();
                Long idT = Long.parseLong(infoTimer.split("=")[0]);
                if (idT.equals(secId.getValorLong())) {
                    timer = timerMemoria;
                }
            }

            //Valida que haya encontrado el timer
            if (timer == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DETENER_TIMER), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ADM_ERR_0005, new LinkedList<Secuencia>());
            }
            //Detiene/Cancela el timer
            timer.cancel();

            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DETENER_TIMER), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new LinkedList<Secuencia>());

        } catch (Exception e) {
            try {
                Logger.getLogger(TimerGenericoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DETENER_TIMER), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(TimerGenericoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    /**
     * Método que se encarga de eliminar el timer de memoria y editar el timer en la base de datos
     * Luego, se encarga de recrearlo en memoria
     */
    public String editarTimer(String comando) {
        try {
            //Extrae los parámetros (id)
            getParser().leerXML(comando);
            Secuencia secId = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Secuencia secClase = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_CLASE_TIMER));
            Secuencia secMetodo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_METODO_TIMER));
            Secuencia secFecha = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_FECHA_TIMER));
            Secuencia secParametro = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_TIMER));

            //Valida los parámetros de entrada
            if (secId == null || secClase == null || secMetodo == null || secFecha == null || secParametro == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_TIMER), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new LinkedList<Secuencia>());
            }
            //Busca el timer en memoria
            Timer timer = null;
            Collection<Timer> timersMemoria = ctx.getTimerService().getTimers();
            for (Iterator iterator = timersMemoria.iterator(); iterator.hasNext();) {
                Timer timerMemoria = (Timer) iterator.next();
                String infoTimer = (String) timerMemoria.getInfo();
                Long idT = Long.parseLong(infoTimer.split("=")[0]);
                if (idT.equals(secId.getValorLong())) {
                    timer = timerMemoria;
                }
            }
            //Valida que haya encontrado el timer en memoria
            if (timer == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_TIMER), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ADM_ERR_0006, new LinkedList<Secuencia>());
            }

            //Busca el timer en base de datos
            TimerGenerico timerBD = null;
            try {
                timerBD = (TimerGenerico) em.createNamedQuery("TimerGenerico.findById").setParameter("id", secId.getValorLong()).getSingleResult();
            } catch (NonUniqueResultException e) {
                timerBD = (TimerGenerico) em.createNamedQuery("TimerGenerico.findById").setParameter("id", secId.getValorLong()).getResultList().get(0);
            } catch (NoResultException e) {
                timerBD = null;
            }
            //Valida que haya encontrado el timer en base de datos
            if (timerBD == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_TIMER), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ADM_ERR_0007, new LinkedList<Secuencia>());
            }

            //Detiene el timer en memoria, edita el timer en base de datos y recrea el timer en memoria
            timer.cancel();

            timerBD.setDireccionInterfaz(secClase.getValor());
            Date fecha = sdfHMS.parse(secFecha.getValor());
            timerBD.setFechaFin(new Timestamp(fecha.getTime()));
            timerBD.setInfoTimer(secParametro.getValor());
            timerBD.setNombreMetodoALLamar(secMetodo.getValor());
            em.merge(timerBD);

            String infoTimer = secParametro.getValor();
            if (infoTimer.contains("=")) {
                infoTimer.replace("=", "IIGGUUAALL");
            }

            TimerService timerService = ctx.getTimerService();
            String infoTimerGenericoBean = null;
            if (infoTimer != null && !infoTimer.isEmpty()) {
                infoTimerGenericoBean = secId.getValor() + "=" + secClase.getValor() + "=" + secMetodo.getValor() + "=" + infoTimer;
            } else {
                infoTimerGenericoBean = secId.getValor() + "=" + secClase.getValor() + "=" + secMetodo.getValor();
            }
            timerService.createTimer(fecha, infoTimerGenericoBean);

            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_TIMER), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new LinkedList<Secuencia>());

        } catch (Exception e) {
            try {
                Logger.getLogger(TimerGenericoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_TIMER), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(TimerGenericoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    /**
     * Método que se encarga de consultar el timer cuyo id llega como parámetro
     */
    public String consultarTimer(String comando) {
        try {
            //Extrae los parámetros (id)
            getParser().leerXML(comando);
            Secuencia secId = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            //Valida los parámetros de entrada
            if (secId == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_TIMER), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new LinkedList<Secuencia>());
            }

            TimerGenerico timerBD = null;
            try {
                timerBD = (TimerGenerico) em.createNamedQuery("TimerGenerico.findById").setParameter("id", secId.getValorLong()).getSingleResult();
            } catch (NonUniqueResultException e) {
                timerBD = (TimerGenerico) em.createNamedQuery("TimerGenerico.findById").setParameter("id", secId.getValorLong()).getResultList().get(0);
            } catch (NoResultException e) {
                timerBD = null;
            }
            //Valida que haya encontrado el timer en base de datos
            if (timerBD == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_TIMER), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ADM_ERR_0008, new LinkedList<Secuencia>());
            }

            //Retorna la información del timer
            Secuencia secTimer = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_TIMER), "");
            if (timerBD.getId() != null) {
                secTimer.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), timerBD.getId().toString()));
            }
            if (timerBD.getDireccionInterfaz() != null) {
                secTimer.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_CLASE_TIMER), timerBD.getDireccionInterfaz()));
            }
            if (timerBD.getNombreMetodoALLamar() != null) {
                secTimer.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_METODO_TIMER), timerBD.getNombreMetodoALLamar()));
            }
            if (timerBD.getFechaFin() != null) {
                secTimer.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_FECHA_TIMER), sdfHMS.format(timerBD.getFechaFin())));
            }
            if (timerBD.getInfoTimer() != null) {
                secTimer.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_TIMER), timerBD.getInfoTimer()));
            }

            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secTimer);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_TIMER), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new LinkedList<Secuencia>());

        } catch (Exception e) {
            try {
                Logger.getLogger(TimerGenericoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_TIMER), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(TimerGenericoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    // ----------------------------------------------------------------------
    // Auxiliares
    // ----------------------------------------------------------------------
    /**
     * Retorna ConstanteBean
     * @return constanteBean ConstanteBean
     */
    private ConstanteLocal getConstanteBean() {
        return constanteBean;
    }

    /**
     * Retorna Parser
     * @return parser Parser
     */
    private ParserT getParser() {
        if (parser == null) {
            parser = new ParserT();
        }
        return parser;
    }

    public void eliminarTimersPorDireccionInterfaz(String interfaz) {
        List<TimerGenerico> timersBD = (List<TimerGenerico>) em.createNamedQuery("TimerGenerico.findByDireccionInterfaz").setParameter("direccionInterfaz", interfaz).getResultList();
        for (TimerGenerico timerGenerico : timersBD) {
            borrarTimerDeBaseDeDatos(timerGenerico.getId());
        }

    }

    public boolean existeTimerCompletamenteIgual(String dirInterfaz, String nombreMetdo, String paramExterno) {
        List<TimerGenerico> lista = em.createNamedQuery("TimerGenerico.findByDireccionNombreYParametro").setParameter("nombreMetodoALLamar", nombreMetdo).setParameter("direccionInterfaz", dirInterfaz).setParameter("infoTimer", paramExterno).getResultList();
        return lista.isEmpty() ? false : true;
    }

    public void eliminarTimers(String direccionInterfaz, String nombreMetodo, String infoAdicional) {
        List<TimerGenerico> lista = em.createNamedQuery("TimerGenerico.findByDireccionNombreYParametro").setParameter("nombreMetodoALLamar", nombreMetodo).setParameter("direccionInterfaz", direccionInterfaz).setParameter("infoTimer", infoAdicional).getResultList();
        for (TimerGenerico timerGenerico : lista) {
            eliminarTimer(timerGenerico.getId());
        }
    }

    /**
     * crea un timer que envia un correo periodicamente al administrador de Sisinfo con el estado del sistema
     */
    public void crearTimerDiagnosticoSisinfo() {
        //elimina el timer en caso  de que ya exista alguno
        List lista = em.createNamedQuery("TimerGenerico.findByParametroExterno").setParameter("infoTimer", getConstanteBean().getConstante(Constantes.CMD_ADMINISTRADOR_SISINFO)).getResultList();
        Iterator ite = lista.iterator();
        while (ite.hasNext()) {
            TimerGenerico timer = (TimerGenerico) ite.next();
            eliminarTimer(timer.getId());
        }

        String RUTA_INTERFAZ_REMOTA = "co.uniandes.sisinfo.serviciosnegocio.AdministradorSisinfoBeanLocal";
        String NOMBRE_METODO_TIMER = "enviarCorreoDiagnosticoSisinfo";

        Long tiempo = periodicidadFacade.findByNombre(getConstanteBean().getConstante(Constantes.VAL_PERIODICIDAD_ENVIO_CORREO_DIAGNOSTICO)).getValor();
        crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(new Date().getTime() + tiempo),
                getConstanteBean().getConstante(Constantes.CMD_ADMINISTRADOR_SISINFO), "ServicioSoporteProcesos", this.getClass().getName(), "crearTimerAdministradorSisinfo", "Este timer se crea para enviar un correo al administradorSisinfo con el estado del sistema");
    }

    /**
     * crea el timer que envia los correos relacionados con las tareas pendientes vencidas
     */
    public void crearTimerTareasPendientesVencidas() {
        //elimina el timer en caso  de que ya exista alguno
        List lista = em.createNamedQuery("TimerGenerico.findByParametroExterno").setParameter("infoTimer", getConstanteBean().getConstante(Constantes.CMD_ADMINISTRADOR_SISINFO)).getResultList();
        Iterator ite = lista.iterator();
        while (ite.hasNext()) {
            TimerGenerico timer = (TimerGenerico) ite.next();
            eliminarTimer(timer.getId());
        }

        String RUTA_INTERFAZ_REMOTA = "co.uniandes.sisinfo.serviciosnegocio.AlertaMultipleLocal";
        String NOMBRE_METODO_TIMER = "enviarRecordatorioTareasVencidas";

        Long tiempo = periodicidadFacade.findByNombre(getConstanteBean().getConstante(Constantes.VAL_PERIODICIDAD_ENVIO_CORREO_DIAGNOSTICO)).getValor();
        crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(new Date().getTime() + tiempo),
                getConstanteBean().getConstante(Constantes.CMD_ADMINISTRADOR_SISINFO), "ServicioSoporteProcesos", this.getClass().getName(), "crearTimerAdministradorSisinfo", "Este timer se crea para enviar un correo al administradorSisinfo con el estado del sistema");
    }

    /**
     * Retorna todos los timers que estan en memoria
     * @return
     */
    public int consultarTimersEnMemoriaFaltantesEnBD() {


        Collection<Timer> timersMemoria = ctx.getTimerService().getTimers();
        int timersFaltantesEnBD = 0;
        System.out.println("consultarTimersEnMemoriaFaltantesEnBD");
        System.out.println(timersMemoria.size());
        for (Timer timerMemoria : timersMemoria) {

            String informacion = timerMemoria.getInfo().toString();
            String[] atributosInfo = informacion.split("=");

            if (atributosInfo[0] != null) {
                if (!timerExisteEnBD(Long.parseLong(atributosInfo[0]))) {
                    System.out.println(atributosInfo[3]);
                    timersFaltantesEnBD += 1;
                }
            }
        }
        return timersFaltantesEnBD;
    }

    /**
     * Retorna una coleccion con todos los timers en la base de datos
     * @return
     */
    public int consultarTimersEnBDFaltantesEnMemoria() {
        List<TimerGenerico> timersBD = timerGenericoFacade.findAll();
        int timersFaltantesEnMemoria = 0;
        System.out.println("consultarTimersEnBDFaltantesEnMemoria");
        for (TimerGenerico timerBD : timersBD) {
            if (!timerExisteEnMemoria(timerBD.getId())) {
                System.out.println(timerBD.getInfoTimer());
                timersFaltantesEnMemoria += 1;
            }
        }
        return timersFaltantesEnMemoria;
    }



    
}
