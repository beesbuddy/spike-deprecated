(ns beesbuddy.spike.web.service.settings
  (:require [clojure.edn :as edn]
            [clojure.string :as str]
            [integrant.core :as ig]))

(defmulti parse-setting (fn [type _] type))

(defmethod parse-setting :int
  [_ value]
  (Integer/parseInt value))

(defmethod parse-setting :string
  [_ value]
  value)

(defmethod parse-setting :edn
  [_ value]
  (edn/read-string value))

(defmethod ig/init-key :beesbuddy.spike.web.service.settings/db-settings
  [_ {:keys [query-fn token-secret]}]
  ;; Fetch settings from the database
  (let [settings (query-fn :get-settings {})
        env-settings {:TOKENSECRET token-secret}
        ;; Convert keys from the database to keywords
        db-settings (into {} (map (fn [{:keys [key value type]}]
                                    [(keyword (str/upper-case key)) (parse-setting (keyword (str/lower-case type)) value)])
                                  settings))]
    ;; Merge database settings with environment settings
    (merge db-settings env-settings)))
