(ns jface-swt.core
  (:import (org.eclipse.swt.widgets Display Shell Composite Label))
  (:import (org.eclipse.jface.layout GridDataFactory))
  (:import (org.eclipse.swt.layout FillLayout))
  (:import (org.eclipse.jface.window ApplicationWindow))
  (:import (org.eclipse.swt.graphics Point))
  (:import (org.eclipse.swt SWT))
  (:import (org.eclipse.jface.action IAction Action ToolBarManager StatusLineManager MenuManager Separator))
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

(def quit-action
  (proxy [Action] ["&Quit"]
    (run []
      (.close my-app-window))
    ))


;;function to make a child composite widget
(defn make-child-composite
  [parent]
  (let [container (proxy [Composite] [parent SWT/NONE])]
    (println "making child composite")
    (.setLayout container (FillLayout.))
    (let [label (Label. container SWT/NONE)]
      (.setText label "fred"))
    (.layout container)
    container
    )
  )


;; must create a proxy  for jface application window to override important hook methods
(def my-app-window
  (proxy
      [ApplicationWindow]
      [nil]

    
    (createContents [parent]
      (let [container (Composite. parent SWT/NONE)]
           (.setLayout container (FillLayout.))
           (proxy-super setStatus  "howdy everyone")
           (let [label (Label. container SWT/NONE)]
             (.setText label "hello fred"))
           (let [data-binding-view (make-child-composite parent)])
           container)
      )

    (getInitialSize []
      ;;(new Point 500 500)
      (let [disp (Display/getDefault)]
        (let [clientArea (.getClientArea disp)]
          (new Point (/ (.width clientArea) 2) (/ (.height clientArea) 2))
          )
        )
      )

    (configureShell [newShell]
      (proxy-super configureShell newShell)
      (. newShell (setText "Kernai on Clojure"))
      ;;should be setting images here on the shell
      )

    (createMenuManager []
      (let [menu-file (MenuManager. "&File")]
        (.setAccelerator quit-action (bit-or SWT/MOD1 (int \q)))
        (.add menu-file (Separator.))
        (.add menu-file quit-action)
        (.add menu-manager menu-file))
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
