package open.api.service;

import open.api.exception.SystemException;
import open.api.model.entity.Application;
import com.baomidou.mybatisplus.extension.service.IService;
import open.api.model.vo.ApplicationCreateVo;
import open.api.model.vo.ApplicationListVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 应用管理表 服务类
 * </p>
 *
 * @author author
 * @since 2024-04-27
 */
public interface IApplicationService extends IService<Application> {

    /**
     * 创建新应用
     * @param createVo
     */
    void saveApplication(ApplicationCreateVo createVo) throws SystemException;

    /**
     * 获取用户应用数据
     * @param userId
     * @return
     */
    List<ApplicationListVo> queryApplicationList(Integer userId);

    /**
     * 修改应用
     * @param createVo
     */
    void editApplication(ApplicationCreateVo createVo) throws SystemException;

    /**
     * 修改应用状态
     * @param id
     */
    void updateAppStatus(Integer id);

    /**
     * 首页查询创建应用数据
     * @return
     */
    Map<String, Object> queryApplications();

    /**
     * 首页根据用户Id查询创建应用数据
     * @return
     */
    int queryApplicationsByUserId(Integer userId);
}
