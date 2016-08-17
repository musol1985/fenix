package com.sot.fenix.components.models;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonGetter;

@Document
public class Prestacion extends AModelId{

	private String nombre;
	
	private String alias;
	
	@Indexed	
	@DBRef
	private Centro centro;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	@JsonGetter("centro")
	public String getJsonCentro(){
		if(centro==null)
			return "";
		return centro.getId().toHexString();
	}

	public Centro getCentro() {
		return centro;
	}

	public void setCentro(Centro centro) {
		System.out.println("-------------------->"+centro);
		this.centro = centro;
	}
	
	
}
