package com.nines.statemachine.enums;

import java.util.Objects;

/**
 * @author tanyujie
 * @classname TransferStatusEnum
 * @description 划款任务明细状态枚举
 * @date 2022/5/13 16:41
 * @since 1.0
 */
public enum TransferStatusEnum {

    /**
     * 待调用往来接口
     */
    WANG_LAI("0", "待调用往来接口"),
    /**
     * 待调用应收/付款单
     */
    SHOULD_RECEIVE_PAY("1", "待调用应收/付款单"),
    /**
     * 待调用收/付款单
     */
    RECEIVE_PAY("2", "待调用收/付款单"),
    /**
     * 付款中
     */
    TRANSFER_IN_PROGRESS("3", "划款中"),
    /**
     * 完成
     */
    COMPLETE("4", "完成");

    private String code;

    private String desc;

    TransferStatusEnum() {
    }

    TransferStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static TransferStatusEnum getEnumByCode(String code) {
        for (TransferStatusEnum value : TransferStatusEnum.values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        throw new RuntimeException("无效的code");
    }
}
