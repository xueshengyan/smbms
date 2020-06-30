package xsy.smbms.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xsy.smbms.pojo.Role;
import xsy.smbms.pojo.User;
import xsy.smbms.service.role.RoleService;
import xsy.smbms.service.user.UserService;
import xsy.smbms.utils.Constants;
import xsy.smbms.utils.PageSupport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.*;

/**
 * Created by Administrator on 2020/6/20.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    /**
     * 查询用户
     * @param userName
     * @param roleId
     * @param model
     * @param pageIndex
     * @return
     * @throws Exception
     */
    @RequestMapping("uList")
    public String userList(@RequestParam(value = "queryname",required = false) String userName,
                           @RequestParam(value = "queryUserRole",required = false) Integer roleId,
                           Model model,
                           @RequestParam(value = "pageIndex",required = false) Integer pageIndex) throws Exception {

        PageSupport pageSupport = new PageSupport();
        if(pageIndex != null){
            pageSupport.setCurrentPageNo(pageIndex);
        }
        if(roleId==null){
            roleId = 0;
        }
        userService.getUserPage(pageSupport,userName,roleId);
        List<Role> roleList = roleService.getRoleList();
        //把角色列表放入model
        model.addAttribute("roleList",roleList);
        //把用户名和角色id返回给页面回显
        model.addAttribute("queryUserName",userName);
        model.addAttribute("queryUserRole",roleId);

        model.addAttribute("userList",pageSupport.getList());
        model.addAttribute("totalCount",pageSupport.getTotalCount());
        model.addAttribute("currentPageNo",pageSupport.getCurrentPageNo());
        model.addAttribute("totalPageCount",pageSupport.getTotalPageCount());

        return "user/userlist";

    }

    /**
     * 查询用户信息
     * @param uid
     * @return
     */
    //使用REST风格
    @RequestMapping(value = "/{uid}",method = RequestMethod.GET)
    public String view(@PathVariable("uid") Integer uid,Model model) throws Exception {
        User user = userService.getUserById(uid);
        model.addAttribute("user",user);
        return "user/userview";
    }

    /**
     * 转发到添加页面
     * @return
     */
    @RequestMapping("/add")
    public String add(Model model) throws Exception {
        //查询角色列表
        List<Role> roleList = roleService.getRoleList();
        model.addAttribute("roleList",roleList);
        return "user/useradd";
    }

    /**
     * 添加用户
     * @return
     */
//    @RequestMapping("/userAdd")
    @RequestMapping(value = {"/",""},method = RequestMethod.POST)
    public String userAdd(User user, HttpSession session,
                          @RequestParam(value = "pic") MultipartFile file,
                          HttpServletRequest request) throws Exception {
        //用户的添加时间
        user.setCreationDate(new Date());
        User userSession =(User) session.getAttribute(Constants.USER_SESSION);
        if(userSession !=null){
            user.setCreatedBy(userSession.getId());
        }
        //保存图片
        if (!file.isEmpty()){
            String path = session.getServletContext().getRealPath("/upload");
            File filePath = new File(path);
            if(!filePath.exists()){
                //创建目录
                filePath.mkdirs();
            }
            //创建文件名
            String oldName = file.getOriginalFilename();
            String suffix = oldName.substring(oldName.lastIndexOf(".") + 1, oldName.length());//源文件后缀
//            String newFileName = System.currentTimeMillis()+ RandomUtils.nextInt(1000,9999)+suffix;
            String newFileName=UUID.randomUUID().toString()+"."+suffix;
            file.transferTo(new File(path+File.separator+newFileName));
            //第二部：给user的idPicPath赋值
            String protocal = request.getScheme();
            String serverName = request.getServerName();
            int port = request.getServerPort();
            String appName = request.getContextPath();
            user.setIdPicPath(protocal+"://"+serverName+":"+port+File.separator+appName+"upload"+File.separator+newFileName);

        }
        int count  = userService.add(user);
        if (count > 0) {
            return "redirect:/user/uList";
        }else {
            return "/add";
        }
    }
    @ResponseBody
    @RequestMapping(value = "/ucexist",method = RequestMethod.GET)
    public String ucexist(@RequestParam("userCode") String userCode) throws Exception {
        User user = userService.getUserByUserCode(userCode);
        Map<String,String> map = new HashMap<>();
        if (user!=null){
            map.put("userCode","exist");
        }else {
            map.put("userCode","notExist");
        }
        return JSON.toJSONString(map);
    }

    /**
     * 跳转到修改页面
     * @param id
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/modify",method = RequestMethod.GET)
    public String modify(@RequestParam(value = "uid") Integer id,
                         Model model) throws Exception {
        User user = userService.getUserById(id);
        model.addAttribute("user",user);
        //获取角色列表
        List<Role> roleList = roleService.getRoleList();
        model.addAttribute("roleList",roleList);
        return "user/usermodify";
    }

    /**
     * 修改用户
     * @param user
     * @param id
     * @param model
     * @return
     * @throws Exception
     */
//    @RequestMapping("/userModify")
    @RequestMapping(value = {"/",""},method = RequestMethod.PUT)
    public String userModify(User user,@RequestParam(name = "uid") Integer id,
                             Model model) throws Exception {
        //根据id获取用户信息
        User userOriginal = userService.getUserById(id);
        userOriginal.setUserName(user.getUserName());
        userOriginal.setGender(user.getGender());
        userOriginal.setBirthday(user.getBirthday());
        userOriginal.setPhone(user.getPhone());
        userOriginal.setUserRole(user.getUserRole());
        int count = userService.modify(userOriginal);
        if (count>0){
            return "redirect:/user/uList";
        }else {
            return "modify";
        }
    }

    /**
     * 删除用户
     * @param id
     * @return
     * @throws Exception
     */
    /*@ResponseBody
    @RequestMapping(value = "/{uid}",method = RequestMethod.DELETE)
    public Object delete(@PathVariable("uid" ) Integer id) throws Exception {
        int count = userService.deleteUser(id);
        Map<String,String> map = new HashMap<>();
        if (count>0){
            map.put("delResult","true");
        }else {
            map.put("delResult","false");
        }
        return JSON.toJSONString(map);
    }*/
    @ResponseBody
    @RequestMapping(value = "/{uid}",method = RequestMethod.DELETE)
    public String delete(@PathVariable("uid") Integer id) throws Exception {
        int count = userService.deleteUser(id);
        if(count>0){
            return "{\"delResult\" : \"true\"}";
        }else {
            return "{\"delResult\" : \"false\"}";
        }
    }

    //测试xml配置中文乱码
    @ResponseBody
    @RequestMapping(value = "/test")
    public String test() throws Exception {
        List<Role> roleList = roleService.getRoleList();
        return JSON.toJSONString(roleList);
    }

    @ResponseBody
    @RequestMapping(value = "/view/{uid}",method = RequestMethod.GET)
    public Object view2(@PathVariable("uid") Integer id,Model model) throws Exception {
        User user = userService.getUserById(id);
        return user;
    }

}
