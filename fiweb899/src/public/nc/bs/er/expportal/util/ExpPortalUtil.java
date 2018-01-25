package nc.bs.er.expportal.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.er.exp.util.ExpUtil;
import nc.bs.er.expapprove.util.ExpenseApproveUtil;
import nc.bs.er.fysqcenter.model.BillNavMenuItemVO;
import nc.bs.er.util.YerUtil;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pf.pub.PfDataCache;
import nc.itf.arap.prv.IBXBillPrivate;
import nc.itf.er.expbillquery.IExpBIllPortalService;
import nc.itf.er.expportal.ExpPortalFactory;
import nc.itf.er.expportal.IExpPortal;
import nc.pub.reject.util.FlowStateConstant;
import nc.uap.cpb.org.exception.CpbBusinessException;
import nc.uap.cpb.org.itf.ICpMenuQry;
import nc.uap.cpb.org.menuitem.MenuRoot;
import nc.uap.cpb.org.vos.MenuItemAdapterVO;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.log.LfwLogger;
import nc.util.fi.pub.SqlUtils;
import nc.vo.arap.bx.util.BXConstans;
import nc.vo.arap.bx.util.BXUtil;
import nc.vo.er.expbillquery.YerExpBillParamVO;
import nc.vo.er.expbillquery.YerExpBillVo;
import nc.vo.er.expportal.PortalQueryFieldName;
import nc.vo.er.task.BillApproveNav;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;

import org.apache.commons.lang.StringUtils;

import uap.web.bd.pub.AppUtil;

public class ExpPortalUtil {

	/**
	 * ��õ��ݴ����ӳ���ֶ���Ĺ�ϵӳ��
	 * 
	 * @return
	 */
	public static Map<String, PortalQueryFieldName> getCodeToQueryFieldMap() {
		/*
		 * modify author:houmeng3 begin
		 */

		Map<String, PortalQueryFieldName> map = new HashMap<String, PortalQueryFieldName>();
		// List<IExpPortal> expPortalClass = getAllExpPortalClass();
		Map<String, IExpPortal> expPortalClass = getAllExpPortalClass();
		if (expPortalClass != null && expPortalClass.size() > 0) {
			for (Map.Entry<String, IExpPortal> entry : expPortalClass
					.entrySet()) {
				setFreebillTypeCode(entry.getValue(), entry.getKey());
				PortalQueryFieldName fieldName = entry.getValue()
						.getPortalQueryFieldName();
				if (("freebillTypeCode".equals(fieldName.getCode()))) {
					map.put(entry.getKey(), fieldName);
				} else {
					map.put(fieldName.getCode(), fieldName);
				}
			}
			// if (expPortalClass != null && expPortalClass.size() > 0) {
			// for (int i = 0; i < expPortalClass.size(); i++) {
			// PortalQueryFieldName fieldName = expPortalClass.get(i)
			// .getPortalQueryFieldName();
			// if (fieldName == null) {
			// continue;
			// }
			// if (StringUtils.isNotEmpty(fieldName.getCode())) {
			// map.put(fieldName.getCode(), fieldName);
			// }
			// }
			/*
			 * end
			 */
		}
		return map;
	}

	private static void setFreebillTypeCode(IExpPortal portal, String value) {
		if (portal != null
				&& portal.getClass().getSuperclass() != null
				&& "FreebillPortal".equals(portal.getClass().getSuperclass()
						.getSimpleName())) {
			AppUtil.addAppAttr("freebillTypeCode", value);
			InvocationInfoProxy.getInstance().setProperty("freebillTypeCode",
					value);
		}
	}

	private static Serializable getFreebillTypeCode() {
		Serializable str = AppUtil.getAppAttr("freebillTypeCode");
		if (str == null) {
			return InvocationInfoProxy.getInstance().getProperty(
					"freebillTypeCode");
		} else {
			return str;
		}
	}

	/**
	 * ��ȡ��Ȩ�޵Ľ������ͱ���
	 * 
	 * @param allTradeTypes
	 * @param fieldName
	 * @return
	 */
	public static Set<String> getTradetypesByCode(
			HashSet<String> allTradeTypes, PortalQueryFieldName fieldName) {

		Set<String> tradeTypes = new HashSet<String>();

		if (allTradeTypes != null) {
			String code = fieldName.getCode();
			if ("freebillTypeCode".equals(code)) {
				// ����ǹ���
				String freebillTypeCode = (String) getFreebillTypeCode();
				if (allTradeTypes.contains(freebillTypeCode)) {
					tradeTypes.add(freebillTypeCode);
				}

			} else {
				for (String tmpType : allTradeTypes) {
					if ("F0".equals(code)) {
						if (tmpType.startsWith(code) || "D0".equals(tmpType)) {
							tradeTypes.add(tmpType);
						}
					} else if ("F1".equals(code)) {
						if (tmpType.startsWith(code) || "D1".equals(tmpType)) {
							tradeTypes.add(tmpType);
						}
					} else if ("F2".equals(code)) {
						if (tmpType.startsWith(code) || "D2".equals(tmpType)) {
							tradeTypes.add(tmpType);
						}
					} else if ("F3".equals(code)) {
						if (tmpType.startsWith(code) || "D3".equals(tmpType)) {
							tradeTypes.add(tmpType);
						}
					} else if ("FIV".equals(code)) {
						if (tmpType.startsWith(code) || "FP0".equals(tmpType)) {
							tradeTypes.add(tmpType);
						}
					} else {
						if (tmpType.startsWith(code)) {
							tradeTypes.add(tmpType);
						}
					}
				}
			}
		}

		return tradeTypes;
	}

	/**
	 * ��ȡ����ɻ���δ������� ����Ӧ���Ƶ���������
	 * 
	 * @param queryField
	 * @param modelCodeMap
	 * @param isHasCompletePortlet
	 * @return
	 */
	public static String getIsCompleteCond(PortalQueryFieldName queryField,
			Map<String, String> modelCodeMap, YerExpBillParamVO paramVO) {

		StringBuilder sql = new StringBuilder();

		String linkId = paramVO.getLinkId();
		String status = paramVO.getBillAppriveState();
		String djdl = queryField.getCode();
		if ("freebillTypeCode".equals(djdl)) {
			// ����ǹ���
			djdl = (String) getFreebillTypeCode();
		}
		String modulecode = modelCodeMap.get(djdl);

		if (paramVO.getIsHasCompletePortlet()) {
			// ��ѯ�����
			if ((djdl.equals("264") || djdl.equals("263"))
					&& ExpPortalUtil.isCmpused()) {// ��Ҫ�ų���������
				UFDate[] date = ExpenseApproveUtil.getBillDates(linkId);
				sql.append(" and  yer_yerexpbill." + queryField.getBilldate()
						+ " >='" + date[0].toLocalString().substring(0, 10)
						+ "'\n");
				// if (!"".equals(linkId) && linkId != null
				// && !"none".equals(linkId)) {
				// } else if (linkId == null) {
				// UFDate[] date = ExpenseApproveUtil
				// .getBillDates(ExpenseApproveUtil.LINK_ONEWEEK);
				// sql.append(" and  yer_yerexpbill."
				// + queryField.getBilldate() + " >='"
				// + date[0].toLocalString().substring(0, 10) + "'\n");
				// }
				// ����ǵ�����payflag�ǿ�
				sql.append(" and (yer_yerexpbill.sxbz = 1 and (yer_yerexpbill.payflag is null or yer_yerexpbill.payflag in (3,101,102)))\n");
			} else {
				UFDate[] date = ExpenseApproveUtil.getBillDates(linkId);
				sql.append(" and  yer_yerexpbill." + queryField.getBilldate()
						+ " >='" + date[0].toLocalString().substring(0, 10)
						+ "'\n");
				// if (!"".equals(linkId) && linkId != null) {
				// } else if (linkId == null) {
				// UFDate[] date = ExpenseApproveUtil
				// .getBillDates(ExpenseApproveUtil.LINK_ONEWEEK);
				// sql.append(" and  yer_yerexpbill."
				// + queryField.getBilldate() + " >='"
				// + date[0].toLocalString().substring(0, 10) + "'\n");
				// }
				if ("LE3".equals(djdl)) {
					sql.append(" and (yer_yerexpbill." + queryField.getDjzt()
							+ " = 1)\n");
				} else if ("sscwo".equals(modulecode)) {// houmeng3+
					sql.append(" and yer_yerexpbill." + queryField.getSxzt()
							+ " = 4 \n");
				} else {
					String isHasCompleteSql = queryField
							.getIsHasCompletePortlet();
					if (isHasCompleteSql != null
							&& !"".equals(isHasCompleteSql)) {
						sql.append(" and " + isHasCompleteSql);
					} else {
						sql.append(" and yer_yerexpbill."
								+ queryField.getSxzt() + " in (1,10)\n");
					}

				}

			}
		} else {
			if ((djdl.equals("264") || djdl.equals("263"))
					&& ExpPortalUtil.isCmpused()) {
				sql.append(" and (yer_yerexpbill.sxbz <> 1 or (yer_yerexpbill.sxbz = 1 and yer_yerexpbill.payflag is not null and yer_yerexpbill.payflag not in (3,101,102))) \n");
			} else {
				if ("sscwo".equals(modulecode)) {// houmeng3+
					sql.append(" and (yer_yerexpbill." + queryField.getSxzt()
							+ " <> 4 or yer_yerexpbill." + queryField.getSxzt()
							+ " is null)\n");
				} else if ("LE3".equals(djdl)) {
					sql.append(" and (yer_yerexpbill." + queryField.getDjzt()
							+ " <> 1)\n");
				} else {
					String notCompleteSql = queryField
							.getNotHasCompletePortlet();
					if (notCompleteSql != null && !"".equals(notCompleteSql)) {
						sql.append(" and " + notCompleteSql);
					} else {
						sql.append(" and yer_yerexpbill."
								+ queryField.getSxzt()
								+ "<>1 and yer_yerexpbill."
								+ queryField.getSxzt() + "<>10\n");
					}

				}

			}
			if (!StringUtils.isEmpty(status)) {
				// ����״̬����
				if ("LE3".equals(djdl)) {
					if ("2".equals(status)) {
						sql.append(" and (yer_yerexpbill."
								+ queryField.getDjzt() + " in('2','3'))\n");
					} else {
						sql.append(" and yer_yerexpbill."
								+ queryField.getDjzt() + " in(" + status
								+ ")\n");
					}
				} else {
					if ("2".equals(status)) {
						sql.append(" and yer_yerexpbill."
								+ queryField.getSpzt() + " in('2','3')\n");
					} else {
						sql.append(" and yer_yerexpbill."
								+ queryField.getSpzt() + " in(" + status
								+ ")\n");
					}
				}
			}

		}

		return sql.toString();
	}

