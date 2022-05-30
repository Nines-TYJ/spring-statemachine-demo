package com.nines.statemachine.config;

import com.nines.statemachine.entity.TransferTaskDetails;
import com.nines.statemachine.enums.TransferEventEnum;
import com.nines.statemachine.enums.TransferStatusEnum;
import com.nines.statemachine.service.ITransferTaskDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author tanyujie
 * @classname TransferStateMachinePersist
 * @description 状态机持久化
 * @date 2022/5/30 10:25
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
public class TransferStateMachinePersist implements StateMachinePersist<TransferStatusEnum, TransferEventEnum, TransferTaskDetails> {

    private final ITransferTaskDetailsService transferTaskDetailsService;

    @Override
    public void write(StateMachineContext<TransferStatusEnum, TransferEventEnum> context, TransferTaskDetails contextObj) {
        contextObj.setStatus(context.getState().getCode());
        contextObj.setUpdateTime(LocalDateTime.now());
        transferTaskDetailsService.updateById(contextObj);
    }

    @Override
    public StateMachineContext<TransferStatusEnum, TransferEventEnum> read(TransferTaskDetails contextObj) {
        TransferTaskDetails data = transferTaskDetailsService.getById(contextObj.getId());
        Assert.notNull(data, "无效的id");
        return new DefaultStateMachineContext<>(TransferStatusEnum.getEnumByCode(data.getStatus()),
                null, null, null, null, "transferStateMachineConfigurer");
    }
}
