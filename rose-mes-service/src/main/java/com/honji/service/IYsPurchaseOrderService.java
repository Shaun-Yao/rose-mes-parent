package com.honji.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.honji.entity.YsPurchaseOrder;
import com.honji.entity.YsPurchaseOrderList;

import java.util.List;

public interface IYsPurchaseOrderService extends IService<YsPurchaseOrder> {

    List<YsPurchaseOrder> selectAll();
    void saveList(List<YsPurchaseOrder> PurchaseOrders);
    void sync(List<YsPurchaseOrder> newPurchaseOrders, List<YsPurchaseOrderList> ysPurchaseOrderLists,
              List<YsPurchaseOrder> updatePurchaseOrders, List<YsPurchaseOrder> removePurchaseOrders);
}