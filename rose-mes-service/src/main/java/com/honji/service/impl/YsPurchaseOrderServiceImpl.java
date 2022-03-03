package com.honji.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.honji.entity.PurchaseOrderList;
import com.honji.entity.YsPurchaseOrder;
import com.honji.entity.YsPurchaseOrderList;
import com.honji.mapper.PurchaseOrderListMapper;
import com.honji.mapper.YsPurchaseOrderListMapper;
import com.honji.mapper.YsPurchaseOrderMapper;
import com.honji.service.IYsPurchaseOrderListService;
import com.honji.service.IYsPurchaseOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class YsPurchaseOrderServiceImpl extends ServiceImpl<YsPurchaseOrderMapper, YsPurchaseOrder> implements IYsPurchaseOrderService {

    @Autowired
    private YsPurchaseOrderMapper purchaseOrderMapper;

    @Autowired
    private PurchaseOrderListMapper orderListMapper;

    @Autowired
    private YsPurchaseOrderListMapper YsOrderListMapper;

    @Autowired
    private IYsPurchaseOrderService ysPurchaseOrderService;

    @Autowired
    private IYsPurchaseOrderListService ysPurchaseOrderListService;

    @DS("mes")
    @Override
    public List<YsPurchaseOrder> selectAll() {
        QueryWrapper<YsPurchaseOrder> queryWrapper = new QueryWrapper<>();

        return purchaseOrderMapper.selectList(queryWrapper);
    }

    @DS("mes")
    @Override
    public void saveList(List<YsPurchaseOrder> PurchaseOrders) {
       ysPurchaseOrderService.saveBatch(PurchaseOrders);
    }


    @DS("mes")
    @Transactional
    @Override
    public void sync(List<YsPurchaseOrder> newPurchaseOrderes, List<YsPurchaseOrderList> ysPurchaseOrderLists,
                     List<YsPurchaseOrder> updatePurchaseOrderes, List<YsPurchaseOrder> removePurchaseOrderes) {

        if (!newPurchaseOrderes.isEmpty()) {
            log.info("YsPurchaseOrder新增数量 == {}", newPurchaseOrderes.size());
            ysPurchaseOrderService.saveList(newPurchaseOrderes);
        }

        //更新和删除都要先删除子表, 所以可以合并删除
        List<String> updateIds = updatePurchaseOrderes.stream().map(YsPurchaseOrder :: getId).collect(Collectors.toList());
        List<String> removeIds = removePurchaseOrderes.stream().map(YsPurchaseOrder :: getId).collect(Collectors.toList());
        //removeIds.addAll(updateIds);//合并主表id
        updateIds.addAll(removeIds);
        log.warn("ids = {}", updateIds);
        if (!updateIds.isEmpty()) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.in("iddto", updateIds);
            YsOrderListMapper.delete(queryWrapper);
        }

        if (!updatePurchaseOrderes.isEmpty()) {
            log.info("YsPurchaseOrder更新数量 == {}", updatePurchaseOrderes.size());
            ysPurchaseOrderService.updateBatchById(updatePurchaseOrderes);
        }

        if (!removePurchaseOrderes.isEmpty()) {
            log.info("YsPurchaseOrder删除数量 == {}", removePurchaseOrderes.size());

            ysPurchaseOrderService.removeByIds(removeIds);
        }
        //主表新增和更新都要插入新子表记录，更新在前面已经删除子表记录
        log.info("YsPurchaseOrderList新增数量 == {}", ysPurchaseOrderLists.size());
        ysPurchaseOrderListService.saveBatch(ysPurchaseOrderLists);

    }

    @DS("rose")
    private List<PurchaseOrderList> listOrderList(String parentId) {
        QueryWrapper<PurchaseOrderList> queryWrapper = new QueryWrapper();
        queryWrapper.in("poid", parentId);
        return orderListMapper.selectList(queryWrapper);
    }

    private void saveSubList(String parentId) {
        List<PurchaseOrderList> orderLists = listOrderList(parentId);
//        for(PurchaseOrderList purchaseOrder : orderLists) {
//            YsPurchaseOrderList newPurchaseOrderList = new YsPurchaseOrderList().setIdb(purchaseOrder.getId())
//                    .setQuantity(purchaseOrder.getQuantity()).setPrice(purchaseOrder.getPrice())
//                    .setAmount(purchaseOrder.getAmount())
//                    .setProjectCode(purchaseOrder.getProjectCode()).setAcceptDate(purchaseOrder.getAcceptDate())
//                    .setEditDate(purchaseOrder.getEditDate());
//
//            String code = purchaseOrder.getInventoryId();
//            YsInventory ysInventory = ysInventories.stream().filter(e -> code.equals(e.getCode())).findAny().get();
//
//            String parentCode = purchaseOrder.getParentId();
//            log.warn("parentCode = {}", parentCode);
//            YsPurchaseOrder order = ysPurchaseOrders.stream().filter(e -> parentCode.equals(e.getCode())).findAny().get();
//            newPurchaseOrderList.setInventoryId(ysInventory.getId());
//            newPurchaseOrderList.setParentId(order.getId());
//            newPurchaseOrderLists.add(newPurchaseOrderList);
//        }

    }
}
