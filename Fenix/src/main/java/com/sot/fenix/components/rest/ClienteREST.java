package com.sot.fenix.components.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sot.fenix.components.json.ClienteRequest;
import com.sot.fenix.components.json.ResponseJSON;
import com.sot.fenix.components.models.Cliente;
import com.sot.fenix.components.services.ClienteService;

@RestController
@RequestMapping("/cliente")
public class ClienteREST{
	
	@Autowired
	private ClienteService clientes;

	
	@RequestMapping(method=RequestMethod.PUT)
    public ResponseJSON<Cliente> nuevo(@RequestBody Cliente cliente) {
		Cliente c=clientes.getDAO().findByCentro_idAndDni(cliente.getCentro().getId(), cliente.getDni());
		if(c==null){
			clientes.getDAO().save(cliente);
			return new ResponseJSON<Cliente>(ResponseJSON.OK, cliente);
		}else{
			return new ResponseJSON<Cliente>(ResponseJSON.YA_EXISTE, c);
		}		
    }
	
	@RequestMapping(method=RequestMethod.POST)
    public ResponseJSON<Cliente> modificar(@RequestBody Cliente cliente) {
		Cliente c=clientes.getDAO().findOne(cliente.getId());
		if(c!=null){
			clientes.getDAO().save(cliente);
			return new ResponseJSON<Cliente>(ResponseJSON.OK, cliente);
		}else{
			return new ResponseJSON<Cliente>(ResponseJSON.NO_EXISTE);
		}		
    }
	
	@RequestMapping(method=RequestMethod.POST, path="buscar")
    public List<Cliente> getByCentro(@RequestBody ClienteRequest request) {
		long t=System.currentTimeMillis();
		List<Cliente>res= clientes.buscar(request.texto, request.centro);
		System.out.println("------>"+(System.currentTimeMillis()-t));
		
		return res;
    }
}

