; ==============================================================================
; TITLE       : matriz
; COMPILED BY : jartu
; COMPILER BY : Juan Arturo Abaurrea Calafell
;               Dani Salanova Dmitriyev
;               Marta González Juan
; ==============================================================================

            ORG         $1000       ; Origen

t           DC.L 3                  
t_1         DC.L 3                  
t_2         DC.L 3                  
t_3         DC.L 3                  
t_4         DC.L 3                  
t_5         DC.L 2                  
t_7         DC.L 0                  
t_8         DC.L 0                  
t_10        DC.L 0                  
t_11        DC.L 0                  
t_12        DC.L 4                  
t_14        DC.L 0                  
t_15        DC.L 8                  
t_22        DC.L 1                  
t_24        DC.L 1                  
t_26        DC.L 4                  
t_28        DC.L 0                  
t_30        DC.L 0                  
t_32        DC.B 'No se pueden multiplicar estas matrices.',0  
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_33        DC.L 0                  
t_34        DC.L 0                  
t_35        DC.L 0                  
t_37        DC.L 0                  
t_38        DC.L 0                  
t_39        DC.L 4                  
t_41        DC.L 0                  
t_42        DC.L 0                  
t_43        DC.L 4                  
t_45        DC.L 0                  
t_46        DC.L 8                  
t_48        DC.L 8                  
t_50        DC.L 8                  
t_63        DC.L 1                  
t_65        DC.L 1                  
t_67        DC.L 1                  
t_69        DC.L 0                  
t_70        DC.L 0                  
t_72        DC.L 0                  
t_73        DC.L 0                  
t_74        DC.L 4                  
t_76        DC.L 0                  
t_77        DC.L 8                  
t_81        DC.B ' ',0              
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_83        DC.L 1                  
t_85        DC.B '\n',0             
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_86        DC.L 1                  
t_49        DS.L 1                  
t_40        DS.L 1                  
t_47        DS.L 1                  
t_48        DS.L 1                  
t_45        DS.L 1                  
t_46        DS.L 1                  
t_43        DS.L 1                  
t_44        DS.L 1                  
t_41        DS.L 1                  
t_42        DS.L 1                  
t_2         DS.L 1                  
t_38        DS.L 1                  
t_1         DS.L 1                  
t_39        DS.L 1                  
t_4         DS.L 1                  
t_3         DS.L 1                  
t_6         DS.L 1                  
t_5         DS.L 1                  
t_8         DS.L 1                  
t_7         DS.L 1                  
t_9         DS.L 1                  
t_36        DS.L 1                  
t_37        DS.L 1                  
t_34        DS.L 1                  
t_35        DS.L 1                  
t_32        DS.L 1                  
t_33        DS.L 1                  
t_30        DS.L 1                  
t_31        DS.L 1                  
A           DS.L 1                  
B           DS.L 1                  
C           DS.L 1                  
t_61        DS.L 1                  
t_62        DS.L 1                  
t_60        DS.L 1                  
t_69        DS.L 1                  
t_67        DS.L 1                  
t_68        DS.L 1                  
t_65        DS.L 1                  
t_66        DS.L 1                  
t_63        DS.L 1                  
t_64        DS.L 1                  
c           DS.L 1                  
i           DS.L 1                  
j           DS.L 1                  
k           DS.L 1                  
m           DS.L 1                  
resultado_1 DS.L 1                  
t_50        DS.L 1                  
t_51        DS.L 1                  
t           DS.L 1                  
B_1         DS.L 1                  
t_58        DS.L 1                  
t_59        DS.L 1                  
t_56        DS.L 1                  
t_57        DS.L 1                  
t_54        DS.L 1                  
t_55        DS.L 1                  
t_52        DS.L 1                  
t_53        DS.L 1                  
argumentos  DS.L 1                  
d_datos_1   DS.L 1                  
t_83        DS.L 1                  
t_84        DS.L 1                  
t_81        DS.L 1                  
t_82        DS.L 1                  
t_80        DS.L 1                  
d_datos     DS.L 1                  
j_2         DS.L 1                  
t_87        DS.L 1                  
j_1         DS.L 1                  
modo        DS.L 1                  
t_85        DS.L 1                  
t_86        DS.L 1                  
t_72        DS.L 1                  
t_73        DS.L 1                  
t_70        DS.L 1                  
A_1         DS.L 1                  
t_71        DS.L 1                  
A_2         DS.L 1                  
t_78        DS.L 1                  
t_79        DS.L 1                  
t_76        DS.L 1                  
t_77        DS.L 1                  
t_74        DS.L 1                  
t_75        DS.L 1                  
t_29        DS.L 1                  
aux         DS.L 1                  
t_27        DS.L 1                  
t_28        DS.L 1                  
auxA        DS.L 1                  
auxC        DS.L 1                  
auxB        DS.L 1                  
t_25        DS.L 1                  
t_26        DS.L 1                  
t_23        DS.L 1                  
i_1         DS.L 1                  
t_24        DS.L 1                  
t_21        DS.L 1                  
t_22        DS.L 1                  
i_2         DS.L 1                  
m_1         DS.L 1                  
t_20        DS.L 1                  
resultado   DS.L 1                  
t_18        DS.L 1                  
t_19        DS.L 1                  
t_16        DS.L 1                  
t_17        DS.L 1                  
matriz3     DS.L 1                  
t_14        DS.L 1                  
t_15        DS.L 1                  
t_12        DS.L 1                  
t_13        DS.L 1                  
aux_1       DS.L 1                  
t_10        DS.L 1                  
t_11        DS.L 1                  
MATRIZ                              ; Etiqueta inicial (main)

            JSR         e_inicio    ; Se ejecuta el main
            SIMHALT                 ; Fin de la ejecución

