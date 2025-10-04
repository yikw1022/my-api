import request from "../request.js";

//请求前缀
const PREFIX = "/lines/plan";

export function save(data) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/save`,
    method: "POST",
    data: data
  });
}

export function edit(data) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/edit`,
    method: "POST",
    data: data,
  });
}

export function echo(id) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/echo/${id}`,
    method: "GET"
  });
}

export function queryPlanData(name) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/list`,
    method: "GET",
    params: {
      name
    }
  });
}

export function remove(id) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/delete/${id}`,
    method: "DELETE",
  });
}