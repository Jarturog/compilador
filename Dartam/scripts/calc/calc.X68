; ==============================================================================
; TITLE       : calc
; COMPILED BY : jartu
; COMPILER BY : Juan Arturo Abaurrea Calafell
;               Dani Salanova Dmitriyev
;               Marta González Juan
; ==============================================================================

            ORG         $1000       ; Origen

t           DC.L 0                  
t_2         DC.L -1                 
t_4         DC.L 50                 
t_5         DC.L 50                 
t_6         DC.L 0                  
t_7         DC.L 5                  
t_8         DC.L 5                  
t_10        DC.L 0                  
t_11        DC.L 3                  
t_12        DC.L 3                  
t_13        DC.L 0                  
argumentos  DS.L 1                  
c           DS.L 1                  
t_2         DS.L 1                  
t_16        DS.L 1                  
t_1         DS.L 1                  
t_4         DS.L 1                  
t_3         DS.L 1                  
t_6         DS.L 1                  
t_5         DS.L 1                  
t_8         DS.L 1                  
t_7         DS.L 1                  
t_9         DS.L 1                  
t           DS.L 1                  
t_14        DS.L 1                  
t_15        DS.L 1                  
t_12        DS.L 1                  
t_13        DS.L 1                  
t_10        DS.L 1                  
t_11        DS.L 1                  
CALC                                ; Etiqueta inicial (main)

            JSR         e_inicio    ; Se ejecuta el main
            SIMHALT                 ; Fin de la ejecución

; --------- e_inicio: skip          
; --------- pmb e_inicio            
; --------- t_1 = not t             
e_inicio:   MOVE.L      t, D0       ; D0 = t
            NOT.L       D0          ; D0 = not D0
            MOVE.L      D0, t_1     ; t_1 = D0
; --------- t_3 = t_1 or t_2        
            MOVE.L      t_1, D0     ; D0 = t_1
            OR.L        t_2, D0     ; D0 = D0 or t_2
            MOVE.L      D0, t_3     ; t_3 = D0
; --------- if t_4 <= t_5 goto e    
            MOVE.L      t_4, D0     ; D0 = t_4
            CMP.L       t_5, D0     ; UPDATE FLAGS WITH D0 - t_5
            BGT         .e          ; IF ((N XOR V) OR Z) FLAGS = 0 GOTO .e
; --------- goto e_1                
            JMP         .e_1        ; goto .e_1
; --------- e: skip                 
; --------- t_6 = -1                
.e:         MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_6     ; t_6 = D0
; --------- e_1: skip               
; --------- t_9 = -t_8              
.e_1:       MOVE.L      t_8, D0     ; D0 = t_8
            NEG.L       D0          ; D0 = -D0
            MOVE.L      D0, t_9     ; t_9 = D0
; --------- if t_7 /= t_9 goto e_2  
            MOVE.L      t_7, D0     ; D0 = t_7
            CMP.L       t_9, D0     ; UPDATE FLAGS WITH D0 - t_9
            BNE         .e_2        ; IF Z FLAG = 0 GOTO .e_2
; --------- goto e_3                
            JMP         .e_3        ; goto .e_3
; --------- e_2: skip               
; --------- t_10 = -1               
.e_2:       MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_10    ; t_10 = D0
; --------- e_3: skip               
; --------- if t_11 == t_12 goto e_4  
.e_3:       MOVE.L      t_11, D0    ; D0 = t_11
            CMP.L       t_12, D0    ; UPDATE FLAGS WITH D0 - t_12
            BEQ         .e_4        ; IF Z FLAG = 1 GOTO .e_4
; --------- goto e_5                
            JMP         .e_5        ; goto .e_5
; --------- e_4: skip               
; --------- t_13 = -1               
.e_4:       MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_13    ; t_13 = D0
; --------- e_5: skip               
; --------- t_14 = t_10 or t_13     
.e_5:       MOVE.L      t_10, D0    ; D0 = t_10
            OR.L        t_13, D0    ; D0 = D0 or t_13
            MOVE.L      D0, t_14    ; t_14 = D0
; --------- t_15 = t_6 or t_14      
            MOVE.L      t_6, D0     ; D0 = t_6
            OR.L        t_14, D0    ; D0 = D0 or t_14
            MOVE.L      D0, t_15    ; t_15 = D0
; --------- t_16 = t_3 and t_15     
            MOVE.L      t_3, D0     ; D0 = t_3
            AND.L       t_15, D0    ; D0 = D0 and t_15
            MOVE.L      D0, t_16    ; t_16 = D0
; --------- c = t_16                
            LEA.L       t_16, A0    ; A0 = @t_16
            MOVE.L      A0, c       ; c = A0
; --------- rtn e_inicio            
            RTS                     ; RETURN TO SUBROUTINE e_inicio




            END CALC                ; Fin del programa
