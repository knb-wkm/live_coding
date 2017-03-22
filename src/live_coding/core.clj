(ns live-coding.core
  (:use
   [clojure.pprint]
   [overtone.live])
  (:require [shadertone.tone :as s]
))

;; metronome
(def metro (metronome 110))

(load-file "src/live_coding/drums.clj")
(load-file "src/live_coding/synths.clj")
(load-file "src/live_coding/channels.clj")

;; start shader
(s/start "src/live_coding/shaders/unknown_pleasures.glsl"
         :width 1400 :height 500)
(s/start-fullscreen "src/live_coding/shaders/unknown_pleasures.glsl"
         :width 2600 :height 1400)

(s/stop)

;; sec 16 count_by 0.25 = 64
(defn player [beat]
  (let [term 32]
    ;; (ch1 beat term)
    ;; (ch2 beat term)
    ;; (ch3 beat term)
    ;; (ch4 beat term)

    ;; (ch10 beat term)
    ;; (ch11 beat term)
    ;; (ch12 beat term)
    ;; (ch13 beat term)

    ;; (ch20 beat term)
    ;; (ch21 beat term)

    ;; (ch30 beat term)
    ;; (ch31 beat term)
    ;; (ch32 beat term)
    ;; (ch33 beat term)

    ;; (ch40 beat term)
    ;; (ch41 beat term)
    (apply-by (metro (+ term beat)) #'player (+ term beat) [])
))

(load-file "src/live_coding/channels.clj")
(player (metro))
(stop)

(snare)

;; neta
(bubbles)
(kill bubbles)

(telmin1)
(kill telmin1)

(ping :note 60)
(ping :note 64)
(ping :note 67)
(ping :note 69)
(ping :note 67)
(ping :note 60)
(count [60 0 64 0 67 0 69 67 60 ])
(demo 10 (sin-osc 440))
(demo 1 (saw 440))
(demo 1 (white-noise))
