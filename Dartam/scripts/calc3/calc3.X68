; ==============================================================================
; TITLE       : calc3
; COMPILED BY : jartu
; COMPILER BY : Juan Arturo Abaurrea Calafell
;               Dani Salanova Dmitriyev
;               Marta González Juan
; ==============================================================================

            ORG         $1000       ; Origen

a           DC.L 16                 ; INT
b           DC.L 20                 ; INT
t_3         DC.B 0                  ; BOOLEAN
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_4         DC.L 1                  ; INT
t_2         DS.L 1                  ; INT
t_5         DS.L 1                  ; INT
c1          DS.L 1                  ; INT
b0          DS.L 1                  ; INT
a0          DS.L 1                  ; INT
a_1         DS.L 1                  ; INT
b_1         DS.L 1                  ; INT
CALC3                               ; Etiqueta inicial (main)
            JSR         e_inicio    ; Se ejecuta el main
            SIMHALT                 ; Fin de la ejecución

; --------- e_inicio: skip          
                                    
; --------- pmb e_inicio            
; --------- a = 16                  
e_inicio:   MOVE.L      #16, D0     ; D0 = #16
            MOVE.L      D0, a       ; a = D0
; --------- b = 20                  
            MOVE.L      #20, D0     ; D0 = #20
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
; --------- t_3 = 0                 
            CLR.L       D0          ; CLEAR D0
            MOVE.B      #0, D0      ; D0 = #0
            MOVE.B      D0, t_3     ; t_3 = D0
; --------- goto e_4                
            JMP         .e_4        ; goto .e_4
; --------- e_3: skip               
                                    
; --------- t_3 = -1                
.e_3:       CLR.L       D0          ; CLEAR D0
            MOVE.B      #-1, D0     ; D0 = #-1
            MOVE.B      D0, t_3     ; t_3 = D0
; --------- e_4: skip               
                                    
; --------- if t_3 == 0 goto e_1    
.e_4:       CLR.L       D0          ; CLEAR D0
            MOVE.B      t_3, D0     ; D0 = t_3
            CMP.B       #0, D0      ; UPDATE FLAGS WITH D0 - #0
            BEQ         .e_1        ; IF Z FLAG = 1 GOTO .e_1
; --------- t_4 = 1                 
            MOVE.L      #1, D0      ; D0 = #1
            MOVE.L      D0, t_4     ; t_4 = D0
; --------- t_5 = a_1 + t_4         
            MOVE.L      a_1, D0     ; D0 = a_1
            MOVE.L      t_4, D1     ; D1 = t_4
            ADD.L       D0, D1      ; D1 = D1 + D0
            MOVE.L      D1, t_5     ; t_5 = D1
; --------- a_1 = t_5               
            MOVE.L      t_5, D0     ; D0 = t_5
            MOVE.L      D0, a_1     ; a_1 = D0
; --------- goto e                  
            JMP         .e          ; goto .e
; --------- e_1: skip               
                                    
; --------- rtn e_mcd1, a_1         
.e_1:       MOVE.L      a_1, 4(SP)  ; PUSH INTO STACK a_1
            RTS                     ; RETURN TO SUBROUTINE e_mcd1


            END CALC3               ; Fin del programa


