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
t_5         DC.L 4                  ; INT
t_6         DC.L 0                  ; BOOLEAN
t_7         DC.B 'El primer algoritmo de mcd entre 16 y 20 resulta en 4'  ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_8         DC.L 4                  ; INT
t_9         DC.L 0                  ; BOOLEAN
t_10        DC.B 'El segundo algoritmo de mcd entre 16 y 20 resulta en 4'  ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_11        DC.L 80                 ; INT
t_12        DC.L 0                  ; BOOLEAN
t_13        DC.B 'El algoritmo de mcm entre 16 y 20 resulta en 80'  ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_14        DC.L 0                  ; BOOLEAN
t_15        DC.L 0                  ; BOOLEAN
t_18        DC.L 0                  ; BOOLEAN
t_22        DC.L 0                  ; BOOLEAN
t_23        DC.L 0                  ; INT
t_25        DC.L 0                  ; INT
t_26        DC.L 0                  ; BOOLEAN
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
t_24        DS.L 1                  ; INT
t_21        DS.L 1                  ; INT
num2        DS.L 1                  ; INT
t_20        DS.L 1                  ; INT
a           DS.L 1                  ; INT
b           DS.L 1                  ; INT
resultado   DS.L 1                  ; INT
t_19        DS.L 1                  ; INT
t_2         DS.L 1                  ; INT
t_16        DS.L 1                  ; INT
t_17        DS.L 1                  ; INT
t_4         DS.L 1                  ; INT
t_3         DS.L 1                  ; INT
c1          DS.L 1                  ; INT
a0          DS.L 1                  ; INT
c2          DS.L 1                  ; INT
c3          DS.L 1                  ; INT
resultado_1 DS.L 1                  ; INT

; --------- SUBROUTINES             
e_show      MOVEA.L     4(SP), A1   ; A1 = POP FROM STACK
            MOVE.L      #13, D0     ; Task 13 of TRAP 15: Display the NULL terminated string pointed to by (A1) with CR, LF
            TRAP        #15         ; Interruption generated
            RTS                     ; RETURN TO SUBROUTINE ...

; --------- INITIALIZATION          
mcd                                 ; Etiqueta inicial (main)

            MOVE.L      #32, D0     ; Task 32 of TRAP 15: Hardware/Simulator
            MOVE.B      #5, D1      ; Enable exception processing (for input/output)
            TRAP        #15         ; Interruption generated
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
            MOVE.L      a, D0       ; D0 = a
            MOVE.L      D0, -(SP)   ; PUSH INTO STACK D0
; --------- param_s b               
            MOVE.L      b, D0       ; D0 = b
            MOVE.L      D0, -(SP)   ; PUSH INTO STACK D0
; --------- call e_mcd1, t_2        
            SUBA.L      #4, SP      ; SP = SP + 4
            JSR         e_mcd1      ; JUMP TO SUBROUTINE e_mcd1
            MOVE.L      (SP)+, D0   ; D0 = POP FROM STACK
            MOVE.L      D0, t_2     ; t_2 = D0
            ADDA.L      #8, SP      ; SP = SP + 8
; --------- c1 = t_2                
            MOVE.L      t_2, D0     ; D0 = t_2
            MOVE.L      D0, c1      ; c1 = D0
; --------- param_s a               
            MOVE.L      a, D0       ; D0 = a
            MOVE.L      D0, -(SP)   ; PUSH INTO STACK D0
; --------- param_s b               
            MOVE.L      b, D0       ; D0 = b
            MOVE.L      D0, -(SP)   ; PUSH INTO STACK D0
; --------- call e_mcd2, t_3        
            SUBA.L      #4, SP      ; SP = SP + 4
            JSR         e_mcd2      ; JUMP TO SUBROUTINE e_mcd2
            MOVE.L      (SP)+, D0   ; D0 = POP FROM STACK
            MOVE.L      D0, t_3     ; t_3 = D0
            ADDA.L      #8, SP      ; SP = SP + 8
; --------- c2 = t_3                
            MOVE.L      t_3, D0     ; D0 = t_3
            MOVE.L      D0, c2      ; c2 = D0
; --------- param_s a               
            MOVE.L      a, D0       ; D0 = a
            MOVE.L      D0, -(SP)   ; PUSH INTO STACK D0
