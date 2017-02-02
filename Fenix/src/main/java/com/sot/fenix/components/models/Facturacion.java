package com.sot.fenix.components.models;

import java.util.List;

public class Facturacion {
	
	private float importeTotal;
	
	private List<Pago> pagos;
	
	private float importeRestante;
	
	private boolean facturaOficialUnica;

	public float getImporteTotal() {
		return importeTotal;
	}

	public void setImporteTotal(float importeTotal) {
		this.importeTotal = importeTotal;
	}

	public List<Pago> getPagos() {
		return pagos;
	}

	public void setPagos(List<Pago> pagos) {
		this.pagos = pagos;
	}

	public float getImporteRestante() {
		return importeRestante;
	}

	public void setImporteRestante(float importeRestante) {
		this.importeRestante = importeRestante;
	}

	public boolean getFacturaOficialUnica() {
		return facturaOficialUnica;
	}

	public void setFacturaOficialUnica(boolean facturaOficialUnica) {
		this.facturaOficialUnica = facturaOficialUnica;
	}
	
	
}
