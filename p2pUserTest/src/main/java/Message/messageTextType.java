package Message;


//消息类型 信号字符串的属性类型 【值】


public class messageTextType {
    //标准请求
    public static final int DEFAULT = 0;    //默认类型；MsgTextStr为：纯字符串
    public static final int REGISTER = 1;   //注册类型；MsgTextStr为："userName" + ";" + "password"
    public static final int LOGIN = 2;      //登录类型；MsgTextStr为："userName" + "_" + "password"
    public static final int REQUEST = 3;
    public static final int OTHER = 4;
    public static final int RECEIVE_ERROR = 5; //接收失败
    public static final int GETALLSHAREDFILES = 6; //查询数据表 shared_files中 所有记录
    public static final int GETMYSHAREDFILES = 7;  //查询数据表 shared_files中 我的所有记录
    public static final int SHAREDAFILE = 8;  // 分享 数据表 shared_files中 一个文件
    public static final int UNSHAREDAFILE = 9;  // 取消分享 数据表 shared_files中 一个文件
    public static final int GETASHAREDFILE = 10; //获取一个分享文件的信息


    //服务器返回"登录"信息类型
    public static final int LOGIN_DONT_EXISTED = 111; //返回 不存在
    public static final int LOGIN_CORRECT = 222; //返回 正确
    public static final int LOGIN_INCORRECT = 333; //返回 不正确
    public static final int LOGIN_SEARCH_ERROR = 444; //返回 查询错误


    //服务器返回"注册"信息类型
    public static final int REGISTER_USERNAME_EXISTED = 123; //返回用户已存在
    public static final int REGISTER_SUCCESS = 234; //注册成功
    public static final int REGISTER_ERROR = 345; //注册失败


    //服务器返回"文件分享记录"信息类型
    public static final int SHARED_SUCCESS = 987; //返回数据库记录成功
    public static final int SHARED_ERROR = 876; //返回数据库记录失败
    public static final int UNSHARED_DONT_EXISTED = 765; //返回 数据库中没查询到该用户分享过该文件
    public static final int UNSHARED_SUCCESS = 654; //返回成功删除【取消分享成功】
    public static final int UNSHARED_ERROR = 543; //返回删除失败【取消分享失败】
    public static final int SHARED_LIST_EMPTY = 432; // 查询列表为空
    public static final int SHARED_LIST = 321; //查询列表
    public static final int UNSHARED_HAS_EXISTED = 210; //返回 数据库中该用户已经分享过该文件
    public static final int SHARED_A_RECORD = 4321; //查询列表
}
