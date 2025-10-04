package open.api.service;

import open.api.exception.SystemException;
import open.api.model.dto.InterfaceInfoSaveDto;
import open.api.model.dto.InterfaceInfoSearchDto;
import open.api.model.dto.OnlineInvokeDto;
import open.api.model.dto.PublishDto;
import open.api.model.entity.InterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import open.api.model.vo.InterfaceInfoSaveVo;
import open.api.model.vo.InvokeDetailVo;
import open.api.response.ResultData;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 接口信息表 服务类
 * </p>
 *
 * @author author
 * @since 2024-04-26
 */
public interface IInterfaceInfoService extends IService<InterfaceInfo> {

    /**
     * 保存接口信息
     * @param saveDto
     */
    void saveInterfaceInfo(InterfaceInfoSaveDto saveDto) throws SystemException;

    /**
     * 分页查询接口数据
     * @param pageNo
     * @param pageSize
     * @param searchDto
     * @return
     */
    Map<String, Object> queryInterfaceList(Integer pageNo, Integer pageSize, InterfaceInfoSearchDto searchDto);

    /**
     * 发布接口
     * @param publishDto
     */
    ResultData publishInterface(PublishDto publishDto) throws SystemException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException;

    /**
     * 回显接口数据
     * @param id
     * @return
     */
    InterfaceInfoSaveVo echoInterfaceInfoById(Integer id);

    /**
     * 修改接口
     * @param infoSaveVo
     */
    void editInterface(InterfaceInfoSaveVo infoSaveVo) throws SystemException;

    /**
     * 下线接口
     * @param id
     */
    void lineInterface(Integer id) throws SystemException;

    /**
     * 在线调用接口测试
     * @param onlineInvokeDto
     * @return
     */
    ResultData onlineInvokeInterface(OnlineInvokeDto onlineInvokeDto) throws SystemException;

    /**
     * 接口调用次数加1
     * @param url
     * @return
     */
    Map<String,Object> incrementInterfaceCount(String url);

    /**
     * 根据接口url查询接口是否存在
     * @param url
     * @return
     */
    boolean queryInterfaceInfoByUrl(String url);

    /**
     * 根据接口url查询接口是否在线
     * @param url
     * @return
     */
    boolean queryInterfaceInfoStatusByUrl(String url);

    /**
     * 分页查询接口数据(管理员)
     * @param pageNo
     * @param pageSize
     * @param searchDto
     * @return
     */
    Map<String, Object> queryAdminInterfaceList(Integer pageNo, Integer pageSize, InterfaceInfoSearchDto searchDto);

    /**
     * 首页查询接口数据
     * @return
     */
    Map<String, Object> queryInterfaces();

    /**
     * 查询首页最近7天Top 5热点api调用次数
     * @return
     */
    Map<String,Object> queryTopFive();

    /**
     * 查询首页Top 10活跃用户
     * @return
     */
    List<Map<String, Object>> queryTopTenUser();

    /**
     * 删除接口
     * @param id
     */
    void deleteInterfaceById(Integer id);
}
