; ==============================================================================
; TITLE       : p
; COMPILED BY : jartu
; COMPILER BY : Juan Arturo Abaurrea Calafell
;               Dani Salanova Dmitriyev
;               Marta González Juan
; ==============================================================================

            ORG         $1000       ; Origen

t           DC.L 2                  ; INT
t_1         DC.L 3                  ; INT
z           DC.L 0                  ; INT
t_6         DC.L 0                  ; INT
t_7         DC.L 0                  ; BOOLEAN
t_8         DC.L 2                  ; INT
t_10        DC.L 1                  ; INT
t_11        DC.L 0                  ; BOOLEAN
t_13        DC.L 2                  ; INT
t_15        DC.L 2                  ; INT
t_2         DS.L 1                  ; INT
t_16        DS.L 1                  ; INT
t_4         DS.L 1                  ; INT
t_9         DS.L 1                  ; INT
x           DS.L 1                  ; INT
t_14        DS.L 1                  ; INT
y           DS.L 1                  ; INT
t_12        DS.L 1                  ; INT

; --------- SUBROUTINES             
e_show      MOVEA.L     4(SP), A1   ; A1 = POP FROM STACK
            MOVE.L      #13, D0     ; Task 13 of TRAP 15: Display the NULL terminated string pointed to by (A1) with CR, LF
            TRAP        #15         ; Interruption generated
            RTS                     ; RETURN TO SUBROUTINE ...

; --------- INITIALIZATION          
p                                   ; Etiqueta inicial (main)

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
; --------- t_4 = t_2               
            MOVE.L      t_2, D0     ; D0 = t_2
            MOVE.L      D0, t_4     ; t_4 = D0
; --------- param_s t_4             
            LEA.L       t_4, A0     ; A0 = t_4
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- rtn e_inicio            
            RTS                     ; RETURN TO SUBROUTINE e_inicio




; --------- e_product: skip         
                                    
; --------- pmb e_product           
e_product:  MOVE.L      8(SP), D0   ; D0 = POP FROM STACK
            MOVE.L      D0, y       ; y = D0
            MOVE.L      12(SP), D0  ; D0 = POP FROM STACK
            MOVE.L      D0, x       ; x = D0
; --------- z = 0                   
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, z       ; z = D0
; --------- e: skip                 
                                    
; --------- t_6 = 0                 
.e:         MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_6     ; t_6 = D0
; --------- if x /= t_6 goto e_3    
            MOVE.L      x, D0       ; D0 = x
            MOVE.L      t_6, D1     ; D1 = t_6
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BNE         .e_3        ; IF Z FLAG = 0 GOTO .e_3
; --------- t_7 = 0                 
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_7     ; t_7 = D0
; --------- goto e_4                
            JMP         .e_4        ; goto .e_4
; --------- e_3: skip               
                                    
; --------- t_7 = -1                
.e_3:       MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_7     ; t_7 = D0
; --------- e_4: skip               
                                    
; --------- if t_7 == 0 goto e_1    
.e_4:       MOVE.L      t_7, D0     ; D0 = t_7
            MOVE.L      #0, D1      ; D1 = #0
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_1        ; IF Z FLAG = 1 GOTO .e_1
; --------- t_8 = 2                 
            MOVE.L      #2, D0      ; D0 = #2
            MOVE.L      D0, t_8     ; t_8 = D0
; --------- t_9 = x mod t_8         
            MOVE.L      x, D0       ; D0 = x
            MOVE.L      t_8, D1     ; D1 = t_8
            DIVS.W      D1, D0      ; D0.h = D0 % D1. D0.l = D0 / D1
            LSR.L       #8, D0      ; D0.l = D0.h
            LSR.L       #8, D0      ; D0.l = D0.h
            MOVE.L      D0, t_9     ; t_9 = D0
; --------- t_10 = 1                
            MOVE.L      #1, D0      ; D0 = #1
            MOVE.L      D0, t_10    ; t_10 = D0
; --------- if t_9 == t_10 goto e_5  
            MOVE.L      t_9, D0     ; D0 = t_9
            MOVE.L      t_10, D1    ; D1 = t_10
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_5        ; IF Z FLAG = 1 GOTO .e_5
; --------- t_11 = 0                
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_11    ; t_11 = D0
; --------- goto e_6                
            JMP         .e_6        ; goto .e_6
; --------- e_5: skip               
                                    
; --------- t_11 = -1               
.e_5:       MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_11    ; t_11 = D0
; --------- e_6: skip               
                                    
; --------- if t_11 == 0 goto e_8   
.e_6:       MOVE.L      t_11, D0    ; D0 = t_11
            MOVE.L      #0, D1      ; D1 = #0
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_8        ; IF Z FLAG = 1 GOTO .e_8
; --------- t_12 = z + y            
            MOVE.L      z, D0       ; D0 = z
            MOVE.L      y, D1       ; D1 = y
            ADD.L       D0, D1      ; D1 = D1 + D0
            MOVE.L      D1, t_12    ; t_12 = D1
; --------- z = t_12                
            MOVE.L      t_12, D0    ; D0 = t_12
            MOVE.L      D0, z       ; z = D0
; --------- goto e_7                
            JMP         .e_7        ; goto .e_7
; --------- e_8: skip               
                                    
; --------- e_7: skip               
.e_8:                               
; --------- t_13 = 2                
.e_7:       MOVE.L      #2, D0      ; D0 = #2
            MOVE.L      D0, t_13    ; t_13 = D0
; --------- t_14 = y * t_13         
;           A*B = A1A0*B1B0 = A0*B0 + A1*B1 * 2^16  
            MOVE.L      y, D0       ; D0 = y
            MOVE.L      t_13, D1    ; D1 = t_13
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
            MOVE.L      D0, t_14    ; t_14 = D0
; --------- y = t_14                
            MOVE.L      t_14, D0    ; D0 = t_14
            MOVE.L      D0, y       ; y = D0
; --------- t_15 = 2                
            MOVE.L      #2, D0      ; D0 = #2
            MOVE.L      D0, t_15    ; t_15 = D0
; --------- t_16 = x / t_15         
            MOVE.L      x, D0       ; D0 = x
            MOVE.L      t_15, D1    ; D1 = t_15
            DIVS.W      D1, D0      ; D0.h = D0 % D1. D0.l = D0 / D1
            AND.L       #$0000FFFF, D0 ; Mask
            MOVE.L      D0, t_16    ; t_16 = D0
; --------- x = t_16                
            MOVE.L      t_16, D0    ; D0 = t_16
            MOVE.L      D0, x       ; x = D0
; --------- goto e                  
            JMP         .e          ; goto .e
; --------- e_1: skip               
                                    
; --------- rtn e_product, z        
.e_1:       MOVE.L      z, D0       ; D0 = z
            MOVE.L      D0, 4(SP)   ; PUSH INTO STACK D0
            RTS                     ; RETURN TO SUBROUTINE e_product


            END p                   ; Fin del programa


