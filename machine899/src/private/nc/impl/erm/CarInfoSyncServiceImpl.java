package nc.impl.erm;

import nc.bs.dao.BaseDAO;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.bs.framework.common.NCLocator;
import nc.itf.erm.ICarInfoSyncService;
import nc.itf.erm.ICarcamlimitMaintain;
import nc.itf.erm.constants.CarInforConstants;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.erm.carcamlimit.Aggcar_limit;
import nc.vo.erm.carcamlimit.Car_limitBVO;
import nc.vo.erm.carcamlimit.Car_limitHVO;
import nc.vo.erm.carinfo.AggCarInfoVO;
import nc.vo.erm.carinfo.CarInfoBodyVO;
import nc.vo.erm.carinfo.CarInfoVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
/**
 * @author 钟泽辉
 * @date 2018-1-3
 * @功能描述  同步信息接口实现类
 */
public class CarInfoSyncServiceImpl implements ICarInfoSyncService {
	private BaseDAO baseDao;

	@Override
	public String syncCarlnfo(CarInfoVO carInfo) throws BusinessException {
		/*pk_org	pk_org
		 *code		carcode
		 *shortname  “报销类型”为“实报实销”时，固定值“实报实销”
		 *pid		pk_pcar
		 *modifier	NC系统
		 *modifiedtime	终审时间
		 *
		 */
		StringBuffer syncSql = new StringBuffer();
		String pk_car = carInfo.getPk_car99();	
		String pk_org = carInfo.getPk_org();
		int opertype = carInfo.getOpertype();

		nc.itf.bd.defdoc.IDefdocService defdocService = NCLocator.getInstance().lookup(nc.itf.bd.defdoc.IDefdocService.class);
				//新增、修改、
				// 新增直接new对象新增
				//同步，查询出来再修改
		if(StringUtil.isEmpty(pk_car) && opertype == CarInforConstants.opertype_add){//判断是是否为新增
			String pk_defdoclist =null;
			String querySql = "select pk_defdoclist from bd_defdoclist where name='车辆信息';";
			
			pk_defdoclist = (String) getBaseDao().executeQuery(querySql, new ColumnProcessor(1));
			
			//新增
			DefdocVO defVO = new DefdocVO();
			defVO.setPk_group("0001H210000000000IGL");
			defVO.setPk_org(carInfo.getPk_org());
			defVO.setCode(carInfo.getCarcode()); 	//编码
			defVO.setName(carInfo.getCarname());	//车名
			defVO.setPid(carInfo.getPk_pcar88());	//上级档案
			defVO.setModifier("NC_USER0000000000000");	//NC系统用户
			defVO.setModifiedtime(carInfo.getApprovedate());		//修改时间
			defVO.setEnablestate(CarInforConstants.opertype_add);	//设置启用状态   
			defVO.setCreator(carInfo.getCreator());
			defVO.setPk_defdoclist(pk_defdoclist);
			defVO.setDatatype(1);	//新增状态为1 （未启用，自定义档案节点看不到）
			if(CarInforConstants.bxlx_sbsx==carInfo.getExptype()){
				defVO.setShortname("实报实销");
			}		
			defVO.setCreationtime(carInfo.getCreationtime());
			DefdocVO[] defvo = new DefdocVO[1];
			defvo[0]=defVO;
			try{
				defvo = defdocService.insertDefdocs(pk_org, defvo );
				pk_car = defvo[0].getPk_defdoc();
			}catch(BusinessException  e){
				e.toString();
			}
			
			if(pk_car!=null){
				String reWriteSql = "update er_carinfo set pk_car99='"+pk_car+"' where pk_carinfo='"+carInfo.getPk_carinfo()+"';";
				
				baseDao.executeUpdate(reWriteSql);
				
				return pk_car;
			}else {
				return null;
			}
			
		}else{
			//同步
			DefdocVO defVO = new DefdocVO();
			defVO = (DefdocVO) getBaseDao().retrieveByPK(DefdocVO.class, pk_car);
			defVO.setCode(carInfo.getCarcode());
			defVO.setPid(carInfo.getPk_pcar88());
			defVO.setModifier("NC_USER0000000000000");	//NC系统用户
			defVO.setModifiedtime(carInfo.getApprovedate());
			if(CarInforConstants.opertype_disable==carInfo.getOpertype()){
				defVO.setEnablestate(3);//停用状态为3
			}else if(CarInforConstants.opertype_enable==carInfo.getOpertype()){
				defVO.setEnablestate(2);//启用状态为2
			}
			DefdocVO[] defvo = new DefdocVO[1];
			
			defvo[0]=defVO;
			
			defdocService.updateDefdocs(pk_org, defvo);
			
			return pk_car;
		}
			
		
	}

