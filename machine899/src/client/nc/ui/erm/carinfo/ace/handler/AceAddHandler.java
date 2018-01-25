package nc.ui.erm.carinfo.ace.handler;

import com.jacob.com.InvocationProxy;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.billform.AddEvent;
import nc.vo.erm.carinfo.CarInfoVO;
import nc.vo.pub.pf.BillStatusEnum;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.AppContext;

public class AceAddHandler implements IAppEventHandler<AddEvent> {

	@Override
	public void handleAppEvent(AddEvent e) {
		String pk_group = e.getContext().getPk_group();
		String pk_org = e.getContext().getPk_org();
		BillCardPanel panel = e.getBillForm().getBillCardPanel();
		// ��������֯Ĭ��ֵ
		panel.setHeadItem("pk_group", pk_group);
		panel.setHeadItem("pk_org", pk_org);
		// ���õ���״̬������Ĭ��ֵ approvestatus
		panel.setHeadItem("approvestatus", -1);//����ʱ��������״̬Ϊ����̬
		panel.setHeadItem("billstatus", 1);//����ʱ����״̬δ���� 
		panel.setHeadItem("billdate", AppContext.getInstance().getBusiDate());
		panel.setTailItem("maketime", AppContext.getInstance().getBusiDate());
		InvocationInfoProxy.getInstance().getUserCode();
		//ѡ����������֯����ӱ�����
		nc.ui.pubapp.uif2app.view.BillForm  billForm = e.getBillForm();
		
		for(int x=0;x<12;x++){
			
			billForm.getBillCardPanel().addLine("pk_carinfo_b");
			billForm.getBillCardPanel().setBodyValueAt(10*(x+1), x, "rowno");	//�к�
			billForm.getBillCardPanel().setBodyValueAt(x+1, x, "accmonth");		//�����
			billForm.getBillCardPanel().setBodyValueAt(new UFDouble(), x, "monthlylimit");	//���޶�
			billForm.getBillCardPanel().setBodyValueAt(new UFDateTime(), x, "ts");	//���޶�
			
		}
	}

}