; --------- d_datos = t_2           
            LEA.L       t_2, A0     ; A0 = @t_2
            MOVE.L      A0, d_datos ; d_datos = A0
; --------- d_datos_1 = t_3         
            LEA.L       t_3, A0     ; A0 = @t_3
            MOVE.L      A0, d_datos_1 ; d_datos_1 = A0
; --------- e_inicio: skip          
; --------- pmb e_inicio            
; --------- param_s A_1             
e_inicio:   MOVE.L      A_1, -(SP)  ; PUSH INTO STACK A_1
; --------- param_s t_4             
            MOVE.L      t_4, -(SP)  ; PUSH INTO STACK t_4
; --------- call e_inicializarMatriz  
            JSR         e_inicializarMatriz ; JUMP TO SUBROUTINE e_inicializarMatriz
            ADDA.L      #8, SP      ; SP = SP + 8
; --------- param_s B               
            MOVE.L      B, -(SP)    ; PUSH INTO STACK B
; --------- param_s t_5             
            MOVE.L      t_5, -(SP)  ; PUSH INTO STACK t_5
; --------- call e_inicializarMatriz  
            JSR         e_inicializarMatriz ; JUMP TO SUBROUTINE e_inicializarMatriz
            ADDA.L      #8, SP      ; SP = SP + 8
; --------- param_s A_1             
            MOVE.L      A_1, -(SP)  ; PUSH INTO STACK A_1
; --------- param_s B               
            MOVE.L      B, -(SP)    ; PUSH INTO STACK B
; --------- call e_multiplicarMatrices, t_6  
            JSR         e_multiplicarMatrices ; JUMP TO SUBROUTINE e_multiplicarMatrices
            ADDA.L      #8, SP      ; SP = SP + 8
            MOVE.L      (SP)+, t_6  ; t_6 = POP FROM STACK
; --------- resultado_1 = t_6       
            LEA.L       t_6, A0     ; A0 = @t_6
            MOVE.L      A0, resultado_1 ; resultado_1 = A0
; --------- param_s resultado_1     
            MOVE.L      resultado_1, -(SP) ; PUSH INTO STACK resultado_1
; --------- call e_mostrarMatriz    
            JSR         e_mostrarMatriz ; JUMP TO SUBROUTINE e_mostrarMatriz
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- rtn e_inicio            
            RTS                     ; RETURN TO SUBROUTINE e_inicio




; --------- e_inicializarMatriz: skip  
; --------- pmb e_inicializarMatriz  
e_inicializarMatriz: MOVE.L      4(SP), m ; m = POP FROM STACK
            MOVE.L      8(SP), modo ; modo = POP FROM STACK
