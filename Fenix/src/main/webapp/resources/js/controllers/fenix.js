materialAdmin
.controller('contextoCtrl', function($scope, $http, $timeout, errorService, messageService, clienteService, userService, modalService, $uibModal) {
    	
    	$scope.cliente;      
        
        this.buscarCliente=function(valor){
        	return clienteService.buscar(valor, userService.getCentro().id);        	
        	
        }
        
        this.seleccionarCliente=function($item, $model){
        	if($item.id==-1){
        		this.crearCliente($item.busqueda);
        	}else{
        		console.log("Cliente seleccionado");
        		console.log($item);
        		$scope.cliente=$item;
        	}   
        	
        	angular.element('#header').removeClass('search-toggled');
        }
        
        this.crearCliente=function(valor){
        	if(this.isDNInoLetra(valor)){
        		valor+=this.calcularLetraDNI(valor);
        	}
        	
        	var dni=this.isDNIoNIE(valor)?valor:"";
        	var apellidos="";
        	var nombre="";
        	
        	if(this.isTexto(valor)){
        		if(valor.indexOf(" ")>-1){
        			nombre=valor.split(" ")[0];
        			apellidos=valor.split(" ")[1];
        		}else{
        			nombre=valor;
        		}
        	}
        	
        	var telefono=this.isTelefono(valor)?valor:"";
        	
        	
        	var data={dni:dni, nombre:nombre, sexo:1, telefono:telefono, apellidos:apellidos, centro:userService.getCentro().id};
        	$scope.modal.mostrar(data);
        }
        
        this.calcularLetraDNI=function(valor){
        	cadena="TRWAGMYFPDXBNJZSQVHLCKET" 
    		posicion = valor % 23 
    		return cadena.substring(posicion,posicion+1) 
        }
        
        this.isDNInoLetra=function(valor){
        	var regexp = /^\d{8}$/;
        	return regexp.test(valor);  
        }
        
        this.isDNIoNIE=function(valor){
        	var regexp = /(^\d{8}[A-Z]$|[A-Z]\d{7}[A-Z])/;
        	return regexp.test(valor);        	
        }
        
        this.isTexto=function(valor){
        	var regexp =/^\D+$/;
        	return regexp.test(valor);        	
        }
        
        this.isTelefono=function(valor){
        	var regexp = /^6\d{8}$/;
        	return regexp.test(valor);        	
        }
        
        this.hasCliente=function(){
        	return $scope.cliente!=undefined;
        }
        
        this.getCliente=function(){
        	return $scope.cliente;
        }
        
        this.getNombreCliente=function(){
        	if(this.hasCliente())
        		return this.getCliente().nombre+" "+this.getCliente().apellidos;
        	return "";
        }
        
        this.cancelarSeleccion=function(){
        	$scope.cliente=undefined;
        }
        
        this.modificarCliente=function(){
        	if(this.hasCliente()){
        		$scope.modal.mostrar($scope.cliente);
        	}
        }
        
        
        $scope.modal={
    			data:{}, 			    		
    			
    			mostrar:function(datos){
    				$scope.modal.data=datos;
    				$scope.modalInstance=modalService.mostrar($uibModal, $scope.modal, "resources/template/modals/cliente.html");
    			},
    			
    			guardar:function(seleccionar){    				
    				var data=$scope.modal.data;
    				var accion;
    				

    				accion=clienteService.nuevo(data);

    				
    				errorService.procesar(accion,{
	    				 0:{
	    					 growl: true,   				 
	    					 texto: "Cliente creado",
	    					 tipo: "success",
	    					 onProcesar: function(res){
	    						 if(seleccionar)
	    							 $scope.cliente=res.data;
	    					 }
	    				 },
	    				 2:{
		   					 titulo: "Atención",    				 
		   					 texto: "Ya existe un cliente con ese DNI o NIE",
		   					 tipo: "warning"
		   				 },
		   				onError:function(){
		   					$scope.modal.mostrar(data);
	    				 }
    				});
    				
    				$scope.modalInstance.close();
    			}   
    	}
    	

})
