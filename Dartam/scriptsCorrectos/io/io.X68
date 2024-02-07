; ==============================================================================
; TITLE       : io
; COMPILED BY : jartu
; COMPILER BY : Juan Arturo Abaurrea Calafell
;               Dani Salanova Dmitriyev
;               Marta González Juan
; ==============================================================================

            ORG         $1000       ; Origen

t           DC.B 'Escribe el fichero a leer:'  ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
d_file      DC.L 81                 ; INT
d_content   DC.L 81                 ; INT
errread     DC.B 'Error de lectura',0  ; Mensaje de error
            DS.W 0                  ; Para evitar imparidad
t_3         DC.B 'El fichero contiene:'  ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_4         DC.B 'Ahora escribe en el fichero:'  ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
errwrite    DC.B 'Error de escritura',0  ; Mensaje de error
            DS.W 0                  ; Para evitar imparidad
errreadonly DC.B 'El fichero solo permite lectura',0  ; Mensaje de error
            DS.W 0                  ; Para evitar imparidad
t_5         DC.B 'Ahora el fichero contiene:'  ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
file        DS.B 256                ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
content     DS.B 256                ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar

; --------- SUBROUTINES             
e_show      MOVEA.L     4(SP), A1   ; A1 = POP FROM STACK
            MOVE.L      #13, D0     ; Task 13 of TRAP 15: Display the NULL terminated string pointed to by (A1) with CR, LF
            TRAP        #15         ; Interruption generated
            RTS                     ; RETURN TO SUBROUTINE ...
e_scan      MOVEA.L     4(SP), A1   ; A1 = POP FROM STACK
            MOVE.L      #2, D0      ; Read string from keyboard and store at (A1), NULL (0) terminated, length returned in D1.W (max 80)
            TRAP        #15         ; Interruption generated
            RTS                     ; RETURN TO SUBROUTINE ...
e_from      MOVEA.L     8(SP), A1   ; Pre: (A1) null terminated file name
            MOVEA.L     4(SP), A2   ; File information -> A2
            MOVE.L      #51, D0     ; Task 51 of TRAP 15: Open existing file
            TRAP        #15         ; Interruption generated
            CMP.W       #2, D0      ; Si error
            BEQ         e_from0     ; Fin
            MOVE.L      #256, D2    
            MOVE.L      #53, D0     ; Task 53 of TRAP 15: Read file
            MOVEA.L     A2, A1      
            TRAP        #15         ; Interruption generated
            CMP.W       #1, D0      ; Si vacío
            BNE         emptyfile   ; Fin
            CMP.W       #256, D2    ; Si vacío
            BNE         emptyfile   ; Fin
            CLR.L       D2          
emptyfile   MOVEA.L     A2, A1      
            ADDA.L      D2, A1      
            MOVE.B      #0, (A1)    
            MOVE.L      #56, D0     ; Task 56 of TRAP 15: Close file
            TRAP        #15         ; Interruption generated
            RTS                     ; RETURN TO SUBROUTINE ...
e_from0:    LEA.L       errread, A1 ; A1 = mnsj error
            MOVE.L      #13, D0     ; Task 13 of TRAP 15: Display the NULL terminated string pointed to by (A1) with CR, LF
            TRAP        #15         ; Interruption generated
            RTS                     ; RETURN TO SUBROUTINE ...
e_into      MOVEA.L     8(SP), A1   ; Pre: (A1) null terminated file name
            MOVEA.L     4(SP), A2   ; A2: pop
            MOVE.L      #52, D0     ; Task 51 of TRAP 15: Open existing file
            TRAP        #15         ; Interruption generated
            CMP.W       #2, D0      ; Si error
            BEQ         e_into0     ; Fin
            CMP.W       #3, D0      ; Si error
            BEQ         e_into1     ; error, read only
            CLR.L       D2          
e_into2     MOVEA.L     A2, A1      
            ADDA.L      D2, A1      
            CMP.B       #0, (A1)    
            BEQ         e_into3     
            ADD.L       #1, D2      
            JMP         e_into2     
