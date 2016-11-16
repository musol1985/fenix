	// =========================================================================
    // POPUP
    // =========================================================================	
	materialAdmin .controller('uiInputController', function($sce, $scope,$uibModal, modalService) {
		$scope.getFormItem=function(){
			return $scope.form[$scope.nombre];			
		}
		
	})
	
	.directive('uiInput', function($compile, $rootScope) {
		  return {
			restrict: 'EA',
			scope: {
			    	form: '=',
			    	nombre: '@',
			    	label: '@',
			    	min: '@?',
			    	max: '@?',
			    	tipo: '@?',
			    	model: '=',
			    	placeholder: '@?',
			    	extra: '=?'
			},	
			templateUrl: function(element, attrs) {
			      return attrs.templateUrl || 'uib/template/input.html';
			},
		    controller: 'uiInputController',
		    controllerAs: 'ctrl',
		    link: function(scope, element, attrs) {
		    	if(attrs.obligatorio=="")scope.obligatorio=true;
		    	
		    	if(!scope.min)scope.min="0";
		    	if(!scope.max)scope.max="3200";
		    	if(!scope.tipo)scope.tipo="text";
		   }
		    /*link: function(scope, element, attrs) {

		    	 var input = element.find('input');
		    	 if(attrs.obligatorio)
		    		 input.attr('required','true');
		    	 if(scope.minLength)
		    		 input.attr('ng-minlength',scope.minLength);
		    	 //input = $compile(input)(scope);
		    	 //element.find('input').replaceWith(input);
		    }*/
		  };
	})
	