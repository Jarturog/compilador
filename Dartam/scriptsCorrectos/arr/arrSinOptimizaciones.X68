; ==============================================================================
; TITLE       : arr
; COMPILED BY : jartu
; COMPILER BY : Juan Arturo Abaurrea Calafell
;               Dani Salanova Dmitriyev
;               Marta González Juan
; ==============================================================================

            ORG         $1000       ; Origen

t           DC.L 3                  ; INT
t_1         DC.L 4                  ; INT
t_2         DC.L ' '                ; CHAR
t_3         DC.L 0                  ; INT
t_4         DC.L 1                  ; INT
t_8         DC.L -1                 ; BOOLEAN
t_9         DC.L 3                  ; INT
t_10        DC.L 4                  ; INT
t_11        DC.L 2                  ; INT
arr         DS.B 28                 ; ARRAY
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
b           DS.L 1                  ; POINTER TO ARRAY, TUPLE OR STRING
c           DS.L 1                  ; POINTER TO ARRAY, TUPLE OR STRING
d_c         DS.L 1                  ; INT
t_6         DS.L 1                  ; INT
d_arr       DS.L 1                  ; INT
t_5         DS.L 1                  ; INT
t_7         DS.L 1                  ; INT
d_arr_1     DS.L 1                  ; INT
c_1         DS.B 36                 ; ARRAY
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
d_c_2       DS.L 1                  ; INT
d_c_1       DS.L 1                  ; INT

; --------- INITIALIZATION          
arr0                                ; Etiqueta inicial (main)
            JSR         e_inicio    ; Se ejecuta el main
            SIMHALT                 ; Fin de la ejecución

; --------- e_inicio: skip          
                                    
; --------- pmb e_inicio            
; --------- t = 3                   
e_inicio:   MOVE.L      #3, D0      ; D0 = #3
            MOVE.L      D0, t       ; t = D0
; --------- d_arr = t               
            MOVE.L      t, D0       ; D0 = t
            MOVE.L      D0, d_arr   ; d_arr = D0
; --------- t_1 = 4                 
            MOVE.L      #4, D0      ; D0 = #4
            MOVE.L      D0, t_1     ; t_1 = D0
; --------- d_arr_1 = t_1           
            MOVE.L      t_1, D0     ; D0 = t_1
            MOVE.L      D0, d_arr_1 ; d_arr_1 = D0
; --------- t_2 =                   
            MOVE.L      #' ', D0    ; D0 = #' '
            MOVE.L      D0, t_2     ; t_2 = D0
; --------- t_3 = 0                 
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_3     ; t_3 = D0
; --------- t_4 = 1                 
            MOVE.L      #1, D0      ; D0 = #1
            MOVE.L      D0, t_4     ; t_4 = D0
; --------- t_5 = t_3 * d_arr_1     
;           A*B = A1A0*B1B0 = A0*B0 + A1*B1 * 2^16  
            MOVE.L      t_3, D0     ; D0 = t_3
            MOVE.L      d_arr_1, D1 ; D1 = d_arr_1
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
            MOVE.L      D0, t_5     ; t_5 = D0
; --------- t_6 = t_4 + t_5         
            MOVE.L      t_4, D0     ; D0 = t_4
            MOVE.L      t_5, D1     ; D1 = t_5
            ADD.L       D0, D1      ; D1 = D1 + D0
            MOVE.L      D1, t_6     ; t_6 = D1
; --------- t_7 = t_6 * 4           
;           A*B = A1A0*B1B0 = A0*B0 + A1*B1 * 2^16  
            MOVE.L      t_6, D0     ; D0 = t_6
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
            MOVE.L      D0, t_7     ; t_7 = D0
; --------- arr[t_7] = t_2          
            MOVE.L      t_2, D0     ; D0 = t_2
            MOVE.L      t_7, D1     ; D1 = t_7
            LEA.L       arr, A0     ; A0 = arr
            ADDA.L      D1, A0      ; A0 = A0 + D1
            MOVE.L      D0, (A0)    ; (A0) = D0
; --------- c = arr                 
            LEA.L       arr, A0     ; A0 = arr
            MOVE.L      A0, c       ; c = A0
; --------- b = c                   
            MOVEA.L     c, A0       ; A0 = c
            MOVE.L      A0, b       ; b = A0
; --------- t_8 = -1                
            MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_8     ; t_8 = D0
; --------- if t_8 == 0 goto e_1    
            MOVE.L      t_8, D0     ; D0 = t_8
            MOVE.L      #0, D1      ; D1 = #0
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_1        ; IF Z FLAG = 1 GOTO .e_1
; --------- c = arr                 
            LEA.L       arr, A0     ; A0 = arr
            MOVE.L      A0, c       ; c = A0
; --------- t_9 = 3                 
            MOVE.L      #3, D0      ; D0 = #3
            MOVE.L      D0, t_9     ; t_9 = D0
; --------- d_c = t_9               
            MOVE.L      t_9, D0     ; D0 = t_9
            MOVE.L      D0, d_c     ; d_c = D0
; --------- t_10 = 4                
            MOVE.L      #4, D0      ; D0 = #4
            MOVE.L      D0, t_10    ; t_10 = D0
; --------- d_c_1 = t_10            
            MOVE.L      t_10, D0    ; D0 = t_10
            MOVE.L      D0, d_c_1   ; d_c_1 = D0
; --------- t_11 = 2                
            MOVE.L      #2, D0      ; D0 = #2
            MOVE.L      D0, t_11    ; t_11 = D0
; --------- d_c_2 = t_11            
            MOVE.L      t_11, D0    ; D0 = t_11
            MOVE.L      D0, d_c_2   ; d_c_2 = D0
; --------- goto e                  
            JMP         .e          ; goto .e
; --------- e_1: skip               
                                    
; --------- e: skip                 
.e_1:                               
; --------- rtn e_inicio            
.e:         RTS                     ; RETURN TO SUBROUTINE e_inicio




            END arr0                ; Fin del programa


