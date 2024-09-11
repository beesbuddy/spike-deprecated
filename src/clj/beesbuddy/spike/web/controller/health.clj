(ns beesbuddy.spike.web.controller.health
  (:require
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
