(ns jface-swt.core
  (:import (org.eclipse.swt SWT))
  (:import (org.eclipse.swt.widgets Display Shell))
  (:import (org.eclipse.jface.databinding.swt DisplayRealm))
  (:import (org.eclipse.core.databinding.observable Realm))
  (:require [jface-swt.com.parinherm.mainview :as ui])
  (:gen-class))

;; this is just testng defprotocol
;; a way to define an interface that will be implemented by a deftype
(defprotocol Entity
  (info [this arg]))

;; example of implementing a defprotocolc
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
              ;; just a test of the protocol defined in this file
              (.info main-entity "hello jface swt gui")

              ;; here starts the gui stuff
              ;; set up the properties of the Application Window
              ;; the stuff in the ui file all runs before this happens
              (. ui/win setBlockOnOpen true)
              (. ui/win addMenuBar)
              (. ui/win addStatusLine)
              (. ui/win addToolBar (bit-or (. SWT FLAT) (. SWT WRAP)))
              (. ui/win open)
              (. ui/default-display (dispose))
              (catch java.lang.Exception e
                (.printStackTrace e)))
            ))]

    (Realm/runWithDefault (DisplayRealm/getRealm (Display/getDefault)) myRunner )
    )
  )
