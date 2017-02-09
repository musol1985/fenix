package com.sot.fenix.components.models.facturacion;

import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;

public class Facturacion {
	public enum ESTADO{
		FACTURANDO, PAGADO_TOTAL, PAGADO_PARCIAL, NO_PAGADO
	}
	
	private float importeTotal;
	
	private List<Pago> pagos;
	
	private float importePagado;
	
	private Factura factura;
	
	@Indexed
	private ESTADO estado;

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


	public float getImportePagado() {
		return importePagado;
	}

	public void setImportePagado(float importePagado) {
		this.importePagado = importePagado;
	}

	public Factura getFactura() {
		return factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

	public ESTADO getEstado() {
		return estado;
	}

	public void setEstado(ESTADO estado) {
		this.estado = estado;
	}

	public void setFacturando(){
		estado=ESTADO.FACTURANDO;
	}
	
	public void setPagoParcial(){
		estado=ESTADO.PAGADO_PARCIAL;
	}
	
	public void setPagoTotal(){
		estado=ESTADO.PAGADO_TOTAL;
	}
	
	public void setNoPagado(){
		estado=ESTADO.NO_PAGADO;
	}
}
