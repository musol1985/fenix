// =========================================================================
// Ajax
// =========================================================================
materialAdmin
	.factory('ajaxInterceptor', function($location){
	    return{// optional method
	    	'request': function(config) {
	    			config.headers['lilap-is-ajax'] = true
	    			return config;
	    	    },	    
	    	'responseError': function(rejection) {
	            // do something on error
	            if(rejection.status === 401){
	            	alert("Sesion expirada!");                
	            }
	            return rejection;
	         }
		};
	    
	})