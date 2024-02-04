; ==============================================================================
; TITLE       : persona
; COMPILED BY : jartu
; COMPILER BY : Juan Arturo Abaurrea Calafell
;               Dani Salanova Dmitriyev
;               Marta González Juan
; ==============================================================================

            ORG         $1000       ; Origen

t           DC.L 1                  
t_1         DC.B 'paco',0           
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_2         DC.L 2                  
t_3         DC.B 'pepa',0           
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_4         DC.L 0                  
t_5         DC.L 4                  
p1          DS.L 1                  
p2          DS.L 1                  
persona     DS.L 1                  
argumentos  DS.L 1                  
t_2         DS.L 1                  
t_1         DS.L 1                  
t_4         DS.L 1                  
t_3         DS.L 1                  
nombrePersona DS.L 1                
t_5         DS.L 1                  
p           DS.L 1                  
t           DS.L 1                  
idPersona   DS.L 1                  
PERSONA                             ; Etiqueta inicial (main)

            JSR         e_inicio    ; Se ejecuta el main
            SIMHALT                 ; Fin de la ejecución

; --------- e_inicio: skip          
; --------- pmb e_inicio            
; --------- param_s p1              
e_inicio:   MOVE.L      p1, -(SP)   ; PUSH INTO STACK p1
; --------- param_s t               
            MOVE.L      t, -(SP)    ; PUSH INTO STACK t
; --------- param_s t_1             
            MOVE.L      t_1, -(SP)  ; PUSH INTO STACK t_1
; --------- call e_nuevaPersona     
            JSR         e_nuevaPersona ; JUMP TO SUBROUTINE e_nuevaPersona
            ADDA.L      #12, SP     ; SP = SP + 12
; --------- param_s p2              
            MOVE.L      p2, -(SP)   ; PUSH INTO STACK p2
; --------- param_s t_2             
            MOVE.L      t_2, -(SP)  ; PUSH INTO STACK t_2
; --------- param_s t_3             
            MOVE.L      t_3, -(SP)  ; PUSH INTO STACK t_3
; --------- call e_nuevaPersona     
            JSR         e_nuevaPersona ; JUMP TO SUBROUTINE e_nuevaPersona
            ADDA.L      #12, SP     ; SP = SP + 12
; --------- rtn e_inicio            
            RTS                     ; RETURN TO SUBROUTINE e_inicio




; --------- e_nuevaPersona: skip    
; --------- pmb e_nuevaPersona      
e_nuevaPersona: MOVE.L      4(SP), p ; p = POP FROM STACK
            MOVE.L      8(SP), idPersona ; idPersona = POP FROM STACK
            MOVE.L      12(SP), nombrePersona ; nombrePersona = POP FROM STACK
; --------- p[t_4] = idPersona      
            MOVEA.L     p, A0       ; A0 = p
            ADDA.L      idPersona, A0 ; A0 = A0 + idPersona
            MOVE.L      t_4, (A0)   ; (A0) = t_4
; --------- p[t_5] = nombrePersona  
            MOVEA.L     p, A0       ; A0 = p
            ADDA.L      nombrePersona, A0 ; A0 = A0 + nombrePersona
            MOVE.L      t_5, (A0)   ; (A0) = t_5
; --------- rtn e_nuevaPersona      
            RTS                     ; RETURN TO SUBROUTINE e_nuevaPersona


            END PERSONA             ; Fin del programa
