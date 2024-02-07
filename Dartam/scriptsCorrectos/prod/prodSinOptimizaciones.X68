; ==============================================================================
; TITLE       : prod
; COMPILED BY : jartu
; COMPILER BY : Juan Arturo Abaurrea Calafell
;               Dani Salanova Dmitriyev
;               Marta González Juan
; ==============================================================================

            ORG         $1000       ; Origen

t           DC.L 2                  ; INT
t_1         DC.L 3                  ; INT
t_3         DC.L 6                  ; INT
t_4         DC.L 0                  ; BOOLEAN
t_5         DC.B '2 * 3 = 6'        ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_6         DC.B '6 está almacenado en 'a''  ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_7         DC.B 'ERROR: 2*3 no ha dado 6'  ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_8         DC.L 0                  ; INT
t_9         DC.L 0                  ; INT
t_10        DC.L 0                  ; BOOLEAN
t_11        DC.L 2                  ; INT
t_13        DC.L 1                  ; INT
t_14        DC.L 0                  ; BOOLEAN
t_16        DC.L 2                  ; INT
t_18        DC.L 2                  ; INT
a           DS.L 1                  ; INT
t_19        DS.L 1                  ; INT
t_2         DS.L 1                  ; INT
t_17        DS.L 1                  ; INT
x           DS.L 1                  ; INT
y           DS.L 1                  ; INT
t_15        DS.L 1                  ; INT
z           DS.L 1                  ; INT
t_12        DS.L 1                  ; INT

; --------- SUBROUTINES             
e_show      MOVEA.L     4(SP), A1   ; A1 = POP FROM STACK
            MOVE.L      #13, D0     ; Task 13 of TRAP 15: Display the NULL terminated string pointed to by (A1) with CR, LF
            TRAP        #15         ; Interruption generated
            RTS                     ; RETURN TO SUBROUTINE ...

; --------- INITIALIZATION          
prod                                ; Etiqueta inicial (main)

            MOVE.L      #32, D0     ; Task 32 of TRAP 15: Hardware/Simulator
            MOVE.B      #5, D1      ; Enable exception processing (for input/output)
            TRAP        #15         ; Interruption generated
            JSR         e_inicio    ; Se ejecuta el main
            SIMHALT                 ; Fin de la ejecución

; --------- e_inicio: skip          
                                    
; --------- pmb e_inicio            
; --------- t = 2                   
e_inicio:   MOVE.L      #2, D0      ; D0 = #2
            MOVE.L      D0, t       ; t = D0
; --------- param_s t               
            MOVE.L      t, D0       ; D0 = t
            MOVE.L      D0, -(SP)   ; PUSH INTO STACK D0
; --------- t_1 = 3                 
            MOVE.L      #3, D0      ; D0 = #3
            MOVE.L      D0, t_1     ; t_1 = D0
; --------- param_s t_1             
            MOVE.L      t_1, D0     ; D0 = t_1
            MOVE.L      D0, -(SP)   ; PUSH INTO STACK D0
; --------- call e_product, t_2     
            SUBA.L      #4, SP      ; SP = SP + 4
            JSR         e_product   ; JUMP TO SUBROUTINE e_product
            MOVE.L      (SP)+, D0   ; D0 = POP FROM STACK
            MOVE.L      D0, t_2     ; t_2 = D0
            ADDA.L      #8, SP      ; SP = SP + 8
; --------- a = t_2                 
            MOVE.L      t_2, D0     ; D0 = t_2
            MOVE.L      D0, a       ; a = D0
; --------- t_3 = 6                 
            MOVE.L      #6, D0      ; D0 = #6
            MOVE.L      D0, t_3     ; t_3 = D0
; --------- if a == t_3 goto e      
            MOVE.L      a, D0       ; D0 = a
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
                                    
; --------- if t_4 == 0 goto e_3    
.e_1:       MOVE.L      t_4, D0     ; D0 = t_4
            MOVE.L      #0, D1      ; D1 = #0
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_3        ; IF Z FLAG = 1 GOTO .e_3
; --------- t_5 = 2 * 3 = 6         
; --------- param_s t_5             
            LEA.L       t_5, A0     ; A0 = t_5
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- t_6 = 6 está almacenado en 'a'  
; --------- param_s t_6             
            LEA.L       t_6, A0     ; A0 = t_6
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- goto e_2                
            JMP         .e_2        ; goto .e_2
; --------- e_3: skip               
                                    
; --------- t_7 = ERROR: 2*3 no ha dado 6  
; --------- param_s t_7             
.e_3:       LEA.L       t_7, A0     ; A0 = t_7
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- e_2: skip               
                                    
; --------- rtn e_inicio            
.e_2:       RTS                     ; RETURN TO SUBROUTINE e_inicio




; --------- e_product: skip         
                                    
; --------- pmb e_product           
e_product:  MOVE.L      8(SP), D0   ; D0 = POP FROM STACK
            MOVE.L      D0, y       ; y = D0
            MOVE.L      12(SP), D0  ; D0 = POP FROM STACK
            MOVE.L      D0, x       ; x = D0
