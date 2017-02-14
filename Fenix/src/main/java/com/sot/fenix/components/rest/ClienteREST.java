package com.sot.fenix.components.rest;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sot.fenix.components.json.ClienteRequest;
import com.sot.fenix.components.json.ResponseJSON;
import com.sot.fenix.components.models.Cliente;
import com.sot.fenix.components.services.ClienteService;
import com.sot.fenix.dao.ClienteDAO;
import com.sot.fenix.templates.REST.ANombreCentroIdREST;

@RestController
@RequestMapping("/cliente")
public class ClienteREST extends ANombreCentroIdREST<ClienteService, Cliente, ClienteDAO>{	
	
	@RequestMapping(method=RequestMethod.PUT)
    public ResponseJSON<Cliente> nuevo(@RequestBody Cliente cliente) {
		Cliente c=service.getDAO().findByCentro_idAndDni(cliente.getCentro().getId(), cliente.getDni());
		if(c==null){
			service.getDAO().save(cliente);
			return new ResponseJSON<Cliente>(ResponseJSON.OK, cliente);
		}else{
			return new ResponseJSON<Cliente>(ResponseJSON.YA_EXISTE, c);
		}		
    }
	
	/*
	@RequestMapping(method=RequestMethod.POST)
    public ResponseJSON<Cliente> modificar(@RequestBody Cliente cliente) {
		Cliente c=service.getDAO().findOne(cliente.getId());
		if(c!=null){
			service.getDAO().save(cliente);
			return new ResponseJSON<Cliente>(ResponseJSON.OK, cliente);
		}else{
			return new ResponseJSON<Cliente>(ResponseJSON.NO_EXISTE);
		}		
    }*/
	
	@RequestMapping(method=RequestMethod.POST, path="buscar")
    public List<Cliente> getByCentro(@RequestBody ClienteRequest request) {
		long t=System.currentTimeMillis();
		List<Cliente>res= service.buscar(request.texto, request.centro);
		System.out.println("------>"+(System.currentTimeMillis()-t));
		
		return res;
    }
	
	@Override
	@RequestMapping(method=RequestMethod.DELETE, path="/{id}")
	//@Secured(value="ROLE_ADMIN")
    public ResponseJSON<Cliente> eliminar(@PathVariable String id){
		return new ResponseJSON<Cliente>(ResponseJSON.ACCION_PROHIBIDA_REST);
	}
}

