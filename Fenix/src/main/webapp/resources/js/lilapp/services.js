materialAdmin
     // =========================================================================
    // HoirariosService
    // =========================================================================
    
    .service('horarioService', function($q, $http, BasicRESTService, userService){
    	var self=this;
    	
    	this.REST=new BasicRESTService("Horario", "horario");
    	
    	this.REST.getAplicados=function (id, page, size) {
            var deferred = $q.defer();
            
            $http.get("horario/aplicados/"+id+"/"+page+"/"+size)
                .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error horarioService.getAplicados');
                    deferred.reject(errResponse);
                }
            );
            return deferred.promise;
        }
    	
    	this.REST.getEditorById=function (id) {
            var deferred = $q.defer();
            
            $http.get("horario/editor/"+id)
                .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error horarioService.getEditorById');
                    deferred.reject(errResponse);
                }
            );
            return deferred.promise;
        }
    	
    	this.updateFromBlocky=function(horario, blockly) {
    		var condiciones=blockly.getCode().split("$");

 	
    	    horario.model.festivos=condiciones[2];
    	    horario.model.vacaciones=condiciones[1];
    	    horario.model.laborables=condiciones[0];
    	    
    	    			 		
    	    		
    	    horario.codigo=LZString.compressToBase64(blockly.getXML());
    		
    		
    	    self.iniciarHorario(horario);

    		return horario;
    	}
    	
    	this.iniciarHorario=function(h){
    		var model=h;
    		
    		if(h.model)
    			model=h.model;
    		
    		console.log(">>>>iniciando horario "+model.nombre);
    		
    		h.isCompilado=function(){
    			if(h.run)
    				return true;
    			
    			return false;
    		}
    		
    		h.compilar=function(){
    			console.log("Compilando "+model.nombre);

				codigo=String(self.template).replace("//##STATEMENTS_FESTIVOS", model.festivos)
											.replace("//##STATEMENTS_VACACIONES", model.vacaciones)
						 					.replace("//##STATEMENTS_LABORABLES", model.laborables);

				h.run=eval("("+codigo+")");
    		}

    		h.aplicar=function(moment, renderBack){
    			if(!h.isCompilado()){
    				h.compilar();
    			} 
    			//console.log("Aplicando el horario "+model.nombre+" al moment "+moment.format('YYYY-MM-DD'));
    			return h.run(moment, renderBack);
    		}
    	}
    	
    	
    	this.template=function(moment, renderBack){
    		  var huecos=[];
    		  
    		  var isProcesado=function(hueco){
    			  var procesado=false;
    			  huecos.forEach(function(value, key){
    				  var fecha=value.start.split(" ")[0];//Obtengo la fecha sin la hora
    				  if(hueco.m.format('YYYY-MM-DD')==fecha && hueco.g!=value.grupo){
    					  procesado=true;			  
    				  }
    			  });
    			  return procesado;
    		  };
    		  
    		  var dia=moment.format('YYYY-MM-DD');
    		  var g=0;    		  
    		  
    		  var addHueco=function(hueco){
    			  //console.log("Adding hueco "+hueco);
    			  if(!isProcesado(hueco)){
    				  //console.log("OK");
    				  var h={start:dia+' '+hueco.s, end: dia+' '+hueco.e, id: hueco.id, color: hueco.color, title:hueco.id, grupo: hueco.g};
    				  if(renderBack && hueco.id=='laborable'){
    					  h.rendering='background';
    				  }
    				  huecos.push(h);
    			  }else{
    				  console.log("Ya procesado");
    			  }
    			  
    		  };

    		  var color="#000000";
    		  var id="festivo";
    		  //##STATEMENTS_FESTIVOS
    		  g++;
    		  color="#FF0000";
    		  id="vacaciones";
    		  //##STATEMENTS_VACACIONES
    		  g++;
    		  color="#0000FF";
    		  id="laborable";
    		  //##STATEMENTS_LABORABLES
    		  return huecos;
    	  };
    });
