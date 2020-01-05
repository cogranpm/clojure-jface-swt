(ns jface-swt.core
  (:import (org.eclipse.swt.widgets Display Shell))
  (:import (org.eclipse.jface.layout GridDataFactory))
  (:import (org.eclipse.jface.window ApplicationWindow))
  (:import (org.eclipse.swt.graphics Point))
  (:import (org.eclipse.swt SWT))
  (:import (org.eclipse.jface.action ToolBarManager StatusLineManager MenuManager Separator))
  (:gen-class))


(defprotocol Entity
  (info [this arg]))

(deftype Sample []
  Entity
  (info [this arg]
    (println arg)))

(def main-entity
  (Sample.))

(def menu-manager
  (new MenuManager "menu"))

;; must create a proxy  for jface application window to override important hook methods
(def my-app-window
  (proxy
      [ApplicationWindow]
      [nil]

    
    (createContents [parent]
      (println "create contents called"))

    (getInitialSize []
      ;;(new Point 500 500)
      (let [disp (Display/getDefault)]
        (let [clientArea (.getClientArea disp)]
          (new Point (.width clientArea) (.height clientArea))
          )
        )
      )

    (configureShell [newShell]
      (proxy-super configureShell newShell)
      (. newShell (setText "Kernai on Clojure"))
      ;;should be setting images here on the shell
      )

    (createMenuManager []
      menu-manager)
      

    (createStatusLineManager []
      (new StatusLineManager))

    (createToolBarManager [style]
      (new ToolBarManager style))

    (close []
      (proxy-super close)
      (println "closing application"))

    
    ))

    
(def default-display
  (Display/getCurrent))




(defn -main
  "example of a jface-swt stand alone application in clojure"
  [& args]
  (.info main-entity "hello world")
  (. my-app-window setBlockOnOpen true)
  (. my-app-window addMenuBar)
  (. my-app-window addStatusLine)
  (. my-app-window addToolBar (bit-or (. SWT FLAT) (. SWT WRAP)))
  (. my-app-window open)
  (. default-display (dispose))
  )
