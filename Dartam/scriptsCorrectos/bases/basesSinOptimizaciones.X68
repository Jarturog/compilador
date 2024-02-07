; ==============================================================================
; TITLE       : bases
; COMPILED BY : jartu
; COMPILER BY : Juan Arturo Abaurrea Calafell
;               Dani Salanova Dmitriyev
;               Marta Gonzalez Juan
; ==============================================================================

            ORG         $1000       ; Origen

t           DC.B 'Prueba de bases'  ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_1         DC.L 48                 ; INT
t_2         DC.L 49                 ; INT
t_3         DC.L 50                 ; INT
t_4         DC.L 51                 ; INT
t_5         DC.L 0                  ; INT
t_6         DC.L 0                  ; INT
t_7         DC.L 0                  ; INT
t_8         DC.B 'Binario: 0b110000'  ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_11        DC.B 'Octal: 0o61'      ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_14        DC.B 'Hexadecimal: 0x32'  ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_17        DC.B 'Decimal: 51'      ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_20        DC.L 'a'                ; CHAR
t_21        DC.L 'b'                ; CHAR
t_23        DC.B 'a:'               ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_25        DC.B 'b:'               ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
octal       DS.L 1                  ; INT
dec         DS.L 1                  ; INT
binario     DS.L 1                  ; INT
t_26        DS.B 256                ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
hex         DS.L 1                  ; INT
t_24        DS.B 256                ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_22        DS.L 1                  ; CHAR
a           DS.L 1                  ; CHAR
b           DS.L 1                  ; CHAR
t_18        DS.L 1                  ; INT
t_19        DS.B 256                ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_16        DS.B 256                ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
zero2       DS.L 1                  ; INT
zero3       DS.L 1                  ; INT
zero1       DS.L 1                  ; INT
t_9         DS.L 1                  ; INT
t_15        DS.L 1                  ; INT
t_12        DS.L 1                  ; INT
t_13        DS.B 256                ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_10        DS.B 256                ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar

; --------- SUBROUTINES             
e_show      MOVEA.L     4(SP), A1   ; A1 = POP FROM STACK
            MOVE.L      #13, D0     ; Task 13 of TRAP 15: Display the NULL terminated string pointed to by (A1) with CR, LF
            TRAP        #15         ; Interruption generated
            RTS                     ; RETURN TO SUBROUTINE ...

; --------- INITIALIZATION          
bases                               ; Etiqueta inicial (main)

            MOVE.L      #32, D0     ; Task 32 of TRAP 15: Hardware/Simulator
            MOVE.B      #5, D1      ; Enable exception processing (for input/output)
            TRAP        #15         ; Interruption generated
            JSR         e_inicio    ; Se ejecuta el main
            SIMHALT                 ; Fin de la ejecucion

; --------- e_inicio: skip          
                                    
; --------- pmb e_inicio            
; --------- t = Prueba de bases     
; --------- param_s t               
e_inicio:   LEA.L       t, A0       ; A0 = t
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- t_1 = 48                
            MOVE.L      #48, D0     ; D0 = #48
            MOVE.L      D0, t_1     ; t_1 = D0
; --------- binario = t_1           
            MOVE.L      t_1, D0     ; D0 = t_1
            MOVE.L      D0, binario ; binario = D0
; --------- t_2 = 49                
            MOVE.L      #49, D0     ; D0 = #49
            MOVE.L      D0, t_2     ; t_2 = D0
; --------- octal = t_2             
            MOVE.L      t_2, D0     ; D0 = t_2
            MOVE.L      D0, octal   ; octal = D0
; --------- t_3 = 50                
            MOVE.L      #50, D0     ; D0 = #50
            MOVE.L      D0, t_3     ; t_3 = D0
; --------- hex = t_3               
            MOVE.L      t_3, D0     ; D0 = t_3
            MOVE.L      D0, hex     ; hex = D0
; --------- t_4 = 51                
            MOVE.L      #51, D0     ; D0 = #51
            MOVE.L      D0, t_4     ; t_4 = D0
; --------- dec = t_4               
            MOVE.L      t_4, D0     ; D0 = t_4
            MOVE.L      D0, dec     ; dec = D0
; --------- t_5 = 0                 
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_5     ; t_5 = D0
; --------- zero1 = t_5             
            MOVE.L      t_5, D0     ; D0 = t_5
            MOVE.L      D0, zero1   ; zero1 = D0
; --------- t_6 = 0                 
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_6     ; t_6 = D0
; --------- zero2 = t_6             
            MOVE.L      t_6, D0     ; D0 = t_6
            MOVE.L      D0, zero2   ; zero2 = D0
; --------- t_7 = 0                 
            MOVE.L      #0, D0      ; D0 = #0
            MOVE.L      D0, t_7     ; t_7 = D0
