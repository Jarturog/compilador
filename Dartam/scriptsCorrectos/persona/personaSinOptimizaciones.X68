; ==============================================================================
; TITLE       : persona
; COMPILED BY : jartu
; COMPILER BY : Juan Arturo Abaurrea Calafell
;               Dani Salanova Dmitriyev
;               Marta Gonzalez Juan
; ==============================================================================

            ORG         $1000       ; Origen

persona:    DS.L 1                  ; Inicializando el miembro maxHambre de la tupla persona
            DS.L 1                  ; Inicializando el miembro inicial de la tupla persona
            DC.L 0                  ; Inicializando el miembro hambre de la tupla persona
t           DC.L 3                  ; INT
t_1         DC.B ''                 ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_2         DC.L 1                  ; INT
t_3         DC.L 'a'                ; CHAR
t_4         DC.L 2                  ; INT
t_5         DC.L 'm'                ; CHAR
t_6         DC.B 'Bienvenido a la simulación'  ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_7         DC.B 'Dos personas pensarán hasta que tengan hambre, momento en el que comerán'  ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_8         DC.B ''                 ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_9         DC.L 0                  ; INT
t_10        DC.L 0                  ; BOOLEAN
t_11        DC.B 'La persona'       ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_12        DC.L 8                  ; INT
t_14        DC.L 1                  ; INT
t_16        DC.L 8                  ; INT
t_17        DC.L 8                  ; INT
t_19        DC.L 1                  ; INT
t_21        DC.L 8                  ; INT
t_22        DC.L 8                  ; INT
t_24        DC.L 0                  ; INT
t_26        DC.L 0                  ; BOOLEAN
t_27        DC.L 8                  ; INT
t_29        DC.L 0                  ; INT
t_31        DC.L 0                  ; BOOLEAN
t_34        DC.B 'La persona'       ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_35        DC.L 4                  ; INT
t_38        DC.B 'piensa'           ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_39        DC.B 'La persona'       ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_40        DC.L 4                  ; INT
t_43        DC.B 'piensa'           ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_44        DC.L 8                  ; INT
t_46        DC.L 0                  ; INT
t_48        DC.L 0                  ; BOOLEAN
t_49        DC.L 4                  ; INT
t_52        DC.L 0                  ; INT
t_53        DC.L 8                  ; INT
t_54        DC.L 4                  ; INT
t_57        DC.L 0                  ; INT
t_58        DC.L 8                  ; INT
t_59        DC.B 'La persona'       ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_60        DC.B 'come'             ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_61        DC.L 1                  ; INT
t_63        DC.B 'Escribe lo que te ha parecido'  ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_64        DC.B 'El resultado se guardará en opinion.txt'  ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_65        DC.B 'opinion.txt'      ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
errwrite    DC.B 'Error de escritura',0  ; Mensaje de error
            DS.W 0                  ; Para evitar imparidad
errreadonly DC.B 'El fichero solo permite lectura',0  ; Mensaje de error
            DS.W 0                  ; Para evitar imparidad
t_66        DC.B 'Fin'              ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_67        DC.L 0                  ; BOOLEAN
t_68        DC.B 'La persona con inicial'  ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_70        DC.B 'Nunca pasará hambre'  ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_71        DC.L 0                  ; INT
t_72        DC.L 4                  ; INT
t_73        DC.B ''                 ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
maxhambre   DS.L 1                  ; INT
t_47        DS.L 1                  ; INT
t_45        DS.L 1                  ; INT
s1          DS.L 1                  ; POINTER TO ARRAY, TUPLE OR STRING
t_41        DS.L 1                  ; CHAR
s2          DS.L 1                  ; POINTER TO ARRAY, TUPLE OR STRING
t_42        DS.B 256                ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
s3          DS.L 1                  ; POINTER TO ARRAY, TUPLE OR STRING
str_1       DS.L 1                  ; POINTER TO ARRAY, TUPLE OR STRING
str         DS.L 1                  ; POINTER TO ARRAY, TUPLE OR STRING
t_36        DS.L 1                  ; CHAR
turnos      DS.L 1                  ; INT
t_37        DS.B 256                ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_32        DS.L 1                  ; BOOLEAN
t_33        DS.L 1                  ; BOOLEAN
t_30        DS.L 1                  ; INT
p1          DS.B 12                 ; TUPLE
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
p2          DS.B 12                 ; TUPLE
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_28        DS.L 1                  ; INT
t_62        DS.L 1                  ; INT
t_25        DS.L 1                  ; INT
t_69        DS.B 256                ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_23        DS.L 1                  ; INT
t_20        DS.L 1                  ; INT
t_18        DS.L 1                  ; INT
i           DS.L 1                  ; INT
inicial     DS.L 1                  ; CHAR
p           DS.L 1                  ; POINTER TO ARRAY, TUPLE OR STRING
t_50        DS.L 1                  ; CHAR
t_51        DS.B 256                ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_15        DS.L 1                  ; INT
t_56        DS.B 256                ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_13        DS.L 1                  ; INT
t_55        DS.L 1                  ; CHAR

