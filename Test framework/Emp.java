package etu001935.model;
import etu001935.framework.annotation.*;
import etu001935.modelView.ModelView;
public class Emp {
    String nom;
    
    @Annotation(Url="Getall")
    public ModelView GetAll(){
       ModelView view = new ModelView("essaie.jsp");
        Emp[] emps ={
            new Emp("Rakoto"),new Emp("Rabe"),new Emp("Rasoa")
        };  
        view.AddItem("all_emp",emps);
        return view;
    }
    public String getNom() {
        return nom;
    }

    public Emp(){}

    public void setNom(String newname) {
        this.nom = newname;
    }
    public Emp(String name) {
        setNom(name);



    }
}
