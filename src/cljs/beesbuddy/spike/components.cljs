(ns beesbuddy.spike.components
  (:require ["@mui/material" :as mui]
            ["@mui/material/Card" :default MuiCard]
            ["@mui/material/styles" :refer [useTheme]]
            ["@mui/system" :refer [styled]]
            [beesbuddy.spike.icons :refer [sidebar-close-icon]]
            [beesbuddy.spike.router :refer [set-page! url-for]]
            [beesbuddy.spike.utils :refer [defn-mui-styled]]
            [re-frame.core :refer [dispatch subscribe]]
            [reagent.core :as r]))

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

(defn header []
  [:header {:class "sticky top-0 z-999 flex w-full bg-white drop-shadow-1 dark:bg-boxdark dark:drop-shadow-none"}
   [:div {:class "flex flex-grow items-center justify-between px-4 py-4 shadow-2 md:px-6 2xl:px-11"}
    [:div {:class "flex items-center gap-2 sm:gap-4 lg:hidden"}
     [:button
      {:class "z-99999 block rounded-sm border border-stroke bg-white p-1.5 shadow-sm dark:border-strokedark dark:bg-boxdark lg:hidden"
       :on-click #(dispatch [:set-sidebar-open {:open true}])}
      [:span {:class "relative block h-5.5 w-5.5 cursor-pointer"}
       [:span {:class "du-block absolute right-0 h-full w-full"}
        [:span {:class (str "relative left-0 top-0 my-1 block h-0.5 w-0 rounded-sm bg-black delay-[0] duration-200 ease-in-out dark:bg-white" (when (not @(subscribe [:sidebar-open])) " !w-full delay-300"))}]
        [:span {:class (str "relative left-0 top-0 my-1 block h-0.5 w-0 rounded-sm bg-black delay-150 duration-200 ease-in-out dark:bg-white" (when (not @(subscribe [:sidebar-open])) " !w-full delay-400"))}]
        [:span {:class (str "relative left-0 top-0 my-1 block h-0.5 w-0 rounded-sm bg-black delay-200 duration-200 ease-in-out dark:bg-white" (when (not @(subscribe [:sidebar-open])) " !w-full delay-500"))}]]
       [:span {:class "absolute right-0 h-full w-full rotate-45"}
        [:span {:class (str "absolute left-2.5 top-0 block h-full w-0.5 rounded-sm bg-black delay-300 duration-200 ease-in-out dark:bg-white" (when (not @(subscribe [:sidebar-open])) " !h-0 !delay-[0]"))}]
        [:span {:class (str "absolute left-2.5 top-0 block h-full w-0.5 rounded-sm bg-black delay-400 duration-200 ease-in-out dark:bg-white" (when (not @(subscribe [:sidebar-open])) " !h-0 !delay-200"))}]]]]]]])

(defn layout [sidebar header & childrens]
  [:div {:class "dark:bg-boxdark-2 dark:text-bodydark"}
   [:div {:class "flex h-screen overflow-hidden"}
    [sidebar]
    [:div {:class "relative flex flex-1 flex-col overflow-y-auto overflow-x-hidden"}
     [header]
     [:main
      [:div {:class "mx-auto max-w-screen-2xl p-4 md:p-6 2xl:p-10"}
       childrens]]]]])

(defn nav-link [token title]
  (let [active-page (subscribe [:active-page])]
    [:li {:class (str (when (= @active-page token) "bg-graydark dark:bg-meta-4") " group relative flex items-center gap-2.5 rounded-sm px-4 py-2 font-medium text-bodydark1 duration-300 ease-in-out hover:bg-graydark dark:hover:bg-meta-4")}
     [:a {:href (url-for token) :style {:width "100%"}} title]]))

(defn menu
  [pages]
  [:div {:class "no-scrollbar flex flex-col overflow-y-auto duration-300 ease-linear"}
   [:nav {:class "mt-5 py-4 px-4 lg:mt-9 lg:px-6"}
    [:div
     [:h3 {:class "mb-4 ml-4 text-sm font-semibold text-bodydark2"} "Menu"]
     [:ul {:class "mb-6 flex flex-col gap-1.5"}]
     (for [[token {:keys [title]}] pages]
       ^{:key token} [nav-link token title])]]])

(defn sidebar [menu]
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
           sidebar-close-icon]]
         [:div {:class "no-scrollbar flex flex-col overflow-y-auto duration-300 ease-linear"}
          menu]])})))