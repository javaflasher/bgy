package uap.iweb.wf.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bs.er.sscbillmsg.service.SscBillMsgService;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pf.pub.PfDataCache;
import nc.bs.pub.pf.PfUtilTools;
import nc.itf.uap.pf.IPFConfig;
import nc.itf.uap.pf.IWorkflowDefine;
import nc.itf.uap.pf.IWorkflowMachine;
import nc.itf.uap.pf.metadata.IFlowBizItf;
import nc.md.data.access.NCObject;
import nc.md.model.IBean;
import nc.pub.reject.util.FlowStateConstant;
import nc.pubitf.reverseflow.srv.IPubReverseFlowService;
import nc.uap.cpb.log.CpLogger;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.billtype2.Billtype2VO;
import nc.vo.pub.billtype2.ExtendedClassEnum;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.rejectitem.entity.RejectItemVO;
import nc.vo.rejectitem.enumerate.BModifiedScope;
import nc.vo.rejectsetting.entity.AggRejectSettingVO;
import nc.vo.rejectsetting.entity.RejectSettingItemVO;
import nc.vo.rejectsetting.entity.RejectSettingVO;
import nc.vo.uap.pf.PFRuntimeException;
import nc.vo.wfengine.core.activity.Activity;
import nc.vo.wfengine.definition.WorkflowTypeEnum;
import nc.vo.wfengine.pub.WfTaskType;
import nc.web.datatrans.itf.ITranslateVODataService;
import nc.wf.SSCWFConst;
import nc.wf.SscObject;
import nc.wf.reject.SSCFlowLinkConst;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import uap.iweb.wf.itf.IWorkflowExcutor;
import uap.iweb.wf.util.IWFHintMessageCallback;
import uap.iweb.wf.util.WFAlertExceptionUtil;
import uap.iweb.wf.util.WFAlertResult;
import uap.iweb.wf.vo.IWorkFlowWebConst;
import uap.iweb.wf.vo.WFActivityInfo;
import uap.iweb.wf.vo.WFAssignInfo;
import uap.iweb.wf.vo.WorkFlowQueryUtil;
import uap.iweb.wf.vo.WorkNoteVO;
import uap.lfw.dbl.cpdoc.util.DocCommonUtil;
import uap.lfw.dbl.vo.MetaDataBaseAggVO;

import com.google.gson.Gson;

@Controller
@RequestMapping("/wf_ctr")
public class WorkFlowController {

	@RequestMapping(value = "/workflownoteQry", method = RequestMethod.GET)
	public void queryWorkflowInfoByBillID(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String billID = (String) request.getParameter("billID");
		String userid = InvocationInfoProxy.getInstance().getUserId();
		// String userid = (String) request.getParameter("userid");
		String billtype = (String) request.getParameter("billtype");
		if (checkParamNotNull(billID, userid, billtype)) {
			Map<String, Object> result = new WorkFlowQueryUtil()
					.queryWorkflowInfoByBillID(billID, userid, billtype);
			writeToResponse(response, result);
		} else {
			Logger.error("参数不完整，请检查！");
		}
	}
	
	@RequestMapping(value = "/workflowHistoryQry", method = RequestMethod.GET)
	public void queryWorkflowHistoryByBillID(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String billID = (String) request.getParameter("billID");
		String userid = InvocationInfoProxy.getInstance().getUserId();
		// String userid = (String) request.getParameter("userid");
		String billtype = (String) request.getParameter("billtype");
		if (checkParamNotNull(billID, userid, billtype)) {
			Map<String, Object> result = new WorkFlowQueryUtil().queryWorkflowInfoByBillID(billID, userid, billtype);
			
			List<WorkNoteVO> finishTasks  = (List<WorkNoteVO>)result.get(IWorkFlowWebConst.HISTORYTASK);
			
			writeToResponse(response, finishTasks);
		} else {
			Logger.error("参数不完整，请检查！");
		}
	}

