(ns circleci.api.v2
  "Tools for interacting both with the undocumented and seemingly
  future Transit based API.

  [1.1][https://circleci.com/docs/api/v1-reference/]"
  {:authors ["Reid 'arrdem' McKenzie <me@arrdem.com>"]
   :license "https://www.eclipse.org/legal/epl-v10.html"}
  (:require [clojure.string :as str]
            [clojure.spec.alpha :as s]
            [clj-http.client :as http]
            [cognitect.transit :as transit]))

(def ^:const +query-endpoint+
  "https://circleci.com/query-api")

(defn- transit-request [config, payload]
  (with-open [s (java.io.ByteArrayOutputStream. 4096)]
    (transit/write (transit/writer s :json) payload)
    (-> (http/get +query-endpoint+
              {:as :transit+json
               :body (.toString s)
               :headers {:content-type "application/transit+json"}
               :cookies {"session" {:value
                                    ;; Cookie manually extracted from logging into circle
                                    ;; Only ring-session and _uetsid are memaningful to auth
                                    ""}}})
        :body)))

(defn get-workflow-status [config, workflow-id]
  {:pre [(uuid? workflow-id)]}
  (transit-request config
   {:type :get-workflow-status,
    :params {:run/id workflow-id}}))
