package com.honji.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.honji.entity.YsProvider;

import java.util.List;

public interface IYsProviderService extends IService<YsProvider> {

    List<YsProvider> selectAll();
    void saveList(List<YsProvider> Providers);
    void sync(List<YsProvider> newProviders, List<YsProvider> updateProviders, List<YsProvider> removeProviders);
}