; --------- c = modo                
            LEA.L       modo, A0    ; A0 = @modo
            MOVE.L      A0, c       ; c = A0
; --------- i = t_7                 
            LEA.L       t_7, A0     ; A0 = @t_7
            MOVE.L      A0, i       ; i = A0
; --------- e: skip                 
; --------- t_9 = m[t_8]            
.e:         MOVEA.L     m, A0       ; A0 = m
            ADDA.L      t_8, A0     ; A0 = A0 + t_8
            MOVE.L      (A0), t_9   ; t_9 = (A0)
; --------- if i < t_9 goto e_3     
            MOVE.L      i, D0       ; D0 = i
            CMP.L       t_9, D0     ; UPDATE FLAGS WITH D0 - t_9
            BGE         .e_3        ; IF (N XOR V) FLAGS = 0 GOTO .e_3
; --------- goto e_4                
            JMP         .e_4        ; goto .e_4
; --------- e_3: skip               
; --------- t_10 = -1               
.e_3:       MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_10    ; t_10 = D0
; --------- e_4: skip               
; --------- if t_10 == 0 goto e_1   
.e_4:       MOVE.L      t_10, D0    ; D0 = t_10
            CMP.L       #0, D0      ; UPDATE FLAGS WITH D0 - #0
            BEQ         .e_1        ; IF Z FLAG = 1 GOTO .e_1
; --------- j = t_11                
            LEA.L       t_11, A0    ; A0 = @t_11
            MOVE.L      A0, j       ; j = A0
; --------- e_5: skip               
; --------- t_13 = m[t_12]          
.e_5:       MOVEA.L     m, A0       ; A0 = m
            ADDA.L      t_12, A0    ; A0 = A0 + t_12
            MOVE.L      (A0), t_13  ; t_13 = (A0)
; --------- if j < t_13 goto e_8    
            MOVE.L      j, D0       ; D0 = j
            CMP.L       t_13, D0    ; UPDATE FLAGS WITH D0 - t_13
            BGE         .e_8        ; IF (N XOR V) FLAGS = 0 GOTO .e_8
; --------- goto e_9                
            JMP         .e_9        ; goto .e_9
; --------- e_8: skip               
; --------- t_14 = -1               
.e_8:       MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_14    ; t_14 = D0
; --------- e_9: skip               
; --------- if t_14 == 0 goto e_6   
.e_9:       MOVE.L      t_14, D0    ; D0 = t_14
            CMP.L       #0, D0      ; UPDATE FLAGS WITH D0 - #0
            BEQ         .e_6        ; IF Z FLAG = 1 GOTO .e_6
; --------- t_16 = m[t_15]          
            MOVEA.L     m, A0       ; A0 = m
            ADDA.L      t_15, A0    ; A0 = A0 + t_15
            MOVE.L      (A0), t_16  ; t_16 = (A0)
; --------- aux = t_16              
            LEA.L       t_16, A0    ; A0 = @t_16
            MOVE.L      A0, aux     ; aux = A0
; --------- t_17 = i + j            
            LEA.L       i, A0       ; A0 = @i
            LEA.L       j, A1       ; A1 = @j
            ADD.L       A0, A1      ; A1 = A1 + A0
            MOVE.L      A1, t_17    ; t_17 = A1
; --------- t_18 = t_17 mod c       
            MOVE.L      t_17, D0    ; D0 = t_17
            DIVS.L      c, D0       ; D0.h = D0 % c. D0.l = D0 / c
            LSR.L       #8, D0      ; D0.l = D0.h
            LSR.L       #8, D0      ; D0.l = D0.h
            MOVE.B      D0, t_18    ; t_18 = D0.l
; --------- t_19 = i * null         
;           A*B = A1A0*B1B0 = A0*B0 + A1*B1 * 2^16  
            MOVE.L      i, D0       ; D0 = i
            MOVE.L      null, D1    ; D1 = null
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
; --------- t_20 = j + t_19         
            LEA.L       j, A0       ; A0 = @j
            LEA.L       t_19, A1    ; A1 = @t_19
            ADD.L       A0, A1      ; A1 = A1 + A0
            MOVE.L      A1, t_20    ; t_20 = A1
