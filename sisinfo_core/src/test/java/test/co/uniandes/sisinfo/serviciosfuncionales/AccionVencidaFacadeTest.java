package test.co.uniandes.sisinfo.serviciosfuncionales;

import java.sql.SQLException;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import co.uniandes.sisinfo.entities.AccionVencida;
import co.uniandes.sisinfo.entities.AsistenciaGraduada;
import co.uniandes.sisinfo.serviciosfuncionales.AccionVencidaFacade;
import co.uniandes.sisinfo.serviciosfuncionales.AccionVencidaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.AsistenciaGraduadaFacade;
import co.uniandes.sisinfo.serviciosfuncionales.AsistenciaGraduadaFacadeLocal;

/**
 * Prueba unitaria para ejecutar de manera explicita todos los metodos de todas
 * las clases que ejecutan metodos con el entitymanager y así generar el log sql
 * necesario para sacar el modelo dinamico de acceso a tablas.
 * 
 * Ejecutar esta prueba con el parámetro VM: 
 * 	 -Dlog4jdbc.debug.stack.prefix=-Dlog4jdbc.debug.stack.prefix=^co\.uniandes\.serviciosfuncionales.* -Xmx500m -XX:MaxPermSize=128m
 * 
 * 
 * Esta prueba usa Arquillian para subir un Glassfish embebido y así desplegar
 * todos los EJB. Aprovechando que todo está en JPA se usó Postgresql como bd (ver
 * archivo glassfish-resources.xml donde se encuentra toda la configuración de acceso).  
 * 
 * 
 */
@RunWith(Arquillian.class)
public class AccionVencidaFacadeTest {

	@Deployment
	public static WebArchive createDeployment() {

		return ShrinkWrap.create(WebArchive.class, "SisinfoCore.war")
				.addPackage(AccionVencidaFacade.class.getPackage())
				.addPackage(AccionVencidaFacadeLocal.class.getPackage())
				.addPackage(AccionVencida.class.getPackage())
				.addPackage(AsistenciaGraduadaFacade.class.getPackage())
				.addPackage(AsistenciaGraduadaFacadeLocal.class.getPackage())
				.addPackage(AsistenciaGraduada.class.getPackage())
				.addAsResource("persistence.xml", "META-INF/persistence.xml")
				.addAsWebInfResource("META-INF/beans.xml", "beans.xml");
	}

	@EJB
	private AccionVencidaFacadeLocal accionVencidaFacade;
	@EJB
	private AsistenciaGraduadaFacadeLocal asistenciaGraduadaFacadeLocal;

	@Test
	public void testAll() throws SQLException {
		AccionVencida av = new AccionVencida();
		accionVencidaFacade.create(av);
		av.setAccion("accion");
		accionVencidaFacade.edit(av);
//		accionVencidaFacade.remove(av);
//		
//		AsistenciaGraduada ag = new AsistenciaGraduada();
//		asistenciaGraduadaFacadeLocal.create(ag);
//		asistenciaGraduadaFacadeLocal.edit(ag);
//		asistenciaGraduadaFacadeLocal.find(ag.getId());
//		asistenciaGraduadaFacadeLocal.remove(ag);
//		asistenciaGraduadaFacadeLocal.findAll();
//		asistenciaGraduadaFacadeLocal.findById(1L);
//		asistenciaGraduadaFacadeLocal.findByPeriodo("1");
//		asistenciaGraduadaFacadeLocal.findByCorreoProfesor("aa");
//		asistenciaGraduadaFacadeLocal.findByCorreoEstudiante("aa");
//		asistenciaGraduadaFacadeLocal.findByPeriodoYCorreoEstudiante("1", "aa");

	}

}
