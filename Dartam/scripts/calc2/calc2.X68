; ==============================================================================
; TITLE       : calc2
; COMPILED BY : jartu
; COMPILER BY : Juan Arturo Abaurrea Calafell
;               Dani Salanova Dmitriyev
;               Marta González Juan
; ==============================================================================

            ORG         $1000       ; Origen

t           DC.B 'a',0              
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_1         DC.B 'hola',0           
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_3         DC.L 97                 
t_4         DC.B 'hola',0           
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_6         DC.L 97                 
t_7         DC.B 'hola',0           
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
argumentos  DS.L 1                  
t           DS.L 1                  
t_2         DS.L 1                  
t_1         DS.L 1                  
t_4         DS.L 1                  
t_3         DS.L 1                  
t_6         DS.L 1                  
t_5         DS.L 1                  
t_8         DS.L 1                  
t_7         DS.L 1                  
CALC2                               ; Etiqueta inicial (main)

            JSR         e_inicio    ; Se ejecuta el main
            SIMHALT                 ; Fin de la ejecución

; --------- e_inicio: skip          
; --------- pmb e_inicio            
; --------- t_2 = t concat t_1      
e_inicio:   LEA.L       t, A0       ; A0 = @t
            LEA.L       t_1, A1     ; A1 = @t_1
            LEA.L       t_2, A2     ; FETCH t_2
            JSR         CONCATENAR  
; --------- param_s t_2             
            MOVE.L      t_2, -(SP)  ; PUSH INTO STACK t_2
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- t_5 = t_3 concat t_4    
            LEA.L       t_3, A0     ; A0 = @t_3
            LEA.L       t_4, A1     ; A1 = @t_4
            LEA.L       t_5, A2     ; FETCH t_5
            JSR         CONCATENAR  
; --------- param_s t_5             
            MOVE.L      t_5, -(SP)  ; PUSH INTO STACK t_5
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- t_8 = t_6 concat t_7    
            LEA.L       t_6, A0     ; A0 = @t_6
            LEA.L       t_7, A1     ; A1 = @t_7
            LEA.L       t_8, A2     ; FETCH t_8
            JSR         CONCATENAR  
; --------- param_s t_8             
            MOVE.L      t_8, -(SP)  ; PUSH INTO STACK t_8
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- rtn e_inicio            
            RTS                     ; RETURN TO SUBROUTINE e_inicio




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
            END CALC2               ; Fin del programa
