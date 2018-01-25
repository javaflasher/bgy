package nc.vo.erm.carinfo;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggCarInfoVOMeta extends AbstractBillMeta {
	public AggCarInfoVOMeta() {
		this.init();
	}

	private void init() {
		this.setParent(nc.vo.erm.carinfo.CarInfoVO.class);
		this.addChildren(nc.vo.erm.carinfo.CarInfoBodyVO.class);
	}
}