package test.fenix.stress;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.sot.fenix.components.json.CitaRequestJSON;
import com.sot.fenix.components.json.ResponseJSON;
import com.sot.fenix.components.models.Centro;
import com.sot.fenix.components.models.Usuario;
import com.sot.fenix.components.models.Visita;
import com.sot.fenix.components.models.citacion.Cita;
import com.sot.fenix.components.rest.CitaREST;
import com.sot.fenix.components.services.CitaService;
import com.sot.fenix.dao.CitaDAO;

import test.fenix.TestUtils;
import test.fenix.stress.template.StressTemplate;
import test.fenix.stress.template.ThreadsManager;

public class StressCitas extends StressTemplate<Cita, CitaDAO, CitaService, CitaREST>{
	public static final int CITAS=2000;
	private static final int THREADS=8;
	
	public static final long DIA_MS=1000*60*60*24;
	
	private List<Cita> citasCache=new ArrayList<Cita>();
	
	@After
	public void  drop(){
		super.drop();	
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
	public void citarMultiThreading()throws Exception{		
		
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
				assertTrue(time<100000);//Debe ejecutarse en menos de 25segundos
				//comprobarIntegridadFacturas();
			}

			@Override
			public void onDoAction(Cita model) throws Exception {
				citar(model);
			}
		};
		
		threads.start();
	}
	
	
	public void crearCitas()throws Exception{
		Date f=new Date(new Date().getTime()-DIA_MS*10);
		for(int i=0;i<CITAS;i++){
			citasCache.add(getCita(f, i*10, 5));
		}
	}
	
	private void citar(Cita c)throws Exception{
		CitaRequestJSON r=new CitaRequestJSON();
		r.setCita(c);
			
		performPUT("/cita", r)
			.andExpect(jsonPath("$.cod").value(ResponseJSON.OK));
	}
	
	private Cita getCita(Date fecha, int offset, int size){
		return TestUtils.getSavedCita(null, centro, cliente, prestacion, usuario, addMinutesToDate(offset, fecha), addMinutesToDate(offset+size, fecha));
	}
	
	private static Date addMinutesToDate(int minutes, Date beforeTime){
	    final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs

	    long curTimeInMs = beforeTime.getTime();
	    Date afterAddingMins = new Date(curTimeInMs + (minutes * ONE_MINUTE_IN_MILLIS));
	    return afterAddingMins;
	}
	
}
