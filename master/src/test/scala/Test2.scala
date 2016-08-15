import java.io.ByteArrayInputStream

import com.jcraft.jsch.JSch

/**
  * Created by zhangxu on 2016/8/3.
  */
object Test2 extends App{
  // 宣告連線資訊
  val user = "root";
  val password = "000000";
  val hostIP = "10.65.103.53";
  val port = 22;

  // 欲執行的指令
  val command = "df -h\n";

  // 建立連線
  val jsch = new JSch();
  val session = jsch.getSession(user, hostIP, port);
  session.setPassword(password);
  session.setConfig("StrictHostKeyChecking", "no");
  session.connect(10 * 1000);
  val channel = session.openChannel("shell");

  // 執行指令
  val is = new ByteArrayInputStream(command.getBytes());
  channel.setInputStream(is);
  channel.setOutputStream(System.out);
  channel.connect(); // 進行連接
  Thread.sleep(5 * 1000);

  // 中斷連線並關閉 seesion 物件
  channel.disconnect();
  session.disconnect();

}
