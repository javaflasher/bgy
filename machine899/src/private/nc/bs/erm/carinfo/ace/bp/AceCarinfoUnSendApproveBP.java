package nc.bs.erm.carinfo.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.erm.carinfo.AggCarInfoVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * ��׼�����ջص�BP
 */
public class AceCarinfoUnSendApproveBP {
	public AggCarInfoVO[] unSend(AggCarInfoVO[] clientBills,
			AggCarInfoVO[] originBills) {
		// ��VO�־û������ݿ���
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
