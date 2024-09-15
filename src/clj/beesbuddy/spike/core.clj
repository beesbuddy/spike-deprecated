(ns beesbuddy.spike.core
  (:require [beesbuddy.spike.config :as config]
            [beesbuddy.spike.env :refer [defaults]]
            [beesbuddy.spike.web.handler] ;; Routes
            [beesbuddy.spike.web.routes.api]
            [beesbuddy.spike.web.routes.pages]
            [beesbuddy.spike.web.routes.ws]
            [clojure.tools.logging :as log]
            [integrant.core :as ig]
            [kit.edge.db.sql.conman]
            [kit.edge.db.sql.migratus]
            [kit.edge.http.hato] ;; Edges       
            [kit.edge.server.undertow])
  (:gen-class))

;; log uncaught exceptions in threads
(Thread/setDefaultUncaughtExceptionHandler
 (reify Thread$UncaughtExceptionHandler
   (uncaughtException [_ thread ex]
     (log/error {:what :uncaught-exception
                 :exception ex
                 :where (str "Uncaught exception on" (.getName thread))}))))

(defonce system (atom nil))

(defn stop-app []
  ((or (:stop defaults) (fn [])))
  (some-> (deref system) (ig/halt!))
  (shutdown-agents))

(defn start-app [& [params]]
  ((or (:start params) (:start defaults) (fn [])))
  (->> (config/system-config (or (:opts params) (:opts defaults) {}))
       (ig/prep)
       (ig/init)
       (reset! system))
  (.addShutdownHook (Runtime/getRuntime) (Thread. stop-app)))

(defn -main [& _]
  (start-app))
