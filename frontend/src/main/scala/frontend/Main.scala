package frontend

import cats.effect.{ExitCode, IO}
import com.raquo.laminar.nodes.ReactiveRoot
import org.scalajs.dom
import org.scalajs.dom.raw.Element
import com.raquo.laminar.api.L._

import scala.scalajs.js.annotation.{JSExportTopLevel, JSImport}
import scala.scalajs.js

@JSImport("resources/index.css", JSImport.Default)
@js.native
object IndexCSS extends js.Object

object Main {
  val css: IndexCSS.type = IndexCSS

  val createContainer: IO[Element] = IO {
    Option(dom.document.getElementById("root")).getOrElse {
      val elem = dom.document.createElement("div")
      elem.id = "root"
      dom.document.body.appendChild(elem)
      elem
    }
  }

  def emptyContainer(container: dom.Element): IO[Unit] = IO {
    // Removing previous implementation on hot reload
    if (scala.scalajs.LinkingInfo.developmentMode) {
      while (container.children.length > 0) {
        container.removeChild(container.children(0))
      }
    }
  }

  def renderAppInContainer(container: dom.Element): IO[ReactiveRoot] = IO {
    render(container, App())
  }

  val program: IO[ExitCode] =
    for {
      container <- createContainer
      _ <- emptyContainer(container)
      _ <- renderAppInContainer(container)
    } yield ExitCode.Success

  @JSExportTopLevel("main")
  def main(): Unit = {
    program.unsafeRunSync()
  }
}
