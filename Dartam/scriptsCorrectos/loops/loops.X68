; ==============================================================================
; TITLE       : loops
; COMPILED BY : jartu
; COMPILER BY : Juan Arturo Abaurrea Calafell
;               Dani Salanova Dmitriyev
;               Marta González Juan
; ==============================================================================

            ORG         $1000       ; Origen

t           DC.L 0                  ; INT
t_1         DC.L 10                 ; INT
t_10        DC.L 1                  ; INT
t_12        DC.L 1                  ; INT
t_14        DC.L 0                  ; BOOLEAN
t_15        DC.L 2                  ; INT
t_17        DC.L 0                  ; BOOLEAN
t_18        DC.L 1                  ; INT
t_20        DC.L 1                  ; INT
t_22        DC.L 0                  ; INT
t_23        DC.L 1                  ; INT
t_25        DC.L 1                  ; INT
t_27        DC.L 1                  ; INT
t_29        DC.L 3                  ; INT
t_31        DC.L 0                  ; BOOLEAN
t_32        DC.L 0                  ; INT
t_33        DC.L 4                  ; INT
t_35        DC.L 0                  ; BOOLEAN
t_36        DC.L 1                  ; INT
t_38        DC.L 1                  ; INT
t_40        DC.L 1                  ; INT
max_global  DS.L 1                  ; INT
t_28        DS.L 1                  ; INT
global      DS.L 1                  ; INT
t_26        DS.L 1                  ; INT
t_24        DS.L 1                  ; INT
t_21        DS.L 1                  ; INT
t_41        DS.L 1                  ; INT
global_2    DS.L 1                  ; INT
global_1    DS.L 1                  ; INT
t_19        DS.L 1                  ; INT
t_2         DS.L 1                  ; INT
t_16        DS.L 1                  ; INT
t_39        DS.L 1                  ; INT
t_4         DS.L 1                  ; INT
t_3         DS.B 256                ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_6         DS.L 1                  ; INT
t_5         DS.B 256                ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_8         DS.L 1                  ; INT
t_7         DS.B 256                ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_9         DS.B 256                ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_37        DS.L 1                  ; INT
t_34        DS.L 1                  ; INT
t_13        DS.L 1                  ; INT
t_11        DS.L 1                  ; INT
t_30        DS.L 1                  ; INT

; --------- SUBROUTINES             
e_show      MOVEA.L     4(SP), A1   ; A1 = POP FROM STACK
            MOVE.L      #13, D0     ; Task 13 of TRAP 15: Display the NULL terminated string pointed to by (A1) with CR, LF
            TRAP        #15         ; Interruption generated
            RTS                     ; RETURN TO SUBROUTINE ...

; --------- INITIALIZATION          
loops                               ; Etiqueta inicial (main)

            MOVE.L      #32, D0     ; Task 32 of TRAP 15: Hardware/Simulator
            MOVE.B      #5, D1      ; Enable exception processing (for input/output)
            TRAP        #15         ; Interruption generated
; --------- t = 0                   
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t       ; t = D0
; --------- global = t              
            MOVE.L      t, D0       ; D0 = t
            MOVE.L      D0, global  ; global = D0
; --------- t_1 = 10                
            MOVE.L      #10, D0     ; D0 = #10
            MOVE.L      D0, t_1     ; t_1 = D0
; --------- max_global = t_1        
            MOVE.L      t_1, D0     ; D0 = t_1
            MOVE.L      D0, max_global ; max_global = D0
            JSR         e_inicio    ; Se ejecuta el main
            SIMHALT                 ; Fin de la ejecución

; --------- e_inicio: skip          
                                    
; --------- pmb e_inicio            
; --------- call e_dowhile          
e_inicio:   JSR         e_dowhile   ; JUMP TO SUBROUTINE e_dowhile
; --------- t_2 = global            
            MOVE.L      global, D0  ; D0 = global
            ADD.L       #$61, D0    ; 0 char starts at $61 in ascii
            MOVE.L      D0, t_2     ; t_2 = D0
