package enums;

import entity.StatusCode;

/**
 * 枚举类,定义一些异常所用到的常量
 */
public enum CustomizeErrorCode implements ICustomizeErrorCode {
    JWTTOKEN_EXPRIED(false, StatusCode.JWTTOKEN_EXPIRED,"token已过期或者token错误！"),
    DECODER_ERROR(false, StatusCode.ERROR,"交理token解密失败"),
    QUESTION_NOT_FOUND(false,StatusCode.ERROR, "问题不存在或已删除！！！"),
    REDIS_EXPIRE_ERROR(false,StatusCode.ERROR,"Redis缓存设置过期失败"),
    REDIS_SYNC_ERROR(false,StatusCode.ERROR,"Redsi同步数据库失败"),
    LIKE_FAILED(false,StatusCode.ERROR,"点赞存入Redis失败！"),
    INSERT_COMMENT_FAILED(false,StatusCode.ERROR,"评论失败！"),
    INCREMENT_COMMENT_COUNT_FAILED(false,StatusCode.ERROR,"添加评论数失败！"),
    INCREMENT_REPLY_COUNT_FAILED(false,StatusCode.ERROR,"添加回复数失败！"),
    ;

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


