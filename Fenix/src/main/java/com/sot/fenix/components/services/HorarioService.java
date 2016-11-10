package com.sot.fenix.components.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sot.fenix.components.json.HorarioJSON;
import com.sot.fenix.components.models.horarios.Horario;
import com.sot.fenix.components.models.horarios.HorarioEditor;
import com.sot.fenix.dao.HorarioDAO;
import com.sot.fenix.dao.HorarioEditorDAO;
import com.sot.fenix.templates.basic.ABasicService;

@Service
public class HorarioService extends ABasicService<HorarioDAO, Horario>{
	@Autowired
	protected HorarioEditorDAO editorDAO;

	public HorarioEditorDAO getEditorDAO() {
		return editorDAO;
	}
	
	public void crear(HorarioJSON nuevoHorario){
		dao.save(nuevoHorario.horario);
		
		HorarioEditor editor=new HorarioEditor();
		editor.setId(nuevoHorario.horario.getId());
		editor.setCodigo(nuevoHorario.codigo);
		editorDAO.save(editor);
	}
	
	public void modificar(HorarioJSON horario){
		dao.save(horario.horario);
		HorarioEditor editor=editorDAO.findOne(horario.horario.getId());
		editor.setCodigo(horario.codigo);
		editorDAO.save(editor);
	}
	
}
