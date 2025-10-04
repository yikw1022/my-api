package open.api.mapper;

import open.api.model.entity.Application;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 应用管理表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-04-27
 */
public interface ApplicationMapper extends BaseMapper<Application> {

    /**
     * 查询昨日新增应用数
     * @return
     */
    @Select("SELECT COUNT(*) FROM application WHERE DATE(create_time) = CURDATE() - INTERVAL 1 DAY")
    int queryYesterdayApplications();
}
