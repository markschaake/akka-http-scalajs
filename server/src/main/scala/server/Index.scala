package template.server

object Index {
  def apply(cacheBreaker: String, environment: Environment) = {
    val mainScript = environment match {
      case Environment.Dev => "client-fastopt.js"
      case Environment.Prod => "client-opt.js"
    }

    s"""<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
    <title>Akka Http and ScalaJS Template</title>
    <style>
     html {
       height: 100%;
     }
     body {
       height: 100%;
       margin: 0;
     }
     #app {
       height: 100%;
     }
    </style>
  </head>
  <body>
    <div id="app"></div>
    <script type="text/javascript" src="/${cacheBreaker}/client-jsdeps.js"></script>
    <script type="text/javascript" src="/${cacheBreaker}/${mainScript}"></script>
    <script type="text/javascript">
     template.client.Main().main();
    </script>
  </body>
</html>
"""
  }
}
