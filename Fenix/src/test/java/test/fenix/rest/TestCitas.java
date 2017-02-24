package test.fenix.rest;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertTrue;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.sot.fenix.components.json.CitaRequestJSON;
import com.sot.fenix.components.json.ResponseJSON;
import com.sot.fenix.components.models.Cita;
import com.sot.fenix.components.models.Cita.ESTADO;
import com.sot.fenix.components.models.Cliente;
import com.sot.fenix.components.models.Prestacion;
import com.sot.fenix.components.models.Usuario;
import com.sot.fenix.components.models.Visita;
import com.sot.fenix.components.rest.CitaREST;
import com.sot.fenix.components.services.CitaService;
import com.sot.fenix.components.services.VisitaService;
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
	
	@Autowired
	private VisitaService visitas;

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
		CitaRequestJSON r=new CitaRequestJSON();
		r.setCita(c);
		
	    //MODIFICAR 
		performPOST("/cita/modificar", r)
			.andExpect(jsonPath("$.cod").value(ResponseJSON.OK));
	    
	    getSavedCita(TestUtils.toDate("30/08/2016 14:50"), TestUtils.toDate("30/08/2016 15:50"));
	    
	    //MODIFICAR  ERR SOLAPA
	    c.setFechaIni(TestUtils.toDate("30/08/2016 14:30"));
	    c.setFechaFin(TestUtils.toDate("30/08/2016 15:00"));
	    performPOST("/cita/modificar", r)
			.andExpect(jsonPath("$.cod").value(ResponseJSON.RES_TIENE_SOLAPA));
	    
	    //Forzar solapa
	    r.setForzar(true);
	    performPOST("/cita/modificar", r)
			.andExpect(jsonPath("$.cod").value(ResponseJSON.OK));
	    
	    //Cliente no encontrado
	    Cliente cErr=new Cliente();
	    cErr.setId(null);
	    c.setCliente(cErr);
	    performPOST("/cita/modificar", r)
			.andExpect(jsonPath("$.cod").value(ResponseJSON.NO_CLIENTE));
	    
	    //NO PRESTACION
	    Prestacion cPrest=new Prestacion();
	    cPrest.setId(null);
	    c.setCliente(cliente);
	    c.setPrestacion(cPrest);
	    performPOST("/cita/modificar", r)
			.andExpect(jsonPath("$.cod").value(ResponseJSON.NO_PRESTACION));
	    
	    //NO Profesional
	    Usuario usu=new Usuario();
	    usu.setId(null);	    
	    c.setPrestacion(prestacion);
	    c.setProfesional(usu);
	    performPOST("/cita/modificar", r)
			.andExpect(jsonPath("$.cod").value(ResponseJSON.NO_PROFESIONAL));

	}
	
	
	@Test
	public void nuevo() throws Exception {
		Cita c=getCita(TestUtils.toDate("30/08/2016 14:30"), TestUtils.toDate("30/08/2016 14:50"));
		c.setImporte(300);
		
		CitaRequestJSON r=new CitaRequestJSON();
		r.setCita(c);
			
		performPUT("/cita", r)
			.andExpect(jsonPath("$.cod").value(ResponseJSON.OK))
			.andExpect(jsonPath("$.data.estado").value(ESTADO.PROGRAMADA.name()));
	    
	    //SOLAPA
		performPUT("/cita", r)
			.andExpect(jsonPath("$.cod").value(ResponseJSON.RES_TIENE_SOLAPA));
		
		//FORZAMOS SOLAPA
		r.setForzar(true);
		performPUT("/cita", r)
			.andExpect(jsonPath("$.cod").value(ResponseJSON.OK));

	    //Cliente no encontrado
	    Cliente cErr=new Cliente();
	    cErr.setId(null);
	    c.setCliente(cErr);
	    performPUT("/cita", r)
			.andExpect(jsonPath("$.cod").value(ResponseJSON.NO_CLIENTE));
	    
	    //NO PRESTACION
	    Prestacion cPrest=new Prestacion();
	    cPrest.setId(null);
	    c.setCliente(cliente);
	    c.setPrestacion(cPrest);
	    performPUT("/cita", r)
			.andExpect(jsonPath("$.cod").value(ResponseJSON.NO_PRESTACION));
	    
	    //NO Profesional
	    Usuario usu=new Usuario();
	    usu.setId(null);	    
	    c.setPrestacion(prestacion);
	    c.setProfesional(usu);
	    performPUT("/cita", r)
			.andExpect(jsonPath("$.cod").value(ResponseJSON.NO_PROFESIONAL));
	}
	
	
	@Test
	public void reprogramar() throws Exception {
		Cita cBD=getSavedCita(TestUtils.toDate("16/07/2016 07:10"), TestUtils.toDate("16/07/2016 07:30"));
		getSavedCita(TestUtils.toDate("16/07/2016 11:00"), TestUtils.toDate("16/07/2016 12:00"));
				
		cBD.setId(cBD.getId());
		cBD.setFechaIni(TestUtils.toDate("16/07/2016 08:00"));
		cBD.setFechaFin(TestUtils.toDate("16/07/2016 10:00"));
		
		CitaRequestJSON req=new CitaRequestJSON();
		req.cita=cBD;
		req.forzar=false;
	    
		//No solapa
		performPOST("/cita/reprogramar",req)
			.andExpect(jsonPath("$.cod").value(ResponseJSON.OK))
			.andExpect(jsonPath("$.data.estado").value(ESTADO.PROGRAMADA.name()));
	    
	    
	    cBD.setFechaIni(TestUtils.toDate("16/07/2016 11:15"));
		cBD.setFechaFin(TestUtils.toDate("16/07/2016 11:20"));
		//Si solapa xo no forzamos
		performPOST("/cita/reprogramar",req)
			.andExpect(jsonPath("$.cod").value(ResponseJSON.RES_TIENE_SOLAPA));
	    
	    req.forzar=true;
		//Si solapa y forzamos 
	    performPOST("/cita/reprogramar",req)
			.andExpect(jsonPath("$.cod").value(ResponseJSON.OK))
			.andExpect(jsonPath("$.data.estado").value(ESTADO.PROGRAMADA.name()));
	}

	@Test
	public void borrar() throws Exception {
		Cita item=getSavedCita(TestUtils.toDate("16/07/2016 07:10"), TestUtils.toDate("16/07/2016 07:30"));
		
	    
	    performDELETE("/cita/"+item.getId().toHexString())
	    	.andExpect(jsonPath("$.cod").value(ResponseJSON.OK));
	    
	    performDELETE("/cita/"+item.getId().toHexString())
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
	
	@Test
	public void capturar()throws Exception{
		Cita c=getSavedCita(TestUtils.toDate("16/07/2016 07:10"), TestUtils.toDate("16/07/2016 07:30"));
		
		performPOST("/"+getRestURL()+"/capturar", c)
			.andExpect(jsonPath("$.cod").value(ResponseJSON.OK));
		
		assertTrue(service.getDAO().findOne(c.getId()).getEstado()==Cita.ESTADO.CAPTURADA);
	   
	    Visita v=visitas.getByCita(c);
	    assertTrue(v!=null);
	    assertTrue(v.getCita().getJsonId().equals(c.getJsonId()));
	}

}
