package com.sot.fenix.components.providers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.sot.fenix.components.models.Usuario;
import com.sot.fenix.components.services.UsuarioService;

@Component
public class LoginProvider implements AuthenticationProvider {
	
	@Autowired
	private UsuarioService usuarios;

	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        Usuario usuario=usuarios.getUsuarioByLogin(username);
               
        if(usuario==null)
        	throw new BadCredentialsException("Nombre de usuario incorrecto");
        
        if(!usuario.getPassword().equalsIgnoreCase(password.trim())){
        	throw new BadCredentialsException("Contraseña incorrecta");
        }           
   
        System.out.println("Login correcto para el usuario "+usuario.getUsername()+" con los perfiles:");
        for(GrantedAuthority p:usuario.getAuthorities()){
        	System.out.println("----"+p.getAuthority());
        }
        
        return new UsernamePasswordAuthenticationToken(usuario, password, usuario.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> arg0) {
		return true;
	}

}
