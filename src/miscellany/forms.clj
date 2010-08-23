(ns miscellany.forms
  (:use miscellany.views)
  (:use miscellany.db)
  (:use hiccup.form-helpers)
  (:use ring.util.response))

(defn form-new-document []
  (view-layout
   [:h2 "New document"]
   (form-to [:post "/new-document"]
	    (label "path" "Path:")
	    (text-field "path")
	    (label "type" "Type:")
	    (text-field "type")
	    (label "tags" "Tags:")
	    (text-field "tags")
	    (label "content" "Content:")
	    (text-area "content")
	    (submit-button "Save"))))

(defn save-new-document [path type tags content]
  (db-new-document path type tags content)
  (redirect (str "/" path)))

(defn form-upload-document []
  (view-layout
   [:h2 "Upload document"]
   (form-to {:enctype "multipart/form-data"}
	    [:post "/upload-document"]
	    (label "path" "Path:")
	    (text-field "path")
	    (label "tags" "Tags:")
	    (text-field "tags")
	    (label "upload" "Upload:")
	    (file-upload "upload")
	    (submit-button "Upload"))))

(defn save-upload-document [path tags upload]
  (db-new-document-with-attachment path tags upload)
  (redirect (str "/" path)))


