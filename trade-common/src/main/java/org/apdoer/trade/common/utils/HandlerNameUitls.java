package org.apdoer.trade.common.utils;

/**
 * @author apdoer
 */
public class HandlerNameUitls {
	
	private static final char UNDERLINE = '_';

	public static String buildIndexMonitorHandleName(String topic) {
		return underlineToCamel(topic) + "MonitorHandler";
	}
	
	public static String buildPosiOpenSyncHandleName(String topic) {
		return underlineToCamel(topic) + "PosiOpenSyncHandler";
	}
	
	public static String buildPosiCloseSyncHandleName(String topic) {
		return underlineToCamel(topic) + "PosiCloseSyncHandler";
	}
	
	/**
	 * 下划线格式字符串转换为驼峰格式字符串
	 * 
	 * @param param
	 * @return
	 */
	private static String underlineToCamel(String param) {
		if (param == null || "".equals(param.trim())) {
			return "";
		}
		int len = param.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = param.charAt(i);
			if (c == UNDERLINE) {
				if (++i < len) {
					sb.append(Character.toUpperCase(param.charAt(i)));
				}
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

}
