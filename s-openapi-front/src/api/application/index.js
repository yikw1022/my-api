import request from "../request.js";

//请求前缀
const PREFIX = "/system/application";

export function saveApplication(data) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/create/save`,
    method: "POST",
    data: data,
  });
}

export function findApplications() {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/query/number`,
    method: "GET",
  });
}

export function queryApplicationsByUserId(userId) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/query/user/number/${userId}`,
    method: "GET",
  });
}

export function applicationEchoById(id) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/app/info/${id}`,
    method: "GET",
  });
}

export function updateApp(data) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/edit`,
    method: "PUT",
    data: data,
  });
}

export function queryApplicationList(userId) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/app/list/${userId}`,
    method: "GET"
  });
}


export function removeApplication(id) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/remove/${id}`,
    method: "DELETE",
  });
}

export function updateStatus(id) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/update/status/${id}`,
    method: "PUT",
  });
}