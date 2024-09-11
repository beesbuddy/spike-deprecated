(ns beesbuddy.spike.web.middleware.before.auth
  (:require
   [buddy.auth.backends :as backends]
   [buddy.auth.middleware :as auth-middleware]
   [integrant.core :as ig]))

(defn jwt-backend [secret] (backends/jws {:secret secret}))

(defmethod ig/init-key :middleware/wrap-jwt-auth [_ {:keys [token-secret]}]
  (let [secret token-secret]
    (fn [handler]
      (-> handler
          (auth-middleware/wrap-authentication (jwt-backend secret))
          (auth-middleware/wrap-authorization (jwt-backend secret))))))

(defmethod ig/halt-key! :middleware/wrap-jwt-auth [_ _] nil)
