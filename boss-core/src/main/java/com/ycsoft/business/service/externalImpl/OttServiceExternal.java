package com.ycsoft.business.service.externalImpl;

import org.springframework.stereotype.Service;
import com.ycsoft.business.service.impl.OttExternalService;

@Service
public class OttServiceExternal extends ParentService implements IOttServiceExternal {

	@Override
	public void saveSyncProd() throws Exception {
		OttExternalService ottService = (OttExternalService) getBean(OttExternalService.class, null);
		ottService.saveSyncProd();
	}

}
