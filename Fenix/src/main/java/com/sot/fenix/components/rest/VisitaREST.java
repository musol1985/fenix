package com.sot.fenix.components.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sot.fenix.components.exceptions.ExceptionREST;
import com.sot.fenix.components.json.ResponseJSON;
import com.sot.fenix.components.models.Cita;
import com.sot.fenix.components.models.Visita;
import com.sot.fenix.components.services.VisitaService;
import com.sot.fenix.dao.VisitaDAO;
import com.sot.fenix.templates.REST.ACentroIdREST;

@RestController
@RequestMapping("/visita")
public class VisitaREST extends ACentroIdREST<VisitaService, Visita, VisitaDAO>{
	final static Logger log = LogManager.getLogger(VisitaREST.class);


	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.POST, path="/pasar")
	//@PreAuthorize("#cita.centro.id == principal.centro.id")
    public ResponseJSON<Visita> pasarVisita(@RequestBody Cita cita) {		
		try{
			log.debug("PasandoVisita REST");
			Visita visita=service.nuevaVisitaFromCita(cita);
			return new ResponseJSON<Visita>(ResponseJSON.OK,visita);			
		}catch(ExceptionREST ex){
			if(ex.getCodigo()==ResponseJSON.NO_EXISTE){
				log.error("La cita con código "+cita.getJsonId()+" no existe en la BD.");
			}else{
				log.error(ex.getMessage());
			}
			return (ResponseJSON<Visita>)ex.toResponse();
		}
    }
	
	@Override
	@RequestMapping(method=RequestMethod.DELETE, path="/{id}")
    public ResponseJSON<Visita> eliminar(@PathVariable String id){
		return new ResponseJSON<Visita>(ResponseJSON.ACCION_PROHIBIDA_REST);
	}
	
	@RequestMapping(method=RequestMethod.POST)
	//@Secured(value="ROLE_ADMIN")
    public ResponseJSON<Visita> modificar(@RequestBody Visita item) {
		return new ResponseJSON<Visita>(ResponseJSON.ACCION_PROHIBIDA_REST);
	}
}

