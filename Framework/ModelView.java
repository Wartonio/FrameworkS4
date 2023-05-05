package etu001935.modelView;

public class ModelView {
    String url;

  
    public ModelView() {

    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String newurl) {
        this.url = newurl;
    }

    public ModelView(String url) {
        this.setUrl(url);
    }
}
