# akka-http-server-events
Template project for Akka HTTP server with server-sent events via websockets.

## Project Architecture

- Main that spins up the application.

- Service that defines REST API

- Manager actor that is top-level supervisor for the application

- ServerEventPublisher that handles publishing ServerEvents to websocket connections

- ScalaJS / ScalaCSS / scalajs-react frontend