e_into3     MOVEA.L     A2, A1      
            MOVE.L      #54, D0     ; Task 54 of TRAP 15: Write file
            TRAP        #15         ; Interruption generated
            MOVE.L      #56, D0     ; Task 56 of TRAP 15: Close file
            TRAP        #15         ; Interruption generated
            RTS                     ; RETURN TO SUBROUTINE ...
e_into0:    LEA.L       errwrite, A1 ; A1 = mnsj error
            MOVE.L      #13, D0     ; Task 13 of TRAP 15: Display the NULL terminated string pointed to by (A1) with CR, LF
            TRAP        #15         ; Interruption generated
            RTS                     ; RETURN TO SUBROUTINE ...
e_into1:    LEA.L       errreadonly, A1 ; A1 = mnsj error
            MOVE.L      #13, D0     ; Task 13 of TRAP 15: Display the NULL terminated string pointed to by (A1) with CR, LF
            TRAP        #15         ; Interruption generated
            RTS                     ; RETURN TO SUBROUTINE ...

; --------- INITIALIZATION          
io                                  ; Etiqueta inicial (main)

            MOVE.L      #32, D0     ; Task 32 of TRAP 15: Hardware/Simulator
            MOVE.B      #5, D1      ; Enable exception processing (for input/output)
            TRAP        #15         ; Interruption generated
            JSR         e_inicio    ; Se ejecuta el main
            SIMHALT                 ; Fin de la ejecución

; --------- e_inicio: skip          
                                    
; --------- pmb e_inicio            
; --------- t = Escribe el fichero a leer:  
; --------- param_s t               
e_inicio:   LEA.L       t, A0       ; A0 = t
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- d_file = 81             
            MOVE.L      #81, D0     ; D0 = #81
            MOVE.L      D0, d_file  ; d_file = D0
; --------- d_content = 81          
            MOVE.L      #81, D0     ; D0 = #81
            MOVE.L      D0, d_content ; d_content = D0
; --------- param_s file            
            LEA.L       file, A0    ; A0 = file
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_scan             
            JSR         e_scan      ; JUMP TO SUBROUTINE e_scan
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- param_s file            
            LEA.L       file, A0    ; A0 = file
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- param_s content         
            LEA.L       content, A0 ; A0 = content
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_from             
            JSR         e_from      ; JUMP TO SUBROUTINE e_from
            ADDA.L      #8, SP      ; SP = SP + 8
; --------- t_3 = El fichero contiene:  
; --------- param_s t_3             
            LEA.L       t_3, A0     ; A0 = t_3
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- param_s content         
            LEA.L       content, A0 ; A0 = content
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- t_4 = Ahora escribe en el fichero:  
; --------- param_s t_4             
            LEA.L       t_4, A0     ; A0 = t_4
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- param_s content         
            LEA.L       content, A0 ; A0 = content
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_scan             
            JSR         e_scan      ; JUMP TO SUBROUTINE e_scan
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- param_s file            
            LEA.L       file, A0    ; A0 = file
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- param_s content         
            LEA.L       content, A0 ; A0 = content
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_into             
            JSR         e_into      ; JUMP TO SUBROUTINE e_into
            ADDA.L      #8, SP      ; SP = SP + 8
; --------- t_5 = Ahora el fichero contiene:  
; --------- param_s t_5             
            LEA.L       t_5, A0     ; A0 = t_5
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- param_s file            
            LEA.L       file, A0    ; A0 = file
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- param_s content         
            LEA.L       content, A0 ; A0 = content
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_from             
            JSR         e_from      ; JUMP TO SUBROUTINE e_from
            ADDA.L      #8, SP      ; SP = SP + 8
; --------- param_s content         
            LEA.L       content, A0 ; A0 = content
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- rtn e_inicio            
            RTS                     ; RETURN TO SUBROUTINE e_inicio




            END io                  ; Fin del programa


