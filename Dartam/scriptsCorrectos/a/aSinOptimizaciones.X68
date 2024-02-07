; ==============================================================================
; TITLE       : a
; COMPILED BY : jartu
; COMPILER BY : Juan Arturo Abaurrea Calafell
;               Dani Salanova Dmitriyev
;               Marta Gonzalez Juan
; ==============================================================================

            ORG         $1000       ; Origen

t           DC.L 3                  ; INT
t_1         DC.L 4                  ; INT
t_2         DC.L 5                  ; INT
t_3         DC.L 'A'                ; CHAR
t_4         DC.L 2                  ; INT
t_5         DC.L 3                  ; INT
t_6         DC.L 4                  ; INT
t_12        DC.L 0                  ; INT
t_13        DC.L 3                  ; INT
t_14        DC.L 0                  ; BOOLEAN
t_15        DC.L 0                  ; INT
t_16        DC.L 4                  ; INT
t_17        DC.L 0                  ; BOOLEAN
t_18        DC.L 0                  ; INT
t_19        DC.L 5                  ; INT
t_20        DC.L 0                  ; BOOLEAN
t_21        DC.L 'A'                ; CHAR
t_22        DC.L 4                  ; INT
t_23        DC.L 5                  ; INT
t_26        DC.L 5                  ; INT
t_30        DC.L 26                 ; INT
t_43        DC.L 1                  ; INT
t_45        DC.L 1                  ; INT
t_47        DC.L 1                  ; INT
d_matrizcars DS.L 1                 ; INT
t_40        DS.L 1                  ; POINTER TO ARRAY, TUPLE OR STRING
t_48        DS.L 1                  ; INT
t_46        DS.L 1                  ; INT
t_44        DS.L 1                  ; INT
t_41        DS.L 1                  ; CHAR
t_42        DS.B 256                ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_38        DS.L 1                  ; INT
t_39        DS.L 1                  ; POINTER TO ARRAY, TUPLE OR STRING
t_8         DS.L 1                  ; INT
t_7         DS.L 1                  ; INT
t_9         DS.L 1                  ; INT
matrizcars  DS.B 240                ; ARRAY
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_36        DS.L 1                  ; INT
t_37        DS.L 1                  ; INT
t_34        DS.L 1                  ; INT
t_35        DS.L 1                  ; INT
t_32        DS.L 1                  ; INT
t_33        DS.L 1                  ; CHAR
t_31        DS.L 1                  ; INT
t_29        DS.L 1                  ; INT
t_27        DS.L 1                  ; INT
t_28        DS.L 1                  ; INT
d_matrizcars_2 DS.L 1               ; INT
d_matrizcars_1 DS.L 1               ; INT
t_25        DS.L 1                  ; INT
t_24        DS.L 1                  ; INT
i           DS.L 1                  ; INT
j           DS.L 1                  ; INT
k           DS.L 1                  ; INT
t_10        DS.L 1                  ; INT
t_11        DS.L 1                  ; INT

; --------- SUBROUTINES             
e_show      MOVEA.L     4(SP), A1   ; A1 = POP FROM STACK
            MOVE.L      #13, D0     ; Task 13 of TRAP 15: Display the NULL terminated string pointed to by (A1) with CR, LF
            TRAP        #15         ; Interruption generated
            RTS                     ; RETURN TO SUBROUTINE ...

; --------- INITIALIZATION          
a                                   ; Etiqueta inicial (main)

            MOVE.L      #32, D0     ; Task 32 of TRAP 15: Hardware/Simulator
            MOVE.B      #5, D1      ; Enable exception processing (for input/output)
            TRAP        #15         ; Interruption generated
            JSR         e_inicio    ; Se ejecuta el main
            SIMHALT                 ; Fin de la ejecucion

; --------- e_inicio: skip          
                                    
; --------- pmb e_inicio            
; --------- t = 3                   
e_inicio:   MOVE.L      #3, D0      ; D0 = #3
            MOVE.L      D0, t       ; t = D0
; --------- d_matrizcars = t        
            MOVE.L      t, D0       ; D0 = t
            MOVE.L      D0, d_matrizcars ; d_matrizcars = D0
; --------- t_1 = 4                 
            MOVE.L      #4, D0      ; D0 = #4
            MOVE.L      D0, t_1     ; t_1 = D0
; --------- d_matrizcars_1 = t_1    
            MOVE.L      t_1, D0     ; D0 = t_1
            MOVE.L      D0, d_matrizcars_1 ; d_matrizcars_1 = D0
; --------- t_2 = 5                 
            MOVE.L      #5, D0      ; D0 = #5
            MOVE.L      D0, t_2     ; t_2 = D0
