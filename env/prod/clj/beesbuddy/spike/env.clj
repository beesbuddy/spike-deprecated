(ns beesbuddy.spike.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init       (fn []
                 (log/info "\n-=[spike starting]=-"))
   :start      (fn []
                 (log/info "\n-=[spike started successfully]=-"))
   :stop       (fn []
                 (log/info "\n-=[spike has shut down successfully]=-"))
   :middleware (fn [handler _] handler)
   :opts       {:profile :prod}})