; --------- SUBROUTINES             
e_show      MOVEA.L     4(SP), A1   ; A1 = POP FROM STACK
            MOVE.L      #13, D0     ; Task 13 of TRAP 15: Display the NULL terminated string pointed to by (A1) with CR, LF
            TRAP        #15         ; Interruption generated
            RTS                     ; RETURN TO SUBROUTINE ...
e_scan      MOVEA.L     4(SP), A1   ; A1 = POP FROM STACK
            MOVE.L      #2, D0      ; Read string from keyboard and store at (A1), NULL (0) terminated, length returned in D1.W (max 80)
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
            LEA.L       p1, A0      
            MOVE.L      A0, -(SP)   
            JSR         initpersona 
            ADDA.L      #4, SP      

            LEA.L       p2, A0      
            MOVE.L      A0, -(SP)   
            JSR         initpersona 
            ADDA.L      #4, SP      

            JSR         e_inicio    ; Se ejecuta el main
            SIMHALT                 ; Fin de la ejecucion

; --------- e_inicio: skip          
                                    
; --------- pmb e_inicio            
; --------- t = 3                   
e_inicio:   MOVE.L      #3, D0      ; D0 = #3
            MOVE.L      D0, t       ; t = D0
; --------- turnos = t              
            MOVE.L      t, D0       ; D0 = t
            MOVE.L      D0, turnos  ; turnos = D0
; --------- t_1 =                   
; --------- str = t_1               
            LEA.L       t_1, A0     ; A0 = t_1
            MOVE.L      A0, str     ; str = A0
; --------- param_s p1              
            LEA.L       p1, A0      ; A0 = p1
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- t_2 = 1                 
            MOVE.L      #1, D0      ; D0 = #1
            MOVE.L      D0, t_2     ; t_2 = D0
; --------- param_s t_2             
            MOVE.L      t_2, D0     ; D0 = t_2
            MOVE.L      D0, -(SP)   ; PUSH INTO STACK D0
; --------- t_3 = a                 
            MOVE.L      #'a', D0    ; D0 = #'a'
            MOVE.L      D0, t_3     ; t_3 = D0
; --------- param_s t_3             
            MOVE.L      t_3, D0     ; D0 = t_3
            MOVE.L      D0, -(SP)   ; PUSH INTO STACK D0
; --------- call e_nuevapersona     
            JSR         e_nuevapersona ; JUMP TO SUBROUTINE e_nuevapersona
            ADDA.L      #12, SP     ; SP = SP + 12
; --------- param_s p2              
            LEA.L       p2, A0      ; A0 = p2
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- t_4 = 2                 
            MOVE.L      #2, D0      ; D0 = #2
            MOVE.L      D0, t_4     ; t_4 = D0
; --------- param_s t_4             
            MOVE.L      t_4, D0     ; D0 = t_4
            MOVE.L      D0, -(SP)   ; PUSH INTO STACK D0
; --------- t_5 = m                 
            MOVE.L      #'m', D0    ; D0 = #'m'
            MOVE.L      D0, t_5     ; t_5 = D0
; --------- param_s t_5             
            MOVE.L      t_5, D0     ; D0 = t_5
            MOVE.L      D0, -(SP)   ; PUSH INTO STACK D0
; --------- call e_nuevapersona     
            JSR         e_nuevapersona ; JUMP TO SUBROUTINE e_nuevapersona
            ADDA.L      #12, SP     ; SP = SP + 12
