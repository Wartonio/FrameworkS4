package etu001935.model;
import etu001935.framework.annotation.*;
import etu001935.modelView.ModelView;
import java.sql.Date;
public class Emp {
    Integer id;
    String nom;
    Date date;
    String[] jours;

    @Annotation(Url="Getall")
    public ModelView GetAll(){
       ModelView view = new ModelView("essaie.jsp");
        Emp[] emps ={
            new Emp("Rakoto"),new Emp("Rabe"),new Emp("Rasoa"),this
        };  
        view.AddItem("all_emp",emps);
        return view;
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
}
