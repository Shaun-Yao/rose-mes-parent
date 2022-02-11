package com.honji.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.honji.entity.Size;
import com.honji.mapper.SizeMapper;
import com.honji.service.ISizeService;
import org.springframework.stereotype.Service;


@Service
public class SizeServiceImpl extends ServiceImpl<SizeMapper, Size> implements ISizeService {

}
