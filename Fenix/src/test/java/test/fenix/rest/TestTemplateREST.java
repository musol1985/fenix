package test.fenix.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

import com.sot.fenix.components.json.ResponseJSON;
import com.sot.fenix.components.models.AModelBasic;
import com.sot.fenix.components.models.AModelId;
import com.sot.fenix.components.models.Centro;
import com.sot.fenix.components.models.Centro.TIPO;
import com.sot.fenix.components.models.Prestacion;
import com.sot.fenix.components.models.Ubicacion;
import com.sot.fenix.components.services.CentroService;
import com.sot.fenix.config.AppConfig;
import com.sot.fenix.config.SecurityConfig;
import com.sot.fenix.templates.basic.IBasicService;

import test.fenix.TestUtils;
import test.fenix.config.TestDBConfig;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, TestDBConfig.class, SecurityConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public abstract class TestTemplateREST<T, S extends IBasicService, I extends AModelBasic> {
	@Autowired
    private T rest;
	
    
    @Autowired
    private S service;
    @Autowired
    private CentroService centros;

	
    private I item;
	protected Centro centro;
	
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
		
		item=getItem();
		item.setNombre("TestItem");
		item.setCentro(centro);
		
		service.getDAO().save(item);
		
	}
	
	public abstract I getItem();
	
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
		service.getDAO().deleteAll();
		centros.getDAO().deleteAll();
	}
	
	public abstract String getRestURL();
	
	
	@Test
	public void nuevo() throws Exception {
		I nuevoItem=getItem();
		nuevoItem.setNombre("TestItemNuevo");
		nuevoItem.setCentro(centro);
		
				
	    mockMvc.perform(put("/"+getRestURL()).contentType(TestUtils.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonBytes(nuevoItem)))	    		
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.cod").value(ResponseJSON.OK))
		.andExpect(jsonPath("$.data.nombre").value("TestItemNuevo"))
		.andExpect(jsonPath("$.data.centro").value(item.getCentro().getId().toHexString()));

	    mockMvc.perform(put("/"+getRestURL()).contentType(TestUtils.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonBytes(nuevoItem)))	    		
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.cod").value(ResponseJSON.YA_EXISTE));
	}
	
	@Test
	public void modificar() throws Exception {	
		item.setNombre("itemModificado");
	    mockMvc.perform(post("/"+getRestURL()).contentType(TestUtils.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonBytes(item)))	    		
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.cod").value(ResponseJSON.OK))
		.andExpect(jsonPath("$.data.nombre").value("itemModificado"))
		.andExpect(jsonPath("$.data.centro").value(item.getCentro().getId().toHexString()));

	    item.setId(new ObjectId());
	    mockMvc.perform(post("/"+getRestURL()).contentType(TestUtils.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonBytes(item)))	    		
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.cod").value(ResponseJSON.NO_EXISTE));
	}
	
	@Test
	public void delete() throws Exception {		
	    mockMvc.perform(MockMvcRequestBuilders.delete("/"+getRestURL()+"/"+item.getId().toHexString()))
		.andDo(print())
		.andExpect(status().isOk())
	    .andExpect(jsonPath("$.cod").value(ResponseJSON.OK));
	    
	    mockMvc.perform(MockMvcRequestBuilders.delete("/"+getRestURL()+"/"+item.getId().toHexString()))
		.andDo(print())
		.andExpect(status().isOk())
	    .andExpect(jsonPath("$.cod").value(ResponseJSON.NO_EXISTE));
		//.andDo(print())
	}
	
	
	@Test
	public void getAll() throws Exception {

	    mockMvc.perform(MockMvcRequestBuilders.get("/"+getRestURL()+"/"+centro.getId().toHexString()+"/1/10"))
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("$.data[0].id").value(item.getId().toHexString()));			
	}
	
	@Test
	public void getAllByCentro() throws Exception {

	    mockMvc.perform(MockMvcRequestBuilders.get("/"+getRestURL()+"/all/"+centro.getId().toHexString()))
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("$.data[0].id").value(item.getId().toHexString()));			
	}
	
	@Test
	public void get() throws Exception {		
		I item2=getItem();
		item2.setNombre("TestItem2");
		item2.setCentro(centro);
		
		service.getDAO().save(item2);

	    mockMvc.perform(MockMvcRequestBuilders.get("/"+getRestURL()+"/"+item2.getId().toHexString()))
			.andExpect(status().isOk())
			.andDo(print())
			  .andExpect(jsonPath("$.cod").value(ResponseJSON.OK));
	    
	    
	    mockMvc.perform(MockMvcRequestBuilders.get("/"+getRestURL()+"/"+new ObjectId().toHexString()))
			.andExpect(status().isOk())
			.andDo(print())
			  .andExpect(jsonPath("$.cod").value(ResponseJSON.NO_EXISTE));	
	}
}
