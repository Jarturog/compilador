; ==============================================================================
; TITLE       : cond
; COMPILED BY : jartu
; COMPILER BY : Juan Arturo Abaurrea Calafell
;               Dani Salanova Dmitriyev
;               Marta González Juan
; ==============================================================================

            ORG         $1000       ; Origen

t           DC.B 'PARTE 1'          ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_1         DC.B 'Muestra de un mal uso del switch (sin break)'  ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_2         DC.L 3                  ; INT
t_3         DC.L 3                  ; INT
t_4         DC.L 0                  ; BOOLEAN
t_5         DC.L 3                  ; INT
t_6         DC.L 4                  ; INT
t_8         DC.L 3                  ; INT
t_9         DC.L 0                  ; BOOLEAN
t_10        DC.B 'a: 3'             ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_11        DC.L 4                  ; INT
t_12        DC.L 0                  ; BOOLEAN
t_13        DC.B 'a: 4'             ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_14        DC.L 2                  ; INT
t_15        DC.L 3                  ; INT
t_16        DC.L 1                  ; INT
t_19        DC.L 8                  ; INT
t_20        DC.L 0                  ; BOOLEAN
t_21        DC.B 'b: 8'             ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_22        DC.B ''                 ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_23        DC.B 'PARTE 2'          ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_24        DC.B 'Muestra de un buen uso del switch (con break)'  ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_25        DC.L 3                  ; INT
t_26        DC.L 3                  ; INT
t_27        DC.L 0                  ; BOOLEAN
t_28        DC.L 3                  ; INT
t_29        DC.L 4                  ; INT
t_31        DC.L 3                  ; INT
t_32        DC.L 0                  ; BOOLEAN
t_33        DC.B 'a: 3'             ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_34        DC.L 4                  ; INT
t_35        DC.L 0                  ; BOOLEAN
t_36        DC.B 'a: 4'             ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_37        DC.L 2                  ; INT
t_38        DC.L 3                  ; INT
t_39        DC.L 1                  ; INT
t_42        DC.L 8                  ; INT
t_43        DC.L 0                  ; BOOLEAN
t_44        DC.B 'b: 8'             ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_45        DC.B ''                 ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_46        DC.B 'PARTE 3'          ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_47        DC.B 'Muestra de un buen uso de si, sino y no'  ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_48        DC.L 1                  ; INT
t_49        DC.L 1                  ; INT
t_53        DC.B 'num1:1    num2:1    aux1:--num1    aux2:num2--'  ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_54        DC.L 0                  ; INT
t_55        DC.L 0                  ; BOOLEAN
t_56        DC.L 0                  ; INT
t_57        DC.L 0                  ; BOOLEAN
t_59        DC.B 'num1: 0 (aux1: 0)'  ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_60        DC.L 1                  ; INT
t_61        DC.L 0                  ; BOOLEAN
t_62        DC.L 0                  ; INT
t_63        DC.L 0                  ; BOOLEAN
t_65        DC.B 'num2: 0 (aux2: 1)'  ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_66        DC.B ''                 ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_67        DC.L 2                  ; INT
t_69        DC.L 1                  ; INT
t_70        DC.L 0                  ; INT
t_71        DC.B 'El numero introducido es par'  ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_72        DC.B 'El numero introducido es impar'  ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_73        DC.B 'Error, el numero no es par ni impar'  ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_74        DC.L 2                  ; INT
t_76        DC.L 1                  ; INT
t_77        DC.L 0                  ; INT
t_78        DC.B 'El numero introducido es par'  ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_79        DC.B 'El numero introducido es impar'  ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_80        DC.B 'Error, el numero no es par ni impar'  ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_81        DC.L 2                  ; INT
t_83        DC.L 0                  ; INT
t_84        DC.L 0                  ; BOOLEAN
t_85        DC.B 'El numero introducido es par'  ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_86        DC.L 1                  ; INT
t_87        DC.L 0                  ; BOOLEAN
t_88        DC.B 'El numero introducido es impar'  ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_89        DC.B 'Error, el numero no es par ni impar'  ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_40        DS.L 1                  ; INT
t_41        DS.L 1                  ; INT
t_7         DS.L 1                  ; BOOLEAN
t_30        DS.L 1                  ; BOOLEAN
num1        DS.L 1                  ; INT
t_68        DS.L 1                  ; INT
num2        DS.L 1                  ; INT
t_64        DS.L 1                  ; BOOLEAN
a           DS.L 1                  ; BOOLEAN
b           DS.L 1                  ; INT
t_50        DS.L 1                  ; INT
t_51        DS.L 1                  ; INT
v           DS.L 1                  ; INT
t_58        DS.L 1                  ; BOOLEAN
t_52        DS.L 1                  ; INT
t_82        DS.L 1                  ; INT
b_1         DS.L 1                  ; INT
v_2         DS.L 1                  ; INT
v_1         DS.L 1                  ; INT
t_75        DS.L 1                  ; INT
aux1        DS.L 1                  ; INT
aux2        DS.L 1                  ; INT
a_1         DS.L 1                  ; BOOLEAN
t_18        DS.L 1                  ; INT
t_17        DS.L 1                  ; INT

