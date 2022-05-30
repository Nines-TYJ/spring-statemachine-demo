# 划款任务详情表
DROP TABLE IF EXISTS `transfer_task_details`;
CREATE TABLE `transfer_task_details`
(
    `id`               varchar(32)    NOT NULL COMMENT 'id',
    `task_id`          varchar(32)    NOT NULL COMMENT '划款任务id',
    `amount`           decimal(20, 2) NOT NULL COMMENT '金额',
    `name`             varchar(100) COMMENT '往来报送企业或个人(企业社会代码或身份证)',
    `wanglai_id`       varchar(50) COMMENT '财务系统返回的往来id',
    `bill_id`          varchar(50) COMMENT '单据内码',
    `doc_no`           varchar(50) COMMENT '单据编号',
    `system_no`        varchar(50) COMMENT '来源系统编号',
    `status`           varchar(2) COMMENT '状态 0: 待调用往来接口 1: 待调用应收/付款单 2: 待调用收/付款单 3: 付款中 4: 完成',
    `result`           varchar(2) COMMENT '调用流程结果 收款：1 制单 2 待审核 3 审核退回 4 已完成 5 已冲账 6 待认领 付款：1：制单，2：审批中，-2：审批退回，3：待复核，-3，复核退回，4：允许发送银行，-4：经办退回，5：网银发送待确认，-5：发送确认退回，6：等待发送银行，7：银行正在处理，8：银行付款成功，-8：核销退回，9：银行付款失败，10：已取消付款，11：已完成，12：已冲账；13：线下付款中（非直联付款复核完成后，票据结算方式付款复核完成后）；14：待结算；-14：结算驳回；-1：已作废',
    `bank_handle_time` datetime COMMENT '银行处理时间',
    `tenant_id`        varchar(50)             DEFAULT NULL COMMENT '租户id',
    `create_time`      datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark`           varchar(255)            DEFAULT NULL COMMENT '备注',
    `del_flag`         int(2) NOT NULL DEFAULT '0' COMMENT '删除标记 0未删除 1已删除',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='划款任务详情表';