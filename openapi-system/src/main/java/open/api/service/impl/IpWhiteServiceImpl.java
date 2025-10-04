package open.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import open.api.mapper.ApplicationKeyMapper;
import open.api.model.entity.ApplicationKey;
import open.api.model.entity.IpWhite;
import open.api.mapper.IpWhiteMapper;
import open.api.service.IIpWhiteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * ip白名单表 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-05-15
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IpWhiteServiceImpl extends ServiceImpl<IpWhiteMapper, IpWhite> implements IIpWhiteService {

    private final ApplicationKeyMapper applicationKeyMapper;

    @Override
    public List<IpWhite> queryIpWhiteList(Integer keyId) {
        QueryWrapper<IpWhite> wrapper = new QueryWrapper<>();
        wrapper.eq("key_id", keyId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public void saveIpWhiteList(IpWhite ipWhite) {
        //将IP数据进行处理，因为用户可能一次性设置多个IP白名单
        String[] ips = ipWhite.getAddress().split(";");
        List<IpWhite> list = new ArrayList<IpWhite>();
        for (String ip : ips) {
            IpWhite ipw = new IpWhite();
            ipw.setKeyId(ipWhite.getKeyId());
            ipw.setKeyName(ipWhite.getKeyName());
            ipw.setAddress(ip);
            list.add(ipw);
        }
        this.saveBatch(list);
    }

    @Override
    public List<String> queryIpWhiteListByAccessKey(String accessKey) {
        log.info("应用key：{}正在查询设置的IP白名单...\n",accessKey);
        QueryWrapper<ApplicationKey> wrapper = new QueryWrapper<>();
        wrapper.eq("access_key", accessKey);
        ApplicationKey applicationKey = applicationKeyMapper.selectOne(wrapper);
        if(null != applicationKey){
            QueryWrapper<IpWhite> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("key_id",applicationKey.getId());
            List<IpWhite> ipWhites = baseMapper.selectList(queryWrapper);
            if(!CollectionUtils.isEmpty(ipWhites)){
                List<String> ipWhiteList = ipWhites.stream().map(IpWhite::getAddress).collect(Collectors.toList());
                log.info("应用key:{}查询到的IP白名单为：{}\n",accessKey,ipWhiteList);
                return ipWhiteList;
            }
        }
        return null;
    }
}
