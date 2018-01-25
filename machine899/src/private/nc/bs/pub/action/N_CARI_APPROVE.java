package nc.bs.pub.action;

import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.erm.carinfo.ace.bp.AceCarinfoApproveBP;
import nc.bs.erm.carinfo.plugin.bpplugin.CarinfoPluginPoint;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.erm.carinfo.AggCarInfoVO;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.erm.constants.CarInforConstants;
import nc.vo.pub.VOStatus;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.framework.common.NCLocator;

public class N_CARI_APPROVE extends AbstractPfAction<AggCarInfoVO> {

	public N_CARI_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggCarInfoVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggCarInfoVO> processor = new CompareAroundProcesser<AggCarInfoVO>(
				CarinfoPluginPoint.APPROVE);
		IRule<AggCarInfoVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.ApproveStatusCheckRule();

		processor.addBeforeRule(rule);

		return processor;
	}

	@Override
	protected AggCarInfoVO[] processBP(Object userObj,
			AggCarInfoVO[] clientFullVOs, AggCarInfoVO[] originBills) {
		AggCarInfoVO[] bills = null;
		nc.itf.erm.ICarinfoMaintain operator = NCLocator.getInstance().lookup(
				nc.itf.erm.ICarinfoMaintain.class);
		try {
			bills = operator.approve(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}	
		return bills;
	}

}
