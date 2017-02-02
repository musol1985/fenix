package com.sot.fenix.components.models.facturacion;

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

	private int idFactura;

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


	public int getIdFactura() {
		return idFactura;
	}

	public void setIdFactura(int idFactura) {
		this.idFactura = idFactura;
	}


	public void setIdFactura(FacturaOficial factura) {
		this.idFactura = factura.getIdFactura();
	}
	
	public boolean hasFactura(){
		return idFactura!=0;
	}
}
