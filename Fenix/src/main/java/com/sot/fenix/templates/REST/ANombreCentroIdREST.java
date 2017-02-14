package com.sot.fenix.templates.REST;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sot.fenix.components.json.ResponseJSON;
import com.sot.fenix.components.models.templates.AModelNombre;
import com.sot.fenix.templates.dao.INombreCentroIdDAO;
import com.sot.fenix.templates.service.ANombreCentroIdService;


public class ANombreCentroIdREST<S extends ANombreCentroIdService<D, I>, I extends AModelNombre, D extends INombreCentroIdDAO<I>> extends ACentroIdREST<S,I,D>{
	
	@RequestMapping(method=RequestMethod.PUT)
	//@Secured(value="ROLE_ADMIN")
    public ResponseJSON<I> nuevo(@RequestBody I item) {
		if(service.getDAO().findByCentroAndNombre(item.getCentro(), item.getNombre())!=null){
			return new ResponseJSON<I>(ResponseJSON.YA_EXISTE);
		}else{
			service.getDAO().save(item);
			
			return new ResponseJSON<I>(ResponseJSON.OK, item);
		}
    }
	
}

