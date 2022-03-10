package com.honji;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.honji.entity.*;
import com.honji.entity.dto.PurchaseOrderListDTO;
import com.honji.service.*;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
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

    @Autowired
    private IProviderClassService providerClassService;
    @Autowired
    private IYsProviderClassService ysProviderClassService;

    @Autowired
    private IProviderService providerService;
    @Autowired
    private IYsProviderService ysProviderService;

    @Autowired
    private IPurchaseOrderService purchaseOrderService;
    @Autowired
    private IYsPurchaseOrderService ysPurchaseOrderService;

    @Autowired
    private IPurchaseOrderListService purchaseOrderListService;
    @Autowired
    private IYsPurchaseOrderListService ysPurchaseOrderListService;


    public static void main(String[] args) {
        SpringApplication.run(SynchApplication.class, args);

    }

    //@Scheduled(fixedDelay = 6000 * 1000)
    public void syncPurchaseOrderList() {
        List<PurchaseOrderList> purchaseOrders = purchaseOrderListService.selectList();
        purchaseOrders = purchaseOrders.subList(0, 1);//测试一条
        List<YsPurchaseOrderList> ysPurchaseOrderLists = ysPurchaseOrderListService.selectAll();
        List<YsPurchaseOrderList> newPurchaseOrderLists = new ArrayList<>();
        List<YsPurchaseOrderList> updatePurchaseOrderLists = new ArrayList<>();
        List<YsPurchaseOrderList> removePurchaseOrderLists = new ArrayList<>();
        List<YsInventory> ysInventories = ysInventoryService.selectAll();
        List<YsPurchaseOrder> ysPurchaseOrders = ysPurchaseOrderService.selectAll();
        for(PurchaseOrderList purchaseOrder : purchaseOrders) {
            boolean isExist = false;
            String id = purchaseOrder.getId();

            for (YsPurchaseOrderList ysPurchaseOrderList : ysPurchaseOrderLists) {
                String ysId = ysPurchaseOrderList.getIdb();
                if (id.equals(ysId)) {
                    isExist = true;
                    //如果编辑日期不相等则有更新,代码相同其它属性有修改
                    if (!purchaseOrder.getEditDate().isEqual(ysPurchaseOrderList.getEditDate())) {

                        ysPurchaseOrderList.setParentId(purchaseOrder.getParentId())
                                .setQuantity(purchaseOrder.getQuantity()).setPrice(purchaseOrder.getPrice())
                                .setProjectCode(purchaseOrder.getProjectCode()).setAcceptDate(purchaseOrder.getAcceptDate())
                                .setEditDate(purchaseOrder.getEditDate());
                        updatePurchaseOrderLists.add(ysPurchaseOrderList);
                    }
                    break;//找到后无需再循环
                }
            }
            if (!isExist) {//不存在则添加
                YsPurchaseOrderList newPurchaseOrderList = new YsPurchaseOrderList().setIdb(purchaseOrder.getId())
                        //.setParentId(purchaseOrder.getParentId())
                        .setQuantity(purchaseOrder.getQuantity()).setPrice(purchaseOrder.getPrice())
                        .setAmount(purchaseOrder.getAmount())
                        .setProjectCode(purchaseOrder.getProjectCode()).setAcceptDate(purchaseOrder.getAcceptDate())
                        .setEditDate(purchaseOrder.getEditDate());

                String code = purchaseOrder.getInventoryId();
                YsInventory ysInventory = ysInventories.stream().filter(e -> code.equals(e.getCode())).findAny().get();

                String parentCode = purchaseOrder.getParentId();
                log.warn("parentCode = {}", parentCode);
                YsPurchaseOrder order = ysPurchaseOrders.stream().filter(e -> parentCode.equals(e.getCode())).findAny().get();
                newPurchaseOrderList.setInventoryId(ysInventory.getId());
                newPurchaseOrderList.setParentId(order.getId());
                newPurchaseOrderLists.add(newPurchaseOrderList);
            }
        }
        //判断是否删除
        for(YsPurchaseOrderList ysPurchaseOrderList : ysPurchaseOrderLists) {
            String ysId = ysPurchaseOrderList.getIdb();
            boolean isExist = purchaseOrders.stream().filter(e -> ysId.equals(e.getId())).findAny().isPresent();
            if(!isExist) {//不存在则添加
                removePurchaseOrderLists.add(ysPurchaseOrderList);
            }
        }
        //执行数据库操作
        ysPurchaseOrderListService.sync(newPurchaseOrderLists, updatePurchaseOrderLists, removePurchaseOrderLists);
    }

    @Scheduled(fixedDelay = 6000 * 1000)
    public void syncPurchaseOrder() {
        QueryWrapper<PurchaseOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("editdate", "2022-03-01");
        List<PurchaseOrder> purchaseOrders = purchaseOrderService.list(queryWrapper);
//        purchaseOrders = purchaseOrders.subList(0, 1);//测试一条
//        purchaseOrders = purchaseOrders.stream()
//                .filter(e->"PO2203-0024".equals(e.getCode()) || "PO2203-0111".equals(e.getCode()))
//                .collect(Collectors.toList());
        List<YsPurchaseOrder> ysPurchaseOrders = ysPurchaseOrderService.selectAll();
        List<YsPurchaseOrder> newPurchaseOrders = new ArrayList<>();
        List<YsPurchaseOrderList> newPurchaseOrderLists = new ArrayList<>();
        List<YsPurchaseOrder> updatePurchaseOrders = new ArrayList<>();
        List<YsPurchaseOrder> removePurchaseOrders = new ArrayList<>();
        List<YsProvider> providers = ysProviderService.selectAll();
        List<YsInventory> ysInventories = ysInventoryService.selectAll();
        for(PurchaseOrder purchaseOrder : purchaseOrders) {
            boolean isExist = false;
            String code = purchaseOrder.getCode();
            for (YsPurchaseOrder ysPurchaseOrder : ysPurchaseOrders) {
                String ysCode = ysPurchaseOrder.getCode();
                if (code.equals(ysCode)) {
                    isExist = true;
                    LocalDateTime createDate = purchaseOrder.getCreateDate();
                    //创建日期为Null属于撤回提交，只修改为未审核状态即可，等提交后再同步相关修改
                    if(createDate == null) {
                        //如是已审核则修改为未审核状态
                        if (ysPurchaseOrder.getStatus().equals("1")) {
                            ysPurchaseOrder.setStatus("0");
                            updatePurchaseOrders.add(ysPurchaseOrder);
                        }
                        //TODO 已经撤回mes应控制不再对此间操作
                        break;
                    }

                    //如果编辑日期或者提交日期不相等则有更新
                    if (!purchaseOrder.getEditDate().isEqual(ysPurchaseOrder.getEditDate())
                            || !createDate.isEqual(ysPurchaseOrder.getCreateDate())) {
                        order2YsOrder(purchaseOrder, ysPurchaseOrder, providers);
                        updatePurchaseOrders.add(ysPurchaseOrder);
                        //主表提交日期有变更，则要更新关联子表记录，更新操作是先删除旧子表记录，这里加上新记录
                        newPurchaseOrderLists.addAll(
                            parseYsPurchaseOrderList(purchaseOrder.getCode(), ysInventories, ysPurchaseOrders)
                        );
                    }
                    break;//找到后无需再循环
                }
            }
            if (!isExist) {//不存在则添加
                if (purchaseOrder.getCreateDate() != null) {//没有提交时间，mes也没有此记录，无须同步
                    YsPurchaseOrder newPurchaseOrder = new YsPurchaseOrder();
                    //id设置为单据方便了表关联
                    newPurchaseOrder.setId(purchaseOrder.getCode());
                    order2YsOrder(purchaseOrder, newPurchaseOrder, providers);
                    newPurchaseOrders.add(newPurchaseOrder);
                    //主表有新记录，则要添加关联子表记录
                    newPurchaseOrderLists.addAll(
                            parseYsPurchaseOrderList(purchaseOrder.getCode(), ysInventories, ysPurchaseOrders)
                    );
                }
            }
        }
        //判断是否删除
        for(YsPurchaseOrder ysPurchaseOrder : ysPurchaseOrders) {
            String ysCode = ysPurchaseOrder.getCode();
            boolean isExist = purchaseOrders.stream().filter(e -> ysCode.equals(e.getCode())).findAny().isPresent();
            if(!isExist) {//不存在则添加
                removePurchaseOrders.add(ysPurchaseOrder);
            }
        }
        //执行数据库操作
        ysPurchaseOrderService.sync(newPurchaseOrders, newPurchaseOrderLists,
                updatePurchaseOrders, removePurchaseOrders);
    }

