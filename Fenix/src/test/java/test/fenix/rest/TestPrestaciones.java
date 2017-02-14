package test.fenix.rest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import com.sot.fenix.components.models.Prestacion;
import com.sot.fenix.components.rest.PrestacionREST;
import com.sot.fenix.components.services.PrestacionService;
import com.sot.fenix.config.AppConfig;
import com.sot.fenix.config.SecurityConfig;
import com.sot.fenix.dao.PrestacionDAO;

import test.fenix.config.TestDBConfig;
import test.fenix.rest.templates.MantenimientosTemplateREST;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, TestDBConfig.class, SecurityConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestPrestaciones extends MantenimientosTemplateREST<Prestacion, PrestacionDAO, PrestacionService, PrestacionREST>{


	@Override
	public String getRestURL() {
		return "prestacion";
	}

	@Override
	public Prestacion getModel() {
		return new Prestacion();
	}

	@Override
	public Prestacion getModelTestModificar(Prestacion modelAModificar) {
		modelAModificar.setNombre("prestacion modificada");
		return modelAModificar;
	}

	@Override
	public void postTestModificar(ResultActions res)throws Exception {
		res.andExpect(jsonPath("$.data.nombre").value("prestacion modificada"));
	}
	
}