; --------- t_8 = 0                 
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_8     ; t_8 = D0
; --------- z = t_8                 
            MOVE.L      t_8, D0     ; D0 = t_8
            MOVE.L      D0, z       ; z = D0
; --------- e_4: skip               
                                    
; --------- t_9 = 0                 
.e_4:       MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_9     ; t_9 = D0
; --------- if x /= t_9 goto e_7    
            MOVE.L      x, D0       ; D0 = x
            MOVE.L      t_9, D1     ; D1 = t_9
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BNE         .e_7        ; IF Z FLAG = 0 GOTO .e_7
; --------- t_10 = 0                
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_10    ; t_10 = D0
; --------- goto e_8                
            JMP         .e_8        ; goto .e_8
; --------- e_7: skip               
                                    
; --------- t_10 = -1               
.e_7:       MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_10    ; t_10 = D0
; --------- e_8: skip               
                                    
; --------- if t_10 == 0 goto e_5   
.e_8:       MOVE.L      t_10, D0    ; D0 = t_10
            MOVE.L      #0, D1      ; D1 = #0
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_5        ; IF Z FLAG = 1 GOTO .e_5
; --------- t_11 = 2                
            MOVE.L      #2, D0      ; D0 = #2
            MOVE.L      D0, t_11    ; t_11 = D0
; --------- t_12 = x mod t_11       
            MOVE.L      x, D0       ; D0 = x
            MOVE.L      t_11, D1    ; D1 = t_11
            DIVS.W      D1, D0      ; D0.h = D0 % D1. D0.l = D0 / D1
            LSR.L       #8, D0      ; D0.l = D0.h
            LSR.L       #8, D0      ; D0.l = D0.h
            MOVE.L      D0, t_12    ; t_12 = D0
; --------- t_13 = 1                
            MOVE.L      #1, D0      ; D0 = #1
            MOVE.L      D0, t_13    ; t_13 = D0
; --------- if t_12 == t_13 goto e_9  
            MOVE.L      t_12, D0    ; D0 = t_12
            MOVE.L      t_13, D1    ; D1 = t_13
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_9        ; IF Z FLAG = 1 GOTO .e_9
; --------- t_14 = 0                
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_14    ; t_14 = D0
; --------- goto e_10               
            JMP         .e_10       ; goto .e_10
; --------- e_9: skip               
                                    
; --------- t_14 = -1               
.e_9:       MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_14    ; t_14 = D0
; --------- e_10: skip              
                                    
; --------- if t_14 == 0 goto e_12  
.e_10:      MOVE.L      t_14, D0    ; D0 = t_14
            MOVE.L      #0, D1      ; D1 = #0
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_12       ; IF Z FLAG = 1 GOTO .e_12
; --------- t_15 = z + y            
            MOVE.L      z, D0       ; D0 = z
            MOVE.L      y, D1       ; D1 = y
            ADD.L       D0, D1      ; D1 = D1 + D0
            MOVE.L      D1, t_15    ; t_15 = D1
; --------- z = t_15                
            MOVE.L      t_15, D0    ; D0 = t_15
            MOVE.L      D0, z       ; z = D0
; --------- goto e_11               
            JMP         .e_11       ; goto .e_11
; --------- e_12: skip              
                                    
; --------- e_11: skip              
.e_12:                              
; --------- t_16 = 2                
.e_11:      MOVE.L      #2, D0      ; D0 = #2
            MOVE.L      D0, t_16    ; t_16 = D0
; --------- t_17 = y * t_16         
;           A*B = A1A0*B1B0 = A0*B0 + A1*B1 * 2^16  
            MOVE.L      y, D0       ; D0 = y
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
            MOVE.L      D0, t_17    ; t_17 = D0
; --------- y = t_17                
            MOVE.L      t_17, D0    ; D0 = t_17
            MOVE.L      D0, y       ; y = D0
; --------- t_18 = 2                
            MOVE.L      #2, D0      ; D0 = #2
            MOVE.L      D0, t_18    ; t_18 = D0
; --------- t_19 = x / t_18         
            MOVE.L      x, D0       ; D0 = x
            MOVE.L      t_18, D1    ; D1 = t_18
            DIVS.W      D1, D0      ; D0.h = D0 % D1. D0.l = D0 / D1
            AND.L       #$0000FFFF, D0 ; Mask
            MOVE.L      D0, t_19    ; t_19 = D0
; --------- x = t_19                
            MOVE.L      t_19, D0    ; D0 = t_19
            MOVE.L      D0, x       ; x = D0
; --------- e_6: skip               
                                    
; --------- goto e_4                
.e_6:       JMP         .e_4        ; goto .e_4
; --------- e_5: skip               
                                    
; --------- rtn e_product, z        
.e_5:       MOVE.L      z, D0       ; D0 = z
            MOVE.L      D0, 4(SP)   ; PUSH INTO STACK D0
            RTS                     ; RETURN TO SUBROUTINE e_product


            END prod                ; Fin del programa


