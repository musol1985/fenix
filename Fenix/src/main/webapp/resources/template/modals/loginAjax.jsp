
<div class="modal-header">
	<h4 class="modal-title">Nueva prestación</h4>
</div>
<div class="modal-body">

	<form name="myForm" class="form-horizontal" novalidate>

		<div class="row">
	
				<ui-input form="myForm" label="Nombre*" nombre="nombre" placeholder="Introduce el nombre"
					model="modalContent.data.nombre" obligatorio min="3" cols="col-sm-10" label-cols="col-sm-2"/>

		</div>
		 <div class="row">	
			<div class="col-sm-1"></div>		
			<div class="col-sm-4">
				<ui-input form="myForm" label="Importe" obligatorio min="1" nombre="importe" placeholder="Introduce el importe en €"
					model="modalContent.data.importe"/>
			</div>
			<div class="col-sm-1"></div>
			<div class="col-sm-4">
				<ui-input form="myForm" label="Duración" nombre="duracion" placeholder="Introduce la duracción en formato 00:00"
					model="modalContent.data.duracion"/>
			</div>
		</div>
		
		<div class="row">
			<div class="form-group col-sm-12">
				<label class="control-lable" for="horario">Horario</label>
				<div>
					<select class="w-100" chosen data-placeholder="Horario"  ng-options="item as item.nombre for item in modalContent.horariosModal track by item.id" ng-model="modalContent.data.horario"></select>
				</div>
			</div>
		</div>
		
		<div class="row">
			<div class="form-group col-sm-12">
				<label class="control-lable" for="color">Color</label>
				<div>
					<uicolores model="modalContent.data.color"/>
				</div>
			</div>
		</div>
		
		<!-- 
		<div class="row">
			
		</div>
		<div class="row">
			<div class="form-group col-sm-12">
				<label class="control-lable" for="horario">Horario</label>
				<div>
					<select class="w-100" chosen data-placeholder="Horario"  ng-options="item as item.nombre for item in modalContent.horariosModal track by item.id" ng-model="modalContent.data.horario"></select>
				</div>
			</div>
		</div>

		<div class="row">
			<div class="form-group col-sm-12">
				<label class="control-lable" for="color">Color</label>
				<div>
					<uicolores model="modalContent.data.color"/>
				</div>
			</div>
		</div>
-->

		<div class="modal-footer">
			<button class="btn bgm-blue btn-lg" ng-click="modalContent.guardar()"
				ng-disabled="myForm.$invalid">Iniciar sesión</button>
			<button class="btn bgm-red btn-lg" ng-click="modalContent.salir()">Salir</button>
		</div>
	</form>

</div>

