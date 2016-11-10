
// =========================================================================
// Horarios v0.1
// =========================================================================
materialAdmin
    .controller('horarios', function($state, $scope, $http, userService, centroService, horarioService, errorService, modalService, $uibModal) {
    
    	$scope.getDatos=function(params, onComplete){
    		horarioService.REST.getByCentro(userService.getCentro().id, params.page(), params.count()).then(function(res){
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
        	$state.go('configuracion.horario',{id: data.id}); 
        };
        
        $scope.eliminar = function(item){
        	console.log(item);
        	errorService.alertaSiNo("Eliminar", "¿Seguro que quieres eliminar el horario?", function(){
        		errorService.procesar(horarioService.REST.eliminar(item.id),{
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
    .controller('editorHorario', function($state, $scope, $uibModal, modalService, horarioService, errorService) {
    	$scope.onLoaded=function(){//callback del blocky
    		return $scope.cargar($state.params.id);
    	}
    	
    	$scope.cargar=function(id){
    		if(id){
	    		accion=horarioService.REST.getEditorById(id);			
				
				errorService.procesar(accion,{
						1:{
		   				 	titulo: "Atención",    				 
		   					 texto: "El horario no existe",
		   					 tipo: "warning"
		   				 }
					}, function(res){
						console.log("Cargando editor del horario");
						$scope.blockly.cargarXML(LZString.decompressFromBase64(res.data.codigo));
				});
				
				return true;
    		}
    		console.log("Cargando editor por defecto");
    		return false;
    	} 
    	
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
    			$scope.horario=horarioService.newFromBlocky($scope.blockly);
    			$scope.calendario.actualizar();
    			
    		}
    	}
    	
    	$scope.eliminar=function(){   
    		if($state.params.id){
    			errorService.alertaSiNo("Eliminar", "¿Seguro que quieres eliminar el horario?", function(){
            		errorService.procesar(horarioService.REST.eliminar($state.params.id),{
    	   				 0:{
    	   					 growl: true,   				 
    	   					 texto: "Horario eliminado correctamente",
    	   					 tipo: "success",
    	   					 onProcesar: function(){
    	   						 $scope.refrescar();
    	   					 }
    	   				 },
    	   				 1:{
    	   					 titulo: "Atención",    				 
    	   					 texto: "No existe el horario",
    	   					 tipo: "warning"
    	   				 }
            		});
            	});
    		}
    	}
    	
    	$scope.guardar=function(){    		
    		var h=horarioService.newFromBlocky($scope.blockly);    		
    		h.horario.nombre=$scope.nombre;
    		if($state.params.id){
    			h.horario.id=$state.params.id;
    		}
    		
			accion=horarioService.REST.nuevo(h,"horario/guardar");			
			
			errorService.procesar(accion,{
				 0:{
					 growl: true,   				 
					 texto: "Horario guardado",
					 tipo: "success"
				 },
				 1:{
   					 titulo: "Atención",    				 
   					 texto: "El horario no existe",
   					 tipo: "warning"
   				 },
				 2:{
   					 titulo: "Atención",    				 
   					 texto: "Ya existe un horario con ese nombre",
   					 tipo: "warning"
   				 }
			});
    	}
    	
    	$scope.aplicarHorario=function(dia){
    		if($scope.horario){
    			return $scope.horario.aplicar(dia);    		
    		}
    	} 	    	
    	
    	$scope.editor=true;
    })