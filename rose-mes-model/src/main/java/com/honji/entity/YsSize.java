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
@TableName(value = "YS_Size")
public class YsSize {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 编码
     */
    private String code;

    /**
     * 名字
     */
    private String name;

    /**
     * 是否使用，默认人0
     */
    @TableField(value = "isuse")
    private short isUse = 0;

    /**
     * 修改日期
     */
    private LocalDateTime editDate;
}
