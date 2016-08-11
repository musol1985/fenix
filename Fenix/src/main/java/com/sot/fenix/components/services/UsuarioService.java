package com.sot.fenix.components.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.sot.fenix.components.models.Usuario;
import com.sot.fenix.dao.UsuarioDAO;
import com.sot.fenix.dao.UsuarioPendienteDAO;

@Service
public class UsuarioService {
	@Autowired
	private UsuarioDAO usuarios;
	@Autowired
	private UsuarioPendienteDAO usuariosPendientes;
	
	public Usuario getUsuarioByLogin(String login){
		return usuarios.findByUsername(login);
	}
	
	public Usuario getUsuarioPendienteByCorreo(String correo){
		return usuariosPendientes.findByCorreo(correo);
	}
	
	public UsuarioDAO getDAO(){
		return usuarios;
	}
	
	public UsuarioPendienteDAO getPendientesDAO(){
		return usuariosPendientes;
	}
	
	public String getCurrent(){
        String userName = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
 
        if (principal instanceof UserDetails) {
            userName = ((UserDetails)principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }
}
