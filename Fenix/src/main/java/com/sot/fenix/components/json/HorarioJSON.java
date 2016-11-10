package com.sot.fenix.components.json;

import com.sot.fenix.components.models.horarios.Horario;

public class HorarioJSON{
	public Horario horario;
	public String codigo;
	
	public boolean isNuevo(){
		return horario.getId()==null;
	}
}
