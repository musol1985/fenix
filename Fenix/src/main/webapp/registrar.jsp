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
        <link href="resources/css/login.css" rel="stylesheet">
        
    </head>
<body data-ng-controller="registrarCtrl as rctrl">
  <div class="wrapper">
	<div class="container">
		<h1>Registrarse</h1>
 
		<form novalidate>
			
			<input type="text" ng-model="user.centro" id="centro" name="centro" required readonly ng-init="user.centro = '${centro }'"/>
			<input type="text" ng-model="user.correo" id="correo" name="correo" required readonly ng-init="user.correo = '${correo }'"/>							   
			
			<div class="m-b-10">Rellena los siguientes datos:</div>
			<input type="text" placeholder="Nombre" name="password">
			<input type="password" placeholder="Contraseña" name="password">
			<input type="password" placeholder="Repetir" name="password2">
			<input type="hidden" name="${_csrf.parameterName}"   value="${_csrf.token}" />
			 
			 <c:if test="${param.error != null}">
                <div class="alert alert-danger" role="alert">
                	Usuario o contraseña incorrectos
            	</div>
            </c:if>
		
		 	<div class="checkbox">
                <label>
                    <input type="checkbox"  name="remember-me">
                    <i class="input-helper"></i>
                    Guardar datos de sesión en este equipo
                </label>
            </div>
			 
			<button data-ng-click="login()" id="login-button">Iniciar</button>

			<ul class="login-navigation">
                <li data-block="#l-forget-password" class="bgm-orange" ng-click="vista=1">He olvidado mi contraseña</li>
            </ul>
		</form>
	</div>
	
	
	<ul class="bg-bubbles">
		<li></li>
		<li></li>
		<li></li>
		<li></li>
		<li></li>
		<li></li>
		<li></li>
		<li></li>
		<li></li>
		<li></li>
	</ul>
</div>
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
        
                
        <!-- LilAPP -->  
        <script src="resources/js/lilapp/controllers.js"></script>
        <script src="resources/js/lilapp/services.js"></script>
        <script src="resources/js/lilapp/directives.js"></script>
        <script src="resources/js/lilapp/validaciones.js"></script>

</body>
</html>

