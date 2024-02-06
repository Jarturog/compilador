; ==============================================================================
; TITLE       : persona
; COMPILED BY : jartu
; COMPILER BY : Juan Arturo Abaurrea Calafell
;               Dani Salanova Dmitriyev
;               Marta González Juan
; ==============================================================================

            ORG         $1000       ; Origen

persona:    DS.L 1                  ; Inicializando el miembro id de la tupla persona
            DS.B 1                  ; Inicializando el miembro let de la tupla persona
t           DC.L 1                  ; INT
t_1         DC.B 'a'                ; CHAR
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_2         DC.L 2                  ; INT
t_3         DC.B 'b'                ; CHAR
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_4         DC.B 'hola'             ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_5         DC.L 4                  ; INT
t_7         DC.L 4                  ; INT
t_9         DC.L 0                  ; INT
t_10        DC.L 4                  ; INT
p1          DS.B 5                  ; COPY OF ARRAY OR TUPLE
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
p2          DS.B 5                  ; COPY OF ARRAY OR TUPLE
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
aux         DS.L 1                  ; STRING
idpersona   DS.L 1                  ; INT
ox          DS.B 1                  ; CHAR
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_6         DS.B 1                  ; CHAR
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_8         DS.B 1                  ; CHAR
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
p           DS.L 1                  ; POINTER TO ARRAY OR TUPLE
let         DS.B 1                  ; CHAR
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
e_show      MOVEA.L     4(SP), A1   ; A1 = POP FROM STACK
            MOVE.L      #13, D0     ; Task 13 of TRAP 15: Display the NULL terminated string pointed to by (A1) with CR, LF
            TRAP        #15         ; Interruption generated
            RTS                     ; RETURN TO SUBROUTINE ...
initpersona LEA.L       persona, A0 ; load persona into A0
            MOVEA.L     4(SP), A1   ; load instance into A1
            MOVE.L      #1, D0      
nextlongwordpersona CMP.L       #0, D0 
            BEQ         endinitpersona 
            MOVE.L      (A0)+, (A1)+ ; copy 4 bytes
            SUB.L       #1, D0      
            JMP         nextlongwordpersona 
endinitpersona                      ; end of loop
            MOVE.B      (A0)+, (A1)+ ; copy 1 byte
            RTS                     

persona0                            ; Etiqueta inicial (main)

            MOVE.L      #32, D0     ; Task 32 of TRAP 15: Hardware/Simulator
            MOVE.B      #5, D1      ; Enable exception processing (for input/output)
            TRAP        #15         ; Interruption generated
            LEA.L       p1, A0      
            MOVE.L      A0, -(SP)   
            JSR         initpersona 
            ADDA.L      #4, SP      

            LEA.L       p2, A0      
            MOVE.L      A0, -(SP)   
            JSR         initpersona 
            ADDA.L      #4, SP      

            JSR         e_inicio    ; Se ejecuta el main
            SIMHALT                 ; Fin de la ejecución

; --------- e_inicio: skip          
                                    
; --------- pmb e_inicio            
; --------- param_s p1              
e_inicio:   LEA.L       p1, A0      ; A0 = p1
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- t = 1                   
            MOVE.L      #1, D0      ; D0 = #1
            MOVE.L      D0, t       ; t = D0
; --------- param_s t               
            MOVE.L      t, D0       ; D0 = t
            MOVE.L      D0, -(SP)   ; PUSH INTO STACK D0
; --------- t_1 = a                 
            CLR.L       D0          ; CLEAR D0
            MOVE.B      #'a', D0    ; D0 = #'a'
            MOVE.B      D0, t_1     ; t_1 = D0
; --------- param_s t_1             
            CLR.L       D0          ; CLEAR D0
            MOVE.B      t_1, D0     ; D0 = t_1
            MOVE.L      D0, -(SP)   ; PUSH INTO STACK D0
; --------- call e_nuevapersona     
            JSR         e_nuevapersona ; JUMP TO SUBROUTINE e_nuevapersona
            ADDA.L      #12, SP     ; SP = SP + 12
; --------- param_s p2              
            LEA.L       p2, A0      ; A0 = p2
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- t_2 = 2                 
            MOVE.L      #2, D0      ; D0 = #2
            MOVE.L      D0, t_2     ; t_2 = D0
; --------- param_s t_2             
            MOVE.L      t_2, D0     ; D0 = t_2
            MOVE.L      D0, -(SP)   ; PUSH INTO STACK D0
; --------- t_3 = b                 
            CLR.L       D0          ; CLEAR D0
            MOVE.B      #'b', D0    ; D0 = #'b'
            MOVE.B      D0, t_3     ; t_3 = D0
; --------- param_s t_3             
            CLR.L       D0          ; CLEAR D0
            MOVE.B      t_3, D0     ; D0 = t_3
            MOVE.L      D0, -(SP)   ; PUSH INTO STACK D0
