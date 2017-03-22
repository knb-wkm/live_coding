(ns live-coding.core)

(demo 5
      (bpf (saw 200) (mouse-x 40 5000) (mouse-y 0.01 1))
)

(demo 5
      (pluck
       (* (sin-osc 440) (env-gen (perc 0.001 20) :action FREE))
       0.1 0.1
       0.01
        )
       )

(demo 1
      (* (env-gen (lin 0.01 0.4 0.1))
         (sin-osc 440))
      )

(demo 1
      (* (env-gen (lin 0.01 0.4 0.1))
         (pink-noise))
      )

(demo 1
      (* (env-gen (perc 0.0001 10))
         (sin-osc 440))
      )

(chord :c4 :major)
(recording-start "~/Desktop/foo.wav")
(recording-stop)

(def server (osc-server 44100 "osc-clj"))

(stop)


(demo 10
      (let [wave1 (pulse 440)
            wave2 (pulse 880)
            wave3 (pulse 440)
            mixed-wave [wave1 wave2 wave3]
            env1 (env-gen (perc 0.2 5))
            sig (* env1 (mix mixed-wave))
            gate (pulse (* 2 (+ 1 (sin-osc:kr 0.05))))
            comp (compander sig gate 0.1 1 0.01 0.1 0.01)
            comp-lpf (lpf comp 1000)
            comp-hpf (hpf comp 100)]
        
        comp-lpf
       )
)

(demo 10
      (let [wave1 (saw 440)
            env1 (env-gen (adsr 0.1 3.3 0.4 0.8 1 0.1) :gate 1 :action FREE)
            env2 (env-gen (lin 0.01 0.4 0.1))]
        
        (* env2 wave1)
        )
)
