import io.jsonwebtoken.Claims;
import org.junit.Test;
import org.springframework.util.StringUtils;
import utils.JWTUtils;
import utils.MD5Utils;

public class MD5Test {

    @Test
    public void test(){
        String s = MD5Utils.MD5Encode("1234567");
        System.out.println(s);
        if (s.equals(MD5Utils.MD5Encode("1234567"))){
            System.out.println(true);
        }
    }

    @Test
    public void test2(){
        String ip = "127.0.0.1";
        String[] split = new String(ip).split("[\\.]");
        String b = "";
        for (String a : split) {
            b += a;
        }
        System.out.println(b);
    }


}
