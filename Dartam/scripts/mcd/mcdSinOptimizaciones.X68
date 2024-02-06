; ==============================================================================
; TITLE       : mcd
; COMPILED BY : jartu
; COMPILER BY : Juan Arturo Abaurrea Calafell
;               Dani Salanova Dmitriyev
;               Marta González Juan
; ==============================================================================

            ORG         $1000       ; Origen

t           DC.L 16                 ; INT
t_1         DC.L 20                 ; INT
t_5         DC.B 0                  ; BOOLEAN
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_6         DC.B 0                  ; BOOLEAN
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_9         DC.B 0                  ; BOOLEAN
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_13        DC.B 0                  ; BOOLEAN
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_14        DC.L 0                  ; INT
t_16        DC.L 0                  ; INT
t_17        DC.B 0                  ; BOOLEAN
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
num1_1      DS.L 1                  ; INT
num2_1      DS.L 1                  ; INT
b0          DS.L 1                  ; INT
a_1         DS.L 1                  ; INT
b_2         DS.L 1                  ; INT
a_3         DS.L 1                  ; INT
b_1         DS.L 1                  ; INT
a_2         DS.L 1                  ; INT
b_3         DS.L 1                  ; INT
num1        DS.L 1                  ; INT
num2        DS.L 1                  ; INT
a           DS.L 1                  ; INT
b           DS.L 1                  ; INT
resultado   DS.L 1                  ; INT
t_2         DS.L 1                  ; INT
t_4         DS.L 1                  ; INT
t_3         DS.L 1                  ; INT
t_8         DS.L 1                  ; INT
t_7         DS.L 1                  ; INT
c1          DS.L 1                  ; INT
a0          DS.L 1                  ; INT
c2          DS.L 1                  ; INT
c3          DS.L 1                  ; INT
resultado_1 DS.L 1                  ; INT
t_15        DS.L 1                  ; INT
t_12        DS.L 1                  ; INT
t_10        DS.L 1                  ; INT
t_11        DS.L 1                  ; INT
MCD                                 ; Etiqueta inicial (main)
            JSR         e_inicio    ; Se ejecuta el main
            SIMHALT                 ; Fin de la ejecución

; --------- e_inicio: skip          
                                    
; --------- pmb e_inicio            
; --------- t = 16                  
e_inicio:   MOVE.L      #16, D0     ; D0 = #16
            MOVE.L      D0, t       ; t = D0
; --------- a = t                   
            MOVE.L      t, D0       ; D0 = t
            MOVE.L      D0, a       ; a = D0
; --------- t_1 = 20                
            MOVE.L      #20, D0     ; D0 = #20
            MOVE.L      D0, t_1     ; t_1 = D0
; --------- b = t_1                 
            MOVE.L      t_1, D0     ; D0 = t_1
            MOVE.L      D0, b       ; b = D0
; --------- param_s a               
            MOVE.L      a, -(SP)    ; PUSH INTO STACK a
; --------- param_s b               
            MOVE.L      b, -(SP)    ; PUSH INTO STACK b
; --------- call e_mcd1, t_2        
            SUBA.L      #4, SP      ; SP = SP + 4
            JSR         e_mcd1      ; JUMP TO SUBROUTINE e_mcd1
            MOVE.L      (SP)+, t_2  ; t_2 = POP FROM STACK
            ADDA.L      #8, SP      ; SP = SP + 8
; --------- c1 = t_2                
            MOVE.L      t_2, D0     ; D0 = t_2
            MOVE.L      D0, c1      ; c1 = D0
; --------- param_s a               
            MOVE.L      a, -(SP)    ; PUSH INTO STACK a
; --------- param_s b               
            MOVE.L      b, -(SP)    ; PUSH INTO STACK b
; --------- call e_mcd2, t_3        
            SUBA.L      #4, SP      ; SP = SP + 4
            JSR         e_mcd2      ; JUMP TO SUBROUTINE e_mcd2
            MOVE.L      (SP)+, t_3  ; t_3 = POP FROM STACK
            ADDA.L      #8, SP      ; SP = SP + 8
; --------- c2 = t_3                
            MOVE.L      t_3, D0     ; D0 = t_3
            MOVE.L      D0, c2      ; c2 = D0
; --------- param_s a               
            MOVE.L      a, -(SP)    ; PUSH INTO STACK a
; --------- param_s b               
            MOVE.L      b, -(SP)    ; PUSH INTO STACK b
; --------- call e_mcm, t_4         
            SUBA.L      #4, SP      ; SP = SP + 4
            JSR         e_mcm       ; JUMP TO SUBROUTINE e_mcm
            MOVE.L      (SP)+, t_4  ; t_4 = POP FROM STACK
            ADDA.L      #8, SP      ; SP = SP + 8
