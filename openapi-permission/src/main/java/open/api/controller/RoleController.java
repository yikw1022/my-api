package open.api.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import open.api.exception.SystemException;
import open.api.model.entity.Role;
import open.api.model.vo.UserVo;
import open.api.response.ResultData;
import open.api.service.IRoleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
@RequestMapping("/permission/role")
@Api(tags = "角色管理模块")
public class RoleController {

    private final IRoleService roleService;

    /**
     * 分页查询所有角色数据
     * @param
     * @return
     */
    @GetMapping("/list/{pageNo}/{pageSize}")
    @ApiOperation(value = "分页查询所有角色数据")
    public ResultData queryRoleListByPage(@PathVariable("pageNo") Integer pageNo,
                                          @PathVariable("pageSize") Integer pageSize,
                                          String roleName) {
        Map<String,Object> map = roleService.queryRoleListByPage(pageNo,pageSize,roleName);
        return ResultData.success(map);
    }

    /**
     * 查询所有角色数据
     * @param
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询所有角色数据")
    public ResultData queryRoleList() {
        List<String> roleList = roleService.queryRoleList();
        return ResultData.success(roleList);
    }

    /**
     * 新增角色
     * @param
     * @return
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增角色")
    public ResultData saveRole(@RequestBody Role role) {
        roleService.save(role);
        return ResultData.success();
    }

    /**
     * 修改角色信息
     * @return
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改角色信息")
    public ResultData updateRoleInfo(@RequestBody Role role) throws SystemException {
        roleService.updateById(role);
        return ResultData.success();
    }

    /**
     * 分配权限
     * @param
     * @return
     */
    @PostMapping("/save/permission/{roleId}")
    @ApiOperation(value = "分配权限")
    public ResultData savePermission(@PathVariable("roleId") Integer roleId,
                               @RequestBody List<Integer> menuIds) {
        roleService.savePermission(roleId,menuIds);
        return ResultData.success();
    }

    /**
     * 回显角色数据
     * @return
     */
    @GetMapping("/echo/{id}")
    @ApiOperation(value = "回显角色数据")
    public ResultData echoRoleById(@PathVariable("id") Integer id){
        Role role = roleService.getById(id);
        return ResultData.success(role);
    }

    /**
     * TODO: 测试环境下禁用 删除角色
     * @return
     */
    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除角色")
    public ResultData deleteByRoleId(@PathVariable("id") Integer id){
        // roleService.removeById(id);
        return ResultData.success("模拟删除成功~");
    }
}
