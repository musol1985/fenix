package test.fenix.rest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.sot.fenix.components.json.ClienteRequest;
import com.sot.fenix.components.json.ResponseJSON;
import com.sot.fenix.components.models.Centro;
import com.sot.fenix.components.models.Cliente;
import com.sot.fenix.components.rest.ClienteREST;
import com.sot.fenix.components.services.ClienteService;
import com.sot.fenix.config.AppConfig;
import com.sot.fenix.config.SecurityConfig;
import com.sot.fenix.dao.ClienteDAO;

import test.fenix.TestUtils;
import test.fenix.config.TestDBConfig;
import test.fenix.rest.templates.MantenimientosTemplateREST;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, TestDBConfig.class, SecurityConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestClientes extends MantenimientosTemplateREST<Cliente, ClienteDAO, ClienteService, ClienteREST>{


	@Override
	public Cliente getModel() {
		Cliente c= TestUtils.getNewCliente(centro);
		c.setDni(String.valueOf(System.currentTimeMillis()));
		return c;
	}

	@Override
	public String getRestURL() {
		return "cliente";
	}

	@Override
	public Cliente getModelTestModificar(Cliente modelAModificar) {
		modelAModificar.setNombre("NombreCambiado");
		return modelAModificar;
	}

	@Override
	public void postTestModificar(ResultActions res) throws Exception {
		res.andExpect(jsonPath("$.data.nombre").value("NombreCambiado"));
	}
	
	@Test
	public void delete() throws Exception {		
	    mockMvc.perform(MockMvcRequestBuilders.delete("/"+getRestURL()+"/"+model.getId().toHexString()))
		.andDo(print())
		.andExpect(status().isOk())
	    .andExpect(jsonPath("$.cod").value(ResponseJSON.ACCION_PROHIBIDA_REST));
	}
	

	@Test
	public void getAll() throws Exception {
		//borro todos los que hay
		service.getDAO().deleteAll();
		
		Cliente c1=TestUtils.getCliente(centro, "123", "juan", "Martinc1", "juan@gmail.com", "6123");
		service.getDAO().save(c1);
		Cliente c2=TestUtils.getCliente(centro, "134", "jua2", "Martinc2", "juan@gmail.com", "6128");
		service.getDAO().save(c2);
		Cliente c3=TestUtils.getCliente(centro, "123", "juan2", "Martinc3", "juan@gmail.com", "6123");
		service.getDAO().save(c3);
		
		Centro cen=TestUtils.getNewCentro();
		cen.setNombre("Centro2");
		centros.getDAO().save(cen);
		
		Cliente c4=TestUtils.getCliente(cen, "123", "juan2", "Martinc4", "juan@gmail.com", "6128");		
		service.getDAO().save(c4);
		
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
