package enums;

import entity.StatusCode;
import jdk.net.SocketFlow;

/**
 * 枚举类,定义一些异常所用到的常量
 */
public enum CustomizeErrorCode implements ICustomizeErrorCode {
    JWTTOKEN_EXPRIED(false, StatusCode.JWTTOKEN_EXPIRED,"token已过期或者token错误！"),
    DECODER_ERROR(false, StatusCode.ERROR,"交理token解密失败"),
    QUESTION_NOT_FOUND(false,StatusCode.ERROR, "问题不存在或已删除！！！"),
    REDIS_EXPIRE_ERROR(false,StatusCode.ERROR,"Redis缓存设置过期失败"),
    REDIS_SYNC_ERROR(false,StatusCode.ERROR,"Redsi同步数据库失败");

    private boolean flag;
    private int code;
    private String message;

    CustomizeErrorCode(boolean flag, int code, String message) {
        this.flag = flag;
        this.code = code;
        this.message = message;
    }

    @Override
    public boolean getFalg() {
        return flag;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}


