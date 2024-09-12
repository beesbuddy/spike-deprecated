(ns user
  "Userspace functions you can run by default in your local REPL."
  #_{:clj-kondo/ignore [:unused-referred-var]}
  #_{:clj-kondo/ignore [:unused-namespace]}
  (:require [beesbuddy.spike.config :as config]
            [clojure.pprint]
            [clojure.spec.alpha :as s]
            [clojure.tools.namespace.repl :as repl] ;; benchmarking
            [expound.alpha :as expound]
            [integrant.core :as ig]
            [integrant.repl :refer [go reset halt]]
            [kit.api :as kit]
            [lambdaisland.classpath.watch-deps :as watch-deps] ;; hot loading for deps
            [migratus.core :as migratus]
            [portal.api :as p]))

;; uncomment to enable hot loading for deps
(watch-deps/start! {:aliases [:dev :test]})

(alter-var-root #'s/*explain-out* (constantly expound/printer))

;; ----------------------------------------------------------------------------------------
;; Adds tap> to improve data inspection in the REPL.
;; ----------------------------------------------------------------------------------------
(add-tap (bound-fn* clojure.pprint/pprint))

;; ----------------------------------------------------------------------------------------
;; Adds portal to improve data inspection.
;; ----------------------------------------------------------------------------------------
(def portal (p/open))
(add-tap #'p/submit)
;; ----------------------------------------------------------------------------------------

(defn dev-prep!
  []
  (integrant.repl/set-prep! (fn []
                              (-> (config/system-config {:profile :dev})
                                  (ig/prep)))))

(defn test-prep!
  []
  (integrant.repl/set-prep! (fn []
                              (-> (config/system-config {:profile :test})
                                  (ig/prep)))))

;; Can change this to test-prep! if you want to run tests as the test profile in your repl
;; You can run tests in the dev profile, too, but there are some differences between
;; the two profiles.
(def system (dev-prep!))

(def migrator (:migratus/migrator system))

(defn create-migration
  [name]
  (migratus/create migrator name))

(repl/set-refresh-dirs "src/clj")

(def refresh repl/refresh)

(refresh)

(comment
  (go)
  (reset)
  (tap> :hello)
  (-> @portal first))
