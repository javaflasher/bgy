package nc.bs.carinfoweb.expportal;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import uap.iweb.bt.BtPara;
import uap.iweb.bt.IURLResolver;
import uap.iweb.plugin.PluginFacade;
import uap.web.bd.pub.AppUtil;
import nc.bs.er.expapprove.util.ExpenseApproveUtil;
import nc.bs.er.linkpfinfo.PfinfoPageModel;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.srmsmweb.util.SSCSupplierEnterConstant;
import nc.itf.er.expportal.BasePortal;
import nc.itf.uap.pf.IPFConfig;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.OpenProperties;
import nc.uap.wfm.engine.TaskProcessUI;
import nc.uap.wfm.model.TaskProcessResult;
import nc.uap.wfm.ncworkflow.cmd.LfwPfUtil;
import nc.uap.wfm.ncworkflow.cmd.YerLfwPfUtil;
import nc.vo.er.expbillquery.YerExpBillnav;
import nc.vo.er.expportal.PortalQueryFieldName;
import nc.vo.er.task.YerTaskVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.srmsm.supplierenterpf.entity.SupplierEnterPFVO;

/**
 * @author 钟泽辉
 * @date 2018-1-16
 * @功能描述 车辆信息维护入口类
 */
public class CarInfoEnterFPortal  extends BasePortal{
	
