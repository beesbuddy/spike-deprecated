(ns beesbuddy.spike.views
  (:require ["@mui/material" :as mui]
            ["@mui/material/Card" :default MuiCard]
            ["@mui/material/styles" :refer [useTheme]]
            ["@mui/system" :refer [styled]]
            [beesbuddy.spike.router :refer [set-page! url-for]]
            [beesbuddy.spike.utils :refer [defn-mui-styled]]
            [re-frame.core :refer [dispatch subscribe]]
            [reagent.core :as r]))


(defn-mui-styled card-styles 
  :padding (fn [theme] (.spacing theme 4)))

(defn text-button [label click-handler] 
  [:> mui/Button {:variant "outlined" :on-click click-handler} label])

(defn card []
  (let [theme (useTheme)]
    [:> ((styled MuiCard)
         card-styles) {:theme theme}
     [:div {:class "text-3xl font-bold"} "Welcome to Spike!"]
     [text-button "Move to login" #(set-page! (url-for :login))]]))

(defn header []
  [:header {:class "sticky top-0 z-999 flex w-full bg-white drop-shadow-1 dark:bg-boxdark dark:drop-shadow-none"}
   [:div {:class "flex flex-grow items-center justify-between px-4 py-4 shadow-2 md:px-6 2xl:px-11"}
    [:div {:class "flex items-center gap-2 sm:gap-4 lg:hidden"}
     [:button
      {:class "z-99999 block rounded-sm border border-stroke bg-white p-1.5 shadow-sm dark:border-strokedark dark:bg-boxdark lg:hidden"
       :on-click #(dispatch [:set-sidebar-open {:open true}])}
      "-->"]]]])

(defn nav-link [page title]
  (let [active-page (subscribe [:active-page])]
    [:li {:class (str (when (= @active-page (keyword page)) "bg-graydark dark:bg-meta-4") "group relative flex items-center gap-2.5 rounded-sm px-4 py-2 font-medium text-bodydark1 duration-300 ease-in-out hover:bg-graydark dark:hover:bg-meta-4")}
     [:a {:href (url-for (keyword page)) :style {:width "100%"}} title]]))

(defn menu
  []
  [:div {:class "no-scrollbar flex flex-col overflow-y-auto duration-300 ease-linear"}
   [:nav {:class "mt-5 py-4 px-4 lg:mt-9 lg:px-6"}
    [:div
     [:h3 {:class "mb-4 ml-4 text-sm font-semibold text-bodydark2"} "Menu"]
     [:ul {:class "mb-6 flex flex-col gap-1.5"}
      [nav-link :home "Home"]
      [nav-link :login "Login"]
      [nav-link :logout "Logout"]
      [nav-link :register "Sign up"]
      [nav-link :settings "Settings"]]]]])

(defn sidebar []
  (let [sidebar-ref (clojure.core/atom nil)
        click-handler (clojure.core/atom nil)
        is-open (subscribe [:sidebar-open])]
    (r/create-class
     {:component-did-mount
      (fn []
        (let [handler (fn [^js e]
                        (let [target (.-target e)]
                          (when (and @is-open
                                     @sidebar-ref
                                     (not (.contains @sidebar-ref target)))
                            (dispatch [:set-sidebar-open {:open (not @is-open)}]))))]
          (.addEventListener js/document "click" handler)
          (reset! click-handler handler)))

      :component-will-unmount
      (fn []
        (when-let [handler @click-handler]
          (.removeEventListener js/document "click" handler)))
      :reagent-render
      (fn []
        [:aside {:ref #(reset! sidebar-ref %)
                 :class (str
                         "absolute left-0 top-0 z-9999 flex h-screen w-72.5 flex-col overflow-y-hidden bg-black duration-300 ease-linear dark:bg-boxdark lg:static lg:translate-x-0"
                         (if (not @(subscribe [:sidebar-open])) " -translate-x-full" " translate-x-0"))}
         [:div {:class "flex items-center justify-between gap-2 px-6 py-5.5 lg:py-6.5"}
          [:a {:href (url-for :home)} "Spike"]
          [:button {:aria-controls "sidebar"
                    :class "block lg:hidden"
                    :on-click #(dispatch [:set-sidebar-open {:open false}])}
           "<--"]]
         [:div {:class "no-scrollbar flex flex-col overflow-y-auto duration-300 ease-linear"}
          [menu]]])})))

(defn layout [& childrens]
  [:div {:class "dark:bg-boxdark-2 dark:text-bodydark"}
   [:div {:class "flex h-screen overflow-hidden"}
    [sidebar]
    [:div {:class "relative flex flex-1 flex-col overflow-y-auto overflow-x-hidden"}
     [header]
     [:main
      [:div {:class "mx-auto max-w-screen-2xl p-4 md:p-6 2xl:p-10"}
       childrens]]]]])

(defn login
  []
  [:div
   [:h2 "Login view"]])

(defn logout
  []
  [:div
   [:h2 "Logout view"]])

(defn register
  []
  [:div
   [:h2 "Register view"]])

(defn settings
  []
  [:div
   [:h2 "Settings view"]])

(defn home
  []
  [:<>
   [:f> card]])

(defn pages
  [page-name]
  [layout
   (case page-name
     :home [home {:key "home"}]
     :login [login {:key "login"}]
     :logout [logout {:key "logout"}]
     :register [register {:key "register"}]
     :settings [settings {:key "settings"}]
     [home {:key "home"}])])

(defn app
  []
  (let [active-page @(subscribe [:active-page])]
    [pages active-page]))
