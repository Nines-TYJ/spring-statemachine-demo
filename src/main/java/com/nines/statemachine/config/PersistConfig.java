package com.nines.statemachine.config;

import com.nines.statemachine.entity.TransferTaskDetails;
import com.nines.statemachine.enums.TransferEventEnum;
import com.nines.statemachine.enums.TransferStatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;

/**
 * @author tanyujie
 * @classname PersistConfig
 * @description TODO
 * @date 2022/5/30 10:50
 * @since 1.0
 */
@Configuration
@RequiredArgsConstructor
public class PersistConfig {

    private final TransferStateMachinePersist transferStateMachinePersist;

    @Bean
    public StateMachinePersister<TransferStatusEnum, TransferEventEnum, TransferTaskDetails> getPersist() {
        return new DefaultStateMachinePersister<>(transferStateMachinePersist);
    }

}
