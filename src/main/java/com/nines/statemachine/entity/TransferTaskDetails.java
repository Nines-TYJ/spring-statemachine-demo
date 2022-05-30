package com.nines.statemachine.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 划款任务详情表
 * </p>
 *
 * @author tanyujie
 * @since 2022-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="TransferTaskDetails对象", description="划款任务详情表")
public class TransferTaskDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "划款任务id")
    private String taskId;

    @ApiModelProperty(value = "金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "往来报送企业或个人(企业社会代码或身份证)")
    private String name;

    @ApiModelProperty(value = "财务系统返回的往来id")
    private String wanglaiId;

    @ApiModelProperty(value = "单据内码")
    private String billId;

    @ApiModelProperty(value = "单据编号")
    private String docNo;

    @ApiModelProperty(value = "单据编号")
    private String systemNo;

    @ApiModelProperty(value = "状态 0: 待调用往来接口 1: 待调用应收/付款单 2: 待调用收/付款单 3: 付款中 4: 完成")
    private String status;

    @ApiModelProperty(value = "调用流程结果 收款：1 制单 2 待审核 3 审核退回 4 已完成 5 已冲账 6 待认领 付款：1：制单，2：审批中，-2：审批退回，3：待复核，-3，复核退回，4：允许发送银行，-4：经办退回，5：网银发送待确认，-5：发送确认退回，6：等待发送银行，7：银行正在处理，8：银行付款成功，-8：核销退回，9：银行付款失败，10：已取消付款，11：已完成，12：已冲账；13：线下付款中（非直联付款复核完成后，票据结算方式付款复核完成后）；14：待结算；-14：结算驳回；-1：已作废")
    private String result;

    @ApiModelProperty(value = "银行处理时间")
    private LocalDateTime bankHandleTime;

    @ApiModelProperty(value = "租户id")
    private String tenantId;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "删除标记 0未删除 1已删除")
    private Integer delFlag;


}
