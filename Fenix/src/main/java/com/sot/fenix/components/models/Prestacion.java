package com.sot.fenix.components.models;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Document
@CompoundIndexes({@CompoundIndex(name="idx_nombre_centro",def="{nombre:1,centro:1}")})
public class Prestacion extends AModelBasic implements ICodDescr{
	
	private String color;
	
	private int duracion;
	
	private float importe;

	

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getDuracion() {
		return duracion;
	}

	public void setDuracion(int duracion) {
		this.duracion = duracion;
	}

	public float getImporte() {
		return importe;
	}

	public void setImporte(float importe) {
		this.importe = importe;
	}

	@Override
	@JsonIgnore
	public String getCodigo() {
		return id.toHexString();
	}

	@Override
	public String getDescripcion() {
		return getNombre();
	}
	
	
}
