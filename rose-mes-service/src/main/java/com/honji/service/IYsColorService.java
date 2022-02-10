package com.honji.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.honji.entity.YsColor;

import java.util.List;

public interface IYsColorService extends IService<YsColor> {

    List<YsColor> selectAll();
    void saveList(List<YsColor> colors);
    void sync(List<YsColor> newColors, List<YsColor> updateColors, List<YsColor> removeColors);
}