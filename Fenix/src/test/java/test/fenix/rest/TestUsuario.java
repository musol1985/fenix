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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.sot.fenix.components.json.NuevoPendienteJSON;
import com.sot.fenix.components.json.RegistrarJSON;
import com.sot.fenix.components.json.ResponseJSON;
import com.sot.fenix.components.models.Centro;
import com.sot.fenix.components.models.Centro.TIPO;
import com.sot.fenix.components.models.Perfil.PERFILES;
import com.sot.fenix.components.models.Ubicacion;
import com.sot.fenix.components.models.Usuario;
import com.sot.fenix.components.models.UsuarioPendiente;
import com.sot.fenix.components.rest.UsuarioREST;
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
public class TestUsuario {
	@Autowired
    private UsuarioREST usuariosREST;
	
    @Autowired
    private UsuarioService usuarios;
    @Autowired
    private CentroService centros;
    
    @Autowired
    private AuthenticationManager auManager;
	
	private Usuario usuario;
	private Centro centro;
	
	private MockMvc mockMvc;
    
	@Before
	public void create() {
		mockMvc= MockMvcBuilders.standaloneSetup(usuariosREST).build();

		centro=new Centro();
		centro.setColor("purple");
		centro.setCorreoAdmin("test@test.com");
		centro.setNombre("Centro test");
		centro.setTipo(TIPO.SANIDAD);
		Ubicacion u=new Ubicacion();
		u.setCalle("Calle test");
		u.setCP("08292");
		u.setId("#idUbicacion");
		u.setNumero("33");
		u.setPais("ES");
		u.setPoblacion("Terrassa");
		u.setProvincia("Barcelona");
		u.setPosicion(new GeoJsonPoint(1, 1));
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
	
	
	private UsuarioPendiente getUsuarioPendienteTest(){
		UsuarioPendiente uP=new UsuarioPendiente();
		uP.setCentro(centro);
		uP.setCorreo("test@test.com");
		uP.setNombre("TestEnviarCorreo");
		usuarios.getPendientesDAO().save(uP);
		System.out.println("Usuario Pendiente: "+uP.getId().toHexString());
		return uP;
	}
	
	private Usuario getUsuarioAdmin(){
		return getUsuarioTest(PERFILES.ADMIN);
	}
	
	private Usuario getUsuario(){
		return getUsuarioTest(PERFILES.USER);
	}
	
	private Usuario getUsuarioTest(PERFILES perfil){
		Usuario uP=new Usuario();
		uP.setPassword("passTest");
		uP.setCentro(centro);
		uP.setCorreo(perfil.name()+"@test.com");
		uP.setNombre("TestUsuario"+perfil.name());
		uP.setPerfil(perfil);
		usuarios.getDAO().save(uP);
		return uP;
	}
	
	
	@After
	public void  drop(){
		usuarios.getDAO().deleteAll();
		centros.getDAO().deleteAll();
		usuarios.getPendientesDAO().deleteAll();
	}
	
	
	@Test
    public void testInit(){
		List<Centro> list =centros.getDAO().findAll();
		assertEquals(1, list.size());
		List<Usuario> usuarios=this.usuarios.getDAO().findAll();
		assertEquals(1, usuarios.size());
	}
	
	@Test
	public void testGetById() throws Exception {
	    mockMvc.perform(get("/usuario/"+usuario.getId().toHexString()))
	    		.andExpect(status().isOk())
	    		//.andDo(print())
	    		.andExpect(jsonPath("$.id", containsString(usuario.getId().toHexString())))
	    		.andExpect(jsonPath("$.centro.id", containsString(centro.getId().toHexString())));
    }
	
	@Test
	public void nuevoPendiente() throws Exception {
		NuevoPendienteJSON request=new NuevoPendienteJSON();
		
		request.centro=centro.getId().toHexString();
		request.usuario=new UsuarioPendiente();
		request.usuario.setNombre("Edu");
		request.usuario.setCorreo("edu@gmail.com");
		
		
	    mockMvc.perform(post("/usuario/pendiente").contentType(TestUtils.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonBytes(request)))
		.andExpect(status().isOk())
		//.andDo(print())
		.andExpect(jsonPath("$.cod").value(0))
		.andExpect(jsonPath("$.data.correo", containsString("edu@gmail.com")));
	    
	    
	    mockMvc.perform(post("/usuario/pendiente").contentType(TestUtils.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonBytes(request)))
		.andExpect(status().isOk())
		//.andDo(print())
		.andExpect(jsonPath("$.cod").value(ResponseJSON.YA_EXISTE));
	}
	
	@Test
	public void enviarCorreo() throws Exception {
		UsuarioPendiente uPendiente=getUsuarioPendienteTest();
	    mockMvc.perform(get("/usuario/pendiente/correo/"+uPendiente.getId().toHexString()))
		.andExpect(status().isOk())
		//.andDo(print())
		.andExpect(jsonPath("$.cod").value(0));
	    
	    mockMvc.perform(get("/usuario/pendiente/correo/"+new ObjectId().toHexString()))
		.andExpect(status().isOk())
		//.andDo(print())
		.andExpect(jsonPath("$.cod").value(ResponseJSON.NO_EXISTE));
	}
	
	@Test
	public void eliminarPendiente() throws Exception {
		UsuarioPendiente uPendiente=getUsuarioPendienteTest();
	    mockMvc.perform(delete("/usuario/pendiente/"+uPendiente.getId().toHexString()))
		.andExpect(status().isOk())
		//.andDo(print())
		.andExpect(jsonPath("$.cod").value(0));
	    
	    mockMvc.perform(delete("/usuario/pendiente/"+uPendiente.getId().toHexString()))
		.andExpect(status().isOk())
		//.andDo(print())
		.andExpect(jsonPath("$.cod").value(ResponseJSON.NO_EXISTE));
	}
	
	@Test
	public void eliminarUsuario() throws Exception {
		Usuario u=getUsuarioAdmin();
	    mockMvc.perform(delete("/usuario/"+u.getId().toHexString()))
		.andExpect(status().isOk())
		//.andDo(print())
		.andExpect(jsonPath("$.cod").value(ResponseJSON.ES_ADMIN));
	    
	    u=getUsuario();
	    
	    mockMvc.perform(delete("/usuario/"+u.getId().toHexString()))
		.andExpect(status().isOk())
		//.andDo(print())
		.andExpect(jsonPath("$.cod").value(ResponseJSON.OK));
	    
	    
	    mockMvc.perform(delete("/usuario/"+u.getId().toHexString()))
		.andExpect(status().isOk())
		//.andDo(print())
		.andExpect(jsonPath("$.cod").value(ResponseJSON.NO_EXISTE));
	}
	
	@Test
	public void registrar() throws Exception {
		UsuarioPendiente uP=getUsuarioPendienteTest();
		RegistrarJSON request=new RegistrarJSON();
		
		request.idPendiente=uP.getId().toHexString();
		request.nombre=uP.getNombre()+"PostRegistro";
		request.password="testPassword";		
		
	    mockMvc.perform(post("/usuario/registrar").contentType(TestUtils.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonBytes(request)))
		.andExpect(status().isOk())
		//.andDo(print())
		.andExpect(jsonPath("$.cod").value(0))
		.andExpect(jsonPath("$.data.nombre", containsString(uP.getNombre()+"PostRegistro")))
		.andExpect(jsonPath("$.data.correo", containsString(uP.getCorreo())));
	    
	    Usuario u=usuarios.getUsuarioByCorreo(uP.getCorreo());

	    mockMvc.perform(get("/usuario/"+u.getId().toHexString()))
			.andExpect(status().isOk())
			//.andDo(print())
			.andExpect(jsonPath("$.correo", containsString(uP.getCorreo())));
	}

	
	@Test
	public void getByCentro() throws Exception {
	    mockMvc.perform(get("/usuario/"+centro.getId().toHexString()+"/1/10"))
			.andExpect(status().isOk())
			//.andDo(print())
			.andExpect(jsonPath("$.data[0].id").value(usuario.getId().toHexString()));
	}
	
	@Test
	public void getAllByCentro() throws Exception {
		Usuario u1=getUsuario();
		UsuarioPendiente uP=getUsuarioPendienteTest();
		
	    mockMvc.perform(get("/usuario/all/"+centro.getId().toHexString()+"/1/10"))
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("$.total").value(3));
	}
	
	@Test
	public void getCurrent() throws Exception {		  
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("test", "pass");
        Authentication authentication = auManager.authenticate(token);

	    SecurityContextHolder.getContext().setAuthentication(authentication);

	    mockMvc.perform(get("/usuario/current"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(usuario.getId().toHexString()));
	}
}
