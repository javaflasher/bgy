package nc.impl.carinfo.web;

import java.util.List;

import org.codehaus.jettison.json.JSONObject;

import uap.iweb.pub.bill.BillItem;
import uap.iweb.pub.bill.util.BillTemplateWebUtil;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.carinfo.web.ICarInfoWebRequestService;
import nc.itf.erm.ICarinfoMaintain;
import nc.itf.erm.constants.CarInforConstants;
import nc.pubitf.rbac.IDataPermissionPubService;
import nc.ssc.fiweb.pub.RequestParam;
import nc.vo.arap.basebill.BaseAggVO;
import nc.vo.erm.carinfo.AggCarInfoVO;
import nc.vo.pub.BusinessException;
import nc.vo.srmsm.supplierenterpf.entity.AggSupplierEnterPFVO;
import nc.web.arap.bill.pub.WebBillTypeFactory;
import nc.web.datatrans.itf.ITranslateDataService;

public class CarInfoWebRequestServiceImpl implements ICarInfoWebRequestService {

	@Override
	public String sendBillSave(RequestParam request) throws Exception {
		
		String jsnObj = request.getParameter("bill");
		
		String pk_billtemplet = request.getParameter("pk_billtemplet");
		
		List<BillItem> billItems = BillTemplateWebUtil.getBillItems(null,pk_billtemplet);
		
		AggCarInfoVO aggvo = NCLocator.getInstance().lookup(ITranslateDataService.class).translateJsonToAggvo(AggCarInfoVO.class, jsnObj);	//工具类获取vo
		
		AggCarInfoVO[] aggvos = {aggvo};
		dealAggVO(aggvo);//处理一下
		
		
		ICarinfoMaintain carinfoMaintain= NCLocator.getInstance().lookup(ICarinfoMaintain.class);
		try{
			aggvos = carinfoMaintain.insert(aggvos, aggvos);
		}catch(BusinessException e){
			Logger.error(e);
		}
		
		JSONObject returnJson = new JSONObject();
		
		returnJson = NCLocator.getInstance().lookup(ITranslateDataService.class).transAggvoToJSON(aggvo);
		
		returnJson.put("success" ,"true");
		
		
		return returnJson.toString();
	}

	@Override
	public String commit(RequestParam request) throws Exception {
		
		return null;
	}

	@Override
	public String recall(RequestParam request) throws Exception {
		
		return null;
	}

	@Override
	public String update(RequestParam request) throws Exception {
		
		return null;
	}

	private void checkPermissionForEdit(String actioncode, String actionName, BaseAggVO vo) throws BusinessException {
		String cuserid = InvocationInfoProxy.getInstance().getUserId();
		String groupId = InvocationInfoProxy.getInstance().getGroupId();
		String billType = vo.getHeadVO().getPk_billtype();
		String resourceCode = getResourceCodeByBillType(billType);
		if(!NCLocator.getInstance().lookup(IDataPermissionPubService.class).
				isUserhasPermission(resourceCode, vo.getHeadVO().getPrimaryKey(), actioncode, groupId, cuserid)){
			throw new BusinessException("当前用户没有操作该单据" + actionName + "的权限");
		}
	}
	
	private static String getResourceCodeByBillType(String billType) throws BusinessException {
	return WebBillTypeFactory.getWebBillType(billType).getResourceCode();
	}
	
	private void dealAggVO(AggCarInfoVO aggvo){
		//aggvo.getParentVO().setBillstatus(CarInforConstants.billstatus_wsp);
		//aggvo.getParentVO().setApprovestatus(CarInforConstants.approval_free);
	}
}