; --------- param_s b               
            MOVE.L      b, D0       ; D0 = b
            MOVE.L      D0, -(SP)   ; PUSH INTO STACK D0
; --------- call e_mcm, t_4         
            SUBA.L      #4, SP      ; SP = SP + 4
            JSR         e_mcm       ; JUMP TO SUBROUTINE e_mcm
            MOVE.L      (SP)+, D0   ; D0 = POP FROM STACK
            MOVE.L      D0, t_4     ; t_4 = D0
            ADDA.L      #8, SP      ; SP = SP + 8
; --------- c3 = t_4                
            MOVE.L      t_4, D0     ; D0 = t_4
            MOVE.L      D0, c3      ; c3 = D0
; --------- t_5 = 4                 
            MOVE.L      #4, D0      ; D0 = #4
            MOVE.L      D0, t_5     ; t_5 = D0
; --------- if c1 == t_5 goto e     
            MOVE.L      c1, D0      ; D0 = c1
            MOVE.L      t_5, D1     ; D1 = t_5
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e          ; IF Z FLAG = 1 GOTO .e
; --------- t_6 = 0                 
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_6     ; t_6 = D0
; --------- goto e_1                
            JMP         .e_1        ; goto .e_1
; --------- e: skip                 
                                    
; --------- t_6 = -1                
.e:         MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_6     ; t_6 = D0
; --------- e_1: skip               
                                    
; --------- if t_6 == 0 goto e_3    
.e_1:       MOVE.L      t_6, D0     ; D0 = t_6
            MOVE.L      #0, D1      ; D1 = #0
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_3        ; IF Z FLAG = 1 GOTO .e_3
; --------- t_7 = El primer algoritmo de mcd entre 16 y 20 resulta en 4  
; --------- param_s t_7             
            LEA.L       t_7, A0     ; A0 = t_7
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- goto e_2                
            JMP         .e_2        ; goto .e_2
; --------- e_3: skip               
                                    
; --------- e_2: skip               
.e_3:                               
; --------- t_8 = 4                 
.e_2:       MOVE.L      #4, D0      ; D0 = #4
            MOVE.L      D0, t_8     ; t_8 = D0
; --------- if c2 == t_8 goto e_4   
            MOVE.L      c2, D0      ; D0 = c2
            MOVE.L      t_8, D1     ; D1 = t_8
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_4        ; IF Z FLAG = 1 GOTO .e_4
; --------- t_9 = 0                 
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_9     ; t_9 = D0
; --------- goto e_5                
            JMP         .e_5        ; goto .e_5
; --------- e_4: skip               
                                    
; --------- t_9 = -1                
.e_4:       MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_9     ; t_9 = D0
; --------- e_5: skip               
                                    
; --------- if t_9 == 0 goto e_7    
.e_5:       MOVE.L      t_9, D0     ; D0 = t_9
            MOVE.L      #0, D1      ; D1 = #0
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_7        ; IF Z FLAG = 1 GOTO .e_7
; --------- t_10 = El segundo algoritmo de mcd entre 16 y 20 resulta en 4  
; --------- param_s t_10            
            LEA.L       t_10, A0    ; A0 = t_10
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- goto e_6                
            JMP         .e_6        ; goto .e_6
; --------- e_7: skip               
                                    
; --------- e_6: skip               
.e_7:                               
; --------- t_11 = 80               
.e_6:       MOVE.L      #80, D0     ; D0 = #80
            MOVE.L      D0, t_11    ; t_11 = D0
; --------- if c3 == t_11 goto e_8  
            MOVE.L      c3, D0      ; D0 = c3
            MOVE.L      t_11, D1    ; D1 = t_11
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_8        ; IF Z FLAG = 1 GOTO .e_8
; --------- t_12 = 0                
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_12    ; t_12 = D0
; --------- goto e_9                
            JMP         .e_9        ; goto .e_9
; --------- e_8: skip               
                                    
; --------- t_12 = -1               
.e_8:       MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_12    ; t_12 = D0
; --------- e_9: skip               
                                    
; --------- if t_12 == 0 goto e_11  
.e_9:       MOVE.L      t_12, D0    ; D0 = t_12
            MOVE.L      #0, D1      ; D1 = #0
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_11       ; IF Z FLAG = 1 GOTO .e_11
; --------- t_13 = El algoritmo de mcm entre 16 y 20 resulta en 80  
; --------- param_s t_13            
            LEA.L       t_13, A0    ; A0 = t_13
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- goto e_10               
            JMP         .e_10       ; goto .e_10
