Blockly.Blocks['horario'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("Horario");
    this.appendStatementInput("laborables")
        .setCheck(null)
        .appendField("Laborables");
    this.appendStatementInput("vacas")
        .setCheck(null)
        .appendField("Vacaciones");
    this.appendStatementInput("esp")
        .setCheck(null)
        .appendField("Especiales");
    this.setColour(210);
    this.setTooltip('');
    this.setHelpUrl('http://www.example.com/');
  }
};

Blockly.Blocks['mes'] = {
  init: function() {
    this.appendStatementInput("NAME")
        .setCheck(null)
        .appendField("Mes")
        .appendField(new Blockly.FieldDropdown([["Enero", "1"], ["Febrero", "2"], ["Marzo", "3"], ["Abril", "4"], ["Mayo", "5"], ["Junio", "6"], ["Julio", "7"], ["Agosto", "8"], ["Septiembre", "9"], ["Octubre", "10"], ["Noviembre", "11"], ["Diciembre", "12"]]), "mes");
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(120);
    this.setTooltip('');
    this.setHelpUrl('http://www.example.com/');
  }
};

Blockly.Blocks['dia'] = {
  init: function() {
    this.appendStatementInput("NAME")
        .setCheck(null)
        .appendField("Dia")
        .appendField(new Blockly.FieldDropdown([["Lunes", "1"], ["Martes", "2"], ["Miercoles", "3"], ["Jueves", "4"], ["Viernes", "5"], ["Sabado", "6"], ["Domingo", "7"]]), "dias");
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(65);
    this.setTooltip('');
    this.setHelpUrl('http://www.example.com/');
  }
};

Blockly.Blocks['hora'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("Hora desde las")
        .appendField(new Blockly.FieldNumber(0, 0, 23), "h")
        .appendField(":")
        .appendField(new Blockly.FieldNumber(0, 0, 59), "m")
        .appendField("hasta las")
        .appendField(new Blockly.FieldNumber(0, 0, 23), "hf")
        .appendField(":")
        .appendField(new Blockly.FieldNumber(0, 0, 59), "mf");
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(20);
    this.setTooltip('');
    this.setHelpUrl('http://www.example.com/');
  }
};

Blockly.Blocks['mes_intervalo'] = {
  init: function() {
    this.appendStatementInput("meses")
        .setCheck(null)
        .appendField("Meses desde")
        .appendField(new Blockly.FieldDropdown([["Enero", "0"], ["Febrero", "1"], ["Marzo", "2"], ["Abril", "3"], ["Mayo", "4"], ["Junio", "5"], ["Julio", "6"], ["Agosto", "7"], ["Septiembre", "8"], ["Octubre", "9"], ["Noviembre", "10"], ["Diciembre", "11"]]), "mesIni")
        .appendField("hasta")
        .appendField(new Blockly.FieldDropdown([["Enero", "0"], ["Febrero", "1"], ["Marzo", "2"], ["Abril", "3"], ["Mayo", "4"], ["Junio", "5"], ["Julio", "6"], ["Agosto", "7"], ["Septiembre", "8"], ["Octubre", "9"], ["Noviembre", "10"], ["Diciembre", "11"]]), "mesFin");
    this.setColour(120);
    this.setTooltip('');
    this.setHelpUrl('http://www.example.com/');
  }
};

Blockly.Blocks['dias_semana_intervalo'] = {
  init: function() {
    this.appendStatementInput("NAME")
        .setCheck(null)
        .appendField("Dias desde el ")
        .appendField(new Blockly.FieldDropdown([["Lunes", "1"], ["Martes", "2"], ["Miercoles", "3"], ["Jueves", "4"], ["Viernes", "5"], ["Sabado", "6"], ["Domingo", "7"]]), "diaIni")
        .appendField("hasta el ")
        .appendField(new Blockly.FieldDropdown([["Lunes", "1"], ["Martes", "2"], ["Miercoles", "3"], ["Jueves", "4"], ["Viernes", "5"], ["Sabado", "6"], ["Domingo", "7"]]), "NAME");
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(65);
    this.setTooltip('');
    this.setHelpUrl('http://www.example.com/');
  }
};

Blockly.Blocks['dias_intervalo'] = {
  init: function() {
    this.appendStatementInput("NAME")
        .setCheck(null)
        .appendField("Dias desde el ")
        .appendField(new Blockly.FieldNumber(0, 1, 31), "dIni")
        .appendField("al")
        .appendField(new Blockly.FieldNumber(31, 1, 31), "dFin");
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(65);
    this.setTooltip('');
    this.setHelpUrl('http://www.example.com/');
  }
};

Blockly.Blocks['semana'] = {
  init: function() {
    this.appendStatementInput("semana")
        .setCheck(null)
        .appendField("Semana")
        .appendField(new Blockly.FieldDropdown([["Primera", "1"], ["Segunda", "2"], ["Tercera", "3"], ["Cuarta", "4"], ["Quinta", "5"]]), "semanas");
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(290);
    this.setTooltip('');
    this.setHelpUrl('http://www.example.com/');
  }
};

Blockly.Blocks['semana_intervalo'] = {
  init: function() {
    this.appendStatementInput("semana")
        .setCheck(null)
        .appendField("Desde la semana ")
        .appendField(new Blockly.FieldDropdown([["Primera", "1"], ["Segunda", "2"], ["Tercera", "3"], ["Cuarta", "4"], ["Quinta", "5"]]), "semanaIni")
        .appendField("hasta la ")
        .appendField(new Blockly.FieldDropdown([["Primera", "1"], ["Segunda", "2"], ["Tercera", "3"], ["Cuarta", "4"], ["Quinta", "5"]]), "semanaFin");
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(290);
    this.setTooltip('');
    this.setHelpUrl('http://www.example.com/');
  }
};