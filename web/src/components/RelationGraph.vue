<template>
  <div ref="chart" class="graph-container"></div>
</template>

<script>
import * as d3 from "d3";
import { useRouter } from "vue-router";

export default {
  name: "RelationGraph",
  props: {
    data: {
      type: Object,
      required: true,
    },
  },
  mounted() {
    const router = useRouter();
    const width = window.innerWidth;
    const height = window.innerHeight;

    const svg = d3
      .select(this.$refs.chart)
      .append("svg")
      .attr("width", width)
      .attr("height", height)
      .style("background", "radial-gradient(circle at center, #f8f9fa, #e9ecef)")
      .style("cursor", "grab");

    const simulation = d3
      .forceSimulation(this.data.nodes)
      .force(
        "link",
        d3
          .forceLink(this.data.links)
          .id((d) => d.id)
          .distance(180)
      )
      .force("charge", d3.forceManyBody().strength(-500))
      .force("center", d3.forceCenter(width / 2, height / 2));

    // 绘制连线
    const link = svg
      .append("g")
      .attr("stroke", "#adb5bd")
      .attr("stroke-width", 2)
      .selectAll("line")
      .data(this.data.links)
      .enter()
      .append("line");

    // 绘制节点
    const node = svg
      .append("g")
      .selectAll("circle")
      .data(this.data.nodes)
      .enter()
      .append("circle")
      .attr("r", 35)
      // 根据完成状态设置节点颜色：已完成=黄色，未完成=灰色
      .attr("fill", (d) => d.completed ? "#ffc107" : "#9e9e9e")
      .attr("stroke", (d) => d.completed ? "#ff9800" : "#757575")
      .attr("stroke-width", 2)
      .style("cursor", "pointer")
      // 添加阴影效果，让已完成的节点更突出
      .style("filter", (d) => d.completed ? "drop-shadow(0 2px 4px rgba(255, 193, 7, 0.4))" : "none")
      .on("mouseover", function(event, d) {
        d3.select(this)
          .transition()
          .duration(200)
          .attr("r", 40)
          // 鼠标悬停时增强阴影效果
          .style("filter", d.completed ? 
            "drop-shadow(0 4px 8px rgba(255, 193, 7, 0.6))" : 
            "drop-shadow(0 2px 4px rgba(0, 0, 0, 0.3))");
      })
      .on("mouseout", function(event, d) {
        d3.select(this)
          .transition()
          .duration(200)
          .attr("r", 35)
          .style("filter", d.completed ? 
            "drop-shadow(0 2px 4px rgba(255, 193, 7, 0.4))" : 
            "none");
      })
      .on("click", (event, d) => {
        // 点击节点时跳转到练习页面，传递练习 ID
        router.push({
          name: 'exercise',
          params: { id: d.id }
        });
      })
      .call(
        d3
          .drag()
          .on("start", dragstarted)
          .on("drag", dragged)
          .on("end", dragended)
      );

    // 绘制文字标签
    const label = svg
      .append("g")
      .selectAll("text")
      .data(this.data.nodes)
      .enter()
      .append("text")
      .text((d) => d.name)
      .attr("font-size", 14)
      .attr("fill", (d) => d.completed ? "#212529" : "#757575")
      .attr("font-family", "Microsoft YaHei, sans-serif")
      .attr("font-weight", (d) => d.completed ? "bold" : "normal")
      .attr("text-anchor", "middle")
      .attr("dy", 6)
      .style("pointer-events", "none");

    // 添加图例
    const legend = svg.append("g")
      .attr("transform", `translate(${width - 150}, 30)`);

    // 已完成图例
    legend.append("circle")
      .attr("cx", 0)
      .attr("cy", 0)
      .attr("r", 10)
      .attr("fill", "#ffc107")
      .attr("stroke", "#ff9800")
      .attr("stroke-width", 2);

    legend.append("text")
      .attr("x", 20)
      .attr("y", 5)
      .text("已完成")
      .attr("font-size", 14)
      .attr("fill", "#212529");

    // 未完成图例
    legend.append("circle")
      .attr("cx", 0)
      .attr("cy", 30)
      .attr("r", 10)
      .attr("fill", "#9e9e9e")
      .attr("stroke", "#757575")
      .attr("stroke-width", 2);

    legend.append("text")
      .attr("x", 20)
      .attr("y", 35)
      .text("未完成")
      .attr("font-size", 14)
      .attr("fill", "#212529");

    // 动画更新
    simulation.on("tick", () => {
      link
        .attr("x1", (d) => d.source.x)
        .attr("y1", (d) => d.source.y)
        .attr("x2", (d) => d.target.x)
        .attr("y2", (d) => d.target.y);

      node.attr("cx", (d) => d.x).attr("cy", (d) => d.y);
      label.attr("x", (d) => d.x).attr("y", (d) => d.y);
    });

    // 拖拽交互
    function dragstarted(event, d) {
      if (!event.active) simulation.alphaTarget(0.3).restart();
      d.fx = d.x;
      d.fy = d.y;
    }
    function dragged(event, d) {
      d.fx = event.x;
      d.fy = event.y;
    }
    function dragended(event, d) {
      if (!event.active) simulation.alphaTarget(0);
      d.fx = null;
      d.fy = null;
    }

    // 自适应窗口大小变化
    window.addEventListener("resize", () => {
      const newWidth = window.innerWidth;
      const newHeight = window.innerHeight;
      svg.attr("width", newWidth).attr("height", newHeight);
      simulation.force("center", d3.forceCenter(newWidth / 2, newHeight / 2));
      simulation.alpha(1).restart();
    });
  },
};
</script>

<style scoped>
.graph-container {
  width: 100vw;
  height: 100vh;
  overflow: hidden;
  background: linear-gradient(to bottom right, #ffffff, #f1f3f5);
}
</style>