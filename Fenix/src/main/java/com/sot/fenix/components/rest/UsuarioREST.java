package com.sot.fenix.components.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sot.fenix.components.models.Usuario;
import com.sot.fenix.components.services.UsuarioService;

@RestController
@RequestMapping("/usuario")
public class UsuarioREST{
	
	@Autowired
	private UsuarioService usuarios;

	@RequestMapping(method=RequestMethod.GET, path="/current")
    public Usuario current() {
		String current=usuarios.getCurrent();
		
		return usuarios.getUsuarioByLogin(current);     
    }
	
	@RequestMapping(method=RequestMethod.GET, path="/{id}")
    public Usuario get(@PathVariable String id) {
		return usuarios.getUsuarioByLogin(id);     
    }

	@RequestMapping(method=RequestMethod.GET, path="/all")
    public List<Usuario> getAll() {
		return usuarios.getDAO().findAll();     
    }
}