; --------- SUBROUTINES             
e_show      MOVEA.L     4(SP), A1   ; A1 = POP FROM STACK
            MOVE.L      #13, D0     ; Task 13 of TRAP 15: Display the NULL terminated string pointed to by (A1) with CR, LF
            TRAP        #15         ; Interruption generated
            RTS                     ; RETURN TO SUBROUTINE ...

; --------- INITIALIZATION          
cond                                ; Etiqueta inicial (main)

            MOVE.L      #32, D0     ; Task 32 of TRAP 15: Hardware/Simulator
            MOVE.B      #5, D1      ; Enable exception processing (for input/output)
            TRAP        #15         ; Interruption generated
            JSR         e_inicio    ; Se ejecuta el main
            SIMHALT                 ; Fin de la ejecución

; --------- e_inicio: skip          
                                    
; --------- pmb e_inicio            
; --------- call e_parte1           
e_inicio:   JSR         e_parte1    ; JUMP TO SUBROUTINE e_parte1
; --------- call e_parte2           
            JSR         e_parte2    ; JUMP TO SUBROUTINE e_parte2
; --------- call e_parte3           
            JSR         e_parte3    ; JUMP TO SUBROUTINE e_parte3
; --------- rtn e_inicio            
            RTS                     ; RETURN TO SUBROUTINE e_inicio




; --------- e_parte1: skip          
                                    
; --------- pmb e_parte1            
; --------- t = PARTE 1             
; --------- param_s t               
e_parte1:   LEA.L       t, A0       ; A0 = t
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- t_1 = Muestra de un mal uso del switch (sin break)  
; --------- param_s t_1             
            LEA.L       t_1, A0     ; A0 = t_1
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- t_2 = 3                 
            MOVE.L      #3, D0      ; D0 = #3
            MOVE.L      D0, t_2     ; t_2 = D0
; --------- t_3 = 3                 
            MOVE.L      #3, D0      ; D0 = #3
            MOVE.L      D0, t_3     ; t_3 = D0
; --------- if t_2 == t_3 goto e    
            MOVE.L      t_2, D0     ; D0 = t_2
            MOVE.L      t_3, D1     ; D1 = t_3
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e          ; IF Z FLAG = 1 GOTO .e
; --------- t_4 = 0                 
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_4     ; t_4 = D0
; --------- goto e_1                
            JMP         .e_1        ; goto .e_1
; --------- e: skip                 
                                    
; --------- t_4 = -1                
.e:         MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_4     ; t_4 = D0
; --------- e_1: skip               
                                    
; --------- t_5 = 3                 
.e_1:       MOVE.L      #3, D0      ; D0 = #3
            MOVE.L      D0, t_5     ; t_5 = D0
; --------- t_6 = 4                 
            MOVE.L      #4, D0      ; D0 = #4
            MOVE.L      D0, t_6     ; t_6 = D0
; --------- if t_4 == 0 goto e_3    
            MOVE.L      t_4, D0     ; D0 = t_4
            MOVE.L      #0, D1      ; D1 = #0
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_3        ; IF Z FLAG = 1 GOTO .e_3
; --------- t_7 = t_5               
            MOVE.L      t_5, D0     ; D0 = t_5
            MOVE.L      D0, t_7     ; t_7 = D0
; --------- goto e_2                
            JMP         .e_2        ; goto .e_2
; --------- e_3: skip               
                                    
; --------- t_7 = t_6               
.e_3:       MOVE.L      t_6, D0     ; D0 = t_6
            MOVE.L      D0, t_7     ; t_7 = D0
; --------- e_2: skip               
                                    
; --------- a = t_7                 
.e_2:       MOVE.L      t_7, D0     ; D0 = t_7
            MOVE.L      D0, a       ; a = D0
; --------- t_8 = 3                 
            MOVE.L      #3, D0      ; D0 = #3
            MOVE.L      D0, t_8     ; t_8 = D0
; --------- if a == t_8 goto e_4    
            MOVE.L      a, D0       ; D0 = a
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
; --------- t_10 = a: 3             
; --------- param_s t_10            
            LEA.L       t_10, A0    ; A0 = t_10
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- goto e_6                
            JMP         .e_6        ; goto .e_6
; --------- e_7: skip               
                                    
; --------- t_11 = 4                
.e_7:       MOVE.L      #4, D0      ; D0 = #4
            MOVE.L      D0, t_11    ; t_11 = D0
; --------- if a == t_11 goto e_8   
            MOVE.L      a, D0       ; D0 = a
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
                                    
; --------- if t_12 == 0 goto e_10  
.e_9:       MOVE.L      t_12, D0    ; D0 = t_12
            MOVE.L      #0, D1      ; D1 = #0
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_10       ; IF Z FLAG = 1 GOTO .e_10
; --------- t_13 = a: 4             
; --------- param_s t_13            
            LEA.L       t_13, A0    ; A0 = t_13
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- goto e_6                
            JMP         .e_6        ; goto .e_6
; --------- e_10: skip              
                                    
; --------- e_6: skip               
.e_10:                              
; --------- param_s a               
.e_6:       MOVE.L      a, D0       ; D0 = a
            MOVE.L      D0, -(SP)   ; PUSH INTO STACK D0
