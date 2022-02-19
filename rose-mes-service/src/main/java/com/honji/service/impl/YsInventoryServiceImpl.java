package com.honji.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.honji.entity.YsInventory;
import com.honji.mapper.YsInventoryMapper;
import com.honji.service.IYsInventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class YsInventoryServiceImpl extends ServiceImpl<YsInventoryMapper, YsInventory> implements IYsInventoryService {


    @Autowired
    private YsInventoryMapper InventoryMapper;

    @Autowired
    private IYsInventoryService ysInventoryService;

    @DS("mes")
    @Override
    public List<YsInventory> selectAll() {
        QueryWrapper<YsInventory> queryWrapper = new QueryWrapper<>();
        return InventoryMapper.selectList(queryWrapper);
    }

    @DS("mes")
    @Override
    public void saveList(List<YsInventory> Inventorys) {
       ysInventoryService.saveBatch(Inventorys);
    }

    @DS("mes")
    @Transactional
    @Override
    public void sync(List<YsInventory> newInventoryes, List<YsInventory> updateInventoryes, List<YsInventory> removeInventoryes) {

        if (!newInventoryes.isEmpty()) {
            log.info("YsInventory新增数量 == {}", newInventoryes.size());
            ysInventoryService.saveList(newInventoryes);
        }

        if (!updateInventoryes.isEmpty()) {
            log.info("YsInventory更新数量 == {}", updateInventoryes.size());
            ysInventoryService.updateBatchById(updateInventoryes);
        }

        if (!removeInventoryes.isEmpty()) {
            log.info("YsInventory删除数量 == {}", removeInventoryes.size());
            List<String> codes = removeInventoryes.stream().map(YsInventory :: getCode).collect(Collectors.toList());
            QueryWrapper<YsInventory> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("inv_code", codes);
            ysInventoryService.remove(queryWrapper);
        }

    }
}
