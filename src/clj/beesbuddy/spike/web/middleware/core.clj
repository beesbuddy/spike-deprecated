(ns beesbuddy.spike.web.middleware.core
  (:require [beesbuddy.spike.env :as env]
            [ring.middleware.defaults :as defaults]
            [ring.middleware.session.cookie :as cookie]
            [ring.util.http-response :as http-response]))


(defn wrap-redirect-to-home [handler]
  (fn [request]
    (let [response (handler request)]
      (if (= 404 (:status response))
        (http-response/temporary-redirect "/")
        response))))

(defn wrap-base
  [{:keys [_metrics site-defaults-config cookie-secret] :as opts}]
  (let [cookie-store (cookie/cookie-store {:key (.getBytes ^String cookie-secret)})]
    (fn [handler]
      (cond-> ((:middleware env/defaults) handler opts)
              true (defaults/wrap-defaults
                     (assoc-in site-defaults-config [:session :store] cookie-store))
              ;; redirect to home page if not found
              false (wrap-redirect-to-home)))))