package open.api.service.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.org.apache.xpath.internal.operations.Or;
import lombok.RequiredArgsConstructor;
import open.api.exception.SystemException;
import open.api.mapper.MenuMapper;
import open.api.mapper.RoleMenuMapper;
import open.api.model.dto.MenuButtonDto;
import open.api.model.dto.MenuDirectoryDto;
import open.api.model.entity.Menu;
import open.api.model.entity.Role;
import open.api.model.entity.RoleMenu;
import open.api.model.enums.MenuStatusEnum;
import open.api.model.enums.MenuTypeEnum;
import open.api.model.vo.MenuBtnEchoVo;
import open.api.model.vo.MenuEchoVo;
import open.api.response.ResponseCodeEnum;
import open.api.service.IMenuService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
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
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    private final RoleMenuMapper roleMenuMapper;

    /**
     * 将菜单数据封装成树形数据格式返回
     */
    @Override
    public List<Tree<String>> queryMenuList() {
        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        List<Menu> menusList = baseMapper.selectList(wrapper);
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        // 最大递归深度
        treeNodeConfig.setDeep(3);
        List<Tree<String>> treeNodes = TreeUtil.build(menusList, "0", treeNodeConfig,
                (treeNode, tree) -> {
                    //这俩属性必须设置
                    tree.setId(treeNode.getId().toString());
                    tree.setParentId(treeNode.getParentId().toString());
                    // 扩展属性 ...
                    tree.putExtra("label", treeNode.getTitle());
                    tree.putExtra("button", treeNode.getButton());
                    tree.putExtra("type", treeNode.getType());
                    tree.putExtra("name", treeNode.getName());
                    tree.putExtra("path", treeNode.getPath());
                });
        return treeNodes;
    }

    @Override
    public List<Integer> queryRoleMenuList(Role role) {
        //如果是admin管理员角色则返回所有的菜单及按钮
        if(role.getRoleName().equalsIgnoreCase("管理员")){
            //由于前端展示需求，只需返回按钮级别的id即可
            //过滤出按钮的id并且状态是开启的
            List<Menu> menuList = baseMapper.selectList(null);
            List<Integer> buttonIds = menuList.stream().filter(menu -> (menu.getType() == MenuTypeEnum.BUTTON.getCode()) && (menu.getStatus() == MenuStatusEnum.OPEN.getCode()))
                    .collect(Collectors.toList())
                    .stream().map(Menu::getId).collect(Collectors.toList());
            return buttonIds;
        }else{
            QueryWrapper<RoleMenu> wrapper = new QueryWrapper<>();
            wrapper.eq("role_id", role.getId());
            List<Integer> menuIdList = roleMenuMapper.selectList(wrapper).stream().map(RoleMenu::getMenuId).collect(Collectors.toList());
            if(!CollectionUtils.isEmpty(menuIdList)){
                //由于前端展示需求，只需返回按钮级别的id即可
                //过滤出按钮的id并且状态是开启的
                List<Menu> menuList = baseMapper.selectBatchIds(menuIdList);
                List<Integer> buttonIds = menuList.stream().filter(menu -> (menu.getType() == MenuTypeEnum.BUTTON.getCode()) && (menu.getStatus() == MenuStatusEnum.OPEN.getCode()))
                        .collect(Collectors.toList())
                        .stream().map(Menu::getId).collect(Collectors.toList());
                return buttonIds;
            }
            return null;
        }
    }

    @Override
    public void saveDirectory(MenuDirectoryDto menuDirectoryDto) throws SystemException {
        //name 和path是唯一的
        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        wrapper.eq("name", menuDirectoryDto.getName())
                .or()
                .eq("path",menuDirectoryDto.getPath());
        Menu menu = baseMapper.selectOne(wrapper);
        if(null != menu) {
            throw new SystemException(ResponseCodeEnum.MENU_EXITS);
        }

        Menu insertMenu = new Menu();
        insertMenu.setName(menuDirectoryDto.getName());
        insertMenu.setPath(menuDirectoryDto.getPath());
        insertMenu.setTitle(menuDirectoryDto.getTitle());
        //因为这是一级目录，所以父id为0
        insertMenu.setParentId(0);
        insertMenu.setType(MenuTypeEnum.DIRECTORY.getCode());
        insertMenu.setSort(1);
        baseMapper.insert(insertMenu);
    }

    @Override
    public void saveMenu(Integer parentId,MenuDirectoryDto menuDirectoryDto) throws SystemException {
        //name 和path是唯一的
        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        wrapper.eq("name", menuDirectoryDto.getName())
                .or()
                .eq("path",menuDirectoryDto.getPath());
        Menu menu = baseMapper.selectOne(wrapper);
        if(null != menu) {
            throw new SystemException(ResponseCodeEnum.MENU_EXITS);
        }
        //查询父目录是否存在
        Menu parentMenu = baseMapper.selectById(parentId);
        if(null == parentMenu){
            throw new SystemException(ResponseCodeEnum.PARENT_DIRECTOR_NOT_EXITS);
        }
        Menu insertMenu = new Menu();
        insertMenu.setName(menuDirectoryDto.getName());
        insertMenu.setPath(parentMenu.getPath() + menuDirectoryDto.getPath());
        insertMenu.setTitle(menuDirectoryDto.getTitle());
        //因为这是一级目录，所以父id为0
        insertMenu.setParentId(parentMenu.getId());
        insertMenu.setType(MenuTypeEnum.MENU.getCode());
        insertMenu.setSort(2);
        baseMapper.insert(insertMenu);
    }

    @Override
    public void saveMenuButton(Integer parentId, MenuButtonDto menuButtonDto) throws SystemException {
        //查询父目录是否存在
        Menu parentMenu = baseMapper.selectById(parentId);
        if(null == parentMenu){
            throw new SystemException(ResponseCodeEnum.PARENT_MENU_NOT_EXITS);
        }
        Menu insertMenu = new Menu();
        insertMenu.setTitle(menuButtonDto.getTitle());
        insertMenu.setButton(menuButtonDto.getButton());
        //因为这是一级目录，所以父id为0
        insertMenu.setParentId(parentMenu.getId());
        insertMenu.setType(MenuTypeEnum.BUTTON.getCode());
        insertMenu.setSort(3);
        baseMapper.insert(insertMenu);
    }

    @Override
    public void removeMenu(Integer id) throws SystemException {
        //根据id查询其下是否有子菜单
        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        List<Menu> menuList = baseMapper.selectList(wrapper);
        if(!CollectionUtils.isEmpty(menuList)){
            throw new SystemException(ResponseCodeEnum.MENU_HAS_SON);
        }
        //删除
        baseMapper.deleteById(id);
    }

    @Override
    public MenuEchoVo echoDirectoryById(Integer id) {
        Menu menu = baseMapper.selectById(id);
        MenuEchoVo echoVo = new MenuEchoVo();
        BeanUtils.copyProperties(menu,echoVo);
        return echoVo;
    }

    @Override
    public MenuBtnEchoVo echoBtnInfoById(Integer id) {
        Menu menu = baseMapper.selectById(id);
        MenuBtnEchoVo echoVo = new MenuBtnEchoVo();
        BeanUtils.copyProperties(menu,echoVo);
        return echoVo;
    }

    @Override
    public void updateDirectory(MenuDirectoryDto menuDirectoryDto) throws SystemException {
        //name 和path是唯一的
        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        wrapper.eq(!StringUtils.isBlank(menuDirectoryDto.getName()),"name", menuDirectoryDto.getName())
                .or()
                .eq(!StringUtils.isBlank(menuDirectoryDto.getPath()),"path",menuDirectoryDto.getPath());
        Menu menu = baseMapper.selectOne(wrapper);
        if((null != menu) && (menu.getId().intValue() != menuDirectoryDto.getId())) {
            throw new SystemException(ResponseCodeEnum.MENU_EXITS);
        }
        Menu originalMenu = baseMapper.selectById(menuDirectoryDto.getId());
        originalMenu.setName(menuDirectoryDto.getName());
        originalMenu.setPath(menuDirectoryDto.getPath());
        originalMenu.setTitle(menuDirectoryDto.getTitle());
        baseMapper.updateById(originalMenu);
    }

    @Override
    public void updateMenu(MenuDirectoryDto menuDirectoryDto) throws SystemException {
        //name 和path是唯一的
        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        wrapper.eq(!StringUtils.isBlank(menuDirectoryDto.getName()),"name", menuDirectoryDto.getName())
                .or()
                .eq(!StringUtils.isBlank(menuDirectoryDto.getPath()),"path",menuDirectoryDto.getPath());
        Menu menu = baseMapper.selectOne(wrapper);
        if((null != menu) && (menu.getId().intValue() != menuDirectoryDto.getId())) {
            throw new SystemException(ResponseCodeEnum.MENU_EXITS);
        }
        Menu originalMenu = baseMapper.selectById(menuDirectoryDto.getId());
        //查询父级菜单
        Menu parentMenu = baseMapper.selectById(originalMenu.getParentId());

        originalMenu.setName(menuDirectoryDto.getName());
        //处理菜单路径
        String inputPath = menuDirectoryDto.getPath();
        int index = inputPath.lastIndexOf("/");
        if(index != -1) {
            String newPath = inputPath.substring(index + 1);
            originalMenu.setPath(parentMenu.getPath() + "/" + newPath);
        }
        originalMenu.setTitle(menuDirectoryDto.getTitle());
        baseMapper.updateById(originalMenu);
    }

    @Override
    public void updateMenuButton(MenuButtonDto menuButtonDto) {
        Menu menu = baseMapper.selectById(menuButtonDto.getId());
        menu.setTitle(menuButtonDto.getTitle());
        menu.setButton(menuButtonDto.getButton());
        baseMapper.updateById(menu);
    }


    /**
     * 将菜单数据封装成树形数据格式返回
     * @param menuIds
     */
//    private List<Tree<String>> buildTree(List<Integer> menuIds) {
//        List<Menu> menusList = menuMapper.selectBatchIds(menuIds);
//        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
//        // 最大递归深度
//        treeNodeConfig.setDeep(2);
//        List<Tree<String>> treeNodes = TreeUtil.build(menusList, "0", treeNodeConfig,
//                (treeNode, tree) -> {
//                    //这俩属性必须设置
//                    tree.setId(treeNode.getId().toString());
//                    tree.setParentId(treeNode.getParentId().toString());
//                    // 扩展属性 ...
//                    tree.putExtra("path", treeNode.getPath());
//                    tree.putExtra("name", treeNode.getName());
//                    tree.putExtra("title", treeNode.getTitle());
//                    tree.putExtra("hidden", treeNode.getHidden());
//                    tree.putExtra("icon", treeNode.getIcon());
//                });
//        return treeNodes;
//    }
}
