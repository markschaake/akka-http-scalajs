# akka-http template project

Template project for Akka HTTP server and ScalaJS web client using scalajs-react and scalacss.

## Running the Application

The template is currently set up for a "dev" process. Deployed application build setup is a TODO. To run the application:

1. clone the repository
2. start `sbt` in the repository root directory
3. build the webclient:

   ```shell
   > client/fastOptJS
   ```

4. start the server:

   ```shell
   > server/reStart
   ```

5. navigate to the application at http://localhost:8081

You can make changes to webclient code and run `> client/fastOptJS` while the server is running and the changes will get picked up. You can leverage tilde-triggered task running to automatically re-generate javascript on source changes:

```shell
> ~client/fastOptJS
```

## Template Highlights

- Akka HTTP server
- All Scala client-side code (ScalaJS + ScalaJS-React + ScalaCSS)
- `upickle` for JSON serialization provides:
  - same JSON serailzation / deserialization method on client and server 
  - ability to use shared models
- Websocket for server-sent events
  
## SBT Project Layout

The SBT project consists of a `shared` `crossProject` that is cross built for both JS and JVM and is dependend upon by both `server` and `webclient`. Common models should be defined in `shared` so they can be used on both client and server.

### `server` project design

- `Main` that spins up the application.
- `Service` serves a websocket endpoint and REST API
- `Manager` actor that is top-level supervisor for the application
- `ServerEventPublisher` that handles publishing ServerEvents to websocket connections

### `webclient` project design

TODO
