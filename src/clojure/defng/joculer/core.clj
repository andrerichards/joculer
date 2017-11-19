(ns defng.joculer.core
  (:require [clojure.data]
            [clojure.pprint])
  (:import (java.io StringWriter)))


(defn jlist?
  "Check if it is a Java List (but not a Clojure 'list?')"
  [l]
  (and (instance? java.util.List l)
       (not (list? l))))

(defn jset?
  "Check if it is a Java Set (but not a Clojure 'set?')"
  [l]
  (and (instance? java.util.Set l)
       (not (set? l))))

(defn jmap?
  "Check if it is a Java Map (but not a Clojure 'map?')"
  [m]
  (and (instance? java.util.Map m)
       (not (map? m))))


(defn ->clojure
  "Recursively transforms Java lists into Clojure vectors, and Java
  maps into Clojure maps.  With option ':keywordize-keys true' will
  convert object fields from strings to keywords."
  ([x] (->clojure x {:keywordize-keys false}))
  ([x opts]
   (let [{:keys [keywordize-keys]} opts
         keyfn (if keywordize-keys keyword str)
         f (fn thisfn [x]
             (cond
               (jmap? x)
               (into {} (for [[k v] x]
                          [(if (string? k)
                             (keyfn k) k)
                           (->clojure v)]))

               (jlist? x)
               (into [] (map ->clojure x))

               (jset? x)
               (into #{} (map ->clojure x))

               :else x))]
     (f x))))


(defn pretty
  [object]
  (clojure.pprint/write object :stream nil))

(defn diff
  [a b]
  (into [] (clojure.data/diff (->clojure a) (->clojure b))))

