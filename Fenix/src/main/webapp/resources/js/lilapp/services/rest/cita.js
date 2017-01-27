materialAdmin
     // =========================================================================
    // CitaService
    // =========================================================================
    .service('citaService', function($q, $http, BasicRESTService, userService, horarioService){
    	var self=this;
    	
    	this.REST=new BasicRESTService("Cita", "cita");
    	
    	
    	this.REST.getMaestros=function (centroId) {
            var deferred = $q.defer();
            
            $http.get("cita/maestros/"+centroId)
                .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error citaService.getMaestros');
                    deferred.reject(errResponse);
                }
            );
            return deferred.promise;
        }
    	
    	this.REST.nueva=function(cita) {
            var deferred = $q.defer();

            $http.put("cita", cita)
                .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error while creating cita');
                    deferred.reject(errResponse);
                }
            );
            return deferred.promise;
        }
    	
    	this.REST.reprogramar=function(cita, forzar) {
            var deferred = $q.defer();
            if(angular.isUndefined(forzar)){
            	forzar=false;
            }
            $http.post("cita/reprogramar", {cita:cita, forzar:forzar})
                .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error while reprogramando cita');
                    deferred.reject(errResponse);
                }
            );
            return deferred.promise;
        }
    	
    	this.REST.capturar=function(cita) {
            var deferred = $q.defer();

            $http.post("cita/capturar", cita)
                .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error while capturando cita');
                    deferred.reject(errResponse);
                }
            );
            return deferred.promise;
        }
    	
    	this.agruparProfesionales=function(maestros){
    		var current;
    		
    		angular.forEach(maestros.profesionales, function(value, key) {
    			if(value.id==userService.current.id){
    				value.nombre+=" (YO)";  
    				value.grupo="";
    				current=value;
    			}else{
    				value.grupo="Otros profesionales";
    			}
			});
    		maestros.profesionales.splice(0, 0, {'id':'-1', 'nombre':'CUALQUIERA', grupo:''});
    		
    		return current;
    	}
    	
    	/**
    	 * Prepara los horario del list que viene del servidor a un hasmap para buscarlo rapido
    	 * Aparte le parara los metodos de compilar, run y aplicar
    	 */
    	this.prepararHorarios=function(maestros){    	
    		var generico;
    		var horarios={};
    		angular.forEach(maestros.horarios, function(value, key) {
    			horarioService.iniciarHorario(value);
    			if(value.generico==true)
    				generico=value;
    			horarios[value.id]=value;
			});    		    		
    		
    		maestros.horarios=horarios;
    		return generico;
    	}
    	
    	//TODO implementar la gestion de horarios
    	this.getHorarioForProfesional=function(profesional, horarios){
    		return horarios[0];
    	}
    	
    	//TODO implementar la gestion de horarios
    	this.getHorarioForPrestacion=function(prestacion, horarios){
    		return horarios[prestacion.horario];
    	}
    })