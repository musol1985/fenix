package com.sot.fenix.templates.REST;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sot.fenix.components.json.PageJSON;
import com.sot.fenix.components.json.ResponseJSON;
import com.sot.fenix.components.json.ResponseListJSON;
import com.sot.fenix.components.models.templates.AModelCentro;
import com.sot.fenix.templates.dao.ICentroIdDAO;
import com.sot.fenix.templates.service.ACentroIdService;


public class ACentroIdREST<S extends ACentroIdService<D, I>, I extends AModelCentro, D extends ICentroIdDAO<I>> extends ABasicREST<S,I,D>{


	@RequestMapping(method=RequestMethod.GET, path="{centro}/{page}/{size}")
	//@Secured(value="ROLE_ADMIN")
    public PageJSON<I> getByCentro(@PathVariable int page, @PathVariable int size, @PathVariable String centro) {

		Page<I> items=service.getByCentro(centro, new PageRequest(page-1, size));		

		return new PageJSON<I>(items.getSize(), items.getTotalPages(), items.getContent());
    }
	
	@RequestMapping(method=RequestMethod.GET, path="all/{centro}")
	//@Secured(value="ROLE_ADMIN")
    public ResponseListJSON<I> getAllByCentro(@PathVariable String centro) {

		List<I> items=service.getDAO().findByCentro_id(new ObjectId(centro));

		return new ResponseListJSON<I>(ResponseJSON.OK, items);
    }
	
	
}

