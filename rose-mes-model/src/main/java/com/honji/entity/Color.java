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
public class Color {

    /**
     * 编码
     */
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
