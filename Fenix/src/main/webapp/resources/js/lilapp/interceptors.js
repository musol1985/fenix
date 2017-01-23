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
	    	'response': function(response) {
	    			console.log("onAjaxResponse"+response.status);
	    			console.log(response.status);
	    			if(response.status=="401"){
	    				alert("Sesion expirada!");
	    			}
	    			 /*if (typeof response.data === 'string') {
	    				 console.log("COmprobando fin de sesion!!!!!");
	                    if (response.data.indexOf instanceof Function &&
	                        response.data.indexOf('<html class="login-content" data-ng-app="materialAdmin">') != -1) {
	                        $location.path("/logout");
	                    }
	                }  */     				
	    		return response;
	    	}
		};
	    
	})