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
 * @author �����
 * @date 2018-1-19
 * @�������� �����ͷ������ֶ�����
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
		DataTable bTable = FIDataTableUtil.getDataTable("body_1bodys");
		//��ͷ������֯
		String pk_org= (String)FIDataTableUtil.getHeadValue(orgtable, "pk_org");
	
		//�޸ı�ͷ������֯
		if("mainOrg".equals(tableID)&&"pk_org".equals(field)){
			//���ñ��������֯	beginaccmonth accmonth	MONTHLYLIMIT
			FIDataTableUtil.setHeadValue(hTable, "pk_org", pk_org);
			//�޸ĳ�����������
			JSONObject refparam = new JSONObject();
			refparam.put("pk_org", pk_org);
			//����������֯�������������
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
			
			String queryNameCodeSql = " select code,name,pid from bd_defdoc where pk_defdoc='"+newValue+"';";	//���ճ����ĳ�Ӧ�������õ�
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
		
	
	}
}
