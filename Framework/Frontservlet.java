package etu001935.framework.servlet;

import etu001935.framework.*;
import etu001935.framework.annotation.*;
import etu001935.modelView.ModelView;
import jakarta.servlet.*;
import java.io.*;
import java.io.ObjectInputStream.GetField;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.util.*;
import java.util.Map.Entry;

import javax.management.modelmbean.ModelMBean;

import java.net.*;
import java.lang.reflect.*;

@MultipartConfig
public class Frontservlet extends HttpServlet {
    HashMap<String, Mapping> MappingUrls;
    HashMap<String, Object> singletons;

    public HashMap<String, Object> getSingletons() {
        return this.singletons;
    }

    public void init() throws ServletException {
        try {
            super.init();
            // String packages = String.valueOf(getInitParameter("packages"));
            String packages = "etu001935.model";
            this.MappingUrls = new HashMap<>();
            this.singletons = new HashMap<>();
            String path = packages.replaceAll("[.]", "\\\\");
            URL packageUrl = Thread.currentThread().getContextClassLoader().getResource(path);
            File packDir = new File(packageUrl.toURI());
            File[] inside = packDir.listFiles(file -> file.getName().endsWith(".class"));
            List<Class> lists = new ArrayList<>();
            for (File f : inside) {
                String c = packages + "." + f.getName().substring(0, f.getName().lastIndexOf("."));
                lists.add(Class.forName(c));
            }
            for (Class c : lists) {
                Method[] methods = c.getDeclaredMethods();
                for (Method m : methods) {
                    if (m.isAnnotationPresent(Annotation.class)) {
                        Annotation url = m.getAnnotation(Annotation.class);
                        if (!url.Url().isEmpty() && url.Url() != null) {
                            Mapping map = new Mapping(c.getName(), m.getName());
                            this.MappingUrls.put(url.Url(), map);
                        }
                    }
                }
                if (c.isAnnotationPresent(Scope.class)
                        && ((Scope) c.getAnnotation(Scope.class)).name().equalsIgnoreCase("singleton")) {
                    this.getSingletons().put(c.getName(), null);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        String url = request.getRequestURI();
        url = url.substring(request.getContextPath().length() + 1);
        for (Map.Entry<String, Mapping> sets : this.MappingUrls.entrySet()) {
            out.println("(url ==>'" + sets.getKey() + "')");
        }
        try {
            if (this.MappingUrls.containsKey(url)) {
                try {
                    Mapping map = this.MappingUrls.get(url);
                    Class c = Class.forName(map.getClassName());
                    Method m = null;
                    Object o = null;
                    if (singletons.containsKey(map.getClassName())) {
                        if (singletons.get(map.getClassName()) == null) {
                            singletons.put(map.getClassName(), c.getConstructor((Class[])null).newInstance());
                             
                        }
                        o = singletons.get(map.getClassName());
                        out.println("tay");
                    } else {
                        o = c.getConstructor((Class[])null).newInstance();
                        out.println("tsy tay");
                    }
                    Method[] methods = c.getDeclaredMethods();
                    for (Method method : methods) {
                        if (method.isAnnotationPresent(Annotation.class)) {
                            Annotation note = method.getAnnotation(Annotation.class);
                            if (!note.Url().isEmpty() && note.Url() != null) {
                                if (method.getName().equals(map.getMethod())) {
                                    m = method;
                                    out.println(m);
                                    break;
                                }
                            }
                        }
                    }
                    Enumeration<String> enu = request.getParameterNames();
                    List<String> list = Collections.list(enu);
                    Parameter[] params = m.getParameters();
                    Object[] objt = new Object[params.length];
                    for (int i = 0; i < params.length; i++) {
                        Parameter p = params[i];
                        Param value = p.getAnnotation(Param.class);
                        String n = value.nom();
                        for (String li : list) {
                            if (n.equals(li)) {

                                Object ob = (p.getType().isArray()) ? request.getParameterValues(n)
                                        : request.getParameter(n);
                                Object oj = null;
                                if (p.getType() == java.sql.Date.class) {
                                    oj = java.sql.Date.valueOf(String.valueOf(ob));
                                } else if (p.getType().isArray())
                                    oj = ob;
                                else
                                    oj = p.getType().getConstructor(String.class).newInstance(String.valueOf(ob));
                                objt[i] = oj;
                                out.println(oj);
                                break;
                            }
                        }
                    }
                    out.print("singleton"+this.getSingletons());
                    
                    Field[] fields = c.getDeclaredFields();
                    for (Field field : fields) {
                        String name = (field.getType().isArray()) ? field.getName() + "[]" : field.getName();
                        for (String li : list) {
                            if (name.equals(li)) {
                                // (condition) ? valeur1 : valeur2 -> condition terner
                                String z = field.getName();
                                String first = z.substring(0, 1).toUpperCase();
                                String last = z.substring(1);
                                Method mth = c.getMethod("set" + first + last, field.getType());
                                Object ob = (field.getType().isArray()) ? request.getParameterValues(name)
                                        : request.getParameter(name);
                                out.println(ob);
                                Object oj = null;
                                if (field.getType() == java.sql.Date.class) {
                                    oj = java.sql.Date.valueOf(String.valueOf(ob));
                                } else if (field.getType().isArray())
                                    oj = ob;
                                else
                                    oj = field.getType().getConstructor(String.class).newInstance(String.valueOf(ob));
                                if (c.isAnnotationPresent(Scope.class)
                                        && ((Scope) c.getAnnotation(Scope.class)).name()
                                                .equalsIgnoreCase("singleton")) {
                                        mth.invoke(o, (Object)null);
                                }
                                mth.invoke(o, oj);
                                break;
                            }
                        }
                    }
                    try {
                        for (Field field : fields) {
                            if (field.getType() == etu001935.framework.Upload.class) {
                                String z = field.getName();
                                String first = z.substring(0, 1).toUpperCase();
                                String last = z.substring(1);
                                Method mth = c.getDeclaredMethod("set" + first + last, field.getType());
                                Object objct = this.fileTraitement(request.getParts(), field);
                                if (c.isAnnotationPresent(Scope.class)
                                    && ((Scope) c.getAnnotation(Scope.class)).name()
                                            .equalsIgnoreCase("singleton")) {
                                    mth.invoke(o, (Object)null);
                                }
                                mth.invoke(o, objct);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace(out);
                        e.printStackTrace();
                        // TODO: handle exception
                    }

                    
                    Object obj = m.invoke(o, objt);

                    if (obj instanceof ModelView) {
                        ModelView mv = (ModelView) obj;
                        for (Map.Entry<String, Object> e : mv.getData().entrySet()) {
                            request.setAttribute(e.getKey(), e.getValue());
                        }
                        RequestDispatcher rd = request.getRequestDispatcher(mv.getUrl());
                        // rd.forward(request, response);
                    }
                } catch (Exception e) {
                    e.printStackTrace(out);
                    // TODO: handle exception
                }
            }
        } catch (Exception e) {
            e.printStackTrace(out);
        }

    }

    private String getFileName(jakarta.servlet.http.Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String[] parts = contentDisposition.split(";");
        for (String partStr : parts) {
            if (partStr.trim().startsWith("filename"))
                return partStr.substring(partStr.indexOf('=') + 1).trim().replace("\"", "");
        }
        return null;
    }

    private Upload fillFileUpload(Upload file, jakarta.servlet.http.Part filepart) {
        try (InputStream io = filepart.getInputStream()) {
            ByteArrayOutputStream buffers = new ByteArrayOutputStream();
            byte[] buffer = new byte[(int) filepart.getSize()];
            int read;
            while ((read = io.read(buffer, 0, buffer.length)) != -1) {
                buffers.write(buffer, 0, read);
            }
            file.setFilename(this.getFileName(filepart));
            file.setData(buffers.toByteArray());
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Upload fileTraitement(Collection<jakarta.servlet.http.Part> files, Field field) {
        Upload file = new Upload();
        String name = field.getName();
        boolean exists = false;
        String filename = null;
        jakarta.servlet.http.Part filepart = null;
        for (jakarta.servlet.http.Part part : files) {
            if (part.getName().equals(name)) {
                filepart = part;
                break;
            }
        }
        file = this.fillFileUpload(file, filepart);
        return file;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}