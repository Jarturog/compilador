; ==============================================================================
; TITLE       : mcd
; COMPILED BY : jartu
; COMPILER BY : Juan Arturo Abaurrea Calafell
;               Dani Salanova Dmitriyev
;               Marta González Juan
; ==============================================================================

            ORG         $1000       ; Origen

t           DC.L 16                 
t_1         DC.L 20                 
t_4         DC.L 0                  
t_5         DC.L 0                  
t_8         DC.L 0                  
t_12        DC.L 0                  
t_13        DC.L 0                  
t_15        DC.L 0                  
t_16        DC.L 0                  
argumentos  DS.L 1                  
num1_1      DS.L 1                  
num2_1      DS.L 1                  
b0          DS.L 1                  
a_1         DS.L 1                  
b_2         DS.L 1                  
a_3         DS.L 1                  
b_1         DS.L 1                  
a_2         DS.L 1                  
b_3         DS.L 1                  
num1        DS.L 1                  
num2        DS.L 1                  
a           DS.L 1                  
b           DS.L 1                  
resultado   DS.L 1                  
t_2         DS.L 1                  
t_16        DS.L 1                  
t_1         DS.L 1                  
t_4         DS.L 1                  
t_3         DS.L 1                  
t_6         DS.L 1                  
t_5         DS.L 1                  
t_8         DS.L 1                  
t_7         DS.L 1                  
c1          DS.L 1                  
c2          DS.L 1                  
a0          DS.L 1                  
t_9         DS.L 1                  
resultado_1 DS.L 1                  
t           DS.L 1                  
t_14        DS.L 1                  
t_15        DS.L 1                  
t_12        DS.L 1                  
t_13        DS.L 1                  
t_10        DS.L 1                  
t_11        DS.L 1                  
MCD                                 ; Etiqueta inicial (main)

            JSR         e_inicio    ; Se ejecuta el main
            SIMHALT                 ; Fin de la ejecución

; --------- e_inicio: skip          
; --------- pmb e_inicio            
; --------- a = t                   
e_inicio:   LEA.L       t, A0       ; A0 = @t
            MOVE.L      A0, a       ; a = A0
; --------- b = t_1                 
            LEA.L       t_1, A0     ; A0 = @t_1
            MOVE.L      A0, b       ; b = A0
; --------- param_s a               
            MOVE.L      a, -(SP)    ; PUSH INTO STACK a
; --------- param_s b               
            MOVE.L      b, -(SP)    ; PUSH INTO STACK b
; --------- call e_mcd1, t_2        
            JSR         e_mcd1      ; JUMP TO SUBROUTINE e_mcd1
            ADDA.L      #8, SP      ; SP = SP + 8
            MOVE.L      (SP)+, t_2  ; t_2 = POP FROM STACK
; --------- c1 = t_2                
            LEA.L       t_2, A0     ; A0 = @t_2
            MOVE.L      A0, c1      ; c1 = A0
; --------- param_s a               
            MOVE.L      a, -(SP)    ; PUSH INTO STACK a
; --------- param_s b               
            MOVE.L      b, -(SP)    ; PUSH INTO STACK b
; --------- call e_mcd2, t_3        
            JSR         e_mcd2      ; JUMP TO SUBROUTINE e_mcd2
            ADDA.L      #8, SP      ; SP = SP + 8
            MOVE.L      (SP)+, t_3  ; t_3 = POP FROM STACK
; --------- c2 = t_3                
            LEA.L       t_3, A0     ; A0 = @t_3
            MOVE.L      A0, c2      ; c2 = A0
; --------- rtn e_inicio            
            RTS                     ; RETURN TO SUBROUTINE e_inicio




; --------- e_mcd1: skip            
; --------- pmb e_mcd1              
e_mcd1:     MOVE.L      8(SP), a0   ; a0 = POP FROM STACK
            MOVE.L      12(SP), b0  ; b0 = POP FROM STACK
; --------- a_1 = a0                
            LEA.L       a0, A0      ; A0 = @a0
            MOVE.L      A0, a_1     ; a_1 = A0
; --------- b_1 = b0                
            LEA.L       b0, A0      ; A0 = @b0
            MOVE.L      A0, b_1     ; b_1 = A0
; --------- e: skip                 
; --------- if a_1 /= b_1 goto e_3  
.e:         MOVE.L      a_1, D0     ; D0 = a_1
            CMP.L       b_1, D0     ; UPDATE FLAGS WITH D0 - b_1
            BNE         .e_3        ; IF Z FLAG = 0 GOTO .e_3
; --------- goto e_4                
            JMP         .e_4        ; goto .e_4
; --------- e_3: skip               
; --------- t_4 = -1                
.e_3:       MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_4     ; t_4 = D0
; --------- e_4: skip               
; --------- if t_4 == 0 goto e_1    
.e_4:       MOVE.L      t_4, D0     ; D0 = t_4
            CMP.L       #0, D0      ; UPDATE FLAGS WITH D0 - #0
            BEQ         .e_1        ; IF Z FLAG = 1 GOTO .e_1
