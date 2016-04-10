/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import java.io.File;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;

/**
 *
 * @author cf.morales46
 */
@Stateless
public class TimerPruebaBean implements TimerPruebaRemote {

    @Resource
    private SessionContext ctx;

    public void timer() {
       TimerService timerService = ctx.getTimerService();
       timerService.createTimer(300000, null);
        System.out.println("i have "+timerService.getTimers().size()+ " timers");
        Collection<Timer> c = timerService.getTimers();
        for (Timer timer : c) {
            System.out.println("Next timeout: "+timer.getNextTimeout());
        }
    }
    
    @Timeout
    private void timerPrueba(Timer t){
        try{

            File f = new File("C:/Documents and Settings/cf.morales46/Mis documentos/Sisinfo/data/prueba.txt");

            PrintWriter pw = new PrintWriter(f);
            pw.println("im workin");
            pw.flush();
            pw.close();
        }catch(Exception e){
            Logger.getLogger(TimerPruebaBean.class.getName()).log(Level.SEVERE, "Error realizando la confirmacion", e);
        }
    }
}