; --------- t_6 = Bienvenido a la simulación  
; --------- param_s t_6             
            LEA.L       t_6, A0     ; A0 = t_6
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- t_7 = Dos personas pensarán hasta que tengan hambre, momento en el que comerán  
; --------- param_s t_7             
            LEA.L       t_7, A0     ; A0 = t_7
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- t_8 =                   
; --------- param_s t_8             
            LEA.L       t_8, A0     ; A0 = t_8
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- t_9 = 0                 
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_9     ; t_9 = D0
; --------- i = t_9                 
            MOVE.L      t_9, D0     ; D0 = t_9
            MOVE.L      D0, i       ; i = D0
; --------- e: skip                 
                                    
; --------- if i < turnos goto e_3  
.e:         MOVE.L      i, D0       ; D0 = i
            MOVE.L      turnos, D1  ; D1 = turnos
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D1 - D0
            BLT         .e_3        ; IF (N XOR V) FLAGS = 1 GOTO .e_3
; --------- t_10 = 0                
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_10    ; t_10 = D0
; --------- goto e_4                
            JMP         .e_4        ; goto .e_4
; --------- e_3: skip               
                                    
; --------- t_10 = -1               
.e_3:       MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_10    ; t_10 = D0
; --------- e_4: skip               
                                    
; --------- if t_10 == 0 goto e_1   
.e_4:       MOVE.L      t_10, D0    ; D0 = t_10
            MOVE.L      #0, D1      ; D1 = #0
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_1        ; IF Z FLAG = 1 GOTO .e_1
; --------- t_11 = La persona       
; --------- str_1 = t_11            
            LEA.L       t_11, A0    ; A0 = t_11
            MOVE.L      A0, str_1   ; str_1 = A0
; --------- t_12 = 8                
            MOVE.L      #8, D0      ; D0 = #8
            MOVE.L      D0, t_12    ; t_12 = D0
; --------- t_13 = p1[t_12]         
            LEA.L       p1, A0      ; A0 = p1
            MOVE.L      t_12, D0    ; D0 = t_12
            ADDA.L      D0, A0      ; A0 = A0 + D0
            MOVE.L      (A0), t_13  ; t_13 = (A0)
; --------- t_14 = 1                
            MOVE.L      #1, D0      ; D0 = #1
            MOVE.L      D0, t_14    ; t_14 = D0
; --------- t_15 = t_13 + t_14      
            MOVE.L      t_13, D0    ; D0 = t_13
            MOVE.L      t_14, D1    ; D1 = t_14
            ADD.L       D0, D1      ; D1 = D1 + D0
            MOVE.L      D1, t_15    ; t_15 = D1
; --------- t_16 = 8                
            MOVE.L      #8, D0      ; D0 = #8
            MOVE.L      D0, t_16    ; t_16 = D0
; --------- p1[t_16] = t_15         
            MOVE.L      t_15, D0    ; D0 = t_15
            MOVE.L      t_16, D1    ; D1 = t_16
            LEA.L       p1, A0      ; A0 = p1
            ADDA.L      D1, A0      ; A0 = A0 + D1
            MOVE.L      D0, (A0)    ; (A0) = D0
; --------- t_17 = 8                
            MOVE.L      #8, D0      ; D0 = #8
            MOVE.L      D0, t_17    ; t_17 = D0
; --------- t_18 = p2[t_17]         
            LEA.L       p2, A0      ; A0 = p2
            MOVE.L      t_17, D0    ; D0 = t_17
            ADDA.L      D0, A0      ; A0 = A0 + D0
            MOVE.L      (A0), t_18  ; t_18 = (A0)
; --------- t_19 = 1                
            MOVE.L      #1, D0      ; D0 = #1
            MOVE.L      D0, t_19    ; t_19 = D0
; --------- t_20 = t_18 + t_19      
            MOVE.L      t_18, D0    ; D0 = t_18
            MOVE.L      t_19, D1    ; D1 = t_19
            ADD.L       D0, D1      ; D1 = D1 + D0
            MOVE.L      D1, t_20    ; t_20 = D1
; --------- t_21 = 8                
            MOVE.L      #8, D0      ; D0 = #8
            MOVE.L      D0, t_21    ; t_21 = D0
; --------- p2[t_21] = t_20         
            MOVE.L      t_20, D0    ; D0 = t_20
            MOVE.L      t_21, D1    ; D1 = t_21
            LEA.L       p2, A0      ; A0 = p2
            ADDA.L      D1, A0      ; A0 = A0 + D1
            MOVE.L      D0, (A0)    ; (A0) = D0
; --------- t_22 = 8                
            MOVE.L      #8, D0      ; D0 = #8
            MOVE.L      D0, t_22    ; t_22 = D0
