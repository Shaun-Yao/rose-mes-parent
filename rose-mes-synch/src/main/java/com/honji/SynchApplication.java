package com.honji;

import com.honji.entity.Color;
import com.honji.entity.YsColor;
import com.honji.service.IColorService;
import com.honji.service.IYsColorService;
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
    private IYsColorService ysColorService;

//    @Autowired
//    Environment env;

    public static void main(String[] args) {
        SpringApplication.run(SynchApplication.class, args);

    }

//    @PostConstruct
//    void started() {
//        log.info("timezone setup");
//        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
//    }
/*

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource primaryDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));
        return  dataSource;
//        return DataSourceBuilder.create().build();
    }

    @Bean(name = "mesDatasource")
    @ConfigurationProperties(prefix = "spring.mes-datasource")
    public DataSource secondDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Primary
    public JdbcTemplate primaryJdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "mesJdbcTemplate")
    public JdbcTemplate secondJdbcTemplate(@Qualifier("mesDatasource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
*/

    @Scheduled(fixedDelay = 600 * 1000)  //1分钟同步一次
    public void syncOffLineBalance() {
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