	@RequestMapping(value = "/workflowHistoryImg", method = RequestMethod.GET)
	public void queryWorkflowHistoryImg(HttpServletRequest request,	HttpServletResponse response) throws Exception {
		String billID = (String) request.getParameter("billID");
//		String userid = InvocationInfoProxy.getInstance().getUserId();
		// String userid = (String) request.getParameter("userid");
		String billtype = (String) request.getParameter("billtype");
		
		
//		IWorkflowDefine wfDefine = new WorkflowDefineImpl();
		IWorkflowDefine wfDefine = NCLocator.getInstance().lookup(IWorkflowDefine.class);
		try {
			byte[] image = wfDefine.toPNGImage(billID, billtype, WorkflowTypeEnum.Workflow.getIntValue());
			response.setContentType("image/jpeg");  
			ServletOutputStream outStream = response.getOutputStream();
			outStream.write(image, 0, image.length);
			outStream.flush();
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	/**
	 * 获得指派信息
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/getAssignInfo", method = RequestMethod.GET)
	public void getAssignInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String pk_workflownote = (String) request
				.getParameter("pk_workflownote");
		// String userid = (String) request.getParameter("userid");
		String userid = InvocationInfoProxy.getInstance().getUserId();
		if (checkParamNotNull(userid, pk_workflownote)) {
			List<WFAssignInfo> result = new WorkFlowQueryUtil().getAssignInfo(
					userid, pk_workflownote);
			writeToResponse(response, result);
		} else {
			Logger.error("参数不完整，请检查！");
		}
	}

	/**
	 * 获取驳回的流程
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/getRejectActivity", method = RequestMethod.GET)
	public void getRejectActivity(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String pk_workflownote = (String) request
				.getParameter("pk_workflownote");
		// String userid = (String) request.getParameter("userid");
		String userid = InvocationInfoProxy.getInstance().getUserId();
		String isAccountExamAct = (String) request.getParameter("isAccountExamAct");
		String billId= (String) request.getParameter("billID");
		String billType= (String) request.getParameter("transType");
		String isreject= (String) request.getParameter("isreject");
		if (checkParamNotNull(userid, pk_workflownote)) {
			IWorkflowDefine workflowDef = NCLocator.getInstance().lookup(IWorkflowDefine.class);
			List<Activity[]> activitys = null;
			try {
				activitys = workflowDef.queryFinishedActivities(billId, billType, WorkflowTypeEnum.Workflow.getIntValue());
			} catch (BusinessException e1) {
				// TODO Auto-generated catch block
				CpLogger.error(e1.getMessage(), e1);
			}
			if("false".equals(isreject)){
				List<WFActivityInfo> result = new WorkFlowQueryUtil()
				.getRejectActvity(userid, pk_workflownote);
				writeToResponse(response, result);

			}else{
			    org.codehaus.jettison.json.JSONArray  result=new org.codehaus.jettison.json.JSONArray ();
				if(activitys!=null&&activitys.size()>0){
					int j=0;
					for(Activity[] act:activitys){
						if(act!=null&&act.length>0){
							for(int i=0; i<act.length;i++){
								org.codehaus.jettison.json.JSONObject json=new  org.codehaus.jettison.json.JSONObject();
								 json.put("activityDefId", act[i].getId());
								 json.put("acitivtyName",act[i].getName());
								 result.put(j,json);
								 j++;
							}
						}
					}
					writeToResponse(response, result);
				}
			}

			
		} else {
			writeToResponse(response, null);
			Logger.error("参数不完整，请检查！");
		}
	}

	@RequestMapping(value = "/doAction", method = RequestMethod.GET)
	public void doWFAction(HttpServletRequest request,
			HttpServletResponse response) throws IOException, BusinessException {
		String pk_workflownote = (String) request.getParameter("pk_workflownote");
		
		
		// String userid = (String) request.getParameter("userid");
		String userid = InvocationInfoProxy.getInstance().getUserId();
		String actionCode = (String) request.getParameter("actionCode");
		String param = URLDecoder.decode(request.getParameter("param"), "UTF-8");
		
		if (checkParamNotNull(userid, pk_workflownote, actionCode, param)) {
			Map<String, String> message = new HashMap<String, String>();
			Gson result = new Gson();
			WFAlertResult wfAlertresult = null;
			try {
				wfAlertresult = this.doAction(actionCode, userid, pk_workflownote, param);
				//add by zhongzh7  申请请求驳回单据，驳回时删除请求驳回信息 2017.09.12 start
				String apbillpk = request.getParameter("apbillpk");
				SscBillMsgService sscBillMsgService = NCLocator.getInstance().lookup(SscBillMsgService.class);
				sscBillMsgService.deleteMesgByMesgId(apbillpk);
				//add by zhongzh7 钟泽辉  申请请求驳回单据，驳回时删除请求驳回信息2017.09.12 end
			}
			catch (PFRuntimeException e) {
				Logger.debug("###########doAction 操作失败,原因:" + e.getLocalizedMessage());
				throw new BusinessException("发生未知原因错误,流程失败,请尝试刷新界面！详细错误请查看日志。");
			}
			catch (BusinessException e) {
				// writeToResponse(response, result.toJson(message));
				Logger.debug("###########doAction 操作失败,原因:"+ e.getLocalizedMessage());
				Throwable throwable = ExceptionUtils.unmarsh(e);
				throw new BusinessException(e.getMessage() == null ? "审批失败,发生未知原因错误!": throwable.getMessage());
			}
			
			message.put("success", "true");
			
			// 增加柔性功能
			if (wfAlertresult != null) {
				HashMap<String, Object> eParm = new HashMap<String, Object>();
				eParm.put("alter_force_pass", wfAlertresult.getExceptionClass());
				JSONObject  jsonArray = JSONObject.fromObject(eParm);
				message.put("isSoAlter", "true");
				message.put("eParam", jsonArray.toString());
				message.put("message", wfAlertresult.getMessage());
			}

			writeToResponse(response, result.toJson(message));
		} else {
			Logger.error("参数不完整，请检查！");
		}
	}

	private void writeToResponse(HttpServletResponse response, Object result)
			throws IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(new Gson().toJson(result));
		response.flushBuffer();
	}

	private boolean checkParamNotNull(String... params) {
		for (String param : params) {
			if (StringUtils.isBlank(param)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 解码
	 * 
	 * @param src <String> 解码前字符串
	 * @author <String> 解码后字符串
	 */
	public static String unescape(String src) {
		if(src == null){
			return null;
		}
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length());
		int lastPos = 0, pos = 0;
		char ch;
		while (lastPos < src.length()) {
			pos = src.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (src.charAt(pos + 1) == 'u') {
					ch = (char) Integer.parseInt(
							src.substring(pos + 2, pos + 6), 16);
					tmp.append(ch);
					lastPos = pos + 6;
				} else {
					ch = (char) Integer.parseInt(
							src.substring(pos + 1, pos + 3), 16);
					tmp.append(ch);
					lastPos = pos + 3;
				}
			} else {
				if (pos == -1) {
					tmp.append(src.substring(lastPos));
					lastPos = src.length();
				} else {
					tmp.append(src.substring(lastPos, pos));
					lastPos = pos;
				}
			}
		}
		return tmp.toString();
	}
	
