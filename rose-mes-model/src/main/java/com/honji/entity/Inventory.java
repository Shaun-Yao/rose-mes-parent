package com.honji.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName(value = "MMF")
public class Inventory {

    /**
     * 编码
     */
    private String code;

    /**
     * 名字
     */
    private String name;

    /**
     * 单位代码
     */
    private String unitId;

    /**
     * 父级目录代码
     */
    private String parentCode;

    /**
     * 树id
     */
//    private String treeId;

    /**
     * 状态：启用，停用两种
     */
    private String status;

    /**
     * 修改日期
     */
    @TableField(value = "EditDate")
    private LocalDateTime editDate;
}