; --------- t_23 = p1[t_22]         
            LEA.L       p1, A0      ; A0 = p1
            MOVE.L      t_22, D0    ; D0 = t_22
            ADDA.L      D0, A0      ; A0 = A0 + D0
            MOVE.L      (A0), t_23  ; t_23 = (A0)
; --------- t_24 = 0                
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_24    ; t_24 = D0
; --------- t_25 = p1[t_24]         
            LEA.L       p1, A0      ; A0 = p1
            MOVE.L      t_24, D0    ; D0 = t_24
            ADDA.L      D0, A0      ; A0 = A0 + D0
            MOVE.L      (A0), t_25  ; t_25 = (A0)
; --------- if t_23 > t_25 goto e_5  
            MOVE.L      t_23, D0    ; D0 = t_23
            MOVE.L      t_25, D1    ; D1 = t_25
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D1 - D0
            BGT         .e_5        ; IF ((N XOR V) OR Z) FLAGS = 0 GOTO .e_5
; --------- t_26 = 0                
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_26    ; t_26 = D0
; --------- goto e_6                
            JMP         .e_6        ; goto .e_6
; --------- e_5: skip               
                                    
; --------- t_26 = -1               
.e_5:       MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_26    ; t_26 = D0
; --------- e_6: skip               
                                    
; --------- t_27 = 8                
.e_6:       MOVE.L      #8, D0      ; D0 = #8
            MOVE.L      D0, t_27    ; t_27 = D0
; --------- t_28 = p2[t_27]         
            LEA.L       p2, A0      ; A0 = p2
            MOVE.L      t_27, D0    ; D0 = t_27
            ADDA.L      D0, A0      ; A0 = A0 + D0
            MOVE.L      (A0), t_28  ; t_28 = (A0)
; --------- t_29 = 0                
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_29    ; t_29 = D0
; --------- t_30 = p2[t_29]         
            LEA.L       p2, A0      ; A0 = p2
            MOVE.L      t_29, D0    ; D0 = t_29
            ADDA.L      D0, A0      ; A0 = A0 + D0
            MOVE.L      (A0), t_30  ; t_30 = (A0)
; --------- if t_28 > t_30 goto e_7  
            MOVE.L      t_28, D0    ; D0 = t_28
            MOVE.L      t_30, D1    ; D1 = t_30
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D1 - D0
            BGT         .e_7        ; IF ((N XOR V) OR Z) FLAGS = 0 GOTO .e_7
; --------- t_31 = 0                
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_31    ; t_31 = D0
; --------- goto e_8                
            JMP         .e_8        ; goto .e_8
; --------- e_7: skip               
                                    
; --------- t_31 = -1               
.e_7:       MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_31    ; t_31 = D0
; --------- e_8: skip               
                                    
; --------- t_32 = t_26 or t_31     
.e_8:       MOVE.L      t_26, D0    ; D0 = t_26
            MOVE.L      t_31, D1    ; D1 = t_31
            OR.L        D0, D1      ; D1 = D1 or D0
            MOVE.L      D1, t_32    ; t_32 = D1
; --------- t_33 = not t_32         
            MOVE.L      t_32, D0    ; D0 = t_32
            NOT.L       D0          ; D0 = not D0
            MOVE.L      D0, t_33    ; t_33 = D0
; --------- if t_33 == 0 goto e_10  
            MOVE.L      t_33, D0    ; D0 = t_33
            MOVE.L      #0, D1      ; D1 = #0
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_10       ; IF Z FLAG = 1 GOTO .e_10
; --------- t_34 = La persona       
; --------- param_s t_34            
            LEA.L       t_34, A0    ; A0 = t_34
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- t_35 = 4                
            MOVE.L      #4, D0      ; D0 = #4
            MOVE.L      D0, t_35    ; t_35 = D0
; --------- t_36 = p1[t_35]         
            LEA.L       p1, A0      ; A0 = p1
            MOVE.L      t_35, D0    ; D0 = t_35
            ADDA.L      D0, A0      ; A0 = A0 + D0
            MOVE.L      (A0), t_36  ; t_36 = (A0)
; --------- t_37 = t_36             
            MOVE.L      t_36, D0    ; D0 = t_36
            ROR.L       #8, D0      ; move to the left so it is followed by 0's, since it is a casting from char to string
            MOVE.L      D0, t_37    ; t_37 = D0
