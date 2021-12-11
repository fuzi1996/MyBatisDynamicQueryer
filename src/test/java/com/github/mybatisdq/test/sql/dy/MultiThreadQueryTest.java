package com.github.mybatisdq.test.sql.dy;

import com.github.mybatisdq.MyBatisDynamicQueryer;
import com.github.mybatisdq.cache.GlobalConfigurationCache;
import com.github.mybatisdq.constant.DynamicSelectConstant;
import com.github.mybatisdq.test.demo.student.mapper.StudentMapper;
import com.github.mybatisdq.test.util.SqlSessionUtil;
import com.github.mybatisdq.test.util.TestUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class MultiThreadQueryTest {
    @Test
    public void multiThreadTest(){
        MyBatisDynamicQueryer dynamicQueryer = TestUtil.getMyBatisDynamicQueryer();
        int MAX_COUNT = 10000;
        CountDownLatch countDownLatch = new CountDownLatch(MAX_COUNT);
        ArrayList<Thread> threads = new ArrayList<>(MAX_COUNT);
        Random random = new Random();
        AtomicInteger currentCount = new AtomicInteger();
        AtomicInteger unCurrentCount = new AtomicInteger();
        ArrayList<String> unCurrentInfo = new ArrayList<>();
        for(int i=0;i<MAX_COUNT;i++){
            threads.add(new MultiThreadQueryer(countDownLatch,dynamicQueryer,random,currentCount,unCurrentCount,unCurrentInfo));
        }
        threads.stream().forEach(t->{
            t.start();
        });
        try {
            countDownLatch.await();
        }catch (Exception e){
            e.printStackTrace();
        }
        unCurrentInfo.stream().forEach(System.out::println);
        System.out.println(String.format("currentCount:%s,uncurrentCount:%s,total:%s",currentCount.get(),unCurrentCount.get(),currentCount.get()+unCurrentCount.get()));
        Assert.assertEquals(MAX_COUNT,currentCount.get());
    }

    @Test
    public void sqlProviderMultiThreadTest(){
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        GlobalConfigurationCache.setConfiguration(sqlSession.getConfiguration());

        int MAX_COUNT = 100;
        CountDownLatch countDownLatch = new CountDownLatch(MAX_COUNT);
        ArrayList<Thread> threads = new ArrayList<>(MAX_COUNT);
        Random random = new Random();
        AtomicInteger currentCount = new AtomicInteger();
        AtomicInteger unCurrentCount = new AtomicInteger();
        ArrayList<String> unCurrentInfo = new ArrayList<>();
        for(int i=0;i<MAX_COUNT;i++){
            threads.add(new MultiThreadQueryerBySqlProvider(countDownLatch,random,currentCount,unCurrentCount,unCurrentInfo));
        }
        threads.stream().forEach(t->{
            t.start();
        });
        try {
            countDownLatch.await();
        }catch (Exception e){
            e.printStackTrace();
        }
        unCurrentInfo.stream().forEach(System.out::println);
        System.out.println(String.format("currentCount:%s,uncurrentCount:%s,total:%s",currentCount.get(),unCurrentCount.get(),currentCount.get()+unCurrentCount.get()));
        Assert.assertEquals(MAX_COUNT,currentCount.get());
    }
}

class MultiThreadQueryer extends Thread{

    private CountDownLatch countDownLatch;
    private MyBatisDynamicQueryer dynamicQueryer;
    private Random random;
    private AtomicInteger currentCount;
    private AtomicInteger unCurrentCount;
    private ArrayList<String> unCurrentInfo;

    public MultiThreadQueryer(CountDownLatch countDownLatch,MyBatisDynamicQueryer dynamicQueryer,Random random,AtomicInteger currentCount,AtomicInteger unCurrentCount,ArrayList<String> unCurrentInfo){
        this.countDownLatch = countDownLatch;
        this.dynamicQueryer = dynamicQueryer;
        this.random = random;
        this.currentCount = currentCount;
        this.unCurrentCount = unCurrentCount;
        this.unCurrentInfo = unCurrentInfo;
    }

    @Override
    public void run() {
        String sql = "select\n<include refid=\"com.github.mybatisdq.test.demo.student.mapper.StudentMapper.selectColumns\">\n<property name=\"alias\" value=\"t1\"/>\n</include>\nfrom students t1\nwhere id = #{id}";
        Map<String,Object> map = new HashMap<>();
        int id = (random.nextInt(11)+1);
        map.put("id",id);
        List<Map> result = new ArrayList<>();
        try {
            result = this.dynamicQueryer.selectList(sql, map,Map.class);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            countDownLatch.countDown();
            if(null != result && !result.isEmpty()){
                long resultId = (long)result.get(0).get("id");
                if(id != resultId){
                    unCurrentInfo.add(String.format("id:%s,result id:%s",id,resultId));
                    unCurrentCount.incrementAndGet();
                }else{
                    currentCount.incrementAndGet();
                }
            }else{
                unCurrentCount.incrementAndGet();
                if(null != result){
                    unCurrentInfo.add(String.format("id:%s,result isEmpty,count:%s",id,countDownLatch.getCount()));
                }else{
                    unCurrentInfo.add(String.format("id:%s,result is null,count:%s",id,countDownLatch.getCount()));
                }
            }
        }
    }
}

class MultiThreadQueryerBySqlProvider extends Thread{

    private CountDownLatch countDownLatch;
    private Random random;
    private AtomicInteger currentCount;
    private AtomicInteger unCurrentCount;
    private ArrayList<String> unCurrentInfo;

    public MultiThreadQueryerBySqlProvider(CountDownLatch countDownLatch,Random random,AtomicInteger currentCount,AtomicInteger unCurrentCount,ArrayList<String> unCurrentInfo){
        this.countDownLatch = countDownLatch;
        this.random = random;
        this.currentCount = currentCount;
        this.unCurrentCount = unCurrentCount;
        this.unCurrentInfo = unCurrentInfo;
    }

    @Override
    public void run() {
        String sql = "select\n<include refid=\"com.github.mybatisdq.test.demo.student.mapper.StudentMapper.selectColumns\">\n<property name=\"alias\" value=\"t1\"/>\n</include>\nfrom students t1\nwhere id = #{id}";
        Map<String,Object> map = new HashMap<>();
        int id = (random.nextInt(11)+1);
        map.put("id",id);
        List<Map<String,Object>> result = null;
        StudentMapper studentMapper = TestUtil.getStudentMapper();
        try {
            map.put(DynamicSelectConstant.getDefaultSqlValueKey(),sql);
            result = studentMapper.queryBySqlProvider(map);
            System.out.println(String.format("%s查询结果:%s",currentCount.get(),result));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            countDownLatch.countDown();
            if(null != result && !result.isEmpty()){
                long resultId = (long)result.get(0).get("id");
                if(id != resultId){
                    unCurrentInfo.add(String.format("id:%s,result id:%s",id,resultId));
                    unCurrentCount.incrementAndGet();
                }else{
                    currentCount.incrementAndGet();
                }
            }else{
                unCurrentCount.incrementAndGet();
                if(null != result){
                    unCurrentInfo.add(String.format("id:%s,result isEmpty,count:%s",id,countDownLatch.getCount()));
                }else{
                    unCurrentInfo.add(String.format("id:%s,result is null,count:%s",id,countDownLatch.getCount()));
                }
            }
        }
    }
}
