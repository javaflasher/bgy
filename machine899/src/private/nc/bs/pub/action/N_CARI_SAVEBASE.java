package nc.bs.pub.action;

import com.jacob.com.InvocationProxy;

import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.erm.carinfo.ace.bp.AceCarinfoInsertBP;
import nc.bs.erm.carinfo.ace.bp.AceCarinfoUpdateBP;
import nc.bs.erm.carinfo.plugin.bpplugin.CarinfoPluginPoint;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.vo.jcom.lang.StringUtil;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.erm.constants.CarInforConstants;
import nc.vo.erm.carinfo.AggCarInfoVO;
import nc.vo.erm.carinfo.CarInfoVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;

public class N_CARI_SAVEBASE extends AbstractPfAction<AggCarInfoVO> {

	@Override
	protected CompareAroundProcesser<AggCarInfoVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggCarInfoVO> processor = null;
		AggCarInfoVO[] clientFullVOs = (AggCarInfoVO[]) this.getVos();
		/*
		 * BillTransferTool<AggCarInfoVO> tool = new
		 * BillTransferTool<AggCarInfoVO>( clientFullVOs); clientFullVOs =
		 * tool.getClientFullInfoBill();
		 */
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggCarInfoVO>(
					CarinfoPluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggCarInfoVO>(
					CarinfoPluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggCarInfoVO> rule = null;

		return processor;
	}

	@Override
	protected AggCarInfoVO[] processBP(Object userObj,
			AggCarInfoVO[] clientFullVOs, AggCarInfoVO[] originBills) {
		
		AggCarInfoVO[] bills = null;
		try {
			nc.itf.erm.ICarinfoMaintain operator = NCLocator.getInstance()
					.lookup(nc.itf.erm.ICarinfoMaintain.class);
			//报销类型为‘限额报销’，保存时校验“会计年”，“启用会计月”，“年限额”必输\
			for(int x=0;x<clientFullVOs.length;x++){
				CarInfoVO hVO =  clientFullVOs[x].getParentVO();
				//hVO.setApprovestatus(CarInforConstants.approval_submit);
				if(hVO!=null){
					//限额报销校验
					if(CarInforConstants.bxlx_xebx == hVO.getExptype() ){
						if(StringUtil.isEmpty(hVO.getAccyear66())){	//cardPanel.getParent()
							throw  new BusinessException("选择【限额报销】时，会计年不能为空");
						}else if(hVO.getBeginaccmonth()==null){
							throw  new BusinessException("选择【限额报销】时，启用会计月不能为空");
						} else if(hVO.getAnnuallimit()==null){
							throw  new BusinessException("选择【限额报销】时，年限额不能为空");
						}
					}
					//车辆pk校验
					if(CarInforConstants.opertype_add!=hVO.getOpertype()){
						//2、	选择“变更”或“停用”或“启用”时，保存时校验“车辆”必输
						if(StringUtil.isEmpty(hVO.getPk_car99())){
							throw  new BusinessException("【操作类型】为“变更”或“停用”或“启用”时，“车辆”不能为空");
						}						
					}				
				}
			}
			if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
					.getPrimaryKey())) {
				bills = operator.update(clientFullVOs, originBills);
			} else {
				bills = operator.insert(clientFullVOs, originBills);
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}
}
