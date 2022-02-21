package com.honji.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Provider {

    /**
     * 编码
     */
    @TableField(value = "providerid")
    private String code;

    /**
     * 全名
     */
    private String name;

    /**
     * 简称
     */
    @TableField(value = "shortname")
    private String shortName;

    /**
     * 名字
     */
    @TableField(value = "provtypeid")
    private String parentCode;


    /**
     * 修改日期
     */
    @TableField(value = "EditDate")
    private LocalDateTime editDate;
}
