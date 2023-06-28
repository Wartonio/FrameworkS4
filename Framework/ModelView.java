package etu001935.modelView;

import java.util.HashMap;

public class ModelView {
    String url;
    HashMap<String,Object> data = new HashMap<String,Object>(); 
    HashMap<String,Object> session = new HashMap<String,Object>();
  
    public ModelView() {

    }
    public void AddItem(String name, Object value){
        this.getData().put(name, value);
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String newurl) {
        this.url = newurl;
    }
    public HashMap<String,Object> getData() {
        return data;
    }

    public void setData(HashMap<String,Object> newdata) {
        this.data = newdata;
    }
    public HashMap<String, Object> getSession() {
        return session;
    }
    public void setSession(HashMap<String, Object> session) {
        this.session = session;
    }
    public ModelView(String url) {
        this.setUrl(url);
    }
    public void addSession(String session,Object object){
        this.getSession().put( session , object );
    }
}
