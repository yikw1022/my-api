package open.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sun.org.apache.bcel.internal.generic.NEW;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import open.api.exception.SystemException;
import open.api.mapper.ApplicationMapper;
import open.api.mapper.IpWhiteMapper;
import open.api.model.entity.Application;
import open.api.model.entity.ApplicationKey;
import open.api.mapper.ApplicationKeyMapper;
import open.api.model.entity.IpWhite;
import open.api.model.enums.ApplicationStatusEnum;
import open.api.model.vo.ApplicationKeyAddVo;
import open.api.response.ResponseCodeEnum;
import open.api.service.IApplicationKeyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import open.api.userinterfaceinfo.UserInterfaceInfoFeign;
import open.api.utils.SecureUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 应用与key联表 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-04-27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationKeyServiceImpl extends ServiceImpl<ApplicationKeyMapper, ApplicationKey> implements IApplicationKeyService {

    private final ApplicationMapper applicationMapper;

    private final IpWhiteMapper ipWhiteMapper;

    private final UserInterfaceInfoFeign userInterfaceInfoFeign;

    @Override
    public void saveApplicationKey(ApplicationKeyAddVo keyAddVo) throws SystemException {
        //每一个应用不超过10个key
        log.info("应用key名称{}正在保存...", keyAddVo.getKeyName());
        QueryWrapper<ApplicationKey> wrapper = new QueryWrapper<>();
        wrapper.eq("application_id", keyAddVo.getApplicationId());
        List<ApplicationKey> applicationKeyList = baseMapper.selectList(wrapper);
        if(!CollectionUtils.isEmpty(applicationKeyList)){
            if(10 < applicationKeyList.size()){
                throw new SystemException(ResponseCodeEnum.KEY_EXCEED);
            }
        }
        //key_name不能重复
        QueryWrapper<ApplicationKey> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("key_name", keyAddVo.getKeyName());
        List<ApplicationKey> applicationKeys = baseMapper.selectList(queryWrapper);
        if(!CollectionUtils.isEmpty(applicationKeys)){
            throw new SystemException(ResponseCodeEnum.KEY_NAME_EXITS);
        }
        //accessKey和secretKey生成
        ApplicationKey applicationKey = new ApplicationKey();
        BeanUtils.copyProperties(keyAddVo,applicationKey);
        applicationKey.setAccessKey(SecureUtils.generateAccessKey(keyAddVo.getKeyName()));
        applicationKey.setSecretKey(SecureUtils.generateSecretKey(keyAddVo.getKeyName()));
        this.save(applicationKey);
        log.info("应用key名称{}保存成功！", keyAddVo.getKeyName());
    }

    @Override
    public List<ApplicationKey> queryKeysByApplicationId(Integer applicationId) {
        QueryWrapper<ApplicationKey> wrapper = new QueryWrapper<>();
        wrapper.eq("application_id",applicationId);
        wrapper.orderByDesc("create_time");
        List<ApplicationKey> applicationKeys = baseMapper.selectList(wrapper);
        if(!CollectionUtils.isEmpty(applicationKeys)){
            return applicationKeys;
        }
        return null;
    }

    @Override
    public String queryKeyByAccessKey(String accessKey) {
        QueryWrapper<ApplicationKey> wrapper = new QueryWrapper<>();
        wrapper.eq("access_key",accessKey);
        ApplicationKey applicationKey = baseMapper.selectOne(wrapper);
        if(null != applicationKey){
            return applicationKey.getSecretKey();
        }
        return null;
    }

    @Override
    public boolean queryApplicationStatus(String accessKey) {
        QueryWrapper<ApplicationKey> wrapper = new QueryWrapper<>();
        wrapper.eq("access_key", accessKey);
        ApplicationKey applicationKey = baseMapper.selectOne(wrapper);
        if(null != applicationKey){
            Application application = applicationMapper.selectById(applicationKey.getApplicationId());
            if(application.getStatus().intValue() == ApplicationStatusEnum.OPEN.getCode()){
                return false;
            }
            return true;
        }
        return true;
    }

    @Override
    public boolean decreaseLines(String accessKey) throws SystemException {
        QueryWrapper<ApplicationKey> wrapper = new QueryWrapper<>();
        wrapper.eq("access_key", accessKey);
        ApplicationKey applicationKey = baseMapper.selectOne(wrapper);
        if(null != applicationKey){
            if(applicationKey.getLine() > 0){
                applicationKey.setLine(applicationKey.getLine() - 1);
                int processed = baseMapper.updateById(applicationKey);
                if(processed > 0){
                    return true;
                }else {
                    return false;
                }
            }else{
                throw new SystemException(ResponseCodeEnum.LIINES_INSUFFICIENT);
            }
        }
        return false;
    }

    @Override
    public boolean queryAccessKeyLines(String accessKey) {
        QueryWrapper<ApplicationKey> wrapper = new QueryWrapper<>();
        wrapper.eq("access_key", accessKey);
        ApplicationKey applicationKey = baseMapper.selectOne(wrapper);
        if(null != applicationKey){
            if(applicationKey.getLine() <= 0){
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public List<ApplicationKey> queryKeysByUserId(Integer userId) {
        QueryWrapper<Application> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        List<Application> applications = applicationMapper.selectList(wrapper);
        if(!CollectionUtils.isEmpty(applications)){
            List<Integer> appIds = applications.stream().map(Application::getId).collect(Collectors.toList());
            List<ApplicationKey> list = new ArrayList<>();
            appIds.stream().forEach(appId -> {
                QueryWrapper<ApplicationKey> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("application_id", appId);
                List<ApplicationKey> applicationKeys = baseMapper.selectList(queryWrapper);
                if(!CollectionUtils.isEmpty(applicationKeys)){
                    applicationKeys.stream().forEach(item -> {
                        list.add(item);
                    });
                }
            });
            return list;
        }
        return null;
    }

    @Override
    public boolean updateLinesByAccessKey(String accessKey,Long lines) {
        QueryWrapper<ApplicationKey> wrapper = new QueryWrapper<>();
        wrapper.eq("access_key", accessKey);
        ApplicationKey applicationKey = baseMapper.selectOne(wrapper);
        if(null != applicationKey) {
            applicationKey.setLine(applicationKey.getLine() + lines);
            int processed = baseMapper.updateById(applicationKey);
            if(processed > 0){
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public int queryApplicationKeysByUserId(Integer userId) {
        QueryWrapper<Application> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<Application> applicationList = applicationMapper.selectList(queryWrapper);
        if(!CollectionUtils.isEmpty(applicationList)){
            List<Integer> appIds = applicationList.stream().map(Application::getId).collect(Collectors.toList());
            int count = 0;
            for (Integer appId : appIds) {
                QueryWrapper<ApplicationKey> wrapper = new QueryWrapper<>();
                wrapper.eq("application_id", appId);
                List<ApplicationKey> applicationKeys = baseMapper.selectList(wrapper);
                count += applicationKeys.size();
            }
            return count;
        }
        return 0;
    }

    @Override
    public String queryKeyNameByAccessKey(String accessKey) {
        QueryWrapper<ApplicationKey> wrapper = new QueryWrapper<>();
        wrapper.eq("access_key", accessKey);
        ApplicationKey applicationKey = baseMapper.selectOne(wrapper);
        if(null != applicationKey){
            return applicationKey.getKeyName();
        }
        return null;
    }

    @Override
    public List<String> queryKeyNameByUserId(Integer userId) {
        QueryWrapper<Application> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        List<Application> applications = applicationMapper.selectList(wrapper);
        if(!CollectionUtils.isEmpty(applications)){
            List<Integer> appIds = applications.stream().map(Application::getId).collect(Collectors.toList());
            List<String> list = new ArrayList<>();
            appIds.stream().forEach(appId -> {
                QueryWrapper<ApplicationKey> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("application_id", appId);
                List<ApplicationKey> applicationKeys = baseMapper.selectList(queryWrapper);
                if(!CollectionUtils.isEmpty(applicationKeys)){
                    applicationKeys.stream().forEach(applicationKey -> {
                        list.add(applicationKey.getKeyName());
                    });
                }
            });
            return list;
        }
        return null;
    }

    @Override
    public void deleteKeyById(Integer id) {
        ApplicationKey applicationKey = baseMapper.selectById(id);
        //根据key_name删除user_interface_info表中的接口调用记录
        userInterfaceInfoFeign.deleteBykeyName(applicationKey.getKeyName());
        QueryWrapper<IpWhite> wrapper = new QueryWrapper<>();
        wrapper.eq("key_id",id);
        ipWhiteMapper.delete(wrapper);
        baseMapper.deleteById(id);
    }
}
