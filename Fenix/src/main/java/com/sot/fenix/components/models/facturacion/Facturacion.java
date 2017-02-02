package com.sot.fenix.components.models.facturacion;

import java.util.List;

public class Facturacion {
	
	private float importeTotal;
	
	private List<Pago> pagos;
	
	private float importePagado;
	
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

	public boolean getFacturaOficialUnica() {
		return facturaOficialUnica;
	}

	public void setFacturaOficialUnica(boolean facturaOficialUnica) {
		this.facturaOficialUnica = facturaOficialUnica;
	}

	public float getImportePagado() {
		return importePagado;
	}

	public void setImportePagado(float importePagado) {
		this.importePagado = importePagado;
	}
	
	
}
