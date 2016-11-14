package com.sot.fenix.components.services;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sot.fenix.components.models.Prestacion;
import com.sot.fenix.components.models.horarios.Horario;
import com.sot.fenix.dao.PrestacionDAO;
import com.sot.fenix.templates.basic.ABasicService;

@Service
public class PrestacionService extends ABasicService<PrestacionDAO, Prestacion>{
	@Autowired
	private HorarioService horarios;
	
	public List<Prestacion> getConHorarioAplicado(String centro){
		Horario h=horarios.getDAO().getGenerico(new ObjectId(centro));
		return dao.getSinHorarioGenerico(h.getId());
	}
}