materialAdmin
    // =========================================================================
    // PrestacionService
    // =========================================================================
    
    .factory('BasicRESTService',  function($http, $q){
    	var self = function (id, url){
    		this.getByCentro=function (centro, page, size) {
                var deferred = $q.defer();
                
                $http.get(url+"/"+centro+"/"+page+"/"+size)
                    .then(
                    function (response) {
                        deferred.resolve(response.data);
                    },
                    function(errResponse){
                        console.error('Error '+id+'.getByCentro');
                        deferred.reject(errResponse);
                    }
                );
                return deferred.promise;
            }
            
            this.getAllByCentro=function (centro) {
                var deferred = $q.defer();
                
                $http.get(url+"/all/"+centro)
                    .then(
                    function (response) {
                        deferred.resolve(response.data);
                    },
                    function(errResponse){
                        console.error('Error '+id+'.getByCentro');
                        deferred.reject(errResponse);
                    }
                );
                return deferred.promise;
            }
        	
        	this.get=function(id) {
                var deferred = $q.defer();

                $http.get(url+"/"+id)
                    .then(
                    function (response) {
                        deferred.resolve(response.data);
                    },
                    function(errResponse){
                        console.error('Error '+id+'.get');
                        deferred.reject(errResponse);
                    }
                );
                return deferred.promise;
            }
        	
        	this.nuevo=function(item, customURL) {
                var deferred = $q.defer();
                
                var u=url;
                
                if(customURL)
                	u=customURL;

                $http.put(u, item)
                    .then(
                    function (response) {
                        deferred.resolve(response.data);
                    },
                    function(errResponse){
                        console.error('Error '+id+'.nueva');
                        deferred.reject(errResponse);
                    }
                );
                return deferred.promise;
            }
        	
        	this.modificar=function(item) {
                var deferred = $q.defer();

                $http.post(url, item)
                    .then(
                    function (response) {
                        deferred.resolve(response.data);
                    },
                    function(errResponse){
                        console.error('Error '+id+'.modificar');
                        deferred.reject(errResponse);
                    }
                );
                return deferred.promise;
            }
        	
        	this.eliminar=function(id) {
                var deferred = $q.defer();

                $http.delete(url+"/"+id)
                    .then(
                    function (response) {
                        deferred.resolve(response.data);
                    },
                    function(errResponse){
                        console.error('Error '+id+'.eliminar');
                        deferred.reject(errResponse);
                    }
                );
                return deferred.promise;
            }
    		
    		
            return this;
        }

        return self;
    });
materialAdmin
    // =========================================================================
    // CentroService
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
    	
    	this.getAll=function() {
            var deferred = $q.defer();

            $http.get("centro/all")
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
    	
    }]);
materialAdmin
     // =========================================================================
    // CitaService
    // =========================================================================
    .service('citaService', function($q, $http, BasicRESTService, userService, horarioService){
    	var self=this;
    	
    	this.REST=new BasicRESTService("Horario", "horario");
    	
    	
    	this.REST.getMaestros=function (centroId) {
            var deferred = $q.defer();
            
            $http.get("cita/maestros/"+centroId)
                .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error citaService.getMaestros');
                    deferred.reject(errResponse);
                }
            );
            return deferred.promise;
        }
    	
    	this.REST.nueva=function(cita) {
            var deferred = $q.defer();

            $http.put("cita", cita)
                .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error while creating cita');
                    deferred.reject(errResponse);
                }
            );
            return deferred.promise;
        }
    	
    	this.agruparProfesionales=function(maestros){
    		var current;
    		
    		angular.forEach(maestros.profesionales, function(value, key) {
    			if(value.id==userService.current.id){
    				value.nombre+=" (YO)";  
    				value.grupo="";
    				current=value;
    			}else{
    				value.grupo="Otros profesionales";
    			}
			});
    		maestros.profesionales.splice(0, 0, {'id':'-1', 'nombre':'CUALQUIERA', grupo:''});
    		
    		return current;
    	}
    	
    	/**
    	 * Prepara los horario del list que viene del servidor a un hasmap para buscarlo rapido
    	 * Aparte le parara los metodos de compilar, run y aplicar
    	 */
    	this.prepararHorarios=function(maestros){    	
    		var generico;
    		var horarios={};
    		angular.forEach(maestros.horarios, function(value, key) {
    			horarioService.iniciarHorario(value);
    			if(value.generico==true)
    				generico=value;
    			horarios[value.id]=value;
			});    		    		
    		
    		maestros.horarios=horarios;
    		return generico;
    	}
    	
    	//TODO implementar la gestion de horarios
    	this.getHorarioForProfesional=function(profesional, horarios){
    		return horarios[0];
    	}
    	
    	//TODO implementar la gestion de horarios
    	this.getHorarioForPrestacion=function(prestacion, horarios){
    		return horarios[prestacion.horario];
    	}
    });