	/**
	 * ��ȡ�û�������Ϣ
	 * 
	 * @param queryField
	 * @param modelCodeMap
	 * @param isHasCompletePortlet
	 * @return
	 */
	public static String getUserCond(PortalQueryFieldName queryField,
			Map<String, String> modelCodeMap, YerExpBillParamVO paramVO) {

		StringBuilder sql = new StringBuilder();

		String djdl = queryField.getCode();
		if ("freebillTypeCode".equals(djdl)) {
			// ����ǹ���
			djdl = (String) getFreebillTypeCode();
		}
		String modulecode = modelCodeMap.get(djdl);
		String psndoc = getRYXXID() == null ? "" : getRYXXID();
		String cusrid = ExpUtil.getPkUser();

		if ("arap".equals(modulecode)) {
			sql.append("yer_yerexpbill.dr =0 and ");
			// �ո��ĵ����Ƶ��˹��������û���������Ա
			if (queryField.getpsndocSql() != null) {
				String realPsnDocWhere = getPsnDocWhere(djdl,
						queryField.getpsndocSql());
				realPsnDocWhere = realPsnDocWhere.replace("%pk_psndoc%",
						psndoc == null ? "" : psndoc);
				
				// ��Զ�� 2017��9��13�� �������Ż��������Ƶ���ΪNCϵͳ������ start 
//				realPsnDocWhere = realPsnDocWhere.replace(
//						"billmaker='%cuserid%'", "billmaker in ('"
//								+ (psndoc == null ? "" : cusrid)
//								+ "','NC_USER0000000000000')");
				realPsnDocWhere = realPsnDocWhere.replace(
						"billmaker='%cuserid%'", "billmaker in ('"
								+ (psndoc == null ? "" : cusrid)
								+ "')");
				// ��Զ�� 2017��9��13�� �������Ż��������Ƶ���ΪNCϵͳ������ end
				sql.append(" " + realPsnDocWhere + " ");
			}

		} else if ("sscwo".equals(modulecode)) {

			sql.append(" (yer_yerexpbill." + queryField.getBillMaker() + "='"
					+ cusrid + "') ");

		} else if ("cmp".equals(modulecode) || "LE3".equals(djdl)) {

			sql.append(" (yer_yerexpbill." + queryField.getBillMaker() + "='"
					+ cusrid + "') ");
		} else {
			sql.append(" (yer_yerexpbill." + queryField.getBillMaker() + "='"
					+ psndoc + "' or yer_yerexpbill.creator='" + cusrid + "') ");

		}

		return sql.toString();
	}

	/**
	 * ��ȡ����Ĺ�������
	 * 
	 * @param queryField
	 * @param modelCodeMap
	 * @param isHasCompletePortlet
	 * @return
	 */
	public static String getSpecialCond(PortalQueryFieldName queryField,
			Map<String, String> modelCodeMap, YerExpBillParamVO paramVO) {

		StringBuilder sql = new StringBuilder();
		String djdl = queryField.getCode();

		if (djdl != null && "36D1".equals(djdl)) {
			// ��������
			sql.append(" and yer_yerexpbill.opsrctype=1 and yer_yerexpbill.dr = 0 \n ");
		} else if (djdl != null && "FIV".equals(djdl)) {
			// ��������
			sql.append(" and yer_yerexpbill.dr = 0 \n ");
			// start Ӧ���� ���˵��ڲ�����Ӧ������F1-Cxx-D46��δȷ�ϣ�t.billstatus = 9���򵥾ݺ�Ϊ�գ�t.billno is null���ĵ���   add by hubina 20170728
		}else if (djdl != null && "F1".equals(djdl)){
			//mod by hubina 20170812 �ڲ�����Ӧ���� ����������������  'F1-Cxx-D44','F1-Cxx-D45' Ϊ��ͳһ�����ݺ�Ϊ�ջ򵥾�״̬Ϊδȷ�ϵĵ��� ȫ�����˵�
			sql.append(" and not exists (select 1 from ap_payablebill t where (t.billno is null or t.billstatus = 9)   and t.pk_payablebill = yer_yerexpbill.pk_payablebill) and  yer_yerexpbill.dr = 0 \n ");
			// end  Ӧ���� ���˵��ڲ�����Ӧ������F1-Cxx-D46��δȷ�ϣ�t.billstatus = 9���򵥾ݺ�Ϊ�գ�t.billno is null���ĵ���   add by hubina 20170728
		}else{
			// ����dr����
			sql.append(" and yer_yerexpbill.dr = 0 \n ");
		}

		return sql.toString();
	}

	/**
	 * ���ݽ������ͻ���ģ����ѯ����ƴ��where����
	 * 
	 * @param queryField
	 * @param modelCodeMap
	 * @param paramVO
	 * @return
	 */
	public static String getQueryCond(PortalQueryFieldName queryField,
			Map<String, String> modelCodeMap, YerExpBillParamVO paramVO) throws BusinessException {

		StringBuilder querySql = new StringBuilder();

		if (!"".equals(paramVO.getWheresql()) && paramVO.getWheresql() != null) {
			querySql.append(" and " + paramVO.getWheresql() + "\n");
		}
		if (!StringUtils.isEmpty(paramVO.getBillTypeCode())) {
			if (paramVO.getIsOtherQuery() != null && paramVO.getIsOtherQuery()) {
				querySql.append(" and yer_yerexpbill." + queryField.getJylxbm()
						+ " in(" + paramVO.getBillTypeCode() + ")\n");
			} else {
				if (paramVO.getBillTypeCode().equals("4641-01")) { // zhangjxh
					querySql.append(" and yer_yerexpbill.djdl = '4641'\n");
				} else if (paramVO.getBillTypeCode().equals("4642-01")) {
					querySql.append(" and yer_yerexpbill.djdl = '4642'\n");
				} else if (paramVO.getBillTypeCode().equals("35-01")) {
					querySql.append(" and yer_yerexpbill."
							+ queryField.getJylxbm() + " like '35%'\n");
				} else {
					querySql.append(" and yer_yerexpbill."
							+ queryField.getJylxbm() + " = '"
							+ paramVO.getBillTypeCode() + "'\n");
				}
			}
		}

		if (!"".equals(paramVO.getLinkId()) && paramVO.getLinkId() != null) {
			UFDate[] date = ExpenseApproveUtil
					.getBillDates(paramVO.getLinkId());
			querySql.append(" and  yer_yerexpbill." + queryField.getBilldate()
					+ " >='" + date[0].toLocalString().substring(0, 10) + "'\n");
		}

		if (StringUtils.isNotEmpty(paramVO.getLikeQueryValue())) {
			// querySql.append(" and  (convert(char(10),yer_yerexpbill.billdate,120) like '%"+
			// paramVO.getLikeQueryValue() + "%'\n");
			querySql.append(" and yer_yerexpbill." + queryField.getBillno()
					+ " like '%" + paramVO.getLikeQueryValue() + "%'\n");
			// querySql.append(" or yer_yerexpbill." + queryField.getTotal() +
			// " like '%"+ paramVO.getLikeQueryValue() + "%')\n");
		}
		//start
		/**
		 * @author menzhen
		 * �������Ż���ѯ�Ż�������������֯�������ڵĲ�ѯ����
		 */
		if(StringUtils.isNotEmpty(paramVO.getPk_org())){
			String[] pk_orgs = paramVO.getPk_org().split(",");
			querySql.append("and yer_yerexpbill." + SqlUtils.getInStr("pk_org", pk_orgs));
		}
		if(StringUtils.isNotEmpty(paramVO.getDate_left())){
			querySql.append(" and yer_yerexpbill." + queryField.getBilldate()
					+ " >= '" + paramVO.getDate_left() + "'\n");
		}
		if(StringUtils.isNotEmpty(paramVO.getDate_right())){
			querySql.append(" and yer_yerexpbill." + queryField.getBilldate()
					+ " <= '" + paramVO.getDate_right() + "'\n");
		}
		if(StringUtils.isNotEmpty(paramVO.getAmount_left())){
			//�ʲ����ٵ����ʲ��䶯���޽���ֶε��ݣ������ս�����
			if(queryField.getCode().startsWith("HF")){
				querySql.append("");
			}else if(queryField.getCode().startsWith("HG")){
				querySql.append("");
			}else{
				querySql.append(" and yer_yerexpbill." + queryField.getTotal()
						+ " >= " + paramVO.getAmount_left() + "\n");
			}
		}
		if(StringUtils.isNotEmpty(paramVO.getAmount_right())){
			//�ʲ����ٵ����ʲ��䶯����?���ֶε��ݣ������ս����?
			if(queryField.getCode().startsWith("HF")){
				querySql.append("");
			}else if(queryField.getCode().startsWith("HG")){
				querySql.append("");
			}else{
				querySql.append(" and yer_yerexpbill." + queryField.getTotal()
						+ " <= " + paramVO.getAmount_right() + "\n");
			}
		}
		//end
		return querySql.toString();
	}

	/**
	 * ƴ�Ӳ˵�Ȩ��������ֻ��ѯ��portal�˵�Ȩ�޵Ľ�������
	 * 
	 * @param modelTradeTypeMap
	 *            ģ������Ȩ�޵Ľ�������ӳ��
	 * @param paramVO
	 * @return
	 */
	public static String getPermCond(PortalQueryFieldName queryField,
			Map<String, String> modelCodeMap, YerExpBillParamVO paramVO) {

		StringBuilder querySql = new StringBuilder();
		HashSet<String> tradeTypes = (HashSet<String>) AppUtil
				.getAppAttr("YER#PERMTRADETYPES");

		if (tradeTypes == null) {
			tradeTypes = getPermTradetypes();
		}
		Set<String> tradTypes = getTradetypesByCode(tradeTypes, queryField);
		if (tradTypes != null) {
			try {
				querySql.append(" and "
						+ SqlUtils.getInStr(
								"yer_yerexpbill." + queryField.getJylxbm(),
								tradTypes.toArray(new String[0])));
			} catch (BusinessException e) {
				Logger.error(e.getMessage(), e);
			}
		}

		return querySql.toString();
	}

	public static HashSet<String> getPermTradetypes() {
		List<BillNavMenuItemVO> billNavMenuVos = ExpPortalUtil
				.getBillNavMenuList();
		Map<String, Integer> billTypeCodeToIndexMap = new HashMap<String, Integer>();
		for (int i = 0; i < billNavMenuVos.size(); i++) {
			BillNavMenuItemVO nav = billNavMenuVos.get(i);
			if (StringUtils.isNotEmpty(nav.getFunurl())
					&& !"ALL".equals(nav.getFunurl())) {
				String billTypeCode = ExpenseApproveUtil
						.getBillTypeByFunURL(nav.getFunurl());
				billTypeCodeToIndexMap.put(billTypeCode, i);
			}
		}
		HashSet<String> tradeTypes = new HashSet<String>();
		tradeTypes.addAll(billTypeCodeToIndexMap.keySet());
		return tradeTypes;
	}

	/**
	 * �Ƿ�װ�ʽ�ϵͳ
	 * 
	 * @return
	 */
	public static boolean isCmpused() {
		UFBoolean isCmpStart = (UFBoolean) AppUtil.getAppAttr("YERISCMPSTART#"
				+ YerUtil.getPK_group());
		if (isCmpStart == null) {
			isCmpStart = new UFBoolean(BXUtil.isProductInstalled(
					YerUtil.getPK_group(), BXConstans.TM_CMP_FUNCODE));
			AppUtil.addAppAttr("YERISCMPSTART#" + YerUtil.getPK_group(),
					isCmpStart);
		}
		return isCmpStart.booleanValue();
	}

