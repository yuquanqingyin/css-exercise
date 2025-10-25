<template>
  <RelationGraph :data="graphData" />
</template>

<script>
import RelationGraph from "@/components/RelationGraph.vue";


export default {
  name: "HomeView",
  components: { 
    RelationGraph,
   },
  data() {
    return {
      graphData: {
        nodes: [
          { id: "1", name: "张三" },
          { id: "2", name: "李四" },
          { id: "3", name: "王五" },
          { id: "4", name: "赵六" },
        ],
        links: [
          { source: "1", target: "2" },
          { source: "2", target: "3" },
          { source: "3", target: "4" },
          { source: "1", target: "4" },
        ],
      },
    };
  },

  setup(){

    

  }
};
</script>
