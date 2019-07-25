package MySQL;

import Message.messageTextType;
import Peer.SharedFileRecord;
import Peer.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class jdbcMysqlFun {
    //mysql驱动包名
    private static final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
    //数据库连接地址
    private static final String URL = "jdbc:mysql://localhost:3306/db_user";
    //用户名
    private static final String USER_NAME = "root";
    //密码
    private static final String PASSWORD = "12345678";

    public jdbcMysqlFun() {
    }

    //查询数据库所有peer用户
    public List<User> findAllUser() throws SQLException {
        List<User> list = new ArrayList<User>();
        Connection connection = null;

        try {
            //加载mysql的驱动类
            Class.forName(DRIVER_NAME);
            //获取数据库连接
            connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
            //mysql查询语句
            String sql = "SELECT * FROM peer_user";
            PreparedStatement prst = connection.prepareStatement(sql);
            //结果集
            ResultSet rs = prst.executeQuery();

            User user = null;
            while (rs.next()) {
                int id = rs.getInt("id");

                user = new User();
                user.setUserName(rs.getString("name"));
                user.setPassword(rs.getString("password"));

                list.add(user);
            }
            rs.close();
            prst.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    //查询数据库所有被分享的文件 shared Files
    public List<SharedFileRecord> findAllSharedFiles() throws SQLException {
        List<SharedFileRecord> list = new ArrayList<SharedFileRecord>();
        Connection connection = null;

        try {
            //加载mysql的驱动类
            Class.forName(DRIVER_NAME);
            //获取数据库连接
            connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
            //mysql查询语句
            String sql = "SELECT * FROM shared_files";
            PreparedStatement prst = connection.prepareStatement(sql);
            //结果集
            ResultSet rs = prst.executeQuery();

            SharedFileRecord sharedFileRecord = null;
            while (rs.next()) {
                int id = rs.getInt("id");

                sharedFileRecord = new SharedFileRecord();
                sharedFileRecord.setSharer(rs.getString("sharer"));
                sharedFileRecord.setHostIPAndPost(rs.getString("hostIPAndPost"));
                sharedFileRecord.setFileName(rs.getString("fileName"));
                sharedFileRecord.setFileLength(rs.getInt("fileLength"));
                sharedFileRecord.setFilePSW(rs.getString("filePSW"));

                list.add(sharedFileRecord);
            }
            rs.close();
            prst.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    //查询数据库中 UserName 用户
    public List<User> findByUserName(String searchUserName) throws SQLException {
        List<User> list = new ArrayList<User>();
        Connection connection = null;

        try {
            //加载mysql的驱动类
            Class.forName(DRIVER_NAME);
            //获取数据库连接
            connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
            //mysql查询语句
            String sql = "SELECT * FROM peer_user where name=? ";

            PreparedStatement prst = connection.prepareStatement(sql);
            prst.setString(1, searchUserName);
            //结果集
            ResultSet rs = prst.executeQuery();

            User user = null;
            while (rs.next()) {

                int id = rs.getInt("id");
                user = new User();
                user.setUserName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                list.add(user);
            }
            rs.close();
            prst.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    //查询数据库中 UserName 分享的文件 shared Files
    public List<SharedFileRecord> findSharedFilesBySharerName(String searchSharerName) throws SQLException {
        List<SharedFileRecord> list = new ArrayList<SharedFileRecord>();
        Connection connection = null;

        try {
            //加载mysql的驱动类
            Class.forName(DRIVER_NAME);
            //获取数据库连接
            connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
            //mysql查询语句
            String sql = "SELECT * FROM shared_files where sharer=? ";

            PreparedStatement prst = connection.prepareStatement(sql);
            prst.setString(1, searchSharerName);
            //结果集
            ResultSet rs = prst.executeQuery();

            SharedFileRecord sharedFileRecord = null;
            while (rs.next()) {

                int id = rs.getInt("id");

                sharedFileRecord = new SharedFileRecord();
                sharedFileRecord.setSharer(rs.getString("sharer"));
                sharedFileRecord.setHostIPAndPost(rs.getString("hostIPAndPost"));
                sharedFileRecord.setFileName(rs.getString("fileName"));
                sharedFileRecord.setFileLength(rs.getInt("fileLength"));
                sharedFileRecord.setFilePSW(rs.getString("filePSW"));

                list.add(sharedFileRecord);

            }
            rs.close();
            prst.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }


    //查询是否存在 userName 用户
    public boolean isUserExisted(String userName) {
        boolean isExistedFlag = false;
        Connection connection = null;
        try {
            //加载mysql的驱动类
            Class.forName(DRIVER_NAME);
            //获取数据库连接
            connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
            //mysql查询语句
            String sql = "SELECT * FROM peer_user where name=? ";

            PreparedStatement prst = connection.prepareStatement(sql);
            prst.setString(1, userName);
            //结果集
            ResultSet rs = prst.executeQuery();

            while (rs.next()) {
                User user = null;
                int id = rs.getInt("id");
                user = new User();
                user.setUserName(rs.getString("name"));
                user.setPassword(rs.getString("password"));

                isExistedFlag = true;
                break;//有一个就退出
            }
            rs.close();
            prst.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return isExistedFlag;
    }


    //查询是否同一sharer已分享同名文件
    public boolean isSharerAndFileNameBothExisted(String sharer, String fileName) {
        boolean isExistedFlag = false;
        Connection connection = null;
        try {
            //加载mysql的驱动类
            Class.forName(DRIVER_NAME);
            //获取数据库连接
            connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
            //mysql查询语句
            String sql = "SELECT * FROM shared_files where sharer=? and fileName=?";

            PreparedStatement prst = connection.prepareStatement(sql);
            prst.setString(1, sharer);
            prst.setString(2, fileName);

            //结果集
            ResultSet rs = prst.executeQuery();

            while (rs.next()) {
                SharedFileRecord sharedFileRecord = null;
                int id = rs.getInt("id");

                sharedFileRecord = new SharedFileRecord();
                sharedFileRecord.setSharer(rs.getString("sharer"));
                sharedFileRecord.setHostIPAndPost(rs.getString("hostIPAndPost"));
                sharedFileRecord.setFileName(rs.getString("fileName"));
                sharedFileRecord.setFileLength(rs.getInt("fileLength"));
                sharedFileRecord.setFilePSW(rs.getString("filePSW"));

                System.out.println("" + sharedFileRecord.toString() + "is Existed!!!");

                isExistedFlag = true;
                break;//有一个就退出
            }
            rs.close();
            prst.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return isExistedFlag;
    }

    /**
     * 获取一个分享文件记录类
     * 返回类型：SharedFileRecord
     * 【慎用】，需先调用isSharerAndFileNameBothExisted()确保，该记录存在
     * */
    public SharedFileRecord getaSharedFileRecordBySharerAndFileName(String sharer, String fileName){
        Connection connection = null;
        //保存搜索的第一个结果
        SharedFileRecord sharedFileRecord = new SharedFileRecord();
        try {
            //加载mysql的驱动类
            Class.forName(DRIVER_NAME);
            //获取数据库连接
            connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
            //mysql查询语句
            String sql = "SELECT * FROM shared_files where sharer=? and fileName=?";

            PreparedStatement prst = connection.prepareStatement(sql);
            prst.setString(1, sharer);
            prst.setString(2, fileName);

            //结果集
            ResultSet rs = prst.executeQuery();


            while (rs.next()) {

                int id = rs.getInt("id");

                sharedFileRecord.setSharer(rs.getString("sharer"));
                sharedFileRecord.setHostIPAndPost(rs.getString("hostIPAndPost"));
                sharedFileRecord.setFileName(rs.getString("fileName"));
                sharedFileRecord.setFileLength(rs.getInt("fileLength"));
                sharedFileRecord.setFilePSW(rs.getString("filePSW"));

                break;//有一个就退出
            }
            rs.close();
            prst.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return sharedFileRecord;
    }


    //向数据库中 添加 UserName
    public int addUser(User addAUser) throws SQLException {
        Connection connection = null;
        boolean isExistedFlag = isUserExisted(addAUser.getUserName());
        int registerResult = 0;

        //若用户名已存在
        if (isExistedFlag == true) return messageTextType.REGISTER_USERNAME_EXISTED;

        //不存在，尝试添加
        try {
            //加载mysql的驱动类
            Class.forName(DRIVER_NAME);
            //获取数据库连接
            connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
            //mysql查询语句
            String sql = "insert into peer_user values (default, ?, ?)";

            PreparedStatement prst = connection.prepareStatement(sql);

            prst.setString(1, addAUser.getUserName());
            prst.setString(2,addAUser.getPassword());

            prst.executeUpdate();
            prst.close();
            registerResult = messageTextType.REGISTER_SUCCESS;
        } catch (Exception e) {
            //若进入此处说明出错
            registerResult = messageTextType.REGISTER_ERROR;
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return registerResult;
    }


    //向数据库中 添加 分享文件信息
    public int addShaerdFile(SharedFileRecord addAFileRecord){
        Connection connection = null;

        //返回是否存储记录成功
        int addResult = 0;

        //若同分享者同文件已分享，返回错误
        if(isSharerAndFileNameBothExisted(addAFileRecord.getSharer(), addAFileRecord.getFileName())){
            addResult = messageTextType.UNSHARED_HAS_EXISTED;
            return addResult;//已存在
        }

        //尝试添加
        try {
            //加载mysql的驱动类
            Class.forName(DRIVER_NAME);
            //获取数据库连接
            connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
            //mysql查询语句
            String sql = "insert into shared_files values (default, ?, ?, ?, ?, ?)";

            PreparedStatement prst = connection.prepareStatement(sql);

            prst.setString(1, addAFileRecord.getSharer());
            prst.setString(2, addAFileRecord.getHostIPAndPost());
            prst.setString(3, addAFileRecord.getFileName());
            prst.setInt(4, addAFileRecord.getFileLength());
            prst.setString(5, addAFileRecord.getFilePSW());

            prst.executeUpdate();
            prst.close();
            addResult = messageTextType.SHARED_SUCCESS;
        } catch (Exception e) {
            //若进入此处说明出错
            addResult = messageTextType.SHARED_ERROR;
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return addResult;
    }

    //向数据库中 删除 UserName
    public boolean deleteUser(String userName) throws SQLException {
        Connection connection = null;
        boolean isExistedFlag = isUserExisted(userName);

        if (isExistedFlag == false) return false;

        try {
            //加载mysql的驱动类
            Class.forName(DRIVER_NAME);
            //获取数据库连接
            connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
            //mysql查询语句
            String sql = "DELETE from peer_user where name=? ";

            PreparedStatement prst = connection.prepareStatement(sql);

            prst.setString(1, userName);

            prst.executeUpdate();
            System.out.println("delete Success");

            prst.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }


    //向数据库中 删除 分享文件信息
    public int deleteShaerdFile(String sharer, String fileName) throws SQLException {
        Connection connection = null;
        int deleteStatus = 0;//记录删除是否成功状态

        //查询是否该sharer已分享这个文件
        boolean isExistedFlag = isSharerAndFileNameBothExisted(sharer, fileName);

        //若未分享过该文件，返回未分享过
        if (isExistedFlag == false){
            System.out.println("" + sharer + "hasn't shared " + fileName);
            deleteStatus = messageTextType.UNSHARED_DONT_EXISTED;
            return deleteStatus;
        }

        //若分享过，尝试取消分享【从数据库中删除记录】
        try {
            //加载mysql的驱动类
            Class.forName(DRIVER_NAME);
            //获取数据库连接
            connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
            //mysql查询语句
            String sql = "DELETE from shared_files where sharer=? and fileName=?";

            PreparedStatement prst = connection.prepareStatement(sql);

            prst.setString(1, sharer);
            prst.setString(2, fileName);
            prst.executeUpdate();
            deleteStatus = messageTextType.UNSHARED_SUCCESS;
            System.out.println("delete " + sharer + "'s shared file(" + fileName + ") Success");
            prst.close();
        } catch (Exception e) {
            //进入此处说明数据库操作失败
            deleteStatus = messageTextType.UNSHARED_ERROR;
            System.out.println("delete " + sharer + "'s shared file(" + fileName + ") Error!!!");
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return deleteStatus;
    }

    //查询数据库中 UserName 用户、密码是否正确
    public int loginCheck(User checkUser) throws SQLException {
        System.out.println("\n\n查询数据库中 " + checkUser.getUserName() + " 用户");
        List<User> searchUserList = null;
        searchUserList = findByUserName(checkUser.getUserName());
        //一、未找到该用户名
        if (searchUserList.isEmpty()){
            //返回，查找用户不存在
            return messageTextType.LOGIN_DONT_EXISTED;
        }
        //二、找到一个该用户名
        else if(searchUserList.size() == 1){
            //若正好搜到一个，继续判断帐号密码是否正确
            User searchedUser = searchUserList.get(0);
            if(searchedUser.getUserName().equals(checkUser.getUserName()) && searchedUser.getPassword().equals(checkUser.getPassword())){
                //1.密码帐号均正确，返回"CORRECT"
                return messageTextType.LOGIN_CORRECT;
            } else {
                //1.密码错误，返回"INCORRECT"
                return messageTextType.LOGIN_INCORRECT;
            }
        }
        //三、其他（eg.找到多个用户）
        //返回客户端"SEARCH ERROR"
        return messageTextType.LOGIN_SEARCH_ERROR;
    }


    public static void main(String[] args) {

        /**
         *
         * peer_user数据表操作
         *
         *
         * */
//        jdbcMysqlFun db = new jdbcMysqlFun();
//
//        //遍历 查询数据库得到的 所有列表
//        System.out.println("\n\n遍历 查询数据库得到的 所有列表");
//        List<User> allUserlist = null;
//        try {
//            allUserlist = db.findAllUser();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        if (allUserlist.isEmpty()){
//            System.out.println("is Empty");
//        } else {
//            for (int i=0; i < allUserlist.size(); i++){
//                System.out.println(allUserlist.get(i).toString());
//            }
//        }
//        //查询数据库中 UserName 用户
//        System.out.println("\n\n查询数据库中 UserName 用户");
//        List<User> searchUserList = null;
//        try {
//            searchUserList = db.findByUserName("admin");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        if (searchUserList.isEmpty()){
//            System.out.println("search is Empty");
//        } else {
//            for (int i=0; i < searchUserList.size(); i++){
//                System.out.println("search:" + searchUserList.get(i).toString());
//            }
//        }
//        //查询数据库中 是否存在 用户名为 "admin"
//        System.out.println("is Existed admin? " + db.isUserExisted("admin"));
//        //向数据库中 删除 UserName="bilibili"
//        try {
//            System.out.println("is successfully delete User[\"bilibili\"]？  -- " + db.deleteUser("bilibili"));
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }



        /**
         *
         * shared_files 数据表操作
         *
         *
         * */
        jdbcMysqlFun db = new jdbcMysqlFun();

        //1.遍历 查询数据表 shared_files中 所有记录
        System.out.println("\n\n1.遍历 查询数据表 shared_files中 所有记录");
        List<SharedFileRecord> allSharedFileslist = null;
        try {
            allSharedFileslist = db.findAllSharedFiles();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (allSharedFileslist.isEmpty()){
            System.out.println("is Empty");
        } else {
            for (int i=0; i < allSharedFileslist.size(); i++){
                System.out.println(allSharedFileslist.get(i).toString());
            }
        }
        //2.查询数据表 shared_files 中 admin 用户的分享记录
        System.out.println("\n\n2.查询数据表 shared_files 中 admin 用户的分享记录");
        List<SharedFileRecord> searchSharerList = null;
        try {
            searchSharerList = db.findSharedFilesBySharerName("admin");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (searchSharerList.isEmpty()){
            System.out.println("search is Empty");
        } else {
            for (int i=0; i < searchSharerList.size(); i++){
                System.out.println("search:" + searchSharerList.get(i).toString());
            }
        }
        //3.查询数据表 shared_files中 是否存在 分享者为 "admin"，分享文件为"hahaha.txt" 的记录
        System.out.println("\n\n3.查询数据表 shared_files中 是否存在 分享者为 \"admin\"，分享文件为\"hahaha.txt\" 的记录");
        System.out.println("is Existed admin? " + db.isSharerAndFileNameBothExisted("admin", "hahaha.txt"));
        //4.向数据表 shared_files中 删除 分享者为 "bilibili"，分享文件为"dilidili.txt" 的记录
        System.out.println("\n\n4.向数据表 shared_files中 删除 分享者为 \"bilibili\"，分享文件为\"dilidili.txt\" 的记录");
        try {
            System.out.println("is successfully delete Sharer Record[\"bilibili\"：\"dilidili.txt\"]？  -- " + db.deleteShaerdFile("bilibili", "dilidili.txt"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //5.向数据表 shared_files中 添加 分享者为 "baidu"，分享文件为"duniang.cpp" 的记录
        System.out.println("\n\n5.向数据表 shared_files中 添加 分享者为 \"baidu\"，分享文件为\"duniang.cpp\" 的记录");
        System.out.println("is successfully add Sharer Record[\"baidu\"：\"duniang.cpp\"]？  -- " + db.addShaerdFile(new SharedFileRecord("baidu","127.0.0.1/1234","duniang.cpp",1024,"~/p2pTest")));







    }
}
