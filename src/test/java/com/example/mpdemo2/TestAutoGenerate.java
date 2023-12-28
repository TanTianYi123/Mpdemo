package com.example.mpdemo2;/**
 * @description:
 * @author: Administrator
 * @time: 2023/12/22 0022 14:56
 */


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.junit.jupiter.api.Test;

/**
 * @description:
 * @author: Administrator
 * @time: 2023/12/22 0022 14:56
 */
public class TestAutoGenerate {
    @Test
    public void autoGenerate(){
        //1、代码生产器
        AutoGenerator mpg = new AutoGenerator();
        //2、全局配置
        GlobalConfig gc = new GlobalConfig();
        //代码生成目录（需要修改）
        String projectPath = "D:\\Soft\\Mpdemo2";
        //代码最终输出目录
        gc.setOutputDir(projectPath+"/src/main/java");
        //配置开发者名称
        gc.setAuthor("tty");
        //配置是否打开目录
        gc.setOpen(false);

        gc.setSwagger2(false);
        //重新生成文件是否覆盖
        gc.setFileOverride(true);
        //配置主键生成策略
        gc.setIdType(IdType.ASSIGN_ID);
        //配置日期类型
        gc.setDateType(DateType.ONLY_DATE);
        gc.setServiceName("%sService");
        mpg.setGlobalConfig(gc);

        //3、数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        //配置url地址;
        dsc.setUrl("jdbc:mysql://ttynju.e3.luyouxia.net:14329/demo?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=true");
        //指定数据库名
        dsc.setSchemaName("demo");
        //配置数据库驱动
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        //配置数据库用户名
        dsc.setUsername("root");
        dsc.setPassword("p9004644");
        mpg.setDataSource(dsc);

        //4、包配置
        PackageConfig pc = new PackageConfig();
        //父包名(要改)
        pc.setParent("com.example.mpdemo2");
        //模块名
        pc.setModuleName("base");
        //entity
        pc.setEntity("entity");
        //mapper
        pc.setMapper("mapper");
        //service
        pc.setService("service");
        //controller
        pc.setController("controller");
        mpg.setPackageInfo(pc);

        //5、策略配置(数据库表配置)
        StrategyConfig strategy = new StrategyConfig();
        //指定表名（可同时操作多张表，使用逗号隔开）
        strategy.setInclude("my_condition");
        //配置数据表与实体类名之间的映射策略
        strategy.setNaming(NamingStrategy.underline_to_camel);
        //配置数据表的字段与实体类属性名之间的映射策略
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        //配置lombook模式
        strategy.setEntityLombokModel(true);
        //配置rest风格控制器
        strategy.setRestControllerStyle(true);
        //配置驼峰转连字符
        strategy.setControllerMappingHyphenStyle(true);
        //配置表前缀，生成实体去除表前缀
        strategy.setTablePrefix(pc.getModuleName() +"_");
        mpg.setStrategy(strategy);


        //启动！
        mpg.execute();


    }
}
