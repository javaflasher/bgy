package nc.bs.erm.carinfo.ace.bp;

import nc.bs.erm.carinfo.plugin.bpplugin.CarinfoPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.erm.carinfo.AggCarInfoVO;

/**
 * 标准单据删除BP
 */
public class AceCarinfoDeleteBP {

	public void delete(AggCarInfoVO[] bills) {

		DeleteBPTemplate<AggCarInfoVO> bp = new DeleteBPTemplate<AggCarInfoVO>(
				CarinfoPluginPoint.DELETE);

		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggCarInfoVO> processer) {
		IRule<AggCarInfoVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();

		processer.addBeforeRule(rule);

	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggCarInfoVO> processer) {
		IRule<AggCarInfoVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.ReturnBillCodeRule();
		((nc.bs.pubapp.pub.rule.ReturnBillCodeRule) rule).setCbilltype("CARI");
		((nc.bs.pubapp.pub.rule.ReturnBillCodeRule) rule).setCodeItem("billno");
		((nc.bs.pubapp.pub.rule.ReturnBillCodeRule) rule)
				.setGroupItem("pk_group");
		((nc.bs.pubapp.pub.rule.ReturnBillCodeRule) rule).setOrgItem("pk_org");

		processer.addAfterRule(rule);

	}
}
