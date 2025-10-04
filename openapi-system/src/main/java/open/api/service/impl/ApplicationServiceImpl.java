package open.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import open.api.exception.SystemException;
import open.api.mapper.ApplicationKeyMapper;
import open.api.model.entity.Application;
import open.api.mapper.ApplicationMapper;
import open.api.model.entity.ApplicationKey;
import open.api.model.entity.User;
import open.api.model.enums.ApplicationStatusEnum;
import open.api.model.vo.ApplicationCreateVo;
import open.api.model.vo.ApplicationListVo;
import open.api.permission.user.UserFeign;
import open.api.response.ResponseCodeEnum;
import open.api.response.ResultData;
import open.api.service.IApplicationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 应用管理表 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-04-27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl extends ServiceImpl<ApplicationMapper, Application> implements IApplicationService {

    private final UserFeign userFeign;


    @Override
    public void saveApplication(ApplicationCreateVo createVo) throws SystemException {
        //查询应用名称是否重名
        log.info("应用{}正在创建...", createVo.getName());
        QueryWrapper<Application> wrapper = new QueryWrapper<>();
        wrapper.eq("name", createVo.getName());
        Application selectOne = baseMapper.selectOne(wrapper);
        if(null != selectOne){
            throw new SystemException(ResponseCodeEnum.APP_NAME_EXITS);
        }
        Application application = new Application();
        BeanUtils.copyProperties(createVo, application);
        baseMapper.insert(application);
        log.info("应用{}创建成功！", createVo.getName());
    }

    @Override
    public List<ApplicationListVo> queryApplicationList(Integer userId) {
        //如果是管理员则查询所有，非管理员查询自己的
        ResultData resultData = userFeign.queryRoles(userId);
        List<String> roleList = (List<String>) resultData.getData();
        if(!CollectionUtils.isEmpty(roleList)){
            if(roleList.contains("管理员")){
                QueryWrapper<Application> wrapper = new QueryWrapper<>();
                wrapper.orderByDesc("create_time");
                List<Application> applications = baseMapper.selectList(wrapper);
                if(!CollectionUtils.isEmpty(applications)){
                    return this.packageData(applications);
                }
                return null;
            }else{
                //非管理员查询应用数据
                QueryWrapper<Application> wrapper = new QueryWrapper<>();
                wrapper.eq("user_id",userId);
                wrapper.orderByDesc("create_time");
                List<Application> applications = baseMapper.selectList(wrapper);
                if(!CollectionUtils.isEmpty(applications)){
                    return this.packageData(applications);
                }
                return null;
            }
        }
        return null;
    }

    private List<ApplicationListVo> packageData(List<Application> applications) {
        List<ApplicationListVo> list = new ArrayList<>();
        applications.stream().forEach(item -> {
            ApplicationListVo applicationListVo = new ApplicationListVo();
            BeanUtils.copyProperties(item,applicationListVo);
            if(item.getStatus().intValue() == ApplicationStatusEnum.OPEN.getCode()){
                applicationListVo.setStatus(true);
            }else{
                applicationListVo.setStatus(false);
            }
            list.add(applicationListVo);
        });
        return list;
    }

    @Override
    public void editApplication(ApplicationCreateVo createVo) throws SystemException {
        //查询应用名称是否重名
        log.info("应用{}正在修改...", createVo.getName());
        QueryWrapper<Application> wrapper = new QueryWrapper<>();
        wrapper.eq("name", createVo.getName());
        Application application = baseMapper.selectOne(wrapper);
        if(null != application){
            throw new SystemException(ResponseCodeEnum.APP_NAME_EXITS);
        }
        Application selectById = baseMapper.selectById(createVo.getId());
        selectById.setName(createVo.getName());
        baseMapper.updateById(selectById);
        log.info("应用{}修改成功！", createVo.getName());
    }

    @Override
    public void updateAppStatus(Integer id) {
        Application application = baseMapper.selectById(id);
        if(application.getStatus().intValue() == ApplicationStatusEnum.OPEN.getCode()){
            log.info("应用{}的状态被修改为禁用...", application.getName());
            application.setStatus(ApplicationStatusEnum.FORBIDDEN.getCode());
        }else{
            log.info("应用{}的状态被修改为开启...", application.getName());
            application.setStatus(ApplicationStatusEnum.OPEN.getCode());
        }
        baseMapper.updateById(application);
    }

    @Override
    public Map<String, Object> queryApplications() {
        //查询总应用数
        int total = 0;
        List<Application> applications = baseMapper.selectList(null);
        if(!CollectionUtils.isEmpty(applications)){
            total = applications.size();
        }
        //查询昨日新增应用数
        int yesterday = baseMapper.queryYesterdayApplications();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("total", total);
        map.put("yesterday", yesterday);
        return map;
    }

    @Override
    public int queryApplicationsByUserId(Integer userId) {
        QueryWrapper<Application> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        List<Application> applications = baseMapper.selectList(wrapper);
        if(!CollectionUtils.isEmpty(applications)){
            return applications.size();
        }
        return 0;
    }
}
