package com.sot.fenix.components.rest;

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
	
	
	@RequestMapping(method=RequestMethod.POST,path="nueva")
    public ResponseJSON<Prestacion> nuevoPendiente(@RequestBody Prestacion prestacion) {
System.out.println("iu");
		if(prestacion.getId()!=null && prestaciones.getDAO().findByCentroAndId(prestacion.getCentro(), prestacion.getId())!=null){
			System.out.println("iu2");
			return new ResponseJSON<Prestacion>(ResponseJSON.YA_EXISTE);
		}else{
			System.out.println("iu3");
			prestaciones.getDAO().save(prestacion);
			
			return new ResponseJSON<Prestacion>(ResponseJSON.OK);
		}
    }
	
}

