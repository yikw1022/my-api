import request from "../request.js";

//请求前缀
const PREFIX = "/permission/user";

export function doLogin(data) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/login`,
    method: "POST",
    data: data,
  });
}

export function doEmailLogin(data) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/login/email`,
    method: "POST",
    data: data,
  });
}

export function doRegister(data) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/register`,
    method: "POST",
    data: data,
  });
}

export function doLogout(token) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/logout`,
    method: "POST",
    params: {
      token
    }
  });
}

export function getUserInfo(token) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/info`,
    method: "POST",
    params: {
      token
    }
  });
}

export function findRegisterUserNum() {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/query/register`,
    method: "GET",
  });
}

export function findUserList(pageNo,pageSize,param) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/list/${pageNo}/${pageSize}`,
    method: "GET",
    params: param,
  });
}

export function queryRoles(userId) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/roles/${userId}`,
    method: "GET",
  });
}

export function queryHomeEchoUserInfo(userId) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/home/echo/${userId}`,
    method: "GET",
  });
}

export function queryEchoUserInfo(userId) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/echo/${userId}`,
    method: "GET",
  });
}

export function updateUserInfo(data) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/update`,
    method: "PUT",
    data: data
  });
}

export function saveRoles(username, data) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/save/roles/${username}`,
    method: "POST",
    data: data,
  });
}

export function deleteUser(userId) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/delete/${userId}`,
    method: "DELETE"
  });
}

export function sendMs(email) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/send/code`,
    method: "GET",
    params: {
      email
    }
  });
}