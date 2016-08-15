import java.io.{BufferedReader, InputStreamReader}
import java.util.stream.Stream

import com.jcraft.jsch.{ChannelExec, JSch, UserInfo}

/**
  * Created by zhangxu on 2016/8/10.
  */
object test3 extends App {
  val jsch = new JSch();
  val addr = "10.65.103.53"
  val user = "root"
  val pass = "000000"

  val session = jsch.getSession(user, addr, 22);
  session.setPassword(pass)
  session.setUserInfo(new MyUserInfo)
  session.connect();


  val channel = session.openChannel("exec");
  val c: ChannelExec = channel.asInstanceOf[ChannelExec]
  val command = "uname -s -r -v"
  c.setCommand(command)
  channel.setInputStream(null);

  val input: BufferedReader = new BufferedReader(new InputStreamReader(channel.getInputStream()));
  channel.connect();
  System.out.println("The remote command is: " + command);

  println(input.readLine())
}

class MyUserInfo extends UserInfo {
  override def promptPassword(message: String): Boolean = {
    System.out.println("MyUserInfo.promptPassword()");
     false;
  }

  override def promptYesNo(message: String): Boolean = {
    System.out.println("MyUserInfo.promptYesNo()");
     false;
  }

  override def showMessage(message: String): Unit = {
    System.out.println("MyUserInfo.showMessage()");
  }

  override def getPassword: String = {
    System.out.println("MyUserInfo.getPassword()");
     null;
  }

  override def promptPassphrase(message: String): Boolean = {
    System.out.println("MyUserInfo.promptPassphrase()");
     false;
  }

  override def getPassphrase: String = {
    System.out.println("MyUserInfo.getPassphrase()");
    null;
  }
}