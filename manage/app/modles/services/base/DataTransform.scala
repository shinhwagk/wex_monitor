package modles.services.base

import java.text.SimpleDateFormat
import java.util.Date

import play.api.libs.json.{Json, Reads, Writes}

/**
  * Created by zhangxu on 2016/8/3.
  */
object DataTransform {

  private implicit val personWrites: Reads[NodeInfo] = Json.reads[NodeInfo]
  private implicit val personReads: Writes[NodeInfo] = Json.writes[NodeInfo]

  private case class NodeInfo(ip: String, status: String, hostname: String, timestamp: Long, environment: String, retry: Int) {
    def transformTimestampToDate: NodeInfo = {
      val latency = System.currentTimeMillis() - timestamp
      NodeInfo(ip, status, hostname, latency, environment, retry)
    }
  }

  def nodesDataTransform(jsonString: List[String]): String =
    Json.toJson(jsonString.map(Json.parse(_).as[NodeInfo]).map(_.transformTimestampToDate)).toString()

}
