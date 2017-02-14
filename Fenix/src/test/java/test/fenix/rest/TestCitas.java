package test.fenix.rest;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.sot.fenix.components.json.ReprogramarRequestJSON;
import com.sot.fenix.components.json.ResponseJSON;
import com.sot.fenix.components.models.Cita;
import com.sot.fenix.components.models.Cita.ESTADO;
import com.sot.fenix.components.models.Cliente;
import com.sot.fenix.components.models.Prestacion;
import com.sot.fenix.components.models.Usuario;
import com.sot.fenix.components.rest.CitaREST;
import com.sot.fenix.components.services.CitaService;
import com.sot.fenix.config.AppConfig;
import com.sot.fenix.config.SecurityConfig;
import com.sot.fenix.dao.CitaDAO;

import test.fenix.TestUtils;
import test.fenix.config.TestDBConfig;
import test.fenix.rest.templates.CentroBasicTemplateREST;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, TestDBConfig.class, SecurityConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestCitas extends CentroBasicTemplateREST<Cita, CitaDAO, CitaService, CitaREST>{

	@Override
	public Cita getModel() {
		return TestUtils.getCita(centro, 0);
	}


	@Override
	public String getRestURL() {
		return "cita";
	}


	@Override
	public Cita getModelTestModificar(Cita modelAModificar) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void postTestModificar(ResultActions res) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	private Cita getCita(Date fIni, Date fFin){
		return TestUtils.getSavedCita(null, centro, cliente, prestacion, usuario, fIni, fFin);
	}
	
	private Cita getSavedCita(Date fIni, Date fFin){
		return TestUtils.getSavedCita(service.getDAO(), centro, cliente, prestacion, usuario, fIni, fFin);
	}
	
	@Override
	@Test
	public void modificar()throws Exception{
		Cita c=getSavedCita(TestUtils.toDate("30/08/2016 14:30"), TestUtils.toDate("30/08/2016 14:50"));
	    //MODIFICAR 
	    mockMvc.perform(MockMvcRequestBuilders.post("/cita")
	    		.contentType(TestUtils.APPLICATION_JSON_UTF8)
	    		.content(TestUtils.convertObjectToJsonBytes(c)))
	    	.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.cod").value(ResponseJSON.OK));
	    
	    Cita c2=getSavedCita(TestUtils.toDate("30/08/2016 14:50"), TestUtils.toDate("30/08/2016 15:50"));
	    
	    //MODIFICAR  ERR SOLAPA
	    c.setFechaIni(TestUtils.toDate("30/08/2016 14:30"));
	    c.setFechaFin(TestUtils.toDate("30/08/2016 15:00"));
	    mockMvc.perform(MockMvcRequestBuilders.post("/cita")
	    		.contentType(TestUtils.APPLICATION_JSON_UTF8)
	    		.content(TestUtils.convertObjectToJsonBytes(c)))
	    	.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.cod").value(CitaREST.RES_TIENE_SOLAPA));
	    
	    //Cliente no encontrado
	    Cliente cErr=new Cliente();
	    cErr.setId(null);
	    c.setCliente(cErr);
	    mockMvc.perform(MockMvcRequestBuilders.post("/cita")
	    		.contentType(TestUtils.APPLICATION_JSON_UTF8)
	    		.content(TestUtils.convertObjectToJsonBytes(c)))
	    	.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.cod").value(CitaREST.RES_NO_CLIENTE));
	    
	    //NO PRESTACION
	    Prestacion cPrest=new Prestacion();
	    cPrest.setId(null);
	    c.setCliente(cliente);
	    c.setPrestacion(cPrest);
	    mockMvc.perform(MockMvcRequestBuilders.post("/cita")
	    		.contentType(TestUtils.APPLICATION_JSON_UTF8)
	    		.content(TestUtils.convertObjectToJsonBytes(c)))
	    	.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.cod").value(CitaREST.RES_NO_PRESTACION));
	    
	    //NO Profesional
	    Usuario usu=new Usuario();
	    usu.setId(null);	    
	    c.setPrestacion(prestacion);
	    c.setProfesional(usu);
	    mockMvc.perform(MockMvcRequestBuilders.post("/cita")
	    		.contentType(TestUtils.APPLICATION_JSON_UTF8)
	    		.content(TestUtils.convertObjectToJsonBytes(c)))
	    	.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.cod").value(CitaREST.RES_NO_PROFESIONAL));
	}
	
	
	@Test
	public void nuevo() throws Exception {
		Cita c=getCita(TestUtils.toDate("30/08/2016 14:30"), TestUtils.toDate("30/08/2016 14:50"));
		c.setImporte(300);
				
	    mockMvc.perform(MockMvcRequestBuilders.put("/cita")
	    		.contentType(TestUtils.APPLICATION_JSON_UTF8)
	    		.content(TestUtils.convertObjectToJsonBytes(c)))
	    	.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.cod").value(ResponseJSON.OK))
			.andExpect(jsonPath("$.data.estado").value(ESTADO.PROGRAMADA.name()));
	    
	    //SOLAPA
	    mockMvc.perform(MockMvcRequestBuilders.put("/cita")
	    		.contentType(TestUtils.APPLICATION_JSON_UTF8)
	    		.content(TestUtils.convertObjectToJsonBytes(c)))
	    	.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.cod").value(CitaREST.RES_TIENE_SOLAPA));

	    //Cliente no encontrado
	    Cliente cErr=new Cliente();
	    cErr.setId(null);
	    c.setCliente(cErr);
	    mockMvc.perform(MockMvcRequestBuilders.put("/cita")
	    		.contentType(TestUtils.APPLICATION_JSON_UTF8)
	    		.content(TestUtils.convertObjectToJsonBytes(c)))
	    	.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.cod").value(CitaREST.RES_NO_CLIENTE));
	    
	    //NO PRESTACION
	    Prestacion cPrest=new Prestacion();
	    cPrest.setId(null);
	    c.setCliente(cliente);
	    c.setPrestacion(cPrest);
	    mockMvc.perform(MockMvcRequestBuilders.put("/cita")
	    		.contentType(TestUtils.APPLICATION_JSON_UTF8)
	    		.content(TestUtils.convertObjectToJsonBytes(c)))
	    	.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.cod").value(CitaREST.RES_NO_PRESTACION));
	    
	    //NO Profesional
	    Usuario usu=new Usuario();
	    usu.setId(null);	    
	    c.setPrestacion(prestacion);
	    c.setProfesional(usu);
	    mockMvc.perform(MockMvcRequestBuilders.put("/cita")
	    		.contentType(TestUtils.APPLICATION_JSON_UTF8)
	    		.content(TestUtils.convertObjectToJsonBytes(c)))
	    	.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.cod").value(CitaREST.RES_NO_PROFESIONAL));
	}
	
	
	@Test
	public void reprogramar() throws Exception {
		Cita cBD=getSavedCita(TestUtils.toDate("16/07/2016 07:10"), TestUtils.toDate("16/07/2016 07:30"));
		Cita cBD2=getSavedCita(TestUtils.toDate("16/07/2016 11:00"), TestUtils.toDate("16/07/2016 12:00"));
				
		cBD.setId(cBD.getId());
		cBD.setFechaIni(TestUtils.toDate("16/07/2016 08:00"));
		cBD.setFechaFin(TestUtils.toDate("16/07/2016 10:00"));
		
		ReprogramarRequestJSON req=new ReprogramarRequestJSON();
		req.cita=cBD;
		req.forzar=false;
	    
		//No solapa
	    mockMvc.perform(MockMvcRequestBuilders.post("/cita/reprogramar")
	    		.contentType(TestUtils.APPLICATION_JSON_UTF8)
	    		.content(TestUtils.convertObjectToJsonBytes(req)))
	    	.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.cod").value(ResponseJSON.OK))
			.andExpect(jsonPath("$.data.estado").value(ESTADO.PROGRAMADA.name()));
	    
	    
	    cBD.setFechaIni(TestUtils.toDate("16/07/2016 11:15"));
		cBD.setFechaFin(TestUtils.toDate("16/07/2016 11:20"));
		//Si solapa xo no forzamos
	    mockMvc.perform(MockMvcRequestBuilders.post("/cita/reprogramar")
	    		.contentType(TestUtils.APPLICATION_JSON_UTF8)
	    		.content(TestUtils.convertObjectToJsonBytes(req)))
	    	.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.cod").value(CitaREST.RES_TIENE_SOLAPA))
			.andExpect(jsonPath("$.data.estado").value(ESTADO.PROGRAMADA.name()));
	    
	    req.forzar=true;
		//Si solapa y forzamos 
	    mockMvc.perform(MockMvcRequestBuilders.post("/cita/reprogramar")
	    		.contentType(TestUtils.APPLICATION_JSON_UTF8)
	    		.content(TestUtils.convertObjectToJsonBytes(req)))
	    	.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.cod").value(ResponseJSON.OK))
			.andExpect(jsonPath("$.data.estado").value(ESTADO.PROGRAMADA.name()));
	}

	@Test
	public void borrar() throws Exception {
		Cita item=getSavedCita(TestUtils.toDate("16/07/2016 07:10"), TestUtils.toDate("16/07/2016 07:30"));
		
	    
	    mockMvc.perform(MockMvcRequestBuilders.delete("/cita/"+item.getId().toHexString()))
		.andDo(print())
		.andExpect(status().isOk())
	    .andExpect(jsonPath("$.cod").value(ResponseJSON.OK));
	    
	    mockMvc.perform(MockMvcRequestBuilders.delete("/cita/"+item.getId().toHexString()))
		.andDo(print())
		.andExpect(status().isOk())
	    .andExpect(jsonPath("$.cod").value(ResponseJSON.NO_EXISTE));
	}
	
	
	@Test
	public void get() throws Exception {

		List<Cita> citas=new ArrayList<Cita>();
		

		for(int i=0;i<10;i++)
			citas.add(TestUtils.getCita(centro,1440*i));
		
		service.getDAO().save(citas);
		
		String fechaIni=TestUtils.fromDateCalendar(citas.get(0).getFechaFin());
		String fechaFin=TestUtils.fromDateCalendar(citas.get(citas.size()-1).getFechaFin());
		
		
		
	    mockMvc.perform(MockMvcRequestBuilders.get("/cita/in")
	    		.param("centro", centro.getId().toHexString())
	    		.param("start", fechaIni)
	    		.param("end", fechaFin)
	    		)
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("$", hasSize(10)));	
	}

}
