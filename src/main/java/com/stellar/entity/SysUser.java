package com.stellar.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import java.time.LocalDateTime;

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
public class SysUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "The username cannot be empty.")
    private String username;

    private String password;

    private String avatar;

    @NotBlank(message = "The email cannot be empty.")
    private String email;

    private String city;

    private LocalDateTime created;

    private LocalDateTime lastLogin;

    @TableField(exist = false)
    private List<SysRole> roles = new ArrayList<>();


}
