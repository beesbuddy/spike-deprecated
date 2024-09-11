(ns beesbuddy.spike.web.routes.api
  (:require
   [beesbuddy.spike.web.controller.health :as health]
   [beesbuddy.spike.web.middleware.exception :as exception]
   [beesbuddy.spike.web.middleware.formats :as formats]
   [buddy.auth :refer [authenticated?]]
   [integrant.core :as ig]
   [reitit.coercion.malli :as malli]
   [reitit.ring.coercion :as coercion]
   [reitit.ring.middleware.muuntaja :as muuntaja]
   [reitit.ring.middleware.parameters :as parameters]
   [reitit.swagger :as swagger]))

(def route-data
  {:coercion   malli/coercion
   :muuntaja   formats/instance
   :swagger    {:id                  ::api
                :securityDefinitions {:jwtAuth {:type "apiKey"
                                                :name "Authorization"
                                                :in   "header"}}}
   :middleware [;; query-params & form-params
                parameters/parameters-middleware
                ;; content-negotiation
                muuntaja/format-negotiate-middleware
                ;; encoding response body
                muuntaja/format-response-middleware
                ;; exception handling
                coercion/coerce-exceptions-middleware
                ;; decoding request body
                muuntaja/format-request-middleware
                ;; coercing response bodys
                coercion/coerce-response-middleware
                ;; coercing request parameters
                coercion/coerce-request-middleware
                ;; exception handling
                exception/wrap-exception]})

(defn secured
  [request]
  (if-not (authenticated? request)
    {:body {:message "Not authorized"} :status 401 :content-type "application/json"}
    {:body {:message "Authorized"} :status 200 :content-type "application/json"}))

;; Routes
(defn api-routes [{:keys [wrap-jwt-auth jwt-sign-in]}]
  [["/swagger.json"
    {:get {:no-doc  true
           :swagger {:info {:title "Spike API"}}
           :handler (swagger/create-swagger-handler)}}]
   ["/sign-in"
    {:post       {:summary "Sign in"
                  :handler jwt-sign-in}
     :parameters {:body {:username string?
                         :password string?}}}]
   ["/health"
    {:get {:summary "Health check"
           :handler health/healthcheck!}}]
   ["/v1" {:middleware [wrap-jwt-auth]}
    ["/secured" {:get {:summary "Secured route"
                       :handler secured
                       :swagger {:security [{:jwtAuth []}]}}}]]])

(derive :reitit.routes/api :reitit/routes)

(defmethod ig/init-key :reitit.routes/api
  [_ {:keys [base-path]
      :or   {base-path "/api"}
      :as   opts}]
  (fn [] [base-path route-data (api-routes opts)]))
