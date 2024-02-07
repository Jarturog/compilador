; ==============================================================================
; TITLE       : persona
; COMPILED BY : jartu
; COMPILER BY : Juan Arturo Abaurrea Calafell
;               Dani Salanova Dmitriyev
;               Marta González Juan
; ==============================================================================

            ORG         $1000       ; Origen

persona:    DS.L 1                  ; Inicializando el miembro id de la tupla persona
            DS.L 1                  ; Inicializando el miembro let de la tupla persona
i           DC.L 0                  ; INT
t_1         DC.L 3                  ; INT
t_2         DC.L 0                  ; BOOLEAN
d_str       DC.L 81                 ; INT
t_4         DC.L 1                  ; INT
t_6         DC.L 0                  ; INT
t_7         DC.L 4                  ; INT
idpersona   DS.L 1                  ; INT
t_5         DS.L 1                  ; INT
p           DS.L 1                  ; POINTER TO ARRAY, TUPLE OR STRING
str         DS.B 256                ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
let         DS.L 1                  ; CHAR

; --------- SUBROUTINES             
e_scan      MOVEA.L     4(SP), A1   ; A1 = POP FROM STACK
            MOVE.L      #2, D0      ; Read string from keyboard and store at (A1), NULL (0) terminated, length returned in D1.W (max 80)
            TRAP        #15         ; Interruption generated
            RTS                     ; RETURN TO SUBROUTINE ...
e_show      MOVEA.L     4(SP), A1   ; A1 = POP FROM STACK
            MOVE.L      #13, D0     ; Task 13 of TRAP 15: Display the NULL terminated string pointed to by (A1) with CR, LF
            TRAP        #15         ; Interruption generated
            RTS                     ; RETURN TO SUBROUTINE ...
initpersona LEA.L       persona, A0 ; load persona into A0
            MOVEA.L     4(SP), A1   ; load instance into A1
            MOVE.L      #2, D0      
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

            MOVE.L      #62, D0     ; Task 62 of TRAP 15: Enable/Disable keyboard IRQ
            MOVE.W      #$0103, D1  ; Enable keyboard IRQ level 1 for key up and key down
            TRAP        #15         ; Interruption generated
            JSR         e_inicio    ; Se ejecuta el main
            SIMHALT                 ; Fin de la ejecución

; --------- e_inicio: skip          
                                    
; --------- pmb e_inicio            
; --------- i = 0                   
e_inicio:   MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, i       ; i = D0
; --------- e: skip                 
                                    
; --------- t_1 = 3                 
.e:         MOVE.L      #3, D0      ; D0 = #3
            MOVE.L      D0, t_1     ; t_1 = D0
; --------- if i < t_1 goto e_3     
            MOVE.L      i, D0       ; D0 = i
            CMP.L       t_1, D0     ; UPDATE FLAGS WITH D0 - t_1
            BLT         .e_3        ; IF (N XOR V) FLAGS = 1 GOTO .e_3
; --------- t_2 = 0                 
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_2     ; t_2 = D0
; --------- goto e_4                
            JMP         .e_4        ; goto .e_4
; --------- e_3: skip               
                                    
; --------- t_2 = -1                
.e_3:       MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_2     ; t_2 = D0
; --------- e_4: skip               
                                    
; --------- if t_2 == 0 goto e_1    
.e_4:       MOVE.L      t_2, D0     ; D0 = t_2
            CMP.L       #0, D0      ; UPDATE FLAGS WITH D0 - #0
            BEQ         .e_1        ; IF Z FLAG = 1 GOTO .e_1
; --------- d_str = 81              
            MOVE.L      #81, D0     ; D0 = #81
            MOVE.L      D0, d_str   ; d_str = D0
; --------- param_s str             
            LEA.L       str, A0     ; A0 = str
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_scan             
            JSR         e_scan      ; JUMP TO SUBROUTINE e_scan
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- param_s str             
            LEA.L       str, A0     ; A0 = str
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- t_4 = 1                 
            MOVE.L      #1, D0      ; D0 = #1
            MOVE.L      D0, t_4     ; t_4 = D0
; --------- t_5 = i + t_4           
            MOVE.L      i, D0       ; D0 = i
            MOVE.L      t_4, D1     ; D1 = t_4
            ADD.L       D0, D1      ; D1 = D1 + D0
            MOVE.L      D1, t_5     ; t_5 = D1
; --------- i = t_5                 
            MOVE.L      t_5, D0     ; D0 = t_5
            MOVE.L      D0, i       ; i = D0
; --------- goto e                  
            JMP         .e          ; goto .e
; --------- e_1: skip               
                                    
; --------- rtn e_inicio            
.e_1:       RTS                     ; RETURN TO SUBROUTINE e_inicio




; --------- e_nuevapersona: skip    
                                    
; --------- pmb e_nuevapersona      
e_nuevapersona: MOVE.L      4(SP), D0 ; D0 = POP FROM STACK
            MOVE.L      D0, let     ; let = D0
            MOVE.L      8(SP), D0   ; D0 = POP FROM STACK
            MOVE.L      D0, idpersona ; idpersona = D0
            MOVE.L      12(SP), D0  ; D0 = POP FROM STACK
            MOVE.L      D0, p       ; p = D0
; --------- t_6 = 0                 
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_6     ; t_6 = D0
; --------- p[t_6] = idpersona      
            MOVE.L      idpersona, D0 ; D0 = idpersona
            MOVE.L      t_6, D1     ; D1 = t_6
            MOVEA.L     p, A0       ; A0 = p
            ADDA.L      D1, A0      ; A0 = A0 + D1
            MOVE.L      D0, (A0)    ; (A0) = D0
; --------- t_7 = 4                 
            MOVE.L      #4, D0      ; D0 = #4
            MOVE.L      D0, t_7     ; t_7 = D0
; --------- p[t_7] = let            
            MOVE.L      let, D0     ; D0 = let
            MOVE.L      t_7, D1     ; D1 = t_7
            MOVEA.L     p, A0       ; A0 = p
            ADDA.L      D1, A0      ; A0 = A0 + D1
            MOVE.L      D0, (A0)    ; (A0) = D0
; --------- rtn e_nuevapersona      
            RTS                     ; RETURN TO SUBROUTINE e_nuevapersona


            END persona0            ; Fin del programa


