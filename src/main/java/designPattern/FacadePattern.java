package designPattern;

import designPattern.inteface.AcceptHandel;

import java.util.Map;

public class FacadePattern {

    private Map<String,Object> param;

    private String offerType;

    private AcceptHandel acceptHandel;

    public FacadePattern(String jsonObj) {
        param = JSONUtil.jsonToMapForKF(jsonObj);
        offerType = param.get("offerType").toString();
    }

    private void init(String jsonObj){

    }

    public Map<String,Object> handel(){
        return  acceptHandel.handel();
    }

    public Map<String,Object> getCheckParam(){
        param.get("");


        return null;
    }
}
