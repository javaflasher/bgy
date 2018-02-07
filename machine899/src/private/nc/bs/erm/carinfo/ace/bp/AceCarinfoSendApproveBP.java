package nc.bs.erm.carinfo.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.erm.carinfo.AggCarInfoVO;
import nc.vo.pub.pf.BillStatusEnum;
import nc.vo.pub.VOStatus;

/**
 * 标准单据送审的BP
 */
public class AceCarinfoSendApproveBP {
	/**
	 * 送审动作
	 * 
	 * @param vos
	 *            单据VO数组
	 * @param script
	 *            单据动作脚本对象
	 * @return 送审后的单据VO数组
	 */

	public AggCarInfoVO[] sendApprove(AggCarInfoVO[] clientBills,
			AggCarInfoVO[] originBills) {
		for (AggCarInfoVO clientFullVO : clientBills) {
			clientFullVO.getParentVO().setAttributeValue("approvestatus",
					BillStatusEnum.COMMIT.value());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
		}
		// 数据持久化
		AggCarInfoVO[] returnVos = new BillUpdate<AggCarInfoVO>().update(
				clientBills, originBills);
		return returnVos;
	}
}
