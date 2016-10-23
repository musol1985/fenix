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
  var code = 'if(moment.month()=='+dropdown_mes+'){';
  
  if(statements_name!=""){
	  code+="   "+statements_name;
  }else{
	  code+="	huecos.push({start:moment.clone().format('YYYY-MM-DD'),end:moment.clone().add(1, 'days').format('YYYY-MM-DD')});";
  }
  return code+="}";
};

Blockly.JavaScript['dia'] = function(block) {
  var dropdown_dias = block.getFieldValue('dias');
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
  var code = '...;\n';
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