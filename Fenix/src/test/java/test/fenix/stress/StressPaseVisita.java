package test.fenix.stress;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.sot.fenix.components.json.ResponseJSON;
import com.sot.fenix.components.models.Centro;
import com.sot.fenix.components.models.Cita;
import com.sot.fenix.components.models.Usuario;
import com.sot.fenix.components.models.Visita;
import com.sot.fenix.components.rest.CitaREST;
import com.sot.fenix.components.rest.VisitaREST;
import com.sot.fenix.components.services.CitaService;
import com.sot.fenix.components.services.VisitaService;
import com.sot.fenix.dao.CitaDAO;
import com.sot.fenix.dao.VisitaDAO;

import test.fenix.TestUtils;
import test.fenix.stress.template.StressTemplate;
import test.fenix.stress.template.ThreadsManager;

public class StressPaseVisita extends StressTemplate<Cita, CitaDAO, CitaService, CitaREST>{
	public static final int CITAS=10000;
	
	private ThreadsManager threads;
	private static final int THREADS=4;

	
	private List<Cita> citasCache=new ArrayList<Cita>();
	
	@Autowired
	private VisitaService visitas;
	
	@After
	public void  drop(){
		super.drop();
		visitas.getDAO().deleteAll();
	}

	@Override
	public String getRestURL() {
		return "cita";
	}

	@Override
	public Cita getModel() {
		return null;
	}

	@Override
	public int getCentrosSize() {
		return 100;
	}

	@Override
	public int getUsuariosPorCentro() {
		return 10;
	}
	
	
	@Test
	public void paseVisita()throws Exception{
		crearCitas();

		ThreadsManager<Cita> threads=new ThreadsManager<Cita>() {
			@Override
			public List<Cita> getAllModels() {
				return citasCache;
			}

			@Override
			public int getMaxThreads() {
				return 1;
			}

			@Override
			public void onFinish(long time) throws AssertionError{
				assertTrue(time<100000);//Debe ejecutarse en menos de 25segundos
				comprobarIntegridadFacturas();
			}

			@Override
			public void onDoAction(Cita model) throws Exception {
				pasarVisita(model);
			}
		};
		
		threads.start();
	}
	
	@Test
	public void paseVisitaMultiThreading()throws Exception{		
		
		crearCitas();

		ThreadsManager<Cita> threads=new ThreadsManager<Cita>() {
			@Override
			public List<Cita> getAllModels() {
				return citasCache;
			}

			@Override
			public int getMaxThreads() {
				return THREADS;
			}

			@Override
			public void onFinish(long time)throws AssertionError {
				assertTrue(time<40000);//Debe ejecutarse en menos de 25segundos
				comprobarIntegridadFacturas();
			}

			@Override
			public void onDoAction(Cita model) throws Exception {
				pasarVisita(model);
			}
		};
		
		threads.start();
	}
	
	private void comprobarIntegridadFacturas(){
		for(Entry<String, Centro> e:centrosCache.entrySet()){
			List<Visita> visitas=this.visitas.getDAO().findByCentro_idOrderByFacturacion_Factura_idFacturaAsc(e.getValue().getId());
			for(int i=0;i<visitas.size();i++){
				//System.out.println("Factura "+e.getValue().getNombre()+" "+visitas.get(i).getNumFactura()+"=="+(i+1));				
				assertTrue(visitas.get(i).getNumFactura()==(i+1));
			}
		}
	}
	
	private void crearCitas(){
		System.out.println("Creando citas");
		for(int i=0;i<CITAS;i++){
			Centro c=getCentro(getRandom(getCentrosSize()));
			Usuario u=getUsuario(c,getUsuariosPorCentro());
			
			Cita cita=TestUtils.getSavedCita(service.getDAO(), cliente, prestacion, c, u, i+10);
			citasCache.add(cita);
		}
		System.out.println("Citas creadas");
	}
	
	
	private void pasarVisita(Cita cita)throws Exception{
		Cita c=new Cita();
		c.setId(cita.getId());
		c.setCentro(cita.getCentro());
		
	    mockMvc.perform(post("/"+getRestURL()+"/capturar").session(getSession()).contentType(TestUtils.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonBytes(c)))	    		
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.cod").value(ResponseJSON.OK));
	   
	    Visita v=visitas.getByCita(c);
	    assertTrue(v!=null);
	    assertTrue(v.getCita().getJsonId().equals(c.getJsonId()));
	}

}
