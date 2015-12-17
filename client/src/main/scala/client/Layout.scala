package template.client

import japgolly.scalajs.react.ReactElement
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.prefix_<^._
import scalacss.Defaults._
import scalacss.ScalaCssReact._

/** CSS for the main application layout */
object LayoutStyle extends StyleSheet {
  import dsl._

  /** Application wrapper div */
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

  val footer = style(
    height(60.px),
    backgroundColor.gray
  )

  /** Main wraps nav and content */
  val main = style(
    flexGrow(1),
    display.flex,
    height(100.%%)
  )

  val selectedNav = mixin(
    borderLeft(2.px, solid, black),
    borderRight(4.px, solid, black),
    backgroundColor(colors.lightGray)
  )

  val nav = style(
    width(150.px),
    minWidth(150.px),
    backgroundColor(colors.lightGray),
    unsafeChild("ul")(
      padding.`0`,
      margin.`0`,
      unsafeChild("li")(
        listStyleType := "none",
        borderLeft(2.px, solid, colors.lightGray),
        borderRight(4.px, solid, colors.lightGray),
        &.hover(
          borderLeft(2.px, solid, colors.darkGray),
          borderRight(4.px, solid, colors.darkGray),
          backgroundColor(colors.lightGray)
        ),
        unsafeChild("a")(
          padding(4.px, 6.px),
          display.block,
          textDecoration := "none",
          color(colors.darkGray),
          &.hover(
            backgroundColor(colors.darkGray),
            color.white
          )
        )
      ),
      unsafeChild("li.active")(selectedNav)
    )
  )

  val content = style(
    flexGrow(1),
    padding(10.px),
    overflow.scroll
  )
}

/** The main application layout that the router uses */
object Layout extends ((RouterCtl[Page], Resolution[Page]) => ReactElement) {
  private def navItem(ctl: RouterCtl[Page], r: Resolution[Page], page: Page, text: String) =
    <.li(
      ctl.link(page)(text),
      ^.classSet("active" -> (page == r.page))
    )

  override def apply(ctl: RouterCtl[Page], r: Resolution[Page]): ReactElement =
    <.div(LayoutStyle.app,
      <.header(LayoutStyle.header, <.h1("Akka Http ScalaJS Template")),
      <.div(LayoutStyle.main,
        <.nav(LayoutStyle.nav,
          <.ul(
            navItem(ctl, r, Page.ServerEvents, "Server Events"),
            navItem(ctl, r, Page.ServerLogs, "Server Logs"),
            navItem(ctl, r, Page.Foos, "Foos")
          )
        ),
        <.div(LayoutStyle.content, r.render())
      ),
      <.footer(LayoutStyle.footer, <.p("Template footer"))
    )
}
