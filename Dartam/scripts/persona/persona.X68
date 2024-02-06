; ==============================================================================
; TITLE       : persona
; COMPILED BY : jartu
; COMPILER BY : Juan Arturo Abaurrea Calafell
;               Dani Salanova Dmitriyev
;               Marta González Juan
; ==============================================================================

            ORG         $1000       ; Origen

persona:    DS.L 1                  ; Inicializando el miembro id de la tupla persona
            DS.L 1                  ; Inicializando el miembro let de la tupla persona
aux         DC.B 'hola'             ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_1         DC.B 'mundo'            ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_2         DC.L 0                  ; INT
t_3         DC.L 4                  ; INT
p           DS.L 1                  ; POINTER TO ARRAY, TUPLE OR STRING
idpersona   DS.L 1                  ; INT
let         DS.L 1                  ; CHAR

; --------- SUBROUTINES             
e_show      MOVEA.L     4(SP), A1   ; A1 = POP FROM STACK
            MOVE.L      #13, D0     ; Task 13 of TRAP 15: Display the NULL terminated string pointed to by (A1) with CR, LF
            TRAP        #15         ; Interruption generated
            RTS                     ; RETURN TO SUBROUTINE ...
initpersona LEA.L       persona, A0 ; load persona into A0
            MOVEA.L     4(SP), A1   ; load instance into A1
            MOVE.L      #2, D0      
nextlongwordpersona CMP.L       #0, D0 
            BEQ         endinitpersona 
            MOVE.L      (A0)+, (A1)+ ; copy 4 bytes
            SUB.L       #1, D0      
            JMP         nextlongwordpersona 
endinitpersona                      ; end of loop
            RTS                     


; --------- INITIALIZATION          
persona0                            ; Etiqueta inicial (main)

            MOVE.L      #32, D0     ; Task 32 of TRAP 15: Hardware/Simulator
            MOVE.B      #5, D1      ; Enable exception processing (for input/output)
            TRAP        #15         ; Interruption generated
            JSR         e_inicio    ; Se ejecuta el main
            SIMHALT                 ; Fin de la ejecución

; --------- e_inicio: skip          
                                    
; --------- pmb e_inicio            
; --------- aux = hola              
; --------- param_s aux             
e_inicio:   MOVE.L      aux, D0     ; D0 = aux
            MOVE.L      D0, -(SP)   ; PUSH INTO STACK D0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- t_1 = mundo             
; --------- param_s t_1             
            LEA.L       t_1, A0     ; A0 = t_1
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- rtn e_inicio            
            RTS                     ; RETURN TO SUBROUTINE e_inicio




; --------- e_nuevapersona: skip    
                                    
; --------- pmb e_nuevapersona      
e_nuevapersona: MOVE.L      4(SP), D0 ; D0 = POP FROM STACK
            MOVE.L      D0, let     ; let = D0
            MOVE.L      8(SP), D0   ; D0 = POP FROM STACK
            MOVE.L      D0, idpersona ; idpersona = D0
            MOVE.L      12(SP), D0  ; D0 = POP FROM STACK
            MOVE.L      D0, p       ; p = D0
; --------- t_2 = 0                 
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_2     ; t_2 = D0
; --------- p[t_2] = idpersona      
            MOVE.L      idpersona, D0 ; D0 = idpersona
            MOVE.L      t_2, D1     ; D1 = t_2
            MOVEA.L     p, A0       ; A0 = p
            ADDA.L      D1, A0      ; A0 = A0 + D1
            MOVE.L      D0, (A0)    ; (A0) = D0
; --------- t_3 = 4                 
            MOVE.L      #4, D0      ; D0 = #4
            MOVE.L      D0, t_3     ; t_3 = D0
; --------- p[t_3] = let            
            MOVE.L      let, D0     ; D0 = let
            MOVE.L      t_3, D1     ; D1 = t_3
            MOVEA.L     p, A0       ; A0 = p
            ADDA.L      D1, A0      ; A0 = A0 + D1
            MOVE.L      D0, (A0)    ; (A0) = D0
; --------- rtn e_nuevapersona      
            RTS                     ; RETURN TO SUBROUTINE e_nuevapersona


            END persona0            ; Fin del programa


