package nc.bs.pub.action;

import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.erm.carinfo.ace.bp.AceCarinfoSendApproveBP;
import nc.bs.erm.carinfo.plugin.bpplugin.CarinfoPluginPoint;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.erm.carinfo.AggCarInfoVO;
import nc.vo.pub.pf.BillStatusEnum;
import nc.vo.pub.VOStatus;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.pub.pf.IPfRetCheckInfo;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.framework.common.NCLocator;

public class N_CARI_SAVE extends AbstractPfAction<AggCarInfoVO> {
	public N_CARI_SAVE() {
		super();
	}

	protected CompareAroundProcesser<AggCarInfoVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggCarInfoVO> processor = new CompareAroundProcesser<AggCarInfoVO>(
				CarinfoPluginPoint.SEND_APPROVE);
		// TODO 在此处添加审核前后规则
		IRule<AggCarInfoVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.CommitStatusCheckRule();

		processor.addBeforeRule(rule);

		return processor;
	}

	@Override
	protected AggCarInfoVO[] processBP(Object userObj,
			AggCarInfoVO[] clientFullVOs, AggCarInfoVO[] originBills) {
		nc.itf.erm.ICarinfoMaintain operator = NCLocator.getInstance().lookup(
				nc.itf.erm.ICarinfoMaintain.class);
		AggCarInfoVO[] bills = null;
		for(int x=0;x<clientFullVOs.length;x++){
			clientFullVOs[x].getParentVO().setAttributeValue("billstatus", 2);//提交时  单据状态为审批中
		}
		try {
			
			bills = operator.save(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
