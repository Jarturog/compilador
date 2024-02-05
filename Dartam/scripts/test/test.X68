; ==============================================================================
; TITLE       : test
; COMPILED BY : jartu
; COMPILER BY : Juan Arturo Abaurrea Calafell
;               Dani Salanova Dmitriyev
;               Marta González Juan
; ==============================================================================

            ORG         $1000       ; Origen

t           DC.L 4                  
t_1         DC.L 1                  
t_2         DC.L 0                  
t_4         DC.L 2                  
t_5         DC.L 1                  
t_7         DC.L 3                  
t_8         DC.L 2                  
t_10        DC.L 4                  
t_11        DC.L 3                  
t_13        DC.L 0                  
t_14        DC.L 3                  
t_15        DC.L 0                  
t_20        DC.L 1                  
t_22        DC.L 1                  
argumentos  DS.L 1                  
aux         DS.L 1                  
d_v         DS.L 1                  
t_23        DS.L 1                  
t_21        DS.L 1                  
t_22        DS.L 1                  
t_20        DS.L 1                  
t_18        DS.L 1                  
t_19        DS.L 1                  
t_2         DS.L 1                  
t_16        DS.L 1                  
t_1         DS.L 1                  
t_17        DS.L 1                  
t_4         DS.L 1                  
t_3         DS.L 1                  
v_1         DS.L 1                  
i           DS.L 1                  
t_6         DS.L 1                  
j           DS.L 1                  
t_5         DS.L 1                  
t_8         DS.L 1                  
t_7         DS.L 1                  
t_9         DS.L 1                  
t           DS.L 1                  
v           DS.L 1                  
t_14        DS.L 1                  
t_15        DS.L 1                  
t_12        DS.L 1                  
t_13        DS.L 1                  
t_10        DS.L 1                  
t_11        DS.L 1                  
TEST                                ; Etiqueta inicial (main)

            JSR         e_inicio    ; Se ejecuta el main
            SIMHALT                 ; Fin de la ejecución

; --------- e_inicio: skip          
; --------- pmb e_inicio            
; --------- d_v = t                 
e_inicio:   LEA.L       t, A0       ; A0 = @t
            MOVE.L      A0, d_v     ; d_v = A0
; --------- t_3 = t_2 * 4           
;           A*B = A1A0*B1B0 = A0*B0 + A1*B1 * 2^16  
            MOVE.L      t_2, D0     ; D0 = t_2
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
            MOVE.L      D0, t_3     ; t_3 = D0
; --------- v[t_3] = t_1            
            MOVEA.L     v, A0       ; A0 = v
            ADDA.L      t_1, A0     ; A0 = A0 + t_1
            MOVE.L      t_3, (A0)   ; (A0) = t_3
; --------- t_6 = t_5 * 4           
;           A*B = A1A0*B1B0 = A0*B0 + A1*B1 * 2^16  
            MOVE.L      t_5, D0     ; D0 = t_5
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
            MOVE.L      D0, t_6     ; t_6 = D0
; --------- v[t_6] = t_4            
            MOVEA.L     v, A0       ; A0 = v
            ADDA.L      t_4, A0     ; A0 = A0 + t_4
            MOVE.L      t_6, (A0)   ; (A0) = t_6
; --------- t_9 = t_8 * 4           
;           A*B = A1A0*B1B0 = A0*B0 + A1*B1 * 2^16  
            MOVE.L      t_8, D0     ; D0 = t_8
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
            MOVE.L      D0, t_9     ; t_9 = D0
; --------- v[t_9] = t_7            
            MOVEA.L     v, A0       ; A0 = v
            ADDA.L      t_7, A0     ; A0 = A0 + t_7
            MOVE.L      t_9, (A0)   ; (A0) = t_9
; --------- t_12 = t_11 * 4         
;           A*B = A1A0*B1B0 = A0*B0 + A1*B1 * 2^16  
            MOVE.L      t_11, D0    ; D0 = t_11
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
            MOVE.L      D0, t_12    ; t_12 = D0
; --------- v[t_12] = t_10          
            MOVEA.L     v, A0       ; A0 = v
            ADDA.L      t_10, A0    ; A0 = A0 + t_10
            MOVE.L      t_12, (A0)  ; (A0) = t_12
; --------- param_s v               
            MOVE.L      v, -(SP)    ; PUSH INTO STACK v
