; ==============================================================================
; TITLE       : loops
; COMPILED BY : jartu
; COMPILER BY : Juan Arturo Abaurrea Calafell
;               Dani Salanova Dmitriyev
;               Marta González Juan
; ==============================================================================

            ORG         $1000       ; Origen

t           DC.L 0                  
t_1         DC.L 1                  
t_3         DC.L 1                  
t_5         DC.L 1                  
t_7         DC.L -1                 
t_8         DC.L -1                 
t_9         DC.L 1                  
t_11        DC.L 1                  
t_13        DC.L 1                  
t_15        DC.L 2                  
t_16        DC.L 1                  
t_18        DC.L 1                  
t_20        DC.L 1                  
t_22        DC.L 1                  
t_24        DC.L -1                 
t_25        DC.L 2                  
t_26        DC.L -1                 
t_27        DC.L 1                  
t_29        DC.L 1                  
t_31        DC.L 1                  
t_33        DC.L 1                  
argumentos  DS.L 1                  
t_29        DS.L 1                  
t_27        DS.L 1                  
t_28        DS.L 1                  
global      DS.L 1                  
b_1         DS.L 1                  
t_25        DS.L 1                  
t_26        DS.L 1                  
t_23        DS.L 1                  
t_24        DS.L 1                  
t_21        DS.L 1                  
t_22        DS.L 1                  
t_20        DS.L 1                  
b           DS.L 1                  
t_18        DS.L 1                  
t_19        DS.L 1                  
t_2         DS.L 1                  
t_16        DS.L 1                  
t_1         DS.L 1                  
t_17        DS.L 1                  
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
t_34        DS.L 1                  
t_13        DS.L 1                  
t_10        DS.L 1                  
t_32        DS.L 1                  
t_11        DS.L 1                  
t_33        DS.L 1                  
t_30        DS.L 1                  
t_31        DS.L 1                  
LOOPS                               ; Etiqueta inicial (main)

            JSR         e_inicio    ; Se ejecuta el main
            SIMHALT                 ; Fin de la ejecución

; --------- global = t              
            LEA.L       t, A0       ; A0 = @t
            MOVE.L      A0, global  ; global = A0
; --------- e_inicio: skip          
; --------- pmb e_inicio            
; --------- rtn e_inicio            
e_inicio:   RTS                     ; RETURN TO SUBROUTINE e_inicio




; --------- e_dowhile: skip         
; --------- pmb e_dowhile           
; --------- e: skip                 
; --------- t_2 = global + t_1      
.e:         LEA.L       global, A0  ; A0 = @global
            LEA.L       t_1, A1     ; A1 = @t_1
            ADD.L       A0, A1      ; A1 = A1 + A0
            MOVE.L      A1, t_2     ; t_2 = A1
; --------- global = t_2            
            LEA.L       t_2, A0     ; A0 = @t_2
            MOVE.L      A0, global  ; global = A0
; --------- goto e_1                
            JMP         .e_1        ; goto .e_1
; --------- t_4 = global + t_3      
            LEA.L       global, A0  ; A0 = @global
            LEA.L       t_3, A1     ; A1 = @t_3
            ADD.L       A0, A1      ; A1 = A1 + A0
            MOVE.L      A1, t_4     ; t_4 = A1
; --------- global = t_4            
            LEA.L       t_4, A0     ; A0 = @t_4
            MOVE.L      A0, global  ; global = A0
; --------- goto e_2                
            JMP         .e_2        ; goto .e_2
; --------- t_6 = global + t_5      
            LEA.L       global, A0  ; A0 = @global
            LEA.L       t_5, A1     ; A1 = @t_5
            ADD.L       A0, A1      ; A1 = A1 + A0
            MOVE.L      A1, t_6     ; t_6 = A1
; --------- global = t_6            
            LEA.L       t_6, A0     ; A0 = @t_6
            MOVE.L      A0, global  ; global = A0
