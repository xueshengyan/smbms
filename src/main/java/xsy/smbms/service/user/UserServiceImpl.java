package xsy.smbms.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xsy.smbms.mapper.user.UserMapper;
import xsy.smbms.pojo.User;
import xsy.smbms.utils.PageSupport;

import java.util.List;

/**
 * Created by Administrator on 2020/6/19.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 登录
     * @param userCode
     * @param userPassword
     * @return
     */
    @Override
    public User login(String userCode, String userPassword) throws Exception {
        return userMapper.getLoginUser(userCode,userPassword);
    }

    @Override
    public void getUserPage(PageSupport pageSupport, String userName, Integer roleId) throws Exception {
        //获取总页数的值
        int userCount = userMapper.getUserCount(userName, roleId);
        //计算总页数
        int totalPage = userCount% pageSupport.getPageSize() == 0?
                userCount/pageSupport.getPageSize() : userCount/pageSupport.getPageSize()+1;

        //页码的兼容性处理
        if(pageSupport.getCurrentPageNo() > totalPage){
            pageSupport.setCurrentPageNo(totalPage);
        }else if(pageSupport.getCurrentPageNo()<1) {
            pageSupport.setCurrentPageNo(1);
        }
        List<User> userList = userMapper.getUserList(userName, roleId, (pageSupport.getCurrentPageNo()-1)* pageSupport.getPageSize(), pageSupport.getPageSize());
        pageSupport.setList(userList);
        //把总页码放入pageSupport;
        pageSupport.setTotalCount(userCount);
        pageSupport.setTotalPageCount(totalPage);
    }

    @Override
    public int add(User user) throws Exception {
        return userMapper.add(user);
    }

    @Override
    public User getUserById(int uid) throws Exception {
        return userMapper.getUserById(String.valueOf(uid));
    }

    @Override
    public int modify(User user) throws Exception {
        return userMapper.modify(user);
    }

    @Override
    public int deleteUser(Integer id) throws Exception {
        return userMapper.deleteUserById(id);
    }

    @Override
    public User getUserByUserCode(String userCode) throws Exception {
        return userMapper.getUserByUserCode(userCode);
    }


}
