package nc.bs.erm.carinfo.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.erm.carinfo.AggCarInfoVO;
import nc.vo.pub.pf.BillStatusEnum;
import nc.vo.pub.VOStatus;

/**
 * ��׼���������BP
 */
public class AceCarinfoSendApproveBP {
	/**
	 * ������
	 * 
	 * @param vos
	 *            ����VO����
	 * @param script
	 *            ���ݶ����ű�����
	 * @return �����ĵ���VO����
	 */

	public AggCarInfoVO[] sendApprove(AggCarInfoVO[] clientBills,
			AggCarInfoVO[] originBills) {
		for (AggCarInfoVO clientFullVO : clientBills) {
			clientFullVO.getParentVO().setAttributeValue("approvestatus",
					BillStatusEnum.COMMIT.value());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
		}
		// ���ݳ־û�
		AggCarInfoVO[] returnVos = new BillUpdate<AggCarInfoVO>().update(
				clientBills, originBills);
		return returnVos;
	}
}