; --------- d_matrizcars_2 = t_2    
            MOVE.L      t_2, D0     ; D0 = t_2
            MOVE.L      D0, d_matrizcars_2 ; d_matrizcars_2 = D0
; --------- t_3 = A                 
            MOVE.L      #'A', D0    ; D0 = #'A'
            MOVE.L      D0, t_3     ; t_3 = D0
; --------- t_4 = 2                 
            MOVE.L      #2, D0      ; D0 = #2
            MOVE.L      D0, t_4     ; t_4 = D0
; --------- t_5 = 3                 
            MOVE.L      #3, D0      ; D0 = #3
            MOVE.L      D0, t_5     ; t_5 = D0
; --------- t_6 = 4                 
            MOVE.L      #4, D0      ; D0 = #4
            MOVE.L      D0, t_6     ; t_6 = D0
; --------- t_7 = t_4 * d_matrizcars  
;           A*B = A1A0*B1B0 = A0*B0 + A1*B1 * 2^16  
            MOVE.L      t_4, D0     ; D0 = t_4
            MOVE.L      d_matrizcars, D1 ; D1 = d_matrizcars
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
; --------- t_8 = t_5 + t_7         
            MOVE.L      t_5, D0     ; D0 = t_5
            MOVE.L      t_7, D1     ; D1 = t_7
            ADD.L       D0, D1      ; D1 = D1 + D0
            MOVE.L      D1, t_8     ; t_8 = D1
; --------- t_9 = t_8 * d_matrizcars_1  
;           A*B = A1A0*B1B0 = A0*B0 + A1*B1 * 2^16  
            MOVE.L      t_8, D0     ; D0 = t_8
            MOVE.L      d_matrizcars_1, D1 ; D1 = d_matrizcars_1
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
; --------- t_10 = t_6 + t_9        
            MOVE.L      t_6, D0     ; D0 = t_6
            MOVE.L      t_9, D1     ; D1 = t_9
            ADD.L       D0, D1      ; D1 = D1 + D0
            MOVE.L      D1, t_10    ; t_10 = D1
; --------- t_11 = t_10 * 4         
;           A*B = A1A0*B1B0 = A0*B0 + A1*B1 * 2^16  
            MOVE.L      t_10, D0    ; D0 = t_10
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
            MOVE.L      D0, t_11    ; t_11 = D0
; --------- matrizcars[t_11] = t_3  
            MOVE.L      t_3, D0     ; D0 = t_3
            MOVE.L      t_11, D1    ; D1 = t_11
            LEA.L       matrizcars, A0 ; A0 = matrizcars
            ADDA.L      D1, A0      ; A0 = A0 + D1
            MOVE.L      D0, (A0)    ; (A0) = D0
; --------- t_12 = 0                
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_12    ; t_12 = D0
; --------- i = t_12                
            MOVE.L      t_12, D0    ; D0 = t_12
            MOVE.L      D0, i       ; i = D0
; --------- e: skip                 
                                    
; --------- t_13 = 3                
.e:         MOVE.L      #3, D0      ; D0 = #3
            MOVE.L      D0, t_13    ; t_13 = D0
; --------- if i < t_13 goto e_3    
            MOVE.L      i, D0       ; D0 = i
            MOVE.L      t_13, D1    ; D1 = t_13
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D1 - D0
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
; --------- t_15 = 0                
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_15    ; t_15 = D0
; --------- j = t_15                
            MOVE.L      t_15, D0    ; D0 = t_15
            MOVE.L      D0, j       ; j = D0
; --------- e_5: skip               
                                    
; --------- t_16 = 4                
.e_5:       MOVE.L      #4, D0      ; D0 = #4
            MOVE.L      D0, t_16    ; t_16 = D0
; --------- if j < t_16 goto e_8    
            MOVE.L      j, D0       ; D0 = j
            MOVE.L      t_16, D1    ; D1 = t_16
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D1 - D0
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
; --------- t_18 = 0                
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_18    ; t_18 = D0
; --------- k = t_18                
            MOVE.L      t_18, D0    ; D0 = t_18
            MOVE.L      D0, k       ; k = D0
; --------- e_10: skip              
                                    
; --------- t_19 = 5                
.e_10:      MOVE.L      #5, D0      ; D0 = #5
            MOVE.L      D0, t_19    ; t_19 = D0
; --------- if k < t_19 goto e_13   
            MOVE.L      k, D0       ; D0 = k
            MOVE.L      t_19, D1    ; D1 = t_19
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D1 - D0
            BLT         .e_13       ; IF (N XOR V) FLAGS = 1 GOTO .e_13