; --------- e_11: skip              
                                    
; --------- e_10: skip              
.e_11:                              
; --------- rtn e_inicio            
.e_10:      RTS                     ; RETURN TO SUBROUTINE e_inicio




; --------- e_mcd1: skip            
                                    
; --------- pmb e_mcd1              
e_mcd1:     MOVE.L      8(SP), D0   ; D0 = POP FROM STACK
            MOVE.L      D0, b0      ; b0 = D0
            MOVE.L      12(SP), D0  ; D0 = POP FROM STACK
            MOVE.L      D0, a0      ; a0 = D0
; --------- a_1 = a0                
            MOVE.L      a0, D0      ; D0 = a0
            MOVE.L      D0, a_1     ; a_1 = D0
; --------- b_1 = b0                
            MOVE.L      b0, D0      ; D0 = b0
            MOVE.L      D0, b_1     ; b_1 = D0
; --------- e_12: skip              
                                    
; --------- if a_1 /= b_1 goto e_15  
.e_12:      MOVE.L      a_1, D0     ; D0 = a_1
            MOVE.L      b_1, D1     ; D1 = b_1
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BNE         .e_15       ; IF Z FLAG = 0 GOTO .e_15
; --------- t_14 = 0                
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_14    ; t_14 = D0
; --------- goto e_16               
            JMP         .e_16       ; goto .e_16
; --------- e_15: skip              
                                    
; --------- t_14 = -1               
.e_15:      MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_14    ; t_14 = D0
; --------- e_16: skip              
                                    
; --------- if t_14 == 0 goto e_13  
.e_16:      MOVE.L      t_14, D0    ; D0 = t_14
            MOVE.L      #0, D1      ; D1 = #0
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_13       ; IF Z FLAG = 1 GOTO .e_13
; --------- if a_1 > b_1 goto e_17  
            MOVE.L      a_1, D0     ; D0 = a_1
            MOVE.L      b_1, D1     ; D1 = b_1
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BGT         .e_17       ; IF ((N XOR V) OR Z) FLAGS = 0 GOTO .e_17
; --------- t_15 = 0                
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_15    ; t_15 = D0
; --------- goto e_18               
            JMP         .e_18       ; goto .e_18
; --------- e_17: skip              
                                    
; --------- t_15 = -1               
.e_17:      MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_15    ; t_15 = D0
; --------- e_18: skip              
                                    
; --------- if t_15 == 0 goto e_20  
.e_18:      MOVE.L      t_15, D0    ; D0 = t_15
            MOVE.L      #0, D1      ; D1 = #0
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_20       ; IF Z FLAG = 1 GOTO .e_20
; --------- t_16 = a_1 - b_1        
            MOVE.L      a_1, D0     ; D0 = a_1
            MOVE.L      b_1, D1     ; D1 = b_1
            SUB.L       D1, D0      ; D0 = D0 - D1
            MOVE.L      D0, t_16    ; t_16 = D0
; --------- a_1 = t_16              
            MOVE.L      t_16, D0    ; D0 = t_16
            MOVE.L      D0, a_1     ; a_1 = D0
; --------- goto e_19               
            JMP         .e_19       ; goto .e_19
; --------- e_20: skip              
                                    
; --------- t_17 = b_1 - a_1        
.e_20:      MOVE.L      b_1, D0     ; D0 = b_1
            MOVE.L      a_1, D1     ; D1 = a_1
            SUB.L       D1, D0      ; D0 = D0 - D1
            MOVE.L      D0, t_17    ; t_17 = D0
; --------- b_1 = t_17              
            MOVE.L      t_17, D0    ; D0 = t_17
            MOVE.L      D0, b_1     ; b_1 = D0
; --------- e_19: skip              
                                    
; --------- e_14: skip              
.e_19:                              
; --------- goto e_12               
.e_14:      JMP         .e_12       ; goto .e_12
; --------- e_13: skip              
                                    
; --------- rtn e_mcd1, a_1         
.e_13:      MOVE.L      a_1, D0     ; D0 = a_1
            MOVE.L      D0, 4(SP)   ; PUSH INTO STACK D0
            RTS                     ; RETURN TO SUBROUTINE e_mcd1


