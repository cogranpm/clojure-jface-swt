(ns jface-swt.com.parinherm.ui
  (:require [clojure.string :as str])
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
  (:import (org.eclipse.jface.viewers ArrayContentProvider ComboViewer ILabelProvider ISelectionChangedListener IStructuredSelection LabelProvider SelectionChangedEvent TableViewer TableViewerColumn ColumnWeightData ColumnLabelProvider))
  (:import (org.eclipse.swt.custom SashForm))
  (:import (org.eclipse.swt.events SelectionAdapter SelectionEvent))
  (:import (org.eclipse.jface.action IAction Action ToolBarManager StatusLineManager MenuManager Separator))
  (:import (org.eclipse.jface.resource ImageRegistry ImageDescriptor))
  (:require [clojure.java.io :as io])

  (:gen-class))


(def app-name "Kernai")
(def realm (DisplayRealm/getRealm (Display/getDefault)))
(def widgets (atom {}))
(def image-registry (atom nil))
(def dbc (DataBindingContext.  realm))
(def wl (WritableList. realm))
(def wm (WritableMap. realm))
;;(def txtFirstName (atom nil))
(def content-provider (ObservableListContentProvider.))
(def menu-manager
  (new MenuManager "menu"))

(declare my-app-window)

(def quit-action
  (proxy [Action] ["&Quit"]
    (run []
      (.close my-app-window))
    ))


(defn make-domain-item
  "function that makes a writable map entry of whatever we are dealing with as the domain model entity"
  [first-name]
  (doto (WritableMap. ) (.put "fname" first-name))
  )


;; note, need to get rid of txtTest argument and make it a namespace level var
;; when figured out how to handle widgets
(defn make-data-bindings
  [amap txtTest]
  (.dispose dbc)
  (let [bindings (.getValidationStatusProviders dbc)]
    ;; need to get rid of each of the bindings
    (doseq [element bindings]
       (.removeBinding dbc (cast Binding element))
      )
    )
  (let [target (.observe (WidgetProperties/text SWT/Modify) txtTest)
        model (Observables/observeMapEntry amap "fname" )]
    (.bindValue dbc target model))

  )

(defn getColumn
  [caption viewer layout]
  (let [column (TableViewerColumn. viewer SWT/LEFT)
        col (.getColumn column)
        colProvider
        (proxy [ColumnLabelProvider] [] 
          (getText [element]
            (.get element "fname")
            )
          
          (getImage [element]
            nil
            )
          )
        ]
    (.setText col caption)
    (.setResizable col false)
    (.setMoveable col false)
    (.setColumnData layout col (ColumnWeightData. 100))
    ;; consider this new way that might be able to do observable on a map entry
    ;; so entity data is simply a clojure map data structure
    ;; code example follows:
    ;; column.setLabelProvider(new ObservableMapCellLabelProvider(
    ;; BeanProperties.value("firstname").observeDetail(cp.getKnownElements())
    ;; )
    (.setLabelProvider column colProvider)
     column
    )
  
  )

(defn addListBindings
  [viewer wm wl content-provider]
  (let [knownElements (.getKnownElements content-provider)
        ;;fname (Observables/observeMapEntry wm "fname")
        ;;fname (.observeDetail (IMapProperty/value "fname") knownElements)
        ;;fname (.observeEach (Properties/selfMap wm "fname" ) knownElements)
        ;;labelMaps (Properties/observeEach knownElements (into-array String ["fname"]))
        ;;labelMaps (Properties/observeEach knownElements (Properties/selfMap wm (into-array String ["fname"])))
        ;;fname (.observeDetail (Observables/observeMapEntry wm "fname") knownElements)

        fname (Properties/observableValue wm)
        ;;fname (Properties/selfValue wm)
        ;;labelMaps (into-array ObservableMap [fname])
        labelMaps (into-array IObservableMap nil)

    
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
        lblFirstName (Label. editContainer SWT/BORDER)
        txtFirstName (Text. editContainer SWT/NONE)
        lblError (Label. editContainer SWT/NONE)
        btnSave (Button. editContainer SWT/PUSH)
        value (WritableValue.)
        ]
    (.setWeights sashForm (int-array [1 2] ))
    (.setLayout listContainer (GridLayout. 1 true))
    (.setLayout editContainer (GridLayout. 2 false))
    (.setHeaderVisible listTable true)
    (.setLinesVisible listTable true)
    (.setLayout listContainer tableLayout)

    (.addSelectionChangedListener
     listView
     (proxy
         [ISelectionChangedListener]
          []
       (selectionChanged [e]
         (let [selection (.getStructuredSelection listView)
               selected-item (.getFirstElement selection)
               ]
           (make-data-bindings selected-item txtFirstName)
           )
         )
       )
     )


    (getColumn "First Name" listView tableLayout)
    (.setContentProvider listView content-provider)
    (.put wm "fname" "wayne")
    (.add wl wm)
    ;;dynamically add a second, say as you would from a database
    (.add wl (make-domain-item "Belconnen"))
    (.add wl (make-domain-item "Bertrand"))
    ;; take this out depending on method used
    (.setInput listView wl)
    ;;(ViewerSupport/bind listView wl (Properties/selfMap wm "fname"))
    ;;(ViewerSupport/bind listView wl (Properties/selfValue (into-array String ["fname"])))
    ;;(ViewerSupport/bind listView wl (Observables/observeMapEntry wm "fname"))


    ;;don't know how to get the observable list stuff working
    ;;in getColumn will temporarily set up a ColumnLabelProvider
    ;;(addListBindings listView wm wl content-provider)
    
    (.setText lblFirstName "First Name")
    (.setText txtFirstName "some text")
    (.setText btnSave "Save")
    ;; test save print value of the writable map
    (.addSelectionListener
     btnSave
     ;; using a proxy to implement anonymous inner class
     ;; inheriting from SelectionAdapter and overriding the widgetSelected method
     (proxy [SelectionAdapter] []
       (widgetSelected [event]
         ;; this proves that value was updated in the model
         (println (.get wm "fname"))
         )
       )
     )
    (.applyTo (GridDataFactory/fillDefaults) lblFirstName)
    (.applyTo (.grab (GridDataFactory/fillDefaults) true false) txtFirstName)
    
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
      (. newShell (setText "Kernai on Clojure - test new change"))

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
      (println "closing application")
      ;; don't forget to return a boolean
      true)
    
    ))

    
(def default-display
  (Display/getCurrent))


