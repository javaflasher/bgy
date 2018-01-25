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
 * @author �����
 * @date 2018-1-3
 * @��������  ͬ����Ϣ�ӿ�ʵ����
 */
public class CarInfoSyncServiceImpl implements ICarInfoSyncService {
	private BaseDAO baseDao;

	@Override
	public String syncCarlnfo(CarInfoVO carInfo) throws BusinessException {
		/*pk_org	pk_org
		 *code		carcode
		 *shortname  ���������͡�Ϊ��ʵ��ʵ����ʱ���̶�ֵ��ʵ��ʵ����
		 *pid		pk_pcar
		 *modifier	NCϵͳ
		 *modifiedtime	����ʱ��
		 *
		 */
		StringBuffer syncSql = new StringBuffer();
		String pk_car = carInfo.getPk_car99();	
		String pk_org = carInfo.getPk_org();
		int opertype = carInfo.getOpertype();

		nc.itf.bd.defdoc.IDefdocService defdocService = NCLocator.getInstance().lookup(nc.itf.bd.defdoc.IDefdocService.class);
				//�������޸ġ�
				// ����ֱ��new��������
				//ͬ������ѯ�������޸�
		if(StringUtil.isEmpty(pk_car) && opertype == CarInforConstants.opertype_add){//�ж����Ƿ�Ϊ����
			String pk_defdoclist =null;
			String querySql = "select pk_defdoclist from bd_defdoclist where name='������Ϣ';";
			
			pk_defdoclist = (String) getBaseDao().executeQuery(querySql, new ColumnProcessor(1));
			
			//����
			DefdocVO defVO = new DefdocVO();
			defVO.setPk_group("0001H210000000000IGL");
			defVO.setPk_org(carInfo.getPk_org());
			defVO.setCode(carInfo.getCarcode()); 	//����
			defVO.setName(carInfo.getCarname());	//����
			defVO.setPid(carInfo.getPk_pcar88());	//�ϼ�����
			defVO.setModifier("NC_USER0000000000000");	//NCϵͳ�û�
			defVO.setModifiedtime(carInfo.getApprovedate());		//�޸�ʱ��
			defVO.setEnablestate(CarInforConstants.opertype_add);	//��������״̬   
			defVO.setCreator(carInfo.getCreator());
			defVO.setPk_defdoclist(pk_defdoclist);
			defVO.setDatatype(1);	//����״̬Ϊ1 ��δ���ã��Զ��嵵���ڵ㿴������
			if(CarInforConstants.bxlx_sbsx==carInfo.getExptype()){
				defVO.setShortname("ʵ��ʵ��");
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
			//ͬ��
			DefdocVO defVO = new DefdocVO();
			defVO = (DefdocVO) getBaseDao().retrieveByPK(DefdocVO.class, pk_car);
			defVO.setCode(carInfo.getCarcode());
			defVO.setPid(carInfo.getPk_pcar88());
			defVO.setModifier("NC_USER0000000000000");	//NCϵͳ�û�
			defVO.setModifiedtime(carInfo.getApprovedate());
			if(CarInforConstants.opertype_disable==carInfo.getOpertype()){
				defVO.setEnablestate(3);//ͣ��״̬Ϊ3
			}else if(CarInforConstants.opertype_enable==carInfo.getOpertype()){
				defVO.setEnablestate(2);//����״̬Ϊ2
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
		 *���޶�		annuallimit
		 *xe		monthlylimit
		 *modifier	NCϵͳ
		 *modifiedtime	����ʱ��
		 */ 
		
		CarInfoVO carHeadVO = aggVO.getParentVO();
		CarInfoBodyVO[] carBodyVOs = (CarInfoBodyVO[]) aggVO.getChildrenVO();
		
		String pk_car = carHeadVO.getPk_car99();	//����pk
		String pk_org = carHeadVO.getPk_org();		//������֯
		int opertype = carHeadVO.getOpertype();	//��������
		String pk_car_limit = carHeadVO.getPk_car_limit();
		
		ICarcamlimitMaintain limitService  = NCLocator.getInstance().lookup(ICarcamlimitMaintain.class);
		
		if(StringUtil.isEmpty(carHeadVO.getPk_car_limit())){//����orͬ��
			
			Aggcar_limit[] aggLimit = new Aggcar_limit[1];
			Car_limitBVO[] limitBVOs = new  Car_limitBVO[12];
			Car_limitBVO limitBVO=null;
			Car_limitHVO limitHVO = new Car_limitHVO();
			limitHVO.setCarname(carHeadVO.getCarname());	//��������
			limitHVO.setCar(carHeadVO.getPk_car99());		//����
			limitHVO.setCarno(carHeadVO.getCarcode());		//���
			limitHVO.setPk_group(carHeadVO.getPk_group());
			limitHVO.setPk_org(carHeadVO.getPk_org());
			limitHVO.setVbilldate(new UFDate(carHeadVO.getApprovedate().toString()));	//�Ƶ�����������ʱ��
			limitHVO.setCreator("NC_USER0000000000000");		//NCϵͳ �û�
			limitHVO.setCreationtime(new UFDateTime());			//�Ƶ�����
			limitHVO.setKjni(carHeadVO.getAccyear66());			//�����
			limitHVO.set������(new UFDouble(0));		
			limitHVO.set���޶�(carHeadVO.getAnnuallimit());
			limitHVO.setModifiedtime(carHeadVO.getApprovedate());
			limitHVO.setBillmaker("NC_USER0000000000000");	//NCϵͳ

			for(int x=0;x<12;x++){
				CarInfoBodyVO bodyVO = carBodyVOs[x];	//������Ϣ����
				limitBVO = new Car_limitBVO();	
				limitBVO.setKjy(Integer.toString(x+1));	//�����
				limitBVO.setCrowno(Integer.toString((x+1)*10));	//�к�
				limitBVO.setDef4(new UFBoolean("N"));		//���Գ����޶�bp����֪Ϊ���Զ�����ΪN
				limitBVO.setDef5(new UFBoolean("N"));		//���Գ����޶�bp����֪Ϊ���Զ�����ΪN
				limitBVO.setXe(bodyVO.getMonthlylimit());	//�޶�
				limitBVO.setXl(new UFDouble(0));				//����
				limitBVOs[x] = limitBVO;
			}
			Aggcar_limit vo = new Aggcar_limit();
			vo.setParent(limitHVO);
			vo.setChildrenVO(limitBVOs);
			aggLimit[0] = vo;
			aggLimit = limitService.insert(aggLimit);		//����������Ϣ���ݣ�
			pk_car_limit = aggLimit[0].getParentVO().getPrimaryKey();
			
			String updateCarinfoSql = "update er_carinfo set pk_car_limit='"+pk_car_limit+"' where pk_carinfo='"+carHeadVO.getPk_carinfo()+"' and dr=0 ";
			
			baseDao.executeUpdate(updateCarinfoSql);
			
			
		}else{
			//ͬ���޸ġ������޶��������ơ���Ϣ����sql
			StringBuffer updateSql = new StringBuffer();
			
			updateSql.append("update erm_car_limit set ").append("pk_org='").append(pk_org).append("', ")
			.append("car='").append(carHeadVO.getPk_car99()).append("', ")
			.append("carno='").append(carHeadVO.getCarcode()).append("', ")
			.append("���޶�='").append(carHeadVO.getAnnuallimit()).append("', ")
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
