import request from "../request.js";

//请求前缀
const PREFIX = "/interface/info";

export function saveInterfaceInfo(data) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/save`,
    method: "POST",
    data: data,
  });
}

export function findInterfaceList(pageNo, pageSize, searchDto) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/list/${pageNo}/${pageSize}`,
    method: "GET",
    params: {
      name: searchDto.name,
      method: searchDto.method,
      description: searchDto.description
    },
  });
}

export function findAdminInterfaceList(pageNo, pageSize, searchDto) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/admin/list/${pageNo}/${pageSize}`,
    method: "GET",
    params: {
      name: searchDto.name,
      method: searchDto.method,
      description: searchDto.description,
    },
  });
}

export function findInterfaces() {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/query/number`,
    method: "GET",
  });
}

export function publishInterface(data) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/publish`,
    method: "POST",
    data: data
  });
}

export function onlineInvokeInterface(data) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/online/invoke`,
    method: "POST",
    data: data,
  });
}

export function lineInterface(id) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/line/${id}`,
    method: "GET"
  });
}

export function queryTopFive() {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/query/top/five`,
    method: "GET",
  });
}

export function queryTopTenUser() {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/query/top/ten/user`,
    method: "GET",
  });
}

export function interfaceEchoById(id) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/echo/${id}`,
    method: "GET",
  });
}

export function updateInterfaceInfo(data) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/edit`,
    method: "PUT",
    data: data,
  });
}


export function removeInterface(id) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/remove/${id}`,
    method: "DELETE",
  });
}