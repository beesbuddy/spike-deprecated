(ns beesbuddy.spike.db
  (:require [cljs.reader]
            [re-frame.core :refer [reg-cofx]]))

;; -- Default app-db Value  ---------------------------------------------------
;;
;; When the application first starts, this will be the value put in app-db
;; Look in:
;;   1. `core.cljs` for  "(dispatch-sync [:initialise-db])"
;;   2. `events.cljs` for the registration of :initialise-db handler
;;
(def default-db {:active-page :home})
