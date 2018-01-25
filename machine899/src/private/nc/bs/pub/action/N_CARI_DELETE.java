package nc.bs.pub.action;

import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.erm.carinfo.ace.bp.AceCarinfoDeleteBP;
import nc.bs.erm.carinfo.plugin.bpplugin.CarinfoPluginPoint;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.erm.carinfo.AggCarInfoVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.framework.common.NCLocator;

public class N_CARI_DELETE extends AbstractPfAction<AggCarInfoVO> {

	@Override
	protected CompareAroundProcesser<AggCarInfoVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggCarInfoVO> processor = new CompareAroundProcesser<AggCarInfoVO>(
				CarinfoPluginPoint.SCRIPT_DELETE);
		// TODO 在此处添加前后规则
		IRule<AggCarInfoVO> rule = null;

		return processor;
	}

	@Override
	protected AggCarInfoVO[] processBP(Object userObj,
			AggCarInfoVO[] clientFullVOs, AggCarInfoVO[] originBills) {
		nc.itf.erm.ICarinfoMaintain operator = NCLocator.getInstance().lookup(
				nc.itf.erm.ICarinfoMaintain.class);
		try {
			operator.delete(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return clientFullVOs;
	}

}
