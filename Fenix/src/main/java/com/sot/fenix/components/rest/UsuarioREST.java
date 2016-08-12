package com.sot.fenix.components.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sot.fenix.components.json.PageJSON;
import com.sot.fenix.components.json.ResponseJSON;
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
	
	@RequestMapping(method=RequestMethod.GET, path="/pendientes/{page}/{size}")
    public PageJSON<UsuarioPendiente> getPendientes(@PathVariable int page, @PathVariable int size) {
		Page<UsuarioPendiente> pagina=usuarios.getPendientesDAO().findAll(new PageRequest(page-1, size));
		return new PageJSON<UsuarioPendiente>(pagina.getTotalElements(), pagina.getContent());     
    }
	
	@RequestMapping(method=RequestMethod.POST, path="/pendiente")
    public ResponseJSON<UsuarioPendiente> nuevoPendiente(@RequestBody UsuarioPendiente uPendiente) {
		String correo=uPendiente.getCorreo();
		
		UsuarioPendiente uPendienteBD=usuarios.getUsuarioPendienteByCorreo(correo);
		
		if(uPendienteBD==null){			
			uPendiente.setFechaEnvio(new Date());
			uPendiente=usuarios.getPendientesDAO().save(uPendiente);
			
			return new ResponseJSON<UsuarioPendiente>(ResponseJSON.OK, uPendiente);
		}else{
			uPendienteBD.setFechaEnvio(new Date());
			usuarios.getPendientesDAO().save(uPendienteBD);
			
			return new ResponseJSON<UsuarioPendiente>(ResponseJSON.YA_EXISTE);
		}
    }
	
	@RequestMapping(method=RequestMethod.DELETE, path="/pendiente/{id}")
	@ResponseBody
    public ResponseJSON<UsuarioPendiente> eliminarPendiente(@PathVariable String id) {
		UsuarioPendiente uPendienteBD=usuarios.getPendientesDAO().findOne(id);
		
		if(uPendienteBD==null){						
			return new ResponseJSON<UsuarioPendiente>(ResponseJSON.NO_EXISTE);
		}else{			
			usuarios.getPendientesDAO().delete(uPendienteBD);
			
			return new ResponseJSON<UsuarioPendiente>(ResponseJSON.OK);
		}
    }
	
	@RequestMapping(method=RequestMethod.DELETE, path="/pendiente/correo/{id}")
	@ResponseBody
    public ResponseJSON<UsuarioPendiente> enviarCorreo(@PathVariable String id) {
		UsuarioPendiente uPendienteBD=usuarios.getPendientesDAO().findOne(id);
		
		if(uPendienteBD==null){						
			return new ResponseJSON<UsuarioPendiente>(ResponseJSON.NO_EXISTE);
		}else{			
			uPendienteBD.setFechaEnvio(new Date());
			usuarios.getPendientesDAO().save(uPendienteBD);
			
			return new ResponseJSON<UsuarioPendiente>(ResponseJSON.OK);
		}
    }
}

