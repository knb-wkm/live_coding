;; kicks
(definst kick1
  [freq       {:default 80 :min 50 :max 140 :step 1}
   env-ratio  {:default 3 :min 1.2 :max 8.0 :step 0.1}
   freq-decay {:default 0.02 :min 0.001 :max 1.0 :step 0.001}
   amp-decay  {:default 0.8 :min 0.001 :max 1.2 :step 0.001}]
  (let [fenv (* (env-gen (envelope [env-ratio 1] [freq-decay] :exp)) freq)
        aenv (env-gen (perc 0.005 amp-decay) :action FREE)]
    (* (sin-osc fenv (* 0.5 Math/PI)) aenv)))

(definst kick2 [freq      {:default 80 :min 10 :max 20000 :step 1}
                amp       {:default 0.8 :min 0.001 :max 1.0 :step 0.001}
                mod-freq  {:default 5 :min 0.001 :max 10.0 :step 0.01}
                mod-index {:default 5 :min 0.001 :max 10.0 :step 0.01}
                sustain   {:default 0.4 :min 0.001 :max 1.0 :step 0.001}
                noise     {:default 0.025 :min 0.001 :max 1.0 :step 0.001}]
  (let [pitch-contour (line:kr (* 2 freq) freq 0.02)
        drum (lpf (sin-osc pitch-contour (sin-osc mod-freq (/ mod-index 1.3))) 1000)
        drum-env (env-gen (perc 0.005 sustain) :action FREE)
        hit (hpf (* noise (white-noise)) 500)
        hit (lpf hit (line 6000 500 0.03))
        hit-env (env-gen (perc))]
    (* amp (+ (* drum drum-env) (* hit hit-env)))))

(definst kick3
  [freq {:default 80 :min 40 :max 140 :step 1}
   amp {:default 0.3 :min 0.001 :max 1 :step 0.001}]
  (let [sub-osc   (sin-osc freq)
        sub-env   (line 1 0 0.7 FREE)
        click-osc  (lpf (white-noise) 1500)
        click-env  (line 1 0 0.02)
        sub-out   (* sub-osc sub-env)
        click-out (* click-osc click-env)]
    (* amp (+ sub-out click-out))))

(definst kick4
  [freq   {:default 80 :min 40 :max 140 :step 1}
   amp    {:default 0.3 :min 0.001 :max 1 :step 0.001}
   attack {:default 0.001 :min 0.001 :max 1.0 :step 0.001}
   decay  {:default 0.4 :min 0.001 :max 1 :step 0.001}]
  (let [env (env-gen (perc attack decay) :action FREE)
        snd (sin-osc freq (* Math/PI 0.5))
        snd (* amp env snd)]
    snd))

;; hats
(definst open-hat
  [amp    {:default 0.3 :min 0.001 :max 1 :step 0.01}
   t      {:default 0.3 :min 0.1 :max 1.0 :step 0.01}
   low    {:default 6000 :min 3000 :max 12000 :step 1}
   hi     {:default 2000 :min 1000 :max 8000 :step 1}]
  (let [low (lpf (white-noise) low)
        hi (hpf low hi)
        env (line 1 0 t :action FREE)]
    (* amp env hi)))

(definst closed-hat
  [amp    {:default 0.3 :min 0.001 :max 1 :step 0.01}
   t      {:default 0.1 :min 0.1 :max 1.0 :step 0.01}
   low    {:default 6000 :min 3000 :max 12000 :step 1}
   hi     {:default 2000 :min 1000 :max 8000 :step 1}]
  (let [low (lpf (white-noise) low)
        hi (hpf low hi)
        env (line 1 0 t :action FREE)]
    (* amp env hi)))

(definst closed-hat2
  [amp    {:default 0.3 :min 0.001 :max 1.0 :step 0.001}
   attack {:default 0.001 :min 0.001 :max 1.0 :step 0.0001}
   decay  {:default 0.07 :min 0.001 :max 1.0 :step 0.001}]
  (let [env (env-gen (perc attack decay) 1 1 0 1 FREE)
        noise (white-noise)
        sqr (* (env-gen (perc 0.01 0.04)) (pulse 880 0.2))
        filt (bpf (+ sqr noise) 9000 0.5)]
    (* 0.5 amp env filt)))

