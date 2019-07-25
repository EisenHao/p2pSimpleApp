package Message;

import java.lang.String.*;

//Peer 与  Server 通信的消息类

public class MsgFormat {

    private boolean isWaitingForRead = false; //是否等待被读 false：已读   true：未读消息
    //消息来自 --> 用户名
    private String fromWho;  //socket.getInetAddress()
    //消息类型
    private int msgText_Type;
    //格式化后的信息【字符串】
    private String msgText_Buf;


    //构造函数1
    public MsgFormat(String fromWho, int msgText_Type, String msgText_Buf) {
        this.fromWho = fromWho;
        this.msgText_Type = msgText_Type;
        this.msgText_Buf = msgText_Buf;
    }

    //构造函数2  【解析格式】将传入的字符串转为类
    public MsgFormat(String Text_Buf) {
        //按分割符号分类
        String[] strArray = Text_Buf.split("><", 3);//参数"3"，最多分成3个子串
        this.fromWho = strArray[0];
        this.msgText_Type = Integer.valueOf(strArray[1]);
        this.msgText_Buf = strArray[2];
    }

    //构造函数3 空
    public MsgFormat() {
        this.fromWho = "";
        this.msgText_Type = messageTextType.DEFAULT;
        this.msgText_Buf = "";
    }

    //【压缩格式】将此类转化为String
    public String MsgToString(){
        return "" + fromWho + "><" + msgText_Type + "><" + msgText_Buf;
    }

    //【解析格式】将传入的字符串转为类
    public void setValueByString(String Text_Buf){
        //按分割符号分类
        String[] strArray = Text_Buf.split("><", 3);//参数"3"，最多分成3个子串
        this.fromWho = strArray[0];
        this.msgText_Type = Integer.valueOf(strArray[1]);
        this.msgText_Buf = strArray[2];
    }

    public String getFromWho() {
        return fromWho;
    }

    public void setFromWho(String fromWho) {
        this.fromWho = fromWho;
    }

    public int getMsgText_Type() {
        return msgText_Type;
    }

    public void setMsgText_Type(int msgText_Type) {
        this.msgText_Type = msgText_Type;
    }

    public String getMsgText_Buf() {
        return msgText_Buf;
    }

    public void setMsgText_Buf(String msgText_Buf) {
        this.msgText_Buf = msgText_Buf;
    }

    public boolean isWaitingForRead() {
        return isWaitingForRead;
    }

    public void setWaitingForRead(boolean waitingForRead) {
        isWaitingForRead = waitingForRead;
    }
}
