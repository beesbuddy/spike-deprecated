(ns beesbuddy.spike.pages
  (:require [beesbuddy.spike.components :refer [card header layout menu
                                                sidebar]]
            [re-frame.core :refer [subscribe]]))

(defn not-found
  []
  [:div
   [:h2 "Not found view"]])

(defn settings
  []
  [:div
   [:h2 "Settings view"]])

(defn home
  [{:keys [url-for]}]
  [:<>
   [:f> card {:url-for url-for}]])

(def pages
  {:home {:title "Home" :page [home {:key "home"}]}
   :settings {:title "Settings" :page [settings {:key "settings"}]}})

(defn get-page
  [name]
  (let [page (get-in pages [name :page] [not-found {:key "not-found"}])]
    [layout (sidebar (menu pages)) header page]))

(defn active-page
  []
  (let [active-page @(subscribe [:active-page])]
    [get-page active-page]))
