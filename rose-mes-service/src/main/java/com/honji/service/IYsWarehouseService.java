package com.honji.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.honji.entity.Warehouse;
import com.honji.entity.YsWarehouse;

import java.util.List;

public interface IYsWarehouseService extends IService<YsWarehouse> {

    List<YsWarehouse> selectAll();
    void sync(List<Warehouse> warehouses);
}