; --------- zero3 = t_7             
            MOVE.L      t_7, D0     ; D0 = t_7
            MOVE.L      D0, zero3   ; zero3 = D0
; --------- t_8 = Binario: 0b110000  
; --------- param_s t_8             
            LEA.L       t_8, A0     ; A0 = t_8
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- t_9 = binario           
            MOVE.L      binario, D0 ; D0 = binario
            MOVE.L      D0, t_9     ; t_9 = D0
; --------- t_10 = t_9              
            MOVE.L      t_9, D0     ; D0 = t_9
            ROR.L       #8, D0      ; move to the left so it is followed by 0's, since it is a casting from char to string
            MOVE.L      D0, t_10    ; t_10 = D0
; --------- param_s t_10            
            LEA.L       t_10, A0    ; A0 = t_10
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- t_11 = Octal: 0o61      
; --------- param_s t_11            
            LEA.L       t_11, A0    ; A0 = t_11
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- t_12 = octal            
            MOVE.L      octal, D0   ; D0 = octal
            MOVE.L      D0, t_12    ; t_12 = D0
; --------- t_13 = t_12             
            MOVE.L      t_12, D0    ; D0 = t_12
            ROR.L       #8, D0      ; move to the left so it is followed by 0's, since it is a casting from char to string
            MOVE.L      D0, t_13    ; t_13 = D0
; --------- param_s t_13            
            LEA.L       t_13, A0    ; A0 = t_13
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- t_14 = Hexadecimal: 0x32  
; --------- param_s t_14            
            LEA.L       t_14, A0    ; A0 = t_14
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- t_15 = hex              
            MOVE.L      hex, D0     ; D0 = hex
            MOVE.L      D0, t_15    ; t_15 = D0
; --------- t_16 = t_15             
            MOVE.L      t_15, D0    ; D0 = t_15
            ROR.L       #8, D0      ; move to the left so it is followed by 0's, since it is a casting from char to string
            MOVE.L      D0, t_16    ; t_16 = D0
; --------- param_s t_16            
            LEA.L       t_16, A0    ; A0 = t_16
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- t_17 = Decimal: 51      
; --------- param_s t_17            
            LEA.L       t_17, A0    ; A0 = t_17
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- t_18 = dec              
            MOVE.L      dec, D0     ; D0 = dec
            MOVE.L      D0, t_18    ; t_18 = D0
; --------- t_19 = t_18             
            MOVE.L      t_18, D0    ; D0 = t_18
            ROR.L       #8, D0      ; move to the left so it is followed by 0's, since it is a casting from char to string
            MOVE.L      D0, t_19    ; t_19 = D0
; --------- param_s t_19            
            LEA.L       t_19, A0    ; A0 = t_19
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- t_20 = a                
            MOVE.L      #'a', D0    ; D0 = #'a'
            MOVE.L      D0, t_20    ; t_20 = D0
; --------- b = t_20                
            MOVE.L      t_20, D0    ; D0 = t_20
            MOVE.L      D0, b       ; b = D0
; --------- t_21 = b                
            MOVE.L      #'b', D0    ; D0 = #'b'
            MOVE.L      D0, t_21    ; t_21 = D0
; --------- a = t_21                
            MOVE.L      t_21, D0    ; D0 = t_21
            MOVE.L      D0, a       ; a = D0
; --------- t_22 = a                
            MOVE.L      a, D0       ; D0 = a
            MOVE.L      D0, t_22    ; t_22 = D0
; --------- a = b                   
            MOVE.L      b, D0       ; D0 = b
            MOVE.L      D0, a       ; a = D0
; --------- b = t_22                
            MOVE.L      t_22, D0    ; D0 = t_22
            MOVE.L      D0, b       ; b = D0
; --------- t_23 = a:               
; --------- param_s t_23            
            LEA.L       t_23, A0    ; A0 = t_23
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- t_24 = a                
            MOVE.L      a, D0       ; D0 = a
            ROR.L       #8, D0      ; move to the left so it is followed by 0's, since it is a casting from char to string
            MOVE.L      D0, t_24    ; t_24 = D0
; --------- param_s t_24            
            LEA.L       t_24, A0    ; A0 = t_24
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- t_25 = b:               
; --------- param_s t_25            
            LEA.L       t_25, A0    ; A0 = t_25
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- t_26 = b                
            MOVE.L      b, D0       ; D0 = b
            ROR.L       #8, D0      ; move to the left so it is followed by 0's, since it is a casting from char to string
            MOVE.L      D0, t_26    ; t_26 = D0
; --------- param_s t_26            
            LEA.L       t_26, A0    ; A0 = t_26
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK A0
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- rtn e_inicio            
            RTS                     ; RETURN TO SUBROUTINE e_inicio




            END bases               ; Fin del programa


