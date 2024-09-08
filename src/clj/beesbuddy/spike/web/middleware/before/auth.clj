(ns beesbuddy.spike.web.middleware.before.auth
  (:require
   [buddy.auth.backends :as backends]
   [buddy.auth.middleware :as auth-middleware]
   [integrant.core :as ig]))

(defn jwt-backend [secret] (backends/jws {:secret secret}))

(derive :before-middleware/jwt-auth :before-middleware/auth)

;; TODO: Rename to :before-middleware/wrap-jwt-auth
(defmethod ig/init-key :before-middleware/auth [_ {:keys [token-secret]}]
  (let [secret token-secret]
    (fn [handler]
      (-> handler
          (auth-middleware/wrap-authentication (jwt-backend secret))
          (auth-middleware/wrap-authorization (jwt-backend secret))))))

;(defmethod ig/halt-key! :before-middleware/auth [_ _] nil)


