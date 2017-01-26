package com.sot.fenix.components.exceptions;

import com.sot.fenix.components.json.ResponseJSON;

public class ExceptionREST extends Exception {
	private int codigo;
	
	public ExceptionREST(int codigo, String mensaje){
		super(mensaje);
		this.codigo=codigo;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	
	@SuppressWarnings("rawtypes")
	public ResponseJSON toResponse(){
		return new ResponseJSON(codigo);
	}
}
