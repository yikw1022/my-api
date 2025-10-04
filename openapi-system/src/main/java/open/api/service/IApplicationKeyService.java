package open.api.service;

import open.api.exception.SystemException;
import open.api.model.entity.ApplicationKey;
import com.baomidou.mybatisplus.extension.service.IService;
import open.api.model.vo.ApplicationKeyAddVo;

import java.util.List;

/**
 * <p>
 * 应用与key联表 服务类
 * </p>
 *
 * @author author
 * @since 2024-04-27
 */
public interface IApplicationKeyService extends IService<ApplicationKey> {

    /**
     * 添加key保存
     * @param keyAddVo
     */
    void saveApplicationKey(ApplicationKeyAddVo keyAddVo) throws SystemException;

    /**
     * 根据应用Id查询Key数据
     * @param applicationId
     * @return
     */
    List<ApplicationKey> queryKeysByApplicationId(Integer applicationId);

    /**
     * 根据accessKey获取对应的secretKey
     * @param accessKey
     * @return
     */
    String queryKeyByAccessKey(String accessKey);

    /**
     * 根据accessKey查询所属应用是否处于开启状态
     * @param accessKey
     * @return
     */
    boolean queryApplicationStatus(String accessKey);

    /**
     * 根据accessKey将额度-1
     * @param accessKey
     * @return
     */
    boolean decreaseLines(String accessKey) throws SystemException;

    /**
     * 根据accessKey查询额度
     * @param accessKey
     * @return
     */
    boolean queryAccessKeyLines(String accessKey);

    /**
     * 根据用户Id查询Key数据
     * @param userId
     * @return
     */
    List<ApplicationKey> queryKeysByUserId(Integer userId);

    /**
     * 用户购买额度后根据accessKey修改额度
     * @param accessKey
     * @return
     */
    boolean updateLinesByAccessKey(String accessKey,Long lines);

    /**
     * 首页根据用户Id查询创建应用Key数据
     * @param userId
     * @return
     */
    int queryApplicationKeysByUserId(Integer userId);

    /**
     * 根据accessKey获取应用key名称
     * @param accessKey
     * @return
     */
    String queryKeyNameByAccessKey(String accessKey);

    /**
     * 根据用户Id查询key名称
     * @param userId
     * @return
     */
    List<String> queryKeyNameByUserId(Integer userId);

    /**
     * 删除key
     * @param id
     */
    void deleteKeyById(Integer id);
}
