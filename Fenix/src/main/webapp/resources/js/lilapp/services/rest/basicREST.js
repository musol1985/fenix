materialAdmin
    // =========================================================================
    // PrestacionService
    // =========================================================================
    
    .factory('BasicRESTService',  function($http, $q){
    	var self = function (id, url){
    		this.getByCentro=function (centro, page, size) {
                var deferred = $q.defer();
                
                $http.get(url+"/"+centro+"/"+page+"/"+size)
                    .then(
                    function (response) {
                        deferred.resolve(response.data);
                    },
                    function(errResponse){
                        console.error('Error '+id+'.getByCentro');
                        deferred.reject(errResponse);
                    }
                );
                return deferred.promise;
            }
            
            this.getAllByCentro=function (centro) {
                var deferred = $q.defer();
                
                $http.get(url+"/all/"+centro)
                    .then(
                    function (response) {
                        deferred.resolve(response.data);
                    },
                    function(errResponse){
                        console.error('Error '+id+'.getByCentro');
                        deferred.reject(errResponse);
                    }
                );
                return deferred.promise;
            }
        	
        	this.get=function(id) {
                var deferred = $q.defer();

                $http.get(url+"/"+id)
                    .then(
                    function (response) {
                        deferred.resolve(response.data);
                    },
                    function(errResponse){
                        console.error('Error '+id+'.get');
                        deferred.reject(errResponse);
                    }
                );
                return deferred.promise;
            }
        	
        	this.nuevo=function(item, customURL) {
                var deferred = $q.defer();
                
                var u=url;
                
                if(customURL)
                	u=customURL;

                $http.put(u, item)
                    .then(
                    function (response) {
                        deferred.resolve(response.data);
                    },
                    function(errResponse){
                        console.error('Error '+id+'.nueva');
                        deferred.reject(errResponse);
                    }
                );
                return deferred.promise;
            }
        	
        	this.modificar=function(item) {
                var deferred = $q.defer();

                $http.post(url, item)
                    .then(
                    function (response) {
                        deferred.resolve(response.data);
                    },
                    function(errResponse){
                        console.error('Error '+id+'.modificar');
                        deferred.reject(errResponse);
                    }
                );
                return deferred.promise;
            }
        	
        	this.eliminar=function(id) {
                var deferred = $q.defer();

                $http.delete(url+"/"+id)
                    .then(
                    function (response) {
                        deferred.resolve(response.data);
                    },
                    function(errResponse){
                        console.error('Error '+id+'.eliminar');
                        deferred.reject(errResponse);
                    }
                );
                return deferred.promise;
            }
    		
    		
            return this;
        }

        return self;
    })