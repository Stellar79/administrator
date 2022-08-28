package com.stellar.service;

import com.stellar.dto.SysMenuDto;
import com.stellar.entity.SysMenu;
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
public interface ISysMenuService extends IService<SysMenu> {

    List<SysMenuDto> getCurrentUserNav();

    List<SysMenu> tree();
}
