import request from "../request.js";

//请求前缀
const PREFIX = "/system/ip/white";

export function saveIpWhiteList(data) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/save`,
    method: "POST",
    data: data,
  });
}

export function queryIpWhiteList(keyId) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/list/${keyId}`,
    method: "GET",
  });
}

export function removeIPWhite(id) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/remove/${id}`,
    method: "DELETE",
  });
}