package com.honji.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.honji.entity.YsUnit;

import java.util.List;

public interface IYsUnitService extends IService<YsUnit> {

    List<YsUnit> selectAll();
    YsUnit selectByCode(String code);
    void saveList(List<YsUnit> Units);
    void sync(List<YsUnit> newUnits, List<YsUnit> updateUnits, List<YsUnit> removeUnits);
}