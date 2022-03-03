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
@TableName(value = "PO")
public class PurchaseOrder {


    /**
     * 供应商代码
     */
    @TableField(value = "providerId")
    private String providerCode;

    /**
     * 单据编号
     */
    @TableField(value = "poid")
    private String code;

    /**
     * 状态
     */
    @TableField(value = "state")
    private String status;

    /**
     * 仓库id
     */
    @TableField(value = "locateid")
    private String wareHouse;

    /**
     * 创建人
     */
    @TableField(value = "submitusrnm")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "submitdate")
    private LocalDateTime createDate;

    /**
     * 更新
     */
    //private String updateBy;

    /**
     * 更新时间
     */
    //private LocalDateTime updateDate;


    /**
     * 单据日期
     */
    @TableField(value = "filldate")
    private LocalDateTime date;

    /**
     * 审核日期
     */
    @TableField(value = "confirmDate")
    private LocalDateTime auditDate;

    /**
     * 修改日期
     */
    @TableField(value = "EditDate")
    private LocalDateTime editDate;
}
