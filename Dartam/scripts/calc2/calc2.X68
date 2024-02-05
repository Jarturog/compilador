; ==============================================================================
; TITLE       : CALC2
; COMPILED BY : jartu
; COMPILER BY : Juan Arturo Abaurrea Calafell
;               Dani Salanova Dmitriyev
;               Marta González Juan
; ==============================================================================

            ORG         $1000       ; Origen

t           DC.L 2                  ; INT
t_1         DC.B '2',0              ; CHAR
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
a           DS.L 1                  ; INT
b           DS.L 1                  ; INT
t_2         DS.L 1                  ; INT
CALC2                               ; Etiqueta inicial (main)

            JSR         e_inicio    ; Se ejecuta el main
            SIMHALT                 ; Fin de la ejecución

; --------- e_inicio: skip          
; --------- pmb e_inicio            
; --------- a = t                   
e_inicio:   MOVE.L      t, D0       ; D0 = t
            MOVE.L      D0, a       ; a = D0
; --------- t_2 = t_1               
            CLR.L       D0          ; CLEAR D0
            MOVE.B      t_1, D0     ; D0 = t_1
            MOVE.L      D0, t_2     ; t_2 = D0
; --------- b = t_2                 
            MOVE.L      t_2, D0     ; D0 = t_2
            MOVE.L      D0, b       ; b = D0
; --------- rtn e_inicio            
            RTS                     ; RETURN TO SUBROUTINE e_inicio




            END CALC2               ; Fin del programa
