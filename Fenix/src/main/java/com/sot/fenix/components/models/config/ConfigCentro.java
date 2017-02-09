package com.sot.fenix.components.models.config;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ConfigCentro {
	@Id
	private IdCentroPK id;
	
	private Object value;

	public IdCentroPK getId() {
		return id;
	}

	public void setId(IdCentroPK id) {
		this.id = id;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	
}
