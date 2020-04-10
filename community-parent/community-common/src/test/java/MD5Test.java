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

    @Test
    public void test3(){
        String url = "http://192.168.129.155:9999/group1/M00/00/00/1.png";
        int i1 = url.indexOf("/",7);
        String substring1 = url.substring(i1+1);
        System.out.println(substring1);
        int i = substring1.indexOf("/");
        String substring = substring1.substring(0, i);
        System.out.println(substring);
        String substring2 = substring1.substring(substring.length() + 1);
        System.out.println(substring2);
    }


}
