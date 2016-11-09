Blockly.JavaScript['horario'] = function(block) {
  var statements_laborables = Blockly.JavaScript.statementToCode(block, 'laborables');
  var statements_vacas = Blockly.JavaScript.statementToCode(block, 'vacas');
  var statements_esp = Blockly.JavaScript.statementToCode(block, 'esp');

  return statements_laborables+"$"+statements_vacas+"$"+statements_esp;
};

Blockly.JavaScript['mes'] = function(block) {
  var dropdown_mes = block.getFieldValue('mes');
  var statements_name = Blockly.JavaScript.statementToCode(block, 'NAME');
  // TODO: Assemble JavaScript into code variable.
   var code = 'if((moment.month()+1)==parseInt('+dropdown_mes+')){\n';
  
  if(statements_name!=""){
	  code+="   g++;\n";
	  code+="   "+statements_name;
  }else{
	  code+="	addHueco({s:'00:00',e:'23:59', id: id, color: color, m: moment, g:g});\n";
  }
  return code+="}\n";
};

Blockly.JavaScript['dia'] = function(block) {
  var dropdown_dias = block.getFieldValue('dias');
  var statements_name = Blockly.JavaScript.statementToCode(block, 'NAME');
  // TODO: Assemble JavaScript into code variable.
  var code = 'if(moment.weekday()=='+dropdown_dias+'){\n';
  
  if(statements_name!=""){
	  code+="   g++;\n";
	  code+="   "+statements_name;
  }else{
	  code+="	addHueco({s:'00:00',e:'23:59', id: id, color: color, m: moment, g:g});\n";
  }
  return code+="}\n";
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
  code="	addHueco({s:'"+hIni+"',e:'"+hFin+"', id: id, color: color, m: moment, g:g});\n";
  return code;
};

Blockly.JavaScript['mes_intervalo'] = function(block) {
  var dropdown_mesini = block.getFieldValue('mesIni');
  var dropdown_mesfin = block.getFieldValue('mesFin');
  var statements_meses = Blockly.JavaScript.statementToCode(block, 'meses');
  // TODO: Assemble JavaScript into code variable.
  var code = 'if((moment.month()+1)>=parseInt('+dropdown_mesini+') && (moment.month()+1)<=parseInt('+dropdown_mesfin+')){\n';
  
  if(statements_meses!=""){
	  code+="   g++;\n";
	  code+="   "+statements_meses;
  }else{
	  code+="	addHueco({s:'00:00',e:'23:59', id: id, color: color, m: moment, g:g});\n";
  }
  return code+="}\n";
};

Blockly.JavaScript['dias_semana_intervalo'] = function(block) {
  var dropdown_diaini = block.getFieldValue('diaIni');
  var dropdown_name = block.getFieldValue('NAME');
  var statements_name = Blockly.JavaScript.statementToCode(block, 'NAME');
  // TODO: Assemble JavaScript into code variable.

  var code = 'if((moment.isoWeekday())>=parseInt('+dropdown_diaini+') && (moment.isoWeekday())<=parseInt('+dropdown_name+')){\n';
  
  if(statements_name!=""){
	  code+="   g++;\n";
	  code+="   "+statements_name;
  }else{
	  code+="	addHueco({s:'00:00',e:'23:59', id: id, color: color, m: moment, g:g});\n";
  }
  return code+="}\n";
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
  var code = 'if(moment.date()=='+number_dia+' && moment.month()+1=='+dropdown_mes+'){\n';
  

  code+="	addHueco({s:'00:00',e:'23:59', id: id, color: color, m: moment, g:g});\n";
  
  return code+"}\n";
};

Blockly.JavaScript['festivo_anual'] = function(block) {
  var number_dia = block.getFieldValue('dia');
  var dropdown_mes = block.getFieldValue('mes');
  var number_anyo = block.getFieldValue('anyo');
  // TODO: Assemble JavaScript into code variable.
  var code = '...;\n';
  return code;
};