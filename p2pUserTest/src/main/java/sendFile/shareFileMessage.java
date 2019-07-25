package sendFile;
import Peer.User;

import java.io.*;
import java.security.*; //文件的MD校验值
import java.util.*;


//定义一个 shareFileMessage类，表示分享的文件信息，并实现Serializable接口

public class shareFileMessage extends Message implements Serializable {
    private String fileName; //文件名
    private int fileLength; //文件大小
    private byte[] fileContent; //文件内容（字节形式）
    private byte[] checkSum; //文件校验码
    boolean isSenderOrReceiver; //是发送者还是接收者

    //构造函数
    public shareFileMessage(User fromWho, File iFile, boolean isSenderOrReceiver ) throws Exception {
        super(fromWho, iFile.getName()); //调用父类构造函数

        this.fileName = iFile.getName(); //获取文件名
        this.fileLength = (int)iFile.length(); //获取文件大小
        this.isSenderOrReceiver = isSenderOrReceiver;//是发送者还是接收者

        this.fileContent = new byte[fileLength]; //定义文件大小的byte数组
        FileInputStream fIn = new FileInputStream(iFile); //文件读写通道
        fIn.read(fileContent); //将文件内容读取到fileContent字节数组内
        fIn.close(); //关闭文件读写通道

        this.checkSum = CalcDigest(); //文件校验吗
    }

    //获取文件的MD5校验值
    private byte[] CalcDigest() throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return md.digest(fileContent); //计算并返回文件内容的MD5校验值
    }

    //检查文件的MD5校验值完整性
    public boolean checkMD5Digest() throws Exception {
        return Arrays.equals(checkSum, CalcDigest());
    }

    //重写equals()方法， 比较两个文件
    public boolean equals (Object obj){
        //若文件不等 返回false
        if(!(obj instanceof shareFileMessage))
            return false;

        shareFileMessage tMsg = (shareFileMessage)obj;
        //分别从由谁发送 和 文件名同时比较，且两者均相同，返回true

        if(this.getFromWho().equals(tMsg.getFromWho()) && this.fileName.equals(tMsg.fileName)){
            return true;
        }
        return false;
    }

    //将文件存储在本地
    public void saveByteToLocalFile(String iPath) throws Exception {
        FileOutputStream fOut = new FileOutputStream(iPath); //新建一个文件输出流
        fOut.write(fileContent);//将字节数组fileContent内的文件内容 写在iPath路径文件中
        fOut.close();
    }

    //重写toString()方法
    public String toString() {
        return "[" + this.getFromWho() + "]" + this.fileName + "(" + this.fileLength + "Bytes)";
    }


    //获取文件内容，将文件内容数据存储在字节数组中
    public byte[] getFileBytes() {
        return fileContent;
    }

    //获取文件长度（大小）
    public int getFileLength() {
        return fileLength;
    }

    //获取文件内容（假设文件内容为char字符）
    public String getContent(){
        char []chArr = new char[fileContent.length]; //设置一个存放文件内容的缓存区

        for (int i=0; i<fileContent.length; i++) {
            chArr[i] = (char)fileContent[i];
        }
        return new String(chArr);
    }

}
