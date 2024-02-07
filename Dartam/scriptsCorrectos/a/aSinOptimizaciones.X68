; ==============================================================================
; TITLE       : a
; COMPILED BY : jartu
; COMPILER BY : Juan Arturo Abaurrea Calafell
;               Dani Salanova Dmitriyev
;               Marta Gonzalez Juan
; ==============================================================================

            ORG         $1000       ; Origen

t           DC.L 4                  ; INT
t_1         DC.L 4                  ; INT
t_2         DC.L 'A'                ; CHAR
t_3         DC.L 2                  ; INT
t_6         DC.L 2                  ; INT
t_7         DC.L 3                  ; INT
t_4         DS.L 1                  ; INT
d_matrizcars DS.L 1                 ; INT
t_5         DS.L 1                  ; CHAR
t_8         DS.L 1                  ; INT
t_9         DS.L 1                  ; INT
d_matrizcars_1 DS.L 1               ; INT
matrizcars  DS.B 64                 ; ARRAY
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_10        DS.L 1                  ; INT

; --------- INITIALIZATION          
a                                   ; Etiqueta inicial (main)
            JSR         e_inicio    ; Se ejecuta el main
            SIMHALT                 ; Fin de la ejecucion

; --------- e_inicio: skip          
                                    
; --------- pmb e_inicio            
; --------- t = 4                   
e_inicio:   MOVE.L      #4, D0      ; D0 = #4
            MOVE.L      D0, t       ; t = D0
; --------- d_matrizcars = t        
            MOVE.L      t, D0       ; D0 = t
            MOVE.L      D0, d_matrizcars ; d_matrizcars = D0
; --------- t_1 = 4                 
            MOVE.L      #4, D0      ; D0 = #4
            MOVE.L      D0, t_1     ; t_1 = D0
; --------- d_matrizcars_1 = t_1    
            MOVE.L      t_1, D0     ; D0 = t_1
            MOVE.L      D0, d_matrizcars_1 ; d_matrizcars_1 = D0
; --------- t_2 = A                 
            MOVE.L      #'A', D0    ; D0 = #'A'
            MOVE.L      D0, t_2     ; t_2 = D0
; --------- t_3 = 2                 
            MOVE.L      #2, D0      ; D0 = #2
            MOVE.L      D0, t_3     ; t_3 = D0
; --------- t_4 = t_3               
            MOVE.L      t_3, D0     ; D0 = t_3
            MOVE.L      D0, t_4     ; t_4 = D0
; --------- t_5 = t_2 + t_4         
            MOVE.L      t_2, D0     ; D0 = t_2
            MOVE.L      t_4, D1     ; D1 = t_4
            ADD.L       D0, D1      ; D1 = D1 + D0
            MOVE.L      D1, t_5     ; t_5 = D1
; --------- t_6 = 2                 
            MOVE.L      #2, D0      ; D0 = #2
            MOVE.L      D0, t_6     ; t_6 = D0
; --------- t_7 = 3                 
            MOVE.L      #3, D0      ; D0 = #3
            MOVE.L      D0, t_7     ; t_7 = D0
; --------- t_8 = t_7 * d_matrizcars_1  
;           A*B = A1A0*B1B0 = A0*B0 + A1*B1 * 2^16  
            MOVE.L      t_7, D0     ; D0 = t_7
            MOVE.L      d_matrizcars_1, D1 ; D1 = d_matrizcars_1
            MOVE.W      D0, D2      ; D2.L = D1.L
            MOVE.W      D1, D3      ; D1.L = D3.L
            ASR.L       #8, D0      ; FIRST 8 BITS OF D0 MOVED RIGHT
            ASR.L       #8, D0      ; D0.L = old D0.H
            ASR.L       #8, D1      ; FIRST 8 BITS OF D1 MOVED RIGHT
            ASR.L       #8, D1      ; D1.L = old D1.H
            MULS.W      D1, D0      ; D0 = D0 * D1
            MULS.W      D2, D3      ; D3 = D2 * D3
            ASL.L       #8, D0      ; FIRST 8 BITS OF D0 MOVED LEFT
            ASL.L       #8, D0      ; D0.H = old D0.L
            ADD.L       D3, D0      ; D0 = D0 + D3
            MOVE.L      D0, t_8     ; t_8 = D0
; --------- t_9 = t_7 + t_8         
            MOVE.L      t_7, D0     ; D0 = t_7
            MOVE.L      t_8, D1     ; D1 = t_8
            ADD.L       D0, D1      ; D1 = D1 + D0
            MOVE.L      D1, t_9     ; t_9 = D1
; --------- t_10 = t_9 * 4          
;           A*B = A1A0*B1B0 = A0*B0 + A1*B1 * 2^16  
            MOVE.L      t_9, D0     ; D0 = t_9
            MOVE.L      #4, D1      ; D1 = #4
            MOVE.W      D0, D2      ; D2.L = D1.L
            MOVE.W      D1, D3      ; D1.L = D3.L
            ASR.L       #8, D0      ; FIRST 8 BITS OF D0 MOVED RIGHT
            ASR.L       #8, D0      ; D0.L = old D0.H
            ASR.L       #8, D1      ; FIRST 8 BITS OF D1 MOVED RIGHT
            ASR.L       #8, D1      ; D1.L = old D1.H
            MULS.W      D1, D0      ; D0 = D0 * D1
            MULS.W      D2, D3      ; D3 = D2 * D3
            ASL.L       #8, D0      ; FIRST 8 BITS OF D0 MOVED LEFT
            ASL.L       #8, D0      ; D0.H = old D0.L
            ADD.L       D3, D0      ; D0 = D0 + D3
            MOVE.L      D0, t_10    ; t_10 = D0
; --------- matrizcars[t_10] = t_5  
            MOVE.L      t_5, D0     ; D0 = t_5
            MOVE.L      t_10, D1    ; D1 = t_10
            LEA.L       matrizcars, A0 ; A0 = matrizcars
            ADDA.L      D1, A0      ; A0 = A0 + D1
            MOVE.L      D0, (A0)    ; (A0) = D0
; --------- rtn e_inicio            
            RTS                     ; RETURN TO SUBROUTINE e_inicio




            END a                   ; Fin del programa


