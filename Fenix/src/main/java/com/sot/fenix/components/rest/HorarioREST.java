package com.sot.fenix.components.rest;

import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sot.fenix.components.json.HorarioJSON;
import com.sot.fenix.components.json.ResponseJSON;
import com.sot.fenix.components.models.horarios.Horario;
import com.sot.fenix.components.models.horarios.HorarioEditor;
import com.sot.fenix.components.services.HorarioService;
import com.sot.fenix.dao.HorarioDAO;
import com.sot.fenix.templates.basic.ABasicREST;

@RestController
@RequestMapping("/horario")
public class HorarioREST extends ABasicREST<HorarioService, Horario, HorarioDAO>{

	@RequestMapping(method=RequestMethod.PUT, path="guardar")
    public ResponseJSON<Horario> guardar(@RequestBody HorarioJSON json) {
		if(json.isNuevo() && service.getDAO().findByCentroAndNombre(json.horario.getCentro(), json.horario.getNombre())!=null){
			return new ResponseJSON<Horario>(ResponseJSON.YA_EXISTE);
		}else{
			if(json.isNuevo()){
				service.crear(json);
			}else{
				service.modificar(json);
			}

			return new ResponseJSON<Horario>(ResponseJSON.OK, json.horario);
		}
    }
	
	@RequestMapping(method=RequestMethod.GET, path="editor/{id}")
    public ResponseJSON<HorarioJSON> getEditor(@PathVariable String id) {
		HorarioEditor item=service.getEditorDAO().findOne(new ObjectId(id));
		Horario h=service.getDAO().findOne(new ObjectId(id));
		if(item!=null && h!=null){
			HorarioJSON json=new HorarioJSON();
			json.codigo=item.getCodigo();
			json.horario=h;
			return new ResponseJSON<HorarioJSON>(ResponseJSON.OK, json);
		}else{
			return new ResponseJSON<HorarioJSON>(ResponseJSON.NO_EXISTE);
		}
    }
}