; --------- c3 = t_4                
            MOVE.L      t_4, D0     ; D0 = t_4
            MOVE.L      D0, c3      ; c3 = D0
; --------- rtn e_inicio            
            RTS                     ; RETURN TO SUBROUTINE e_inicio




; --------- e_mcd1: skip            
                                    
; --------- pmb e_mcd1              
e_mcd1:     MOVE.L      8(SP), b0   ; b0 = POP FROM STACK
            MOVE.L      12(SP), a0  ; a0 = POP FROM STACK
; --------- a_1 = a0                
            MOVE.L      a0, D0      ; D0 = a0
            MOVE.L      D0, a_1     ; a_1 = D0
; --------- b_1 = b0                
            MOVE.L      b0, D0      ; D0 = b0
            MOVE.L      D0, b_1     ; b_1 = D0
; --------- e: skip                 
                                    
; --------- if a_1 /= b_1 goto e_3  
.e:         MOVE.L      a_1, D0     ; D0 = a_1
            CMP.L       b_1, D0     ; UPDATE FLAGS WITH D0 - b_1
            BNE         .e_3        ; IF Z FLAG = 0 GOTO .e_3
; --------- t_5 = 0                 
            CLR.L       D0          ; CLEAR D0
            MOVE.B      #0, D0      ; D0 = #0
            MOVE.B      D0, t_5     ; t_5 = D0
; --------- goto e_4                
            JMP         .e_4        ; goto .e_4
; --------- e_3: skip               
                                    
; --------- t_5 = -1                
.e_3:       CLR.L       D0          ; CLEAR D0
            MOVE.B      #-1, D0     ; D0 = #-1
            MOVE.B      D0, t_5     ; t_5 = D0
; --------- e_4: skip               
                                    
; --------- if t_5 == 0 goto e_1    
.e_4:       CLR.L       D0          ; CLEAR D0
            MOVE.B      t_5, D0     ; D0 = t_5
            CMP.B       #0, D0      ; UPDATE FLAGS WITH D0 - #0
            BEQ         .e_1        ; IF Z FLAG = 1 GOTO .e_1
; --------- if a_1 > b_1 goto e_5   
            MOVE.L      a_1, D0     ; D0 = a_1
            CMP.L       b_1, D0     ; UPDATE FLAGS WITH D0 - b_1
            BGT         .e_5        ; IF ((N XOR V) OR Z) FLAGS = 0 GOTO .e_5
; --------- t_6 = 0                 
            CLR.L       D0          ; CLEAR D0
            MOVE.B      #0, D0      ; D0 = #0
            MOVE.B      D0, t_6     ; t_6 = D0
; --------- goto e_6                
            JMP         .e_6        ; goto .e_6
; --------- e_5: skip               
                                    
; --------- t_6 = -1                
.e_5:       CLR.L       D0          ; CLEAR D0
            MOVE.B      #-1, D0     ; D0 = #-1
            MOVE.B      D0, t_6     ; t_6 = D0
; --------- e_6: skip               
                                    
; --------- if t_6 == 0 goto e_8    
.e_6:       CLR.L       D0          ; CLEAR D0
            MOVE.B      t_6, D0     ; D0 = t_6
            CMP.B       #0, D0      ; UPDATE FLAGS WITH D0 - #0
            BEQ         .e_8        ; IF Z FLAG = 1 GOTO .e_8
; --------- t_7 = a_1 - b_1         
            MOVE.L      a_1, D0     ; D0 = a_1
            MOVE.L      b_1, D1     ; D1 = b_1
            SUB.L       D1, D0      ; D0 = D0 - D1
            MOVE.L      D0, t_7     ; t_7 = D0
; --------- a_1 = t_7               
            MOVE.L      t_7, D0     ; D0 = t_7
            MOVE.L      D0, a_1     ; a_1 = D0
; --------- goto e_7                
            JMP         .e_7        ; goto .e_7
; --------- e_8: skip               
                                    
; --------- t_8 = b_1 - a_1         
.e_8:       MOVE.L      b_1, D0     ; D0 = b_1
            MOVE.L      a_1, D1     ; D1 = a_1
            SUB.L       D1, D0      ; D0 = D0 - D1
            MOVE.L      D0, t_8     ; t_8 = D0
; --------- b_1 = t_8               
            MOVE.L      t_8, D0     ; D0 = t_8
            MOVE.L      D0, b_1     ; b_1 = D0
; --------- e_7: skip               
                                    
; --------- e_2: skip               
.e_7:                               
; --------- goto e                  
.e_2:       JMP         .e          ; goto .e
; --------- e_1: skip               
                                    
