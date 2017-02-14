package test.fenix.rest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import com.sot.fenix.components.models.horarios.Horario;
import com.sot.fenix.components.rest.HorarioREST;
import com.sot.fenix.components.services.HorarioService;
import com.sot.fenix.config.AppConfig;
import com.sot.fenix.config.SecurityConfig;
import com.sot.fenix.dao.HorarioDAO;

import test.fenix.config.TestDBConfig;
import test.fenix.rest.templates.MantenimientosTemplateREST;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, TestDBConfig.class, SecurityConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestHorarios extends MantenimientosTemplateREST<Horario, HorarioDAO, HorarioService, HorarioREST>{


	@Override
	public String getRestURL() {
		return "horario";
	}

	@Override
	public Horario getModel() {
		return new Horario();
	}

	@Override
	public Horario getModelTestModificar(Horario modelAModificar) {
		modelAModificar.setNombre("HorarioModificado");
		return modelAModificar;
	}

	@Override
	public void postTestModificar(ResultActions res) throws Exception {
		res.andExpect(jsonPath("$.data.nombre").value("HorarioModificado"));
	}
	
}