; --------- t_3 = t_2               
            MOVE.L      t_2, D0     ; D0 = t_2
            ROR.L       #8, D0      ; move to the left so it is followed by 0's, since it is a casting from char to string
            MOVE.L      D0, t_3     ; t_3 = D0
; --------- param_s t_3             
            LEA.L       t_3, A0     ; A0 = t_3
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- call e_while            
            JSR         e_while     ; JUMP TO SUBROUTINE e_while
; --------- t_4 = global            
            MOVE.L      global, D0  ; D0 = global
            ADD.L       #$61, D0    ; 0 char starts at $61 in ascii
            MOVE.L      D0, t_4     ; t_4 = D0
; --------- t_5 = t_4               
            MOVE.L      t_4, D0     ; D0 = t_4
            ROR.L       #8, D0      ; move to the left so it is followed by 0's, since it is a casting from char to string
            MOVE.L      D0, t_5     ; t_5 = D0
; --------- param_s t_5             
            LEA.L       t_5, A0     ; A0 = t_5
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- call e_dofor            
            JSR         e_dofor     ; JUMP TO SUBROUTINE e_dofor
; --------- t_6 = global            
            MOVE.L      global, D0  ; D0 = global
            ADD.L       #$61, D0    ; 0 char starts at $61 in ascii
            MOVE.L      D0, t_6     ; t_6 = D0
; --------- t_7 = t_6               
            MOVE.L      t_6, D0     ; D0 = t_6
            ROR.L       #8, D0      ; move to the left so it is followed by 0's, since it is a casting from char to string
            MOVE.L      D0, t_7     ; t_7 = D0
; --------- param_s t_7             
            LEA.L       t_7, A0     ; A0 = t_7
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- call e_for              
            JSR         e_for       ; JUMP TO SUBROUTINE e_for
; --------- t_8 = global            
            MOVE.L      global, D0  ; D0 = global
            ADD.L       #$61, D0    ; 0 char starts at $61 in ascii
            MOVE.L      D0, t_8     ; t_8 = D0
; --------- t_9 = t_8               
            MOVE.L      t_8, D0     ; D0 = t_8
            ROR.L       #8, D0      ; move to the left so it is followed by 0's, since it is a casting from char to string
            MOVE.L      D0, t_9     ; t_9 = D0
; --------- param_s t_9             
            LEA.L       t_9, A0     ; A0 = t_9
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- rtn e_inicio            
            RTS                     ; RETURN TO SUBROUTINE e_inicio




; --------- e_dowhile: skip         
                                    
; --------- pmb e_dowhile           
; --------- e: skip                 
e_dowhile:                          
; --------- t_10 = 1                
.e:         MOVE.L      #1, D0      ; D0 = #1
            MOVE.L      D0, t_10    ; t_10 = D0
; --------- t_11 = global + t_10    
            MOVE.L      global, D0  ; D0 = global
            MOVE.L      t_10, D1    ; D1 = t_10
            ADD.L       D0, D1      ; D1 = D1 + D0
            MOVE.L      D1, t_11    ; t_11 = D1
; --------- global = t_11           
            MOVE.L      t_11, D0    ; D0 = t_11
            MOVE.L      D0, global  ; global = D0
; --------- t_12 = 1                
            MOVE.L      #1, D0      ; D0 = #1
            MOVE.L      D0, t_12    ; t_12 = D0
; --------- t_13 = global + t_12    
            MOVE.L      global, D0  ; D0 = global
            MOVE.L      t_12, D1    ; D1 = t_12
            ADD.L       D0, D1      ; D1 = D1 + D0
            MOVE.L      D1, t_13    ; t_13 = D1
; --------- global = t_13           
            MOVE.L      t_13, D0    ; D0 = t_13
            MOVE.L      D0, global  ; global = D0
; --------- goto e_2                
            JMP         .e_2        ; goto .e_2
; --------- e_2: skip               
                                    
