(ns beesbuddy.spike.utils)

(defmacro defn-mui-styled [name & styles]
  `(defn ~name [props#]
     (let [theme# (.-theme props#)
           styles-map# (into {}
                             (map (fn [[k# v#]]
                                    [k# (if (fn? v#) (v# theme#) v#)])
                                  (partition 2 (list ~@styles))))]
       (cljs.core/clj->js styles-map#))))
