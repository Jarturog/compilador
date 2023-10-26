# Parte que realiza el análisis léxico mediante JFLex
## ARTAM pasado a expresiones regulares:


digito      [0-9]
digitos     {digito}+
signo       [+-]?  ##SOBRA
ent10       signo[([1-9]{digito}*)0+]
ent2        0b[01]+
ent8        0o[0-7]+
ent16       0x[A-Fa-f0-9]+
val_ent     [{ent10}{ent2}{ent8}{ent16}]
val_real    {ent10}?\.{digitos}?([Ee]{ent10})?
//numero      [{val_ent}{val_real}]
car         [A-Za-z]
cars        {car}+
id          ({car}|_)[{car}_digito]*
kw_car      car
kw_ent      ent
kw_real     real
kw_prop     prop
tipo        [{kw_car}{kw_ent}{kw_real}{kw_prop}]
kw_void     void
val_prop    (verdadero|falso)
val_car     '{car}'
val_cars    \"{cars}\"
!!prop      ({prop}{op_binario}{prop})|   (props luego se puede convertir en algo  !!es lo mismo q operacion!! )
param       {tipo}{id}
params      ({param},)*{param}
!!cabFunc     [{tipo}{kw_void}]{id}\({params}?\)  #ponemos llaves apertura cierre? 
dec         {tipo}{id}(,{id})*
decAsig     {dec}:{asignable}
asignable   {}:{}
instr       ({llamadaFunc}|{operacion}|{decAsig});
op_binario  [=\<\>(\<=)(\>=)\+\-\*\/\%\|&\\]       # = < > <= >= + - * / % | & \
llamadaFunc {id}\(!!parametros!!\) #le ponemos puntocoma como final de linea?
operacion   ({digito}|{digitos}|{id}){op_binario}({digito}|{digitos}|{id})