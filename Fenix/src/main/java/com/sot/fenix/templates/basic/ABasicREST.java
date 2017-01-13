package com.sot.fenix.templates.basic;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sot.fenix.components.json.PageJSON;
import com.sot.fenix.components.json.ResponseJSON;
import com.sot.fenix.components.json.ResponseListJSON;
import com.sot.fenix.components.models.templates.AModelBasic;


public class ABasicREST<S extends ABasicService<D, I>, I extends AModelBasic, D extends IBasicDAO<I>>{
	
	@Autowired
	protected S service;

	@RequestMapping(method=RequestMethod.GET, path="{centro}/{page}/{size}")
    public PageJSON<I> getByCentro(@PathVariable int page, @PathVariable int size, @PathVariable String centro) {

		Page<I> items=service.getByCentro(centro, new PageRequest(page-1, size));		

		return new PageJSON<I>(items.getSize(), items.getTotalPages(), items.getContent());
    }
	
	@RequestMapping(method=RequestMethod.GET, path="all/{centro}")
    public ResponseListJSON<I> getAllByCentro(@PathVariable String centro) {

		List<I> items=service.getDAO().findByCentro_id(new ObjectId(centro));

		return new ResponseListJSON<I>(ResponseJSON.OK, items);
    }
	
	@RequestMapping(method=RequestMethod.GET, path="{id}")
    public ResponseJSON<I> getByCentro(@PathVariable String id) {

		I item=(I) service.getDAO().findOne(new ObjectId(id));	
		if(item!=null){
			return new ResponseJSON<I>(ResponseJSON.OK, item);
		}else{
			return new ResponseJSON<I>(ResponseJSON.NO_EXISTE);
		}
    }
	
	
	@RequestMapping(method=RequestMethod.PUT)
    public ResponseJSON<I> nuevo(@RequestBody I item) {
		if(service.getDAO().findByCentroAndNombre(item.getCentro(), item.getNombre())!=null){
			return new ResponseJSON<I>(ResponseJSON.YA_EXISTE);
		}else{
			service.getDAO().save(item);
			
			return new ResponseJSON<I>(ResponseJSON.OK, item);
		}
    }
	
	@RequestMapping(method=RequestMethod.POST)
    public ResponseJSON<I> modificar(@RequestBody I item) {
		if(service.getDAO().findOne(item.getId())==null)
			return new ResponseJSON<I>(ResponseJSON.NO_EXISTE);
		
		service.getDAO().save(item);
			
		return new ResponseJSON<I>(ResponseJSON.OK, item);
    }
	
	@RequestMapping(method=RequestMethod.DELETE, path="/{id}")
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

