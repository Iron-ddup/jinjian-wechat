package com.jumian.wechat.wechat;



import com.jumian.wechat.open.api.WechatOpenClient;
import com.jumian.wechat.open.properties.OpenProperties;
import com.jumian.wechat.pay.api.WechatPayClient;
import com.jumian.wechat.utils.ApiResponse;
import com.jumian.wechat.utils.JsonUtils;
import com.jumian.wechat.utils.ResponseUtil;
import com.jumian.wechat.vo.shop.MicroMerchantVO;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("wechat")
public class WechatController {

    @Autowired
    private WechatPayClient wechatPayClient;
    @Autowired
    private WechatOpenClient wechatOpenClient;



    @RequestMapping("getCert")
    public ApiResponse getCert() {
        Map<String, String> certficates = wechatPayClient.getCertficates();
        return ResponseUtil.buildResult(certficates);
    }

    @RequestMapping(value = "uploadMedia",method = RequestMethod.POST)
    public ApiResponse uploadMedia(@RequestParam(value = "multipartFile", required = false) MultipartFile multipartFile) {
        String mediaId = wechatPayClient.uploadMedia(multipartFile);
        return ResponseUtil.buildResult(mediaId);
    }

    /**
     * @api {post} /wechat/microMerchantSubmit 小微商户进件
     * @apiVersion 1.0.0
     * @apiDescription 小微商户进件
     * @apiGroup 16、微信授权
     * @apiParam {String} idCardCopy 身份证人像面照片(图片media)
     * @apiParam {String} idCardNational 身份证国徽面照片(图片media)
     * @apiParam {String} idCardName 身份证姓名
     * @apiParam {String} idCardNumber 身份证号码
     * @apiParam {String} idCardValidTime 身份证有效期限
     * @apiParam {String} accountName 开户名称
     * @apiParam {String} accountBank 开户银行
     * @apiParam {String} bankAddressCode 开户银行省市编码
     * @apiParam {String} bankName 开户银行全称（含支行）
     * @apiParam {String} accountNumber 银行账号
     * @apiParam {String} storeName 门店名称
     * @apiParam {String} storeAddressCode 门店省市编码
     * @apiParam {String} storeStreet 门店街道名称
     * @apiParam {String} storeLongitude 门店经度
     * @apiParam {String} storeLatitude 门店纬度
     * @apiParam {String} storeEntrancePic 门店门口照片(图片media)
     * @apiParam {String} indoorPic 店内环境照片(图片media)
     * @apiParam {String} addressCertification 经营场地证明
     * @apiParam {String} merchantShortname 商户简称
     * @apiParam {String} servicePhone 客服电话
     * @apiParam {String} productDesc 售卖商品/提供服务描述,餐饮、线下零售、居民生活服务、休闲娱乐、交通出行、其他
     * @apiParam {String} businessAdditionDesc 补充说明
     * @apiParam {String} businessAdditionPics 补充材料(图片media),如["122","333"]
     * @apiParam {String} contact 联系人姓名
     * @apiParam {String} contactPhone 手机号码
     * @apiParam {String} contactEmail 联系邮箱
     */
    @RequestMapping("microMerchantSubmit")
    public ApiResponse microMerchantSubmit(MicroMerchantVO microMerchantVO) {
        microMerchantVO.setBusinessCode("1001" + DateTime.now().toString("yyyyMMddHHmmSS"));
        microMerchantVO.setRate("0.6%");
        String json = JsonUtils.objectToJson(microMerchantVO);
        boolean isSuccess = wechatPayClient.microMerchantSubmit(json);

        return ResponseUtil.buildResult(isSuccess);
    }

    @RequestMapping("getMicroMerchantState")
    public ApiResponse a(String applymentId) {
        boolean isSuccess = wechatPayClient.getMicroMerchantState(applymentId);
        return ResponseUtil.buildResult(isSuccess);
    }
}
