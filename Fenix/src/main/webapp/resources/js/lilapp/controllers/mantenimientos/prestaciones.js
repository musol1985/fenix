
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
    			$scope.popup.horariosModal=res.data;
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
    	
    	$scope.refrescar=function(tabla){
    		tabla.reload();		    		    		    		
    	}
    	
    	$scope.popup={};
    	
    	$scope.nuevo = function () {
    		var data={id:'', nombre:'', color:'', centro:userService.getCentro().id, horario:$scope.horarioGen};
    		$scope.popup.mostrar(data);
        };
        
        $scope.modificar = function (data) {
        	if(!data.horario.id)data.horario={id:data.horario};
        	$scope.popup.mostrar(data);
        };
		
		$scope.onPreGuardar=function(data){
			data.horario=data.horario.id;
		}
		
		$scope.onNuevo=function(data){
			return prestacionService.REST.nuevo(data);
		}
		
		$scope.onModificar=function(data){
			return prestacionService.REST.modificar(data);
		}
		
		$scope.onPostGuardar=function(data, accion, modificando){
			var txtOK=modificando?"modificada":"creada";
			
			errorService.procesar(accion,{
				 0:{
					 growl: true, texto: "Prestación "+txtOK, tipo: "success",
					 onProcesar: function(){
						 $scope.popup.refrescar();
					 }
				 },
				 1:{
  					 titulo: "Atención", texto: "La prestación no existe", tipo: "warning"
  				 },
				 2:{
  					 titulo: "Atención", texto: "Ya existe una prestación con ese nombre", tipo: "warning"
  				 },
  				onError:function(){
  					data.horario={id:data.horario};
  					$scope.popup.showPostError();
				 }
			});
		}
       
        $scope.eliminar = function(item){
        	console.log(item);
        	errorService.alertaSiNo("Eliminar", "¿Seguro que quieres eliminar la prestación?", function(){
        		errorService.procesar(prestacionService.REST.eliminar(item.id),{
	   				 0:{
	   					 growl: true, texto: "Prestación eliminada correctamente",tipo: "success",
	   					 onProcesar: function(){
	   						 $scope.refrescar();
	   					 }
	   				 },
	   				 1:{ titulo: "Atención",texto: "No existe la prestación", tipo: "warning"}
        		});
        	});
        }
    })