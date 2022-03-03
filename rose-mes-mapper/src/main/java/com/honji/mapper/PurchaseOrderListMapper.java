package com.honji.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.honji.entity.PurchaseOrderList;
import com.honji.entity.dto.PurchaseOrderListDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@DS("rose")
public interface PurchaseOrderListMapper extends BaseMapper<PurchaseOrderList> {
//    @Select({"<script>",
//            "SELECT polist.id, poid as parentId, mmf.code as inventoryId, ",
//            "multqty as quantity, multprice as price, multtotal as amount,",
//            "deliverDate as acceptDate, projectCode, polist.editdate",
//            "FROM polist JOIN mmf on polist.matid = mmf.matid ",
//            "WHERE poid is not null ",
//            "</script>"})
    @Select({"<script>",
            "SELECT polist.id, poid, mmf.code as seriesid, ",
            "multqty, multprice, multtotal,",
            "deliverDate, projectCode, polist.editdate",
            "FROM polist JOIN mmf on polist.matid = mmf.matid ",
            "WHERE poid is not null ",
            "</script>"})
    List<PurchaseOrderList> list();

    @Select({"<script>",
            "SELECT polist.id, poid as parentId, mmf.code as inventoryId, ",
            "multqty as quantity, multprice as price, multtotal as amount,",
            "deliverDate as acceptDate, projectCode, polist.editdate",
            "FROM polist JOIN mmf on polist.matid = mmf.matid ",
            "WHERE poid = '${parentCode}' ",
            "</script>"})
    List<PurchaseOrderListDTO> listByParentCode(@Param("parentCode") String parentCode);
}