; --------- call e_nuevapersona     
            JSR         e_nuevapersona ; JUMP TO SUBROUTINE e_nuevapersona
            ADDA.L      #12, SP     ; SP = SP + 12
; --------- t_4 = hola              
; --------- aux = t_4               
            LEA.L       t_4, A0     ; A0 = t_4
            MOVE.L      A0, aux     ; aux = A0
; --------- param_s aux             
            LEA.L       aux, A0     ; A0 = aux
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- t_5 = 4                 
            MOVE.L      #4, D0      ; D0 = #4
            MOVE.L      D0, t_5     ; t_5 = D0
; --------- t_6 = p1[t_5]           
            MOVEA.L     p1, A0      ; A0 = p1
            ADDA.L      t_5, A0     ; A0 = A0 + t_5
            MOVE.L      (A0), t_6   ; t_6 = (A0)
; --------- ox = t_6                
            CLR.L       D0          ; CLEAR D0
            MOVE.B      t_6, D0     ; D0 = t_6
            MOVE.B      D0, ox      ; ox = D0
; --------- param_s ox              
            LEA.L       ox, A0      ; A0 = ox
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- t_7 = 4                 
            MOVE.L      #4, D0      ; D0 = #4
            MOVE.L      D0, t_7     ; t_7 = D0
; --------- t_8 = p2[t_7]           
            MOVEA.L     p2, A0      ; A0 = p2
            ADDA.L      t_7, A0     ; A0 = A0 + t_7
            MOVE.L      (A0), t_8   ; t_8 = (A0)
; --------- param_s t_8             
            LEA.L       t_8, A0     ; A0 = t_8
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- rtn e_inicio            
            RTS                     ; RETURN TO SUBROUTINE e_inicio




; --------- e_nuevapersona: skip    
                                    
; --------- pmb e_nuevapersona      
e_nuevapersona: MOVE.L      4(SP), D0 ; D0 = POP FROM STACK
            MOVE.B      D0, let     ; let = D0
            MOVE.L      8(SP), D0   ; D0 = POP FROM STACK
            MOVE.L      D0, idpersona ; idpersona = D0
            MOVE.L      12(SP), D0  ; D0 = POP FROM STACK
            MOVE.L      D0, p       ; p = D0
; --------- t_9 = 0                 
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_9     ; t_9 = D0
; --------- p[t_9] = idpersona      
            MOVE.L      idpersona, D0 ; D0 = idpersona
            MOVE.L      t_9, D1     ; D1 = t_9
            LEA.L       p, A0       ; A0 = p
            CLR.L       D2          
            DIVS.W      #4, D0      ; D0.h = D0 % 4. D0.l = D0 / 4
            MOVE.W      D0, D2      
            LSR.L       #8, D0      ; D0.l = D0.h
            LSR.L       #8, D0      ; D0.l = D0.h
            ADDA.L      D2, A0      ; A0 = A0 + D2
            MOVE.L      (A0), D3    ; D3 = (A0)
            MOVE.L      #$FFFFFFFF, D4 ; MASK
mask        CMP.W       #0, D0      ; .W porque no hace falta más
            BEQ         endmask     
            LSR.L       #8, D4      
            SUB.W       #1, D0      
            JMP         mask        
endmask     AND.L       D4, D3      ; MASK
            MOVE.L      D3, (A0)    ; (A0) = D3
; --------- t_10 = 4                
            MOVE.L      #4, D0      ; D0 = #4
            MOVE.L      D0, t_10    ; t_10 = D0
; --------- p[t_10] = let           
            CLR.L       D0          ; CLEAR D0
            MOVE.B      let, D0     ; D0 = let
            MOVE.L      t_10, D1    ; D1 = t_10
            LEA.L       p, A0       ; A0 = p
            CLR.L       D2          
            DIVS.W      #4, D0      ; D0.h = D0 % 4. D0.l = D0 / 4
            MOVE.W      D0, D2      
            LSR.L       #8, D0      ; D0.l = D0.h
            LSR.L       #8, D0      ; D0.l = D0.h
            ADDA.L      D2, A0      ; A0 = A0 + D2
            MOVE.L      (A0), D3    ; D3 = (A0)
            MOVE.L      #$FFFFFFFF, D4 ; MASK
mask0       CMP.W       #0, D0      ; .W porque no hace falta más
            BEQ         endmask0    
            LSR.L       #8, D4      
            SUB.W       #1, D0      
            JMP         mask0       
endmask0    AND.L       D4, D3      ; MASK
            MOVE.L      D3, (A0)    ; (A0) = D3
; --------- rtn e_nuevapersona      
            RTS                     ; RETURN TO SUBROUTINE e_nuevapersona


            END persona0            ; Fin del programa


