package visao;



import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;




public class aplicacao extends Application {
        private Parent parent;
        private FXMLLoader fxmlLoader;
        private static FXMLDocumentController controller;
    @Override
    public void start(Stage stage) throws Exception {
        Parent parent = null;
        FXMLLoader fxmlLoader = null;
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
            parent = (Parent) fxmlLoader.load();
        } catch (IOException ex) {
            System.out.println("Erro ao iniciar a tela de cadastro de Estado "+ex.getMessage());
        }
        controller = (FXMLDocumentController) fxmlLoader.getController();
        Scene scene = new Scene(parent);
        
        
        
        
        stage.setScene(scene);
        stage.setTitle("Lavoro");
        stage.setResizable(false);
        stage.show();
    }
    
    public static FXMLDocumentController getController(){
        return controller;
    }
    
    public static void main(String[] args) {
           
        launch(args);
        
    }
    
}
