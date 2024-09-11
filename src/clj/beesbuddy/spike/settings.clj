(ns beesbuddy.spike.settings
  (:require [clojure.string :as str]
            [integrant.core :as ig]))

(defmethod ig/init-key :settings/container
  [_ {:keys [query-fn token-secret]}]
  ;; Fetch settings from the database
  (let [settings (query-fn :get-settings {})
        env-settings {:TOKENSECRET token-secret}
        ;; Convert keys from the database to keywords
        db-settings (into {} (map (fn [{:keys [key value]}]
                                    [(keyword (str/upper-case key)) value])
                                  settings))]
    ;; Merge database settings with environment settings
    (merge db-settings env-settings)))