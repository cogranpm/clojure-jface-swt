(ns jface-swt.core
  (:import (org.eclipse.swt.widgets Display Shell))
  (:import (org.eclipse.jface.layout GridDataFactory))
  (:import (org.eclipse.jface.window ApplicationWindow))
  (:import (org.eclipse.swt.graphics Point))
  (:import (org.eclipse.swt SWT))
  (:import (org.eclipse.jface.action ToolBarManager StatusLineManager MenuManager Separator))
  (:gen-class))


(def menu-manager
  (new MenuManager "menu"))

(def my-app-window
  (proxy
      [ApplicationWindow]
      [nil]

    
    (createContents [parent]
      (println "create contents called"))

    (getInitialSize []
      (new Point 500 500))

    (configureShell [newShell]
      (proxy-super configureShell newShell)
      (. newShell (setText "Kernai"))
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
  ;;(gui-main)
  (. my-app-window setBlockOnOpen true)
  (. my-app-window addMenuBar)
  (. my-app-window addStatusLine)
  (. my-app-window addToolBar (bit-or (. SWT FLAT) (. SWT WRAP)))
  (. my-app-window open)
  (. default-display (dispose))
  )
