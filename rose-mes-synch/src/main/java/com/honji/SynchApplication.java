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

    @Autowired
    private IWarehouseService warehouseService;
    @Autowired
    private IYsWarehouseService ysWarehouseService;


    public static void main(String[] args) {
        SpringApplication.run(SynchApplication.class, args);

    }

    //@Scheduled(fixedDelay = 6000 * 1000)
    public void syncPurchaseOrderList() {
        List<PurchaseOrderList> purchaseOrders = purchaseOrderListService.selectList();
        purchaseOrders = purchaseOrders.subList(0, 1);//????????????
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
                    //???????????????????????????????????????,?????????????????????????????????
                    if (!purchaseOrder.getEditDate().isEqual(ysPurchaseOrderList.getEditDate())) {

                        ysPurchaseOrderList.setParentId(purchaseOrder.getParentId())
                                .setQuantity(purchaseOrder.getQuantity()).setPrice(purchaseOrder.getPrice())
                                .setProjectCode(purchaseOrder.getProjectCode()).setAcceptDate(purchaseOrder.getAcceptDate())
                                .setEditDate(purchaseOrder.getEditDate());
                        updatePurchaseOrderLists.add(ysPurchaseOrderList);
                    }
                    break;//????????????????????????
                }
            }
            if (!isExist) {//??????????????????
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
        //??????????????????
        for(YsPurchaseOrderList ysPurchaseOrderList : ysPurchaseOrderLists) {
            String ysId = ysPurchaseOrderList.getIdb();
            boolean isExist = purchaseOrders.stream().filter(e -> ysId.equals(e.getId())).findAny().isPresent();
            if(!isExist) {//??????????????????
                removePurchaseOrderLists.add(ysPurchaseOrderList);
            }
        }
        //?????????????????????
        ysPurchaseOrderListService.sync(newPurchaseOrderLists, updatePurchaseOrderLists, removePurchaseOrderLists);
    }

    @Scheduled(fixedDelay = 6000 * 1000)
    public void syncPurchaseOrder() {
        QueryWrapper<PurchaseOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("editdate", "2022-03-01");
        List<PurchaseOrder> purchaseOrders = purchaseOrderService.list(queryWrapper);
//        purchaseOrders = purchaseOrders.subList(0, 1);//????????????
//        purchaseOrders = purchaseOrders.stream()
//                .filter(e->"PO2203-0024".equals(e.getCode()) || "PO2203-0111".equals(e.getCode()))
//                .collect(Collectors.toList());
        List<YsPurchaseOrder> ysPurchaseOrders = ysPurchaseOrderService.selectAll();
        List<YsPurchaseOrder> newPurchaseOrders = new ArrayList<>();
        List<YsPurchaseOrderList> newPurchaseOrderLists = new ArrayList<>();
        List<YsPurchaseOrder> updatePurchaseOrders = new ArrayList<>();
        List<YsPurchaseOrder> removePurchaseOrders = new ArrayList<>();
//        List<YsProvider> providers = ysProviderService.selectAll();
        List<YsInventory> ysInventories = ysInventoryService.selectAll();
        for(PurchaseOrder purchaseOrder : purchaseOrders) {
            boolean isExist = false;
            String code = purchaseOrder.getCode();
            for (YsPurchaseOrder ysPurchaseOrder : ysPurchaseOrders) {
                String ysCode = ysPurchaseOrder.getCode();
                if (code.equals(ysCode)) {
                    isExist = true;
                    LocalDateTime createDate = purchaseOrder.getCreateDate();
                    //???????????????Null??????????????????????????????????????????????????????????????????????????????????????????
                    if(createDate == null) {
                        //??????????????????????????????????????????
                        if (ysPurchaseOrder.getStatus().equals("1")) {
                            ysPurchaseOrder.setStatus("0");
                            updatePurchaseOrders.add(ysPurchaseOrder);
                        }
                        //TODO ????????????mes??????????????????????????????
                        break;
                    }

                    //?????????????????????????????????????????????????????????
                    if (!purchaseOrder.getEditDate().isEqual(ysPurchaseOrder.getEditDate())
                            || !createDate.isEqual(ysPurchaseOrder.getCreateDate())) {
                        order2YsOrder(purchaseOrder, ysPurchaseOrder);
                        updatePurchaseOrders.add(ysPurchaseOrder);
                        //??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                        newPurchaseOrderLists.addAll(
                            parseYsPurchaseOrderList(purchaseOrder.getCode(), ysInventories)
                        );
                    }
                    break;//????????????????????????
                }
            }
            if (!isExist) {//??????????????????
                if (purchaseOrder.getCreateDate() != null) {//?????????????????????mes?????????????????????????????????
                    YsPurchaseOrder newPurchaseOrder = new YsPurchaseOrder();
                    //id?????????????????????????????????
                    newPurchaseOrder.setId(purchaseOrder.getCode());
                    order2YsOrder(purchaseOrder, newPurchaseOrder);
                    newPurchaseOrders.add(newPurchaseOrder);
                    //???????????????????????????????????????????????????
                    newPurchaseOrderLists.addAll(
                            parseYsPurchaseOrderList(purchaseOrder.getCode(), ysInventories)
                    );
                }
            }
        }
        //??????????????????
        for(YsPurchaseOrder ysPurchaseOrder : ysPurchaseOrders) {
            String ysCode = ysPurchaseOrder.getCode();
            boolean isExist = purchaseOrders.stream().filter(e -> ysCode.equals(e.getCode())).findAny().isPresent();
            if(!isExist) {//??????????????????
                removePurchaseOrders.add(ysPurchaseOrder);
            }
        }
        //?????????????????????
        ysPurchaseOrderService.sync(newPurchaseOrders, newPurchaseOrderLists,
                updatePurchaseOrders, removePurchaseOrders);
    }


    //@Scheduled(fixedDelay = 6000 * 1000)
    public void syncWarehouse() {
        QueryWrapper<Warehouse> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("locateid", "1");//????????????
        List<Warehouse> warehouses = warehouseService.list(queryWrapper);
        //?????????????????????
        ysWarehouseService.sync(warehouses);
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
            //rose????????????????????????????????????trim
            String code = provider.getCode().trim();
            for (YsProvider ysProvider : ysProviders) {
                String ysCode = ysProvider.getCode();
                if (code.equals(ysCode)) {
                    isExist = true;
                    //???????????????????????????????????????,?????????????????????????????????
                    if (!provider.getEditDate().isEqual(ysProvider.getEditDate())) {
                        //??????????????????name???editDate
                        ysProvider.setName(provider.getName()).setShortName(provider.getShortName())
                                .setParentCode(provider.getParentCode()).setEditDate(provider.getEditDate());
                        updateProviders.add(ysProvider);
                    }
                    break;//????????????????????????
                }
            }
            if (!isExist) {//??????????????????
                YsProvider newProvider = new YsProvider().setId(code).setCode(code)
                        .setName(provider.getName()).setShortName(provider.getShortName())
                        .setParentCode(provider.getParentCode())
                        .setEditDate(provider.getEditDate());

                newProviders.add(newProvider);
            }
        }
        //??????????????????
        for(YsProvider ysProvider : ysProviders) {
            String ysCode = ysProvider.getCode();
            //rose????????????????????????????????????trim
            boolean isExist = providers.stream().filter(e -> ysCode.equals(e.getCode().trim())).findAny().isPresent();
            if(!isExist) {//??????????????????
                removeProviders.add(ysProvider);
            }
        }
        //?????????????????????
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
                    //???????????????????????????????????????,?????????????????????????????????
                    if (!providerClass.getEditDate().isEqual(ysProviderClass.getEditDate())) {
                        //??????????????????name???editDate
                        ysProviderClass.setName(providerClass.getName())
                                .setEditDate(providerClass.getEditDate());
                        updateProviderClasses.add(ysProviderClass);
                    }
                    break;//????????????????????????
                }
            }
            if (!isExist) {//??????????????????
                YsProviderClass newProviderClass = new YsProviderClass().setCode(code)
                        .setName(providerClass.getName())
                        .setEditDate(providerClass.getEditDate());

                newProviderClasses.add(newProviderClass);
            }
        }
        //??????????????????
        for(YsProviderClass ysProviderClass : ysProviderClasses) {
            String ysCode = ysProviderClass.getCode();
            boolean isExist = providerClasses.stream().filter(e -> ysCode.equals(e.getCode())).findAny().isPresent();
            if(!isExist) {//??????????????????
                removeProviderClasses.add(ysProviderClass);
            }
        }
        //?????????????????????
        ysProviderClassService.sync(newProviderClasses, updateProviderClasses, removeProviderClasses);
    }

