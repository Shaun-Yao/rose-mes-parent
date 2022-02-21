package com.honji.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.honji.entity.YsProviderClass;

import java.util.List;

public interface IYsProviderClassService extends IService<YsProviderClass> {

    List<YsProviderClass> selectAll();
    void saveList(List<YsProviderClass> ProviderClasses);
    void sync(List<YsProviderClass> newProviderClasses, List<YsProviderClass> updateProviderClasses, List<YsProviderClass> removeProviderClasses);
}