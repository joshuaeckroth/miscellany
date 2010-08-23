(ns miscellany.views
  (:use hiccup.core)
  (:use hiccup.page-helpers)
  (:use hiccup.form-helpers)
  (:use miscellany.db))

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

(defn mk-doc-summary [doc]
  [:p [:a {:href (str "/" (:path doc))} (:path doc)] "&mdash;" (:content doc)])

(defn view-documents []
  (apply view-layout
   [:h2 "All documents"]
   (map (fn [doc] (mk-doc-summary (:value doc))) (db-list-documents))))

(defn view-document [path]
  (view-layout
   [:h2 "Document"]
   [:h3 path]))