; --------- t_20 = 0                
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_20    ; t_20 = D0
; --------- goto e_14               
            JMP         .e_14       ; goto .e_14
; --------- e_13: skip              
                                    
; --------- t_20 = -1               
.e_13:      MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_20    ; t_20 = D0
; --------- e_14: skip              
                                    
; --------- if t_20 == 0 goto e_11  
.e_14:      MOVE.L      t_20, D0    ; D0 = t_20
            MOVE.L      #0, D1      ; D1 = #0
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_11       ; IF Z FLAG = 1 GOTO .e_11
; --------- t_21 = A                
            MOVE.L      #'A', D0    ; D0 = #'A'
            MOVE.L      D0, t_21    ; t_21 = D0
; --------- t_22 = 4                
            MOVE.L      #4, D0      ; D0 = #4
            MOVE.L      D0, t_22    ; t_22 = D0
; --------- t_23 = 5                
            MOVE.L      #5, D0      ; D0 = #5
            MOVE.L      D0, t_23    ; t_23 = D0
; --------- t_24 = t_22 * t_23      
;           A*B = A1A0*B1B0 = A0*B0 + A1*B1 * 2^16  
            MOVE.L      t_22, D0    ; D0 = t_22
            MOVE.L      t_23, D1    ; D1 = t_23
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
            MOVE.L      D0, t_24    ; t_24 = D0
; --------- t_25 = i * t_24         
;           A*B = A1A0*B1B0 = A0*B0 + A1*B1 * 2^16  
            MOVE.L      i, D0       ; D0 = i
            MOVE.L      t_24, D1    ; D1 = t_24
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
            MOVE.L      D0, t_25    ; t_25 = D0
; --------- t_26 = 5                
            MOVE.L      #5, D0      ; D0 = #5
            MOVE.L      D0, t_26    ; t_26 = D0
; --------- t_27 = j * t_26         
;           A*B = A1A0*B1B0 = A0*B0 + A1*B1 * 2^16  
            MOVE.L      j, D0       ; D0 = j
            MOVE.L      t_26, D1    ; D1 = t_26
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
            MOVE.L      D0, t_27    ; t_27 = D0
; --------- t_28 = t_27 + k         
            MOVE.L      t_27, D0    ; D0 = t_27
            MOVE.L      k, D1       ; D1 = k
            ADD.L       D0, D1      ; D1 = D1 + D0
            MOVE.L      D1, t_28    ; t_28 = D1
; --------- t_29 = t_25 + t_28      
            MOVE.L      t_25, D0    ; D0 = t_25
            MOVE.L      t_28, D1    ; D1 = t_28
            ADD.L       D0, D1      ; D1 = D1 + D0
            MOVE.L      D1, t_29    ; t_29 = D1
; --------- t_30 = 26               
            MOVE.L      #26, D0     ; D0 = #26
            MOVE.L      D0, t_30    ; t_30 = D0
; --------- t_31 = t_29 mod t_30    
            MOVE.L      t_29, D0    ; D0 = t_29
            MOVE.L      t_30, D1    ; D1 = t_30
            DIVS.W      D1, D0      ; D0.h = D0 % D1. D0.l = D0 / D1
            LSR.L       #8, D0      ; D0.l = D0.h
            LSR.L       #8, D0      ; D0.l = D0.h
            MOVE.L      D0, t_31    ; t_31 = D0
; --------- t_32 = t_31             
            MOVE.L      t_31, D0    ; D0 = t_31
            MOVE.L      D0, t_32    ; t_32 = D0
; --------- t_33 = t_21 + t_32      
            MOVE.L      t_21, D0    ; D0 = t_21
            MOVE.L      t_32, D1    ; D1 = t_32
            ADD.L       D0, D1      ; D1 = D1 + D0
            MOVE.L      D1, t_33    ; t_33 = D1
; --------- t_34 = i * d_matrizcars  
;           A*B = A1A0*B1B0 = A0*B0 + A1*B1 * 2^16  
            MOVE.L      i, D0       ; D0 = i
            MOVE.L      d_matrizcars, D1 ; D1 = d_matrizcars
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
; --------- t_35 = j + t_34         
            MOVE.L      j, D0       ; D0 = j
            MOVE.L      t_34, D1    ; D1 = t_34
            ADD.L       D0, D1      ; D1 = D1 + D0
            MOVE.L      D1, t_35    ; t_35 = D1
; --------- t_36 = t_35 * d_matrizcars_1  
;           A*B = A1A0*B1B0 = A0*B0 + A1*B1 * 2^16  
            MOVE.L      t_35, D0    ; D0 = t_35
            MOVE.L      d_matrizcars_1, D1 ; D1 = d_matrizcars_1
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
            MOVE.L      D0, t_36    ; t_36 = D0
