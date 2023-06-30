package etu001935.model;
import etu001935.framework.annotation.*;
import etu001935.modelView.ModelView;
import java.sql.Date;
import java.util.HashMap;

import etu001935.framework.Upload;
// @Scope(name="singleton")
public class Emp {
    Integer id;
    String nom;
    Date date;
    String[] jours;
    Upload fileUpload;
    HashMap<String,Object> sessions;


    @Annotation(Url="Getall")
    @Authantification(user="admin")
    public ModelView GetAll(){
       ModelView view = new ModelView("essaie.jsp");
        Emp[] emps ={
           this
        };  
        view.AddItem("all_emp",emps);
        return view;
    }
    @Annotation(Url="FindById")
    public ModelView findById(@Param(nom = "anarana") Integer id) {
        ModelView view = new ModelView("Sprint8.jsp");
        view.AddItem("Id",id);
        return view;
        
    }
    public HashMap<String, Object> getSessions() {
        return sessions;
    }
    public void setSessions(HashMap<String, Object> sessions) {
        this.sessions = sessions;
    }
    public String[] getJours(){
        return jours;
    }
    public void setJours(String[] jours){
        this.jours=jours;
    }
    public String getNom() {
        return nom;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public Integer  getId(){
        return id;
    }
    public void setId(Integer id){
        this.id=id;
    }
    public Emp(){}

    public void setNom(String newname) {
        this.nom = newname;
    }
    public Emp(String name) {
        setNom(name);
    }
    public  Upload getFileUpload() {
        return fileUpload;
    }
    public void setFileUpload(Upload upload) {
        this.fileUpload = upload;
    }

    @Annotation(Url = "login")
    @Session()
    public etu001935.modelView.ModelView login(){
        etu001935.modelView.ModelView mv=new etu001935.modelView.ModelView("Sprint8.jsp");
        System.out.println(this.getSessions().get("nom"));
        mv.addSession("connecte","eny");
        mv.addSession("user","admin");
        mv.addSession("nom","beton");
        return mv;
    }

    @Annotation(Url = "test_Session")
    @Session()
    public etu001935.modelView.ModelView getSession(){
        etu001935.modelView.ModelView mv=new etu001935.modelView.ModelView("Sprint8.jsp");
        mv.AddItem("nom",this.getSessions().get("nom"));
        System.out.println(this.getSessions().get("nom"));
        this.getSessions().put("nom","zah");
        return mv;
    }
}
