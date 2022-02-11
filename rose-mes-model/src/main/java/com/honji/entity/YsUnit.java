package com.honji.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName(value = "YS_AA_Unit")
public class YsUnit {

    @TableId(value = "unit_id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 编码
     */
    @TableField(value = "unit_code")
    private String code;

    /**
     * 名字
     */
    @TableField(value = "unit_name")
    private String name;

    private short isMainUnit = 0;
    private short isSingleUnit = 1;
    private short isGroup = 0;
    private short unitRate = 1;

    /**
     * 状态，默认0
     */
    private short status = 0;

    /**
     * 创建人
     */
    private String createBy = "admin";

    /**
     * 创建人
     */
    private LocalDateTime createDate = LocalDateTime.now();

    /**
     * 更新人
     */
    private String updateBy = "admin";

    /**
     * 更新日期
     */
    private LocalDateTime updateDate = LocalDateTime.now();

    /**
     * 修改日期
     */
    private LocalDateTime editDate;
}
