import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

class Edge implements Comparable<Edge>{
    int a, b, cost;
    Edge(int a, int b, int cost){
        this.cost = cost; this.a = a; this.b =b;
    }
    
    public int compareTo(Edge x){
        return Integer.compare(this.cost, x.cost);
    }
}

public class PrimsMST {
    static int[] ar;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt(), m = sc.nextInt();
        ar = new int[n+1];
        ArrayList<ArrayList<Edge>> adj = new ArrayList<ArrayList<Edge>>(n+1);
        for(int i=0; i<n+1; i++)
            adj.add(new ArrayList<Edge>());
        Arrays.fill(ar , -1);
        while(m-- > 0){
            int a = sc.nextInt(), b = sc.nextInt(), cost = sc.nextInt();
            Edge x = new Edge(a, b, cost);
            adj.get(a).add(new Edge(a, b, cost));
            adj.get(b).add(new Edge(b, a, cost));
        }
        int s = sc.nextInt();
        int count = 0, sum = 0;
        PriorityQueue<Edge> pq = new PriorityQueue<Edge>();
        Iterator<Edge> it = adj.get(s).iterator();
        while(it.hasNext()){
            pq.add(it.next());
        }
        
        while(count != n-1){
            Edge curr = pq.remove();
            int a = curr.a, b = curr.b;
            if(getPar(a) != getPar(b)){
                count++;
                sum = sum + curr.cost;
                union(a, b);
                Iterator<Edge> it2 = adj.get(b).iterator();
                while(it2.hasNext())
                    pq.add(it2.next());
            }
        }
        System.out.println(sum);
    }
    
    static int getPar(int a){
        while(ar[a] != -1)
            a = ar[a];
        return a;
    }
    
    static void union(int a, int b){
        ar[getPar(b)] = getPar(a);
    }
}