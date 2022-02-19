package com.honji;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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

    @Autowired
    private IInventoryClassService inventoryClassService;
    @Autowired
    private IYsInventoryClassService ysInventoryClassService;

    @Autowired
    private IInventoryService inventoryService;
    @Autowired
    private IYsInventoryService ysInventoryService;


    public static void main(String[] args) {
        SpringApplication.run(SynchApplication.class, args);

    }

    @Scheduled(fixedDelay = 6000 * 1000)  //1分钟同步一次
    public void syncInventory() {
        List<Inventory> inventoryes = inventoryService.list();
        List<YsInventory> ysInventoryes = ysInventoryService.selectAll();
        List<YsUnit> ysUnits = ysUnitService.selectAll();
        List<YsInventory> newInventoryes = new ArrayList<>();
        List<YsInventory> updateInventoryes = new ArrayList<>();
        List<YsInventory> removeInventoryes = new ArrayList<>();
        for(Inventory inventory : inventoryes) {
            boolean isExist = false;
            String code = inventory.getCode();
            for (YsInventory ysInventory : ysInventoryes) {
                String ysCode = ysInventory.getCode();
                if (code.equals(ysCode)) {
                    isExist = true;
                    //如果编辑日期不相等则有更新,代码相同其它属性有修改
                    if (!inventory.getEditDate().isEqual(ysInventory.getEditDate())) {
                        //更新暂时只有name，editDate
                        ysInventory.setName(inventory.getName())
                                .setEditDate(inventory.getEditDate());
                        updateInventoryes.add(ysInventory);
                    }
                    break;//找到后无需再循环
                }
            }
            if (!isExist) {//不存在则添加
                YsInventory newInventory = new YsInventory().setCode(code)
                        .setName(inventory.getName()).setEditDate(inventory.getEditDate());
                String treeId = inventory.getTreeId();
                String parentId = parseParentId(treeId);
                String unitCode = inventory.getUnitId();
                YsUnit unit = ysUnits.stream().filter(e -> unitCode.equals(e.getCode())).findAny().get();
                newInventory.setUnitId(unit.getId());

                if (parentId.equals("0")) {//空指针直接报错
                    newInventory.setParentCode("0");//直接设置，不用查询0父菜单
                } else {
                    for (Inventory ic : inventoryes) {
                        if (parentId.equals(ic.getTreeId())) {
                            newInventory.setParentCode(ic.getCode());
                            break;
                        }
                    }
                }
                newInventoryes.add(newInventory);
            }
        }
        //判断是否删除
        for(YsInventory ysInventory : ysInventoryes) {
            String ysCode = ysInventory.getCode();
            boolean isExist = inventoryes.stream().filter(e -> ysCode.equals(e.getCode())).findAny().isPresent();
            if(!isExist) {//不存在则添加
                removeInventoryes.add(ysInventory);
            }
        }
        //执行数据库操作
        ysInventoryService.sync(newInventoryes, updateInventoryes, removeInventoryes);
    }

    @Scheduled(fixedDelay = 6000 * 1000)  //1分钟同步一次
    public void syncInventoryClass() {
        QueryWrapper<InventoryClass> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("seriesid", "1");//排队“货品分类”菜单
        List<InventoryClass> inventoryClasses = inventoryClassService.list(queryWrapper);
        List<YsInventoryClass> ysInventoryClasses = ysInventoryClassService.selectAll();
        List<YsInventoryClass> newInventoryClasses = new ArrayList<>();
        List<YsInventoryClass> updateInventoryClasses = new ArrayList<>();
        List<YsInventoryClass> removeInventoryClasses = new ArrayList<>();
        for(InventoryClass inventoryClass : inventoryClasses) {
            boolean isExist = false;
            String code = inventoryClass.getCode();
            for (YsInventoryClass ysInventoryClass : ysInventoryClasses) {
                String ysCode = ysInventoryClass.getCode();
                if (code.equals(ysCode)) {
                    isExist = true;
                    //如果编辑日期不相等则有更新,代码相同其它属性有修改
                    if (!inventoryClass.getEditDate().isEqual(ysInventoryClass.getEditDate())) {
                        //更新暂时只有name，editDate
                        ysInventoryClass.setName(inventoryClass.getName())
                                .setEditDate(inventoryClass.getEditDate());
                        updateInventoryClasses.add(ysInventoryClass);
                    }
                    break;//找到后无需再循环
                }
            }
            if (!isExist) {//不存在则添加
                YsInventoryClass newInventoryClass = new YsInventoryClass().setCode(code)
                        .setName(inventoryClass.getName()).setEditDate(inventoryClass.getEditDate());
                String treeId = inventoryClass.getTreeId();
                String parentId = parseParentId(treeId);

                if (parentId.equals("0")) {//空指针直接报错
                    newInventoryClass.setParentCode("0");//直接设置，不用查询0父菜单
                } else {
                    for (InventoryClass ic : inventoryClasses) {
                        if (parentId.equals(ic.getTreeId())) {
                            newInventoryClass.setParentCode(ic.getCode());
                            break;
                        }
                    }
                }
                newInventoryClasses.add(newInventoryClass);
            }
        }
        //判断是否删除
        for(YsInventoryClass ysInventoryClass : ysInventoryClasses) {
            String ysCode = ysInventoryClass.getCode();
            boolean isExist = inventoryClasses.stream().filter(e -> ysCode.equals(e.getCode())).findAny().isPresent();
            if(!isExist) {//不存在则添加
                removeInventoryClasses.add(ysInventoryClass);
            }
        }
        //执行数据库操作
        ysInventoryClassService.sync(newInventoryClasses, updateInventoryClasses, removeInventoryClasses);
    }

    private String parseParentId(String treeId) {
        String parentId = null;
        switch (treeId.length()) {
            case 3://3位id则为1级菜单，无父菜单
                parentId = "0";
                break;
            case 5://5位id则为2级菜单，截取前3位为父菜单代码
                parentId = treeId.substring(0, 3);
                break;
            case 8:
                parentId = treeId.substring(0, 5);
                break;
            case 11:
                parentId = treeId.substring(0, 8);
                break;
            case 14:
                parentId = treeId.substring(0, 11);
                break;
            case 17:
                parentId = treeId.substring(0, 14);
                break;
            case 20://最深到7级菜单
                parentId = treeId.substring(0, 17);
                break;
            default:
                log.error("treeId 长度超出范围 ={}", treeId);
                break;

        }
        return parentId;
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
                        //更新暂时只有name
                        ysUnit.setName(unit.getName()).setEditDate(unit.getEditDate());
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

    @Scheduled(fixedDelay = 6000 * 1000)  //1分钟同步一次
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
                        //更新暂时只有name
                        ysSize.setName(size.getName())
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

    @Scheduled(fixedDelay = 6000 * 1000)  //1分钟同步一次
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
                        //更新暂时只有name
                        ysColor.setName(color.getName()).setEditDate(color.getEditDate());
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
