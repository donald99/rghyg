package com.example.Bama.background.config;

/**
 * Created by sreay on 14-8-18.
 */
public class ServerConfig {
	// 测试环境
	public static String BASE_URL_TEST = "http://ghzofhit.dev.codevm.com";
	// 正式环境
	public static String BASE_URL_OFFICAL = "http://ghzofhit.dev.codevm.com";

	public static String BASE_URL = BASE_URL_OFFICAL;

	public static void initUrl(boolean boo) {
		if (boo) {
			ServerConfig.BASE_URL = ServerConfig.BASE_URL_TEST;
		} else {
			ServerConfig.BASE_URL = ServerConfig.BASE_URL_OFFICAL;
		}
	}

	public static final String URL_GET_RANK_LISR = "/soul/htdocs/im/ranking/user";
	public static final String URL_GET_CONTACT_REPORT = "/soul/htdocs/im/contact/report";
	public static final String URL_GET_TAG_LIST = "/soul/htdocs/im/tag/list";
	public static final String URL_GET_TAG_DETAIL = "/soul/htdocs/im/tag/detail";
	public static final String URL_GROUP_ADD = "/soul/htdocs/im/group/add";
	public static final String URL_USER_GET = "/soul/htdocs/im/user/get";
	public static final String URL_GROUP_INFO_GET = "/soul/htdocs/im/group/get";
	public static final String URL_VERSION_UPDATE = "xxxx";
}
