package com.honji.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.honji.entity.Color;

import java.util.List;

public interface IColorService extends IService<Color> {

    List<Color> selectAll();
}