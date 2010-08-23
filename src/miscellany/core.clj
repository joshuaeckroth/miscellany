(ns miscellany.core
  (:use compojure.core)
  (:use hiccup.core)
  (:use hiccup.page-helpers)
  (:use ring.middleware.reload)
  (:use ring.middleware.stacktrace)
  (:use ring.middleware.multipart-params)
  (:use ring.util.response)
  (:require [compojure.route :as route])
  (:use miscellany.middleware)
  (:use ring.middleware.file)
  (:use ring.middleware.file-info)
  (:use miscellany.views)
  (:use miscellany.forms))

(def production?
  (= "production" (get (System/getenv) "APP_ENV")))

(def development?
  (not production?))

(defn parse-input [a b]
  [(Integer/parseInt a) (Integer/parseInt b)])

(defroutes handler
  (GET "/" [] (view-documents))
  (GET "/new-document" [] (form-new-document))
  (POST "/new-document" [path type tags content] (save-new-document path type tags content))
  (GET "/upload-document" [] (form-upload-document))
  (POST "/upload-document" [path tags upload] (save-upload-document path tags upload))
  (GET "/:path" [path] (view-document path)))

(def app
     (-> #'handler
	 (wrap-file "public")
	 (wrap-file-info)
	 (wrap-request-logging)
	 (wrap-if development? wrap-reload '[miscellany.middleware miscellany.core])
	 (wrap-reload '[miscellany.middleware miscellany.core])
	 (wrap-bounce-favicon)
	 (wrap-exception-logging)
	 (wrap-multipart-params)
	 (wrap-if production? wrap-failsafe)
	 (wrap-if development? wrap-stacktrace)))
