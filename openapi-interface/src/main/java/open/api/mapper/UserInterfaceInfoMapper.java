package open.api.mapper;

import open.api.model.entity.UserInterfaceInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户调用接口次数信息表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-05-08
 */
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    /**
     * 获取当前用户的今日调用接口数
     * @param userId
     * @return
     */
    @Select("SELECT COUNT(*) FROM user_interface_info WHERE DATE(create_time) = CURDATE() AND user_id = #{userId}")
    Long getInvokeInterfaceNumberToday(Integer userId);

    /**
     * 获取当前用户的总调用接口数
     * @param userId
     * @return
     */
    @Select("SELECT COUNT(*) FROM user_interface_info WHERE user_id = #{userId}")
    Long getInvokeInterfaceNumberTotal(Integer userId);

    List<Long> getInvokeCountByKeyNameAndDate(@Param("keyName") String keyName);

    List<String> getInvokeDateByKeyName(@Param("keyName") String keyName);

    /**
     * 获取当前用户在24小时内的流量详情数据
     * @param keyName
     * @return
     */
    List<Long> findOneDayCountList(@Param("keyName") String keyName);

    /**
     * 获取普通用户的流量详情数据(24小时)
     * @param keyName
     * @return
     */
    List<String> findOneDayTimeList(@Param("keyName") String keyName);

    /**
     * 获取普通用户的流量详情数据(30天)
     * @param keyName
     * @return
     */
    List<Long> findThirtyDayCountList(@Param("keyName") String keyName);

    /**
     * 获取普通用户的流量详情数据(30天)
     * @param keyName
     * @return
     */
    List<String> findThirtyDayTimeList(@Param("keyName") String keyName);

    /**
     * 获取普通用户的流量详情数据(6个月)
     * @param keyName
     * @return
     */
    List<Long> findSixMonthCountList(@Param("keyName") String keyName);

    /**
     * 获取普通用户的流量详情数据(6个月)
     * @param keyName
     * @return
     */
    List<String> findSixMonthTimeList(@Param("keyName") String keyName);
}
