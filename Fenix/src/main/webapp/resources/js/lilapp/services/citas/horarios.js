materialAdmin
     // =========================================================================
    // HoirariosService
    // =========================================================================
    
    .service('horariosService', function($http, $q){
    	var self=this;
    	
    	this.newFromBlocky=function(blockly) {

    		return {    	
    			nombre: 'horarioTest',   
    			condiciones: blockly.getCode(),
    			aplicar:function(moment){
					if(!this.funcion){
						console.log("Compilando "+this.nombre);

						codigo=String(self.template).replace("//##STATEMENTS_VACACIONES", this.condiciones.vacaciones)
								 					.replace("//##STATEMENTS_LABORABLES", this.condiciones.laborables);
						console.log(codigo);
						this.funcion=eval("("+codigo+")");
			    	}
					console.log("Aplicando "+this.nombre);
					return this.funcion(moment);   				    				
    			}
    		}
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
    		  
    		  var addHueco=function(hueco){
    			  if(!isProcesado(hueco.moment)){
    				  huecos.push({start:hueco.start, end: hueco.end, id: hueco.id, color: hueco.color});
    			  }
    			  
    		  };
    		  var color="#257e4a";
    		  //##STATEMENTS_VACACIONES
    		  color="FF0000";
    		  //##STATEMENTS_LABORABLES
    		  return huecos;
    	  };
    })