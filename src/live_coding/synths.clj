(ns live-coding.core)

(definst simple-flute [freq 880
                       amp 0.5
                       attack 0.4
                       decay 0.5
                       sustain 0.8
                       release 1
                       gate 1
                       out 0]
  (let [env  (env-gen (adsr attack decay sustain release) gate :action FREE)
        mod1 (lin-lin:kr (sin-osc:kr 6) -1 1 (* freq 0.99) (* freq 1.01))
        mod2 (lin-lin:kr (lf-noise2:kr 1) -1 1 0.2 1)
        mod3 (lin-lin:kr (sin-osc:kr (ranged-rand 4 6)) -1 1 0.5 1)
        sig (distort (* env (sin-osc [freq mod1])))
        sig (* amp sig mod2 mod3)
        env2 (env-gen (perc attack release))
        sig (* env2 sig)]
    sig))

(definst supersaw [freq 440 amp 1 attack 0.1 release 0.3]
  (let [input  (lf-saw freq)
        shift1 (lf-saw 4)
        shift2 (lf-saw 7)
        shift3 (lf-saw 5)
        shift4 (lf-saw 2)
        comp1  (> input shift1)
        comp2  (> input shift2)
        comp3  (> input shift3)
        comp4  (> input shift4)
        output (+ (- input comp1) (- input comp2) (- input comp3) (- input comp4))
        output (- output input)
        output (leak-dc:ar (* output 0.25))
        env (env-gen (perc 0.1 0.3))
        output (* env output)]
    (* amp output)))

(definst ping
  [note   {:default 72   :min 0     :max 120 :step 1}
   attack {:default 0.02 :min 0.001 :max 1   :step 0.001}
   decay  {:default 0.3  :min 0.001 :max 1   :step 0.001}
   amp 0.8]
  (let [snd (sin-osc (midicps note))
        env (env-gen (perc attack decay) :action FREE)]
    (* amp env snd)))

(definst tb303
  [note       {:default 60 :min 0 :max 120 :step 1}
   wave       {:default 1 :min 0 :max 2 :step 1}
   r          {:default 0.8 :min 0.01 :max 0.99 :step 0.01}
   attack     {:default 0.01 :min 0.001 :max 4 :step 0.001}
   decay      {:default 0.1 :min 0.001 :max 4 :step 0.001}
   sustain    {:default 0.6 :min 0.001 :max 0.99 :step 0.001}
   release    {:default 0.01 :min 0.001 :max 4 :step 0.001}
   cutoff     {:default 100 :min 1 :max 20000 :step 1}
   env-amount {:default 0.01 :min 0.001 :max 4 :step 0.001}
   amp        {:default 2 :min 0 :max 5 :step 0.01}]
  (let [freq       (midicps note)
        freqs      [freq (* 1.01 freq)]
        vol-env    (env-gen (adsr attack decay sustain release)
                            (line:kr 1 0 (+ attack decay release))
                            :action FREE)
        fil-env    (env-gen (perc))
        fil-cutoff (+ cutoff (* env-amount fil-env))
        waves      (* vol-env
                      [(saw freqs)
                       (pulse freqs 0.5)
                       (lf-tri freqs)])
        selector   (select wave waves)
        filt       (rlpf selector fil-cutoff r)]
    (* amp filt)))

(definst rise-fall-pad
  [freq 440 t 4 amt 0.3 amp 0.8]
  (let [f-env      (env-gen (perc t t) 1 1 0 1 FREE)
        src        (saw [freq (* freq 1.01)])
        signal     (rlpf (* 0.3 src)
                         (+ (* 0.6 freq) (* f-env 2 freq)) 0.2)
        k          (/ (* 2 amt) (- 1 amt))
        distort    (/ (* (+ 1 k) signal) (+ 1 (* k (abs signal))))
        gate       (pulse (* 2 (+ 1 (sin-osc:kr 0.05))))
        compressor (compander distort gate 0.01 1 0.5 0.01 0.01)
        dampener   (+ 1 (* 0.5 (sin-osc:kr 0.5)))
        reverb     (free-verb compressor 0.5 0.5 dampener)
        echo       (comb-n reverb 0.4 0.3 0.5)]
    (* amp echo)))

