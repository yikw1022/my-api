import request from "../request.js";

//请求前缀
const PREFIX = "/lines/order/info";

/**
 * 这是不对接任何支付渠道，只是模拟支付接口，后续对接支付渠道，可以直接删除
 */
export function testSave(data) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/test/save`,
    method: "POST",
    data: data
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

export function queryPay(data) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/pay`,
    method: "POST",
    data: data,
  });
}

export function queryOrderStatus(orderId) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/query`,
    method: "POST",
    params: {
      orderId
    },
  });
}

export function queryOrderData(userId,type,orderId) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/query/order/${userId}/${type}`,
    method: "GET",
    params: {
      orderId
    },
  });
}

export function updateOrderInfo(data) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/update`,
    method: "PUT",
    data: data
  });
}

export function echoOrderById(id) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/echo/${id}`,
    method: "GET",
  });
}

export function deleteOrderById(id) {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/remove/${id}`,
    method: "DELETE"
  });
}

export function findOrders() {
  return request({
    //模板字符串拼接参数
    url: `${PREFIX}/query/number`,
    method: "GET",
  });
}