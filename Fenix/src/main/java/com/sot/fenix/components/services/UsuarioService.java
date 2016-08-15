package com.sot.fenix.components.services;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.sot.fenix.components.models.Centro;
import com.sot.fenix.components.models.Usuario;
import com.sot.fenix.components.models.UsuarioPendiente;
import com.sot.fenix.dao.UsuarioDAO;
import com.sot.fenix.dao.UsuarioPendienteDAO;

@Service
public class UsuarioService {
	@Autowired
	private UsuarioDAO usuarios;
	@Autowired
	private UsuarioPendienteDAO usuariosPendientes;
	
	@Autowired
	MongoTemplate template;
	
	public Usuario getUsuarioByLogin(String login){
		return usuarios.findByUsername(login);
	}
	
	/*public List<Usuario> getUsuarioByCentro(String centro){
		
		Query q = new Query(new Criteria("centro.$id").in(new ObjectId(centro)));

		return template.find(q, Usuario.class);
	}*/
	
	public List<Usuario> getUsuarioByCentro(String centro){
		Centro c=new Centro();
		c.setId(new ObjectId(centro));
		return usuarios.findByCentro(c);
	}
	
	public Page<Usuario> getUsuarioByCentro(String centro, Pageable page){
		Centro c=new Centro();
		c.setId(new ObjectId(centro));
		return usuarios.findByCentro(c, page);
	}
	
	public List<UsuarioPendiente> getUsuarioPendienteByCentro(String centro){
		Centro c=new Centro();
		c.setId(new ObjectId(centro));
		return usuariosPendientes.findByCentro(c);
	}
	
	public Page<UsuarioPendiente> getUsuarioPendienteByCentro(String centro, Pageable page){
		Centro c=new Centro();
		c.setId(new ObjectId(centro));
		return usuariosPendientes.findByCentro(c, page);
	}
	
	public UsuarioPendiente getUsuarioPendienteByCorreo(String correo){
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
	
	
	public void enviarEmail(UsuarioPendiente usuario){
		System.out.println("Enviando mail con codigo: "+usuario.getId());
	}
	

}
