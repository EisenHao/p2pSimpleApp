package Controler;

import Message.MsgFormat;
import Message.messageTextType;
import Socket.ClientSocketFun;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class RegisterPage {
    //GUI相关
    static JFrame registerFrame;
    public JPanel RegisterJPanel;
    private JTextField userField;
    private JPasswordField passwordField1;
    private JButton registerButton;
    private JPasswordField passwordField2;

    //定义一个 ClientSocketFun 对象
    private ClientSocketFun clientSocketFun;


    //构造函数
    public RegisterPage(Socket inputConnectedSocket) {


        //客户端的Socket 函数驱动
        clientSocketFun = new ClientSocketFun(inputConnectedSocket);


        registerFrame = new JFrame("RegisterPage");
        registerFrame.setContentPane(RegisterJPanel);
        registerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        registerFrame.pack();
        registerFrame.setVisible(true);


        //点击注册按钮
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                String password1 = String.valueOf(passwordField1.getPassword());
                String password2 = String.valueOf(passwordField2.getPassword());

                //若输入框非空
                if(!"".equals(username) && !"".equals(password1) && password1.equals(password2)) {
                    //查询服务器，尝试注册
                    //发送一则带格式的注册消息（用户名 + 密码）到服务器
                    MsgFormat receiveMsg = clientSocketFun.sendAMsg(messageTextType.REGISTER,"" + username + ";" + password1);
                    //阻塞以监听，服务器端返回字符串
//                    System.out.println("receiveMsg.getMsgText_Buf()= " + receiveMsg.getMsgText_Buf());
                    //若注册成功
                    if (messageTextType.REGISTER_SUCCESS == receiveMsg.getMsgText_Type()) {
                        int m = JOptionPane.showConfirmDialog(null, receiveMsg.getMsgText_Buf(), "Register Success",JOptionPane.DEFAULT_OPTION);//返回的是按钮的index  i=0或者1
                    }
                    //若注册失败
                    else {
                        int n = JOptionPane.showConfirmDialog(null, receiveMsg.getMsgText_Buf(), "Register Error",JOptionPane.PLAIN_MESSAGE);//返回的是按钮的index  i=0或者1
                    }
                }
                else {
                    //Register Error
                    System.out.println("Input Error !!!");
                    int n = JOptionPane.showConfirmDialog(null, "Register Error!!!", "Error",JOptionPane.DEFAULT_OPTION);//返回的是按钮的index  i=0或者1
                }
            }
        });
    }
}
