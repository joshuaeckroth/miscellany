(use 'ring.adapter.jetty)
(require 'miscellany.core)
(use 'somnium.congomongo)
(use 'miscellany.db)

(set-connection! (db-connect))

(let [port (Integer/parseInt (get (System/getenv) "PORT" "8080"))]
  (run-jetty #'miscellany.core/app {:port port}))