; --------- t_21 = t_20 * 4         
;           A*B = A1A0*B1B0 = A0*B0 + A1*B1 * 2^16  
            MOVE.L      t_20, D0    ; D0 = t_20
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
            MOVE.L      D0, t_21    ; t_21 = D0
; --------- aux[t_21] = t_18        
            MOVEA.L     aux, A0     ; A0 = aux
            ADDA.L      t_18, A0    ; A0 = A0 + t_18
            MOVE.L      t_21, (A0)  ; (A0) = t_21
; --------- e_7: skip               
; --------- t_23 = j + t_22         
.e_7:       LEA.L       j, A0       ; A0 = @j
            LEA.L       t_22, A1    ; A1 = @t_22
            ADD.L       A0, A1      ; A1 = A1 + A0
            MOVE.L      A1, t_23    ; t_23 = A1
; --------- j = t_23                
            LEA.L       t_23, A0    ; A0 = @t_23
            MOVE.L      A0, j       ; j = A0
; --------- goto e_5                
            JMP         .e_5        ; goto .e_5
; --------- e_6: skip               
; --------- e_2: skip               
; --------- t_25 = i + t_24         
.e_2:       LEA.L       i, A0       ; A0 = @i
            LEA.L       t_24, A1    ; A1 = @t_24
            ADD.L       A0, A1      ; A1 = A1 + A0
            MOVE.L      A1, t_25    ; t_25 = A1
; --------- i = t_25                
            LEA.L       t_25, A0    ; A0 = @t_25
            MOVE.L      A0, i       ; i = A0
; --------- goto e                  
            JMP         .e          ; goto .e
; --------- e_1: skip               
; --------- rtn e_inicializarMatriz  
.e_1:       RTS                     ; RETURN TO SUBROUTINE e_inicializarMatriz


; --------- e_multiplicarMatrices: skip  
; --------- pmb e_multiplicarMatrices  
e_multiplicarMatrices: MOVE.L      8(SP), A ; A = POP FROM STACK
            MOVE.L      12(SP), B   ; B = POP FROM STACK
; --------- t_27 = A_2[t_26]        
            MOVEA.L     A_2, A0     ; A0 = A_2
            ADDA.L      t_26, A0    ; A0 = A0 + t_26
            MOVE.L      (A0), t_27  ; t_27 = (A0)
; --------- t_29 = B_1[t_28]        
            MOVEA.L     B_1, A0     ; A0 = B_1
            ADDA.L      t_28, A0    ; A0 = A0 + t_28
            MOVE.L      (A0), t_29  ; t_29 = (A0)
; --------- if t_27 == t_29 goto e_10  
            MOVE.L      t_27, D0    ; D0 = t_27
            CMP.L       t_29, D0    ; UPDATE FLAGS WITH D0 - t_29
            BEQ         .e_10       ; IF Z FLAG = 1 GOTO .e_10
; --------- goto e_11               
            JMP         .e_11       ; goto .e_11
; --------- e_10: skip              
; --------- t_30 = -1               
.e_10:      MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_30    ; t_30 = D0
; --------- e_11: skip              
; --------- t_31 = not t_30         
.e_11:      MOVE.L      t_30, D0    ; D0 = t_30
            NOT.L       D0          ; D0 = not D0
            MOVE.L      D0, t_31    ; t_31 = D0
; --------- if t_31 == 0 goto e_13  
            MOVE.L      t_31, D0    ; D0 = t_31
            CMP.L       #0, D0      ; UPDATE FLAGS WITH D0 - #0
            BEQ         .e_13       ; IF Z FLAG = 1 GOTO .e_13
; --------- param_s t_32            
            MOVE.L      t_32, -(SP) ; PUSH INTO STACK t_32
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- rtn e_multiplicarMatrices, C  
            MOVE.L      e_multiplicarMatrices, -(SP) ; PUSH INTO STACK e_multiplicarMatrices
            RTS                     ; RETURN TO SUBROUTINE e_multiplicarMatrices
; --------- goto e_12               
            JMP         .e_12       ; goto .e_12
; --------- e_13: skip              
; --------- e_12: skip              
; --------- param_s C               
.e_12:      MOVE.L      C, -(SP)    ; PUSH INTO STACK C
; --------- param_s t_33            
            MOVE.L      t_33, -(SP) ; PUSH INTO STACK t_33
