package com.honji.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.honji.entity.YsInventory;

import java.util.List;

public interface IYsInventoryService extends IService<YsInventory> {

    List<YsInventory> selectAll();
    void saveList(List<YsInventory> Inventorys);
    void sync(List<YsInventory> newInventorys, List<YsInventory> updateInventorys, List<YsInventory> removeInventorys);
}