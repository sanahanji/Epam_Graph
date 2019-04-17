import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Queue;

public class RustAndMurderer {
    static InputStream is;
    static PrintWriter out;
    static String INPUT = "";
    
    static void solve()
    {
        for(int T = ni();T >= 1;T--){
            int n = ni(), m = ni();
            int[] from = new int[m];
            int[] to = new int[m];
            for(int i = 0;i < m;i++){
                from[i] = ni()-1;
                to[i] = ni()-1;
            }
            int[][] g = packU(n, from, to);
            for(int[] row : g){
                Arrays.sort(row);
            }
            int f = ni()-1;
            int[] d = new int[n];
            Arrays.fill(d, 9999999);
            d[f] = 0;
            Queue<Integer> q = new ArrayDeque<Integer>();
            q.add(f);
            LST lst = new LST(n);
            lst.setRange(n);
            lst.unset(f);
            while(!q.isEmpty()){
                int cur = q.poll();
                int p = 0;
                for(int i = lst.next(0);i != -1;i = lst.next(i+1)){
                    while(p < g[cur].length && g[cur][p] < i)p++;
                    if(p < g[cur].length && g[cur][p] == i)continue;
                    if(d[i] > d[cur] + 1){
                        d[i] = d[cur] + 1;
                        q.add(i);
                        lst.unset(i);
                    }
                }
            }
            
            StringBuilder sb = new StringBuilder();
            for(int i = 0;i < n;i++){
                if(d[i] > 0){
                    sb.append(" " + d[i]);
                }
            }
            out.println(sb.substring(1));
        }
    }
    
    public static class LST {
        public long[][] set;
        public int n;
//        public int size;
        
        public LST(int n) {
            this.n = n;
            int d = 1;
            for(int m = n;m > 1;m>>>=6, d++);
            
            set = new long[d][];
            for(int i = 0, m = n>>>6;i < d;i++, m>>>=6){
                set[i] = new long[m+1];
            }
//            size = 0;
        }
        
        // [0,r)
        public LST setRange(int r)
        {
            for(int i = 0;i < set.length;i++, r=r+63>>>6){
                for(int j = 0;j < r>>>6;j++){
                    set[i][j] = -1L;
                }
                if((r&63) != 0)set[i][r>>>6] |= (1L<<r)-1;
            }
            return this;
        }
        
        // [0,r)
        public LST unsetRange(int r)
        {
            if(r >= 0){
                for(int i = 0;i < set.length;i++, r=r+63>>>6){
                    for(int j = 0;j < r+63>>>6;j++){
                        set[i][j] = 0;
                    }
                    if((r&63) != 0)set[i][r>>>6] &= ~((1L<<r)-1);
                }
            }
            return this;
        }
        
        public LST set(int pos)
        {
            if(pos >= 0 && pos < n){
//                if(!get(pos))size++;
                for(int i = 0;i < set.length;i++, pos>>>=6){
                    set[i][pos>>>6] |= 1L<<pos;
                }
            }
            return this;
        }
        
        public LST unset(int pos)
        {
            if(pos >= 0 && pos < n){
//                if(get(pos))size--;
                for(int i = 0;i < set.length && (i == 0 || set[i-1][pos] == 0L);i++, pos>>>=6){
                    set[i][pos>>>6] &= ~(1L<<pos);
                }
            }
            return this;
        }
        
        public boolean get(int pos)
        {
            return pos >= 0 && pos < n && set[0][pos>>>6]<<~pos<0;
        }
        
        public int prev(int pos)
        {
            for(int i = 0;i < set.length && pos >= 0;i++, pos>>>=6, pos--){
                int pre = prev(set[i][pos>>>6], pos&63);
                if(pre != -1){
                    pos = pos>>>6<<6|pre;
                    while(i > 0)pos = pos<<6|63-Long.numberOfLeadingZeros(set[--i][pos]);
                    return pos;
                }
            }
            return -1;
        }
        
        public int next(int pos)
        {
            for(int i = 0;i < set.length && pos>>>6 < set[i].length;i++, pos>>>=6, pos++){
                int nex = next(set[i][pos>>>6], pos&63);
                if(nex != -1){
                    pos = pos>>>6<<6|nex;
                    while(i > 0)pos = pos<<6|Long.numberOfTrailingZeros(set[--i][pos]);
                    return pos;
                }
            }
            return -1;
        }
        
        private static int prev(long set, int n)
        {
            long h = Long.highestOneBit(set<<~n);
            if(h == 0L)return -1;
            return Long.numberOfTrailingZeros(h)-(63-n);
        }
        