; --------- call e_inicializarMatriz  
            JSR         e_inicializarMatriz ; JUMP TO SUBROUTINE e_inicializarMatriz
            ADDA.L      #8, SP      ; SP = SP + 8
; --------- i_1 = t_34              
            LEA.L       t_34, A0    ; A0 = @t_34
            MOVE.L      A0, i_1     ; i_1 = A0
; --------- e_14: skip              
; --------- t_36 = A_2[t_35]        
.e_14:      MOVEA.L     A_2, A0     ; A0 = A_2
            ADDA.L      t_35, A0    ; A0 = A0 + t_35
            MOVE.L      (A0), t_36  ; t_36 = (A0)
; --------- if i_1 < t_36 goto e_17  
            MOVE.L      i_1, D0     ; D0 = i_1
            CMP.L       t_36, D0    ; UPDATE FLAGS WITH D0 - t_36
            BGE         .e_17       ; IF (N XOR V) FLAGS = 0 GOTO .e_17
; --------- goto e_18               
            JMP         .e_18       ; goto .e_18
; --------- e_17: skip              
; --------- t_37 = -1               
.e_17:      MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_37    ; t_37 = D0
; --------- e_18: skip              
; --------- if t_37 == 0 goto e_15  
.e_18:      MOVE.L      t_37, D0    ; D0 = t_37
            CMP.L       #0, D0      ; UPDATE FLAGS WITH D0 - #0
            BEQ         .e_15       ; IF Z FLAG = 1 GOTO .e_15
; --------- j_1 = t_38              
            LEA.L       t_38, A0    ; A0 = @t_38
            MOVE.L      A0, j_1     ; j_1 = A0
; --------- e_19: skip              
; --------- t_40 = B_1[t_39]        
.e_19:      MOVEA.L     B_1, A0     ; A0 = B_1
            ADDA.L      t_39, A0    ; A0 = A0 + t_39
            MOVE.L      (A0), t_40  ; t_40 = (A0)
; --------- if j_1 < t_40 goto e_22  
            MOVE.L      j_1, D0     ; D0 = j_1
            CMP.L       t_40, D0    ; UPDATE FLAGS WITH D0 - t_40
            BGE         .e_22       ; IF (N XOR V) FLAGS = 0 GOTO .e_22
; --------- goto e_23               
            JMP         .e_23       ; goto .e_23
; --------- e_22: skip              
; --------- t_41 = -1               
.e_22:      MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_41    ; t_41 = D0
; --------- e_23: skip              
; --------- if t_41 == 0 goto e_20  
.e_23:      MOVE.L      t_41, D0    ; D0 = t_41
            CMP.L       #0, D0      ; UPDATE FLAGS WITH D0 - #0
            BEQ         .e_20       ; IF Z FLAG = 1 GOTO .e_20
; --------- k = t_42                
            LEA.L       t_42, A0    ; A0 = @t_42
            MOVE.L      A0, k       ; k = A0
; --------- e_24: skip              
; --------- t_44 = A_2[t_43]        
.e_24:      MOVEA.L     A_2, A0     ; A0 = A_2
            ADDA.L      t_43, A0    ; A0 = A0 + t_43
            MOVE.L      (A0), t_44  ; t_44 = (A0)
; --------- if k < t_44 goto e_27   
            MOVE.L      k, D0       ; D0 = k
            CMP.L       t_44, D0    ; UPDATE FLAGS WITH D0 - t_44
            BGE         .e_27       ; IF (N XOR V) FLAGS = 0 GOTO .e_27
; --------- goto e_28               
            JMP         .e_28       ; goto .e_28
; --------- e_27: skip              
; --------- t_45 = -1               
.e_27:      MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_45    ; t_45 = D0
; --------- e_28: skip              
; --------- if t_45 == 0 goto e_25  
.e_28:      MOVE.L      t_45, D0    ; D0 = t_45
            CMP.L       #0, D0      ; UPDATE FLAGS WITH D0 - #0
            BEQ         .e_25       ; IF Z FLAG = 1 GOTO .e_25
; --------- t_47 = A_2[t_46]        
            MOVEA.L     A_2, A0     ; A0 = A_2
            ADDA.L      t_46, A0    ; A0 = A0 + t_46
            MOVE.L      (A0), t_47  ; t_47 = (A0)
