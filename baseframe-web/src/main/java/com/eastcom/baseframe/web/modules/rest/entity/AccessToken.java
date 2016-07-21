package com.eastcom.baseframe.web.modules.rest.entity;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * access token
 * 
 * @author wutingguang <br>
 */
public class AccessToken {

	/**
	 * 默认access_token过期时间
	 */
	public static final int EXPIRES_IN = 7200;
	
	/**
	 * appId -- access_token -- expires
	 */
	private static Map<String, String> appIdMap = Maps.newHashMap();
	
	/**
	 * access_token -- expires
	 */
	private static Map<String, Integer> tokenMap = Maps.newHashMap();

	/**
	 * 判断token是否可用
	 * 
	 * @param token
	 * @return
	 */
	public static boolean isAlive(String token) {
		if (tokenMap.get(token) != null && (Integer) tokenMap.get(token) > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 根据动态生成的token得到其对应的appId信息
	 * 
	 * @param token
	 * @return
	 */
	public static String getAppId(String token){
		for (Map.Entry<String, String> entry : appIdMap.entrySet()) {
			if (entry.getValue().equals(token)) {
				return entry.getKey();
			}
		} 
		return null;
	}
	
	/**
	 * 保存token
	 * 保证旧的access_token在获取新的access_token时候还能使用
	 * 新旧access_token会共存一段时间，保证业务平滑过渡
	 * 
	 * @param token
	 * @param expires
	 * @return
	 */
	public synchronized static void put(String appId,String token) {
		if (appIdMap.get(appId) != null) {
			tokenMap.put(appIdMap.get(appId),10);
		}
		appIdMap.put(appId, token);
		tokenMap.put(token, EXPIRES_IN);
	}
	
	/**
	 * 刷新token
	 */
	public synchronized static void refresh() {
		List<String> delKeys = Lists.newArrayList();
		for (Map.Entry<String, Integer> entry : tokenMap.entrySet()) {
			Integer value = entry.getValue() - 1;
			tokenMap.put(entry.getKey(), value);
			if( value <= 0){
				delKeys.add(entry.getKey());
			}
		}
		for (int i = 0; i < delKeys.size(); i++) {
			tokenMap.remove(delKeys.get(i));
		}
	}
	
	public static void main(String[] args) {
		AccessToken.put("spsadmin","1");
		try {
			for (int i = 0; i < 100; i++) {
				Thread.sleep(1000);
				AccessToken.refresh();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
