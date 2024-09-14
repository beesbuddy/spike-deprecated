(ns beesbuddy.spike.web.controller.auth
  (:require [buddy.sign.jwt :as jwt]
            [clj-time.core :as time]
            [integrant.core :as ig]))

(defn login-ok [body] {:status 200 :body body})
(defn login-bad-request [body] {:status 400 :body body})

(defn jwt-sign-in [settings verify-user]
  (let [token-secret (:TOKENSECRET settings)]
    (fn [request]
      (let [username (get-in request [:parameters :body :username])
            password (get-in request [:parameters :body :password])
            valid? (verify-user username password)]
        (if valid?
          (let [claims {:user (keyword username)
                        :exp  (str (time/plus (time/now) (time/seconds 3600)))}
                token (jwt/sign {:claims claims} token-secret)]
            (login-ok {:token token}))
          (login-bad-request {:message "Wrong auth data"}))))))

(defmethod ig/init-key :beesbuddy.spike.web.controller.auth/jwt-sign-in
  [_ {:keys [settings verify-user]}]
  (fn [request] ((jwt-sign-in settings verify-user) request)))