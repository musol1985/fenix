package com.sot.fenix.components.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.sot.fenix.components.models.Usuario;
import com.sot.fenix.components.models.UsuarioPendiente;
import com.sot.fenix.dao.CentroDAO;
import com.sot.fenix.dao.UsuarioDAO;
import com.sot.fenix.dao.UsuarioPendienteDAO;

@Service
public class CentroService {
	@Autowired
	private CentroDAO centros;

	public CentroDAO getDAO() {
		return centros;
	}


}
