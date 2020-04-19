package com.artemka091102.explosion;

class Triple {
    int a, b, c;

    Triple(int aa, int bb, int cc) {
        a = aa;
        b = bb;
        c = cc;
    }

    public boolean equals(Object arg){
        if(this==arg)return true;
        if(arg==null)return false;
        if(arg instanceof Triple){
            Triple other = (Triple)arg;
            return this.a==other.a && this.b==other.b && this.c==other.c;
        }
        return false;
    }

    public int hashCode(){
      int res=1;
      res = res*1000000007 + a;
      res = res*1000000007 + b;
      res = res*1000000007 + c;
      //any other combination is valid as long as it includes only constants, a, b and c

      return res;
    }
}