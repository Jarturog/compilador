; ==============================================================================
; TITLE       : calc2
; COMPILED BY : jartu
; COMPILER BY : Juan Arturo Abaurrea Calafell
;               Dani Salanova Dmitriyev
;               Marta González Juan
; ==============================================================================

            ORG         $1000       ; Origen

t           DC.L 20                 ; INT
t_1         DC.L 25                 ; INT
t_2         DC.L 0                  ; INT
uxiono      DC.L 21                 ; INT
t_4         DC.L 0                  ; INT
t_6         DC.L 0                  ; INT
t_8         DC.B 'yuju'             ; STRING
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
a           DS.B 0                  ; POINTER TO ARRAY OR TUPLE
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
b           DS.B 5                  ; POINTER TO ARRAY OR TUPLE
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
t_7         DS.L 1                  ; INT
pimpin      DS.B 5                  ; COPY OF ARRAY OR TUPLE
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
ups         DS.B 0                  ; COPY OF ARRAY OR TUPLE
            DC.B 0                  ; Los strings y chars acaban en 0
            DS.W 0                  ; No pueden haber variables en zonas de memoria impar
CALC2                               ; Etiqueta inicial (main)

            MOVE.L      #32, D0     ; Task 32 of TRAP 15: Hardware/Simulator
            MOVE.B      #5, D1      ; Enable exception processing (for input/output)
            TRAP        #15         ; Interruption generated
            JSR         e_inicio    ; Se ejecuta el main
            SIMHALT                 ; Fin de la ejecución

; --------- e_inicio: skip          
; --------- pmb e_inicio            
; --------- ups = a                 
e_inicio:   CLR.L       D0          ; CLEAR D0
            MOVE.B      a, D0       ; D0 = a
            MOVE.B      D0, ups     ; ups = D0
; --------- pimpin = b              
            CLR.L       D0          ; CLEAR D0
            MOVE.B      b, D0       ; D0 = b
            MOVE.B      D0, pimpin  ; pimpin = D0
; --------- pimpin[t_2] = t_1       
            MOVEA.L     pimpin, A0  ; A0 = pimpin
            ADDA.L      t_1, A0     ; A0 = A0 + t_1
            MOVE.L      t_2, (A0)   ; (A0) = t_2
; --------- pimpin[t_4] = uxiono    
            MOVEA.L     pimpin, A0  ; A0 = pimpin
            ADDA.L      uxiono, A0  ; A0 = A0 + uxiono
            MOVE.L      t_4, (A0)   ; (A0) = t_4
; --------- uxiono = 26             
            MOVE.L      #26, D0     ; D0 = #26
            MOVE.L      D0, uxiono  ; uxiono = D0
; --------- t_7 = pimpin[t_6]       
            MOVEA.L     pimpin, A0  ; A0 = pimpin
            ADDA.L      t_6, A0     ; A0 = A0 + t_6
            MOVE.L      (A0), t_7   ; t_7 = (A0)
; --------- uxiono = t_7            
            MOVE.L      t_7, D0     ; D0 = t_7
            MOVE.L      D0, uxiono  ; uxiono = D0
; --------- param_s t_8             
            LEA.L       t_8, A0     ; A0 = t_8
            MOVE.L      A0, -(SP)   ; PUSH INTO STACK t_8
; --------- call e_show             
            JSR         e_show      ; JUMP TO SUBROUTINE e_show
            ADDA.L      #4, SP      ; SP = SP + 4
; --------- rtn e_inicio            
            RTS                     ; RETURN TO SUBROUTINE e_inicio




e_show      MOVEA.L     4(SP), A1   ; A1 = POP FROM STACK
            MOVE.L      #13, D0     ; Task 13 of TRAP 15: Display the NULL terminated string pointed to by (A1) with CR, LF
            TRAP        #15         ; Interruption generated
            RTS                     ; RETURN TO SUBROUTINE ...
            END CALC2               ; Fin del programa