; --------- auxA = t_47             
            LEA.L       t_47, A0    ; A0 = @t_47
            MOVE.L      A0, auxA    ; auxA = A0
; --------- t_49 = B_1[t_48]        
            MOVEA.L     B_1, A0     ; A0 = B_1
            ADDA.L      t_48, A0    ; A0 = A0 + t_48
            MOVE.L      (A0), t_49  ; t_49 = (A0)
; --------- auxB = t_49             
            LEA.L       t_49, A0    ; A0 = @t_49
            MOVE.L      A0, auxB    ; auxB = A0
; --------- t_51 = C[t_50]          
            MOVEA.L     C, A0       ; A0 = C
            ADDA.L      t_50, A0    ; A0 = A0 + t_50
            MOVE.L      (A0), t_51  ; t_51 = (A0)
; --------- auxC = t_51             
            LEA.L       t_51, A0    ; A0 = @t_51
            MOVE.L      A0, auxC    ; auxC = A0
; --------- t_52 = auxC[i_1]        
            MOVEA.L     auxC, A0    ; A0 = auxC
            ADDA.L      i_1, A0     ; A0 = A0 + i_1
            MOVE.L      (A0), t_52  ; t_52 = (A0)
; --------- t_53 = t_52[j_1]        
            MOVEA.L     t_52, A0    ; A0 = t_52
            ADDA.L      j_1, A0     ; A0 = A0 + j_1
            MOVE.L      (A0), t_53  ; t_53 = (A0)
; --------- t_54 = auxA[i_1]        
            MOVEA.L     auxA, A0    ; A0 = auxA
            ADDA.L      i_1, A0     ; A0 = A0 + i_1
            MOVE.L      (A0), t_54  ; t_54 = (A0)
; --------- t_55 = t_54[k]          
            MOVEA.L     t_54, A0    ; A0 = t_54
            ADDA.L      k, A0       ; A0 = A0 + k
            MOVE.L      (A0), t_55  ; t_55 = (A0)
; --------- t_56 = auxB[k]          
            MOVEA.L     auxB, A0    ; A0 = auxB
            ADDA.L      k, A0       ; A0 = A0 + k
            MOVE.L      (A0), t_56  ; t_56 = (A0)
; --------- t_57 = t_56[j_1]        
            MOVEA.L     t_56, A0    ; A0 = t_56
            ADDA.L      j_1, A0     ; A0 = A0 + j_1
            MOVE.L      (A0), t_57  ; t_57 = (A0)
; --------- t_58 = t_55 * t_57      
;           A*B = A1A0*B1B0 = A0*B0 + A1*B1 * 2^16  
            MOVE.L      t_55, D0    ; D0 = t_55
            MOVE.L      t_57, D1    ; D1 = t_57
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
            MOVE.L      D0, t_58    ; t_58 = D0
; --------- t_59 = t_53 + t_58      
            LEA.L       t_53, A0    ; A0 = @t_53
            LEA.L       t_58, A1    ; A1 = @t_58
            ADD.L       A0, A1      ; A1 = A1 + A0
            MOVE.L      A1, t_59    ; t_59 = A1
; --------- t_60 = i_1 * null       
;           A*B = A1A0*B1B0 = A0*B0 + A1*B1 * 2^16  
            MOVE.L      i_1, D0     ; D0 = i_1
            MOVE.L      null, D1    ; D1 = null
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
            MOVE.L      D0, t_60    ; t_60 = D0
; --------- t_61 = j_1 + t_60       
            LEA.L       j_1, A0     ; A0 = @j_1
            LEA.L       t_60, A1    ; A1 = @t_60
            ADD.L       A0, A1      ; A1 = A1 + A0
            MOVE.L      A1, t_61    ; t_61 = A1
; --------- t_62 = t_61 * 4         
;           A*B = A1A0*B1B0 = A0*B0 + A1*B1 * 2^16  
            MOVE.L      t_61, D0    ; D0 = t_61
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
            MOVE.L      D0, t_62    ; t_62 = D0
; --------- auxC[t_62] = t_59       
            MOVEA.L     auxC, A0    ; A0 = auxC
            ADDA.L      t_59, A0    ; A0 = A0 + t_59
            MOVE.L      t_62, (A0)  ; (A0) = t_62