	/**
	 * ��õ��ݴ����ע����Ĺ�ϵӳ��
	 * 
	 * @return
	 */
	public static Map<String, IExpPortal> getCodeToIExpPortalMap() {
		Map<String, IExpPortal> map = new HashMap<String, IExpPortal>();
		/*
		 * modify author: houmeng3 begin
		 */
		Map<String, IExpPortal> expPortalClass = getAllExpPortalClass();
		if (expPortalClass != null && expPortalClass.size() > 0) {
			for (Map.Entry<String, IExpPortal> entry : expPortalClass
					.entrySet()) {
				setFreebillTypeCode(entry.getValue(), entry.getKey());
				PortalQueryFieldName fieldName = entry.getValue()
						.getPortalQueryFieldName();
				if (("freebillTypeCode".equals(fieldName.getCode()))) {
					map.put((String) entry.getKey(), entry.getValue());
				} else {
					map.put(entry.getValue().getPortalQueryFieldName()
							.getCode(), entry.getValue());
				}
				// List<IExpPortal> expPortalClass = getAllExpPortalClass();
				// if (expPortalClass != null && expPortalClass.size() > 0) {
				// for (int i = 0; i < expPortalClass.size(); i++) {
				// map.put(expPortalClass.get(i).getPortalQueryFieldName()
				// .getCode(), expPortalClass.get(i));

			}
		}
		/*
		 * end
		 */
		return map;
	}

	public static String getDjdl(String billTypeCode) {
		// add by duxd start ��Ӧ��׼��ά�� 20170831
		if (billTypeCode != null && billTypeCode.startsWith("4S12")) {
			return "4S12";
		}
		// add by duxd end ��Ӧ��׼��ά�� 20170831
		
		//add by ����� ������Ϣά�� 20180117 start
		if (billTypeCode != null && billTypeCode.startsWith("CARI")) {
			return "CARI";
		}
		//add by ����� ������Ϣά�� 20180117 end

		// add by lyh 2016-9-12 �ʲ��䶯
		if (billTypeCode != null && billTypeCode.startsWith("HG")) {
			return "HG";
		}
		if (billTypeCode != null && billTypeCode.startsWith("HF")) {
			return "HF";
		}
		// add end
		Map<String, String> modelCodeMap = ExpPortalFactory.getModelCodeMap();
		Map<String, String> realCodeMap = new HashMap<String, String>();
		realCodeMap.putAll(modelCodeMap);

		// �ո����⴦��
		if (billTypeCode != null && billTypeCode.startsWith("FP0")) {
			return "FIV";
		}
		if (billTypeCode != null
				&& (billTypeCode.startsWith("D0") || billTypeCode
						.startsWith("F0"))) {
			return "F0";
		}
		if (billTypeCode != null
				&& (billTypeCode.startsWith("D1") || billTypeCode
						.startsWith("F1"))) {
			return "F1";
		}
		if (billTypeCode != null
				&& (billTypeCode.startsWith("D2") || billTypeCode
						.startsWith("F2"))) {
			return "F2";
		}
		if (billTypeCode != null
				&& (billTypeCode.startsWith("D3") || billTypeCode
						.startsWith("F3"))) {
			return "F3";
		}

		int len = 3;
		if (billTypeCode.startsWith("261X-")) {// ���乩Ӧ������
			BilltypeVO billtypeVO = PfDataCache.getBillType(billTypeCode);
			if (billtypeVO != null) {
				String modelCodeME = billtypeVO.getSystemcode();
				if ("me".equalsIgnoreCase(modelCodeME)) {
					String djdlME = billtypeVO.getParentbilltype();
					return djdlME;
				}
			}
		}

		if (billTypeCode.startsWith("36D1")) {
			return "36D1";
		}

		String modelcode = modelCodeMap.get(billTypeCode);
		if (modelcode != null && "cmp".equals(modelcode)) {
			len = 4;
		}
		/*
		 * modify author houmeng3 �������䱨�����Ż� begin
		 */
		else if (modelcode != null && "sscwo".equals(modelcode)) {
			return billTypeCode;
		} else if (modelcode != null && "me".equals(modelcode)) {
			len = 4;
		} else if (modelcode != null && "so".equals(modelcode)) {
			len = 2;
		}

		/*
		 * end
		 */
		String djdl = "";
		if (billTypeCode.length() >= len) {
			djdl = billTypeCode.substring(0, len);
		} else {
			djdl = billTypeCode;
		}

		return djdl;
	}

	/**
	 * ���ݵ������ͱ���͵���״̬��ȡ����״̬ʵ��ֵ
	 * 
	 * @param billTypeCode
	 * @return
	 */
	public static String getDjzt(String billTypeCode, int djzt) {

		String strDJZT = "";
		String djdl = getDjdl(billTypeCode);
		// add by lyh 2016-9-23 �ʲ��䶯
		if (djdl.equals("HG") || djdl.equals("HF")) {
			switch (djzt) {
			case 0:
				strDJZT = "����̬";
				break;
			case 1:
				strDJZT = "���ύ";
				break;
			case 2:
				strDJZT = "������";
				break;
			case 3:
				strDJZT = "����ͨ��";
				break;
			}
		} else
		// add end
		if (djdl.equals("263") || djdl.equals("264")) {
			switch (djzt) {
			case -1:
				strDJZT = "����";
				break;
			case 0:
				strDJZT = "�ݴ�";
				break;
			case 1:
				strDJZT = "����";
				break;
			case 2:
				strDJZT = "����";
				break;
			case 3:
				strDJZT = "ǩ��";
				break;
			}
		} else if (djdl.equals("LE3")) {
			switch (djzt) {
			case -1:
				strDJZT = "����";
				break;
			case 0:
				strDJZT = "����";
				break;
			case 1:
				strDJZT = "����";
				break;
			case 2:
				strDJZT = "����";
				break;
			case 3:
				strDJZT = "����";
				break;
			}
		} else if (djdl.equals("262") || djdl.equals("261")) {
			switch (djzt) {
			case -1:
				strDJZT = "����";
				break;
			case 0:
				strDJZT = "�ݴ�";
				break;
			case 1:
				strDJZT = "����";
				break;
			case 3:
				strDJZT = "������";
				break;
			}
		} else if (djdl.equals("36D1")) {
			switch (djzt) {
			case -1:
				strDJZT = "����̬";
				break;
			case 1:
				strDJZT = "���ύ";
				break;
			case 2:
				strDJZT = "������";
				break;
			case 3:
				strDJZT = "������";
				break;
			case 4:
				strDJZT = "��������";
				break;
			case 5:
				strDJZT = "������";
				break;
			}
		} else if (djdl.equals("F0") || djdl.equals("F1") || djdl.equals("F2")
				|| djdl.equals("F3")) {
			switch (djzt) {
			case -1:
				strDJZT = "����";
				break;
			case -99:
				strDJZT = "�ݴ�";
				break;
			case 1:
				strDJZT = "����ͨ��";
				break;
			case 2:
				strDJZT = "������";
				break;
			case 8:
				strDJZT = "ǩ��";
				break;
			case 9:
				strDJZT = "δȷ��";
				break;
			}
		} else if (djdl.equals("FIV")) {
			switch (djzt) {
			case 1:
				strDJZT = "δ����";
				break;
			case 2:
				strDJZT = "������";
				break;
			case 3:
				strDJZT = "������";
				break;
			}
		} else if (djdl.equals("doc")) {// chenysh@yonyou.com
			switch (djzt) {
			case -1:
				strDJZT = "����";
				break;
			case 0:
				strDJZT = "�ݴ�";
				break;
			case 1:
				strDJZT = "����";
				break;
			case 2:
				strDJZT = "���";
				break;
			case 3:
				strDJZT = "ǩ��";
				break;
			case 4:
				strDJZT = "�����";
				break;
			}
			//add by duxd start ��Ӧ��׼��ά�� 20170831
		}else if(djdl.equals("4S12")){
			//0=���ɣ�1=�ύ��2=�����У�3=����δͨ����4=����ͨ����5=�ݴ棬6=�Ѵ�أ�7=������-1=����ҵ��ʹ�ã�8=ȷ�ϣ�
			switch (djzt) {
			case -1:
				strDJZT = "����ҵ��ʹ��";
				break;
			case 0:
				strDJZT = "����";
				break;
			case 1:
				strDJZT = "�ύ";
				break;
			case 2:
				strDJZT = "������";
				break;
			case 3:
				strDJZT = "����δͨ��";
				break;
			case 4:
				strDJZT = "����ͨ��";
				break;
			case 5:
				strDJZT = "�ݴ�";
				break;
			case 6:
				strDJZT = "�Ѵ��";
				break;
			case 7:
				strDJZT = "����";
				break;
			case 8:
				strDJZT = "ȷ��";
				break;
			}
		}

		return strDJZT;
	}

	/**
	 * ���ݵ������ͱ��������״̬��ȡ����״̬ʵ��ֵ
	 * 
	 * @param billTypeCode
	 * @param djzt
	 * @return
	 */
	public static String getSpzt(String billTypeCode, int spzt) {

		String strSPZT = "";
		String djdl = getDjdl(billTypeCode);
		// add by lyh 2016-9-23 �ʲ��䶯
		if (djdl.equals("HG") || djdl.equals("HF")) {
			switch (spzt) {
			case 0:
				strSPZT = "����̬";
				break;
			case 1:
				strSPZT = "���ύ";
				break;
			case 2:
				strSPZT = "������";
				break;
			case 3:
				strSPZT = "����ͨ��";
				break;
			}
		} else 
		// add end
		if (djdl.equals("FIV")) {
			switch (spzt) {
			case 1:
				strSPZT = "δ����";
				break;
			case 2:
				strSPZT = "������";
				break;
			case 3:
				strSPZT = "������";
				break;
			}
		} else if (djdl.equals("doc")) {// chenysh+
			switch (spzt) {
			case -1:
				strSPZT = "����̬";
				break;
			case 0:
				strSPZT = "������";
				break;
			case 1:
				strSPZT = "����ͨ��";
				break;
			case 2:
				strSPZT = "������ͨ��";
				break;
			case 3:
				strSPZT = "�ύ̬";
				break;
			}

		} else {
			switch (spzt) {
			case -1:
				strSPZT = "����";
				break;
			case 0:
				strSPZT = "����δͨ��";
				break;
			case 1:
				strSPZT = "����ͨ��";
				break;
			case 2:
				strSPZT = "����������";
				break;
			case 3:
				strSPZT = "�ύ";
				break;
			}
		}

		return strSPZT;
	}