//    @Scheduled(fixedDelay = 6000 * 1000)  //1??????????????????
    public void syncInventory() {
        List<Inventory> inventoryes = inventoryService.list();
        List<YsInventory> ysInventoryes = ysInventoryService.selectAll();
//        List<YsUnit> ysUnits = ysUnitService.selectAll();
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
                    //???????????????????????????????????????,?????????????????????????????????
                    if (!inventory.getEditDate().isEqual(ysInventory.getEditDate())) {
                        inventory2YsInventory(inventory, ysInventory);
                        updateInventoryes.add(ysInventory);
                    }
                    break;//????????????????????????
                }
            }
            if (!isExist) {//??????????????????
                YsInventory newInventory = new YsInventory().setId(code).setCode(code);
                inventory2YsInventory(inventory, newInventory);
                newInventoryes.add(newInventory);
            }
        }
        //??????????????????
        for(YsInventory ysInventory : ysInventoryes) {
            String ysCode = ysInventory.getCode();
            boolean isExist = inventoryes.stream().filter(e -> ysCode.equals(e.getCode())).findAny().isPresent();
            if(!isExist) {//??????????????????
                removeInventoryes.add(ysInventory);
            }
        }
        //?????????????????????
        ysInventoryService.sync(newInventoryes, updateInventoryes, removeInventoryes);
    }

