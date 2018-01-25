package nc.itf.erm;
import nc.vo.erm.carinfo.AggCarInfoVO;
import nc.vo.erm.carinfo.CarInfoBodyVO;
import nc.vo.erm.carinfo.CarInfoVO;
import nc.vo.pub.BusinessException;

/**
 * @author 钟泽辉
 * @date 2018-1-3
 * @功能描述 车辆信息维护功能
 */
public interface ICarInfoSyncService {
	/**同步【自定义档案维护-业务单元】（车辆信息F057）接口
	 * @param carInfo
	 * @return	
	 * @author 钟泽辉
	 */
	public String syncCarlnfo(CarInfoVO carInfo) throws BusinessException; 
	
	/**同步 【车辆限额限量控制】接口
	 * @param carLimit
	 * @return
	 * @author 钟泽辉
	 */
	public String syncCarlimit(AggCarInfoVO aggVO) throws BusinessException ;
	
}