	@Override
	public String syncCarlimit(AggCarInfoVO aggVO) throws BusinessException {
		/*pk_org	pk_org
		 *vbilldate billdate
		 *car		pk_car
		 *carno		carcode
		 *kjni		accyear
		 *年限额		annuallimit
		 *xe		monthlylimit
		 *modifier	NC系统
		 *modifiedtime	终审时间
		 */ 
		
		CarInfoVO carHeadVO = aggVO.getParentVO();
		CarInfoBodyVO[] carBodyVOs = (CarInfoBodyVO[]) aggVO.getChildrenVO();
		
		String pk_car = carHeadVO.getPk_car99();	//车辆pk
		String pk_org = carHeadVO.getPk_org();		//财务组织
		int opertype = carHeadVO.getOpertype();	//操作类型
		String pk_car_limit = carHeadVO.getPk_car_limit();
		
		ICarcamlimitMaintain limitService  = NCLocator.getInstance().lookup(ICarcamlimitMaintain.class);
		
		if(StringUtil.isEmpty(carHeadVO.getPk_car_limit())){//新增or同步
			
			Aggcar_limit[] aggLimit = new Aggcar_limit[1];
			Car_limitBVO[] limitBVOs = new  Car_limitBVO[12];
			Car_limitBVO limitBVO=null;
			Car_limitHVO limitHVO = new Car_limitHVO();
			limitHVO.setCarname(carHeadVO.getCarname());	//车辆名臣
			limitHVO.setCar(carHeadVO.getPk_car99());		//车辆
			limitHVO.setCarno(carHeadVO.getCarcode());		//编号
			limitHVO.setPk_group(carHeadVO.getPk_group());
			limitHVO.setPk_org(carHeadVO.getPk_org());
			limitHVO.setVbilldate(new UFDate(carHeadVO.getApprovedate().toString()));	//制单日期是终审时间
			limitHVO.setCreator("NC_USER0000000000000");		//NC系统 用户
			limitHVO.setCreationtime(new UFDateTime());			//制单日期
			limitHVO.setKjni(carHeadVO.getAccyear66());			//会计年
			limitHVO.set年限量(new UFDouble(0));		
			limitHVO.set年限额(carHeadVO.getAnnuallimit());
			limitHVO.setModifiedtime(carHeadVO.getApprovedate());
			limitHVO.setBillmaker("NC_USER0000000000000");	//NC系统

			for(int x=0;x<12;x++){
				CarInfoBodyVO bodyVO = carBodyVOs[x];	//车辆信息表体
				limitBVO = new Car_limitBVO();	
				limitBVO.setKjy(Integer.toString(x+1));	//会计月
				limitBVO.setCrowno(Integer.toString((x+1)*10));	//行号
				limitBVO.setDef4(new UFBoolean("N"));		//调试车辆限额bp，不知为何自动设置为N
				limitBVO.setDef5(new UFBoolean("N"));		//调试车辆限额bp，不知为何自动设置为N
				limitBVO.setXe(bodyVO.getMonthlylimit());	//限额
				limitBVO.setXl(new UFDouble(0));				//限量
				limitBVOs[x] = limitBVO;
			}
			Aggcar_limit vo = new Aggcar_limit();
			vo.setParent(limitHVO);
			vo.setChildrenVO(limitBVOs);
			aggLimit[0] = vo;
			aggLimit = limitService.insert(aggLimit);		//新增车辆信息单据；
			pk_car_limit = aggLimit[0].getParentVO().getPrimaryKey();
			
			String updateCarinfoSql = "update er_carinfo set pk_car_limit='"+pk_car_limit+"' where pk_carinfo='"+carHeadVO.getPk_carinfo()+"' and dr=0 ";
			
			baseDao.executeUpdate(updateCarinfoSql);
			
			
		}else{
			//同步修改【车辆限额限量控制】信息，用sql
			StringBuffer updateSql = new StringBuffer();
			
			updateSql.append("update erm_car_limit set ").append("pk_org='").append(pk_org).append("', ")
			.append("car='").append(carHeadVO.getPk_car99()).append("', ")
			.append("carno='").append(carHeadVO.getCarcode()).append("', ")
			.append("年限额='").append(carHeadVO.getAnnuallimit()).append("', ")
			.append("kjni='").append(carHeadVO.getAccyear66()).append("', ")
			.append("modifier='").append("NC_USER0000000000000").append("', ")
			.append("modifiedtime='").append(carHeadVO.getApprovedate()).append("'  ")
			.append( " where pk_car_limit='").append(pk_car_limit).append("';");
			int num = baseDao.executeUpdate(updateSql.toString());
			
			for(int x=0;x<12 ;x++ ){
				
				String updateBodySql ="update erm_car_limit_b set limit_e='"+carBodyVOs[x].getMonthlylimit()+"' where pk_car_limit='"+carHeadVO.getPk_car_limit()+"' and kjy='"+(x+1)+"' and dr=0;";
				int number = baseDao.executeUpdate(updateBodySql);
				
			}	
		}
		
		
		return pk_car_limit;
	}
	
	private  BaseDAO getBaseDao(){
		if(baseDao != null){
			return baseDao;
		}else{
			 baseDao = new BaseDAO(); 
			return baseDao;
		}
	}
}
