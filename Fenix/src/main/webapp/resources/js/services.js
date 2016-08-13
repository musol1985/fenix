materialAdmin

    // =========================================================================
    // Header Messages and Notifications list Data
    // =========================================================================

    .service('messageService', ['$resource', function($resource){
        this.getMessage = function(img, user, text) {
            var gmList = $resource("data/messages-notifications.json");
            
            return gmList.get({
                img: img,
                user: user,
                text: text
            });
        }
    }])
    
    // =========================================================================
    // ErrorService
    // =========================================================================
    
    .service('errorService', ['growlService', function(growlService){
    	var self=this;
    
    	this.procesar = function(ajax, mensajes, onProcesar){
    		ajax.then(
	    		function(response){
	    			if(response.cod>-1){
		    			var t=mensajes[response.cod];
		    			if(t){
		    				if(t.growl && t.growl==true){
		    					self.alertaGrowl(t.texto, t.tipo, t.onProcesar, response);
		    				}else{
		    					self.alerta(t.titulo, t.texto, t.tipo, t.onProcesar, response);
		    				}
		    			}
		    			if(onProcesar)
		    				onProcesar(response);
	    			}else{
	    				self.alerta("Error", "Error desconocido", "error", mensajes.onError);
	    			}
	    		},
	    		function (errResponse){
	    			self.alerta("Error", "Error desconocido", "error", mensajes.onError);
	    		});
    	}
    	
    	this.alerta=function(titulo, texto, tipo, onClick, response){
    		swal({   
                title: titulo,   
                text: texto,   
                type: tipo
            }, function(){
            	if(onClick)
            		onClick(response);            	
            });
    	}
    	
    	this.alertaSiNo=function(titulo, texto, onClick){
    		swal({   
                title: titulo,   
                text: texto,   
                type: 'warning',
                showCancelButton: true,   
                confirmButtonColor: "#DD6B55",   
                confirmButtonText: "Si", 
                cancelButtonText: "No"  
            }, function(){
            	if(onClick)
            		onClick();            	
            });
    	}
    	
    	this.alertaGrowl=function(texto, tipo, onProcesar, response){
    		growlService.growl(texto, tipo);
    		if(onProcesar)
    			onProcesar(response);
    	}
    }]) 
    
    
    // =========================================================================
    // ErrorService
    // =========================================================================
    
    .service('modalService', [function(){

    	this.mostrar = function(uibModal, contenido){
    		return modalInstances(true, '', 'static', true, uibModal, contenido)
    	}
    	
    	function modalInstances(animation, size, backdrop, keyboard, uibModal, contenido) {
            var modalInstance = uibModal.open({
                animation: animation,
                templateUrl: 'myModalContent.html',
                controller: 'ModalInstanceCtrl',
                size: size,
                backdrop: backdrop,
                keyboard: keyboard,
                resolve: {
                    content: function () {
                        return contenido;
                    }
                }
            
            });
            
            return modalInstance;
        }
    }])
    
    
    // =========================================================================
    // UserService
    // =========================================================================
    
    .service('userService', ['$http', '$q', function($http, $q){
    	var self=this;
    	
    	this.current={}
    	
        this.getCurrentUser=function getCurrentUser() {
            var deferred = $q.defer();
            
            $http.get("usuario/current")
                .then(
                function (response) {
                	self.current=response.data;
                    deferred.resolve(response.data);
                },
                function(errResponse){
                	self.current={}
                    console.error('Error while fetching Users');
                    deferred.reject(errResponse);
                }
            );
            return deferred.promise;
        }
    	
    	this.getAll=function(page, size) {
            var deferred = $q.defer();

            $http.get("usuario/all/"+page+"/"+size)
                .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error while fetching Users');
                    deferred.reject(errResponse);
                }
            );
            return deferred.promise;
        }
    	
    	this.getPendientes=function(page, size) {
            var deferred = $q.defer();

            $http.get("usuario/pendientes/"+page+"/"+size)
                .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error while fetching Users');
                    deferred.reject(errResponse);
                }
            );
            return deferred.promise;
        }
    	
    	this.nuevoPendiente=function(user) {
            var deferred = $q.defer();

            $http.post("usuario/pendiente", user)
                .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error while fetching Users');
                    deferred.reject(errResponse);
                }
            );
            return deferred.promise;
        }
    	
    	this.registrar=function(user) {
            var deferred = $q.defer();

            $http.post("usuario/registrar", user)
                .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error while fetching Users');
                    deferred.reject(errResponse);
                }
            );
            return deferred.promise;
        }
    	
    	this.eliminarPendiente=function(id) {
            var deferred = $q.defer();

            $http.delete("usuario/pendiente/"+id)
                .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error while fetching Users');
                    deferred.reject(errResponse);
                }
            );
            return deferred.promise;
        }
    	
    	this.eliminar=function(id) {
            var deferred = $q.defer();

            $http.delete("usuario/"+id)
                .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error while fetching Users');
                    deferred.reject(errResponse);
                }
            );
            return deferred.promise;
        }

    	this.enviarCorreo=function(id) {
            var deferred = $q.defer();

            $http.get("usuario/pendiente/correo/"+id)
                .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error while fetching Users');
                    deferred.reject(errResponse);
                }
            );
            return deferred.promise;
        }
    }])
    
    
    // =========================================================================
    // UserService
    // =========================================================================
    
    .service('centroService', ['$http', '$q', function($http, $q){
    	var self=this;

    	this.nuevoCentro=function(centro) {
            var deferred = $q.defer();

            $http.post("centro/nuevo", centro)
                .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error while fetching Users');
                    deferred.reject(errResponse);
                }
            );
            return deferred.promise;
        }
    	
    }])
    

    // =========================================================================
    // Best Selling Widget Data (Home Page)
    // =========================================================================

    .service('bestsellingService', ['$resource', function($resource){
        this.getBestselling = function(img, name, range) {
            var gbList = $resource("data/best-selling.json");
            
            return gbList.get({
                img: img,
                name: name,
                range: range,
            });
        }
    }])

    
    // =========================================================================
    // Todo List Widget Data
    // =========================================================================

    .service('todoService', ['$resource', function($resource){
        this.getTodo = function(todo) {
            var todoList = $resource("data/todo.json");
            
            return todoList.get({
                todo: todo
            });
        }
    }])


    // =========================================================================
    // Recent Items Widget Data
    // =========================================================================
    
    .service('recentitemService', ['$resource', function($resource){
        this.getRecentitem = function(id, name, price) {
            var recentitemList = $resource("data/recent-items.json");
            
            return recentitemList.get ({
                id: id,
                name: name,
                price: price
            })
        }
    }])


    // =========================================================================
    // Recent Posts Widget Data
    // =========================================================================
    
    .service('recentpostService', ['$resource', function($resource){
        this.getRecentpost = function(img, user, text) {
            var recentpostList = $resource("data/messages-notifications.json");
            
            return recentpostList.get ({
                img: img,
                user: user,
                text: text
            })
        }
    }])
    
    // =========================================================================
    // Data Table
    // =========================================================================
    
    .service('tableService', [function(){
        this.data = [
            {
                "id": 10238,
                "name": "Marc Barnes",
                "email": "marc.barnes54@example.com",
                "username": "MarcBarnes",
                "contact": "(382)-122-5003"
            },
            {   
                "id": 10243,
                "name": "Glen Curtis",
                "email": "glen.curtis11@example.com",
                "username": "GlenCurtis",
                "contact": "(477)-981-4948"
            },
            {
                "id": 10248,
                "name": "Beverly Gonzalez",
                "email": "beverly.gonzalez54@example.com",
                "username": "BeverlyGonzalez",
                "contact": "(832)-255-5161"
            },
            {
                "id": 10253,
                "name": "Yvonne Chavez",
                "email": "yvonne.chavez@example.com",
                "username": "YvonneChavez",
                "contact": "(477)-446-3715"
            },
            {
                "id": 10234,
                "name": "Melinda Mitchelle",
                "email": "melinda@example.com",
                "username": "MelindaMitchelle",
                "contact": "(813)-716-4996"
                
            },
            {
                "id": 10239,
                "name": "Shannon Bradley",
                "email": "shannon.bradley42@example.com",
                "username": "ShannonBradley",
                "contact": "(774)-291-9928"
            },
            {
                "id": 10244,
                "name": "Virgil Kim",
                "email": "virgil.kim81@example.com",
                "username": "VirgilKim",
                "contact": "(219)-181-7898"
            },
            {
                "id": 10249,
                "name": "Letitia Robertson",
                "email": "letitia.rober@example.com",
                "username": "Letitia Robertson",
                "contact": "(647)-209-4589"
            },
            {
                "id": 10237,
                "name": "Claude King",
                "email": "claude.king22@example.com",
                "username": "ClaudeKing",
                "contact": "(657)-988-8701"
            },
            {
                "id": 10242,
                "name": "Roland Craig",
                "email": "roland.craig47@example.com",
                "username": "RolandCraig",
                "contact": "(932)-935-9471"
            },
            {
                "id": 10247,
                "name": "Colleen Parker",
                "email": "colleen.parker38@example.com",
                "username": "ColleenParker",
                "contact": "(857)-459-2792"
            },
            {
                "id": 10252,
                "name": "Leah Jensen",
                "email": "leah.jensen27@example.com",
                "username": "LeahJensen",
                "contact": "(861)-275-4686"
            },
            {
                "id": 10236,
                "name": "Harold Martinez",
                "email": "martinez67@example.com",
                "username": "HaroldMartinez",
                "contact": "(836)-634-9133"
            },
            {
                "id": 10241,
                "name": "Keith Lowe",
                "email": "keith.lowe96@example.com",
                "username": "KeithLowe",
                "contact": "(778)-787-3100"
            },
            {
                "id": 10246,
                "name": "Charles Walker",
                "email": "charles.walker90@example.com",
                "username": "CharlesWalker",
                "contact": "(486)-440-4716"
            },
            {
                "id": 10251,
                "name": "Lillie Curtis",
                "email": "lillie.curtis12@example.com",
                "username": "LillieCurtis",
                "contact": "(342)-510-2258"
            },
            {
                "id": 10235,
                "name": "Genesis Reynolds",
                "email": "genesis@example.com",
                "username": "GenesisReynolds",
                "contact": "(339)-375-1858"
            },
            {
                "id": 10240,
                "name": "Oscar Palmer",
                "email": "oscar.palmer24@example.com",
                "username": "OscarPalmer",
                "contact": "(544)-270-9912"
            },
            {
                "id": 10245,
                "name": "Lena Bishop",
                "email": "Lena Bishop",
                "username": "LenaBishop",
                "contact": "(177)-521-1556"
            },
            {
                "id": 10250,
                "name": "Kent Nguyen",
                "email": "kent.nguyen34@example.com",
                "username": "KentNguyen",
                "contact": "(506)-533-6801"
            }
        ];
    }])


    // =========================================================================
    // Malihu Scroll - Custom Scroll bars
    // =========================================================================
    .service('scrollService', function() {
        var ss = {};
        ss.malihuScroll = function scrollBar(selector, theme, mousewheelaxis) {
            $(selector).mCustomScrollbar({
                theme: theme,
                scrollInertia: 100,
                axis:'yx',
                mouseWheel: {
                    enable: true,
                    axis: mousewheelaxis,
                    preventDefault: true
                }
            });
        }
        
        return ss;
    })


    //==============================================
    // BOOTSTRAP GROWL
    //==============================================

    .service('growlService', function(){
        var gs = {};
        gs.growl = function(message, type) {
            $.growl({
                message: message
            },{
                type: type,
                allow_dismiss: false,
                label: 'Cancel',
                className: 'btn-xs btn-inverse',
                placement: {
                    from: 'top',
                    align: 'right'
                },
                delay: 2500,
                animate: {
                        enter: 'animated bounceIn',
                        exit: 'animated bounceOut'
                },
                offset: {
                    x: 20,
                    y: 85
                }
            });
        }
        
        return gs;
    })
