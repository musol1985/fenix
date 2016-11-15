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