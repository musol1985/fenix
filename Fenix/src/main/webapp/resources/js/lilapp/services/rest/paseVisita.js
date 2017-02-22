materialAdmin
     // =========================================================================
    // CitaService
    // =========================================================================
    .service('paseVisitaService', function($q, $http, BasicRESTService, userService, horarioService){
    	var self=this;
    	
    	this.REST=new BasicRESTService("Visita", "visita");
    	
    	
    	this.REST.pasarVisita=function(cita) {
            var deferred = $q.defer();
 
            $http.post("visita/pasar", cita)
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
    })