//    @Scheduled(fixedDelay = 6000 * 1000)
    public void syncInventoryClass() {
        QueryWrapper<InventoryClass> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("seriesid", "1");//??????????????????????????????
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
                    //???????????????????????????????????????,?????????????????????????????????
                    if (!inventoryClass.getEditDate().isEqual(ysInventoryClass.getEditDate())) {
                        //??????????????????name???editDate
                        ysInventoryClass.setName(inventoryClass.getName())
                                .setEditDate(inventoryClass.getEditDate());
                        updateInventoryClasses.add(ysInventoryClass);
                    }
                    break;//????????????????????????
                }
            }
            if (!isExist) {//??????????????????
                YsInventoryClass newInventoryClass = new YsInventoryClass().setCode(code)
                        .setName(inventoryClass.getName()).setEditDate(inventoryClass.getEditDate());
                String treeId = inventoryClass.getTreeId();
                String parentId = parseParentId(treeId);
                if (code.startsWith("K")) {//??????
                    newInventoryClass.setType("1");
                } else if (code.startsWith("M")) {//??????
                    if (code.startsWith("M1")) {//??????
                        newInventoryClass.setType("2");
                    }
                    if (code.startsWith("M2")) {//??????
                        newInventoryClass.setType("3");
                    }
                }

                if (parentId.equals("0")) {//?????????????????????
                    newInventoryClass.setParentCode("0");//???????????????????????????0?????????
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
        //??????????????????
        for(YsInventoryClass ysInventoryClass : ysInventoryClasses) {
            String ysCode = ysInventoryClass.getCode();
            boolean isExist = inventoryClasses.stream().filter(e -> ysCode.equals(e.getCode())).findAny().isPresent();
            if(!isExist) {//??????????????????
                removeInventoryClasses.add(ysInventoryClass);
            }
        }
        //?????????????????????
        ysInventoryClassService.sync(newInventoryClasses, updateInventoryClasses, removeInventoryClasses);
    }

    private void inventory2YsInventory(Inventory inventory, YsInventory ysInventory) {
        ysInventory.setName(inventory.getName()).setParentCode(inventory.getParentCode())
                .setUnitId(inventory.getUnitId()).setEditDate(inventory.getEditDate());
        if ("??????".equals(inventory.getStatus())) {
            ysInventory.setStatus("0");
        } else {
            ysInventory.setStatus("2");
        }

    }

    private String parseParentId(String treeId) {
        String parentId = null;
        switch (treeId.length()) {
            case 3://3???id??????1????????????????????????
                parentId = "0";
                break;
            case 5://5???id??????2?????????????????????3?????????????????????
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
            case 20://?????????7?????????
                parentId = treeId.substring(0, 17);
                break;
            default:
                log.error("treeId ?????????????????? ={}", treeId);
                break;

        }
        return parentId;
    }

//    @Scheduled(fixedDelay = 6000 * 1000)  //1??????????????????
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
                    //???????????????????????????????????????
                    if (!unit.getEditDate().isEqual(ysUnit.getEditDate())) {
                        //??????????????????name
                        ysUnit.setName(unit.getName()).setEditDate(unit.getEditDate());
                        updateUnits.add(ysUnit);
                    }
                    break;//????????????????????????
                }
            }
            if(!isExist) {//??????????????????
                YsUnit newUnit = new YsUnit().setId(code).setCode(code)
                        .setName(unit.getName()).setEditDate(unit.getEditDate());
                newUnits.add(newUnit);
            }
        }

        //??????????????????
        for(YsUnit ysUnit : ysUnits) {
            String ysCode = ysUnit.getCode();
            boolean isExist = units.stream().filter(e -> ysCode.equals(e.getCode())).findAny().isPresent();
            if(!isExist) {//??????????????????
                removeUnits.add(ysUnit);
            }
        }
        //?????????????????????
        ysUnitService.sync(newUnits, updateUnits, removeUnits);

    }