	private WFAlertResult doAction(String actionCode, String pk_user, String pk_workflownote, String param) throws BusinessException {
		WFAlertResult ret = null;
		Map<String, Object> paramMap = readFromJson(param);
		String json = (String)paramMap.get("eParam");
		HashMap<String, Object> eParam = (HashMap<String, Object>)readFromJson(json);
		eParam = eParam == null ? new HashMap<String, Object>() : eParam;
		IWorkflowExcutor workFlowExcutor = this.getService();
		
		String note = unescape((String) paramMap.get(IWorkFlowWebConst.PARAM_NOTE));
		
		if (IWorkFlowWebConst.ACTION_AGREE.equals(actionCode)) {
			Map<String, List<String>> task_userids_map = (Map<String, List<String>>) paramMap.get(IWorkFlowWebConst.PARAM_ASSIGNINFO);
			ret = workFlowExcutor.doAgree(pk_user, pk_workflownote, note, task_userids_map, eParam);
		} else if (IWorkFlowWebConst.ACTION_DISAGREE.equals(actionCode)) {
			Map<String, List<String>> task_userids_map = (Map<String, List<String>>) paramMap.get(IWorkFlowWebConst.PARAM_ASSIGNINFO);
			ret = workFlowExcutor.doDisAgree(pk_user, pk_workflownote, note, task_userids_map,eParam);
		} else if (IWorkFlowWebConst.ACTION_REJECT.equals(actionCode)) {
			String pk_active = (String) paramMap.get(IWorkFlowWebConst.PARAM_REJECT_ACTIVITY);
			String isAccountExamAct = (String) paramMap.get("isAccountExamAct");
			if ("Y".equals(isAccountExamAct)) {
				ret = this.doReject(pk_active, note, paramMap, eParam,pk_workflownote);
			} else {
				ret = workFlowExcutor.doReject(pk_user, pk_workflownote, note, pk_active, eParam);
			}
		} else if (IWorkFlowWebConst.ACTION_CANCELAPPROVE.equals(actionCode)) {
			ret = workFlowExcutor.doCancelApprove(pk_user, pk_workflownote, eParam);
		} else if (IWorkFlowWebConst.ACTION_REASSIGN.equals(actionCode)) {
			String reassigUser = (String) paramMap.get(IWorkFlowWebConst.PARAM_REASSIG_USER);
			workFlowExcutor.doReassign(pk_user, pk_workflownote, note, reassigUser);
		} else if (IWorkFlowWebConst.ACTION_ADDAPPROVE.equals(actionCode)) {
			List<String> addUser = (List<String>) paramMap.get(IWorkFlowWebConst.PARAM_ADDAPPROVE);
			workFlowExcutor.doAddApprove(pk_user, pk_workflownote, note, addUser);
		} else if (IWorkFlowWebConst.ACTION_RECALL.equals(actionCode)) {
			workFlowExcutor.doRecall(pk_user, pk_workflownote);
		}
		
		return ret;
	}
	