; --------- e_26: skip              
; --------- t_64 = k + t_63         
.e_26:      LEA.L       k, A0       ; A0 = @k
            LEA.L       t_63, A1    ; A1 = @t_63
            ADD.L       A0, A1      ; A1 = A1 + A0
            MOVE.L      A1, t_64    ; t_64 = A1
; --------- k = t_64                
            LEA.L       t_64, A0    ; A0 = @t_64
            MOVE.L      A0, k       ; k = A0
; --------- goto e_24               
            JMP         .e_24       ; goto .e_24
; --------- e_25: skip              
; --------- e_21: skip              
; --------- t_66 = j_1 + t_65       
.e_21:      LEA.L       j_1, A0     ; A0 = @j_1
            LEA.L       t_65, A1    ; A1 = @t_65
            ADD.L       A0, A1      ; A1 = A1 + A0
            MOVE.L      A1, t_66    ; t_66 = A1
; --------- j_1 = t_66              
            LEA.L       t_66, A0    ; A0 = @t_66
            MOVE.L      A0, j_1     ; j_1 = A0
; --------- goto e_19               
            JMP         .e_19       ; goto .e_19
; --------- e_20: skip              
; --------- e_16: skip              
; --------- t_68 = i_1 + t_67       
.e_16:      LEA.L       i_1, A0     ; A0 = @i_1
            LEA.L       t_67, A1    ; A1 = @t_67
            ADD.L       A0, A1      ; A1 = A1 + A0
            MOVE.L      A1, t_68    ; t_68 = A1
; --------- i_1 = t_68              
            LEA.L       t_68, A0    ; A0 = @t_68
            MOVE.L      A0, i_1     ; i_1 = A0
; --------- goto e_14               
            JMP         .e_14       ; goto .e_14
; --------- e_15: skip              
; --------- rtn e_multiplicarMatrices, C  
.e_15:      MOVE.L      e_multiplicarMatrices, -(SP) ; PUSH INTO STACK e_multiplicarMatrices
            RTS                     ; RETURN TO SUBROUTINE e_multiplicarMatrices


; --------- e_mostrarMatriz: skip   
; --------- pmb e_mostrarMatriz     
e_mostrarMatriz: MOVE.L      4(SP), m ; m = POP FROM STACK
; --------- i_2 = t_69              
            LEA.L       t_69, A0    ; A0 = @t_69
            MOVE.L      A0, i_2     ; i_2 = A0
; --------- e_29: skip              
; --------- t_71 = m_1[t_70]        
.e_29:      MOVEA.L     m_1, A0     ; A0 = m_1
            ADDA.L      t_70, A0    ; A0 = A0 + t_70
            MOVE.L      (A0), t_71  ; t_71 = (A0)
; --------- if i_2 < t_71 goto e_32  
            MOVE.L      i_2, D0     ; D0 = i_2
            CMP.L       t_71, D0    ; UPDATE FLAGS WITH D0 - t_71
            BGE         .e_32       ; IF (N XOR V) FLAGS = 0 GOTO .e_32
; --------- goto e_33               
            JMP         .e_33       ; goto .e_33
; --------- e_32: skip              
; --------- t_72 = -1               
.e_32:      MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_72    ; t_72 = D0
; --------- e_33: skip              
; --------- if t_72 == 0 goto e_30  
.e_33:      MOVE.L      t_72, D0    ; D0 = t_72
            CMP.L       #0, D0      ; UPDATE FLAGS WITH D0 - #0
            BEQ         .e_30       ; IF Z FLAG = 1 GOTO .e_30
; --------- j_2 = t_73              
            LEA.L       t_73, A0    ; A0 = @t_73
            MOVE.L      A0, j_2     ; j_2 = A0
; --------- e_34: skip              
; --------- t_75 = m_1[t_74]        
.e_34:      MOVEA.L     m_1, A0     ; A0 = m_1
            ADDA.L      t_74, A0    ; A0 = A0 + t_74
            MOVE.L      (A0), t_75  ; t_75 = (A0)
; --------- if j_2 < t_75 goto e_37  
            MOVE.L      j_2, D0     ; D0 = j_2
            CMP.L       t_75, D0    ; UPDATE FLAGS WITH D0 - t_75
            BGE         .e_37       ; IF (N XOR V) FLAGS = 0 GOTO .e_37
