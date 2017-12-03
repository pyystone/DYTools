package cn.stonepyy.dy.danmu.net;


import cn.stonepyy.dy.danmu.Utils.ByteUtil;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MySocketSend {

    public static final byte[] CLIENT_FLAG = new byte[]{(byte) 0xb1, 0x02, 0x00, 0x00};

    private String mServerHost;
    private int mServerPort;
    private String mContent;
    private SocketCallBack mCallBack;


    private Socket mSocket;

    public MySocketSend(String host, int port, String content, SocketCallBack callBack) {
        mServerHost = host;
        mServerPort = port;
        mContent = content;
        mCallBack = callBack;

    }

    public void send() throws IOException {
        mSocket = new Socket(mServerHost,mServerPort);
        mSocket.getOutputStream().write(makeData());

        Reader reader = new InputStreamReader(mSocket.getInputStream());
        char chars[] = new char[64];

        StringBuilder sb = new StringBuilder();
        while(reader.read(chars) != -1) {
            sb.append(chars);
        }

        mCallBack.rev(sb.toString());

    }

    // 一条斗鱼 Socket 消息包含 5 个部分：
    // 1. 数据长度，大小为后四部分的字节长度，占 4 个字节。
    // 2. 内容和第一部分一样，占 4 个字节。
    // 3. 斗鱼固定的请求码，占 4 个字节。
    //     本地 -> 服务器是 0xb1,0x02,0x00,0x00 。
    //     服务器 -> 本地是 0xb2,0x02,0x00,0x00 。
    // 4. 消息内容。
    // 5. 尾部一个空字节 0x00 ，占 1 个字节。

    private byte[] makeData() {
        byte[] messageBytes;
        try {
            messageBytes = mContent.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        // 需要发送的消息的实际字节长度。
        int messageLength = messageBytes.length + 13;

        byte mByteArrayCache[] = new byte[messageLength];
        // 1
        int length = messageBytes.length + 9;
        byte[] lengthBytes = ByteUtil.toDouYuBytes(length);
        System.arraycopy(lengthBytes, 0, mByteArrayCache, 0, lengthBytes.length);
        // 2
        System.arraycopy(lengthBytes, 0, mByteArrayCache, 4, lengthBytes.length);
        // 3
        System.arraycopy(CLIENT_FLAG, 0, mByteArrayCache, 8, CLIENT_FLAG.length);
        // 4
        System.arraycopy(messageBytes, 0, mByteArrayCache, 12, messageBytes.length);
        // 5
        mByteArrayCache[messageLength - 1] = (byte) 0x00;


        return mByteArrayCache;
    }

    public interface SocketCallBack {
        void rev(String msg);
    }

}
