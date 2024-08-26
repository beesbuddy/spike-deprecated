(ns beesbuddy.spike.core
  (:require
   [reagent.dom :as d]
   [re-frame.core :refer [dispatch-sync]]
   [beesbuddy.spike.router :as router]
   [beesbuddy.spike.events]
   [beesbuddy.spike.subs]
   [beesbuddy.spike.views]))

;; -------------------------
;; Initialize app
;; -------------------------
(defn ^:dev/after-load mount-root []
  ;; Router config can be found within `./router.cljs`. Here we are just hooking
  ;; up the router on start
  (router/start!)

  ;; Put an initial value into app-db.
  ;; The event handler for `:initialise-db` can be found in `events.cljs`
  ;; Using the sync version of dispatch means that value is in
  ;; place before we go onto the next step.
  (dispatch-sync [:initialise-db])

  ;; Render the UI into the HTML's <div id="app" /> element
  ;; The view function `conduit.views/conduit-app` is the
  ;; root view for the entire UI.
  (d/render [beesbuddy.spike.views/app]
            (.getElementById js/document "app")))

(defn ^:export ^:dev/once init! []
  (mount-root))
