package com.zozmom.constants;


public class Constant {
	
	public static final String GET_IP_URL="http://ip.taobao.com/service/getIpInfo2.php?ip=myip" ;
	
	public static final String weixin="http://weixin.zozmom.com/";
	public static final String weixin_circle="http://weixin.zozmom.com/#luckyCircle";
	
	// 分享
	public static final String SHARES_LUCKYDRRAW=weixin+"#shareLuckyDraw";
	
	public static final String FACE_URL ="http://face.zozmom.com/";
	public static final String FACE_KEY="_face";
	public static final String SHARE_URL="http://share.zozmom.com/";
	public static final String SHARE_KEY="_share";
	public static final String PRODUCT_URL="http://product.zozmom.com/";
	public static final String PRODUCT_KEY="_product";
	
	
//	public static final String URL_SERVER_ROOT = "http://10.1.24.131:8080/yyzm-app";
//	public static final String URL_SERVER_ROOT = "http://192.168.0.115:8081/";
	public static final String URL_SERVER_ROOT = "https://api.zozmom.com";
//	public static final String URL_SERVER_ROOT = "http://120.27.152.196:8888/yyzm-app";
	public static final String LOGIN = URL_SERVER_ROOT + "/user/login/";
	public static final String REGISTER_CHECK = URL_SERVER_ROOT + "/user/registerChecked";
//	public static final String REGISTER = URL_SERVER_ROOT + "/user/register";
	public static final String ALTER_PWD_BYPHONE = URL_SERVER_ROOT + "/user/modifyPasswordByPhone";
	public static final String ALTER_PWD_BYPWD= URL_SERVER_ROOT + "/user/verify/modifyPasswordByPwd";
	//获取用户详细信息
//	public static final String GET_USERMESSAGE_BYUSERID=URL_SERVER_ROOT+"/user/getUserInfo";
	//修改个人信息
	public static final String UPDATA_USER=URL_SERVER_ROOT+"/user/verify/updateUserInfo";
	
	public static final String SELECTE_PRODUCT_BYTYPE_HOT=URL_SERVER_ROOT+"/product/productList/hot";
	public static final String SELECTE_PRODUCT_BYTYPE_NEW=URL_SERVER_ROOT+"/product/productList/new";
	public static final String SELECTE_PRODUCT_BYTYPE_PROGRESS=URL_SERVER_ROOT+"/product/productList/progress";
	public static final String SELECTE_PRODUCT_BYTYPE_TOTAL=URL_SERVER_ROOT+"/product/productList/total";
	// 创建订单
//	public static final String CREATE_ORDER=URL_SERVER_ROOT+"/order/verify/createOrder";
	//闪屏 和banner
	public static final String SCREEN=URL_SERVER_ROOT+"/home/getBannerOrScrrenList/s";
	
	public static final String BANNER=URL_SERVER_ROOT+"/home/getBannerOrScrrenList/b";
	// 查看单个商品详情
	public static final String PRODUCT_MESSAGE=URL_SERVER_ROOT+"/product/productDetail";
	
	//根据type查询商品
	public static final String SELECT_PRODUCT_BY_TYPE=URL_SERVER_ROOT+"/product/productByType/";
	
	//查询用户参与的次数和购买的逐梦码
	public static final String SELECT_USER_JION=URL_SERVER_ROOT+"/product/joinDreamCount";
	
	//根据id查找最新一期的商品详情
	public static final String LAST_PRODUCT_DETAIL=URL_SERVER_ROOT+"/product/latestProductDetail";
	//创建订单
	public static final String CREATEORDER=URL_SERVER_ROOT+"/order/v2/verify/createOrder";
	//支付
	public static final String  PAY_ORDER_ByCoin=URL_SERVER_ROOT+"/pay/verify/orderPay/";
	public static final String  PAY_ORDER=URL_SERVER_ROOT+"/ipay/verify/pay/";
//	public static final String  PAY_ORDER=URL_SERVER_ROOT+"/pay/verify/payWithChannel/";
//	/verify/payWithChannel/{orderNum}?channel=…&clientIp=…&dreamCoin=…&selected=…
	//最新揭晓
	public static final String LAST_LOTTERY=URL_SERVER_ROOT+"/product/lotteryProductList";
	//获取已经揭晓的逐梦号
	public static final String PRODUCT_RECORD=URL_SERVER_ROOT+"/product/dreamProductRecord";
	
	//计算详情
	public static final String PRODUCT_CALCDATA=URL_SERVER_ROOT+"/product/lotteryProductCalcData";
	
