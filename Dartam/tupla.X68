; ==============================================================================
; TITLE       : tupla
; COMPILED BY : jartu
; COMPILER BY : Juan Arturo Abaurrea Calafell
;               Dani Salanova Dmitriyev
;               Marta González Juan
; ==============================================================================


            ORG         $1000       ; Origen

            INCLUDE     "PRINT.X68" ; Escritura en consola
            INCLUDE     "SCAN.X68"  ; Lectura de teclado
            INCLUDE     "WRITE.X68" ; Escritura de ficheros
            INCLUDE     "READ.X68"  ; Lectura de ficheros



; einicio: skip
; pmb ninicio
; t1 = 3
einicio     MOVE.W      3, D0       ; D0 = 3
            MOVE.W      t1, D0      ; t1 = D0
; d1arr = t1
            MOVE.W      t1, D0      ; D0 = t1
            MOVE.W      d1arr, D0   ; d1arr = D0
; t2 = 4
            MOVE.W      4, D0       ; D0 = 4
            MOVE.W      t2, D0      ; t2 = D0
; t3 = 5
            MOVE.W      5, D0       ; D0 = 5
            MOVE.W      t3, D0      ; t3 = D0
; t4 = t2 + t3
            MOVE.W      t2, D0      ; D0 = t2
            ADD.W       t3, D0      ; D0 = D0 + t3
            MOVE.W      D0, t4      ; t4 = D0
; d2arr = t4
            MOVE.W      t4, D0      ; D0 = t4
            MOVE.W      d2arr, D0   ; d2arr = D0
; c = arr
            MOVE.W      arr, D0     ; D0 = arr
            MOVE.W      c, D0       ; c = D0
; c = arr
            MOVE.W      arr, D0     ; D0 = arr
            MOVE.W      c, D0       ; c = D0
; t5 = 0
            MOVE.W      0, D0       ; D0 = 0
            MOVE.W      t5, D0      ; t5 = D0
; t6 = 1
            MOVE.W      1, D0       ; D0 = 1
            MOVE.W      t6, D0      ; t6 = D0
; t7 = t5 * d2arr
            MOVE.W      t5, D0      ; D0 = t5
            MULS.W      d2arr, D0   ; D0 = D0 * d2arr
            MOVE.W      D0, t7      ; t7 = D0
; t8 = t6 + t7
            MOVE.W      t6, D0      ; D0 = t6
            ADD.W       t7, D0      ; D0 = D0 + t7
            MOVE.W      D0, t8      ; t8 = D0
; t9 = t8 * 4
            MOVE.W      t8, D0      ; D0 = t8
            MULS.W      4, D0       ; D0 = D0 * 4
            MOVE.W      D0, t9      ; t9 = D0
; arr[t9] = b
            MOVEA.W     arr, A0     ; A0 = arr
            ADDA.W      b, A0       ; A0 = A0 + b
            MOVE.W      t9, (A0)    ; (A0) = t9
; t10 = 0
            MOVE.W      0, D0       ; D0 = 0
            MOVE.W      t10, D0     ; t10 = D0
; t11 = b[t10]
            MOVEA.W     b, A0       ; A0 = b
            ADDA.W      t10, A0     ; A0 = A0 + t10
            MOVE.W      (A0), t11   ; t11 = (A0)
; num = t11
            MOVE.W      t11, D0     ; D0 = t11
            MOVE.W      num, D0     ; num = D0
; t12 = 4
            MOVE.W      4, D0       ; D0 = 4
            MOVE.W      t12, D0     ; t12 = D0
; t13 = b[t12]
            MOVEA.W     b, A0       ; A0 = b
            ADDA.W      t12, A0     ; A0 = A0 + t12
            MOVE.W      (A0), t13   ; t13 = (A0)
; let = t13
            MOVE.W      t13, D0     ; D0 = t13
            MOVE.W      let, D0     ; let = D0
; t14 = 6
            MOVE.W      6, D0       ; D0 = 6
            MOVE.W      t14, D0     ; t14 = D0
; t15 = b[t14]
            MOVEA.W     b, A0       ; A0 = b
            ADDA.W      t14, A0     ; A0 = A0 + t14
            MOVE.W      (A0), t15   ; t15 = (A0)
; numeraso = t15
            MOVE.W      t15, D0     ; D0 = t15
            MOVE.W      numeraso, D0 ; numeraso = D0
; t16 = 0
            MOVE.W      0, D0       ; D0 = 0
            MOVE.W      t16, D0     ; t16 = D0
; t17 = numeraso[t16]
            MOVEA.W     numeraso, A0 ; A0 = numeraso
            ADDA.W      t16, A0     ; A0 = A0 + t16
            MOVE.W      (A0), t17   ; t17 = (A0)
; numdeverdad = t17
            MOVE.W      t17, D0     ; D0 = t17
            MOVE.W      numdeverdad, D0 ; numdeverdad = D0
; t18 = 1
            MOVE.W      1, D0       ; D0 = 1
            MOVE.W      t18, D0     ; t18 = D0
; n = t18
            MOVE.W      t18, D0     ; D0 = t18
            MOVE.W      n, D0       ; n = D0
; t19 = n - 1
            MOVE.W      n, D0       ; D0 = n
            SUB.W       1, D0       ; D0 = D0 - 1
            MOVE.W      D0, t19     ; t19 = D0
; n = t19
            MOVE.W      t19, D0     ; D0 = t19
            MOVE.W      n, D0       ; n = D0
; param_s numdeverdad
            MOVE.W      numdeverdad, -(SP) ; PUSH INTO STACK numdeverdad
; call nab, t20
            JSR         nab         ; JUMP TO SUBROUTINE nab
            MOVE.W      (SP)+, nab  ; nab = POP FROM STACK
; t21 = n + t20
            MOVE.W      n, D0       ; D0 = n
            ADD.W       t20, D0     ; D0 = D0 + t20
            MOVE.W      D0, t21     ; t21 = D0
; aa = t21
            MOVE.W      t21, D0     ; D0 = t21
            MOVE.W      aa, D0      ; aa = D0
; t22 = n + 1
            MOVE.W      n, D0       ; D0 = n
            ADD.W       1, D0       ; D0 = D0 + 1
            MOVE.W      D0, t22     ; t22 = D0
; t23 = n
            MOVE.W      n, D0       ; D0 = n
            MOVE.W      t23, D0     ; t23 = D0
; n = t22
            MOVE.W      t22, D0     ; D0 = t22
            MOVE.W      n, D0       ; n = D0
; param_s numdeverdad
            MOVE.W      numdeverdad, -(SP) ; PUSH INTO STACK numdeverdad
; call nab, t24
            JSR         nab         ; JUMP TO SUBROUTINE nab
            MOVE.W      (SP)+, nab  ; nab = POP FROM STACK
; t25 = t23 + t24
            MOVE.W      t23, D0     ; D0 = t23
            ADD.W       t24, D0     ; D0 = D0 + t24
            MOVE.W      D0, t25     ; t25 = D0
; bb = t25
            MOVE.W      t25, D0     ; D0 = t25
            MOVE.W      bb, D0      ; bb = D0
; rtn ninicio
            MOVE.W      ninicio, -(SP) ; PUSH INTO STACK ninicio
            RTS                     ; RETURN TO SUBROUTINE ninicio


; eab: skip
; pmb nab
; rtn nab, b_1
eab         MOVE.W      nab, -(SP)  ; PUSH INTO STACK nab
            RTS                     ; RETURN TO SUBROUTINE nab


            SIMHALT                 ; Fin de la ejecución
            END         START       ; Fin del programa
