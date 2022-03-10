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
@TableName(value = "jy_pu_purchaseorder")
public class YsPurchaseOrder {



    //@TableId(type = IdType.ASSIGN_ID)
    /**
     * 手动设置与code相同，方便子表关联
     */
    private String id;

    /**
     * 订单编码
     */
    private String code;

    /**
     * 供应商id
     */
    @TableField(value = "idpartner")
    private String partnerId;


    /**
     * 0代表未审核，1代表已审核
     */
    @TableField(value = "auditstatus")
    private String status;

    /**
     * 仓库Id
     */
    @TableField(value = "idwarehouse")
    private String wareHouse;


    /**
     * 单据日期
     */
    @TableField(value = "voucherdate")
    private LocalDateTime date;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createDate;

    /**
     * 更新
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateDate;


    /**
     * 到货日期
     */
//    @TableField(value = "acceptdate")
//    private LocalDateTime confirmDate;

    /**
     * 审核日期
     */
    @TableField(value = "auditedDate")
    private LocalDateTime auditDate;

    /**
     * 修改日期
     */
    private LocalDateTime editDate;
}