;; snares
(definst snare
  [freq   {:default 405 :min 100 :max 1000 :step 1}
   amp    {:default 0.3 :min 0.001 :max 1 :step 0.01}
   sustain {:default 0.1 :min 0.01 :max 1.0 :step 0.001}
   decay  {:default 0.1 :min 0.1 :max 1.0 :step 0.01}
   drum-amp 0.25
   crackle-amp 40
   tightness 1000]
  (let [drum-env  (env-gen (perc 0.005 sustain) :action FREE)
        drum-osc  (mix (* drum-env (sin-osc [freq (* freq 0.53)])))
        drum-s3   (* drum-env (pm-osc (saw (* freq 0.85)) 184 (/ 0.5 1.3)))
        drum      (* drum-amp (+ drum-s3 drum-osc))
        noise     (* 0.1 (lf-noise0 20000))
        noise-env (env-gen (perc 0.005 sustain) :action FREE)
        filtered  (* 0.5 (brf noise 8000 0.1))
        filtered  (* 0.5 (brf filtered 5000 0.1))
        filtered  (* 0.5 (brf filtered 3600 0.1))
        filtered  (* (brf filtered 2000 0.0001) noise-env)
        resonance (* (resonz filtered tightness) crackle-amp)]
    (* amp (+ drum resonance))))

(definst noise-snare
  [freq   {:default 1000 :min 100 :max 10000 :step 1}
   amp    {:default 0.3 :min 0.001 :max 1 :step 0.01}
   decay  {:default 0.1 :min 0.1 :max 1.0 :step 0.01}]
  (let [env (env-gen (perc 0 decay) :action FREE)
        snd (bpf (gray-noise) freq 3)]
    (* snd env amp)))

;; toms
(definst tom
  [freq {:default 90 :min 50 :max 400 :step 1}
   amp {:default 0.3 :min 0.001 :max 1 :step 0.01}
   sustain {:default 0.4 :min 0.01 :max 1.0 :step 0.001}
   mode-level {:default 0.25 :min 0.01 :max 1.0 :step 0.001}
   timbre {:default 1 :min 0.1 :max 5.0 :step 0.1}
   stick-level {:default 0.2 :min 0.0 :max 1.0 :step 0.1}]
  (let [env (env-gen (perc 0.005 sustain) :action FREE)
        s1 (* 0.5 env (sin-osc (* freq 0.8)))
        s2 (* 0.5 env (sin-osc freq))
        s3 (* 5 env (sin-osc (saw (* 0.9 freq))
                             (* (sin-osc (* freq 0.85))
                                (/ timbre 1.3))))
        mix (* mode-level (+ s1 s2 s3))
        stick (* stick-level
                 (env-gen (perc 0.001 0.01))
                 (crackle 2.0))
        mix2 (* amp (+ mix stick))]
    mix2))

;; percussive
(definst clap
  [low {:default 7500 :min 100 :max 10000 :step 1}
   hi  {:default 1500 :min 100 :max 10000 :step 1}
   amp {:default 0.3 :min 0.001 :max 1 :step 0.01}
   decay {:default 0.6 :min 0.1 :max 0.8 :step 0.001}]
  (let [noise      (bpf (lpf (white-noise) low) hi)
        clap-env   (line 1 0 decay :action FREE)
        noise-envs (map #(envelope [0 0 1 0] [(* % 0.01) 0 0.04]) (range 8))
        claps      (apply + (* noise (map env-gen noise-envs)))]
    (* amp (* claps clap-env))))

(definst haziti-clap
  [freq   {:default 44.77 :min 20 :max 400 :step 1}
   attack {:default 0.036 :min 0.00001 :max 2 :step 0.0001}
   decay  {:default 1.884 :min 0.00001 :max 2 :step 0.0001}
   rq     {:default 0.08 :min 0.01 :max 1 :step 0.01}
   amp    {:default 0.8 :min 0.01 :max 1 :step 0.01}]
  (let [noiz (white-noise)
        bfreq (* freq (abs (lf-noise0 80)))
        filt (* 4 (bpf (rhpf noiz 4064.78 rq) bfreq (* 1 rq)))
        env  (x-line 1 0.001 decay :action FREE)
        wave (lf-tri (* (abs (lf-noise0:kr 699)) 4400))
        wenv (env-gen (perc 0.00001 0.008))
        skip (* wave wenv)]
    (* amp (+ (* env filt) skip))))

