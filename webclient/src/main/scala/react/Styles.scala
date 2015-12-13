package example.akkwebsockets.webclient

import scalacss.Defaults._

object Styles extends BaseStyle {
  import dsl._

  val app = style(
    addClassName("app"),
    display.flex,
    flexDirection.column,
    height(100.%%),
    fontFamily := "monospace"
  )

  val header = style(
    backgroundColor.black,
    color.white,
    textAlign.center,
    unsafeChild("h1")(
      marginTop(6.px),
      marginBottom(6.px)
    )
  )

  val main = style(
    flexGrow(1),
    display.flex,
    height(100.%%)
  )

  val footer = style(
    height(60.px)
  )

  val nav = style(
    width(200.px),
    backgroundColor(Colors.navBackground),
    unsafeChild("ul")(
      padding.`0`,
      margin.`0`,
      unsafeChild("li")(
        listStyleType := "none",
        padding(4.px, 6.px),
        borderLeft(2.px, solid, Colors.navBackground),
        borderRight(4.px, solid, Colors.navBackground),
        &.hover(
          borderLeft(2.px, solid, black),
          borderRight(4.px, solid, black),
          backgroundColor(Colors.navBackground)
        )
      )
    )
  )

  val content = style(
    flexGrow(1)
  )
}
