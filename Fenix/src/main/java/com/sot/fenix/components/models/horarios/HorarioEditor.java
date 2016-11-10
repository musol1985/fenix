package com.sot.fenix.components.models.horarios;

import org.springframework.data.mongodb.core.mapping.DBRef;

import com.sot.fenix.components.models.AModelId;

public class HorarioEditor extends AModelId{

	private String codigo;

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	

}
