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

import com.sot.fenix.components.json.NuevoPendienteJSON;
import com.sot.fenix.components.json.PageJSON;
import com.sot.fenix.components.json.RegistrarJSON;
import com.sot.fenix.components.json.ResponseJSON;
import com.sot.fenix.components.models.Centro;
import com.sot.fenix.components.models.IUsuario;
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
		
		Usuario usuario=usuarios.getUsuarioByCorreo(current);
		
		return usuario;
    }
	
	@RequestMapping(method=RequestMethod.GET, path="/{id}")
    public Usuario get(@PathVariable String id) {		
		return usuarios.getDAO().findOne(new ObjectId(id));     
    }
	
	
	@RequestMapping(method=RequestMethod.POST, path="/registrar")
    public ResponseJSON<Usuario> registrar(@RequestBody RegistrarJSON registrar) {
		UsuarioPendiente uPendienteBD=usuarios.getPendientesDAO().findOne(new ObjectId(registrar.idPendiente));

		if(uPendienteBD!=null){			
			if(usuarios.getUsuarioByCorreo(uPendienteBD.getCorreo())==null){									
					Usuario usuario=new Usuario();
					usuario.setCorreo(uPendienteBD.getCorreo());
					usuario.setNombre(registrar.nombre);
					usuario.setPassword(registrar.password);
					
					Centro centro=uPendienteBD.getCentro();
					
					if(centro.getCorreoAdmin().equals(uPendienteBD.getCorreo())){
						usuario.setPerfil(PERFILES.ADMIN);
					}else{
						usuario.setPerfil(PERFILES.USER);
					}
					usuario.setCentro(centro);
				
					usuarios.getDAO().save(usuario);
					usuarios.getPendientesDAO().delete(uPendienteBD);
					
					if(centro.getId()==null){
						centros.getDAO().save(centro);
					}
					
				return new ResponseJSON<Usuario>(ResponseJSON.OK,usuario);
			}else{
				return new ResponseJSON<Usuario>(ResponseJSON.YA_EXISTE);
			}
		}else{
			return new ResponseJSON<Usuario>(ResponseJSON.NO_EXISTE);
		}
    }

	@RequestMapping(method=RequestMethod.GET, path="/{centro}/{page}/{size}")
    public PageJSON<Usuario> getByCentro(@PathVariable int page, @PathVariable int size, @PathVariable String centro) {
		System.out.println(centro);
		Page<Usuario> usuariosBD;		

		usuariosBD=usuarios.getUsuarioByCentro(centro, new PageRequest(page-1, size));

		return new PageJSON<Usuario>(usuariosBD.getSize(), usuariosBD.getContent());
    }
	
	@RequestMapping(method=RequestMethod.GET, path="all/{centro}/{page}/{size}")
    public PageJSON<IUsuario> getAllByCentro(@PathVariable int page, @PathVariable int size, @PathVariable String centro) {
		System.out.println(centro);
		List<IUsuario> res=new ArrayList<IUsuario>();

		res.addAll(usuarios.getUsuarioByCentro(centro));		
		res.addAll(usuarios.getUsuarioPendienteByCentro(centro));
		
		int total=res.size();
		page--;
		int max=page*size+size;
		if(max>res.size())
			max=res.size();
		int ini=page*size;
		if(ini>max)
			ini=0;
		res=res.subList(ini, max);

		return new PageJSON<IUsuario>(total, res);
    }
	
	@RequestMapping(method=RequestMethod.GET, path="/pendientes/{centro}/{page}/{size}")
    public PageJSON<UsuarioPendiente> getPendientes(@PathVariable int page, @PathVariable int size, @PathVariable String centro) {
		System.out.println("Centro:"+centro);
		Page<UsuarioPendiente> pagina=usuarios.getUsuarioPendienteByCentro(centro, new PageRequest(page-1, size));
		return new PageJSON<UsuarioPendiente>(pagina.getTotalElements(), pagina.getContent());     
    }
	
	@RequestMapping(method=RequestMethod.POST, path="/pendiente")
    public ResponseJSON<UsuarioPendiente> nuevoPendiente(@RequestBody NuevoPendienteJSON nuevo) {
		UsuarioPendiente uPendiente=nuevo.usuario;
		String correo=uPendiente.getCorreo();
		
		UsuarioPendiente uPendienteBD=usuarios.getUsuarioPendienteByCorreo(correo);
		
		if(uPendienteBD==null){			
			uPendiente.setFechaEnvio(new Date());
			
			Centro centro=centros.getDAO().findOne(new ObjectId(nuevo.centro));
			uPendiente.setCentro(centro);
			
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
		}else if(usuario.isAdmin()){
			return new ResponseJSON<UsuarioPendiente>(ResponseJSON.ES_ADMIN);
		}else{
			usuarios.getDAO().delete(usuario);
			
			return new ResponseJSON<UsuarioPendiente>(ResponseJSON.OK);
		}
    }
	
	@RequestMapping(method=RequestMethod.DELETE, path="/pendiente/{id}")
	@ResponseBody
    public ResponseJSON<UsuarioPendiente> eliminarPendiente(@PathVariable String id) {
		UsuarioPendiente uPendienteBD=usuarios.getPendientesDAO().findOne(new ObjectId(id));
		
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
		UsuarioPendiente uPendienteBD=usuarios.getPendientesDAO().findOne(new ObjectId(id));
		
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

