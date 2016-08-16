package test.fenix.rest;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.sot.fenix.components.json.NuevoCentroJSON;
import com.sot.fenix.components.json.NuevoPendienteJSON;
import com.sot.fenix.components.json.PosicionJSON;
import com.sot.fenix.components.json.RegistrarJSON;
import com.sot.fenix.components.json.ResponseJSON;
import com.sot.fenix.components.models.Centro;
import com.sot.fenix.components.models.Centro.TIPO;
import com.sot.fenix.components.models.Perfil.PERFILES;
import com.sot.fenix.components.models.Ubicacion;
import com.sot.fenix.components.models.Usuario;
import com.sot.fenix.components.models.UsuarioPendiente;
import com.sot.fenix.components.rest.CentroREST;
import com.sot.fenix.components.services.CentroService;
import com.sot.fenix.components.services.UsuarioService;
import com.sot.fenix.config.AppConfig;
import com.sot.fenix.config.SecurityConfig;

import test.fenix.TestUtils;
import test.fenix.config.TestDBConfig;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, TestDBConfig.class, SecurityConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestCentros {
	@Autowired
    private CentroREST centroREST;
	
    @Autowired
    private UsuarioService usuarios;
    @Autowired
    private CentroService centros;

	
	private Usuario usuario;
	private Centro centro;
	
	private MockMvc mockMvc;
    
	@Before
	public void create() {
		mockMvc= MockMvcBuilders.standaloneSetup(centroREST).build();

		centro=new Centro();
		centro.setColor("purple");
		centro.setCorreoAdmin("test@test.com");
		centro.setNombre("Centro test");
		centro.setTipo(TIPO.SANIDAD);
		Ubicacion u=getUbicacion();
		centro.setUbicacion(u);
		
		centros.getDAO().save(centro);
		
		usuario=new Usuario();
		usuario.setCorreo("test");
		usuario.setNombre("test");
		usuario.setPassword("pass");
		usuario.setPerfil(PERFILES.ROOT);
		usuario.setCentro(centro);
		
		usuarios.getDAO().insert(usuario);
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
	
	
	
	@After
	public void  drop(){
		usuarios.getDAO().deleteAll();
		centros.getDAO().deleteAll();
		usuarios.getPendientesDAO().deleteAll();
	}
	
	
	
	@Test
	public void nuevo() throws Exception {
		NuevoCentroJSON request=new NuevoCentroJSON();
		
		Centro c=new Centro();
		c.setCorreoAdmin("test@test.com");
		c.setNombre("Centro test");
		c.setTipo(TIPO.SANIDAD);
		Ubicacion u=getUbicacion();
		u.setPosicion(null);
		c.setUbicacion(u);
		request.centro=c;
		request.nombreAdmin="Nombre test";
		request.posicion=new PosicionJSON();
		request.posicion.lat=1;
		request.posicion.lng=2;
		
		
	    mockMvc.perform(post("/centro/nuevo").contentType(TestUtils.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonBytes(request)))
		.andExpect(status().isOk())
		.andDo(print())
		.andExpect(jsonPath("$.cod").value(ResponseJSON.OK))
		.andExpect(jsonPath("$.data.nombre").value("Centro test"));

	}
	
	
	@Test
	public void getAll() throws Exception {

	    mockMvc.perform(get("/centro/all"))
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("$.data[0].id").value(centro.getId().toHexString()));
	}
	
}