; --------- call e_switch           
            JSR         e_switch    ; JUMP TO SUBROUTINE e_switch
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- t_14 = 2                
            MOVE.L      #2, D0      ; D0 = #2
            MOVE.L      D0, t_14    ; t_14 = D0
; --------- t_15 = 3                
            MOVE.L      #3, D0      ; D0 = #3
            MOVE.L      D0, t_15    ; t_15 = D0
; --------- if 0 >= t_15 goto e_11  
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      t_15, D1    ; D1 = t_15
            CMP.L       D0, D1      ; UPDATE FLAGS WITH D1 - D0
            BGE         .e_11       ; IF (N XOR V) FLAGS = 0 GOTO .e_11
; --------- t_16 = 1                
            MOVE.L      #1, D0      ; D0 = #1
            MOVE.L      D0, t_16    ; t_16 = D0
; --------- goto e_13               
            JMP         .e_13       ; goto .e_13
; --------- e_11: skip              
                                    
; --------- t_16 = t_14             
.e_11:      MOVE.L      t_14, D0    ; D0 = t_14
            MOVE.L      D0, t_16    ; t_16 = D0
; --------- t_17 = t_15             
            MOVE.L      t_15, D0    ; D0 = t_15
            MOVE.L      D0, t_17    ; t_17 = D0
; --------- e_12: skip              
                                    
; --------- if 1 == t_17 goto e_13  
.e_12:      MOVE.L      #1, D0      ; D0 = #1
            MOVE.L      t_17, D1    ; D1 = t_17
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_13       ; IF Z FLAG = 1 GOTO .e_13
; --------- t_18 = t_14 * t_16      
;           A*B = A1A0*B1B0 = A0*B0 + A1*B1 * 2^16  
            MOVE.L      t_14, D0    ; D0 = t_14
            MOVE.L      t_16, D1    ; D1 = t_16
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
; --------- t_16 = t_18             
            MOVE.L      t_18, D0    ; D0 = t_18
            MOVE.L      D0, t_16    ; t_16 = D0
; --------- t_18 = t_17 - 1         
            MOVE.L      t_17, D0    ; D0 = t_17
            MOVE.L      #1, D1      ; D1 = #1
            SUB.L       D1, D0      ; D0 = D0 - D1
            MOVE.L      D0, t_18    ; t_18 = D0
; --------- t_17 = t_18             
            MOVE.L      t_18, D0    ; D0 = t_18
            MOVE.L      D0, t_17    ; t_17 = D0
; --------- goto e_12               
            JMP         .e_12       ; goto .e_12
; --------- e_13: skip              
                                    
; --------- b = t_16                
.e_13:      MOVE.L      t_16, D0    ; D0 = t_16
            MOVE.L      D0, b       ; b = D0
; --------- t_19 = 8                
            MOVE.L      #8, D0      ; D0 = #8
            MOVE.L      D0, t_19    ; t_19 = D0
; --------- if b == t_19 goto e_14  
            MOVE.L      b, D0       ; D0 = b
            MOVE.L      t_19, D1    ; D1 = t_19
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_14       ; IF Z FLAG = 1 GOTO .e_14
; --------- t_20 = 0                
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_20    ; t_20 = D0
; --------- goto e_15               
            JMP         .e_15       ; goto .e_15
; --------- e_14: skip              
                                    
; --------- t_20 = -1               
.e_14:      MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_20    ; t_20 = D0
; --------- e_15: skip              
                                    
; --------- if t_20 == 0 goto e_17  
.e_15:      MOVE.L      t_20, D0    ; D0 = t_20
            MOVE.L      #0, D1      ; D1 = #0
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_17       ; IF Z FLAG = 1 GOTO .e_17
; --------- t_21 = b: 8             
; --------- param_s t_21            
            LEA.L       t_21, A0    ; A0 = t_21
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- goto e_16               
            JMP         .e_16       ; goto .e_16
; --------- e_17: skip              
                                    
; --------- e_16: skip              
.e_17:                              
; --------- param_s b               
.e_16:      MOVE.L      b, D0       ; D0 = b
            MOVE.L      D0, -(SP)   ; PUSH INTO STACK D0
; --------- call e_switch           
            JSR         e_switch    ; JUMP TO SUBROUTINE e_switch
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- t_22 =                  
; --------- param_s t_22            
            LEA.L       t_22, A0    ; A0 = t_22
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- rtn e_parte1            
            RTS                     ; RETURN TO SUBROUTINE e_parte1


; --------- e_parte2: skip          
                                    
; --------- pmb e_parte2            
; --------- t_23 = PARTE 2          
; --------- param_s t_23            
e_parte2:   LEA.L       t_23, A0    ; A0 = t_23
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- t_24 = Muestra de un buen uso del switch (con break)  
; --------- param_s t_24            
            LEA.L       t_24, A0    ; A0 = t_24
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- t_25 = 3                
            MOVE.L      #3, D0      ; D0 = #3
            MOVE.L      D0, t_25    ; t_25 = D0
; --------- t_26 = 3                
            MOVE.L      #3, D0      ; D0 = #3
            MOVE.L      D0, t_26    ; t_26 = D0
