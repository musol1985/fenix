


package test.fenix.rest.templates;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.sot.fenix.components.models.Cliente;
import com.sot.fenix.components.models.Prestacion;
import com.sot.fenix.components.models.templates.AModelCentro;
import com.sot.fenix.components.services.ClienteService;
import com.sot.fenix.components.services.PrestacionService;
import com.sot.fenix.config.AppConfig;
import com.sot.fenix.config.SecurityConfig;
import com.sot.fenix.templates.REST.ACentroIdREST;
import com.sot.fenix.templates.dao.ICentroIdDAO;
import com.sot.fenix.templates.service.ACentroIdService;

import test.fenix.TestUtils;
import test.fenix.config.TestDBConfig;
/**
 * Template para los tests de flujo de acciones de un centro(facturar, citar, pasar visita, etc.)
 * Inicializa prestacion y cliente
 * @author eduarmar
 *
 * @param <I> Model que se va a testear(Cita, Visita..)
 * @param <R> Rest del model que se va a testear
 * @param <D> Dao del model que se va a testear
 * @param <S> Service del model que se va a testear

 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, TestDBConfig.class, SecurityConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public abstract class CentroBasicTemplateREST<I extends AModelCentro, D extends ICentroIdDAO<I>, S extends ACentroIdService<D, I>, R extends ACentroIdREST<S,I,D>> extends TestTemplateREST<I,D,S,R> {
	@Autowired
	protected PrestacionService prestaciones;
    @Autowired
    protected ClienteService clientes;
    
    
    protected Prestacion prestacion;
    protected Cliente cliente;

    @Before
	public void create() {
    	super.create();
    	
		prestacion=new Prestacion();
		prestacion.setNombre("Test Prestacion");
		prestacion.setCentro(centro);
		
		prestaciones.getDAO().save(prestacion);
		
		cliente=TestUtils.getNewCliente(centro);
		clientes.getDAO().save(cliente);
	}
    
    
	@After
	public void  drop(){
		super.drop();
		prestaciones.getDAO().deleteAll();
		clientes.getDAO().deleteAll();
	}
	
}
