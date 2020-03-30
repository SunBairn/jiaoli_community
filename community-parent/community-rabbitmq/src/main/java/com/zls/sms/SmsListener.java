package com.zls.sms;

import com.aliyuncs.exceptions.ClientException;
import com.zls.utils.SmsUtil;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 短信监听类
 */
@Component
public class SmsListener {

    @Autowired
    SmsUtil smsUtil;

    @Value("${aliyun.sms.template_code}")
    private String template_cod;

    @Value("${aliyun.sms.sing_name}")
    private String sign_name;

    @RabbitListener(queues = "sms")
    public void sendSms(Map<String,String> map){
        String mobile = map.get("mobile");
        String code = map.get("code");
        System.out.println("手机号："+map.get("mobile"));
        System.out.println("验证码："+map.get("code"));
        try {
            smsUtil.sendSms(map.get("mobile"),template_cod,sign_name,"{\"code\":\""+code+"\"}");
        } catch (ClientException e) {
            e.printStackTrace();
        }

    }



}
