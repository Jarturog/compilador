; ==============================================================================
; TITLE       : arr
; COMPILED BY : jartu
; COMPILER BY : Juan Arturo Abaurrea Calafell
;               Dani Salanova Dmitriyev
;               Marta González Juan
; ==============================================================================

            ORG         $1000       ; Origen

t           DC.L 3                  
t_1         DC.L 4                  
t_2         DC.L 5                  
t_4         DC.L 32                 
t_5         DC.L 0                  
t_6         DC.L 1                  
t_11        DC.L -1                 
t_12        DC.L 3                  
t_13        DC.L 4                  
t_14        DC.L 2                  
argumentos  DS.L 1                  
d_c         DS.L 1                  
c_1         DS.L 1                  
d_c_2       DS.L 1                  
d_c_1       DS.L 1                  
arr         DS.L 1                  
b           DS.L 1                  
c           DS.L 1                  
t_2         DS.L 1                  
t_1         DS.L 1                  
t_4         DS.L 1                  
t_3         DS.L 1                  
t_6         DS.L 1                  
d_arr       DS.L 1                  
t_5         DS.L 1                  
t_8         DS.L 1                  
t_7         DS.L 1                  
d_arr_1     DS.L 1                  
t_9         DS.L 1                  
t           DS.L 1                  
t_14        DS.L 1                  
t_12        DS.L 1                  
t_13        DS.L 1                  
t_10        DS.L 1                  
t_11        DS.L 1                  
ARR                                 ; Etiqueta inicial (main)

            JSR         e_inicio    ; Se ejecuta el main
            SIMHALT                 ; Fin de la ejecución

; --------- e_inicio: skip          
; --------- pmb e_inicio            
; --------- d_arr = t               
e_inicio:   LEA.L       t, A0       ; A0 = @t
            MOVE.L      A0, d_arr   ; d_arr = A0
; --------- t_3 = t_1 + t_2         
            LEA.L       t_1, A0     ; A0 = @t_1
            LEA.L       t_2, A1     ; A1 = @t_2
            ADD.L       A0, A1      ; A1 = A1 + A0
            MOVE.L      A1, t_3     ; t_3 = A1
; --------- d_arr_1 = t_3           
            LEA.L       t_3, A0     ; A0 = @t_3
            MOVE.L      A0, d_arr_1 ; d_arr_1 = A0
; --------- t_7 = t_5 * d_arr_1     
;           A*B = A1A0*B1B0 = A0*B0 + A1*B1 * 2^16  
            MOVE.L      t_5, D0     ; D0 = t_5
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
            MOVE.L      D0, t_7     ; t_7 = D0
; --------- t_8 = t_6 + t_7         
            LEA.L       t_6, A0     ; A0 = @t_6
            LEA.L       t_7, A1     ; A1 = @t_7
            ADD.L       A0, A1      ; A1 = A1 + A0
            MOVE.L      A1, t_8     ; t_8 = A1
; --------- t_9 = t_8 * 2           
;           A*B = A1A0*B1B0 = A0*B0 + A1*B1 * 2^16  
            MOVE.L      t_8, D0     ; D0 = t_8
            MOVE.L      #2, D1      ; D1 = #2
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
            MOVE.L      D0, t_9     ; t_9 = D0
; --------- arr[t_9] = t_4          
            MOVEA.L     arr, A0     ; A0 = arr
            ADDA.L      t_4, A0     ; A0 = A0 + t_4
            MOVE.L      t_9, (A0)   ; (A0) = t_9
; --------- c = arr                 
            LEA.L       arr, A0     ; A0 = @arr
            MOVE.L      A0, c       ; c = A0
; --------- t_10 = c + arr          
            LEA.L       c, A0       ; A0 = @c
            LEA.L       arr, A1     ; A1 = @arr
            ADD.L       A0, A1      ; A1 = A1 + A0
            MOVE.L      A1, t_10    ; t_10 = A1
; --------- b = t_10                
            LEA.L       t_10, A0    ; A0 = @t_10
            MOVE.L      A0, b       ; b = A0
; --------- if t_11 == 0 goto e_1   
            MOVE.L      t_11, D0    ; D0 = t_11
            CMP.L       #0, D0      ; UPDATE FLAGS WITH D0 - #0
            BEQ         .e_1        ; IF Z FLAG = 1 GOTO .e_1
; --------- c = arr                 
            LEA.L       arr, A0     ; A0 = @arr
            MOVE.L      A0, c       ; c = A0
; --------- d_c = t_12              
            LEA.L       t_12, A0    ; A0 = @t_12
            MOVE.L      A0, d_c     ; d_c = A0
; --------- d_c_1 = t_13            
            LEA.L       t_13, A0    ; A0 = @t_13
            MOVE.L      A0, d_c_1   ; d_c_1 = A0
; --------- d_c_2 = t_14            
            LEA.L       t_14, A0    ; A0 = @t_14
            MOVE.L      A0, d_c_2   ; d_c_2 = A0
; --------- goto e                  
            JMP         .e          ; goto .e
; --------- e_1: skip               
; --------- e: skip                 
; --------- rtn e_inicio            
.e:         RTS                     ; RETURN TO SUBROUTINE e_inicio




            END ARR                 ; Fin del programa
