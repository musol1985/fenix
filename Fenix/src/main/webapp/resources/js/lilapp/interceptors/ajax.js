// =========================================================================
// Ajax
// =========================================================================
materialAdmin
	.factory('ajaxInterceptor', function($rootScope){
	    return{// optional method
	    	'request': function(config) {
	    			config.headers['lilap-is-ajax'] = true
	    			return config;
	    	    },	    
	    	'responseError': function(rejection) {
	            // do something on error
	            if(rejection.status === 401){
	            	console.log("Sesion expirada!");
	            	$rootScope.$broadcast('onSesionExpirada', rejection);
	            }
	            return rejection;
	         }
		};
	    
	})