; --------- if global < max_global goto e_3  
.e_2:       MOVE.L      global, D0  ; D0 = global
            MOVE.L      max_global, D1 ; D1 = max_global
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BLT         .e_3        ; IF (N XOR V) FLAGS = 1 GOTO .e_3
; --------- t_14 = 0                
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_14    ; t_14 = D0
; --------- goto e_4                
            JMP         .e_4        ; goto .e_4
; --------- e_3: skip               
                                    
; --------- t_14 = -1               
.e_3:       MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_14    ; t_14 = D0
; --------- e_4: skip               
                                    
; --------- if t_14 == 0 goto e_1   
.e_4:       MOVE.L      t_14, D0    ; D0 = t_14
            MOVE.L      #0, D1      ; D1 = #0
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_1        ; IF Z FLAG = 1 GOTO .e_1
; --------- goto e                  
            JMP         .e          ; goto .e
; --------- e_1: skip               
                                    
; --------- rtn e_dowhile           
.e_1:       RTS                     ; RETURN TO SUBROUTINE e_dowhile


; --------- e_while: skip           
                                    
; --------- pmb e_while             
; --------- e_5: skip               
e_while:                            
; --------- t_15 = 2                
.e_5:       MOVE.L      #2, D0      ; D0 = #2
            MOVE.L      D0, t_15    ; t_15 = D0
; --------- t_16 = max_global * t_15  
;           A*B = A1A0*B1B0 = A0*B0 + A1*B1 * 2^16  
            MOVE.L      max_global, D0 ; D0 = max_global
            MOVE.L      t_15, D1    ; D1 = t_15
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
            MOVE.L      D0, t_16    ; t_16 = D0
; --------- if global < t_16 goto e_8  
            MOVE.L      global, D0  ; D0 = global
            MOVE.L      t_16, D1    ; D1 = t_16
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BLT         .e_8        ; IF (N XOR V) FLAGS = 1 GOTO .e_8
; --------- t_17 = 0                
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_17    ; t_17 = D0
; --------- goto e_9                
            JMP         .e_9        ; goto .e_9
; --------- e_8: skip               
                                    
; --------- t_17 = -1               
.e_8:       MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_17    ; t_17 = D0
; --------- e_9: skip               
                                    
; --------- if t_17 == 0 goto e_6   
.e_9:       MOVE.L      t_17, D0    ; D0 = t_17
            MOVE.L      #0, D1      ; D1 = #0
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_6        ; IF Z FLAG = 1 GOTO .e_6
; --------- t_18 = 1                
            MOVE.L      #1, D0      ; D0 = #1
            MOVE.L      D0, t_18    ; t_18 = D0
; --------- t_19 = global + t_18    
            MOVE.L      global, D0  ; D0 = global
            MOVE.L      t_18, D1    ; D1 = t_18
            ADD.L       D0, D1      ; D1 = D1 + D0
            MOVE.L      D1, t_19    ; t_19 = D1
; --------- global = t_19           
            MOVE.L      t_19, D0    ; D0 = t_19
            MOVE.L      D0, global  ; global = D0
; --------- t_20 = 1                
            MOVE.L      #1, D0      ; D0 = #1
            MOVE.L      D0, t_20    ; t_20 = D0
; --------- t_21 = global + t_20    
            MOVE.L      global, D0  ; D0 = global
            MOVE.L      t_20, D1    ; D1 = t_20
            ADD.L       D0, D1      ; D1 = D1 + D0
            MOVE.L      D1, t_21    ; t_21 = D1
; --------- global = t_21           
            MOVE.L      t_21, D0    ; D0 = t_21
            MOVE.L      D0, global  ; global = D0
; --------- goto e_7                
            JMP         .e_7        ; goto .e_7
; --------- e_7: skip               
                                    
; --------- goto e_5                
.e_7:       JMP         .e_5        ; goto .e_5
; --------- e_6: skip               
                                    
; --------- rtn e_while             
.e_6:       RTS                     ; RETURN TO SUBROUTINE e_while


