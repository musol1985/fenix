materialAdmin 
	// =========================================================================
    // COLORES
    // =========================================================================
    .controller('uiColoresController', function($scope) {
	    if (angular.isUndefined($scope.colores)){
	    	 $scope.colores = [
	             'teal',
	             'red',
	             'pink',
	             'blue',
	             'lime',
	             'green',
	             'cyan',
	             'orange',
	             'purple',
	             'gray',
	             'black',
	         ]
	    }	
	    
	    $scope.onClickColor=function(tag, $index){
	    	$scope.model=tag;
	    	$scope.activeState = $index;
	    	if($scope.onSeleccionar)
	    		$scope.onSeleccionar({color:tag, index:$index});	    	
	    }
	    
	    function iniciar(){
	    	$scope.activeState=$scope.colores.indexOf($scope.model);
	    }
	    
	    iniciar();	    
	})
	
	.directive('uicolores', function($compile, $rootScope) {
		  return {
			restrict: 'EA',
			scope: {
			    	model: '=',
			    	colores: '@?',
			    	onSeleccionar: '&?'
			},		  
		    controller: 'uiColoresController',
		    controllerAs: 'ctrl',		    
		    templateUrl: function(element, attrs) {
		      return attrs.templateUrl || 'uib/template/colores.html';
		    }
		  };
	})
	
	
	