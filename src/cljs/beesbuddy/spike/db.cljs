(ns beesbuddy.spike.db
  (:require [cljs.reader]))

;; -- Default app-db Value  ---------------------------------------------------
;;
;; When the application first starts, this will be the value put in app-db
;; Look in:
;;   1. `core.cljs` for  "(dispatch-sync [:initialise-db])"
;;   2. `events.cljs` for the registration of :initialise-db handler
;;
(def default-db {:active-page :home :sidebar-open true})
