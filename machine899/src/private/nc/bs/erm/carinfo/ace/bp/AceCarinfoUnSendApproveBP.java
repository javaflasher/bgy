package nc.bs.erm.carinfo.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.erm.carinfo.AggCarInfoVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据收回的BP
 */
public class AceCarinfoUnSendApproveBP {
	public AggCarInfoVO[] unSend(AggCarInfoVO[] clientBills,
			AggCarInfoVO[] originBills) {
		// 把VO持久化到数据库中
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggCarInfoVO> update = new BillUpdate<AggCarInfoVO>();
		AggCarInfoVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggCarInfoVO[] clientBills) {
		for (AggCarInfoVO clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("approvestatus",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setAttributeValue("billstatus",1);
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
	}
}
