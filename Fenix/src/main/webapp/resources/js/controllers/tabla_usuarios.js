materialAdmin
    .controller('tablaUsuariosCtrl', function($filter, $sce, ngTableParams, userService) {
    	var self=this;
        var data = [];
        
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
                $defer.resolve(data.slice((params.page() - 1) * params.count(), params.page() * params.count()));
            }
        });
    })