//    @Scheduled(fixedDelay = 6000 * 1000)
    public void syncProvider() {
        List<Provider> providers = providerService.list();
        List<YsProvider> ysProviders = ysProviderService.selectAll();
        List<YsProvider> newProviders = new ArrayList<>();
        List<YsProvider> updateProviders = new ArrayList<>();
        List<YsProvider> removeProviders = new ArrayList<>();
        for(Provider provider : providers) {
            boolean isExist = false;
            //rose查询数据后面有空格，必须trim
            String code = provider.getCode().trim();
            for (YsProvider ysProvider : ysProviders) {
                String ysCode = ysProvider.getCode();
                if (code.equals(ysCode)) {
                    isExist = true;
                    //如果编辑日期不相等则有更新,代码相同其它属性有修改
                    if (!provider.getEditDate().isEqual(ysProvider.getEditDate())) {
                        //更新暂时只有name，editDate
                        ysProvider.setName(provider.getName()).setShortName(provider.getShortName())
                                .setParentCode(provider.getParentCode()).setEditDate(provider.getEditDate());
                        updateProviders.add(ysProvider);
                    }
                    break;//找到后无需再循环
                }
            }
            if (!isExist) {//不存在则添加
                YsProvider newProvider = new YsProvider().setCode(code)
                        .setName(provider.getName()).setShortName(provider.getShortName())
                        .setParentCode(provider.getParentCode())
                        .setEditDate(provider.getEditDate());

                newProviders.add(newProvider);
            }
        }
        //判断是否删除
        for(YsProvider ysProvider : ysProviders) {
            String ysCode = ysProvider.getCode();
            //rose查询数据后面有空格，必须trim
            boolean isExist = providers.stream().filter(e -> ysCode.equals(e.getCode().trim())).findAny().isPresent();
            if(!isExist) {//不存在则添加
                removeProviders.add(ysProvider);
            }
        }
        //执行数据库操作
        ysProviderService.sync(newProviders, updateProviders, removeProviders);
    }

