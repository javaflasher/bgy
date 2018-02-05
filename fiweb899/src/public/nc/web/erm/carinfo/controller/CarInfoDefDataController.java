package nc.web.erm.carinfo.controller;

import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.nweb.arap.util.OrgChangedLoadDefData;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFDateTime;
import nc.web.pub.utils.FIDataTableUtil;
import org.springframework.stereotype.Component;
import uap.iweb.entity.DataTable;
import uap.iweb.event.EventResponse;
import uap.iweb.icontext.IWebViewContext;
import uap.json.JSONObject;
import uap.iweb.entity.Row;


/**
 * @author ÷”‘Ûª‘
 * @date 2018-1-19
 * @π¶ƒ‹√Ë ˆ 
 */
@Component("CarInfoDefDataController")
public class CarInfoDefDataController {
	
	public void loadData() throws Exception {
		Map<String, String> clientAttrs = IWebViewContext.getRequest()
				.getEnvironment().getClientAttributes();
		String pk_group = InvocationInfoProxy.getInstance().getGroupId();
		String pk_user = InvocationInfoProxy.getInstance().getUserId();
		String uistate = IWebViewContext.getEventParameter("uistate");
		EventResponse respose = IWebViewContext.getResponse();
		
		JSONObject json = new JSONObject();
		DataTable hTable = IWebViewContext.getRequest().getDataTable(
				"headform");
		DataTable bTable = IWebViewContext.getRequest().getDataTable(
				"body_1pk_apply_b");
		DataTable mainOrgTable = IWebViewContext.getRequest().getDataTable(
				"mainOrg");
		if("add".equals(uistate)){
			FIDataTableUtil.setHeadValue(hTable, "billmaker", pk_user);
			FIDataTableUtil.setHeadValue(hTable, "pk_group", pk_group);
			FIDataTableUtil.setHeadValue(hTable, "maketime", new UFDateTime().toString());
			FIDataTableUtil.setHeadValue(hTable, "approvestatus", "-1");
			FIDataTableUtil.setHeadValue(hTable, "billstatus", "1");
			FIDataTableUtil.setHeadValue(hTable, "billdate", "");
			
		}
		
		
		
	}

}
