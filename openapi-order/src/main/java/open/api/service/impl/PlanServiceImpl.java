package open.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import open.api.model.entity.Plan;
import open.api.mapper.PlanMapper;
import open.api.service.IPlanService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 额度套餐表 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-05-05
 */
@Service
public class PlanServiceImpl extends ServiceImpl<PlanMapper, Plan> implements IPlanService {

    @Override
    public List<Plan> queryPlanList(String name) {
        QueryWrapper<Plan> wrapper = new QueryWrapper<>();
        wrapper.like(!StringUtils.isBlank(name.trim()),"name",name);
        wrapper.orderByDesc("create_time");
        return baseMapper.selectList(wrapper);
    }
}
