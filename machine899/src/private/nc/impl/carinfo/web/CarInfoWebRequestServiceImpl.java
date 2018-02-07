package nc.impl.carinfo.web;

import java.util.HashMap;
import java.util.List;

import nc.bs.businessevent.BusinessEvent;
import nc.bs.businessevent.EventDispatcher;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.itf.carinfo.web.ICarInfoWebRequestService;
import nc.itf.erm.ICarinfoMaintain;
import nc.pubitf.cmp.apply.web.ILoginQueryService;
import nc.pubitf.rbac.IDataPermissionPubService;
import nc.ssc.fiweb.pub.RequestParam;
import nc.vo.arap.basebill.BaseAggVO;
import nc.vo.erm.carinfo.AggCarInfoVO;
import nc.vo.erm.carinfo.CarInfoBodyVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.web.arap.bill.pub.WebBillTypeFactory;
import nc.web.datatrans.itf.ITranslateDataService;
import nc.web.datatrans.itf.ITranslateVODataService;
import org.codehaus.jettison.json.JSONObject;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.codecs.OracleCodec;
import uap.iweb.exception.WebRuntimeException;
import uap.iweb.log.Logger;
import uap.iweb.pub.bill.BillItem;
import uap.iweb.pub.bill.util.BillTemplateWebUtil;
import uap.web.util.Coder;

public class CarInfoWebRequestServiceImpl implements ICarInfoWebRequestService {

