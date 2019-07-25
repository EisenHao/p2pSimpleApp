package Controler;

import Message.MsgFormat;
import Socket.ClientSocketFun;
import Socket.SocketConnectedInfo;
import Message.messageTextType;
import static java.lang.Thread.sleep;


public class Main {
    public static void main(String[] args) throws InterruptedException {
        int i = 0;
        //连接套接字
        SocketConnectedInfo socketConnectedInfo = new SocketConnectedInfo("127.0.0.1", 1234);

        String serverIsConnected;
        if(socketConnectedInfo.getSocket() == null){
            serverIsConnected = new String("Connecte server Error");
        }else{
            serverIsConnected = new String("Connecte server Success");
        }

        //客户端的Socket 函数驱动
        ClientSocketFun clientSocketFun = new ClientSocketFun(socketConnectedInfo.getSocket());

       //GUI 界面
        LoginPage loginPage = new LoginPage(socketConnectedInfo.getSocket(), serverIsConnected); //创建登录界面
//        HomePage homePage = new HomePage("admin", socketConnectedInfo.getSocket()); //创建主页界面
//        RegisterPage registerPage = new RegisterPage(socketConnectedInfo.getSocket()); //创建注册界面

        while (true) {
            sleep(500);
//            System.out.println(i++);
//            //发送一则带格式的消息
//            clientSocketFun.sendAMsg(messageTextType.DEFAULT, "" + (i++));

        }


    }
}
