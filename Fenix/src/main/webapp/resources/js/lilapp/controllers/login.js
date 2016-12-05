// =========================================================================
// Login
// =========================================================================
materialAdmin
    .controller('loginController', function($scope, userService) {    	
    	$scope.vista=0;
    	$scope.errorReset=false;
    	
    	
    	$scope.enviarCorreo=function(){
    		var respuesta=userService.resetPassword($scope.correoReset);
    		respuesta.then(function(datos) {
    				$scope.vista=2;
    			}, function(error) {
    				$scope.errorReset=true;
    			});
    		
    		
    	}
    })