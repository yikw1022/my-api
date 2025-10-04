package open.api.service.impl;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openapi.client.OpenClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import open.api.exception.SystemException;
import open.api.mapper.UserInterfaceInfoMapper;
import open.api.model.dto.*;
import open.api.model.entity.*;
import open.api.mapper.InterfaceInfoMapper;
import open.api.model.enums.ApplicationStatusEnum;
import open.api.model.enums.MenuStatusEnum;
import open.api.model.vo.InterfaceInfoSaveVo;
import open.api.model.vo.InterfaceInfoVo;
import open.api.permission.user.UserFeign;
import open.api.response.ResponseCodeEnum;
import open.api.response.ResultData;
import open.api.service.IInterfaceInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import open.api.system.applicaionkey.ApplicationKeyFeign;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 接口信息表 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-04-26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo> implements IInterfaceInfoService {

    private final OpenClient openClient;

    private final ApplicationKeyFeign applicationKeyFeign;

    private final UserFeign userFeign;

    private final UserInterfaceInfoMapper userInterfaceInfoMapper;

    private Map<String,String> interfaceNames;
    private Map<String,String> interfacePublishNames;

    /**
     * 初始化所有未发布的接口
     * @return
     */
    public Map<String,String> getInterfaceName(){
        QueryWrapper<InterfaceInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("status",ApplicationStatusEnum.FORBIDDEN.getCode());
        wrapper.orderByDesc("create_time");
        List<InterfaceInfo> interfaceInfoList = this.list(wrapper);
        if(!CollectionUtils.isEmpty(interfaceInfoList)){
            Map<String,String> map = new HashMap<>();
            interfaceInfoList.stream().forEach(item -> {
                map.put(item.getUrl(),item.getName());
            });
            return map;
        }
        return null;
    }

    /**
     * 初始化所有的接口
     * @return
     */
    public Map<String,String> getAllInterface(){
        QueryWrapper<InterfaceInfo> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        List<InterfaceInfo> interfaceInfoList = this.list(wrapper);
        if(!CollectionUtils.isEmpty(interfaceInfoList)){
            Map<String,String> map = new HashMap<>();
            interfaceInfoList.stream().forEach(item -> {
                map.put(item.getUrl(),item.getName());
            });
            return map;
        }
        return null;
    }

    @Override
    public void saveInterfaceInfo(InterfaceInfoSaveDto saveDto) throws SystemException {
        //查询url或者接口名称是否重复
        log.info("{}接口正在保存...",saveDto.getName());
        QueryWrapper<InterfaceInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("url",saveDto.getUrl())
                .or()
                .eq("name",saveDto.getName());
        InterfaceInfo selectOne = baseMapper.selectOne(wrapper);
        if(null != selectOne){
            throw new SystemException(ResponseCodeEnum.URL_EXITS);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(saveDto, interfaceInfo);
        this.save(interfaceInfo);
        log.info("{}接口保存成功...",saveDto.getName());
    }

    @Override
    public Map<String, Object> queryInterfaceList(Integer pageNo, Integer pageSize, InterfaceInfoSearchDto searchDto) {
        Page<InterfaceInfo> pageInfo = new Page<InterfaceInfo>(pageNo, pageSize);
        searchDto.setDescription(searchDto.getDescription().trim());
        searchDto.setName(searchDto.getName().trim());
        searchDto.setMethod(searchDto.getMethod().trim());
        QueryWrapper<InterfaceInfo> wrapper = new QueryWrapper<>();
        //模糊查询
        wrapper.like(!StringUtils.isBlank(searchDto.getDescription()),"description",searchDto.getDescription());
        wrapper.like(!StringUtils.isBlank(searchDto.getMethod()),"method",searchDto.getMethod());
        wrapper.like(!StringUtils.isBlank(searchDto.getName()),"name",searchDto.getName());
        //查询接口状态为在线状态的所有接口
        wrapper.eq("status",ApplicationStatusEnum.OPEN.getCode());
        wrapper.orderByDesc("create_time");

        Page<InterfaceInfo> interfaceInfoPage = baseMapper.selectPage(pageInfo, wrapper);
        if(interfaceInfoPage != null){
            List<InterfaceInfo> interfaceInfoList = interfaceInfoPage.getRecords();
            List<InterfaceInfoVo> list = new ArrayList<>();
            if(!CollectionUtils.isEmpty(interfaceInfoList)){
                interfaceInfoList.stream().forEach(interfaceInfo -> {
                    InterfaceInfoVo interfaceInfoSaveDto = new InterfaceInfoVo();
                    BeanUtils.copyProperties(interfaceInfo, interfaceInfoSaveDto);
                    if(interfaceInfo.getStatus().intValue() == MenuStatusEnum.OPEN.getCode()){
                        interfaceInfoSaveDto.setStatus(true);
                    }else{
                        interfaceInfoSaveDto.setStatus(false);
                    }
                    list.add(interfaceInfoSaveDto);
                });
                Map<String,Object> map = new HashMap<>();
                map.put("data", list);
                map.put("total", interfaceInfoPage.getTotal());
                return map;
            }
        }
        return null;
    }

    @Override
    public ResultData publishInterface(PublishDto publishDto) throws SystemException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        log.info("{}接口url正在进行发布...",publishDto.getUrl());
        //1、校验接口是否存在
        InterfaceInfo interfaceInfo = baseMapper.selectById(publishDto.getId());
        if (null == interfaceInfo){
            throw new SystemException(ResponseCodeEnum.INTERFACE_NOT_EXITS);
        }
        //获取所有未发布的接口
        interfaceNames = getInterfaceName();
        //两种情况：①、有请求参数；②、无请求参数
        //根据请求地址获取接口名称
        String methodName = interfaceNames.get(publishDto.getUrl());
        if(StringUtils.isBlank(publishDto.getParams())){
            //②、无请求参数
            return commonInterfaceRequestNoParams(interfaceInfo,publishDto,methodName);
        }else{
            //①、有请求参数
            return commonInterfaceRequestWithParams(interfaceInfo,publishDto,methodName);
        }
    }

    /**
     * 任何无请求参数的接口调用共用逻辑，
     * 这样就实现了根据请求地址url动态请求接口
     * @param interfaceInfo
     * @param publishDto
     */
    private ResultData commonInterfaceRequestNoParams(InterfaceInfo interfaceInfo, PublishDto publishDto,String methodName) {
        try {
            // 获取OpenClient类的Method对象
            log.info("无请求参数的接口{}正在进行发布测试....",methodName);
            Method method = OpenClient.class.getMethod(methodName);
            // 调用方法并传递参数
            String result = (String) method.invoke(openClient);
            if (result instanceof String) {
                return handleUpdateInterfaceStatus(interfaceInfo,result);
            } else {
                throw new SystemException(ResponseCodeEnum.FAIL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("无请求参数接口{}发布过程接口测试失败........",methodName);
            return ResultData.fail(ResponseCodeEnum.INTERFACE_NOT_PASS);
        }
    }

    private ResultData commonInterfaceRequestWithParams(InterfaceInfo interfaceInfo, PublishDto publishDto,String methodName){
        log.info("有请求参数的接口{}正在进行发布测试....",methodName);
        log.info("请求参数为【{}】",publishDto.getParams());
        //①、有请求参数
        JSONObject json = JSON.parseObject(publishDto.getParams());
        //json数据如何转成数组
        String[] userParams = json.keySet().toArray(new String[json.size()]);
        Map<String,Object> params = new HashMap<>();
        for (String key : userParams) {
            //将用户的请求参数用map封装
            params.put(key,json.getString(key));
        }
        try {
            //根据请求参数获取参数类型
            Class[] parameterType = getParameterType(params);
            //获取实际的请求参数
            Object[] parameters = getParameters(params);
            // 获取OpenClient类的Method对象
            Method method = OpenClient.class.getDeclaredMethod(methodName,parameterType);
            // 调用方法并传递参数
            String result = (String) method.invoke(openClient, parameters);
            if (result instanceof String) {
                return handleUpdateInterfaceStatus(interfaceInfo,result);
            } else {
                throw new SystemException(ResponseCodeEnum.FAIL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("有请求参数接口{}发布过程接口测试失败........",methodName);
            return ResultData.fail(ResponseCodeEnum.INTERFACE_NOT_PASS);
        }
    }

    /**
     * 根据接口请求结果处理接口状态是否修改
     * @param result
     */
    private ResultData handleUpdateInterfaceStatus(InterfaceInfo interfaceInfo, String result) throws SystemException {
        //解析结果
        ResultData resultData = JSON.parseObject(result,ResultData.class);
        //如何将json字符串转为java对象
        if(!StringUtils.isBlank(Integer.toString(resultData.getCode()))){
            if(200 == resultData.getCode()){
                //3、将接口状态改为开启
                if(interfaceInfo.getStatus().intValue() == ApplicationStatusEnum.OPEN.getCode()){
                    interfaceInfo.setStatus(ApplicationStatusEnum.FORBIDDEN.getCode());
                    log.info("接口{}修改状态为离线状态....",interfaceInfo.getName());
                }else{
                    interfaceInfo.setStatus(ApplicationStatusEnum.OPEN.getCode());
                    log.info("接口{}修改状态为发布状态....",interfaceInfo.getName());
                }
                baseMapper.updateById(interfaceInfo);
                log.info("接口{}发布成功....",interfaceInfo.getName());
                return resultData;
            }else{
                throw new SystemException(ResponseCodeEnum.INTERFACE_NOT_PASS);
            }
        }else{
            throw new SystemException(ResponseCodeEnum.INTERFACE_NOT_PASS);
        }
    }

    /**
     * 根据参数集合获取对应的Method参数类型数组
     * 假设参数类型已知且简单处理，复杂情况需更精确映射
     * @param params 参数集合
     * @return Class<?>[] 参数类型数组
     */
    private Class<?>[] getParameterType(Map<String, Object> params) throws SystemException {
        Class<?>[] parameterTypes = new Class[params.size()];
        int index = 0;
        for (Object value : params.values()) {
            if (value instanceof Integer) {
                parameterTypes[index++] = int.class;
            } else if (value instanceof String) {
                parameterTypes[index++] = String.class;
            } else {
                throw new SystemException(ResponseCodeEnum.UNSUPORT_PARAM_TYPE);
            }
        }
        return parameterTypes;
    }

    /**
     * 从参数集合中提取实际参数列表
     * @param params 参数集合
     * @return Object[] 参数列表
     */
    private Object[] getParameters(Map<String, Object> params) {
        return params.values().toArray();
    }

    @Override
    public InterfaceInfoSaveVo echoInterfaceInfoById(Integer id) {
        InterfaceInfoSaveVo saveVo = new InterfaceInfoSaveVo();
        InterfaceInfo interfaceInfo = baseMapper.selectById(id);
        BeanUtils.copyProperties(interfaceInfo,saveVo);
        if(interfaceInfo.getStatus() ==  ApplicationStatusEnum.OPEN.getCode()){
            saveVo.setStatus(true);
        }else{
            saveVo.setStatus(false);
        }
        return saveVo;
    }

    @Override
    public void editInterface(InterfaceInfoSaveVo infoSaveVo) throws SystemException {
        InterfaceInfo interfaceInfo = baseMapper.selectById(infoSaveVo.getId());
        if(!interfaceInfo.getUrl().equalsIgnoreCase(infoSaveVo.getUrl())){
            //查询url是否重复
            QueryWrapper<InterfaceInfo> wrapper = new QueryWrapper<>();
            wrapper.eq("url",infoSaveVo.getUrl());
            InterfaceInfo selectOne = baseMapper.selectOne(wrapper);
            if(null != selectOne){
                throw new SystemException(ResponseCodeEnum.URL_EXITS);
            }
            BeanUtils.copyProperties(infoSaveVo,interfaceInfo);
            baseMapper.updateById(interfaceInfo);
            log.info("接口{}修改成功！",selectOne.getName());
        }else{
            BeanUtils.copyProperties(infoSaveVo,interfaceInfo);
            baseMapper.updateById(interfaceInfo);
            log.info("接口{}修改成功！",interfaceInfo.getName());
        }
    }

    @Override
    public void lineInterface(Integer id) throws SystemException {
        //1、校验接口是否存在、
        InterfaceInfo interfaceInfo = baseMapper.selectById(id);
        if (null == interfaceInfo){
            throw new SystemException(ResponseCodeEnum.INTERFACE_NOT_EXITS);
        }
        //2、将接口状态改为关闭
        interfaceInfo.setStatus(ApplicationStatusEnum.FORBIDDEN.getCode());
        baseMapper.updateById(interfaceInfo);
        log.info("接口{}下线成功....",interfaceInfo.getName());
    }

    @Override
    public ResultData onlineInvokeInterface(OnlineInvokeDto onlineInvokeDto) throws SystemException {
        //3、根据用户的accessKey获取相应的secretKey
        ResultData resultData = applicationKeyFeign.queryKeyByAccessKey(onlineInvokeDto.getAccessKey());
        //获取用户的openClient
        OpenClient userOpenClient = new OpenClient(onlineInvokeDto.getAccessKey(),resultData.getData().toString());
        //获取所有接口
        interfacePublishNames = getAllInterface();
        //两种情况：①、有请求参数；②、无请求参数
        //根据请求地址获取接口名称
        String methodName = interfacePublishNames.get(onlineInvokeDto.getUrl());
        if(StringUtils.isBlank(onlineInvokeDto.getParams())){
            //②、无请求参数
            return onlineInvokeRequestNoParams(userOpenClient,methodName);
        }else{
            //①、有请求参数
            return onlineInvokeRequestWithParams(userOpenClient,onlineInvokeDto,methodName);
        }
    }

    @Override
    public Map<String,Object> incrementInterfaceCount(String url) {
        QueryWrapper<InterfaceInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("url",url);
        InterfaceInfo interfaceInfo = baseMapper.selectOne(wrapper);
        interfaceInfo.setNumber(interfaceInfo.getNumber() + 1);
        int process = baseMapper.updateById(interfaceInfo);
        Map<String,Object> map = new HashMap<>();
        map.put("interfaceId", interfaceInfo.getId());
        if(process > 0){
            log.info("{}接口url调用次数加1成功！",url);
            map.put("increment", true);
            return map;
        }
        log.info("{}接口url调用次数加1失败！",url);
        map.put("increment", false);
        return map;
    }

    @Override
    public boolean queryInterfaceInfoByUrl(String url) {
        QueryWrapper<InterfaceInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("url",url);
        InterfaceInfo interfaceInfo = baseMapper.selectOne(wrapper);
        if(null != interfaceInfo){
            return false;
        }
        return true;
    }

    @Override
    public boolean queryInterfaceInfoStatusByUrl(String url) {
        QueryWrapper<InterfaceInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("url",url);
        InterfaceInfo interfaceInfo = baseMapper.selectOne(wrapper);
        if(null != interfaceInfo){
            if(interfaceInfo.getStatus().intValue() == ApplicationStatusEnum.OPEN.getCode()){
                return false;
            }
            return true;
        }
        return true;
    }

    @Override
    public Map<String, Object> queryAdminInterfaceList(Integer pageNo, Integer pageSize, InterfaceInfoSearchDto searchDto) {
        Page<InterfaceInfo> pageInfo = new Page<InterfaceInfo>(pageNo, pageSize);
        searchDto.setDescription(searchDto.getDescription().trim());
        searchDto.setName(searchDto.getName().trim());
        searchDto.setMethod(searchDto.getMethod().trim());
        QueryWrapper<InterfaceInfo> wrapper = new QueryWrapper<>();
        //模糊查询
        wrapper.like(!StringUtils.isBlank(searchDto.getDescription()),"description",searchDto.getDescription());
        wrapper.like(!StringUtils.isBlank(searchDto.getMethod()),"method",searchDto.getMethod());
        wrapper.like(!StringUtils.isBlank(searchDto.getName()),"name",searchDto.getName());
        //查询接口状态为在线状态的所有接口
        wrapper.orderByDesc("create_time");

        Page<InterfaceInfo> interfaceInfoPage = baseMapper.selectPage(pageInfo, wrapper);
        if(interfaceInfoPage != null){
            List<InterfaceInfo> interfaceInfoList = interfaceInfoPage.getRecords();
            List<InterfaceInfoSaveVo> list = new ArrayList<>();
            if(!CollectionUtils.isEmpty(interfaceInfoList)){
                interfaceInfoList.stream().forEach(interfaceInfo -> {
                    InterfaceInfoSaveVo interfaceInfoSaveDto = new InterfaceInfoSaveVo();
                    BeanUtils.copyProperties(interfaceInfo, interfaceInfoSaveDto);
                    if(interfaceInfo.getStatus().intValue() == MenuStatusEnum.OPEN.getCode()){
                        interfaceInfoSaveDto.setStatus(true);
                    }else{
                        interfaceInfoSaveDto.setStatus(false);
                    }
                    list.add(interfaceInfoSaveDto);
                });
                Map<String,Object> map = new HashMap<>();
                map.put("data", list);
                map.put("total", interfaceInfoPage.getTotal());
                return map;
            }
        }
        return null;
    }

    @Override
    public Map<String, Object> queryInterfaces() {
        //查询总接口数
        int total = 0;
        List<InterfaceInfo> interfaceInfoList = baseMapper.selectList(null);
        if(!CollectionUtils.isEmpty(interfaceInfoList)){
            total = interfaceInfoList.size();
        }
        //查询昨日新增接口数
        int yesterday = baseMapper.queryYesterdayInterfaces();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("total", total);
        map.put("yesterday", yesterday);
        return map;
    }

    @Override
    public Map<String,Object> queryTopFive() {
        Page<InterfaceInfo> pageInfo = new Page<>(1, 5);
        //获取Top 5的数据
        QueryWrapper<InterfaceInfo> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("number");
        Page<InterfaceInfo> interfaceInfoPage = baseMapper.selectPage(pageInfo, wrapper);
        if(null != interfaceInfoPage){
            List<InterfaceInfo> infoList = interfaceInfoPage.getRecords();
            if(!CollectionUtils.isEmpty(infoList)){
                Map<String,Object> resultMap = new HashMap<String,Object>();
                List<String> nameList = infoList.stream().map(InterfaceInfo::getName).collect(Collectors.toList());
                List<Map<String,Object>> seriesMap = new ArrayList<Map<String,Object>>();
                infoList.stream().forEach(item ->{
                    Map<String,Object> map = new HashMap<String,Object>();
                    map.put("name", item.getName());
                    map.put("value", item.getNumber());
                    seriesMap.add(map);
                });
                resultMap.put("legend",nameList);
                resultMap.put("series",seriesMap);
                return resultMap;
            }
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> queryTopTenUser() {
        List<Map<String, Object>> list = baseMapper.queryTopTenUser();
        if(!CollectionUtils.isEmpty(list)){
            List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
            list.stream().forEach(item -> {
                Map<String, Object> map = new HashMap<String, Object>();
                Integer userId = (Integer) item.get("user_id");
                Long total = (Long) item.get("total");
                ResultData resultData = userFeign.queryUserNameById(userId);
                map.put("value", total);
                if(resultData.getCode() == 200){
                    map.put("name", resultData.getData());
                }
                resultList.add(map);
            });
            return resultList;
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteInterfaceById(Integer id) {
        QueryWrapper<UserInterfaceInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("interface_id", id);
        List<UserInterfaceInfo> userInterfaceInfos = userInterfaceInfoMapper.selectList(wrapper);
        if(!CollectionUtils.isEmpty(userInterfaceInfos)){
            List<Integer> ids = userInterfaceInfos.stream().map(UserInterfaceInfo::getId).collect(Collectors.toList());
            userInterfaceInfoMapper.deleteBatchIds(ids);
        }
        baseMapper.deleteById(id);
    }

    /**
     * 在线调用有参数接口
     * @param userOpenClient
     * @param onlineInvokeDto
     * @param methodName
     * @return
     */
    private ResultData onlineInvokeRequestWithParams(OpenClient userOpenClient,OnlineInvokeDto onlineInvokeDto, String methodName) {
        log.info("在线调用有参接口....",methodName);
        log.info("请求参数为【{}】",onlineInvokeDto.getParams());
        //①、有请求参数
        JSONObject json = JSON.parseObject(onlineInvokeDto.getParams());
        //json数据如何转成数组
        String[] userParams = json.keySet().toArray(new String[json.size()]);
        Map<String,Object> params = new HashMap<>();
        for (String key : userParams) {
            //将用户的请求参数用map封装
            params.put(key,json.getString(key));
        }
        try {
            //根据请求参数获取参数类型
            Class[] parameterType = getParameterType(params);
            //获取实际的请求参数
            Object[] parameters = getParameters(params);
            // 获取OpenClient类的Method对象
            Method method = OpenClient.class.getDeclaredMethod(methodName,parameterType);
            // 调用方法并传递参数
            String result = (String) method.invoke(userOpenClient, parameters);
            if (result instanceof String) {
                //解析返回结果
                ResultData resultData = JSON.parseObject(result, ResultData.class);
                if(resultData.getCode() == 200){
                    log.info("在线调用有参接口{}成功！！！！参数包括：【{}】",methodName,onlineInvokeDto.getParams());
                }
                return resultData;
            } else {
                throw new SystemException(ResponseCodeEnum.INTERFACE_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("在线调用有参接口失败........",methodName);
            return ResultData.fail(ResponseCodeEnum.INTERFACE_ERROR);
        }
    }

    /**
     * 在线调用无参数接口
     * @param userOpenClient
     * @param methodName
     * @return
     */
    private ResultData onlineInvokeRequestNoParams(OpenClient userOpenClient, String methodName) {
        try {
            // 获取OpenClient类的Method对象
            log.info("在线调用无参接口{}....",methodName);
            //通过反射获取OpenClient类的方法
            Method method = OpenClient.class.getMethod(methodName);
            // 调用方法并传递参数
            String result = (String) method.invoke(userOpenClient);
            if (result instanceof String) {
                //解析返回结果
                ResultData resultData = JSON.parseObject(result, ResultData.class);
                if(resultData.getCode() == 200){
                    log.info("在线调用无参接口{}成功！！！！",methodName);
                }
                return resultData;
            } else {
                throw new SystemException(ResponseCodeEnum.INTERFACE_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("在线调用无参接口{}失败........",methodName);
            return ResultData.fail(ResponseCodeEnum.INTERFACE_ERROR);
        }
    }
}
