import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl.{Flow, GraphDSL, RunnableGraph, Sink, Source, Unzip, Zip}

import scala.concurrent.Future
import scala.collection.mutable.Map
import scala.util.Random
import scala.concurrent.duration._

/**
  * Created by zhangxu on 2016/8/1.
  */
object Test extends App {

  import service.base.ActorSystemServices._

  //
  //  Source(1 to 10).via(Flow[Int]
  //    .map { p => println(p, 1); Thread.sleep(1000); p + 1 })
  //    .buffer(5, OverflowStrategy.backpressure)
  //    .map { p => println(p, 3); Thread.sleep(1000); p + 1 }
  //    .buffer(2, OverflowStrategy.backpressure)
  //    .runWith(Sink.foreach { p => println(p, 2); Thread.sleep(100000) })
  Source.tick(initialDelay = 0.second, interval = 1.second, ())
    .map{_ =>println(1,1); 1}.buffer(5,OverflowStrategy.backpressure)
    .map{_ =>println(1,3); 1}.buffer(2,OverflowStrategy.backpressure)
    .runWith(Sink.foreach{p => println(p,2) ;Thread.sleep(1000000); })
  //  val start = RunnableGraph.fromGraph(GraphDSL.create() { implicit b =>
  //    import GraphDSL.Implicits._
  //
  //    val unzip = b.add(Unzip[Int,Boolean])
  //    val zip = b.add(Zip[Int,Boolean]())
  //
  //    val s  = Source(List((1,true),(2,false),(3,true)))
  //
  //    s ~> unzip.in
  //         unzip.out1 ~> Flow[Boolean].filter(p=>p  ).map{p=>println(p);p} ~> zip.in1
  //         unzip.out0 ~> Flow[Int].map(_ + 0) .map{p=>println(p);p}        ~> zip.in0
  //                                                       zip.out ~> Sink.foreach(println)
  //
  //    ClosedShape
  //  }).run()
}
