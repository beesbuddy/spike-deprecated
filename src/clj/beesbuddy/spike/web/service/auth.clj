(ns beesbuddy.spike.web.service.auth
  (:require [buddy.hashers :as hashers]
            [integrant.core :as ig]))

(defn- hash-password [password]
  (hashers/derive password))

(defn- verify-password [password hash]
  (try
    (hashers/check password hash)
    (catch Exception _e false)))

(defn- find-user [query-fn username]
  (query-fn :find-user-by-username {:username username}))

(defmethod ig/init-key :beesbuddy.spike.web.service.auth/verify-user
  [_ {:keys [query-fn]}]
  (fn [username, password] (let [user (find-user query-fn username)
                                 hash (get-in user [:password])]
                             (verify-password password hash))))

(defmethod ig/init-key :beesbuddy.spike.web.service.auth/store-user
  [_ {:keys [query-fn]}]
  (fn [username, password]
    (query-fn :store-user username (hash-password password))))