package com.honji.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.honji.entity.InventoryClass;
import com.honji.mapper.InventoryClassMapper;
import com.honji.service.IInventoryClassService;
import org.springframework.stereotype.Service;


@Service
public class InventoryClassServiceImpl extends ServiceImpl<InventoryClassMapper, InventoryClass> implements IInventoryClassService {

}
