package nc.ui.erm.carinfo.ace.handler;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;
import nc.vo.erm.carinfo.CarInfoBodyVO;
import nc.vo.pub.lang.UFDouble;

/**
 * 单据表体字段编辑后事件
 * 
 * @since 6.0
 * @version 2011-7-12 下午08:17:33
 * @author duy
 */
public class AceBodyAfterEditHandler implements
		IAppEventHandler<CardBodyAfterEditEvent> {

	@SuppressWarnings("restriction")
	@Override
	public void handleAppEvent(CardBodyAfterEditEvent e) {
		//String key = e.getKey();
		BillCardPanel cardPanel = e.getBillCardPanel();
		String key = e.getKey();
		Object obj = e.getValue();
		if("monthlylimit".equals(key)){
			//合计金额
			UFDouble amount  = new UFDouble(0);
			for(int x=0;x<12;x++){
				Object money = cardPanel.getBodyValueAt(x, "monthlylimit");
				if(money!=null){
					amount = amount.add(new UFDouble(money.toString())) ;
				}				
			}
			cardPanel.setHeadItem("annuallimit", amount);
		}
	}

}
