 (ns beesbuddy.spike.subs
   (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
 :active-page
 (fn [db _]
   (:active-page db)))

(reg-sub :sidebar-open
         (fn [db _]
           (:sidebar-open db)))