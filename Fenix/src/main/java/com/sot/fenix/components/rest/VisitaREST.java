package com.sot.fenix.components.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sot.fenix.components.exceptions.ExceptionREST;
import com.sot.fenix.components.json.ResponseJSON;
import com.sot.fenix.components.models.Cita;
import com.sot.fenix.components.models.Visita;
import com.sot.fenix.components.services.VisitaService;

@RestController
@RequestMapping("/visita")
public class VisitaREST{
	final static Logger log = LogManager.getLogger(VisitaREST.class);

	@Autowired
	private VisitaService visitas;

	
	@RequestMapping(method=RequestMethod.POST, path="/pasar/visita")
    public ResponseJSON<Visita> pasarVisita(@RequestBody Cita cita) {		
		try{
			log.debug("PasandoVisita REST");
			Visita visita=visitas.nuevaVisitaFromCita(cita);
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
}

