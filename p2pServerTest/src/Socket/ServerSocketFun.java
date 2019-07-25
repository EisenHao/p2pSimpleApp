package Socket;
import Message.messageTextType;

// 服务器端Socket代码

import Message.MsgFormat;
import MySQL.jdbcMysqlFun;
import Peer.SharedFileRecord;
import Peer.User;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;


//服务器端的Socket 函数驱动
public class ServerSocketFun extends Thread {

    //私有套接字， 构造函数时传参进入
    private Socket socket;
    //读缓存类
    private MsgFormat serverReadMsg; //serverReadMsg
    //发送流
    private OutputStream outSocketStream = null;
    //接收流
    private InputStream inSocketStream = null;


    //析构函数
    public void finalize(){
        //关闭发送流
        try {
            outSocketStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //关闭接收流
        try {
            inSocketStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //构造函数
    public ServerSocketFun(Socket inputConnectedSocket) {
        //初始化读缓存类
        serverReadMsg = new MsgFormat();
        //已连接的套接字直接赋值
        socket = inputConnectedSocket;

        //获取输出流
        try {
            outSocketStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取接收流
        try {
            inSocketStream = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //接收线程 （一直在此线程内 监听获取消息）
    @Override
    public void run() {
        super.run();
        try {
            int len = 0;
            byte[] buf = new byte[1024];
            //一直在线程内
            while ((len = inSocketStream.read(buf)) != -1){

                serverReadMsg.setValueByString(new String(buf,0, len));
                serverReadMsg.setWaitingForRead(true);//设置

                System.out.println("===============================================================");
                System.out.println("收到的原始数据：" + new String(buf,0, len));
                System.out.println("解压格式");
                //获取读到的信息 【解析格式】
                System.out.println("fromwho:" + serverReadMsg.getFromWho());
                System.out.println("MsgText_Type:" + serverReadMsg.getMsgText_Type());
                System.out.println("MsgText_Buf:" + serverReadMsg.getMsgText_Buf());
                System.out.println("===============================================================");


                /**
                 *
                 *
                 *
                 *   逻辑处理
                 *
                 * :服务器端根据收到的请求类型作出相应的操作及反馈
                 *
                 *
                 * */

                //1. 收到的标志为"DEFAULT"
                if(serverReadMsg.getMsgText_Type() == messageTextType.DEFAULT){
                    //显示收到该字符串
                    System.out.println("返回收到该字符串:" + sendAMsg(messageTextType.DEFAULT, serverReadMsg.getMsgText_Buf()));
                }

                //2. 收到的标志为"LOGIN"
                else if(serverReadMsg.getMsgText_Type() == messageTextType.LOGIN){
                    //分解信息得到用户和密码
                    String[] strArray = serverReadMsg.getMsgText_Buf().split(";", 2);//参数"2"，最多分成2个子串
                    String username = strArray[0];
                    String password = strArray[1];

                    //查询数据库中 UserName 用户、密码是否正确
                    jdbcMysqlFun db = new jdbcMysqlFun();
                    int searchResult = db.loginCheck(new User(username, password));
                    switch (searchResult) {
                        //若不存在
                        case messageTextType.LOGIN_DONT_EXISTED:
                            //回复客户端，查找用户不存在
                            System.out.println(username + " don't existed, "
                                    + sendAMsg(messageTextType.LOGIN_DONT_EXISTED, "DONT_EXISTED"));
                            break;
                        //若帐号密码正确
                        case messageTextType.LOGIN_CORRECT:
                            System.out.println(username + "-" + password + " CORRECT, "
                                    + sendAMsg(messageTextType.LOGIN_CORRECT, "CORRECT"));
                            break;
                        //若帐号密码错误
                        case messageTextType.LOGIN_INCORRECT:
                            System.out.println(username + "-" + password + " INCORRECT, "
                                    + sendAMsg(messageTextType.LOGIN_INCORRECT, "INCORRECT"));
                            break;
                        default:
                            System.out.println(username + "-" + password + " SEARCH ERROR, "
                                    + sendAMsg(messageTextType.LOGIN_SEARCH_ERROR, "SEARCH_ERROR"));
                            break;
                    }
                }

                //3. 收到的标志为"REGISTER"
                else if(serverReadMsg.getMsgText_Type() == messageTextType.REGISTER){
                    //分解信息得到用户和密码
                    String[] strArray = serverReadMsg.getMsgText_Buf().split(";", 2);//参数"2"，最多分成2个子串
                    String username = strArray[0];
                    String password = strArray[1];

                    //尝试在数据库中注册
                    jdbcMysqlFun db = new jdbcMysqlFun();
                    int registerResult = db.addUser(new User(username, password));
                    switch (registerResult) {
                        //若用户名已存在
                        case messageTextType.REGISTER_USERNAME_EXISTED:
                            //回复客户端，查找用户不存在
                            System.out.println(username + " REGISTER_USERNAME_EXISTED, "
                                    + sendAMsg(messageTextType.REGISTER_USERNAME_EXISTED, "USERNAME:" + username + " IS EXISTED"));
                            break;
                        //若注册成功
                        case messageTextType.REGISTER_SUCCESS:
                            System.out.println(username + "-" + password + " CORRECT, "
                                    + sendAMsg(messageTextType.REGISTER_SUCCESS, "USERNAME:" + username + "(psw:" + password + ") REGISTER CORRECT"));
                            break;
                        //若注册失败
                        default:
                            System.out.println(username + "-" + password + " REGISTER ERROR, "
                                    + sendAMsg(messageTextType.REGISTER_ERROR, "USERNAME:" + username +" REGISTER_ERROR"));
                            break;
                    }
                }

                //4.收到消息为获取所有已分享的文件记录列表
                else if(serverReadMsg.getMsgText_Type() == messageTextType.GETALLSHAREDFILES){
                    System.out.println(serverReadMsg.getMsgText_Buf());
                    jdbcMysqlFun db = new jdbcMysqlFun();
                    //遍历 查询数据表 shared_files中 所有记录
                    System.out.println("\n\n1.遍历 查询数据表 shared_files中 所有记录");
                    List<SharedFileRecord> allSharedFileslist = null;
                    try {
                        allSharedFileslist = db.findAllSharedFiles();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    //若列表为空
                    if (allSharedFileslist.isEmpty()){
                        System.out.println("is Empty");
                        //回复客户端，查找文件分享列表为空
                        System.out.println("SHARED_FILE_LIST_IS_EMPTY"
                                + sendAMsg(messageTextType.SHARED_LIST_EMPTY, "SHARED_FILE_LIST_IS_EMPTY"));
                    }
                    //若列表非空
                    else {
                        String str = "" + allSharedFileslist.size(); //待发送给客户端信息首部为列表元素个数
                        StringBuilder sendStr = new StringBuilder(str);//构造一个StringBuilder对象

                        //依次添加 所有 文件记录类 的String形式 到待发送sendStr
                        for (int i=0; i < allSharedFileslist.size(); i++){
                            sendStr.append("#" + allSharedFileslist.get(i).TranslateToString());
                        }

                        System.out.println("SEND_SHARED_FILE_LIST:"
                                + sendAMsg(messageTextType.SHARED_LIST, sendStr.toString()));

                        System.out.println("Show SendStr:\n" + sendStr.toString());
                    }
                }

                //5.收到消息为获取 我的已分享的文件记录列表
                else if(serverReadMsg.getMsgText_Type() == messageTextType.GETMYSHAREDFILES){
                    System.out.println(serverReadMsg.getMsgText_Buf());
                    jdbcMysqlFun db = new jdbcMysqlFun();

                    //2.查询数据表 shared_files 中 admin 用户的分享记录
                    System.out.println("\n\n2.查询数据表 shared_files 中 admin 用户的分享记录");
                    List<SharedFileRecord> searchSharerList = null;
                    try {
                        searchSharerList = db.findSharedFilesBySharerName(serverReadMsg.getMsgText_Buf());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    //若列表为空
                    if (searchSharerList.isEmpty()){
                        System.out.println("is Empty");
                        //回复客户端，查找文件分享列表为空
                        System.out.println("SHARED_FILE_LIST_IS_EMPTY"
                                + sendAMsg(messageTextType.SHARED_LIST_EMPTY, "SHARED_FILE_LIST_IS_EMPTY"));

                    }
                    //若列表非空
                    else {
                        String str = "" + searchSharerList.size(); //待发送给客户端信息首部为列表元素个数
                        StringBuilder sendStr = new StringBuilder(str);//构造一个StringBuilder对象

                        //依次添加 所有 文件记录类 的String形式 到待发送sendStr
                        for (int i=0; i < searchSharerList.size(); i++){
                            sendStr.append("#" + searchSharerList.get(i).TranslateToString());
                        }
                        System.out.println("SEND_SHARED_FILE_LIST:" + sendAMsg(messageTextType.SHARED_LIST, sendStr.toString()));
                        System.out.println("Show SendStr:\n" + sendStr.toString());
                    }
                }

                //6.收到消息为删除 我的一个分享的文件记录 请求
                else if(serverReadMsg.getMsgText_Type() == messageTextType.UNSHAREDAFILE) {
                    //分解信息得到用户和待删除记录文件名
                    String[] strArray = serverReadMsg.getMsgText_Buf().split(";", 2);//参数"2"，最多分成2个子串
                    String sharer = strArray[0];
                    String cancelFileName = strArray[1];

                    System.out.println(serverReadMsg.getMsgText_Buf());
                    jdbcMysqlFun db = new jdbcMysqlFun();

                    //删除成功与否的结果标志：deleteResult
                    int deleteResult = db.deleteShaerdFile(sharer, cancelFileName);
                    switch (deleteResult) {
                        //1.若未分享过该文件，返回未分享过
                        case messageTextType.UNSHARED_DONT_EXISTED:
                            //回复客户端，未分享过该文件，返回未分享过
                            System.out.println("" + sharer + "hasn't shared " + cancelFileName
                                    + sendAMsg(messageTextType.UNSHARED_DONT_EXISTED, "" + sharer + "hasn't shared " + cancelFileName));
                            break;
                        //2.若成功取消分享
                        case messageTextType.UNSHARED_SUCCESS:
                            System.out.println("delete " + sharer + "'s shared file(" + cancelFileName + ") Success"
                                    + sendAMsg(messageTextType.UNSHARED_SUCCESS, "delete " + sharer + "'s shared file(" + cancelFileName + ") Success"));
                            break;
                        //3.取消分享失败
                        default:
                            System.out.println("delete " + sharer + "'s shared file(" + cancelFileName + ") Error!!! "
                                    + sendAMsg(messageTextType.UNSHARED_ERROR, "delete " + sharer + "'s shared file(" + cancelFileName + ") Error!!!"));
                            break;
                    }
                }

                //7.收到添加一条分享文件记录的请求
                else if(serverReadMsg.getMsgText_Type() == messageTextType.SHAREDAFILE) {
                    System.out.println(serverReadMsg.getMsgText_Buf());
                    //将收到的【压缩成String类型】的 SharedFileRecord 类还原成类
                    SharedFileRecord sharedFileRecord = new SharedFileRecord(serverReadMsg.getMsgText_Buf());

                    //调用数据库
                    jdbcMysqlFun db = new jdbcMysqlFun();
                    int addResult = db.addShaerdFile(sharedFileRecord);
                    switch (addResult) {
                        //1.若同分享者同文件已分享,已存在
                        case messageTextType.UNSHARED_HAS_EXISTED:
                            //回复客户端，同分享者同文件已分享,已存在
                            System.out.println("" + sharedFileRecord.getSharer() + "has shared " + sharedFileRecord.getFileName()
                                    + sendAMsg(messageTextType.UNSHARED_HAS_EXISTED, "" + sharedFileRecord.getSharer() + "has shared " + sharedFileRecord.getFileName()));
                            break;
                        //2.若成功添加分享记录
                        case messageTextType.SHARED_SUCCESS:
                            System.out.println("add " + sharedFileRecord.getSharer() + "'s shared file(" + sharedFileRecord.getFileName() + ") Success"
                                    + sendAMsg(messageTextType.SHARED_SUCCESS, "add " + sharedFileRecord.getSharer() + "'s shared file(" + sharedFileRecord.getFileName() + ") Success"));
                            break;
                        //3.添加分享记录失败
                        default:
                            System.out.println("add " + sharedFileRecord.getSharer() + "'s shared file(" + sharedFileRecord.getFileName() + ") Error!!!"
                                    + sendAMsg(messageTextType.SHARED_ERROR, "add " + sharedFileRecord.getSharer() + "'s shared file(" + sharedFileRecord.getFileName() + ") Error!!!"));
                            break;
                    }
                }

                //8.获取一个分享文件的信息记录的请求
                else if(serverReadMsg.getMsgText_Type() == messageTextType.GETASHAREDFILE) {
                    //分解信息得到用户和待删除记录文件名
                    String[] strArray = serverReadMsg.getMsgText_Buf().split(";", 2);//参数"2"，最多分成2个子串
                    String sharer = strArray[0];
                    String fileName = strArray[1];

                    System.out.println(serverReadMsg.getMsgText_Buf());
                    jdbcMysqlFun db = new jdbcMysqlFun();


                    //查询 分享者和分享文件名 的记录是否存在
                    //若不存在
                    if(!db.isSharerAndFileNameBothExisted(sharer, fileName)){
                        //回复客户端，该分享者和文件的记录 不存在
                        System.out.println("" + sharer + "hasn't shared " + fileName
                                + sendAMsg(messageTextType.UNSHARED_DONT_EXISTED, "" + sharer + "hasn't shared " + fileName));
                    }
                    //若存在
                    else {
                        //从数据库中获取该记录
                        SharedFileRecord searchedRecord = db.getaSharedFileRecordBySharerAndFileName(sharer, fileName);
                        //回复客户端，该分享者和文件的记录
                        System.out.println("search" + sharer + " " + fileName + "Success"
                                + sendAMsg(messageTextType.SHARED_A_RECORD, searchedRecord.TranslateToString()));
                    }
                }













                //while内 延时等待 10 ms
                try {
                    this.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //发送 一个 消息 【格式化包装】
    //参数： 数据类型 、发送数据
    public boolean sendAMsg(int msgText_Type, String sendMsgText) {
        if(socket != null) {
            //写操作
            try {
                //格式化一个发送数据包
                MsgFormat sendFormat = new MsgFormat(""+ socket.getLocalSocketAddress(), msgText_Type, sendMsgText);
                //将发送类转成字符串，再转成Bytes发送
                outSocketStream.write(sendFormat.MsgToString().getBytes());
                outSocketStream.flush();//清空缓存区的内容
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;//发送成功
        }
        return false;//发送失败
    }

    //检测是否有未读信息
    public boolean isThereANewMsg(){
        return serverReadMsg.isWaitingForRead();
    }

    //获取未读消息
    public MsgFormat getServerReadMsg() {
        serverReadMsg.setWaitingForRead(false);//已读
        return serverReadMsg;
    }
}