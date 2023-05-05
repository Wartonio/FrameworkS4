package etu001935.model;
import etu001935.framework.annotation.*;
import etu001935.modelView.ModelView;
public class Emp {
    String nom;
    
    @Annotation(Url="Getall")
    public ModelView GetAll(){
       return new ModelView("essaie.jsp");
    }
}
