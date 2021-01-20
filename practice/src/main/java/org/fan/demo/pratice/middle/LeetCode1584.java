package org.fan.demo.pratice.middle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 给你一个points数组，表示 2D 平面上的一些点，其中points[i] = [xi, yi]。
 *
 * 连接点[xi, yi] 和点[xj, yj]的费用为它们之间的 曼哈顿距离：|xi - xj| + |yi - yj|，其中|val|表示val的绝对值。
 *
 * 请你返回将所有点连接的最小总费用。只有任意两点之间 有且仅有一条简单路径时，才认为所有点都已连接。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/min-cost-to-connect-all-points
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 * 
 * @version 1.0
 * @author: Fan
 * @date 2021.1.19 18:46
 */
public class LeetCode1584 {


  public int minCostConnectPoints(int[][] points) {
    int n = points.length;
    DisjointSetUnion dsu = new DisjointSetUnion(n);
    List<Edge> edges = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      for (int j = i + 1; j < n; j++) {
        edges.add(new Edge(dist(points, i, j), i, j));
      }
    }
    edges.sort(Comparator.comparingInt(edge -> edge.len));
    int ret = 0, num = 1;
    for (Edge edge : edges) {
      int len = edge.len, x = edge.x, y = edge.y;
      if (dsu.unionSet(x, y)) {
        ret += len;
        num++;
        if (num == n) {
          break;
        }
      }
    }
    return ret;
  }

  public int dist(int[][] points, int x, int y) {
    return Math.abs(points[x][0] - points[y][0]) + Math.abs(points[x][1] - points[y][1]);
  }
}
class DisjointSetUnion {
  int[] f;
  int[] rank;
  int n;

  public DisjointSetUnion(int n) {
    this.n = n;
    this.rank = new int[n];
    Arrays.fill(this.rank, 1);
    this.f = new int[n];
    for (int i = 0; i < n; i++) {
      this.f[i] = i;
    }
  }

  public int find(int x) {
    return f[x] == x ? x : (f[x] = find(f[x]));
  }

  public boolean unionSet(int x, int y) {
    int fx = find(x), fy = find(y);
    if (fx == fy) {
      return false;
    }
    if (rank[fx] < rank[fy]) {
      int temp = fx;
      fx = fy;
      fy = temp;
    }
    rank[fx] += rank[fy];
    f[fy] = fx;
    return true;
  }
}

class Edge {
  int len, x, y;

  public Edge(int len, int x, int y) {
    this.len = len;
    this.x = x;
    this.y = y;
  }
}
