package Socket;

import java.io.IOException;
import java.net.Socket;

//定义一个连接套接字类，最外层链接后，传参socket，各处均可调用

public class SocketConnectedInfo {
    //私有套接字
    private Socket socket;

    //构造函数, 尝试连接，套接字
    public SocketConnectedInfo(String host, int port) {
        //需要服务器的IP地址和端口号，才能获得正确的Socket对象
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //默认构造函数, 尝试连接，套接字
    public SocketConnectedInfo() {
        //需要服务器的IP地址和端口号，才能获得正确的Socket对象
        try {
            socket = new Socket("127.0.0.1", 1234);//默认IP地址、端口配置
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //析构函数
    public void finalize(){
        //关闭套接字
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //返回已连接的套接字
    public Socket getSocket() {
        return socket;
    }
}
