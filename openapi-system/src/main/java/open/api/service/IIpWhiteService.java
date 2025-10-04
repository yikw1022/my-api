package open.api.service;

import open.api.model.entity.IpWhite;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * ip白名单表 服务类
 * </p>
 *
 * @author author
 * @since 2024-05-15
 */
public interface IIpWhiteService extends IService<IpWhite> {

    /**
     * 根据keyId的查询IP白名单数据
     * @param keyId
     * @return
     */
    List<IpWhite> queryIpWhiteList(Integer keyId);

    /**
     * 保存IP白名单数据
     * @param ipWhite
     */
    void saveIpWhiteList(IpWhite ipWhite);

    /**
     * 根据应用key获取设置的IP白名单
     * @param accessKey
     * @return
     */
    List<String> queryIpWhiteListByAccessKey(String accessKey);
}