	/**
	 * ���ݵ������ͺ���Դϵͳ�����ȡ��Դϵͳ��ʵ��ֵ
	 */
	public static String getSrcSys(String billTypeCode, String srcSys,
			Map<Integer, String> arapSrc) {

		String ss = "";
		String djdl = getDjdl(billTypeCode);
		if (djdl.equals("261") || djdl.equals("262") || djdl.equals("263")
				|| djdl.equals("264") || djdl.equals("LE3")) {
			if ("264X-Cxx-0033".equals(billTypeCode)
					|| "264X-Cxx-0031".equals(billTypeCode)
					|| "264X-Cxx-0034".equals(billTypeCode)) {
				if (srcSys != null && !srcSys.equals("")) {
					ss = "��Դϵͳ";
				} else {
					ss = "���ù���";
				}
			} else {
				ss = "���ù���";
			}
		} else if (djdl.equals("F0") || djdl.equals("F1") || djdl.equals("F2")
				|| djdl.equals("F3")) {
			if (srcSys != null && !srcSys.equals("")) {
				try {
					ss = arapSrc.get(Integer.parseInt(srcSys));
				} catch (Exception e) {
					ss = srcSys;
				}
			}
		} else if (djdl.equals("36D1")) {
			if (srcSys == null || srcSys.equals("")) {
				ss = "�ֽ����";
			} else if (srcSys.equals("1")) {
				ss = "Ӧ��ϵͳ";
			} else if (srcSys.equals("2")) {
				ss = "����";
			} else if (srcSys.equals("3")) {
				ss = "�ֹ�¼��";
			} else if (srcSys.equals("4")) {
				ss = "��Ӧ��";
			} else if (srcSys.equals("5")) {
				ss = "��Ŀ����";
			}
		} else if (djdl.equals("FIV") || djdl.equals("FP0")) {
			ss = "Ӧ��ϵͳ";
		} else if (djdl.equals("doc")) { // ��Ӧ��
			ss = "NCϵͳ";
		}

		if (ss == null || ss.trim().length() == 0) {
			return srcSys;
		}
		return ss;
	}

	/**
	 * ���ݵ������ͺ���Դϵͳ�����ȡ��Դϵͳ��ʵ��ֵ
	 */
	public static String getSrcSys(String billTypeCode,
			Map<Integer, String> arapSrc, String billid) {

		String ss = "";
		String djdl = getDjdl(billTypeCode);
		if (djdl.equals("261") || djdl.equals("262") || djdl.equals("263")
				|| djdl.equals("264") || djdl.equals("LE3")) {
			if ("264X-Cxx-0033".equals(billTypeCode)
					|| "264X-Cxx-0031".equals(billTypeCode)
					|| "264X-Cxx-0034".equals(billTypeCode)) {
				String sql = "select zyx30 from er_bxzb where pk_jkbx ='"
						+ billid + "'";
				String srcSys = ExpenseApproveUtil.getArapSrcSys(sql);
				if (srcSys != null && !srcSys.equals("")) {
					ss = "��Դϵͳ";
				} else {
					ss = "���ù���";
				}
			} else {
				ss = "���ù���";
			}
		} else if (djdl.equals("F0") || djdl.equals("F1") || djdl.equals("F2")
				|| djdl.equals("F3")) {
			String sql = "";
			if (djdl.equals("F0"))
				sql = "select case  when  def30 ='~' or def30 is null then convert(varchar,src_syscode) else def30 end from ar_recbill where pk_recbill ='"
						+ billid + "'";
			if (djdl.equals("F1"))
				sql = "select case  when  def30 ='~' or def30 is null then convert(varchar,src_syscode) else def30 end from ap_payablebill where pk_payablebill ='"
						+ billid + "'";
			if (djdl.equals("F2"))
				sql = "select case  when  def30 ='~' or def30 is null then convert(varchar,src_syscode) else def30 end from ar_gatherbill where pk_gatherbill ='"
						+ billid + "'";
			if (djdl.equals("F3"))
				sql = "select case  when  def30 ='~' or def30 is null then convert(varchar,src_syscode) else def30 end from ap_paybill where pk_paybill ='"
						+ billid + "'";
			String code = ExpenseApproveUtil.getArapSrcSys(sql);
			if (code != null) {
				ss = arapSrc.get(Integer.parseInt(code));
			}
		} else if (djdl.equals("36D1")) {
			String sql = "select sourcesystypecode from cmp_apply where pk_apply ='"
					+ billid + "'";
			String srcSys = ExpenseApproveUtil.getArapSrcSys(sql);
			if (srcSys == null) {
				ss = "�ֽ����";
			} else if (srcSys.equals("1")) {
				ss = "Ӧ��ϵͳ";
			} else if (srcSys.equals("2")) {
				ss = "����";
			} else if (srcSys.equals("3")) {
				ss = "�ֹ�¼��";
			} else if (srcSys.equals("4")) {
				ss = "��Ӧ��";
			} else if (srcSys.equals("5")) {
				ss = "��Ŀ����";
			}
		} else if (djdl.equals("FIV") || djdl.equals("FP0")) {
			ss = "Ӧ��ϵͳ";
		} else if (djdl.equals("doc")) { // ��Ӧ��
			ss = "NCϵͳ";
		}
		return ss;
	}

	/**
	 * ƴ�����н�������SQL���
	 * 
	 * @param queryBillSql
	 * @param entry
	 * @return
	 */
	public static String getBillQuerySQL(PortalQueryFieldName queryFiled,
			Map<String, String> modelCodeMap, YerExpBillParamVO paramVO,
			boolean isDetail) throws BusinessException {

		String tableName = queryFiled.getmTableName();
		if (!isDetail) {
			// �ո������������ϸ��ѯ��ֻ��ѯ����������ƴ�ӽ�����Ϣ��
			tableName = ExpPortalUtil.getRealTable(queryFiled.getCode(),
					tableName);
		}

		if (!tableName.startsWith("(")) {// ������տ�͸����tableName������ʵ�ı���ʱ������ӱ����
			tableName += " yer_yerexpbill";
		}

		StringBuffer sql = new StringBuffer();
		sql.append(" select "
				+ ExpPortalUtil.getBaseBillQueryField(queryFiled, isDetail,
						modelCodeMap));
		sql.append(" from " + tableName);
		sql.append(" where "
				+ ExpPortalUtil.getBaseBillWhere(queryFiled, isDetail,
						modelCodeMap, paramVO));
		if (queryFiled.getCode().equals("LE3")) {
			String tsql = sql.toString();
			tsql = tsql.replace("yer_yerexpbill.billtype", "'LE35'");
			return tsql;
		}

		return sql.toString();
	}

	/**
	 * ���ص��ݲ�ѯ��
	 * 
	 * @param queryField
	 * @return
	 */
	public static String getBaseBillQueryField(PortalQueryFieldName queryField,
			Map<String, String> modelCodeMap) {

		return getBaseBillQueryField(queryField, true, modelCodeMap);
	}

	/**
	 * �������Ż���ȡ��ѯ�ֶΣ�Ϊ����һ�£���ѯ���������͵�����ϸ��Ҫ���������
	 * 
	 * @param queryField
	 * @param isDetail
	 *            �Ƿ��ѯ��ϸ
	 * @param modelCodeMap
	 * @return
	 */
	public static String getBaseBillQueryField(PortalQueryFieldName queryField,
			boolean isDetail, Map<String, String> modelCodeMap) {

		List<String> fields = new ArrayList<String>();
		if (isDetail) {

			fields.add(" creator");
			fields.add(getStringNullField(queryField.getPk_id())
					+ " as pk_yerbill");
			fields.add(getStringNullField(queryField.getBillno())
					+ " as billno");
			fields.add(getStringNullField(queryField.getBilldate())
					+ " as billdate");
			fields.add(getStringNullField(queryField.getBillMaker())
					+ " as billmaker");
			fields.add(getStringNullField(queryField.getPk_group())
					+ " as pk_group");
			fields.add(getStringNullField(queryField.getPk_org())
					+ " as pk_org");
			fields.add(getStringNullField(queryField.getDeptId())
					+ " as deptid");
			fields.add(getStringNullField(queryField.getDjlxbm())
					+ " as billtype");
			fields.add(getStringNullField(queryField.getJylxbm())
					+ " as tradetype");
			fields.add(getStringNullField(queryField.getJylxid())
					+ " as pk_tradetypeid");
			fields.add(getNumNullField(queryField.getSpzt())
					+ " as approvestatus");
			fields.add(getNumNullField(queryField.getTotal()) + " as total");
			fields.add(getNumNullField(queryField.getPayFlag()) + " as payflag");
			fields.add(getStringNullField(queryField.getPk_id()) + " as billid");
			fields.add(getStringNullField(queryField.getDjdl()) + " as djdl");
			fields.add(getNumNullField(queryField.getDjzt()) + " as djzt");
			fields.add(getStringNullField(queryField.getApprover())
					+ " as approver");
			fields.add(queryField.getSpsj() + " as shrq");
			fields.add(queryField.getQcbz() + " as qcbz");
			fields.add(queryField.getSxzt());
			fields.add(queryField.getCurrtype() == null ? "pk_currtype"
					: queryField.getCurrtype().equals("none") ? "' ' as defitem10"
							: queryField.getCurrtype() + " as defitem10");
			fields.add(" dr");
			fields.add(getStringNullField(queryField.getSrcSys())
					+ " as defitem11");
			//chenlun	����		����755�����ӱ������Ż���ѯ�����ʾ�ֶ�		2017/09/13	begin
			fields.add(getStringNullField(queryField.getInvoicecode()) + " as defitem3");//��Ʊ��
			fields.add(getStringNullField(queryField.getDef26()) + " as defitem4");//��ͬ��
			fields.add(getStringNullField(queryField.getNoinvoice()) + " as defitem5");//��Ʊ����
			fields.add(getStringNullField(queryField.getJsfs()) + " as defitem6");//���㷽ʽ
			fields.add(getStringNullField(queryField.getDef38()) + " as defitem8");//����֧����ʽ
			fields.add(getStringNullField(queryField.getPk_payorg_v()) + " as defitem12");//֧����λ
			//chenlun	����		����755�����ӱ������Ż���ѯ�����ʾ�ֶ�		2017/09/13	end

			appendQueryField(fields, queryField, isDetail, modelCodeMap);

			return getQeryFieldSql("yer_yerexpbill", fields, isDetail);
		} else {
			fields.add(getStringNullField(queryField.getPk_id())
					+ " as pk_yerbill");
			fields.add(getStringNullField(queryField.getJylxbm())
					+ " as tradetype");

			appendQueryField(fields, queryField, isDetail, modelCodeMap);

			return getQeryFieldSql("yer_yerexpbill", fields, isDetail);
		}

	}

	/**
	 * ƴ�Ӷ���Ĳ�ѯ�ֶ�
	 * 
	 * @param fields
	 * @param queryField
	 * @param isDetail
	 * @param modelCodeMap
	 */
	public static void appendQueryField(List<String> fields,
			PortalQueryFieldName queryField, boolean isDetail,
			Map<String, String> modelCodeMap) {

	}

