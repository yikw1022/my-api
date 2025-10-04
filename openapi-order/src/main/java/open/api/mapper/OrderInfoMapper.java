package open.api.mapper;

import open.api.model.entity.OrderInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 订单信息表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-05-05
 */
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    /**
     * 查询昨日新增订单数
     * @return
     */
    @Select("SELECT COUNT(*) FROM order_info WHERE DATE(create_time) = CURDATE() - INTERVAL 1 DAY")
    int queryYesterdayOrders();
}
