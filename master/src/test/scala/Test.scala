import java.io._
import java.net.InetSocketAddress
import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl.{Flow, GraphDSL, RunnableGraph, Sink, Source, Unzip, Zip}
import org.apache.sshd.client.SshClient
import org.apache.sshd.client.channel.ClientChannel
import org.apache.sshd.client.future.AuthFuture
import org.apache.sshd.client.session.ClientSession

import scala.concurrent.Future
import scala.collection.mutable.Map
import scala.util.Random
import scala.concurrent.duration._

/**
  * Created by zhangxu on 2016/8/1.
  */
class testd extends Iterator[Int] {
  var b = 0

  override def hasNext: Boolean = b < 10

  override def next(): Int = {
    b +=1
    b
  }
}

object Test extends App {

  import org.gk.services.base.ActorSystemServices._

  //  val client = SshClient.setUpDefaultClient()
//  client.start();
//  val connect = client.connect("root", "10.65.103.53", 22)
//  val session = connect.verify().getSession
//  session.addPasswordIdentity("000000");
//  session.auth().verify()
//  val ex = session.createExecChannel("df -h | grep /dev")
//  ex.setOut(System.out)
//
//  ex.open().verify()
//
//  var b = 0
//  while (b < 5) {
//    Thread.sleep(1000)
//    b += 1
//  }
//
//  ex.close()
  //  session.close()
  Source.fromIterator(() => new testd).runForeach(println)


  //  session.auth().verify(...timeout...);
  //
  //  val channel = session.createChannel(ClientChannel.CHANNEL_SHELL)
  //  channel.setIn(new NoCloseInputStream(System.in));
  //  channel.setOut(new NoCloseOutputStream(System.out));
  //  channel.setErr(new NoCloseOutputStream(System.err));
  //  channel.open();
  //  channel.waitFor(ClientChannel.CLOSED, 0);

  //  val cmd = "ifconfig";
  //  val client = SshClient.setUpDefaultClient();
  //  client.start();
  //  val session: ClientSession = client.connect("root", "10.65.103.53", 22).verify(10, TimeUnit.SECONDS).getSession
  //  val session2 = client.connect("root", "10.65.103.53", 22).await().getSession
  //  session.addPasswordIdentity("000000")
  //  val b: AuthFuture = session.auth().verify(10, TimeUnit.SECONDS);
  //while(!b.await()){ println(1)}
  //
  //  val shell = session.createExecChannel("uname");
  //  // Create a pipe that will block reading when the buffer is full
  //  val pis = new PipedInputStream();
  //  val pos = new PipedOutputStream(pis)
  ////  shell.setOut()
  //
  //
  //  shell.setOut(System.out);
  //  shell.open();
  //
  //  shell.close();

  //  if(session.auth().await()){
  //    System.out.println("auth failed");
  //  }

  //  def b: Int = {
  //    Random.nextInt(11)
  //  }
  //  val b1 = ()=>1
  //
  //  def a(a: =>Int): Int ={
  //    a
  //  }
  //
  //  println(a({println(1);1}))
  //  println(a(b))
  //  println(a(b))
  //  println(a(b1()))
  //  import org.gk.services.base.ActorSystemServices._

  //
  //  Source(1 to 10).via(Flow[Int]
  //    .map { p => println(p, 1); Thread.sleep(1000); p + 1 })
  //    .buffer(5, OverflowStrategy.backpressure)
  //    .map { p => println(p, 3); Thread.sleep(1000); p + 1 }
  //    .buffer(2, OverflowStrategy.backpressure)
  //    .runWith(Sink.foreach { p => println(p, 2); Thread.sleep(100000) })
  //  Source.tick(initialDelay = 0.second, interval = 1.second, ())
  //    .map{_ =>println(1,1); 1}.buffer(5,OverflowStrategy.backpressure)
  //    .map{_ =>println(1,3); 1}.buffer(2,OverflowStrategy.backpressure)
  //    .runWith(Sink.foreach{p => println(p,2) ;Thread.sleep(1000000); })
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
