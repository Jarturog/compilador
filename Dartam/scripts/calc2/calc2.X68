; ==============================================================================
; TITLE       : calc2
; COMPILED BY : jartu
; COMPILER BY : Juan Arturo Abaurrea Calafell
;               Dani Salanova Dmitriyev
;               Marta González Juan
; ==============================================================================

            ORG         $1000       ; Origen

t           DC.L 97                 
t_2         DC.B 'hola',0           
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
argumentos  DS.L 1                  
t           DS.L 1                  
t_2         DS.L 1                  
t_1         DS.L 1                  
t_3         DS.L 1                  
CALC2                               ; Etiqueta inicial (main)

            JSR         e_inicio    ; Se ejecuta el main
            SIMHALT                 ; Fin de la ejecución

; --------- e_inicio: skip          
; --------- pmb e_inicio            
; --------- t_1 = t                 
e_inicio:   LEA.L       t, A0       ; A0 = @t
            MOVE.L      A0, t_1     ; t_1 = A0
; --------- t_3 = t_1 concat t_2    
            LEA.L       t_1, A0     ; A0 = @t_1
            LEA.L       t_2, A1     ; A1 = @t_2
            LEA.L       t_3, A2     ; FETCH t_3
            JSR         CONCATENAR  
; --------- param_s t_3             
            MOVE.L      t_3, -(SP)  ; PUSH INTO STACK t_3
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