//    @Scheduled(fixedDelay = 6000 * 1000)  //1??????????????????
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
                    //???????????????????????????????????????
                    if (!size.getEditDate().isEqual(ysSize.getEditDate())) {
                        //??????????????????name
                        ysSize.setName(size.getName())
                                .setEditDate(size.getEditDate());
                        updateSizes.add(ysSize);
                    }
                    break;//????????????????????????
                }
            }
            if(!isExist) {//??????????????????
                YsSize newSize = new YsSize().setCode(code)
                        .setName(size.getName()).setEditDate(size.getEditDate());
                newSizes.add(newSize);
            }
        }

        //??????????????????
        for(YsSize ysSize : ysSizes) {
            String ysCode = ysSize.getCode();
            boolean isExist = sizes.stream().filter(e -> ysCode.equals(e.getCode())).findAny().isPresent();
            if(!isExist) {//??????????????????
                removeSizes.add(ysSize);
            }
        }
        //?????????????????????
        ysSizeService.sync(newSizes, updateSizes, removeSizes);

    }

//    @Scheduled(fixedDelay = 6000 * 1000)  //1??????????????????
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
                    //???????????????????????????????????????
                    if (!color.getEditDate().isEqual(ysColor.getEditDate())) {
                        //??????????????????name
                        ysColor.setName(color.getName()).setEditDate(color.getEditDate());
                        updateColors.add(ysColor);
                    }
                    break;//????????????????????????
                }
            }
            if(!isExist) {//??????????????????
                YsColor newColor = new YsColor().setId(code).setCode(code)
                        .setName(color.getName()).setEditDate(color.getEditDate());
                newColors.add(newColor);
            }
        }

        //??????????????????
        for(YsColor ysColor : ysColors) {
            String ysCode = ysColor.getCode();
            boolean isExist = colors.stream().filter(e -> ysCode.equals(e.getCode())).findAny().isPresent();
            if(!isExist) {//??????????????????
                removeColors.add(ysColor);
            }
        }
        //?????????????????????
        ysColorService.sync(newColors, updateColors, removeColors);

    }

    private void order2YsOrder(PurchaseOrder purchaseOrder, YsPurchaseOrder ysPurchaseOrder) {
        ysPurchaseOrder.setCode(purchaseOrder.getCode()).setPartnerId(purchaseOrder.getProviderCode())
                .setDate(purchaseOrder.getDate()).setAuditDate(purchaseOrder.getAuditDate())
                .setWareHouse(purchaseOrder.getWareHouse())
                .setCreateBy(purchaseOrder.getCreateBy()).setCreateDate(purchaseOrder.getCreateDate())
                .setUpdateBy(purchaseOrder.getCreateBy()).setUpdateDate(purchaseOrder.getCreateDate())
                .setEditDate(purchaseOrder.getEditDate());
        String status = purchaseOrder.getStatus();
        if("?????????".equals(status)) {
            ysPurchaseOrder.setStatus("1");
        } else {
            ysPurchaseOrder.setStatus("0");
        }
//        final String providerCode = purchaseOrder.getProviderCode();
//        YsProvider provider = providers.stream().filter(e -> providerCode.equals(e.getCode())).findAny().get();
//        ysPurchaseOrder.setPartnerId(provider.getId());

    }

    private List<YsPurchaseOrderList> parseYsPurchaseOrderList(String parentId, List<YsInventory> ysInventories) {
        List<YsPurchaseOrderList> newPurchaseOrderLists = new ArrayList<>();
        List<PurchaseOrderListDTO> orderLists =  purchaseOrderListService.selectListByParentCode(parentId);
        for(PurchaseOrderListDTO purchaseOrder : orderLists) {
            YsPurchaseOrderList newPurchaseOrderList = new YsPurchaseOrderList().setIdb(purchaseOrder.getId())
                    .setParentId(purchaseOrder.getParentId())
                    .setQuantity(purchaseOrder.getQuantity()).setPrice(purchaseOrder.getPrice())
                    .setAmount(purchaseOrder.getAmount())
                    .setColor(purchaseOrder.getColor()).setBatchNumber(purchaseOrder.getSize())
                    .setProjectCode(purchaseOrder.getProjectCode()).setAcceptDate(purchaseOrder.getAcceptDate())
                    .setEditDate(purchaseOrder.getEditDate());

            String code = purchaseOrder.getInventoryId();
            log.warn("InventoryId = {}", code);
            YsInventory ysInventory = ysInventories.stream().filter(e -> code.equals(e.getCode())).findAny().get();

            newPurchaseOrderList.setInventoryId(ysInventory.getId());
            newPurchaseOrderLists.add(newPurchaseOrderList);
        }
        return newPurchaseOrderLists;
    }
}
