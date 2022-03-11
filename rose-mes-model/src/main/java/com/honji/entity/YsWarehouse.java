package com.honji.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName(value = "YS_AA_WareHouse")
public class YsWarehouse {


    /**
     * 与编码一致
     */
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
     * 地址
     */
    private String address;

    /**
     * 状态 0正常，1停用
     */
    private String status = "0";

    /**
     * 是否启用货位 0未启用，1已启用
     * 默认0就行
     */
    @TableField(value = "isCargo")
    private String isCargo = "0";

    /**
     * 可用量参与计算
     */
    @TableField(value = "availableJoin")
    private String availableJoin = "0";

    /**
     * 允许零库存出库
     */
    @TableField(value = "allowZeroOut")
    private String allowZeroOut = "0";


}