; --------- if t_25 == t_26 goto e_18  
            MOVE.L      t_25, D0    ; D0 = t_25
            MOVE.L      t_26, D1    ; D1 = t_26
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_18       ; IF Z FLAG = 1 GOTO .e_18
; --------- t_27 = 0                
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_27    ; t_27 = D0
; --------- goto e_19               
            JMP         .e_19       ; goto .e_19
; --------- e_18: skip              
                                    
; --------- t_27 = -1               
.e_18:      MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_27    ; t_27 = D0
; --------- e_19: skip              
                                    
; --------- t_28 = 3                
.e_19:      MOVE.L      #3, D0      ; D0 = #3
            MOVE.L      D0, t_28    ; t_28 = D0
; --------- t_29 = 4                
            MOVE.L      #4, D0      ; D0 = #4
            MOVE.L      D0, t_29    ; t_29 = D0
; --------- if t_27 == 0 goto e_21  
            MOVE.L      t_27, D0    ; D0 = t_27
            MOVE.L      #0, D1      ; D1 = #0
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_21       ; IF Z FLAG = 1 GOTO .e_21
; --------- t_30 = t_28             
            MOVE.L      t_28, D0    ; D0 = t_28
            MOVE.L      D0, t_30    ; t_30 = D0
; --------- goto e_20               
            JMP         .e_20       ; goto .e_20
; --------- e_21: skip              
                                    
; --------- t_30 = t_29             
.e_21:      MOVE.L      t_29, D0    ; D0 = t_29
            MOVE.L      D0, t_30    ; t_30 = D0
; --------- e_20: skip              
                                    
; --------- a_1 = t_30              
.e_20:      MOVE.L      t_30, D0    ; D0 = t_30
            MOVE.L      D0, a_1     ; a_1 = D0
; --------- t_31 = 3                
            MOVE.L      #3, D0      ; D0 = #3
            MOVE.L      D0, t_31    ; t_31 = D0
; --------- if a_1 == t_31 goto e_22  
            MOVE.L      a_1, D0     ; D0 = a_1
            MOVE.L      t_31, D1    ; D1 = t_31
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_22       ; IF Z FLAG = 1 GOTO .e_22
; --------- t_32 = 0                
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_32    ; t_32 = D0
; --------- goto e_23               
            JMP         .e_23       ; goto .e_23
; --------- e_22: skip              
                                    
; --------- t_32 = -1               
.e_22:      MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_32    ; t_32 = D0
; --------- e_23: skip              
                                    
; --------- if t_32 == 0 goto e_25  
.e_23:      MOVE.L      t_32, D0    ; D0 = t_32
            MOVE.L      #0, D1      ; D1 = #0
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_25       ; IF Z FLAG = 1 GOTO .e_25
; --------- t_33 = a: 3             
; --------- param_s t_33            
            LEA.L       t_33, A0    ; A0 = t_33
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- goto e_24               
            JMP         .e_24       ; goto .e_24
; --------- e_25: skip              
                                    
; --------- t_34 = 4                
.e_25:      MOVE.L      #4, D0      ; D0 = #4
            MOVE.L      D0, t_34    ; t_34 = D0
; --------- if a_1 == t_34 goto e_26  
            MOVE.L      a_1, D0     ; D0 = a_1
            MOVE.L      t_34, D1    ; D1 = t_34
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_26       ; IF Z FLAG = 1 GOTO .e_26
; --------- t_35 = 0                
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_35    ; t_35 = D0
; --------- goto e_27               
            JMP         .e_27       ; goto .e_27
; --------- e_26: skip              
                                    
; --------- t_35 = -1               
.e_26:      MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_35    ; t_35 = D0
; --------- e_27: skip              
                                    
; --------- if t_35 == 0 goto e_28  
.e_27:      MOVE.L      t_35, D0    ; D0 = t_35
            MOVE.L      #0, D1      ; D1 = #0
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_28       ; IF Z FLAG = 1 GOTO .e_28
; --------- t_36 = a: 4             
; --------- param_s t_36            
            LEA.L       t_36, A0    ; A0 = t_36
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- goto e_24               
            JMP         .e_24       ; goto .e_24
; --------- e_28: skip              
                                    
; --------- e_24: skip              
.e_28:                              
; --------- param_s a_1             
.e_24:      MOVE.L      a_1, D0     ; D0 = a_1
            MOVE.L      D0, -(SP)   ; PUSH INTO STACK D0
; --------- call e_switchconbreak   
            JSR         e_switchconbreak ; JUMP TO SUBROUTINE e_switchconbreak
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- t_37 = 2                
            MOVE.L      #2, D0      ; D0 = #2
            MOVE.L      D0, t_37    ; t_37 = D0
; --------- t_38 = 3                
            MOVE.L      #3, D0      ; D0 = #3
            MOVE.L      D0, t_38    ; t_38 = D0
; --------- if 0 >= t_38 goto e_29  
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      t_38, D1    ; D1 = t_38
            CMP.L       D0, D1      ; UPDATE FLAGS WITH D1 - D0
            BGE         .e_29       ; IF (N XOR V) FLAGS = 0 GOTO .e_29
