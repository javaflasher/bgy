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
		// 设置主组织默认值
		panel.setHeadItem("pk_group", pk_group);
		panel.setHeadItem("pk_org", pk_org);
		// 设置单据状态、日期默认值 approvestatus
		panel.setHeadItem("approvestatus", -1);//新增时单据审批状态为自由态
		panel.setHeadItem("billstatus", 1);//新增时单据状态未审批 
		panel.setHeadItem("billdate", AppContext.getInstance().getBusiDate());
		panel.setTailItem("maketime", AppContext.getInstance().getBusiDate());
		InvocationInfoProxy.getInstance().getUserCode();
		//选择主财务组织后，添加表体行
		nc.ui.pubapp.uif2app.view.BillForm  billForm = e.getBillForm();
		
		for(int x=0;x<12;x++){
			
			billForm.getBillCardPanel().addLine("pk_carinfo_b");
			billForm.getBillCardPanel().setBodyValueAt(10*(x+1), x, "rowno");	//行号
			billForm.getBillCardPanel().setBodyValueAt(x+1, x, "accmonth");		//会计月
			billForm.getBillCardPanel().setBodyValueAt(new UFDouble(), x, "monthlylimit");	//月限额
			billForm.getBillCardPanel().setBodyValueAt(new UFDateTime(), x, "ts");	//月限额
			
		}
	}

}
