(ns beesbuddy.spike.web.controller.auth
  (:require [buddy.sign.jwt :as jwt]
            [clj-time.core :as time]
            [integrant.core :as ig]))

(def auth-data
  {:admin "secret"
   :user  "secret"})

(defn login-ok [body] {:status 200 :body body})
(defn login-bad-request [body] {:status 400 :body body})


(defn jwt-sign-in [settings]
  (let [token-secret (:TOKENSECRET settings)]
    (fn [request]
      (let [username (get-in request [:parameters :body :username])
            password (get-in request [:parameters :body :password])
            valid? (some-> auth-data
                           (get (keyword username))
                           (= password))]
        (if valid?
          (let [claims {:user (keyword username)
                        :exp  (str (time/plus (time/now) (time/seconds 3600)))}
                token (jwt/sign {:claims claims} token-secret)]
            (login-ok {:token token}))
          (login-bad-request {:message "Wrong auth data"}))))))
