package co.edu.uniandes.sisinfo.ant;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper; 

public class AntRunner {

    private Project project;
        
    public AntRunner(File build, File basedir, BuildListener listener) {
        try{
            project = new Project();
            project.init();
            ProjectHelper.configureProject(project, build);
            project.addBuildListener(listener);
            ProjectHelper.getProjectHelper().parse(project, build);
            project.setBaseDir(basedir);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public String[] getTargets() {
        Collection targets = new ArrayList();
        for (Object key : project.getTargets().keySet()) {
            if(key.toString().length() > 0)
                targets.add(key.toString());
        }
        return (String[]) targets.toArray(new String[0]);
    }

    public void executeTarget(String target) {
        project.executeTarget(target);
    }
}
