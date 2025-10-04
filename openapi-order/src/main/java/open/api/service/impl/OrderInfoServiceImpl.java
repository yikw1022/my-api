package open.api.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import open.api.exception.SystemException;
import open.api.mapper.PlanMapper;
import open.api.model.dto.SaveOrderInfoDto;
import open.api.model.entity.OrderInfo;
import open.api.mapper.OrderInfoMapper;
import open.api.model.entity.Plan;
import open.api.model.enums.PayEnum;
import open.api.model.enums.QueryOrderEnum;
import open.api.model.vo.QueryOrderInfoVo;
import open.api.permission.user.UserFeign;
import open.api.response.ResponseCodeEnum;
import open.api.response.ResultData;
import open.api.service.IOrderInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import open.api.system.applicaionkey.ApplicationKeyFeign;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单信息表 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-05-05
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements IOrderInfoService {

    private final ApplicationKeyFeign applicationKeyFeign;
    private final UserFeign userFeign;
    private final PlanMapper planMapper;
    private final AlipayConfig alipayConfig;

    private final Object lock = new Object();

    private static final String TRADE_SUCCESS = "TRADE_SUCCESS";

    @Override
    public String saveOrderInfo(SaveOrderInfoDto orderInfoDto) throws SystemException {
        //查询是否有重复的订单号
        QueryWrapper<OrderInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id",orderInfoDto.getOrderId());
        OrderInfo orderInfo = baseMapper.selectOne(wrapper);
        if(null != orderInfo){
            throw new SystemException(ResponseCodeEnum.SYSTEM_ERROR);
        }
        //生成一个唯一的订单号
        String orderId = IdUtil.simpleUUID();
        log.info("正在保存订单号为{}的订单数据中...", orderId);
        OrderInfo saveOrderInfo = new OrderInfo();
        BeanUtils.copyProperties(orderInfoDto,saveOrderInfo);
        saveOrderInfo.setOrderId(orderId);
        baseMapper.insert(saveOrderInfo);
        log.info("成功保存订单号为{}的订单数据", orderId);
        return orderId;
    }

    private void updateOrderInfoStatusToSuccess(String orderId) {
        //修改状态为已支付
        OrderInfo orderInfo = updateStatusToSuccess(orderId);

        //将用户的accessKey所对应的额度修改，0.01元/次
        Long lines = (long) (orderInfo.getAmount() / 0.0001);
        ResultData resultData = applicationKeyFeign.updateLinesByAccessKey(orderInfo.getAccessKey(), lines);
        if(resultData.getCode() == 200){
            if((Boolean) resultData.getData()){
                log.info("id为{}的用户购买{}额度套餐,额度添加{}次成功....",orderInfo.getUserId(),orderInfo.getAmount(),lines);
            }else{
                log.info("id为{}的用户购买{}额度套餐,额度添加{}次失败....",orderInfo.getUserId(),orderInfo.getAmount(),lines);
            }
        }
    }

    @Override
    public List<QueryOrderInfoVo> queryOrderByUserId(Integer userId, Integer type,String orderId) {
        //查询当前用户是否拥有管理员权限
        ResultData resultData = userFeign.queryRoles(userId);
        if(resultData.getCode() == 200){
            List<String> roleNames = (List<String>) resultData.getData();
            if(roleNames.contains("管理员")){
                //有管理员权限
                if(QueryOrderEnum.All_DATA.getCode() == type.intValue()){
                    //获取全部订单数据
                    return queryAdminOrderData(type,orderId);
                }else if(QueryOrderEnum.HAVEDPAY_DATA.getCode() == type.intValue()){
                    //获取已支付订单数据
                    return queryAdminOrderData(type,orderId);
                }else if(QueryOrderEnum.UNPAY_DATA.getCode() == type.intValue()){
                    //获取未支付订单数据
                    return queryAdminOrderData(type,orderId);
                }
            }else{
                //无管理员权限
                if(QueryOrderEnum.All_DATA.getCode() == type.intValue()){
                    //获取全部订单数据
                    return queryOrderDataByUserId(userId,type,orderId);
                }else if(QueryOrderEnum.HAVEDPAY_DATA.getCode() == type.intValue()){
                    //获取已支付订单数据
                    return queryOrderDataByUserId(userId,type,orderId);
                }else if(QueryOrderEnum.UNPAY_DATA.getCode() == type.intValue()){
                    //获取未支付订单数据
                    return queryOrderDataByUserId(userId,type,orderId);
                }
            }
        }
        return null;
    }

    @Override
    public Map<String, Object> queryOrders() {
        //查询已支付总订单数
        int total = 0;
        QueryWrapper<OrderInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("status", PayEnum.HAVED_PAY.getCode());
        List<OrderInfo> orderInfos = baseMapper.selectList(wrapper);
        if(!CollectionUtils.isEmpty(orderInfos)){
            total = orderInfos.size();
        }
        //查询昨日新增订单数
        int yesterday = baseMapper.queryYesterdayOrders();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("total", total);
        map.put("yesterday", yesterday);
        return map;
    }

    @Override
    public QueryOrderInfoVo echoOrderInfo(Integer id) {
        OrderInfo orderInfo = baseMapper.selectById(id);
        QueryOrderInfoVo queryOrderInfoVo = new QueryOrderInfoVo();
        Plan plan = planMapper.selectById(orderInfo.getPlanId());
        BeanUtils.copyProperties(orderInfo, queryOrderInfoVo);
        queryOrderInfoVo.setPlanType(plan.getPrice() + plan.getUnit());
        return queryOrderInfoVo;
    }

    @Override
    public void updateOrderInfo(QueryOrderInfoVo vo) {
        OrderInfo orderInfo = baseMapper.selectById(vo.getId());
        BeanUtils.copyProperties(vo, orderInfo);
        baseMapper.updateById(orderInfo);
    }

    private OrderInfo updateStatusToSuccess(String orderId) {
        QueryWrapper<OrderInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id",orderId);
        OrderInfo orderInfo = baseMapper.selectOne(wrapper);
        if(null != orderInfo){
            orderInfo.setStatus(PayEnum.HAVED_PAY.getCode());
            baseMapper.updateById(orderInfo);
        }
        return orderInfo;
    }

    @Override
    public boolean queryOrderInfoStatus(String orderId, HttpServletRequest res) {
        synchronized(lock) {
            boolean isSuccess = false;

            try {
                //使用支付宝的SDK查询订单状态
                AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig);
                AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
                AlipayTradeQueryModel model = new AlipayTradeQueryModel();

                model.setOutTradeNo(orderId);
                request.setBizModel(model);
                AlipayTradeQueryResponse response = alipayClient.execute(request);
                if (response.isSuccess()) {
                    if (TRADE_SUCCESS.equalsIgnoreCase(response.getTradeStatus())) {
                        log.info("订单号为：{}的订单支付成功", orderId);
                        isSuccess = true;
                        // TODO：优化建议：异步更新数据库订单状态已支付以及修改额度数值
                        updateOrderInfoStatusToSuccess(orderId);

                        return isSuccess;
                    } else {
                        log.info("订单号为：{}的订单支付失败，状态为：{}", orderId, response.getTradeStatus());
                        return isSuccess;
                    }
                } else {
                    log.info("订单号为：{}的查询请求失败", orderId);
                    return isSuccess;
                }
            } catch (AlipayApiException e) {
                log.error("支付宝发起支付请求异常", e);
                throw new RuntimeException("处理支付宝请求时发生异常", e);
            } finally {
                lock.notifyAll();
            }
        }
    }

    @Override
    public void testSaveOrderInfo(SaveOrderInfoDto orderInfoDto) throws SystemException {
        //查询是否有重复的订单号
        QueryWrapper<OrderInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id",orderInfoDto.getOrderId());
        OrderInfo orderInfo = baseMapper.selectOne(wrapper);
        if(null != orderInfo){
            throw new SystemException(ResponseCodeEnum.SYSTEM_ERROR);
        }
        //生成一个唯一的订单号
        String orderId = IdUtil.simpleUUID();
        log.info("正在保存订单号为{}的订单数据中...", orderId);
        OrderInfo saveOrderInfo = new OrderInfo();
        BeanUtils.copyProperties(orderInfoDto,saveOrderInfo);
        saveOrderInfo.setOrderId(orderId);
        baseMapper.insert(saveOrderInfo);
        log.info("成功保存订单号为{}的订单数据", orderId);

        //修改订单状态为已支付
        updateOrderInfoStatusToSuccess(orderId);
        log.info("订单号为{}的订单状态修改为已支付成功", orderId);
    }

    private List<QueryOrderInfoVo> queryOrderDataByUserId(Integer userId, Integer type,String orderId) {
        QueryWrapper<OrderInfo> wrapper = new QueryWrapper<>();
        wrapper.like(!StringUtils.isBlank(orderId.trim()),"order_id",orderId);
        wrapper.eq("user_id", userId);
        wrapper.orderByDesc("create_time");
        if(QueryOrderEnum.HAVEDPAY_DATA.getCode() == type.intValue()){
            wrapper.eq("status", PayEnum.HAVED_PAY.getCode());
        }else if(QueryOrderEnum.UNPAY_DATA.getCode() == type.intValue()){
            wrapper.eq("status", PayEnum.NOT_PAY.getCode());
        }
        List<OrderInfo> orderInfos = baseMapper.selectList(wrapper);
        if(!CollectionUtils.isEmpty(orderInfos)){
            List<QueryOrderInfoVo> list = new ArrayList<>();
            orderInfos.stream().forEach(vo ->{
                Plan plan = planMapper.selectById(vo.getPlanId());
                QueryOrderInfoVo queryOrderInfoVo = new QueryOrderInfoVo();
                BeanUtils.copyProperties(vo, queryOrderInfoVo);
                queryOrderInfoVo.setPlanType(plan.getPrice() + plan.getUnit());
                list.add(queryOrderInfoVo);
            });
            return list;
        }
        return null;
    }

    private List<QueryOrderInfoVo> queryAdminOrderData(Integer type,String orderId) {
        QueryWrapper<OrderInfo> wrapper = new QueryWrapper<>();
        wrapper.like(!StringUtils.isBlank(orderId),"order_id",orderId);
        wrapper.orderByDesc("create_time");
        if(QueryOrderEnum.HAVEDPAY_DATA.getCode() == type.intValue()){
            wrapper.eq("status", PayEnum.HAVED_PAY.getCode());
        }else if(QueryOrderEnum.UNPAY_DATA.getCode() == type.intValue()){
            wrapper.eq("status", PayEnum.NOT_PAY.getCode());
        }
        List<OrderInfo> orderInfos = baseMapper.selectList(wrapper);
        if(!CollectionUtils.isEmpty(orderInfos)){
            List<QueryOrderInfoVo> list = new ArrayList<>();
            orderInfos.stream().forEach(vo ->{
                Plan plan = planMapper.selectById(vo.getPlanId());
                QueryOrderInfoVo queryOrderInfoVo = new QueryOrderInfoVo();
                BeanUtils.copyProperties(vo, queryOrderInfoVo);
                queryOrderInfoVo.setPlanType(plan.getPrice() + plan.getUnit());
                list.add(queryOrderInfoVo);
            });
            return list;
        }
        return null;
    }
}
