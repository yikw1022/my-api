import request from "../request.js";

//请求前缀
const PREFIX = "/permission/menu";

export function queryMenuList() {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/list`,
    method: "GET",
  });
}

export function queryRoleMenu(data) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/role/query`,
    method: "POST",
    data: data
  });
}

export function saveDirectory(data) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/save/directory`,
    method: "POST",
    data: data,
  });
}

export function saveMenu(parentId,data) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/save/menu`,
    method: "POST",
    data: data,
    params: {
      parentId: parentId
    }
  });
}

export function saveMenuBtn(parentId, data) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/save/menu/button`,
    method: "POST",
    data: data,
    params: {
      parentId: parentId,
    },
  });
}

export function queryEchoMenuInfo(id) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/menu/echo/${id}`,
    method: "GET",
  });
}

export function queryEchoMenuBtnInfo(id) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/btn/echo/${id}`,
    method: "GET",
  });
}

export function updateMenuDirInfo(data) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/update/directory`,
    method: "PUT",
    data: data,
  });
}

export function updateMenuInfo(data) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/update/menu`,
    method: "PUT",
    data: data,
  });
}

export function updateMenuBtnInfo(data) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/update/menu/btn`,
    method: "PUT",
    data: data,
  });
}

export function deleteMenu(id) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/remove/${id}`,
    method: "DELETE"
  });
}  