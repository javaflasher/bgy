package nc.itf.erm;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.erm.carinfo.AggCarInfoVO;
import nc.vo.pub.BusinessException;

public interface ICarinfoMaintain {

	public void delete(AggCarInfoVO[] clientFullVOs, AggCarInfoVO[] originBills)
			throws BusinessException;

	public AggCarInfoVO[] insert(AggCarInfoVO[] clientFullVOs,
			AggCarInfoVO[] originBills) throws BusinessException;

	public AggCarInfoVO[] update(AggCarInfoVO[] clientFullVOs,
			AggCarInfoVO[] originBills) throws BusinessException;

	public String[] queryPKs(IQueryScheme queryScheme) throws BusinessException;

	public AggCarInfoVO[] queryBillByPK(String[] pks) throws BusinessException;

	public AggCarInfoVO[] save(AggCarInfoVO[] clientFullVOs,
			AggCarInfoVO[] originBills) throws BusinessException;

	public AggCarInfoVO[] unsave(AggCarInfoVO[] clientFullVOs,
			AggCarInfoVO[] originBills) throws BusinessException;

	public AggCarInfoVO[] approve(AggCarInfoVO[] clientFullVOs,
			AggCarInfoVO[] originBills) throws BusinessException;

	public AggCarInfoVO[] unapprove(AggCarInfoVO[] clientFullVOs,
			AggCarInfoVO[] originBills) throws BusinessException;
}
