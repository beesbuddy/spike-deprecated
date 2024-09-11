(ns beesbuddy.spike.events
  #_{:clj-kondo/ignore [:unused-namespace]}
  #_{:clj-kondo/ignore [:unused-referred-var]}
  (:require
   [ajax.core :refer [json-request-format json-response-format]]
   [beesbuddy.spike.db :refer [default-db]]
   [beesbuddy.spike.router :as router]
   [cljs.reader :as rdr]
   [clojure.string :as str]
   [day8.re-frame.http-fx]
   [re-frame.core :refer [reg-event-db reg-event-fx reg-fx inject-cofx trim-v after path]]))

;; -- Event Handlers ----------------------------------------------------------
;;
(reg-event-fx
 :initialise-db [] (fn [] {:db default-db}))

(reg-event-fx :set-active-page []
              (fn [{:keys [db]} [_ {:keys [page]}]]
                (let [set-page (assoc db :active-page page)]
                  (case page
                    :home {:db set-page}
                    (:login :logout :register :settings) {:db set-page}))))

(reg-event-fx :set-sidebar-open []
              (fn [{:keys [db]} [_ {:keys [open]}]]
                {:db (assoc db :sidebar-open open)}))