	@Override
	public void openToMakeBill(String tradetype, String tradeTypeName) {
		String pk_org = LfwRuntimeEnvironment.getLfwSessionBean().getUser_org();
		String pk_group = InvocationInfoProxy.getInstance().getGroupId();
		String pk_user = InvocationInfoProxy.getInstance().getUserId();
		YerExpBillnav billTypeToURL = new YerExpBillnav();
		billTypeToURL = (AppUtil.getAppAttr("billTypeToURL") == null) ? null: (YerExpBillnav) AppUtil.getAppAttr("billTypeToURL");
		StringBuffer funURL = new StringBuffer();
		if (billTypeToURL == null) {
			billTypeToURL = ExpenseApproveUtil.billTypeToURL();
			AppUtil.addAppAttr("billTypeToURL", billTypeToURL);
		}
		
		if ((billTypeToURL != null)	&& (billTypeToURL.getBillTypeToURL().size() > 0)) {
			
			String nodecode = "20117010000";
			
			BtPara para = new BtPara();
			para.setNodeKey(tradetype);
			para.setFunNode(nodecode);
			para.setTemplateType(0);

			para.setPageTemplate("carinfocard.html");
			PluginFacade.get(IURLResolver.class).setPara(para);
			StringBuffer url = new StringBuffer(para.getUrl());
			if (url.indexOf("?") != -1) {
				url.append("&");
			} else {
				url.append("?");
			}
			String templateName = tradeTypeName;
			try {
				templateName = URLEncoder.encode(URLEncoder.encode(templateName,"UTF-8"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			funURL.append("/iwebap" + url.toString() + "tradetype="
					+ tradetype + "&nodecode=" + nodecode + "&pk_org="
					+ pk_org + "&pk_user="+pk_user+"&templateName="
					+ templateName + "#/add");
				LfwRuntimeEnvironment.getWebContext().getRequest().setAttribute("url", funURL.toString());

			String js = "var el = document.createElement('a');document.body.appendChild(el);"
			        +"el.href ='"
					+ funURL.toString()
					+ "'; el.target = '_blank';"
					+ "el.click();" + "document.body.removeChild(el);";
			AppLifeCycleContext.current().getApplicationContext().getCurrentWindowContext().addExecScript(js);
		}
	}

	@Override
	public PortalQueryFieldName getPortalQueryFieldName() {
		PortalQueryFieldName queryField = new PortalQueryFieldName();
		queryField.setCode("CARI");
		queryField.setApprover("approver");
		queryField.setBilldate("billdate");
		queryField.setBillMaker("billmaker");
		queryField.setBillno("billno");
		queryField.setDeptId("transtype");
		queryField.setDjdl("transtype");
		queryField.setDjlxbm("transtype");
		queryField.setDjzt("billstatus");
		queryField.setJylxbm("transtype");
		queryField.setJylxid("transtypepk");
		queryField.setmTableName("er_carinfo");
		queryField.setPk_group("pk_group");
		queryField.setPk_id("pk_carinfo");
		queryField.setPk_org("pk_org");
		queryField.setPk_project("");
		queryField.setSpsj("approvedate");
		queryField.setSpzt("approvestatus");
		queryField.setSxzt("billstatus");
		queryField.setSzxmid("");
		queryField.setTotal("annuallimit");
		queryField.setCurrtype("none");
		
		//queryField.setpsndocSql(" (yer_yerexpbill." + SupplierEnterPFVO.BILLMAKER + "='%cuserid%') ");
		//queryField.setSrcSys("case  when  yer_yerexpbill.def30 ='~' or yer_yerexpbill.def30 is null then convert(varchar,src_syscode) else yer_yerexpbill.def30 end");
		queryField.setIsHasCompletePortlet("yer_yerexpbill.approvestatus in (1)  ");
		queryField.setNotHasCompletePortlet("yer_yerexpbill.approvestatus not in (1)  ");
		return queryField;
		
	}
	
	@Override
	public void expensePortalClickBillCode(String billid, String tradetype,
			String tradeName, String djzt, String spzt) {
		String pk_user = InvocationInfoProxy.getInstance().getUserId();
		// NC功能节点号
		String nodecode = "20117010000";	//在入口类查询模板
		BtPara para = new BtPara();
		para.setNodeKey(tradetype);
		para.setFunNode(nodecode);
		para.setTemplateType(0);
		String pageTemplate = "carinfocard.html";
		para.setPageTemplate(pageTemplate);
		PluginFacade.get(IURLResolver.class).setPara(para);
		String flagURL = "";
		if (para.getUrl().indexOf("?") == -1) {
			flagURL = "?";
		} else {
			flagURL = "&";
		}
		String templateName = tradeName;
		try {
			templateName = URLEncoder.encode(URLEncoder.encode(templateName,"UTF-8"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String url = "/iwebap" + para.getUrl() + flagURL + "openbillid="
				+ billid + "&tradetype=" + tradetype +"&pk_user="+pk_user + "&templateName="
				+ templateName + "#/view/" + billid;
		TaskProcessUI tpi = new TaskProcessUI();
		tpi.setUrl(url);
		if (tpi != null) {
			String js = "var el = document.createElement('a');document.body.appendChild(el);"
					+ "el.href ='"
					+ tpi.getUrl()
					+ "'; el.target = '_blank';"
					+ "el.click();" + "document.body.removeChild(el);";
			AppLifeCycleContext.current().getApplicationContext()
					.getCurrentWindowContext().addExecScript(js);
		}

	}

	@Override
	public void checkPortalClickBillCode(String billid, String billType,
			String taskPk, int state, int workflowtype, String actiontype) {
		// NC功能节点号
		String nodecode = "20117010";
		BtPara para = new BtPara();
		para.setNodeKey(billType);
		para.setFunNode(nodecode);
		para.setTemplateType(0);
		String pageTemplate = "carinfocard.html";
		para.setPageTemplate(pageTemplate);
		PluginFacade.get(IURLResolver.class).setPara(para);
		String flagURL = "";
		if (para.getUrl().indexOf("?") == -1) {
			flagURL = "?";
		} else {
			flagURL = "&";
		}
		String templateName = "车辆信息维护";
		try {
			templateName = URLEncoder.encode(URLEncoder.encode(templateName,"UTF-8"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String url = "/iwebap" + para.getUrl() + flagURL + "openbillid="
				+ billid + "&tradetype=" + billType + "&templateName="
				+ templateName + "#/view/" + billid;
		TaskProcessUI tpi = new TaskProcessUI();
		tpi.setUrl(url);
		if (tpi != null) {
			String js = "var el = document.createElement('a');document.body.appendChild(el);"
					+ "el.href ='"
					+ tpi.getUrl()
					+ "'; el.target = '_blank';"
					+ "el.click();" + "document.body.removeChild(el);";
			AppLifeCycleContext.current().getApplicationContext()
					.getCurrentWindowContext().addExecScript(js);
		}

	}
	
	@Override
	public void linkWorkflowInfo(String billId, String pk_org, String billtype) {
		Map<String, String> paramMap = new HashMap<String, String>();
		//paramMap.put("model", YerWorkflowPfinfoPageModel.class.getName());
		paramMap.put("model", PfinfoPageModel.class.getName());
		paramMap.put("billType", billtype);
		paramMap.put("billId", billId);
		OpenProperties props = new OpenProperties();
		props.setWidth("1000");
		props.setHeight("600");
		props.setTitle(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
				"per_codes", "0per_codes0052")/* @res "联查审批情况" */);
		//props.setOpenId("workflowpfinfo");
		props.setOpenId("pfinfo");
		props.setParamMap(paramMap);
		AppLifeCycleContext.current().getViewContext().navgateTo(props);
	}
	

	@Override
	public String getMenu_CategoryID() {
		return SSCSupplierEnterConstant.YER_MENU_CATEGORY;
	}

	@Override
	public String getMenuCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getChildMenuCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void approveBill(List<TaskProcessResult> tprList, YerTaskVO[] vos,
			String oper, String content) {
			// 审批信息
			Map<String, String> approveInfoMap = new HashMap<String, String>();
			IPFConfig bsConfig = (IPFConfig) NCLocator.getInstance().lookup(IPFConfig.class.getName());
			approveInfoMap.put(LfwPfUtil.APPROVEINFO, oper);
			approveInfoMap.put(LfwPfUtil.APPROVEMESSAGE, content);
			for (int i = 0; i < vos.length; i++) {
				AggregatedValueObject aggVO = null;
				TaskProcessResult taskProcessResult = new TaskProcessResult();
				try {
					aggVO = bsConfig.queryBillDataVO(vos[i].getBilltype(), vos[i].getBillid());
					String actionType = "APPROVE";
					YerLfwPfUtil.runAction(actionType, "CARI-Cxx-001", aggVO, null,
							null, null, null, null, approveInfoMap);
					taskProcessResult.setIsPass(Boolean.TRUE);
					taskProcessResult.setBillPk(vos[i].getBillid());
					taskProcessResult.setBillNo(vos[i].getBillno());
					tprList.add(taskProcessResult);
				} catch (Exception e) {
					Logger.error(e.getMessage(), e);
					taskProcessResult.setIsPass(Boolean.FALSE);
					taskProcessResult.setBillPk(vos[i].getBillid());
					taskProcessResult.setBillNo(vos[i].getBillno());
					tprList.add(taskProcessResult);
					continue;
				}
			}
		
	}
	
	

}
