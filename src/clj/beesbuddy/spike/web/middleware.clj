;; The purpose of this namespace is to define integrant related middlewares 
;; that need to be required before usage in the system.edn
(ns beesbuddy.spike.web.middleware)

;; Require the auth middleware that will be injected by integrant
(require '[beesbuddy.spike.web.middleware.before.auth])