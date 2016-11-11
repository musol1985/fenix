materialAdmin
     // =========================================================================
    // HoirariosService
    // =========================================================================
    
    .service('horarioService', function($q, $http, BasicRESTService, userService){
    	var self=this;
    	
    	this.REST=new BasicRESTService("Horario", "horario");
    	
    	
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
				console.log(codigo);
				h.run=eval("("+codigo+")");
    		}

    		h.aplicar=function(moment, renderBack){
    			if(!h.isCompilado()){
    				h.compilar();
    			}
    			console.log("Aplicando el horario "+model.nombre+" al moment "+moment.format('YYYY-MM-DD'));
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
    			  console.log("Adding hueco "+hueco);
    			  if(!isProcesado(hueco)){
    				  console.log("OK");
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
    		  color="#257e4a";
    		  id="laborable";
    		  //##STATEMENTS_LABORABLES
    		  return huecos;
    	  };
    })