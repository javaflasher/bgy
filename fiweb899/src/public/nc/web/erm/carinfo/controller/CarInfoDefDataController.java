package nc.web.erm.carinfo.controller;

import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;

import org.springframework.stereotype.Component;

import uap.iweb.event.EventResponse;
import uap.iweb.icontext.IWebViewContext;
import uap.json.JSONObject;



/**
 * @author ÷”‘Ûª‘
 * @date 2018-1-19
 * @π¶ƒ‹√Ë ˆ 
 */
@Component("CarInfoDefDataController")
public class CarInfoDefDataController {
	
	public void loadData() throws Exception {
		Map<String, String> clientAttrs = IWebViewContext.getRequest()
				.getEnvironment().getClientAttributes();
		String pk_group = InvocationInfoProxy.getInstance().getGroupId();
		String pk_user = InvocationInfoProxy.getInstance().getUserId();
		String uistate = IWebViewContext.getEventParameter("uistate");
		
		EventResponse respose = IWebViewContext.getResponse();
		JSONObject json = new JSONObject();
		
	}

}
