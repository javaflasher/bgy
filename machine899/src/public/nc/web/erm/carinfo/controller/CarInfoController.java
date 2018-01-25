package nc.web.erm.carinfo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import uap.iweb.action.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/help_ctr")
public class CarInfoController extends BaseController {
	
	@SuppressWarnings("restriction")
	@RequestMapping(value = "/getPdfPath", method = RequestMethod.GET)
	@ResponseBody
	public void save(HttpServletRequest request, HttpServletResponse response){
		String test = "breakpoint";
	} 
}
