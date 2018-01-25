package nc.itf.carinfo.web;

import nc.ssc.fiweb.pub.RequestParam;

public interface ICarInfoWebRequestService {

	/**车辆信息维护单据保存
	 * @param request
	 * @return
	 * @throws Exception
	 * @author 钟泽辉
	 */
	public String sendBillSave(RequestParam request) throws Exception;

	/**车辆信息维护单据提交
	 * @param request
	 * @return
	 * @throws Exception
	 * @author 钟泽辉
	 */
	public String commit(RequestParam request) throws Exception;

	/**车辆信息维护收回
	 * @param request
	 * @return
	 * @throws Exception
	 * @author 钟泽辉
	 */
	public String recall(RequestParam request) throws Exception;
	
	/**车辆信息维护更新
	 * @param request
	 * @return
	 * @throws Exception
	 * @author 钟泽辉
	 */
	public String update(RequestParam request) throws Exception;
	

	

}