; --------- e_2: skip               
; --------- if t_7 == 0 goto e_1    
.e_2:       MOVE.L      t_7, D0     ; D0 = t_7
            CMP.L       #0, D0      ; UPDATE FLAGS WITH D0 - #0
            BEQ         .e_1        ; IF Z FLAG = 1 GOTO .e_1
; --------- goto e                  
            JMP         .e          ; goto .e
; --------- e_1: skip               
; --------- rtn e_dowhile           
.e_1:       RTS                     ; RETURN TO SUBROUTINE e_dowhile


; --------- e_while: skip           
; --------- pmb e_while             
; --------- e_3: skip               
; --------- if t_8 == 0 goto e_4    
.e_3:       MOVE.L      t_8, D0     ; D0 = t_8
            CMP.L       #0, D0      ; UPDATE FLAGS WITH D0 - #0
            BEQ         .e_4        ; IF Z FLAG = 1 GOTO .e_4
; --------- t_10 = global + t_9     
            LEA.L       global, A0  ; A0 = @global
            LEA.L       t_9, A1     ; A1 = @t_9
            ADD.L       A0, A1      ; A1 = A1 + A0
            MOVE.L      A1, t_10    ; t_10 = A1
; --------- global = t_10           
            LEA.L       t_10, A0    ; A0 = @t_10
            MOVE.L      A0, global  ; global = A0
; --------- goto e_4                
            JMP         .e_4        ; goto .e_4
; --------- t_12 = global + t_11    
            LEA.L       global, A0  ; A0 = @global
            LEA.L       t_11, A1    ; A1 = @t_11
            ADD.L       A0, A1      ; A1 = A1 + A0
            MOVE.L      A1, t_12    ; t_12 = A1
; --------- global = t_12           
            LEA.L       t_12, A0    ; A0 = @t_12
            MOVE.L      A0, global  ; global = A0
; --------- goto e_5                
            JMP         .e_5        ; goto .e_5
; --------- t_14 = global + t_13    
            LEA.L       global, A0  ; A0 = @global
            LEA.L       t_13, A1    ; A1 = @t_13
            ADD.L       A0, A1      ; A1 = A1 + A0
            MOVE.L      A1, t_14    ; t_14 = A1
; --------- global = t_14           
            LEA.L       t_14, A0    ; A0 = @t_14
            MOVE.L      A0, global  ; global = A0
; --------- e_5: skip               
; --------- goto e_3                
.e_5:       JMP         .e_3        ; goto .e_3
; --------- e_4: skip               
; --------- rtn e_while             
.e_4:       RTS                     ; RETURN TO SUBROUTINE e_while


; --------- e_dofor: skip           
; --------- pmb e_dofor             
; --------- b = t_15                
e_dofor:    LEA.L       t_15, A0    ; A0 = @t_15
            MOVE.L      A0, b       ; b = A0
; --------- e_6: skip               
; --------- t_17 = global + t_16    
.e_6:       LEA.L       global, A0  ; A0 = @global
            LEA.L       t_16, A1    ; A1 = @t_16
            ADD.L       A0, A1      ; A1 = A1 + A0
            MOVE.L      A1, t_17    ; t_17 = A1
; --------- global = t_17           
            LEA.L       t_17, A0    ; A0 = @t_17
            MOVE.L      A0, global  ; global = A0
; --------- goto e_7                
            JMP         .e_7        ; goto .e_7
; --------- t_19 = global + t_18    
            LEA.L       global, A0  ; A0 = @global
            LEA.L       t_18, A1    ; A1 = @t_18
            ADD.L       A0, A1      ; A1 = A1 + A0
            MOVE.L      A1, t_19    ; t_19 = A1
; --------- global = t_19           
            LEA.L       t_19, A0    ; A0 = @t_19
            MOVE.L      A0, global  ; global = A0
; --------- goto e_8                
            JMP         .e_8        ; goto .e_8
; --------- t_21 = global + t_20    
            LEA.L       global, A0  ; A0 = @global
            LEA.L       t_20, A1    ; A1 = @t_20
            ADD.L       A0, A1      ; A1 = A1 + A0
            MOVE.L      A1, t_21    ; t_21 = A1
