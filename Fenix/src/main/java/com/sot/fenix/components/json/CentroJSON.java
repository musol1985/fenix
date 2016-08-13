package com.sot.fenix.components.json;

import java.util.List;

import com.sot.fenix.components.models.Centro;

public class CentroJSON{
	public String id;
	public String nombre;
	public String correoAdmin;
	public String poblacion;
	
	public CentroJSON(){
		
	}
	
	public CentroJSON(Centro c){
		id=c.getId().toHexString();
		nombre=c.getNombre();
		correoAdmin=c.getCorreoAdmin();
		poblacion=c.getPoblacion();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getCorreoAdmin() {
		return correoAdmin;
	}
	public void setCorreoAdmin(String correoAdmin) {
		this.correoAdmin = correoAdmin;
	}
	public String getPoblacion() {
		return poblacion;
	}
	public void setPoblacion(String poblacion) {
		this.poblacion = poblacion;
	}
	

	
}
