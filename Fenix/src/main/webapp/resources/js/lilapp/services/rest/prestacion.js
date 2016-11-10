materialAdmin
    // =========================================================================
    // PrestacionService
    // =========================================================================
    
    .service('prestacionService', function(BasicRESTService){
    	this.REST=new BasicRESTService("Prestacion", "prestacion");
    })