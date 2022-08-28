package com.stellar.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
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
public class SysRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "The role name cannot be empty.")
    private String name;
    @NotBlank(message = "The unique code cannot be empty.")
    private String uniqueCode;

    private String remark;

    @TableField(exist = false)
    private List<Long> menuIds = new ArrayList<>();


}
