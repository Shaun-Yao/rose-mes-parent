package com.honji.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.honji.entity.PurchaseOrderList;
import com.honji.entity.dto.PurchaseOrderListDTO;
import com.honji.mapper.PurchaseOrderListMapper;
import com.honji.service.IPurchaseOrderListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PurchaseOrderListServiceImpl extends ServiceImpl<PurchaseOrderListMapper, PurchaseOrderList> implements IPurchaseOrderListService {

    @Autowired
    private PurchaseOrderListMapper orderListMapper;

    @Override
    public List<PurchaseOrderList> selectList() {
        return orderListMapper.list();
    }

    @Override
    public List<PurchaseOrderListDTO> selectListByParentCode(String parentCode) {
        return orderListMapper.listByParentCode(parentCode);
    }
}
