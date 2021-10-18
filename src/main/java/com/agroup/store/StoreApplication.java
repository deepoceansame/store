package com.agroup.store;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

//@componentScan("com.agroup")
@MapperScan("com.agroup.store.mapper")
@SpringBootApplication
public class StoreApplication {
    private static final Logger LOG = LoggerFactory.getLogger(StoreApplication.class);
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(StoreApplication.class);
        Environment env = app.run(args).getEnvironment();
        LOG.info("启动成功!!");
        LOG.info("地址：\thttp://127.0.0.1:{}", env.getProperty("server.port"));
    }

}
