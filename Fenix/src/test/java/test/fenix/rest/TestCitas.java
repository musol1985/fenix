package test.fenix.rest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.sot.fenix.components.json.ResponseJSON;
import com.sot.fenix.components.models.Centro;
import com.sot.fenix.components.models.Centro.TIPO;
import com.sot.fenix.components.models.Cita;
import com.sot.fenix.components.models.Cita.ESTADO;
import com.sot.fenix.components.models.Perfil.PERFILES;
import com.sot.fenix.components.models.Prestacion;
import com.sot.fenix.components.models.Ubicacion;
import com.sot.fenix.components.models.Usuario;
import com.sot.fenix.components.rest.CitaREST;
import com.sot.fenix.components.services.CentroService;
import com.sot.fenix.components.services.CitaService;
import com.sot.fenix.components.services.ClienteService;
import com.sot.fenix.components.services.PrestacionService;
import com.sot.fenix.components.services.UsuarioService;
import com.sot.fenix.config.AppConfig;
import com.sot.fenix.config.SecurityConfig;

import test.fenix.TestUtils;
import test.fenix.config.TestDBConfig;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, TestDBConfig.class, SecurityConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestCitas {
	@Autowired
    private CitaREST rest;
	
	@Autowired
	private UsuarioService usuarios;
    @Autowired
    private PrestacionService prestaciones;
    @Autowired
    private CentroService centros;
    @Autowired
    private ClienteService clientes;
    @Autowired
    private CitaService citas;
	
    private Prestacion prestacion;
	private Centro centro;
	private Usuario usuario;
	
	private MockMvc mockMvc;
    
	@Before
	public void create() {
		mockMvc= MockMvcBuilders.standaloneSetup(rest).build();

		centro=getCentro("Centro test");
		
		centros.getDAO().save(centro);
		
		prestacion=new Prestacion();
		prestacion.setNombre("Test Prestacion");
		prestacion.setCentro(centro);
		
		prestaciones.getDAO().save(prestacion);
		
		usuario=getUsuario("Usuario 1");
		
		
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
	
	private Date getDate(int offset){
		Calendar date = Calendar.getInstance();
		long t= date.getTimeInMillis();
		return new Date(t + (offset * 60000));
	}
	
	
	
	@After
	public void  drop(){
		prestaciones.getDAO().deleteAll();
		centros.getDAO().deleteAll();
		citas.getDAO().deleteAll();
		usuarios.getDAO().deleteAll();
		clientes.getDAO().deleteAll();
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
	
	@Test
	public void nuevo() throws Exception {
		Cita c=new Cita();
		c.setCentro(centro);
		c.setFechaIni(toDate("30/08/2016 14:30"));
		c.setFechaFin(toDate("30/08/2016 14:50"));
		/*c.setPrestacion(prestacion);
		c.setProfesional(usuario);*/
		c.setEstado(ESTADO.PROGRAMADA);
		
	    mockMvc.perform(MockMvcRequestBuilders.put("/cita")
	    		.contentType(TestUtils.APPLICATION_JSON_UTF8)
	    		.content(TestUtils.convertObjectToJsonBytes(c)))
	    	.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.cod").value(ResponseJSON.OK))
			.andExpect(jsonPath("$.data.estado").value(ESTADO.PROGRAMADA.name()));

	}
	
	
	@Test
	public void get() throws Exception {
		List<Cita> citas=new ArrayList<Cita>();
		for(int i=0;i<10;i++)
			citas.add(getCita(10*i));
		
		this.citas.getDAO().save(citas);
		
	    mockMvc.perform(MockMvcRequestBuilders.get("/cita/"+centro.getId().toHexString()+"/"+fromDate(citas.get(0).getFechaIni())+"/"+fromDate(citas.get(6).getFechaIni())))
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("$.data[0].id").value(prestacion.getId().toHexString()));	
		

	}

}
