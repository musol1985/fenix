// =========================================================================
// Login
// =========================================================================
materialAdmin
    .controller('registrarCtrl', function($scope, userService, $window, errorService){
    	$scope.user={};
    	$scope.error=false;
    	$scope.errorDesc="";

    	$scope.registrar=function(){   

    		if(!$scope.myForm.$valid)
    			return;
    		console.log($scope.user);
    		userService.registrar($scope.user).then(function(response){
	    			if(response.cod==0){
	    				angular.element('#myForm').submit();
	    			}else if(response.cod==1){
	    				$scope.error=true;
	    				$scope.errorDesc="El correo no tiene registros pendientes";
	    			}else if(response.cod==2){
	    				$scope.error=true;
	    				$scope.errorDesc="Ya existe un usuario registrado con ese correo";
	    			}
	    		}, function(errResponse){
	    			$scope.error=true;
    				$scope.errorDesc="Error desconocido";
	    		}
    		);
    	
    	}
    })