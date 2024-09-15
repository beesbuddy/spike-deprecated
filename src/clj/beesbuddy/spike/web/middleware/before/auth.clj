(ns beesbuddy.spike.web.middleware.before.auth
  (:require
   [buddy.auth.backends :as backends]
   [buddy.auth.middleware :as auth-middleware]
   [integrant.core :as ig]))

(defn- jwt-backend [secret] (backends/jws {:secret secret}))

(defn wrap-jwt-auth [settings]
  (let [token-secret (:TOKENSECRET settings)]
    (fn [handler]
      (-> handler
          (auth-middleware/wrap-authentication (jwt-backend token-secret))
          (auth-middleware/wrap-authorization (jwt-backend token-secret))))))

(defmethod ig/init-key :beesbuddy.spike.web.middleware.before.auth/wrap-jwt-auth
  [_ {:keys [settings]}]
  (wrap-jwt-auth settings))