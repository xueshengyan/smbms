package xsy.smbms.service.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xsy.smbms.mapper.role.RoleMapper;
import xsy.smbms.pojo.Role;

import java.util.List;

/**
 * Created by Administrator on 2020/6/20.
 */
@Service

public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper ;

    @Override
    public List<Role> getRoleList() throws Exception {
        return roleMapper.getRoleList();

    }
}
