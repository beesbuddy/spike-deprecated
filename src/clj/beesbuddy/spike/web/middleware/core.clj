(ns beesbuddy.spike.web.middleware.core
  (:require [beesbuddy.spike.env :as env]
            [beesbuddy.spike.web.middleware.after.redirect :refer [wrap-redirect-to-home]]
            [ring.middleware.defaults :as defaults]
            [ring.middleware.session.cookie :as cookie]))

(defn wrap-base
  [{:keys [_metrics site-defaults-config cookie-secret] :as opts}]
  (let [cookie-store (cookie/cookie-store {:key (.getBytes ^String cookie-secret)})]
    (fn [handler]
      (cond-> ((:middleware env/defaults) handler opts)
        true (defaults/wrap-defaults
              (assoc-in site-defaults-config [:session :store] cookie-store))
              ;; redirect to home page if not found
        true (wrap-redirect-to-home)))))