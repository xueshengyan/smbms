package xsy.smbms.service.role;

import xsy.smbms.pojo.Role;

import java.util.List;

/**
 * Created by Administrator on 2020/6/20.
 */
public interface RoleService {

    public List<Role> getRoleList()throws Exception;

}
