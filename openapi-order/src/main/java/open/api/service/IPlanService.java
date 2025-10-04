package open.api.service;

import open.api.model.entity.Plan;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 额度套餐表 服务类
 * </p>
 *
 * @author author
 * @since 2024-05-05
 */
public interface IPlanService extends IService<Plan> {

    /**
     * 查询套餐数据
     * @return
     */
    List<Plan> queryPlanList(String name);
}