; --------- goto e_38               
            JMP         .e_38       ; goto .e_38
; --------- e_37: skip              
; --------- t_76 = -1               
.e_37:      MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_76    ; t_76 = D0
; --------- e_38: skip              
; --------- if t_76 == 0 goto e_35  
.e_38:      MOVE.L      t_76, D0    ; D0 = t_76
            CMP.L       #0, D0      ; UPDATE FLAGS WITH D0 - #0
            BEQ         .e_35       ; IF Z FLAG = 1 GOTO .e_35
; --------- t_78 = m_1[t_77]        
            MOVEA.L     m_1, A0     ; A0 = m_1
            ADDA.L      t_77, A0    ; A0 = A0 + t_77
            MOVE.L      (A0), t_78  ; t_78 = (A0)
; --------- aux_1 = t_78            
            LEA.L       t_78, A0    ; A0 = @t_78
            MOVE.L      A0, aux_1   ; aux_1 = A0
; --------- t_79 = aux_1[i_2]       
            MOVEA.L     aux_1, A0   ; A0 = aux_1
            ADDA.L      i_2, A0     ; A0 = A0 + i_2
            MOVE.L      (A0), t_79  ; t_79 = (A0)
; --------- t_80 = t_79[j_2]        
            MOVEA.L     t_79, A0    ; A0 = t_79
            ADDA.L      j_2, A0     ; A0 = A0 + j_2
            MOVE.L      (A0), t_80  ; t_80 = (A0)
; --------- t_82 = t_80 concat t_81  
            LEA.L       t_80, A0    ; A0 = @t_80
            LEA.L       t_81, A1    ; A1 = @t_81
            LEA.L       t_82, A2    ; FETCH t_82
            JSR         CONCATENAR  
; --------- param_s t_82            
            MOVE.L      t_82, -(SP) ; PUSH INTO STACK t_82
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- e_36: skip              
; --------- t_84 = j_2 + t_83       
.e_36:      LEA.L       j_2, A0     ; A0 = @j_2
            LEA.L       t_83, A1    ; A1 = @t_83
            ADD.L       A0, A1      ; A1 = A1 + A0
            MOVE.L      A1, t_84    ; t_84 = A1
; --------- j_2 = t_84              
            LEA.L       t_84, A0    ; A0 = @t_84
            MOVE.L      A0, j_2     ; j_2 = A0
; --------- goto e_34               
            JMP         .e_34       ; goto .e_34
; --------- e_35: skip              
; --------- param_s t_85            
.e_35:      MOVE.L      t_85, -(SP) ; PUSH INTO STACK t_85
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- e_31: skip              
; --------- t_87 = i_2 + t_86       
.e_31:      LEA.L       i_2, A0     ; A0 = @i_2
            LEA.L       t_86, A1    ; A1 = @t_86
            ADD.L       A0, A1      ; A1 = A1 + A0
            MOVE.L      A1, t_87    ; t_87 = A1
; --------- i_2 = t_87              
            LEA.L       t_87, A0    ; A0 = @t_87
            MOVE.L      A0, i_2     ; i_2 = A0
; --------- goto e_29               
            JMP         .e_29       ; goto .e_29
; --------- e_30: skip              
; --------- rtn e_mostrarMatriz     
.e_30:      RTS                     ; RETURN TO SUBROUTINE e_mostrarMatriz


CONCATENAR: MOVE.B      (A0)+, D0   
            BEQ         .CONC       
            MOVE.B      D0, (A2)+   
            BRA         CONCATENAR  
.CONC:      MOVE.B      (A1)+, D0   
            BEQ         .ENDCONC    
            MOVE.B      D0, (A2)+   
            BRA         .CONC       
.ENDCONC:   RTS                     ; RETURN TO SUBROUTINE ...
e_show      MOVEA.L     256(SP), A1 ; A1 = POP FROM STACK
            MOVE.L      #14, D0     ; Task 14 of TRAP 15: Display the NULL terminated string pointed to by (A1)
            TRAP        #15         ; Interruption generated
            RTS                     ; RETURN TO SUBROUTINE ...
            END MATRIZ              ; Fin del programa