(definst overpad
  [note 60 amp 0.7 attack 0.001 release 2]
  (let [freq  (midicps note)
        env   (env-gen (perc attack release) :action FREE)
        f-env (+ freq (* 3 freq (env-gen (perc 0.012 (- release 0.1)))))
        bfreq (/ freq 2)
        sig   (apply +
                     (concat (* 0.7 (sin-osc [bfreq (* 0.99 bfreq)]))
                             (lpf (saw [freq (* freq 1.01)]) f-env)))
        audio (* amp env sig)]
    audio))

(definst bass
  [freq 120 t 0.6 amp 0.8]
  (let [env  (env-gen (perc 0.08 t) :action FREE)
        src  (saw [freq (* 0.98 freq) (* 2.015 freq)])
        src  (clip2 (* 1.3 src) 0.8)
        sub  (sin-osc (/ freq 2))
        filt (resonz (rlpf src (* 4.4 freq) 0.09) (* 2.0 freq) 2.9)]
    (* env amp (fold:ar (distort (* 1.3 (+ filt sub))) 0.08))))

(definst vintage-bass
  [note 40 velocity 80 t 0.6 amp 1 gate 1 release 1]
  (let [freq     (midicps note)
        sub-freq (midicps (- note 12))
        velocity (/ velocity 127.0)
        sawz1    (* 0.275 (saw [freq (* 1.01 freq)]))
        sawz2    (* 0.75 (saw [(- freq 2) (+ 1 freq)]))
        sqz      (* 0.3 (pulse [sub-freq (- sub-freq 1)]))
        mixed    (* 5 (+ sawz1 sawz2 sqz))
        env      (env-gen (adsr 0.1 3.3 0.4 0.8) gate :action FREE)
        filt     (* env (moog-ff mixed (* velocity env (+ freq 200)) 2.2))
        open-env (env-gen (perc 0.1 release) :action FREE)
        filt     (* open-env filt)]
    (* amp filt)))


; B3 modeled a church organ using additive synthesis of 9 sin oscillators
; * Octave under root
; *	Fifth over root
; * Root
; * Octave over root
; * Octave and a fifth over root
; * Two octaves over root
; * Two octaves and a major third over root
; * Two octaves and a fifth over root
; * Three octaves over root
; Work in progress...  just getting started
(comment definst b3
  [note 60 a 0.01 d 3 s 1 r 0.01]
  (let [freq  (midicps note)
        waves (sin-osc [(* 0.5 freq)
                        freq
                        (* (/ 3 2) freq)
                        (* 2 freq)
                        (* freq 2 (/ 3 2))
                        (* freq 2 2)
                        (* freq 2 2 (/ 5 4))
                        (* freq 2 2 (/ 3 2))
                        (* freq 2 2 2)])
        snd   (apply + waves)
        env   (env-gen (adsr a d s r) :action FREE)]
    (* env snd 0.1)))

; Experimenting with Karplus Strong synthesis...
(definst ks1
  [note  {:default 60  :min 10   :max 120  :step 1}
   amp   {:default 0.8 :min 0.01 :max 0.99 :step 0.01}
   dur   {:default 2   :min 0.1  :max 4    :step 0.1}
   decay {:default 30  :min 1    :max 50   :step 1}
   coef  {:default 0.3 :min 0.01 :max 2    :step 0.01}]
  (let [freq (midicps note)
        noize (* 0.8 (white-noise))
        dly (/ 1.0 freq)
        plk   (pluck noize 1 (/ 1.0 freq) dly
                     decay
                     coef)
        dist (distort plk)
        filt (rlpf dist (* 12 freq) 0.6)
        clp (clip2 filt 0.8)
        reverb (free-verb clp 0.4 0.8 0.2)]
    (* amp (env-gen (perc 0.0001 dur) :action FREE) reverb)))