; --------- param_s t_37            
            LEA.L       t_37, A0    ; A0 = t_37
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- t_38 = piensa           
; --------- param_s t_38            
            LEA.L       t_38, A0    ; A0 = t_38
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_imprimirtexto    
            JSR         e_imprimirtexto ; JUMP TO SUBROUTINE e_imprimirtexto
            ADDA.L      #12, SP     ; SP = SP + 12
; --------- t_39 = La persona       
; --------- param_s t_39            
            LEA.L       t_39, A0    ; A0 = t_39
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- t_40 = 4                
            MOVE.L      #4, D0      ; D0 = #4
            MOVE.L      D0, t_40    ; t_40 = D0
; --------- t_41 = p2[t_40]         
            LEA.L       p2, A0      ; A0 = p2
            MOVE.L      t_40, D0    ; D0 = t_40
            ADDA.L      D0, A0      ; A0 = A0 + D0
            MOVE.L      (A0), t_41  ; t_41 = (A0)
; --------- t_42 = t_41             
            MOVE.L      t_41, D0    ; D0 = t_41
            ROR.L       #8, D0      ; move to the left so it is followed by 0's, since it is a casting from char to string
            MOVE.L      D0, t_42    ; t_42 = D0
; --------- param_s t_42            
            LEA.L       t_42, A0    ; A0 = t_42
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- t_43 = piensa           
; --------- param_s t_43            
            LEA.L       t_43, A0    ; A0 = t_43
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_imprimirtexto    
            JSR         e_imprimirtexto ; JUMP TO SUBROUTINE e_imprimirtexto
            ADDA.L      #12, SP     ; SP = SP + 12
; --------- goto e_2                
            JMP         .e_2        ; goto .e_2
; --------- goto e_9                
            JMP         .e_9        ; goto .e_9
; --------- e_10: skip              
                                    
; --------- e_9: skip               
.e_10:                              
; --------- t_44 = 8                
.e_9:       MOVE.L      #8, D0      ; D0 = #8
            MOVE.L      D0, t_44    ; t_44 = D0
; --------- t_45 = p1[t_44]         
            LEA.L       p1, A0      ; A0 = p1
            MOVE.L      t_44, D0    ; D0 = t_44
            ADDA.L      D0, A0      ; A0 = A0 + D0
            MOVE.L      (A0), t_45  ; t_45 = (A0)
; --------- t_46 = 0                
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_46    ; t_46 = D0
; --------- t_47 = p1[t_46]         
            LEA.L       p1, A0      ; A0 = p1
            MOVE.L      t_46, D0    ; D0 = t_46
            ADDA.L      D0, A0      ; A0 = A0 + D0
            MOVE.L      (A0), t_47  ; t_47 = (A0)
; --------- if t_45 > t_47 goto e_11  
            MOVE.L      t_45, D0    ; D0 = t_45
            MOVE.L      t_47, D1    ; D1 = t_47
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D1 - D0
            BGT         .e_11       ; IF ((N XOR V) OR Z) FLAGS = 0 GOTO .e_11
; --------- t_48 = 0                
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_48    ; t_48 = D0
; --------- goto e_12               
            JMP         .e_12       ; goto .e_12
; --------- e_11: skip              
                                    
; --------- t_48 = -1               
.e_11:      MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_48    ; t_48 = D0
; --------- e_12: skip              
                                    
; --------- if t_48 == 0 goto e_14  
.e_12:      MOVE.L      t_48, D0    ; D0 = t_48
            MOVE.L      #0, D1      ; D1 = #0
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_14       ; IF Z FLAG = 1 GOTO .e_14
; --------- t_49 = 4                
            MOVE.L      #4, D0      ; D0 = #4
            MOVE.L      D0, t_49    ; t_49 = D0
; --------- t_50 = p1[t_49]         
            LEA.L       p1, A0      ; A0 = p1
            MOVE.L      t_49, D0    ; D0 = t_49
            ADDA.L      D0, A0      ; A0 = A0 + D0
            MOVE.L      (A0), t_50  ; t_50 = (A0)
; --------- t_51 = t_50             
            MOVE.L      t_50, D0    ; D0 = t_50
            ROR.L       #8, D0      ; move to the left so it is followed by 0's, since it is a casting from char to string
            MOVE.L      D0, t_51    ; t_51 = D0
; --------- str_1 = t_51            
            LEA.L       t_51, A0    ; A0 = t_51
            MOVE.L      A0, str_1   ; str_1 = A0
