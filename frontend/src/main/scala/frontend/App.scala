package frontend

import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes.ReactiveHtmlElement
import org.scalajs.dom.html
import HttpClient._
import org.scalajs.dom
import sttp.client._
import io.circe.generic.auto._
import io.circe.parser.decode
import models.SharedModelClass

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.util.{Random, Success, Try}

object App {

  private val css = AppCSS
  dom.console.log(css.asInstanceOf[js.Object])

  val postBus: EventBus[Int] = new EventBus()

  val numberToPost: Var[Int] = Var(0)

  val returned: EventStream[String] = postBus.events.flatMap(
    nbr =>
      EventStream.fromFuture(
        boilerplate
          .response(asStringAlways)
          .post(path("hello").param("nbr", nbr.toString))
          .send()
          .map(_.body)
      )
  )

  def randomSharedModel(): SharedModelClass = SharedModelClass(Random.nextString(5), Random.nextInt())

  def apply(): ReactiveHtmlElement[html.Div] = div(
    className := "App",
    h1("Frontend works!"),
    section(
      h2("Backend works?"),
      p(child <-- EventStream.fromFuture(boilerplate.response(asStringAlways).get(path("hello")).send().map(_.body)))
    ),
    section(
      h2("Post to backend!"),
      input(
        value <-- numberToPost.signal.map(_.toString),
        inContext(
          thisElem =>
            onInput.mapTo(Try(thisElem.ref.value.toInt)).collect { case Success(nbr) => nbr } --> numberToPost.writer
        )
      ),
      button("Click me!", onClick.mapTo(numberToPost.now) --> postBus.writer),
      br(),
      span("Returned: ", child <-- returned.map(identity[String]))
    ),
    section(
      h2("Database works?"),
      p(
        button(
          onClick --> (
              _ =>
                boilerplate
                  .response(ignore)
                  .put(path("models", "insert"))
                  .body(randomSharedModel())
                  .send()
            ),
          "Insert random element"
        )
      ), {
        val downloadBus = new EventBus[Boolean]()

        p(
          button("Download models", onClick.mapTo(true) --> downloadBus.writer),
          ul(
            children <-- downloadBus.events.flatMap(
              _ =>
                EventStream
                  .fromFuture(
                    boilerplate
                      .get(path("models", "get"))
                      .response(asStringAlways.map(decode[List[SharedModelClass]]))
                      .send()
                      .map(_.body.getOrElse(Nil))
                  )
                  .map(_.map(_.toString).map(li(_)))
            )
          )
        )
      }
    )
  )

}
