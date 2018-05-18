package com.lazada.microservice.constans;

import com.lazada.microservice.util.JsonDateFormat;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import java.util.List;

/**
 * 返回常量类
 * @author Administrator
 *
 */
public class ResultConstant {


	/**
	 * 状态码名称：status    以下是指向的值
	 */


	/**
	 * 状态码：10000   标识成功
	 */
	public static final String SUCCESS = "10000";   //标识成功
	/**
	 * 状态码：10001  标识失败
	 */
	public static final String ERROR = "10001";   //标识失败
	/**
	 * 状态码：10002  标识类型错误
	 */
	public static final String TYPE_ERROR = "10002";   //标识类型错误
	/**
	 * 状态码：10003    标识超过限制大小
	 */
	public static final String SIZE_ERROR = "10003";   //标识超过限制大小
	/**
	 *  状态码：10004   标识参数空值错误
	 */
	public static final String PARAM_NULL_ERROR = "10004";   //标识参数空值错误
	/**
	 * 状态码：10005   标识返回空值错误
	 */
	public static final String RESULT_NULL_ERROR = "10005";   //标识返回空值错误
	/**
	 *  状态码：10006   标识格式错误
	 */
	public static final String FORMAT_ERROR = "10006";   //标识格式错误
	/**
	 * 状态码：10007   标识异常
	 */
	public static final String EXCEPTION_ERROR = "10007";   //标识异常
	/**
	 * 状态码：10008   标识存在错误
	 */
	public static final String EXIST_ERROR = "10008";   //标识存在错误
	/**
	 * 状态码：10009   标识不存在错误
	 */
	public static final String NO_EXIST_ERROR = "10009";   //标识不存在错误
	/**
	 * 状态码：10010  标识状态错误
	 */
	public static final String STATUS_ERROR = "10010";   //标识状态错误
	/**
	 * 状态码：10011   标识不匹配错误
	 */
	public static final String MATCH_ERROR = "10011";   //标识不匹配错误
	/**
	 * 状态码：10012   标识操作类型不合法错误
	 */
	public static final String OPERATION_ERROR = "10012";   //标识操作类型不合法错误
	/**
	 * 状态码：10013   标识密码错误
	 */
	public static final String PASSWORD_ERROR = "10013";   //标识密码错误
	/**
	 * 状态码：10014  标识用户名或密码错误
	 */
	public static final String USERNAME$PASSWORD_ERROR = "10014";   //标识用户名或密码错误
	/**
	 * 状态码：10015   标识未登录
	 */
	public static final String NOT_LOGIN_ERROR = "10015";   //标识未登录
	/**
	 * 状态码：10016   标识内容超过限制
	 */
	public static final String CONTENT_SIZE_ERROR = "10016";    //标识内容超过限制
	/**
	 * 状态码：10017   标识加密异常
	 */
	public static final String ENCRYPTION_EXCEPTION = "10017";   //加密异常
	/**
	 * 状态码：10018   标识不可删除
	 */
	public static final  String NOT_DELETE ="10018";   //标识不可删除
	/**
	 * 状态码：10019    标识无权限
	 */
	public static final  String NOT_POWER ="10019";   //标识无权限
	/**
	 * 状态码：10020    标识无文件
	 */
	public static final  String NOT_FILE ="10020";   //标识无文件

	/**
	 * 状态码：10020    标识验证码错误
	 */
	public static final  String LOGIN_CODE ="10021";   //标识验证码错误

	/**
	 * 解密约定
	 */
	public static final String SYSTEM_PWD_KEY = "ADMIN@YCXC20170106DESKEY";    //前后端解密的Key


	/**
	 * 根据传入的参数，将参数封装入JSON，返回String字符串
	 *    ////===========>>>>>>>用于返回分页数据
	 * @param list ：数据集合
	 * @param pageNum ：当前页
	 * @param pageSum ：总页数
	 * @param object ：用户对象
	 * @return ：返回JSON字符串
	 */
	public static String convertJSON$ToString(List list,Integer pageNum,Integer pageSum,Object object){

		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new JsonDateFormat("yyyy-MM-dd"));
		//创建JSON对象
		JSONObject jsonObject = new JSONObject();

