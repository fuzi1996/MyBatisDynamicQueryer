package com.github.mybatisdq.test.demo.student.support;


import java.util.Map;

public class StudentMap {
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String GENDER = "gender";
    public static final String GRADE = "grade";
    public static final String SCORE = "score";

    private Map<String,Object> map;

    public StudentMap(Map<String,Object> map){
        this.map = map;
    }
    
    public Long getId(){
        return ((Long) this.map.get(ID));
    }

    public void setId(Long id){
        this.map.put(ID,id);
    }

    public String getName(){
        return ((String) this.map.get(NAME));
    }

    public void setName(String name){
        this.map.put(NAME,name);
    }

    public Integer getGender(){
        return ((Integer) this.map.get(GENDER));
    }

    public void setGender(Integer gender){
        this.map.put(GENDER,gender);
    }

    public Integer getGrade(){
        return ((Integer) this.map.get(GRADE));
    }

    public void setGrade(Integer grade){
        this.map.put(GRADE,grade);
    }

    public Integer getScore(){
        return ((Integer) this.map.get(SCORE));
    }

    public void setScore(Integer score){
        this.map.put(SCORE,score);
    }

    public Map<String,Object> getRealMap(){
        return this.map;
    }
}