; --------- call e_invertir         
            JSR         e_invertir  ; JUMP TO SUBROUTINE e_invertir
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- rtn e_inicio            
            RTS                     ; RETURN TO SUBROUTINE e_inicio




; --------- e_invertir: skip        
; --------- pmb e_invertir          
e_invertir: MOVE.L      4(SP), v    ; v = POP FROM STACK
; --------- i = t_13                
            LEA.L       t_13, A0    ; A0 = @t_13
            MOVE.L      A0, i       ; i = A0
; --------- j = t_14                
            LEA.L       t_14, A0    ; A0 = @t_14
            MOVE.L      A0, j       ; j = A0
; --------- e: skip                 
; --------- if i < j goto e_3       
.e:         MOVE.L      i, D0       ; D0 = i
            CMP.L       j, D0       ; UPDATE FLAGS WITH D0 - j
            BGE         .e_3        ; IF (N XOR V) FLAGS = 0 GOTO .e_3
; --------- goto e_4                
            JMP         .e_4        ; goto .e_4
; --------- e_3: skip               
; --------- t_15 = -1               
.e_3:       MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_15    ; t_15 = D0
; --------- e_4: skip               
; --------- if t_15 == 0 goto e_1   
.e_4:       MOVE.L      t_15, D0    ; D0 = t_15
            CMP.L       #0, D0      ; UPDATE FLAGS WITH D0 - #0
            BEQ         .e_1        ; IF Z FLAG = 1 GOTO .e_1
; --------- t_16 = v_1[i]           
            MOVEA.L     v_1, A0     ; A0 = v_1
            ADDA.L      i, A0       ; A0 = A0 + i
            MOVE.L      (A0), t_16  ; t_16 = (A0)
; --------- aux = t_16              
            LEA.L       t_16, A0    ; A0 = @t_16
            MOVE.L      A0, aux     ; aux = A0
; --------- t_17 = v_1[j]           
            MOVEA.L     v_1, A0     ; A0 = v_1
            ADDA.L      j, A0       ; A0 = A0 + j
            MOVE.L      (A0), t_17  ; t_17 = (A0)
; --------- t_18 = i * 4            
;           A*B = A1A0*B1B0 = A0*B0 + A1*B1 * 2^16  
            MOVE.L      i, D0       ; D0 = i
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
            MOVE.L      D0, t_18    ; t_18 = D0
; --------- v_1[t_18] = t_17        
            MOVEA.L     v_1, A0     ; A0 = v_1
            ADDA.L      t_17, A0    ; A0 = A0 + t_17
            MOVE.L      t_18, (A0)  ; (A0) = t_18
; --------- t_19 = j * 4            
;           A*B = A1A0*B1B0 = A0*B0 + A1*B1 * 2^16  
            MOVE.L      j, D0       ; D0 = j
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
            MOVE.L      D0, t_19    ; t_19 = D0
; --------- v_1[t_19] = aux         
            MOVEA.L     v_1, A0     ; A0 = v_1
            ADDA.L      aux, A0     ; A0 = A0 + aux
            MOVE.L      t_19, (A0)  ; (A0) = t_19
; --------- t_21 = i + t_20         
            LEA.L       i, A0       ; A0 = @i
            LEA.L       t_20, A1    ; A1 = @t_20
            ADD.L       A0, A1      ; A1 = A1 + A0
            MOVE.L      A1, t_21    ; t_21 = A1
; --------- i = t_21                
            LEA.L       t_21, A0    ; A0 = @t_21
            MOVE.L      A0, i       ; i = A0
; --------- t_23 = j - t_22         
            LEA.L       j, A0       ; A0 = @j
            LEA.L       t_22, A1    ; A1 = @t_22
            SUB.L       A0, A1      ; A1 = A1 - A0
            MOVE.L      A1, t_23    ; t_23 = A1
; --------- j = t_23                
            LEA.L       t_23, A0    ; A0 = @t_23
            MOVE.L      A0, j       ; j = A0
; --------- e_2: skip               
; --------- goto e                  
.e_2:       JMP         .e          ; goto .e
; --------- e_1: skip               
; --------- rtn e_invertir          
.e_1:       RTS                     ; RETURN TO SUBROUTINE e_invertir


            END TEST                ; Fin del programa