; --------- if a_1 > b_1 goto e_5   
            MOVE.L      a_1, D0     ; D0 = a_1
            CMP.L       b_1, D0     ; UPDATE FLAGS WITH D0 - b_1
            BLE         .e_5        ; IF ((N XOR V) OR Z) FLAGS = 1 GOTO .e_5
; --------- goto e_6                
            JMP         .e_6        ; goto .e_6
; --------- e_5: skip               
; --------- t_5 = -1                
.e_5:       MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_5     ; t_5 = D0
; --------- e_6: skip               
; --------- if t_5 == 0 goto e_8    
.e_6:       MOVE.L      t_5, D0     ; D0 = t_5
            CMP.L       #0, D0      ; UPDATE FLAGS WITH D0 - #0
            BEQ         .e_8        ; IF Z FLAG = 1 GOTO .e_8
; --------- t_6 = a_1 - b_1         
            LEA.L       a_1, A0     ; A0 = @a_1
            LEA.L       b_1, A1     ; A1 = @b_1
            SUB.L       A0, A1      ; A1 = A1 - A0
            MOVE.L      A1, t_6     ; t_6 = A1
; --------- a_1 = t_6               
            LEA.L       t_6, A0     ; A0 = @t_6
            MOVE.L      A0, a_1     ; a_1 = A0
; --------- goto e_7                
            JMP         .e_7        ; goto .e_7
; --------- e_8: skip               
; --------- t_7 = b_1 - a_1         
.e_8:       LEA.L       b_1, A0     ; A0 = @b_1
            LEA.L       a_1, A1     ; A1 = @a_1
            SUB.L       A0, A1      ; A1 = A1 - A0
            MOVE.L      A1, t_7     ; t_7 = A1
; --------- b_1 = t_7               
            LEA.L       t_7, A0     ; A0 = @t_7
            MOVE.L      A0, b_1     ; b_1 = A0
; --------- e_7: skip               
; --------- e_2: skip               
; --------- goto e                  
.e_2:       JMP         .e          ; goto .e
; --------- e_1: skip               
; --------- rtn e_mcd1, a_1         
.e_1:       MOVE.L      e_mcd1, -(SP) ; PUSH INTO STACK e_mcd1
            RTS                     ; RETURN TO SUBROUTINE e_mcd1


; --------- e_mcm: skip             
; --------- pmb e_mcm               
e_mcm:      MOVE.L      8(SP), num1 ; num1 = POP FROM STACK
            MOVE.L      12(SP), num2 ; num2 = POP FROM STACK
; --------- if num1 > num2 goto e_9  
            MOVE.L      num1, D0    ; D0 = num1
            CMP.L       num2, D0    ; UPDATE FLAGS WITH D0 - num2
            BLE         .e_9        ; IF ((N XOR V) OR Z) FLAGS = 1 GOTO .e_9
; --------- goto e_10               
            JMP         .e_10       ; goto .e_10
; --------- e_9: skip               
; --------- t_8 = -1                
.e_9:       MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_8     ; t_8 = D0
; --------- e_10: skip              
; --------- if t_8 == 0 goto e_12   
.e_10:      MOVE.L      t_8, D0     ; D0 = t_8
            CMP.L       #0, D0      ; UPDATE FLAGS WITH D0 - #0
            BEQ         .e_12       ; IF Z FLAG = 1 GOTO .e_12
; --------- a_2 = num1              
            LEA.L       num1, A0    ; A0 = @num1
            MOVE.L      A0, a_2     ; a_2 = A0
; --------- b_2 = num2              
            LEA.L       num2, A0    ; A0 = @num2
            MOVE.L      A0, b_2     ; b_2 = A0
; --------- goto e_11               
            JMP         .e_11       ; goto .e_11
; --------- e_12: skip              
; --------- a_2 = num2              
.e_12:      LEA.L       num2, A0    ; A0 = @num2
            MOVE.L      A0, a_2     ; a_2 = A0
; --------- b_2 = num1              
            LEA.L       num1, A0    ; A0 = @num1
            MOVE.L      A0, b_2     ; b_2 = A0
; --------- e_11: skip              
; --------- param_s num1            
.e_11:      MOVE.L      num1, -(SP) ; PUSH INTO STACK num1
; --------- param_s num2            
            MOVE.L      num2, -(SP) ; PUSH INTO STACK num2
; --------- call e_mcd2, t_9        
            JSR         e_mcd2      ; JUMP TO SUBROUTINE e_mcd2
            ADDA.L      #8, SP      ; SP = SP + 8
            MOVE.L      (SP)+, t_9  ; t_9 = POP FROM STACK
; --------- t_10 = a_2 / t_9        
            MOVE.L      a_2, D0     ; D0 = a_2
            DIVS.L      t_9, D0     ; D0.h = D0 % t_9. D0.l = D0 / t_9
            MOVE.B      D0, t_10    ; t_10 = D0.l
