package open.api.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import open.api.exception.SystemException;
import open.api.model.dto.MenuButtonDto;
import open.api.model.dto.MenuDirectoryDto;
import open.api.model.entity.Menu;
import open.api.model.entity.Role;
import open.api.model.vo.MenuBtnEchoVo;
import open.api.model.vo.MenuEchoVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2024-04-21
 */
public interface IMenuService extends IService<Menu> {

    /**
     * 查询所有菜单数据
     */
    List<Tree<String>> queryMenuList();

    /**
     * 查询当前角色所拥有的所有的菜单以及按钮权限
     * @return
     */
    List<Integer> queryRoleMenuList(Role role);

    /**
     * 保存一级菜单目录
     * @param menuDirectoryDto
     */
    void saveDirectory(MenuDirectoryDto menuDirectoryDto) throws SystemException;

    /**
     * 保存菜单
     * @param menuDirectoryDto
     */
    void saveMenu(Integer parentId,MenuDirectoryDto menuDirectoryDto) throws SystemException;

    /**
     * 保存菜单按钮
     * @param parentId
     * @param menuButtonDto
     */
    void saveMenuButton(Integer parentId, MenuButtonDto menuButtonDto) throws SystemException;

    /**
     * 删除菜单
     * @param id
     */
    void removeMenu(Integer id) throws SystemException;

    /**
     * 回显菜单目录数据
     * @param id
     * @return
     */
    MenuEchoVo echoDirectoryById(Integer id);

    /**
     * 回显按钮权限数据
     * @param id
     * @return
     */
    MenuBtnEchoVo echoBtnInfoById(Integer id);

    /**
     * 修改一级菜单目录
     * @param menuDirectoryDto
     */
    void updateDirectory(MenuDirectoryDto menuDirectoryDto) throws SystemException;

    /**
     * 修改菜单
     * @param menuDirectoryDto
     */
    void updateMenu(MenuDirectoryDto menuDirectoryDto) throws SystemException;

    /**
     * 修改菜单按钮
     * @param menuButtonDto
     */
    void updateMenuButton(MenuButtonDto menuButtonDto);
}
