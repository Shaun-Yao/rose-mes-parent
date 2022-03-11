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
@TableName(value = "POLIST")
public class PurchaseOrderList {

    private String id;

    /**
     * 父id
     */
    @TableField(value = "poid")
    private String parentId;

    /**
     * 分类档案代码
     */
    @TableField(value = "seriesid")
    private String inventoryId;


    /**
     * 颜色
     */
    private String color;

    /**
     * 规格
     */
    @TableField(value = "model")
    private String number;


    /**
     * 数量
     */
    @TableField(value = "multqty")
    private double quantity;

    /**
     * 单价
     */
    @TableField(value = "multprice")
    private double price;

    /**
     * 总价
     */
    @TableField(value = "multtotal")
    private double amount;


    /**
     * 发货日期
     */
    @TableField(value = "deliverDate")
    private LocalDateTime acceptDate;

    /**
     * 内部单号
     */
    @TableField(value = "projectCode")
    private String projectCode;


    /**
     * 修改日期
     */
    @TableField(value = "EditDate")
    private LocalDateTime editDate;
}
