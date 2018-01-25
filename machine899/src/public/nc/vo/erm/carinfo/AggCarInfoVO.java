package nc.vo.erm.carinfo;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.erm.carinfo.CarInfoVO")
public class AggCarInfoVO extends AbstractBill {

	@Override
	public IBillMeta getMetaData() {
		IBillMeta billMeta = BillMetaFactory.getInstance().getBillMeta(
				AggCarInfoVOMeta.class);
		return billMeta;
	}

	@Override
	public CarInfoVO getParentVO() {
		return (CarInfoVO) this.getParent();
	}
}