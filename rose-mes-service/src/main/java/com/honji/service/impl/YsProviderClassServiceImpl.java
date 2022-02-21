package com.honji.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.honji.entity.YsProviderClass;
import com.honji.mapper.YsProviderClassMapper;
import com.honji.service.IYsProviderClassService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@DS("mes")
@Slf4j
@Service
public class YsProviderClassServiceImpl extends ServiceImpl<YsProviderClassMapper, YsProviderClass> implements IYsProviderClassService {


    @Autowired
    private YsProviderClassMapper ProviderClassMapper;

    @Autowired
    private IYsProviderClassService ysProviderClassService;

    @Override
    public List<YsProviderClass> selectAll() {
        QueryWrapper<YsProviderClass> queryWrapper = new QueryWrapper<>();
        return ProviderClassMapper.selectList(queryWrapper);
    }

    @Override
    public void saveList(List<YsProviderClass> ProviderClasss) {
       ysProviderClassService.saveBatch(ProviderClasss);
    }

    @Transactional
    @Override
    public void sync(List<YsProviderClass> newProviderClasses, List<YsProviderClass> updateProviderClasses, List<YsProviderClass> removeProviderClasses) {

        if (!newProviderClasses.isEmpty()) {
            log.info("YsProviderClass新增数量 == {}", newProviderClasses.size());
            ysProviderClassService.saveList(newProviderClasses);
        }

        if (!updateProviderClasses.isEmpty()) {
            log.info("YsProviderClass更新数量 == {}", updateProviderClasses.size());
            ysProviderClassService.updateBatchById(updateProviderClasses);
        }

        if (!removeProviderClasses.isEmpty()) {
            log.info("YsProviderClass删除数量 == {}", removeProviderClasses.size());
            List<String> ids = removeProviderClasses.stream().map(YsProviderClass :: getId).collect(Collectors.toList());
            ysProviderClassService.removeByIds(ids);
        }

    }
}