; --------- e_dofor: skip           
                                    
; --------- pmb e_dofor             
; --------- t_22 = 0                
e_dofor:    MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_22    ; t_22 = D0
; --------- global_1 = t_22         
            MOVE.L      t_22, D0    ; D0 = t_22
            MOVE.L      D0, global_1 ; global_1 = D0
; --------- e_10: skip              
                                    
; --------- t_23 = 1                
.e_10:      MOVE.L      #1, D0      ; D0 = #1
            MOVE.L      D0, t_23    ; t_23 = D0
; --------- t_24 = global_1 + t_23  
            MOVE.L      global_1, D0 ; D0 = global_1
            MOVE.L      t_23, D1    ; D1 = t_23
            ADD.L       D0, D1      ; D1 = D1 + D0
            MOVE.L      D1, t_24    ; t_24 = D1
; --------- global_1 = t_24         
            MOVE.L      t_24, D0    ; D0 = t_24
            MOVE.L      D0, global_1 ; global_1 = D0
; --------- t_25 = 1                
            MOVE.L      #1, D0      ; D0 = #1
            MOVE.L      D0, t_25    ; t_25 = D0
; --------- t_26 = global_1 + t_25  
            MOVE.L      global_1, D0 ; D0 = global_1
            MOVE.L      t_25, D1    ; D1 = t_25
            ADD.L       D0, D1      ; D1 = D1 + D0
            MOVE.L      D1, t_26    ; t_26 = D1
; --------- global_1 = t_26         
            MOVE.L      t_26, D0    ; D0 = t_26
            MOVE.L      D0, global_1 ; global_1 = D0
; --------- goto e_12               
            JMP         .e_12       ; goto .e_12
; --------- e_12: skip              
                                    
; --------- t_27 = 1                
.e_12:      MOVE.L      #1, D0      ; D0 = #1
            MOVE.L      D0, t_27    ; t_27 = D0
; --------- t_28 = global_1 + t_27  
            MOVE.L      global_1, D0 ; D0 = global_1
            MOVE.L      t_27, D1    ; D1 = t_27
            ADD.L       D0, D1      ; D1 = D1 + D0
            MOVE.L      D1, t_28    ; t_28 = D1
; --------- global_1 = t_28         
            MOVE.L      t_28, D0    ; D0 = t_28
            MOVE.L      D0, global_1 ; global_1 = D0
; --------- t_29 = 3                
            MOVE.L      #3, D0      ; D0 = #3
            MOVE.L      D0, t_29    ; t_29 = D0
; --------- t_30 = max_global * t_29  
;           A*B = A1A0*B1B0 = A0*B0 + A1*B1 * 2^16  
            MOVE.L      max_global, D0 ; D0 = max_global
            MOVE.L      t_29, D1    ; D1 = t_29
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
            MOVE.L      D0, t_30    ; t_30 = D0
; --------- if global_1 < t_30 goto e_13  
            MOVE.L      global_1, D0 ; D0 = global_1
            MOVE.L      t_30, D1    ; D1 = t_30
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BLT         .e_13       ; IF (N XOR V) FLAGS = 1 GOTO .e_13
; --------- t_31 = 0                
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_31    ; t_31 = D0
; --------- goto e_14               
            JMP         .e_14       ; goto .e_14
; --------- e_13: skip              
                                    
; --------- t_31 = -1               
.e_13:      MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_31    ; t_31 = D0
; --------- e_14: skip              
                                    
; --------- if t_31 == 0 goto e_11  
.e_14:      MOVE.L      t_31, D0    ; D0 = t_31
            MOVE.L      #0, D1      ; D1 = #0
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_11       ; IF Z FLAG = 1 GOTO .e_11
; --------- goto e_10               
            JMP         .e_10       ; goto .e_10
; --------- e_11: skip              
                                    
; --------- rtn e_dofor             
.e_11:      RTS                     ; RETURN TO SUBROUTINE e_dofor


