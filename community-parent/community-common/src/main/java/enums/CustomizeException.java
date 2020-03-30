package enums;

/**
 * 自定义异常处理方式
 */

public class CustomizeException extends RuntimeException {

    private boolean flag;
    private int code;
    private String message;

    public CustomizeException(ICustomizeErrorCode customizeErrorCode) {
        this.flag = customizeErrorCode.getFalg();
        this.code = customizeErrorCode.getCode();
        this.message = customizeErrorCode.getMessage();
    }

    public boolean getFlag() {
        return flag;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
