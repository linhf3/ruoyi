import request from '@/utils/request'

// 查询证劵交易数据源列表
export function listFutures(query) {
  return request({
    url: '/security/futures/list',
    method: 'get',
    params: query
  })
}

// 查询证劵交易数据源详细
export function getFutures(id) {
  return request({
    url: '/security/futures/' + id,
    method: 'get'
  })
}

// 新增证劵交易数据源
export function addFutures(data) {
  return request({
    url: '/security/futures',
    method: 'post',
    data: data
  })
}

// 修改证劵交易数据源
export function updateFutures(data) {
  return request({
    url: '/security/futures',
    method: 'put',
    data: data
  })
}

// 删除证劵交易数据源
export function delFutures(id) {
  return request({
    url: '/security/futures/' + id,
    method: 'delete'
  })
}

// 爬取证劵交易数据
export function crawl() {
  return request({
    url: '/security/futures/crawl',
    method: 'get'
  })
}
