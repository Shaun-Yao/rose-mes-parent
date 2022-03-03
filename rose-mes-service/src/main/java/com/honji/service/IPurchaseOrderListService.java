package com.honji.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.honji.entity.PurchaseOrderList;
import com.honji.entity.dto.PurchaseOrderListDTO;

import java.util.List;

public interface IPurchaseOrderListService extends IService<PurchaseOrderList> {
    List<PurchaseOrderList> selectList();
    List<PurchaseOrderListDTO> selectListByParentCode(String parentCode);
}