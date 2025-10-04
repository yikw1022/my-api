package open.api.service;

import open.api.model.entity.UserInterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import open.api.model.vo.InvokeDetailVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户调用接口次数信息表 服务类
 * </p>
 *
 * @author author
 * @since 2024-05-08
 */
public interface IUserInterfaceInfoService extends IService<UserInterfaceInfo> {

    /**
     * 保存用户调用接口的信息
     * @param interfaceInfo
     * @return
     */
    boolean saveUserInterfaceInfo(UserInterfaceInfo interfaceInfo);

    /**
     * 获取用户的今日调用接口次数
     * @param userId
     * @return
     */
    Map<String,Object> queryInvokeCountByUserId(Integer userId);

    /**
     * 获取当前用户的首页key调用接口数据详情
     * @param userId
     * @return
     */
    List<InvokeDetailVo> queryInvokeDetailByUserId(Integer userId);

    /**
     * 获取普通用户的流量详情数据(24小时)
     * @param userId
     * @return
     */
    List<InvokeDetailVo> queryInterfaceTrafficByUserId(Integer userId);

    /**
     * 获取普通用户的流量详情数据(30天)
     * @param userId
     * @return
     */
    List<InvokeDetailVo> queryThirtyDayTrafficByUserId(Integer userId);

    /**
     * 获取普通用户的流量详情数据(6个月)
     * @param userId
     * @return
     */
    List<InvokeDetailVo> querySixMonthTrafficByUserId(Integer userId);

    /**
     * 根据key_name删除调用接口记录
     * @param keyName
     */
    void deleteBykeyName(String keyName);
}
