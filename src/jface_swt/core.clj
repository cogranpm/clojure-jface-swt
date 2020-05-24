(ns jface-swt.core
  (:import (org.eclipse.swt SWT))
  (:import (org.eclipse.swt.widgets Display Shell))
  (:import (org.eclipse.jface.databinding.swt DisplayRealm))
  (:import (org.eclipse.core.databinding.observable Realm))
  (:require [jface-swt.com.parinherm.ui :as ui])
  (:gen-class))

;; this is just testng defprotocol
;; a way to define an interface that will be implemented by a deftype
(defprotocol Entity
  (info [this arg]))

;; example of implementing a defprotocol
(deftype Sample []
  Entity
  (info [this arg]
    (println arg)))

(def main-entity
  (Sample.))


(defn -main
  "example of a jface-swt stand alone application in clojure"
  [& args]
  (let [myRunner
        (reify java.lang.Runnable
          (run [this]
            (try
              (.info main-entity "hello jface swt gui")
              (. ui/my-app-window setBlockOnOpen true)
              (. ui/my-app-window addMenuBar)
              (. ui/my-app-window addStatusLine)
              (. ui/my-app-window addToolBar (bit-or (. SWT FLAT) (. SWT WRAP)))
              (. ui/my-app-window open)
              (. ui/default-display (dispose))
              (catch java.lang.Exception e
                (.printStackTrace e)))
            ))]

    (Realm/runWithDefault (DisplayRealm/getRealm (Display/getDefault)) myRunner )
    )
  )
