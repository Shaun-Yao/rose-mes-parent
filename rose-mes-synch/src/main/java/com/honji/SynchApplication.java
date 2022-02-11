package com.honji;

import com.honji.entity.*;
import com.honji.service.*;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@MapperScan("com.honji.mapper")
//@EnableAsync
@EnableScheduling
@Slf4j
public class SynchApplication {
    @Autowired
    private IColorService colorService;
    @Autowired
    private ISizeService sizeService;
    @Autowired
    private IUnitService unitService;

    @Autowired
    private IYsColorService ysColorService;
    @Autowired
    private IYsSizeService ysSizeService;
    @Autowired
    private IYsUnitService ysUnitService;

    public static void main(String[] args) {
        SpringApplication.run(SynchApplication.class, args);

    }


    @Scheduled(fixedDelay = 6000 * 1000)  //1分钟同步一次
    public void syncUnit() {
        List<Unit> units = unitService.list();
        List<YsUnit> ysUnits = ysUnitService.selectAll();
        List<YsUnit> newUnits = new ArrayList<>();
        List<YsUnit> updateUnits = new ArrayList<>();
        List<YsUnit> removeUnits = new ArrayList<>();
        for(Unit unit : units) {
            boolean isExist = false;
            String code = unit.getCode();
            for(YsUnit ysUnit : ysUnits) {
                String ysCode = ysUnit.getCode();
                if (code.equals(ysCode)) {
                    isExist = true;
                    //如果编辑日期不相等则有更新
                    if (!unit.getEditDate().isEqual(ysUnit.getEditDate())) {
                        //更新暂时只有code,name
                        ysUnit.setCode(code).setName(unit.getName())
                                .setEditDate(unit.getEditDate());
                        updateUnits.add(ysUnit);
                    }
                    break;//找到后无需再循环
                }
            }
            if(!isExist) {//不存在则添加
                YsUnit newUnit = new YsUnit().setCode(code)
                        .setName(unit.getName()).setEditDate(unit.getEditDate());
                newUnits.add(newUnit);
            }
        }

        //判断是否删除
        for(YsUnit ysUnit : ysUnits) {
            String ysCode = ysUnit.getCode();
            boolean isExist = units.stream().filter(e -> ysCode.equals(e.getCode())).findAny().isPresent();
            if(!isExist) {//不存在则添加
                removeUnits.add(ysUnit);
            }
        }
        //执行数据库操作
        ysUnitService.sync(newUnits, updateUnits, removeUnits);

    }

    //@Scheduled(fixedDelay = 6000 * 1000)  //1分钟同步一次
    public void syncSize() {
        List<Size> sizes = sizeService.list();
        List<YsSize> ysSizes = ysSizeService.selectAll();
        List<YsSize> newSizes = new ArrayList<>();
        List<YsSize> updateSizes = new ArrayList<>();
        List<YsSize> removeSizes = new ArrayList<>();
        for(Size size : sizes) {
            boolean isExist = false;
            String code = size.getCode();
            for(YsSize ysSize : ysSizes) {
                String ysCode = ysSize.getCode();
                if (code.equals(ysCode)) {
                    isExist = true;
                    //如果编辑日期不相等则有更新
                    if (!size.getEditDate().isEqual(ysSize.getEditDate())) {
                        //更新暂时只有code,name
                        ysSize.setCode(code).setName(size.getName())
                                .setEditDate(size.getEditDate());
                        updateSizes.add(ysSize);
                    }
                    break;//找到后无需再循环
                }
            }
            if(!isExist) {//不存在则添加
                YsSize newSize = new YsSize().setCode(code)
                        .setName(size.getName()).setEditDate(size.getEditDate());
                newSizes.add(newSize);
            }
        }

        //判断是否删除
        for(YsSize ysSize : ysSizes) {
            String ysCode = ysSize.getCode();
            boolean isExist = sizes.stream().filter(e -> ysCode.equals(e.getCode())).findAny().isPresent();
            if(!isExist) {//不存在则添加
                removeSizes.add(ysSize);
            }
        }
        //执行数据库操作
        ysSizeService.sync(newSizes, updateSizes, removeSizes);

    }

    //@Scheduled(fixedDelay = 6000 * 1000)  //1分钟同步一次
    public void syncColor() {
        List<Color> colors = colorService.list();
        List<YsColor> ysColors = ysColorService.selectAll();
        List<YsColor> newColors = new ArrayList<>();
        List<YsColor> updateColors = new ArrayList<>();
        List<YsColor> removeColors = new ArrayList<>();
        for(Color color : colors) {
            boolean isExist = false;
            String code = color.getCode();
            for(YsColor ysColor : ysColors) {
                String ysCode = ysColor.getCode();
                if (code.equals(ysCode)) {
                    isExist = true;
                    //如果编辑日期不相等则有更新
                    if (!color.getEditDate().isEqual(ysColor.getEditDate())) {
                        //更新暂时只有code,name
                        ysColor.setCode(code).setName(color.getName())
                                .setEditDate(color.getEditDate());
                        updateColors.add(ysColor);
                    }
                    break;//找到后无需再循环
                }
            }
            if(!isExist) {//不存在则添加
                YsColor newColor = new YsColor().setCode(code)
                        .setName(color.getName()).setEditDate(color.getEditDate());
                newColors.add(newColor);
            }
        }

        //判断是否删除
        for(YsColor ysColor : ysColors) {
            String ysCode = ysColor.getCode();
            boolean isExist = colors.stream().filter(e -> ysCode.equals(e.getCode())).findAny().isPresent();
            if(!isExist) {//不存在则添加
                removeColors.add(ysColor);
            }
        }
        //执行数据库操作
        ysColorService.sync(newColors, updateColors, removeColors);

    }
}
