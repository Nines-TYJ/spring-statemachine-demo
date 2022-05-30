package com.nines.statemachine;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author tanyujie
 * @classname StateMachineApplication
 * @description TODO
 * @date 2022/5/30 11:41
 * @since 1.0
 */
@MapperScan("com.nines.statemachine.mapper")
@SpringBootApplication
public class StateMachineApplication {

    public static void main(String[] args) {
        SpringApplication.run(StateMachineApplication.class, args);
    }

}
