; ==============================================================================
; TITLE       : calc2
; COMPILED BY : jartu
; COMPILER BY : Juan Arturo Abaurrea Calafell
;               Dani Salanova Dmitriyev
;               Marta González Juan
; ==============================================================================

            ORG         $1000       ; Origen

t           DC.L 256                ; INT
t_1         DC.B 'codigoIntermedio_calc2.txt'  ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
errRead     DC.B 'Error de lectura',0  ; Mensaje de error
            DS.W 0                  ; Para evitar imparidad
a           DS.L 1                  ; STRING
d_a         DS.L 1                  ; INT
CALC2                               ; Etiqueta inicial (main)

            MOVE.L      #32, D0     ; Task 32 of TRAP 15: Hardware/Simulator
            MOVE.B      #5, D1      ; Enable exception processing (for input/output)
            TRAP        #15         ; Interruption generated
            JSR         e_inicio    ; Se ejecuta el main
            SIMHALT                 ; Fin de la ejecución

; --------- e_inicio: skip          
; --------- pmb e_inicio            
; --------- d_a = t                 
e_inicio:   MOVE.L      t, D0       ; D0 = t
            MOVE.L      D0, d_a     ; d_a = D0
; --------- param_s t_1             
            LEA.L       t_1, A0     ; A0 = t_1
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK t_1
; --------- param_s a               
            LEA.L       a, A0       ; A0 = a
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK a
; --------- call e_from_1           
            JSR         e_from_1    ; JUMP TO SUBROUTINE e_from_1
            ADDA.L      #8, SP      ; SP = SP + 8
; --------- param_s a               
            LEA.L       a, A0       ; A0 = a
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK a
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- rtn e_inicio            
            RTS                     ; RETURN TO SUBROUTINE e_inicio




e_from_1    MOVEA.L     8(SP), A1   ; Pre: (A1) null terminated file name
            MOVEA.L     4(SP), A2   ; A2: pop
            MOVE.L      #51, D0     ; Task 51 of TRAP 15: Open existing file
            TRAP        #15         ; Interruption generated
            CMP.W       #2, D0      ; Si error
            BEQ         .e_from_1   ; Fin
            MOVEA.L     A2, A1      
            MOVE.L      #256, D2    
            MOVE.L      #53, D0     ; Task 53 of TRAP 15: Read file
            TRAP        #15         ; Interruption generated
            MOVE.L      #56, D0     ; Task 56 of TRAP 15: Close file
            TRAP        #15         ; Interruption generated
            RTS                     ; RETURN TO SUBROUTINE ...
.e_from_1:  LEA.L       errRead, A1 ; A1 = mnsj error
            MOVE.L      #13, D0     ; Task 13 of TRAP 15: Display the NULL terminated string pointed to by (A1) with CR, LF
            TRAP        #15         ; Interruption generated
            RTS                     ; RETURN TO SUBROUTINE ...
e_show      MOVEA.L     4(SP), A1   ; A1 = POP FROM STACK
            MOVE.L      #13, D0     ; Task 13 of TRAP 15: Display the NULL terminated string pointed to by (A1) with CR, LF
            TRAP        #15         ; Interruption generated
            RTS                     ; RETURN TO SUBROUTINE ...
            END CALC2               ; Fin del programa

