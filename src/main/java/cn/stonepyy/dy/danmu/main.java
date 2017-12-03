package cn.stonepyy.dy.danmu;

import cn.stonepyy.dy.danmu.Utils.MD5Utils;
import cn.stonepyy.dy.danmu.net.MySocketSend;

import java.io.IOException;
import java.util.UUID;

public class main {
    private static String msg = "type@=loginreq/username@=/ct@=0/password@=/roomid@=533493/devid@=0C586D24974420E4E40D4670400339E6/rt@=1460093455/vk@=3f68859e4118cb19485306941cd2ed8d/ver@=20150929/ltkid@=/biz@=/stk@=/";
    private static String msg2 = "type@=loginreq/roomid@=209436/";
    public static void main(String[] args) {
        String rt = String.valueOf(System.currentTimeMillis() / 1000);
        String devId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        String vk = MD5Utils.MD5(rt + "7oE9nPEG9xXV69phU31FYCLUagKeYtsF" + devId);
        String loginMessage = String.format("type@=loginreq/username@=/ct@=0/password@=/roomid@=%1$s/devid@=%2$s/rt@=%3$s/vk@=%4$s/ver@=20150929/ltkid@=/biz@=/stk@=/",
                209436,
                devId,
                rt,
                vk);

        MySocketSend socket = new MySocketSend("119.90.49.91", 8051, loginMessage, msg -> System.out.print("rev:" + msg));
        try {
            socket.send();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