; --------- t_39 = 1                
            MOVE.L      #1, D0      ; D0 = #1
            MOVE.L      D0, t_39    ; t_39 = D0
; --------- goto e_31               
            JMP         .e_31       ; goto .e_31
; --------- e_29: skip              
                                    
; --------- t_39 = t_37             
.e_29:      MOVE.L      t_37, D0    ; D0 = t_37
            MOVE.L      D0, t_39    ; t_39 = D0
; --------- t_40 = t_38             
            MOVE.L      t_38, D0    ; D0 = t_38
            MOVE.L      D0, t_40    ; t_40 = D0
; --------- e_30: skip              
                                    
; --------- if 1 == t_40 goto e_31  
.e_30:      MOVE.L      #1, D0      ; D0 = #1
            MOVE.L      t_40, D1    ; D1 = t_40
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_31       ; IF Z FLAG = 1 GOTO .e_31
; --------- t_41 = t_37 * t_39      
;           A*B = A1A0*B1B0 = A0*B0 + A1*B1 * 2^16  
            MOVE.L      t_37, D0    ; D0 = t_37
            MOVE.L      t_39, D1    ; D1 = t_39
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
            MOVE.L      D0, t_41    ; t_41 = D0
; --------- t_39 = t_41             
            MOVE.L      t_41, D0    ; D0 = t_41
            MOVE.L      D0, t_39    ; t_39 = D0
; --------- t_41 = t_40 - 1         
            MOVE.L      t_40, D0    ; D0 = t_40
            MOVE.L      #1, D1      ; D1 = #1
            SUB.L       D1, D0      ; D0 = D0 - D1
            MOVE.L      D0, t_41    ; t_41 = D0
; --------- t_40 = t_41             
            MOVE.L      t_41, D0    ; D0 = t_41
            MOVE.L      D0, t_40    ; t_40 = D0
; --------- goto e_30               
            JMP         .e_30       ; goto .e_30
; --------- e_31: skip              
                                    
; --------- b_1 = t_39              
.e_31:      MOVE.L      t_39, D0    ; D0 = t_39
            MOVE.L      D0, b_1     ; b_1 = D0
; --------- t_42 = 8                
            MOVE.L      #8, D0      ; D0 = #8
            MOVE.L      D0, t_42    ; t_42 = D0
; --------- if b_1 == t_42 goto e_32  
            MOVE.L      b_1, D0     ; D0 = b_1
            MOVE.L      t_42, D1    ; D1 = t_42
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_32       ; IF Z FLAG = 1 GOTO .e_32
; --------- t_43 = 0                
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_43    ; t_43 = D0
; --------- goto e_33               
            JMP         .e_33       ; goto .e_33
; --------- e_32: skip              
                                    
; --------- t_43 = -1               
.e_32:      MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_43    ; t_43 = D0
; --------- e_33: skip              
                                    
; --------- if t_43 == 0 goto e_35  
.e_33:      MOVE.L      t_43, D0    ; D0 = t_43
            MOVE.L      #0, D1      ; D1 = #0
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_35       ; IF Z FLAG = 1 GOTO .e_35
; --------- t_44 = b: 8             
; --------- param_s t_44            
            LEA.L       t_44, A0    ; A0 = t_44
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- goto e_34               
            JMP         .e_34       ; goto .e_34
; --------- e_35: skip              
                                    
; --------- e_34: skip              
.e_35:                              
; --------- param_s b_1             
.e_34:      MOVE.L      b_1, D0     ; D0 = b_1
            MOVE.L      D0, -(SP)   ; PUSH INTO STACK D0
; --------- call e_switchconbreak   
            JSR         e_switchconbreak ; JUMP TO SUBROUTINE e_switchconbreak
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- t_45 =                  
; --------- param_s t_45            
            LEA.L       t_45, A0    ; A0 = t_45
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- rtn e_parte2            
            RTS                     ; RETURN TO SUBROUTINE e_parte2


; --------- e_parte3: skip          
                                    
; --------- pmb e_parte3            
; --------- t_46 = PARTE 3          
; --------- param_s t_46            
e_parte3:   LEA.L       t_46, A0    ; A0 = t_46
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- t_47 = Muestra de un buen uso de si, sino y no  
; --------- param_s t_47            
            LEA.L       t_47, A0    ; A0 = t_47
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- t_48 = 1                
            MOVE.L      #1, D0      ; D0 = #1
            MOVE.L      D0, t_48    ; t_48 = D0
; --------- num1 = t_48             
            MOVE.L      t_48, D0    ; D0 = t_48
            MOVE.L      D0, num1    ; num1 = D0
; --------- t_49 = 1                
            MOVE.L      #1, D0      ; D0 = #1
            MOVE.L      D0, t_49    ; t_49 = D0
; --------- num2 = t_49             
            MOVE.L      t_49, D0    ; D0 = t_49
            MOVE.L      D0, num2    ; num2 = D0
; --------- t_50 = num1 - 1         
            MOVE.L      num1, D0    ; D0 = num1
            MOVE.L      #1, D1      ; D1 = #1
            SUB.L       D1, D0      ; D0 = D0 - D1
            MOVE.L      D0, t_50    ; t_50 = D0
