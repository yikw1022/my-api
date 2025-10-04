import request from "../request.js";

//请求前缀
const PREFIX = "/system/application/key";

export function saveApplicationKey(data) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/save`,
    method: "POST",
    data: data,
  });
}

export function queryKeysByAppId(applicationId) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/queryKey/${applicationId}`,
    method: "GET",
  });
}

export function queryKeysByUserId(userId) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/queryKey/byUserId/${userId}`,
    method: "GET",
  });
}

export function queryApplicationKeyByUserId(userId) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/query/user/number/${userId}`,
    method: "GET",
  });
}

export function queryApplicationList(userId) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/app/list/${userId}`,
    method: "GET"
  });
}

export function removeKey(id) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/remove/${id}`,
    method: "DELETE",
  });
}