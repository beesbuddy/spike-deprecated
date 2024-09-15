(ns beesbuddy.spike.web.controller.health
  (:require
   [integrant.core :as ig]
   [ring.util.http-response :as http-response])
  (:import
   (java.lang.management ManagementFactory)
   [java.util Date]))

(defn healthcheck!
  [_req]
  (http-response/ok
   {:time     (str (Date. (System/currentTimeMillis)))
    :up-since (str (Date. (.getStartTime (ManagementFactory/getRuntimeMXBean))))
    :app      {:status  "up"
               :message ""}}))

(defmethod ig/init-key :beesbuddy.spike.web.controller.health/healthcheck!
  [_ _]
  (fn [request] (healthcheck! request)))
