(let [properties (select-keys (into {} (System/getProperties))
                              ["os.arch" "os.name"])
      platform (apply format "%s (%s)" (vals properties))
      swt (case platform
            ;; "amd64 (Windows 10)" (vec '(org.eclipse.platform/org.eclipse.swt.win32.win32.x86_64 "3.110.0"))
            ;; "amd64 (Linux)"      (vec '(org.eclipse.platform/org.eclipse.swt.gtk.linux.x86_64 "3.110.0")) 
            ;; "x86_64 (Mac OS X)" (vec '(org.eclipse.platform/org.eclipse.swt.cocoa.macosx.x86_64 "3.110.0")) )

      "amd64 (Windows 10)" "lib/org.eclipse.swt.win32.win32.x86_64_3.114.0.v20200304-0601.jar"
      "amd64 (Linux)"      "lib/org.eclipse.swt.gtk.linux.x86_64_3.112.0.v20190904-0609.jar"
      "x86_64 (Mac OS X)" "lib/org.eclipse.swt.cocoa.macosx.x86_64_3.110.0.v20190305-0602.jar" )



      jvmOpts (case platform
                "amd64 (Windows 10)" (list) 
                "amd64 (Linux)" (list)
                "x86_64 (Mac OS X)" (list "-XstartOnFirstThread") 
                )
      ]

(defproject jface-swt "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}


  :dependencies [[org.clojure/clojure "1.10.0"]

                 [org.clojars.nakkaya/miglayout "3.7.3.1"]

                 ;;[~swt "3.110.0" ]

                 
                 [org.eclipse.platform/org.eclipse.core.commands "3.9.700"
                  :exclusions
                  [org.eclipse.platform/org.eclipse.equinox.common]]

                 [org.eclipse.platform/org.eclipse.equinox.common "3.12.0"]

                 [com.ibm.icu/icu4j "67.1"]

                 [org.eclipse.platform/org.eclipse.core.jobs "3.10.800"
                  :exclusions [org.eclipse.platform/org.eclipse.equinox.common]]

                 [org.eclipse.platform/org.eclipse.osgi "3.15.300"]

                 [org.eclipse.platform/org.eclipse.core.databinding.beans "1.6.100"
                  :exclusions
                  [org.eclipse.platform/org.eclipse.core.databinding.observable
                   org.eclipse.platform/org.eclipse.equinox.common
                   org.eclipse.platform/org.eclipse.core.databinding.property]]

                 [org.eclipse.platform/org.eclipse.core.databinding.observable "1.9.0"
                  :exclusions
                  [org.eclipse.platform/org.eclipse.equinox.common]]

                 [org.eclipse.platform/org.eclipse.core.databinding "1.9.0"
                  :exclusions
                  [
                   org.eclipse.platform/org.eclipse.equinox.common
                   org.eclipse.platform/org.eclipse.core.databinding.observable
                   org.eclipse.platform/org.eclipse.core.databinding.property
                   ]]

                 [org.eclipse.platform/org.eclipse.core.databinding.property "1.8.0"
                  :exclusions
                  [
                   org.eclipse.platform/org.eclipse.core.databinding.observable
                   org.eclipse.platform/org.eclipse.equinox.common
                   ]]

                 [org.eclipse.platform/org.eclipse.jface.databinding "1.11.100"
                  :exclusions
                  [
                   org.eclipse.platform/org.eclipse.jface
                   org.eclipse.platform/org.eclipse.equinox.common
                   org.eclipse.platform/org.eclipse.core.databinding 
                   org.eclipse.platform/org.eclipse.core.databinding.observable
                   org.eclipse.platform/org.eclipse.core.databinding.property
                   org.eclipse.platform/org.eclipse.swt]
                  ]

                 [org.eclipse.platform/org.eclipse.jface "3.20.0"
                  :exclusions
                  [
                   org.eclipse.platform/org.eclipse.swt
                   org.eclipse.platform/org.eclipse.core.commands
                   org.eclipse.platform/org.eclipse.equinox.common
                   ]]

                 ;; [org.eclipse.platform/org.eclipse.core.runtime "3.19.0"
                 ;;  :exclusions
                 ;;  [
                 ;;   org.eclipse.platform/org.eclipse.equinox.common
                 ;;   org.eclipse.platform/org.eclipse.equinox.registry
                 ;;   org.eclipse.platform/org.eclipse.osgi
                 ;;   org.eclipse.platform/org.eclipse.equinox.app
                 ;;   org.eclipse.platform/org.eclipse.equinox.preferences
                 ;;   org.eclipse.platform/org.eclipse.core.contenttype
                 ;;   org.eclipse.platform/org.eclipse.core.jobs
                 ;;   org.eclipse.platform/org.eclipse.core.runtime
                 ;;   org.eclipse.platform/org.eclipse.core.commands

                 ;;   ]
                 ;;  ]



                 ;; [org.eclipse.platform/org.eclipse.text "3.10.200"
                 ;;  :exclusions
                 ;;  [
                 ;;   org.eclipse.platform/org.eclipse.equinox.common
                 ;;   org.eclipse.platform/org.eclipse.equinox.registry
                 ;;   org.eclipse.platform/org.eclipse.osgi
                 ;;   org.eclipse.platform/org.eclipse.equinox.app
                 ;;   org.eclipse.platform/org.eclipse.equinox.preferences
                 ;;   org.eclipse.platform/org.eclipse.core.contenttype
                 ;;   org.eclipse.platform/org.eclipse.core.jobs
                 ;;   org.eclipse.platform/org.eclipse.core.runtime
                 ;;   org.eclipse.platform/org.eclipse.core.commands
                 ;;   ]
                 ;;  ]

                 ;; [org.eclipse.platform/org.eclipse.jface.text "3.16.300"
                 ;;  :exclusions
                 ;;  [
                 ;;   org.eclipse.platform/org.eclipse.swt
                 ;;   org.eclipse.platform/org.eclipse.jface
                 ;;   org.eclipse.platform/org.eclipse.text
                 ;;   org.eclipse.platform/org.eclipse.equinox.common
                 ;;   org.eclipse.platform/org.eclipse.equinox.registry
                 ;;   org.eclipse.platform/org.eclipse.osgi
                 ;;   org.eclipse.platform/org.eclipse.equinox.app
                 ;;   org.eclipse.platform/org.eclipse.equinox.preferences
                 ;;   org.eclipse.platform/org.eclipse.core.contenttype
                 ;;   org.eclipse.platform/org.eclipse.core.jobs
                 ;;   org.eclipse.platform/org.eclipse.core.runtime
                 ;;   org.eclipse.platform/org.eclipse.core.commands
                 ;;   ]
                 ;;  ]
                 ]

  :managed-dependencies [
                         [org.eclipse.platform/org.eclipse.swt "3.114.100"]
                         [org.eclipse.platform/org.eclipse.core.runtime "3.19.0"]
                         [org.eclipse.platform/org.eclipse.jface.text "3.16.300"]
                         [org.eclipse.platform/org.eclipse.text "3.10.200"]
                         ;;[org.eclipse.platform/org.eclipse.jface "3.20.0"]
                         ;;[org.eclipse.platform/org.eclipse.core.databinding.beans "1.6.100"]
                         ;;[org.eclipse.platform/org.eclipse.core.databinding.observable "1.9.0"]
                         ;;[org.eclipse.platform/org.eclipse.core.databinding "1.9.0"]
                         ;;[org.eclipse.platform/org.eclipse.core.databinding.property "1.8.0"]
                         ;;[org.eclipse.platform/org.eclipse.jface.databinding "1.11.100"]

                         ;;[org.eclipse.platform/org.eclipse.core.commands "3.9.700"]
                         ;; ~swt
                         ]

  :repositories [["java.net" "https://download.java.net/maven/2"]
                  ["sonatype" {:url "https://oss.sonatype.org/content/repositories/releases"
                              ;; If a repository contains releases only setting
                              ;; :snapshots to false will speed up dependencies.
                              :snapshots false
                              ;; Disable signing releases deployed to this repo.
                              ;; (Not recommended.)
                              :sign-releases false
                              ;; You can also set the policies for how to handle
                              ;; :checksum failures to :fail, :warn, or :ignore.
                              :checksum :fail
                              ;; How often should this repository be checked for
                              ;; snapshot updates? (:daily, :always, or :never)
                              :update :always
                              ;; You can also apply them to releases only:
                               :releases {:checksum :fail :update :always}}]]
  :resource-paths ["resources/images"
                   ;; not ideal, have to place platform specific jar for swt in the lib folder
                   ;; won't download automatically maven style
                   ~swt
                   ;;"lib/com.ibm.icu_64.2.0.v20190507-1337.jar"
                   ;;"lib/org.eclipse.core.commands_3.9.700.v20191217-1850.jar"
                   ;;"lib/org.eclipse.core.databinding.beans_1.6.100.v20191217-1850.jar"
                   ;;"lib/org.eclipse.core.databinding.observable_1.9.0.v20200205-2119.jar"
                   ;;"lib/org.eclipse.core.databinding.property_1.8.0.v20200124-0715.jar"
                   ;;"lib/org.eclipse.core.databinding_1.8.0.v20200205-2008.jar"
                   ;;"lib/org.eclipse.core.runtime_3.17.100.v20200203-0917.jar"
                   ;;"lib/org.eclipse.jface.databinding_1.11.0.v20200205-2119.jar"
                   ;;"lib/org.eclipse.jface.text_3.16.200.v20200218-0828.jar"
                   ;;"lib/org.eclipse.jface_3.19.0.v20200218-1607.jar"
                   ;;"lib/org.eclipse.osgi_3.15.200.v20200214-1600.jar"
                   ;;"lib/org.eclipse.swt_3.114.0.v20200304-0601.jar"
                   ;;"lib/org.eclipse.text_3.10.100.v20200217-1239.jar"
                   ;;"lib/org.eclipse.equinox.common_3.11.0.v20200206-0817.jar"
                   ]
  :main ^:skip-aot jface-swt.core
  :target-path "target/%s"
  :jvm-opts ~jvmOpts ;;["-XstartOnFirstThread"]
  :aliases {"dumbrepl" ["trampoline" "run" "-m" "clojure.main/main"]}
  :profiles {:uberjar {:aot :all}}))
