<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
    <html class="login-content" data-ng-app="materialAdmin">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Fenix App</title>

        <!-- Vendor CSS -->
        <link href="resources/vendors/bower_components/animate.css/animate.min.css" rel="stylesheet">
        <link href="resources/vendors/bower_components/material-design-iconic-font/dist/css/material-design-iconic-font.min.css" rel="stylesheet">

        <!-- CSS -->
        <link href="resources/css/app.min.1.css" rel="stylesheet">
        <link href="resources/css/app.min.2.css" rel="stylesheet">
    </head>

    <body class="login-content" >
        <!-- Register -->
        <div class="lc-block toggled" id="l-register" data-ng-controller="registrarCtrl as rctrl">
        	<img class="lcb-user" src="resources/img/profile-pics/1471099531_Swift.png" alt="">
        	 <c:url var="loginUrl" value="/login" />    
        	<form name="myForm" id="myForm" class="form-horizontal" novalidate action="${loginUrl}" method="post">
        		<div class="input-group p-t-30">
	                <span class="input-group-addon"><i class="zmdi zmdi-store"></i></span>
                	<input type="text" ng-model="user.centro" id="centro" name="centro" class="form-control" required readonly ng-init="user.centro = '${centro }'"/>							   
	            </div>
	        	<div class="input-group m-b-20">
	        		<input type="hidden" ng-model="user.idPendiente" id="idPendiente" name="idPendiente" ng-init="user.idPendiente= '${id }'"/>
	                <span class="input-group-addon"><i class="zmdi zmdi-email"></i></span>
                		
                          <input type="text" ng-model="user.correo" id="correo" name="correo" class="form-control" required readonly ng-init="user.correo = '${correo }'"/>							   
					
	            </div>
	            <div class="input-group m-b-20">
                   	<span class="input-group-addon"><i class="zmdi zmdi-account"></i></span>
                   	<div class="fg-line">                   		
                          <input type="text" ng-model="user.nombre" id="nombre" name="nombre" class="form-control" placeholder="Introduce tu nombre" required ng-minlength="3" ng-init="user.nombre = '${nombre }'"/>							   
					</div>
                </div>
                <div ng-show="!myForm.nombre.$pristine" class="has-error input-group-addon ">
					<p ng-show="myForm.nombre.$error.required" class="help-block">Este campo es obligatorio</p>
			        <p ng-show="myForm.nombre.$error.minlength" class="help-block">Tamaño minimo de 3 caracteres</p>
		        </div> 
                
                <div class="input-group m-b-20">
                   	<span class="input-group-addon"><i class="zmdi zmdi-male"></i></span>
                   	<div class="fg-line">                   		
                          <input type="password" ng-model="user.password" id="password" name="password" class="form-control" placeholder="Contraseña" required ng-minlength="3"/>							   
					</div>
                </div>
               	<div ng-show="!myForm.password.$pristine" class="has-error input-group-addon ">
					<p ng-show="myForm.password.$error.required" class="help-block">Este campo es obligatorio</p>
			        <p ng-show="myForm.password.$error.minlength" class="help-block">Tamaño minimo de 3 caracteres</p>
		        </div> 
                
                
                <div class="input-group">
                   	<span class="input-group-addon"><i class="zmdi zmdi-male"></i></span>
                   	<div class="fg-line">                   		
                          <input type="password" ng-model="password2" id="password2" name="password2" class="form-control" placeholder="Confirmar contraseña" pw-check="password" required ng-minlength="3"/>							   
					</div>					
                </div>
                <div class="has-error input-group-addon ">
	               	<div ng-show="!myForm.password2.$pristine">
						<p ng-show="myForm.password2.$error.required" class="help-block">Este campo es obligatorio</p>
				        <p ng-show="myForm.password2.$error.minlength" class="help-block">Tamaño minimo de 3 caracteres</p>
			        </div> 
			        <div ng-show="!myForm.password.$pristine">
			        	<p ng-show="myForm.password2.$error.pwmatch" class="help-block">Las contraseñas no coinciden</p>
			        </div>
		        </div>
	
	            <div class="clearfix"></div>
	
				<div ng-class="{ 'has-error' : myForm.tcRead.$invalid, 'has-success': !myForm.nombre.$invalid}">
		            <div class="checkbox" >
		                <label>
		                	<input type="checkbox" name="tcRead" id="tcRead" ng-model="readTermsConditions" ng-true-value="2" check-required />
		                    <i class="input-helper"></i>
		                    He leído y acepto la licencia de uso
		                </label>
		                <div ng-show="!myForm.tcRead.$pristine" >
							<p ng-show="myForm.tcRead.$error.checkRequired" class="help-block">
								Debes aceptar la licencia
					        </p>
				        </div>
		            </div>
				</div>
				
				<div class="alert alert-danger m-t-20" role="alert" data-ng-if="error==true">Ha ocurrido un error: {{errorDesc}}</div>
				
	            <a ng-href="" class="btn btn-login btn-danger btn-float" ng-disabled="myForm.$invalid" ng-click="registrar()"><i class="zmdi zmdi-arrow-forward"></i></a>
            </form>
        </div>

        <!-- Older IE warning message -->
        <!--[if lt IE 9]>
            <div class="ie-warning">
                <h1 class="c-white">Warning!!</h1>
                <p>You are using an outdated version of Internet Explorer, please upgrade <br/>to any of the following web browsers to access this website.</p>
                <div class="iew-container">
                    <ul class="iew-download">
                        <li>
                            <a href="resources/http://www.google.com/chrome/">
                                <img src="resources/img/browsers/chrome.png" alt="">
                                <div>Chrome</div>
                            </a>
                        </li>
                        <li>
                            <a href="resources/https://www.mozilla.org/en-US/firefox/new/">
                                <img src="resources/img/browsers/firefox.png" alt="">
                                <div>Firefox</div>
                            </a>
                        </li>
                        <li>
                            <a href="resources/http://www.opera.com">
                                <img src="resources/img/browsers/opera.png" alt="">
                                <div>Opera</div>
                            </a>
                        </li>
                        <li>
                            <a href="resources/https://www.apple.com/safari/">
                                <img src="resources/img/browsers/safari.png" alt="">
                                <div>Safari</div>
                            </a>
                        </li>
                        <li>
                            <a href="resources/http://windows.microsoft.com/en-us/internet-explorer/download-ie">
                                <img src="resources/img/browsers/ie.png" alt="">
                                <div>IE (New)</div>
                            </a>
                        </li>
                    </ul>
                </div>
                <p>Sorry for the inconvenience!</p>
            </div>
        <![endif]-->


        <!-- Core -->
        <script src="resources/vendors/bower_components/jquery/dist/jquery.min.js"></script>

        <!-- Angular -->
        <script src="resources/vendors/bower_components/angular/angular.min.js"></script>
        <script src="resources/vendors/bower_components/angular-resource/angular-resource.min.js"></script>
        <script src="resources/vendors/bower_components/angular-animate/angular-animate.min.js"></script>
        <script src="resources/vendors/bower_components/angular-resource/angular-resource.min.js"></script>

        
        <!-- Angular Modules -->
        <script src="resources/vendors/bower_components/angular-ui-router/release/angular-ui-router.min.js"></script>
        <script src="resources/vendors/bower_components/angular-loading-bar/src/loading-bar.js"></script>
        <script src="resources/vendors/bower_components/oclazyload/dist/ocLazyLoad.min.js"></script>
        <script src="resources/vendors/bower_components/angular-bootstrap/ui-bootstrap-tpls.min.js"></script>

        <!-- Common Vendors -->
        <script src="resources/vendors/bower_components/jquery.nicescroll/jquery.nicescroll.min.js"></script>
        <script src="resources/vendors/bower_components/Waves/dist/waves.min.js"></script>
        <script src="resources/vendors/bower_components/angular-nouislider/src/nouislider.min.js"></script>
        <script src="resources/vendors/bower_components/ng-table/dist/ng-table.min.js"></script>
        <script src="resources/vendors/bootstrap-growl/bootstrap-growl.min.js"></script>

        <!-- Placeholder for IE9 -->
        <!--[if IE 9 ]>
            <script src="resources/vendors/bower_components/jquery-placeholder/jquery.placeholder.min.js"></script>
        <![endif]-->
                
        <!-- App level -->
        <script src="resources/js/app.js"></script>
        <script src="resources/js/config.js"></script>
        <script src="resources/js/controllers/main.js"></script>
        <script src="resources/js/services.js"></script>
        <script src="resources/js/services/rest.js"></script>
        <script src="resources/js/templates.js"></script>
        <script src="resources/js/controllers/ui-bootstrap.js"></script>
        <script src="resources/js/controllers/table.js"></script>


        <!-- Template Modules -->
        <script src="resources/js/modules/template.js"></script>
        <script src="resources/js/modules/ui.js"></script>
        <script src="resources/js/modules/form.js"></script>
    </body>
</html>
