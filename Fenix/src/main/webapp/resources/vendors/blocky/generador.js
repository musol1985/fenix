Blockly.JavaScript['horario'] = function(block) {
  var statements_horario = Blockly.JavaScript.statementToCode(block, 'horario');
  var statements_vacas = Blockly.JavaScript.statementToCode(block, 'vacas');
  var statements_esp = Blockly.JavaScript.statementToCode(block, 'esp');
  // TODO: Assemble JavaScript into code variable.
  var code = "function comprobarHorario(moment){";
  code="	var huecos=[];"
  code+="	"+statements_horario;
  code+="	return huecos;"
  code+="}";
  return code;
};

Blockly.JavaScript['mes'] = function(block) {
  var dropdown_mes = block.getFieldValue('mes');
  var statements_name = Blockly.JavaScript.statementToCode(block, 'NAME');
  // TODO: Assemble JavaScript into code variable.
  var code = 'if(moment.month()=='+dropdown_mes+'){';
  
  if(statements_name!=""){
	  code+="   "+statements_name;
  }else{
	  code+="	huecos.push({start:moment.clone().format('YYYY-MM-DD'),end:moment.clone().add(1, 'days').format('YYYY-MM-DD')});";
  }
  code+="}";
  return code;
};

Blockly.JavaScript['dia'] = function(block) {
  var dropdown_dias = block.getFieldValue('dias');
  var statements_name = Blockly.JavaScript.statementToCode(block, 'NAME');
  // TODO: Assemble JavaScript into code variable.
  var code = 'if(moment.esDia('+dropdown_dias+'){\n';
  
  if(statements_name!=""){
	  code+="   var horas=[];";
	  code+="   horas.push("+statements_name+");";
  }else{
	  code+=" return {start:'00:00', end:'23:59'};"
  }
  code+="}";
  return code;
};

Blockly.JavaScript['diasintervalo'] = function(block) {
  var number_dini = block.getFieldValue('dIni');
  var number_dfin = block.getFieldValue('dFin');
  var statements_name = Blockly.JavaScript.statementToCode(block, 'NAME');
  // TODO: Assemble JavaScript into code variable.
  var code = '...;\n';
  return code;
};

Blockly.JavaScript['diassemanaintervalo'] = function(block) {
  var dropdown_diaini = block.getFieldValue('diaIni');
  var dropdown_name = block.getFieldValue('NAME');
  var statements_name = Blockly.JavaScript.statementToCode(block, 'NAME');
  // TODO: Assemble JavaScript into code variable.
  var code = '...;\n';
  return code;
};

Blockly.JavaScript['hora'] = function(block) {
  var number_h = block.getFieldValue('h');
  var number_m = block.getFieldValue('m');
  var number_hf = block.getFieldValue('hf');
  var number_mf = block.getFieldValue('mf');
  // TODO: Assemble JavaScript into code variable.
  var code = "{start:'"+number_h+":"+number_m+"'}"
  return code;
};