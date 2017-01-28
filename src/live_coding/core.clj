(ns live-coding.core
  (:use
   [clojure.pprint]
   [overtone.live])
  (:require [shadertone.tone :as s]
))

(load-file "src/live_coding/drums.clj")
(load-file "src/live_coding/synths.clj")
(load-file "src/live_coding/channels.clj")

;; start shader
(s/start "src/live_coding/shaders/unknown_pleasures.glsl"
         :width 2600 :height 1400)

;; metronome
(def metro (metronome 120))

;; sec 16 count_by 0.25 = 64
(defn player [beat]
  (let [sec 16]
    ;; (ch1 beat sec)
    ;; (ch2 beat sec)
    ;; (ch3 beat sec)
    ;; (ch4 beat sec)
    ;; (ch5 beat sec)
    ;; (ch6 beat sec)
    ;; (ch7 beat sec)

    (ch10 beat sec)
    (ch11 beat sec)
    (ch13 beat sec)
    (apply-by (metro (+ sec beat)) #'player (+ sec beat) [])
))

(player (metro))
(stop)

(bubbles :base-freq 520)
(kill bubbles)


(defn ch10 [beat sec]
  (let [beats (range 0 sec 0.25)
        pattern [1 0 0 0 1 0 0 0 1 0 0 0 1 0 1 1]
        pattern (flatten (repeat (/ sec 4) pattern))
        pairs (map vector beats pattern)]

    (doseq [pair pairs]
      (let [b (first pair) n (last pair)]
        (at (metro (+ b beat)) (kick2 :amp n))
    ))
))

(defn ch11 [beat sec]
  (let [beats (range 0 sec 0.25)
        pattern [0 1 0 1 1 1 0 0 0 0 0 0 0 1 0 1]
        pattern (flatten (repeat (/ sec 4) pattern))
        pairs (map vector beats pattern)]

    (doseq [pair pairs]
      (let [b (first pair) n (last pair)]
        (at (metro (+ b beat)) (closed-hat :amp (* n 0.2)))
    ))
))

(defn ch13 [beat sec]
  (let [amp 1.5
        release 3]

    (at (metro (+ 0.0 beat))
        (mix [(simple-flute :freq (midi->hz 58) :amp amp :release release)
              (simple-flute :freq (midi->hz 60) :amp amp :release release)
              (simple-flute :freq (midi->hz 67) :amp amp :release release)]))

    (at (metro (+ 4.0 beat))
        (mix [(simple-flute :freq (midi->hz 56) :amp amp :release release)
              (simple-flute :freq (midi->hz 58) :amp amp :release release)
              (simple-flute :freq (midi->hz 65) :amp amp :release release)]))

    (at (metro (+ 8.0 beat))
        (mix [(simple-flute :freq (midi->hz 56) :amp amp :release release)
              (simple-flute :freq (midi->hz 58) :amp amp :release release)
              (simple-flute :freq (midi->hz 65) :amp amp :release release)]))

    (at (metro (+ 12.0 beat))
        (mix [(simple-flute :freq (midi->hz 55) :amp amp :release release)
              (simple-flute :freq (midi->hz 57) :amp amp :release release)
              (simple-flute :freq (midi->hz 62) :amp amp :release release)]))
))


