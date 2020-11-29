# jface-swt

An attempt to get a jface/swt application up and running using pure clojure/lein for the running/building

To get this running in the repl some tricks are needed
ncider inside of emacs doesn't work
need to run lein dumbrepl in a command window
"lein dumbrepl" is something custom defined in the project.cli to get around the threading issues with running swt via a repl
found it on stack overflow 

then require the namespace (load it):
(require '[jface-swt.core])

then switch to the namespace
(in-ns 'jface-swt.core)

then run main:
(-main)

problem is that the repl is not responsive until gui is closed due to event loop i suppose