	/**
	 * ��ȡ�������Ż��Ż���������
	 * 
	 * @param queryField
	 * @param isDetail
	 * @param modelCodeMap
	 * @return
	 */
	public static String getBaseBillWhere(PortalQueryFieldName queryField,
			boolean isDetail, Map<String, String> modelCodeMap,
			YerExpBillParamVO paramVO)  throws BusinessException {

		StringBuilder sql = new StringBuilder();
		sql.append(getUserCond(queryField, modelCodeMap, paramVO));
		sql.append(getIsCompleteCond(queryField, modelCodeMap, paramVO));
		sql.append(getSpecialCond(queryField, modelCodeMap, paramVO));
		sql.append(getQueryCond(queryField, modelCodeMap, paramVO));
		sql.append(getPermCond(queryField, modelCodeMap, paramVO));
		String billFlowState = (String) (AppUtil.getAppAttr("BILLFLOWSTATE") == null ? "all"
				: AppUtil.getAppAttr("BILLFLOWSTATE"));//�������̣���������״̬ add by houmeng3
		if(FlowStateConstant.REJECT_NAME.equals(billFlowState)){//����״̬Ϊ����  �������� add by houmeng3
			sql.append(" and exists (select flowstate from ssc_rejectsetting reject where reject.flowstate in(2,3,5) and reject.billid=yer_yerexpbill."+queryField.getPk_id()+" ) \n");
		}else if(FlowStateConstant.COMMINT_NAME.equals(billFlowState)){//����״̬Ϊ�ύ
			sql.append(" and exists (select flowstate from ssc_rejectsetting reject where reject.flowstate=0 and reject.billid=yer_yerexpbill."+queryField.getPk_id()+" ) \n");
		}else if(FlowStateConstant. RECOMMINT_NAME.equals(billFlowState)){//����״̬Ϊ�����ύ
			sql.append(" and exists (select flowstate from ssc_rejectsetting reject where reject.flowstate=1 and reject.billid=yer_yerexpbill."+queryField.getPk_id()+" ) \n");
		}else if(FlowStateConstant. REJECT_REPEAT_NAME.equals(billFlowState)){//����״̬Ϊ��������
			sql.append(" and exists (select flowstate from ssc_rejectsetting reject where reject.flowstate=2 and reject.billid=yer_yerexpbill."+queryField.getPk_id()+" ) \n");
		}else if(FlowStateConstant. REJECT_UNREPEAT_NAME.equals(billFlowState)){//����״̬Ϊ���ز�����
			sql.append(" and exists (select flowstate from ssc_rejectsetting reject where reject.flowstate=3 and reject.billid=yer_yerexpbill."+queryField.getPk_id()+" ) \n");
		}
		return sql.toString();
	}

	/**
	 * ͳ�Ʋ�ͬ���������ж�������ʱ�����Ľ�����Ϣ�������ո��ı���Ҫ���������������ܿ�����ȡ��BasePortal��ʵ������
	 * 
	 * @param djdl
	 * @return
	 */
	public static String getRealTable(String djdl, String tableName) {

		List<String> gatherBillCodes = Arrays.asList(new String[] { "D2", "F2",
				"F2-", "103" });
		List<String> payBillCodes = Arrays.asList(new String[] { "D3", "F3",
				"F3-", "104" });

		if (gatherBillCodes.contains(djdl)) {
			return "ar_gatherbill";
		} else if (payBillCodes.contains(djdl)) {
			return "ap_paybill";
		}

		return tableName;
	}

	/**
	 * �ո��ĵ����Ӳ�ѯ�����Ż�����or exists ת��Ϊunion���ⲿ��Ӧ�÷ŵ��ո��Ĵ�����
	 * 
	 * @param djdl
	 * @return
	 */
	public static String getPsnDocWhere(String djdl, String psndocwhere) {

		List<String> gatherBillCodes = Arrays.asList(new String[] { "D2", "F2",
				"F2-", "103" });
		List<String> payableBillCodes = Arrays.asList(new String[] { "D1",
				"F1", "F1-", "102" });
		List<String> payBillCodes = Arrays.asList(new String[] { "D3", "F3",
				"F3-", "104" });
		List<String> recBillCodes = Arrays.asList(new String[] { "D0", "F0",
				"F0-", "101" });
		if (gatherBillCodes.contains(djdl)) {
			return "pk_gatherbill in (select pk_gatherbill from ar_gatheritem where pk_psndoc='%pk_psndoc%' and ar_gatheritem.dr=0 union all select pk_gatherbill from ar_gatherbill where billmaker='%cuserid%')";
		} else if (payableBillCodes.contains(djdl)) {
			return "pk_payablebill in (select pk_payablebill from ap_payableitem where pk_psndoc='%pk_psndoc%' and ap_payableitem.dr=0 union all select pk_payablebill from ap_payablebill where billmaker='%cuserid%')";
		} else if (payBillCodes.contains(djdl)) {
			return "pk_paybill in (select pk_paybill from ap_payitem where pk_psndoc='%pk_psndoc%' and ap_payitem.dr=0 union all select pk_paybill from ap_paybill where billmaker='%cuserid%')";
		} else if (recBillCodes.contains(djdl)) {
			return "pk_recbill in (select pk_recbill from ar_recitem where pk_psndoc='%pk_psndoc%' and ar_recitem.dr=0 union all select pk_recbill from ar_recbill where billmaker='%cuserid%')";
		}
		return psndocwhere;
	}

	/**
	 * ����������ѯ��
	 * 
	 * @param queryField
	 * @return
	 */
	public static String getApproveQueryField(PortalQueryFieldName queryField) {

		String[] fields = {
				getStringNullField(queryField.getPk_id()) + " as pk_yertask",
				getStringNullField(queryField.getBilldate()) + " as billdate",
				getStringNullField(queryField.getBillMaker()) + " as billmaker",
				getStringNullField(queryField.getPk_group()) + " as pk_group",
				getStringNullField(queryField.getPk_org()) + " as pk_org",
				getStringNullField(queryField.getDeptId()) + " as deptid",
				getNumNullField(queryField.getTotal()) + " as total",
				getStringNullField(queryField.getSzxmid()) + " as pk_iobsclass",
				getStringNullField(queryField.getPk_project())
						+ " as pk_project",
				getStringNullField(queryField.getDjdl()) + " as djdl",
				getStringNullField(queryField.getApprover()) + " as approver",
				//chenlun	����		����755�������������Ż���ѯ�����ʾ�ֶ�		2017/09/14	begin
				getStringNullField(queryField.getInvoicecode()) + " as defitem8",//��Ʊ��
				getStringNullField(queryField.getDef26()) + " as defitem9",//��ͬ��
				getStringNullField(queryField.getNoinvoice()) + " as defitem12",//��Ʊ����
				getStringNullField(queryField.getJsfs()) + " as defitem13",//���㷽ʽ
				getStringNullField(queryField.getDef38()) + " as defitem16",//����֧����ʽ
				getStringNullField(queryField.getPk_payorg_v()) + " as defitem18",//֧����λ
				//chenlun	����		����755�������������Ż���ѯ�����ʾ�ֶ�		2017/09/14	end
				queryField.getSpsj() + " as shrq"
		// getNumNullField(queryField.getDefitem10()) + " as defitem10"
		};
		/*
		 * modify author:houmeng3 begin
		 */
		return getApproveQeryFieldSql(
				queryField.getmTableName().contains("left") ? queryField
						.getmTableName().split(" ")[1]
						: queryField.getmTableName(), fields);
		// return getQeryFieldSql(queryField.getmTableName(), fields);
	}

	private static String getApproveQeryFieldSql(String tableName,
			String[] queryFidle) {
		StringBuffer querySql = new StringBuffer();
		// malxa�޸ģ�Ϊ������arap�Ļ���ȵ���֧��״̬��ʵ����������ʱ�޸ģ�������Ҫ�˹��ܾ�����Ϊ֮ǰ����
		if (queryFidle[0].indexOf("' '") == -1
				&& queryFidle[0].indexOf("-100") == -1) {
			querySql.append(tableName + "." + queryFidle[0]);
		} else {
			querySql.append(queryFidle[0]);
		}

		for (int i = 1; i < queryFidle.length; i++) {
			// malxa�޸ģ�Ϊ������arap�Ļ���ȵ���֧��״̬��ʵ����������ʱ�޸ģ�������Ҫ�˹��ܾ�����Ϊ֮ǰ����
			if (queryFidle[i].indexOf("' '") == -1
					&& queryFidle[i].indexOf("-100") == -1) {
				if (queryFidle[i].contains("%tablename%")) {
					String filed = queryFidle[i].replace("%tablename%",
							tableName);
					querySql.append(",").append(filed);
				} else if (queryFidle[i].contains("%4641%")) {
					queryFidle[i] = queryFidle[i].replace("%4641%", "'4641'");
					querySql.append(",").append(queryFidle[i]);
				} else if (queryFidle[i].contains("%4642%")) {
					queryFidle[i] = queryFidle[i].replace("%4642%", "'4642'");
					querySql.append(",").append(queryFidle[i]);
				} else if (queryFidle[i].contains("%35%")) {
					queryFidle[i] = queryFidle[i].replace("%35%", "'35'");
					querySql.append(",").append(queryFidle[i]);
				}

				else {
					querySql.append(",")
							.append(tableName + "." + queryFidle[i]);
				}

			} else {
				querySql.append(",").append(queryFidle[i]);
			}
		}

		return querySql.toString();
	}

	// end
	public static String getRYXXID() {

		String jkbxrId = (String) AppUtil.getAppAttr("YERPSNID#"
				+ YerUtil.getPk_user());
		if (StringUtils.isEmpty(jkbxrId)) {
			String[] jkbxrIds = null;
			try {
				jkbxrIds = NCLocator
						.getInstance()
						.lookup(IBXBillPrivate.class)
						.queryPsnidAndDeptid(YerUtil.getPk_user(),
								YerUtil.getPK_group());
				jkbxrId = jkbxrIds == null ? "" : jkbxrIds[0];
				AppUtil.addAppAttr("YERPSNID#" + YerUtil.getPk_user(), jkbxrId);
			} catch (BusinessException e1) {
				Logger.error(e1.getMessage(), e1);
			}
		}

		return jkbxrId;
	}