; --------- num1 = t_50             
            MOVE.L      t_50, D0    ; D0 = t_50
            MOVE.L      D0, num1    ; num1 = D0
; --------- aux1 = num1             
            MOVE.L      num1, D0    ; D0 = num1
            MOVE.L      D0, aux1    ; aux1 = D0
; --------- t_51 = num2 - 1         
            MOVE.L      num2, D0    ; D0 = num2
            MOVE.L      #1, D1      ; D1 = #1
            SUB.L       D1, D0      ; D0 = D0 - D1
            MOVE.L      D0, t_51    ; t_51 = D0
; --------- t_52 = num2             
            MOVE.L      num2, D0    ; D0 = num2
            MOVE.L      D0, t_52    ; t_52 = D0
; --------- num2 = t_51             
            MOVE.L      t_51, D0    ; D0 = t_51
            MOVE.L      D0, num2    ; num2 = D0
; --------- aux2 = t_52             
            MOVE.L      t_52, D0    ; D0 = t_52
            MOVE.L      D0, aux2    ; aux2 = D0
; --------- t_53 = num1:1    num2:1    aux1:--num1    aux2:num2--  
; --------- param_s t_53            
            LEA.L       t_53, A0    ; A0 = t_53
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- t_54 = 0                
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_54    ; t_54 = D0
; --------- if aux1 == t_54 goto e_36  
            MOVE.L      aux1, D0    ; D0 = aux1
            MOVE.L      t_54, D1    ; D1 = t_54
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_36       ; IF Z FLAG = 1 GOTO .e_36
; --------- t_55 = 0                
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_55    ; t_55 = D0
; --------- goto e_37               
            JMP         .e_37       ; goto .e_37
; --------- e_36: skip              
                                    
; --------- t_55 = -1               
.e_36:      MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_55    ; t_55 = D0
; --------- e_37: skip              
                                    
; --------- t_56 = 0                
.e_37:      MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_56    ; t_56 = D0
; --------- if num1 == t_56 goto e_38  
            MOVE.L      num1, D0    ; D0 = num1
            MOVE.L      t_56, D1    ; D1 = t_56
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_38       ; IF Z FLAG = 1 GOTO .e_38
; --------- t_57 = 0                
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_57    ; t_57 = D0
; --------- goto e_39               
            JMP         .e_39       ; goto .e_39
; --------- e_38: skip              
                                    
; --------- t_57 = -1               
.e_38:      MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_57    ; t_57 = D0
; --------- e_39: skip              
                                    
; --------- t_58 = t_55 and t_57    
.e_39:      MOVE.L      t_55, D0    ; D0 = t_55
            MOVE.L      t_57, D1    ; D1 = t_57
            AND.L       D0, D1      ; D1 = D1 and D0
            MOVE.L      D1, t_58    ; t_58 = D1
; --------- if t_58 == 0 goto e_41  
            MOVE.L      t_58, D0    ; D0 = t_58
            MOVE.L      #0, D1      ; D1 = #0
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_41       ; IF Z FLAG = 1 GOTO .e_41
; --------- t_59 = num1: 0 (aux1: 0)  
; --------- param_s t_59            
            LEA.L       t_59, A0    ; A0 = t_59
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- goto e_40               
            JMP         .e_40       ; goto .e_40
; --------- e_41: skip              
                                    
; --------- e_40: skip              
.e_41:                              
; --------- t_60 = 1                
.e_40:      MOVE.L      #1, D0      ; D0 = #1
            MOVE.L      D0, t_60    ; t_60 = D0
; --------- if aux2 == t_60 goto e_42  
            MOVE.L      aux2, D0    ; D0 = aux2
            MOVE.L      t_60, D1    ; D1 = t_60
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_42       ; IF Z FLAG = 1 GOTO .e_42
; --------- t_61 = 0                
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_61    ; t_61 = D0
; --------- goto e_43               
            JMP         .e_43       ; goto .e_43
; --------- e_42: skip              
                                    
; --------- t_61 = -1               
.e_42:      MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_61    ; t_61 = D0
; --------- e_43: skip              
                                    
; --------- t_62 = 0                
.e_43:      MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_62    ; t_62 = D0
; --------- if num1 == t_62 goto e_44  
            MOVE.L      num1, D0    ; D0 = num1
            MOVE.L      t_62, D1    ; D1 = t_62
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_44       ; IF Z FLAG = 1 GOTO .e_44
; --------- t_63 = 0                
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_63    ; t_63 = D0
; --------- goto e_45               
            JMP         .e_45       ; goto .e_45
; --------- e_44: skip              
                                    
; --------- t_63 = -1               
.e_44:      MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_63    ; t_63 = D0
; --------- e_45: skip              
                                    
; --------- t_64 = t_61 and t_63    
.e_45:      MOVE.L      t_61, D0    ; D0 = t_61
            MOVE.L      t_63, D1    ; D1 = t_63
            AND.L       D0, D1      ; D1 = D1 and D0
            MOVE.L      D1, t_64    ; t_64 = D1
