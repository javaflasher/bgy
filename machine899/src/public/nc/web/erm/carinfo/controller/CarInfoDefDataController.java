package nc.web.erm.carinfo.controller;

import java.util.Map;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.logging.Log;
import nc.nweb.arap.util.OrgChangedLoadDefData;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFDate;
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
		
		EventResponse respose = IWebViewContext.getResponse();
		
		String pk_group = InvocationInfoProxy.getInstance().getGroupId();
		String pk_user = InvocationInfoProxy.getInstance().getUserId();
		String uistate = IWebViewContext.getEventParameter("uistate");
		
		UFDate date = new UFDate(InvocationInfoProxy.getInstance().getBizDateTime());
		
		DataTable hTable = IWebViewContext.getRequest().getDataTable("headform");
		DataTable bTable = IWebViewContext.getRequest().getDataTable("body_1pk_carinfo_b");
		DataTable mainOrgTable = IWebViewContext.getRequest().getDataTable("mainOrg");
		
		JSONObject json = new JSONObject();
		
		try {
			if ("add".equals(uistate)) {
				FIDataTableUtil.setHeadValue(mainOrgTable, "pk_org", "0001H2100000000022LJ");//0001H210000000263BBV 0001H210000000000IGL
				FIDataTableUtil.setHeadValue(hTable, "billmaker", pk_user);
				FIDataTableUtil.setHeadValue(hTable, "pk_org", "0001H2100000000022LJ");
				FIDataTableUtil.setHeadValue(hTable, "pk_group", pk_group);
				FIDataTableUtil.setHeadValue(hTable, "maketime", date);
				FIDataTableUtil.setHeadValue(hTable, "approvestatus", "-1");
				FIDataTableUtil.setHeadValue(hTable, "billstatus", "1");
				FIDataTableUtil.setHeadValue(hTable, "billdate", date);
				FIDataTableUtil.setHeadValue(bTable, "rowno", 10);
				FIDataTableUtil.setHeadValue(bTable, "accmonth", 1);
				
			}
			json.put("success", "true");
			respose.write(json.toString());
			
		} catch (Exception e) {
			json.put("success", "false");
			Log.getInstance(this.getClass()).error(e.getMessage(),e);
			json.put("message", e.getMessage());
			respose.write(json.toString());
		}
	}

}
