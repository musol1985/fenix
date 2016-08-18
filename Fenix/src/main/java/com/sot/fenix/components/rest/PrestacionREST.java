package com.sot.fenix.components.rest;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sot.fenix.components.json.PageJSON;
import com.sot.fenix.components.json.ResponseJSON;
import com.sot.fenix.components.models.Prestacion;
import com.sot.fenix.components.services.PrestacionService;

@RestController
@RequestMapping("/prestacion")
public class PrestacionREST{
	
	@Autowired
	private PrestacionService prestaciones;

	@RequestMapping(method=RequestMethod.GET, path="{centro}/{page}/{size}")
    public PageJSON<Prestacion> getByCentro(@PathVariable int page, @PathVariable int size, @PathVariable String centro) {

		Page<Prestacion> prestaciones=this.prestaciones.getByCentro(centro, new PageRequest(page-1, size));		

		return new PageJSON<Prestacion>(prestaciones.getSize(), prestaciones.getContent());
    }
	
	@RequestMapping(method=RequestMethod.GET, path="{id}")
    public ResponseJSON<Prestacion> getByCentro(@PathVariable String id) {

		Prestacion p=prestaciones.getDAO().findOne(new ObjectId(id));	
		if(p!=null){
			return new ResponseJSON<Prestacion>(ResponseJSON.OK, p);
		}else{
			return new ResponseJSON<Prestacion>(ResponseJSON.NO_EXISTE);
		}
    }
	
	
	@RequestMapping(method=RequestMethod.PUT)
    public ResponseJSON<Prestacion> nueva(@RequestBody Prestacion prestacion) {
		if(prestaciones.getDAO().findByCentroAndNombre(prestacion.getCentro(), prestacion.getNombre())!=null){
			return new ResponseJSON<Prestacion>(ResponseJSON.YA_EXISTE);
		}else{
			prestaciones.getDAO().save(prestacion);
			
			return new ResponseJSON<Prestacion>(ResponseJSON.OK, prestacion);
		}
    }
	
	@RequestMapping(method=RequestMethod.POST)
    public ResponseJSON<Prestacion> modificar(@RequestBody Prestacion prestacion) {
		if(prestaciones.getDAO().findOne(prestacion.getId())==null)
			return new ResponseJSON<Prestacion>(ResponseJSON.NO_EXISTE);
		
		prestaciones.getDAO().save(prestacion);
			
		return new ResponseJSON<Prestacion>(ResponseJSON.OK, prestacion);
    }
	
	@RequestMapping(method=RequestMethod.DELETE, path="/{prestacion}")
    public ResponseJSON<Prestacion> eliminar(@PathVariable String prestacion) {
		Prestacion p=prestaciones.getDAO().findOne(new ObjectId(prestacion));
		if(p!=null){
			prestaciones.getDAO().delete(p);
			return new ResponseJSON<Prestacion>(ResponseJSON.OK);
		}else{
			return new ResponseJSON<Prestacion>(ResponseJSON.NO_EXISTE);
		}
    }
	
}

