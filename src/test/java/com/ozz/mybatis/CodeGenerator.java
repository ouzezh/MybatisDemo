package com.ozz.mybatis;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.apache.commons.lang3.StringUtils;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

/**
 * 自动生成代码，执行 main 方法控制台输入模块表名回车自动生成对应项目目录中
 * 
 * https://mp.baomidou.com/guide/generator.html
 */
public class CodeGenerator {

  /**
   * <p>
   * 读取控制台内容
   * </p>
   */
  public static String scanner(String tip) {
    @SuppressWarnings("resource")
    Scanner scanner = new Scanner(System.in);
    StringBuilder help = new StringBuilder();
    help.append("请输入" + tip + "：");
    System.out.println(help.toString());
    if (scanner.hasNext()) {
      String ipt = scanner.next();
      if (StringUtils.isNotEmpty(ipt)) {
        return ipt;
      }
    }
    throw new MybatisPlusException("请输入正确的" + tip + "！");
  }

  public static void main(String[] args) {
    String url =
        "jdbc:log4jdbc:mysql://jw_db:3306/api?useUnicode=true&characterEncoding=utf8&serverTimezone=Hongkong";
    String username = "api";
    String password = "p";
    String parent = "com.ozz.parent";
    String superEntityClass = "com.ozz.mybatis.model.BaseObject";
    String superControllerClass = "com.ozz.mybatis.web.BaseController";
    String author = "ozz";

    // 代码生成器
    AutoGenerator mpg = new AutoGenerator();

    // 全局配置
    GlobalConfig gc = new GlobalConfig();
    String projectPath = System.getProperty("user.dir");
    gc.setOutputDir(projectPath + "/src/main/generated");
    gc.setFileOverride(false);
    gc.setAuthor(author);
    gc.setOpen(false);
    // gc.setSwagger2(true); //实体属性 Swagger2 注解
    gc.setBaseResultMap(true);
    gc.setBaseColumnList(true);// 生成基本的SQL片段
    gc.setIdType(IdType.AUTO);
    mpg.setGlobalConfig(gc);

    // 数据源配置
    DataSourceConfig dsc = new DataSourceConfig();
    dsc.setUrl(url);
    // dsc.setSchemaName("public");
    dsc.setDriverName("net.sf.log4jdbc.DriverSpy");
    dsc.setUsername(username);
    dsc.setPassword(password);
    mpg.setDataSource(dsc);

    // 包配置
    PackageConfig pc = new PackageConfig();
    pc.setModuleName(scanner("模块名"));
    pc.setParent(parent);
//  pc.setParent(parent).setMapper("mapper").setService("service").setController("controller").setEntity("entity").setXml("mapper");
    mpg.setPackageInfo(pc);

    // 自定义配置
    InjectionConfig cfg = new InjectionConfig() {
      @Override
      public void initMap() {
        // to do nothing
      }
    };

    // 如果模板引擎是 freemarker
    String templatePath = "/templates/mapper.xml.ftl";
    // 如果模板引擎是 velocity
    // String templatePath = "/templates/mapper.xml.vm";

    // 自定义输出配置
    List<FileOutConfig> focList = new ArrayList<>();
    // 自定义配置会被优先输出
    focList.add(new FileOutConfig(templatePath) {
      @Override
      public String outputFile(TableInfo tableInfo) {
        // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
        return projectPath + "/src/main/resources/mapper/" + pc.getModuleName() + "/"
            + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
      }
    });
    /*
     * cfg.setFileCreate(new IFileCreate() {
     * 
     * @Override public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String
     * filePath) { // 判断自定义文件夹是否需要创建 checkDir("调用默认方法创建的目录"); return false; } });
     */
    cfg.setFileOutConfigList(focList);
    mpg.setCfg(cfg);

    // 策略配置
    StrategyConfig strategy = new StrategyConfig();
    strategy.setNaming(NamingStrategy.underline_to_camel);
    strategy.setColumnNaming(NamingStrategy.underline_to_camel);
    strategy.setEntityTableFieldAnnotationEnable(false);
    if(StringUtils.isNotEmpty(superEntityClass)) {
      strategy.setSuperEntityClass(superEntityClass);
    }
    strategy.setEntityLombokModel(true);
    strategy.setRestControllerStyle(true);
    // 公共父类
    if(StringUtils.isNotEmpty(superControllerClass)) {
      strategy.setSuperControllerClass(superControllerClass);
    }
    
    // 写于父类中的公共字段
    // strategy.setSuperEntityColumns("id");
    strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));
    strategy.setControllerMappingHyphenStyle(true);
    strategy.setTablePrefix(pc.getModuleName() + "_");
    mpg.setStrategy(strategy);
    mpg.setTemplateEngine(new FreemarkerTemplateEngine());
    mpg.execute();
  }

}
