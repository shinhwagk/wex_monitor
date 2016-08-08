package org.gk.services.base
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import play.api.libs.ws.ahc.AhcWSClient

/**
  * Created by zhangxu on 2016/8/2.
  */
object ActorSystemServices {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher
  implicit val wsClient: AhcWSClient = AhcWSClient()


//  val pickMaxOfThree = GraphDSL.create() { implicit b =>
//    import GraphDSL.Implicits._
//
//    val f1 = b.add(Flow[String])
//    val f0 = b.add(Flow[String])
//    val f2 = b.add(Flow[String])
//
//    f0.out ~> f2 ~> f1.in
//
//    UniformFanInShape(f1.out, f0.in)
//  }
//
//  val resultSink = Sink.foreach[String](println(_))

  //  val g1 = RunnableGraph.fromGraph(GraphDSL.create(resultSink,pairUpWithToString)((_,_)){ implicit b => (sink,fll) =>
  //    import GraphDSL.Implicits._
  //
  //    val pm3: UniformFanInShape[String, String] = b.add(pickMaxOfThree)
  //
  ////    Source.fromPublisher(DatabaseSerivces.selectHeartNodes(0)) ~> pm3.in(0)
  //
  //    Source.single("1") ~> fll ~> pm3.in(0)
  //    pm3.out ~> sink.in
  //    ClosedShape
  //  })

//  val pairUpWithToString =
//    Flow.fromGraph(GraphDSL.create() { implicit b =>
//      import GraphDSL.Implicits._
//      val f1 = b.add(Flow[String].buffer(10,OverflowStrategy.backpressure))
//      val f0 = b.add(Flow[Unit])
//
//      Source(List("1","3")) ~> f1.in
//
//      f0.out ~> Sink.ignore
//
//      FlowShape(f0.in, f1.out)
//    })

//  def main(args: Array[String]) {
//    val g = Source.tick(initialDelay = 0.second, interval = 1.second, ())
//      .via(pairUpWithToString)
//      .runForeach(println)
//    Concurrent.broadcast[TweetInfo]
//    Await.result(g, Duration.Inf)
//  }
//
//  val mergedStream = Source[String]() { implicit builder =>
//
//    val merge = builder.add(Merge[String](streams.length))
//
//    for (i <- 0 until streams.length) {
//      builder.addEdge(builder.add(streams(i)), merge.in(i))
//    }
//
//    merge.out
//  }
}
