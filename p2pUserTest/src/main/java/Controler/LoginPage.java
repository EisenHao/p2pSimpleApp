package Controler;


import Message.MsgFormat;
import Message.messageTextType;
import Socket.ClientSocketFun;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class LoginPage {
    //GUI相关
    static JFrame loginFrame;
    public JPanel rootJPanel;
    private JButton loginButton;
    private JButton registerButton;
    private JPasswordField passwordField;
    private JTextField userField;

    //定义一个 ClientSocketFun 对象
    private ClientSocketFun clientSocketFun;

    //构造函数
    public LoginPage(Socket inputConnectedSocket, String serverIsConnected) {
        //客户端的Socket 函数驱动
        clientSocketFun = new ClientSocketFun(inputConnectedSocket);


        loginFrame = new JFrame("Login Page [" + serverIsConnected + "]");
        loginFrame.setContentPane(rootJPanel);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.pack();
        loginFrame.setVisible(true);


        /**
         * 点击登录按钮
         * */
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                String password = String.valueOf(passwordField.getPassword());

                //若输入框非空
                if (!"".equals(username) && !"".equals(password)) {
                    //查询服务器登录

                    boolean isLoginSuccess = false;
                    //发送一则带格式的消息（用户名 + 密码）到服务器
                    MsgFormat receiveMsg = clientSocketFun.sendAMsg(messageTextType.LOGIN,"" + username + ";" + password);

                    //判断服务端返回的消息类型
                    if (messageTextType.LOGIN_CORRECT == receiveMsg.getMsgText_Type()) {
                        isLoginSuccess = true;//验证成功
                        HomePage homePage = new HomePage(username, inputConnectedSocket); //创建主页界面
                    } else {
                        isLoginSuccess = false;//验证失败
                        int n = JOptionPane.showConfirmDialog(null, receiveMsg.getMsgText_Buf(), "Login Error",JOptionPane.PLAIN_MESSAGE);//返回的是按钮的index  i=0或者1
                    }
                }
                else {
                    //Input null
                    int n = JOptionPane.showConfirmDialog(null, "Input Error!!!", "Login Error",JOptionPane.DEFAULT_OPTION);//返回的是按钮的index  i=0或者1
                }
            }
        });

        /**
         * 点击注册按钮
         * */
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                RegisterPage registerPage = new RegisterPage(inputConnectedSocket); //创建注册界面

            }
        });
    }
}
