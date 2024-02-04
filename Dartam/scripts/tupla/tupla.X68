; ==============================================================================
; TITLE       : tupla
; COMPILED BY : jartu
; COMPILER BY : Juan Arturo Abaurrea Calafell
;               Dani Salanova Dmitriyev
;               Marta González Juan
; ==============================================================================

            ORG         $1000       ; Origen

t           DC.L 3                  
t_1         DC.L 4                  
t_2         DC.L 5                  
t_4         DC.L 0                  
t_5         DC.L 1                  
t_9         DC.L 0                  
t_11        DC.L 4                  
t_13        DC.L 6                  
t_15        DC.L 0                  
t_17        DC.L 1                  
bb          DS.L 1                  
argumentos  DS.L 1                  
num         DS.L 1                  
numeraso    DS.L 1                  
b_1         DS.L 1                  
let         DS.L 1                  
t_23        DS.L 1                  
t_24        DS.L 1                  
t_21        DS.L 1                  
t_22        DS.L 1                  
t_20        DS.L 1                  
arr         DS.L 1                  
aa          DS.L 1                  
a           DS.L 1                  
b           DS.L 1                  
c           DS.L 1                  
t_18        DS.L 1                  
t_19        DS.L 1                  
t_2         DS.L 1                  
t_16        DS.L 1                  
t_1         DS.L 1                  
t_17        DS.L 1                  
t_4         DS.L 1                  
t_3         DS.L 1                  
t_6         DS.L 1                  
d_arr       DS.L 1                  
t_5         DS.L 1                  
t_8         DS.L 1                  
t_7         DS.L 1                  
n           DS.L 1                  
d_arr_1     DS.L 1                  
t_9         DS.L 1                  
numdeverdad DS.L 1                  
t           DS.L 1                  
t_14        DS.L 1                  
t_15        DS.L 1                  
t_12        DS.L 1                  
t_13        DS.L 1                  
t_10        DS.L 1                  
t_11        DS.L 1                  
TUPLA                               ; Etiqueta inicial (main)

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
; --------- c = arr                 
            LEA.L       arr, A0     ; A0 = @arr
            MOVE.L      A0, c       ; c = A0
; --------- c = arr                 
            LEA.L       arr, A0     ; A0 = @arr
            MOVE.L      A0, c       ; c = A0
; --------- t_6 = t_4 * d_arr_1     
;           A*B = A1A0*B1B0 = A0*B0 + A1*B1 * 2^16  
            MOVE.L      t_4, D0     ; D0 = t_4
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
            MOVE.L      D0, t_6     ; t_6 = D0
; --------- t_7 = t_5 + t_6         
            LEA.L       t_5, A0     ; A0 = @t_5
            LEA.L       t_6, A1     ; A1 = @t_6
            ADD.L       A0, A1      ; A1 = A1 + A0
            MOVE.L      A1, t_7     ; t_7 = A1
; --------- t_8 = t_7 * 4           
;           A*B = A1A0*B1B0 = A0*B0 + A1*B1 * 2^16  
            MOVE.L      t_7, D0     ; D0 = t_7
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
            MOVE.L      D0, t_8     ; t_8 = D0
; --------- arr[t_8] = b            
            MOVEA.L     arr, A0     ; A0 = arr
            ADDA.L      b, A0       ; A0 = A0 + b
            MOVE.L      t_8, (A0)   ; (A0) = t_8
; --------- t_10 = b[t_9]           
            MOVEA.L     b, A0       ; A0 = b
            ADDA.L      t_9, A0     ; A0 = A0 + t_9
            MOVE.L      (A0), t_10  ; t_10 = (A0)
; --------- num = t_10              
            LEA.L       t_10, A0    ; A0 = @t_10
            MOVE.L      A0, num     ; num = A0
; --------- t_12 = b[t_11]          
            MOVEA.L     b, A0       ; A0 = b
            ADDA.L      t_11, A0    ; A0 = A0 + t_11
            MOVE.L      (A0), t_12  ; t_12 = (A0)
; --------- let = t_12              
            LEA.L       t_12, A0    ; A0 = @t_12
            MOVE.L      A0, let     ; let = A0