; --------- if t_64 == 0 goto e_47  
            MOVE.L      t_64, D0    ; D0 = t_64
            MOVE.L      #0, D1      ; D1 = #0
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_47       ; IF Z FLAG = 1 GOTO .e_47
; --------- t_65 = num2: 0 (aux2: 1)  
; --------- param_s t_65            
            LEA.L       t_65, A0    ; A0 = t_65
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- goto e_46               
            JMP         .e_46       ; goto .e_46
; --------- e_47: skip              
                                    
; --------- e_46: skip              
.e_47:                              
; --------- param_s aux1            
.e_46:      MOVE.L      aux1, D0    ; D0 = aux1
            MOVE.L      D0, -(SP)   ; PUSH INTO STACK D0
; --------- call e_ifs              
            JSR         e_ifs       ; JUMP TO SUBROUTINE e_ifs
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- param_s aux2            
            MOVE.L      aux2, D0    ; D0 = aux2
            MOVE.L      D0, -(SP)   ; PUSH INTO STACK D0
; --------- call e_ifs              
            JSR         e_ifs       ; JUMP TO SUBROUTINE e_ifs
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- t_66 =                  
; --------- param_s t_66            
            LEA.L       t_66, A0    ; A0 = t_66
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- rtn e_parte3            
            RTS                     ; RETURN TO SUBROUTINE e_parte3


; --------- e_switch: skip          
                                    
; --------- pmb e_switch            
e_switch:   MOVE.L      4(SP), D0   ; D0 = POP FROM STACK
            MOVE.L      D0, v       ; v = D0
; --------- t_67 = 2                
            MOVE.L      #2, D0      ; D0 = #2
            MOVE.L      D0, t_67    ; t_67 = D0
; --------- t_68 = v mod t_67       
            MOVE.L      v, D0       ; D0 = v
            MOVE.L      t_67, D1    ; D1 = t_67
            DIVS.W      D1, D0      ; D0.h = D0 % D1. D0.l = D0 / D1
            LSR.L       #8, D0      ; D0.l = D0.h
            LSR.L       #8, D0      ; D0.l = D0.h
            MOVE.L      D0, t_68    ; t_68 = D0
; --------- v = t_68                
            MOVE.L      t_68, D0    ; D0 = t_68
            MOVE.L      D0, v       ; v = D0
; --------- goto e_49               
            JMP         .e_49       ; goto .e_49
; --------- t_69 = 1                
            MOVE.L      #1, D0      ; D0 = #1
            MOVE.L      D0, t_69    ; t_69 = D0
; --------- t_70 = 0                
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_70    ; t_70 = D0
; --------- e_51: skip              
                                    
; --------- t_71 = El numero introducido es par  
; --------- param_s t_71            
.e_51:      LEA.L       t_71, A0    ; A0 = t_71
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- e_50: skip              
                                    
; --------- t_72 = El numero introducido es impar  
; --------- param_s t_72            
.e_50:      LEA.L       t_72, A0    ; A0 = t_72
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- t_73 = Error, el numero no es par ni impar  
; --------- param_s t_73            
            LEA.L       t_73, A0    ; A0 = t_73
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- goto e_48               
            JMP         .e_48       ; goto .e_48
; --------- e_49: skip              
                                    
; --------- if v == t_70 goto e_51  
.e_49:      MOVE.L      v, D0       ; D0 = v
            MOVE.L      t_70, D1    ; D1 = t_70
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_51       ; IF Z FLAG = 1 GOTO .e_51
; --------- if v == t_69 goto e_50  
            MOVE.L      v, D0       ; D0 = v
            MOVE.L      t_69, D1    ; D1 = t_69
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_50       ; IF Z FLAG = 1 GOTO .e_50
; --------- e_48: skip              
                                    
; --------- rtn e_switch            
.e_48:      RTS                     ; RETURN TO SUBROUTINE e_switch


; --------- e_switchconbreak: skip  
                                    
; --------- pmb e_switchconbreak    
e_switchconbreak: MOVE.L      4(SP), D0 ; D0 = POP FROM STACK
            MOVE.L      D0, v_1     ; v_1 = D0
; --------- t_74 = 2                
            MOVE.L      #2, D0      ; D0 = #2
            MOVE.L      D0, t_74    ; t_74 = D0
; --------- t_75 = v_1 mod t_74     
            MOVE.L      v_1, D0     ; D0 = v_1
            MOVE.L      t_74, D1    ; D1 = t_74
            DIVS.W      D1, D0      ; D0.h = D0 % D1. D0.l = D0 / D1
            LSR.L       #8, D0      ; D0.l = D0.h
            LSR.L       #8, D0      ; D0.l = D0.h
            MOVE.L      D0, t_75    ; t_75 = D0
; --------- v_1 = t_75              
            MOVE.L      t_75, D0    ; D0 = t_75
            MOVE.L      D0, v_1     ; v_1 = D0
; --------- goto e_53               
            JMP         .e_53       ; goto .e_53
; --------- t_76 = 1                
            MOVE.L      #1, D0      ; D0 = #1
            MOVE.L      D0, t_76    ; t_76 = D0
; --------- t_77 = 0                
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_77    ; t_77 = D0
; --------- e_55: skip              
                                    