; --------- t_11 = t_10 * b_2       
;           A*B = A1A0*B1B0 = A0*B0 + A1*B1 * 2^16  
            MOVE.L      t_10, D0    ; D0 = t_10
            MOVE.L      b_2, D1     ; D1 = b_2
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
            MOVE.L      D0, t_11    ; t_11 = D0
; --------- resultado = t_11        
            LEA.L       t_11, A0    ; A0 = @t_11
            MOVE.L      A0, resultado ; resultado = A0
; --------- rtn e_mcm, resultado    
            MOVE.L      e_mcm, -(SP) ; PUSH INTO STACK e_mcm
            RTS                     ; RETURN TO SUBROUTINE e_mcm


; --------- e_mcd2: skip            
; --------- pmb e_mcd2              
e_mcd2:     MOVE.L      8(SP), num1 ; num1 = POP FROM STACK
            MOVE.L      12(SP), num2 ; num2 = POP FROM STACK
; --------- if num1_1 > num2_1 goto e_13  
            MOVE.L      num1_1, D0  ; D0 = num1_1
            CMP.L       num2_1, D0  ; UPDATE FLAGS WITH D0 - num2_1
            BLE         .e_13       ; IF ((N XOR V) OR Z) FLAGS = 1 GOTO .e_13
; --------- goto e_14               
            JMP         .e_14       ; goto .e_14
; --------- e_13: skip              
; --------- t_12 = -1               
.e_13:      MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_12    ; t_12 = D0
; --------- e_14: skip              
; --------- if t_12 == 0 goto e_16  
.e_14:      MOVE.L      t_12, D0    ; D0 = t_12
            CMP.L       #0, D0      ; UPDATE FLAGS WITH D0 - #0
            BEQ         .e_16       ; IF Z FLAG = 1 GOTO .e_16
; --------- a_3 = num1_1            
            LEA.L       num1_1, A0  ; A0 = @num1_1
            MOVE.L      A0, a_3     ; a_3 = A0
; --------- b_3 = num2_1            
            LEA.L       num2_1, A0  ; A0 = @num2_1
            MOVE.L      A0, b_3     ; b_3 = A0
; --------- goto e_15               
            JMP         .e_15       ; goto .e_15
; --------- e_16: skip              
; --------- a_3 = num2_1            
.e_16:      LEA.L       num2_1, A0  ; A0 = @num2_1
            MOVE.L      A0, a_3     ; a_3 = A0
; --------- b_3 = num1_1            
            LEA.L       num1_1, A0  ; A0 = @num1_1
            MOVE.L      A0, b_3     ; b_3 = A0
; --------- e_15: skip              
; --------- resultado_1 = t_13      
.e_15:      LEA.L       t_13, A0    ; A0 = @t_13
            MOVE.L      A0, resultado_1 ; resultado_1 = A0
; --------- e_17: skip              
; --------- resultado_1 = b_3       
.e_17:      LEA.L       b_3, A0     ; A0 = @b_3
            MOVE.L      A0, resultado_1 ; resultado_1 = A0
; --------- t_14 = a_3 mod b_3      
            MOVE.L      a_3, D0     ; D0 = a_3
            DIVS.L      b_3, D0     ; D0.h = D0 % b_3. D0.l = D0 / b_3
            LSR.L       #8, D0      ; D0.l = D0.h
            LSR.L       #8, D0      ; D0.l = D0.h
            MOVE.B      D0, t_14    ; t_14 = D0.l
; --------- b_3 = t_14              
            LEA.L       t_14, A0    ; A0 = @t_14
            MOVE.L      A0, b_3     ; b_3 = A0
; --------- a_3 = resultado_1       
            LEA.L       resultado_1, A0 ; A0 = @resultado_1
            MOVE.L      A0, a_3     ; a_3 = A0
; --------- e_19: skip              
; --------- if b_3 /= t_15 goto e_20  
.e_19:      MOVE.L      b_3, D0     ; D0 = b_3
            CMP.L       t_15, D0    ; UPDATE FLAGS WITH D0 - t_15
            BNE         .e_20       ; IF Z FLAG = 0 GOTO .e_20
; --------- goto e_21               
            JMP         .e_21       ; goto .e_21
; --------- e_20: skip              
; --------- t_16 = -1               
.e_20:      MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_16    ; t_16 = D0
; --------- e_21: skip              
; --------- if t_16 == 0 goto e_18  
.e_21:      MOVE.L      t_16, D0    ; D0 = t_16
            CMP.L       #0, D0      ; UPDATE FLAGS WITH D0 - #0
            BEQ         .e_18       ; IF Z FLAG = 1 GOTO .e_18
; --------- goto e_17               
            JMP         .e_17       ; goto .e_17
; --------- e_18: skip              
; --------- rtn e_mcd2, resultado_1  
.e_18:      MOVE.L      e_mcd2, -(SP) ; PUSH INTO STACK e_mcd2
            RTS                     ; RETURN TO SUBROUTINE e_mcd2


            END MCD                 ; Fin del programa
