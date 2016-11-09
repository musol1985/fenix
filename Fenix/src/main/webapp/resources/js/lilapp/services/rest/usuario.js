materialAdmin
    // =========================================================================
    // UserService
    // =========================================================================
    
    .service('userService', ['$http', '$q', function($http, $q){
    	var self=this;
    	
    	this.current={}
    	
        this.getCurrentUser=function getCurrentUser() {
            var deferred = $q.defer();
            
            $http.get("usuario/current")
                .then(
                function (response) {
                	self.current=response.data;
                    deferred.resolve(response.data);
                },
                function(errResponse){
                	self.current={}
                    console.error('Error while fetching Users');
                    deferred.reject(errResponse);
                }
            );
            return deferred.promise;
        }
    	
    	this.getCentro=function(){
    		return this.current.centro;
    	}
    	
    	this.getAll=function(page, size) {
            var deferred = $q.defer();

            $http.get("usuario/all/"+page+"/"+size)
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
    	
    	this.getByCentro=function(page, size, centro) {
            var deferred = $q.defer();

            $http.get("usuario/"+centro+"/"+page+"/"+size)
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
    	
    	this.getAllByCentro=function(page, size, centro) {
            var deferred = $q.defer();

            $http.get("usuario/all/"+centro+"/"+page+"/"+size)
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
    	
    	this.getListaByCentro=function(centro) {
            var deferred = $q.defer();

            $http.get("usuario/lista/"+centro)
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
    	
    	this.getPendientes=function(page, size, centro) {
            var deferred = $q.defer();

            $http.get("usuario/pendientes/"+centro+"/"+page+"/"+size)
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
    	
    	this.nuevoPendiente=function(user) {
            var deferred = $q.defer();

            $http.post("usuario/pendiente", user)
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
    	
    	this.registrar=function(user) {
            var deferred = $q.defer();

            $http.post("usuario/registrar", user)
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
    	
    	this.eliminarPendiente=function(id) {
            var deferred = $q.defer();

            $http.delete("usuario/pendiente/"+id)
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
    	
    	this.eliminar=function(id) {
            var deferred = $q.defer();

            $http.delete("usuario/"+id)
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

    	this.enviarCorreo=function(id) {
            var deferred = $q.defer();

            $http.get("usuario/pendiente/correo/"+id)
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