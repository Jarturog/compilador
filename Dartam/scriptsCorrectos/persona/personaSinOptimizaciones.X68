; ==============================================================================
; TITLE       : persona
; COMPILED BY : jartu
; COMPILER BY : Juan Arturo Abaurrea Calafell
;               Dani Salanova Dmitriyev
;               Marta González Juan
; ==============================================================================

            ORG         $1000       ; Origen

persona:    DS.L 1                  ; Inicializando el miembro id de la tupla persona
            DS.L 1                  ; Inicializando el miembro inicial de la tupla persona
            DC.L 0                  ; Inicializando el miembro hambre de la tupla persona
t           DC.L 81                 ; INT
t_1         DC.L 0                  ; INT
t_2         DC.L 1                  ; INT
t_3         DC.L 0                  ; BOOLEAN
t_4         DC.L 81                 ; INT
t_5         DC.L 1                  ; INT
t_7         DC.B 'prueba.txt'       ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
errread     DC.B 'Error de lectura',0  ; Mensaje de error
            DS.W 0                  ; Para evitar imparidad
t_8         DC.L 0                  ; INT
t_9         DC.L 4                  ; INT
idpersona   DS.L 1                  ; INT
str_1       DS.B 256                ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
i           DS.L 1                  ; INT
t_6         DS.L 1                  ; INT
inicial     DS.L 1                  ; CHAR
p           DS.L 1                  ; POINTER TO ARRAY, TUPLE OR STRING
str         DS.B 256                ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
d_str       DS.L 1                  ; INT
d_str_1     DS.L 1                  ; INT

; --------- SUBROUTINES             
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
e_show      MOVEA.L     4(SP), A1   ; A1 = POP FROM STACK
            MOVE.L      #13, D0     ; Task 13 of TRAP 15: Display the NULL terminated string pointed to by (A1) with CR, LF
            TRAP        #15         ; Interruption generated
            RTS                     ; RETURN TO SUBROUTINE ...
initpersona LEA.L       persona, A0 ; load persona into A0
            MOVEA.L     4(SP), A1   ; load instance into A1
            MOVE.L      #3, D0      
nextlongwordpersona CMP.L       #0, D0 
            BEQ         endinitpersona 
            MOVE.L      (A0)+, (A1)+ ; copy 4 bytes
            SUB.L       #1, D0      
            JMP         nextlongwordpersona 
endinitpersona                      ; end of loop
            RTS                     


; --------- INITIALIZATION          
persona0                            ; Etiqueta inicial (main)

            MOVE.L      #32, D0     ; Task 32 of TRAP 15: Hardware/Simulator
            MOVE.B      #5, D1      ; Enable exception processing (for input/output)
            TRAP        #15         ; Interruption generated
            JSR         e_inicio    ; Se ejecuta el main
            SIMHALT                 ; Fin de la ejecución

; --------- e_inicio: skip          
                                    
; --------- pmb e_inicio            
; --------- t = 81                  
e_inicio:   MOVE.L      #81, D0     ; D0 = #81
            MOVE.L      D0, t       ; t = D0
; --------- d_str = t               
            MOVE.L      t, D0       ; D0 = t
            MOVE.L      D0, d_str   ; d_str = D0
; --------- t_1 = 0                 
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_1     ; t_1 = D0
; --------- i = t_1                 
            MOVE.L      t_1, D0     ; D0 = t_1
            MOVE.L      D0, i       ; i = D0
; --------- e: skip                 
                                    
; --------- t_2 = 1                 
.e:         MOVE.L      #1, D0      ; D0 = #1
            MOVE.L      D0, t_2     ; t_2 = D0
; --------- if i < t_2 goto e_3     
            MOVE.L      i, D0       ; D0 = i
            MOVE.L      t_2, D1     ; D1 = t_2
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BLT         .e_3        ; IF (N XOR V) FLAGS = 1 GOTO .e_3
; --------- t_3 = 0                 
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_3     ; t_3 = D0
; --------- goto e_4                
            JMP         .e_4        ; goto .e_4
; --------- e_3: skip               
                                    
; --------- t_3 = -1                
.e_3:       MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_3     ; t_3 = D0
; --------- e_4: skip               
                                    
; --------- if t_3 == 0 goto e_1    
.e_4:       MOVE.L      t_3, D0     ; D0 = t_3
            MOVE.L      #0, D1      ; D1 = #0
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_1        ; IF Z FLAG = 1 GOTO .e_1
; --------- t_4 = 81                
            MOVE.L      #81, D0     ; D0 = #81
            MOVE.L      D0, t_4     ; t_4 = D0
; --------- d_str_1 = t_4           
            MOVE.L      t_4, D0     ; D0 = t_4
            MOVE.L      D0, d_str_1 ; d_str_1 = D0
; --------- e_2: skip               
                                    
; --------- t_5 = 1                 
.e_2:       MOVE.L      #1, D0      ; D0 = #1
            MOVE.L      D0, t_5     ; t_5 = D0
; --------- t_6 = i + t_5           
            MOVE.L      i, D0       ; D0 = i
            MOVE.L      t_5, D1     ; D1 = t_5
            ADD.L       D0, D1      ; D1 = D1 + D0
            MOVE.L      D1, t_6     ; t_6 = D1
; --------- i = t_6                 
            MOVE.L      t_6, D0     ; D0 = t_6
            MOVE.L      D0, i       ; i = D0
; --------- goto e                  
            JMP         .e          ; goto .e
; --------- e_1: skip               
                                    
; --------- t_7 = prueba.txt        
; --------- param_s t_7             
.e_1:       LEA.L       t_7, A0     ; A0 = t_7
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- param_s str             
            LEA.L       str, A0     ; A0 = str
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_from             
            JSR         e_from      ; JUMP TO SUBROUTINE e_from
            ADDA.L      #8, SP      ; SP = SP + 8
; --------- param_s str             
            LEA.L       str, A0     ; A0 = str
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- rtn e_inicio            
            RTS                     ; RETURN TO SUBROUTINE e_inicio




; --------- e_nuevapersona: skip    
                                    
; --------- pmb e_nuevapersona      
e_nuevapersona: MOVE.L      4(SP), D0 ; D0 = POP FROM STACK
            MOVE.L      D0, inicial ; inicial = D0
            MOVE.L      8(SP), D0   ; D0 = POP FROM STACK
            MOVE.L      D0, idpersona ; idpersona = D0
            MOVE.L      12(SP), D0  ; D0 = POP FROM STACK
            MOVE.L      D0, p       ; p = D0
; --------- t_8 = 0                 
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_8     ; t_8 = D0
; --------- p[t_8] = idpersona      
            MOVE.L      idpersona, D0 ; D0 = idpersona
            MOVE.L      t_8, D1     ; D1 = t_8
            MOVEA.L     p, A0       ; A0 = p
            ADDA.L      D1, A0      ; A0 = A0 + D1
            MOVE.L      D0, (A0)    ; (A0) = D0
; --------- t_9 = 4                 
            MOVE.L      #4, D0      ; D0 = #4
            MOVE.L      D0, t_9     ; t_9 = D0
; --------- p[t_9] = inicial        
            MOVE.L      inicial, D0 ; D0 = inicial
            MOVE.L      t_9, D1     ; D1 = t_9
            MOVEA.L     p, A0       ; A0 = p
            ADDA.L      D1, A0      ; A0 = A0 + D1
            MOVE.L      D0, (A0)    ; (A0) = D0
; --------- rtn e_nuevapersona      
            RTS                     ; RETURN TO SUBROUTINE e_nuevapersona


            END persona0            ; Fin del programa


