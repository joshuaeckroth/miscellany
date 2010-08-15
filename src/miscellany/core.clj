(ns miscellany.core
  (:use compojure.core)
  (:use hiccup.core)
  (:use hiccup.page-helpers)
  (:use somnium.congomongo)
  (:use [somnium.congomongo.util :only [named]])
  (:use ring.middleware.reload)
  (:use ring.middleware.stacktrace)
  (:use ring.util.response)
  (:require [compojure.route :as route])
  (:import [com.mongodb Mongo])
  (:use miscellany.middleware)
  (:use ring.middleware.file)
  (:use ring.middleware.file-info))

(def production?
  (= "production" (get (System/getenv) "APP_ENV")))

(def development?
  (not production?))

(defn view-layout [& content]
  (html
   (doctype :xhtml-strict)
   (xhtml-tag "en"
	      [:head
	       [:meta {:http-equiv "Content-type"
		       :content "text/html; charset=utf-8"}]
	       [:title "miscellany"]
	       [:link {:href "/miscellany.css" :rel "stylesheet" :type "text/css"}]]
	      [:body content])))

(defn view-input [& [a b]]
  (view-layout
   [:h2 "add two numbers"]
   [:form {:method "post" :action "/"}
    (if (and a b) [:p "those are not both numbers"])
    [:input.math {:type "text" :name "a"}] [:span.math " + "]
    [:input.math {:type "text" :name "b"}] [:br]
    [:input.action {:type "submit" :value "add"}]]))

(defn view-output [a b sum]
  (view-layout
   [:h2 "two numbers added"]
   [:p.math a " + " b " = " sum]
   [:a.action {:href "/"} "add more numbers"]))

(defn parse-input [a b]
  [(Integer/parseInt a) (Integer/parseInt b)])

(defroutes handler
  (GET "/" [] (view-input))
  (POST "/" [a b]
	(try
	  (let [[a b] (parse-input a b)
		sum (+ a b)]
	    (view-output a b sum))
	  (catch NumberFormatException e
	    (view-input a b))))
  (ANY "/*" [path]
       (redirect "/")))

(def app
     (-> #'handler
	 (wrap-file "public")
	 (wrap-file-info)
	 (wrap-request-logging)
	 (wrap-if development? wrap-reload '[miscellany.middleware miscellany.core])
	 (wrap-reload '[miscellany.middleware miscellany.core])
	 (wrap-bounce-favicon)
	 (wrap-exception-logging)
	 (wrap-if production? wrap-failsafe)
	 (wrap-if development? wrap-stacktrace)))

(defn mongo-connect []
  (let [mongo (Mongo. "flame.mongohq.com" 27078)
	db (doto (.getDB mongo (named "artifice-dot-cc"))
	     (.authenticate "joshuaeckroth" (char-array "lambda%0")))]
    {:mongo mongo :db db}))

(defn mongo-test []
  (let [conn (mongo-connect)]
    (with-mongo conn
      (insert! :robots {:name "robby"})
      (println (fetch-one :robots))
      (println (collections)))
    (close-connection conn)))

(defn -main [] (mongo-test))