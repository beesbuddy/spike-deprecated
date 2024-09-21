(ns beesbuddy.spike.pages
  (:require ["@mui/material" :as mui]
            ["@mui/material/Card" :default MuiCard]
            ["@mui/material/styles" :refer [useTheme]]
            ["@mui/system" :refer [styled]]
            [beesbuddy.spike.components :refer [header layout menu sidebar]]
            [beesbuddy.spike.router :refer [set-page! url-for]]
            [beesbuddy.spike.utils :refer [defn-mui-styled]]
            [re-frame.core :refer [subscribe]]))

#_{:clj-kondo/ignore [:unused-binding]}
(defn-mui-styled card-styles
  :padding (fn [theme] (.spacing theme 4)))

(defn text-button [label click-handler]
  [:> mui/Button {:variant "outlined" :on-click click-handler} label])

(defn card []
  (let [theme (useTheme)]
    [:> ((styled MuiCard)
         card-styles) {:theme theme}
     [:div {:class "text-3xl font-bold"} "Welcome to Spike!"]
     [text-button "Move to login" #(set-page! :login)]]))

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
