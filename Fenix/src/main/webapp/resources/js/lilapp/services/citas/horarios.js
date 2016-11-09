materialAdmin
     // =========================================================================
    // HoirariosService
    // =========================================================================
    
    .service('horariosService', function($http, $q){
    	var self=this;

    	this.nueva=function(cita) {
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
    })