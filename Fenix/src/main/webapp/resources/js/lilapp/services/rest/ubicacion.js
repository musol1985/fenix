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
    