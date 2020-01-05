(defproject jface-swt "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}


  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojars.nakkaya/miglayout "3.7.3.1"]

                 ;;[org.eclipse/swt-gtk-linux-x86_64 "3.5.2"]
                 ;;[org.eclipse.swt/org.eclipse.swt.cocoa.macosx.x86_64 "4.3"]
                 ;;[org.eclipse/jface "3.3.0-I20070606-0010"]
                 ;; https://mvnrepository.com/artifact/org.eclipse.core/org.eclipse.core.runtime
                 ;;[org.eclipse.core/org.eclipse.core.runtime "3.7.0"]

                 ;; https://mvnrepository.com/artifact/org.eclipse.platform/org.eclipse.jface.text
                 ;;[org.eclipse.platform/org.eclipse.swt "3.112.0"]

                 ;; https://mvnrepository.com/artifact/org.eclipse.platform/org.eclipse.swt.gtk.linux.x86_64
                 ;;causes a big fat crash
                 ;;[org.eclipse.platform/org.eclipse.swt.gtk.linux.x86_64 "3.112.0"]

                 ;; https://mvnrepository.com/artifact/org.eclipse.platform/org.eclipse.jface
                 ;;[org.eclipse.platform/org.eclipse.jface "3.17.0" :exclusions [org.eclipse.platform/org.eclipse.equinox.common org.eclipse.platform/org.eclipse.core.commands org.eclipse.platform/org.eclipse.swt]]
                 

                 ;; https://mvnrepository.com/artifact/org.eclipse.platform/org.eclipse.text
                 ;;[org.eclipse.platform/org.eclipse.text "3.9.0"]

                 ;; https://mvnrepository.com/artifact/org.eclipse.platform/org.eclipse.core.jobs
                 ;;[org.eclipse.platform/org.eclipse.core.jobs "3.10.500"]

                 ;; https://mvnrepository.com/artifact/org.eclipse.platform/org.eclipse.jface.databinding
                 ;;causes a crash
                 ;;[org.eclipse.platform/org.eclipse.jface.databinding "1.9.100"]

                 
                 ;; https://mvnrepository.com/artifact/org.eclipse.platform/org.eclipse.core.databinding.property
                 ;;[org.eclipse.platform/org.eclipse.core.databinding.property "1.7.100" :exclusions [org.eclipse.platform/org.eclipse.equinox.common org.eclipse.platform/org.eclipse.core.databinding.observable] ]

                 ;; https://mvnrepository.com/artifact/org.eclipse.platform/org.eclipse.core.databinding.observable
                 ;;[org.eclipse.platform/org.eclipse.core.databinding.observable "1.8.0" :exclusions [org.eclipse.platform/org.eclipse.equinox.common]]

                 ;; https://mvnrepository.com/artifact/org.eclipse.platform/org.eclipse.core.databinding.beans
                 ;;[org.eclipse.platform/org.eclipse.core.databinding.beans "1.5.100"]



                 ;; https://mvnrepository.com/artifact/org.eclipse.platform/org.eclipse.swt.win32.win32.x86_64
                 ;;[org.eclipse.platform/org.eclipse.swt.win32.win32.x86_64 "3.112.0"]

                 ;; https://mvnrepository.com/artifact/org.eclipse.platform/org.eclipse.swt.cocoa.macosx.x86_64
                 ;;[org.eclipse.platform/org.eclipse.swt.cocoa.macosx.x86_64 "3.112.0"]


                 ;;[org.eclipse.platform/org.eclipse.jface.text "3.15.300"]
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
  :resource-paths ["lib/org.eclipse.swt_3.111.0.v20190605-1801.jar"
                   "lib/org.eclipse.swt.gtk.linux.x86_64_3.112.0.v20190904-0609.jar"
                   "lib/org.eclipse.jface_3.16.0.v20190528-0922.jar" 
                   "lib/org.eclipse.core.runtime_3.15.300.v20190508-0543.jar"
                   "lib/org.eclipse.osgi_3.14.0.v20190517-1309.jar"
                   "lib/org.eclipse.equinox.common_3.10.400.v20190516-1504.jar" 
                   "lib/org.eclipse.core.commands_3.9.400.v20190516-1358.jar"]
  :main ^:skip-aot jface-swt.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
