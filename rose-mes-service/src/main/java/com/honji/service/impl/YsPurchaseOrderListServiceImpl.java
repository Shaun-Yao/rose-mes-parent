package com.honji.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.honji.entity.YsPurchaseOrderList;
import com.honji.mapper.YsPurchaseOrderListMapper;
import com.honji.service.IYsPurchaseOrderListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@DS("mes")
@Slf4j
@Service
public class YsPurchaseOrderListServiceImpl extends ServiceImpl<YsPurchaseOrderListMapper, YsPurchaseOrderList> implements IYsPurchaseOrderListService {


    @Autowired
    private YsPurchaseOrderListMapper PurchaseOrderMapper;

    @Autowired
    private IYsPurchaseOrderListService YsPurchaseOrderListService;

    @Override
    public List<YsPurchaseOrderList> selectAll() {
        QueryWrapper<YsPurchaseOrderList> queryWrapper = new QueryWrapper<>();
        return PurchaseOrderMapper.selectList(queryWrapper);
    }

    @Override
    public void saveList(List<YsPurchaseOrderList> PurchaseOrders) {
       YsPurchaseOrderListService.saveBatch(PurchaseOrders);
    }


    @Transactional
    @Override
    public void sync(List<YsPurchaseOrderList> newPurchaseOrderes, List<YsPurchaseOrderList> updatePurchaseOrderes, List<YsPurchaseOrderList> removePurchaseOrderes) {

        if (!newPurchaseOrderes.isEmpty()) {
            log.info("YsPurchaseOrderList新增数量 == {}", newPurchaseOrderes.size());
            YsPurchaseOrderListService.saveList(newPurchaseOrderes);
        }

        if (!updatePurchaseOrderes.isEmpty()) {
            log.info("YsPurchaseOrderList更新数量 == {}", updatePurchaseOrderes.size());
            YsPurchaseOrderListService.updateBatchById(updatePurchaseOrderes);
        }

        if (!removePurchaseOrderes.isEmpty()) {
            log.info("YsPurchaseOrderList删除数量 == {}", removePurchaseOrderes.size());
            List<String> ids = removePurchaseOrderes.stream().map(YsPurchaseOrderList ::getIdb).collect(Collectors.toList());
            YsPurchaseOrderListService.removeByIds(ids);
        }

    }
}
