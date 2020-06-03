(ns jface-swt.com.parinherm.mainview
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
(def entity-list (WritableList. realm))
(def current-entity (WritableMap. realm))
(def content-provider (ObservableListContentProvider.))
(def column-first-name "fname")

(def menu-manager
  (new MenuManager "menu"))

(declare win)

(def quit-action
  (proxy [Action] ["&Quit"]
    (run []
      (.close win))
    ))


(defn make-domain-item
  "function that makes a writable map entry of whatever we are dealing with as the domain model entity"
  [first-name]
  (doto (WritableMap. ) (.put column-first-name first-name))
  )

(defn make-dummy-data
  []
  ;; some dummy data
  (.put current-entity column-first-name "wayne")
  (.add entity-list current-entity)
  (.add entity-list (make-domain-item "Belconnen"))
  (.add entity-list (make-domain-item "Bertrand"))
  (.add entity-list (make-domain-item "Ballyntine"))

  )

;; this is called every time item selected in the list
(defn make-data-bindings
  [entity]
  (.dispose dbc)
  (let [bindings (.getValidationStatusProviders dbc)]
    (doseq [element bindings]
       (.removeBinding dbc (cast Binding element))
      )
    )

  ;; one of these for every field
  (let [target (.observe (WidgetProperties/text SWT/Modify) (:txtFirstName @widgets))
        model (Observables/observeMapEntry entity column-first-name )]
    (.bindValue dbc target model))

  )

(defn getColumn
  [caption viewer layout]
  (let [column (TableViewerColumn. viewer SWT/LEFT)
        col (.getColumn column)
        colProvider
        (proxy [ColumnLabelProvider] [] 
          (getText [element]
            (.get element column-first-name)
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
    (.setLabelProvider column colProvider)
     column
    )
  )


;;function to make a child composite widget
;; eventually move these out to individual files
;; one for each child composite (view)
(defn make-child-composite
  [parent]
  ;;  (reset! widgets {:fred "someval"})
  ;;  (println (:fred @widgets))
  ;;(swap! widgets conj {:simon "ickbah"})

  ;; all these widgets need to be local to this function
  ;; as they require a parent argument and parent is
  ;; not available until the parent window is shown
  ;; so workaround is to keep a map in an atom and
  ;; add the widgets to it with a key so it's possible to
  ;; get the widget that way
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

    ;; save necessary widgets in a dedicated mutable map atom
    ;; (swap! widgets conj {:lblFirstName lblFirstName})
    (swap! widgets conj {:txtFirstName txtFirstName})

    (.setWeights sashForm (int-array [1 2] ))
    (.setLayout listContainer (GridLayout. 1 true))
    (.setLayout editContainer (GridLayout. 2 false))
    (.setHeaderVisible listTable true)
    (.setLinesVisible listTable true)
    (.setLayout listContainer tableLayout)

    ;; could this be more succint and use a lambda style
    ;; answer, could use a macro or something to create proxy in background:

    (.addSelectionChangedListener
     listView
     (proxy
         [ISelectionChangedListener]
          []
       (selectionChanged [e]
         (let [selection (.getStructuredSelection listView)
               selected-item (.getFirstElement selection)
               ]
           (make-data-bindings selected-item)
           )
         )
       )
     )

 
    (getColumn "First Name" listView tableLayout)
    (.setContentProvider listView content-provider)

    (make-dummy-data)

    (.setInput listView entity-list)
    (.setText btnSave "Save")
    ;; test save print value of the writable map
    (.addSelectionListener
     btnSave
     ;; using a proxy to implement anonymous inner class
     ;; inheriting from SelectionAdapter and overriding the widgetSelected method
     (proxy [SelectionAdapter] []
       (widgetSelected [event]
         ;; this proves that value was updated in the model
         (println (.get current-entity column-first-name))
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
(def win
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
      true)
    
    ))


(def default-display
  (Display/getCurrent))

