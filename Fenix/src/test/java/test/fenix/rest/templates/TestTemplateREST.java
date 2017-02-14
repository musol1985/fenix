package test.fenix.rest.templates;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.sot.fenix.components.json.ResponseJSON;
import com.sot.fenix.components.models.Centro;
import com.sot.fenix.components.models.Usuario;
import com.sot.fenix.components.models.templates.AModelCentro;
import com.sot.fenix.components.models.templates.AModelId;
import com.sot.fenix.components.models.templates.AModelNombre;
import com.sot.fenix.components.services.CentroService;
import com.sot.fenix.components.services.UsuarioService;
import com.sot.fenix.config.AppConfig;
import com.sot.fenix.config.SecurityConfig;
import com.sot.fenix.templates.REST.ABasicREST;
import com.sot.fenix.templates.dao.IBasicIdDAO;
import com.sot.fenix.templates.service.ABasicService;

import test.fenix.TestUtils;
import test.fenix.config.TestDBConfig;

/**
 * Implementación basica de los tests de REST
 * inicializa centro y usuario
 * @author eduarmar
 *
 * @param <T>
 * @param <S>
 * @param <I>
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, TestDBConfig.class, SecurityConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public abstract class TestTemplateREST<I extends AModelId, D extends IBasicIdDAO<I>, S extends ABasicService<D>, R extends ABasicREST<S,I,D>> {
	@Autowired
    protected R rest;
	
    
    @Autowired
    protected S service;    

    protected I model;
	
	protected MockMvc mockMvc;
	
	//ITEMS
	protected Centro centro;
	protected Usuario usuario;
	
	//SERVICIOS
	@Autowired
	protected UsuarioService usuarios;
    @Autowired
    protected CentroService centros;

    
	@Before
	public void create() {
		mockMvc= MockMvcBuilders.standaloneSetup(rest).build();

		centro=TestUtils.getNewCentro();
		centros.getDAO().save(centro);
		
		usuario=TestUtils.getNewUsuario("usuario test", centro);
		usuarios.getDAO().save(usuario);
		
		model=getNewModel(true);
	}
	
	protected I getNewModel(boolean saveBD){
		I model=getModel();
		
		if(model!=null){
			if(model instanceof AModelCentro){				
				((AModelCentro)model).setCentro(centro);
			}		
			if(model instanceof AModelNombre){
				((AModelNombre)model).setNombre(model.getClass().getName()+" Test");
			}
			
			if(saveBD)
				service.getDAO().save(model);
		}
		return model;
	}
	
	public abstract I getModel();
	
	@After
	public void  drop(){
		service.getDAO().deleteAll();
		centros.getDAO().deleteAll();
		usuarios.getDAO().deleteAll();				
	}
	
	public abstract String getRestURL();
	/*
	
	@Test
	public void nuevo() throws Exception {
		I nuevoItem=getNewModel(false);
				
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
	}*/
	
	@Test
	public void get() throws Exception {		
		I item2=getNewModel(true);

	    mockMvc.perform(MockMvcRequestBuilders.get("/"+getRestURL()+"/"+item2.getId().toHexString()))
			.andExpect(status().isOk())
			.andDo(print())
			  .andExpect(jsonPath("$.cod").value(ResponseJSON.OK));
	    
	    
	    mockMvc.perform(MockMvcRequestBuilders.get("/"+getRestURL()+"/"+new ObjectId().toHexString()))
			.andExpect(status().isOk())
			.andDo(print())
			  .andExpect(jsonPath("$.cod").value(ResponseJSON.NO_EXISTE));	
	}
	
	
	/**
	 * Metodo para modificar un model para el test de modificar(por ejemplo cambiarle el nombre)
	 * @param modelAModificar
	 * @return
	 */
	public abstract I getModelTestModificar(I modelAModificar);
	
	/**
	 * Metodo que se ejecuta en el test modificar, y que sirve para comprobar el resultado de la modificacion
	 * @param res
	 */
	public abstract void postTestModificar(ResultActions res)throws Exception;
	
	@Test
	public void modificar() throws Exception {	
		I modelModificado=getModelTestModificar(model);
	    ResultActions res=mockMvc.perform(post("/"+getRestURL()).contentType(TestUtils.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonBytes(modelModificado)))	    		
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.cod").value(ResponseJSON.OK));
	    postTestModificar(res);

	    modelModificado.setId(new ObjectId());
	    mockMvc.perform(post("/"+getRestURL()).contentType(TestUtils.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonBytes(modelModificado)))	    		
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.cod").value(ResponseJSON.NO_EXISTE));
	}

	
	@Test
	public void delete() throws Exception {		
	    mockMvc.perform(MockMvcRequestBuilders.delete("/"+getRestURL()+"/"+model.getId().toHexString()))
		.andDo(print())
		.andExpect(status().isOk())
	    .andExpect(jsonPath("$.cod").value(ResponseJSON.OK));
	    
	    mockMvc.perform(MockMvcRequestBuilders.delete("/"+getRestURL()+"/"+model.getId().toHexString()))
		.andDo(print())
		.andExpect(status().isOk())
	    .andExpect(jsonPath("$.cod").value(ResponseJSON.NO_EXISTE));
		//.andDo(print())
	}
	
	/*
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
	}*/
	
	
}
