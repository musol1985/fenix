materialAdmin
     // =========================================================================
    // HoirariosService
    // =========================================================================
    
    .service('horariosService', function($http, $q){
    	var self=this;
    	
    	this.newFromBlocky=function(blockly) {
    		var condiciones=blockly.getCode().split("$");
    		console.log(condiciones);
    		/*alert(condiciones.vacaciones);
    		alert(condiciones.laborables);*/
    		var horario= {    	
    			nombre: 'horarioTest',
    			festivos: condiciones[2],
    			vacaciones: condiciones[1],
    			laborables: condiciones[0],
    			aplicar:function(moment){
					if(!this.funcion){
						console.log("Compilando "+this.nombre);

						codigo=String(self.template).replace("//##STATEMENTS_FESTIVOS", this.festivos)
													.replace("//##STATEMENTS_VACACIONES", this.vacaciones)
								 					.replace("//##STATEMENTS_LABORABLES", this.laborables);
						console.log(codigo);
						this.funcion=eval("("+codigo+")");
			    	}
					console.log("Aplicando el horario "+this.nombre+" al moment "+moment.format('YYYY-MM-DD'));
					return this.funcion(moment);   				    				
    			}    		
    		}
    		
    		return horario;
    	}
    	
    	
    	this.template=function(moment){
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
    			  if(!isProcesado(hueco)){
    				  huecos.push({start:dia+' '+hueco.s, end: dia+' '+hueco.e, id: hueco.id, color: hueco.color, title:hueco.id, grupo: hueco.g});
    			  }
    			  
    		  };
    		  
    		  var color="#000000";
    		  var id="festivo";
    		  //##STATEMENTS_FESTIVOS
    		  g++;
    		  color="#257e4a";
    		  id="vacaciones";
    		  //##STATEMENTS_VACACIONES
    		  g++;
    		  color="#FF0000";
    		  id="laborable";
    		  //##STATEMENTS_LABORABLES
    		  return huecos;
    	  };
    })