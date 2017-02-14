package com.sot.fenix.components.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sot.fenix.components.json.HorarioJSON;
import com.sot.fenix.components.models.horarios.Horario;
import com.sot.fenix.components.models.horarios.HorarioEditor;
import com.sot.fenix.dao.HorarioDAO;
import com.sot.fenix.dao.HorarioEditorDAO;
import com.sot.fenix.templates.service.ANombreCentroIdService;

@Service
public class HorarioService extends ANombreCentroIdService<HorarioDAO, Horario>{
	@Autowired
	protected HorarioEditorDAO editorDAO;

	public HorarioEditorDAO getEditorDAO() {
		return editorDAO;
	}
	
	public void crear(HorarioJSON nuevoHorario){
		dao.save(nuevoHorario.model);
		
		HorarioEditor editor=new HorarioEditor();
		editor.setId(nuevoHorario.model.getId());
		editor.setCodigo(nuevoHorario.codigo);
		editorDAO.save(editor);
	}
	
	public void modificar(HorarioJSON horario){
		dao.save(horario.model);
		HorarioEditor editor=editorDAO.findOne(horario.model.getId());
		editor.setCodigo(horario.codigo);
		editorDAO.save(editor);
	}
	
}
