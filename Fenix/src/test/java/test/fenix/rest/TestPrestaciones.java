package test.fenix.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.sot.fenix.components.json.NuevoCentroJSON;
import com.sot.fenix.components.json.PosicionJSON;
import com.sot.fenix.components.json.ResponseJSON;
import com.sot.fenix.components.models.Centro;
import com.sot.fenix.components.models.Centro.TIPO;
import com.sot.fenix.components.models.Prestacion;
import com.sot.fenix.components.models.Ubicacion;
import com.sot.fenix.components.rest.PrestacionREST;
import com.sot.fenix.components.services.CentroService;
import com.sot.fenix.components.services.PrestacionService;
import com.sot.fenix.config.AppConfig;
import com.sot.fenix.config.SecurityConfig;

import test.fenix.TestUtils;
import test.fenix.config.TestDBConfig;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, TestDBConfig.class, SecurityConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestPrestaciones {
	@Autowired
    private PrestacionREST prestacionREST;
	
    
    @Autowired
    private PrestacionService prestaciones;
    @Autowired
    private CentroService centros;

	
    private Prestacion prestacion;
	private Centro centro;
	
	private MockMvc mockMvc;
    
	@Before
	public void create() {
		mockMvc= MockMvcBuilders.standaloneSetup(prestacionREST).build();

		centro=new Centro();
		centro.setColor("purple");
		centro.setCorreoAdmin("test@test.com");
		centro.setNombre("Centro test");
		centro.setTipo(TIPO.SANIDAD);
		Ubicacion u=getUbicacion();
		centro.setUbicacion(u);
		
		centros.getDAO().save(centro);
		
		prestacion=new Prestacion();
		prestacion.setAlias("Test");
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
	}
	
	
	
	@Test
	public void nuevo() throws Exception {
		Prestacion p=new Prestacion();
		p.setNombre("Prestacion 2");
		p.setAlias("Test2");
		p.setCentro(centro);
		
		
	    mockMvc.perform(post("/prestacion/nueva").contentType(TestUtils.APPLICATION_JSON_UTF8))
               // .content("{'id':'57b45d7814605f47fb3ae131','nombre':'Test Prestacion','alias':'Test'}"))		
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.cod").value(ResponseJSON.OK))
		.andExpect(jsonPath("$.data.nombre").value("Centro test"));

	}
	
	
	@Test
	public void getAll() throws Exception {

	    mockMvc.perform(get("/prestacion/"+centro.getId().toHexString()+"/1/10"))
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("$.data[0].id").value(prestacion.getId().toHexString()));
	}
	
}
