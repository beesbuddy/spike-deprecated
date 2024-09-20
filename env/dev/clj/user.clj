(ns user
  "Userspace functions you can run by default in your local REPL."
  (:require [beesbuddy.spike.config :as config]
            [beesbuddy.spike.mulog-events :as mulog-events]
            [clojure.pprint]
            [clojure.spec.alpha :as s]
            [clojure.tools.namespace.repl :as repl] ;; benchmarking
            [com.brunobonacci.mulog :as mulog]
            [expound.alpha :as expound]
            [integrant.core :as ig]
            [integrant.repl :refer [go reset]]
            [integrant.repl.state :as state]
            [lambdaisland.classpath.watch-deps :as watch-deps] ;; hot loading for deps
            [migratus.core]
            [portal.api :as inspect]))

;; uncomment to enable hot loading for deps
(watch-deps/start! {:aliases [:dev :test]})

(alter-var-root #'s/*explain-out* (constantly expound/printer))

;; ----------------------------------------------------------------------------------------
;; Adds tap> to improve data inspection in the REPL.
;; ----------------------------------------------------------------------------------------
(add-tap (bound-fn* clojure.pprint/pprint))

(defn dev-prep!
  []
  (integrant.repl/set-prep! (fn []
                              (-> (config/system-config {:profile :dev})
                                  (ig/prep)))))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn test-prep!
  []
  (integrant.repl/set-prep! (fn []
                              (-> (config/system-config {:profile :test})
                                  (ig/prep)))))

;; Can change this to test-prep! if you want to run tests as the test profile in your repl
;; You can run tests in the dev profile, too, but there are some differences between
;; the two profiles.
(dev-prep!)

(repl/set-refresh-dirs "src/clj")

(def refresh repl/refresh)

(refresh)

(comment
  (go)
  (reset)
  (def query-fn (:db.sql/query-fn state/system))
  (query-fn :store-user "test" "test")
  (query-fn :find-user-by-username {:username "test"})
  (def migrator (:migratus/migrator state/system))
  (migratus.core/create migrator "add-table")
  (migratus.core/rollback migrator)
  (migratus.core/migrate migrator)
  ;; ---------------------------------------------------------
  ;; Start Portal and capture all evaluation results

  ;; Open Portal window in browser with dark theme
  ;; https://cljdoc.org/d/djblue/portal/0.37.1/doc/ui-concepts/themes
  ;; Portal options:
  ;; - light theme {:portal.colors/theme :portal.colors/solarized-light}
  ;; - dark theme  {:portal.colors/theme :portal.colors/gruvbox}
  (def portal
    "Open portal window if no portal sessions have been created.
    A portal session is created when opening a portal window"
    (or (seq (inspect/sessions))
        (inspect/open {:portal.colors/theme :portal.colors/gruvbox})))
  (add-tap #'inspect/submit)
  (tap> :hello)
  (mulog-events/stop)
  ;; ----------------------------------------------------------------------------------------
  ;; Example mulog event message
  ;; ----------------------------------------------------------------------------------------
  (mulog/log ::dev-user-ns :message "Example event message" :ns (ns-publics *ns*))
  (-> @portal first))
