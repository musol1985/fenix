// =========================================================================
// Login
// =========================================================================
materialAdmin
    .controller('loginController', function($scope, userService, $location) {    	
    	$scope.vista=0;
    	$scope.errorReset=false;

    	$scope.pagina="/app#"+$location.url();    	
    	
    	$scope.enviarCorreo=function(){
    		var respuesta=userService.resetPassword($scope.correoReset);
    		respuesta.then(function(datos) {
    				$scope.vista=2;
    			}, function(error) {
    				$scope.errorReset=true;
    			});
    		
    		
    	}
    })