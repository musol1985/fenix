package com.sot.fenix.components.models.horarios;

import org.springframework.data.mongodb.core.mapping.DBRef;

import com.sot.fenix.components.models.AModelId;

public class HorarioEditor extends AModelId{

	private String codigo;

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	
	public static HorarioEditor getGenerico(Horario h){
		HorarioEditor e=new HorarioEditor();
		e.id=h.getId();		
		e.codigo="DwDwtgNgBOEHYGcC8AiAFgFwwBwFwHp8B3EgOiIGZSB7AJwHN8BGATjfxE0hQD5gAjCNQDGAaygYAntgCmqNHQCGtAJbUUUFQBNUAXwwBCAFYAqAHIANAHoAzEABY0ABgCKAbQBaYewgDCGkFQKew1JVABWcN5gBAxFDBkwGTgMKDhFJNQIRX4lQRkEaMERcSlZVCSEAH0VFJlaADdFIQ1tVAADDABdYQgAVSQAGRkAdRsR+iMbBABZQYABBuibFRkILTSMuRRKgEk4FV4mYHwVta0+M/XNzJ2CgDFao4AmE6uLmLiEpJSb7cqCkUhGIJNJtloVIpqghEop0jU6o1mupNDoUDJ2gAKAAkaAAIvwnKJBggPAB2ABSICIAAkLEwAErLVbXdK3CGKfaHHjHU4sj7vP6oMwAQRmAFFeOE3vy+LF4olkqk2dtRRKgSVQeV0EpWmiAOLtKwGADeEAZABV2iL9WgGXADA0VLQAAoyZnnIXoXgADhl50u/K9YF4Tn960DnpV8hsL3DAqD0Z2sZ4Yb5AeAcBkIAwfGKILK2wUtEUetQLi6LrgADUAMSiH38bFgbH0Mm7e5k3z4d2R1lbeRHMnxvsbJMh1Mj4CCpNoFPPNPvUfBlOL2UnfOiPj4LM57eb7fy75K/fArcnI+KlKnkqHr5X3OfBU/ZUDlBNYRQjUFsEVQGo1AAFoAD9hGxFx+AAUicFgTQpfVqwASggKwTRGe5AI9ftbkqX0p3wA8L3vF85WIpUvQKbBv1KX8UBsAoMBUBoUTaFBqwMKoADZdEg3xdCccILGwfU3DgKwGlwe4DCwsc3w5I4pxnN9cJ5fDCPwS8SI3M9t1gHggA==";
		return e;
		
	}
}