	// 计算中 获取时间
	public static final String PRODUCT_DREAM_RECORD=URL_SERVER_ROOT+"/product/productDreamRecord";
	// 获取user信息 by id
	public static final String USER_DREAM_LIST=URL_SERVER_ROOT+"/user/userInfo/";
	//获取user 逐梦记录
	public static final String USER_DREAM_RECORD_LIST=URL_SERVER_ROOT+"/user/dreamRecordList/";
	//获取 自己的 逐梦记录
//	public static final String MAIN_DREAM_RECORD_LIST=URL_SERVER_ROOT+"/user/verify/dreamRecordList/";
	//查询地址
	public static final String SELECT_ADDRESS=URL_SERVER_ROOT+"/user/verify/getAddressList";
	//添加地址
	public static final String ADD_ADDRESS=URL_SERVER_ROOT+"/user/verify/saveAddress";
	//删除地址
	public static final String DELETE_ADDRESS=URL_SERVER_ROOT+"/user/verify/deleteAddress?id=";
	//修改地址
	public static final String UPDATE_ADDRESS=URL_SERVER_ROOT+"/user/verify/updateAddress";
	//选择地址
	public static final String CHOOSE_ADDRESS=URL_SERVER_ROOT+"/order/verify/updateOrderAddress";
	//实现的梦想
	public static final String LUCK_DREAM=URL_SERVER_ROOT+"/user/luckyDreamRecord/";
	//自己实现的梦想
//	public static final String MAIN_LUCK_DREAM=URL_SERVER_ROOT+"/user/verify/luckyDreamRecord/";
	//晒单详情
	public static final String SHOW_INFO=URL_SERVER_ROOT+"/product/shareOrderInfo";
	//提交晒单
	public static final String SUBMIT_SHOW=URL_SERVER_ROOT+"/product/verify/addShareOrder";
	
	//查询自己的晒单列表
	public static final String ownShareOrderList=URL_SERVER_ROOT+"/product/verify/ownShareOrderList/";
	//查询别人晒单列表
	public static final String shareOrderList=URL_SERVER_ROOT+"/product/shareOrderList";
	 //查询商品晒单列表
	public static final String productShareOrderList=URL_SERVER_ROOT+"/product/productShareOrderList";
	//更新晒单详情
	public static final String updateShareOrder=URL_SERVER_ROOT+"/product/verify/updateShareOrder";
	//确认收货
	public static final String confirmReceipt=URL_SERVER_ROOT+"/order/verify/confirmReceipt";
	//获取剩余逐梦币和佣金
	public static final String ownPrivateInfo=URL_SERVER_ROOT+"/user/verify/ownPrivateInfo";
//	//用type获取渠道id
//	public static final String shareBonus=URL_SERVER_ROOT+"/bonus/verify/shareBonus/";
	//领红包
	public static final String receiveBonus=URL_SERVER_ROOT+"/bonus/receiveBonus/";
//			var zm_shareBonus='/bonus/verify/shareBonus/' //用type获取渠道id
	 //获取红包列表
	public static final String userBonusList=URL_SERVER_ROOT+"/bonus/verify/userBonusList/";
	//获取物流信息
	public static final String userLogisticsInfo=URL_SERVER_ROOT+"/user/verify/userLogisticsInfo";
	//取消订单
	public static final String cancelOrder=URL_SERVER_ROOT+"/order/verify/cancelOrder/";
	//佣金兑换
	public static final String D_COMM=URL_SERVER_ROOT+"/user/verify/useBrokerageUsed";
	//分享
	public static final String zm_shareBonus=URL_SERVER_ROOT+"/bonus/verify/shareBonus/";
	//分享商品
	public static final String share_product="http://weixin.zozmom.com/#productDetail?pid=";
	
	//获取充值记录
	public static final String GET_RECHARGE_RECORD=URL_SERVER_ROOT+"/user/verify/getRechargeRecord";//{userId}
	//获取消费记录
	public static final String GET_CONSUNME_RECORD=URL_SERVER_ROOT+"/user/verify/getConsumeRecord";//{userId}
	//获取积分明细
	public static final String GEY_brokerage_RECORD=URL_SERVER_ROOT+"/user/verify/brokerageRecordList";
	//充值逐梦币
//	public static final String RECHARGE_DREAM_COIN=URL_SERVER_ROOT+"/pay/verify/payWithDreamCoin";
	public static final String RECHARGE_DREAM_COIN=URL_SERVER_ROOT+"/ipay/verify/payWithDreamCoin";
	//channel支付渠道  price 充值金额
	
	public static final String  INPUT_CODE=URL_SERVER_ROOT+"/user/verify/joinInviteNexus/";
	
	//更新版本
	public static final String UP_VERSION= URL_SERVER_ROOT +"/home/checkVersion/";//更新版本{vid}
	
	//查看商品的网页详情
	public static final String product_mess=weixin+"#productDetails?pid=";
	
	//提现
	public static final String USER_APPLY=URL_SERVER_ROOT+"/user/verify/applyForWithdraw";
	
	//分享添加次数
	public static final String USER_SHARE_ADDTIME=URL_SERVER_ROOT+"/activity/verify/shareActivity/1";

	public static final String GET_NEWUSER_HONGBAO=URL_SERVER_ROOT+"/activity/defaultActivity";
	//专区
	public static final String DIS=URL_SERVER_ROOT+"/product/productArea/";
	
	//活动
	public static final String ACTIVITY_LIST=URL_SERVER_ROOT+"/activity/activityConfigureList";
	
	//查看邀请人
	public static final String FIND_INVITEDUSER=URL_SERVER_ROOT+"/user/findUserInviteCode";
	
	//验证码
	public static final String gee_check1=URL_SERVER_ROOT+"/user/startCaptcha";
	public static final String gee_check=URL_SERVER_ROOT+"/user/verifyLogin";
}
