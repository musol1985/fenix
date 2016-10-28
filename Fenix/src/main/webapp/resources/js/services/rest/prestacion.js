materialAdmin
    // =========================================================================
    // PrestacionService
    // =========================================================================
    
    .service('prestacionService', ['$http', '$q', function($http, $q){
        this.getByCentro=function (centro, page, size) {
            var deferred = $q.defer();
            
            $http.get("prestacion/"+centro+"/"+page+"/"+size)
                .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error prestacion.getByCentro');
                    deferred.reject(errResponse);
                }
            );
            return deferred.promise;
        }
        
        this.getAllByCentro=function (centro) {
            var deferred = $q.defer();
            
            $http.get("prestacion/all/"+centro)
                .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error prestacion.getByCentro');
                    deferred.reject(errResponse);
                }
            );
            return deferred.promise;
        }
    	
    	this.get=function(id) {
            var deferred = $q.defer();

            $http.get("prestacion/"+id)
                .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error prsestacion.get');
                    deferred.reject(errResponse);
                }
            );
            return deferred.promise;
        }
    	
    	this.nueva=function(prestacion) {
            var deferred = $q.defer();

            $http.put("prestacion", prestacion)
                .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error creando una prestacion');
                    deferred.reject(errResponse);
                }
            );
            return deferred.promise;
        }
    	
    	this.modificar=function(prestacion) {
            var deferred = $q.defer();

            $http.post("prestacion", prestacion)
                .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error modificando una prestacion');
                    deferred.reject(errResponse);
                }
            );
            return deferred.promise;
        }
    	
    	this.eliminar=function(id) {
            var deferred = $q.defer();

            $http.delete("prestacion/"+id)
                .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error eliminando una prestacion');
                    deferred.reject(errResponse);
                }
            );
            return deferred.promise;
        }
    }])