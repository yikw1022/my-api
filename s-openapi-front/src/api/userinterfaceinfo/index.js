import request from "../request.js";

//请求前缀
const PREFIX = "/interface/user/info";

export function findUserInterfaces(userId) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/invoke/count/${userId}`,
    method: "GET",
  });
}

export function queryInvokeDetailByUserId(userId) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/query/detail/invoke/${userId}`,
    method: "GET",
  });
}

export function queryOneDayTraffic(userId) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/query/interface/traffic/${userId}`,
    method: "GET",
  });
}

export function queryThirtyDayTraffic(userId) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/query/thirtyDay/traffic/${userId}`,
    method: "GET",
  });
}

export function querySixMonthTraffic(userId) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/query/six/month/traffic/${userId}`,
    method: "GET",
  });
}