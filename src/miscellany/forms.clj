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

