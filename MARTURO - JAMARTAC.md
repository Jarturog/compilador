#MARTURO - JAMARTAC

- Un cos general de programa on hi hagi d’haver els subprogrames, les declaracions i les instruccions del programa:      void main(letra[][] args) { ··· }
- Definició de subprogrames:      tipoRetornoSugerido nombreFunción(parámetros) { ··· }
- Tipus:
  o Enter:      ent
  o Cadena de caràcters:      letra[]
  o Lògic:      prop (de proposición, ya que puede ser cierta o falsa)
  o Altres*:      real, letra, puntero...
- Tipus definits per l’usuari:
  o Tuples:      tupla nombreTupla { ··· } (como los struct's en C)
  o Taules amb múltiples dimensions:      tipoDeDato nombreDeVariable [][]        y      tipoDeDato nombreDeVariable []**2 (yo me encargaré de este, no te ralles marta)
- Valors de qualsevulla dels tipus contemplats:
  o Declaració i ús de variables:      tipoDeDato nombreDeVariable: valorVariable
  o Constants:      const nombreConstante: valorConstante
- Operacions:
  o Assignació:      nombreDeVariable: valorVariable
  o Condicional:      ¿proposiciónAEvaluar? { ··· } ¿segundaProposiciónSiNoSeCumpleLaAnterior? { ··· } ¿no? { ··· }      el 'no' es el else
  o Selecció múltiple (tipus switch):      ¿¿proposiciónAEvaluar?? { caso valor1-> ··· fin; caso valor2-> ··· caso predeterminado-> ··· }
  o Bucles:
    ▪ while:      mientras (proposiciónAEvaluar) { ··· }
    ▪ repeat until:      haz { ··· } mientras (proposiciónAEvaluar);
    ▪ for:      para (declaraciones; proposiciónAEvaluar; ejecutadoEnCadaIteración) { ··· }
    ~~▪ altres~~
  o Crida a procediments i funcions amb paràmetres:      nombreFuncion(parámetro1, parámetro2, ···);
  o Retorn de funcions si aquestes s’implementen:      nombreDeVariable: nombreFuncion(parámetro1, parámetro2, ···);
- Expressions aritmètiques i lògiques:
  o Fent ús de literals del tipus adient:      variable: 5+4;
  o Fent ús de constants i variables:      variable: constante + variable2;
- Operacions d’entrada i sortida:
  o Entrada per teclat:      todavía a pensar
  o Sortida per pantalla:      todavía a pensar
  o Entrada i sortida des de fitxer:      todavía a pensar
- Operadors:
  o Aritmètics: suma, resta, producte, divisió, mòdul:      +, -, *, /, \
  o Relacionals: igual, diferent, major, menor, major o igual, menor o igual:      =, /=, >, <, >=, <=
  o Lògics: i, o, no:      &, |, ¬
  o Especials:
    ▪ Pre increment/decrement:      ++variable/--variable
    ▪ Post increment/decrement:      variable++/variable--
    ▪ Assignació i operació alhora és a dir la combinació de cada operador amb l’operació d’assignació (+=, ...):      +=, -=, *=, /=, \=, &=, |=, **=
    ▪ Operació condicional (? -> ):
    ▪ altres:      potencia: ** y porcentaje: %, donde 5% = 0.05
