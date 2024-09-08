(ns beesbuddy.spike.web.middleware.after.redirect
  (:require [ring.util.http-response :as http-response]))

(defn wrap-redirect-to-home [handler]
  (fn [request]
    (let [response (handler request)]
      (if (= 404 (:status response))
        (http-response/temporary-redirect "/")
        response))))