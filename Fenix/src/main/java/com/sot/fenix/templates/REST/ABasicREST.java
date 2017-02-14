package com.sot.fenix.templates.REST;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sot.fenix.components.json.ResponseJSON;
import com.sot.fenix.components.models.templates.AModelId;
import com.sot.fenix.components.rest.CentroREST;
import com.sot.fenix.templates.dao.IBasicIdDAO;
import com.sot.fenix.templates.service.ABasicService;


public class ABasicREST<S extends ABasicService<D>, I extends AModelId, D extends IBasicIdDAO<I>>{
	final static Logger log = LogManager.getLogger(ABasicREST.class);
	
	@Autowired
	protected S service;
	
	@RequestMapping(method=RequestMethod.GET, path="{id}")
	//@Secured(value="ROLE_ADMIN")
    public ResponseJSON<I> getById(@PathVariable String id) {

		log.debug("Obteniendo by id "+id);
		I item=(I) service.getDAO().findOne(new ObjectId(id));	
		if(item!=null){
			return new ResponseJSON<I>(ResponseJSON.OK, item);
		}else{
			return new ResponseJSON<I>(ResponseJSON.NO_EXISTE);
		}
    }

	
	@RequestMapping(method=RequestMethod.POST)
	//@Secured(value="ROLE_ADMIN")
    public ResponseJSON<I> modificar(@RequestBody I item) {
		log.debug("Modificando "+item);
		if(service.getDAO().findOne(item.getId())==null)
			return new ResponseJSON<I>(ResponseJSON.NO_EXISTE);

		service.getDAO().save(item);
		log.debug("Modificado "+item);
		
		return new ResponseJSON<I>(ResponseJSON.OK, item);
    }
	
	@RequestMapping(method=RequestMethod.DELETE, path="/{id}")
	//@Secured(value="ROLE_ADMIN")
    public ResponseJSON<I> eliminar(@PathVariable String id) {
		I item=service.getDAO().findOne(new ObjectId(id));
		if(item!=null){
			service.getDAO().delete(item);
			return new ResponseJSON<I>(ResponseJSON.OK);
		}else{
			return new ResponseJSON<I>(ResponseJSON.NO_EXISTE);
		}
    }
	
	
	
}

