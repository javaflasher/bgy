package nc.itf.erm;
import nc.vo.erm.carinfo.AggCarInfoVO;
import nc.vo.erm.carinfo.CarInfoBodyVO;
import nc.vo.erm.carinfo.CarInfoVO;
import nc.vo.pub.BusinessException;

/**
 * @author �����
 * @date 2018-1-3
 * @�������� ������Ϣά������
 */
public interface ICarInfoSyncService {
	/**ͬ�����Զ��嵵��ά��-ҵ��Ԫ����������ϢF057���ӿ�
	 * @param carInfo
	 * @return	
	 * @author �����
	 */
	public String syncCarlnfo(CarInfoVO carInfo) throws BusinessException; 
	
	/**ͬ�� �������޶��������ơ��ӿ�
	 * @param carLimit
	 * @return
	 * @author �����
	 */
	public String syncCarlimit(AggCarInfoVO aggVO) throws BusinessException ;
	
}
