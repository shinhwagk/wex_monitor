import akka.actor.{ActorSystem, Props}
import akka.stream.ActorMaterializer
import org.gk.services.base.DatabaseSerivces

/**
  * Created by zhangxu on 2016/8/1.
  */
object TestHeartService extends App {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

//  val myActor = system.actorOf(Props(new MyActor()), "myactor2")

//  var num = 0
//  while (num < 100000) {
////    DatabaseSerivces.getNormalNodes.foreach(_.foreach(myActor ! Heart(_)))
//    DatabaseSerivces.getNormalNodes.foreach(println)
//    Thread.sleep(1000)
//  }
}
