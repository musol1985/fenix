package test.fenix.tasks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

import com.sot.fenix.components.listeners.DeployListener;
import com.sot.fenix.components.models.Centro;
import com.sot.fenix.components.models.Centro.TIPO;
import com.sot.fenix.components.models.Cliente;
import com.sot.fenix.components.models.Perfil.PERFILES;
import com.sot.fenix.components.models.citacion.Cita;
import com.sot.fenix.components.models.citacion.Cita.ESTADO;
import com.sot.fenix.components.models.Prestacion;
import com.sot.fenix.components.models.Ubicacion;
import com.sot.fenix.components.models.Usuario;
import com.sot.fenix.components.models.Visita;
import com.sot.fenix.components.services.CentroService;
import com.sot.fenix.components.services.CitaService;
import com.sot.fenix.components.services.ClienteService;
import com.sot.fenix.components.services.ConfigCentroService;
import com.sot.fenix.components.services.PrestacionService;
import com.sot.fenix.components.services.UsuarioService;
import com.sot.fenix.components.services.VisitaService;
import com.sot.fenix.config.AppConfig;
import com.sot.fenix.config.SecurityConfig;

import test.fenix.TestUtils;
import test.fenix.config.TestDBConfig;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, TestDBConfig.class, SecurityConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestIntegrityTask {
	final static Logger log = LogManager.getLogger(TestIntegrityTask.class);
	
    @Autowired
    private ConfigCentroService config;
    @Autowired
    private VisitaService visitas;

	@Autowired
	private DeployListener task;
	

	@Autowired
	private UsuarioService usuarios;
    @Autowired
    private PrestacionService prestaciones;
    @Autowired
    private ClienteService clientes;
    @Autowired
    private CitaService citas;
    @Autowired
    private CentroService centros;

	
    private Prestacion prestacion;
	private Centro centro;
	private Usuario usuario;
	private Cliente cliente;

    
	@Before
	public void create() {

		centro=TestUtils.getNewCentro();		
		centros.getDAO().save(centro);
		
		prestacion=TestUtils.getNewPrestacion(centro);		
		prestaciones.getDAO().save(prestacion);
		
		usuario=TestUtils.getNewUsuario("nuevoUsuario", centro);
		usuarios.getDAO().save(usuario);
		
		cliente=TestUtils.getNewCliente(centro);
		clientes.getDAO().save(cliente);
		
	}
	
	@After
	public void  drop(){
		prestaciones.getDAO().deleteAll();
		centros.getDAO().deleteAll();
		citas.getDAO().deleteAll();
		usuarios.getDAO().deleteAll();
		clientes.getDAO().deleteAll();
		visitas.getDAO().deleteAll();
		config.getDAO().deleteAll();
	}
	
	
	private List<Visita> test2Crear(boolean autofacturar, boolean simularError)throws Exception{
		List<Cita> listaCitas=new ArrayList<Cita>();
		Cita c1=TestUtils.getSavedCita(citas.getDAO(), centro, cliente, prestacion, usuario, TestUtils.toDate("30/08/2016 14:30"), TestUtils.toDate("30/08/2016 14:50"));		
		
		for(int i=0;i<5;i++){
			listaCitas.add(TestUtils.getSavedCita(citas.getDAO(), centro, cliente, prestacion, usuario, TestUtils.toDate("30/08/2016 1"+i+":00"), TestUtils.toDate("30/08/2016 1"+i+":10")));
		}		
		
		Visita v1=visitas.nuevaVisitaFromCita(c1);	
		
		//Desactivamos la generacion auto de facturas
		config.setValue(centro, ConfigCentroService.AUTO_FACTURAR, autofacturar);
		
		List<Visita> listaVisitas=new ArrayList<Visita>();
		
		for(Cita c:listaCitas){
			listaVisitas.add(visitas.nuevaVisitaFromCita(c));
		}

		if(simularError)
			for(Visita v:listaVisitas){
				simularErrorFacturaPre(v);
			}
		
		return listaVisitas;
	}
	
	private List<Visita> generarVisitasPost(int size)throws Exception{
		List<Visita> listaVisitasPost=new ArrayList<Visita>();
		
		for(int i=0;i<size;i++){
			listaVisitasPost.add(visitas.nuevaVisitaFromCita(TestUtils.getSavedCita(citas.getDAO(), centro, cliente, prestacion, usuario,TestUtils.toDate("23/08/2016 1"+i+":00"), TestUtils.toDate("23/08/2016 1"+i+":10"))));
		}
		return listaVisitasPost;
	}
	
	private List<Visita> actualizrFromBD(List<Visita> listaVisitas)throws Exception{
		List<Visita> listaVisitasActualizadas=new ArrayList<Visita>();
		
		for(Visita v:listaVisitas){
			listaVisitasActualizadas.add(visitas.getDAO().findOne(v.getId()));
		}
		return listaVisitasActualizadas;
	}
	
	private void comprobacion(List<Visita> listaVisitasActualizadas)throws Exception{
		//Comprobamos que tengan num de factura
		for(Visita v:listaVisitasActualizadas){
			assertTrue(v.getNumFactura()>0);
		}

		//Comprobamos que no haya repetidos
		for(Visita v:listaVisitasActualizadas){
			for(Visita v2:listaVisitasActualizadas){
				if(v2!=v){
					assertTrue((v.getNumFactura()!=v2.getNumFactura()));
				}
			}
		}
		
		long seqIntegrity=config.getNumFacturaIntegrity(centro);
		long seqFactura=config.getSequence(centro.getId(), ConfigCentroService.SEQ_FACTURA);
		assertEquals(seqIntegrity, seqFactura);	
	}
	
	@Test
	/**
	 * Genera 3 bien falla en 2 y genera otras 2 bien, 
	 * las que falla se quita la factura(la sequence ya ha hecho next)
	 * @throws Exception
	 */
	public void testIntegrityFacturas1()throws Exception{
		List<Visita> listaVisitas=test2Crear(true, false);
		
		//La 4 y 5 se les quita la factura(el estado ya se ha puesto
		simularErrorFactura(listaVisitas.get(3));
		simularErrorFactura(listaVisitas.get(4));	
		
		List<Visita> listaVisitasPost=generarVisitasPost(2);
		
		task.contextRefreshedEvent();
		
		listaVisitas.addAll(listaVisitasPost);//Añadimos las post a las que se tienen que revisar
		List<Visita> listaVisitasActualizadas=actualizrFromBD(listaVisitas);

		comprobacion(listaVisitasActualizadas);
	}
	
	/**
	 * Genera 1 bien y luego genera 4 con el generar factura=false
	 * por lo que es el caso de que falla antes de obtener la sequence(factura.estado=facturando)
	 * @throws Exception
	 */
	@Test
	public void testIntegrityFacturas2()throws Exception{		
		List<Visita> listaVisitas=test2Crear(false, true);
		
		task.contextRefreshedEvent();
		
		List<Visita> listaVisitasActualizadas=actualizrFromBD(listaVisitas);
		
		comprobacion(listaVisitasActualizadas);
	}
	
	
	/**
	 * Como el 2 pero aparte, despues de los errores, se genera 5 correctas(es decir errores entre medio)
	 * @throws Exception
	 */
	@Test
	public void testIntegrityFacturas3Multi()throws Exception{		
		List<Visita> listaVisitas=test2Crear(false, true);
		
		//Activamos, simulando que se han creado citas y facturado correctamente(threads mas lentos que otros)
		config.setValue(centro, ConfigCentroService.AUTO_FACTURAR, true);
		
		List<Visita> listaVisitasPost=generarVisitasPost(5);
		
		task.contextRefreshedEvent();
		
		listaVisitas.addAll(listaVisitasPost);//Añadimos las post a las que se tienen que revisar
		List<Visita> listaVisitasActualizadas=actualizrFromBD(listaVisitas);

		comprobacion(listaVisitasActualizadas);
	}
	
	/**
	 * Mezcla de 1 y 3
	 * Genera 1 bien y luego genera 4 con el generar factura=false
	 * por lo que es el caso de que falla antes de obtener la sequence(factura.estado=facturando)
	 * Luego gneera 2 bien + 3 bien
	 * y genera 2 mal (se le quita factura, por lo que la sequence ha sido aumentada)
	 * y genera 2 bien
	 * @throws Exception
	 */
	@Test
	public void testIntegrityTotal()throws Exception{		
		List<Visita> listaVisitasTest3=test2Crear(false, true);
		
		//Activamos, simulando que se han creado citas y facturado correctamente(threads mas lentos que otros)
		config.setValue(centro, ConfigCentroService.AUTO_FACTURAR, true);
		
		List<Visita> listaVisitasTest3Post=generarVisitasPost(2);
		
		List<Visita> listaVisitasTest1=test2Crear(true, false);
		
		//La 4 y 5 se les quita la factura(el estado ya se ha puesto
		simularErrorFactura(listaVisitasTest1.get(3));
		simularErrorFactura(listaVisitasTest1.get(4));	
		
		List<Visita> listaVisitasTest1Post=generarVisitasPost(2);
		
		task.contextRefreshedEvent();
		
		List<Visita> todas=new ArrayList<Visita>();
		todas.addAll(listaVisitasTest3);
		todas.addAll(listaVisitasTest3Post);
		todas.addAll(listaVisitasTest1);
		todas.addAll(listaVisitasTest1Post);

		List<Visita> listaVisitasActualizadas=actualizrFromBD(todas);

		comprobacion(listaVisitasActualizadas);
	}
	
	/**
	 * REaliza el test total varias veces
	 * @throws Exception
	 */
	@Test
	public void testIntegrityTotalMulti()throws Exception{		
		testIntegrityTotal();
		testIntegrityTotal();
	}

	
	private void simularErrorFactura(Visita v){
		//Simulamos que la visita 2 se ha quedado sin num factura
		v.getFacturacion().setFacturando();
		v.getFacturacion().setFactura(null);
		visitas.getDAO().save(v);
	}
	
	private void simularErrorFacturaPre(Visita v){
		v.getFacturacion().setFacturando();
		visitas.getDAO().save(v);
	}
}
