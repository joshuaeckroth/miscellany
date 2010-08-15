(use 'ring.adapter.jetty)
(require 'miscellany.core)

(let [port (Integer/parseInt (get (System/getenv) "PORT" "8080"))]
  (run-jetty #'miscellany.core/app {:port port}))



