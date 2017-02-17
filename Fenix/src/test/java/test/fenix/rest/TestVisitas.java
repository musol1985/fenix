package test.fenix.rest;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.bson.types.ObjectId;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import com.sot.fenix.components.json.ResponseJSON;
import com.sot.fenix.components.models.Cita;
import com.sot.fenix.components.models.Visita;
import com.sot.fenix.components.rest.VisitaREST;
import com.sot.fenix.components.services.CitaService;
import com.sot.fenix.components.services.VisitaService;
import com.sot.fenix.config.AppConfig;
import com.sot.fenix.config.SecurityConfig;
import com.sot.fenix.dao.VisitaDAO;

import test.fenix.TestUtils;
import test.fenix.config.TestDBConfig;
import test.fenix.rest.templates.CentroBasicTemplateREST;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, TestDBConfig.class, SecurityConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestVisitas extends CentroBasicTemplateREST<Visita, VisitaDAO, VisitaService, VisitaREST>{

    @Autowired
    protected CitaService citas;

	@Override
	public Visita getModel() {
		return TestUtils.getNewVisita(centro);
	}


	@Override
	public String getRestURL() {
		return "visita";
	}


	@Override
	public Visita getModelTestModificar(Visita modelAModificar) {
		modelAModificar.setFecha(new Date());
		return modelAModificar;
	}


	@Override
	public void postTestModificar(ResultActions res) throws Exception {
		
	}


	@Override
	public int getModificarCode() {
		return ResponseJSON.ACCION_PROHIBIDA_REST;
	}


	@Override
	public int getDeleteCode() {
		return ResponseJSON.ACCION_PROHIBIDA_REST;
	}
	
	@Test
	public void testPaseVisita()throws Exception{
		Cita c=TestUtils.getSavedCita(citas.getDAO(), centro, cliente, prestacion, usuario, TestUtils.toDate("30/08/2016 14:50"), TestUtils.toDate("30/08/2016 15:50"));				
		
	    ResultActions res=mockMvc.perform(post("/"+getRestURL()+"/pasar/visita").session(getSession()).contentType(TestUtils.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonBytes(c)))	    		
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.cod").value(ResponseJSON.OK));
	   
	    Visita v=service.getByCita(c);
	    assertTrue(v!=null);
	    assertTrue(v.getCita().getJsonId().equals(c.getJsonId()));
	    
	}

}
