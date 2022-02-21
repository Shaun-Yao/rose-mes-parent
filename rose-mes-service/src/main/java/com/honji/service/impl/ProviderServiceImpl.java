package com.honji.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.honji.entity.Provider;
import com.honji.mapper.ProviderMapper;
import com.honji.service.IProviderService;
import org.springframework.stereotype.Service;


@Service
public class ProviderServiceImpl extends ServiceImpl<ProviderMapper, Provider> implements IProviderService {

}
