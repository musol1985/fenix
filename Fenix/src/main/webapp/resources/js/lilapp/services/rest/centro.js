materialAdmin
    // =========================================================================
    // CentroService
    // =========================================================================
    
    .service('centroService', ['$http', '$q', function($http, $q){
    	var self=this;

    	this.nuevoCentro=function(centro) {
            var deferred = $q.defer();

            $http.post("centro/nuevo", centro)
                .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error while fetching Users');
                    deferred.reject(errResponse);
                }
            );
            return deferred.promise;
        }
    	
    	this.getAll=function() {
            var deferred = $q.defer();

            $http.get("centro/all")
                .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error while fetching Users');
                    deferred.reject(errResponse);
                }
            );
            return deferred.promise;
        }
    	
    }])