package com.sot.fenix.components.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sot.fenix.components.json.UsuarioJSON;
import com.sot.fenix.components.models.Usuario;
import com.sot.fenix.components.models.UsuarioPendiente;
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
    public List<UsuarioJSON> getAll() {
		List<Usuario> usuariosBD=usuarios.getDAO().findAll();     
		List<UsuarioJSON> usuarios=new ArrayList<UsuarioJSON>(usuariosBD.size());
		for(Usuario u:usuariosBD){
			usuarios.add(new UsuarioJSON(u));
		}
		return usuarios;
    }
	
	@RequestMapping(method=RequestMethod.POST, path="/pendiente")
    public UsuarioPendiente nuevoPendiente(@RequestBody UsuarioPendiente uPendiente) {
		
		usuarios.getPendientesDAO().save(uPendiente);
		
		return uPendiente;
    }
}

