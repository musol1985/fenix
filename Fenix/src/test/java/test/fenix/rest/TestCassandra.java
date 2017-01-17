package test.fenix.rest;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.sot.fenix.components.models.Usuario;
import com.sot.fenix.components.services.UsuarioService;
import com.sot.fenix.config.AppConfig;
import com.sot.fenix.config.SecurityConfig;

import test.fenix.config.TestDBConfig;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestDBConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestCassandra {
    @Autowired
    private UsuarioService usuarios;
	
	private Usuario usuario;

    
	@Test
	public void create() {
		
	}
	
	
}
