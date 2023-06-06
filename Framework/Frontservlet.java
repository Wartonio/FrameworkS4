package etu001935.framework.servlet;
import etu001935.framework.*;
import etu001935.framework.annotation.*;
import etu001935.modelView.ModelView;
import jakarta.servlet.*;
import java.io.*;
import java.io.ObjectInputStream.GetField;

import jakarta.servlet.http.*;
import java.util.*;
import java.util.Map.Entry;

import javax.management.modelmbean.ModelMBean;

import java.net.*;
import java.lang.reflect.*;

public class Frontservlet extends HttpServlet{
    HashMap<String,Mapping> MappingUrls;

    public void init() throws ServletException{
        try {
            super.init();
            // String packages = String.valueOf(getInitParameter("packages"));
            String packages = "etu001935.model";
            this.MappingUrls=new HashMap<>();
            String path = packages.replaceAll("[.]","\\\\");
            URL packageUrl=Thread.currentThread().getContextClassLoader().getResource(path);
            File packDir =new File(packageUrl.toURI());
            File[] inside=packDir.listFiles(file->file.getName().endsWith(".class"));
            List<Class> lists = new ArrayList<>();
            for(File f : inside){
                    String c =packages+"."+f.getName().substring(0,f.getName().lastIndexOf("."));
                    lists.add(Class.forName(c));
            }
            for(Class c:lists){
                Method[] methods = c.getDeclaredMethods();
                for(Method m : methods){
                    if(m.isAnnotationPresent(Annotation.class)){
                        Annotation url = m.getAnnotation(Annotation.class);
                        if(!url.Url().isEmpty() && url.Url() !=null){
                            Mapping map = new Mapping(c.getName() , m.getName());
                            this.MappingUrls.put(url.Url(),map);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void processRequest(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
        response.setContentType("text/plain");
        PrintWriter out=response.getWriter();
        String url = request.getRequestURI();
        url  = url.substring(request.getContextPath().length()+1);
        for(Map.Entry<String,Mapping> sets : this.MappingUrls.entrySet()) {
            out.println("(url ==>'"+ sets.getKey() +"')");
        }
        try{
            if(this.MappingUrls.containsKey(url)){
                try {
                    Mapping map = this.MappingUrls.get(url);
                Class c = Class.forName(map.getClassName());
                Method m = null;
                Object o = c.getConstructor().newInstance();
                Method[] methods = c.getDeclaredMethods();
                for(Method method : methods){
                    if(method.isAnnotationPresent(Annotation.class)){
                        Annotation note = method.getAnnotation(Annotation.class);
                        if(!note.Url().isEmpty() && note.Url()!=null){
                            if(method.getName().equals(map.getMethod())){
                                m = method;
                                out.println(m);
                                break;
                            }
                        }
                    }
                }
                Enumeration<String> enu=request.getParameterNames();
                List<String> list= Collections.list(enu);
                Field[] fields=c.getDeclaredFields(); 
                for (Field field : fields){
                    String name=(field.getType().isArray()) ? field.getName()+"[]" : field.getName();
                    for(String li:list){
                        if(name.equals(li)){
                            // (condition) ? valeur1 : valeur2 -> condition terner
                            String z=field.getName();
                            String first=z.substring(0,1).toUpperCase();
                            String last=z.substring(1);
                            Method mth=c.getMethod("set"+ first +last,field.getType());
                            Object ob =(field.getType().isArray())? request.getParameterValues(name) : request.getParameter(name);
                            out.println(ob);
                            Object oj = null;
                            if(field.getType()==java.sql.Date.class){
                                oj=java.sql.Date.valueOf(String.valueOf(ob));
                            }else if(field.getType().isArray())
                                oj=ob;
                            else
                                oj=field.getType().getConstructor(String.class).newInstance(String.valueOf(ob));
                            mth.invoke( o , oj );
                            break;
                        }
                    }
                }
                Object obj = m.invoke( o , (Object[])null);
                if(obj instanceof ModelView){
                    ModelView mv = (ModelView)obj;
                    for(Map.Entry<String,Object> e : mv.getData().entrySet()){
                        request.setAttribute(e.getKey(), e.getValue());
                    }
                    RequestDispatcher rd = request.getRequestDispatcher(mv.getUrl());
                    rd.forward(request,response);
                }
                } catch (Exception e) {
                    e.printStackTrace(out);
                    // TODO: handle exception
                }
            }
        }catch(Exception e){
            e.printStackTrace(out);
        }
        
    }

    public void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
        processRequest(request,response);
    }

    public void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
        processRequest(request,response);
    }
}