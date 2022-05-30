package com.nines.statemachine;

import com.nines.statemachine.entity.TransferTaskDetails;
import com.nines.statemachine.enums.TransferEventEnum;
import com.nines.statemachine.enums.TransferStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * @author tanyujie
 * @classname StateMachineTest
 * @description 测试类
 * @date 2022/5/30 11:02
 * @since 1.0
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StateMachineApplication.class)
public class StateMachineTest {

    @Resource
    private StateMachinePersister<TransferStatusEnum, TransferEventEnum, TransferTaskDetails> stateMachinePersister;

    @Resource
    private StateMachineFactory<TransferStatusEnum, TransferEventEnum> stateMachineFactory;

    @Test
    public void stateMachineTest() throws Exception {
        StateMachine<TransferStatusEnum, TransferEventEnum> stateMachine = stateMachineFactory.getStateMachine("transferStateMachineConfigurer");
        stateMachine.startReactively();
        TransferTaskDetails details = new TransferTaskDetails();
        details.setId("1");
        stateMachinePersister.restore(stateMachine, details);
        log.info("恢复状态机后的状态为：{}", stateMachine.getState().getId());
        Message<TransferEventEnum> message = MessageBuilder.withPayload(TransferEventEnum.DO_WANG_LAI).setHeader("details", details).build();
        stateMachine.sendEvent(Mono.just(message)).subscribe();
        log.info("dowanglai后的状态为：{}", stateMachine.getState().getId());
        message = MessageBuilder.withPayload(TransferEventEnum.DO_SHOULD_RECEIVE_PAY).setHeader("details", details).build();
        stateMachine.sendEvent(Mono.just(message)).subscribe();
        log.info("doshouldReceivePay后的状态为：{}", stateMachine.getState().getId());
        message = MessageBuilder.withPayload(TransferEventEnum.DO_RECEIVE_PAY).setHeader("details", details).build();
        stateMachine.sendEvent(Mono.just(message)).subscribe();
        log.info("doReceivePay后的状态为：{}", stateMachine.getState().getId());
        message = MessageBuilder.withPayload(TransferEventEnum.DO_QUERY).setHeader("details", details).build();
        stateMachine.sendEvent(Mono.just(message)).subscribe();
        log.info("doQuery后的状态为：{}", stateMachine.getState().getId());
        stateMachinePersister.persist(stateMachine, details);
    }

}
