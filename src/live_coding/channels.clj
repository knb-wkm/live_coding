(defn ch1 [beat sec]
  (let [beats (range 0 sec 0.25)
        pattern [1 0 1 1 0 0 1 0 0 0 0 0 0 0 0 0]
        pattern (flatten (repeat (/ sec 4) pattern))
        pairs (map vector beats pattern)]

    (doseq [pair pairs]
      (let [b (first pair) n (last pair)]
        (at (metro (+ b beat)) (kick2 :amp n))
    ))
))

(defn ch2 [beat sec]
  (let [beats (range 0 sec 0.25)
        pattern [0 0 0 0 0 0 0 0 1 1 1 0 1 0 1 0]
        pattern (flatten (repeat (/ sec 4) pattern))
        pairs (map vector beats pattern)]

    (doseq [pair pairs]
      (let [b (first pair) n (last pair)]
        (at (metro (+ b beat)) (clap :amp (* n 0.1)))
    ))
))

(defn ch3 [beat sec]
  (let [beats   (range 0 sec 0.25)
        pattern [63 0 73 70 0 0 66 0 0 0 75 73 0 0 78 0]
        pattern (flatten (repeat (/ sec 4) pattern))
        pairs    (map vector beats pattern)]

    (doseq [pair pairs]
      (let [b (first pair) n (last pair)]
        (at (metro (+ b beat)) (vintage-bass :note n :amp 0.6))
    ))
))

(defn ch4 [beat sec]
  (let [n (/ sec 2)]
    (at (metro (+ 0 beat)) (rise-fall-pad :freq (midi->hz 75)))
    (at (metro (+ n beat)) (rise-fall-pad :freq (midi->hz 73)))
    )
)

(defn ch5 [beat sec]
  (let [n (/ sec 2)]
    (at (metro (+ 1 beat)) (rise-fall-pad :freq (midi->hz 65)))
    (at (metro (+ n 1 beat)) (rise-fall-pad :freq (midi->hz 61)))
    )
)

(defn ch6 [beat sec]
  (let [n (/ sec 2)]
    (at (metro (+ 2 beat)) (rise-fall-pad :freq (midi->hz 87)))
    (at (metro (+ n 2 beat)) (rise-fall-pad :freq (midi->hz 85)))
    )
)

(defn ch7 [beat sec]
  (let [n (/ sec 2)]
    (at (metro (+ 1 beat)) (whoahaha :freq (midi->hz 30) :amp 0.2))
    (at (metro (+ n 1 beat)) (whoahaha :freq (midi->hz 30) :amp 0.2))
    )
)

(defn ch8 [beat sec]
  (let [beats   (range 0 sec 0.25)
        pattern [63 0 73 70 0 0 66 0 0 0 75 73 0 0 78 0]
        pattern (flatten (repeat (/ sec 4) pattern))
        pairs    (map vector beats pattern)]

    (doseq [pair pairs]
      (let [b (first pair) n (last pair)]
        (at (metro (+ b beat)) (vintage-bass :note n :amp 0.6))
    ))

))

(defn ch10 [beat sec]
  (let [beats (range 0 sec 0.25)
        pattern [1 0 1 0 0 0 0 0 1 0 0 0 0 0 1 1]
        pattern (flatten (repeat (/ sec 4) pattern))
        pairs (map vector beats pattern)]

    (doseq [pair pairs]
      (let [b (first pair) n (last pair)]
        (at (metro (+ b beat)) (kick2 :amp n))
    ))
))

(defn ch11 [beat sec]
  (let [beats (range 0 sec 0.25)
        pattern [0 0 0 0 1 0 0 0 0 0 0 0 1 0 1 0]
        pattern (flatten (repeat (/ sec 4) pattern))
        pairs (map vector beats pattern)]

    (doseq [pair pairs]
      (let [b (first pair) n (last pair)]
        (at (metro (+ b beat)) (open-hat :amp (* n 0.2)))
    ))
))

