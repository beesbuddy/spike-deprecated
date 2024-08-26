(ns beesbuddy.spike.events
  (:require
   [beesbuddy.spike.db :refer [default-db]]
   [re-frame.core :refer [reg-event-db reg-event-fx reg-fx inject-cofx trim-v after path]]
   [beesbuddy.spike.router :as router]
   [day8.re-frame.http-fx]                                 ;; registers the :http-xhrio effect handler with re-frame
   [ajax.core :refer [json-request-format json-response-format]]
   [clojure.string :as str]
   [cljs.reader :as rdr]))

;; -- Event Handlers ----------------------------------------------------------
;;
(reg-event-fx
 :initialise-db [] (fn [] {:db default-db}))

(reg-event-fx :set-active-page []
              (fn [{:keys [db]} [_ {:keys [page _slug _profile _favorited]}]]
                (let [set-page (assoc db :active-page page)]
                  (case page
                    :home {:db set-page}
                    (:login :logout :register :settings) {:db set-page}))))

