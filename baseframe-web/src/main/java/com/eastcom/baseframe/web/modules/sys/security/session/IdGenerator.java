package com.eastcom.baseframe.web.modules.sys.security.session;

import java.io.Serializable;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;

import com.eastcom.baseframe.common.utils.IdGen;

public class IdGenerator implements SessionIdGenerator {

	@Override
	public Serializable generateId(Session session) {
		return IdGen.uuid();
	}

}
