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
import co.uniandes.sisinfo.entities.CalificacionJurado;
import co.uniandes.sisinfo.serviciosfuncionales.AccionVencidaFacade;
import co.uniandes.sisinfo.serviciosfuncionales.AccionVencidaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.CalificacionJuradoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ConstanteFacade;
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
public class CalificacionJuradoFacadeTest {

	@Deployment
	public static WebArchive createDeployment() {

		return ShrinkWrap.create(WebArchive.class, "SisinfoCore.war")
				.addPackage(AccionVencidaFacade.class.getPackage())
				.addPackage(AccionVencidaFacadeLocal.class.getPackage())
				.addPackage(AccionVencida.class.getPackage())
				 
				 
				 
				.addPackage(ConstanteFacade.class.getPackage())
				 
				 
				.addPackage(EventoExternoBeanLocal.class.getPackage())
				.addPackage(ProgramaFacadeLocal.class.getPackage())
				 
				.addPackage(RolFacadeLocal.class.getPackage())
				.addPackage(TipoDocumentoFacadeLocal.class.getPackage())
				
				.addAsResource("persistence.xml", "META-INF/persistence.xml")
				.addAsWebInfResource("META-INF/beans.xml", "beans.xml");
	}
	
	@EJB
	private CalificacionJuradoFacadeLocal facade;
	
	@Test
	public void testAll() throws SQLException {
		CalificacionJurado av = new CalificacionJurado();
		facade.create(av);
		av.getCategoriaCriteriosJurado();
		av.getCoasesor();
		av.getJuradoExterno();
		av.getJuradoInterno();
		av.getTesisCalificada();
		av.setHash(String.valueOf(System.currentTimeMillis()));
		facade.edit(av);
		facade.remove(av);
		facade.find(1L);
		facade.findAll();
		facade.count();
		facade.findByHash(av.getHash());
		
	
	}
}