	/**
	 * 
	 * @param isWithPerm
	 *            �Ƿ��������Ȩ��
	 * @return
	 */
	public static List<BillNavMenuItemVO> getBillNavMenuList(boolean isWithPerm) {

		/*
		 * modify �������䱨�����Ż� houmeng3 begin
		 */
		// List<IExpPortal> expPortalClass = getAllExpPortalClass();
		Map<String, IExpPortal> expPortalClass = getAllExpPortalClass();
		Map<String, String> menuCategoryMap = new HashMap<String, String>();
		// ����һ���˵�
		Map<String, String> menuOneCodeMap = new HashMap<String, String>();
		// ��������˵�
		Map<String, String> menuChildCodeMap = new HashMap<String, String>();

		for (Map.Entry<String, IExpPortal> entry : expPortalClass.entrySet()) {
			// AppUtil.addAppAttr("freebillTypeCode", entry.getKey());
			setFreebillTypeCode(entry.getValue(), entry.getKey());
			menuCategoryMap.put(entry.getValue().getMenu_CategoryID(), entry
					.getValue().getMenu_CategoryID());
			List<String> childMenuCode = entry.getValue().getChildMenuCode();
			if (childMenuCode != null && childMenuCode.size() > 0) {
				for (String menuCode : childMenuCode) {
					menuChildCodeMap.put(menuCode, menuCode);
				}
				menuOneCodeMap.put(entry.getValue().getMenuCode(), entry
						.getValue().getMenuCode());
			} else {
				menuOneCodeMap.put(entry.getValue().getMenuCode(), "ALL");
			}
		}
		/*
		 * end
		 */
		List<MenuItemAdapterVO> menuItemAdapterVOList = new ArrayList<MenuItemAdapterVO>();
		List<BillNavMenuItemVO> billNavMenuItemVOList = new ArrayList<BillNavMenuItemVO>();
		try {
			// ��ȡ�˵�ע����
			ICpMenuQry menuQry = NCLocator.getInstance().lookup(
					ICpMenuQry.class);

			Iterator<String> ite = menuCategoryMap.keySet().iterator();
			BillNavMenuItemVO itemVO = new BillNavMenuItemVO();
			itemVO.setFuncode("0000");
			itemVO.setMenuitemcode("0000");
			itemVO.setMenuitemname(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("expensepub_2", "yerexpensepub-0026")/* @res "ȫ��" */);
			itemVO.setFunurl("ALL");
			billNavMenuItemVOList.add(itemVO);
			Map<String, String> menuCodeMap = new HashMap<String, String>();
			while (ite.hasNext()) {
				String menuCategory = ite.next();
				MenuRoot[] linkRoots = null;
				if (isWithPerm) {
					linkRoots = menuQry.getMenuRootWithPermission(menuCategory,
							YerUtil.getPk_user(), true, true);
				} else {
					linkRoots = menuQry.getMenuRoot(menuCategory);
				}
				for (int i = 0; i < linkRoots.length; i++) {
					MenuRoot thisRoot = linkRoots[i];
					if (menuOneCodeMap.get(thisRoot.getCode()) != null) {// һ���˵�����
						menuItemAdapterVOList = thisRoot.getNodes();
						for (int j = 0; j < menuItemAdapterVOList.size(); j++) {
							MenuItemAdapterVO menuItemAdapterVO = menuItemAdapterVOList
									.get(j);
							if (menuChildCodeMap != null
									&& menuChildCodeMap.size() > 0
									&& !menuOneCodeMap.get(thisRoot.getCode())
											.equals("ALL")) {// �����˵�����
								if (menuChildCodeMap.get(menuItemAdapterVO
										.getMenuitem().getCode()) == null) {
									continue;
								}
							}

							getMenuItemVO(menuItemAdapterVO,
									billNavMenuItemVOList,
									thisRoot.getAdapterVO(), menuCodeMap);
						}
					}
				}
			}

		} catch (CpbBusinessException e) {
			LfwLogger.error(e.getMessage(), e);
		}

		Collections.sort(billNavMenuItemVOList,
				new Comparator<BillNavMenuItemVO>() {

					@Override
					public int compare(BillNavMenuItemVO o1,
							BillNavMenuItemVO o2) {

						if (o1.getFuncode().equals(o2.getFuncode())) {
							if (o1.getMenuitemcode().equals(o1.getFuncode())) {
								return -1;
							} else if (o2.getMenuitemcode().equals(
									o2.getFuncode())) {
								return 1;

							} else {
								if (o1.getOrdernum() == null) {
									return -1;
								} else if (o2.getOrdernum() == null) {
									return 1;
								} else {
									return -1
											* (o1.getOrdernum().compareTo(o2
													.getOrdernum()));
								}
							}

						} else {
							return o1.getFuncode().compareTo(o2.getFuncode());
						}

					}

				});

		return billNavMenuItemVOList;
	}

	/**
	 * ����ע�����ȡ�˵���Ϣ��keyΪһ���˵���valueΪ��һ��?˵������ж����˵?
	 * 
	 * @return
	 */
	public static List<BillNavMenuItemVO> getBillNavMenuList() {
		return getBillNavMenuList(false);
	}

	public static String getExpenseNavHtml(
			List<BillNavMenuItemVO> billNavMenuVos, boolean isHasCompletePortlet) {
		// �����Ĳ˵�
		List<BillNavMenuItemVO> newbillNavMenuVos = new ArrayList<BillNavMenuItemVO>();
		StringBuffer b = new StringBuffer();
		b.append("<li id=\\\"li_expBillTypeCondi\\\"  style=\\\"display:block\\\">");
		b.append("<div class=\\\"expcondi_type_div\\\">" + "��������"/*
																 * nc.vo.ml.
																 * NCLangRes4VoTransl
																 * .
																 * getNCLangRes(
																 * ).getStrByID(
																 * "expensepub_2"
																 * ,
																 * "yerexpensepub-0040"
																 * )/* @res
																 * "��������"
																 */
				+ "</div>");
		// �Ƿ����µ�һ�У��µ�һ����Ҫ����
		boolean isNewLine = false;

		// ��ǰ��ʾ����
		int curRow = 1;
		// ���յ���������������
		newbillNavMenuVos = billTypeSortByCount(billNavMenuVos,
				isHasCompletePortlet);
		// �������ʧ�ܣ���Ĭ�ϲ˵�
		/*
		 * if(newbillNavMenuVos == null || newbillNavMenuVos.size() == 0) {
		 * newbillNavMenuVos = billNavMenuVos; }
		 */
		List<BillNavMenuItemVO> menuNewVos = new ArrayList<BillNavMenuItemVO>();
		BillNavMenuItemVO vo = new BillNavMenuItemVO();
		vo.setMenuitemcode("000");
		vo.setMenuitemname("ȫ��"/*
								 * nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
								 * .getStrByID("expensepub_2",
								 * "yerexpensepub-0026")/* @res "ȫ��"
								 */);
		menuNewVos.add(vo);
		StringBuffer hiddenBillTypeCode = new StringBuffer();
		for (int i = 0; i < newbillNavMenuVos.size(); i++) {
			BillNavMenuItemVO nav = newbillNavMenuVos.get(i);
			String billTypeCode = ExpenseApproveUtil.getBillTypeByFunURL(nav
					.getFunurl());
			if (StringUtils.isEmpty(billTypeCode)) {
				billTypeCode = nav.getMenuitemcode();
			}
			if (menuNewVos.size() == 11 && newbillNavMenuVos.size() > 10) {
				BillNavMenuItemVO showVo = new BillNavMenuItemVO();
				showVo.setMenuitemcode("show");
				showVo.setMenuitemname(nc.vo.ml.NCLangRes4VoTransl
						.getNCLangRes().getStrByID("expensepub_2",
								"yerexpensepub-0043")/* @res "����" */);
				int other = 0;
				for (int j = i; j < newbillNavMenuVos.size(); j++) {
					BillNavMenuItemVO itemvo = newbillNavMenuVos.get(j);
					other += itemvo.getBillCount();
				}
				showVo.setBillCount(other);
				menuNewVos.add(showVo);
			}
			if (!"".equals(nav.getFunurl()) && nav.getFunurl() != null
					&& !"ALL".equals(nav.getFunurl())) {
				// �������صĵ�������code���Ա���������ťʱ��������
				if (menuNewVos.size() == 12) {
					hiddenBillTypeCode.append("'" + billTypeCode + "'");
				} else if (menuNewVos.size() > 12) {
					hiddenBillTypeCode.append(",'").append(billTypeCode + "'");

				}
			}
			if (!"ALL".equals(nav.getFunurl())) {
				menuNewVos.add(nav);
			}
		}
		AppUtil.addAppAttr("approve_hiddenBillTypeCode",
				hiddenBillTypeCode.toString());
		// ÿ����ʾ����
		int showCount = 6;
		int total = 0;

		for (int i = 0; i < menuNewVos.size(); i++) {
			BillNavMenuItemVO nav = menuNewVos.get(i);

			if (nav.getBillCount() > 0 && !nav.getMenuitemname().equals("����")) {
				total += nav.getBillCount();
			}

			String billTypeCode = ExpenseApproveUtil.getBillTypeByFunURL(nav
					.getFunurl());
			if (StringUtils.isEmpty(billTypeCode)) {
				billTypeCode = nav.getMenuitemcode();
			}
			if (i == 0) {
				b.append(
						"<div id=\\\"expense_billType_showDiv_all_link\\\"  style=\\\"color:#FFFFFF;background-color:#028FD1\\\" onclick=\\\"javascript:queryExpenseBillByType(this,'');\\\">")
						.append(nav.getMenuitemname());
				if (nav.getBillCount() > 0) {
					b.append("&nbsp;" + nav.getBillCount() + "</div>");
				} else {
					b.append("</div>");
				}
				continue;
			}

			if ((i + 1) % showCount == 0) {
				b.append(
						"<div id=\\\"expense_billType_showDiv_"
								+ i
								+ "_link\\\"   onclick=\\\"javascript:queryExpenseBillByType(this,'"
								+ billTypeCode + "');\\\">").append(
						nav.getMenuitemname());
				if (nav.getBillCount() > 0) {
					b.append("&nbsp;" + nav.getBillCount() + "</div>");
				} else {
					b.append("</div>");
				}
				if (curRow < 2) {
					b.append("</li>");
					b.append("<li id=\\\"li_expBillTypeCondi\\\" class=\\\"\\\" >");
					b.append("<div  style=\\\"clear:both\\\">");
				} else {// ������ʾ
					b.append("</li>");
					b.append("<li id=\\\"li_expBillTypeCondi_display\\\" style=\\\"display:none;\\\">");
					b.append("<div  style=\\\"clear:both\\\">");
				}
				curRow++;
				isNewLine = true;
			} else {
				if (isNewLine) {// �µ�һ�У���Ҫ������ʾ

					b.append("<div class=\\\"expcondi_type_div\\\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div>");
					isNewLine = false;
				}
				b.append(
						"<div id=\\\"expense_billType_showDiv_"
								+ i
								+ "_link\\\"   onclick=\\\"javascript:queryExpenseBillByType(this,'"
								+ billTypeCode + "');\\\">").append(
						nav.getMenuitemname());
				if (nav.getBillCount() > 0) {
					b.append("&nbsp;" + nav.getBillCount() + "</div>");
				} else {
					b.append("</div>");
				}

			}

		}
		b.append("</li>");

		b.append("<li>");
		b.append("<div  style=\\\"clear:both;height:8px;\\\">&nbsp;</div>");
		b.append("</li>");

		if (!isHasCompletePortlet) {
			// ����δ�����
			AppLifeCycleContext
					.current()
					.getApplicationContext()
					.addExecScript(
							"$(\"#makebill_waitCount_div\").html(\"" + total
									+ "\");");
		}

		return b.toString();
	}

