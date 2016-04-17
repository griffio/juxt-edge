(ns edge.webserver
  (:require
    [aleph.http :as http]
    [bidi.ring :refer [make-handler redirect]]
    [clojure.tools.logging :refer [errorf]]
    [com.stuartsierra.component :refer [Lifecycle using]]
    [schema.core :as schema]
    [yada.yada :refer [yada] :as yada])
  (:import (java.io File)))

(def site
  [""
   (yada/swaggered
     ["/"
      [
       ["hello"  (yada "Hello World!\n")]
       ["content" (yada "Content!\n")]
       ["goodbye"  (yada "Goodbye World!\n")]
       ["" (yada/handler (File. "target/dev"))]]]

     {})])

(schema/defrecord Webserver [port :- schema/Int
                             server]
  Lifecycle
  (start [component]
    (println (str "Webserver starting on port " port "!"))
    (try
      (let [server
            (http/start-server (make-handler site)
                               {:port port})]
        (assoc component :server server))
      (catch Exception e
        (errorf e "Some failure")
        component)))

  (stop [component]
    (println "Webserver stopping!")
    (when server
      (.close server))
    component))

(defn new-webserver [port]
  (map->Webserver {:port port}))