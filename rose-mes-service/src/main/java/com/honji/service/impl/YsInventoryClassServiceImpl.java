package com.honji.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.honji.entity.YsInventoryClass;
import com.honji.mapper.YsInventoryClassMapper;
import com.honji.service.IYsInventoryClassService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class YsInventoryClassServiceImpl extends ServiceImpl<YsInventoryClassMapper, YsInventoryClass> implements IYsInventoryClassService {


    @Autowired
    private YsInventoryClassMapper InventoryClassMapper;

    @Autowired
    private IYsInventoryClassService ysInventoryClassService;

    @DS("mes")
    @Override
    public List<YsInventoryClass> selectAll() {
        QueryWrapper<YsInventoryClass> queryWrapper = new QueryWrapper<>();
        return InventoryClassMapper.selectList(queryWrapper);
    }

    @DS("mes")
    @Override
    public void saveList(List<YsInventoryClass> InventoryClasss) {
       ysInventoryClassService.saveBatch(InventoryClasss);
    }

    @DS("mes")
    @Transactional
    @Override
    public void sync(List<YsInventoryClass> newInventoryClasses, List<YsInventoryClass> updateInventoryClasses, List<YsInventoryClass> removeInventoryClasses) {

        if (!newInventoryClasses.isEmpty()) {
            log.info("YsInventoryClass新增数量 == {}", newInventoryClasses.size());
            ysInventoryClassService.saveList(newInventoryClasses);
        }

        if (!updateInventoryClasses.isEmpty()) {
            log.info("YsInventoryClass更新数量 == {}", updateInventoryClasses.size());
            ysInventoryClassService.updateBatchById(updateInventoryClasses);
        }

        if (!removeInventoryClasses.isEmpty()) {
            log.info("YsInventoryClass删除数量 == {}", removeInventoryClasses.size());
            List<String> codes = removeInventoryClasses.stream().map(YsInventoryClass :: getCode).collect(Collectors.toList());
            QueryWrapper<YsInventoryClass> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("inv_code", codes);
            ysInventoryClassService.remove(queryWrapper);
        }

    }
}
