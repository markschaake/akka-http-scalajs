package example.akkwebsockets.webclient

import scalacss.Defaults._

trait ColorScheme { this: StyleSheet.Inline =>
  import dsl._

  object Colors {
    val navBackground = Color("#ccc")
  }
}

trait BaseStyle extends StyleSheet.Inline with ColorScheme
