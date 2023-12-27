# Trabajo de la asignatura Compiladores

Consistirá en realizar un compilador, el cual tiene la parte front-end (analizadores léxico, sintáctico y semántico) y la back-end (código objeto, optimización y ensamblador).

# DARTAM

- Un cos general de programa on hi hagi d’haver els subprogrames, les declaracions i les instruccions del programa:       void main(car[][] args) { ··· }
- Definició de subprogrames:      tipoRetornoSugerido nombreFunción(parámetros) { ··· }

- Tipus:

  - Enter:      ent
  - Cadena de caràcters:      car[]
  - Lògic:      prop (de proposición, ya que puede ser cierta o falsa)
  - Altres*:      real, car

- Tipus definits per l’usuari:

  - Tuples:      tupla nombreTupla { ··· } (como los struct's en C)
  - Taules amb múltiples dimensions:      tipoDeDato nombreDeVariable [][]        y      tipoDeDato nombreDeVariable []**2 (yo me encargaré de este, no te ralles marta)

- Valors de qualsevulla dels tipus contemplats:

  - Declaració i ús de variables:      tipoDeDato nombreDeVariable: valorVariable
  - Constants:      const nombreConstante: valorConstante

- Operacions:

  - Assignació:      nombreDeVariable: valorVariable
  - Condicional:      si proposiciónAEvaluar { ··· } sino segundaProposiciónSiNoSeCumpleLaAnterior { ··· } no { ··· }      el 'no' es el else
  - Selecció múltiple (tipus switch):      selec proposiciónAEvaluar { caso valor1-> ··· fin; caso valor2-> ··· caso predeterminado-> ··· }
  - Bucles:

    - while:      loop (proposiciónAEvaluar) { ··· }
    - repeat until:      do { ··· } loop (proposiciónAEvaluar);
    - for:      floop (declaraciones; proposiciónAEvaluar; ejecutadoEnCadaIteración) { ··· }
    ~~- altres~~
  - Crida a procediments i funcions amb paràmetres:      nombreFuncion(parámetro1, parámetro2, ···);

  - Retorn de funcions si aquestes s’implementen:      nombreDeVariable: nombreFuncion(parámetro1, parámetro2, ···);

- Expressions aritmètiques i lògiques:

  - Fent ús de literals del tipus adient:      variable: 5+4;
  - Fent ús de constants i variables:      variable: constante + variable2;

- Operacions d’entrada i sortida:

  - Entrada per teclat:      enter();
  - Sortida per pantalla:      show();
  - Entrada i sortida des de fitxer:      write();/read();

- Operadors:

  - Aritmètics: suma, resta, producte, divisió, mòdul:      +, -, *, /, \
  - Relacionals: igual, diferent, major, menor, major o igual, menor o igual:      =, /=, >, <, >=, <=
  - Lògics: i, o, no:      &, |, ¬
  - Especials:
    - Pre increment/decrement:      ++variable/--variable
    - Post increment/decrement:      variable++/variable--
    - Assignació i operació alhora és a dir la combinació de cada operador amb l’operació d’assignació (+=, ...):      +=, -=, *=, /=, \=, &=, |=, **=
    - Operació condicional (? -> ):
    - altres:      potencia: ** y porcentaje: %, donde 5% = 0.05
