package com.stellar.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.stellar.entity.SysRole;
import com.stellar.mapper.SysRoleMapper;
import com.stellar.service.ISysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Stellar
 * @since 2022-07-17
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Override
    public List<SysRole> listRolesByUserId(Long userId) {
        List<SysRole> sysRoles = this.list(new QueryWrapper<SysRole>()
            .inSql("id","SELECT role_id FROM sys_user_role where user_id = "+userId ));
    return sysRoles;
    }

}
