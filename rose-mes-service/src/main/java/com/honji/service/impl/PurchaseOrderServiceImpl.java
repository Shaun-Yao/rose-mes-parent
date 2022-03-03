package com.honji.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.honji.entity.PurchaseOrder;
import com.honji.mapper.PurchaseOrderMapper;
import com.honji.service.IPurchaseOrderService;
import org.springframework.stereotype.Service;


@Service
public class PurchaseOrderServiceImpl extends ServiceImpl<PurchaseOrderMapper, PurchaseOrder> implements IPurchaseOrderService {

}
