package open.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import open.api.model.entity.User;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-04-21
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 查询昨日新增人数
     * @return
     */
    @Select("SELECT COUNT(*) FROM user WHERE DATE(create_time) = CURDATE() - INTERVAL 1 DAY")
    int queryYesterdayRegisterUsers();
}
