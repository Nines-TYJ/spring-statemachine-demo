package com.nines.statemachine.enums;

/**
 * @author tanyujie
 * @classname TransferEventEnum
 * @description 划款任务明细状态枚举
 * @date 2022/5/13 16:41
 * @since 1.0
 */
public enum TransferEventEnum {

    /**
     * 调用往来接口
     */
    DO_WANG_LAI,
    /**
     * 调用应收/付款单
     */
    DO_SHOULD_RECEIVE_PAY,
    /**
     * 调用收/付款单
     */
    DO_RECEIVE_PAY,
    /**
     * 查询付款结果
     */
    DO_QUERY;


}
