package com.wq.mybatis.utils;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuqingvika on 2018/7/1.
 */
public class GeneratorDisplay {

    public void generator() throws Exception{

        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        //指定 逆向工程配置文件
        //String filePath1 = this.getClass().getClassLoader().getResource("generatorConfig.xml").getPath();
        String filepath2 = System.getProperty("user.dir")+"/mybatis-generateConfig/generatorConfig.xml";
        File configFile = new File(filepath2);//new File("generatorConfig.xml"); 这样指定拿不到文件
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config,
                callback, warnings);
        myBatisGenerator.generate(null);

    }

    public static void main(String[] args) throws Exception {
        try {
            GeneratorDisplay generatorSqlmap = new GeneratorDisplay();
            generatorSqlmap.generator();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
