package com.sot.fenix.components.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sot.fenix.components.json.HorarioJSON;
import com.sot.fenix.components.json.ResponseJSON;
import com.sot.fenix.components.models.horarios.Horario;
import com.sot.fenix.components.services.HorarioService;
import com.sot.fenix.components.services.PrestacionService;
import com.sot.fenix.dao.HorarioDAO;
import com.sot.fenix.templates.basic.ABasicREST;

@RestController
@RequestMapping("/horario")
public class HorarioREST extends ABasicREST<HorarioService, Horario, HorarioDAO>{

	@Autowired
	private PrestacionService prestaciones;
	
	@RequestMapping(method=RequestMethod.PUT, path="guardar")
    public ResponseJSON<HorarioJSON> guardar(@RequestBody HorarioJSON json) {
		if(json.isNuevo() && service.getDAO().findByCentroAndNombre(json.model.getCentro(), json.model.getNombre())!=null){
			return new ResponseJSON<HorarioJSON>(ResponseJSON.YA_EXISTE);
		}else{
			if(json.isNuevo()){
				service.crear(json);
			}else{
				service.modificar(json);
			}

			return new ResponseJSON<HorarioJSON>(ResponseJSON.OK, json);
		}
    }
	

}

