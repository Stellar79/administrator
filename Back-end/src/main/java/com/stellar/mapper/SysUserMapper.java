package com.stellar.mapper;

import com.stellar.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Stellar
 * @since 2022-07-17
 */
@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {

    List<Long> getNavMenuIds(Long userId);
    List<SysUser> listByMenuId(Long menuId);
}
