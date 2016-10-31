package cn.jit.quickindex.bean;

import cn.jit.quickindex.util.PinyinUtils;

/**
 * 为了按姓名首字母排序,需要实现Comparable接口
 */
public class Person implements Comparable<Person> {

    private String name;
    private String pinyin;

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Person(String name) {
        this.name = name;
        this.pinyin = PinyinUtils.getPinyin(name);
    }

    @Override
    public int compareTo(Person o) {
        return this.pinyin.compareTo(o.getPinyin());
    }
}
