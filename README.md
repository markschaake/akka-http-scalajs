# akka-http template project

Template project for Akka HTTP server and ScalaJS web client using scalajs-react and scalacss.

## Highlights

- All Scala (no NPM, no Javascript, no LESS)
- `upickle` for JSON serialization provides:
  - same JSON serailzation / deserialization method on client and server 
  - ability to use shared models
  
## SBT Project Layout

The SBT project consists of a `shared` `crossProject` that is cross built for both JS and JVM and is dependend upon by both `server` and `webclient`. Common models should be defined in `shared` so they can be used on both client and server.

### `server` project design

- `Main` that spins up the application.
- `Service` serves a websocket endpoint and REST API
- `Manager` actor that is top-level supervisor for the application
- `ServerEventPublisher` that handles publishing ServerEvents to websocket connections

### `webclient` project design

TODO
