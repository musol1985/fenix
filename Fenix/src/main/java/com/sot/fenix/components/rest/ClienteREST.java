package com.sot.fenix.components.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

		clientes.getDAO().save(cliente);
		
		return new ResponseJSON<Cliente>(ResponseJSON.OK, cliente);		
    }
	
	@RequestMapping(method=RequestMethod.GET, path="{texto}")
    public List<Cliente> getByCentro(@PathVariable String texto) {
		
		return clientes.buscar(texto);
    }
}

