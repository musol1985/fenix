package com.sot.fenix.components.models;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sot.fenix.components.models.templates.AModelId;

@Document
@JsonIgnoreProperties(ignoreUnknown=true)
public class Pago extends AModelId{
	private float importe;
	
	@CreatedDate
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm")
	private Date fecha;

	private boolean oficial;
	
	private int numFactura;

	public float getImporte() {
		return importe;
	}

	public void setImporte(float importe) {
		this.importe = importe;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public boolean isOficial() {
		return oficial;
	}

	public void setOficial(boolean oficial) {
		this.oficial = oficial;
	}

	public int getNumFactura() {
		return numFactura;
	}

	public void setNumFactura(int numFactura) {
		this.numFactura = numFactura;
	}

}
