package com.sot.fenix.components.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sot.fenix.components.models.UsuarioPendiente;
import com.sot.fenix.components.services.UsuarioService;

@Controller
@RequestMapping("/")
public class MainController{
	final static Logger logger = LogManager.getLogger(MainController.class);
	
	@Autowired
	private UsuarioService usuarios;
	  
	    @RequestMapping(value = "/", method = RequestMethod.GET)
	    public String homePage(ModelMap model) {
	        return "/views/landing.jsp";
	    }
	 
	    @RequestMapping(value = "/app", method = RequestMethod.GET)
	    public String adminPage(ModelMap model) {
	        model.addAttribute("user", getPrincipal());
	        return "/resources/app.html";
	    }
	    
	    @RequestMapping(value = "/app", method = RequestMethod.POST)
	    public String onLogin(ModelMap model) {
	        model.addAttribute("user", getPrincipal());
	        return "/resources/app.html";
	    }
	     
	    @RequestMapping(value = "/app/admin", method = RequestMethod.GET)
	    public String dbaPage(ModelMap model) {
	        model.addAttribute("user", getPrincipal());
	        return "/views/admin.jsp";
	    }
	    
	    @RequestMapping(value = "/app/root", method = RequestMethod.GET)
	    public String rootPage(ModelMap model) {
	        model.addAttribute("user", getPrincipal());
	        return "/views/root.jsp";
	    }
	 
	    @RequestMapping(value = "/denegado", method = RequestMethod.GET)
	    public String accessDeniedPage(ModelMap model, HttpServletResponse response) {
	    	response.setStatus(401);
	        model.addAttribute("user", getPrincipal());
	        return "/401.html";
	    }
	    

	    @RequestMapping(value = "/login", method = RequestMethod.GET)
	    //public String loginPage(@CookieValue(value = "auto", defaultValue = "N") String auto) {
	    public String loginPage(HttpServletRequest r) {	   
	        return "/login.jsp";
	    }
	    
	    @RequestMapping(value = "/login", method = RequestMethod.POST)
	    //public String loginPage(@CookieValue(value = "auto", defaultValue = "N") String auto) {
	    public String loginPagePOST(HttpServletRequest r) {	   
	        return "/login.jsp";
	    }
	    
	    @RequestMapping(value = "/loginAjax", method = RequestMethod.GET)
	    //public String loginPage(@CookieValue(value = "auto", defaultValue = "N") String auto) {
	    public String loginPageAjax(HttpServletRequest r) {	   
	        return "/loginAjax.jsp";
	    }
	    
	    @RequestMapping(value = "/registrar", method = RequestMethod.GET)
	    public String loginPage(ModelMap model, @RequestParam String id) {
	    	
	    	UsuarioPendiente uPendienteBD=usuarios.getPendientesDAO().findOne(new ObjectId(id));
	    	
	    	if(uPendienteBD!=null){
		    	model.addAttribute("correo", uPendienteBD.getCorreo());
		    	model.addAttribute("id", uPendienteBD.getId());
		    	model.addAttribute("nombre", uPendienteBD.getNombre());
		    	model.addAttribute("centro", uPendienteBD.getCentro().getNombre());
		    	
		        return "/registrar.jsp";
	    	}else{
	    		return "/401.jsp";
	    	}
	    }
	 
	    @RequestMapping(value="/logout", method = RequestMethod.GET)
	    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
	        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	        if (auth != null){    
	            new SecurityContextLogoutHandler().logout(request, response, auth);
	        }
	        return "redirect:/login.jsp?logout";
	    }
	 
	    private String getPrincipal(){
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
