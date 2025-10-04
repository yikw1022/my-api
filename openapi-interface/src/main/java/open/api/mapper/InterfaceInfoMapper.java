package open.api.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import open.api.model.entity.InterfaceInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 接口信息表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-04-26
 */
public interface InterfaceInfoMapper extends BaseMapper<InterfaceInfo> {

    /**
     * 查询昨日新增接口数
     * @return
     */
    @Select("SELECT COUNT(*) FROM interface_info WHERE DATE(create_time) = CURDATE() - INTERVAL 1 DAY")
    int queryYesterdayInterfaces();

    List<Map<String, Object>> queryTopTenUser();
}
