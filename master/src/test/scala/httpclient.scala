import scala.concurrent.Await

/**
  * Created by zhangxu on 2016/8/5.
  */
object httpclient extends App {

  import akka.actor.ActorSystem
  import akka.stream.ActorMaterializer
  import akka.stream.scaladsl.Source

  import scala.concurrent.duration._

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  //  while (true) {
  //    Source(List("b", "a", "f")).runForeach { ip =>
  //      val connectionFlow = Http().outgoingConnection(ip)
  //      val responseFuture = Source.single(HttpRequest(uri = "/")).via(connectionFlow).runWith(Sink.ignore)
  //      val num = Random.nextInt(10)
  //      responseFuture.onComplete {
  //        case Success(_) => println("request succeded")
  //        case Failure(_) => println("request failed")
  //      }
  //    }
  //
  //
  //
  //
  //    Thread.sleep(10)
  //  }
  val b = Source.tick(initialDelay = 0.second, interval = 1.second, CCC).map(p => p.a).runForeach(p=>p)
  Await.result(b, Duration.Inf)
}

object CCC {
  def a = println(1)
}
