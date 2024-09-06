(ns beesbuddy.spike.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
 :active-page          ;; usage: (subscribe [:active-page])
 (fn [db _]            ;; db is the (map) value stored in the app-db atom
   (:active-page db))) ;; extract a value from the application state