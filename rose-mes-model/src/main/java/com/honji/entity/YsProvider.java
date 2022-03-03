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
@TableName(value = "YS_AA_Partner")
public class YsProvider {


    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 编码
     */
    @TableField(value = "Partner_codeB")
    private String code;

    /**
     * 20客户，21供应商，22客户/供应商
     */
    @TableField(value = "partnerType")
    private int partnerType = 21;

    /**
     * 父级编码
     */
    @TableField(value = "Partner_code")
    private String parentCode;

    /**
     * 名字
     */
    @TableField(value = "partner_full_nameb")
    private String name;

    /**
     * 名字
     */
    @TableField(value = "Partner_nameB")
    private String shortName;

    /**
     * 状态
     */
    private String status = "0";


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
