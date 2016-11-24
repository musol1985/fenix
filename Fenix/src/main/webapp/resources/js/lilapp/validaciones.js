materialAdmin
	.directive('pwCheck', [function () {
	    return {
	        require: 'ngModel',
	        link: function (scope, elem, attrs, ctrl) {
	            var firstPassword = '#' + attrs.pwCheck;
	            elem.add(firstPassword).on('keyup', function () {
	                scope.$apply(function () {
	                    // console.info(elem.val() === $(firstPassword).val());
	                    ctrl.$setValidity('pwmatch', elem.val() === $(firstPassword).val());
	                });
	            });
	        }
	    }
	}])
	.directive('horaCheck', [function () {
	    return {
	        require: 'ngModel',
	        link: function (scope, elem, attrs, ctrl) {
	        	if(attrs.menorQue){
		            var otro = '#' + attrs.menorQue;
		            elem.add(otro).on('keyup', function () {
		                scope.$apply(function () {
		                    ctrl.$setValidity('horaMenor', elem.val() === $(otro).val());
		                });
		            });
	        	}
	        }
	    }
	}])
	
    