// =========================================================================
// Citas
// =========================================================================
materialAdmin
    .controller('paseVisitaController', function($scope, $compile, $document, paseVisitaService, userService) {
    	
    	$scope.ctr.pagos={};
    	$scope.ctr.facturas={};
    	
    	$scope.visita={

    	}

    	$scope.guardar=function(){
    		alert("guardando...");
    	}
    	
    	
    	if($scope.capturando){
    		paseVisitaService.REST.pasarVisita({id:$scope.citaId, centro:userService.getCentro().id}).then(function(res){
    			if(res.cod==0){
    				$scope.visita=res.data;
    				$scope.ctr.pagos.tabla.reload();
    				$scope.ctr.facturas.tabla.reload();	
    			}
    		}, function(error){
    			errorService.alertaGrowl("Error al obtener los maestros", 'danger');
    		});
    	}
    	
    	$scope.getPagos=function(params, onComplete){
    		if($scope.visita.id){
    			onComplete($scope.visita.facturacion.pagos, $scope.visita.facturacion.pagos.length);
    		}else{
    			onComplete([{importe:37.5}],1);
    		}    		
    	}
    	
    	$scope.getFacturas=function(params, onComplete){
    		if($scope.visita.id){
    			onComplete($scope.visita.facturacion.factura, $scope.visita.facturacion.factura.length);
    		}else{
    			onComplete([{importe:37.5}],1);
    		}    		
    	}
    })