	private IWorkflowExcutor getService() {
		return NCLocator.getInstance().lookup(IWorkflowExcutor.class);
	}
	
	private Map<String, Object> readFromJson(String param) {
		return new Gson().fromJson(param, Map.class);
	}
	/**
	 * 获取驳回事项的下拉
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/getRejectItemVOs", method = RequestMethod.GET)
	public void getRejectItemVOs(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String pk_group = InvocationInfoProxy.getInstance().getGroupId();
		if (checkParamNotNull(pk_group)) {
			IPubReverseFlowService ss = NCLocator.getInstance().lookup(
					IPubReverseFlowService.class);
			String transType=request.getParameter("transType");
			RejectItemVO[] vos = ss.queryRejectItemVOByGroup(pk_group, transType);
			if (vos != null && vos.length > 0) {
				writeToResponse(response, vos);
			} else {
				Map<String, String> message = new HashMap<String, String>();
				message.put("code", "false");
				writeToResponse(response, message);
			}
		} else {
			writeToResponse(response, null);
			Logger.error("参数不完整，请检查！");
		}
	}
	
	/**
	 * 会计初审驳回设置界面带出ssc初审意见
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/getRejectSetting", method = RequestMethod.GET)
	public void getRejectSetting(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String billID = (String) request.getParameter("billID");
		String transType = (String) request.getParameter("transType");
		if (checkParamNotNull(billID, transType)) {
			IPubReverseFlowService ss = NCLocator.getInstance().lookup(IPubReverseFlowService.class);
			Map<String, Object> voMap = ss.queryAggRejectSetting(billID, transType);

			if (voMap != null && voMap.get("setting") != null) {
				org.codehaus.jettison.json.JSONObject jsonR = new org.codehaus.jettison.json.JSONObject();
				RejectSettingVO settingVO=((AggRejectSettingVO[]) voMap.get("setting"))[0].getParentVO();

				String flowLink=settingVO.getActivitytype();
				if(!SSCFlowLinkConst.FirstExamAct.equals(flowLink)||FlowStateConstant.RECOMMINT==settingVO.getFlowstate()){
					Map<String, String> message = new HashMap<String, String>();
					message.put("code", "false");
					writeToResponse(response, message);
					return;
				}
				String rejectReason = settingVO.getRejectreason();// 驳回原因
				RejectItemVO[] rejectItemVOs = (RejectItemVO[]) voMap.get("item");// 驳回事项
				if (rejectItemVOs != null && rejectItemVOs.length > 0) {
					ITranslateVODataService transService = (ITranslateVODataService) NCLocator.getInstance().lookup(ITranslateVODataService.class);
					JSONArray jsonNew = transService.transValueObjectToJSON(rejectItemVOs);
					jsonR.put("rejectItemVOs", jsonNew);
				}
				jsonR.put("rejectReason", rejectReason);
				jsonR.put("code", false);
				response.setContentType("text/html");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonR.toString());
				response.flushBuffer();
			} else {
				Map<String, String> message = new HashMap<String, String>();
				message.put("code", "false");
				writeToResponse(response, message);
			}
		} else {
			writeToResponse(response, null);
			Logger.error("参数不完整，请检查！");
		}
	}
	
	/**
	 * 当前环节是否是会计初审
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings("restriction")
	@RequestMapping(value = "/isAccountExamAct", method = RequestMethod.GET)
	public void isAccountExamAct( HttpServletRequest request, HttpServletResponse response)  throws Exception {
		String billID = (String) request.getParameter("billID");
		String transType = (String) request.getParameter("transType");
		boolean isAccountExamAct = NCLocator.getInstance()
				.lookup(IPubReverseFlowService.class)
				.isAccountExamActLink(billID, transType);
		Map<String, String> message = new HashMap<String, String>();
		if (isAccountExamAct) {
			message.put("code", "true");
		} else {
			message.put("code", "false");
		}
		writeToResponse(response, message);
		
	}
	
	/**
	 * 获取驳回设置所选的驳回选项
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/getRejectItemSelected", method = RequestMethod.GET)
	public void getRejectItemSelected(HttpServletRequest request, HttpServletResponse response) throws Exception {
		 String billID = (String) request.getParameter("billID");
		 String transType = (String) request.getParameter("transType");
//		String billID = "0001H210000000WA95NB";//测试数据
//		String transType = "264X-Cxx-0038";//测试数据
		if (checkParamNotNull(billID, transType)) {
			IPubReverseFlowService ss = NCLocator.getInstance().lookup(
					IPubReverseFlowService.class);
			Map<String, Object> voMap = ss.getALLRejectItemData(billID, transType);
			
			if (voMap != null ) {
				org.codehaus.jettison.json.JSONObject jsonR = new org.codehaus.jettison.json.JSONObject();
				List<Map<String, String>> rejectLinkList=(List<Map<String, String>>) voMap.get("flowLink");//驳回环节
				String rejectReason=(String) voMap.get("rejectReason");//驳回原因
				RejectItemVO[] rejectItemVOs=(RejectItemVO[]) voMap.get("rejectItemVOs");//驳回事项
				if(rejectItemVOs!=null&&rejectItemVOs.length>0){
					ITranslateVODataService transService = (ITranslateVODataService) NCLocator
							.getInstance().lookup(ITranslateVODataService.class);
					JSONArray jsonNew = transService.transValueObjectToJSON(rejectItemVOs);
					jsonR.put("rejectItemVOs", jsonNew);
				}
				
			   
			    org.codehaus.jettison.json.JSONArray  linkJosn=new org.codehaus.jettison.json.JSONArray ();
			    int i=0;
			    for (Map<String, String> mapout : rejectLinkList){
			 
			    	org.codehaus.jettison.json.JSONObject json=new  org.codehaus.jettison.json.JSONObject();
			    	for (String key : mapout.keySet()){
			    		 
			    		 json.put(key, mapout.get(key));
	
			    	}
			    	linkJosn.put(i,json);
			    	i++;
			    }
			    
				jsonR.put("flowLink", linkJosn);
				jsonR.put("rejectReason", rejectReason);
				jsonR.put("billid", billID);
				jsonR.put("billtype",transType);
				jsonR.put("code", true);
				response.setContentType("text/html");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonR.toString());
				response.flushBuffer();
			} else {
				Map<String, String> message = new HashMap<String, String>();
				message.put("code", "");
				writeToResponse(response, message);
			}
		} else {
			writeToResponse(response, null);
			Logger.error("参数不完整，请检查！");
		}
	}
	/**
	 * 根据驳回环节选择驳回事项数据
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/getFLowLinkBySelectLink", method = RequestMethod.GET)
	public void getFLowLinkBySelectLink(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String billID = (String) request.getParameter("billID");
		String billType = (String) request.getParameter("billType");
		String ifOtherLink = (String) request.getParameter("ifOtherLink");
		String pk = (String) request.getParameter("pk");
		boolean flag = false;
		if ("true".equals(ifOtherLink)) {
			flag = true;
		}
		IPubReverseFlowService ss = NCLocator.getInstance().lookup(IPubReverseFlowService.class);
		Map<String,Object> rejectData=ss.getFLowLinkBySelectLink(billID, billType, flag, pk);
		String rejectReason=(String) rejectData.get("rejectReason");
		org.codehaus.jettison.json.JSONObject jsonR = new org.codehaus.jettison.json.JSONObject();
		RejectItemVO[] rejectItemVOs=(RejectItemVO[])rejectData.get("rejectItemVOs");
		if(rejectItemVOs!=null&&rejectItemVOs.length>0){
			ITranslateVODataService transService = (ITranslateVODataService) NCLocator
					.getInstance().lookup(ITranslateVODataService.class);
			JSONArray jsonNew = transService.transValueObjectToJSON(rejectItemVOs);
			jsonR.put("rejectItemVOs", jsonNew);
		}
		jsonR.put("rejectReason", rejectReason);
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonR.toString());
		response.flushBuffer();
	}
	private void writeToResponse(HttpServletResponse response, SuperVO[] vos) throws Exception {
		ITranslateVODataService transService = (ITranslateVODataService)NCLocator.getInstance().lookup(ITranslateVODataService.class);
		JSONArray jsonNew = transService.transValueObjectToJSON(vos);
		org.codehaus.jettison.json.JSONObject jsonR = new org.codehaus.jettison.json.JSONObject();
		jsonR.put("applytransferVO", jsonNew);
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonR.toString());
		response.flushBuffer();
	}
	private WFAlertResult doReject(String pk_active, String note, Map<String, Object> paramMap, HashMap<String, Object> eParam,String pk_workflownote) throws BusinessException {
//		String taskID = (String) AppUtil.getAppAttr("taskID");
		
		AggregatedValueObject aggVO = null;
		try {
			String targetID = pk_active;
			String txtMemo = note;
			String billType = (String) paramMap.get("transType");
			String billID = (String) paramMap.get("billID");
			String ifRepeat = (String) paramMap.get("ifrepeat");
			String bmodifiedscope = (String) paramMap.get("bmodifiedscope");
			List<String> rejectItemList=null;
			if(paramMap.get("pk_rejectitem")!=null){
				rejectItemList =  (List<String>) paramMap.get("pk_rejectitem");//已选驳回事项
			}
			
			IPFConfig pfSver = NCLocator.getInstance().lookup(IPFConfig.class);
			aggVO = pfSver.queryBillDataVO(billType, billID);

			if (aggVO instanceof MetaDataBaseAggVO) {
				String docPK = (String) aggVO.getParentVO().getAttributeValue("pk_doc");
				IBean bean = DocCommonUtil.getBeanByMD(docPK);
				aggVO.getParentVO().setAttributeValue("bean", bean);
			}

//			ISSCWFUtil util = NCLocator.getInstance().lookup(ISSCWFUtil.class);

			HashMap<Object, Object> hmPfExParams = null;
			
			IWorkflowMachine iwm = NCLocator.getInstance().lookup(IWorkflowMachine.class);
			WorkflownoteVO noteVO = iwm.checkWorkFlow(IPFActionName.SIGNAL, billType, aggVO, hmPfExParams);
			
			// 得到交易类型
			NCObject ncObj = NCObject.newInstance(aggVO);
			IFlowBizItf itf = (IFlowBizItf) ncObj.getBizInterface(IFlowBizItf.class);
			String tranType = itf.getTranstype();

			noteVO.getTaskInfo().getTask().setTaskType(WfTaskType.Backward.getIntValue());

			// 设置流程控制对象
			SscObject sscObj = new SscObject();

			// 获取是否重走 重走为2 不重走为3
			if (ifRepeat.equals(String.valueOf(FlowStateConstant.REJECT_UNREPEAT))) {
				// 集成方式下，共享服务不需重走流程，只需设定标志，让外系统重走流程
//				if (nc.ssc.pub.util.SscIntergrationUtil.isSscDeploy())
//					sscObj.setSscifRepeatWorkflow("2");
//				else
					noteVO.getTaskInfo().getTask().setSubmit2RjectTache(true);
			}

			noteVO.setApproveresult("R");

			// 只有和外系统进行集成且审核节点才驳回初审
			noteVO.getTaskInfo().getTask().setJumpToActivity(targetID);
			// 设置意见
			noteVO.setChecknote(txtMemo);
			
//			sscObj.setSscTaskID(taskID);
			sscObj.setSscSourceAction("R");
			sscObj.setImageRejectAction(SSCWFConst.ImageReScan);
			sscObj.setSscSourceActiveID(noteVO.getTaskInfo().getTask().getActivityID());
			sscObj.setSscTargetActiveID(targetID);
			sscObj.setChecknote(txtMemo);
			if(rejectItemList!=null&&rejectItemList.size()>0){//逆向流程houmeng3
				AggRejectSettingVO rsvo = this.createRejectSettingVO(tranType, billID,rejectItemList,txtMemo,ifRepeat,bmodifiedscope,pk_workflownote );
				sscObj.setSscAggRejectSettingVO(rsvo);
			}
			

			// zhaojianc 2016-05-19 碧桂园
//			if (nc.ssc.pub.util.SscIntergrationUtil.isSscDeploy()) {
//				PfParameterVO paraVo = PfUtilBaseTools.getVariableValue(itf.getBilltype(), "SIGNAL", aggVO, new AggregatedValueObject[] { aggVO }, sscObj, null, noteVO, new HashMap(), new Hashtable()); // new
//																																																					// PfParameterVO();
//				int ret = ((IWorkflowMachine) NCLocator.getInstance().lookup(IWorkflowMachine.class)).forwardCheckFlow(paraVo);
//			} else {
			
//				IplatFormEntry iIplatFormEntry = (IplatFormEntry) NCLocator.getInstance().lookup(IplatFormEntry.class.getName());
//				iIplatFormEntry.processAction("SIGNAL", tranType, noteVO, aggVO, sscObj, null);
//			}
			NCLocator.getInstance().lookup(IPubReverseFlowService.class).processAction("SIGNAL", tranType, noteVO, aggVO, sscObj, null);
			return getAfterSucessHintMessage(aggVO, noteVO.getPk_billtype());
		} catch (BusinessException e) {
			WFAlertResult result = handleAlterException(eParam, aggVO, e);
			if (result != null) {
				return result;
			}
			throw e;
		}
	}
	
	/**
	 * 创建驳回事项保存vo
	 * @param transType
	 * @param billID
	 * @param rejectitems 
	 * @return
	 */
	private AggRejectSettingVO createRejectSettingVO(String transType, String billID, List<String> rejectItemList,String rejectNote,String ifrepeat,String bmodifiedscope,String pk_workflownote) {
		AggRejectSettingVO aggVO =null;
		AggRejectSettingVO[] oldAggVO=null;
		RejectSettingVO rejectSettingVO=null;
		List<RejectSettingItemVO> itemVOList=new ArrayList<RejectSettingItemVO>();
		try {
//			NCLocator.getInstance().lookup(IPubReverseFlowService.class).deleteRejectSettingByBill(billID, transType);
		    oldAggVO = NCLocator.getInstance().lookup(IPubReverseFlowService.class).query(" billid='"+billID+"' and billtype='"+transType+"' ");
		} catch (BusinessException e) {
			Logger.error(e.getMessage(),e);
		}
		if(oldAggVO!=null&&oldAggVO.length>0){
			aggVO=oldAggVO[0];
			rejectSettingVO=aggVO.getParentVO();
			rejectSettingVO.setStatus(VOStatus.UPDATED);
			RejectSettingItemVO[] childern=aggVO.getChildrenVO();
			if(childern!=null&&childern.length>0){
				for(RejectSettingItemVO itemVO:childern){
					itemVO.setStatus(VOStatus.DELETED);
					itemVOList.add(itemVO);
				}
			}
		}else{
			aggVO=new AggRejectSettingVO();
			rejectSettingVO=new RejectSettingVO();
			aggVO.setParent(rejectSettingVO);
			rejectSettingVO.setStatus(VOStatus.NEW);
		}
		
		rejectSettingVO.setBillid(billID);
		rejectSettingVO.setBilltype(transType);
		rejectSettingVO.setRejectreason(rejectNote);	
		rejectSettingVO.setPk_checkflow(pk_workflownote);
		
		if(String.valueOf(FlowStateConstant.REJECT_REPEAT).equals(ifrepeat)){
			rejectSettingVO.setFlowstate(FlowStateConstant.REJECT_REPEAT);
		}else{
			rejectSettingVO.setFlowstate(FlowStateConstant.REJECT_UNREPEAT);
		}
		if(BModifiedScope.ALL_MODIFIED.toStringValue().equals(bmodifiedscope)){
			rejectSettingVO.setBmodifiedscope(BModifiedScope.ALL_MODIFIED.toIntValue());
		}else if(BModifiedScope.PART_MODIFIED.toStringValue().equals(bmodifiedscope)){
			rejectSettingVO.setBmodifiedscope(BModifiedScope.PART_MODIFIED.toIntValue());
		}else{
			rejectSettingVO.setBmodifiedscope(BModifiedScope.NO_MODIFIED.toIntValue());
		}
		rejectSettingVO.setActivitytype(SSCWFConst.AccountExamAct);
//		List<RejectSettingItemVO> itemVOs=new ArrayList<RejectSettingItemVO>();
		for(String rejectItem :rejectItemList){
			RejectSettingItemVO itemVO=new RejectSettingItemVO(); 
			itemVO.setPk_rejectitem(rejectItem);
			itemVO.setStatus(VOStatus.NEW);
			itemVOList.add(itemVO);
			
		}
		aggVO.setChildrenVO(itemVOList.toArray(new RejectSettingItemVO[0]));
		return aggVO;
	}
	private WFAlertResult getAfterSucessHintMessage(Object billVO,
			String billtype) throws BusinessException {
		IWFHintMessageCallback callback = getWFHintMessageCallback(billtype);
		if (callback != null) {
			String hintMessage = callback.getHintMessageAfterSucess(billVO);
			if (StringUtils.isNotBlank(hintMessage)) {
				WFAlertResult result = new WFAlertResult();
				result.setHintMessage(hintMessage);
				return result;
			}
		}
		return null;
	}

