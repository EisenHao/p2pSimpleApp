package Socket;

// 客户端 Socket代码

import Message.MsgFormat;
import Message.messageTextType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import static java.lang.Thread.sleep;


//客户端的Socket 函数驱动
public class ClientSocketFun{

    //定义一个Socket对象， 构造函数时传参进入
    private Socket socket;

    //构造函数
    public ClientSocketFun(Socket inputConnectedSocket) {
        //已连接的套接字直接赋值
        socket = inputConnectedSocket;
    }


    /**
    * 接收返回消息
    * 阻塞以等待接收消息
    */
    public MsgFormat receiveMsg(){
        //读缓存类
        MsgFormat clientReceiveMsg = new MsgFormat();
        try {
            //获取接收流
            InputStream inSocketStream = socket.getInputStream();
            int len = 0;
            byte[] buf = new byte[1024];
            //阻塞等待消息，一直在线程内
            while (true) {
                len = inSocketStream.read(buf);

                //若收到值
                if( len != -1) {
                    clientReceiveMsg.setValueByString(new String(buf,0, len));
                    clientReceiveMsg.setWaitingForRead(true);

                    System.out.println("===============================================================");
                    System.out.println("收到的原始数据：" + new String(buf,0, len));
                    System.out.println("解压格式");
                    //获取读到的信息 【解析格式】
                    System.out.println("fromwho:" + clientReceiveMsg.getFromWho());
                    System.out.println("MsgText_Type:" + clientReceiveMsg.getMsgText_Type());
                    System.out.println("MsgText_Buf:" + clientReceiveMsg.getMsgText_Buf());
                    System.out.println("===============================================================");

//                    //关闭接收流【不可关闭，否则报错】
//                    try {
//                        inSocketStream.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                    break; // 退出while
                }
                sleep(50);//延时等待
            }
        } catch (IOException e) {
            //进入此处，说明接收出错
            clientReceiveMsg = new MsgFormat("", messageTextType.RECEIVE_ERROR,"");
            e.printStackTrace();
        } catch (InterruptedException e) {
            //进入此处，说明接收出错
            clientReceiveMsg = new MsgFormat("", messageTextType.RECEIVE_ERROR,"");
            e.printStackTrace();
        }
        return clientReceiveMsg;
    }



//    Socket s=new Socket(str_ip,10003, ip, 10001);//连接目的


    /**
     * 发送 一个 消息 【格式化包装】，然后监听接受 response 消息
     * 参数： 数据类型 、发送数据
     * 返回值 MsgFormat
     */
    public MsgFormat sendAMsg(int msgText_Type, String sendMsgText) {
        //定义接收服务器端的Response消息
        MsgFormat responseMsg = new MsgFormat();
        if(socket != null) {
            //尝试发送数据操作
            try {
                OutputStream outSocketStream = socket.getOutputStream();
                //格式化一个发送数据包
                MsgFormat sendFormat = new MsgFormat(""+ socket.getLocalSocketAddress(), msgText_Type, sendMsgText);
                //将发送类转成字符串，再转成Bytes发送
                outSocketStream.write(sendFormat.MsgToString().getBytes());
                outSocketStream.flush();//清空缓存区的内容

                responseMsg = receiveMsg();//接收服务器端的Response消息

//                //关闭发送流【不可关闭，否则报错】
//                outSocketStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseMsg;//返回收到的 response 消息
        }
        return new MsgFormat("", messageTextType.RECEIVE_ERROR, "");//发送失败
    }

}
