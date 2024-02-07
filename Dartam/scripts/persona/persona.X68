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
t           DC.L 1                  ; INT
t_1         DC.L 'a'                ; CHAR
t_2         DC.L 4                  ; INT
i           DC.L 0                  ; INT
t_7         DC.L 3                  ; INT
t_8         DC.L 0                  ; BOOLEAN
t_9         DC.L 4                  ; INT
t_12        DC.L 1                  ; INT
t_14        DC.L 4                  ; INT
t_15        DC.L 4                  ; INT
t_19        DC.L 1                  ; INT
t_21        DC.L 4                  ; INT
t_25        DC.L 0                  ; INT
t_26        DC.L 4                  ; INT
p1          DS.B 8                  ; TUPLE
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
p2          DS.B 8                  ; TUPLE
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
let         DS.L 1                  ; CHAR
t_24        DS.L 1                  ; CHAR
t_22        DS.L 1                  ; CHAR
t_20        DS.L 1                  ; INT
t_18        DS.L 1                  ; CHAR
t_16        DS.L 1                  ; CHAR
idpersona   DS.L 1                  ; INT
t_3         DS.L 1                  ; CHAR
t_5         DS.L 1                  ; CHAR
p           DS.L 1                  ; POINTER TO ARRAY, TUPLE OR STRING
t_13        DS.L 1                  ; CHAR
t_10        DS.L 1                  ; CHAR

; --------- SUBROUTINES             
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
            MOVE.L      #'a', D0    ; D0 = #'a'
            MOVE.L      D0, t_1     ; t_1 = D0
; --------- param_s t_1             
            MOVE.L      t_1, D0     ; D0 = t_1
            MOVE.L      D0, -(SP)   ; PUSH INTO STACK D0
; --------- call e_nuevapersona     
            JSR         e_nuevapersona ; JUMP TO SUBROUTINE e_nuevapersona
            ADDA.L      #12, SP     ; SP = SP + 12
; --------- t_2 = 4                 
            MOVE.L      #4, D0      ; D0 = #4
            MOVE.L      D0, t_2     ; t_2 = D0
; --------- t_3 = p1[t_2]           
            LEA.L       p1, A0      ; A0 = p1
            ADDA.L      t_2, A0     ; A0 = A0 + t_2
            MOVE.L      (A0), t_3   ; t_3 = (A0)
; --------- t_5 = t_3               
            MOVE.L      t_3, D0     ; D0 = t_3
            MOVE.L      D0, t_5     ; t_5 = D0
; --------- param_s t_5             
            LEA.L       t_5, A0     ; A0 = t_5
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- i = 0                   
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, i       ; i = D0
; --------- e: skip                 
                                    
; --------- t_7 = 3                 
.e:         MOVE.L      #3, D0      ; D0 = #3
            MOVE.L      D0, t_7     ; t_7 = D0
; --------- if i < t_7 goto e_3     
            MOVE.L      i, D0       ; D0 = i
            CMP.L       t_7, D0     ; UPDATE FLAGS WITH D0 - t_7
            BLT         .e_3        ; IF (N XOR V) FLAGS = 1 GOTO .e_3
; --------- t_8 = 0                 
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_8     ; t_8 = D0
; --------- goto e_4                
            JMP         .e_4        ; goto .e_4
; --------- e_3: skip               
                                    
; --------- t_8 = -1                
.e_3:       MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_8     ; t_8 = D0
; --------- e_4: skip               
                                    
; --------- if t_8 == 0 goto e_1    
.e_4:       MOVE.L      t_8, D0     ; D0 = t_8
            CMP.L       #0, D0      ; UPDATE FLAGS WITH D0 - #0
            BEQ         .e_1        ; IF Z FLAG = 1 GOTO .e_1
; --------- t_9 = 4                 
            MOVE.L      #4, D0      ; D0 = #4
            MOVE.L      D0, t_9     ; t_9 = D0
; --------- t_10 = p1[t_9]          
            LEA.L       p1, A0      ; A0 = p1
            ADDA.L      t_9, A0     ; A0 = A0 + t_9
            MOVE.L      (A0), t_10  ; t_10 = (A0)
; --------- t_12 = 1                
            MOVE.L      #1, D0      ; D0 = #1
            MOVE.L      D0, t_12    ; t_12 = D0
; --------- t_13 = t_10 + t_12      
            MOVE.L      t_10, D0    ; D0 = t_10
            MOVE.L      t_12, D1    ; D1 = t_12
            ADD.L       D0, D1      ; D1 = D1 + D0
            MOVE.L      D1, t_13    ; t_13 = D1