; --------- t_14 = b[t_13]          
            MOVEA.L     b, A0       ; A0 = b
            ADDA.L      t_13, A0    ; A0 = A0 + t_13
            MOVE.L      (A0), t_14  ; t_14 = (A0)
; --------- numeraso = t_14         
            LEA.L       t_14, A0    ; A0 = @t_14
            MOVE.L      A0, numeraso ; numeraso = A0
; --------- t_16 = numeraso[t_15]   
            MOVEA.L     numeraso, A0 ; A0 = numeraso
            ADDA.L      t_15, A0    ; A0 = A0 + t_15
            MOVE.L      (A0), t_16  ; t_16 = (A0)
; --------- numdeverdad = t_16      
            LEA.L       t_16, A0    ; A0 = @t_16
            MOVE.L      A0, numdeverdad ; numdeverdad = A0
; --------- n = t_17                
            LEA.L       t_17, A0    ; A0 = @t_17
            MOVE.L      A0, n       ; n = A0
; --------- t_18 = n - 1            
            LEA.L       n, A0       ; A0 = @n
            MOVE.L      #1, D0      ; D0 = #1
            SUB.L       A0, D0      ; D0 = D0 - A0
            MOVE.L      D0, t_18    ; t_18 = D0
; --------- n = t_18                
            LEA.L       t_18, A0    ; A0 = @t_18
            MOVE.L      A0, n       ; n = A0
; --------- param_s numdeverdad     
            MOVE.L      numdeverdad, -(SP) ; PUSH INTO STACK numdeverdad
; --------- call e_ab, t_19         
            JSR         e_ab        ; JUMP TO SUBROUTINE e_ab
            ADDA.L      #4, SP      ; SP = SP + 4
            MOVE.L      (SP)+, t_19 ; t_19 = POP FROM STACK
; --------- t_20 = n + t_19         
            LEA.L       n, A0       ; A0 = @n
            LEA.L       t_19, A1    ; A1 = @t_19
            ADD.L       A0, A1      ; A1 = A1 + A0
            MOVE.L      A1, t_20    ; t_20 = A1
; --------- aa = t_20               
            LEA.L       t_20, A0    ; A0 = @t_20
            MOVE.L      A0, aa      ; aa = A0
; --------- t_21 = n + 1            
            LEA.L       n, A0       ; A0 = @n
            MOVE.L      #1, D0      ; D0 = #1
            ADD.L       A0, D0      ; D0 = D0 + A0
            MOVE.L      D0, t_21    ; t_21 = D0
; --------- t_22 = n                
            LEA.L       n, A0       ; A0 = @n
            MOVE.L      A0, t_22    ; t_22 = A0
; --------- n = t_21                
            LEA.L       t_21, A0    ; A0 = @t_21
            MOVE.L      A0, n       ; n = A0
; --------- param_s numdeverdad     
            MOVE.L      numdeverdad, -(SP) ; PUSH INTO STACK numdeverdad
; --------- call e_ab, t_23         
            JSR         e_ab        ; JUMP TO SUBROUTINE e_ab
            ADDA.L      #4, SP      ; SP = SP + 4
            MOVE.L      (SP)+, t_23 ; t_23 = POP FROM STACK
; --------- t_24 = t_22 + t_23      
            LEA.L       t_22, A0    ; A0 = @t_22
            LEA.L       t_23, A1    ; A1 = @t_23
            ADD.L       A0, A1      ; A1 = A1 + A0
            MOVE.L      A1, t_24    ; t_24 = A1
; --------- bb = t_24               
            LEA.L       t_24, A0    ; A0 = @t_24
            MOVE.L      A0, bb      ; bb = A0
; --------- rtn e_inicio            
            RTS                     ; RETURN TO SUBROUTINE e_inicio




; --------- e_ab: skip              
; --------- pmb e_ab                
e_ab:       MOVE.L      8(SP), b    ; b = POP FROM STACK
; --------- rtn e_ab, b_1           
            MOVE.L      e_ab, -(SP) ; PUSH INTO STACK e_ab
            RTS                     ; RETURN TO SUBROUTINE e_ab


            END TUPLA               ; Fin del programa
