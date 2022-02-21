package com.honji.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.honji.entity.YsProvider;
import com.honji.mapper.YsProviderMapper;
import com.honji.service.IYsProviderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class YsProviderServiceImpl extends ServiceImpl<YsProviderMapper, YsProvider> implements IYsProviderService {


    @Autowired
    private YsProviderMapper ProviderMapper;

    @Autowired
    private IYsProviderService ysProviderService;

    @DS("mes")
    @Override
    public List<YsProvider> selectAll() {
        QueryWrapper<YsProvider> queryWrapper = new QueryWrapper<>();
        return ProviderMapper.selectList(queryWrapper);
    }

    @DS("mes")
    @Override
    public void saveList(List<YsProvider> Providers) {
       ysProviderService.saveBatch(Providers);
    }

    @DS("mes")
    @Transactional
    @Override
    public void sync(List<YsProvider> newProvideres, List<YsProvider> updateProvideres, List<YsProvider> removeProvideres) {

        if (!newProvideres.isEmpty()) {
            log.info("YsProvider新增数量 == {}", newProvideres.size());
            ysProviderService.saveList(newProvideres);
        }

        if (!updateProvideres.isEmpty()) {
            log.info("YsProvider更新数量 == {}", updateProvideres.size());
            ysProviderService.updateBatchById(updateProvideres);
        }

        if (!removeProvideres.isEmpty()) {
            log.info("YsProvider删除数量 == {}", removeProvideres.size());
            List<String> ids = removeProvideres.stream().map(YsProvider :: getId).collect(Collectors.toList());
            ysProviderService.removeByIds(ids);
        }

    }
}
