(ns miscellany.db
  (:use couchdb.client)
  (:use [clojure.string :only (split trim)]))

(defn db-new-document
  [path type tags content]
  (document-create
   "http://localhost:5984" "miscellany"
   {:path path :type type :tags (if tags (map trim (split tags #","))) :content content}))

(defn db-list-documents
  []
  (:rows (view-get
	  "http://localhost:5984" "miscellany" "ui-views" "list-documents")))