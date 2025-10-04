package open.api.config;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import open.api.constant.RedisConstant;
import open.api.interfaceinfo.InterfaceInfoFeign;
import open.api.model.entity.UserInterfaceInfo;
import open.api.response.ResponseCodeEnum;
import open.api.response.ResultData;
import open.api.system.applicaionkey.ApplicationKeyFeign;
import open.api.system.ipwhite.IpWhiteFeign;
import open.api.userinterfaceinfo.UserInterfaceInfoFeign;
import open.api.utils.EncodeUtil;
import open.api.utils.JwtUtil;
import open.api.utils.SignUtil;
import org.apache.commons.lang.StringUtils;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ClassName: MyGlobalFilter
 * Package: open.api.config
 * Description:
 *
 * @Author: @weixueshi
 * @Create: 2024/5/1 - 23:33
 * @Version: v1.0
 */
/**
 * 自定义全局网关过滤器
 */
@Slf4j
@Component
public class InterfaceGlobalFilter implements GlobalFilter, Ordered {
    @Resource
    @Lazy
    private ApplicationKeyFeign applicationKeyFeign;

    @Resource
    @Lazy
    private IpWhiteFeign ipWhiteFeign;

    @Resource
    @Lazy
    private InterfaceInfoFeign interfaceInfoFeign;

    @Resource
    @Lazy
    private UserInterfaceInfoFeign userInterfaceInfoFeign;

    @Resource
    @Lazy
    private RedisTemplate redisTemplate;

    /**
     * 设置IP地址白名单
     */
    private List<String> IP_WHITE_LIST = Arrays.asList("127.0.0.1");

    /**
     * 设置请求时间不超过5分钟
     */
    private Long  TIME_OUT = 60 * 5L;

    /**
     * 用户token
     */
    private static String USER_TOKEN = "";

    //所谓白名单就是用户没有登录就可以访问的路径
    //黑名单就是用户没有登录就不可以访问的路径
    //1.创建自定义的拦截器对象
    //2.配置白名单并存放在一个List集合
    List<String> patters = Arrays.asList("/permission/user/login","/permission/user/register","/permission/user/send/code","/permission/user/login/email");

