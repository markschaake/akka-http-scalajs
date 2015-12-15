package template.client.todos

import template.client.StyleSheet

import japgolly.scalajs.react.Addons.ReactCssTransitionGroup
import japgolly.scalajs.react.BackendScope
import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._
import org.scalajs.dom.window

import scalacss.ScalaCssReact._
import scalacss.Defaults._

object TodoListStyle extends StyleSheet {
  import dsl._

  val todoList = style(
    addClassName("todo-list"),
    unsafeChild("li")(
      color.green,
      listStyleType := "none",
      cursor.pointer
    )
  )
}

class TodoListBackend(scope: BackendScope[Unit, Vector[String]]) {
  def handleAdd =
    scope.modState(_ :+ window.prompt("Enter some text"))

  def handleRemove(i: Int) =
    scope.modState(_.zipWithIndex.filterNot(_._2 == i).map(_._1))

  def render(state: Vector[String]) =
    <.div(TodoListStyle.todoList,
      <.button(^.onClick --> handleAdd, "Add Item"),
      ReactCssTransitionGroup(
        "example",
        component = "ul",
        enterTimeout = 1,
        leaveTimeout = 1
      )(
        state.zipWithIndex.map { case (s, i) =>
          <.li(^.key := s, ^.onClick --> handleRemove(i), s)
        }: _*
      )
    )
}

object TodoList {
  val component = ReactComponentB[Unit]("TodoList")
  .initialState(Vector("hello", "world", "click", "me"))
  .renderBackend[TodoListBackend]
  .buildU
}
