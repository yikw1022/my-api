package open.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import open.api.mapper.RoleMapper;
import open.api.mapper.RoleMenuMapper;
import open.api.model.entity.Role;
import open.api.model.entity.RoleMenu;
import open.api.service.IRoleService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2024-04-21
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    private final RoleMenuMapper roleMenuMapper;

    @Override
    public Map<String, Object> queryRoleListByPage(Integer pageNo, Integer pageSize, String roleName) {
        Page<Role> pageInfo = new Page<Role>(pageNo, pageSize);
        String trimRoleName = "";
        if(!StringUtils.isBlank(roleName)){
            trimRoleName = roleName.trim();
        }
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        wrapper.like(!StringUtils.isBlank(trimRoleName),"role_name",trimRoleName);
        wrapper.orderByDesc("create_time");
        Page<Role> rolePage = baseMapper.selectPage(pageInfo, wrapper);
        if(rolePage != null){
            List<Role> roleList = rolePage.getRecords();
            if(!CollectionUtils.isEmpty(roleList)){
                Map<String,Object> map = new HashMap<>();
                map.put("data", roleList);
                map.put("total", rolePage.getTotal());
                return map;
            }
        }
        return null;
    }

    @Override
    public List<String> queryRoleList() {
        List<String> list = new ArrayList<>();
        List<Role> roleList = baseMapper.selectList(null);
        if(!CollectionUtils.isEmpty(roleList)){
            roleList.stream().forEach(role -> {
                list.add(role.getRoleName());
            });
            return list;
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savePermission(Integer roleId, List<Integer> menuIds) {
        //分配前将之前所拥有的菜单id删除再重新保存
        QueryWrapper<RoleMenu> wrapper = new QueryWrapper<>();
        wrapper.eq("role_id", roleId);
        List<Integer> roleMenuIds = roleMenuMapper.selectList(wrapper).stream().map(RoleMenu::getId).collect(Collectors.toList());
        if(!CollectionUtils.isEmpty(roleMenuIds)){
            roleMenuMapper.deleteBatchIds(roleMenuIds);
        }
        //保存新的菜单id
        if(!CollectionUtils.isEmpty(menuIds)){
            menuIds.stream().forEach(menuId ->{
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(menuId);
                roleMenuMapper.insert(roleMenu);
            });
        }
    }
}
