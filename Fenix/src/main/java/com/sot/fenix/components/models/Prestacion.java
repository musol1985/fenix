package com.sot.fenix.components.models;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.sot.fenix.components.models.horarios.Horario;
import com.sot.fenix.components.models.templates.AModelNombre;
import com.sot.fenix.components.models.templates.ICodDescr;

@Document
@CompoundIndexes({@CompoundIndex(name="idx_nombre_centro",def="{nombre:1,centro:1}")})
public class Prestacion extends AModelNombre implements ICodDescr{
	
	private String color;
	
	private int duracion;
	
	private float importe;

	@DBRef
	private Horario horario;

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
	

	public Horario getHorario() {
		return horario;
	}

	public void setHorario(Horario horario) {
		this.horario = horario;
	}

	@Override
	@JsonIgnore
	public String getCodigo() {
		if(id!=null)
			return id.toHexString();
		return null;
	}

	@Override
	public String getDescripcion() {
		return getNombre();
	}
	
	@JsonGetter("horario")
	public String getJsonHorario(){
		if(horario==null)
			return "";
		return horario.getId().toHexString();
	}
	@JsonSetter("horario")
	public void setJsonHorario(String id) {
		if(id!=null && !id.isEmpty()){
			horario=new Horario();
			horario.setId(new ObjectId(id));
		}
	}
}
