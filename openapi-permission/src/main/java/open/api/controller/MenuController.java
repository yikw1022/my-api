package open.api.controller;


import cn.hutool.core.lang.tree.Tree;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import open.api.exception.SystemException;
import open.api.model.dto.MenuButtonDto;
import open.api.model.dto.MenuDirectoryDto;
import open.api.model.entity.Menu;
import open.api.model.entity.Role;
import open.api.model.vo.MenuBtnEchoVo;
import open.api.model.vo.MenuEchoVo;
import open.api.response.ResultData;
import open.api.service.IMenuService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author author
 * @since 2024-04-21
 */
@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/permission/menu")
@Api(tags = "菜单管理模块")
public class MenuController {

    private final IMenuService menuService;

    /**
     * 查询所有菜单数据
     * @param
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询所有菜单数据")
    public ResultData queryMenuList() {
        List<Tree<String>> menuList = menuService.queryMenuList();
        return ResultData.success(menuList);
    }

    /**
     * 保存一级菜单目录
     * @param
     * @return
     */
    @PostMapping("/save/directory")
    @ApiOperation(value = "保存一级菜单目录")
    public ResultData saveDirectory(@RequestBody MenuDirectoryDto menuDirectoryDto) throws SystemException {
        menuService.saveDirectory(menuDirectoryDto);
        return ResultData.success();
    }

    /**
     * 修改一级菜单目录
     * @param
     * @return
     */
    @PutMapping("/update/directory")
    @ApiOperation(value = "修改一级菜单目录")
    public ResultData updateDirectory(@RequestBody MenuDirectoryDto menuDirectoryDto) throws SystemException {
        menuService.updateDirectory(menuDirectoryDto);
        return ResultData.success();
    }

    /**
     * 保存菜单
     * @param
     * @return
     */
    @PostMapping("/save/menu")
    @ApiOperation(value = "保存菜单")
    public ResultData saveMenu(@RequestParam Integer parentId,
                               @RequestBody MenuDirectoryDto menuDirectoryDto) throws SystemException {
        menuService.saveMenu(parentId,menuDirectoryDto);
        return ResultData.success();
    }

    /**
     * 修改菜单
     * @param
     * @return
     */
    @PutMapping("/update/menu")
    @ApiOperation(value = "修改菜单")
    public ResultData updateMenu(@RequestBody MenuDirectoryDto menuDirectoryDto) throws SystemException {
        menuService.updateMenu(menuDirectoryDto);
        return ResultData.success();
    }

    /**
     * 保存菜单按钮
     * @param
     * @return
     */
    @PostMapping("/save/menu/button")
    @ApiOperation(value = "保存菜单按钮")
    public ResultData saveMenuButton(@RequestParam Integer parentId,
                               @RequestBody MenuButtonDto menuButtonDto) throws SystemException {
        menuService.saveMenuButton(parentId,menuButtonDto);
        return ResultData.success();
    }

    /**
     * 修改菜单按钮
     * @param
     * @return
     */
    @PutMapping("/update/menu/btn")
    @ApiOperation(value = "修改菜单按钮")
    public ResultData updateMenuButton(@RequestBody MenuButtonDto menuButtonDto) throws SystemException {
        menuService.updateMenuButton(menuButtonDto);
        return ResultData.success();
    }

    /**
     * 回显菜单目录数据
     * @return
     */
    @GetMapping("/menu/echo/{id}")
    @ApiOperation(value = "回显菜单目录数据")
    public ResultData echoDirectoryById(@PathVariable("id") Integer id){
        MenuEchoVo echoVo = menuService.echoDirectoryById(id);
        return ResultData.success(echoVo);
    }

    /**
     * 回显按钮权限数据
     * @return
     */
    @GetMapping("/btn/echo/{id}")
    @ApiOperation(value = "回显按钮权限数据")
    public ResultData echoBtnInfoById(@PathVariable("id") Integer id){
        MenuBtnEchoVo echoVo = menuService.echoBtnInfoById(id);
        return ResultData.success(echoVo);
    }

    /**
     * TODO: 测试环境下禁用 删除菜单
     * @param
     * @return
     */
    @DeleteMapping("/remove/{id}")
    @ApiOperation(value = "删除菜单")
    public ResultData removeMenu(@PathVariable("id") Integer id) throws SystemException {
        // menuService.removeMenu(id);
        return ResultData.success("模拟删除成功~");
    }

    /**
     * 查询当前角色所拥有的所有的菜单以及按钮权限
     * @param
     * @return
     */
    @PostMapping("/role/query")
    @ApiOperation(value = "查询当前角色所拥有的所有的菜单以及按钮权限")
    public ResultData queryRoleMenuList(@RequestBody Role role) {
        List<Integer> menuIdList = menuService.queryRoleMenuList(role);
        return ResultData.success(menuIdList);
    }
}