; --------- t_14 = 4                
            MOVE.L      #4, D0      ; D0 = #4
            MOVE.L      D0, t_14    ; t_14 = D0
; --------- p1[t_14] = t_13         
            MOVE.L      t_13, D0    ; D0 = t_13
            MOVE.L      t_14, D1    ; D1 = t_14
            MOVEA.L     p1, A0      ; A0 = p1
            ADDA.L      D1, A0      ; A0 = A0 + D1
            MOVE.L      D0, (A0)    ; (A0) = D0
; --------- t_15 = 4                
            MOVE.L      #4, D0      ; D0 = #4
            MOVE.L      D0, t_15    ; t_15 = D0
; --------- t_16 = p1[t_15]         
            LEA.L       p1, A0      ; A0 = p1
            ADDA.L      t_15, A0    ; A0 = A0 + t_15
            MOVE.L      (A0), t_16  ; t_16 = (A0)
; --------- t_18 = t_16             
            MOVE.L      t_16, D0    ; D0 = t_16
            MOVE.L      D0, t_18    ; t_18 = D0
; --------- param_s t_18            
            LEA.L       t_18, A0    ; A0 = t_18
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- t_19 = 1                
            MOVE.L      #1, D0      ; D0 = #1
            MOVE.L      D0, t_19    ; t_19 = D0
; --------- t_20 = i + t_19         
            MOVE.L      i, D0       ; D0 = i
            MOVE.L      t_19, D1    ; D1 = t_19
            ADD.L       D0, D1      ; D1 = D1 + D0
            MOVE.L      D1, t_20    ; t_20 = D1
; --------- i = t_20                
            MOVE.L      t_20, D0    ; D0 = t_20
            MOVE.L      D0, i       ; i = D0
; --------- goto e                  
            JMP         .e          ; goto .e
; --------- e_1: skip               
                                    
; --------- p2 = p1                 
.e_1:       LEA.L       p1, A0      ; A0 = p1
            MOVE.L      A0, p2      ; p2 = A0
; --------- t_21 = 4                
            MOVE.L      #4, D0      ; D0 = #4
            MOVE.L      D0, t_21    ; t_21 = D0
; --------- t_22 = p2[t_21]         
            LEA.L       p2, A0      ; A0 = p2
            ADDA.L      t_21, A0    ; A0 = A0 + t_21
            MOVE.L      (A0), t_22  ; t_22 = (A0)
; --------- t_24 = t_22             
            MOVE.L      t_22, D0    ; D0 = t_22
            MOVE.L      D0, t_24    ; t_24 = D0
; --------- param_s t_24            
            LEA.L       t_24, A0    ; A0 = t_24
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- rtn e_inicio            
            RTS                     ; RETURN TO SUBROUTINE e_inicio




; --------- e_nuevapersona: skip    
                                    
; --------- pmb e_nuevapersona      
e_nuevapersona: MOVE.L      4(SP), D0 ; D0 = POP FROM STACK
            MOVE.L      D0, let     ; let = D0
            MOVE.L      8(SP), D0   ; D0 = POP FROM STACK
            MOVE.L      D0, idpersona ; idpersona = D0
            MOVE.L      12(SP), D0  ; D0 = POP FROM STACK
            MOVE.L      D0, p       ; p = D0
; --------- t_25 = 0                
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_25    ; t_25 = D0
; --------- p[t_25] = idpersona     
            MOVE.L      idpersona, D0 ; D0 = idpersona
            MOVE.L      t_25, D1    ; D1 = t_25
            MOVEA.L     p, A0       ; A0 = p
            ADDA.L      D1, A0      ; A0 = A0 + D1
            MOVE.L      D0, (A0)    ; (A0) = D0
; --------- t_26 = 4                
            MOVE.L      #4, D0      ; D0 = #4
            MOVE.L      D0, t_26    ; t_26 = D0
; --------- p[t_26] = let           
            MOVE.L      let, D0     ; D0 = let
            MOVE.L      t_26, D1    ; D1 = t_26
            MOVEA.L     p, A0       ; A0 = p
            ADDA.L      D1, A0      ; A0 = A0 + D1
            MOVE.L      D0, (A0)    ; (A0) = D0
; --------- rtn e_nuevapersona      
            RTS                     ; RETURN TO SUBROUTINE e_nuevapersona


            END persona0            ; Fin del programa