//    @Scheduled(fixedDelay = 6000 * 1000)
    public void syncProviderClass() {
        List<ProviderClass> providerClasses = providerClassService.list();
        List<YsProviderClass> ysProviderClasses = ysProviderClassService.selectAll();
        List<YsProviderClass> newProviderClasses = new ArrayList<>();
        List<YsProviderClass> updateProviderClasses = new ArrayList<>();
        List<YsProviderClass> removeProviderClasses = new ArrayList<>();
        for(ProviderClass providerClass : providerClasses) {
            boolean isExist = false;
            String code = providerClass.getCode();
            for (YsProviderClass ysProviderClass : ysProviderClasses) {
                String ysCode = ysProviderClass.getCode();
                if (code.equals(ysCode)) {
                    isExist = true;
                    //如果编辑日期不相等则有更新,代码相同其它属性有修改
                    if (!providerClass.getEditDate().isEqual(ysProviderClass.getEditDate())) {
                        //更新暂时只有name，editDate
                        ysProviderClass.setName(providerClass.getName())
                                .setEditDate(providerClass.getEditDate());
                        updateProviderClasses.add(ysProviderClass);
                    }
                    break;//找到后无需再循环
                }
            }
            if (!isExist) {//不存在则添加
                YsProviderClass newProviderClass = new YsProviderClass().setCode(code)
                        .setName(providerClass.getName())
                        .setEditDate(providerClass.getEditDate());

                newProviderClasses.add(newProviderClass);
            }
        }
        //判断是否删除
        for(YsProviderClass ysProviderClass : ysProviderClasses) {
            String ysCode = ysProviderClass.getCode();
            boolean isExist = providerClasses.stream().filter(e -> ysCode.equals(e.getCode())).findAny().isPresent();
            if(!isExist) {//不存在则添加
                removeProviderClasses.add(ysProviderClass);
            }
        }
        //执行数据库操作
        ysProviderClassService.sync(newProviderClasses, updateProviderClasses, removeProviderClasses);
    }

