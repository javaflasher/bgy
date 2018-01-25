package nc.web.erm.carinfo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import uap.iweb.action.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import nc.bs.framework.common.NCLocator;
import nc.itf.carinfo.web.ICarInfoWebRequestService;

/**
 * @author ÷”‘Ûª‘
 * @date 2018-1-19
 * @π¶ƒ‹√Ë ˆ 
 */
@SuppressWarnings("restriction")
@Controller
@RequestMapping("/carinfo_ctrl")
public class CarInfoController extends BaseController {
	
	@SuppressWarnings("restriction")
	@RequestMapping(value = "/viewBill", method = RequestMethod.GET)
	@ResponseBody
	public void viewBill(HttpServletRequest request, HttpServletResponse response){
		ICarInfoWebRequestService  service = NCLocator.getInstance().lookup(ICarInfoWebRequestService.class);
		
		String test = "breakpoint";
	} 	
	@SuppressWarnings("restriction")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public void saveBill(HttpServletRequest request, HttpServletResponse response){
		ICarInfoWebRequestService  service = NCLocator.getInstance().lookup(ICarInfoWebRequestService.class);
		
		String test = "breakpoint";
	}

	
	@SuppressWarnings("restriction")
	@RequestMapping(value = "/submit", method = RequestMethod.GET)
	@ResponseBody
	public void sendBill2Approver(HttpServletRequest request, HttpServletResponse response){
		ICarInfoWebRequestService  service = NCLocator.getInstance().lookup(ICarInfoWebRequestService.class);
		
		String test = "breakpoint";
	}
	
	@SuppressWarnings("restriction")
	@RequestMapping(value = "/recall", method = RequestMethod.GET)
	@ResponseBody
	public void recallBill(HttpServletRequest request, HttpServletResponse response){
		ICarInfoWebRequestService  service = NCLocator.getInstance().lookup(ICarInfoWebRequestService.class);
		
		String test = "breakpoint";
		
	}

}