; --------- global = t_21           
            LEA.L       t_21, A0    ; A0 = @t_21
            MOVE.L      A0, global  ; global = A0
; --------- e_8: skip               
; --------- t_23 = b + t_22         
.e_8:       LEA.L       b, A0       ; A0 = @b
            LEA.L       t_22, A1    ; A1 = @t_22
            ADD.L       A0, A1      ; A1 = A1 + A0
            MOVE.L      A1, t_23    ; t_23 = A1
; --------- b = t_23                
            LEA.L       t_23, A0    ; A0 = @t_23
            MOVE.L      A0, b       ; b = A0
; --------- if t_24 == 0 goto e_7   
            MOVE.L      t_24, D0    ; D0 = t_24
            CMP.L       #0, D0      ; UPDATE FLAGS WITH D0 - #0
            BEQ         .e_7        ; IF Z FLAG = 1 GOTO .e_7
; --------- goto e_6                
            JMP         .e_6        ; goto .e_6
; --------- e_7: skip               
; --------- rtn e_dofor             
.e_7:       RTS                     ; RETURN TO SUBROUTINE e_dofor


; --------- e_for: skip             
; --------- pmb e_for               
; --------- b_1 = t_25              
e_for:      LEA.L       t_25, A0    ; A0 = @t_25
            MOVE.L      A0, b_1     ; b_1 = A0
; --------- e_9: skip               
; --------- if t_26 == 0 goto e_10  
.e_9:       MOVE.L      t_26, D0    ; D0 = t_26
            CMP.L       #0, D0      ; UPDATE FLAGS WITH D0 - #0
            BEQ         .e_10       ; IF Z FLAG = 1 GOTO .e_10
; --------- t_28 = global + t_27    
            LEA.L       global, A0  ; A0 = @global
            LEA.L       t_27, A1    ; A1 = @t_27
            ADD.L       A0, A1      ; A1 = A1 + A0
            MOVE.L      A1, t_28    ; t_28 = A1
; --------- global = t_28           
            LEA.L       t_28, A0    ; A0 = @t_28
            MOVE.L      A0, global  ; global = A0
; --------- goto e_10               
            JMP         .e_10       ; goto .e_10
; --------- t_30 = global + t_29    
            LEA.L       global, A0  ; A0 = @global
            LEA.L       t_29, A1    ; A1 = @t_29
            ADD.L       A0, A1      ; A1 = A1 + A0
            MOVE.L      A1, t_30    ; t_30 = A1
; --------- global = t_30           
            LEA.L       t_30, A0    ; A0 = @t_30
            MOVE.L      A0, global  ; global = A0
; --------- goto e_11               
            JMP         .e_11       ; goto .e_11
; --------- t_32 = global + t_31    
            LEA.L       global, A0  ; A0 = @global
            LEA.L       t_31, A1    ; A1 = @t_31
            ADD.L       A0, A1      ; A1 = A1 + A0
            MOVE.L      A1, t_32    ; t_32 = A1
; --------- global = t_32           
            LEA.L       t_32, A0    ; A0 = @t_32
            MOVE.L      A0, global  ; global = A0
; --------- e_11: skip              
; --------- t_34 = b_1 + t_33       
.e_11:      LEA.L       b_1, A0     ; A0 = @b_1
            LEA.L       t_33, A1    ; A1 = @t_33
            ADD.L       A0, A1      ; A1 = A1 + A0
            MOVE.L      A1, t_34    ; t_34 = A1
; --------- b_1 = t_34              
            LEA.L       t_34, A0    ; A0 = @t_34
            MOVE.L      A0, b_1     ; b_1 = A0
; --------- goto e_9                
            JMP         .e_9        ; goto .e_9
; --------- e_10: skip              
; --------- rtn e_for               
.e_10:      RTS                     ; RETURN TO SUBROUTINE e_for


            END LOOPS               ; Fin del programa
