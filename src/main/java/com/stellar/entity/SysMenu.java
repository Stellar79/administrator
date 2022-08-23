package com.stellar.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author Stellar
 * @since 2022-07-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysMenu extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * the first level menu is 0
     */
    @NotNull(message = "The parent menu cannot be empty")
    private Long parentId;

    @NotBlank(message = "The menu name cannot be empty")
    private String name;

    /**
     * the url of menu
     */
    private String path;

    /**
     * the permissions,separated by commas. For example: user:list, user:create
     */
    @NotBlank(message = "The permission code cannot be empty")
    private String permissionCode;

    private String component;

    /**
     * 0: column, 1: menu, 2: button
     */
    @NotNull(message = "The type of menu cannot be empty")
    private Integer type;

    /**
     * icon of menu
     */
    private String icon;

    @TableField("sort_num")
    private Integer sortNum;

    @TableField(exist = false)
    private List<SysMenu> children = new ArrayList<>();

}
