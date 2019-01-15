package designPattern.inteface;

import net.sf.json.JSONObject;

import java.util.Map;

public abstract class AcceptHandelAbst {

    /**
     * 受理调用csf method
     * @param CSFCode   对应的csf编码
     * @param res       总入参
     * @return          CSFService返回的JSONObject
     */
    private void callCSFService(String CSFCode,Map<String,Object> res){
        JSONObject hasFailedRes = null;
//        logger.info("sumbitCfgBusiAccept入参："+ res);
//        JSONObject interData = null;
//        try {
//            interData = CSFCommonController.getResponse(CSFCode,res );
//        } catch (GeneralException e) {
//            e.printStackTrace();
//            hasFailedRes = new JSONObject();
//            hasFailedRes.put("hasCallFailed","1");
//            hasFailedRes.put("errInfo","常客平台CSF接口调用报错！");
//            return hasFailedRes;
//        }
//        logger.info("sumbitCfgBusiAccept出参："+ interData.toString());
//        if(!interData.isNullObject()  && !interData.isEmpty()){
//            if(String.valueOf(Constants.RESULT_CODE_SUCCESS).equals(interData.getString("rtnCode"))) {//统一接口平台返回成功
//                JSONObject object = JSONObject.fromObject(interData.getString("object"));
//                if (!object.isNullObject()  && !object.isEmpty()) {
//                    if (String.valueOf(Constants.RESULT_CODE_SUCCESS).equals(object.getString("respCode"))) {//ESB返回成功
//                        JSONObject result = JSONParseUtil.getJSONObject(object, "result");
//                        if (!result.isNullObject()  && !result.isEmpty()) {
//                            result.put("hasCallFailed","0");
//                            return result;
//                        }
//                    }else{
//                        hasFailedRes = new JSONObject();
//                        hasFailedRes.put("hasCallFailed","1");
//                        hasFailedRes.put("errInfo",object.getString("respDesc"));
//                    }
//                }
//            }else{
//                hasFailedRes = new JSONObject();
//                hasFailedRes.put("hasCallFailed","1");
//                hasFailedRes.put("errInfo",interData.getString("rtnMsg"));
//            }
//        }
//        return hasFailedRes;
    }

}
