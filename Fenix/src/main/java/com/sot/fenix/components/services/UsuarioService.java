package com.sot.fenix.components.services;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.sot.fenix.components.models.Centro;
import com.sot.fenix.components.models.Prestacion;
import com.sot.fenix.components.models.Usuario;
import com.sot.fenix.components.models.UsuarioPendiente;
import com.sot.fenix.components.models.horarios.Horario;
import com.sot.fenix.dao.UsuarioDAO;
import com.sot.fenix.dao.UsuarioPendienteDAO;
import com.sot.fenix.templates.service.ABasicService;

@Service
public class UsuarioService extends ABasicService<UsuarioDAO>{

	@Autowired
	private UsuarioPendienteDAO pendientesDAO;
	@Autowired
	private HorarioService horarios;
	
	
	@Autowired
	MongoTemplate template;
	
	public Usuario getUsuarioByCorreo(String login){
		return dao.findByCorreo(login);
	}

	
	public List<Usuario> getUsuarioByCentro(String centro){
		Centro c=new Centro();
		c.setId(new ObjectId(centro));
		return dao.findByCentro(c);
	}
	
	public Page<Usuario> getUsuarioByCentro(String centro, Pageable page){
		Centro c=new Centro();
		c.setId(new ObjectId(centro));
		return dao.findByCentro(c, page);
	}
	
	public List<UsuarioPendiente> getUsuarioPendienteByCentro(String centro){
		Centro c=new Centro();
		c.setId(new ObjectId(centro));
		return pendientesDAO.findByCentro(c);
	}
	
	public Page<UsuarioPendiente> getUsuarioPendienteByCentro(String centro, Pageable page){
		Centro c=new Centro();
		c.setId(new ObjectId(centro));
		return pendientesDAO.findByCentro(c, page);
	}
	
	public UsuarioPendiente getUsuarioPendienteByCorreo(String correo){
		return pendientesDAO.findByCorreo(correo);
	}

	
	public UsuarioPendienteDAO getPendientesDAO(){
		return pendientesDAO;
	}
	
	public Usuario getCurrentUsuario(){
		Authentication auth=SecurityContextHolder.getContext().getAuthentication();
		if(auth==null)
			return null;
		
		Object principal = auth.getPrincipal();		
		
        if (principal instanceof Usuario) {
            return (Usuario)principal;
        } else {
            String userName = principal.toString();
            return dao.findByCorreo(userName);
        }
                
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

	public List<Prestacion> getConHorarioAplicado(String centro){
		Horario h=horarios.getDAO().getGenerico(new ObjectId(centro));
		return dao.getSinHorarioGenerico(h.getId());
	}
}
