package com.zy.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;   

@SuppressWarnings("rawtypes")
public class CacheManager {   
	private static HashMap cacheMap = new HashMap();   
  
    //��ʵ�����췽��   
    private CacheManager() {   
        super();   
    }   
    //��ȡ����ֵ�Ļ���   
    public static boolean getSimpleFlag(String key){   
        try{   
            return (Boolean) cacheMap.get(key);   
        }catch(NullPointerException e){   
            return false;   
        }   
    }   
    public static long getServerStartdt(String key){   
        try {   
            return (Long)cacheMap.get(key);   
        } catch (Exception ex) {   
            return 0;   
        }   
    }   
    //���ò���ֵ�Ļ���   
    @SuppressWarnings("unchecked")
	public synchronized static boolean setSimpleFlag(String key,boolean flag){   
        if (flag && getSimpleFlag(key)) {//����Ϊ�治��������   
            return false;   
        }else{   
            cacheMap.put(key, flag);   
            return true;   
        }   
    }   
    
    @SuppressWarnings("unchecked")
	public synchronized static boolean setSimpleFlag(String key,long serverbegrundt){   
        if (cacheMap.get(key) == null) {   
            cacheMap.put(key,serverbegrundt);   
            return true;   
        }else{   
            return false;   
        }   
    }   
  
  
    //�õ����档ͬ����̬����   
    private synchronized static Cache getCache(String key) {   
        return (Cache) cacheMap.get(key);   
    }   
  
    //�ж��Ƿ����һ������   
    private synchronized static boolean hasCache(String key) {   
        return cacheMap.containsKey(key);   
    }   
  
    //������л���   
    public synchronized static void clearAll() {   
        cacheMap.clear();   
    }   
  
    //���ĳһ���ض�����,ͨ������HASHMAP�µ����ж������ж�����KEY�봫���TYPE�Ƿ�ƥ��   
    public synchronized static void clearAll(String type) {   
        Iterator i = cacheMap.entrySet().iterator();   
        String key;   
        ArrayList<String> arr = new ArrayList<String>();   
        try {   
            while (i.hasNext()) {   
                java.util.Map.Entry entry = (java.util.Map.Entry) i.next();   
                key = (String) entry.getKey();   
                if (key.startsWith(type)) { //���ƥ����ɾ����   
                    arr.add(key);   
                }   
            }   
            for (int k = 0; k < arr.size(); k++) {   
                clearOnly(arr.get(k));   
            }   
        } catch (Exception ex) {   
            ex.printStackTrace();   
        }   
    }   
  
    //���ָ���Ļ���   
    public synchronized static void clearOnly(String key) {   
        cacheMap.remove(key);   
    }   
  
    //���뻺��   
    @SuppressWarnings("unchecked")
	public synchronized static void putCache(String key, Cache obj) {   
        cacheMap.put(key, obj);   
    }   
  
    //��ȡ������Ϣ   
    public static Cache getCacheInfo(String key) {   
  
        if (hasCache(key)) {   
            Cache cache = getCache(key);   
            if (cacheExpired(cache)) { //�����ж��Ƿ���ֹ����   
                cache.setExpired(true);   
            }   
            return cache;   
        }else  
            return null;   
    }   
  
    //���뻺����Ϣ   
    @SuppressWarnings("unchecked")
	public static void putCacheInfo(String key, Cache obj, long dt,boolean expired) {   
        Cache cache = new Cache();   
        cache.setKey(key);   
        cache.setTimeOut(dt + System.currentTimeMillis()); //���ö�ú���»���   
        cache.setValue(obj);   
        cache.setExpired(expired); //����Ĭ������ʱ����ֹ״̬ΪFALSE   
        cacheMap.put(key, cache);   
    }   
    //��д���뻺����Ϣ����   
    @SuppressWarnings("unchecked")
	public static void putCacheInfo(String key,Cache obj,long dt){   
        Cache cache = new Cache();   
        cache.setKey(key);   
        cache.setTimeOut(dt+System.currentTimeMillis());   
        cache.setValue(obj);   
        cache.setExpired(false);   
        cacheMap.put(key,cache);   
    }   
  
    //�жϻ����Ƿ���ֹ   
    public static boolean cacheExpired(Cache cache) {   
        if (null == cache) { //����Ļ��治����   
            return false;   
        }   
        long nowDt = System.currentTimeMillis(); //ϵͳ��ǰ�ĺ�����   
        long cacheDt = cache.getTimeOut(); //�����ڵĹ��ں�����   
        if (cacheDt <= 0||cacheDt>nowDt) { //����ʱ��С�ڵ�����ʱ,���߹���ʱ����ڵ�ǰʱ��ʱ����ΪFALSE   
            return false;   
        } else { //���ڹ���ʱ�� ������   
            return true;   
        }   
    }   
  
    //��ȡ�����еĴ�С   
    public static int getCacheSize() {   
        return cacheMap.size();   
    }   
  
    //��ȡָ�������͵Ĵ�С   
    public static int getCacheSize(String type) {   
        int k = 0;   
        Iterator i = cacheMap.entrySet().iterator();   
        String key;   
        try {   
            while (i.hasNext()) {   
                java.util.Map.Entry entry = (java.util.Map.Entry) i.next();   
                key = (String) entry.getKey();   
                if (key.indexOf(type) != -1) { //���ƥ����ɾ����   
                    k++;   
                }   
            }   
        } catch (Exception ex) {   
            ex.printStackTrace();   
        }   
  
        return k;   
    }   
  
    //��ȡ��������е����м�ֵ����   
    @SuppressWarnings({ "unchecked", "finally" })
	public static ArrayList<String> getCacheAllkey() {   
        ArrayList a = new ArrayList();   
        try {   
            Iterator i = cacheMap.entrySet().iterator();   
            while (i.hasNext()) {   
                java.util.Map.Entry entry = (java.util.Map.Entry) i.next();   
                a.add((String) entry.getKey());   
            }   
        } catch (Exception ex) {
        	ex.printStackTrace();
        } 
        finally {   
            return a;   
        } 
    }   
  
    //��ȡ���������ָ������ �ļ�ֵ����   
    @SuppressWarnings({ "unchecked", "finally" })
	public static ArrayList<String> getCacheListkey(String type) {   
        ArrayList a = new ArrayList();   
        String key;   
        try {   
            Iterator i = cacheMap.entrySet().iterator();   
            while (i.hasNext()) {   
                java.util.Map.Entry entry = (java.util.Map.Entry) i.next();   
                key = (String) entry.getKey();   
                if (key.indexOf(type) != -1) {   
                    a.add(key);   
                }   
            }   
        } catch (Exception ex) {
        	ex.printStackTrace();
        } 
        finally {   
            return a;   
        }   
    }   
  
}  