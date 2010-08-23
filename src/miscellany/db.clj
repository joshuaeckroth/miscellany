(ns miscellany.db
  (:use couchdb.client)
  (:use [clojure.string :only (split trim)])
  (:use [clojure.java.io :as io :only (reader)])
  (:import [java.io FileInputStream]))

(defn db-new-document
  [path type tags content]
  (document-create
   "http://localhost:5984" "miscellany"
   {:path path :type type :tags (if tags (map trim (split tags #","))) :content content}))

(defn db-new-document-with-attachment
  [path tags upload]
  (let [doc (document-create
	     "http://localhost:5984" "miscellany"
	     {:path path :type (:content-type upload) :tags (if tags (map trim (split tags #",")))
	      :filename (:filename upload) :size (:size upload)})
	payload (FileInputStream. (:tempfile upload))]
    (attachment-create "http://localhost:5984" "miscellany" (:_id doc) "attachment" payload (:content-type upload))))

(defn db-list-documents
  []
  (:rows (view-get
	  "http://localhost:5984" "miscellany" "ui-views" "list-documents")))