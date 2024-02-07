; ==============================================================================
; TITLE       : calc
; COMPILED BY : jartu
; COMPILER BY : Juan Arturo Abaurrea Calafell
;               Dani Salanova Dmitriyev
;               Marta Gonzalez Juan
; ==============================================================================

            ORG         $1000       ; Origen

t           DC.L 0                  ; BOOLEAN
t_2         DC.L -1                 ; BOOLEAN
t_4         DC.L '2'                ; CHAR
t_5         DC.L '2'                ; CHAR
t_6         DC.L 0                  ; BOOLEAN
t_7         DC.L 5                  ; INT
t_8         DC.L 5                  ; INT
t_10        DC.L 0                  ; BOOLEAN
t_11        DC.L 3                  ; INT
t_12        DC.L 3                  ; INT
t_13        DC.L 0                  ; BOOLEAN
t_17        DC.B 'verdadero'        ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_18        DC.B 'falso'            ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
c           DS.L 1                  ; BOOLEAN
t_16        DS.L 1                  ; BOOLEAN
t_1         DS.L 1                  ; BOOLEAN
t_3         DS.L 1                  ; BOOLEAN
t_9         DS.L 1                  ; INT
t_14        DS.L 1                  ; BOOLEAN
t_15        DS.L 1                  ; BOOLEAN

; --------- SUBROUTINES             
e_show      MOVEA.L     4(SP), A1   ; A1 = POP FROM STACK
            MOVE.L      #13, D0     ; Task 13 of TRAP 15: Display the NULL terminated string pointed to by (A1) with CR, LF
            TRAP        #15         ; Interruption generated
            RTS                     ; RETURN TO SUBROUTINE ...

; --------- INITIALIZATION          
calc                                ; Etiqueta inicial (main)

            MOVE.L      #32, D0     ; Task 32 of TRAP 15: Hardware/Simulator
            MOVE.B      #5, D1      ; Enable exception processing (for input/output)
            TRAP        #15         ; Interruption generated
            JSR         e_inicio    ; Se ejecuta el main
            SIMHALT                 ; Fin de la ejecucion

; --------- e_inicio: skip          
                                    
; --------- pmb e_inicio            
; --------- t = 0                   
e_inicio:   MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t       ; t = D0
; --------- t_1 = not t             
            MOVE.L      t, D0       ; D0 = t
            NOT.L       D0          ; D0 = not D0
            MOVE.L      D0, t_1     ; t_1 = D0
; --------- t_2 = -1                
            MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_2     ; t_2 = D0
; --------- t_3 = t_1 or t_2        
            MOVE.L      t_1, D0     ; D0 = t_1
            MOVE.L      t_2, D1     ; D1 = t_2
            OR.L        D0, D1      ; D1 = D1 or D0
            MOVE.L      D1, t_3     ; t_3 = D1
; --------- t_4 = 2                 
            MOVE.L      #'2', D0    ; D0 = #'2'
            MOVE.L      D0, t_4     ; t_4 = D0
; --------- t_5 = 2                 
            MOVE.L      #'2', D0    ; D0 = #'2'
            MOVE.L      D0, t_5     ; t_5 = D0
; --------- if t_4 <= t_5 goto e    
            MOVE.L      t_4, D0     ; D0 = t_4
            MOVE.L      t_5, D1     ; D1 = t_5
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D1 - D0
            BLE         .e          ; IF ((N XOR V) OR Z) FLAGS = 1 GOTO .e
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
                                    
; --------- t_7 = 5                 
.e_1:       MOVE.L      #5, D0      ; D0 = #5
            MOVE.L      D0, t_7     ; t_7 = D0
; --------- t_8 = 5                 
            MOVE.L      #5, D0      ; D0 = #5
            MOVE.L      D0, t_8     ; t_8 = D0
; --------- t_9 = -t_8              
            MOVE.L      t_8, D0     ; D0 = t_8
            NEG.L       D0          ; D0 = -D0
            MOVE.L      D0, t_9     ; t_9 = D0
; --------- if t_7 /= t_9 goto e_2  
            MOVE.L      t_7, D0     ; D0 = t_7
            MOVE.L      t_9, D1     ; D1 = t_9
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BNE         .e_2        ; IF Z FLAG = 0 GOTO .e_2
; --------- t_10 = 0                
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_10    ; t_10 = D0
; --------- goto e_3                
            JMP         .e_3        ; goto .e_3
; --------- e_2: skip               
                                    
; --------- t_10 = -1               
.e_2:       MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_10    ; t_10 = D0
; --------- e_3: skip               
                                    
; --------- t_11 = 3                
.e_3:       MOVE.L      #3, D0      ; D0 = #3
            MOVE.L      D0, t_11    ; t_11 = D0
; --------- t_12 = 3                
            MOVE.L      #3, D0      ; D0 = #3
            MOVE.L      D0, t_12    ; t_12 = D0
; --------- if t_11 == t_12 goto e_4  
            MOVE.L      t_11, D0    ; D0 = t_11
            MOVE.L      t_12, D1    ; D1 = t_12
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_4        ; IF Z FLAG = 1 GOTO .e_4
; --------- t_13 = 0                
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_13    ; t_13 = D0
; --------- goto e_5                
            JMP         .e_5        ; goto .e_5
; --------- e_4: skip               
                                    
; --------- t_13 = -1               
.e_4:       MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_13    ; t_13 = D0
; --------- e_5: skip               
                                    
; --------- t_14 = t_10 or t_13     
.e_5:       MOVE.L      t_10, D0    ; D0 = t_10
            MOVE.L      t_13, D1    ; D1 = t_13
            OR.L        D0, D1      ; D1 = D1 or D0
            MOVE.L      D1, t_14    ; t_14 = D1
; --------- t_15 = t_6 or t_14      
            MOVE.L      t_6, D0     ; D0 = t_6
            MOVE.L      t_14, D1    ; D1 = t_14
            OR.L        D0, D1      ; D1 = D1 or D0
            MOVE.L      D1, t_15    ; t_15 = D1
; --------- t_16 = t_3 and t_15     
            MOVE.L      t_3, D0     ; D0 = t_3
            MOVE.L      t_15, D1    ; D1 = t_15
            AND.L       D0, D1      ; D1 = D1 and D0
            MOVE.L      D1, t_16    ; t_16 = D1
; --------- c = t_16                
            MOVE.L      t_16, D0    ; D0 = t_16
            MOVE.L      D0, c       ; c = D0
; --------- if c == 0 goto e_7      
            MOVE.L      c, D0       ; D0 = c
            MOVE.L      #0, D1      ; D1 = #0
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_7        ; IF Z FLAG = 1 GOTO .e_7
; --------- t_17 = verdadero        
; --------- param_s t_17            
            LEA.L       t_17, A0    ; A0 = t_17
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- goto e_6                
            JMP         .e_6        ; goto .e_6
; --------- e_7: skip               
                                    
; --------- t_18 = falso            
; --------- param_s t_18            
.e_7:       LEA.L       t_18, A0    ; A0 = t_18
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- e_6: skip               
                                    
; --------- rtn e_inicio            
.e_6:       RTS                     ; RETURN TO SUBROUTINE e_inicio




            END calc                ; Fin del programa


