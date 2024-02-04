; ==============================================================================
; TITLE       : str
; COMPILED BY : jartu
; COMPILER BY : Juan Arturo Abaurrea Calafell
;               Dani Salanova Dmitriyev
;               Marta González Juan
; ==============================================================================

            ORG         $1000       ; Origen

t           DC.B 'Hola mundo',0     
t_1         DC.B 'miau',0           
argumentos  DS.L 1                  
string      DS.L 1                  
STR                                 ; Etiqueta inicial (main)
            MOVE.L      #32, D0     ; Task 32 of TRAP 15: Hardware/Simulator
            MOVE.B      #5, D1      ; Enable exception processing (for input/output)
            TRAP        #15         ; Interruption generated

            MOVE.L      #62, D0     ; Task 62 of TRAP 15: Enable/Disable keyboard IRQ
            MOVE.W      #$0103, D1  ; Enable keyboard IRQ level 1 for key up and key down
            TRAP        #15         ; Interruption generated

            JSR         e_inicio    ; Se ejecuta el main
            SIMHALT                 ; Fin de la ejecución

; --------- e_inicio: skip          
; --------- pmb e_inicio            
; --------- t = Hola mundo          
; --------- string = t              
e_inicio:   LEA.L       t, A0       ; A0 = (t)
            MOVE.L      A0, string  ; string = A0
; --------- param_s string          
            MOVE.L      string, -(SP) ; PUSH INTO STACK string
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- t_1 = miau              
; --------- string = t_1            
            LEA.L       t_1, A0     ; A0 = (t_1)
            MOVE.L      A0, string  ; string = A0
; --------- param_s string          
            MOVE.L      string, -(SP) ; PUSH INTO STACK string
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- rtn e_inicio            
            RTS                     ; RETURN TO SUBROUTINE e_inicio




e_show      MOVEA.L     4(SP), A1   ; A1 = POP FROM STACK
            MOVE.L      #14, D0     ; Task 14 of TRAP 15: Display the NULL terminated string pointed to by (A1)
            TRAP        #15         ; Interruption generated
            RTS                     ; RETURN TO SUBROUTINE ...
            END STR                 ; Fin del programa
