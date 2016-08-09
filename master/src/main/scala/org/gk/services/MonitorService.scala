package org.gk.services

import akka.Done
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import org.gk.common.Configure
import org.gk.services.base.DatabaseSerivces
import org.gk.services.base.DatabaseTables.{Node, NodeStatus, Task}
import slick.driver.MySQLDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
  * Created by zhangxu on 2016/8/8.
  */
object MonitorService {

  import org.gk.services.base.DatabaseSerivces._

  val _monitor_interval = Configure.monitor_interval * 1000

  val _monitor_list = () => {
    val query = for {
      n <- _nodes_Table.filter(_.status === NodeStatus.NORMAL)
      t <- _tasks_Table
      m <- _monitor_Table if m.node === n.ip && m.task === t.name
    } yield (n, t, m)
    db.stream(query.result)
  }
}

class MonitorService(implicit system: ActorSystem, materializer: ActorMaterializer) {

  import MonitorService._
  import org.gk.common.MyType._

  private val log = Logging(this)

  val _node_task_monitor: (Node, Task) => Future[Done] = (node: Node, task: Task) => {
    log.info(s"monitor: ${node.ip},${task.name}")
    Source.single(HttpRequest(uri = s"/api/agent/monitor/${task.name}/${task.category}"))
      .via(Http().outgoingConnection(node.ip, node.port))
      .filter(_.status == StatusCodes.NotFound)
      .mapAsync(11)(_ => _node_task_send(node, task))
      .runWith(Sink.foreach(_ => log.info(s"monitor:${node}.${task}")))
  }

  val _node_task_send: (Node, Task) => Future[Done] = (node: Node, task: Task) => {
    val request =
      HttpRequest(
        HttpMethods.POST,
        uri = s"/api/agent/monitor/${task.name}/${task.category}",
        entity = HttpEntity(ContentTypes.`text/plain(UTF-8)`, scala.io.Source.fromFile("master/data/tasks/" + task.name + "." + task.category).mkString)
      )
    val connectionFlow = Http().outgoingConnection(node.ip, node.port)
    Source.single(request).via(connectionFlow)
      .mapAsync(11)(_ => _node_task_monitor(node, task))
      .runWith(Sink.foreach(_ => log.info(s"monitor send:${node}.${task}")))
  }

  val start = {
    Future {
      while (true) {
        Source.fromPublisher(_monitor_list.apply)
          .filter(System.currentTimeMillis() / 1000 % _._3.interval == 0)
          .mapAsync(4)(r => _node_task_monitor(r._1, r._2)).runWith(Sink.ignore)
        Thread.sleep(_monitor_interval)
      }
    }
  }
}