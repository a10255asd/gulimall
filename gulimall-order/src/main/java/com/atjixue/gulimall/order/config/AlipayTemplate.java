package com.atjixue.gulimall.order.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.atjixue.gulimall.order.vo.PayVo;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "alipay")
@Component
@Data
public class AlipayTemplate {

    //在支付宝创建的应用的id
    private   String app_id = "2021000121668737";

    // 商户私钥，您的PKCS8格式RSA2私钥
    private  String merchant_private_key = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCETx9X3TRQvW4CY1JhwIWXfKg+VqJeMIIiUy+RFUaHw0ZVikANCgEVNLkZPNIkHd0qznRdSEDMhWigKuyqYvLIVWucZLbA/4tjuGRYhUa/zOKcTZxgkGp4AlbMaSRul4LQvcaE1Yy6Kl2/yf87bchyrQQTzS2UNzTbE3PjzTxHYO9GKsVSR+7PrDJ1tsydgpDs/kUubP/6+i9T8YDdJbp3w4mk/eyQTWa1bFfls8/WtYli0OJrTo/AVgJI3BR4QkkCX/GJmaSRerXjUB+dOWUDrc5czWqflfNMXqC/pv8DEks/6Ivke4Bkjl2ogQFQa40/Y6aGX290BDtHcxhQFeWNAgMBAAECggEAUpnfFIi6R4nr6zu8y+BYxtmBY+HMXodkRy+t66LftJR6DZ9RG5LaWDXhNVteSkQGttnLlg0sv0Kurgf+VuWNcvq5QxsxaU5g5HHS29zg7Y9pDnH5peOjRKcZL2aY4x5eAhnonTeBMxyOCNWALcb9NHLFr+MRiI3qxrbfTCHudxSsZiupZKHeQkZ7MQOPPmnmZ30961aHKonIPMG0MWc2Lfvz/jUu+pwCCV42hzVLZBrHeHGf3/ENuGZe7RObSlH+yTtWC/FXTlk4VEXnoZdQ/n7VR+sMzZRvvw5yseduewYyN3Fz28+oxoRyLeEmuUqYbV8uH6+YAThop1JMh1XQlQKBgQDAbxpZ6uKHtNZ6QJFBgLqJXfciL327cPt0fFtuhyGjt7qn3tUH5pZezVdB0h1Xj7rCoVPe54O7LwopuqE+Es+KB2SQq3STKv9k1+myf4kY9dSSxxGW1XENnL+LnSFh51c2M4AiEeKnMb9I7FwTIZ+sGsojKoVkTk6eIUEHO8T/HwKBgQCwA6TcQEOUKnqJA+GvnX2ZUP/bfrJ8PMEWqiYvv7eR5Y5sKznV7ecHkpY9xHoaimCo4+6MiWyHYt0KeFo7cGIqJY8oYpzngZiAKxCOt/IqcX8imk4u/5a+yLEFxz1WkMLqEOVnsZOIXWRNltBNanjz3rgWXWlViu44eLYmd7eB0wKBgD92JH2U3cIdXEIS+xRcDKhyqabHfLKNLxAaVtjbjbd9RvxHm3+whtSa6XTWg0C2MMQxCgTP2vynJbXfS3+6GmGN0Jd5Q8eo/XLXVcW9E4x8ve7t4ZoZp9/yWqUDW9f6wCMq1s3lkhbLbCbzc+YHR64tY443XqhWvsydcOGez3fHAoGAGfD/jEKgcybrlmQPEC37A+yvOO9saOkfYDLFnAX6jTMzEXgxdvkwMqqFICdkA6NfaaTmVKowZ6fHgjKIwffnT+TnO4GFNI2iJ0mxkAdGVsERhs0NNtoOoqG7jWl2/doqEAtbdDP01YVGMjVHEBHgL7kkko9D5gqq34HS/spoLmcCgYBn1ejF2FRVj+zKjUIyqK4ZRdlLav6fuTEDrkUpL1u0WMvlqDEQj8QB0tYMpO5GA0jFb/trDrW9SrbkTmmRnkSe9tamj3OIDPFKHHinmGJMlMrioHWEM+B/6Aynki8SS9QnVAHZjkbO7K3I2ToJzc7RvHK6zSX2U1tXG3yFqrGNFA==";
    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    private  String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlVt9s1NJz9aDHDxzJVnz4Uqh935LmLT/706IA4g/GBBXebtiWVbuW8j4oozwZwLhXVsc0Y6limP0UYvbA1dVF5Y32Op+wSjcZX0S+7MZHHhBsNr1HT6/Tahtl5LbnKueEQljyRysNAZGXlWbTluw7/ckG71HrYJm5EVtKX9QcIYeGCrV+Q3zatr5hM1RchD8DyVGwdWFaEI1ydww/yiHzdN8YLIuWKf5YPN5NPTYLrburvLHVm2vFDHiSq5e9FNpVDKHtphhrZPUA5EpTBtSZ3PcW1+V/C4ERpZyVOy+ekKpbrXBcfkavP/fc3gOGlanslOAibTAQ3fKINxehiST0QIDAQAB";
    // 服务器[异步通知]页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    // 支付宝会悄悄的给我们发送一个请求，告诉我们支付成功的信息
    private  String notify_url ="http://localhost:8080/alipay.trade.page.pay-JAVA-UTF-8/notify_url.jsp";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    //同步通知，支付成功，一般跳转到成功页
    private  String return_url = " http://member.guliamll.com/memberOrder.html";

    // 签名方式
    private  String sign_type = "RSA2";

    // 字符编码格式
    private  String charset = "utf-8";

    private String timeout = "30m";

    // 支付宝网关； https://openapi.alipaydev.com/gateway.do
    private  String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    public  String pay(PayVo vo) throws AlipayApiException {

        //AlipayClient alipayClient = new DefaultAlipayClient(AlipayTemplate.gatewayUrl, AlipayTemplate.app_id, AlipayTemplate.merchant_private_key, "json", AlipayTemplate.charset, AlipayTemplate.alipay_public_key, AlipayTemplate.sign_type);
        //1、根据支付宝的配置生成一个支付客户端
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl,
                app_id, merchant_private_key, "json",
                charset, alipay_public_key, sign_type);

        //2、创建一个支付请求 //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(return_url);
        alipayRequest.setNotifyUrl(notify_url);

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = vo.getOut_trade_no();
        //付款金额，必填
        String total_amount = vo.getTotal_amount();
        //订单名称，必填
        String subject = vo.getSubject();
        //商品描述，可空
        String body = vo.getBody();

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                + "\"timeout_express\":\" " + timeout + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        String result = alipayClient.pageExecute(alipayRequest).getBody();

        //会收到支付宝的响应，响应的是一个页面，只要浏览器显示这个页面，就会自动来到支付宝的收银台页面
        System.out.println("支付宝的响应："+result);

        return result;

    }
}
