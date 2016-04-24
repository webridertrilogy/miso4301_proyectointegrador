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
import co.uniandes.sisinfo.entities.TareaMultiple;
import co.uniandes.sisinfo.serviciosfuncionales.AccionVencidaFacade;
import co.uniandes.sisinfo.serviciosfuncionales.AccionVencidaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.AsistenciaGraduadaFacade;
import co.uniandes.sisinfo.serviciosfuncionales.AsistenciaGraduadaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ConstanteFacade;
import co.uniandes.sisinfo.serviciosfuncionales.TareaMultipleFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.ProgramaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.seguridad.RolFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.soporte.TipoDocumentoFacadeLocal;
import co.uniandes.sisinfo.serviciosnegocio.EventoExternoBeanLocal;

/**
 * Prueba unitaria para ejecutar de manera explicita todos los metodos de todas
 * las clases que ejecutan metodos con el entitymanager y así generar el log sql
 * necesario para sacar el modelo dinamico de acceso a tablas.
 * 
 * Ejecutar esta prueba con el parámetro VM: 
 * 	 -Xmx500m -XX:MaxPermSize=128m -Dlog4jdbc.debug.stack.prefix=co.uniandes.sisinfo
 * 
 * 
 * Esta prueba usa Arquillian para subir un Glassfish embebido y así desplegar
 * todos los EJB. Aprovechando que todo está en JPA se usó Postgresql como bd (ver
 * archivo glassfish-resources.xml donde se encuentra toda la configuración de acceso).  
 * 
 * 
 */
@RunWith(Arquillian.class)
public class TareaMultipleFacadeTest {

	@Deployment
	public static WebArchive createDeployment() {

		return ShrinkWrap.create(WebArchive.class, "SisinfoCore.war")
				.addPackage(AccionVencidaFacade.class.getPackage())
				.addPackage(AccionVencidaFacadeLocal.class.getPackage())
				.addPackage(AccionVencida.class.getPackage())
				.addPackage(AsistenciaGraduadaFacade.class.getPackage())
				.addPackage(AsistenciaGraduadaFacadeLocal.class.getPackage())
				.addPackage(AsistenciaGraduada.class.getPackage())
				.addPackage(ConstanteFacade.class.getPackage())
				.addPackage(AsistenciaGraduadaFacadeLocal.class.getPackage())
				 
				.addPackage(EventoExternoBeanLocal.class.getPackage())
				.addPackage(ProgramaFacadeLocal.class.getPackage())
				 
				.addPackage(RolFacadeLocal.class.getPackage())
				.addPackage(TipoDocumentoFacadeLocal.class.getPackage())
				
				.addAsResource("persistence.xml", "META-INF/persistence.xml")
				.addAsWebInfResource("META-INF/beans.xml", "beans.xml");
	}
	
	@EJB
	private TareaMultipleFacadeLocal facade;
	
	@Test
	public void testAll() throws SQLException {
		TareaMultiple av = new TareaMultiple();
		
		facade.create(av);
		av.setAsunto("a");
		facade.edit(av);
		facade.remove(av);
		facade.find(1L);
		facade.findByCorreoYTipo("a", "a");
		facade.findByRolYTipo("q", "a");
		facade.findByCorreo("a");
		facade.findByRol("a");
		facade.findByRolNoPersonal("a");
		facade.findByRolYTipoNoPersonal("a", "a");
		facade.findByTipo("a");		
		facade.findByEstadoTareaSencilla("a");
		facade.findByCorreoRolAndEstadoTareaSencilla("a", "a", "a");
		facade.findByRolAndEstadoTareaSencillaNoPersonal("a", "a");
		
	}
}