; --------- e_mcm: skip             
                                    
; --------- pmb e_mcm               
e_mcm:      MOVE.L      8(SP), D0   ; D0 = POP FROM STACK
            MOVE.L      D0, num2    ; num2 = D0
            MOVE.L      12(SP), D0  ; D0 = POP FROM STACK
            MOVE.L      D0, num1    ; num1 = D0
; --------- if num1 > num2 goto e_21  
            MOVE.L      num1, D0    ; D0 = num1
            MOVE.L      num2, D1    ; D1 = num2
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BGT         .e_21       ; IF ((N XOR V) OR Z) FLAGS = 0 GOTO .e_21
; --------- t_18 = 0                
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_18    ; t_18 = D0
; --------- goto e_22               
            JMP         .e_22       ; goto .e_22
; --------- e_21: skip              
                                    
; --------- t_18 = -1               
.e_21:      MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_18    ; t_18 = D0
; --------- e_22: skip              
                                    
; --------- if t_18 == 0 goto e_24  
.e_22:      MOVE.L      t_18, D0    ; D0 = t_18
            MOVE.L      #0, D1      ; D1 = #0
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_24       ; IF Z FLAG = 1 GOTO .e_24
; --------- a_2 = num1              
            MOVE.L      num1, D0    ; D0 = num1
            MOVE.L      D0, a_2     ; a_2 = D0
; --------- b_2 = num2              
            MOVE.L      num2, D0    ; D0 = num2
            MOVE.L      D0, b_2     ; b_2 = D0
; --------- goto e_23               
            JMP         .e_23       ; goto .e_23
; --------- e_24: skip              
                                    
; --------- a_2 = num2              
.e_24:      MOVE.L      num2, D0    ; D0 = num2
            MOVE.L      D0, a_2     ; a_2 = D0
; --------- b_2 = num1              
            MOVE.L      num1, D0    ; D0 = num1
            MOVE.L      D0, b_2     ; b_2 = D0
; --------- e_23: skip              
                                    
; --------- param_s num1            
.e_23:      MOVE.L      num1, D0    ; D0 = num1
            MOVE.L      D0, -(SP)   ; PUSH INTO STACK D0
; --------- param_s num2            
            MOVE.L      num2, D0    ; D0 = num2
            MOVE.L      D0, -(SP)   ; PUSH INTO STACK D0
; --------- call e_mcd2, t_19       
            SUBA.L      #4, SP      ; SP = SP + 4
            JSR         e_mcd2      ; JUMP TO SUBROUTINE e_mcd2
            MOVE.L      (SP)+, D0   ; D0 = POP FROM STACK
            MOVE.L      D0, t_19    ; t_19 = D0
            ADDA.L      #8, SP      ; SP = SP + 8
; --------- t_20 = a_2 / t_19       
            MOVE.L      a_2, D0     ; D0 = a_2
            MOVE.L      t_19, D1    ; D1 = t_19
            DIVS.W      D1, D0      ; D0.h = D0 % D1. D0.l = D0 / D1
            AND.L       #$0000FFFF, D0 ; Mask
            MOVE.L      D0, t_20    ; t_20 = D0
; --------- t_21 = t_20 * b_2       
;           A*B = A1A0*B1B0 = A0*B0 + A1*B1 * 2^16  
            MOVE.L      t_20, D0    ; D0 = t_20
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
            MOVE.L      D0, t_21    ; t_21 = D0
; --------- resultado = t_21        
            MOVE.L      t_21, D0    ; D0 = t_21
            MOVE.L      D0, resultado ; resultado = D0
; --------- rtn e_mcm, resultado    
            MOVE.L      resultado, D0 ; D0 = resultado
            MOVE.L      D0, 4(SP)   ; PUSH INTO STACK D0
            RTS                     ; RETURN TO SUBROUTINE e_mcm


; --------- e_mcd2: skip            
                                    
; --------- pmb e_mcd2              
e_mcd2:     MOVE.L      8(SP), D0   ; D0 = POP FROM STACK
            MOVE.L      D0, num2_1  ; num2_1 = D0
            MOVE.L      12(SP), D0  ; D0 = POP FROM STACK
            MOVE.L      D0, num1_1  ; num1_1 = D0
