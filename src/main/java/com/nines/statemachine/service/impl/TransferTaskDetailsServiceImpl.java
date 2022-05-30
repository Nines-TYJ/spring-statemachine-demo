package com.nines.statemachine.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nines.statemachine.entity.TransferTaskDetails;
import com.nines.statemachine.mapper.TransferTaskDetailsMapper;
import com.nines.statemachine.service.ITransferTaskDetailsService;
import org.springframework.stereotype.Service;

/**
 * @author tanyujie
 * @classname TransferTaskDetailsServiceImpl
 * @description TODO
 * @date 2022/5/30 10:12
 * @since 1.0
 */
@Service
public class TransferTaskDetailsServiceImpl extends ServiceImpl<TransferTaskDetailsMapper, TransferTaskDetails> implements ITransferTaskDetailsService {
}