	@Override
	public String sendBillSave(RequestParam request) throws Exception {
		boolean compression = "true".equals(request.getParameter("compression"));
		String jsnObj = request.getParameter("bill");
		String pk_billtemplet = request.getParameter("pk_billtemplet");
		String compressType = request.getParameter("compressType");
		if (compression) {
			jsnObj = Coder.decode(jsnObj, compressType);
		}
		List<BillItem> billItems = BillTemplateWebUtil.getBillItems(null,pk_billtemplet);
		AggCarInfoVO aggvo = NCLocator.getInstance().lookup(ITranslateDataService.class).translateJsonToAggvo(AggCarInfoVO.class, jsnObj); // 工具类获取vo
		AggCarInfoVO[] aggvos = { aggvo };
		dealAggVO(aggvo);// 处理一下
		ICarinfoMaintain carinfoMaintain = NCLocator.getInstance().lookup(
				ICarinfoMaintain.class);
		try {
			aggvos = carinfoMaintain.insert(aggvos, aggvos);
			EventDispatcher.fireEvent(new BusinessEvent("060f5b73-457d-4875-b036-269c80d3aa4c", "1004", aggvos)); // 新增保存后更新redis
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		JSONObject returnJson = new JSONObject();
		if (billItems == null) {
			returnJson = NCLocator.getInstance()
					.lookup(ITranslateVODataService.class)
					.transAggvoToJSON(aggvos[0]);
		} else {
			returnJson = NCLocator.getInstance().lookup(ITranslateVODataService.class).transAggvoToJSON(aggvos[0], billItems);
		}
		returnJson.put("success", "true");
		return returnJson.toString();
	}

	@Override
	public String commit(RequestParam request) throws Exception {

		JSONObject jsonObj = new JSONObject();
		String openbillid = ESAPI.encoder().encodeForSQL(new OracleCodec(),request.getParameter("pk_bill"));
		String pk_billtemplet = ESAPI.encoder().encodeForSQL(new OracleCodec(),request.getParameter("pk_billtemplet"));
		List<BillItem> billItems = uap.iweb.pub.bill.util.BillTemplateWebUtil.getBillItems(null, pk_billtemplet);
		AggCarInfoVO[] aggVOs = NCLocator.getInstance().lookup(ICarinfoMaintain.class).queryBillByPK(new String[] { openbillid });

		if (aggVOs == null || aggVOs.length == 0 || aggVOs[0] == null|| aggVOs[0].getParentVO().getBillstatus() != 1) {
			throw new BusinessException("单据状态已改变，请刷新界面！");
		}
		
		WorkflownoteVO wfnoteVO = new WorkflownoteVO();
		wfnoteVO.setPk_org(aggVOs[0].getParentVO().getPk_org());
		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put(PfUtilBaseTools.PARAM_SILENTLY,PfUtilBaseTools.PARAM_SILENTLY);
		NCLocator.getInstance().lookup(ILoginQueryService.class).processBatch("SAVE", "CARI-Cxx-001", wfnoteVO, aggVOs, null,paramMap);

		/*
		 * IplatFormEntry iIplatFormEntry = (IplatFormEntry) NCLocator
		 * .getInstance().lookup(IplatFormEntry.class.getName()); retObj =
		 * iIplatFormEntry.processAction(actionCode, billOrTranstype,
		 * worknoteVO, billvo, userObj, eParam);
		 */
		AggCarInfoVO[] returnVOs = NCLocator.getInstance().lookup(ICarinfoMaintain.class).queryBillByPK(new String[] { openbillid });
		// 更新redis
		EventDispatcher.fireEvent(new BusinessEvent("e53252e6-3165-440e-926b-d1f55ba60fe2", "1004", returnVOs));

		if (billItems == null) {
			jsonObj = NCLocator.getInstance().lookup(ITranslateVODataService.class).transAggvoToJSON(returnVOs[0]);
			;
		} else {
			jsonObj = NCLocator.getInstance().lookup(ITranslateVODataService.class).transAggvoToJSON(returnVOs[0], billItems);
		}
		jsonObj.put("success", "true");

		return jsonObj.toString();
	}

	@Override
	public String recall(RequestParam request) throws Exception {

		JSONObject jsonObj = new JSONObject();
		String openbillid = ESAPI.encoder().encodeForSQL(new OracleCodec(),request.getParameter("pk_bill"));
		String pk_billtemplet = ESAPI.encoder().encodeForSQL(new OracleCodec(),request.getParameter("pk_billtemplet"));
		List<BillItem> billItems = uap.iweb.pub.bill.util.BillTemplateWebUtil.getBillItems(null, pk_billtemplet);

		AggCarInfoVO[] aggVOs = NCLocator.getInstance().lookup(ICarinfoMaintain.class).queryBillByPK(new String[] { openbillid });

		if (aggVOs != null && aggVOs.length > 0 && aggVOs[0] != null
				&& aggVOs[0].getParentVO().getApprovestatus() == 3) {
			try {
				// 单据状态是否未审批中
				HashMap<String, String> paramMap = new HashMap<String, String>();
				paramMap.put(PfUtilBaseTools.PARAM_SILENTLY,
						PfUtilBaseTools.PARAM_SILENTLY);
				WorkflownoteVO workflownoteVO = new WorkflownoteVO();
				NCLocator.getInstance().lookup(ILoginQueryService.class).processBatch("UNSAVEBILL", "CARI-Cxx-001",workflownoteVO, aggVOs, null, paramMap);
				AggCarInfoVO[] returnvo = NCLocator.getInstance().lookup(ICarinfoMaintain.class).queryBillByPK(new String[] { openbillid });
				// 更新redis
				EventDispatcher.fireEvent(new BusinessEvent("e53252e6-3165-440e-926b-d1f55ba60fe2", "1004",returnvo));
				AggCarInfoVO[] newAggVOs = NCLocator.getInstance().lookup(ICarinfoMaintain.class).queryBillByPK(new String[] { openbillid });
				if (billItems == null) {
					jsonObj = NCLocator.getInstance().lookup(ITranslateVODataService.class).transAggvoToJSON(newAggVOs[0]);
				} else {
					jsonObj = NCLocator.getInstance().lookup(ITranslateVODataService.class).transAggvoToJSON(newAggVOs[0], billItems);
				}
				jsonObj.put("success", "true");
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
				throw new BusinessException("单据状态已改变，请刷新界面！");
			}

		} else {
			throw new BusinessException("单据状态已改变，请刷新界面！");
		}
		return jsonObj.toString();
	}

	@Override
	public String update(RequestParam request) throws Exception {

		boolean compression = "true".equals(request.getParameter("compression"));
		String jsnObj = request.getParameter("bill");
		String pk_billtemplet = ESAPI.encoder().encodeForSQL(new OracleCodec(),request.getParameter("pk_billtemplet"));
		String compressType = request.getParameter("compressType");
		String openbillid = ESAPI.encoder().encodeForSQL(new OracleCodec(),request.getParameter("openbillid"));
		if (compression) {
			jsnObj = Coder.decode(jsnObj, compressType);
		}
		List<BillItem> billItems = BillTemplateWebUtil.getBillItems(null,pk_billtemplet);

		AggCarInfoVO aggvo = NCLocator.getInstance()
				.lookup(ITranslateDataService.class)
				.translateJsonToAggvo(AggCarInfoVO.class, jsnObj); // 工具类获取vo
		AggCarInfoVO[] originAggvos = NCLocator.getInstance().lookup(ICarinfoMaintain.class).queryBillByPK(new String[] { openbillid });
		AggCarInfoVO[] aggvos = { aggvo };
		AggCarInfoVO[] newAggvos = null;
		// dealAggVO(aggvo);//处理一下
		ICarinfoMaintain carinfoMaintain = NCLocator.getInstance().lookup(
				ICarinfoMaintain.class);
		try {
			aggvos[0].getParent().setStatus(VOStatus.UPDATED);// 要设置为更新才可以更新，真nm坑
			CarInfoBodyVO[] body = (CarInfoBodyVO[]) aggvos[0].getChildrenVO();
			for (int x = 0; x < body.length; x++) {
				body[x].setStatus(VOStatus.UPDATED);// naive， 还有表体！
			}
			newAggvos = carinfoMaintain.update(aggvos, originAggvos);
			body = (CarInfoBodyVO[]) newAggvos[0].getChildrenVO();
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		JSONObject returnJson = new JSONObject();
		returnJson = NCLocator.getInstance().lookup(ITranslateVODataService.class).transAggvoToJSON(newAggvos[0]);
		returnJson.put("success", "true");
		return returnJson.toString();
	}

	@Override
	public String view(RequestParam request) throws Exception {

		String billId = request.getParameter("openbillid");
		// JSONObject json = new JSONObject();
		String openbillid = request.getParameter("openbillid");
		String pk_billtemplet = request.getParameter("pk_billtemplet");
		String jsonString = "";
		List<BillItem> billItems = uap.iweb.pub.bill.util.BillTemplateWebUtil
				.getBillItems(null, pk_billtemplet);
		try {
			AggCarInfoVO[] aggVOs = NCLocator.getInstance()
					.lookup(ICarinfoMaintain.class)
					.queryBillByPK(new String[] { openbillid });
			if (aggVOs != null && aggVOs.length > 0) {
				JSONObject jsonObj = new JSONObject();
				if (billItems != null) {
					jsonObj = NCLocator.getInstance().lookup(ITranslateVODataService.class).transAggvoToJSON(aggVOs[0],billItems);
				} else {
					jsonObj = NCLocator.getInstance().lookup(ITranslateVODataService.class).transAggvoToJSON(aggVOs[0]);
				}
				if (jsonObj != null) {
					jsonObj.put("success", "true");
					jsonString = jsonObj.toString();
				} else {
					jsonObj.put("success", "false");
					jsonString = jsonObj.toString();
				}
			} else {
				String operate = "$.removeWaiting();window.close();"; // 关闭当前页面
				WebRuntimeException exception = new WebRuntimeException("未查询到单据");
				exception.setOperate(operate);
				throw exception;
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		}
		return jsonString;
	}

	@Override
	public String delete(RequestParam request) throws Exception {

		ICarinfoMaintain carinfoMaintain = NCLocator.getInstance().lookup(ICarinfoMaintain.class);

		String openbillid = ESAPI.encoder().encodeForSQL(new OracleCodec(),request.getParameter("openbillid"));
		String pk_billtemplet = ESAPI.encoder().encodeForSQL(new OracleCodec(),request.getParameter("pk_billtemplet"));
		String jsonString = "";
		List<BillItem> billItems = uap.iweb.pub.bill.util.BillTemplateWebUtil.getBillItems(null, pk_billtemplet);
		try {
			AggCarInfoVO[] aggVOs = NCLocator.getInstance().lookup(ICarinfoMaintain.class).queryBillByPK(new String[] { openbillid });
			if (aggVOs[0].getParentVO().getBillstatus() != 1 || aggVOs[0].getParentVO().getApprovestatus()!=-1) {
				throw new BusinessException("非自由态单据不可删除！");	
			}
			carinfoMaintain.delete(aggVOs, aggVOs);
			if (aggVOs != null && aggVOs.length > 0) {
				JSONObject jsonObj = new JSONObject();
				if (billItems != null) {
					jsonObj = NCLocator.getInstance().lookup(ITranslateDataService.class).transAggvoToJSON(billItems, aggVOs[0]);
				} else {
					jsonObj = NCLocator.getInstance().lookup(ITranslateDataService.class).transAggvoToJSON(aggVOs[0]);
				}
				if (jsonObj != null) {
					jsonObj.put("success", "true");
					jsonString = jsonObj.toString();
				} else {
					jsonObj.put("success", "false");
					jsonString = jsonObj.toString();
				}
			} else {
				String operate = "$.removeWaiting();window.close();"; // 关闭当前页面
				WebRuntimeException exception = new WebRuntimeException("未查询到单据");
				exception.setOperate(operate);
				throw exception;
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		}
		return jsonString;
	}

	private void checkPermissionForEdit(String actioncode, String actionName,
			BaseAggVO vo) throws BusinessException {
		String cuserid = InvocationInfoProxy.getInstance().getUserId();
		String groupId = InvocationInfoProxy.getInstance().getGroupId();
		String billType = vo.getHeadVO().getPk_billtype();
		String resourceCode = getResourceCodeByBillType(billType);
		if (!NCLocator.getInstance().lookup(IDataPermissionPubService.class).isUserhasPermission(resourceCode,vo.getHeadVO().getPrimaryKey(), actioncode, groupId,
						cuserid)) {
			throw new BusinessException("当前用户没有操作该单据" + actionName + "的权限");
		}
	}

	private static String getResourceCodeByBillType(String billType)
			throws BusinessException {
		return WebBillTypeFactory.getWebBillType(billType).getResourceCode();
	}

	private void dealAggVO(AggCarInfoVO aggvo) {
		// aggvo.getParentVO().setBillstatus(CarInforConstants.billstatus_wsp);
		// aggvo.getParentVO().setApprovestatus(CarInforConstants.approval_free);
	}

	@Override
	public String edit(RequestParam request) throws Exception {
		JSONObject jsonObj = new JSONObject();
		String openbillid = request.getParameter("openbillid");
		AggCarInfoVO[] aggvos = NCLocator.getInstance()
				.lookup(ICarinfoMaintain.class)
				.queryBillByPK(new String[] { openbillid });
		if (aggvos == null || aggvos.length == 0 || aggvos[0] == null
				|| aggvos[0].getParentVO().getApprovestatus() == 3) {
			throw new BusinessException("单据状态已改变，请刷新界面！");
		}
		jsonObj.put("success", "true");
		return jsonObj.toString();
	}
}
