materialAdmin 
	// =========================================================================
    // LISTA Item
    // =========================================================================

	
	.directive('uilistaitem', function($compile, $rootScope) {
		  return {
			restrict: 'EA',
			scope: {
					titulo: '=',
			    	color: '=',
			    	descripcion: '=',
			    	subDescripcion: '='
			},    
		    templateUrl: function(element, attrs) {
		      return attrs.templateUrl || 'uib/template/listaitem.html';
		    }
		  };
	})
	
	
	