package test.fenix.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.sot.fenix.components.json.ResponseJSON;
import com.sot.fenix.components.models.Centro;
import com.sot.fenix.components.models.Centro.TIPO;
import com.sot.fenix.components.models.Cliente;
import com.sot.fenix.components.models.Prestacion;
import com.sot.fenix.components.models.Ubicacion;
import com.sot.fenix.components.rest.ClienteREST;
import com.sot.fenix.components.services.CentroService;
import com.sot.fenix.components.services.ClienteService;
import com.sot.fenix.components.services.PrestacionService;
import com.sot.fenix.config.AppConfig;
import com.sot.fenix.config.SecurityConfig;

import test.fenix.TestUtils;
import test.fenix.config.TestDBConfig;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, TestDBConfig.class, SecurityConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestClientes {
	@Autowired
    private ClienteREST rest;
	
    
    @Autowired
    private PrestacionService prestaciones;
    @Autowired
    private CentroService centros;
    @Autowired
    private ClienteService clientes;
	
    private Prestacion prestacion;
	private Centro centro;
	
	private MockMvc mockMvc;
    
	@Before
	public void create() {
		mockMvc= MockMvcBuilders.standaloneSetup(rest).build();

		centro=new Centro();
		centro.setColor("purple");
		centro.setCorreoAdmin("test@test.com");
		centro.setNombre("Centro test");
		centro.setTipo(TIPO.SANIDAD);
		Ubicacion u=getUbicacion();
		centro.setUbicacion(u);
		
		centros.getDAO().save(centro);
		
		prestacion=new Prestacion();
		prestacion.setNombre("Test Prestacion");
		prestacion.setCentro(centro);
		
		prestaciones.getDAO().save(prestacion);
		
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
		prestaciones.getDAO().deleteAll();
		centros.getDAO().deleteAll();
		clientes.getDAO().deleteAll();
	}
	

	
	
	
	@Test
	public void getAll() throws Exception {
		Cliente c=new Cliente();
		c.setNombre("Juan");c.setApellido1("Martin");c.setApellido2("Lopez");c.setCentro(centro);c.setCorreo("juan@gmail.com");c.setDni("123");c.setTelefono(6123);
		clientes.getDAO().save(c);
		c.setId(null);c.setNombre("Jua2");c.setApellido1("Martin");c.setApellido2("Lopez");c.setCentro(centro);c.setCorreo("juan@gmail.com");c.setDni("134");c.setTelefono(6123);
		clientes.getDAO().save(c);
		c.setId(null);c.setNombre("Juan2");c.setApellido1("Martin");c.setApellido2("Lopez");c.setCentro(centro);c.setCorreo("juan@gmail.com");c.setDni("123");c.setTelefono(6123);
		clientes.getDAO().save(c);
		
	    mockMvc.perform(MockMvcRequestBuilders.get("/cliente/123"))
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("$.data[0].id").value(prestacion.getId().toHexString()));			
	}
	

}
