
// =========================================================================
// Horarios v0.1
// =========================================================================
materialAdmin
    .controller('horarios', function($state, $scope, $http, userService, centroService, prestacionService, errorService, modalService, $uibModal) {
    
    	$scope.getDatos=function(params, onComplete){
    		prestacionService.getByCentro(userService.getCentro().id, params.page(), params.count()).then(function(res){
    			onComplete(res.data, res.total);
            }, function(error){
            	errorService.alertaGrowl("Error al obtener horarios", 'danger');
            });
    	}

    	$scope.refrescar=function(tabla){
    		tabla.reload();		    		    		    		
    	}
    	
    	$scope.crear = function () {
    		$state.go('configuracion.horario');  
        };
         
        $scope.modificar = function (data) {
        	$scope.modal.data=angular.copy(data);
        };
        
        $scope.eliminar = function(item){
        	console.log(item);
        	errorService.alertaSiNo("Eliminar", "¿Seguro que quieres eliminar el horario?", function(){
        		errorService.procesar(prestacionService.eliminar(item.id),{
	   				 0:{
	   					 growl: true,   				 
	   					 texto: "Prestación eliminada correctamente",
	   					 tipo: "success",
	   					 onProcesar: function(){
	   						 $scope.refrescar();
	   					 }
	   				 },
	   				 1:{
	   					 titulo: "Atención",    				 
	   					 texto: "No existe la prestación",
	   					 tipo: "warning"
	   				 }
        		});
        	});
        }
    })
// =========================================================================
// Editor Horarios v0.1
// ========================================================================= 
    .controller('editorHorario', function($scope, $uibModal, modalService) {
    	$scope.modal={
			mostrar:function(){
				$scope.modalInstance=modalService.mostrar($uibModal, $scope.modal, "resources/template/editores/horario.html");
			}    			
    	}
    	
    	$scope.test=function(){
    		$scope.modal.mostrar();
    	}
    	

    	$scope.mostrarCodigo=function(){
    		alert($scope.blockly.getCode());
    	}
    	
    	$scope.toggleEditor=function(vistaPrevia){
    		$scope.editor=!$scope.editor;
    		
    		if(vistaPrevia){
    			$scope.horario={};
    			$scope.calendario.actualizar();
    			
    		}
    	}
    	
    	$scope.aplicarHorario=function(dia){
    		if($scope.horario){
    			var res=[];
    			console.log($scope.blockly.getCode());

    			var funcion=eval("("+$scope.blockly.getCode()+")");
    			return funcion(dia);
    		}    		
    	}
    	
    	/*$scope.comprobarHorario=function(moment){	
    		var huecos=[];	 
    		if(moment.month()==1){   
    			var dia=moment.format('YYYY-MM-DD');	
    			huecos.push({start:dia+' 00:00',
    				end:dia+' 23:59', 
    				id: 'disponible', 
    				color: '#257e4a'});
    			}	
    		return huecos;
    		}*/
    			
    	
    	$scope.editor=true;
    })