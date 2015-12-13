package example.akkwebsockets.webclient

import scalacss.Defaults._
import scalacss.mutable

class StyleTheme(implicit r: mutable.Register) extends StyleSheet.Inline()(r) {
  import dsl._
  object Colors {
    val lightGray = c"#ccc"
    val darkGray = c"#333"
  }
}
