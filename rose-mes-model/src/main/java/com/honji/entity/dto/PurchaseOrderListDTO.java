package com.honji.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class PurchaseOrderListDTO {

    private String id;

    /**
     * 父id
     */
    private String parentId;

    /**
     * 分类档案代码
     */
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
    private double amount;


    /**
     * 颜色
     */
    private String color;

    /**
     * 规格
     */
    private String size;

    /**
     * 发货日期
     */
    private LocalDateTime acceptDate;

    /**
     * 内部单号
     */
    private String projectCode;


    /**
     * 修改日期
     */
    private LocalDateTime editDate;
}