; --------- t_52 = 0                
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_52    ; t_52 = D0
; --------- t_53 = 8                
            MOVE.L      #8, D0      ; D0 = #8
            MOVE.L      D0, t_53    ; t_53 = D0
; --------- p1[t_53] = t_52         
            MOVE.L      t_52, D0    ; D0 = t_52
            MOVE.L      t_53, D1    ; D1 = t_53
            LEA.L       p1, A0      ; A0 = p1
            ADDA.L      D1, A0      ; A0 = A0 + D1
            MOVE.L      D0, (A0)    ; (A0) = D0
; --------- goto e_13               
            JMP         .e_13       ; goto .e_13
; --------- e_14: skip              
                                    
; --------- t_54 = 4                
.e_14:      MOVE.L      #4, D0      ; D0 = #4
            MOVE.L      D0, t_54    ; t_54 = D0
; --------- t_55 = p2[t_54]         
            LEA.L       p2, A0      ; A0 = p2
            MOVE.L      t_54, D0    ; D0 = t_54
            ADDA.L      D0, A0      ; A0 = A0 + D0
            MOVE.L      (A0), t_55  ; t_55 = (A0)
; --------- t_56 = t_55             
            MOVE.L      t_55, D0    ; D0 = t_55
            ROR.L       #8, D0      ; move to the left so it is followed by 0's, since it is a casting from char to string
            MOVE.L      D0, t_56    ; t_56 = D0
; --------- str_1 = t_56            
            LEA.L       t_56, A0    ; A0 = t_56
            MOVE.L      A0, str_1   ; str_1 = A0
; --------- t_57 = 0                
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_57    ; t_57 = D0
; --------- t_58 = 8                
            MOVE.L      #8, D0      ; D0 = #8
            MOVE.L      D0, t_58    ; t_58 = D0
; --------- p2[t_58] = t_57         
            MOVE.L      t_57, D0    ; D0 = t_57
            MOVE.L      t_58, D1    ; D1 = t_58
            LEA.L       p2, A0      ; A0 = p2
            ADDA.L      D1, A0      ; A0 = A0 + D1
            MOVE.L      D0, (A0)    ; (A0) = D0
; --------- e_13: skip              
                                    
; --------- t_59 = La persona       
; --------- param_s t_59            
.e_13:      LEA.L       t_59, A0    ; A0 = t_59
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- param_s str_1           
            MOVEA.L     str_1, A0   ; A0 = str_1
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- t_60 = come             
; --------- param_s t_60            
            LEA.L       t_60, A0    ; A0 = t_60
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_imprimirtexto    
            JSR         e_imprimirtexto ; JUMP TO SUBROUTINE e_imprimirtexto
            ADDA.L      #12, SP     ; SP = SP + 12
; --------- e_2: skip               
                                    
; --------- t_61 = 1                
.e_2:       MOVE.L      #1, D0      ; D0 = #1
            MOVE.L      D0, t_61    ; t_61 = D0
; --------- t_62 = i + t_61         
            MOVE.L      i, D0       ; D0 = i
            MOVE.L      t_61, D1    ; D1 = t_61
            ADD.L       D0, D1      ; D1 = D1 + D0
            MOVE.L      D1, t_62    ; t_62 = D1
; --------- i = t_62                
            MOVE.L      t_62, D0    ; D0 = t_62
            MOVE.L      D0, i       ; i = D0
; --------- goto e                  
            JMP         .e          ; goto .e
; --------- e_1: skip               
                                    
; --------- param_s str             
.e_1:       MOVEA.L     str, A0     ; A0 = str
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- t_63 = Escribe lo que te ha parecido  
; --------- param_s t_63            
            LEA.L       t_63, A0    ; A0 = t_63
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- t_64 = El resultado se guardará en opinion.txt  
; --------- param_s t_64            
            LEA.L       t_64, A0    ; A0 = t_64
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- param_s str             
            MOVEA.L     str, A0     ; A0 = str
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_scan             
            JSR         e_scan      ; JUMP TO SUBROUTINE e_scan
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- t_65 = opinion.txt      
; --------- param_s t_65            
            LEA.L       t_65, A0    ; A0 = t_65
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- param_s str             
            MOVEA.L     str, A0     ; A0 = str
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_into             
            JSR         e_into      ; JUMP TO SUBROUTINE e_into
            ADDA.L      #8, SP      ; SP = SP + 8
