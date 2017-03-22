(ns live-coding.core)

(defn ch1 [beat sec]
  (let [interval 0.25
        beats (range 0 sec interval)
        pattern [1 0 1 1 0 0 1 0 0 0 0 0 0 0 0 0]
        refrain (/ (/ sec interval) (count pattern))
        pattern (repeat refrain pattern)
        pattern (flatten pattern)
        beat-patterns (map vector beats pattern)]

    (doseq [bp beat-patterns]
      (let [b (first bp) p (last bp)]
        (at (metro (+ b beat)) (kick2 :amp (* p 0.8)))
    ))
))

(defn ch2 [beat sec]
  (let [interval 0.25
        beats (range 0 sec interval)
        pattern [0 0 0 0 0 0 0 0 1 1 1 0 1 0 1 0]
        refrain (/ (/ sec interval) (count pattern))
        pattern (repeat refrain pattern)
        pattern (flatten pattern)
        beat-patterns (map vector beats pattern)]

    (doseq [bp beat-patterns]
      (let [b (first bp) p (last bp)]
        (at (metro (+ b beat)) (clap :amp (* p 0.45)))
    ))
))

(defn ch3 [beat sec]
  (let [interval 0.25
        beats (range 0 sec interval)
        pattern [0 0 0 0 1 1 1 0 0 0 0 1 0 1 0 1]
        refrain (/ (/ sec interval) (count pattern))
        pattern (repeat refrain pattern)
        pattern (flatten pattern)
        beat-patterns (map vector beats pattern)]

    (doseq [bp beat-patterns]
      (let [b (first bp) p (last bp)]
        (at (metro (+ b beat)) (snare :amp (* p 1.2)))
    ))
))

(defn ch4 [beat sec]
  (let [interval 0.25
        beats (range 0 sec interval)
        pattern [0 0 1 0 0 0 1 1 0 0 0 0 1 0 0 0]
        refrain (/ (/ sec interval) (count pattern))
        pattern (repeat refrain pattern)
        pattern (flatten pattern)
        beat-patterns (map vector beats pattern)]

    (doseq [bp beat-patterns]
      (let [b (first bp) p (last bp)]
        (at (metro (+ b beat)) (open-hat :amp (* p 0.6)))
    ))
))

(defn ch10 [beat sec]
  (let [amp 0.8
        release 0.3
        interval 0.25
        beats (range 0 sec interval)
        pattern [39 0 39 42 0 42 46 0 46 0 46 39 0 0 42 0
                 39 0 39 42 0 42 46 0 46 0 46 39 0 0 42 0
                 37 0 37 41 0 41 44 0 44 0 44 37 0 0 42 0
                 39 0 39 42 0 42 46 0 46 0 46 39 0 0 42 0]

        refrain (/ (/ sec interval) (count pattern))
        pattern (repeat refrain pattern)
        pattern (flatten pattern)
        beat-patterns (map vector beats pattern)]

    (doseq [bp beat-patterns]
      (let [b (first bp) p (last bp)]
        (if (= p 0)
          true
          (at (metro (+ b beat)) (vintage-bass :note p :amp amp :release release)))
        )
      )
))

(defn ch11 [beat sec]
  (let [amp 0.4
        interval 0.25
        beats (range 0 sec interval)
        pattern [0 0 75 0 0 78 0 0 82 0 0 0 75 0 78 0
                 0 0 75 0 0 78 0 0 82 0 0 0 75 0 78 0
                 0 0 73 0 0 77 0 0 80 0 0 0 73 0 77 0
                 0 0 75 0 0 78 0 0 82 0 0 0 75 0 78 0]

        refrain (/ (/ sec interval) (count pattern))
        pattern (repeat refrain pattern)
        pattern (flatten pattern)
        beat-patterns (map vector beats pattern)]

    (doseq [bp beat-patterns]
      (let [b (first bp) p (last bp)]
        (if (= p 0)
          true
          (at (metro (+ b beat)) (vintage-bass :note p :amp amp)))
        )
      )
))

(defn ch13 [beat sec]
  (let [amp 0.4
        interval 0.25
        beats (range 0 sec interval)
        pattern [0 0 63 0 0 66 0 0 70 0 0 0 63 0 66 0
                 0 0 63 0 0 66 0 0 70 0 0 0 63 0 66 0
                 0 0 61 0 0 65 0 0 68 0 0 0 61 0 65 0
                 0 0 63 0 0 66 0 0 70 0 0 0 63 0 66 0]

        refrain (/ (/ sec interval) (count pattern))
        pattern (repeat refrain pattern)
        pattern (flatten pattern)
        beat-patterns (map vector beats pattern)]

    (doseq [bp beat-patterns]
      (let [b (first bp) p (last bp)]
        (if (= p 0)
          true
          (at (metro (+ b beat)) (vintage-bass :note p :amp amp)))
        )
      )
))

(defn ch12 [beat sec]
  (let [amp 0.5
        release 7
        interval 4
        beats (range 0 sec interval)
        pattern [[82 78] [85 82] [80 77] [82 78] [82 78] [85 82] [80 77] [82 78]]
        refrain (/ (/ sec interval) (count pattern))
        beat-patterns (map vector beats pattern)]
    (doseq [bp beat-patterns]
      (let [b (first bp) p (last bp)]
        (if (= p 0)
          true
          (do (at (metro (+ b beat)) (violin :pitch (first p) :amp amp :release release))
              (at (metro (+ b beat)) (violin :pitch (last  p) :amp amp :release release)))
          )
        )
      )))

