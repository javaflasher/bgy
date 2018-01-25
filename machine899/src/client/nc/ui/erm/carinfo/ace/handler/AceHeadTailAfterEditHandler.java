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
 * ���ݱ�ͷ��β�ֶα༭���¼�������
 * 
 * @since 6.0
 * @version 2011-7-7 ����02:52:22
 * @author duy
 */
public class AceHeadTailAfterEditHandler implements
		IAppEventHandler<CardHeadTailAfterEditEvent> {

	@Override
	public void handleAppEvent(CardHeadTailAfterEditEvent e) {
		IYerQueryService queryService= NCLocator.getInstance().lookup(IYerQueryService.class);  //basedao�鲻��
		
		BillCardPanel cardPanel = e.getBillCardPanel();
		// �༭������Զ�����1-12����µı�����_�޶�Ĭ��Ϊ��_������ɾ������
		String pk_org = (String) cardPanel.getHeadItem("pk_org").getValueObject();
		String key = e.getKey();
		Object obj = e.getValue();
		if ("pk_org".equals(e.getKey())) {
			nc.ui.pub.beans.MessageDialog.showWarningDlg(cardPanel.getParent(),
					"���Դ���", "���Բ���");
		}
		if ("exptype".equals(key)) {
			// ������ʽΪʵ��ʵ�������û���£�����꣬���޶���岻�ܱ༭
			if (nc.itf.erm.constants.CarInforConstants.bxlx_sbsx == (Integer) obj) {
				// nc.ui.pub.beans.MessageDialog.showWarningDlg(cardPanel.getParent(),
				// "���Դ���", "���Բ���");
				cardPanel.getHeadItem("accyear66").setEnabled(false); // ���û���겻�ɱ༭��Ϊ��
				cardPanel.setHeadItem("accyear66", null);
				
				cardPanel.getHeadItem("beginaccmonth").setEnabled(false); // �������û���²��ɱ༭��Ϊ��
				cardPanel.setHeadItem("beginaccmonth", null);
				
				cardPanel.getHeadItem("annuallimit").setEnabled(false); // �������޶�ɱ༭��Ϊ��
				cardPanel.setHeadItem("annuallimit", null);
				
				cardPanel.getBodyItem("monthlylimit").setEnabled(false);	//���ñ�����ɱ༭��Ϊ��
				cardPanel.getBodyItem("remarks").setEnabled(false);			//���ñ��屸ע���ɱ༭��Ϊ��
				for(int x=0;x<12;x++){
					cardPanel.setBodyValueAt(new UFDouble(0), x, "monthlylimit","pk_carinfo_b");
					cardPanel.setBodyValueAt(null, x, "remarks","pk_carinfo_b");
				}
				
			} else {
				cardPanel.getHeadItem("accyear66").setEnabled(true); // ���û���겻�ɱ༭
				cardPanel.getHeadItem("beginaccmonth").setEnabled(true); // �������û���²��ɱ༭
				cardPanel.getHeadItem("annuallimit").setEnabled(true); // �������޶�ɱ༭
				cardPanel.getBodyItem("monthlylimit").setEnabled(true);
				cardPanel.getBodyItem("remarks").setEnabled(true);
			}
		} else if ("opertype".equals(key)){	//��������
			if(CarInforConstants.opertype_add == ((Integer)obj)){
				cardPanel.getHeadItem("pk_car99").setEnabled(false); // ���ó������ɱ༭��Ϊ��
				cardPanel.setHeadItem("pk_car99", null);
			}
			
		}else if("pk_car99".equals(key)){
			//cardPanel.setHeadItem("pk_car99", obj.toString());	//��֪Ϊ�����ò���
			//ѡ�������Զ��������������롱�͡��������ơ�
			List<HashMap<String, String>> carinfos = new ArrayList<HashMap<String,String>>();
			String pk_car_limit  = cardPanel.getHeadItem("accyear66").getValueObject().toString();
			String pk_car =  (String) obj; 
			Object jknObj = cardPanel.getHeadItem("accyear66").getValueObject();
			if(!StringUtil.isEmpty(pk_car)){
				
				IQueryBillService queyr= NCLocator.getInstance().lookup(IQueryBillService.class);
				//ѡ�񡰳����������ѡ��ĳ�������ͬ��������֯����ͬ������ꡱ�ġ������޶��������ơ������Զ������������޶��������ơ������������޶�޶���ݵġ������޶��������ơ��������޶�����޶�ֶΡ�
				if(jknObj!=null){
					int kjn =(Integer)jknObj;
					String querySql = "select  lh.pk_car_limit,lh.���޶�,lb.limit_e,lb.kjy from erm_car_limit  lh inner join erm_car_limit_b lb on lh.pk_car_limit=lb.pk_car_limit  where lh.pk_org='"
					+pk_org+"' and lh.kjni='"+kjn+"' and lh.car='"+pk_car+"' and lh.dr=0 and lb.dr=0;";
					
					ArrayList<HashMap<String,Object>> infos = new ArrayList<HashMap<String,Object>>();
					try {
						infos= (ArrayList<HashMap<String, Object>>) queryService.executeQuery(querySql, new MapListProcessor());
					} catch (BusinessException e1) {
						e1.printStackTrace();
					}
					if(infos.size()>0){
						if(infos.get(0).get("���޶�")!=null){
							cardPanel.getHeadItem("annuallimit").setValue(new UFDouble((infos.get(0).get("���޶�").toString())));
						}
						
						HashMap<String, Object> info= infos.get(0);
						for(int x=0;x<infos.size();x++){
							if(info.get("limit_e")!=null){
								cardPanel.setBodyValueAt(new UFDouble(info.get("limit_e").toString()), x, "monthlylimit","pk_carinfo_b");
							}	
						}
						cardPanel.getHeadItem("pk_car_limit").setValue((infos.get(0).get("pk_car_limit").toString()));	//��д����
					}else{
						for(int x=0;x<12;x++){
							cardPanel.setBodyValueAt(new UFDouble(0), x, "monthlylimit","pk_carinfo_b");//	�����������ޡ������޶��������ơ���¼�����塰���޶����Ϊ0��		
						}
						cardPanel.getHeadItem("pk_car_limit").setValue(null);	//�鲻���������
					}
					
				}
				//ѡ���������ó������롢�������ơ��ϼ�����
				String queryNameCodeSql = " select code,name,pid from bd_defdoc where pk_defdoc='"+pk_car+"';";	//���ճ����ĳ�Ӧ�������õ�
				ArrayList<HashMap<String,String>> cnInfo = new ArrayList<HashMap<String,String>>();
				try {
					cnInfo= (ArrayList<HashMap<String, String>>) queryService.executeQuery(queryNameCodeSql, new MapListProcessor());
				} catch (BusinessException e1) {
					e1.printStackTrace();
				}
				//DefdocVO defVO= (DefdocVO)queyr.queryBill(DefdocVO.class, pk_car);
				if(cnInfo.size()>0){
					cardPanel.setHeadItem("carcode", cnInfo.get(0).get("code"));	//��������
					cardPanel.setHeadItem("carname", cnInfo.get(0).get("name"));	//��������
					cardPanel.setHeadItem("pk_pcar88", cnInfo.get(0).get("pid"));	//�ϼ�����
				}
			}
					
		}else if("beginaccmonth".equals(key) && obj!=null ){
			//���û���£����ĸ��¿�ʼ��ֵ��
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
			//�������޶�
			int annuallimit = ((UFDouble)obj).intValue();
			int  month =  cardPanel.getHeadItem("beginaccmonth").getValueObject()==null?0:(Integer)cardPanel.getHeadItem("beginaccmonth").getValueObject();
			int mod = annuallimit%(13-month);
			int monthlylimit = (annuallimit - mod)/(13-month);
			if(month>0){//���������������
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
				//δ�������������
				for(int x=0;x<12;x++){
					if(x!=11){
						cardPanel.setBodyValueAt(new UFDouble(monthlylimit), x, "monthlylimit","pk_carinfo_b");
					}else{
						cardPanel.setBodyValueAt(new UFDouble(monthlylimit+mod), x, "monthlylimit","pk_carinfo_b");
					}		
				}			
			}		
		}else if("carcode".equals(key)){
			//�ظ���У�飺�������������ƺŲ�����ͬ����
			Object operatetyp = cardPanel.getHeadItem("opertype").getValueObject();
			if(operatetyp!=null){
				if(nc.itf.erm.constants.CarInforConstants.opertype_add == (Integer)operatetyp){	//
					String carcode =  cardPanel.getHeadItem("carcode").getValueObject().toString();
					String sql  =  "select doc.name  from bd_defdoc doc left join bd_defdoclist lst on doc.pk_defdoclist=lst.pk_defdoclist where  lst.name = '������Ϣ' and doc.code='"+carcode+"';";
					String name = null;
					try {
						  name = (String) queryService.executeQuery(sql, new ColumnProcessor(1));
					} catch (BusinessException e1) {
						e1.printStackTrace();
					}
					if(name!=null){
						nc.ui.pub.beans.MessageDialog.showWarningDlg(cardPanel.getParent(), "��ʾ����", "�Ѵ�����ͬ�ĳ��ƺţ�����ͣ�øó��ƺţ�������������Ϣ~");
					}
				}else{
					
				}
			}
			
			
		}
	}
	

}
