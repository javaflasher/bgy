package nc.bs.erm.carinfo.ace.bp;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.itf.erm.constants.CarInforConstants;
import nc.vo.erm.carinfo.AggCarInfoVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;

/**
 * ��׼���������BP
 */
public class AceCarinfoUnApproveBP {

	public AggCarInfoVO[] unApprove(AggCarInfoVO[] clientBills,
			AggCarInfoVO[] originBills) throws BusinessException {
		BillUpdate<AggCarInfoVO> update = new BillUpdate<AggCarInfoVO>();
		AggCarInfoVO[] returnVos  = null;
		returnVos = update.update(clientBills, originBills);
		//�����������ȡ������  ����֮����ȡ������
		for(int x=0;x<originBills.length;x++){
//			int status = originBills[x].getParentVO().getApprovestatus();
//			if(status!= CarInforConstants.approval_sptg){
//				
//			}else{	
//				throw new BusinessException("�����������ȡ��������");
//				//nc.ui.pub.beans.MessageDialog.showWarningDlg(null, "���ڴ���", "�����ָ��ܿ����ڲ������ڵ�ǰ����");
//			}
		}
		
		
		return returnVos;
	}

	private void setHeadVOStatus(AggCarInfoVO[] clientBills) {
		for (AggCarInfoVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
	}
}
