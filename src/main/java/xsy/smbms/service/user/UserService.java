package xsy.smbms.service.user;

import xsy.smbms.pojo.User;
import xsy.smbms.utils.PageSupport;

/**
 * Created by Administrator on 2020/6/19.
 */
public interface UserService {

    public User login(String userCode, String userPassword) throws Exception;

    public void getUserPage(PageSupport pageSupport, String userName, Integer roleId) throws Exception;

    //添加
    public int add(User user) throws Exception;

    //通过id获取用户
    public User getUserById(int uid) throws Exception;

    //修改用户
    public  int modify(User user) throws Exception;

    public int deleteUser(Integer id) throws Exception;


    public User getUserByUserCode(String userCode) throws Exception;
}
