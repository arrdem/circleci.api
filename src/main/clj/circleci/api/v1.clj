(ns circleci.api.v1
  "Tools for interacting both with the documented CircleCI [http 1.1 API][1.1] and the undocumented
  seemingly future Transit based API.

  [1.1][https://circleci.com/docs/api/v1-reference/]"
  {:authors ["Reid 'arrdem' McKenzie <me@arrdem.com>"]}
  (:require [clj-http.client :as http]
            [clojure.spec.alpha :as s]
            [cheshire.core :as json]
            [clojure.string :as str]
            [clojure.instant :as inst]))

(def ^:const +api-prefix+
  "https://circleci.com/api/v1.1")

(s/def ::vcs-type
  #{:github :bitbucket})

(s/def ::username
  string?)

(s/def ::project
  string?)

(s/def ::build-num
  pos-int?)

(s/def ::branch
  string?)

(s/def ::token
  string?)

(s/def ::config
  (s/keys :req-un [::token]
          :opt-un [::vcs-type
                   ::username]))

(defn- do-request [{:keys [token] :as config} url & args]
  (-> (http/get (apply format (str +api-prefix+ url) args)
                {:accept :json
                 :query-params {"circle-token" token}})
      :body
      (json/parse-string
       #(java.net.URLDecoder/decode %))))

(defn me
  "Get information about the signed in user."
  [config]
  (do-request config "/me")) 

(defn projects
  "List of all the projects you're following on CircleCI, with build information organized by branch."
  [config]
  (as-> config %
    (do-request % "/projects")))

(defn build-summary
  "Build summary for each of the last 30 builds for a single git repo."
  [config vcs username project]
  (as-> config %
    (do-request % "/project/%s/%s/%s"
                vcs username project)))

(defn recent-builds
  "Build summary for each of the last 30 recent builds, ordered by `build-num`.

  By default retrieves the last 30 builds for the auth token's user, but can also retrieve the last
  30 builds for a particular project."
  ([config]
   (as-> config %
     (do-request % "/recent-builds")))
  ([config vcs username project]
   (do-request config "/project/%s/%s/%s"
               vcs username project)))

(defn build
  "Full details for a single build.

  The response includes all of the fields from the build summary. This is also the payload for the
  notification webhooks, in which case this object is the value to a key named ‘payload’."
  [config vcs username project build-num]
  (do-request config "/project/%s/%s/%s/%s"
              vcs username project build-num))

(defn build-artifacts
  "List the artifacts produced by a given build."
  [config vcs username project build-num]
  (do-request config "/project/%s/%s/%s/%s/artifacts"
              vcs username project build-num))
