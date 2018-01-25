package nc.bs.erm.carinfo.ace.bp;

import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.itf.erm.ICarInfoSyncService;
import nc.itf.erm.constants.CarInforConstants;
import nc.vo.erm.carinfo.AggCarInfoVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;

/**
 * 标准单据审核的BP
 */
public class AceCarinfoApproveBP {

	/**
	 * 审核动作
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */

	public AggCarInfoVO[] approve(AggCarInfoVO[] clientBills,
			AggCarInfoVO[] originBills) {
		
		for(int x=0;x<clientBills.length;x++){
			if(clientBills[x].getParentVO().getApprovestatus()==CarInforConstants.approval_sptg){
				clientBills[x].getParentVO().setBillstatus(CarInforConstants.billstatus_sptg);//更新单据状为审批通过；
			}
		}
		BillUpdate<AggCarInfoVO> update = new BillUpdate<AggCarInfoVO>();
		
		AggCarInfoVO[] returnVos = update.update(clientBills, originBills);
		
		ICarInfoSyncService syncService = NCLocator.getInstance().lookup(ICarInfoSyncService.class);
		for(int x=0;x<returnVos.length;x++){	
			if(returnVos[x].getParentVO().getApprovestatus()== CarInforConstants.approval_sptg){	/* 终审通过*/	
				try {
					String pk_car = syncService.syncCarlnfo(returnVos[x].getParentVO());
					
					if(StringUtil.isEmpty(returnVos[x].getParentVO().getPk_car99())){
						returnVos[x].getParentVO().setPk_car99(pk_car);		//新增后回写
					}
					if(returnVos[x].getParentVO().getPaymode().intValue() == CarInforConstants.fffs_bxff &&
							returnVos[x].getParentVO().getExptype().intValue() == CarInforConstants.bxlx_xebx){
						
						String pk_car_limit  =  syncService.syncCarlimit(returnVos[x]);	//同步限额
						if(StringUtil.isEmpty(returnVos[x].getParentVO().getPk_car_limit())){
							returnVos[x].getParentVO().setPk_car_limit(pk_car_limit);	//新增后回写
						}
					}
					
				} catch (BusinessException e) {
					e.printStackTrace();
				}
			}
		}
		
		return returnVos;
	}

}