        private static int next(long set, int n)
        {
            long h = Long.lowestOneBit(set>>>n);
            if(h == 0L)return -1;
            return Long.numberOfTrailingZeros(h)+n;
        }
        
        @Override
        public String toString()
        {
            List<Integer> list = new ArrayList<Integer>();
            for(int pos = next(0);pos != -1;pos = next(pos+1)){
                list.add(pos);
            }
            return list.toString();
        }
    }
    
    static int[][] packU(int n, int[] from, int[] to) {
        int[][] g = new int[n][];
        int[] p = new int[n];
        for (int f : from)
            p[f]++;
        for (int t : to)
            p[t]++;
        for (int i = 0; i < n; i++)
            g[i] = new int[p[i]];
        for (int i = 0; i < from.length; i++) {
            g[from[i]][--p[from[i]]] = to[i];
            g[to[i]][--p[to[i]]] = from[i];
        }
        return g;
    }
    
    public static void main(String[] args) throws Exception
    {
        long S = System.currentTimeMillis();
        is = INPUT.isEmpty() ? System.in : new ByteArrayInputStream(INPUT.getBytes());
        out = new PrintWriter(System.out);
        
        solve();
        out.flush();
        long G = System.currentTimeMillis();
        tr(G-S+"ms");
    }
    
    private static boolean eof()
    {
        if(lenbuf == -1)return true;
        int lptr = ptrbuf;
        while(lptr < lenbuf)if(!isSpaceChar(inbuf[lptr++]))return false;
        
        try {
            is.mark(1000);
            while(true){
                int b = is.read();
                if(b == -1){
                    is.reset();
                    return true;
                }else if(!isSpaceChar(b)){
                    is.reset();
                    return false;
                }
            }
        } catch (IOException e) {
            return true;
        }
    }
    
    private static byte[] inbuf = new byte[1024];
    static int lenbuf = 0, ptrbuf = 0;
    
    private static int readByte()
    {
        if(lenbuf == -1)throw new InputMismatchException();
        if(ptrbuf >= lenbuf){
            ptrbuf = 0;
            try { lenbuf = is.read(inbuf); } catch (IOException e) { throw new InputMismatchException(); }
            if(lenbuf <= 0)return -1;
        }
        return inbuf[ptrbuf++];
    }
    
    private static boolean isSpaceChar(int c) { return !(c >= 33 && c <= 126); }
    private static int skip() { int b; while((b = readByte()) != -1 && isSpaceChar(b)); return b; }
    
    private static double nd() { return Double.parseDouble(ns()); }
    private static char nc() { return (char)skip(); }
    
    private static String ns()
    {
        int b = skip();
        StringBuilder sb = new StringBuilder();
        while(!(isSpaceChar(b))){ // when nextLine, (isSpaceChar(b) && b != ' ')
            sb.appendCodePoint(b);
            b = readByte();
        }
        return sb.toString();
    }
    
    private static char[] ns(int n)
    {
        char[] buf = new char[n];
        int b = skip(), p = 0;
        while(p < n && !(isSpaceChar(b))){
            buf[p++] = (char)b;
            b = readByte();
        }
        return n == p ? buf : Arrays.copyOf(buf, p);
    }
    
    private static char[][] nm(int n, int m)
    {
        char[][] map = new char[n][];
        for(int i = 0;i < n;i++)map[i] = ns(m);
        return map;
    }
    
    private static int[] na(int n)
    {
        int[] a = new int[n];
        for(int i = 0;i < n;i++)a[i] = ni();
        return a;
    }
    
    private static int ni()
    {
        int num = 0, b;
        boolean minus = false;
        while((b = readByte()) != -1 && !((b >= '0' && b <= '9') || b == '-'));
        if(b == '-'){
            minus = true;
            b = readByte();
        }
        
        while(true){
            if(b >= '0' && b <= '9'){
                num = num * 10 + (b - '0');
            }else{
                return minus ? -num : num;
            }
            b = readByte();
        }
    }
    
    private static long nl()
    {
        long num = 0;
        int b;
        boolean minus = false;
        while((b = readByte()) != -1 && !((b >= '0' && b <= '9') || b == '-'));
        if(b == '-'){
            minus = true;
            b = readByte();
        }
        
        while(true){
            if(b >= '0' && b <= '9'){
                num = num * 10 + (b - '0');
            }else{
                return minus ? -num : num;
            }
            b = readByte();
        }
    }
    
    private static void tr(Object... o) { if(INPUT.length() != 0)System.out.println(Arrays.deepToString(o)); }
}