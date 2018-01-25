package nc.bs.erm.carinfo.ace.bp;

import nc.bs.erm.carinfo.plugin.bpplugin.CarinfoPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.erm.carinfo.AggCarInfoVO;

/**
 * ��׼����ɾ��BP
 */
public class AceCarinfoDeleteBP {

	public void delete(AggCarInfoVO[] bills) {

		DeleteBPTemplate<AggCarInfoVO> bp = new DeleteBPTemplate<AggCarInfoVO>(
				CarinfoPluginPoint.DELETE);

		// ����ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ����ִ�к�ҵ�����
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggCarInfoVO> processer) {
		IRule<AggCarInfoVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();

		processer.addBeforeRule(rule);

	}

	/**
	 * ɾ����ҵ�����
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