; --------- if num1_1 > num2_1 goto e_25  
            MOVE.L      num1_1, D0  ; D0 = num1_1
            MOVE.L      num2_1, D1  ; D1 = num2_1
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BGT         .e_25       ; IF ((N XOR V) OR Z) FLAGS = 0 GOTO .e_25
; --------- t_22 = 0                
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_22    ; t_22 = D0
; --------- goto e_26               
            JMP         .e_26       ; goto .e_26
; --------- e_25: skip              
                                    
; --------- t_22 = -1               
.e_25:      MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_22    ; t_22 = D0
; --------- e_26: skip              
                                    
; --------- if t_22 == 0 goto e_28  
.e_26:      MOVE.L      t_22, D0    ; D0 = t_22
            MOVE.L      #0, D1      ; D1 = #0
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_28       ; IF Z FLAG = 1 GOTO .e_28
; --------- a_3 = num1_1            
            MOVE.L      num1_1, D0  ; D0 = num1_1
            MOVE.L      D0, a_3     ; a_3 = D0
; --------- b_3 = num2_1            
            MOVE.L      num2_1, D0  ; D0 = num2_1
            MOVE.L      D0, b_3     ; b_3 = D0
; --------- goto e_27               
            JMP         .e_27       ; goto .e_27
; --------- e_28: skip              
                                    
; --------- a_3 = num2_1            
.e_28:      MOVE.L      num2_1, D0  ; D0 = num2_1
            MOVE.L      D0, a_3     ; a_3 = D0
; --------- b_3 = num1_1            
            MOVE.L      num1_1, D0  ; D0 = num1_1
            MOVE.L      D0, b_3     ; b_3 = D0
; --------- e_27: skip              
                                    
; --------- t_23 = 0                
.e_27:      MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_23    ; t_23 = D0
; --------- resultado_1 = t_23      
            MOVE.L      t_23, D0    ; D0 = t_23
            MOVE.L      D0, resultado_1 ; resultado_1 = D0
; --------- e_29: skip              
                                    
; --------- resultado_1 = b_3       
.e_29:      MOVE.L      b_3, D0     ; D0 = b_3
            MOVE.L      D0, resultado_1 ; resultado_1 = D0
; --------- t_24 = a_3 mod b_3      
            MOVE.L      a_3, D0     ; D0 = a_3
            MOVE.L      b_3, D1     ; D1 = b_3
            DIVS.W      D1, D0      ; D0.h = D0 % D1. D0.l = D0 / D1
            LSR.L       #8, D0      ; D0.l = D0.h
            LSR.L       #8, D0      ; D0.l = D0.h
            MOVE.L      D0, t_24    ; t_24 = D0
; --------- b_3 = t_24              
            MOVE.L      t_24, D0    ; D0 = t_24
            MOVE.L      D0, b_3     ; b_3 = D0
; --------- a_3 = resultado_1       
            MOVE.L      resultado_1, D0 ; D0 = resultado_1
            MOVE.L      D0, a_3     ; a_3 = D0
; --------- e_31: skip              
                                    
; --------- t_25 = 0                
.e_31:      MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_25    ; t_25 = D0
; --------- if b_3 /= t_25 goto e_32  
            MOVE.L      b_3, D0     ; D0 = b_3
            MOVE.L      t_25, D1    ; D1 = t_25
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BNE         .e_32       ; IF Z FLAG = 0 GOTO .e_32
; --------- t_26 = 0                
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_26    ; t_26 = D0
; --------- goto e_33               
            JMP         .e_33       ; goto .e_33
; --------- e_32: skip              
                                    
; --------- t_26 = -1               
.e_32:      MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_26    ; t_26 = D0
; --------- e_33: skip              
                                    
; --------- if t_26 == 0 goto e_30  
.e_33:      MOVE.L      t_26, D0    ; D0 = t_26
            MOVE.L      #0, D1      ; D1 = #0
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_30       ; IF Z FLAG = 1 GOTO .e_30
; --------- goto e_29               
            JMP         .e_29       ; goto .e_29
; --------- e_30: skip              
                                    
; --------- rtn e_mcd2, resultado_1  
.e_30:      MOVE.L      resultado_1, D0 ; D0 = resultado_1
            MOVE.L      D0, 4(SP)   ; PUSH INTO STACK D0
            RTS                     ; RETURN TO SUBROUTINE e_mcd2


            END mcd                 ; Fin del programa


