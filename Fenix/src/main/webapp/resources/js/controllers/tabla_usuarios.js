materialAdmin
    .controller('tablaUsuariosCtrl', function($filter, $sce, ngTableParams, userService) {
    	var self=this;
        /*var data = [];
        
        userService.getAll().then(function(data){
        	self.data=data;
        });
        
        //Usuarios
        this.tablaUsuarios = new ngTableParams({
            page: 1,            // show first page
            count: 10          // count per page
        }, {
            total: data.length, // length of data
            getData: function($defer, params) {
            	alert("getData");
               // $defer.resolve(self.data.slice((params.page() - 1) * params.count(), params.page() * params.count()));
            }
        });*/
    	
    	this.tablaUsuarios={};
    	
    	this.tablaUsuarios.datos=[];
    	this.tablaUsuarios.tabla=new ngTableParams({
            page: 1,            // show first page
            count: 10          // count per page
        }, {
            total: self.tablaUsuarios.datos.length, // length of data
            getData: function($defer, params) {
            	userService.getAll().then(function(data){
            		alert("getData");
            		self.tablaUsuarios.datos=data;
                });
               // $defer.resolve(self.data.slice((params.page() - 1) * params.count(), params.page() * params.count()));
            }
        });

    })