; --------- t_66 = Fin              
; --------- param_s t_66            
            LEA.L       t_66, A0    ; A0 = t_66
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
            MOVE.L      D0, maxhambre ; maxhambre = D0
            MOVE.L      12(SP), D0  ; D0 = POP FROM STACK
            MOVE.L      D0, p       ; p = D0
; --------- if maxhambre > turnos goto e_15  
            MOVE.L      maxhambre, D0 ; D0 = maxhambre
            MOVE.L      turnos, D1  ; D1 = turnos
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D1 - D0
            BGT         .e_15       ; IF ((N XOR V) OR Z) FLAGS = 0 GOTO .e_15
; --------- t_67 = 0                
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_67    ; t_67 = D0
; --------- goto e_16               
            JMP         .e_16       ; goto .e_16
; --------- e_15: skip              
                                    
; --------- t_67 = -1               
.e_15:      MOVE.L      #-1, D0     ; D0 = #-1
            MOVE.L      D0, t_67    ; t_67 = D0
; --------- e_16: skip              
                                    
; --------- if t_67 == 0 goto e_18  
.e_16:      MOVE.L      t_67, D0    ; D0 = t_67
            MOVE.L      #0, D1      ; D1 = #0
            CMP.L       D1, D0      ; UPDATE FLAGS WITH D0 - D1
            BEQ         .e_18       ; IF Z FLAG = 1 GOTO .e_18
; --------- t_68 = La persona con inicial  
; --------- param_s t_68            
            LEA.L       t_68, A0    ; A0 = t_68
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- t_69 = inicial          
            MOVE.L      inicial, D0 ; D0 = inicial
            ROR.L       #8, D0      ; move to the left so it is followed by 0's, since it is a casting from char to string
            MOVE.L      D0, t_69    ; t_69 = D0
; --------- param_s t_69            
            LEA.L       t_69, A0    ; A0 = t_69
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- t_70 = Nunca pasará hambre  
; --------- param_s t_70            
            LEA.L       t_70, A0    ; A0 = t_70
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- goto e_17               
            JMP         .e_17       ; goto .e_17
; --------- e_18: skip              
                                    
; --------- e_17: skip              
.e_18:                              
; --------- t_71 = 0                
.e_17:      MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_71    ; t_71 = D0
; --------- p[t_71] = maxhambre     
            MOVE.L      maxhambre, D0 ; D0 = maxhambre
            MOVE.L      t_71, D1    ; D1 = t_71
            MOVEA.L     p, A0       ; A0 = p
            ADDA.L      D1, A0      ; A0 = A0 + D1
            MOVE.L      D0, (A0)    ; (A0) = D0
; --------- t_72 = 4                
            MOVE.L      #4, D0      ; D0 = #4
            MOVE.L      D0, t_72    ; t_72 = D0
; --------- p[t_72] = inicial       
            MOVE.L      inicial, D0 ; D0 = inicial
            MOVE.L      t_72, D1    ; D1 = t_72
            MOVEA.L     p, A0       ; A0 = p
            ADDA.L      D1, A0      ; A0 = A0 + D1
            MOVE.L      D0, (A0)    ; (A0) = D0
; --------- rtn e_nuevapersona      
            RTS                     ; RETURN TO SUBROUTINE e_nuevapersona


; --------- e_imprimirtexto: skip   
                                    
; --------- pmb e_imprimirtexto     
e_imprimirtexto: MOVE.L      4(SP), D0 ; D0 = POP FROM STACK
            MOVE.L      D0, s3      ; s3 = D0
            MOVE.L      8(SP), D0   ; D0 = POP FROM STACK
            MOVE.L      D0, s2      ; s2 = D0
            MOVE.L      12(SP), D0  ; D0 = POP FROM STACK
            MOVE.L      D0, s1      ; s1 = D0
; --------- param_s s1              
            MOVEA.L     s1, A0      ; A0 = s1
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- param_s s2              
            MOVEA.L     s2, A0      ; A0 = s2
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- param_s s3              
            MOVEA.L     s3, A0      ; A0 = s3
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- t_73 =                  
; --------- param_s t_73            
            LEA.L       t_73, A0    ; A0 = t_73
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- rtn e_imprimirtexto     
            RTS                     ; RETURN TO SUBROUTINE e_imprimirtexto


            END persona0            ; Fin del programa


