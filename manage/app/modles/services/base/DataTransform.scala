package modles.services.base

import java.text.SimpleDateFormat
import java.util.Date

import play.api.libs.json.{Json, Reads, Writes}

/**
  * Created by zhangxu on 2016/8/3.
  */
object DataTransform {
  private val sdf = new SimpleDateFormat("yyyyMMdd:HH:mm");

  private implicit val personWrites: Reads[NodeInfo] = Json.reads[NodeInfo]
  private implicit val personReads: Writes[NodeInfo] = Json.writes[NodeInfo]

  private case class NodeInfo(ip: String, status: String, hostname: String, timestamp: String, environment: String) {
    def transformTimestampToDate: NodeInfo = {
      val dateString = sdf.format(new Date(timestamp.toLong))
      NodeInfo(ip, status, hostname, dateString, environment)
    }
  }

  def nodesDataTransform(jsonString: List[String]): String = {
    Json.toJson(jsonString.map(Json.parse(_).as[NodeInfo]).map(_.transformTimestampToDate)).toString()
  }

}
