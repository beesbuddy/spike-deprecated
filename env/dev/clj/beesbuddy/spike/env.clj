(ns beesbuddy.spike.env
  (:require
   [beesbuddy.spike.dev-middleware :refer [wrap-dev]]
   [clojure.tools.logging :as log]))

(def defaults
  {:init       (fn []
                 (log/info "\n-=[spike starting using the development or test profile]=-"))
   :start      (fn []
                 (log/info "\n-=[spike started successfully using the development or test profile]=-"))
   :stop       (fn []
                 (log/info "\n-=[spike has shut down successfully]=-"))
   :middleware wrap-dev
   :opts       {:profile       :dev
                :persist-data? true}})
