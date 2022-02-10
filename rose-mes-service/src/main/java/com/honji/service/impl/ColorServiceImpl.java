package com.honji.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.honji.entity.Color;
import com.honji.mapper.ColorMapper;
import com.honji.service.IColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class ColorServiceImpl extends ServiceImpl<ColorMapper, Color> implements IColorService {

//    @Autowired
//    @Qualifier("mesJdbcTemplate")
//    JdbcTemplate jdbcTemplate;

    @Autowired
    private ColorMapper colorMapper;

    @DS("rose")
    public List<Color> selectAll() {

        QueryWrapper<Color> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("EditDate", "2022-01-01");
        return colorMapper.selectList(queryWrapper);
    }
}
