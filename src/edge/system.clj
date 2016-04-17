;; Copyright © 2016, JUXT LTD.

(ns edge.system
  "Components and their dependency relationships"
  (:refer-clojure :exclude (read))
  (:require
    [com.stuartsierra.component :refer (system-map system-using using)]
    [edge.webserver :refer [new-webserver]]))

(defn new-system-map []
  (system-map
    :webserver (new-webserver 3000)))


(defn new-dependency-map []
  {:webserver {}})

(defn new-production-system
  "Create the production system"
  []
  (-> (new-system-map)
      (system-using (new-dependency-map))))