materialAdmin
    // =========================================================================
    // ClienteService
    // =========================================================================
    
    .service('clienteService', ['$http', '$q', '$rootScope', function($http, $q, $rootScope){
    	this.seleccionado;
    	
    	this.isSeleccionado=function(){
    		if(this.seleccionado)
    			return true;
    		return false;
    	}
    	
    	this.getSeleccionado=function(){
    		return this.seleccionado;
    	}
    	
    	this.seleccionar=function(cliente){
    		this.seleccionado=cliente;
    		$rootScope.$broadcast('onSeleccionarCliente', cliente);
    	}
    	
    	this.desSeleccionar=function(){
    		this.seleccionar(undefined);
    	}
    	
        this.buscar=function (texto, centro) {
            var deferred = $q.defer();
            
            $http.post("cliente/buscar",{texto:texto, centro:centro})
                .then(
                function (response) {   
                	var res=response.data.map(function(item){
                    	item.texto=item.nombre+" "+item.apellidos+", "+item.dni+", "+item.telefono;
     
                    	return item;
                    })
                    console.log(res);
                    if(res.length==0){
                    	res=[{texto:'No se ha encontrado. Pulsa enter para crear uno nuevo', id:-1, busqueda:texto}];
                    }
                    console.log(res);
                    deferred.resolve(res);
                },
                function(errResponse){
                    console.error('Error cliente.getByCentro');
                    deferred.reject(errResponse);
                }
            );
            return deferred.promise;
        }
    	
    	this.nuevo=function(cliente) {
            var deferred = $q.defer();

            $http.put("cliente", cliente)
                .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error creando un cliente');
                    deferred.reject(errResponse);
                }
            );
            return deferred.promise;
        }
    	
    	this.modificar=function(cliente) {
            var deferred = $q.defer();

            $http.post("cliente", cliente)
                .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error modificando una prestacion');
                    deferred.reject(errResponse);
                }
            );
            return deferred.promise;
        }
    }])
    ;
materialAdmin
    // =========================================================================
    // PrestacionService
    // =========================================================================
    
    .service('prestacionService', function(BasicRESTService){
    	this.REST=new BasicRESTService("Prestacion", "prestacion");
    });
materialAdmin
    // =========================================================================
    // UbicacionService
    // =========================================================================
    
    .service('ubicacionService', ['$http', '$q', function($http, $q){
    	this.getUbicacion = function(val) {
            return $http.get('//maps.googleapis.com/maps/api/geocode/json', {
                params: {
                    address: val,
                    sensor: false
                }
            }).then(function(response){
                return response.data.results.map(function(item){
                    return item;
                });
            });
        }
    	
    	this.setUbicacion=function($item, $model, $label, $event){
    		console.log($item);
    		var ubicacion={};
    		var direccion={};
    		
    		angular.forEach($item.address_components, function(address) {
    			var componente={};
    			
    			angular.forEach(address, function(value, key) {
    				if(key=="types"){
    					if(value.indexOf("locality")>-1){
    						componente.tipo="poblacion";
    					}else if(value.indexOf("route")>-1){
    						componente.tipo="calle";
    					}else if(value.indexOf("street_number")>-1){
    						componente.tipo="numero";
    					}else if(value.indexOf("administrative_area_level_2")>-1){
    						componente.tipo="provincia";
    					}else if(value.indexOf("postal_code")>-1){
    						componente.tipo="CP";
    					}else if(value.indexOf("country")>-1){
    						componente.tipo="pais";
    					}
    				}
    				if(key=="short_name"){
    					componente.valor=value;
    				}
    			});
    			
    			if(componente.tipo=="poblacion"){
    				direccion.poblacion=componente.valor;
    			}else if(componente.tipo=="calle"){
    				direccion.calle=componente.valor;
    			}else if(componente.tipo=="numero"){
    				direccion.numero=componente.valor;
    			}else if(componente.tipo=="provincia"){
    				direccion.provincia=componente.valor;
    			}else if(componente.tipo=="CP"){
    				direccion.CP=componente.valor;
    			}else if(componente.tipo=="pais"){
    				direccion.pais=componente.valor;
    			}
			});
    		
    		direccion.id=$item.place_id;
    		ubicacion.direccion=direccion;
			ubicacion.posicion=$item.geometry.location;
    		
    		console.log(ubicacion);
    		return ubicacion;
    	}
    }])
    ;
materialAdmin
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
    	
    	this.getCentro=function(){
    		return this.current.centro;
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
    	
    	this.getByCentro=function(page, size, centro) {
            var deferred = $q.defer();

            $http.get("usuario/"+centro+"/"+page+"/"+size)
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
    	
    	this.getAllByCentro=function(page, size, centro) {
            var deferred = $q.defer();

            $http.get("usuario/all/"+centro+"/"+page+"/"+size)
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
    	
    	this.getListaByCentro=function(centro) {
            var deferred = $q.defer();

            $http.get("usuario/lista/"+centro)
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
    	
    	this.getPendientes=function(page, size, centro) {
            var deferred = $q.defer();

            $http.get("usuario/pendientes/"+centro+"/"+page+"/"+size)
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
    	
    	this.resetPassword=function(correo) {
            var deferred = $q.defer();

            $http.post("usuario/reset", {correo:correo})
                .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error while resetPassword');
                    deferred.reject(errResponse);
                }
            );
            return deferred.promise;
        }
    	
    	
    	this.modificar=function(item) {
            var deferred = $q.defer();

            $http.post("usuario/modificar", item)
                .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error '+id+'.modificar');
                    deferred.reject(errResponse);
                }
            );
            return deferred.promise;
        }
    }])