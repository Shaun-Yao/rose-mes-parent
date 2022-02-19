package com.honji.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.honji.entity.Inventory;
import com.honji.mapper.InventoryMapper;
import com.honji.service.IInventoryService;
import org.springframework.stereotype.Service;


@Service
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, Inventory> implements IInventoryService {

}
