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
    			vacaciones: condiciones[1],
    			laborables: condiciones[0],
    			aplicar:function(moment){
					if(!this.funcion){
						console.log("Compilando "+this.nombre);

						codigo=String(self.template).replace("//##STATEMENTS_VACACIONES", this.vacaciones)
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
    		  
    		  var isProcesado=function(moment){
    			  var procesado=false;
    			  huecos.forEach(function(value, key){
    				  if(moment.format('YYYY-MM-DD')==value.moment.format('YYYY-MM-DD')){
    					  procesado=true;			  
    				  }
    			  });
    			  return procesado;
    		  };
    		  
    		  var dia=moment.format('YYYY-MM-DD');
    		  
    		  var addHueco=function(hueco){
    			  if(!isProcesado(hueco.moment)){
    				  huecos.push({start:dia+' '+hueco.s, end: dia+' '+hueco.e, id: hueco.id, color: hueco.color, title:hueco.id});
    			  }
    			  
    		  };
    		  
    		  var color="#257e4a";
    		  var id="vacaciones";
    		  //##STATEMENTS_VACACIONES
    		  color="#FF0000";
    		  id="laborable";
    		  //##STATEMENTS_LABORABLES
    		  return huecos;
    	  };
    })