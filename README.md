# circleci.api
<img align="right" src="/etc/circleci.api.jpg" width=300/>

[![Clojars Project](http://clojars.org/me.arrdem/circleci.api/latest-version.svg)](https://clojars.org/me.arrdem/circleci.api)

> Alright who broke the build
>
> -- Anonymous

**TL;DR** A quick and dirty client for the CircleCI REST APIs.

## Usage

### V1(.1) API

This is CircleCI's published, [documented](https://circleci.com/docs/api/v1-reference/) API.
At present, only the read endpoints are implemented.

FIXME: enumerate the entire API here.

### V2.0? API

A little spelunking in CircleCI's current live JavaScript revealed what seemed to be ClojureScript in the browser, communicating with a Ring server over Transit.
The only known transit query endpoint is supported, although this endpoint requires a live browser cookie and does not support token based authentication.

Use at your own risk.

## License

Copyright (C) 2018 Reid "arrdem" McKenzie

Distributed under the [Eclipse Public License](/LICENSE) either version 1.0 or (at your option) any later version.
