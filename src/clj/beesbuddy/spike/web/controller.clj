;; The purpose of this namespace is to define integrant related controllers 
;; that need to be required before usage in the system.edn
(ns beesbuddy.spike.web.controller)

;; Require the auth controller that will be injected by integrant
(require '[beesbuddy.spike.web.controller.auth])