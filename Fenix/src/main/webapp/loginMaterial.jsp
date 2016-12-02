<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
    <html class="login-content" data-ng-app="materialAdmin">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>LilApp</title>

        <!-- Vendor CSS -->
        <link href="resources/vendors/bower_components/animate.css/animate.min.css" rel="stylesheet">
        <link href="resources/vendors/bower_components/material-design-iconic-font/dist/css/material-design-iconic-font.min.css" rel="stylesheet">

        <!-- CSS -->
        <link href="resources/css/app.css" rel="stylesheet">
    </head>

    <body class="login-content" data-ng-controller="loginCtrl as lctrl">

        <!-- Login -->
        <c:url var="loginUrl" value="/login" />        
	        <div class="lc-block" id="l-login" data-ng-class="{ 'toggled': lctrl.login === 1 }" data-ng-if="lctrl.login === 1">
	        	<form action="${loginUrl}" method="post" data-ng-submit="login">
	        
	        		<c:if test="${param.error != null}">
	                    <div class="alert alert-danger" role="alert">
	                    	Usuario o contraseña incorrectos
	                	</div>
	                </c:if>
		        
		            <div class="input-group m-b-20">
		                <span class="input-group-addon"><i class="zmdi zmdi-account"></i></span>
		                <div class="fg-line">
		                    <input type="email" class="form-control" placeholder="Correo" name="correo">
		                </div>
		            </div>
		
		            <div class="input-group m-b-20">
		                <span class="input-group-addon"><i class="zmdi zmdi-male"></i></span>
		                <div class="fg-line">
		                    <input type="password" class="form-control" placeholder="Contraseña" name="password">
		                </div>
		            </div>
		            
		            <input type="hidden" name="${_csrf.parameterName}"   value="${_csrf.token}" />
	                             
		
		            <div class="clearfix"></div>
		
		            <div class="checkbox">
		                <label>
		                    <input type="checkbox"  name="remember-me">
		                    <i class="input-helper"></i>
		                    Guardar datos de sesión en este equipo
		                </label>
		            </div>
		
		            <button data-ng-click="login()" class="btn btn-login btn-danger btn-float"><i class="zmdi zmdi-arrow-forward"></i></button>
		
		            <ul class="login-navigation">
		                <li data-block="#l-forget-password" class="bgm-orange" data-ng-click="lctrl.login = 0; lctrl.forgot = 1">He olvidado mi contraseña</li>
		            </ul>
	            </form>
	        </div>

        <!-- Forgot Password -->
        <div class="lc-block" id="l-forget-password" data-ng-class="{ 'toggled': lctrl.forgot === 1 }" data-ng-if="lctrl.forgot === 1">
            <p class="text-left">Introduce tu correo electronico para resetear tu contraseña</p>

            <div class="input-group m-b-20">
                <span class="input-group-addon"><i class="zmdi zmdi-email"></i></span>
                <div class="fg-line">
                    <input type="email" class="form-control" placeholder="Correo">
                </div>
            </div>

            <a href="resources/" class="btn btn-login btn-danger btn-float"><i class="zmdi zmdi-arrow-forward"></i></a>

            <ul class="login-navigation">
                <li data-block="#l-login" class="bgm-green" data-ng-click="lctrl.forgot = 0; lctrl.login = 1">Iniciar sesión</li>                
            </ul>
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

        <!-- Placeholder for IE9 -->
        <!--[if IE 9 ]>
            <script src="resources/vendors/bower_components/jquery-placeholder/jquery.placeholder.min.js"></script>
        <![endif]-->
                
        <!-- App level -->
        <script src="resources/js/app.js"></script>
        <script src="resources/js/config.js"></script>
        <script src="resources/js/controllers/main.js"></script>
        <script src="resources/js/services.js"></script>
        <script src="resources/js/templates.js"></script>
        <script src="resources/js/controllers/ui-bootstrap.js"></script>
        <script src="resources/js/controllers/table.js"></script>


        <!-- Template Modules -->
        <script src="resources/js/modules/template.js"></script>
        <script src="resources/js/modules/ui.js"></script>
        <script src="resources/js/modules/form.js"></script>
    </body>
</html>
