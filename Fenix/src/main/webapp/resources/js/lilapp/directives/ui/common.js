materialAdmin 
	// =========================================================================
    // CARD
    // =========================================================================
    .controller('uicardController', function($scope) {
	    if (angular.isUndefined($scope.ocultarAction))
	        $scope.ocultarAction = false;
    	
	})
	
	.directive('uicard', function($compile, $rootScope) {
		  return {
			restrict: 'EA',
			scope: {
			    	model: '=',
			    	titulo: '@',
			    	descripcion: '@',
			    	onAction: '&?',
			    	ocultarAction: '@?' 
			},		  
		    controller: 'uicardController',
		    controllerAs: 'ctrl',		    
		    templateUrl: function(element, attrs) {
		      return attrs.templateUrl || 'uib/template/card.html';
		    },
		    transclude: {
		    	'ul':'ul',		    
		    	'cuerpo':'cuerpo'
		    }
		  };
	})
	// =========================================================================
    // POPUP
    // =========================================================================	
	.controller('uipopupController', function($scope,$uibModal, modalService) {
		 alert($scope.isUpdating);
		 console.log($scope.isUpdating);
		 if (angular.isUndefined($scope.isUpdating)){
			
		 }

		$scope.popup.mostrar=function(data){
			if(data && $scope.isUpdating({data:data})){
				data=angular.copy(data);
			}else{
				data=$scope.getNew();
			}
			$scope.popup.data=data;
			console.log(data);
			$scope.modalInstance=modalService.mostrar($uibModal, $scope.popup, $scope.template);
		}

		$scope.popup.guardar=function(){
			$scope.onSave({data:$scope.popup.data, modificando:$scope.isUpdating({data:$scope.popup.data})});
			$scope.modalInstance.close();
		}
		
		$scope.popup.showPostError=function(){
			$scope.modalInstance=modalService.mostrar($uibModal, $scope.popup, $scope.template);
		}
		
		$scope.popup.refrescar=function(){
			$scope.refrescarTabla.tabla.reload();
		}
    	
	})
	
	.directive('uipopup', function($compile, $rootScope) {
		  return {
			restrict: 'EA',
			scope: {
			    	popup: '=',
			    	template: '@',
			    	getNew: '&',
			    	isUpdating: '&',
			    	onSave: '&',
			    	refrescarTabla: '='
			},		  
		    controller: 'uipopupController',
		    controllerAs: 'ctrl'
		  };
	})
	