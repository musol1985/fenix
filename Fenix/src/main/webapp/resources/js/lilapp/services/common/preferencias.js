materialAdmin
    // =========================================================================
    // PreferenciasService
    // =========================================================================
    
    .service('PreferenciasService', function($cookies){
    	var self=this;
    	
    	var VISTA_CALENDARIO="vistaCalendario";
    	var HUECOS_CALENDARIO="huecosCalendario";
    	
        this.getPreferencia=function(id, defecto) {
            var valor=$cookies.get(id);
            if(!valor)
            	return defecto;
            return valor;            
        }
    	
    	this.setPreferencia=function(id, valor){
    		$cookies.put(id, valor);
    	}
    	
    	
    	this.setVistaCalendario=function(valor){
    		this.setPreferencia(VISTA_CALENDARIO, valor);
    	}
    	
    	this.getVistaCalendario=function(){
    		return this.getPreferencia(VISTA_CALENDARIO, "agendaWeek");
    	}
    	
    	this.setHuecosCalendario=function(valor){
    		this.setPreferencia(HUECOS_CALENDARIO, valor);
    	}
    	
    	this.getHuecosCalendario=function(){
    		return this.getPreferencia(HUECOS_CALENDARIO, "00:15:00");
    	}
    })