		//判断数据是否为空
		if (list != null && list.size() > 0){
			//将数据集合存入json数组中
			JSONArray jsonArray = new JSONArray().fromObject(list,jsonConfig);
			//将数组存入json对象
			jsonObject.put("list",jsonArray.toString());
		}else{
			//将数组存入json对象
			jsonObject.put("list","");
		}
		if (pageNum != null){
			//将参数存入json对象中
			jsonObject.put("pageNum",pageNum);
		}
		if (pageSum != null){
			//将参数存入json对象中
			jsonObject.put("pageSum",pageSum);
		}
		if (object != null){
			//创建JSON对象，过滤时间
			JSONObject json = JSONObject.fromObject(object,jsonConfig);
			//将参数存入json对象中
			jsonObject.put("user",json.toString());
		}
		//判断JSON串是否为空
		if(jsonObject.isEmpty() || jsonObject == null){
			//返回空值状态码
			return ResultConstant.RESULT_NULL_ERROR;
		}
		//返回json对象转换为String字符串
		return jsonObject.toString();
	}


	/**
	 * 转换成JSON串
	 * @param list ：返回值集合
	 * @param object ：对象1
	 * @param object1 ：对象2
	 * @param object2 ：对象3
	 * @return
	 */
	public static String convertJSON$ToString(List list,Object object,Object object1,Object object2){
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new JsonDateFormat("yyyy-MM-dd"));
		//创建JSON对象
		JSONObject jsonObject = new JSONObject();
		//判断数据是否为空
		if (list != null && list.size() > 0){
			//将数据集合存入json数组中
			JSONArray jsonArray = new JSONArray().fromObject(list,jsonConfig);
			//将数组存入json对象
			jsonObject.put("listZore",jsonArray.toString());
		}
		if (object != null){
			//创建JSON对象，过滤时间
			JSONObject json = JSONObject.fromObject(object,jsonConfig);
			//将参数存入json对象中
			jsonObject.put("objectZore",json.toString());
		}
		if (object1 != null){
			//创建JSON对象，过滤时间
			JSONObject json = JSONObject.fromObject(object1,jsonConfig);
			//将参数存入json对象中
			jsonObject.put("objectOne",json.toString());
		}
		if (object2 != null){
			//创建JSON对象，过滤时间
			JSONObject json = JSONObject.fromObject(object2,jsonConfig);
			//将参数存入json对象中
			jsonObject.put("objectTwo",json.toString());
		}
		//判断JSON串是否为空
		if(jsonObject.isEmpty() || jsonObject == null){
			//返回空值状态码
			return ResultConstant.RESULT_NULL_ERROR;
		}
		//返回json对象转换为String字符串
		return jsonObject.toString();
	}


	/**
	 * 将数据转换成JSON串
	 * @param list ：集合1
	 * @param list1 ：集合2
	 * @param object ：对象1
	 * @param object1 ：对象2
	 * @return
	 */
	public static String convertJSON$ToString(List list,List list1,Object object,Object object1){

		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new JsonDateFormat("yyyy-MM-dd"));
		//创建JSON对象
		JSONObject jsonObject = new JSONObject();

		//判断数据是否为空
		if (list != null && list.size() > 0){
			//将数据集合存入json数组中
			JSONArray jsonArray = new JSONArray().fromObject(list,jsonConfig);
			//将数组存入json对象
			jsonObject.put("listZore",jsonArray.toString());
		}
		if (list1 != null && list1.size() > 0){
			//将数据集合存入json数组中
			JSONArray jsonArray = new JSONArray().fromObject(list1,jsonConfig);
			//将数组存入json对象
			jsonObject.put("listOne",jsonArray.toString());
		}
		if (object != null){
			//创建JSON对象，过滤时间
			JSONObject json = JSONObject.fromObject(object,jsonConfig);
			//将参数存入json对象中
			jsonObject.put("objectZore",json.toString());
		}
		if (object1 != null){
			//创建JSON对象，过滤时间
			JSONObject json = JSONObject.fromObject(object1,jsonConfig);
			//将参数存入json对象中
			jsonObject.put("objectOne",json.toString());
		}
		//判断JSON串是否为空
		if(jsonObject.isEmpty() || jsonObject == null){
			//返回空值状态码
			return ResultConstant.RESULT_NULL_ERROR;
		}
		//返回json对象转换为String字符串
		return jsonObject.toString();
	}


	/**
	 * 封装单个对象
	 * @param object ：返回的对象
	 * @return
	 */
	public static String convertJSON$ToString(Object object){

		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new JsonDateFormat("yyyy-MM-dd"));
		//创建JSON对象
		JSONObject jsonObject = null;
		if (object != null){
			//创建JSON对象，过滤时间
			jsonObject = JSONObject.fromObject(object,jsonConfig);
//			//将参数存入json对象中
//			jsonObject.put("object",json.toString());
		}

		//判断JSON串是否为空
		if(jsonObject.isEmpty() || jsonObject == null){
			//返回空值状态码
			return ResultConstant.RESULT_NULL_ERROR;
		}
		//返回json对象转换为String字符串
		return jsonObject.toString();
	}


	/**
	 * 封装单个集合
	 * @param list ：返回的集合
	 * @return
	 */
	public static String convertJSON$ToString(List list){

		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new JsonDateFormat("yyyy-MM-dd"));
		//创建JSON对象
		JSONObject jsonObject = new JSONObject();
		if (list != null){
			//创建JSON对象，过滤时间   将数据集合存入json数组中
			JSONArray jsonArray = new JSONArray().fromObject(list,jsonConfig);
			//将参数存入json对象中
			jsonObject.put("list",jsonArray);
		}else{
			//将数组存入json对象
			jsonObject.put("list","");
		}

		//判断JSON串是否为空
		if(jsonObject.isEmpty() || jsonObject == null){
			//返回空值状态码
			return ResultConstant.RESULT_NULL_ERROR;
		}
		//返回json对象转换为String字符串
		return jsonObject.toString();
	}


	/**
	 * 封装一个集合、一个对象
	 * @param list
	 * @param object
	 * @return
	 */
	public static String convertJSON$ToString(List list,Object object){

		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new JsonDateFormat("yyyy-MM-dd"));
		//创建JSON对象
		JSONObject jsonObject = new JSONObject();

		//判断数据是否为空
		if (list != null && list.size() > 0){
			//将数据集合存入json数组中
			JSONArray jsonArray = new JSONArray().fromObject(list,jsonConfig);
			//将数组存入json对象
			jsonObject.put("list",jsonArray.toString());
		}else{
			//将数组存入json对象
			jsonObject.put("list","");
		}

		if (object != null){
			//创建JSON对象，过滤时间
			JSONObject json = JSONObject.fromObject(object,jsonConfig);
			//将参数存入json对象中
			jsonObject.put("object",json.toString());
		}
		//判断JSON串是否为空
		if(jsonObject.isEmpty() || jsonObject == null){
			//返回空值状态码
			return ResultConstant.RESULT_NULL_ERROR;
		}
		//返回json对象转换为String字符串
		return jsonObject.toString();
	}


	/**
	 * 用于返回信息
	 * @param statusCode：状态码
	 * @param resuMessage ：返回信息
	 * @return
	 */
	public static String resuInfo(String statusCode,String resuMessage){
		//json配置对象
		JsonConfig jsonConfig = new JsonConfig();
		//设置json中的时间
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new JsonDateFormat("yyyy-MM-dd"));
		//创建JSON对象
		JSONObject jsonObject = new JSONObject();
		//判断状态码是否为空
		if(statusCode != null && !statusCode.equals("")){
			jsonObject.put("code",statusCode);
		}
		//判断信息是否为空
		if(resuMessage != null && !resuMessage.equals("")){
			jsonObject.put("message",resuMessage);
		}
		//在此就不进行判断了
		//直接返回信息
		return jsonObject.toString();
	}

}
