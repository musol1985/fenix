package test.fenix.stress.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import com.sot.fenix.components.json.ResponseJSON;
import com.sot.fenix.components.models.Centro;
import com.sot.fenix.components.models.Usuario;
import com.sot.fenix.components.models.templates.AModelCentro;
import com.sot.fenix.config.AppConfig;
import com.sot.fenix.config.SecurityConfig;
import com.sot.fenix.templates.REST.ACentroIdREST;
import com.sot.fenix.templates.dao.ICentroIdDAO;
import com.sot.fenix.templates.service.ACentroIdService;

import test.fenix.TestUtils;
import test.fenix.config.TestDBConfig;
import test.fenix.rest.templates.CentroBasicTemplateREST;

/**
 * Implementación basica de los tests de REST
 * inicializa centro y usuario
 * @author eduarmar
 *
 * @param <T>
 * @param <S>
 * @param <I>
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, TestDBConfig.class, SecurityConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public abstract class StressTemplate<I extends AModelCentro, D extends ICentroIdDAO<I>, S extends ACentroIdService<D, I>, R extends ACentroIdREST<S,I,D>> extends CentroBasicTemplateREST<I,D,S,R>{
	   
    
    protected HashMap<String, Centro> centrosCache=new HashMap<String, Centro>();
    protected HashMap<String, List<Usuario>> usuariosCache=new HashMap<String, List<Usuario>>();
    protected Random rand=new Random();

    protected int getRandom(int max){
    	return rand.nextInt(max);
    }
    
    protected Centro getCentro(int num){
    	return centrosCache.get("Centro test"+num);
    }
    
    protected Usuario getUsuario(Centro centro, int num){
    	return usuariosCache.get(centro.getNombre()).get(num);
    }
    
	@Before
	public void create() {
		super.create();
		
		System.out.println("Creando "+getCentrosSize()+"centros");
		for(int i=0;i<getCentrosSize();i++){
			Centro centro=TestUtils.getNewCentro();
			centro.setCorreoAdmin("test@gmail.com"+i);
			centro.setNombre(centro.getNombre()+i);
			centros.getDAO().save(centro);
			
			config.iniciarSequenciasCentro(centro);
			
			centrosCache.put(centro.getNombre(), centro);
			
			List<Usuario> usuarioCacheList=new ArrayList<Usuario>();
			usuariosCache.put(centro.getNombre(), usuarioCacheList);
			
			Usuario usuario=TestUtils.getNewUsuario(String.valueOf(i), centro);
			usuarios.getDAO().save(usuario);
			usuarioCacheList.add(usuario);
			
			//System.out.println("Creando "+getUsuariosPorCentro()+" usuarios para el centro "+centro.getNombre());
			for(int e=0;e<getUsuariosPorCentro();e++){
				usuario=TestUtils.getNewUsuarioNoRoot("noRoot@"+centro.getNombre()+e, centro);
				usuarios.getDAO().save(usuario);
				
				usuarioCacheList.add(usuario);
			}
			//System.out.println("Usuarios creados");
		}
		System.out.println("Centros creados");

	}
    
	public abstract int getCentrosSize();
	public abstract int getUsuariosPorCentro();
	

	
	public abstract String getRestURL();

	
	@Test
	public void get() throws Exception {		

	}
	
	
	public int getModificarCode(){
		return ResponseJSON.OK;
	}
	
	public int getDeleteCode(){
		return ResponseJSON.OK;
	}
	
	/**
	 * Metodo para modificar un model para el test de modificar(por ejemplo cambiarle el nombre)
	 * @param modelAModificar
	 * @return
	 */
	public  I getModelTestModificar(I modelAModificar){
		return null;
	}
	
	/**
	 * Metodo que se ejecuta en el test modificar, y que sirve para comprobar el resultado de la modificacion
	 * @param res
	 */
	public void postTestModificar(ResultActions res)throws Exception{
		
	}

	@Test
	public void modificar() throws Exception {	

	}

	
	@Test
	public void delete() throws Exception {		

	}
	
	
}
