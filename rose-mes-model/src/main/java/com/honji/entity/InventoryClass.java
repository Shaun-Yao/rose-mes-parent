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
@TableName(value = "series")
public class InventoryClass {

    /**
     * 编码
     */
    @TableField(value = "seriesid")
    private String code;

    /**
     * 名字
     */
    private String name;

    /**
     * 名字
     */
    @TableField(value = "treeId")
    private String treeId;

    /**
     * 修改日期
     */
    @TableField(value = "EditDate")
    private LocalDateTime editDate;
}