(defn ch20 [beat sec]
  (let [amp 0.2
        interval 8
        beats (range 0 sec interval)
        pattern [70 70 70 0]
        refrain (/ (/ sec interval) (count pattern))
        pattern (repeat refrain pattern)
        pattern (flatten pattern)
        beat-patterns (map vector beats pattern)]

    (doseq [bp beat-patterns]
      (let [b (first bp) p (last bp)]
        (if (= p 0)
          true
          (at (metro (+ b beat))
              (rise-fall-pad :freq (midi->hz p) :amp amp)
              (rise-fall-pad :freq (midi->hz (+ p 3)) :amp amp)
              (rise-fall-pad :freq (midi->hz (+ p 5)) :amp amp))
          ))
      )
))

(defn ch21 [beat sec]
  (let [amp 0.02
        interval 4
        beats (range 0 sec interval)
        pattern [0 70 0 10 0 0 10 70]
        refrain (/ (/ sec interval) (count pattern))
        beat-patterns (map vector beats pattern)]
    (doseq [bp beat-patterns]
      (let [b (first bp) p (last bp)]
        (if (= p 0)
          true
          (do
            (at (metro (+ b beat)) (whoahaha :freq (midi->hz p) :amp amp))
            (at (metro (+ b beat 1)) (whoahaha :freq (midi->hz p) :amp amp))
            ))))
))

(defn ch30 [beat sec]
  (let [amp 0.8
        release 0.5
        interval 0.5
        beats (range 0 sec interval)
        pattern [44 44 0 42 44 44 0 42 44 44 0 42 44 44 0 42
                 37 37 0 42 37 37 0 42 37 37 0 42 37 37 0 42]
        refrain (/ (/ sec interval) (count pattern))
        pattern (repeat refrain pattern)
        pattern (flatten pattern)
        beat-patterns (map vector beats pattern)]

    (doseq [bp beat-patterns]
      (let [b (first bp) p (last bp)]
        (if (= p 0)
          true
          (at (metro (+ b beat)) (vintage-bass :note p :amp amp :release release)))
        )
      )
))

(defn ch31 [beat sec]
  (let [interval 0.25
        beats (range 0 sec interval)
        pattern [1 1 0 0 1 0 0 0 1 0 0 0 1 1 0 0]
        refrain (/ (/ sec interval) (count pattern))
        pattern (repeat refrain pattern)
        pattern (flatten pattern)
        beat-patterns (map vector beats pattern)]

    (doseq [bp beat-patterns]
      (let [b (first bp) p (last bp)]
        (at (metro (+ b beat)) (kick2 :amp (* p 0.7)))
    ))
))

(defn ch32 [beat sec]
  (let [interval 0.25
        beats (range 0 sec interval)
        pattern [1 0 1 1 0 0 1 1 1 0 1 0 0 0 1 1]
        refrain (/ (/ sec interval) (count pattern))
        pattern (repeat refrain pattern)
        pattern (flatten pattern)
        beat-patterns (map vector beats pattern)]

    (doseq [bp beat-patterns]
      (let [b (first bp) p (last bp)]
        (at (metro (+ b beat)) (closed-hat2 :amp (* p 0.6)))
    ))
))

(defn ch33 [beat sec]
  (let [interval 0.25
        beats (range 0 sec interval)
        pattern [0 0 1 0 1 1 0 0 0 0 1 1 0 1 0 0]
        refrain (/ (/ sec interval) (count pattern))
        pattern (repeat refrain pattern)
        pattern (flatten pattern)
        beat-patterns (map vector beats pattern)]

    (doseq [bp beat-patterns]
      (let [b (first bp) p (last bp)]
        (at (metro (+ b beat)) (closed-hat :amp (* p 0.55)))
    ))
))

(defn ch40 [beat sec]
  (let [amp 0.3
        interval 0.25
        beats (range 0 sec interval)
        pattern [76 72 69 72 76 72 69 72 76 72 69 72 76 72 69 72
                 76 72 69 72 76 72 69 72 76 72 69 72 76 72 69 72
                 74 71 67 71 74 71 67 71 74 71 67 71 74 71 67 71
                 74 71 67 71 74 71 67 71 74 71 67 71 74 71 67 71]
        refrain (/ (/ sec interval) (count pattern))
        pattern (repeat refrain pattern)
        pattern (flatten pattern)
        beat-patterns (map vector beats pattern)]

    (doseq [bp beat-patterns]
      (let [b (first bp) p (last bp)]
        (if (= p 0)
          true
          (at (metro (+ b beat)) (ping :note p :amp amp :release 0.01)))
        )
      )
))

(defn ch41 [beat sec]
  (let [amp 0.4
        release 10
        interval 8
        beats (range 0 sec interval)
        pattern [[69 64 60] [67 62 55] [69 64 60] [67 62 55]]
        refrain (/ (/ sec interval) (count pattern))
        beat-patterns (map vector beats pattern)]

    (doseq [bp beat-patterns]
      (let [b (first bp) p (last bp)]
        (if (= p 0)
          true
          (at (metro (+ b beat))
              (violin :pitch (first p) :amp amp :release release)
              (violin :pitch (second p) :amp amp :release release)
              (violin :pitch (last p) :amp amp :release release)
              )
        )
      ))
))


;; (defn ch50 [beat sec]
;;   (let [interval 8
;;         beats (range 0 sec interval)
;;         pattern [60 0 64 0 67 69 67 60]
;;         refrain (/ (/ sec interval) (count pattern))
;;         beat-patterns (map vector beats pattern)]
;;     (doseq [bp beat-patterns]
;;       (let [b (first bp) p (last bp)]
;;         (if (= p 0)
;;           true
;;           (at (metro (+ b beat))
;;               (ping :note (first p))

;;               )
;;           )
;;         )
;;       )
;;     ))

