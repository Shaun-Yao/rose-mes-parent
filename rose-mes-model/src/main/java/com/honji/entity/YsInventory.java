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
@TableName(value = "YS_AA_Inventory")
public class YsInventory {


    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 编码
     */
    @TableField(value = "inv_codeB")
    private String code;

    /**
     * 父级编码
     */
    @TableField(value = "inv_code")
    private String parentCode;

    /**
     * 名字
     */
    @TableField(value = "inv_nameB")
    private String name;

    /**
     * 状态 0正常，2停用
     */
    private String status;

    /**
     * 单位id
     */
    @TableField(value = "idunit")
    private String unitId;

    @TableField(value = "valueType")
    private String valueType = "10";

    /**
     * 创建人
     */
    private String createBy = "admin";

    /**
     * 创建人
     */
    private LocalDateTime createDate = LocalDateTime.now();


    /**
     * 修改日期
     */
    private LocalDateTime editDate;
}
