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
    
    // =========================================================================
    // ClienteService
    // =========================================================================
    
    .service('clienteService', ['$http', '$q', function($http, $q){
        this.buscar=function (texto, centro) {
            var deferred = $q.defer();
            
            $http.post("cliente/buscar",{texto:texto, centro:centro})
                .then(
                function (response) {   
                	var res=response.data.map(function(item){
                    	item.texto=item.nombre+" "+item.apellidos+", "+item.dni+", "+item.telefono;
     
                    	return item;
                    })
                    console.log(res);
                    if(res.length==0){
                    	res=[{texto:'No se ha encontrado. Pulsa enter para crear uno nuevo', id:-1, busqueda:texto}];
                    }
                    console.log(res);
                    deferred.resolve(res);
                },
                function(errResponse){
                    console.error('Error cliente.getByCentro');
                    deferred.reject(errResponse);
                }
            );
            return deferred.promise;
        }
    	
    	this.nuevo=function(cliente) {
            var deferred = $q.defer();

            $http.put("cliente", cliente)
                .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error creando un cliente');
                    deferred.reject(errResponse);
                }
            );
            return deferred.promise;
        }
    	
    	this.modificar=function(cliente) {
            var deferred = $q.defer();

            $http.post("cliente", cliente)
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
    }])
    
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
    
    // =========================================================================
    // UbicacionService
    // =========================================================================
    
    .service('ubicacionService', ['$http', '$q', function($http, $q){
    	this.getUbicacion = function(val) {
            return $http.get('//maps.googleapis.com/maps/api/geocode/json', {
                params: {
                    address: val,
                    sensor: false
                }
            }).then(function(response){
                return response.data.results.map(function(item){
                    return item;
                });
            });
        }
    	
    	this.setUbicacion=function($item, $model, $label, $event){
    		console.log($item);
    		var ubicacion={};
    		var direccion={};
    		
    		angular.forEach($item.address_components, function(address) {
    			var componente={};
    			
    			angular.forEach(address, function(value, key) {
    				if(key=="types"){
    					if(value.indexOf("locality")>-1){
    						componente.tipo="poblacion";
    					}else if(value.indexOf("route")>-1){
    						componente.tipo="calle";
    					}else if(value.indexOf("street_number")>-1){
    						componente.tipo="numero";
    					}else if(value.indexOf("administrative_area_level_2")>-1){
    						componente.tipo="provincia";
    					}else if(value.indexOf("postal_code")>-1){
    						componente.tipo="CP";
    					}else if(value.indexOf("country")>-1){
    						componente.tipo="pais";
    					}
    				}
    				if(key=="short_name"){
    					componente.valor=value;
    				}
    			});
    			
    			if(componente.tipo=="poblacion"){
    				direccion.poblacion=componente.valor;
    			}else if(componente.tipo=="calle"){
    				direccion.calle=componente.valor;
    			}else if(componente.tipo=="numero"){
    				direccion.numero=componente.valor;
    			}else if(componente.tipo=="provincia"){
    				direccion.provincia=componente.valor;
    			}else if(componente.tipo=="CP"){
    				direccion.CP=componente.valor;
    			}else if(componente.tipo=="pais"){
    				direccion.pais=componente.valor;
    			}
			});
    		
    		direccion.id=$item.place_id;
    		ubicacion.direccion=direccion;
			ubicacion.posicion=$item.geometry.location;
    		
    		console.log(ubicacion);
    		return ubicacion;
    	}
    }])
    
    // =========================================================================
    // UserService
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
    

    