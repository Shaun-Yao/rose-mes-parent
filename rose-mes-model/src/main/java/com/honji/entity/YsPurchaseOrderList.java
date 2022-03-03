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
@TableName(value = "jy_pu_purchaseorder_b")
public class YsPurchaseOrderList {


    /**
     * 设置跟rose同个id
     */
    private String idb;

    /**
     * 父单id
     */
    @TableField(value = "iddto")
    private String parentId;

    /**
     * 存货档案id
     */
    @TableField(value = "idinventory")
    private String inventoryId;

    /**
     * 数量
     */
    private double quantity;

    /**
     * 单价
     */
    private double price;

    /**
     * 总价
     */
    @TableField(value = "discountamount")
    private double amount;


    /**
     * 到货日期
     */
    @TableField(value = "acceptDate")
    private LocalDateTime acceptDate;

    /**
     * 内部单号
     */
    @TableField(value = "internalnob")
    private String projectCode;


    /**
     * 修改日期
     */
    private LocalDateTime editDate;
}