    /**
    * 注入消息转换器
    * @param converters
    * @return
    */
    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
        return new HttpMessageConverters(converters.orderedStream().collect(Collectors.toList()));
    }

    /**
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();

        if(patters.contains(request.getPath().toString())){
            //如果是登录请求则直接放行
            return chain.filter(exchange);
        }

        if(!request.getPath().toString().startsWith("/provider")){
            USER_TOKEN = headers.getFirst("authorization");
        }

        // 以/provider开头的请求，说明是内部SDK服务，需要进行接口鉴权
        if(request.getPath().toString().startsWith("/provider")){
            //接口鉴权以及业务处理
            return handleAuthAndVerify(exchange,chain,request);
        }else{
            //判断请求头是否携带token
            return handleVerifyToken(exchange,request,chain);
        }
    }

    private Mono<Void> handleVerifyToken(ServerWebExchange exchange,ServerHttpRequest request,GatewayFilterChain chain) {
        HttpHeaders headers = request.getHeaders();
        String token = headers.getFirst("authorization");
        if(StringUtils.isBlank(token)){
            return handlerResponse(exchange.getResponse(),ResponseCodeEnum.FAIL);
        }
        //与redis缓存的token做比较
        String cacheToken = (String) redisTemplate.opsForValue().get(RedisConstant.TOKEN_CACHE_KEY + JwtUtil.parseToken(token));
        if(StringUtils.isBlank(cacheToken)){
            return handlerResponse(exchange.getResponse(),ResponseCodeEnum.FAIL);
        }
        return chain.filter(exchange);
    }

    private Mono<Void> handleAuthAndVerify(ServerWebExchange exchange, GatewayFilterChain chain,ServerHttpRequest request) {
        ServerHttpResponse response = exchange.getResponse();
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String body = headers.getFirst("body");
        String sign = headers.getFirst("sign");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        //校验接口和应用逻辑
        JSONObject jsonObject = JSONUtil.parseObj(EncodeUtil.decodeBody(body));
        String url = jsonObject.getStr("url");
        String encodeUrl = EncodeUtil.encodeBody(url);
        //校验接口是否存在
        ResultData result = interfaceInfoFeign.queryInterfaceInfoByUrl(encodeUrl);
        if ((Boolean) result.getData()){
            return handlerResponse(response,ResponseCodeEnum.INTERFACE_NOT_EXITS);
        }

        //校验accessKey所属应用是否开启
        ResultData status = applicationKeyFeign.queryApplicationStatus(accessKey);
        if((Boolean) status.getData()){
            return handlerResponse(response,ResponseCodeEnum.APPLICATION_FORBIDDEN);
        }
        //查询额度是否有剩余
        ResultData lines = applicationKeyFeign.queryAccessKeyLines(accessKey);
        if((Boolean) lines.getData()){
            return handlerResponse(response,ResponseCodeEnum.LIINES_INSUFFICIENT);
        }
        //验证应用key设置的IP白名单
        List<String> ipWhiteList = ipWhiteFeign.queryIpWhiteListByAccessKey(accessKey);
        if(!CollectionUtils.isEmpty(ipWhiteList)){
            if(!ipWhiteList.contains(request.getRemoteAddress().getHostName())){
                //7、调用失败，封装返回一个规范的错误码
                log.error("请求地址在应用key设置的黑名单中...{}", request.getRemoteAddress().getHostName());
                return handlerResponse(response, ResponseCodeEnum.IPADDRESS_FORBIDDEN);
            }
        }
        //1、请求日志打印
        printLog(request);
        //2、访问控制：黑白名单处理
        //TODO: 访问控制：黑白名单处理
        // if(!IP_WHITE_LIST.contains(request.getRemoteAddress().getHostName())){
        //     //7、调用失败，封装返回一个规范的错误码
        //     log.error("请求地址在黑名单中...{}", request.getRemoteAddress().getHostName());
        //     return handlerResponse(response, ResponseCodeEnum.IPADDRESS_FORBIDDEN);
        // }
        //3、用户鉴权
        //远程调用system模块查询key数据
        ResultData resultData = applicationKeyFeign.queryKeyByAccessKey(accessKey);
        String secretKey = (String) resultData.getData();
        if(null == secretKey){
            log.error("secretKey认证失败");
            return handlerResponse(response,ResponseCodeEnum.VERTIFY_CODE_ERROR);
        }
        //对比签名sign是否相同
        //重新生成一次即可,secretKey可从数据库中获取，盐值可在客户端配置时配置
        String newSign = SignUtil.getSign(body, secretKey);
        if(!newSign.equalsIgnoreCase(sign)){
            log.error("签名认证失败");
            return handlerResponse(response,ResponseCodeEnum.VERTIFY_CODE_ERROR);
        }
        //防重放措施：验证随机数和时间戳（调用时间不能超过5分钟，验证随机数不再实现）
        Long currentTime = System.currentTimeMillis() / 1000;
        if((currentTime - Long.parseLong(timestamp)) >= TIME_OUT){
            log.error("超时请求，认证失败{}",(currentTime - Long.parseLong(timestamp)));
            return handlerResponse(response,ResponseCodeEnum.VERTIFY_CODE_ERROR);
        }
        //4、请求转发，调用模拟接口
        //解决chain.filter(exchange);异步操作问题
        return handlerBusiness(exchange,chain,accessKey);
    }


    /**
     * 过滤器执行优先级，数字越小优先级越大
     * @return
     */
    @Override
    public int getOrder() {
        return -1;
    }

    /**
     * 解决chain.filter(exchange);异步操作问题
     * @param exchange
     * @param chain
     * @return
     */
    private Mono<Void> handlerBusiness(ServerWebExchange exchange, GatewayFilterChain chain,String accessKey) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            HttpStatus statusCode = originalResponse.getStatusCode();
            if (statusCode != HttpStatus.OK) {
                return chain.filter(exchange);//降级处理返回数据
            }
            ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                @Override
                public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                    if (body instanceof Flux) {
                        Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                        return super.writeWith(fluxBody.buffer().map(dataBuffer -> {
                            //如果响应过大，会进行截断，出现乱码，然后看api DefaultDataBufferFactory
                            //有个join方法可以合并所有的流，乱码的问题解决
                            DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                            DataBuffer join = dataBufferFactory.join(dataBuffer);
                            byte[] content = new byte[join.readableByteCount()];
                            join.read(content);
                            //释放掉内存
                            DataBufferUtils.release(join);
                            //responseData就是下游系统返回的内容,可以查看修改
                            String responseData = new String(content, Charset.forName("UTF-8"));

                            String userBody = exchange.getRequest().getHeaders().getFirst("body");
                            JSONObject jsonObject = JSONUtil.parseObj(EncodeUtil.decodeBody(userBody));
                            String url = jsonObject.getStr("url");
                            //说明：为什么url需要加密？不加密的话比如：http://localhost:9000/interface/info作为参数请求
                            //openfeign远程调用会认为是再请求url地址，就会报错，因此采用这边加密，接口提供方解密的方式携带url参数。
                            String encodeUrl = EncodeUtil.encodeBody(url);
                            //根据接口地址url将调用次数+1
                            Integer interfaceId;
                            interfaceId = handlerIncrementInterfaceInvokeNum(encodeUrl,url);

                            //将当前用户调用接口的信息保存数据做记录
                            handlerSaveInterfaceUserInfo(interfaceId,accessKey);

                            //应用key额度-1
                            handlerDecreaseLines(accessKey);

                            //响应日志记录
                            log.info("********网关请求成功响应信息********\n");
                            log.info("网关响应内容:{}\n", responseData);
                            return bufferFactory.wrap(content);
                        }));
                    } else {
                        log.error("响应code异常:{}\n", getStatusCode());
                    }
                    return super.writeWith(body);
                }
            };
            return chain.filter(exchange.mutate().response(decoratedResponse).build());
        } catch (Exception e) {
            log.error("网关处理异常\n" + e);
            return chain.filter(exchange);
        }
    }

    /**
     * 将当前用户调用接口的信息保存数据做记录
     * @param interfaceId
     */
    private void handlerSaveInterfaceUserInfo(Integer interfaceId,String accessKey) {
        //获取key_name
        ResultData result = applicationKeyFeign.queryKeyNameByAccessKey(accessKey);
        String keyName = "";
        if(result.getCode() == 200){
            keyName = (String) result.getData();
        }
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        Integer userId = JwtUtil.parseToken(USER_TOKEN);
        userInterfaceInfo.setUserId(userId);
        userInterfaceInfo.setInterfaceId(interfaceId);
        userInterfaceInfo.setKeyName(keyName);
        ResultData resultData = userInterfaceInfoFeign.saveUserInterfaceInfo(userInterfaceInfo);
        if((Boolean) resultData.getData()){
            log.info("用户id{}调用接口id{}记录保存成功！！\n",userId,interfaceId);
        }else{
            log.info("用户id{}调用接口id{}记录保存失败！！\n",userId,interfaceId);
        }
    }

    /**
     * 应用key额度-1
     * @param accessKey
     */
    private void handlerDecreaseLines(String accessKey) {
        ResultData lines = applicationKeyFeign.decreaseLines(accessKey);
        if((Boolean) lines.getData()){
            log.info("应用key：{}额度-1成功！\n",accessKey);
        }else{
            log.info("应用key：{}额度-1失败！\n",accessKey);
        }
    }

    /**
     * 根据接口地址url将调用次数+1
     * @param encodeUrl
     * @return
     */
    private Integer handlerIncrementInterfaceInvokeNum(String encodeUrl,String url) {
        ResultData resultData = interfaceInfoFeign.incrementInterfaceCount(encodeUrl);
        if(resultData.getCode() == 200){
            Map<String,Object> data = (Map<String, Object>) resultData.getData();
            boolean increment = (boolean) data.get("increment");
            if(increment){
                log.info("接口{}调用次数+1成功！\n",url);
            }else{
                log.info("接口{}调用次数+1失败！\n",url);
            }
            return (Integer) data.get("interfaceId");
        }
        return 0;
    }

    /**
     * 封装一个规范的错误码
     * @param response
     */
    private Mono<Void> handlerResponse(ServerHttpResponse response,ResponseCodeEnum responseCodeEnum) {
        response.getHeaders().set("Content-Type", "application/json;charset=UTF-8");
        //原本使用hutool的json工具，但是一直乱码，后面改成了fastjson解决了
        DataBuffer buffer = response.bufferFactory().wrap(JSON.toJSONBytes(ResultData.fail(responseCodeEnum)));
        return response.writeWith(Mono.just(buffer));
    }

    /**
     * 打印请求日志
     * @param request
     */
    private void printLog(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String body = headers.getFirst("body");
        String sign = headers.getFirst("sign");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        //1、请求日志打印
        log.info("用户的accessKey为：{}\n",accessKey);
        log.info("请求体body为：{}\n",body);
        log.info("签名sign为：{}\n",sign);
        log.info("随机数nonce为：{}\n",nonce);
        log.info("当前请求时间为：{}\n",timestamp);
        log.info("请求方法为：{}\n",request.getMethod());
        log.info("请求路径为：{}\n",request.getPath());
        log.info("请求IP为：{}\n",request.getRemoteAddress().getHostName());
    }
}
