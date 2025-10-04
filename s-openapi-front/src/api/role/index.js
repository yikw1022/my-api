import request from "../request.js";

//请求前缀
const PREFIX = "/permission/role";

export function findRoleList(pageNo, pageSize, roleName) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/list/${pageNo}/${pageSize}`,
    method: "GET",
    params: {
      roleName,
    },
  });
}

export function queryRoleList() {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/list`,
    method: "GET",
  });
}

export function save(data) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/save`,
    method: "POST",
    data: data
  });
}

export function savePermission(data,roleId) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/save/permission/${roleId}`,
    method: "POST",
    data: data,
  });
}

export function queryEchoRoleInfo(id) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/echo/${id}`,
    method: "GET",
  });
}

export function updateRoleInfo(data) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/update`,
    method: "PUT",
    data: data,
  });
}

export function deleteRole(id) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/delete/${id}`,
    method: "DELETE",
  });
}