//    @Scheduled(fixedDelay = 6000 * 1000)  //1分钟同步一次
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
                        inventory2YsInventory(inventory, ysInventory, ysUnits, inventoryes);
                        updateInventoryes.add(ysInventory);
                    }
                    break;//找到后无需再循环
                }
            }
            if (!isExist) {//不存在则添加
                YsInventory newInventory = new YsInventory().setCode(code);
                inventory2YsInventory(inventory, newInventory, ysUnits, inventoryes);
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

//    @Scheduled(fixedDelay = 6000 * 1000)
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
                if (code.startsWith("K")) {//款式
                    newInventoryClass.setType("1");
                } else if (code.startsWith("M")) {//款式
                    if (code.startsWith("M1")) {//主料
                        newInventoryClass.setType("2");
                    }
                    if (code.startsWith("M2")) {//辅料
                        newInventoryClass.setType("3");
                    }
                }

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

    private void inventory2YsInventory(Inventory inventory, YsInventory ysInventory,
                                       List<YsUnit> ysUnits, List<Inventory> inventoryes) {
        ysInventory.setName(inventory.getName()).setParentCode(inventory.getParentCode())
                .setEditDate(inventory.getEditDate());
//        String treeId = inventory.getTreeId();
//        String parentId = parseParentId(treeId);
        String unitCode = inventory.getUnitId();
        YsUnit unit = ysUnits.stream().filter(e -> unitCode.equals(e.getCode())).findAny().get();
        ysInventory.setUnitId(unit.getId());
        if ("启用".equals(inventory.getStatus())) {
            ysInventory.setStatus("0");
        } else {
            ysInventory.setStatus("2");
        }

//        if (parentId.equals("0")) {//空指针直接报错
//            ysInventory.setParentCode("0");//直接设置，不用查询0父菜单
//        } else {
//            for (Inventory ic : inventoryes) {
//                if (parentId.equals(ic.getTreeId())) {
//                    ysInventory.setParentCode(ic.getCode());
//                    break;
//                }
//            }
//        }
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

//    @Scheduled(fixedDelay = 6000 * 1000)  //1分钟同步一次
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

//    @Scheduled(fixedDelay = 6000 * 1000)  //1分钟同步一次
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

//    @Scheduled(fixedDelay = 6000 * 1000)  //1分钟同步一次
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

    private void order2YsOrder(PurchaseOrder purchaseOrder, YsPurchaseOrder ysPurchaseOrder, List<YsProvider> providers) {
        ysPurchaseOrder.setCode(purchaseOrder.getCode())
                .setDate(purchaseOrder.getDate()).setAuditDate(purchaseOrder.getAuditDate())
                .setWareHouse("1491700404601700352")//TODO 固定仓库id
                .setCreateBy(purchaseOrder.getCreateBy()).setCreateDate(purchaseOrder.getCreateDate())
                .setUpdateBy(purchaseOrder.getCreateBy()).setUpdateDate(purchaseOrder.getCreateDate())
                .setEditDate(purchaseOrder.getEditDate());
        String status = purchaseOrder.getStatus();
        if("已审核".equals(status)) {
            ysPurchaseOrder.setStatus("1");
        } else {
            ysPurchaseOrder.setStatus("0");
        }
        final String providerCode = purchaseOrder.getProviderCode();
        YsProvider provider = providers.stream().filter(e -> providerCode.equals(e.getCode())).findAny().get();
        ysPurchaseOrder.setPartnerId(provider.getId());

    }

    private List<YsPurchaseOrderList> parseYsPurchaseOrderList(String parentId, List<YsInventory> ysInventories,
                                                               List<YsPurchaseOrder> ysPurchaseOrders) {
        List<YsPurchaseOrderList> newPurchaseOrderLists = new ArrayList<>();
        QueryWrapper<PurchaseOrderList> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("poid", parentId);
        List<PurchaseOrderListDTO> orderLists =  purchaseOrderListService.selectListByParentCode(parentId);
        for(PurchaseOrderListDTO purchaseOrder : orderLists) {
            YsPurchaseOrderList newPurchaseOrderList = new YsPurchaseOrderList().setIdb(purchaseOrder.getId())
                    .setParentId(purchaseOrder.getParentId())
                    .setQuantity(purchaseOrder.getQuantity()).setPrice(purchaseOrder.getPrice())
                    .setAmount(purchaseOrder.getAmount())
                    .setProjectCode(purchaseOrder.getProjectCode()).setAcceptDate(purchaseOrder.getAcceptDate())
                    .setEditDate(purchaseOrder.getEditDate());

            String code = purchaseOrder.getInventoryId();
            log.warn("InventoryId = {}", code);
            YsInventory ysInventory = ysInventories.stream().filter(e -> code.equals(e.getCode())).findAny().get();

//            String parentCode = purchaseOrder.getParentId();
//            YsPurchaseOrder order = ysPurchaseOrders.stream().filter(e -> parentCode.equals(e.getCode())).findAny().get();
            newPurchaseOrderList.setInventoryId(ysInventory.getId());
//            newPurchaseOrderList.setParentId(order.getId());
            newPurchaseOrderLists.add(newPurchaseOrderList);
        }
        return newPurchaseOrderLists;
    }
}