; --------- rtn e_mcd1, a_1         
.e_1:       MOVE.L      a_1, 4(SP)  ; PUSH INTO STACK a_1
            RTS                     ; RETURN TO SUBROUTINE e_mcd1


; --------- e_mcm: skip             
                                    
; --------- pmb e_mcm               
e_mcm:      MOVE.L      8(SP), num2 ; num2 = POP FROM STACK
            MOVE.L      12(SP), num1 ; num1 = POP FROM STACK
; --------- if num1 > num2 goto e_9  
            MOVE.L      num1, D0    ; D0 = num1
            CMP.L       num2, D0    ; UPDATE FLAGS WITH D0 - num2
            BGT         .e_9        ; IF ((N XOR V) OR Z) FLAGS = 0 GOTO .e_9
; --------- t_9 = 0                 
            CLR.L       D0          ; CLEAR D0
            MOVE.B      #0, D0      ; D0 = #0
            MOVE.B      D0, t_9     ; t_9 = D0
; --------- goto e_10               
            JMP         .e_10       ; goto .e_10
; --------- e_9: skip               
                                    
; --------- t_9 = -1                
.e_9:       CLR.L       D0          ; CLEAR D0
            MOVE.B      #-1, D0     ; D0 = #-1
            MOVE.B      D0, t_9     ; t_9 = D0
; --------- e_10: skip              
                                    
; --------- if t_9 == 0 goto e_12   
.e_10:      CLR.L       D0          ; CLEAR D0
            MOVE.B      t_9, D0     ; D0 = t_9
            CMP.B       #0, D0      ; UPDATE FLAGS WITH D0 - #0
            BEQ         .e_12       ; IF Z FLAG = 1 GOTO .e_12
; --------- a_2 = num1              
            MOVE.L      num1, D0    ; D0 = num1
            MOVE.L      D0, a_2     ; a_2 = D0
; --------- b_2 = num2              
            MOVE.L      num2, D0    ; D0 = num2
            MOVE.L      D0, b_2     ; b_2 = D0
; --------- goto e_11               
            JMP         .e_11       ; goto .e_11
; --------- e_12: skip              
                                    
; --------- a_2 = num2              
.e_12:      MOVE.L      num2, D0    ; D0 = num2
            MOVE.L      D0, a_2     ; a_2 = D0
; --------- b_2 = num1              
            MOVE.L      num1, D0    ; D0 = num1
            MOVE.L      D0, b_2     ; b_2 = D0
; --------- e_11: skip              
                                    
; --------- param_s num1            
.e_11:      MOVE.L      num1, -(SP) ; PUSH INTO STACK num1
; --------- param_s num2            
            MOVE.L      num2, -(SP) ; PUSH INTO STACK num2
; --------- call e_mcd2, t_10       
            SUBA.L      #4, SP      ; SP = SP + 4
            JSR         e_mcd2      ; JUMP TO SUBROUTINE e_mcd2
            MOVE.L      (SP)+, t_10 ; t_10 = POP FROM STACK
            ADDA.L      #8, SP      ; SP = SP + 8
; --------- t_11 = a_2 / t_10       
            MOVE.L      a_2, D0     ; D0 = a_2
            MOVE.L      t_10, D1    ; D1 = t_10
            DIVS.W      D1, D0      ; D0.h = D0 % D1. D0.l = D0 / D1
            AND.L       #$0000FFFF, D0 ; Mask
            MOVE.L      D0, t_11    ; t_11 = D0
; --------- t_12 = t_11 * b_2       
;           A*B = A1A0*B1B0 = A0*B0 + A1*B1 * 2^16  
            MOVE.L      t_11, D0    ; D0 = t_11
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
            MOVE.L      D0, t_12    ; t_12 = D0
; --------- resultado = t_12        
            MOVE.L      t_12, D0    ; D0 = t_12
            MOVE.L      D0, resultado ; resultado = D0
; --------- rtn e_mcm, resultado    
            MOVE.L      resultado, 4(SP) ; PUSH INTO STACK resultado
            RTS                     ; RETURN TO SUBROUTINE e_mcm


; --------- e_mcd2: skip            
                                    
; --------- pmb e_mcd2              
e_mcd2:     MOVE.L      8(SP), num2_1 ; num2_1 = POP FROM STACK
            MOVE.L      12(SP), num1_1 ; num1_1 = POP FROM STACK
; --------- if num1_1 > num2_1 goto e_13  
            MOVE.L      num1_1, D0  ; D0 = num1_1
            CMP.L       num2_1, D0  ; UPDATE FLAGS WITH D0 - num2_1
            BGT         .e_13       ; IF ((N XOR V) OR Z) FLAGS = 0 GOTO .e_13
