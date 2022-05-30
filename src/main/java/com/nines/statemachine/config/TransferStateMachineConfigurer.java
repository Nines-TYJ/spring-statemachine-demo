package com.nines.statemachine.config;
import com.nines.statemachine.entity.TransferTaskDetails;
import com.nines.statemachine.enums.TransferEventEnum;
import com.nines.statemachine.enums.TransferStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.Objects;

/**
 * @author tanyujie
 * @classname TransferStateMachineConfigurer
 * @description 状态机配置类
 * @date 2022/5/25 10:31
 * @since 1.0
 */
@Slf4j
@Configuration
@EnableStateMachineFactory
public class TransferStateMachineConfigurer extends EnumStateMachineConfigurerAdapter<TransferStatusEnum, TransferEventEnum> {

    @Override
    public void configure(StateMachineConfigurationConfigurer<TransferStatusEnum, TransferEventEnum> config) throws Exception {
        config
                .withConfiguration()
                .machineId("transferStateMachineConfigurer");
    }

    @Override
    public void configure(StateMachineStateConfigurer<TransferStatusEnum, TransferEventEnum> states) throws Exception {
        states
                .withStates()
                .initial(TransferStatusEnum.WANG_LAI)
                .states(EnumSet.allOf(TransferStatusEnum.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<TransferStatusEnum, TransferEventEnum> transitions) throws Exception {
        transitions
                .withExternal()
                .source(TransferStatusEnum.WANG_LAI)
                .target(TransferStatusEnum.SHOULD_RECEIVE_PAY)
                .event(TransferEventEnum.DO_WANG_LAI)
                .action(doWangLaiAction())
                .and()
                .withExternal()
                .source(TransferStatusEnum.SHOULD_RECEIVE_PAY)
                .target(TransferStatusEnum.RECEIVE_PAY)
                .event(TransferEventEnum.DO_SHOULD_RECEIVE_PAY)
                .action(doShouldReceivePayAction())
                .and()
                .withExternal()
                .source(TransferStatusEnum.RECEIVE_PAY)
                .target(TransferStatusEnum.TRANSFER_IN_PROGRESS)
                .event(TransferEventEnum.DO_RECEIVE_PAY)
                .action(doReceivePayAction())
                .and()
                .withExternal()
                .source(TransferStatusEnum.TRANSFER_IN_PROGRESS)
                .target(TransferStatusEnum.COMPLETE)
                .event(TransferEventEnum.DO_QUERY)
                .action(doQuery());
    }

    @Bean
    public Action<TransferStatusEnum, TransferEventEnum> doWangLaiAction() {
        return context -> {
            log.info("执行doWangLaiAction");
            TransferTaskDetails details = (TransferTaskDetails) context.getMessage().getHeaders().get("details");
            Assert.notNull(details, "明细为空");
            if (Objects.equals(details.getId(), "1")) {
                details.setWanglaiId("1");
            } else {
                throw new RuntimeException("失败");
            }
        };
    }

    @Bean
    public Action<TransferStatusEnum, TransferEventEnum> doShouldReceivePayAction() {
        return context -> {
            log.info("执行doShouldReceivePayAction");
            TransferTaskDetails details = (TransferTaskDetails) context.getMessage().getHeaders().get("details");
            Assert.notNull(details, "明细为空");
            if (Objects.equals(details.getId(), "1")) {
                details.setBillId("1");
            } else {
                throw new RuntimeException("失败");
            }
        };
    }

    @Bean
    public Action<TransferStatusEnum, TransferEventEnum> doReceivePayAction() {
        return context -> {
            log.info("执行doReceivePayAction");
            TransferTaskDetails details = (TransferTaskDetails) context.getMessage().getHeaders().get("details");
            Assert.notNull(details, "明细为空");
            if (Objects.equals(details.getId(), "1")) {
            } else {
                throw new RuntimeException("失败");
            }
        };
    }

    @Bean
    public Action<TransferStatusEnum, TransferEventEnum> doQuery() {
        return context -> {
            log.info("执行doQuery");
            TransferTaskDetails details = (TransferTaskDetails) context.getMessage().getHeaders().get("details");
            Assert.notNull(details, "明细为空");
            if (Objects.equals(details.getId(), "2")) {
                details.setResult("11");
                details.setBankHandleTime(LocalDateTime.now());
            } else {
                throw new RuntimeException("失败");
            }
        };
    }

}
