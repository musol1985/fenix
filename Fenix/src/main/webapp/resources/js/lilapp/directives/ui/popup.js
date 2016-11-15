	// =========================================================================
    // POPUP
    // =========================================================================	
	materialAdmin .controller('uipopupController', function($scope,$uibModal, modalService) {
		$scope.modificando=function(data){
			if(!data)
				return false;
			
			if($scope.isUpdating){
				return $scope.isUpdating({data:data});
			}else if(data.id && data.id!=''){
				return true;
			}
			return false;
		}

		$scope.popup.mostrar=function(data){ 
			if($scope.modificando(data)){
				data=angular.copy(data);
			}
			
			$scope.popup.data=data;

			$scope.modalInstance=modalService.mostrar($uibModal, $scope.popup, $scope.template);
		}

		$scope.popup.guardar=function(){
			var params={data:$scope.popup.data};
			if($scope.onPreSave)
				$scope.onPreSave(params);
			
			var accion;
			var modificando=false;
			
			if(!$scope.modificando(params.data)){
				accion=$scope.onNew(params);
			}else{
				accion=$scope.onUpdate(params);
				modificando=true;
			}
			
			if($scope.onPostSave && accion)
				$scope.onPostSave({data: params.data, accion:accion, modificando:modificando})

			$scope.modalInstance.close();
		}
		
		$scope.popup.showPostError=function(){
			$scope.modalInstance=modalService.mostrar($uibModal, $scope.popup, $scope.template);
		}
		
		$scope.popup.refrescar=function(){
			$scope.refrescarTabla.tabla.reload();
		}
    	
	})
	
	.directive('uiPopup', function($compile, $rootScope) {
		  return {
			restrict: 'EA',
			scope: {
			    	popup: '=',
			    	template: '@',
			    	isUpdating: '&?',
			    	onNew: '&',
			    	onUpdate: '&',
			    	onPreSave: '&?',
			    	onPostSave: '&?',
			    	refrescarTabla: '='
			},		  
		    controller: 'uipopupController',
		    controllerAs: 'ctrl'
		  };
	})
	