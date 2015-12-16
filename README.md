# akka-http template project

Template project for Akka HTTP server and ScalaJS web client using scalajs-react and scalacss.

## Running the Application

The template is currently set up for a "dev" process. Deployed application build setup is a TODO. To run the application:

1. clone the repository
2. start `sbt` in the repository root directory
3. start the server:

   ```shell
   > server/reStart
   ```

4. navigate to the application at http://localhost:8081

You can make changes to client code and run `> server/reStart` while the server is running and the changes will get picked up. You can leverage tilde-triggered task running to automatically re-start the server on source changes:

```shell
> ~server/reStart
```

**Note**: you can significantly speed up client build time if you have NodeJS (`npm` on `PATH`) by adding a `client/local.sbt` containing:

```scala
postLinkJSEnv in Global := NodeJSEnv().value
```

## Template Highlights

- Akka HTTP server
- All Scala client-side code (ScalaJS + ScalaJS-React + ScalaCSS)
- `upickle` for JSON serialization provides:
  - same JSON serailzation / deserialization method on client and server 
  - ability to use shared models
- Websocket for server-sent events
  
## SBT Project Layout

The SBT project consists of a `shared` `crossProject` that is cross built for both JS and JVM and is dependend upon by both `server` and `client`. Common models should be defined in `shared` so they can be used on both client and server.

### `server` project design

- `Main` that spins up the application.
- `Service` serves a websocket endpoint and REST API
- `Manager` actor that is top-level supervisor for the application
- `ServerEventPublisher` that handles publishing ServerEvents to websocket connections

### `client` project design

TODO
