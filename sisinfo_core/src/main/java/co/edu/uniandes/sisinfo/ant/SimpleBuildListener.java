package co.edu.uniandes.sisinfo.ant;

import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildListener;

public class SimpleBuildListener implements BuildListener {

    public void buildFinished(BuildEvent buildEvent) {
    }

    public void buildStarted(BuildEvent buildEvent) {
    }

    public void messageLogged(BuildEvent buildEvent) {
        String mensaje = " [";
        mensaje += (buildEvent.getTask() == null) ? "" : buildEvent.getTask().getTaskName()+"] ";
        mensaje += buildEvent.getMessage();
        System.out.println(mensaje);
    }

    public void targetFinished(BuildEvent buildEvent) {
        System.out.println("target finished");
    }

    public void targetStarted(BuildEvent buildEvent) {
        System.out.println(buildEvent.getTarget().getName()+":");
    }

    public void taskFinished(BuildEvent buildEvent) {
    }

    public void taskStarted(BuildEvent buildEvent) {
    }
}
