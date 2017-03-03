package com.sot.fenix.components.models.Test;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.sot.fenix.components.models.templates.AModelCentro;

@Document
public class TestModel extends AModelCentro{
	private String texto;
	private List<TestSubModel> subs;

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public List<TestSubModel> getSubs() {
		return subs;
	}

	public void setSubs(List<TestSubModel> subs) {
		this.subs = subs;
	}
	
	
}
