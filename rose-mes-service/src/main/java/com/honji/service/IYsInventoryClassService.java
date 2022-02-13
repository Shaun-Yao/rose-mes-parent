package com.honji.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.honji.entity.YsInventoryClass;

import java.util.List;

public interface IYsInventoryClassService extends IService<YsInventoryClass> {

    List<YsInventoryClass> selectAll();
    void saveList(List<YsInventoryClass> InventoryClasss);
    void sync(List<YsInventoryClass> newInventoryClasss, List<YsInventoryClass> updateInventoryClasss, List<YsInventoryClass> removeInventoryClasss);
}