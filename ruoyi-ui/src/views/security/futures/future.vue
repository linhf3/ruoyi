<template>
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          size="mini"
          @click="refresh"
          v-hasPermi="['security:future:start']"
        >开始</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          size="mini"
          @click="stop"
          v-hasPermi="['security:future:stop']"
        >停止</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="futuresList" :row-class-name="rowClassName" >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="编码" align="center" prop="code" />
      <el-table-column label="名称" align="center" prop="name" />
      <el-table-column label="数值" align="center" prop="price" />
      <el-table-column label="偏离" align="center" prop="proportion" />
      <el-table-column label="振幅" align="center" prop="dailySpread" />
      <el-table-column label="最大点数" align="center" prop="dianshu" />
      <el-table-column label="上振" align="center" prop="up" />
      <el-table-column label="下振" align="center" prop="down" />
      <el-table-column label="波动提示值" align="center" prop="undulate" />
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

  </div>
</template>

<script>
import {findList} from "@/api/security/future";

export default {
  name: "Future",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 证劵交易数据源表格数据
      futuresList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10
      }
    };
  },
  created() {
    this.refresh();
  },
  methods: {
    /** 查询证劵交易数据源列表 */
    getList() {
      //this.loading = true;
      findList().then(response => {
        this.futuresList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },

    rowClassName({ row }) {
      if (row.positiveNegativeFlag === 1) {
        return 'row-positiveNegativeFlag';
      }
      return '';
    },
    /** 爬取数据按钮操作 */
    handleCrawl() {
      crawl();
      this.getList();
    },
    refresh() { // 从服务端加载数据的函数
      if(this.futuresList.length <= 0){
        this.getList();
      }
      // 实现轮询
      this.clearTimeSet = setInterval(()=>{this.getList(),this.time}, 4000);
    },
    stop(){
      clearInterval(this.clearTimeSet);
    }


  }
};
</script>

<style>
.row-positiveNegativeFlag {
  color: red;
}
</style>
