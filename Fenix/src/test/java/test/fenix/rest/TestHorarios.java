package test.fenix.rest;

import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.sot.fenix.components.models.horarios.Horario;
import com.sot.fenix.components.rest.HorarioREST;
import com.sot.fenix.components.services.HorarioService;
import com.sot.fenix.config.AppConfig;
import com.sot.fenix.config.SecurityConfig;

import test.fenix.config.TestDBConfig;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, TestDBConfig.class, SecurityConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestHorarios extends TestTemplateREST<HorarioREST, HorarioService, Horario>{

	@Override
	public Horario getItem() {
		return new Horario();
	}

	@Override
	public String getRestURL() {
		return "horario";
	}
	
}
