(ns jface-swt.core
  (:import (org.eclipse.swt.widgets Display Shell Composite Label Button DateTime Spinner Table Text))
  (:import (org.eclipse.jface.layout GridDataFactory))
  (:import (org.eclipse.swt.layout FillLayout GridLayout))
  (:import (org.eclipse.jface.window ApplicationWindow))
  (:import (org.eclipse.swt.graphics Point Image))
  (:import (org.eclipse.swt SWT))
  (:import (org.eclipse.core.databinding Binding DataBindingContext UpdateValueStrategy AggregateValidationStatus))
  (:import (org.eclipse.core.databinding.beans.typed BeanProperties))
  (:import (org.eclipse.core.databinding.property.map IMapProperty))
  (:import (org.eclipse.core.databinding.property Properties))
  (:import (org.eclipse.core.databinding.conversion IConverter))
  (:import (org.eclipse.core.databinding.observable ChangeEvent IChangeListener Observables Realm))
  (:import (org.eclipse.jface.databinding.swt DisplayRealm))
  (:import (org.eclipse.core.databinding.observable.list IObservableList WritableList))
  (:import (org.eclipse.core.databinding.observable.map IObservableMap ObservableMap  WritableMap))
  (:import (org.eclipse.core.databinding.observable.set IObservableSet))
  (:import (org.eclipse.core.databinding.observable.value IObservableValue WritableValue))
  (:import (org.eclipse.core.databinding.validation IValidator ValidationStatus))
  (:import (org.eclipse.core.runtime IStatus))
  (:import (org.eclipse.jface.databinding.fieldassist ControlDecorationSupport))
  (:import (org.eclipse.jface.databinding.swt.typed WidgetProperties))
  (:import (org.eclipse.jface.databinding.viewers IViewerObservableValue ObservableListContentProvider ObservableMapLabelProvider ViewerSupport))
  (:import (org.eclipse.jface.databinding.viewers.typed ViewerProperties))
  (:import (org.eclipse.jface.layout GridDataFactory TableColumnLayout))
  (:import (org.eclipse.jface.viewers ArrayContentProvider ComboViewer ILabelProvider ISelectionChangedListener IStructuredSelection LabelProvider SelectionChangedEvent TableViewer TableViewerColumn ColumnWeightData))
  (:import (org.eclipse.swt.custom SashForm))
  (:import (org.eclipse.swt.events SelectionAdapter SelectionEvent))
  (:import (org.eclipse.jface.action IAction Action ToolBarManager StatusLineManager MenuManager Separator))
  (:import (org.eclipse.jface.resource ImageRegistry ImageDescriptor))
  (:require [clojure.java.io :as io])
  (:gen-class))


(def widgets (atom {}))

(def image-registry (atom nil))

;;(def content-provider (ObservableListContentProvider.))

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

(declare my-app-window)

(def quit-action
  (proxy [Action] ["&Quit"]
    (run []
      (.close my-app-window))
    ))


(defn getColumn
  [caption viewer layout]
  (let [column (TableViewerColumn. viewer SWT/LEFT)
        col (.getColumn column)]
    (.setText col caption)
    (.setResizable col false)
    (.setMoveable col false)
    (.setColumnData layout col (ColumnWeightData. 100))
    column
    )
  
  )

(defn addListBindings
  [viewer wm wl content-provider]
  (let [knownElements (.getKnownElements content-provider)
        ;;fname (Observables/observeMapEntry wm "fname")
        ;;fname (.observeDetail (IMapProperty/value "fname") knownElements)
        ;;fname (.observeDetail (Properties/selfMap wm "fname" ) knownElements)
        ;;labelMaps (Properties/observeEach knownElements (into-array String ["fname"]))
        ;;labelMaps (Properties/observeEach knownElements (Properties/selfMap wm (into-array String ["fname"])))


        fname (.observeDetail (Observables/observeMapEntry wm "fname") knownElements)
        labelMaps (into-array IObservableMap [fname])
        ;;labelMaps (into-array ObservableMap [fname])
    
        label-provider (proxy [ObservableMapLabelProvider] [labelMaps]
        (getText [element index]
          (println "in the label provider")
          ;; change this to be based on the index of the map
        "wannamingo"
          )
        )
        ]
    (.setContentProvider viewer content-provider)
    (.setLabelProvider viewer label-provider)
    (.setInput viewer wl)
    ))



