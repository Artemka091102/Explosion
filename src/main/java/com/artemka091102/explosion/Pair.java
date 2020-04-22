package com.artemka091102.explosion;

class Pair {
    int a, b;

    Pair(int aa, int bb) {
        a = aa;
        b = bb;
    }

    public boolean equals(Object arg){
        if(this==arg)return true;
        if(arg==null)return false;
        if(arg instanceof Pair){
            Pair other = (Pair)arg;
            return this.a==other.a && this.b==other.b;
        }
        return false;
    }

    public int hashCode(){
      int res=1;
      res = res*1000000007 + a;
      res = res*1000000007 + b;
      //any other combination is valid as long as it includes only constants, a, b and c

      return res;
    }
}