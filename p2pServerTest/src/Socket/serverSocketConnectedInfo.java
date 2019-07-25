package Socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//定义一个连接套接字类，最外层链接后，传参socket，各处均可调用

public class serverSocketConnectedInfo {
    //私有服务套接字
    private ServerSocket serverSocket;
    //私有套接字
    private Socket socket;


    //构造函数, 服务器公布端口号
    public serverSocketConnectedInfo(int openPort) {
        //服务端公开端口，等待客户端进行连接
        try {
            serverSocket = new ServerSocket(openPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Wait for client connect...");
        //阻塞等待 客户端 Socket 链接，若链接则accept()正常返回 已连接 socket
        try {
            socket = serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(socket.getLocalSocketAddress()+" SUCCESS TO CONNECT...");
//        System.out.println("getHostAddress = " + socket.getInetAddress().getHostAddress());
//        System.out.println("getCanonicalHostName = " + socket.getInetAddress().getCanonicalHostName());
//        System.out.println("getHostName = " + socket.getInetAddress().getHostName());
//        System.out.println("from who ? = " + socket.getInetAddress()); //
    }





    //析构函数
    public void finalize(){
        //关闭套接字
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //关闭服务套接字
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //返回服务套接字
    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    //返回已连接的套接字
    public Socket getSocket() {
        return socket;
    }


}
