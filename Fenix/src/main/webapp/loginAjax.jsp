
<div class="modal-header">
	<h4 class="modal-title">Nueva prestación</h4>
</div>
<div class="modal-body">


		<form action="/login" method="post" data-ng-submit="login">
			<input type="text" placeholder="Correo" name="correo" ng-model="modalContent.data.correo">
			<input type="password" placeholder="Contrase�a" name="password" ng-model="modalContent.data.password">
			<input type="text" name="${_csrf.parameterName}"  value="${_csrf.token}" />
			 
			 <c:if test="${param.error != null}">
                <div class="alert alert-danger" role="alert">
                	Usuario o contrase�a incorrectos
            	</div>
            </c:if>


			<ul class="login-navigation">
                <li data-block="#l-forget-password" class="bgm-orange" ng-click="vista=1">He olvidado mi contrase�a</li>
            </ul>
		</form>

		<div class="modal-footer">
			<button class="btn bgm-blue btn-lg" ng-click="modalContent.aceptar()"
				ng-disabled="myForm.$invalid">Iniciar sesión</button>
			<button class="btn bgm-red btn-lg" ng-click="modalContent.salir()">Salir</button>
		</div>
	</form>

</div>

