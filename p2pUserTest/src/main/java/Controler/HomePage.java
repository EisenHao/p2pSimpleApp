package Controler;

import Message.MsgFormat;
import Message.messageTextType;
import Peer.SharedFileRecord;
import Socket.ClientSocketFun;
import javax.swing.JFileChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.Socket;

public class HomePage {
    //GUI相关
    static JFrame homeFrame;
    public JPanel HomeJPanel;
    private JList sharedFilesList;
    private JList myShareList;
    private JTextField sharerField;
    private JTextField fileNameField;
    private JButton downloadButton;
    private JTextField uploadFileField;
    private JButton uploadButton;
    private JTextField cancelField;
    private JButton cancelShareButton;
    private JButton logoutButton;
    private JTextArea textArea1;
    private JButton refreshListButton;
    private JButton refreshMyListButton;


    //定义一个 ClientSocketFun 对象
    private ClientSocketFun clientSocketFun;


    // GUI 构造函数
    public HomePage(String userName, Socket inputConnectedSocket) {
        //客户端的Socket 函数驱动
        clientSocketFun = new ClientSocketFun(inputConnectedSocket);

        homeFrame = new JFrame(userName + "'s HomePage");
        homeFrame.setContentPane(HomeJPanel);
        homeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        homeFrame.pack();
        homeFrame.setVisible(true);

        textArea1.setText(userName);
        textArea1.setEditable(false);//不可编辑
        Color backgroundColor = new Color(180,180,180);
        textArea1.setCaretColor(backgroundColor);//光标颜色
        textArea1.setSelectionColor(backgroundColor); //呈现选中部分的背景颜色
        textArea1.setSelectedTextColor(backgroundColor);//选中部分文本的颜色
        textArea1.setDisabledTextColor(backgroundColor);//不可用时文本的颜色


        sharedFilesList.setLayoutOrientation(JList.VERTICAL);//设置显示列（1列显示）
        sharedFilesList.setVisibleRowCount(20);//设置最多显示多少行

        myShareList.setLayoutOrientation(JList.VERTICAL);//设置显示列（1列显示）
        myShareList.setVisibleRowCount(10);//设置最多显示多少行



        /**
         * 点击：登出按钮
         * 动作：关闭Home页面，回到登录界面
         * */
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int n = JOptionPane.showConfirmDialog(null, "Exit?", "Exit?",JOptionPane.YES_NO_OPTION);//返回的是按钮的index  i=0或者1
                System.out.println(n);//n==0,Yes
                if(n==0){
                    homeFrame.dispose();
                }
            }
        });


        /**点击：所有分享的文件的 刷新 按钮
         * 发送申请获取所有Peer分享的文件记录信息
         * 等候服务器发送数据，并将数据刷新在JList组件上，并显示
         */
        refreshListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Socket链接服务器 查询 数据表 share_files 中的 所有文件分享记录
                //发送一则请求的消息到服务器
                MsgFormat receiveMsg = clientSocketFun.sendAMsg(messageTextType.GETALLSHAREDFILES,"GET ALL SHARED FILES");

                //定义待显示 中间 变量
                DefaultListModel m = new DefaultListModel(); //定义一个用于显示sharedFilesList框内内容的变量

                //判断服务端返回的消息类型
                if (messageTextType.SHARED_LIST == receiveMsg.getMsgText_Type()) {

                    //按分割符号分类
                    String[] strArray = receiveMsg.getMsgText_Buf().split("#");
                    //解析列表元素个数
                    int numOfSharedFiles = Integer.valueOf(strArray[0]);

                    //解压缩格式
                    SharedFileRecord sharedFileRecord = new SharedFileRecord(); //解压缩格式用到中间变量
                    //将每个文件分享信息添加到一个 容器中
                    for (int i=1; i<numOfSharedFiles+1; i++){
                        sharedFileRecord.setValueByString(strArray[i]);//将字符串转成类【解压缩格式】
                        //显示输出
                        System.out.println(sharedFileRecord.toString());
                        //添加第 i 个到容器中
                        m.addElement(sharedFileRecord.toString());
                    }
                } else {
                    //定义待显示 中间 变量
                    m.addElement("Empty!");
                    System.out.println("is Empty!");
                }

                //将容器内的值传给JList显示
                sharedFilesList.setModel(m);
            }
        });


        /**点击：我的分享的文件的 刷新 按钮
         * 发送申请获取“我”分享的文件记录信息
         * 等候服务器发送数据，并将数据刷新在JList组件上，并显示
         * */
        refreshMyListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Socket链接服务器 查询 数据表 share_files 中的 所有文件分享记录
                //发送一则请求的消息到服务器
                MsgFormat receiveMsg = clientSocketFun.sendAMsg(messageTextType.GETMYSHAREDFILES, userName);

                //定义待显示 中间 变量
                DefaultListModel m = new DefaultListModel(); //定义一个用于显示sharedFilesList框内内容的变量

                //判断服务端返回的消息类型
                if (messageTextType.SHARED_LIST == receiveMsg.getMsgText_Type()) {

                    //按分割符号分类
                    String[] strArray = receiveMsg.getMsgText_Buf().split("#");
                    //解析列表元素个数
                    int numOfSharedFiles = Integer.valueOf(strArray[0]);

                    //解压缩格式
                    SharedFileRecord sharedFileRecord = new SharedFileRecord(); //解压缩格式用到中间变量
                    //将每个文件分享信息添加到一个 容器中
                    for (int i=1; i<numOfSharedFiles+1; i++){
                        sharedFileRecord.setValueByString(strArray[i]);//将字符串转成类【解压缩格式】
                        //显示输出
                        System.out.println(sharedFileRecord.toString());
                        //添加第 i 个到容器中
                        m.addElement(sharedFileRecord.toString());
                    }
                } else {
                    //定义待显示 中间 变量
                    m.addElement("Empty!");
                    System.out.println("Empty!");
                }

                //将容器内的值传给JList显示
                myShareList.setModel(m);
            }
        });


        /**点击：取消分享按钮
         * 发送用户名，取消文件名信息到服务器，并获取服务器处理后返回的信息
         * */
        cancelShareButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cancelFileName = cancelField.getText();
                //判断是否为空
                //若不为空
                if(!"".equals(cancelFileName)){
                    //发送一则请求的消息到服务器，请求删除对应用户的分享记录
                    MsgFormat receiveMsg = clientSocketFun.sendAMsg(messageTextType.UNSHAREDAFILE,  "" + userName + ";" + cancelFileName);
                    //判断客户端返回的消息类型
                    switch (receiveMsg.getMsgText_Type()){
                        //1.若未分享过该文件，返回未分享过
                        case messageTextType.UNSHARED_DONT_EXISTED:
                            //未分享过该文件，返回未分享过
                            int o = JOptionPane.showConfirmDialog(null, receiveMsg.getMsgText_Buf(), "Cancel Error",JOptionPane.DEFAULT_OPTION);//返回的是按钮的index  i=0或者1
                            break;
                        //2.若成功取消分享
                        case messageTextType.UNSHARED_SUCCESS:
                            int p = JOptionPane.showConfirmDialog(null, receiveMsg.getMsgText_Buf(), "Cancel Success",JOptionPane.DEFAULT_OPTION);//返回的是按钮的index  i=0或者1
                            break;
                        //3.取消分享失败
                        default:
                            int q = JOptionPane.showConfirmDialog(null, receiveMsg.getMsgText_Buf(), "Cancel Error",JOptionPane.DEFAULT_OPTION);//返回的是按钮的index  i=0或者1
                            break;
                    }
                }
                //若为空
                else {
                    int n = JOptionPane.showConfirmDialog(null, "Input Error!!!", "Cancel Error",JOptionPane.DEFAULT_OPTION);//返回的是按钮的index  i=0或者1
                }
            }
        });


        /**
         * 点击：下载文件按钮
         * 联机对应的IP/Port, 下载文件
         * */
        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sharer = sharerField.getText();
                String fileName = fileNameField.getText();
                // 否输入非为空
                if(!"".equals(sharer) && !"".equals(fileName)) {
                    //发送一则请求的消息到服务器，获取一个分享文件的信息
                    MsgFormat receiveMsg = clientSocketFun.sendAMsg(messageTextType.GETASHAREDFILE, sharer + ";" + fileName);
                    //判断服务端返回的消息类型
                    //若返回记录不存在
                    if (messageTextType.UNSHARED_DONT_EXISTED == receiveMsg.getMsgText_Type()){
                        int o = JOptionPane.showConfirmDialog(null, receiveMsg.getMsgText_Buf(), "Download Error",JOptionPane.DEFAULT_OPTION);//返回的是按钮的index  i=0或者1
                    }
                    //若成功返回一个文件记录消息
                    else if(messageTextType.SHARED_A_RECORD == receiveMsg.getMsgText_Type()){
                        //解压缩格式
                        SharedFileRecord sharedFileRecord = new SharedFileRecord(receiveMsg.getMsgText_Buf()); //解压缩格式用到中间变量
                        System.out.println("接收到的信息：\n" + sharedFileRecord.toString());

                        //选择一个下载文件夹路径
                        JFileChooser jFileChooser = new JFileChooser();
                        jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//仅选择路径
                        jFileChooser.showDialog(new JLabel(), "Choose a directories to download");

                        File directorie = jFileChooser.getSelectedFile();

                        int o = JOptionPane.showConfirmDialog(null,
                                "Try to download " + sharedFileRecord.getFileName() + " to " + directorie.getAbsolutePath(),
                                "Connect... ["+ sharedFileRecord.getSharer() + "]" + sharedFileRecord.getHostIPAndPost() ,JOptionPane.DEFAULT_OPTION);//返回的是按钮的index  i=0或者1

                        System.out.println(directorie.getAbsolutePath());

                    }
                    //未知错误
                    else {
                        int o = JOptionPane.showConfirmDialog(null, "UnKnown Error!!!", "Download Error",JOptionPane.DEFAULT_OPTION);//返回的是按钮的index  i=0或者1
                    }
                }
                // 否输入非为空
                else {
                    int n = JOptionPane.showConfirmDialog(null, " input Error !!! ",
                            "Input Error",JOptionPane.DEFAULT_OPTION);//返回的是按钮的index  i=0或者1
                }
            }
        });


        /**
         * 点击：分享文件按钮
         * 记录：用户名、主机IP及端口、文件名、文件位置，并将以上信息发送到服务端
         * */
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);//仅选择文件
                jFileChooser.showDialog(new JLabel(), "Choose a file to share");

                File file = jFileChooser.getSelectedFile();
                System.out.println(file.getName() + "'s PSW = " + file.getAbsolutePath());

                uploadFileField.setText("" + file.getAbsolutePath());

                //创建一个分享的文件信息类,以记录文件信息
                SharedFileRecord sharedFileRecord = new SharedFileRecord(userName,
                        "" + inputConnectedSocket.getLocalSocketAddress(),
                        file.getName(),
                        (int)file.length(),
                        file.getAbsolutePath()
                        );

                //发送一则请求的消息到服务器，请求 添加一条 对应用户的分享记录
                MsgFormat receiveMsg = clientSocketFun.sendAMsg(messageTextType.SHAREDAFILE,  sharedFileRecord.TranslateToString());
                //判断客户端返回的消息类型
                switch (receiveMsg.getMsgText_Type()){
                    //1.若同分享者同文件已分享,已存在
                    case messageTextType.UNSHARED_HAS_EXISTED:
                        //同分享者同文件已分享,已存在
                        int o = JOptionPane.showConfirmDialog(null, receiveMsg.getMsgText_Buf(), "Share Error",JOptionPane.DEFAULT_OPTION);//返回的是按钮的index  i=0或者1
                        break;
                    //2.若成功添加分享记录
                    case messageTextType.SHARED_SUCCESS:
                        int p = JOptionPane.showConfirmDialog(null, receiveMsg.getMsgText_Buf(), "Share Success",JOptionPane.DEFAULT_OPTION);//返回的是按钮的index  i=0或者1
                        break;
                    //3.添加分享记录失败
                    default:
                        int q = JOptionPane.showConfirmDialog(null, receiveMsg.getMsgText_Buf(), "Share Error",JOptionPane.DEFAULT_OPTION);//返回的是按钮的index  i=0或者1
                        break;
                }
            }
        });


    }
}