	public static String getApproveNavHtml(
			List<BillApproveNav> billApproveList, String curBillTypeCode) {
		StringBuffer hiddenBillTypeCode = new StringBuffer();
		StringBuffer b = new StringBuffer();
		b.append("<li>");
		b.append("<div class=\\\"expcondi_type_div\\\">"
				+ nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
						"expensepub_2", "yerexpensepub-0040")/* @res "��������" */
				+ "</div>");
		int allApproveCount = 0;
		// �Ƿ����µ�һ�У��µ�һ����Ҫ����
		boolean isNewLine = false;
		// ��ǰ��ʾ����
		int curRow = 1;
		List<BillApproveNav> approveNewList = new ArrayList<BillApproveNav>();
		BillApproveNav vo = new BillApproveNav();
		vo.setBillTypeCode("000");
		vo.setDjdl("000");
		vo.setBillTypeName(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
				.getStrByID("expensepub_2", "yerexpensepub-0026")/* @res "ȫ��" */);
		approveNewList.add(vo);
		// ���������͸��ݴ������Ӵ�С����
		Collections.sort(billApproveList, new Comparator<BillApproveNav>() {

			@Override
			public int compare(BillApproveNav arg0, BillApproveNav arg1) {
				return new Integer(arg1.getApproveCount())
						.compareTo(new Integer(arg0.getApproveCount()));
			}
		});
		for (int i = 0; i < billApproveList.size(); i++) {
			vo = billApproveList.get(i);
			vo.setBillTypeName(ExpUtil.getBilltypeNameMultiLang(vo
					.getBillTypeCode()));
			if (approveNewList.size() == 11 && billApproveList.size() > 10) {
				BillApproveNav showVo = new BillApproveNav();
				showVo.setBillTypeCode("show");
				showVo.setDjdl("show");
				showVo.setBillTypeName(nc.vo.ml.NCLangRes4VoTransl
						.getNCLangRes().getStrByID("expensepub_2",
								"yerexpensepub-0043")/* @res "����" */);
				int othernum = 0;
				for (int j = i; j < billApproveList.size(); j++) {
					othernum += Integer.parseInt(billApproveList.get(j)
							.getApproveCount());
				}
				showVo.setApproveCount(othernum + "");
				approveNewList.add(showVo);
			}
			// �������صĵ�������code���Ա���������ťʱ��������
			if (approveNewList.size() == 12) {
				hiddenBillTypeCode.append(billApproveList.get(i)
						.getBillTypeCode());
			} else if (approveNewList.size() > 12) {
				hiddenBillTypeCode.append(",").append(
						billApproveList.get(i).getBillTypeCode());

			}
			approveNewList.add(billApproveList.get(i));
		}

		AppUtil.addAppAttr("approve_hiddenBillTypeCode",
				hiddenBillTypeCode.toString());

		// ÿ����ʾ����
		int showCount = 6;

