package open.api.service;

import open.api.exception.SystemException;
import open.api.model.dto.SaveOrderInfoDto;
import open.api.model.entity.OrderInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import open.api.model.vo.QueryOrderInfoVo;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单信息表 服务类
 * </p>
 *
 * @author author
 * @since 2024-05-05
 */
public interface IOrderInfoService extends IService<OrderInfo> {

    /**
     * 保存订单信息，获取订单Id
     * @param orderInfoDto
     * @return
     */
    String saveOrderInfo(SaveOrderInfoDto orderInfoDto) throws SystemException;

    /**
     * 根据用户id获取订单数据
     * @param userId
     * @param type
     * @return
     */
    List<QueryOrderInfoVo> queryOrderByUserId(Integer userId, Integer type,String orderId);

    /**
     * 首页查询订单数据
     * @return
     */
    Map<String, Object> queryOrders();

    /**
     * 回显订单数据
     * @param id
     * @return
     */
    QueryOrderInfoVo echoOrderInfo(Integer id);

    /**
     * 修改订单数据
     * @param vo
     */
    void updateOrderInfo(QueryOrderInfoVo vo);

    /**
     * 查询订单支付状态
     * @param orderId
     * @param request
     * @return
     */
    boolean queryOrderInfoStatus(String orderId, HttpServletRequest request);

    /**
     * 测试保存订单信息，获取订单Id
     * @param orderInfoDto
     * @return
     */
    void testSaveOrderInfo(@Valid SaveOrderInfoDto orderInfoDto) throws SystemException;
}
