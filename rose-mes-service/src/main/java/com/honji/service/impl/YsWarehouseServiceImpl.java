package com.honji.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.honji.entity.Warehouse;
import com.honji.entity.YsWarehouse;
import com.honji.mapper.YsWarehouseMapper;
import com.honji.service.IYsWarehouseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@DS("mes")
@Slf4j
@Service
public class YsWarehouseServiceImpl extends ServiceImpl<YsWarehouseMapper, YsWarehouse> implements IYsWarehouseService {


    @Autowired
    private YsWarehouseMapper ysWarehouseMapper;

    @Autowired
    private WarehouseServiceImpl warehouseService;

    @Autowired
    private IYsWarehouseService ysWarehouseService;

    @Override
    public List<YsWarehouse> selectAll() {
        QueryWrapper<YsWarehouse> queryWrapper = new QueryWrapper<>();
        return ysWarehouseMapper.selectList(queryWrapper);
    }

    @Transactional
    @Override
    public void sync(List<Warehouse> warehouses) {

        List<YsWarehouse> newWarehouses = new ArrayList<>();
        for(Warehouse warehouse : warehouses) {
            String code = warehouse.getCode();
            YsWarehouse newWarehouse = new YsWarehouse().setId(code).setCode(code)
                    .setName(warehouse.getName()).setAddress(warehouse.getAddress());
            newWarehouses.add(newWarehouse);
        }
        QueryWrapper<YsWarehouse> deleteAll = new QueryWrapper<>();
        ysWarehouseMapper.delete(deleteAll);
        ysWarehouseService.saveBatch(newWarehouses);
    }
}
