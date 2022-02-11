package com.honji.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.honji.entity.YsSize;
import com.honji.mapper.YsSizeMapper;
import com.honji.service.IYsSizeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class YsSizeServiceImpl extends ServiceImpl<YsSizeMapper, YsSize> implements IYsSizeService {


    @Autowired
    private YsSizeMapper SizeMapper;

    @Autowired
    private IYsSizeService ysSizeService;

    @DS("mes")
    @Override
    public List<YsSize> selectAll() {
        QueryWrapper<YsSize> queryWrapper = new QueryWrapper<>();
        return SizeMapper.selectList(queryWrapper);
    }

    @DS("mes")
    @Override
    public void saveList(List<YsSize> Sizes) {
       ysSizeService.saveBatch(Sizes);
    }

    @DS("mes")
    @Transactional
    @Override
    public void sync(List<YsSize> newSizes, List<YsSize> updateSizes, List<YsSize> removeSizes) {


        if (!newSizes.isEmpty()) {
            log.info("YsSize新增数量 == {}", newSizes.size());
            ysSizeService.saveList(newSizes);
        }

        if (!updateSizes.isEmpty()) {
            log.info("YsSize更新数量 == {}", updateSizes.size());
            ysSizeService.updateBatchById(updateSizes);
        }

        if (!removeSizes.isEmpty()) {
            log.info("YsSize删除数量 == {}", removeSizes.size());
            List<String> ids = removeSizes.stream().map(YsSize :: getId).collect(Collectors.toList());
            ysSizeService.removeByIds(ids);
        }

    }
}
