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
import com.sot.fenix.components.models.Cita;
import com.sot.fenix.components.models.Cita.ESTADO;
import com.sot.fenix.components.models.Cliente;
import com.sot.fenix.components.models.Perfil.PERFILES;
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

		centro=getCentro("Centro test");
		
		centros.getDAO().save(centro);
		
		prestacion=new Prestacion();
		prestacion.setNombre("Test Prestacion");
		prestacion.setCentro(centro);
		
		prestaciones.getDAO().save(prestacion);
		
		usuario=getUsuario("Usuario 1");
		usuarios.getDAO().save(usuario);
		
		cliente=getCliente();
		clientes.getDAO().save(cliente);
		
	}
	
	private Usuario getUsuario(String nombre){
		Usuario usuario=new Usuario();
		usuario.setCorreo("test"+nombre);
		usuario.setNombre(nombre);
		usuario.setPassword("pass");
		usuario.setPerfil(PERFILES.ROOT);
		usuario.setCentro(centro);
		
		return usuario;		
	}
	
	private Centro getCentro(String nombre){
		Centro centro=new Centro();
		centro.setColor("purple");
		centro.setCorreoAdmin("test@test.com");
		centro.setNombre(nombre);
		centro.setTipo(TIPO.SANIDAD);
		Ubicacion u=getUbicacion();
		centro.setUbicacion(u);
		return centro;
	}
	
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
	
	private Cita getCita(int offset){
		Cita c=new Cita();
		c.setCentro(centro);
		c.setFechaIni(getDate(offset));
		c.setFechaFin(getDate(offset+30));
		c.setEstado(ESTADO.PROGRAMADA);
		return c;
		
	}
	
	private Cita getSavedCita(Date fechaIni, Date fechaFin){
		Cita c=new Cita();
		c.setCentro(centro);
		c.setFechaIni(fechaIni);
		c.setFechaFin(fechaFin);
		c.setCliente(cliente);
		c.setPrestacion(prestacion);
		c.setProfesional(usuario);
		c.setEstado(ESTADO.PROGRAMADA);
		c.setImporte(100f);
		citas.getDAO().save(c);
		return c;
		
	}
	
	private Date getDate(int offset){
		Calendar date = Calendar.getInstance();
		long t= date.getTimeInMillis();
		return new Date(t + (offset * 60000));
	}
	
	private Cliente getCliente(){
		Cliente c3=new Cliente();
		c3.setId(null);
		c3.setNombre("juan2");
		c3.setApellidos("Martinc3");
		c3.setCentro(centro);
		c3.setCorreo("juan@gmail.com");
		c3.setDni("123");
		c3.setTelefono("6123");
		return c3;
	}
	
	
	
	@After
	public void  drop(){
		prestaciones.getDAO().deleteAll();
		centros.getDAO().deleteAll();
		citas.getDAO().deleteAll();
		usuarios.getDAO().deleteAll();
		clientes.getDAO().deleteAll();
		visitas.getDAO().deleteAll();
	}
	
	public static Date toDate(String date){
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		try {
			return formatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String fromDate(Date date){
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
   
		return df.format(date);
	}
	
	public static String fromDateCalendar(Date date){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
   
		return df.format(date);
	}
	
	private List<Visita> test2Crear(boolean autofacturar, boolean simularError)throws Exception{
		List<Cita> listaCitas=new ArrayList<Cita>();
		
		Cita c1=getSavedCita(toDate("30/08/2016 14:30"), toDate("30/08/2016 14:50"));
		for(int i=0;i<5;i++){
			listaCitas.add(getSavedCita(toDate("30/08/2016 1"+i+":00"), toDate("30/08/2016 1"+i+":10")));
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
			listaVisitasPost.add(visitas.nuevaVisitaFromCita(getSavedCita(toDate("23/08/2016 1"+i+":00"), toDate("23/08/2016 1"+i+":10"))));
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
