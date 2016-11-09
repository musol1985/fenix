materialAdmin 
	// =========================================================================
    // CARD
    // =========================================================================
    .controller('uicardController', function($q, $filter, $timeout,$scope, $http, ngTableParams, userService, centroService, prestacionService, errorService) {
    	
	})
	
	.directive('uicard', function($compile, $rootScope) {
		  return {
			restrict: 'EA',
			scope: {
			    	model: '=',
			    	titulo: '@',
			    	descripcion: '@',
			    	onAction: '&'
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
	
	
	