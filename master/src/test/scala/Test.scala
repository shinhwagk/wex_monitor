import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ActorMaterializerSettings, Supervision}
import akka.stream.scaladsl.{Flow, Source}

import scala.concurrent.Future
import scala.collection.mutable.Map
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Random

/**
  * Created by zhangxu on 2016/8/1.
  */
object Test extends App {

  import service.base.ActorSystemServices._

  Source(1 to 10).via(Flow[Int].map(b(_))).runForeach(println)

  val b: (Int) => Int = (x: Int) => x + 1
}
