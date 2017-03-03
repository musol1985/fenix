package test.fenix.stress;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.sot.fenix.components.models.Centro;
import com.sot.fenix.components.services.CentroService;
import com.sot.fenix.components.services.TestService;
import com.sot.fenix.config.AppConfig;
import com.sot.fenix.config.SecurityConfig;

import test.fenix.TestUtils;
import test.fenix.config.TestDBConfig;
import test.fenix.stress.template.ThreadsManager;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, TestDBConfig.class, SecurityConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StressTest {
	public static final int SIZE=3;
	private static final int THREADS=1;

	private Centro centro;
	
	private List<String> modelCache=new ArrayList<String>();

	@Autowired
	private TestService service;
	@Autowired
	private CentroService centros;
	
	@Before
	public void init(){
		centro=TestUtils.getNewCentro();
		centros.getDAO().save(centro);
	}
	
	@Test
	public void testMultiThreading()throws Exception{		
		
		crearTests();

		ThreadsManager<String> threads=new ThreadsManager<String>() {
			@Override
			public List<String> getAllModels() {
				return modelCache;
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
			public void onDoAction(String model) throws Exception {
				service.generarTestsModels(model, centro, 5000, "Submodel", 5000);
			}
		};
		
		threads.start();
	}
	
	
	public void crearTests()throws Exception{
		for(int i=0;i<SIZE;i++){
			modelCache.add("TestId"+i);
		}
	}
	
}