; --------- t_78 = El numero introducido es par  
; --------- param_s t_78            
.e_55:      LEA.L       t_78, A0    ; A0 = t_78
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- goto e_52               
            JMP         .e_52       ; goto .e_52
; --------- e_54: skip              
                                    
; --------- t_79 = El numero introducido es impar  
; --------- param_s t_79            
.e_54:      LEA.L       t_79, A0    ; A0 = t_79
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- goto e_52               
            JMP         .e_52       ; goto .e_52
; --------- t_80 = Error, el numero no es par ni impar  
; --------- param_s t_80            
            LEA.L       t_80, A0    ; A0 = t_80
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- goto e_52               
            JMP         .e_52       ; goto .e_52
; --------- goto e_52               
            JMP         .e_52       ; goto .e_52
; --------- e_53: skip              
                                    
; --------- if v_1 == t_77 goto e_55  
.e_53:      MOVE.L      v_1, D0     ; D0 = v_1
            MOVE.L      t_77, D1    ; D1 = t_77
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_55       ; IF Z FLAG = 1 GOTO .e_55
; --------- if v_1 == t_76 goto e_54  
            MOVE.L      v_1, D0     ; D0 = v_1
            MOVE.L      t_76, D1    ; D1 = t_76
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_54       ; IF Z FLAG = 1 GOTO .e_54
; --------- e_52: skip              
                                    
; --------- rtn e_switchconbreak    
.e_52:      RTS                     ; RETURN TO SUBROUTINE e_switchconbreak


; --------- e_ifs: skip             
                                    
; --------- pmb e_ifs               
e_ifs:      MOVE.L      4(SP), D0   ; D0 = POP FROM STACK
            MOVE.L      D0, v_2     ; v_2 = D0
; --------- t_81 = 2                
            MOVE.L      #2, D0      ; D0 = #2
            MOVE.L      D0, t_81    ; t_81 = D0
; --------- t_82 = v_2 mod t_81     
            MOVE.L      v_2, D0     ; D0 = v_2
            MOVE.L      t_81, D1    ; D1 = t_81
            DIVS.W      D1, D0      ; D0.h = D0 % D1. D0.l = D0 / D1
            LSR.L       #8, D0      ; D0.l = D0.h
            LSR.L       #8, D0      ; D0.l = D0.h
            MOVE.L      D0, t_82    ; t_82 = D0
; --------- v_2 = t_82              
            MOVE.L      t_82, D0    ; D0 = t_82
            MOVE.L      D0, v_2     ; v_2 = D0
; --------- t_83 = 0                
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_83    ; t_83 = D0
; --------- if v_2 == t_83 goto e_56  
            MOVE.L      v_2, D0     ; D0 = v_2
            MOVE.L      t_83, D1    ; D1 = t_83
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_56       ; IF Z FLAG = 1 GOTO .e_56
; --------- t_84 = 0                
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_84    ; t_84 = D0
; --------- goto e_57               
            JMP         .e_57       ; goto .e_57
; --------- e_56: skip              
                                    
; --------- t_84 = -1               
.e_56:      MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_84    ; t_84 = D0
; --------- e_57: skip              
                                    
; --------- if t_84 == 0 goto e_59  
.e_57:      MOVE.L      t_84, D0    ; D0 = t_84
            MOVE.L      #0, D1      ; D1 = #0
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_59       ; IF Z FLAG = 1 GOTO .e_59
; --------- t_85 = El numero introducido es par  
; --------- param_s t_85            
            LEA.L       t_85, A0    ; A0 = t_85
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- goto e_58               
            JMP         .e_58       ; goto .e_58
; --------- e_59: skip              
                                    
; --------- t_86 = 1                
.e_59:      MOVE.L      #1, D0      ; D0 = #1
            MOVE.L      D0, t_86    ; t_86 = D0
; --------- if v_2 == t_86 goto e_60  
            MOVE.L      v_2, D0     ; D0 = v_2
            MOVE.L      t_86, D1    ; D1 = t_86
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_60       ; IF Z FLAG = 1 GOTO .e_60
; --------- t_87 = 0                
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_87    ; t_87 = D0
; --------- goto e_61               
            JMP         .e_61       ; goto .e_61
; --------- e_60: skip              
                                    
; --------- t_87 = -1               
.e_60:      MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_87    ; t_87 = D0
; --------- e_61: skip              
                                    
; --------- if t_87 == 0 goto e_62  
.e_61:      MOVE.L      t_87, D0    ; D0 = t_87
            MOVE.L      #0, D1      ; D1 = #0
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_62       ; IF Z FLAG = 1 GOTO .e_62
; --------- t_88 = El numero introducido es impar  
; --------- param_s t_88            
            LEA.L       t_88, A0    ; A0 = t_88
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- goto e_58               
            JMP         .e_58       ; goto .e_58
; --------- e_62: skip              
                                    
; --------- t_89 = Error, el numero no es par ni impar  
; --------- param_s t_89            
.e_62:      LEA.L       t_89, A0    ; A0 = t_89
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- e_58: skip              
                                    
; --------- rtn e_ifs               
.e_58:      RTS                     ; RETURN TO SUBROUTINE e_ifs


            END cond                ; Fin del programa


