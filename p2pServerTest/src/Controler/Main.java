package Controler;

import Socket.ServerSocketFun;
import Socket.serverSocketConnectedInfo;
//import Message.messageTextType;
import static java.lang.Thread.sleep;


public class Main {
    public static void main(String[] args) throws InterruptedException {

        //连接套接字
        serverSocketConnectedInfo serverSocketConnectedInfo = new serverSocketConnectedInfo(1234);

        //服务器端的Socket 函数驱动
        ServerSocketFun serverSocketFun = new ServerSocketFun(serverSocketConnectedInfo.getSocket());
        serverSocketFun.start();

        int i = 0;


        while(true){
            sleep(500);
//            System.out.println(i++);
//            server_test.sendAMsg("i = " + i);
        }
    }

}
