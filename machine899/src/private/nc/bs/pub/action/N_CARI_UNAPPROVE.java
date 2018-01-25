package nc.bs.pub.action;

import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.erm.carinfo.ace.bp.AceCarinfoUnApproveBP;
import nc.bs.erm.carinfo.plugin.bpplugin.CarinfoPluginPoint;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.erm.carinfo.AggCarInfoVO;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.pub.VOStatus;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.framework.common.NCLocator;

public class N_CARI_UNAPPROVE extends AbstractPfAction<AggCarInfoVO> {

	public N_CARI_UNAPPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggCarInfoVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggCarInfoVO> processor = new CompareAroundProcesser<AggCarInfoVO>(
				CarinfoPluginPoint.UNAPPROVE);
		IRule<AggCarInfoVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.UnapproveStatusCheckRule();

		processor.addBeforeRule(rule);

		return processor;
	}

	@Override
	protected AggCarInfoVO[] processBP(Object userObj,
			AggCarInfoVO[] clientFullVOs, AggCarInfoVO[] originBills) {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AggCarInfoVO[] bills = null;
		try {
			nc.itf.erm.ICarinfoMaintain operator = NCLocator.getInstance()
					.lookup(nc.itf.erm.ICarinfoMaintain.class);
			bills = operator.unapprove(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
