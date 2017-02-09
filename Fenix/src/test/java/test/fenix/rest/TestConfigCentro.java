package test.fenix.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.sot.fenix.components.models.Centro;
import com.sot.fenix.components.models.Centro.TIPO;
import com.sot.fenix.components.models.Ubicacion;
import com.sot.fenix.components.services.CentroService;
import com.sot.fenix.components.services.ConfigCentroService;
import com.sot.fenix.config.AppConfig;
import com.sot.fenix.config.SecurityConfig;

import test.fenix.config.TestDBConfig;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, TestDBConfig.class, SecurityConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestConfigCentro {

    @Autowired
    private ConfigCentroService config;
    @Autowired
    private CentroService centros;

	private Centro centro;

    
	@Before
	public void create() {
		centro=getCentro("Centro test");
		
		centros.getDAO().save(centro);
	}

	
	private Centro getCentro(String nombre){
		Centro centro=new Centro();
		centro.setColor("purple");
		centro.setCorreoAdmin("test@test.com");
		centro.setNombre(nombre);
		centro.setTipo(TIPO.SANIDAD);
		Ubicacion u=getUbicacion();
		centro.setUbicacion(u);
		return centro;
	}
	
	private Ubicacion getUbicacion(){
		Ubicacion u=new Ubicacion();
		u.setCalle("Calle test");
		u.setCP("08292");
		u.setId("#idUbicacion");
		u.setNumero("33");
		u.setPais("ES");
		u.setPoblacion("Terrassa");
		u.setProvincia("Barcelona");
		u.setPosicion(new GeoJsonPoint(1, 1));
		return u;
	}

	
	
	@After
	public void  drop(){
		centros.getDAO().deleteAll();
		config.getDAO().deleteAll();
	}
	

	@Test
	public void testParams()throws Exception{
		assertTrue( config.isAutoFacturar(centro));
		config.setValue(centro, config.AUTO_FACTURAR, false);
		assertFalse( config.isAutoFacturar(centro));
		
		for(int i=0;i<10;i++){
			assertEquals(i+1, config.siguienteFactura(centro));
		}
	}

	
}
