package com.sot.fenix.components.json;

import java.util.List;

public class ResponseListJSON<T extends Object> {
	public static final int DESCONOCIDO=-1;
	public static final int OK=0;
	public static final int NO_EXISTE=1;
	public static final int YA_EXISTE=2;
	
	public int cod;
	public List<T> data;
	
	public ResponseListJSON(int codigo, List<T> json) {
		this.cod = codigo;
		this.data = json;
	}
	
	public ResponseListJSON(int codigo) {
		this.cod = codigo;
	}
}
