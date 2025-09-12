package net.ofts.vecCalc.calc;

import java.util.Stack;

public class StringMatcher {
    public String str;
    public Stack<Integer> stack;
    public int index;
    public int length;

    public StringMatcher(String str){
        this.str = str;
        this.index = 0;
        this.length = str.length();
        this.stack = new Stack<>();
    }

    public boolean isEnd() {return index == length;}
    public boolean isClear() {return stack.empty();}

    public void trim(){
        if (index == length) return;
        char current = str.charAt(index);
        while(current == ' ' || current == '\n' || current == '\t' || current == '\r'){
            index ++;
            if (index == length) return;
            current = str.charAt(index);
        }
    }

    public String get(int n){
        if (index + n <= length) return str.substring(index, index + n);
        return "";
    }

    public String getIfExist(int n){
        return str.substring(index, Math.min(index + n, length));
    }

    public void skip(int n){
        index += n;
        trim();
    }

    public boolean match(String str){
        boolean matched = get(str.length()).equals(str);
        if (matched) skip(str.length());
        return matched;
    }

    public boolean matchIgnoreCase(String str){
        return match(str.toUpperCase()) || match(str.toLowerCase());
    }

    public void push(){
        stack.push(index);
    }

    public void pop(){
        index = stack.pop();
    }

    public void ignore() {stack.pop();}
}
