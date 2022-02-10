package com.honji.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.honji.entity.YsColor;
import com.honji.mapper.YsColorMapper;
import com.honji.service.IYsColorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class YsColorServiceImpl extends ServiceImpl<YsColorMapper, YsColor> implements IYsColorService {


    @Autowired
    private YsColorMapper colorMapper;

    @Autowired
    private IYsColorService ysColorService;

    @DS("mes")
    @Override
    public List<YsColor> selectAll() {
        QueryWrapper<YsColor> queryWrapper = new QueryWrapper<>();
        return colorMapper.selectList(queryWrapper);
    }

    @DS("mes")
    @Override
    public void saveList(List<YsColor> colors) {
       ysColorService.saveBatch(colors);
    }

    @DS("mes")
    @Transactional
    @Override
    public void sync(List<YsColor> newColors, List<YsColor> removeColors) {

        if (!removeColors.isEmpty()) {
            log.info("YsColor删除数量 == {}", removeColors.size());
            List<String> ids = removeColors.stream().map(YsColor :: getId).collect(Collectors.toList());
            ysColorService.removeByIds(ids);
        }
        if (!newColors.isEmpty()) {
            log.info("YsColor新增数量 == {}", newColors.size());
            ysColorService.saveList(newColors);
        }

    }
}
