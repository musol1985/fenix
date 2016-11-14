
// =========================================================================
// Prestaciones v12
// =========================================================================
materialAdmin
    .controller('prestaciones', function($scope, $http, userService, centroService, prestacionService, errorService, modalService, horarioService, $uibModal) {    

    	$scope.getDatos=function(params, onComplete){
    		prestacionService.REST.getByCentro(userService.getCentro().id, params.page(), params.count()).then(function(res){
    			onComplete(res.data, res.total);
            }, function(error){
            	errorService.alertaGrowl("Error al obtener prestaciones", 'danger');
            });
    		horarioService.REST.getAllByCentro(userService.getCentro().id).then(function(res){    			
    			$scope.modal.horariosModal=res.data;
    			angular.forEach(res.data, function(horario) {
    				if(horario.generico){
    					$scope.horarioGen=horario;
    					console.log("Horario generic: "+horario.id+" "+horario.nombre);
    				}    				
    			});
            }, function(error){
            	errorService.alertaGrowl("Error al obtener horarios", 'danger');
            });
    	}

    	$scope.modal={
    			data:{horario:''},    			 	
    			horariosModal:[],
    	
    			getNew:function(){
    				return {id:'', nombre:'', color:'', centro:userService.getCentro().id, horario:$scope.horarioGen};
    			},    			    		
    			
    			mostrar:function(reset){
    				if(reset){
    					$scope.modal.data=this.getNew();
    				}else{
    					$scope.modal.data.horario={id:$scope.modal.data.horario};
    				}
    				console.log($scope.modal.data);
    				$scope.modalInstance=modalService.mostrar($uibModal, $scope.modal, "resources/template/modals/prestacion.html");
    			},
    			
    			guardar:function(){    	
    				var data=$scope.modal.data;
    				var accion;
    				
    				data.horario=data.horario.id;
    				console.log($scope.modal.data);
    				
    				if(data.id==''){
    					accion=prestacionService.REST.nuevo(data);
    				}else{
    					accion=prestacionService.REST.modificar(data);
    				}
    				
    				errorService.procesar(accion,{
	    				 0:{
	    					 growl: true,   				 
	    					 texto: "Prestación creada",
	    					 tipo: "success",
	    					 onProcesar: function(){
	    						 $scope.refrescar();
	    					 }
	    				 },
	    				 1:{
		   					 titulo: "Atención",    				 
		   					 texto: "La prestación no existe",
		   					 tipo: "warning"
		   				 },
	    				 2:{
		   					 titulo: "Atención",    				 
		   					 texto: "Ya existe una prestación con ese nombre",
		   					 tipo: "warning"
		   				 },
		   				onError:function(){
		   					$scope.modal.mostrar(false);
	    				 }
    				});
    				
    				$scope.modalInstance.close();
    			},
    	        
    	        setColor : function(color){
    	        	this.data.color=color;
    	        },
    	        
    	        getColores :function(){
    	        	return   [
    	                         'lightblue',
    	                         'bluegray',
    	                         'cyan',
    	                         'teal',
    	                         'green',
    	                         'orange',
    	                         'blue',
    	                         'purple'
    	                     ]
    	        }    
    	}
    	
    	$scope.refrescar=function(tabla){
    		tabla.reload();		    		    		    		
    	}
    	
    	$scope.nuevo = function () {
    		$scope.modal.mostrar(true);
        };
         
        $scope.modificar = function (data) {
        	$scope.modal.data=angular.copy(data);
    		$scope.modal.mostrar(false);
        };
        
        $scope.eliminar = function(item){
        	console.log(item);
        	errorService.alertaSiNo("Eliminar", "¿Seguro que quieres eliminar la prestación?", function(){
        		errorService.procesar(prestacionService.REST.eliminar(item.id),{
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