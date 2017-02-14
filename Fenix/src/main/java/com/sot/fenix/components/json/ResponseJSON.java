package com.sot.fenix.components.json;

public class ResponseJSON<T extends Object> {
	public static final int DESCONOCIDO=-1;
	public static final int OK=0;
	public static final int NO_EXISTE=1;
	public static final int YA_EXISTE=2;
	public static final int ES_ADMIN=3;
	public static final int ACCION_PROHIBIDA_REST=4;
	
	
	//VISITAS
	public static final int YA_PAGADA=99;
	public static final int PAGO_EN_EXCESO=98;
	public static final int NO_HAY_PAGOS=97;
	public static final int YA_FACTURADA=96;
	
	public int cod;
	public T data;
	
	public ResponseJSON(int codigo, T json) {
		this.cod = codigo;
		this.data = json;
	}
	
	public ResponseJSON(int codigo) {
		this.cod = codigo;
	}
}
