package com.sot.fenix.components.rest;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sot.fenix.components.exceptions.ExceptionREST;
import com.sot.fenix.components.json.NuevoCentroJSON;
import com.sot.fenix.components.json.PageJSON;
import com.sot.fenix.components.json.ResponseJSON;
import com.sot.fenix.components.json.ResponseListJSON;
import com.sot.fenix.components.models.Centro;
import com.sot.fenix.components.services.CentroService;
import com.sot.fenix.dao.CentroDAO;
import com.sot.fenix.templates.REST.ABasicREST;

@RestController
@RequestMapping("/centro")
public class CentroREST extends ABasicREST<CentroService, Centro, CentroDAO>{
	final static Logger log = LogManager.getLogger(CentroREST.class);

	@RequestMapping(method=RequestMethod.GET)
    public String current() {
		return "OK";     
    }
	
	@Override
	@RequestMapping(method=RequestMethod.DELETE, path="/{id}")
    public ResponseJSON<Centro> eliminar(@PathVariable String id){
		return new ResponseJSON<Centro>(ResponseJSON.ACCION_PROHIBIDA_REST);
	}

	@RequestMapping(method=RequestMethod.PUT,path="/nuevo")
    public ResponseJSON<Centro> nuevo(@RequestBody NuevoCentroJSON nuevoCentro) {
		try{
			Centro centro=service.nuevoCentro(nuevoCentro);
			return new ResponseJSON<Centro>(ResponseJSON.OK, centro);
		}catch(ExceptionREST ex){
			log.error(ex.getMessage());
			
			return (ResponseJSON<Centro>)ex.toResponse();
		}
    }
	
	
	@RequestMapping(method=RequestMethod.GET, path="/all/{page}/{size}")
    public PageJSON<Centro> getAll(@PathVariable int page, @PathVariable int size) {
		Page<Centro> centros=this.service.getDAO().findAll(new PageRequest(page-1, size));   

		return new PageJSON<Centro>(centros.getTotalElements(), centros.getContent());
    }
	
	@RequestMapping(method=RequestMethod.GET, path="/all")
    public ResponseListJSON<Centro> getAll() {
		List<Centro> centros=this.service.getDAO().findAll();   

		return new ResponseListJSON<Centro>(ResponseJSON.OK, centros);
    }

}

