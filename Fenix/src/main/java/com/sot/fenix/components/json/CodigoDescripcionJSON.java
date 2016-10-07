package com.sot.fenix.components.json;

import com.sot.fenix.components.models.ICodDescr;

public class CodigoDescripcionJSON {
	public String codigo;
	public String descripcion;
	
	public CodigoDescripcionJSON(){
		
	}
	
	public CodigoDescripcionJSON(ICodDescr c) {
		this.codigo = c.getCodigo();
		this.descripcion = c.getDescripcion();
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
}