	private IWFHintMessageCallback getWFHintMessageCallback(String billtype) {
		ArrayList<Billtype2VO> bt2VOs = PfDataCache.getBillType2Info(billtype,
				ExtendedClassEnum.AFTER_SUCESS_HINTMESSAGE_CALLBACK
				.getIntValue());
		// 实例化
		for (Billtype2VO bt2VO : bt2VOs) {
			try {
				Object obj = PfUtilTools.findBizImplOfBilltype(billtype,
						bt2VO.getClassname());
				return ((IWFHintMessageCallback) obj);
			} catch (Exception e) {
				Logger.error("无法实例化前台业务插件类billType=" + billtype + ",className="
						+ bt2VO.getClassname(), e);
			}
		}
		return null;
	}

	private WFAlertResult handleAlterException(HashMap<String, Object> eParam,
			AggregatedValueObject billvo, Exception e) throws BusinessException {
		if (eParam != null) {
			WFAlertResult result = WFAlertExceptionUtil.getInstance()
					.getAlertMessageByException(e, billvo);
			if (result != null) {
				appendLastExceptionClass(eParam, result);
				return result;
			}
		}
		return null;
	}

	private void appendLastExceptionClass(HashMap<String, Object> eParam, WFAlertResult result) {
		@SuppressWarnings("unchecked")
		List<String> exceptionClass = (List) eParam.get("alter_force_pass");
		if ((exceptionClass != null) && (!exceptionClass.isEmpty())) {
			result.getExceptionClass().addAll(exceptionClass);
		}
	}


}
