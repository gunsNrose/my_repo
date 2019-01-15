package designPattern;

import designPattern.constants.Constants;

public enum CSFEnum {

    PREPAYORDER("prepayOrder",Constants.INTERFACE_ZHEJIANG_SUBMIT_PREPAYORDER),
    INCREMENTOFFER("incrementOffer",Constants.INTERFACE_ZHEJIANG_SUBMIT_INCREMENTOFFER);

    private String type;

    private String csfCode;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCsfCode() {
        return csfCode;
    }

    public void setCsfCode(String csfCode) {
        this.csfCode = csfCode;
    }

    CSFEnum(String type, String csfCode) {
        this.type = type;

        this.csfCode = csfCode;
    }

    public CSFEnum get(String type){
        CSFEnum[] values = CSFEnum.values();
        for (CSFEnum csfEnum: values){
            if(type.equals(csfEnum.getType())){
                return csfEnum;
            }
        }
        return null;
    }
}
