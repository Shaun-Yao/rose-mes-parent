package com.honji.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.honji.entity.YsUnit;
import com.honji.mapper.YsUnitMapper;
import com.honji.service.IYsUnitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@DS("mes")
@Slf4j
@Service
public class YsUnitServiceImpl extends ServiceImpl<YsUnitMapper, YsUnit> implements IYsUnitService {


    @Autowired
    private YsUnitMapper UnitMapper;

    @Autowired
    private IYsUnitService ysUnitService;


    @Override
    public List<YsUnit> selectAll() {
        QueryWrapper<YsUnit> queryWrapper = new QueryWrapper<>();
        return UnitMapper.selectList(queryWrapper);
    }

    @Override
    public YsUnit selectByCode(String code) {
        QueryWrapper<YsUnit> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("unit_code", code);
        return UnitMapper.selectOne(queryWrapper);
    }

    @Override
    public void saveList(List<YsUnit> Units) {
       ysUnitService.saveBatch(Units);
    }

    @Transactional
    @Override
    public void sync(List<YsUnit> newUnits, List<YsUnit> updateUnits, List<YsUnit> removeUnits) {


        if (!newUnits.isEmpty()) {
            log.info("YsUnit新增数量 == {}", newUnits.size());
            ysUnitService.saveList(newUnits);
        }

        if (!updateUnits.isEmpty()) {
            log.info("YsUnit更新数量 == {}", updateUnits.size());
            ysUnitService.updateBatchById(updateUnits);
        }

        if (!removeUnits.isEmpty()) {
            log.info("YsUnit删除数量 == {}", removeUnits.size());
            List<String> ids = removeUnits.stream().map(YsUnit :: getId).collect(Collectors.toList());
            ysUnitService.removeByIds(ids);
        }

    }
}