(definst ks1-demo
  [note 60 amp 0.8 gate 1]
  (let [freq (midicps note)
        noize (* 0.8 (white-noise))
        dly (/ 1.0 freq)
        plk   (pluck noize gate (/ 1.0 freq) dly
                     (mouse-x 0.1 50)
                     (mouse-y 0.0001 0.9999))
        dist (distort plk)
        filt (rlpf dist (* 12 freq) 0.6)
        reverb (free-verb filt 0.4 0.8 0.2)]
    (* amp (env-gen (perc 0.0001 2) :action FREE) reverb)))

(definst ks-stringer
  [freq 440 rate 6]
  (let [noize (* 0.8 (white-noise))
        trig  (dust rate)
        coef  (mouse-x -0.999 0.999)
        delay (/ 1.0 (* (mouse-y 0.001 0.999) freq))
        plk   (pluck noize trig (/ 1.0 freq) delay 10 coef)
        filt (rlpf plk (* 12 freq) 0.6)]
    (* 0.8 filt)))

(definst fm-demo
  [note 60 amp 0.2 gate 0]
  (let [freq (midicps note)
        osc-a (* (sin-osc (mouse-x 20 3000))
                 0.3)
        osc-b (* amp (sin-osc (* (mouse-y 3000 0) osc-a)))]
    osc-a))

; From the SC2 examples included with SC
; Don't think it's quite there, but almost...
(definst harmonic-swimming
  [amp 0.5]
  (let [freq     100
        partials 20
        z-init   0
        offset   (line:kr 0 -0.02 60)
        snd (loop [z z-init
                   i 0]
              (if (= partials i)
                z
                (let [f (clip:kr (mul-add
                                   (lf-noise1:kr [(+ 6 (rand 4))
                                                  (+ 6 (rand 4))])
                                   0.2 offset))
                      src  (f-sin-osc (* freq (inc i)))
                      newz (mul-add src f z)]
                  (recur newz (inc i)))))]
    (out 10 (pan2 (* amp snd)))))

(definst whoahaha
  [freq 440 dur 5 osc 100 mul 1000 amp 1]
  (let [freqs [freq (* freq 1.0068) (* freq 1.0159)]
        sound (resonz (saw (map #(+ % (* (sin-osc osc) mul)) freqs))
                      (x-line 10000 10 25)
                      (line 1 0.05 25))
        sound (apply + sound)]
    (* amp
       (* (lf-saw:kr (line:kr 13 17 3)) (line:kr 1 0 dur FREE) sound))
))

(definst bubbles
  [bass-freq 80]
  (let [bub (+ bass-freq (* 3 (lf-saw:kr [8 7.23])))
        glis (+ bub (* 24 (lf-saw:kr 0.4 0)))
        freq (midicps glis)
        src (* 0.04 (sin-osc freq))
        zout (comb-n src :decay-time 4)]
    zout))

(definst violin
  [pitch   {:default 60  :min 0   :max 127 :step 1}
   amp     {:default 1.0 :min 0.0 :max 1.0 :step 0.01}
   gate    {:default 1   :min 0   :max 1   :step 1}
   out-bus {:default 0   :min 0   :max 127 :step 1}
   release 3]
  
  (let [freq   (midicps pitch)
        ;; 3b) portamento to change frequency slowly
        freqp  (slew:kr freq 100.0 100.0)
        ;; 3a) vibrato to make it seem "real"
        freqv  (vibrato :freq freqp :rate 6 :depth 0.02 :delay 1)
        ;; 1) the main osc for the violin
        saw    (saw freqv)
        ;; 2) add an envelope for "bowing"
        saw0   (* saw (env-gen (adsr 1.5 1.5 0.8 1.5) :gate gate :action FREE))
        ;; a low-pass filter prior to our filter bank
        saw1   (lpf saw0 4000) ;; freq???
        ;; 4) the "formant" filters
        band1  (bpf saw1 300 (/ 3.5))
        band2  (bpf saw1 700 (/ 3.5))
        band3  (bpf saw1 3000 (/ 2))
        saw2   (+ band1 band2 band3)
        ;; a high-pass filter on the way out
        saw3   (hpf saw2 30)
        env   (env-gen (perc 0.01 release))]
    ;; (out out-bus (pan2 (* amp saw3)))))
    (* amp (* env saw3))))

