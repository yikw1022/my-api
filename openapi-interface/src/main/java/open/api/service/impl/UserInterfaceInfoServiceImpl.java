package open.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import open.api.model.entity.UserInterfaceInfo;
import open.api.mapper.UserInterfaceInfoMapper;
import open.api.model.vo.InvokeDetailVo;
import open.api.response.ResultData;
import open.api.service.IUserInterfaceInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import open.api.system.applicaionkey.ApplicationKeyFeign;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户调用接口次数信息表 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-05-08
 */
@Service
@RequiredArgsConstructor
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo> implements IUserInterfaceInfoService {

    private final ApplicationKeyFeign applicationKeyFeign;

    @Override
    public boolean saveUserInterfaceInfo(UserInterfaceInfo interfaceInfo) {
        return this.save(interfaceInfo);
    }

    @Override
    public Map<String,Object> queryInvokeCountByUserId(Integer userId) {
        //获取当前用户的今日调用接口数
        Long today = baseMapper.getInvokeInterfaceNumberToday(userId);
        //获取当前用户的总调用接口数
        Long total = baseMapper.getInvokeInterfaceNumberTotal(userId);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("today", today);
        map.put("total", total);
        return map;
    }

    @Override
    public List<InvokeDetailVo> queryInvokeDetailByUserId(Integer userId) {
        //根据当前用户的id获取key名称
        List<String> keyNameList = getKeyNameList(userId);
        //根据key名称获取相对应名称在最近一周内的接口调用次数
        if(!CollectionUtils.isEmpty(keyNameList)){
            List<InvokeDetailVo> list = new ArrayList<>();
            keyNameList.stream().forEach(keyName ->{
                List<Long> countList = baseMapper.getInvokeCountByKeyNameAndDate(keyName);
                List<String> createTimeList = baseMapper.getInvokeDateByKeyName(keyName);
                InvokeDetailVo invokeDetailVo = new InvokeDetailVo();
                invokeDetailVo.setKeyName(keyName);
                invokeDetailVo.setCount(countList);
                invokeDetailVo.setCreateTime(createTimeList);
                list.add(invokeDetailVo);
            });
            return list;
        }
        return null;
    }

    /**
     * 根据当前用户的id获取应用key名称
     * @param userId
     * @return
     */
    private List<String> getKeyNameList(Integer userId){
        ResultData resultData = applicationKeyFeign.queryKeyNameByUserId(userId);
        if(resultData.getCode() == 200){
            return (List<String>) resultData.getData();
        }
        return null;
    }

    @Override
    public List<InvokeDetailVo> queryInterfaceTrafficByUserId(Integer userId) {
        //根据当前用户的id获取key名称
        List<String> keyNameList = getKeyNameList(userId);
        //根据key名称获取相对应名称在24小时内的接口调用次数
        if(!CollectionUtils.isEmpty(keyNameList)){
            List<InvokeDetailVo> list = new ArrayList<>();
            keyNameList.stream().forEach(keyName ->{
                List<Long> countList = baseMapper.findOneDayCountList(keyName);
                List<String> createTimeList = baseMapper.findOneDayTimeList(keyName);
                InvokeDetailVo invokeDetailVo = new InvokeDetailVo();
                invokeDetailVo.setKeyName(keyName);
                invokeDetailVo.setCount(countList);
                invokeDetailVo.setCreateTime(createTimeList);
                list.add(invokeDetailVo);
            });
            return list;
        }
        return null;
    }

    @Override
    public List<InvokeDetailVo> queryThirtyDayTrafficByUserId(Integer userId) {
        //根据当前用户的id获取key名称
        List<String> keyNameList = getKeyNameList(userId);
        //根据key名称获取相对应名称在30天内的接口调用次数
        if(!CollectionUtils.isEmpty(keyNameList)){
            List<InvokeDetailVo> list = new ArrayList<>();
            keyNameList.stream().forEach(keyName ->{
                List<Long> countList = baseMapper.findThirtyDayCountList(keyName);
                List<String> createTimeList = baseMapper.findThirtyDayTimeList(keyName);
                InvokeDetailVo invokeDetailVo = new InvokeDetailVo();
                invokeDetailVo.setKeyName(keyName);
                invokeDetailVo.setCount(countList);
                invokeDetailVo.setCreateTime(createTimeList);
                list.add(invokeDetailVo);
            });
            return list;
        }
        return null;
    }

    @Override
    public List<InvokeDetailVo> querySixMonthTrafficByUserId(Integer userId) {
        //根据当前用户的id获取key名称
        List<String> keyNameList = getKeyNameList(userId);
        //根据key名称获取相对应名称在6个月内的接口调用次数
        if(!CollectionUtils.isEmpty(keyNameList)){
            List<InvokeDetailVo> list = new ArrayList<>();
            keyNameList.stream().forEach(keyName ->{
                List<Long> countList = baseMapper.findSixMonthCountList(keyName);
                List<String> createTimeList = baseMapper.findSixMonthTimeList(keyName);
                InvokeDetailVo invokeDetailVo = new InvokeDetailVo();
                invokeDetailVo.setKeyName(keyName);
                invokeDetailVo.setCount(countList);
                invokeDetailVo.setCreateTime(createTimeList);
                list.add(invokeDetailVo);
            });
            return list;
        }
        return null;
    }

    @Override
    public void deleteBykeyName(String keyName) {
        QueryWrapper<UserInterfaceInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("key_name",keyName);
        List<UserInterfaceInfo> list = baseMapper.selectList(wrapper);
        if(!CollectionUtils.isEmpty(list)){
            List<Integer> ids = list.stream().map(UserInterfaceInfo::getId).collect(Collectors.toList());
            baseMapper.deleteBatchIds(ids);
        }
    }
}