; --------- e_for: skip             
                                    
; --------- pmb e_for               
; --------- t_32 = 0                
e_for:      MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_32    ; t_32 = D0
; --------- global_2 = t_32         
            MOVE.L      t_32, D0    ; D0 = t_32
            MOVE.L      D0, global_2 ; global_2 = D0
; --------- e_15: skip              
                                    
; --------- t_33 = 4                
.e_15:      MOVE.L      #4, D0      ; D0 = #4
            MOVE.L      D0, t_33    ; t_33 = D0
; --------- t_34 = max_global * t_33  
;           A*B = A1A0*B1B0 = A0*B0 + A1*B1 * 2^16  
            MOVE.L      max_global, D0 ; D0 = max_global
            MOVE.L      t_33, D1    ; D1 = t_33
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
            MOVE.L      D0, t_34    ; t_34 = D0
; --------- if global_2 < t_34 goto e_18  
            MOVE.L      global_2, D0 ; D0 = global_2
            MOVE.L      t_34, D1    ; D1 = t_34
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BLT         .e_18       ; IF (N XOR V) FLAGS = 1 GOTO .e_18
; --------- t_35 = 0                
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_35    ; t_35 = D0
; --------- goto e_19               
            JMP         .e_19       ; goto .e_19
; --------- e_18: skip              
                                    
; --------- t_35 = -1               
.e_18:      MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_35    ; t_35 = D0
; --------- e_19: skip              
                                    
; --------- if t_35 == 0 goto e_16  
.e_19:      MOVE.L      t_35, D0    ; D0 = t_35
            MOVE.L      #0, D1      ; D1 = #0
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_16       ; IF Z FLAG = 1 GOTO .e_16
; --------- t_36 = 1                
            MOVE.L      #1, D0      ; D0 = #1
            MOVE.L      D0, t_36    ; t_36 = D0
; --------- t_37 = global_2 + t_36  
            MOVE.L      global_2, D0 ; D0 = global_2
            MOVE.L      t_36, D1    ; D1 = t_36
            ADD.L       D0, D1      ; D1 = D1 + D0
            MOVE.L      D1, t_37    ; t_37 = D1
; --------- global_2 = t_37         
            MOVE.L      t_37, D0    ; D0 = t_37
            MOVE.L      D0, global_2 ; global_2 = D0
; --------- t_38 = 1                
            MOVE.L      #1, D0      ; D0 = #1
            MOVE.L      D0, t_38    ; t_38 = D0
; --------- t_39 = global_2 + t_38  
            MOVE.L      global_2, D0 ; D0 = global_2
            MOVE.L      t_38, D1    ; D1 = t_38
            ADD.L       D0, D1      ; D1 = D1 + D0
            MOVE.L      D1, t_39    ; t_39 = D1
; --------- global_2 = t_39         
            MOVE.L      t_39, D0    ; D0 = t_39
            MOVE.L      D0, global_2 ; global_2 = D0
; --------- goto e_17               
            JMP         .e_17       ; goto .e_17
; --------- e_17: skip              
                                    
; --------- t_40 = 1                
.e_17:      MOVE.L      #1, D0      ; D0 = #1
            MOVE.L      D0, t_40    ; t_40 = D0
; --------- t_41 = global_2 + t_40  
            MOVE.L      global_2, D0 ; D0 = global_2
            MOVE.L      t_40, D1    ; D1 = t_40
            ADD.L       D0, D1      ; D1 = D1 + D0
            MOVE.L      D1, t_41    ; t_41 = D1
; --------- global_2 = t_41         
            MOVE.L      t_41, D0    ; D0 = t_41
            MOVE.L      D0, global_2 ; global_2 = D0
; --------- goto e_15               
            JMP         .e_15       ; goto .e_15
; --------- e_16: skip              
                                    
; --------- rtn e_for               
.e_16:      RTS                     ; RETURN TO SUBROUTINE e_for


            END loops               ; Fin del programa


