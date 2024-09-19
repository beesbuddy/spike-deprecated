(ns hooks.spike
  (:require [clj-kondo.hooks-api :as api]))

(defn defn-mui-styled [{:keys [node]}]
  (let [[_ name & body] (:children node)
        fn-node (api/list-node
                 (list* (api/token-node 'defn)
                        name
                        (api/vector-node [(api/token-node 'props#)])
                        body))]
    {:node fn-node}))