; --------- t_13 = 0                
            CLR.L       D0          ; CLEAR D0
            MOVE.B      #0, D0      ; D0 = #0
            MOVE.B      D0, t_13    ; t_13 = D0
; --------- goto e_14               
            JMP         .e_14       ; goto .e_14
; --------- e_13: skip              
                                    
; --------- t_13 = -1               
.e_13:      CLR.L       D0          ; CLEAR D0
            MOVE.B      #-1, D0     ; D0 = #-1
            MOVE.B      D0, t_13    ; t_13 = D0
; --------- e_14: skip              
                                    
; --------- if t_13 == 0 goto e_16  
.e_14:      CLR.L       D0          ; CLEAR D0
            MOVE.B      t_13, D0    ; D0 = t_13
            CMP.B       #0, D0      ; UPDATE FLAGS WITH D0 - #0
            BEQ         .e_16       ; IF Z FLAG = 1 GOTO .e_16
; --------- a_3 = num1_1            
            MOVE.L      num1_1, D0  ; D0 = num1_1
            MOVE.L      D0, a_3     ; a_3 = D0
; --------- b_3 = num2_1            
            MOVE.L      num2_1, D0  ; D0 = num2_1
            MOVE.L      D0, b_3     ; b_3 = D0
; --------- goto e_15               
            JMP         .e_15       ; goto .e_15
; --------- e_16: skip              
                                    
; --------- a_3 = num2_1            
.e_16:      MOVE.L      num2_1, D0  ; D0 = num2_1
            MOVE.L      D0, a_3     ; a_3 = D0
; --------- b_3 = num1_1            
            MOVE.L      num1_1, D0  ; D0 = num1_1
            MOVE.L      D0, b_3     ; b_3 = D0
; --------- e_15: skip              
                                    
; --------- t_14 = 0                
.e_15:      MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_14    ; t_14 = D0
; --------- resultado_1 = t_14      
            MOVE.L      t_14, D0    ; D0 = t_14
            MOVE.L      D0, resultado_1 ; resultado_1 = D0
; --------- e_17: skip              
                                    
; --------- resultado_1 = b_3       
.e_17:      MOVE.L      b_3, D0     ; D0 = b_3
            MOVE.L      D0, resultado_1 ; resultado_1 = D0
; --------- t_15 = a_3 mod b_3      
            MOVE.L      a_3, D0     ; D0 = a_3
            MOVE.L      b_3, D1     ; D1 = b_3
            DIVS.W      D1, D0      ; D0.h = D0 % D1. D0.l = D0 / D1
            LSR.L       #8, D0      ; D0.l = D0.h
            LSR.L       #8, D0      ; D0.l = D0.h
            MOVE.L      D0, t_15    ; t_15 = D0
; --------- b_3 = t_15              
            MOVE.L      t_15, D0    ; D0 = t_15
            MOVE.L      D0, b_3     ; b_3 = D0
; --------- a_3 = resultado_1       
            MOVE.L      resultado_1, D0 ; D0 = resultado_1
            MOVE.L      D0, a_3     ; a_3 = D0
; --------- e_19: skip              
                                    
; --------- t_16 = 0                
.e_19:      MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_16    ; t_16 = D0
; --------- if b_3 /= t_16 goto e_20  
            MOVE.L      b_3, D0     ; D0 = b_3
            CMP.L       t_16, D0    ; UPDATE FLAGS WITH D0 - t_16
            BNE         .e_20       ; IF Z FLAG = 0 GOTO .e_20
; --------- t_17 = 0                
            CLR.L       D0          ; CLEAR D0
            MOVE.B      #0, D0      ; D0 = #0
            MOVE.B      D0, t_17    ; t_17 = D0
; --------- goto e_21               
            JMP         .e_21       ; goto .e_21
; --------- e_20: skip              
                                    
; --------- t_17 = -1               
.e_20:      CLR.L       D0          ; CLEAR D0
            MOVE.B      #-1, D0     ; D0 = #-1
            MOVE.B      D0, t_17    ; t_17 = D0
; --------- e_21: skip              
                                    
; --------- if t_17 == 0 goto e_18  
.e_21:      CLR.L       D0          ; CLEAR D0
            MOVE.B      t_17, D0    ; D0 = t_17
            CMP.B       #0, D0      ; UPDATE FLAGS WITH D0 - #0
            BEQ         .e_18       ; IF Z FLAG = 1 GOTO .e_18
; --------- goto e_17               
            JMP         .e_17       ; goto .e_17
; --------- e_18: skip              
                                    
; --------- rtn e_mcd2, resultado_1  
.e_18:      MOVE.L      resultado_1, 4(SP) ; PUSH INTO STACK resultado_1
            RTS                     ; RETURN TO SUBROUTINE e_mcd2


            END MCD                 ; Fin del programa


