package com.sot.fenix.components.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sot.fenix.components.exceptions.ExceptionREST;
import com.sot.fenix.components.json.PagoJSON;
import com.sot.fenix.components.json.ResponseJSON;
import com.sot.fenix.components.models.Visita;
import com.sot.fenix.components.services.VisitaService;

@RestController
@RequestMapping("/visita")
public class VisitaREST{
	final static Logger log = LogManager.getLogger(VisitaREST.class);

	@Autowired
	private VisitaService visitas;

	
	@RequestMapping(method=RequestMethod.POST, path="/pagar")
    public ResponseJSON<Visita> pagar(@RequestBody PagoJSON pagoJSON) {		
		try{
			Visita visita=visitas.realizarPago(new ObjectId(pagoJSON.visitaId), pagoJSON.importe, pagoJSON.generarFactura);
			return new ResponseJSON<Visita>(ResponseJSON.OK,visita);
		}catch(ExceptionREST ex){
			if(ex.getCodigo()==ResponseJSON.NO_EXISTE){
				log.error("La visita con código "+pagoJSON.visitaId+" no existe en la BD.");
			}else{
				log.error(ex.getMessage());
			}
			return ex.toResponse();
		}
    }
}

