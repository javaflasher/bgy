package nc.bs.pub.action;

import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.erm.carinfo.ace.bp.AceCarinfoUnSendApproveBP;
import nc.bs.erm.carinfo.plugin.bpplugin.CarinfoPluginPoint;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.erm.carinfo.AggCarInfoVO;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.framework.common.NCLocator;

public class N_CARI_UNSAVEBILL extends AbstractPfAction<AggCarInfoVO> {

	public N_CARI_UNSAVEBILL() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggCarInfoVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggCarInfoVO> processor = new CompareAroundProcesser<AggCarInfoVO>(
				CarinfoPluginPoint.UNSEND_APPROVE);
		// TODO 在此处添加前后规则
		IRule<AggCarInfoVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.UncommitStatusCheckRule();

		processor.addBeforeRule(rule);

		return processor;
	}

	@Override
	protected AggCarInfoVO[] processBP(Object userObj,
			AggCarInfoVO[] clientFullVOs, AggCarInfoVO[] originBills) {
		nc.itf.erm.ICarinfoMaintain operator = NCLocator.getInstance().lookup(
				nc.itf.erm.ICarinfoMaintain.class);
		for(int x=0;x<clientFullVOs.length;x++){
			clientFullVOs[x].getParentVO().setAttributeValue("billstatus", 1);//收回时  单据状态为未审批
		}
		AggCarInfoVO[] bills = null;
		try {
			bills = operator.unsave(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
