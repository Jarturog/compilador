; ==============================================================================
; TITLE       : str
; COMPILED BY : jartu
; COMPILER BY : Juan Arturo Abaurrea Calafell
;               Dani Salanova Dmitriyev
;               Marta González Juan
; ==============================================================================

            ORG         $1000       ; Origen

t           DC.B 'Hola mundo',0     
argumentos  DS.L 1                  
string      DS.L 1                  
STR                                 ; Etiqueta inicial (main)
            MOVE.L      #32, D0     ; Task 32 of TRAP 15: Hardware/Simulator
            MOVE.B      #5, D1      ; Enable exception processing (for input/output)
            TRAP        #15         ; Interruption generated

            MOVE.L      #62, D0     ; Task 62 of TRAP 15: Enable/Disable keyboard IRQ
            MOVE.W      $0103, D1   ; Enable keyboard IRQ level 1 for key up and key down
            TRAP        #15         ; Interruption generated

; --------- e_inicio: skip          
; --------- pmb e_inicio            
; --------- t = Hola mundo          
; --------- string = t              
e_inicio    MOVE.L      t, D0       ; D0 = t
            MOVE.L      D0, string  ; string = D0
; --------- param_s string          
            MOVE.L      string, -(SP) ; PUSH INTO STACK string
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
; --------- rtn e_inicio            
            RTS                     ; RETURN TO SUBROUTINE e_inicio




            SIMHALT                 ; Fin de la ejecución
            END STR                 ; Fin del programa
