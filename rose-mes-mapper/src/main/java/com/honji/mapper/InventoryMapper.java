package com.honji.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.honji.entity.Inventory;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface InventoryMapper extends BaseMapper<Inventory> {

    @Select({"<script>",
            "SELECT result.code, result.name, seriesid as parentCode, unit.unitid as unit_id,",
            " mmfstate as status, edit_date FROM ",
            "(SELECT code, name, seriesid, mmfstate, mmf.editdate as edit_date,",
            "CASE when provmat.price is null then 0 else provmat.price END as price, ",
            "CASE when provmat.unit is null then mmf.unit else provmat.unit END as unit ",
            " FROM mmf LEFT JOIN ",
            "(SELECT matid, max(multprice) price, max(multunit) unit FROM provmat GROUP BY matid) provmat",
            " on mmf.matid = provmat.matid ",
            //"WHERE mmf.MMFState = '启用'",
            " ) result",
            " join unit on result.unit = unit.name",
//            " join series on result.seriesid = series.seriesid",
            "</script>"})
    List<Inventory> selectList();
}
