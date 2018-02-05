package nc.itf.carinfo.web;

import nc.ssc.fiweb.pub.RequestParam;

public interface ICarInfoWebRequestService {

	/**������Ϣά�����ݱ���
	 * @param request
	 * @return
	 * @throws Exception
	 * @author �����
	 */
	public String sendBillSave(RequestParam request) throws Exception;

	/**������Ϣά�������ύ
	 * @param request
	 * @return
	 * @throws Exception
	 * @author �����
	 */
	public String commit(RequestParam request) throws Exception;

	/**������Ϣά���ջ�
	 * @param request
	 * @return
	 * @throws Exception
	 * @author �����
	 */
	public String recall(RequestParam request) throws Exception;
	
	/**������Ϣά������
	 * @param request
	 * @return
	 * @throws Exception
	 * @author �����
	 */
	public String update(RequestParam request) throws Exception;
	
	/**������Ϣά�������Ϣ
	 * @param request
	 * @return
	 * @throws Exception
	 * @author �����
	 */
	public String view(RequestParam request) throws Exception;
	
	/**������Ϣά���༭
	 * @param request
	 * @return
	 * @throws Exception
	 * 
	 */
	public String edit(RequestParam request) throws Exception;
	

}