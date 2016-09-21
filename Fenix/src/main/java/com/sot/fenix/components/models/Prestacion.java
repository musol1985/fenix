package com.sot.fenix.components.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

@Document
@CompoundIndexes({@CompoundIndex(name="idx_nombre_centro",def="{nombre:1,centro:1}")})
public class Prestacion extends AModelId{
	
	private String nombre;
	
	@DBRef
	private Centro centro;
	
	private String color;
	
	private int duracion;
	
	private float importe;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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
		this.centro = centro;
	}

	@JsonSetter("centro")
	public void setJsonCentro(String id) {
		if(id!=null && !id.isEmpty()){
			centro=new Centro();
			centro.id=new ObjectId(id);
		}
	}

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
	
	
}
