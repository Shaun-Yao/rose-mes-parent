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
@TableName(value = "locate")
public class Warehouse {

    /**
     * 编码
     */
    @TableField(value = "locateid")
    private String code;

    /**
     * 全名
     */
    private String name;

    /**
     * 地址
     */
    private String address;


    /**
     * 修改日期
     */
    @TableField(value = "EditDate")
    private LocalDateTime editDate;
}