(definst telmin1 [amp 0.7]
  (* amp
     (mix (sin-osc [(mouse-x 100 400 EXP)
                    (mouse-x 200 500 EXP)
                    (mouse-y 400 600 EXP)
                    (mouse-y 500 700 EXP)]))))


(definst random-sin [amp 1 release 10]
  (let [wave (sin-osc (+ 1000 (* 1600 (lf-noise0:kr 12))))
        env (env-gen (perc 0.1 release))]

    (* amp (* env wave))
))
  

; // Originally from the STK instrument models...
(comment definst bowed
  [note 60 velocity 80 gate 1 amp 1
   bow-offset 0 bow-slope 0.5 bow-position 0.75 vib-freq 6.127 vib-gain 0.2]
  (let [freq         (midicps note)
        velocity     (/ velocity 127)
        beta-ratio   (+ 0.027236 (* 0.2 bow-position))
        base-delay   (reciprocal freq)
        [fb1 fb2]    (local-in 2)
        vibrato      (* (sin-osc vib-freq) vib-gain)
        neck-delay   (+ (* base-delay (- 1 beta-ratio)) (* base-delay vibrato))
        neck         (delay-l fb1 0.05 neck-delay)
        nut-refl     (neg neck)
        bridge       (delay-l fb2 0.025 (* base-delay beta-ratio))
        string-filt  (one-pole (* bridge 0.95) 0.55)
        bridge-refl  (neg string-filt)
        adsr         (* amp (env-gen (adsr 0.02 3.005 1.0 0.01) gate :action FREE))
        string-vel   (+ bridge-refl nut-refl)
        vel-diff     (- adsr string-vel)
        slope        (- 5.0 (* 4 bow-slope))
        bow-table    (clip:ar (pow (abs (+ (* (+ vel-diff bow-offset) slope) 0.75 )) -4) 0 1)
        new-vel       (* vel-diff bow-table)]
   (local-out (+ [bridge-refl nut-refl] new-vel))
   (resonz (* bridge 0.5) 500 0.85)))

(comment definst flute
  [gate 1 freq 440 amp 1.0 endreflection 0.5 jetreflection 0.5
   jetratio 0.32 noise-gain 0.15 vibfreq 5.925 vib-gain 0.0 amp 1.0]
  (let [nenv           (env-gen (linen 0.2 0.03 0.5 0.5) gate :action FREE)
        adsr           (+ (* amp 0.2) (env-gen (adsr 0.005 0.01 1.1 0.01) gate :action FREE))
        noise          (* (white-noise) noise-gain)
        vibrato        (sin-osc vibfreq 0 vib-gain)
        delay          (reciprocal (* freq 0.66666))
        lastout        (local-in 1)
        breathpressure (* adsr (+ noise, vibrato))
        filter         (leak-dc (one-pole (neg lastout) 0.7))
        pressurediff   (- breathpressure (* jetreflection filter))
        jetdelay       (delay-l pressurediff 0.025 (* delay jetratio))
        jet            (clip2 (* jetdelay (- (squared jetdelay) 1.0)) 1.0)
        boredelay      (delay-l (+ jet (* endreflection filter) 0.05 delay))]
    (local-out boredelay)
    (* 0.3 boredelay amp nenv)))


