package template.client

import scalacss.Defaults._
import scalacss.mutable

/** Color scheme and universal styles */
class Theme(implicit r: mutable.Register) extends StyleSheet.Inline()(r) {
  import dsl._
  object Colors {
    val lightGray = c"#ccc"
    val darkGray = c"#333"
  }
}

/** Base trait that provides acces to the main theme styles */
trait StyleSheet extends StyleSheet.Inline {
  val theme = new Theme
  val colors = theme.Colors
}
