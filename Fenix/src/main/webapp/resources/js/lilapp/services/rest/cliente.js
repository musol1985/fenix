materialAdmin
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
    