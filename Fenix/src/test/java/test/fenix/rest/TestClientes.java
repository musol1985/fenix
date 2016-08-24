package test.fenix.rest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.bson.types.ObjectId;
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

import com.sot.fenix.components.json.ClienteRequest;
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

		centro=getCentro("Centro test");
		
		centros.getDAO().save(centro);
		
		prestacion=new Prestacion();
		prestacion.setNombre("Test Prestacion");
		prestacion.setCentro(centro);
		
		prestaciones.getDAO().save(prestacion);
		
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
		prestaciones.getDAO().deleteAll();
		centros.getDAO().deleteAll();
		clientes.getDAO().deleteAll();
	}
	
	@Test
	public void nuevo() throws Exception {
		Cliente c3=new Cliente();c3.setId(null);c3.setNombre("juan2");c3.setApellidos("Martinc3");c3.setCentro(centro);c3.setCorreo("juan@gmail.com");c3.setDni("123");c3.setTelefono(6123);

	    mockMvc.perform(MockMvcRequestBuilders.put("/cliente")
	    		.contentType(TestUtils.APPLICATION_JSON_UTF8)
	    		.content(TestUtils.convertObjectToJsonBytes(c3)))
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("$.cod").value(ResponseJSON.OK))
			.andExpect(jsonPath("$.data.nombre").value(c3.getNombre()));
	    
	    mockMvc.perform(MockMvcRequestBuilders.put("/cliente")
	    		.contentType(TestUtils.APPLICATION_JSON_UTF8)
	    		.content(TestUtils.convertObjectToJsonBytes(c3)))
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("$.cod").value(ResponseJSON.YA_EXISTE))
			.andExpect(jsonPath("$.data.nombre").value(c3.getNombre()));
	}
	
	
	@Test
	public void modificar() throws Exception {
		Cliente c3=new Cliente();c3.setId(null);c3.setNombre("juan2");c3.setApellidos("Martinc3");c3.setCentro(centro);c3.setCorreo("juan@gmail.com");c3.setDni("123");c3.setTelefono(6123);
		clientes.getDAO().save(c3);
		
	    mockMvc.perform(MockMvcRequestBuilders.post("/cliente")
	    		.contentType(TestUtils.APPLICATION_JSON_UTF8)
	    		.content(TestUtils.convertObjectToJsonBytes(c3)))
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("$.cod").value(ResponseJSON.OK))
			.andExpect(jsonPath("$.data.nombre").value(c3.getNombre()));
	    
	    c3.setId(new ObjectId());
	    mockMvc.perform(MockMvcRequestBuilders.post("/cliente")
	    		.contentType(TestUtils.APPLICATION_JSON_UTF8)
	    		.content(TestUtils.convertObjectToJsonBytes(c3)))
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("$.cod").value(ResponseJSON.NO_EXISTE));
	}
	
	
	
	@Test
	public void getAll() throws Exception {
		
		Cliente c1=new Cliente();c1.setNombre("juan");c1.setApellidos("Martinc1");c1.setCentro(centro);c1.setCorreo("juan@gmail.com");c1.setDni("123");c1.setTelefono(6123);
		clientes.getDAO().save(c1);
		Cliente c2=new Cliente();c2.setId(null);c2.setNombre("jua2");c2.setApellidos("Martinc2");c2.setCentro(centro);c2.setCorreo("juan@gmail.com");c2.setDni("134");c2.setTelefono(6123);
		clientes.getDAO().save(c2);
		Cliente c3=new Cliente();c3.setId(null);c3.setNombre("juan2");c3.setApellidos("Martinc3");c3.setCentro(centro);c3.setCorreo("juan@gmail.com");c3.setDni("123");c3.setTelefono(6123);
		clientes.getDAO().save(c3);
		
		Centro cen=getCentro("Centro test2");		
		centros.getDAO().save(cen);
		Cliente c4=new Cliente();c4.setId(null);c4.setNombre("juan2");c4.setApellidos("Martinc4");c4.setCentro(cen);c4.setCorreo("juan@gmail.com");c4.setDni("123");c4.setTelefono(6123);
		clientes.getDAO().save(c4);
		
		ClienteRequest r=new ClienteRequest();
		
		//Debe devolver el c1 y c3
		System.out.println("Debe devolver el c1 y c3");		
		r.centro=centro.getId().toHexString();
		r.texto="123";
	    mockMvc.perform(MockMvcRequestBuilders.post("/cliente/buscar")
	    		.contentType(TestUtils.APPLICATION_JSON_UTF8)
	    		.content(TestUtils.convertObjectToJsonBytes(r)))
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("$.[0].id").value(c1.getId().toHexString()))
			.andExpect(jsonPath("$.[1].id").value(c3.getId().toHexString()));
		//Debe devolver el c4
	    System.out.println("Debe devolver el c4");
	    r.centro=cen.getId().toHexString();
		r.texto="123";
	    mockMvc.perform(MockMvcRequestBuilders.post("/cliente/buscar")
	    		.contentType(TestUtils.APPLICATION_JSON_UTF8)
	    		.content(TestUtils.convertObjectToJsonBytes(r)))
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("$.[0].id").value(c4.getId().toHexString()));	
	  //Debe devolver el c1, c2 y c3
	    System.out.println("Debe devolver el c1, c2 y c3");
		r.centro=centro.getId().toHexString();
		r.texto="jU";
	    mockMvc.perform(MockMvcRequestBuilders.post("/cliente/buscar")
	    		.contentType(TestUtils.APPLICATION_JSON_UTF8)
	    		.content(TestUtils.convertObjectToJsonBytes(r)))
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("$.[0].id").value(c1.getId().toHexString()))
			.andExpect(jsonPath("$.[1].id").value(c2.getId().toHexString()))
	    	.andExpect(jsonPath("$.[2].id").value(c3.getId().toHexString()));
	    //Debe devolver el c4
	    System.out.println("Debe devolver el c4");
	    r.centro=cen.getId().toHexString();
		r.texto="jU";
	    mockMvc.perform(MockMvcRequestBuilders.post("/cliente/buscar")
	    		.contentType(TestUtils.APPLICATION_JSON_UTF8)
	    		.content(TestUtils.convertObjectToJsonBytes(r)))
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("$.[0].id").value(c4.getId().toHexString()));
	}
	

}
