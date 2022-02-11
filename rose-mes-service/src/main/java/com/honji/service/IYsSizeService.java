package com.honji.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.honji.entity.YsSize;

import java.util.List;

public interface IYsSizeService extends IService<YsSize> {

    List<YsSize> selectAll();
    void saveList(List<YsSize> Sizes);
    void sync(List<YsSize> newSizes, List<YsSize> updateSizes, List<YsSize> removeSizes);
}