grammar PPMKTBAG;

start: cityDef definitionBlocks;

cityDef: 'city' '->' cityName BLOCK_SEPARATOR;
cityName: STRING;

definitionBlocks: variablesBlock roadsBlock estatesBlock;

variablesBlock: 'variables' '->' '{' variableDefinitions '}' BLOCK_SEPARATOR;
variableDefinitions: variableDef | ;
variableDef:
    VAR 'as' 'Float' '->' FLOAT BLOCK_SEPARATOR variableDefinitions |
    VAR 'as' 'String' '->' STRING BLOCK_SEPARATOR variableDefinitions;

roadsBlock: 'roads' '->' OPEN_BLOCK roadDefinitions CLOSE_BLOCK BLOCK_SEPARATOR;
roadDefinitions: roadDef | ;
roadDef: roadIdentifier 'as' featureType '->' '[' pointList ']' BLOCK_SEPARATOR roadDefinitions;
roadIdentifier: STRING | VAR;

estatesBlock: 'estates' '->' OPEN_BLOCK  estateDefinitions CLOSE_BLOCK BLOCK_SEPARATOR;
estateDefinitions: estateDef | ;
estateDef: estateIdentifier 'as' featureType '->' '[' pointList ']' BLOCK_SEPARATOR estateDefinitions;
estateIdentifier: STRING | VAR;

featureType: 'Line' | 'Polygon';

pointList: point point additionalPoints;
point: 'Point' '(' pointArg ',' pointArg ')' ',';
additionalPoints: point | ;
pointArg: VAR | FLOAT;


FLOAT: ('0'..'9')+ ('.' ('0'..'9')+)?;
STRING : '"' ~('\r' | '\n' | '"')* '"' ;
VAR: [a-zA-Z_][a-zA-Z0-9_]*;
BLOCK_SEPARATOR: ';';
OPEN_BLOCK: '{';
CLOSE_BLOCK: '}';
WS: [ \n\t]+ -> skip;