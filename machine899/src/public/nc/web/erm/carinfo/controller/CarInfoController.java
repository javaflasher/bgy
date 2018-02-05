package nc.web.erm.carinfo.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import uap.iweb.action.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.carinfo.web.ICarInfoWebRequestService;
import nc.ssc.fiweb.pub.RequestParam;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * @author 钟泽辉
 * @date 2018-1-19
 * @功能描述 
 */
@SuppressWarnings("restriction")
@Controller
@RequestMapping("/carinfo_ctrl")
public class CarInfoController extends BaseController {
	
	@SuppressWarnings("restriction")
	@RequestMapping(value = "/viewBill", method = RequestMethod.GET)
	@ResponseBody
	public void viewBill(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		ICarInfoWebRequestService  service = NCLocator.getInstance().lookup(ICarInfoWebRequestService.class);
		String jsonBill = null;
		try {
			jsonBill = service.view(new RequestParam(request));
			if(jsonBill!=null){
				response.setContentType("text/html");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonBill);
			}else{
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("success", "false");
				jsonObj.put("message", "查不到单据");
				response.getWriter().write(jsonObj.toString());
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("success", "false");
			jsonObj.put("message", e.getMessage());
			response.getWriter().write(jsonObj.toString());
		}
		String test = "breakpoint";
	} 	
	
	
	@SuppressWarnings("restriction")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public void saveBill(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		ICarInfoWebRequestService  service = NCLocator.getInstance().lookup(ICarInfoWebRequestService.class);
		RequestParam requestParam = new RequestParam(request);
		String billid =  requestParam.getParameter("openbillid");
		try {
			String jsonBill = null;
			if(billid==null){
				jsonBill = service.sendBillSave(new RequestParam(request));
			}else{
				jsonBill = service.update(new RequestParam(request)); 
			}
			response.setContentType("text/html");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonBill);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("success", "false");
			jsonObj.put("message", e.getMessage());
			response.getWriter().write(jsonObj.toString());
			
		} finally {
			try {
				response.flushBuffer();	//立即输出
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String test = "breakpoint";
		Logger.debug(test);
	}

	
	@SuppressWarnings("restriction")
	@RequestMapping(value = "/submit", method = RequestMethod.GET)
	@ResponseBody
	public void sendBill2Approver(HttpServletRequest request, HttpServletResponse response) throws Exception{
		ICarInfoWebRequestService  service = NCLocator.getInstance().lookup(ICarInfoWebRequestService.class);
		try {
			String jsonString  = service.commit(new RequestParam(request));
			response.setContentType("text/html");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonString);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("success", "false");
			jsonObj.put("message", e.getMessage());
			response.getWriter().write(jsonObj.toString());
		}
		String test = "breakpoint";
	}
	
	@SuppressWarnings("restriction")
	@RequestMapping(value = "/recall", method = RequestMethod.GET)
	@ResponseBody
	public void recallBill(HttpServletRequest request, HttpServletResponse response){
		ICarInfoWebRequestService  service = NCLocator.getInstance().lookup(ICarInfoWebRequestService.class);
		
		String test = "breakpoint";
		
	}
	
	@SuppressWarnings("restriction")
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	@ResponseBody
	public void edit(HttpServletRequest request, HttpServletResponse response) throws Exception{
		ICarInfoWebRequestService  service = NCLocator.getInstance().lookup(ICarInfoWebRequestService.class);
		String jsonString  = service.edit(new RequestParam(request));
		
		String test = "breakpoint";
		
	}
	

}
