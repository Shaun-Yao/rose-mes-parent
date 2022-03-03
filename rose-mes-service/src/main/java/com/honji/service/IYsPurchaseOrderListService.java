package com.honji.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.honji.entity.YsPurchaseOrderList;

import java.util.List;

public interface IYsPurchaseOrderListService extends IService<YsPurchaseOrderList> {

    List<YsPurchaseOrderList> selectAll();
    void saveList(List<YsPurchaseOrderList> PurchaseOrders);
    void sync(List<YsPurchaseOrderList> newPurchaseOrders, List<YsPurchaseOrderList> updatePurchaseOrders, List<YsPurchaseOrderList> removePurchaseOrders);
}