package com.sot.fenix.components.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
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
import com.sot.fenix.components.json.RegistrarJSON;
import com.sot.fenix.components.json.ResponseJSON;
import com.sot.fenix.components.json.UsuarioJSON;
import com.sot.fenix.components.models.Centro;
import com.sot.fenix.components.models.Perfil.PERFILES;
import com.sot.fenix.components.models.Usuario;
import com.sot.fenix.components.models.UsuarioPendiente;
import com.sot.fenix.components.services.CentroService;
import com.sot.fenix.components.services.UsuarioService;

@RestController
@RequestMapping("/usuario")
public class UsuarioREST{
	
	@Autowired
	private UsuarioService usuarios;
	@Autowired
	private CentroService centros;


	@RequestMapping(method=RequestMethod.GET, path="/current")
    public Usuario current() {
		String current=usuarios.getCurrent();
		
		return usuarios.getUsuarioByLogin(current);     
    }
	
	@RequestMapping(method=RequestMethod.GET, path="/{id}")
    public Usuario get(@PathVariable String id) {
		return usuarios.getUsuarioByLogin(id);     
    }
	
	
	@RequestMapping(method=RequestMethod.POST, path="/registrar")
    public ResponseJSON<UsuarioJSON> registrar(@RequestBody RegistrarJSON registrar) {
		UsuarioPendiente uPendienteBD=usuarios.getPendientesDAO().findOne(registrar.idPendiente);

		if(uPendienteBD!=null){			
			if(usuarios.getUsuarioByLogin(uPendienteBD.getCorreo())==null){									
					Usuario usuario=new Usuario();
					usuario.setUsername(uPendienteBD.getCorreo());
					usuario.setNombre(registrar.nombre);
					usuario.setPassword(registrar.password);
					
					Centro centro=centros.getDAO().findByCorreoAdmin(uPendienteBD.getCorreo());
					
					if(centro!=null){
						usuario.setPerfil(PERFILES.ADMIN);
					}else{
						usuario.setPerfil(PERFILES.USER);
					}
				
					usuarios.getDAO().save(usuario);
					usuarios.getPendientesDAO().delete(uPendienteBD);
					
					if(centro!=null){
						centro.getUsuarios().add(usuario);
						centros.getDAO().save(centro);
					}
					
				return new ResponseJSON<UsuarioJSON>(ResponseJSON.OK, new UsuarioJSON(usuario));
			}else{
				return new ResponseJSON<UsuarioJSON>(ResponseJSON.YA_EXISTE);
			}
		}else{
			return new ResponseJSON<UsuarioJSON>(ResponseJSON.NO_EXISTE);
		}
    }

	@RequestMapping(method=RequestMethod.GET, path="/all/{page}/{size}")
    public PageJSON<UsuarioJSON> getAll(@PathVariable int page, @PathVariable int size) {
		Page<Usuario> usuariosBD=usuarios.getDAO().findAll(new PageRequest(page-1, size));     
		List<UsuarioJSON> usuarios=new ArrayList<UsuarioJSON>(usuariosBD.getContent().size());
		for(Usuario u:usuariosBD.getContent()){
			usuarios.add(new UsuarioJSON(u));
		}
		return new PageJSON<UsuarioJSON>(usuariosBD.getTotalElements(), usuarios);
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
			
			usuarios.enviarEmail(uPendiente);
			
			return new ResponseJSON<UsuarioPendiente>(ResponseJSON.OK, uPendiente);
		}else{
			uPendienteBD.setFechaEnvio(new Date());
			usuarios.enviarEmail(uPendienteBD);
			usuarios.getPendientesDAO().save(uPendienteBD);
			
			return new ResponseJSON<UsuarioPendiente>(ResponseJSON.YA_EXISTE);
		}
    }
	
	@RequestMapping(method=RequestMethod.DELETE, path="/{id}")
	@ResponseBody
    public ResponseJSON<UsuarioPendiente> eliminar(@PathVariable String id) {
		Usuario usuario=usuarios.getDAO().findOne(new ObjectId(id));
		
		if(usuario==null){						
			return new ResponseJSON<UsuarioPendiente>(ResponseJSON.NO_EXISTE);
		}else{			
			usuarios.getDAO().delete(usuario);
			
			return new ResponseJSON<UsuarioPendiente>(ResponseJSON.OK);
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
	
	@RequestMapping(method=RequestMethod.GET, path="/pendiente/correo/{id}")
	@ResponseBody
    public ResponseJSON<UsuarioPendiente> enviarCorreo(@PathVariable String id) {
		UsuarioPendiente uPendienteBD=usuarios.getPendientesDAO().findOne(id);
		
		if(uPendienteBD==null){						
			return new ResponseJSON<UsuarioPendiente>(ResponseJSON.NO_EXISTE);
		}else{			
			uPendienteBD.setFechaEnvio(new Date());
			usuarios.getPendientesDAO().save(uPendienteBD);
						
			usuarios.enviarEmail(uPendienteBD);
			
			return new ResponseJSON<UsuarioPendiente>(ResponseJSON.OK);
		}
    }
}

