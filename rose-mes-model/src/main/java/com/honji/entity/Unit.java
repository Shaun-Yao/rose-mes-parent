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
public class Unit {

    /**
     * 编码
     */
    @TableField(value = "UnitID")
    private String code;

    /**
     * 名字
     */
    private String name;

    /**
     * 修改日期
     */
    @TableField(value = "EditDate")
    private LocalDateTime editDate;
}
