package com.stellar.service;

import com.stellar.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Stellar
 * @since 2022-07-17
 */
public interface ISysRoleService extends IService<SysRole> {


    List<SysRole> listRolesByUserId(Long userId);
}
