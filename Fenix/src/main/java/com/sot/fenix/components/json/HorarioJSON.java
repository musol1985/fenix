package com.sot.fenix.components.json;

import com.sot.fenix.components.models.horarios.Horario;

public class HorarioJSON{
	public Horario model;
	public String codigo;
	
	public boolean isNuevo(){
		return model.getId()==null;
	}
}
