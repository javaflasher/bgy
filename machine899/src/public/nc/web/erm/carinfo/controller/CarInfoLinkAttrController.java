package nc.web.erm.carinfo.controller;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.itf.er.query.IYerQueryService;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.nweb.arap.pub.ArapWebConst;
import nc.nweb.arap.util.FIDataTableUtil;
import nc.vo.pub.BusinessException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Component;
import uap.iweb.entity.DataTable;
import uap.iweb.event.run.DataTableFieldEvent;
import uap.iweb.icontext.IWebViewContext;

/**
 * @author 钟泽辉
 * @date 2018-1-19
 * @功能描述 处理表头表体的字段联动
 */
@Component("CarInfoLinkAttrController")
public class CarInfoLinkAttrController {
	
	
	@SuppressWarnings("rawtypes")
	public void valueChanged(DataTableFieldEvent dtEvent) throws Exception {
		
		IYerQueryService  queryService= NCLocator.getInstance().lookup(IYerQueryService.class);
		
		String tableID = dtEvent.getDataTable();
		String field = dtEvent.getField();
		String newValue = dtEvent.getNewValue();
		DataTable orgtable = IWebViewContext.getRequest().getDataTable("mainOrg");
		DataTable hTable = FIDataTableUtil.getDataTable("headform");
		DataTable bTable = FIDataTableUtil.getDataTable("body_1pk_carinfo_b");	//body_1pk_carinfo_b	body_1bodys
		//表头财务组织
		String pk_org= (String)FIDataTableUtil.getHeadValue(orgtable, "pk_org");
	
		//修改表头财务组织
		if("mainOrg".equals(tableID)&&"pk_org".equals(field)){
			//设置表体财务组织	beginaccmonth accmonth	MONTHLYLIMIT
			FIDataTableUtil.setHeadValue(hTable, "pk_org", pk_org);
			//修改车辆参照条件
			JSONObject refparam = new JSONObject();
			refparam.put("pk_org", pk_org);
			//更换财务组织后，清除表体内容
			FIDataTableUtil.setDataTableRefParam(hTable, "pk_car99", refparam);
			FIDataTableUtil.setHeadValue(hTable, "pk_car99", null);
			FIDataTableUtil.setHeadValue(hTable, "carname", null);
			FIDataTableUtil.setHeadValue(hTable, "carcode", null);
			FIDataTableUtil.setHeadValue(hTable, "pk_pcar88", null);
			FIDataTableUtil.setHeadValue(hTable, "carproperty77", null);
			FIDataTableUtil.setHeadValue(hTable, "bipno", null);
			FIDataTableUtil.setHeadValue(hTable, "annuallimit", null);
			FIDataTableUtil.setHeadValue(hTable, "remarks", null);
			FIDataTableUtil.setHeadValue(hTable, "accyear66", null);

		}
		
		if("pk_car99".equals(field)){
			
			String queryNameCodeSql = " select code,name,pid from bd_defdoc where pk_defdoc='"+newValue+"';";	//参照出来的车应该是启用的
			ArrayList<HashMap<String,String>> cnInfo = new ArrayList<HashMap<String,String>>();
			try {
				cnInfo= (ArrayList<HashMap<String, String>>) queryService.executeQuery(queryNameCodeSql, new MapListProcessor());
				FIDataTableUtil.setHeadValue(hTable, "carname", cnInfo.get(0).get("name"));
				FIDataTableUtil.setHeadValue(hTable, "carcode", cnInfo.get(0).get("code"));
				FIDataTableUtil.setHeadValue(hTable, "pk_pcar88", cnInfo.get(0).get("pid"));
			} catch (BusinessException e1) {
				e1.printStackTrace();
			}
			
		}
		//实报实销”时：不能编辑“会计年”、“启用会计月”和“年限额”；不能编辑表体
		if("exptype".equals(field) && "1".equals(newValue)){
			FIDataTableUtil.setHeadItemEnabled(hTable, "accyear66", false);
			FIDataTableUtil.setHeadValue(hTable, "accyear66", null);
			FIDataTableUtil.setHeadItemEnabled(hTable, "beginaccmonth", false);
			FIDataTableUtil.setHeadValue(hTable, "beginaccmonth", null);
			FIDataTableUtil.setHeadItemEnabled(hTable, "annuallimit", false);
			FIDataTableUtil.setHeadValue(hTable, "annuallimit", null);
			
//			FIDataTableUtil.setBodyItemEnabledForAll(bTable, "monthlylimit", true);
//			FIDataTableUtil.setBodyValue(bTable, "monthlylimit", null);
//			FIDataTableUtil.setBodyItemEnabledForAll(bTable, "remarks", true);
//			FIDataTableUtil.setBodyValue(bTable, "remarks", null);
			
		}else{
			FIDataTableUtil.setHeadItemEnabled(hTable, "accyear66", true);		
			FIDataTableUtil.setHeadItemEnabled(hTable, "beginaccmonth", true);			
			FIDataTableUtil.setHeadItemEnabled(hTable, "annuallimit", true);
//			FIDataTableUtil.setBodyItemEnabledForAll(bTable, "monthlylimit", true);
//			FIDataTableUtil.setBodyItemEnabledForAll(bTable, "remarks", true);
			
		}
		
	
	}
}