		for (int i = 0; i < approveNewList.size(); i++) {
			BillApproveNav nav = approveNewList.get(i);
			if (i == 0) {
				if (StringUtils.isEmpty(curBillTypeCode)
						|| nav.getBillTypeCode().equals("000")) {
					b.append(
							"<div id=\\\"check_billType_showDiv_all\\\" class=\\\"expcondi_div\\\" style=\\\"cursor:pointer;color:#FFFFFF;background-color:#028FD1\\\" onclick=\\\"javascript:queryBillByType(this,'"
									+ nav.getBillTypeCode()
									+ "','"
									+ nav.getDjdl() + "');\\\">").append(
							nav.getBillTypeName() + "</div>");
				} else {
					b.append(
							"<div id=\\\"check_billType_showDiv_all\\\" class=\\\"expcondi_div\\\" style=\\\"cursor:pointer;\\\" onclick=\\\"javascript:queryBillByType(this,'"
									+ nav.getBillTypeCode()
									+ "','"
									+ nav.getDjdl() + "');\\\">").append(
							nav.getBillTypeName() + "&nbsp;"
									+ nav.getApproveCount() + "</div>");
				}
				continue;
			}

			String djdl = nav.getDjdl();
			if (("".equals(djdl) || djdl == null || "null".equals(djdl))
					&& nav.getBillTypeCode().length() >= 3) {
				djdl = nav.getBillTypeCode().substring(0, 3);
			} else if ("102".equals(djdl)) {// Ӧ�������ݴ���͵������Ͳ�һ�£���ʱ�����⴦��
				djdl = "F1";
			} else if ("bt".equals(djdl)) {// �������뵥�ݴ���
				djdl = "36D1";
			}
			if (!nav.getBillTypeName().equals("����"))
				allApproveCount += Integer.parseInt(nav.getApproveCount());
			if ((i + 1) % showCount == 0) {
				if (StringUtils.isNotEmpty(curBillTypeCode)
						&& nav.getBillTypeCode().equals(curBillTypeCode)) {
					b.append(
							"<div id=\\\"check_billType_showDiv_"
									+ i
									+ "\\\" class=\\\"expcondi_div\\\" style=\\\"cursor:pointer;color:#FFFFFF;background-color:#028FD1;\\\" onclick=\\\"javascript:queryBillByType(this,'"
									+ nav.getBillTypeCode() + "','"
									+ nav.getDjdl() + "');\\\">").append(
							nav.getBillTypeName() + "&nbsp;"
									+ nav.getApproveCount() + "</div>");

				} else {
					b.append(
							"<div id=\\\"check_billType_showDiv_"
									+ i
									+ "\\\" class=\\\"expcondi_div\\\" style=\\\"cursor:pointer;\\\" onclick=\\\"javascript:queryBillByType(this,'"
									+ nav.getBillTypeCode() + "','"
									+ nav.getDjdl() + "');\\\">").append(
							nav.getBillTypeName() + "&nbsp;"
									+ nav.getApproveCount() + "</div>");
				}

				if (curRow < 2) {
					b.append("</li>");
					b.append("<li id=\\\"li_expCheckTypeCondi\\\" class=\\\"\\\" >");
					b.append("<div  style=\\\"clear:both\\\"/>");
				} else {// ������ʾ
					b.append("</li>");
					b.append("<li id=\\\"li_expCheckTypeCondi_display\\\" class=\\\"\\\" style=\\\"display:none;\\\">");
					b.append("<div  style=\\\"clear:both\\\"/>");
				}
				curRow++;
				isNewLine = true;
			} else {
				if (isNewLine) {// �µ�һ�У���Ҫ������ʾ

					b.append("<div class=\\\"expcondi_type_div\\\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div>");
					isNewLine = false;
				}
				if (StringUtils.isNotEmpty(curBillTypeCode)
						&& nav.getBillTypeCode().equals(curBillTypeCode)) {
					b.append(
							"<div id=\\\"check_billType_showDiv_"
									+ i
									+ "\\\" class=\\\"expcondi_div\\\" style=\\\"cursor:pointer;color:#FFFFFF;background-color:#028FD1;\\\" onclick=\\\"javascript:queryBillByType(this,'"
									+ nav.getBillTypeCode() + "','"
									+ nav.getDjdl() + "');\\\">").append(
							nav.getBillTypeName() + "&nbsp;"
									+ nav.getApproveCount() + "</div>");
				} else {
					if ("F1".equals(djdl)) {
						b.append(
								"<div id=\\\"check_billType_showDiv_"
										+ i
										+ "\\\" class=\\\"expcondi_div\\\" style=\\\"cursor:pointer;\\\" onclick=\\\"javascript:queryBillByType(this,'"
										+ nav.getBillTypeCode() + "','" + djdl
										+ "');\\\">").append(
								nav.getBillTypeName() + "&nbsp;"
										+ nav.getApproveCount() + "</div>");
					} else if ("36D1".equals(djdl)) {
						b.append(
								"<div id=\\\"check_billType_showDiv_"
										+ i
										+ "\\\" class=\\\"expcondi_div\\\" style=\\\"cursor:pointer;\\\" onclick=\\\"javascript:queryBillByType(this,'"
										+ nav.getBillTypeCode() + "','" + djdl
										+ "');\\\">").append(
								nav.getBillTypeName() + "&nbsp;"
										+ nav.getApproveCount() + "</div>");
					} else {
						b.append(
								"<div id=\\\"check_billType_showDiv_"
										+ i
										+ "\\\" class=\\\"expcondi_div\\\" style=\\\"cursor:pointer;\\\" onclick=\\\"javascript:queryBillByType(this,'"
										+ nav.getBillTypeCode() + "','"
										+ nav.getDjdl() + "');\\\">").append(
								nav.getBillTypeName() + "&nbsp;"
										+ nav.getApproveCount() + "</div>");
					}
				}
			}

		}
		b.append("</li>");
		b.append("@allApproveCount" + allApproveCount);
		return b.toString();

	}

	private static List<BillNavMenuItemVO> billTypeSortByCount(
			List<BillNavMenuItemVO> billNavMenuVos, boolean isHasCompletePortlet) {
		// ���浥�����ͱ����Ӧ��List�����е�����
		List<BillNavMenuItemVO> newBillNavMenuVos = new ArrayList<BillNavMenuItemVO>();
		try {
			Map<String, Integer> billTypeCodeToIndexMap = new HashMap<String, Integer>();
			Map<String, Integer> getSortBillType = new LinkedHashMap<String, Integer>();
			for (int i = 0; i < billNavMenuVos.size(); i++) {
				BillNavMenuItemVO nav = billNavMenuVos.get(i);
				if (StringUtils.isNotEmpty(nav.getFunurl())
						&& !"ALL".equals(nav.getFunurl())) {
					String billTypeCode = ExpenseApproveUtil
							.getBillTypeByFunURL(nav.getFunurl());
					billTypeCodeToIndexMap.put(billTypeCode, i);
				}
			}
			IExpBIllPortalService expBillQueryService = null;
			// if (expBillQueryService == null) {
			expBillQueryService = NCLocator.getInstance().lookup(
					IExpBIllPortalService.class);
			// }

			HashSet<String> tradeTypes = new HashSet<String>();
			tradeTypes.addAll(billTypeCodeToIndexMap.keySet());
			AppUtil.addAppAttr("YER#PERMTRADETYPES", tradeTypes);// �Ż���Ȩ�޵Ľ�������

			getSortBillType = expBillQueryService
					.getSortBillType(isHasCompletePortlet);
			if (getSortBillType.containsKey("4641")) {// zhangjxh+����ͻ��������뵥
				int billnum = getSortBillType.get("4641");
				getSortBillType.remove("4641");
				getSortBillType.put("4641-01", billnum);
			}
			if (getSortBillType.containsKey("4642")) {// zhangjxh+����������Ʒ���뵥
				int billnum = getSortBillType.get("4642");
				getSortBillType.remove("4642");
				getSortBillType.put("4642-01", billnum);
			}
			if (getSortBillType.containsKey("35")) {// zhangjxh+����ͻ����õ�
				int billnum = getSortBillType.get("35");
				getSortBillType.remove("35");
				getSortBillType.put("35-01", billnum);
			}

			Iterator<String> ite = getSortBillType.keySet().iterator();
			while (ite.hasNext()) {
				// ���ղ�ѯ��������˳����ӣ���ɾ����ӹ��õ�������
				String key = ite.next();
				if (billTypeCodeToIndexMap.get(key) != null) {
					BillNavMenuItemVO vo = billNavMenuVos
							.get(billTypeCodeToIndexMap.get(key));
					vo.setBillCount(getSortBillType.get(key));
					newBillNavMenuVos.add(vo);
					billTypeCodeToIndexMap.remove(key);
				}
			}

			// �����û�δ¼�ĵ�������û�в���������԰���Ĭ��˳�����
			/*
			 * Iterator<String> billTypeToIndexIte =
			 * billTypeCodeToIndexMap.keySet().iterator();
			 * while(billTypeToIndexIte.hasNext()) {
			 * newBillNavMenuVos.add(billNavMenuVos
			 * .get(billTypeCodeToIndexMap.get(billTypeToIndexIte.next()))); }
			 */
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);

		}
		return newBillNavMenuVos;
	}

	/**
	 * 
	 * @param itemVO
	 * @param menuListVO
	 * @param notLeafitemVO
	 * @param menuCodeMap
	 */
	public static void getMenuItemVO(MenuItemAdapterVO itemVO,
			List<BillNavMenuItemVO> menuListVO,
			MenuItemAdapterVO notLeafitemVO, Map<String, String> menuCodeMap) {
		// �������Ŀ¼�������������
		if (itemVO.getMenuitem().getIsnotleaf().booleanValue()) {
			List<MenuItemAdapterVO> menuItemAdapterVOList = itemVO
					.getChildrenMenu();
			if (menuItemAdapterVOList != null
					&& menuItemAdapterVOList.size() > 0) {
				for (int i = 0; i < menuItemAdapterVOList.size(); i++) {
					getMenuItemVO(menuItemAdapterVOList.get(i), menuListVO,
							itemVO, menuCodeMap);
				}
			}
		} else {
			// String billTypeCode = "";
			String djdl = "";
			// String[] funUrl = itemVO.getFunnode().getUrl().split("=");
			// if (funUrl.length == 2) {
			// billTypeCode = funUrl[1];
			// }
			// djdl = getDjdlByBillTypeCode(billTypeCode);
			// ���˵�����һ����Ŀ¼��ӵ�����
			addMenuItem(menuCodeMap, menuListVO, notLeafitemVO, notLeafitemVO,
					djdl);

			// ����ǰ�˵���ӵ�����
			addMenuItem(menuCodeMap, menuListVO, notLeafitemVO, itemVO, djdl);
		}
	}

	/**
	 * ������������Ӳ˵���������ظ��Ļ���˵�
	 * 
	 * @param menuCodeMap
	 * @param menuListVO
	 * @param notLeafitemVO
	 *            ��һ����Ŀ¼
	 * @param nowItemVO
	 *            ��ǰ���˵�
	 */
	public static void addMenuItem(Map<String, String> menuCodeMap,
			List<BillNavMenuItemVO> menuListVO,
			MenuItemAdapterVO notLeafitemVO, MenuItemAdapterVO nowItemVO,
			String djdl) {
		if (menuCodeMap.get(nowItemVO.getMenuitem().getCode()) == null) {
			BillNavMenuItemVO billNavMenuItemVO = new BillNavMenuItemVO();
			billNavMenuItemVO.setFuncode(notLeafitemVO.getMenuitem().getCode());// �ϼ��˵�����
			billNavMenuItemVO
					.setMenuitemcode(nowItemVO.getMenuitem().getCode());
			billNavMenuItemVO
					.setMenuitemname(nowItemVO.getMenuitem().getName());
			billNavMenuItemVO.setFunurl(nowItemVO.getFunnode().getUrl());
			menuCodeMap.put(nowItemVO.getMenuitem().getCode(), nowItemVO
					.getMenuitem().getCode());
			billNavMenuItemVO.setBilltype(djdl);

			billNavMenuItemVO.setOrdernum(notLeafitemVO.getMenuitem()
					.getOrdernum());

			menuListVO.add(billNavMenuItemVO);

		}
	}

	private static String getStringNullField(String fieldName) {
		if (StringUtils.isEmpty(fieldName)) {
			return "' '";
		}
		return fieldName;
	}

	public static String outGetStringNullField(String fieldName) {
		if (StringUtils.isEmpty(fieldName)) {
			return "' '";
		}
		return fieldName;
	}

	private static String getNumNullField(String fieldName) {
		if (StringUtils.isEmpty(fieldName)) {
			return "-100";
		}
		return fieldName;
	}

	private static String getCurrtype(String fieldName) {
		if (StringUtils.isEmpty(fieldName)) {
			return "-100";
		}
		return fieldName;
	}

	private static String getQeryFieldSql(String tableName,
			List<String> queryFidle, boolean isDetail) {
		StringBuffer querySql = new StringBuffer();

		if (queryFidle.get(0).indexOf("' '") == -1
				&& queryFidle.get(0).indexOf("-100") == -1) {
			querySql.append(tableName + "." + queryFidle.get(0));
		} else {
			querySql.append(queryFidle.get(0));
		}

		for (int i = 1; i < queryFidle.size(); i++) {
			if (queryFidle.get(i) == null) {// ����������뵥��Ч״̬����
				querySql.append(",").append(" -100 as sxbz");
			} else if (queryFidle.get(i).indexOf("' '") == -1
					&& queryFidle.get(i).indexOf("-100") == -1
					&& !queryFidle.get(i).startsWith("(")
					&& queryFidle.get(i).indexOf("1K1") == -1) {

				if (queryFidle.get(i).contains("case")) {
					querySql.append(",").append(queryFidle.get(i));
					continue;
				} else if (queryFidle.get(i).contains("%4641%")) {
					querySql.append(",").append(
							queryFidle.get(i).replace("%4641%", "'4641'"));
					continue;
				} else if (queryFidle.get(i).contains("%4642%")) {
					querySql.append(",").append(
							queryFidle.get(i).replace("%4642%", "'4642'"));
					continue;
				} else if (queryFidle.get(i).contains("%35%")) { // zhangjxh1120
					querySql.append(",").append(
							queryFidle.get(i).replace("%35%", "'35'"));
					continue;
				}

				querySql.append(",")
						.append(tableName + "." + queryFidle.get(i));
			} else {
				if (queryFidle.get(i).startsWith("(")) {
					querySql.append(",")
							.append(queryFidle.get(i).replace("%tablename%",
									tableName));
				} else {
					querySql.append(",").append(queryFidle.get(i));
				}
			}
		}

		return querySql.toString();
	}

	/*
	 * author: houmeng3 Date:2015.10.15 �������䱨�����Ż� begin
	 */

	// private static List<IExpPortal> getAllExpPortalClass() {
	//
	// return ExpPortalFactory.getInstance().getAllExpPortalMap();
	// }
	private static Map<String, IExpPortal> getAllExpPortalClass() {

		return ExpPortalFactory.getInstance().getAllExpPortalMap();
	}

	/*
	 * houmeng3
	 */

	public static List<SuperVO> dealMutiLang(List<SuperVO> vo) {
		if (vo == null || vo.size() == 0) {
			return vo;
		}

		for (int billIndex = 0; billIndex < vo.size(); billIndex++) {
			YerExpBillVo bill = (YerExpBillVo) vo.get(billIndex);
			String billType = bill.getBilltype();

			// ����״̬���ﴦ��
			String strDJZT = "";
			String defitem1 = bill.getDefitem1() == null ? "" : bill
					.getDefitem1();
			if (defitem1.equals("����")) {
				strDJZT = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("ersmart", "1yerfreepr0043")/* @res "����" */;
			} else if (defitem1.equals("�ݴ�")) {
				strDJZT = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("ersmart", "1yerfreepr0042")/* @res "�ݴ�" */;
			} else if (defitem1.equals("����")) {
				strDJZT = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("ersmart", "1yerfreepr0031")/* @res "����" */;
			} else if (defitem1.equals("������")) {
				strDJZT = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("ersmart", "1yerfreepr0035")/* @res "������" */;
			} else if (defitem1.equals("���")) {
				strDJZT = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("ersmart", "1yerfreepr0032")/* @res "���" */;
			} else if (defitem1.equals("���")) {
				strDJZT = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("ersmart", "1yerfreepr0032")/* @res "���" */;
			} else if (defitem1.equals("ǩ��")) {
				strDJZT = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("ersmart", "1yerfreepr0033")/* @res "ǩ��" */;
			} else if (defitem1.equals("����ͨ��")) {
				strDJZT = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("yer", "yer_enum_checkedok")/* @res "����ͨ��" */;
			} else if (defitem1.equals("������")) {
				strDJZT = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("yer", "yer_enum_checking")/* @res "������" */;
			} else if (defitem1.equals("δȷ��")) {
				strDJZT = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("yer", "yer_enum_unConfirm")/* @res "δȷ��" */;
			} else if (defitem1.equals("����̬")) {
				strDJZT = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("yer", "yer_enum_free")/* @res "����̬" */;
			} else if (defitem1.equals("���ύ")) {
				strDJZT = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("yer", "yer_enum_unSubmit")/* @res "���ύ" */;
			} else if (defitem1.equals("������")) {
				strDJZT = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("expensepub_2", "yerexpensepub-0041")/*
																		 * @res
																		 * "������"
																		 */;
			} else if (defitem1.equals("������")) {
				strDJZT = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("yer", "yer_enum_unCreate")/* @res "������" */;
			} else if (defitem1.equals("��������")) {
				strDJZT = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("yer", "yer_enum_someCreate")/* @res "��������" */;
			} else if (defitem1.equals("�����")) {
				strDJZT = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("expensepub_2", "yerexpensepub-0022")/*
																		 * @res
																		 * "�����"
																		 */;
			} else if (defitem1.equals("������")) {
				strDJZT = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("yer", "yer_enum_hasCreate")/* @res "������" */;
			} else if (defitem1.equals("������Ч")) {
				strDJZT = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("yer", "yerotherpub-0001")/*
															 * @res "������Ч"
															 */;
			} else if (defitem1.equals("����̬")) {
				strDJZT = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("yer", "yerotherpub-0002")/*
															 * @res "����̬"
															 */;
			} else if (defitem1.equals("������ͨ��")) {
				strDJZT = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("yer", "yerotherpub-0003")/*
															 * @res "������ͨ��"
															 */;
			} else if (defitem1.equals("�ر�")) {
				strDJZT = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("yer", "yerotherpub-0004")/*
															 * @res "�ر�"
															 */;
			}

			if (!"".equals(defitem1)) {
				bill.setDefitem1(strDJZT);
			}

			// ����״̬���ﴦ��
			String strSPZT = "";
			String defitem2 = bill.getDefitem2();
			if (defitem2 != null) {
				if (defitem2.equals("��ʼ̬")) {
					strSPZT = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
							.getStrByID("yer", "yer_enum_init")/* @res "��ʼ̬" */;
				} else if (defitem2.equals("�ύ̬")) {
					strSPZT = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
							.getStrByID("yer", "yer_enum_submit")/* @res "�ύ̬" */;
				} else if (defitem2.equals("������")) {
					strSPZT = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
							.getStrByID("yer", "yer_enum_checking")/*
																	 * @res
																	 * "������"
																	 */;
				} else if (defitem2.equals("����ͨ��")) {
					strSPZT = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
							.getStrByID("yer", "yer_enum_checkedok")/*
																	 * @res
																	 * "����ͨ��"
																	 */;
				} else if (defitem2.equals("������ͨ��")) {
					strSPZT = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
							.getStrByID("yer", "yer_enum_unchecked")/*
																	 * @res
																	 * "������ͨ��"
																	 */;
				}
			}
			if (!"".equals(strSPZT)) {
				bill.setDefitem2(strSPZT);
			}
		}
		return vo;
	}
	
	/**
	 * @param fieldName
	 * @return
	 * ��Ʊ����ǰ����ʾת��
	 * chenlun	�޸�	����755�����ӱ������Ż����������Ż���ѯ�����ʾ�ֶ�		2017/09/13
	 */
	public static String getNoinvoice(String fieldName) {
		if (fieldName==null || fieldName.trim().length()==0) {
			return null;
		}
		if("Y".equals(fieldName)){
			return "��";
		}else{
			return "��";
		}
	}
}
