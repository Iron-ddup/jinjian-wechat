package com.jumian.wechat.open.constants;

public class OpenUrlConstants {

    public final static String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/component/api_component_token";

    public final static String AUTHORIZE_URL = "https://mp.weixin.qq.com/cgi-bin/componentloginpage";

    public final static String PRE_AUTH_CODE_URL = "https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode";

    public final static String QUERY_AUTH_URL = "https://api.weixin.qq.com/cgi-bin/component/api_query_auth";

    public final static String AUTHORIZER_INFO_URL = "https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_info";

    public final static String AUTHORIZER_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/component/api_authorizer_token";

    public final static String COMMIT_URL = "https://api.weixin.qq.com/wxa/commit";

    public final static String SET_NICKNAME_URL = "https://api.weixin.qq.com/wxa/setnickname";

    public final static String MODIFY_HEAD_IMAGE_URL = "https://api.weixin.qq.com/cgi-bin/account/modifyheadimage";

    public final static String MODIFY_SIGNATURE_URL = "https://api.weixin.qq.com/cgi-bin/account/modifysignature";

    public final static String ADD_CATEGORY_URL = "https://api.weixin.qq.com/cgi-bin/wxopen/addcategory";

    public final static String GET_CATEGORY_URL = "https://api.weixin.qq.com/cgi-bin/wxopen/getcategory";

    public final static String MODIFY_DOMAIN_URL = "https://api.weixin.qq.com/wxa/modify_domain";

    public final static String CODE2SESSION_URL = "https://api.weixin.qq.com/sns/component/jscode2session?appid=%s&js_code=%s&grant_type=authorization_code&component_appid=%s&component_access_token=%s";

    public final static String FAST_REGISTER_WEAPP_URL = "https://api.weixin.qq.com/cgi-bin/component/fastregisterweapp?action=create&component_access_token=%s";

    public final static String MEDIA_UPLOAD_URL="https://api.weixin.qq.com/cgi-bin/media/upload";

    public final static String SUBMIT_AUDIT_URL="https://api.weixin.qq.com/wxa/submit_audit";

    public final static String GET_AUDITSTATUS_URL="https://api.weixin.qq.com/wxa/get_auditstatus";

    public final static String GET_WXACODE_URL="https://api.weixin.qq.com/wxa/getwxacode";

    public final static String GET_WXACODE_UNLIMIT_URL="https://api.weixin.qq.com/wxa/getwxacodeunlimit";

    public final static String TEMP_SEND_URL="https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send";

    public final static String RELEASE_URL="https://api.weixin.qq.com/wxa/release";

    public final static String ADD_TEMP_URL="https://api.weixin.qq.com/cgi-bin/wxopen/template/add";
}
