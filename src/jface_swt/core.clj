(ns jface-swt.core
  (:import (org.eclipse.swt.widgets Display Shell))
  (:gen-class))

(defn gui-loop [display shell]
  (when-not (. shell (isDisposed))
    (if-not (. display (readAndDispatch))
      (. display (sleep)))
    (recur display shell)))
  
(defn gui-main []
  (let [display (new Display)
        shell (doto (new Shell display)
                (.open)
                (.setText "hello there"))]
    (gui-loop display shell)
    (. display (dispose))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!")
  (gui-main)
  )