;;function to make a child composite widget
(defn make-child-composite
  [parent]
  ;;  (reset! widgets {:fred "someval"})
  ;;  (println (:fred @widgets))
  ;;  (swap! widgets conj {:simon "ickbah"})
  ;;  (println @widgets)
  (let [container (proxy [Composite] [parent SWT/NONE])
        sashForm (SashForm. container SWT/HORIZONTAL)
        listContainer (Composite. sashForm SWT/NONE)
        editContainer (Composite. sashForm SWT/NONE)
        listView (TableViewer. listContainer SWT/NONE)
        listTable (.getTable listView)
        tableLayout (TableColumnLayout.)
        label (Label. editContainer SWT/BORDER)
        txtTest (Text. editContainer SWT/NONE)
        lblError (Label. editContainer SWT/NONE)
        btnSave (Button. editContainer SWT/PUSH)
        dbc (DataBindingContext.)
        value (WritableValue.)
        wm (WritableMap.)
        wl (WritableList.)
        content-provider (ObservableListContentProvider.)
        ]
    (.setWeights sashForm (int-array [1 2] ))
    (.setLayout listContainer (GridLayout. 1 true))
    (.setLayout editContainer (GridLayout. 2 false))
    (.setHeaderVisible listTable true)
    (.setLinesVisible listTable true)
    (.setLayout listContainer tableLayout)
    (getColumn "First Name" listView tableLayout)
    ;;(.setContentProvider listView content-provider)
    (addListBindings listView wm wl content-provider)
    
    (.setText label "First Name")
    (.setText txtTest "some text")
    (.setText btnSave "Save")
    ;; test save print value of the writable map
    (.addSelectionListener
     btnSave
     ;; using a proxy to implement anonymous inner class
     ;; inheriting from SelectionAdapter and overriding the widgetSelected method
     (proxy [SelectionAdapter] []
       (widgetSelected [event]
         (println (.get wm "fname"))
         )
       )
     )
    (.applyTo (GridDataFactory/fillDefaults) label)
    (.applyTo (.grab (GridDataFactory/fillDefaults) true false) txtTest)
    (.put wm "fname" "wayne")
    (.add wl wm)
    
    ;;(ViewerSupport/bind listView wl (Properties/selfMap wm "fname"))
    ;;(ViewerSupport/bind listView wl (Properties/selfValue (into-array String ["fname"])))
    
    (let [target (.observe (WidgetProperties/text SWT/Modify) txtTest)
          model (Observables/observeMapEntry wm "fname" )]
      (.bindValue dbc target model))
    (.setLayout container (FillLayout. SWT/VERTICAL))
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
           (let [data-binding-view (make-child-composite container)]
             (swap! widgets conj {:databind-example data-binding-view}))
           (.layout container)
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

      (reset! image-registry (ImageRegistry. ))
      (.put @image-registry "activity-big" (ImageDescriptor/createFromURL (->> "Activity_32x.png" io/resource)))
      (.put @image-registry "activity-small" (ImageDescriptor/createFromURL (io/resource "Activity_16xSM.png")))
      (let [image-big (.get @image-registry "activity-big") image-small (.get @image-registry "activity-small")]
        (.setImages newShell  (into-array Image [image-small image-big]))
        )
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
  (let [myRunner
        (reify java.lang.Runnable
          (run [this]
            (try
              (.info main-entity "hello world")
              (. my-app-window setBlockOnOpen true)
              (. my-app-window addMenuBar)
              (. my-app-window addStatusLine)
              (. my-app-window addToolBar (bit-or (. SWT FLAT) (. SWT WRAP)))
              (. my-app-window open)
              (. default-display (dispose))
              (catch java.lang.Exception e
                (.printStackTrace e)))
            ))]

    (Realm/runWithDefault (DisplayRealm/getRealm (Display/getDefault)) myRunner )
    )
  )
