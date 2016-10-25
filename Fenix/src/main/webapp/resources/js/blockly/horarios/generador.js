Blockly.JavaScript['horario'] = function(block) {
  var statements_laborables = Blockly.JavaScript.statementToCode(block, 'laborables');
  var statements_vacas = Blockly.JavaScript.statementToCode(block, 'vacas');
  var statements_esp = Blockly.JavaScript.statementToCode(block, 'esp');
  // TODO: Assemble JavaScript into code variable.
  var code = "function comprobarHorario(moment){";
  code+="	var huecos=[];"
  code+="	"+statements_laborables;
  code+="	return huecos;"
  code+="}";
  return code;
};

Blockly.JavaScript['mes'] = function(block) {
  var dropdown_mes = block.getFieldValue('mes');
  var statements_name = Blockly.JavaScript.statementToCode(block, 'NAME');
  // TODO: Assemble JavaScript into code variable.
  var code = 'console.log("Mes:"+moment.month());'
	  code+= 'console.log("Drop"+'+dropdown_mes+');'
	  code+='if((moment.month()-1)==parseInt('+dropdown_mes+')){';
  
  if(statements_name!=""){
	  code+="   "+statements_name;
  }else{
	  code+="   var dia=moment.format('YYYY-MM-DD');"
	  code+="	huecos.push({start:dia+' 00:00',end:dia+' 23:59', id: 'disponible', color: '#257e4a'});";
  }
  return code+="}";
};

Blockly.JavaScript['dia'] = function(block) {
  var dropdown_dias = block.getFieldValue('dias');
  var statements_name = Blockly.JavaScript.statementToCode(block, 'NAME');
  // TODO: Assemble JavaScript into code variable.
  var code = 'if(moment.weekday()=='+dropdown_dias+'){';
  
  if(statements_name!=""){
	  code+="   "+statements_name;
  }else{
	  code+="   var dia=moment.format('YYYY-MM-DD');"
	  code+="	huecos.push({start:dia+' 00:00',end:dia+' 23:59', id: 'disponible', color: '#257e4a'});";
  }
  return code+="}";
};

Blockly.JavaScript['hora'] = function(block) {
  var number_h = block.getFieldValue('h');
  var number_m = block.getFieldValue('m');
  var number_hf = block.getFieldValue('hf');
  var number_mf = block.getFieldValue('mf');
  
  if(number_h.length==1)number_h="0"+number_h;
  if(number_m.length==1)number_m="0"+number_m;
  if(number_hf.length==1)number_hf="0"+number_hf;
  if(number_mf.length==1)number_mf="0"+number_mf;
  
  var hIni=number_h+":"+number_m;
  var hFin=number_hf+":"+number_mf;
  // TODO: Assemble JavaScript into code variable.
  var code="   var dia=moment.format('YYYY-MM-DD');"
  code+="	huecos.push({start:dia+' "+hIni+"',end:dia+' "+hFin+"', id: 'disponible', color: '#257e4a'});";
  return code;
};

Blockly.JavaScript['mes_intervalo'] = function(block) {
  var dropdown_mesini = block.getFieldValue('mesIni');
  var dropdown_mesfin = block.getFieldValue('mesFin');
  var statements_meses = Blockly.JavaScript.statementToCode(block, 'meses');
  // TODO: Assemble JavaScript into code variable.
  var code = '...;\n';
  return code;
};

Blockly.JavaScript['dias_semana_intervalo'] = function(block) {
  var dropdown_diaini = block.getFieldValue('diaIni');
  var dropdown_name = block.getFieldValue('NAME');
  var statements_name = Blockly.JavaScript.statementToCode(block, 'NAME');
  // TODO: Assemble JavaScript into code variable.
  var code = '...;\n';
  return code;
};

Blockly.JavaScript['dias_intervalo'] = function(block) {
  var number_dini = block.getFieldValue('dIni');
  var number_dfin = block.getFieldValue('dFin');
  var statements_name = Blockly.JavaScript.statementToCode(block, 'NAME');
  // TODO: Assemble JavaScript into code variable.
  var code = '...;\n';
  return code;
};

Blockly.JavaScript['semana'] = function(block) {
  var dropdown_semanas = block.getFieldValue('semanas');
  var statements_semana = Blockly.JavaScript.statementToCode(block, 'semana');
  // TODO: Assemble JavaScript into code variable.
  var code = '...;\n';
  return code;
};

Blockly.JavaScript['semana_intervalo'] = function(block) {
  var dropdown_semanaini = block.getFieldValue('semanaIni');
  var dropdown_semanafin = block.getFieldValue('semanaFin');
  var statements_semana = Blockly.JavaScript.statementToCode(block, 'semana');
  // TODO: Assemble JavaScript into code variable.
  var code = '...;\n';
  return code;
};

Blockly.JavaScript['festivo'] = function(block) {
  var number_dia = block.getFieldValue('dia');
  var dropdown_mes = block.getFieldValue('mes');
  // TODO: Assemble JavaScript into code variable.
  var code = '...;\n';
  return code;
};

Blockly.JavaScript['festivo_anual'] = function(block) {
  var number_dia = block.getFieldValue('dia');
  var dropdown_mes = block.getFieldValue('mes');
  var number_anyo = block.getFieldValue('anyo');
  // TODO: Assemble JavaScript into code variable.
  var code = '...;\n';
  return code;
};