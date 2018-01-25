	package nc.impl.pub.ace;

import nc.bs.erm.carinfo.ace.bp.AceCarinfoDeleteBP;
import nc.bs.erm.carinfo.ace.bp.AceCarinfoInsertBP;
import nc.bs.erm.carinfo.ace.bp.AceCarinfoUpdateBP;
import nc.bs.erm.carinfo.ace.bp.AceCarinfoSendApproveBP;
import nc.bs.erm.carinfo.ace.bp.AceCarinfoUnSendApproveBP;
import nc.bs.erm.carinfo.ace.bp.AceCarinfoApproveBP;
import nc.bs.erm.carinfo.ace.bp.AceCarinfoUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.vo.erm.carinfo.AggCarInfoVO;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.query2.sql.process.QuerySchemeProcessor;
import nc.impl.pubapp.pattern.database.DataAccessUtils;
import nc.vo.pubapp.pattern.data.IRowSet;
import nc.vo.pubapp.bill.pagination.PaginationTransferObject;
import nc.vo.pubapp.bill.pagination.util.PaginationUtils;
import nc.impl.pubapp.pattern.data.bill.BillQuery;

public abstract class AceCarinfoPubServiceImpl {
	// 新增
	public AggCarInfoVO[] pubinsertBills(AggCarInfoVO[] clientFullVOs,
			AggCarInfoVO[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggCarInfoVO> transferTool = new BillTransferTool<AggCarInfoVO>(
					clientFullVOs);
			// 调用BP
			AceCarinfoInsertBP action = new AceCarinfoInsertBP();
			AggCarInfoVO[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggCarInfoVO[] clientFullVOs,
			AggCarInfoVO[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceCarinfoDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggCarInfoVO[] pubupdateBills(AggCarInfoVO[] clientFullVOs,
			AggCarInfoVO[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggCarInfoVO> transferTool = new BillTransferTool<AggCarInfoVO>(
					clientFullVOs);
			AceCarinfoUpdateBP bp = new AceCarinfoUpdateBP();
			AggCarInfoVO[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 分页查询方法，查询所有PK
	public String[] pubquerypkbills(IQueryScheme queryScheme)
			throws BusinessException {
		// String beanId=(String) queryScheme.get(QueryConstants.BEAN_ID);
		StringBuffer sql = new StringBuffer();
		QuerySchemeProcessor processor = new QuerySchemeProcessor(queryScheme);
		String mainAlias = processor.getMainTableAlias();
		sql.append(" select distinct ");
		sql.append(mainAlias);
		sql.append(".");
		sql.append("pk_carinfo");
		sql.append(processor.getFinalFromWhere());
		DataAccessUtils dao = new DataAccessUtils();
		IRowSet rowset = dao.query(sql.toString());
		String[] keys = rowset.toOneDimensionStringArray();
		return keys;
	}

	// 分页查询方法，根据PK查单据
	public AggCarInfoVO[] pubquerybillbypkbills(String[] pks)
			throws BusinessException {
		AggCarInfoVO[] bills = null;
		BillQuery<AggCarInfoVO> query = new BillQuery<AggCarInfoVO>(
				AggCarInfoVO.class);
		bills = query.query(pks);
		return PaginationUtils.filterNotExistBills(bills, pks);
	}

	// 提交
	public AggCarInfoVO[] pubsendapprovebills(AggCarInfoVO[] clientFullVOs,
			AggCarInfoVO[] originBills) throws BusinessException {
		AceCarinfoSendApproveBP bp = new AceCarinfoSendApproveBP();
		AggCarInfoVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 收回
	public AggCarInfoVO[] pubunsendapprovebills(AggCarInfoVO[] clientFullVOs,
			AggCarInfoVO[] originBills) throws BusinessException {
		AceCarinfoUnSendApproveBP bp = new AceCarinfoUnSendApproveBP();
		AggCarInfoVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggCarInfoVO[] pubapprovebills(AggCarInfoVO[] clientFullVOs,
			AggCarInfoVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceCarinfoApproveBP bp = new AceCarinfoApproveBP();
		AggCarInfoVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

	public AggCarInfoVO[] pubunapprovebills(AggCarInfoVO[] clientFullVOs,
			AggCarInfoVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceCarinfoUnApproveBP bp = new AceCarinfoUnApproveBP();
		AggCarInfoVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}