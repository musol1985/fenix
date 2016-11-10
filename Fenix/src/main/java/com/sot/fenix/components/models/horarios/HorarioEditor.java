package com.sot.fenix.components.models.horarios;

import org.springframework.data.mongodb.core.mapping.DBRef;

import com.sot.fenix.components.models.AModelId;

public class HorarioEditor extends AModelId{

	@DBRef
	private Horario horario;
	
	private String codigo;

	public Horario getHorario() {
		return horario;
	}

	public void setHorario(Horario horario) {
		this.horario = horario;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	

}
