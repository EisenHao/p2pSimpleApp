package Peer;

// 客户端的 分享的文件信息类

public class SharedFileRecord {
    private String sharer; //分享者
    private String hostIPAndPost;//分享者电脑的主机IP及端口
    private String fileName; //被分享的文件名
    private int fileLength; //被分享的文件大小
    private String filePSW; //被分享的文件在主机中的位置

    //构造函数1
    public SharedFileRecord(String sharer, String hostIPAndPost, String fileName, int fileLength, String filePSW) {
        this.sharer = sharer;
        this.hostIPAndPost = hostIPAndPost;
        this.fileName = fileName;
        this.fileLength = fileLength;
        this.filePSW = filePSW;
    }

    //构造函数2：空
    public SharedFileRecord() {
        this.sharer = "";
        this.hostIPAndPost = "";
        this.fileName = "";
        this.fileLength = 0;
        this.filePSW = "";
    }

    //构造函数3  【解析格式】将传入的字符串转为类
    public SharedFileRecord(String Text_Buf) {
        //按分割符号分类
        String[] strArray = Text_Buf.split(";", 5);//参数"5"，最多分成5个子串
        this.sharer = strArray[0];
        this.hostIPAndPost = strArray[1];
        this.fileName = strArray[2];
        this.fileLength = Integer.valueOf(strArray[3]);
        this.filePSW = strArray[4];
    }

    public String getSharer() {
        return sharer;
    }

    public void setSharer(String sharer) {
        this.sharer = sharer;
    }

    public String getHostIPAndPost() {
        return hostIPAndPost;
    }

    public void setHostIPAndPost(String hostIPAndPost) {
        this.hostIPAndPost = hostIPAndPost;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFileLength() {
        return fileLength;
    }

    public void setFileLength(int fileLength) {
        this.fileLength = fileLength;
    }

    public String getFilePSW() {
        return filePSW;
    }

    public void setFilePSW(String filePSW) {
        this.filePSW = filePSW;
    }


    //【压缩格式】将此类转化为String
    public String TranslateToString(){
        return "" + sharer + ";" + hostIPAndPost + ";" + fileName + ";" + fileLength + ";" + filePSW;
    }


    //【解析格式】将传入的字符串转为类
    public void setValueByString(String Text_Buf){
        //按分割符号分类
        String[] strArray = Text_Buf.split(";", 5);//参数"5"，最多分成5个子串
        this.sharer = strArray[0];
        this.hostIPAndPost = strArray[1];
        this.fileName = strArray[2];
        this.fileLength = Integer.valueOf(strArray[3]);
        this.filePSW = strArray[4];
    }


    @Override
    public String toString() {
        return "fileName:" + fileName + ", fileLength：" + fileLength + " B, from:[" + sharer
                + "]:" + hostIPAndPost + ", PSW:" + filePSW
                + "]";
    }

}
