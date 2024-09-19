(ns beesbuddy.spike.events
  (:require [beesbuddy.spike.db :refer [default-db]]
            [day8.re-frame.http-fx]
            [re-frame.core :refer [reg-event-fx]]))

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

