package com.stellar.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.stellar.mapper.SysUserMapper;
import com.stellar.dto.SysMenuDto;
import com.stellar.entity.SysMenu;
import com.stellar.entity.SysUser;
import com.stellar.mapper.SysMenuMapper;
import com.stellar.service.ISysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stellar.service.ISysUserService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Autowired
    ISysUserService sysUserService;
    @Autowired
    SysUserMapper sysUserMapper;

    @Override
    public List<SysMenuDto> getCurrentUserNav() {
        /*Get Current user*/
        String username =
            (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SysUser sysUser = sysUserService.getByUsername(username);
        /*Get menus of the user*/
        List<Long> menuIds = sysUserMapper.getNavMenuIds(sysUser.getId());
        /*Get Tree menu*/
        List<SysMenu> menus = buildTreeMenu(this.listByIds(menuIds));
        return convert(menus);
    }

    @Override
    public List<SysMenu> tree() {
        // Get information of all menus by ordering
        List<SysMenu> menus = this.list(new QueryWrapper<SysMenu>().orderByAsc("sort_num"));
        // Convert menus to tree
        return buildTreeMenu(menus);
    }

    /*Convert Menus to menusDto*/
    private List<SysMenuDto> convert(List<SysMenu> menus) {
        List<SysMenuDto> menuDtos = new ArrayList<>();
        menus.forEach(
            menu ->{
                SysMenuDto sysMenuDto = new SysMenuDto();
                sysMenuDto.setId(menu.getId());
                sysMenuDto.setName(menu.getPermissionCode());
                sysMenuDto.setTitle(menu.getName());
                sysMenuDto.setComponent(menu.getComponent());
                sysMenuDto.setIcon(menu.getIcon());
                sysMenuDto.setPath(menu.getPath());
                if (menu.getChildren().size()>0){
                sysMenuDto.setChildren(convert(menu.getChildren()));
            }
                menuDtos.add(sysMenuDto);
            });
        return menuDtos;

    }




    // 转换为树状结构
    private List<SysMenu> buildTreeMenu(List<SysMenu> menus) {
        List<SysMenu> finalMenus = new ArrayList<>();
        for (SysMenu menu : menus) {
            for (SysMenu aMenu : menus) {
                if(aMenu.getParentId() == menu.getId()){
                    menu.getChildren().add(aMenu);
                }
            }
            if (menu.getParentId() == 0L){
                finalMenus.add(menu);
            }

        }
         return finalMenus;
    }

}


