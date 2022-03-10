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
@TableName(value = "YS_AA_InventoryClass")
public class YsInventoryClass {


    @TableId(value = "pk_id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 编码
     */
    @TableField(value = "inv_code")
    private String code;

    /**
     * 父级编码
     */
    private String parentCode;
    private String parentCodes = "";//默认空字符串就行

    /**
     * 名字
     */
    @TableField(value = "inv_name")
    private String name;

    /**
     * 类型：1成品、2主料、3辅料
     */
    @TableField(value = "InvNature")
    private String type;

    private short treeSort = 0;
    private String treeSorts = "";//默认空字符串就行
    private String treeLeaf = "";//默认空字符串就行
    private short treeLevel = 0;
    private String treeNames = "";//默认空字符串就行

    /**
     * 状态，默认0
     */
    private short status = 0;

    /**
     * 创建人
     */
    private String createBy = "admin";

    /**
     * 创建人
     */
    private LocalDateTime createDate = LocalDateTime.now();

    /**
     * 更新人
     */
    private String updateBy = "admin";

    /**
     * 更新日期
     */
    private LocalDateTime updateDate = LocalDateTime.now();

    /**
     * 修改日期
     */
    private LocalDateTime editDate;
}
