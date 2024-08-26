(ns beesbuddy.spike.views
  (:require [beesbuddy.spike.router :refer [url-for]]
            [re-frame.core :refer [subscribe]]))


(defn header
  []
  (let [active-page @(subscribe [:active-page])]
    [:nav.navbar.navbar-light
     [:div.container
      [:a.navbar-brand {:href (url-for :home)} "Spike"]

      [:ul.nav.navbar-nav.pull-xs-right
       [:li.nav-item
        [:a.nav-link {:href (url-for :home) :class (when (= active-page :home) "active")} "Home"]]
       [:li.nav-item
        [:a.nav-link {:href (url-for :login) :class (when (= active-page :login) "active")} "Sign in"]]
       [:li.nav-item
        [:a.nav-link {:href (url-for :logout) :class (when (= active-page :logout) "active")} "Sign out"]]
       [:li.nav-item
        [:a.nav-link {:href (url-for :register) :class (when (= active-page :register) "active")} "Sign up"]]
       [:li.nav-item
        [:a.nav-link {:href (url-for :settings) :class (when (= active-page :settings) "active")} "Setings"]]]]]))

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
  [:div
   [:h2 "Welcome to Spike! (Home view)"]])

(defn pages
  [page-name]
  (case page-name
    :home [home]
    :login [login]
    :logout [logout]
    :register [register]
    :settings [settings]
    [home]))

(defn app
  []
  (let [active-page @(subscribe [:active-page])]
    [:div
     [header]
     [pages active-page]]))
