package com.honji.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.honji.entity.Warehouse;
import com.honji.mapper.WarehouseMapper;
import com.honji.service.IWarehouseService;
import org.springframework.stereotype.Service;


@Service
public class WarehouseServiceImpl extends ServiceImpl<WarehouseMapper, Warehouse> implements IWarehouseService {

}
