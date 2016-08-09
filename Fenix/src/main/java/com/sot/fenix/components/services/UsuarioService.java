package com.sot.fenix.components.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sot.fenix.components.models.Usuario;
import com.sot.fenix.dao.UsuarioDAO;

@Service
public class UsuarioService {
	@Autowired
	private UsuarioDAO usuarios;
	
	public Usuario getUsuarioByLogin(String login){
		return usuarios.findOne(login);
	}
}
