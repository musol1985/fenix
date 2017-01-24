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
<body data-ng-controller="loginController as ctrl">
  <div class="wrapper">
	<div class="container" data-ng-show="vista==0">
		<h1>LilApp</h1>
		<c:url var="loginUrl" value="/login" />   

		<form action="${loginUrl}" method="post" data-ng-submit="login">
			<input type="text" placeholder="Correo" name="correo">
			<input type="password" placeholder="Contraseña" name="password">
			<input type="hidden" name="${_csrf.parameterName}"   value="${_csrf.token}" />
			<input type="text" placeholder="Correo" name="pagina" ng-model="pagina">
			 
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
	<div class="container" data-ng-show="vista==1">
		<h1>LilApp</h1>
		<form method="post" name="myForm" id="myForm">
			<div>
               	Introduce tu dirección de correo para enviarte la forma de reiniciar tu contraseña olvidada
           	</div>
			
			<input class="m-t-10" required type="email" placeholder="Correo" name="correo" ng-model="correoReset">
			
			<div class="alert alert-danger" role="alert" ng-show="!myForm.correo.$pristine && myForm.correo.$invalid" >
				<p ng-show="myForm.correo.$error.required" class="help-block">El correo es obligatorio</p>
				<p ng-show="myForm.correo.$error.email" class="help-block">Debe ser un email correcto</p>
			</div>

			<div class="alert alert-danger" role="alert" data-ng-if="errorReset==true">
                	Ha ocurrido un error. Por favor, vuelve a intentarlo en unos minutos
            </div>
			 
			<button data-ng-click="enviarCorreo()" id="login-button" ng-disabled="myForm.$invalid">Enviar</button>
			
			<ul class="login-navigation">
                <li data-block="#l-forget-password" class="bgm-blue" ng-click="vista=0">Iniciar sesión</li>
            </ul>
		</form>
	</div>
	<div class="container" data-ng-show="vista==2">
		<h1>LilApp</h1>
		<form method="post">
			<div>
               	Comprueba tu bandeja de entrada de correo. Se te han enviado las instrucciones para establecer una nueva contraseña.
           	</div>
			
			<ul class="login-navigation">
                <li data-block="#l-forget-password" class="bgm-blue" ng-click="vista=0">Iniciar sesión</li>
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
          <script src="resources/vendors/bower_components/angular-cookies/angular-cookies.js"></script>

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
        <script src="resources/js/lilapp/interceptors.js"></script>
        

</body>
</html>

