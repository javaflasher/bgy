package nc.ui.erm.carinfo.ace.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.itf.er.query.IYerQueryService;
import nc.itf.erm.constants.CarInforConstants;
import nc.itf.pubapp.pub.smart.IQueryBillService;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.ecpubapp.uif2app.model.IQueryService;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailAfterEditEvent;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.erm.carinfo.CarInfoVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

/**
 * 单据表头表尾字段编辑后事件处理类
 * 
 * @since 6.0
 * @version 2011-7-7 下午02:52:22
 * @author duy
 */
public class AceHeadTailAfterEditHandler implements
		IAppEventHandler<CardHeadTailAfterEditEvent> {

	@Override
	public void handleAppEvent(CardHeadTailAfterEditEvent e) {
		IYerQueryService queryService= NCLocator.getInstance().lookup(IYerQueryService.class);  //basedao查不了
		
		BillCardPanel cardPanel = e.getBillCardPanel();
		// 编辑表体后自动带出1-12会计月的表体行_限额默认为零_不能增删表体行
		String pk_org = (String) cardPanel.getHeadItem("pk_org").getValueObject();
		String key = e.getKey();
		Object obj = e.getValue();
		if ("pk_org".equals(e.getKey())) {
			nc.ui.pub.beans.MessageDialog.showWarningDlg(cardPanel.getParent(),
					"测试窗口", "测试测试");
		}
		if ("exptype".equals(key)) {
			// 报销方式为实报实销，启用会计月，会计年，年限额，表体不能编辑
			if (nc.itf.erm.constants.CarInforConstants.bxlx_sbsx == (Integer) obj) {
				// nc.ui.pub.beans.MessageDialog.showWarningDlg(cardPanel.getParent(),
				// "测试窗口", "测试测试");
				cardPanel.getHeadItem("accyear66").setEnabled(false); // 设置会计年不可编辑及为空
				cardPanel.setHeadItem("accyear66", null);
				
				cardPanel.getHeadItem("beginaccmonth").setEnabled(false); // 设置启用会计月不可编辑及为空
				cardPanel.setHeadItem("beginaccmonth", null);
				
				cardPanel.getHeadItem("annuallimit").setEnabled(false); // 设置年限额不可编辑及为空
				cardPanel.setHeadItem("annuallimit", null);
				
				cardPanel.getBodyItem("monthlylimit").setEnabled(false);	//设置表体金额不可编辑及为空
				cardPanel.getBodyItem("remarks").setEnabled(false);			//设置表体备注不可编辑及为空
				for(int x=0;x<12;x++){
					cardPanel.setBodyValueAt(new UFDouble(0), x, "monthlylimit","pk_carinfo_b");
					cardPanel.setBodyValueAt(null, x, "remarks","pk_carinfo_b");
				}
				
			} else {
				cardPanel.getHeadItem("accyear66").setEnabled(true); // 设置会计年不可编辑
				cardPanel.getHeadItem("beginaccmonth").setEnabled(true); // 设置启用会计月不可编辑
				cardPanel.getHeadItem("annuallimit").setEnabled(true); // 设置年限额不可编辑
				cardPanel.getBodyItem("monthlylimit").setEnabled(true);
				cardPanel.getBodyItem("remarks").setEnabled(true);
			}
		} else if ("opertype".equals(key)){	//操作类型
			if(CarInforConstants.opertype_add == ((Integer)obj)){
				cardPanel.getHeadItem("pk_car99").setEnabled(false); // 设置车辆不可编辑及为空
				cardPanel.setHeadItem("pk_car99", null);
			}
			
		}else if("pk_car99".equals(key)){
			//cardPanel.setHeadItem("pk_car99", obj.toString());	//不知为何设置不上
			//选择车辆后自动带出“车辆编码”和“车辆名称”
			List<HashMap<String, String>> carinfos = new ArrayList<HashMap<String,String>>();
			String pk_car_limit  = cardPanel.getHeadItem("accyear66").getValueObject().toString();
			String pk_car =  (String) obj; 
			Object jknObj = cardPanel.getHeadItem("accyear66").getValueObject();
			if(!StringUtil.isEmpty(pk_car)){
				
				IQueryBillService queyr= NCLocator.getInstance().lookup(IQueryBillService.class);
				//选择“车辆”后如果选择的车辆有相同“财务组织”相同“会计年”的【车辆限额限量控制】，则自动带出【车辆限额限量控制】的主键、年限额、限额到单据的“车辆限额限量控制”、“年限额”、“限额”字段。
				if(jknObj!=null){
					int kjn =(Integer)jknObj;
					String querySql = "select  lh.pk_car_limit,lh.年限额,lb.limit_e,lb.kjy from erm_car_limit  lh inner join erm_car_limit_b lb on lh.pk_car_limit=lb.pk_car_limit  where lh.pk_org='"
					+pk_org+"' and lh.kjni='"+kjn+"' and lh.car='"+pk_car+"' and lh.dr=0 and lb.dr=0;";
					
					ArrayList<HashMap<String,Object>> infos = new ArrayList<HashMap<String,Object>>();
					try {
						infos= (ArrayList<HashMap<String, Object>>) queryService.executeQuery(querySql, new MapListProcessor());
					} catch (BusinessException e1) {
						e1.printStackTrace();
					}
					if(infos.size()>0){
						if(infos.get(0).get("年限额")!=null){
							cardPanel.getHeadItem("annuallimit").setValue(new UFDouble((infos.get(0).get("年限额").toString())));
						}
						
						HashMap<String, Object> info= infos.get(0);
						for(int x=0;x<infos.size();x++){
							if(info.get("limit_e")!=null){
								cardPanel.setBodyValueAt(new UFDouble(info.get("limit_e").toString()), x, "monthlylimit","pk_carinfo_b");
							}	
						}
						cardPanel.getHeadItem("pk_car_limit").setValue((infos.get(0).get("pk_car_limit").toString()));	//回写表体
					}else{
						for(int x=0;x<12;x++){
							cardPanel.setBodyValueAt(new UFDouble(0), x, "monthlylimit","pk_carinfo_b");//	更换车辆后，无【车辆限额限量控制】记录，表体“月限额”设置为0；		
						}
						cardPanel.getHeadItem("pk_car_limit").setValue(null);	//查不到就清除；
					}
					
				}
				//选择车辆后，设置车辆编码、车辆名称、上级档案
				String queryNameCodeSql = " select code,name,pid from bd_defdoc where pk_defdoc='"+pk_car+"';";	//参照出来的车应该是启用的
				ArrayList<HashMap<String,String>> cnInfo = new ArrayList<HashMap<String,String>>();
				try {
					cnInfo= (ArrayList<HashMap<String, String>>) queryService.executeQuery(queryNameCodeSql, new MapListProcessor());
				} catch (BusinessException e1) {
					e1.printStackTrace();
				}
				//DefdocVO defVO= (DefdocVO)queyr.queryBill(DefdocVO.class, pk_car);
				if(cnInfo.size()>0){
					cardPanel.setHeadItem("carcode", cnInfo.get(0).get("code"));	//车辆编码
					cardPanel.setHeadItem("carname", cnInfo.get(0).get("name"));	//车辆名称
					cardPanel.setHeadItem("pk_pcar88", cnInfo.get(0).get("pid"));	//上级档案
				}
			}
					
		}else if("beginaccmonth".equals(key) && obj!=null ){
			//启用会计月（从哪个月开始赋值）
			int month = (Integer) obj;
			Object annuallimitObj = cardPanel.getHeadItem("annuallimit").getValueObject();
			if(annuallimitObj!=null){
				UFDouble  annuallimit = (UFDouble) annuallimitObj;
				int mod = annuallimit.intValue()%(13-month);
				int monthlylimit = (annuallimit.intValue() - mod)/(13-month);
				for(int x=0;x<12;x++){
					if(x<month-1){
						cardPanel.setBodyValueAt(new UFDouble(0), x, "monthlylimit","pk_carinfo_b");
						cardPanel.setBodyValueAt(null, x, "remarks","pk_carinfo_b");
					}else{
						if(x!=11){
							cardPanel.setBodyValueAt(new UFDouble(monthlylimit), x, "monthlylimit","pk_carinfo_b");
						}else{
							cardPanel.setBodyValueAt(new UFDouble(monthlylimit+mod), x, "monthlylimit","pk_carinfo_b");
						}					
					}				
				}
			}
			
		}else if("annuallimit".equals(key)){
			//输入年限额
			int annuallimit = ((UFDouble)obj).intValue();
			int  month =  cardPanel.getHeadItem("beginaccmonth").getValueObject()==null?0:(Integer)cardPanel.getHeadItem("beginaccmonth").getValueObject();
			int mod = annuallimit%(13-month);
			int monthlylimit = (annuallimit - mod)/(13-month);
			if(month>0){//有输入启动会计月
				for(int x=0;x<12;x++){
					if(x<month-1){
						cardPanel.setBodyValueAt(new UFDouble(0), x, "monthlylimit","pk_carinfo_b");
						cardPanel.setBodyValueAt(null, x, "remarks","pk_carinfo_b");
					}else{
						if(x!=11){
							cardPanel.setBodyValueAt(new UFDouble(monthlylimit), x, "monthlylimit","pk_carinfo_b");
						}else{
							cardPanel.setBodyValueAt(new UFDouble(monthlylimit+mod), x, "monthlylimit","pk_carinfo_b");
						}					
					}				
				}
			}else{
				//未输入启动会计月
				for(int x=0;x<12;x++){
					if(x!=11){
						cardPanel.setBodyValueAt(new UFDouble(monthlylimit), x, "monthlylimit","pk_carinfo_b");
					}else{
						cardPanel.setBodyValueAt(new UFDouble(monthlylimit+mod), x, "monthlylimit","pk_carinfo_b");
					}		
				}			
			}		
		}else if("carcode".equals(key)){
			//重复性校验：车辆新增，车牌号不能又同样的
			Object operatetyp = cardPanel.getHeadItem("opertype").getValueObject();
			if(operatetyp!=null){
				if(nc.itf.erm.constants.CarInforConstants.opertype_add == (Integer)operatetyp){	//
					String carcode =  cardPanel.getHeadItem("carcode").getValueObject().toString();
					String sql  =  "select doc.name  from bd_defdoc doc left join bd_defdoclist lst on doc.pk_defdoclist=lst.pk_defdoclist where  lst.name = '车辆信息' and doc.code='"+carcode+"';";
					String name = null;
					try {
						  name = (String) queryService.executeQuery(sql, new ColumnProcessor(1));
					} catch (BusinessException e1) {
						e1.printStackTrace();
					}
					if(name!=null){
						nc.ui.pub.beans.MessageDialog.showWarningDlg(cardPanel.getParent(), "提示窗口", "已存在相同的车牌号，请先停用该车牌号，再新增车辆信息~");
					}
				}else{
					
				}
			}
			
			
		}
	}
	

}
