package com.sot.fenix.components.json;

import com.sot.fenix.components.models.ICodDescr;

public class CodigoDescripcionJSON {
	public String id;
	public String descripcion;
	
	public CodigoDescripcionJSON(){
		
	}
	
	public CodigoDescripcionJSON(ICodDescr c) {
		this.id = c.getCodigo();
		this.descripcion = c.getDescripcion();
	}

	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
}
