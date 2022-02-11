package com.honji.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.honji.entity.Unit;
import com.honji.mapper.UnitMapper;
import com.honji.service.IUnitService;
import org.springframework.stereotype.Service;


@Service
public class UnitServiceImpl extends ServiceImpl<UnitMapper, Unit> implements IUnitService {

}
