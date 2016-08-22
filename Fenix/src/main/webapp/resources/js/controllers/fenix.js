materialAdmin
.controller('contextoCtrl', function($scope, $http, $timeout, messageService, clienteService, userService) {
    	
    	$scope.cliente={};
        // Any function returning a promise object can be used to load values asynchronously
        $scope.getLocation = function(val) {
            return $http.get('//maps.googleapis.com/maps/api/geocode/json', {
                params: {
                    address: val,
                    sensor: false
                }
            }).then(function(response){
                return response.data.results.map(function(item){
                    return item.formatted_address;
                });
            });
        }
        
        
        this.buscarCliente=function(valor){
        	return clienteService.buscar(valor, userService.getCentro().id);        	
        	
        }
        
        this.seleccionarCliente=function($item, $model){
        	
        }
})