; --------- t_37 = k + t_36         
            MOVE.L      k, D0       ; D0 = k
            MOVE.L      t_36, D1    ; D1 = t_36
            ADD.L       D0, D1      ; D1 = D1 + D0
            MOVE.L      D1, t_37    ; t_37 = D1
; --------- t_38 = t_37 * 4         
;           A*B = A1A0*B1B0 = A0*B0 + A1*B1 * 2^16  
            MOVE.L      t_37, D0    ; D0 = t_37
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
            MOVE.L      D0, t_38    ; t_38 = D0
; --------- matrizcars[t_38] = t_33  
            MOVE.L      t_33, D0    ; D0 = t_33
            MOVE.L      t_38, D1    ; D1 = t_38
            LEA.L       matrizcars, A0 ; A0 = matrizcars
            ADDA.L      D1, A0      ; A0 = A0 + D1
            MOVE.L      D0, (A0)    ; (A0) = D0
; --------- t_39 = matrizcars[i]    
            LEA.L       matrizcars, A0 ; A0 = matrizcars
            MOVE.L      i, D0       ; D0 = i
            ADDA.L      D0, A0      ; A0 = A0 + D0
            MOVE.L      (A0), t_39  ; t_39 = (A0)
; --------- t_40 = t_39[j]          
            MOVEA.L     t_39, A0    ; A0 = t_39
            MOVE.L      j, D0       ; D0 = j
            ADDA.L      D0, A0      ; A0 = A0 + D0
            MOVE.L      (A0), t_40  ; t_40 = (A0)
; --------- t_41 = t_40[k]          
            MOVEA.L     t_40, A0    ; A0 = t_40
            MOVE.L      k, D0       ; D0 = k
            ADDA.L      D0, A0      ; A0 = A0 + D0
            MOVE.L      (A0), t_41  ; t_41 = (A0)
; --------- t_42 = t_41             
            MOVE.L      t_41, D0    ; D0 = t_41
            ROR.L       #8, D0      ; move to the left so it is followed by 0's, since it is a casting from char to string
            MOVE.L      D0, t_42    ; t_42 = D0
; --------- param_s t_42            
            LEA.L       t_42, A0    ; A0 = t_42
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- e_12: skip              
                                    
; --------- t_43 = 1                
.e_12:      MOVE.L      #1, D0      ; D0 = #1
            MOVE.L      D0, t_43    ; t_43 = D0
; --------- t_44 = k + t_43         
            MOVE.L      k, D0       ; D0 = k
            MOVE.L      t_43, D1    ; D1 = t_43
            ADD.L       D0, D1      ; D1 = D1 + D0
            MOVE.L      D1, t_44    ; t_44 = D1
; --------- k = t_44                
            MOVE.L      t_44, D0    ; D0 = t_44
            MOVE.L      D0, k       ; k = D0
; --------- goto e_10               
            JMP         .e_10       ; goto .e_10
; --------- e_11: skip              
                                    
; --------- e_7: skip               
.e_11:                              
; --------- t_45 = 1                
.e_7:       MOVE.L      #1, D0      ; D0 = #1
            MOVE.L      D0, t_45    ; t_45 = D0
; --------- t_46 = j + t_45         
            MOVE.L      j, D0       ; D0 = j
            MOVE.L      t_45, D1    ; D1 = t_45
            ADD.L       D0, D1      ; D1 = D1 + D0
            MOVE.L      D1, t_46    ; t_46 = D1
; --------- j = t_46                
            MOVE.L      t_46, D0    ; D0 = t_46
            MOVE.L      D0, j       ; j = D0
; --------- goto e_5                
            JMP         .e_5        ; goto .e_5
; --------- e_6: skip               
                                    
; --------- e_2: skip               
.e_6:                               
; --------- t_47 = 1                
.e_2:       MOVE.L      #1, D0      ; D0 = #1
            MOVE.L      D0, t_47    ; t_47 = D0
; --------- t_48 = i + t_47         
            MOVE.L      i, D0       ; D0 = i
            MOVE.L      t_47, D1    ; D1 = t_47
            ADD.L       D0, D1      ; D1 = D1 + D0
            MOVE.L      D1, t_48    ; t_48 = D1
; --------- i = t_48                
            MOVE.L      t_48, D0    ; D0 = t_48
            MOVE.L      D0, i       ; i = D0
; --------- goto e                  
            JMP         .e          ; goto .e
; --------- e_1: skip               
                                    
; --------- rtn e_inicio            
.e_1:       RTS                     ; RETURN TO SUBROUTINE e_inicio




            END a                   ; Fin del programa


